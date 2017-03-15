package com.linjia.web.thirdService.JGpush.constant;

import java.util.HashMap;
import java.util.Map;

public class PushConstant {

	//类型(1,新订单;2，订单提醒;3，催单;4，取消申请;5，自配送;6用户取消申请（京东）)
	public  String TYPE_MESSAGE="1";
	
	private static Map<String, String> messageMap;
	
	static{
		messageMap = new HashMap<String, String>();
		
		messageMap.put("1", "[邻家便利店]你有新订单,请及时处理");
		messageMap.put("2", "[邻家便利店]你有预约订单,请及时处理");
		messageMap.put("3", "[邻家便利店]你有加急订单,请及时处理");
		messageMap.put("4", "[邻家便利店]你有取消订单,请及时处理");
		messageMap.put("5", "[邻家便利店]你有自配送订单,请及时处理");
		messageMap.put("6", "[邻家便利店]你有用户取消申请订单,请及时处理");
		messageMap.put("7", "[邻家便利店]你有新的锁定订单,请及时处理");
		
	}
	
	/**
	 * 获取推送消息
	 * 
	 * @param methodName
	 * @return
	 */
	public static String getMessage(String type) {
		String message = messageMap.get(type);
		return message;
	}
}
