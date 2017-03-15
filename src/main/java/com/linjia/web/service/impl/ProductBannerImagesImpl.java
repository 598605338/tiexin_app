package com.linjia.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.web.dao.ProductBannerImagesMapper;
import com.linjia.web.dao.ProductSpecMapper;
import com.linjia.web.model.ProductBannerImages;
import com.linjia.web.model.ProductSpec;
import com.linjia.web.service.ProductBannerImagesService;
import com.linjia.web.service.ProductSpecService;

@Service
@Transactional
public class ProductBannerImagesImpl extends BaseServiceImpl<ProductBannerImages, Long> implements ProductBannerImagesService {
	
	@Resource
	private ProductBannerImagesMapper mapper;
	
	@Override
	public BaseDao<ProductBannerImages, Long> getDao() {
		return mapper;
	}

	@Override
	public List<String> selectAllByProductId(long productId) {
		return mapper.selectAllByProductId(productId);
	}


	

}
