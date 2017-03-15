package com.linjia.web.thirdService.jddj.config;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linjia.tools.Util;

import o2o.openplatform.sdk.util.MD5Util;



/** 
 * @author  lixinling: 
 * @date 2017年2月20日 下午5:11:07 
 * @version 1.0 
*/
public class JDDJUtil {
	
	protected static final Logger logger = LoggerFactory.getLogger(Util.class);
	
	/**
	 * 京东到家获取签名
	 * lixinling 
	 * 2017年2月20日 下午5:37:52
	 * @param params
	 * @param appSecret
	 * @return
	 * @throws Exception
	 */
	public static String getSignByMD5(Map params, String appSecret)
	        throws Exception
	    {
			Method method = o2o.openplatform.sdk.util.SignUtils.class.getDeclaredMethod("concatParams",Map.class);
			method.setAccessible(true);
			String sysStr = (String)method.invoke(o2o.openplatform.sdk.util.SignUtils.class, params);
	        StringBuilder resultStr = new StringBuilder("");
	        resultStr.append(appSecret).append(sysStr).append(appSecret);
	        logger.info("resultStr:"+resultStr);
	        return MD5Util.getMD5String(resultStr.toString()).toUpperCase();
	    }
	
	/**
	 * 返回URLDecoder数据
	 * lixinling 
	 * 2017年2月20日 下午5:37:52
	 * @param params
	 * @param appSecret
	 * @return
	 * @throws Exception
	 */
	public static String getURLDecoderUTF8(String word)
			throws Exception
	{
		return URLDecoder.decode(word, "UTF-8");
	}
}
