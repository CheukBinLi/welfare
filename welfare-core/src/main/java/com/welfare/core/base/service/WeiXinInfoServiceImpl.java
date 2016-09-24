package com.welfare.core.base.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cheuks.bin.original.common.dbmanager.dao.BaseDao;
import com.cheuks.bin.original.common.dbmanager.service.AbstractService;
import com.welfare.core.base.dao.WeiXinInfoDao;
import com.welfare.core.base.entity.WeiXinInfo;

@Component
public class WeiXinInfoServiceImpl extends AbstractService<WeiXinInfo, String> implements WeiXinInfoService {

	@Autowired
	private WeiXinInfoDao weiXinInfoDao;

	@Override
	public BaseDao<WeiXinInfo, String> getService() {
		return weiXinInfoDao;
	}
}
