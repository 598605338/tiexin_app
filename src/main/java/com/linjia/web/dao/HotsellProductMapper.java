package com.linjia.web.dao;


import java.util.List;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.HotsellProduct;
import com.linjia.web.query.HotsellProductQuery;

public interface HotsellProductMapper extends  BaseDao<HotsellProduct, Long> {
	
	/**
	 * 根据baseId查询活动商品数据
	 * lixinling 
	 * 2017年2月10日 下午5:32:40
	 * @param list
	 * @return
	 */
	List<HotsellProduct> selectByBaseId(HotsellProductQuery query);
	
	/**
	 * 根据baseId删除所有活动商品数据
	 * lixinling 
	 * 2017年2月10日 下午5:32:40
	 * @param list
	 * @return
	 */
	int deleteByBaseId(Integer baseId);
}