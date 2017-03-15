package com.linjia.web.service;


import com.linjia.base.service.BaseService;
import com.linjia.web.model.AccountcashdepositRecord;

public interface AccountcashdepositRecordService extends BaseService<AccountcashdepositRecord, Long>{
	
	/**
	 * 插入充值记录
	 * lixinling 
	 * 2016年9月17日 下午4:44:31
	 * @param model
	 * @return
	 */
	AccountcashdepositRecord insertRecord(AccountcashdepositRecord model,String spbill_create_ip, String openid);

	/**
	 * 查询订单是否已经退款成功
	 * lixinling 
	 * 2016年12月23日 上午10:45:51
	 * @param groupId
	 * @return
	 */
	Integer selectRefundByOrderGroupId(Long groupId);
}
