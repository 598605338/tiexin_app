package com.linjia.web.thirdService.bea.service;

import com.linjia.web.thirdService.bea.model.ElemeResData;
import com.linjia.web.thirdService.bea.request.ElemeCreateOrderRequest.ElemeCreateRequestData;

public interface BeaService {

	/**
	 * 蜂鸟配送
	 * @return
	 */
    ElemeResData beaSend(ElemeCreateRequestData data);
	
	/**
	 * 定点次日达
	 * @return
	 */
    ElemeResData nextDaySend(ElemeCreateRequestData data);
	
	/**
	 * 查询订单状态
	 * @return
	 */
    ElemeResData queryStatus(String orderId);
	
	/* 
	 * @param orderId 订单id
	 * @param reason_code 取消原因编码
	 * @param cancel_reson 取消原因
	 */
    ElemeResData cancelBeaOrder(String orderId, Integer reason_code, String cancel_reason);
}
