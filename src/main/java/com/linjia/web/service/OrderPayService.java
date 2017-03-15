package com.linjia.web.service;


import java.math.BigDecimal;
import java.util.Map;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.OrderGroup;
import com.linjia.web.model.PinTuanOrder;

public interface OrderPayService extends BaseService<Object, Long>{
	
	/**
	 * 调起支付
	 * lixinling 
	 * 2016年8月10日 下午4:17:42
	 * @param payType
	 * @param body
	 * @param totalFee
	 * @param outTradeNo
	 * @param spbill_create_ip
	 * @return
	 */
    Map<String,Object> payHandle(int payType, String body, String attach, BigDecimal realPrice, long outTradeNo, String spbill_create_ip, String userId, String openid, String notify_url, String passsword);
	
	/**
	 * 支付完成后更新普通订单状态
	 * lixinling 
	 * 2016年8月10日 下午4:17:42
	 * @param payType
	 * @param body
	 * @param totalFee
	 * @param outTradeNo
	 * @param spbill_create_ip
	 * @return
	 */
    boolean updateOrderGroupByPaySuccessed(OrderGroup orderGroup, String transaction_id, String out_trade_no, int payType, int payStatus, int orderGroupStatus, String mallCode);
	
	/**
	 * 支付完成后更新拼团订单状态
	 * lixinling 
	 * 2016年8月10日 下午4:17:42
	 * @param payType
	 * @param body
	 * @param totalFee
	 * @param outTradeNo
	 * @param spbill_create_ip
	 * @return
	 */
    boolean updatePintuanOrderByPaySuccessed(PinTuanOrder pinTuanOrder, int payType, int payStatus);
}
