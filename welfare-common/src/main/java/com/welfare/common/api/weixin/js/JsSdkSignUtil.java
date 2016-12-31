package com.welfare.common.api.weixin.js;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 微信JS-JDK签名工具
 */
public class JsSdkSignUtil {
	public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> hashMap = new HashMap();
        StringBuffer signStringBuf = new StringBuffer();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String signString;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        signStringBuf.append("jsapi_ticket=" + jsapi_ticket);
        signStringBuf.append("&noncestr=" + nonce_str);
        signStringBuf.append("&timestamp=" + timestamp);
        signStringBuf.append("&url=" + url);

        signString = signStringBuf.toString();
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(signString.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        hashMap.put("url", url);
		hashMap.put("jsapi_ticket", jsapi_ticket);
        hashMap.put("nonceStr", nonce_str);
        hashMap.put("timestamp", timestamp);
        hashMap.put("signature", signature);

        return hashMap;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
