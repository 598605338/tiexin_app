package com.linjia.web.service;


import java.util.List;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.HistoryKeyword;
import com.linjia.web.model.HotKeyword;
import com.linjia.web.model.Product;
import com.linjia.web.query.SearchQuery;

public interface SearchService extends BaseService<Object, Long>{
	
	/**
	 * 历史搜索
	 * lixinling 
	 * 2016年8月26日 下午1:43:06
	 * @param userId
	 * @return
	 */
	List<HistoryKeyword> selectHistoryByUserId(String userId);
	
	/**
	 * 热门搜索
	 * lixinling 
	 * 2016年8月26日 下午1:43:06
	 * @param userId
	 * @return
	 */
	List<HotKeyword> selectHotKeyword();
	
	/**
	 * 搜索操作
	 * lixinling 
	 * 2016年8月26日 下午1:43:06
	 * @param userId
	 * @return
	 */
	List<Product> search(SearchQuery query);
}
