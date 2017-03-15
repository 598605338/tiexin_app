package com.linjia.web.service;


import java.util.List;
import java.util.Map;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.CardCoupon;
import com.linjia.web.model.CardCouponProduct;
import com.linjia.web.query.UserCardCouponQuery;

/**
 * 优惠券对应的商品
 * @author lixinling
 * 2016年8月15日 上午9:55:23
 */
public interface CardCouponProductService extends BaseService<CardCouponProduct, Long>{
	
	/**
	 * 查询优惠券校验商品行数据
	 * lixinling 
	 * 2016年9月22日 下午1:44:30
	 * @param cardCouponId
	 * @return
	 */
	CardCouponProduct selectDetailBycardCouponId(Long cardCouponId, String mallCode);

	/**
	 * 查询用户商品券对应的商品code
	 * lixinling 
	 * 2016年9月22日 下午1:44:30
	 * @param cardCouponId
	 * @return
	 */
	CardCouponProduct selectByuserCardCouponId(Long userCardCouponId);
	
	/**
	 * 查询商品券对应的商品code
	 * lixinling 
	 * 2016年11月22日 下午1:44:30
	 * @param cardCouponId
	 * @return
	 */
	String selectPCodeByuserCardCouponId(Long userCardCouponId);
}
