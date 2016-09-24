package com.welfare.core.fundraising.dto;

import java.io.Serializable;
import java.util.Date;

public class FundraisingLogWeixinDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nickName;
	private String photoUrl;
	private double payAmount;
	private String blessingMessage;
	private Date completeDate;

	public String getNickName() {
		return nickName;
	}

	public FundraisingLogWeixinDto setNickName(String nickName) {
		this.nickName = nickName;
		return this;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public FundraisingLogWeixinDto setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
		return this;
	}

	public double getPayAmount() {
		return payAmount;
	}

	public FundraisingLogWeixinDto setPayAmount(double payAmount) {
		this.payAmount = payAmount;
		return this;
	}

	public String getBlessingMessage() {
		return blessingMessage;
	}

	public FundraisingLogWeixinDto setBlessingMessage(String blessingMessage) {
		this.blessingMessage = blessingMessage;
		return this;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public FundraisingLogWeixinDto setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
		return this;
	}

	public FundraisingLogWeixinDto(String nickName, String photoUrl, double payAmount, String blessingMessage, Date completeDate) {
		super();
		this.nickName = nickName;
		this.photoUrl = photoUrl;
		this.payAmount = payAmount;
		this.blessingMessage = blessingMessage;
		this.completeDate = completeDate;
	}

}
