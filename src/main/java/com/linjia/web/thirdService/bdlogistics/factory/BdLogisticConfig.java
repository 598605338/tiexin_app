package com.linjia.web.thirdService.bdlogistics.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linjia.web.thirdService.meituan.exception.ApiSysException;
import com.linjia.web.thirdService.meituan.utils.PropertiesUtil;

public class BdLogisticConfig {

	private static String urlPrefix = "";
	private static Map<String, String> urlMap;
	private static Map<Integer, String> errorCodeMap;
	private static Map<String, String> appMap;
	private static List<Map<String,Object>> bdLogisCancelList;

	static {
		appMap = new HashMap<String, String>();
		urlMap = new HashMap<String, String>();
		errorCodeMap = new HashMap<Integer, String>();
		bdLogisCancelList = new ArrayList<Map<String,Object>>();
		
		appMap.put("app_id", "");
		appMap.put("app_key", "");
		
		// 接收第三方平台新订单 
		urlMap.put("createOrder", "/api/createorder");
		// 取消订单
		urlMap.put("cancelOrder", "/api/cancelorder ");
		// 查询订单是否配送范围内
		urlMap.put("queryIfSend", "/api/usershopregion ");
		
		errorCodeMap.put(110002,"请求参数错误");
		errorCodeMap.put(110040," API不存在");
		errorCodeMap.put(110041," API临时关闭");
		errorCodeMap.put(200002," APP不存在");
		errorCodeMap.put(200003," 签名错误,请确保sign值的生成规则");
		errorCodeMap.put(200004," push_time错误。请确保push_time值的合理性,开放物流平台目前将push_time的时效限制在收到请求时刻的前3分钟");
		errorCodeMap.put(200010," 客户不存在,请要求百度物流创建客户");
		errorCodeMap.put(200020," 店铺不存在,请要求百度物流创建店铺");
		errorCodeMap.put(200100," 订单菜品详情字段异常。");
		errorCodeMap.put(200101,"订单不存在");
		errorCodeMap.put(200102," 本地域配送繁忙，暂停新订单");
		errorCodeMap.put(200103," 订单无法取消(已完成订单)");
		errorCodeMap.put(200105," 用户超出配送范围,请确保用户坐标的正确性已经确认用户坐标在百度物流的配送范围内");

		
		Map<String,Object> map1=new HashMap<String, Object>();
		map1.put("id", 1);
		map1.put("info","用户取消");
		
		Map<String,Object> map2=new HashMap<String, Object>();
		map1.put("id", 2);
		map1.put("info","商户取消");
		
		Map<String,Object> map3=new HashMap<String, Object>();
		map1.put("id", 3);
		map1.put("info",":重复订单");
		
		Map<String,Object> map4=new HashMap<String, Object>();
		map1.put("id", 4);
		map1.put("info",":配送超时");
		
		Map<String,Object> map5=new HashMap<String, Object>();
		map1.put("id", 99);
		map1.put("info","其他");
		bdLogisCancelList.add(map1);
		bdLogisCancelList.add(map2);
		bdLogisCancelList.add(map3);
		bdLogisCancelList.add(map4);
		bdLogisCancelList.add(map5);
	}

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
					urlPrefix = "http://.61.30.232:8186";
				} else if ("1".equals(env)) {
					urlPrefix = "http://wuliu.baidu.com";
				} else if ("2".equals(env)) {
					urlPrefix = "http://.0.0.1:8080/api/v1";
				}
			}
		 }catch (ApiSysException e) {
			e.printStackTrace();
		}
		String url = urlPrefix;
		return url;
	}

	/**
	 * 获取请求的错误信息
	 * 
	 * @param methodName
	 * @return
	 */
	public static String genErrorMsg(Integer errorCode) {
		String errorMsg = errorCodeMap.get(errorCode);
		return errorMsg;
	}

	/**
	 * 获取请求的url
	 * 
	 * @param methodName
	 * @return
	 */
	public static String getReqUrl(String cmdStr) {
		String url = geUrlPrefix()+urlMap.get(cmdStr);
		return url;
	}
	
	/**
	 * 获取app参数值
	 * 
	 * @param methodName
	 * @return
	 */
	public static String getAppConfig(String cmdStr) {
		String url = appMap.get(cmdStr);
		return url;
	}
	
	 /**
     * 百度物流返回取消原因map
     */
    public static List<Map<String,Object>> getBdLogisCancelReason(){
    	return bdLogisCancelList;
    }
}
