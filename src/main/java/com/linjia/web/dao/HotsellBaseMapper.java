package com.linjia.web.dao;

import java.util.List;
import java.util.Map;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.HotsellBase;
import com.linjia.web.query.HotsellBaseQuery;

public interface HotsellBaseMapper extends BaseDao<HotsellBase, Long> {
	
	/**
	 * 查询当前正在进行的活动
	 * lixinling 
	 * 2017年2月10日 下午1:45:39
	 * @param query
	 * @return
	 */
	HotsellBase selectCurrentActivity();
	
	/**
	 * 根据条件查询热销活动的总数量
	 * lixinling 
	 * 2017年2月10日 下午1:45:39
	 * @param query
	 * @return
	 */
	Integer totalCountByPage(HotsellBaseQuery query);
	
	/**
	 * 更新使用状态
	 * lixinling 
	 * 2017年2月10日 下午1:45:39
	 * @param query
	 * @return
	 */
	boolean updateStatusByPrimaryKey(Map<String,Object> param);
}