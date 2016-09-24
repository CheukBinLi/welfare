package com.welfare.core.base.service;

import java.util.Map;

import com.cheuks.bin.original.common.dbmanager.service.BaseService;
import com.welfare.core.base.entity.UnifiedOrder;
import com.welfare.core.fundraising.entity.FundraisingLog;

public interface UnifiedOrderService extends BaseService<UnifiedOrder, String> {
	public Map<String, Object> createFundraisingOrder(UnifiedOrder unifiedOrder, FundraisingLog fundraisingLog, String appId, String mchId, String apiKey, String body, String attach, double amount, String spBillCreateIP, String timeStart, String timeExpire, String notifyUrl, String limitPay, String weixinOpenId) throws Throwable;

	public FundraisingLog executeWeixinCallByFundraisingOrder(String unifiedOrderId, Object additional) throws Throwable;

}
