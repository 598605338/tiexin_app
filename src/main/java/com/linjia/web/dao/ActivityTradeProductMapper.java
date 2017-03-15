package com.linjia.web.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.ActivityTradeProduct;
import com.linjia.web.model.CardCoupon;

public interface ActivityTradeProductMapper extends BaseDao<ActivityTradeProduct, Long> {

	/**
	 * 插入活动兑换商品数据
	 * @param info
	 * @return
	 */
	boolean insertActTradePro(ActivityTradeProduct info);
	
	/**
	 * 更新活动兑换商品数据
	 * @param info
	 * @return
	 */
	boolean updateActTradeProById(ActivityTradeProduct info);
	
	/**
	 * 删除活动兑换商品数据
	 * @param activity_id
	 * @return
	 */
	boolean deleteActTradeProById(int id);
	
	/**
	 * 删除活动兑换商品数据
	 * @param activity_id
	 * @return
	 */
	boolean deleteActTradeProByActId(int activity_id);
	

	/**
	 * 查询单条活动兑换商品数据
	 * @param activity_id
	 * @return
	 */
	ActivityTradeProduct selectActTradeProById(int id);
	
	/**
	 * 查询单条活动兑换的所有商品的商品数目
	 * @param activity_id
	 * @return
	 */
	int selectActTraNum(int activity_id);
	
	/**
	 * 查询单条活动兑换的所有商品数据
	 * @param activity_id
	 * @return
	 */
	List<ActivityTradeProduct> selectActTradeProByActId(int activity_id);
	

	/**
	 * 查询多条活动兑换商品数据
	 * @param activity_id
	 * @return
	 */
	List<ActivityTradeProduct> selectActTradeProAll(Map<String,Object> map);
	
	/**
	 * 校验合法性并查询换购商品的换购价格
	 * @param activity_id
	 * @return
	 */
	BigDecimal selectTradePrice(Map<String,Object> map);
	
	
	/**
	 * 查询多条活动兑换商品数据
	 * @param activity_id
	 * @return
	 */
	List<ActivityTradeProduct> selectTradedProduct(Map<String,Object> map);
}
