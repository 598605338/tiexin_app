package com.linjia.web.dao;

import java.util.List;
import java.util.Map;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.CardCoupon;
import com.linjia.web.query.UserCardCouponQuery;

/**
 * 优惠券
 * @author lixinling
 * 2016年8月15日 上午9:55:23
 */
public interface CardCouponMapper extends BaseDao<CardCoupon, Long> {
    
	
	/**
	 * 查询用户未过期的优惠券
	 * lixinling 
	 * 2016年8月15日 上午9:49:32
	 * @param params
	 * @return
	 */
	List<CardCoupon> selectNotExpiredByUserId(UserCardCouponQuery userCardCouponQuery);
	
	/**
	 * 查询用户已过期的优惠券
	 * lixinling 
	 * 2016年8月15日 上午9:49:32
	 * @param params
	 * @return
	 */
	List<CardCoupon> selectExpiredByUserId(UserCardCouponQuery userCardCouponQuery);
	
	/**
	 * 查询用户已使用的优惠券
	 * lixinling 
	 * 2016年8月15日 上午9:49:32
	 * @param params
	 * @return
	 */
	List<CardCoupon> selectUsedByUserId(UserCardCouponQuery userCardCouponQuery);
	
	/**
	 * 校验用户使用优惠券的合法性
	 * lixinling 
	 * 2016年8月15日 上午9:49:32
	 * @param params
	 * @return
	 */
	CardCoupon checkCouponByUserCardCouponId(UserCardCouponQuery userCardCouponQuery);
	
	/**
	 * 查询优惠券对门店和商品的支持信息
	 * lixinling 
	 * 2016年9月23日 上午10:49:32
	 * @param params
	 * @return
	 */
	CardCoupon cardCheckByPrimaryKey(Long cardId);
	
	/**
	 * 查询用户在本次订单中能使用的优惠券
	 * lixinling 
	 * 2016年8月15日 上午9:49:32
	 * @param params
	 * @return
	 */
	List<CardCoupon> selectCanUsedByUserId(UserCardCouponQuery userCardCouponQuery);
	
	/**
	 * 更新优惠券剩余数量
	 * lixinling 
	 * 2016年11月28日 上午9:49:32
	 * @param params
	 * @return
	 */
	int updateTotalNumByCardId(Map<String,Object> param);
}