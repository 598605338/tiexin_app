package com.linjia.web.service;


import java.util.List;
import java.util.Map;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.HotsellBase;
import com.linjia.web.model.HotsellProduct;
import com.linjia.web.model.Product;
import com.linjia.web.query.HotsellBaseQuery;
import com.linjia.web.query.HotsellProductQuery;

public interface HotsellService extends BaseService<HotsellBase, Long>{

	/**
	 * 查询热销活动数据列表
	 * lixinling 
	 * 2017年2月10日 下午1:45:39
	 * @param query
	 * @return
	 */
	Map<String,Object> selectByPageList(HotsellProductQuery query);
	
}
