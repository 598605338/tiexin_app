package com.linjia.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.web.dao.ProductTagsMapper;
import com.linjia.web.model.ProductTags;
import com.linjia.web.service.ProductTagsService;

@Service
@Transactional
public class ProductTagsServiceImpl extends BaseServiceImpl<ProductTags, Long> implements ProductTagsService {
	
	@Resource
	private ProductTagsMapper mapper;
	
	@Override
	public BaseDao<ProductTags, Long> getDao() {
		return mapper;
	}

	@Override
	public List<String> selectTagsByProductId(long productId) {
		return mapper.selectTagsByProductId(productId);
	}


	

}
