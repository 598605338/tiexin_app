package com.linjia.web.thirdService.dada.constants;

import com.linjia.web.thirdService.meituan.exception.ApiSysException;
import com.linjia.web.thirdService.meituan.utils.PropertiesUtil;

public class DaDaLogisticsConfig {
	 //开放平台分配的测试key值
//    public final static String APP_KEY = "dada9fd5737ef7a76da1";
	//正式环境key
	public final static String APP_KEY = "dada9e693672e5b6922b";
    
    private static String urlPrefix = "";

    /**
     * grant_code 授权码
     * access_token 权限token
     * 实际使用的建议放在redis memcache等中间缓存
     * Created by pocket on 16/7/17.
     */
   //申请的access_token的授权码,有效时间为15分钟
   public static int GRANTCODE = 0;
   //access_token的有效时间,单位为秒，默认7天有效期（604800秒） expires_in
   public static String ACCESSTOKEN = "9621817b1d9c79f079003a6ebc424fa8";
   //refresh_token access_token过期情况下，用来刷新access_token值,一般30天的有效期
   public static String REFRESHTOKEN="";
   
   //授权grant_type
   public static String GRANTTYPE="authorization_code";
   
   //刷新grant_type
   public static String REGRANTTYPE="refresh_token";
   
   //scope
   public static String SCOPE="dada_base";
   
   //令牌加密常量字符串
   public static String RIVET="dada";
   
   //请求编码
   public static String CHARSET="UTF-8";
   
   /**
	 * 通过方法名生成url
	 * 
	 * @param methodName
	 * @return
	 */
	public static String geUrlPrefix(){
		try {
			if (urlPrefix.equals("")) {
				String env = PropertiesUtil.getEnvironmentMode();
				if ("0".equals(env)) {
					urlPrefix = "http://public.ga.qa.imdada.cn";
				} else if ("1".equals(env)) {
					urlPrefix = "http://public.imdada.cn";
				} else if ("2".equals(env)) {
					urlPrefix = "http://127.0.0.1:8080/api/v1";
				}
			}
		 }catch (ApiSysException e) {
			e.printStackTrace();
		}
		String url = urlPrefix;
		return url;
	}
   
}
