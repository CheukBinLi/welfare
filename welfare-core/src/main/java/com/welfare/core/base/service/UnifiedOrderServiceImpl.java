package com.welfare.core.base.service;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cheuks.bin.original.common.dbmanager.dao.BaseDao;
import com.cheuks.bin.original.common.dbmanager.service.AbstractService;
import com.welfare.common.api.weixin.OrderPayByJs;
import com.welfare.core.base.dao.UnifiedOrderDao;
import com.welfare.core.base.entity.UnifiedOrder;
import com.welfare.core.contant.StatusContant;
import com.welfare.core.fundraising.dao.FundraisingLogDao;
import com.welfare.core.fundraising.entity.FundraisingLog;
import com.welfare.core.fundraising.service.FundraisingLogService;
import com.welfare.core.util.GeneratedIDService;

@Component
public class UnifiedOrderServiceImpl extends AbstractService<UnifiedOrder, String> implements UnifiedOrderService {

	@Autowired
	private UnifiedOrderDao unifiedOrderDao;

	@Autowired
	private FundraisingLogDao fundraisingLogDao;

	@Autowired
	private FundraisingLogService fundraisingLogService;

	@Override
	public BaseDao<UnifiedOrder, String> getService() {
		return unifiedOrderDao;
	}

	/***
	 * 筹款订单
	 */
	public Map<String, Object> createFundraisingOrder(UnifiedOrder unifiedOrder, FundraisingLog fundraisingLog, String appId, String mchId, String apiKey, String body, String attach, double amount, String spBillCreateIP, String timeStart, String timeExpire, String notifyUrl, String limitPay, String weixinOpenId) throws Throwable {
		unifiedOrder.setId(GeneratedIDService.newInstance().nextID().toString()).setStatus(StatusContant.STATUS_INIT);
		unifiedOrder = unifiedOrderDao.save(unifiedOrder);
		fundraisingLog.setStatus(StatusContant.STATUS_INIT).setCompleteDate(new Date());
		fundraisingLog.setUnifiedOrderId(unifiedOrder.getId());
		fundraisingLogDao.save(fundraisingLog);
		OrderPayByJs order = new OrderPayByJs();
		// Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> result = order.getOrderPaySign(appId, mchId, apiKey, body, attach, unifiedOrder.getId(), amount, spBillCreateIP, timeStart, timeExpire, notifyUrl, "no_credit", weixinOpenId);
		result.put("fundraisingLogId", fundraisingLog.getId());
		result.put("unifiedOrderId", fundraisingLog.getUnifiedOrderId());
		return result;
	}

	public FundraisingLog executeWeixinCallByFundraisingOrder(String unifiedOrderId, Object additional) throws Throwable {
		UnifiedOrder unifiedOrder = getByPk(unifiedOrderId);
		if (StatusContant.STATUS_COMPLETE == unifiedOrder.getStatus())
			return null;
		unifiedOrder.setStatus(StatusContant.STATUS_COMPLETE);
		update(unifiedOrder);
		FundraisingLog fundraisingLog = fundraisingLogService.updateFundraisingLogStatusByUnifiedOrderId(unifiedOrderId, StatusContant.STATUS_COMPLETE);
		return fundraisingLog;
	}

}
