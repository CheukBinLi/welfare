package com.welfare.core.fundraising.service;

import com.cheuks.bin.original.common.dbmanager.service.BaseService;
import com.welfare.core.fundraising.entity.Fundraising;

public interface FundraisingService extends BaseService<Fundraising, Integer> {

	Fundraising getActivFundraisingByPk(int id) throws Throwable;

}
