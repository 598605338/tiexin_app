package com.linjia.web.service;


import java.util.List;
import java.util.Map;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.TailGoodsClear;
import com.linjia.web.query.TailGoodsClearQuery;

public interface TailGoodsClearService extends BaseService<TailGoodsClear, Long>{

	/**
	 * 取得每日清仓数据
	 * lixinling 
	 * 2016年9月10日 下午1:44:37
	 * @param query
	 * @return
	 */
	List<TailGoodsClear> selectEveryDayClearList(TailGoodsClearQuery query);
	
	/**
	 * 取得本期清仓数据
	 * lixinling 
	 * 2016年9月10日 下午1:45:28
	 * @param query
	 * @return
	 */
	List<TailGoodsClear> selectCurrentClearList(TailGoodsClearQuery query);
	

	/**
	 * 根据id更改活动状态
	 * lixinling 
	 * 2016年9月10日 下午1:45:28
	 * @param query
	 * @return
	 */
	int updateActivityStatusByPrimaryKey(Map<String,Object> map);
	

	/**
	 * 根据商品id，查询商品是否正在参加清仓活动 
	 * lixinling 
	 * 2016年9月10日 下午1:45:28
	 * @param query
	 * @return
	 */
	TailGoodsClear selectByProductId(Long productId, String mallCode);
}
