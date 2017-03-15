package com.linjia.web.service;


import java.util.Map;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.FavouriteProduct;

public interface FavouriteProductService extends BaseService<FavouriteProduct, Long>{

	/**
	 * 删除商品关注记录
	 * lixinling 
	 * 2016年11月24日 下午2:21:13
	 * @param params
	 * @return
	 */
	int deleteByProIDAndUserId(Map<String,Object> params);
}
