package com.linjia.web.service;


import java.util.Map;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.UserCardCoupon;

public interface UserCardCouponService extends BaseService<UserCardCoupon, Long>{
	
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
	long selectCount(String userId, String mallCode);
	
	/**
	 * 验证优惠券的有效性
	 * lixinling 
	 * 2016年8月15日 上午11:00:07
	 * @param params
	 * @return
	 */
	int checkValid(Long id, String userId);

	/**
	 * 增加用户优惠券
	 * lixinling 
	 * 2016年8月15日 上午11:00:07
	 * @param params
	 * @return
	 */
	int insertUserCardCoupon(String userId, Long cardCouponId, int sourceType, Long id);
}
