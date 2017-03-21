package com.linjia.web.thirdService.message.constants;

public class MessageConfig {

	public static String USERNAME="";
	
	public static String PWD="";
	
	public static String SIGN="【邻家便利】(邻家商城)";
	
	public static String MSGTYPE="注册验证码是  ";
	
	public static String CONTENT="，邻家提醒您请勿将此码发给他人，请尽快验证";
	
	// 查询余额
	public static String BALANCEURL="http://114.113.154.5/sms.aspx?action=overage&account="+USERNAME+"&password="+PWD;

	// 发送短信
	public static String SENDNSGURL="http://114.113.154.5/sms.aspx?action=send&account=" + USERNAME + "&password="+PWD;
	
	// 状态报告接口
	public static String STATUSURL="http://114.113.154.5/statusApi.aspx?action=query&account="+USERNAME+"&password="+PWD;
}
