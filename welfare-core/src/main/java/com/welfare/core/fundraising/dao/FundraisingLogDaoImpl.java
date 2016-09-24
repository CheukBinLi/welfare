package com.welfare.core.fundraising.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cheuks.bin.original.common.dbmanager.DBAdapter;
import com.cheuks.bin.original.common.dbmanager.dao.AbstractDao;
import com.welfare.core.fundraising.entity.FundraisingLog;

@Component
public class FundraisingLogDaoImpl extends AbstractDao<FundraisingLog, Integer> implements FundraisingLogDao {

	@Autowired
	private DBAdapter dBAdapter;

	@Override
	public Class<FundraisingLog> getEntityClass() {
		return FundraisingLog.class;
	}

	@Override
	public DBAdapter getDBAdapter() {
		return dBAdapter;
	}

}
