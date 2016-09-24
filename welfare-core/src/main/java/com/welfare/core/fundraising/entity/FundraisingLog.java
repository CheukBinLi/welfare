package com.welfare.core.fundraising.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.welfare.core.base.entity.BaseEntity;

@Entity(name = "BIZ_FUNDRAISING_LOG")
public class FundraisingLog extends BaseEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int fundraisingId;
	@Column(updatable = false)
	private String weiXinInfoId;
	private String unifiedOrderId;
	private double payAmount;
	private int status;
	private String blessingMessage;
	private Date completeDate;

	public int getId() {
		return id;
	}

	public FundraisingLog setId(int id) {
		this.id = id;
		return this;
	}

	public int getFundraisingId() {
		return fundraisingId;
	}

	public FundraisingLog setFundraisingId(int fundraisingId) {
		this.fundraisingId = fundraisingId;
		return this;
	}

	public String getWeiXinInfoId() {
		return weiXinInfoId;
	}

	public FundraisingLog setWeiXinInfoId(String weiXinInfoId) {
		this.weiXinInfoId = weiXinInfoId;
		return this;
	}

	public double getPayAmount() {
		return payAmount;
	}

	public FundraisingLog setPayAmount(double payAmount) {
		this.payAmount = payAmount;
		return this;
	}

	public String getUnifiedOrderId() {
		return unifiedOrderId;
	}

	public FundraisingLog setUnifiedOrderId(String unifiedOrderId) {
		this.unifiedOrderId = unifiedOrderId;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public FundraisingLog setStatus(int status) {
		this.status = status;
		return this;
	}

	public String getBlessingMessage() {
		return blessingMessage;
	}

	public FundraisingLog setBlessingMessage(String blessingMessage) {
		this.blessingMessage = blessingMessage;
		return this;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public FundraisingLog setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
		return this;
	}

	public FundraisingLog(int id, int fundraisingId, String weiXinInfoId, double payAmount, int status, String blessingMessage, Date completeDate) {
		super();
		this.id = id;
		this.fundraisingId = fundraisingId;
		this.weiXinInfoId = weiXinInfoId;
		this.payAmount = payAmount;
		this.status = status;
		this.blessingMessage = blessingMessage;
		this.completeDate = completeDate;
	}

	public FundraisingLog(int id) {
		super();
		this.id = id;
	}

	public FundraisingLog() {
		super();
	}
}
