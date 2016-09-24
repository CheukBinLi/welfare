package com.welfare.core.base.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "UNIFIED_ORDER")
public class UnifiedOrder extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String weiXinInfoId;
	private String productDescription;
	private double payAmount;
	private int status;
	private Date createDateTime;

	public UnifiedOrder(String id, String weiXinInfoId, String productDescription, double payAmount, int status, Date createDateTime) {
		super();
		this.id = id;
		this.weiXinInfoId = weiXinInfoId;
		this.productDescription = productDescription;
		this.payAmount = payAmount;
		this.status = status;
		this.createDateTime = createDateTime;
	}

	public UnifiedOrder(String id) {
		super();
		this.id = id;
	}

	public UnifiedOrder() {
		super();
	}

	public String getId() {
		return id;
	}

	public UnifiedOrder setId(String id) {
		this.id = id;
		return this;
	}

	public String getWeiXinInfoId() {
		return weiXinInfoId;
	}

	public UnifiedOrder setWeiXinInfoId(String weiXinInfoId) {
		this.weiXinInfoId = weiXinInfoId;
		return this;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public UnifiedOrder setProductDescription(String productDescription) {
		this.productDescription = productDescription;
		return this;
	}

	public double getPayAmount() {
		return payAmount;
	}

	public UnifiedOrder setPayAmount(double payAmount) {
		this.payAmount = payAmount;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public UnifiedOrder setStatus(int status) {
		this.status = status;
		return this;
	}

	public Date getCreateDateTime() {
		return createDateTime;
	}

	public UnifiedOrder setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
		return this;
	}

}
