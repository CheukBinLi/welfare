package com.welfare.common.api.weixin.js;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.welfare.common.util.ExtHttpClientUtil;

/**
 * 微信Js-SDK票据
 */
public class JsSdkTicket {
    /**
     * 根据accessToken获取JS sdk票据，有效期2小时
     */
    public static String getJsSdkTicket(String accessToken) {
        String ticket;

        String requestUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi";

        String responseContentString = ExtHttpClientUtil.get(requestUrl);
        JSONObject jsonObject = JSON.parseObject(responseContentString);

        ticket = jsonObject.getString("ticket");
        
        return ticket;
    }
}