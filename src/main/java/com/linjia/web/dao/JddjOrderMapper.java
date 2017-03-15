package com.linjia.web.dao;


import java.util.Map;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.thirdService.jddj.model.OrderInfoDTO;


public interface JddjOrderMapper extends BaseDao<OrderInfoDTO, Long> {
	
	/**
	 * 根据orderId查询订单数量
	 * lixinling 
	 * 2017年1月11日 下午2:42:21
	 * @param orderId
	 * @return
	 */
	Integer countByOrderId(Long orderId);
	
	/**
	 * 根据orderId查询订单状态
	 * lixinling 
	 * 2017年1月11日 下午2:42:21
	 * @param orderId
	 * @return
	 */
	Integer selectOrderStatusByOrderId(Long orderId);
	
	/**
	 * 根据orderId更新订单状态
	 * lixinling 
	 * 2017年1月11日 下午2:42:21
	 * @param orderId
	 * @return
	 */
	boolean updateByPrimaryKey(Map<String,Object> params);
}