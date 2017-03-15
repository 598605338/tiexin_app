package com.linjia.web.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.web.dao.LargeCatagoryMapper;
import com.linjia.web.model.LargeCatagory;
import com.linjia.web.service.LargeCatagoryService;

@Service
@Transactional
public class LargeCatagoryServiceImpl extends BaseServiceImpl<LargeCatagory, Long> implements LargeCatagoryService {
	
	@Resource
	private LargeCatagoryMapper mapper;

	@Override
	public BaseDao<LargeCatagory, Long> getDao() {
		return mapper;
	}
	
	


	

}
