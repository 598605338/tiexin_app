package com.linjia.web.dao;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.ProductSpec;


public interface ProductSpecMapper extends BaseDao<ProductSpec, Long>{
	/**
	 * 根据商品id查询商品的安全库存
	 * 
	 * @return
	 */
	int selectSafeQuantityByProductId(Long productId);
}