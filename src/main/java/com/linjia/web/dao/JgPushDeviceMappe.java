package com.linjia.web.dao;

import java.util.List;
import java.util.Map;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.JgPushCustomer;

public interface JgPushDeviceMappe extends BaseDao<JgPushCustomer, Long>{
	

	/**
	 * 极光推送设备信息插入
	 * @param jgPushCustomer
	 * @return
	 */
    int insertJgPush(JgPushCustomer jgPushCustomer);
	
	/**
	 * 极光推送设备信息删除
	 * @param shop_id
	 * @param registration_id
	 * @return
	 */
    int deleteJgPush(Map<String, String> map);
	
	/**
	 * 极光推送设备信息修改
	 * @param jgPushCustomer
	 * @return
	 */
    int  updateJgPush(JgPushCustomer jgPushCustomer);
	
	/**
	 * 极光推送设备信息查询
	 * @param shop_id
	 * @param regId
	 * @return
	 */
    List<JgPushCustomer>  selectJgPush(Map<String, Object> map);
	
	/**
	 * 极光推送设备id集合查询
	 * @param shop_ids
	 * @return
	 */
    List<String> selectJgPushIdList(String shop_ids);
	
	/**
	 * 根据邻家订单id,查询店铺id
	 * @param order_id
	 * @return
	 */
    String selectLjShopIdbyOrderid(String order_id);
	
	/**
	 * 根据美团订单id,查询店铺id
	 * @param order_id
	 * @return
	 */
    String selectMtShopIdbyOrderid(String order_id);
	
	/**
	 * 根据百度订单id,查询店铺id
	 * @param order_id
	 * @return
	 */
    String selectBdShopIdbyOrderid(String order_id);
	
	/**
	 * 根据京东订单id,查询店铺id
	 * @param order_id
	 * @return
	 */
    String selectJdShopIdbyOrderid(Long order_id);
	
	/**
	 * 根据饿了么订单id,查询店铺id
	 * @param order_id
	 * @return
	 */
    String selectElemeShopIdbyOrderid(String order_id);
}
