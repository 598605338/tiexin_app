package com.linjia.web.thirdService.jddj.config;

import com.linjia.constants.Application;

/** 
 * 京东到家接口配置信息
 * @author  lixinling: 
 * @date 2017年1月10日 上午11:46:35 
 * @version 1.0 
*/
public class JDDJConfigure {
	/** 应用的app_key* */
	private static String app_key = "";
	/** 应用的app_Secret* */
	private static String app_Secret = "";
	/** 采用OAuth授权方式为必填参数* */
	private static String token = "";
	/** 暂时只支持json* */
	private static String format = "json";
	/** API协议版本，可选值:1.0* */
	private static String v = "1.0";
	
	/** 0表示成功 其他均为失败* */
	public static String code_SUCCESS = "0";
	
	/** 结果描述* */
	public static String MSG_SUCCESS = "操作成功";
	
	/** 0表示成功 其他均为失败* */
	public static String code_FAIL = "-1";
	
	/** 结果描述* */
	public static String MSG_FAIL = "操作失败";
	
	/**************到家接口调用地址****************/	
	
	/** https调用正式API入口地址* */
	private static String baseURI="https://openo2o.jd.com/djapi/";
	
	/** https调用测式API入口地址* */
	private static String Test_baseURI="https://openo2o.jd.com/mockapi/";
	
	/** 新版订单查询接口* */
	public static String orderQuery="order/es/query";
	
	/** 商家确认接单接口(若门店接单模式为“手动接单”模式，商家需要调该接口，确认或取消接单接口)* */
	public static String orderAcceptOperate="ocs/orderAcceptOperate";
	
	/** 拣货完成且众包配送接口(京东到家配送订单模式下，商家拣货完成时需要发货调用此接口，召唤达达配送员)* */
	public static String orderJDZBDelivery="bm/open/api/order/OrderJDZBDelivery";
	
	/** 订单取消申请确认接口(用户发起订单取消申请，商家侧确认取消接口)* */
	public static String orderCancelOperate="ocs/orderCancelOperate";
	
	/** 商家确认收到拒收退回（或取消）的商品接口* */
	public static String confirmReceiveGoods="order/confirmReceiveGoods";
	
	/** 催配送员抢单接口* */
	public static String urgeDispatching="bm/urgeDispatching";
	
	/** 催配送员抢单接口* */
	public static String adjustOrder="orderAdjust/adjustOrder";
	
	/**************到家接口调用地址****************/
	
	/**************到家订单状态****************/
	public interface JDDJ_ORDER_STATUS{
		//20010:锁定，20020:订单取消，20040:超时未支付系统取消，31000:等待付款，31020:已付款，
		//41000:待处理，32000:等待出库，33040:配送中，33060:已妥投 34000:京东已收款，90000:订单完成；
		
		/** 锁定*/
        int LOCK = 20010;
		/** 订单取消*/
        int CANCEL = 20020;
		/** 已付款*/
        int PAYED = 31020;
		/** 待处理*/
        int UNHANDLE = 41000;
		/** 等待出库*/
        int WAIT_SEND = 32000;
		/** 配送中*/
        int SENDDING = 33040;
		/** 已妥投*/
        int DELIVERY = 33060;
		/** 订单完成*/
        int FINISH = 90000;
		/** 等待商家确认*/
        int WAIT_CONFIRM = 20030;
	}
	/**************到家订单状态****************/
	
	
	public static String getApp_key() {
		return app_key;
	}
	public static void setApp_key(String app_key) {
		JDDJConfigure.app_key = app_key;
	}
	public static String getToken() {
		return token;
	}
	public static void setToken(String token) {
		JDDJConfigure.token = token;
	}
	public static String getFormat() {
		return format;
	}
	public static void setFormat(String format) {
		JDDJConfigure.format = format;
	}
	public static String getV() {
		return v;
	}
	public static void setV(String v) {
		JDDJConfigure.v = v;
	}
	
	public static String getApp_Secret() {
		return app_Secret;
	}
	public static void setApp_Secret(String app_Secret) {
		JDDJConfigure.app_Secret = app_Secret;
	}
	public static String getBaseURI() {
		if(Application.isTest){
			return Test_baseURI;
		}
		return baseURI;
	}
	public static void setBaseURI(String baseURI) {
		JDDJConfigure.baseURI = baseURI;
	}
	
	/**
	 * 订单状态
	 * 
	 * @author lxl
	 * 
	 */
	public interface ORDER_STATUS{
		/** 锁定*/
        int LOCK = 20010;
		/** 订单取消*/
        int CANCEL = 20020;
		/** 超时未支付系统取消*/
        int TIMEOUT_CANCEL = 20040;
		/** 等待付款*/
        int UN_PAY = 31000;
		/** 已付款*/
        int PAYED = 31020;
		/** 已接单*/
        int ACCEPT = 41010;
		/** 待处理*/
        int UN_HANDLE = 41000;
		/** 等待出库*/
        int UN_SEND = 32000;
		/** 配送中*/
        int SENDDING = 33040;
		/** 已妥投*/
        int SENDED = 33060;
		/** 京东已收款*/
        int JD_RECEIVED_MONDY = 34000;
		/** 订单完成*/
        int SUCCESSED = 90000;
	}
}
