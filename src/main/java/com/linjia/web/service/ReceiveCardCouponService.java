package com.linjia.web.service;


import java.util.List;
import java.util.Map;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.ReceiveCardCoupon;
import com.linjia.web.query.ReceiveCardCouponQuery;

public interface ReceiveCardCouponService extends BaseService<ReceiveCardCoupon, Long>{
	
	/**
	 * 分页查询数据
	 * lixinling 
	 * 2016年9月4日 下午4:18:41
	 * @param query
	 * @return
	 */
	List<ReceiveCardCoupon> selectByPageList(ReceiveCardCouponQuery query);
	
	/**
	 * 更新发布数量
	 * lixinling 
	 * 2016年9月8日 下午1:34:54
	 * @param param
	 * @return
	 */
	int updatePublishNumByPrimaryKey(Map<String,Object> param);
	
	/**
	 * 用户领取优惠券
	 * lixinling 
	 * 2016年9月8日 下午1:34:54
	 * @param param
	 * @return
	 */
	boolean insertCardCouponForUser(String userId, Long cardCouponId, Long id, int publishNum);
}
