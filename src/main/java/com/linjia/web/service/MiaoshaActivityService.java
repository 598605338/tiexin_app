package com.linjia.web.service;


import java.util.Date;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.MiaoshaActivityBase;
import com.linjia.web.model.User;
import com.linjia.web.query.MiaoshaActivityBaseQuery;

public interface MiaoshaActivityService extends BaseService<MiaoshaActivityBase, Long>{
	/**
	 * 查询单个对象
	 * 
	 * @return
	 */
	MiaoshaActivityBase selectOne(MiaoshaActivityBaseQuery query);
}
