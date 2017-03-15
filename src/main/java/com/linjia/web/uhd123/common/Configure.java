package com.linjia.web.uhd123.common;

import java.util.HashMap;
import java.util.Map;

import com.linjia.constants.Application;

/** 
 * 地址
 * @author  lixinling: 
 * @date 2016年9月29日 下午2:13:49 
 * @version 1.0 
*/
public class Configure {

	// 租户 请求根地址
	public static String baseUrl = "";
	
	// 租户ID
	public static String tenant_id = "";
	
	// 租户 authorization
	public static String authorization = "";
	
	// 平台商家shop_id
	public static String shop_id_linjia = "";
	
	// 平台商家shop_name
	public static String shop_name_linjia = "";
	
	// 平台platform_id
	public static String platform_id_linjia ="";
	
	// 平台platform_name
	public static String platform_name_linjia ="";
	
	// 平台商家shop_id
	public static String shop_id_baiduwaimai = "";
	
	// 平台商家shop_name
	public static String shop_name_baiduwaimai = "";
	
	// 平台platform_id
	public static String platform_id_baiduwaimai ="";
	
	// 平台platform_name
	public static String platform_name_baiduwaimai ="";
	
	// 平台商家shop_id
	public static String shop_id_meituan = "";
	
	// 平台商家shop_name
	public static String shop_name_meituan = "";
	
	// 平台platform_id
	public static String platform_id_meituan ="";
	
	// 平台platform_name
	public static String platform_name_meituan ="";
	
	// 平台商家shop_id
	public static String shop_id_jingdongdajia = "";
	
	// 平台商家shop_name
	public static String shop_name_jingdongdajia = "";
	
	// 平台platform_id
	public static String platform_id_jingdongdajia ="";
	
	// 平台platform_name
	public static String platform_name_jingdongdajia ="";

	// 测试租户 请求根地址
	public static String TEST_baseUrl = "";
	
	// 测试租户ID
	public static String TEST_tenant_id = "";
	
	// 测试租户 authorization
	public static String TEST_authorization = "";
	
	/**
	 * 取得请求base信息
	 * lixinling 
	 * 2016年11月21日 上午11:33:11
	 */
	public static Map<String,String> getRequestBaseInfo(){
		Map<String,String> infoMap = new HashMap<String,String>();
		if(Application.isTest){
			//测试状态
			infoMap.put("tenant_id", TEST_tenant_id);
			infoMap.put("baseUrl", TEST_baseUrl);
			infoMap.put("authorization", TEST_authorization);
		}else{
			infoMap.put("tenant_id", tenant_id);
			infoMap.put("baseUrl", baseUrl);
			infoMap.put("authorization", authorization);
		}
		return infoMap;
	}

	public static String getTenant_id() {
		return tenant_id;
	}

	public static void setTenant_id(String tenant_id) {
		Configure.tenant_id = tenant_id;
	}
	
	
}
