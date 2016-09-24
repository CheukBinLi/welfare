package com.welfare.core.fundraising.dto;

import java.io.Serializable;

public class FundraisingLogStatisticalDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private long counts;
	private double totalAmount;

	public long getCounts() {
		return counts;
	}

	public FundraisingLogStatisticalDto setCounts(long counts) {
		this.counts = counts;
		return this;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public FundraisingLogStatisticalDto setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
		return this;
	}

	public FundraisingLogStatisticalDto(long counts, double totalAmount) {
		super();
		this.counts = counts;
		this.totalAmount = totalAmount;
	}

	public FundraisingLogStatisticalDto() {
		super();
	}

}
