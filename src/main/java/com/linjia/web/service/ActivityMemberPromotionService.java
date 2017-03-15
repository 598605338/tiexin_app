package com.linjia.web.service;


import java.util.Map;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.ActivityMemberPromotion;

public interface ActivityMemberPromotionService extends BaseService<ActivityMemberPromotion, Long>{

	/**
	 * 查询当前时间正在进行的会员促销活动
	 * lixinling 
	 * 2016年9月19日 上午11:19:25
	 * @return
	 */
	ActivityMemberPromotion selectByCurrentTime();
	

	/**
	 * 查询当前时间正在进行的会员促销活动
	 * lixinling 
	 * 2016年9月19日 上午11:19:25
	 * @return
	 */
	int updatePrizeNumById(int prizeNum, Long id);
}
