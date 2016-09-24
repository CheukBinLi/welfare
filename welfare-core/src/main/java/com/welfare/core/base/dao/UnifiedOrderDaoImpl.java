package com.welfare.core.base.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cheuks.bin.original.common.dbmanager.DBAdapter;
import com.cheuks.bin.original.common.dbmanager.dao.AbstractDao;
import com.welfare.core.base.entity.UnifiedOrder;

@Component
public class UnifiedOrderDaoImpl extends AbstractDao<UnifiedOrder, String> implements UnifiedOrderDao {

	@Autowired
	private DBAdapter dBAdapter;

	@Override
	public Class<UnifiedOrder> getEntityClass() {
		return UnifiedOrder.class;
	}

	@Override
	public DBAdapter getDBAdapter() {
		return dBAdapter;
	}

}
