package com.linjia.web.service;

import java.util.List;

import com.linjia.web.model.PinTuanOrder;
import com.linjia.web.query.PinTuanOrderQuery;

public interface PinTuanOrderService{

	/**
	 * 插入数据
	 * @param pt
	 * @return
	 */
	boolean insertPtOrder(PinTuanOrder pt);

	/**
	 * 更新数据
	 * @param pt
	 * @return
	 */
	boolean updatePtOrderById(PinTuanOrder pt);

	/**
	 * 删除数据
	 * @param id
	 * @return
	 */
	boolean deletePtOrderById(int id);

	/**
	 * 查询单条数据
	 * @param user_id
	 * @param id
	 * @return
	 */
	PinTuanOrder selectPtOrderById(Long id);

	/**
	 * 查询多条数据
	 * @param userId
	 * @return
	 */
	List<PinTuanOrder> selectPtOrderList(PinTuanOrderQuery query);
	
	/**
	 * 查询所有超时未支付订单
	 * @param userId
	 * @return
	 */
	List<PinTuanOrder> selectTimeOutOrderUnPay();
	
	/**
	 * 查询所有超时未成团 
	 * @param userId
	 * @return
	 */
	List<PinTuanOrder> selectPtTimeOutOrder();

}
