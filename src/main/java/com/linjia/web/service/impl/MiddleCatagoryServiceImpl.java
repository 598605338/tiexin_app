package com.linjia.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.web.dao.MiddleCatagoryMapper;
import com.linjia.web.model.MiddleCatagory;
import com.linjia.web.service.MiddleCatagoryService;

@Service
@Transactional
public class MiddleCatagoryServiceImpl extends BaseServiceImpl<MiddleCatagory, Long> implements MiddleCatagoryService {
	
	@Resource
	private MiddleCatagoryMapper mapper;

	@Override
	public BaseDao<MiddleCatagory, Long> getDao() {
		return mapper;
	}

	@Override
	public List<MiddleCatagory> selectByLargeCatagoryId(int largeCatagoryId) {
		return mapper.selectByLargeCatagoryId(largeCatagoryId);
	}
	
	

}
