package com.linjia.web.dao;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.thirdService.message.model.Message;


public interface MessageMapper extends BaseDao<Message, Long>{
	
	/**
	 * 插入短信验证码数据
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	Integer insertMessage(Message message);
	
	/**
	 * 删除验证码
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param id
	 * @return
	 */
	Integer deleteMessage(Integer id);
	
	/**
	 * 查询验证码
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	Message selectMessage(String moblie);
	
}