package com.welfare.common.api.weixin.auth;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.welfare.common.util.ExtHttpClientUtil;

public class AuthUtil {
	private final static Logger logger = LoggerFactory.getLogger(AuthUtil.class);

	/**
	 * 根据微信网页登陆授权的Code,获取AccessToken,这里的AccessToken是特殊的,和API 的AccessToken不同 微信网页授权是通过OAuth2.0机制实现的，在用户授权给公众号后，公众号可以获取到一个网页授权特有的接口调用凭证（网页授权access_token），通过网页授权access_token可以进行授权后接口调用，如获取用户基本信息 其他微信接口，需要通过基础支持中的“获取access_token”接口来获取到的普通access_token调用
	 * <p>
	 * 首先请注意，这里通过code换取的是一个特殊的网页授权access_token,与基础支持中的access_token（该access_token用于调用其他接口）不同。公众号可通过下述接口来获取网页授权access_token。如果网页授权的作用域为snsapi_base，则本步骤中获取到网页授权access_token的同时，也获取到了openid，snsapi_base式的网页授权流程即到此为止
	 */
	public static HashMap getWebLoginAuth(String appId, String appSecret, String code) {
		String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appSecret + "&code=" + code + "&grant_type=authorization_code";

		String responseContentString = ExtHttpClientUtil.get(requestUrl);
		JSONObject jsonObject = JSON.parseObject(responseContentString);

		String accessToken = jsonObject.getString("access_token");
		String refreshToken = jsonObject.getString("refresh_token");
		String openId = jsonObject.getString("openid");
		String scope = jsonObject.getString("scope");
		String unionId = jsonObject.getString("unionid");

		HashMap hashMap = new HashMap();
		hashMap.put("accessToken", accessToken);
		hashMap.put("refreshToken", refreshToken);
		hashMap.put("openId", openId);
		hashMap.put("scope", scope);
		hashMap.put("unionId", unionId);

		logger.info("网页登陆授权数据:" + JSON.toJSONString(hashMap));

		return hashMap;
	}

	/**
	 * 由于网页授权的access_token拥有较短的有效期，当access_token超时后，可以使用refresh_token进行刷新，refresh_token有效期为30天，当refresh_token失效之后，需要用户重新授权。
	 */
	public static HashMap refreshWebAuthAccessToken(String appId, String refreshToken) {
		String requestUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + appId + "&grant_type=refresh_token&refresh_token=" + refreshToken;

		String responseContentString = ExtHttpClientUtil.get(requestUrl);
		JSONObject jsonObject = JSON.parseObject(responseContentString);

		String accessToken = jsonObject.getString("access_token");
		String newRefreshToken = jsonObject.getString("refresh_token");
		String openId = jsonObject.getString("openid");
		String scope = jsonObject.getString("scope");

		HashMap hashMap = new HashMap();
		hashMap.put("accessToken", accessToken);
		hashMap.put("refreshToken", newRefreshToken);
		hashMap.put("openId", openId);
		hashMap.put("scope", scope);

		logger.info("刷新网页授权AccessToken数据:" + JSON.toJSONString(hashMap));

		return hashMap;
	}
}
