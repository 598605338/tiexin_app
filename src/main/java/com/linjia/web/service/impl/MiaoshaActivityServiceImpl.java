package com.linjia.web.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.web.dao.MiaoshaActivityMapper;
import com.linjia.web.model.MiaoshaActivityBase;
import com.linjia.web.model.User;
import com.linjia.web.query.MiaoshaActivityBaseQuery;
import com.linjia.web.service.MiaoshaActivityService;

@Service
@Transactional
public class MiaoshaActivityServiceImpl extends BaseServiceImpl<MiaoshaActivityBase, Long> implements MiaoshaActivityService {
	
	@Resource
	private MiaoshaActivityMapper mapper;
	
	@Override
	public BaseDao<MiaoshaActivityBase, Long> getDao() {
		return mapper;
	}


	@Override
	public MiaoshaActivityBase selectOne(MiaoshaActivityBaseQuery query) {
		return mapper.selectOne(query);
	}

}
