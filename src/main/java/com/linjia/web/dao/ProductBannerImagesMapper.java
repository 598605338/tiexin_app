package com.linjia.web.dao;

import java.util.List;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.ProductBannerImages;


public interface ProductBannerImagesMapper extends BaseDao<ProductBannerImages, Long> {
	
	/**
	 * 根据商品id查询轮播图列表
	 * lixinling
	 * 2016年7月8日 上午10:11:59
	 * @param productId
	 * @return
	 */
	List<String> selectAllByProductId(long productId);
}