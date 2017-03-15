package com.linjia.web.service.impl;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.constants.Application;
import com.linjia.constants.Constants;
import com.linjia.tools.DateComFunc;
import com.linjia.tools.Util;
import com.linjia.web.dao.AccountcashdepositRecordMapper;
import com.linjia.web.model.AccountcashdepositRecord;
import com.linjia.web.service.AccountcashdepositRecordService;
import com.linjia.web.service.OrderPayService;

@Service
@Transactional
public class AccountcashdepositRecordServiceImpl extends BaseServiceImpl<AccountcashdepositRecord, Long> implements
		AccountcashdepositRecordService {

	@Resource
	private AccountcashdepositRecordMapper mapper;
	

	@Override
	public BaseDao<AccountcashdepositRecord, Long> getDao() {
		return mapper;
	}

	@Override
	public AccountcashdepositRecord insertRecord(AccountcashdepositRecord model,String spbill_create_ip, String openid) {
		mapper.insertSelective(model);
		return model;
	}

	@Override
	public Integer selectRefundByOrderGroupId(Long groupId) {
		return mapper.selectRefundByOrderGroupId(groupId);
	}
}
