package com.linjia.web.dao;



import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.MiaoshaActivityBase;
import com.linjia.web.query.MiaoshaActivityBaseQuery;

public interface MiaoshaActivityMapper extends BaseDao<MiaoshaActivityBase, Long>{
	
	/**
	 * 查询一条数据
	 * 
	 * @param id
	 * @return
	 */
	MiaoshaActivityBase selectOne(MiaoshaActivityBaseQuery query);
	
	
	
}
