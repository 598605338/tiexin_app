package com.linjia.web.dao;

import java.util.List;
import java.util.Map;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.ScoreOrder;
import com.linjia.web.query.ScoreOrderQuery;


public interface ScoreOrderMapper extends BaseDao<ScoreOrder, Long> {
	
	/**
	 * 更新积分订单状态
	 * lixinling 
	 * 2016年8月11日 下午1:21:56
	 * @param id
	 * @param orderStatus
	 * @return
	 */
	boolean updateStatusById(Map<String,Object> params);
	
	/**
	 * 将兑换的卡券Id存入订单记录中 
	 * lixinling 
	 * 2016年8月11日 下午1:21:56
	 * @param id
	 * @param orderStatus
	 * @return
	 */
	boolean updateUserCardCouponIdById(Map<String,Object> params);
	
	/**
	 * 查询兑换记录
	 * lixinling 
	 * 2016年9月18日 下午8:59:59
	 * @return
	 */
	List<ScoreOrder> selectExchangeRecordList(ScoreOrderQuery query);
}