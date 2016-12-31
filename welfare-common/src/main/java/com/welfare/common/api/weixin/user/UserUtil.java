package com.welfare.common.api.weixin.user;

import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.welfare.common.util.ExtHttpClientUtil;

public class UserUtil {
	/**
	 * 获取微信用户基本信息
	 */
	public static HashMap getUserInfo(String accessToken, String openId) {
		String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN";
		String responseContentString = ExtHttpClientUtil.get(requestUrl);
		JSONObject jsonObject = JSON.parseObject(responseContentString);

		String subscribe = jsonObject.getString("subscribe");
		String openid = jsonObject.getString("openid");
		String nickname = jsonObject.getString("nickname");
		String sex = jsonObject.getString("sex");
		String language = jsonObject.getString("language");
		String city = jsonObject.getString("city");
		String province = jsonObject.getString("province");
		String country = jsonObject.getString("country");
		String headImgUrl = jsonObject.getString("headimgurl");
		String subscribeTime = jsonObject.getString("subscribe_time");
		String unionId = jsonObject.getString("unionid");
		String remark = jsonObject.getString("remark");
		String groupId = jsonObject.getString("groupid");
		String tagIdList = jsonObject.getString("tagid_list");

		HashMap hashMap = new HashMap();
		hashMap.put("subscribe", subscribe);
		hashMap.put("openId", openid);
		hashMap.put("sex", sex);
		hashMap.put("nickname", nickname);
		hashMap.put("language", language);
		hashMap.put("city", city);
		hashMap.put("province", province);
		hashMap.put("country", country);
		hashMap.put("headImgUrl", headImgUrl);
		hashMap.put("subscribeTime", subscribeTime);
		hashMap.put("unionId", unionId);
		hashMap.put("remark", remark);
		hashMap.put("groupid", "groupid");
		hashMap.put("groupId", groupId);
		hashMap.put("tagIdList", tagIdList);

		return hashMap;
	}
}
