package com.linjia.web.service;


import java.util.List;
import java.util.Map;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.ThirdOrderProduct;
import com.linjia.web.thirdService.jddj.model.DJOrderQuery;
import com.linjia.web.thirdService.jddj.model.JddjDeliveryStatus;
import com.linjia.web.thirdService.jddj.model.OrderInfoDTO;
import com.linjia.web.thirdService.jddj.model.OrderProductDTO;

public interface ThirdJDdjService extends BaseService<OrderInfoDTO, Long>{

	/**
	 * 查询订单信息
	 * lixinling 
	 * 2016年9月8日 下午5:14:05
	 * @param query
	 * @return
	 */
	List<OrderInfoDTO> orderQuery(DJOrderQuery query);
	
	/**
	 * 将京东到家的订单信息存入本地表
	 * lixinling 
	 * 2017年1月11日 下午2:34:26
	 * @param order
	 * @return
	 */
	int insertOrder(OrderInfoDTO order);
	
	/**
	 * 根据orderId查询订单信息
	 * lixinling 
	 * 2016年9月8日 下午5:14:05
	 * @param query
	 * @return
	 */
	OrderInfoDTO orderQueryById(Long orderId);
	
	/**
	 * 修改本地订单状态并更新京东到家订单状态，最后进行鼎力云抛单操作
	 * lixinling 
	 * 2017年1月12日 下午5:38:34
	 * @param order
	 * @param userId
	 * @param isAgreed 操作类型 true 接单 false不接单
	 * @return
	 */
	boolean orderAcceptOperate(OrderInfoDTO order, String userId, Boolean isAgreed);
	
	/**
	 * 根据orderId更新订单状态
	 * lixinling 
	 * 2017年1月11日 下午2:42:21
	 * @param orderId
	 * @return
	 */
	boolean updateByPrimaryKey(Map<String,Object> params);
	
	/**
	 * 商家后台京东众包配送接口
	 * lixinling 
	 * 2017年1月12日 下午5:38:34
	 * @param order
	 * @param userId
	 * @param isAgreed 操作类型 true 接单 false不接单
	 * @return
	 */
	boolean orderJDZBDelivery(Long orderId, String userId);

	/**
	 * 订单取消申请确认操作
	 * lixinling 
	 * 2017年2月24日 下午3:22:18
	 * @param request
	 * @param orderId 订单号
	 * @param userId 操作人
	 * @param isAgreed 操作类型 true 同意 false驳回
	 * @param remark 操作备注
	 * @return
	 */
	boolean orderCancelOperate(Long orderId, String userId, Boolean isAgreed, String remark);
	
	/**
	 * 商家确认收到拒收退回（或取消）的商品接口
	 * lixinling 
	 * 2017年2月24日 下午3:22:18
	 * @param request
	 * @param orderId 订单号
	 * @param userId 操作人
	 * @param isAgreed 操作类型 true 同意 false驳回
	 * @param remark 操作备注
	 * @return
	 */
	boolean confirmReceiveGoods(Long orderId);
	
	/**
	 * 催配送员抢单接口
	 * lixinling 
	 * 2017年2月27日 下午3:22:18
	 * @param request
	 * @param orderId 订单号
	 * @param userId 操作人
	 * @return
	 */
	boolean urgeDispatching(Long orderId, String userId);
	
	/**
	 * 订单调整接口
	 * lixinling 
	 * 2017年2月27日 下午3:22:18
	 * @param request
	 * @param orderId 订单号
	 * @param userId 操作人
	 * @return
	 */
	boolean adjustOrder(Long orderId, String userId, String remark, String oaosAdjustDTOList);

	/**
	 * 插入运单状态消息
	 * lixinling 
	 * 2017年2月27日 上午9:42:06
	 * @param deliveryStatus
	 * @return
	 */
	int insertDeliveryStatus(JddjDeliveryStatus deliveryStatus);
	
	/**
	 * 根据订单号查询订单是否存在
	 * lixinling 
	 * 2017年3月6日 上午9:42:06
	 * @param orderId
	 * @return
	 */
	int countByOrderId(Long orderId);
	
	/**
	 * 根据订单Id取得订单商品列表
	 * lixinling 
	 * 2017年03月01日 下午5:15:04
	 * @param orderId
	 * @return
	 */
	List<OrderProductDTO> getOrderProductByOrderId(Long orderId);
	
	/**
	 * 为商家端提供订单商品列表
	 * lixinling 
	 * 2017年03月1日 下午2:54:35
	 * @param list
	 * @return
	 */
	List<ThirdOrderProduct> selectJdOrderProduct(Long orderId);

	/**
	 * 根据orderId查询订单状态
	 * lixinling 
	 * 2017年1月11日 下午2:42:21
	 * @param orderId
	 * @return
	 */
	Integer selectOrderStatusByOrderId(Long orderId);
}
