package com.linjia.web.dao;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.CustMaster;

public interface CustMasterMapper extends BaseDao<CustMaster, String> {
	
	/**
	 * 根据登陆手机号查询用户的login
	 * lixinling 
	 * 2016年8月12日 下午1:45:48
	 * @param model
	 * @return
	 */
	String selectLoginByPrimaryKey(String userId);
	
	/**
	 * 当用户记录存在时进行更新，不存在时进行插入
	 * lixinling 
	 * 2016年8月12日 下午1:45:48
	 * @param model
	 * @return
	 */
	int insertOrUpdate(CustMaster model);
	
	/**
	 * 更新用户信息
	 * lixinling 
	 * 2016年9月7日 下午3:22:59
	 * @param model
	 * @return
	 */
	int updateByPrimaryKey(CustMaster model);
}