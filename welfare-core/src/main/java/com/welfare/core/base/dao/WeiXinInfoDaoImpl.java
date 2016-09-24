package com.welfare.core.base.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cheuks.bin.original.common.dbmanager.DBAdapter;
import com.cheuks.bin.original.common.dbmanager.dao.AbstractDao;
import com.welfare.core.base.entity.WeiXinInfo;

@Component
public class WeiXinInfoDaoImpl extends AbstractDao<WeiXinInfo, String> implements WeiXinInfoDao {

	@Autowired
	private DBAdapter dBAdapter;

	@Override
	public Class<WeiXinInfo> getEntityClass() {
		return WeiXinInfo.class;
	}

	@Override
	public DBAdapter getDBAdapter() {
		return dBAdapter;
	}

}
