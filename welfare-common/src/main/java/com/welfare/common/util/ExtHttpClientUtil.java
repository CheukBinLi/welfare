package com.welfare.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtHttpClientUtil {
    private final static Logger logger = LoggerFactory.getLogger(ExtHttpClientUtil.class);

    /**
     * HTTP post请求
     */
    public static void post(String url, Map<String, String> parameters) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String responseContentString = "";
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        if (parameters != null) {
            for (String k : parameters.keySet()) {
                formParams.add(new BasicNameValuePair(k, parameters.get(k)));
            }
        }
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
            httpPost.setEntity(uefEntity);
            logger.info("Http Post请求地址:" + httpPost.getURI());
            CloseableHttpResponse response = httpClient.execute(httpPost);
            logger.info("请求状态:" + response.getStatusLine());
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    responseContentString = EntityUtils.toString(entity, Consts.UTF_8);
                    logger.info("请求返回的内容:" + responseContentString);
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * HTTP get请求
     */
    public static String get(String url, Map<String, String> urlParameters) {
        String requestUrl = url;
        String responseContentString = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();

        if (UtilValidate.isNotEmpty(urlParameters)) {
            StringBuffer urlParametersStr = new StringBuffer(url);
            urlParametersStr.append("?");

            if (UtilValidate.isNotEmpty(urlParameters)) {
                for (String key : urlParameters.keySet()) {
                    urlParametersStr.append("&" + key);
                    urlParametersStr.append("=" + urlParameters.get(key));
                }
            }
            requestUrl = urlParametersStr.toString();
        }

        try {
            HttpGet httpGet = new HttpGet(requestUrl);
            logger.info("Http Get请求地址:" + httpGet.getURI());
            CloseableHttpResponse response = httpClient.execute(httpGet);
            logger.info("请求状态:" + response.getStatusLine());
            try {
                HttpEntity entity = response.getEntity();
                if (UtilValidate.isNotEmpty(entity)) {
                    responseContentString = EntityUtils.toString(entity, Consts.UTF_8);
                    logger.info("请求返回的内容:" + responseContentString);
                } else {
                    logger.warn("HttpEntity为空!");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            logger.error(e.getMessage());
        } catch (ParseException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        return responseContentString;
    }

    public static String get(String url) {
        return get(url, null);
    }
}