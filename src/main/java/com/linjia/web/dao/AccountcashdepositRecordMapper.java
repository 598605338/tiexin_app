package com.linjia.web.dao;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.AccountcashdepositRecord;


public interface AccountcashdepositRecordMapper extends BaseDao<AccountcashdepositRecord, Long> {
	
	
	/**
	 * 查询订单是否已经退款成功
	 * lixinling 
	 * 2016年12月23日 上午10:45:51
	 * @param groupId
	 * @return
	 */
	Integer selectRefundByOrderGroupId(Long groupId);
}