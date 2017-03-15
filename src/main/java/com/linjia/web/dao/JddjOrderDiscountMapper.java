package com.linjia.web.dao;

import java.util.List;
import java.util.Map;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.thirdService.jddj.model.OrderDiscountDTO;


public interface JddjOrderDiscountMapper extends BaseDao<OrderDiscountDTO, Long>  {
	
	/**
	 * 批量插入订单的优惠List列表
	 * lixinling 
	 * 2017年1月11日 下午4:29:59
	 * @param list
	 * @return
	 */
	int insertBatch(Map<String,Object> params);
}