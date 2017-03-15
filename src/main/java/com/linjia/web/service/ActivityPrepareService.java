package com.linjia.web.service;


import java.util.List;
import java.util.Map;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.ActivityPrepare;
import com.linjia.web.query.ActivityPrepareQuery;

public interface ActivityPrepareService extends BaseService<ActivityPrepare, Long>{

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
	int updateQuantityById(Integer activityPrepareId, Integer buyQty);
}
