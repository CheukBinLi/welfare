package com.welfare.common.api.weixin.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.welfare.common.api.weixin.common.WeixinErrorCode;
import com.welfare.common.util.ExtHttpClientUtil;
import com.welfare.common.util.UtilValidate;

public class AccessTokenUtil {

    private final static Logger logger = LoggerFactory.getLogger(AccessTokenUtil.class);

    public static String getAccessToken(String appId, String appSecret) {
        String accessToken = "";
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
        String responseContentString = ExtHttpClientUtil.get(requestUrl);

        JSONObject jsonObject = JSON.parseObject(responseContentString);
        accessToken = jsonObject.getString("access_token");
        String errcode = jsonObject.getString("errcode");

        if (UtilValidate.isWhitespace(accessToken)) {
            logger.warn("access_token为空!");
            logger.warn("errorCode:" + errcode);
            logger.warn("errorMsg:" + WeixinErrorCode.ERROR_MAP.get(errcode));
        }

        return accessToken;
    }
}