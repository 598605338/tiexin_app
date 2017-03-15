package com.linjia.web.dao;

import java.util.Map;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.UserCardCoupon;


public interface UserCardCouponMapper extends BaseDao<UserCardCoupon, Long> {
	
	/**
	 * 更新优惠券使用状态
	 * lixinling 
	 * 2016年8月15日 上午11:00:07
	 * @param params
	 * @return
	 */
	int updateUseStatusByPrimaryKey(UserCardCoupon model);
	
	/**
	 * 查询用户优惠券的数量
	 * lixinling 
	 * 2016年8月15日 上午11:00:07
	 * @param params
	 * @return
	 */
	long selectCount(Map<String,Object> params);
	
	/**
	 * 验证优惠券的有效性
	 * lixinling 
	 * 2016年8月15日 上午11:00:07
	 * @param params
	 * @return
	 */
	int checkValid(Map<String,Object> params);
}