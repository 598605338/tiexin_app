package com.linjia.web.dao;

import java.util.List;
import java.util.Map;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.OrderRefund;
import com.linjia.web.query.OrderRefundQuery;

public interface OrderRefundMapper extends BaseDao<OrderRefund, Long> {
	
	/**
	 * 更新退款单状态
	 * lixinling 
	 * 2016年7月29日 上午11:31:40
	 * @return
	 */
	int updateRefundStatusById(Map<String,Object> map);
	
	/**
	 * 查询我的退款单
	 * lixinling 
	 * 2016年7月29日 上午11:31:40
	 * @return
	 */
	List<OrderRefund> selectMyRefundOrderList(OrderRefundQuery query);
	
	/**
	 * 查询昨天所有正在退款的微信退款单
	 * lixinling 
	 * 2016年7月29日 上午11:31:40
	 * @return
	 */
	List<OrderRefund> selectWxRefundingOrder();
	
	/**
	 * 根据id删除退款单
	 * @param id
	 * @return
	 */
	boolean deleteRefundOrderById(Long id);
	
	/**
	 * 根据订单id查询单条退款单
	 * xiangsy 
	 * 2016年7月29日 上午11:31:40
	 * @return
	 */
	OrderRefund selectOneOrder(Map<String,Object> map);
	
	/**
	 * 根据订单id查询单条退款单
	 * xiangsy 
	 * 2016年7月29日 上午11:31:40
	 * @return
	 */
	Long selectRefundIdByOrderId(Long orderGroupId);
}