package com.linjia.web.thirdService.dada.factory;

import java.util.HashMap;
import java.util.Map;

public class LogisticsUrlFactory {

	private static Map<String, String> urlMAP;

	static {
		urlMAP = new HashMap<String, String>();
		// 获取授权码grant_code(GET),参数:app_key=APPKEY&scope=dada_base
		urlMAP.put("getGrantCode", "/oauth/authorize");
		// 获取access_token(GET),参数：grant_type=authorization_code&app_key=APPKEY&grant_code=CODE
		urlMAP.put("getAccessToken", "/oauth/access_token");
		// 获取access_token(GET)，参数:grant_type=refresh_token&app_key=APPKEY&refresh_token=REFRESHTOKEN
		urlMAP.put("reFreshAccessToken", "/oauth/refresh_token");
		// 添加订单(post)
		urlMAP.put("addOrder", "/v1_0/addOrder/");
		// 订单回调(GET)
		// urlMAP.put("ordercallback","callback返回的url");
		// 获取配送员信息(GET)
		urlMAP.put("getDminfo", "/v1_0/getDmInfo");
		// 获取小票信息(GET)
		urlMAP.put("getXiaoPiao", "/v1_0/getInvoicePicture");
		// 获取订单状态信息(GET)
		urlMAP.put("getOrderStatus", "/v1_0/getOrderInfo");
		// 获取取消理由(GET)
		urlMAP.put("getCanceReason", "/v1_0/getCancelReasons");
		// 重新添加订单(POST)
		urlMAP.put("reAddOrder", "/v1_0/reAddOrder/");
		// 取消订单-正式环境(POST)
		urlMAP.put("canCeOrder", "/v2_0/cancelOrder");
		// 追加加订单
		urlMAP.put("reAddNewOrder", "/v1_0/appointNewOrder/");
		// 增加小费(POST)
		urlMAP.put("addXiaoFei", "/v1_0/tips/update/");
		// 获取城市信息列表
		urlMAP.put("getCityList", "/v1_0/getCity");
		// 取消订单(仅在测试环境供调试使用)(GET)
		urlMAP.put("canceOrderTest", "/v1_0/cancelOrder");
		// 接受订单(仅在测试环境供调试使用)(GET)
		urlMAP.put("acceptOrderTest", "/v1_0/acceptOrder");
		// 拒绝订单(仅在测试环境供调试使用)(GET)
		urlMAP.put("refuseOrderTest", "/v1_0/rejectOrder/");
		// 完成取货(仅在测试环境供调试使用)(GET)
		urlMAP.put("fetchOrderTest", "/v1_0/fetchOrder");
		// 订单已送达(仅在测试环境供调试使用)(GET)
		urlMAP.put("finishOrderTest", "/v1_0/finishOrder");
		// 设置access_token过期（仅在测试环境供调试使用）REFRESHACCESS_TOKEN(GET)
		urlMAP.put("setAccessTokentimeoutTest", "/cancelAccessToken/");
		// 刷新access_token过期（仅在测试环境供调试使用）REFRESHACCESS_TOKEN(GET)
		urlMAP.put("setRefreshTokentimeoutTest", "/cancelRefreshToken/");
	}
	
	public static String getCmdUrl(String cmd){
		String cmdUrl=urlMAP.get(cmd);
		return cmdUrl;
	}
}
