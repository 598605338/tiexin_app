package com.linjia.web.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.web.dao.DesaccountPayRecordMapper;
import com.linjia.web.model.DesaccountPayRecord;
import com.linjia.web.service.DesaccountPayRecordService;

@Service
@Transactional
public class DesaccountPayRecordServiceImpl extends BaseServiceImpl<DesaccountPayRecord, Long> implements DesaccountPayRecordService {
	
	@Resource
	private DesaccountPayRecordMapper mapper;

	@Override
	public BaseDao<DesaccountPayRecord, Long> getDao() {
		return mapper;
	}
	
}
