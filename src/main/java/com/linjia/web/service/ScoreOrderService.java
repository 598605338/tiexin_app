package com.linjia.web.service;


import java.util.List;
import java.util.Map;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.ScoreOrder;
import com.linjia.web.model.ScoreProduct;
import com.linjia.web.query.ScoreOrderQuery;
import com.linjia.web.query.ScoreProductQuery;

public interface ScoreOrderService extends BaseService<ScoreOrder, Long>{
	
	/**
	 * 积分兑换订单生成(纯积分模式)
	 * 
	 * lixinling 
	 * 2016年8月10日 上午10:18:57
	 * @param query
	 * @param userId
	 * @return
	 */
	boolean insertScoreOrder(ScoreProduct scoreProduct, String userId, String loginCod, String comment);
	
	/**
	 * 积分兑换订单生成+并支付（积分+金钱模式）
	 * 
	 * lixinling 
	 * 2016年8月10日 上午10:18:57
	 * @param query
	 * @param userId
	 * @return
	 */
	Map<String, Object> insertPayScoreOrder(ScoreProduct scoreProduct, ScoreOrder model, String userId, String loginCode);
	
	/**
	 * 更改订单状态并将兑换的商品券放入用户我的优惠券中
	 * 
	 * lixinling 
	 * 2016年8月10日 上午10:18:57
	 * @param query
	 * @param userId
	 * @return
	 */
    boolean updatePayScoreOrderStatus(String userId, Long cardCouponId, Long scoreOrderId, Integer payType);
	
	/**
	 * 更新积分订单状态
	 * lixinling 
	 * 2016年8月11日 下午1:21:56
	 * @param id
	 * @param orderStatus
	 * @return
	 */
	boolean updateStatusById(Long id, int orderStatus);
	
	/**
	 * 查询兑换记录
	 * lixinling 
	 * 2016年9月18日 下午8:59:59
	 * @return
	 */
	List<ScoreOrder> selectExchangeRecordList(ScoreOrderQuery query);
}
