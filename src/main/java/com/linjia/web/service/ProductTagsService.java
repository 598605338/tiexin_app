package com.linjia.web.service;


import java.util.List;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.ProductBannerImages;
import com.linjia.web.model.ProductTags;

public interface ProductTagsService extends BaseService<ProductTags, Long>{
	
	/**
	 * 根据商品id查询商品标签列表
	 * lixinling
	 * 2016年7月8日 上午10:11:59
	 * @param productId
	 * @return
	 */
	List<String> selectTagsByProductId(long productId);
}
