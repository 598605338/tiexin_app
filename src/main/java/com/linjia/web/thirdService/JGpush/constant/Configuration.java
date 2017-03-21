package com.linjia.web.thirdService.JGpush.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author xiangshouyi
 *
 */
public class Configuration {

	private static Map<String, String> keyMap;
	private static Map<String, String> apiMap;
	
	static {
		keyMap = new HashMap<String, String>();
		apiMap = new HashMap<String, String>();

		keyMap.put("jpush_app_key", "");
		keyMap.put("jpush_master_secret", "");
		
		//向某单个设备或者某设备列表推送一条通知、或者消息。
		apiMap.put("messagePush", "");
		//该 API,只用于验证推送调用是否能够成功，与推送 API 的区别在于：不向用户发送任何消息。 其他字段说明：同推送 API。
		apiMap.put("messageValidate", "");
		//提供各类统计数据查询功能
		apiMap.put("messageSum", "");
		//用于在服务器端查询、设置、更新、删除设备的 tag,alias 信息。
		apiMap.put("tagsAndsAliasOpr", "");

		//查询设备(设备的别名与标签),获取当前设备的所有属性，包含tags, alias，手机号码mobile。
		//GET /v3/devices/{registration_id}
		
		//更新设备 （设备的别名与标签）,更新当前设备的指定属性，当前支持tags, alias，手机号码mobile。
		//使用短信业务，请结合服务端SMS_MESSAGE字段
		//POST /v3/devices/{registration_id}
		
		//查询标签列表,获取当前应用的所有标签列表。
		//GET /v3/tags/
		
		//判断设备与标签的绑定,查询某个设备是否在 tag 下。
		//GET /v3/tags/{tag_value}/registration_ids/{registration_id}
	}


	/**
	 * 获取请求的命令
	 * 
	 * @param methodName
	 * @return
	 */
	public static String genApiKey(String cmdStr) {
		String key = keyMap.get(cmdStr);
		return key;
	}
	
	/**
	 * 获取请求的命令
	 * 
	 * @param methodName
	 * @return
	 */
	public static String genUrlCmd(String cmdStr) {
		String cmd = apiMap.get(cmdStr);
		return cmd;
	}
	
	public static boolean isTestDev(){
		return false;
	}
}
