package com.linjia.web.service;


import java.util.List;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.MiddleCatagory;

public interface MiddleCatagoryService extends BaseService<MiddleCatagory, Long>{
	
	/**
	 * 根据largeCatagoryId查询中分类
	 * lixinling
	 * 2016年7月8日 上午10:11:59
	 * @param productId
	 * @return
	 */
	List<MiddleCatagory> selectByLargeCatagoryId(int largeCatagoryId);
}
