package com.welfare.core.fundraising.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cheuks.bin.original.common.dbmanager.dao.BaseDao;
import com.cheuks.bin.original.common.dbmanager.service.AbstractService;
import com.cheuks.bin.original.common.util.CollectionUtil;
import com.welfare.core.contant.StatusContant;
import com.welfare.core.fundraising.dao.FundraisingLogDao;
import com.welfare.core.fundraising.dto.FundraisingLogStatisticalDto;
import com.welfare.core.fundraising.dto.FundraisingLogWeixinDto;
import com.welfare.core.fundraising.entity.FundraisingLog;

@Component
public class FundraisingLogServiceImpl extends AbstractService<FundraisingLog, Integer> implements FundraisingLogService {

	@Autowired
	private FundraisingLogDao fundraisingLogDao;

	@Override
	public BaseDao<FundraisingLog, Integer> getService() {
		return fundraisingLogDao;
	}

	// public List<FundraisingLog> getCompleteList(int fundraisingId, int page, int size) throws Throwable {
	// Map<String, Object> params = CollectionUtil.newInstance().toMap("fundraisingId", fundraisingId, "status", StatusContant.STATUS_COMPLETE, "orderby", "completeDate", "sort", "DESC");
	// List<FundraisingLog> fundraisingLogs = getList(params, false, page, size);
	// return fundraisingLogs;
	// }

	public List<FundraisingLogWeixinDto> getFundraisingLogWeixinDtoList(int fundraisingId, int page, int size) throws Throwable {
		Map<String, Object> params = CollectionUtil.newInstance().toMap("fundraisingId", fundraisingId, "status", StatusContant.STATUS_COMPLETE, "orderby", "completeDate", "sort", "DESC");
		List<FundraisingLogWeixinDto> fundraisingLogWeixinDtos = fundraisingLogDao.getList("fundraisingLogWeixinDtoList", params, true, page, size);
		return fundraisingLogWeixinDtos;
	}

	public FundraisingLogWeixinDto getFundraisingLogWeixinDto(int fundraisingLogId) throws Throwable {
		Map<String, Object> params = CollectionUtil.newInstance().toMap("id", fundraisingLogId);
		List<FundraisingLogWeixinDto> fundraisingLogWeixinDtos = fundraisingLogDao.getList("fundraisingLogWeixinDtoList", params, true, 0, 1);
		return fundraisingLogWeixinDtos.get(0);
	}

	public FundraisingLogStatisticalDto getFundraisingLogStatisticalDto(int fundraisingId) throws Throwable {
		FundraisingLogStatisticalDto fundraisingLogs = null;
		try {
			fundraisingLogs = (FundraisingLogStatisticalDto) fundraisingLogDao.uniqueResult("statistical", true, CollectionUtil.newInstance().toMap("fundraisingId", fundraisingId, "status", StatusContant.STATUS_COMPLETE));
		} catch (Exception e) {
			e.printStackTrace();
			fundraisingLogs = new FundraisingLogStatisticalDto();
		}
		return fundraisingLogs;
	}

	public FundraisingLog getFundraisingLogByUnifiedOrderId(String unifiedOrderId) throws Throwable {
		Map<String, Object> params = CollectionUtil.newInstance().toMap("unifiedOrderId", unifiedOrderId);
		FundraisingLog fundraisingLog = getList(params, false, 0, 1).get(0);
		return fundraisingLog;
	}

	public FundraisingLog updateFundraisingLogStatusByUnifiedOrderId(String unifiedOrderId, int status) throws Throwable {
		Map<String, Object> params = CollectionUtil.newInstance().toMap("unifiedOrderId", unifiedOrderId);
		FundraisingLog fundraisingLog = getList(params, false, 0, 1).get(0);
		fundraisingLog.setStatus(status).setCompleteDate(new Date());
		update(fundraisingLog);
		return fundraisingLog;
	}

	@Override
	public void update(Integer id, Map<String, Object> params) throws Throwable {
		FundraisingLog e = getService().get(id);
		String weiXingId = e.getWeiXinInfoId();
		e = fillObject(e, params);
		if (e.getWeiXinInfoId().equals(weiXingId))
			getService().update(e);
	}

}
