package com.linjia.web.service;

import java.util.List;
import java.util.Map;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.OrderGroup;
import com.linjia.web.model.OrderRefund;
import com.linjia.web.query.OrderRefundQuery;

public interface OrderRefundService extends BaseService<OrderRefund, Long> {

	/**
	 * 退款
	 * lixinling 
	 * 2016年11月29日 下午3:19:28
	 * @param orderGroup
	 * @param orderType 1:普通订单,2:拼团订单,3:积分订单,其他为普通订单
	 * @param orderType 1:拼团订单,其他为普通订单
	 * @param source 默认为整单退款操作，1为客服退款操作
	 * @return
	 */
	Map<String,Object> insertRefund(OrderGroup orderGroup, String userId, Integer orderType, Integer source, Long refundId);
	
	/**
	 * 更新退款单状态
	 * lixinling 
	 * 2016年7月29日 上午11:31:40
	 * @return
	 */
	boolean updateRefundStatusById(Map<String,Object> map);
	
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
	 * 查询单条退款单
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
