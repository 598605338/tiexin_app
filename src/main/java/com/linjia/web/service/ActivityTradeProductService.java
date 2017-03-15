package com.linjia.web.service;


import java.util.List;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.ActivityTradeProduct;

public interface ActivityTradeProductService extends BaseService<ActivityTradeProduct, Long>{

	/**
	 * 取得兑换列表数据
	 * lixinling 
	 * 2016年9月8日 下午5:14:05
	 * @param query
	 * @return
	 */
	List<ActivityTradeProduct> selectTradedProduct(String[] tradeProductIdArray,Long activityId);
}
