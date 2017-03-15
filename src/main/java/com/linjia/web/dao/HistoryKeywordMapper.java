package com.linjia.web.dao;

import java.util.List;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.HistoryKeyword;

public interface HistoryKeywordMapper extends BaseDao<HistoryKeyword, Long> {
	
	/**
	 * 历史搜索
	 * lixinling 
	 * 2016年8月26日 下午1:43:06
	 * @param userId
	 * @return
	 */
	List<HistoryKeyword> selectByUserId(String userId);
}