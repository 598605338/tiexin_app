package com.linjia.web.dao;

import java.util.List;
import java.util.Map;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.ActivityPrepare;
import com.linjia.web.query.ActivityPrepareQuery;


public interface ActivityPrepareMapper extends BaseDao<ActivityPrepare, Long> {
	
	/**
	 * 分页取得列表数据
	 * lixinling 
	 * 2016年9月8日 下午5:14:05
	 * @param query
	 * @return
	 */
	List<ActivityPrepare> selectByPageList(ActivityPrepareQuery query);
	
	/**
	 * 根据记录id对预约商品进行减库存操作
	 * lixinling 
	 * 2016年10月28日 下午5:24:31
	 * @param param
	 * @return
	 */
	int updateQuantityById(Map<String,Object> param);
}