package com.linjia.web.dao;

import java.util.Map;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.FavouriteProduct;


public interface FavouriteProductMapper extends BaseDao<FavouriteProduct, Long> {
	
	/**
	 * 删除商品关注记录
	 * lixinling 
	 * 2016年11月24日 下午2:21:13
	 * @param params
	 * @return
	 */
	int deleteByProIDAndUserId(Map<String,Object> params);
}