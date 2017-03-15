package com.linjia.web.dao;

import java.util.List;
import java.util.Map;

import com.linjia.web.model.ActivityInfo;

public interface ActivityInfoMapper{

	/**
	 * 插入活动信息数据
	 * @param info
	 * @return
	 */
	boolean insertActInfo(ActivityInfo info);
	
	/**
	 * 更新活动信息数据
	 * @param info
	 * @return
	 */
	boolean updateActInfoById(ActivityInfo info);
	
	/**
	 * 删除活动信息数据
	 * @param activity_id
	 * @return
	 */
	boolean deleteActInfoById(int activity_id);
	

	/**
	 * 查询单条活动信息数据
	 * @param activity_id
	 * @return
	 */
	ActivityInfo selectActInfoById(int activity_id);
	

	/**
	 * 查询多条活动信息数据
	 * @param activity_id
	 * @return
	 */
	List<ActivityInfo> selectActInfoAll(Map<String,Object> map);
	
	/**
	 * 查询当前时间正在进行的活动数据
	 * @param activity_id
	 * @return
	 */
	List<ActivityInfo> selectActInfoByCurrTime(Map<String,Object> map);
	
	/**
	 * 查询当前时间订单的相关活动信息 
	 * selectOrderActInfoByCurrTime
	 * @return
	 */
	List<ActivityInfo> selectOrderActInfoByCurrTime(Map<String,Object> map);
	
	/**
	 * 查询当前时间针对全部商品的折扣活动
	 * selectOrderActInfoByCurrTime
	 * @return
	 */
	ActivityInfo selectDiscountActAllProductByNow(String mallCode);
	
}
