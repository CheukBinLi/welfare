package com.welfare.core.fundraising.service;

import java.util.List;

import com.cheuks.bin.original.common.dbmanager.service.BaseService;
import com.welfare.core.fundraising.dto.FundraisingLogStatisticalDto;
import com.welfare.core.fundraising.dto.FundraisingLogWeixinDto;
import com.welfare.core.fundraising.entity.FundraisingLog;

public interface FundraisingLogService extends BaseService<FundraisingLog, Integer> {

	// public List<FundraisingLog> getCompleteList(int fundraisingId, int page, int size) throws Throwable;

	public FundraisingLogStatisticalDto getFundraisingLogStatisticalDto(int fundraisingId) throws Throwable;

	public List<FundraisingLogWeixinDto> getFundraisingLogWeixinDtoList(int fundraisingId, int page, int size) throws Throwable;

	public FundraisingLogWeixinDto getFundraisingLogWeixinDto(int fundraisingLogId) throws Throwable;

	public FundraisingLog getFundraisingLogByUnifiedOrderId(String unifiedOrderId) throws Throwable;

	public FundraisingLog updateFundraisingLogStatusByUnifiedOrderId(String unifiedOrderId, int status) throws Throwable;

}
