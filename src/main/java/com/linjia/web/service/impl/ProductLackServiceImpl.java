package com.linjia.web.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.linjia.web.dao.ProductLackMapper;
import com.linjia.web.model.ProductLack;
import com.linjia.web.query.ProductLackQuery;
import com.linjia.web.service.ProductLackService;

@Service
@Transactional
public class ProductLackServiceImpl implements ProductLackService{

	@Resource
	private ProductLackMapper mapper;
	
	@Override
	public Integer insertLackProduct(ProductLack productLack) {
		return mapper.insertLackProduct(productLack);
	}

	@Override
	public Integer deleteLackProduct(Map<String, Object> map) {
		return mapper.deleteLackProduct(map);
	}

	@Override
	public List<ProductLack> selectLackProduct(ProductLackQuery query) {
		return mapper.selectLackProduct(query);
	}

}
