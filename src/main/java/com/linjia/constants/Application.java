package com.linjia.constants;

import com.linjia.wxpay.common.Configure;

/** 
 * 系统常量类
 * 
 * @author  lixinling: 
 * @date 2016年7月1日 下午2:51:25 
 * @version 1.0 
 */
public class Application {
	
	public static boolean isTest = false;
	
	/**
	 * 系统资源根路径
	 */
	public final static String TEST_SERVER_BASE_PATH = "http://b2c.linjia-cvs.cn";
	
	/**
	 * 系统资源根路径
	 */
	public final static String SERVER_BASE_PATH = "http://www.linjia-cvs.cn";
	
	/**
	 * 系统资源根路径
	 */
	public final static String TEST_R_BASE_PATH = "http://b2c.linjia-cvs.cn/linjia_1";
	
	/**
	 * 系统资源根路径
	 */
	public final static String R_BASE_PATH = "http://www.linjia-cvs.cn/linjia_1";
	
	/**
	 * RedisKeyPrefix:登陆用户信息
	 */
	public final static String USERINFO_PREFIX = "USERINFO_";
	
	/**
	 * RedisKeyExpire:过期时间
	 */
	public final static int USERINFO_EXPIRE = 30*24*60*60;
	
	/**
	 * 微信授权URL
	 */
	public final static String OAUTH2_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Configure.getAppid() + "&redirect_uri=http%3A%2F%2Fwww.linjia-cvs.cn/index/index%2F&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

	// 加密key
	private static final String LINJIA_KEY = "linjia";
	
	//百度物流source_name
	public final static String BD_SOURCE_NAME = "邻里家";
	
	//饿了么邻家APPID
	public final static int ELEME_APPID = 84496616;
	
	public static int order_pdNum=0;

	public static void setTest(boolean isTest) {
		Application.isTest = isTest;
	}
	
	public static String getljKey() {
		return LINJIA_KEY;
	}
	
	public static String getServerBasePath() {
		if(isTest){
			return TEST_SERVER_BASE_PATH;
		}else{
			return SERVER_BASE_PATH;
		}
	}
	
	public static String getRBasePath() {
		if(isTest){
			return TEST_R_BASE_PATH;
		}else{
			return R_BASE_PATH;
		}
	}
	
}
