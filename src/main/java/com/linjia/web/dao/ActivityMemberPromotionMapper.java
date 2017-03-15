package com.linjia.web.dao;

import java.util.Map;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.ActivityMemberPromotion;


public interface ActivityMemberPromotionMapper extends BaseDao<ActivityMemberPromotion, Long>  {
	
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
	int updatePrizeNumById(Map<String,Object> param);
}