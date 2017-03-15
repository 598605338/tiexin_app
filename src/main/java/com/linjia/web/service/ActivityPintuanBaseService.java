package com.linjia.web.service;


import java.util.List;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.ActivityPintuanBase;
import com.linjia.web.query.ActivityPintuanBaseQuery;

public interface ActivityPintuanBaseService extends BaseService<ActivityPintuanBase, Long>{
	
	/**
	 * 取得拼团商品列表
	 * lixinling
	 * 2016年7月7日 下午5:21:33
	 * @param query
	 * @return
	 */
	List<ActivityPintuanBase> selectPintuanProductList(ActivityPintuanBaseQuery query);
	
	/**
	 * 根据id取得拼团商品详情信息
	 * lixinling
	 * 2016年7月7日 下午5:21:33
	 * @param query
	 * @return
	 */
	ActivityPintuanBase selectDetailById(int id);
	
	/**
	 * 根据id更新
	 * @param base
	 * @return
	 */
	boolean updateByBaseId(ActivityPintuanBase base);
}
