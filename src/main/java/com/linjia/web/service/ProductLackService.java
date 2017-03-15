package com.linjia.web.service;

import java.util.List;
import java.util.Map;

import com.linjia.web.model.ProductLack;
import com.linjia.web.query.ProductLackQuery;

public interface ProductLackService {

	/**
	 * 插入缺少的商品对象
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	Integer insertLackProduct(ProductLack productLack);
	
	/**
	 * 删除缺少的商品对象
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	Integer deleteLackProduct(Map<String,Object> map);
	
	/**
	 * 查询缺少的商品对象
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	List<ProductLack> selectLackProduct(ProductLackQuery query);
}
