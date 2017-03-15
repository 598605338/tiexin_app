package com.linjia.web.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.OrderGroup;
import com.linjia.web.query.OrderGroupQuery;

public interface OrderGroupMapper extends BaseDao<OrderGroup, Long> {
	
	/**
	 * 更改订单状态
	 * lixinling 
	 * 2016年7月28日 下午3:32:16
	 * @param map
	 * @return
	 */
	int updateStatusById(Map<String,Object> map);
	
	/**
	 * 查询我的全部订单
	 * lixinling 
	 * 2016年7月28日 下午3:32:16
	 * @param map
	 * @return
	 */
	List<OrderGroup> selectMyAllOrderList(OrderGroupQuery query);
	
	/**
	 * 查询用户当前订单数量
	 * lixinling 
	 * 2016年9月26日 下午4:14:30
	 * @param userId
	 * @return
	 */
	Long selectCountByUserId(String userId);
	
	/**
	 * 催单
	 * lixinling 
	 * 2016年9月26日 下午4:14:30
	 * @param userId
	 * @return
	 */
	 int updateUrgeNumById(Long groupId);
	
		
	/**
	 * 查询我的发送物流的订单全部信息
	 * xiangsy 
	 * 2016年7月28日 下午3:32:16
	 * @param map
	 * @return
	 */
	OrderGroup selectLogisOrderInfo(Long order_id);
	
	/**
	 * 查询所有已超时的订单
	 * lixinling 
	 * 2016年10月04日 下午3:32:16
	 * @param map
	 * @return
	 */
	List<OrderGroup> selectAllTimeOutOrder(Date currentTime);
	
	
	/**
	 * 根据鼎力云订单号查询相应订单
	 * lixinling 
	 * 2016年10月18日 下午3:32:16
	 * @param map
	 * @return
	 */
	OrderGroup selectByUhdOrderId(String uhdOrderId);
	
	/**
	 * 查询订单状态
	 * lixinling 
	 * 2016年12月13日 下午3:32:16
	 * @param map
	 * @return
	 */
	Integer selectStatusByPrimaryKey(Long groupId);
	
	/**
	 * 查询达达1取货1小时以后订单未完成的订单号
	 */
	List<Long> selectDaDaFinOrder();
}