package com.linjia.web.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.web.dao.FavouriteProductMapper;
import com.linjia.web.model.FavouriteProduct;
import com.linjia.web.service.FavouriteProductService;

@Service
@Transactional
public class FavouriteProductServiceImpl extends BaseServiceImpl<FavouriteProduct, Long> implements FavouriteProductService {
	
	@Resource
	private FavouriteProductMapper mapper;

	@Override
	public BaseDao<FavouriteProduct, Long> getDao() {
		return mapper;
	}

	@Override
	public int deleteByProIDAndUserId(Map<String, Object> params) {
		return mapper.deleteByProIDAndUserId(params);
	}
	
}
