package com.linjia.web.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.web.dao.AccountcashChangeMapper;
import com.linjia.web.model.AccountcashChange;
import com.linjia.web.service.AccountcashChangeService;

@Service
@Transactional
public class AccountcashChangeServiceImpl extends BaseServiceImpl<AccountcashChange, Long> implements AccountcashChangeService {
	
	@Resource
	private AccountcashChangeMapper mapper;

	@Override
	public BaseDao<AccountcashChange, Long> getDao() {
		return mapper;
	}

	@Override
	public int insert(int action, String userId, BigDecimal occur) {
		AccountcashChange model = new AccountcashChange();
		model.setAction(action);
		model.setUserId(userId);
		model.setOccur(occur);
		model.setCreDate(new Date());
		return mapper.insertSelective(model);
	}
	
	
}
