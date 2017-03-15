package com.linjia.web.thirdService.message.service;

import org.json.JSONObject;

import com.linjia.web.thirdService.message.model.Message;

public interface MessageService {

	/**
	 * 查询余额
	 * @param userid 用户id
	 * @return
	 */
    JSONObject selBalance(String userid);
	
	/**
	 * 发送短信
	 * @param userid 用户id  可不写
	 * @param mobile 用户接收手机号  必填
	 * @param content 短信内容  不填发送验证码，填写发送填写内容
	 * @param sendTime 发送时间  空表示立即发送，非空制定发送时间
	 * @return
	 */
    JSONObject SendMessage(String userid, String mobile, String content, String sendTime);
	
	/**
	 * 状态报告
	 * @param userid 用户id  
	 * @return
	 */
    JSONObject StatusReport(String userid);
	
	/**
	 * 推送状态与上行
	 * @param userid
	 * @param account
	 * @param password
	 * @param action
	 * @return
	 */
    JSONObject callBackInfo(String userid, String account, String password, String action);
	
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
	
	/**
	 * 发送短信
	 * @param userid 用户id  可不写
	 * @param mobile 用户接收手机号  必填
	 * @param content 短信内容  不填发送验证码，填写发送填写内容
	 * @param sendTime 发送时间  空表示立即发送，非空制定发送时间
	 * @return
	 */
	JSONObject sendCheckCode(String userid, String mobile,String content,String sendTime);
}
