package com.linjia.web.service;


import java.math.BigDecimal;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.AccountcashChange;

public interface AccountcashChangeService extends BaseService<AccountcashChange, Long>{
	
	/**
	 * 插入用户钱包交易变化记录
	 * lixinling 
	 * 2016年9月21日 上午11:21:41
	 * @param action
	 * @param userId
	 * @param occur
	 * @return
	 */
    int insert(int action, String userId, BigDecimal occur);
}
