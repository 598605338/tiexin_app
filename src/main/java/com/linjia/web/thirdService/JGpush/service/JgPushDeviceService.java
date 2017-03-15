package com.linjia.web.thirdService.JGpush.service;

import java.util.List;
import java.util.Map;

import com.linjia.web.model.JgPushCustomer;

public interface JgPushDeviceService {

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
    List<JgPushCustomer> selectJgPush(String shop_id, String regId);
	
	/**
	 * 极光推送设备id集合查询
	 * @param shop_ids
	 * @return
	 */
    List<String> selectJgPushIdList(String shop_ids);
	
	/**
	 * 极光消息推送
	 * @param shop_ids
	 * @param type 消息类型
	 * @param orderSource 订单来源(1,邻家；2，美团；3，百度)
	 * @return
	 */
    int pushMessage(String shop_ids, String type, String orderSource);
	
	/**
	 * 极光消息推送
	 * @param orderId
	 * @param type 类型(1,新订单;2，订单提醒;3，催单;4，取消申请;5，自配送;6用户取消申请（京东）;7订单锁定)
	 * @param orderSource 订单来源(1,邻家；2，美团；3，百度；4，京东;5,饿了么)
	 * @return
	 */
    int pushMessageByOrderId(Long orderId, String type, String orderSource);
	
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
	 * 根据饿了么订单id,查询店铺id
	 * @param order_id
	 * @return
	 */
    String selectElemeShopIdbyOrderid(String order_id);
}
