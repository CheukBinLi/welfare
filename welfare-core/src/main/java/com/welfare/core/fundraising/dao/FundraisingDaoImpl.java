package com.welfare.core.fundraising.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cheuks.bin.original.common.dbmanager.DBAdapter;
import com.cheuks.bin.original.common.dbmanager.dao.AbstractDao;
import com.welfare.core.fundraising.entity.Fundraising;

@Component
public class FundraisingDaoImpl extends AbstractDao<Fundraising, Integer> implements FundraisingDao {

	@Autowired
	private DBAdapter dBAdapter;

	@Override
	public Class<Fundraising> getEntityClass() {
		return Fundraising.class;
	}

	@Override
	public DBAdapter getDBAdapter() {
		return dBAdapter;
	}

}
