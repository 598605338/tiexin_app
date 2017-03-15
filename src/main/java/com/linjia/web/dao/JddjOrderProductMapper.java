package com.linjia.web.dao;

import java.util.List;
import java.util.Map;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.ThirdOrderProduct;
import com.linjia.web.thirdService.jddj.model.OrderProductDTO;


public interface JddjOrderProductMapper extends BaseDao<OrderProductDTO, Long> {
	
	/**
	 * 批量插入到家订单商品数据
	 * lixinling 
	 * 2017年1月11日 下午2:54:35
	 * @param list
	 * @return
	 */
	int insertBatch(Map<String,Object> params);
	
	/**
	 * 根据订单Id取得订单商品列表
	 * lixinling 
	 * 2017年03月1日 下午2:54:35
	 * @param list
	 * @return
	 */
	List<OrderProductDTO> selectListByOrderId(Long orderId);
	
	/**
	 * 为商家端提供订单商品列表
	 * lixinling 
	 * 2017年03月1日 下午2:54:35
	 * @param list
	 * @return
	 */
	List<ThirdOrderProduct> selectJdOrderProduct(Long orderId);
}