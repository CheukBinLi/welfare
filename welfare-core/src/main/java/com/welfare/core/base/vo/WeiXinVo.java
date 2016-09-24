package com.welfare.core.base.vo;

import java.util.HashMap;
import java.util.Map;

public class WeiXinVo {

	private Map<String, String> values;
	// private String openId;
	// private String accessToken;
	// private String ticket;

	public WeiXinVo() {
		super();
	}

	public WeiXinVo(Map<String, String> values) {
		super();
		this.values = values;
	}

	public WeiXinVo(String openId, String accessToken, String ticket) {
		super();
		values = new HashMap<String, String>();
		values.put("openId", openId);
		values.put("accessToken", accessToken);
		values.put("ticket", ticket);
	}

	public String getOpenId() {
		return values.get("openId");
	}

	public WeiXinVo setOpenId(String openId) {
		values.put("openId", openId);
		return this;
	}

	public String getAccessToken() {
		return values.get("accessToken");
	}

	public WeiXinVo setAccessToken(String accessToken) {
		values.put("accessToken", accessToken);
		return this;
	}

	public String getTicket() {
		return values.get("ticket");
	}

	public WeiXinVo setTicket(String ticket) {
		values.put("ticket", ticket);
		return this;
	}

	public Map<String, String> getValues() {
		return values;
	}

}
