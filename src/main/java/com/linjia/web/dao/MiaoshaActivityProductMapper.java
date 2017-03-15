package com.linjia.web.dao;

import java.util.List;
import java.util.Map;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.MiaoshaActivityProduct;
import com.linjia.web.query.MiaoshaActivityProductQuery;

public interface MiaoshaActivityProductMapper extends BaseDao<MiaoshaActivityProduct, Long> {
	
	
	List<MiaoshaActivityProduct> selectAllByBaseId(MiaoshaActivityProductQuery query);
	
	
	/**
	 * 根据商品id，判断该商品是否正在进行秒杀
	 * lixinling
	 * 2016年7月18日 上午10:17:11
	 * @param product_id
	 * @return
	 */
	MiaoshaActivityProduct getPanicingProductByProductId(Map map);
	
	/**
	 * 对秒杀的商品进行减库存操作
	 * lixinling
	 * 2016年7月18日 上午10:17:11
	 * @param product_id
	 * @return
	 */
	int updateQuantityById(Map map);
}