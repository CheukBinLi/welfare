package com.welfare.core.base.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "WEI_XIN_INFO")
public class WeiXinInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	private String openId;
	private String nickName;
	private String photoUrl;
	// private String accessToken;
	// private transient String refreshToken;
	// private transient String ticket;
	private String parent;// 上家
	private Date updateDate;

	public String getOpenId() {
		return openId;
	}

	public WeiXinInfo setOpenId(String openId) {
		this.openId = openId;
		return this;
	}

	public String getNickName() {
		return nickName;
	}

	public WeiXinInfo setNickName(String nickName) {
		this.nickName = nickName;
		return this;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public WeiXinInfo setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
		return this;
	}

	// public String getAccessToken() {
	// return accessToken;
	// }
	//
	// public WeiXinInfo setAccessToken(String accessToken) {
	// this.accessToken = accessToken;
	// return this;
	// }

	public String getParent() {
		return parent;
	}

	public WeiXinInfo setParent(String parent) {
		this.parent = parent;
		return this;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public WeiXinInfo setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
		return this;
	}

	// public String getRefreshToken() {
	// return refreshToken;
	// }
	//
	// public WeiXinInfo setRefreshToken(String refreshToken) {
	// this.refreshToken = refreshToken;
	// return this;
	// }
	//
	// public String getTicket() {
	// return ticket;
	// }
	//
	// public WeiXinInfo setTicket(String ticket) {
	// this.ticket = ticket;
	// return this;
	// }

	// public WeiXinInfo(String openId, String nickName, String photoUrl, String accessToken, String parent, Date updateDate) {
	// super();
	// this.openId = openId;
	// this.nickName = nickName;
	// this.photoUrl = photoUrl;
	// this.accessToken = accessToken;
	// this.parent = parent;
	// this.updateDate = updateDate;
	// }

	public WeiXinInfo(String openId) {
		super();
		this.openId = openId;
	}

	public WeiXinInfo(String openId, String nickName, String photoUrl, String parent, Date updateDate) {
		super();
		this.openId = openId;
		this.nickName = nickName;
		this.photoUrl = photoUrl;
		this.parent = parent;
		this.updateDate = updateDate;
	}

	public WeiXinInfo() {
		super();
	}

}
