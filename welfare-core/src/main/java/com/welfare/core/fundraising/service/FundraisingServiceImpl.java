package com.welfare.core.fundraising.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cheuks.bin.original.common.dbmanager.dao.BaseDao;
import com.cheuks.bin.original.common.dbmanager.service.AbstractService;
import com.welfare.core.fundraising.dao.FundraisingDao;
import com.welfare.core.fundraising.entity.Fundraising;

@Component
public class FundraisingServiceImpl extends AbstractService<Fundraising, Integer> implements FundraisingService {

	@Autowired
	private FundraisingDao fundraisingDao;

	@Override
	public BaseDao<Fundraising, Integer> getService() {
		return fundraisingDao;
	}

	public Fundraising getActivFundraisingByPk(int id) throws Throwable {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("fromDate", new Date());
		params.put("thruDate", new Date());
		return (Fundraising) fundraisingDao.uniqueResult("activFundraisingByPk", true, params);
	}

}
