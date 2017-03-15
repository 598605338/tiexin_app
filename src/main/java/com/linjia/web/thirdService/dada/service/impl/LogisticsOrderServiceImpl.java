package com.linjia.web.thirdService.dada.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.linjia.web.thirdService.dada.constants.DaDaLogisticsConfig;
import com.linjia.web.thirdService.dada.factory.LogisticsUrlFactory;
import com.linjia.web.thirdService.dada.factory.StatusCodeFactory;
import com.linjia.web.thirdService.dada.model.DaDaLogisticsParam;
import com.linjia.web.thirdService.dada.service.LogisticsOrderService;
import com.linjia.web.thirdService.dada.utils.HttpRequestUtil;
import com.linjia.web.thirdService.dada.utils.SignUtil;

@Service
public class LogisticsOrderServiceImpl implements LogisticsOrderService {

	@Override
	public JSONObject addOrder(DaDaLogisticsParam logpm) {
		 HttpRequestUtil httpRequest=HttpRequestUtil.getHttpsRequestSingleton();
		 String url = DaDaLogisticsConfig.geUrlPrefix()+LogisticsUrlFactory.getCmdUrl("addOrder");
	     Map<String, String> param = new HashMap<String, String>();
	     String signature="";
	     param.put("token",DaDaLogisticsConfig.ACCESSTOKEN);
	     int timestamp=(int) (System.currentTimeMillis()/1000);
	     param.put("timestamp",timestamp+"");
	     //生成令牌
	     signature=SignUtil.buildSignature(timestamp, DaDaLogisticsConfig.ACCESSTOKEN, DaDaLogisticsConfig.RIVET);
	     
	     param.put("signature",signature);	
	     param.put("origin_id",logpm.getOrigin_id());	
	     param.put("city_name",logpm.getCity_name());	
	     param.put("city_code",logpm.getCity_code());	
	     param.put("pay_for_supplier_fee",logpm.getPay_for_supplier_fee()+"");
	     param.put("fetch_from_receiver_fee",logpm.getFetch_from_receiver_fee()+"");
	     param.put("deliver_fee",logpm.getDeliver_fee()+"");
	     param.put("tips",logpm.getTips()+"");
	     param.put("create_time",logpm.getCreate_time()+"");
	     param.put("info",logpm.getInfo());
	     param.put("cargo_type",logpm.getCargo_type()+"");	
	     param.put("cargo_weight",logpm.getCargo_weight()+"");	
	     param.put("cargo_price",logpm.getCargo_price()+"");	
	     param.put("cargo_num",logpm.getCargo_num()+"");	
	     param.put("is_prepay",logpm.getIs_prepay()+"");	
	     param.put("expected_fetch_time",logpm.getExpected_fetch_time()+"");	
	     param.put("expected_finish_time",logpm.getExpected_finish_time()+"");	
	     param.put("supplier_id",logpm.getSupplier_id());		
	     param.put("supplier_name",logpm.getSupplier_name());		
	     param.put("supplier_address",logpm.getSupplier_address());	
	     param.put("supplier_phone",logpm.getSupplier_phone());		
	     param.put("supplier_tel",logpm.getSupplier_tel());		
	     param.put("supplier_lat",logpm.getSupplier_lat()+"");		
	     param.put("supplier_lng",logpm.getSupplier_lng()+"");		
	     param.put("invoice_title",logpm.getInvoice_title());		
	     param.put("receiver_name",logpm.getReceiver_name());		
	     param.put("receiver_address",logpm.getReceiver_address());	
	     param.put("receiver_phone",logpm.getReceiver_phone());		
	     param.put("receiver_tel",logpm.getReceiver_tel());		
	     param.put("receiver_lat",logpm.getReceiver_lat()+"");		
	     param.put("receiver_lng",logpm.getReceiver_lng()+"");		
	     param.put("callback",logpm.getCallback());
	     JSONObject jsonObject = httpRequest.sendPost(url, param);
	     if("fail".equals(jsonObject.getString("status"))){
		     int errorCode=jsonObject.getInt("errorCode");
		     String errorMessage=StatusCodeFactory.getMapMessage(errorCode);
		     jsonObject.put("errorMessage", errorMessage);
	     }
		return jsonObject;
	}

	@Override
	public JSONObject getDmInfo(String dm_id,Long orderId) {
		 String order_id=String.valueOf(orderId);
		 HttpRequestUtil httpRequest=HttpRequestUtil.getHttpsRequestSingleton();
		 String url = DaDaLogisticsConfig.geUrlPrefix()+LogisticsUrlFactory.getCmdUrl("getDminfo");
	     Map<String, Object> param = new HashMap<String, Object>();
	     String signature="";
	     int timestamp=(int) (System.currentTimeMillis()/1000);
	     param.put("token",DaDaLogisticsConfig.ACCESSTOKEN);
	     param.put("timestamp",timestamp);
	     //生成令牌
	     signature=SignUtil.buildSignature(timestamp, DaDaLogisticsConfig.ACCESSTOKEN, DaDaLogisticsConfig.RIVET);
	     
	     param.put("signature",signature);	
	     param.put("dm_id",dm_id);	
	     param.put("order_id",order_id);	
	     JSONObject jsonObject = httpRequest.sendGet(url, param);
	     if("fail".equals(jsonObject.getString("status"))){
		     int errorCode=jsonObject.getInt("errorCode");
		     String errorMessage=StatusCodeFactory.getMapMessage(errorCode);
		     jsonObject.put("errorMessage", errorMessage);
	     }
		return jsonObject;
	}

	@Override
	public JSONObject getXiaoPiao(Long orderId) {
		String order_id=String.valueOf(orderId);
		HttpRequestUtil httpRequest=HttpRequestUtil.getHttpsRequestSingleton();
		String url = DaDaLogisticsConfig.geUrlPrefix()+LogisticsUrlFactory.getCmdUrl("getXiaoPiao");
	     Map<String, Object> param = new HashMap<String, Object>();
	     String signature="";
	     int timestamp=(int) (System.currentTimeMillis()/1000);
	     param.put("token",DaDaLogisticsConfig.ACCESSTOKEN);
	     param.put("timestamp",timestamp);
	     //生成令牌
	     signature=SignUtil.buildSignature(timestamp, DaDaLogisticsConfig.ACCESSTOKEN, DaDaLogisticsConfig.RIVET);
	     
	     param.put("signature",signature);
	     param.put("order_id",order_id);
	     JSONObject jsonObject = httpRequest.sendGet(url, param);
	     if("fail".equals(jsonObject.getString("status"))){
		     int errorCode=jsonObject.getInt("errorCode");
		     String errorMessage=StatusCodeFactory.getMapMessage(errorCode);
		     jsonObject.put("errorMessage", errorMessage);
	     }
		return jsonObject;
	}

	@Override
	public JSONObject getOrderStatus(Long orderId) {
		String order_id=String.valueOf(orderId);
		HttpRequestUtil httpRequest=HttpRequestUtil.getHttpsRequestSingleton();
		String url = DaDaLogisticsConfig.geUrlPrefix()+LogisticsUrlFactory.getCmdUrl("getOrderStatus");
	     Map<String, Object> param = new HashMap<String, Object>();
	     String signature="";
	     int timestamp=(int) (System.currentTimeMillis()/1000);
	     param.put("token",DaDaLogisticsConfig.ACCESSTOKEN);
	     param.put("timestamp",timestamp);
	     //生成令牌
	     signature=SignUtil.buildSignature(timestamp, DaDaLogisticsConfig.ACCESSTOKEN, DaDaLogisticsConfig.RIVET);
	     
	     param.put("signature",signature);
	     param.put("order_id",order_id);
	     JSONObject jsonObject = httpRequest.sendGet(url, param);
	     if("fail".equals(jsonObject.getString("status"))){
		     int errorCode=jsonObject.getInt("errorCode");
		     String errorMessage=StatusCodeFactory.getMapMessage(errorCode);
		     jsonObject.put("errorMessage", errorMessage);
	     }
		return jsonObject;
	}

	@Override
	public JSONObject getCanceReason() {
		HttpRequestUtil httpRequest=HttpRequestUtil.getHttpsRequestSingleton();
		String url = DaDaLogisticsConfig.geUrlPrefix()+LogisticsUrlFactory.getCmdUrl("getCanceReason");
		 Map<String, Object> param = new HashMap<String, Object>();
		 JSONObject jsonObject = httpRequest.sendGet(url, param);
	     if("fail".equals(jsonObject.getString("status"))){
		     int errorCode=jsonObject.getInt("errorCode");
		     String errorMessage=StatusCodeFactory.getMapMessage(errorCode);
		     jsonObject.put("errorMessage", errorMessage);
	     }
		return jsonObject;
	}

	@Override
	public JSONObject rePublisOrder(DaDaLogisticsParam logpm) {
		HttpRequestUtil httpRequest=HttpRequestUtil.getHttpsRequestSingleton();
		String url = DaDaLogisticsConfig.geUrlPrefix()+LogisticsUrlFactory.getCmdUrl("reAddNewOrder");
	     Map<String, String> param = new HashMap<String, String>();
	     String signature="";
	     param.put("token",DaDaLogisticsConfig.ACCESSTOKEN);
	     int timestamp=(int) (System.currentTimeMillis()/1000);
	     param.put("timestamp",timestamp+"");
	     //生成令牌
	     signature=SignUtil.buildSignature(timestamp, DaDaLogisticsConfig.ACCESSTOKEN, DaDaLogisticsConfig.RIVET);
	     
	     param.put("signature",signature);	
	     param.put("origin_id",logpm.getOrigin_id());	
	     param.put("city_name",logpm.getCity_name());	
	     param.put("city_code",logpm.getCity_code());	
	     param.put("pay_for_supplier_fee",logpm.getPay_for_supplier_fee()+"");
	     param.put("fetch_from_receiver_fee",logpm.getFetch_from_receiver_fee()+"");
	     param.put("deliver_fee",logpm.getDeliver_fee()+"");
	     param.put("create_time",logpm.getCreate_time()+"");
	     param.put("info",logpm.getInfo());
	     param.put("cargo_type",logpm.getCargo_type()+"");	
	     param.put("cargo_weight",logpm.getCargo_weight()+"");	
	     param.put("cargo_price",logpm.getCargo_price()+"");	
	     param.put("cargo_num",logpm.getCargo_num()+"");	
	     param.put("is_prepay",logpm.getIs_prepay()+"");	
	     param.put("expected_fetch_time",logpm.getExpected_fetch_time()+"");	
	     param.put("expected_finish_time",logpm.getExpected_finish_time()+"");	
	     param.put("supplier_id",logpm.getSupplier_id());		
	     param.put("supplier_name",logpm.getSupplier_name());		
	     param.put("supplier_address",logpm.getSupplier_address());	
	     param.put("supplier_phone",logpm.getSupplier_phone());		
	     param.put("supplier_tel",logpm.getSupplier_tel());		
	     param.put("supplier_lat",logpm.getSupplier_lat()+"");		
	     param.put("supplier_lng",logpm.getSupplier_lng()+"");		
	     param.put("invoice_title",logpm.getInvoice_title());		
	     param.put("receiver_name",logpm.getReceiver_name());		
	     param.put("receiver_address",logpm.getReceiver_address());	
	     param.put("receiver_phone",logpm.getReceiver_phone());		
	     param.put("receiver_tel",logpm.getReceiver_tel());		
	     param.put("receiver_lat",logpm.getReceiver_lat()+"");		
	     param.put("receiver_lng",logpm.getReceiver_lng()+"");		
	     param.put("callback",logpm.getCallback());
	     JSONObject jsonObject = httpRequest.sendPost(url, param);
	     if("fail".equals(jsonObject.getString("status"))){
		     int errorCode=jsonObject.getInt("errorCode");
		     String errorMessage=StatusCodeFactory.getMapMessage(errorCode);
		     jsonObject.put("errorMessage", errorMessage);
	     }
		return jsonObject;
	}

	@Override
	public JSONObject canceOrder(Long orderId,String cancel_reason_id,String cancel_reason) {
		String order_id=String.valueOf(orderId);
		HttpRequestUtil httpRequest=HttpRequestUtil.getHttpsRequestSingleton();
		String url = DaDaLogisticsConfig.geUrlPrefix()+LogisticsUrlFactory.getCmdUrl("canCeOrder");
	     Map<String, Object> param = new HashMap<String, Object>();
	     String signature="";
	     int timestamp=(int)(System.currentTimeMillis()/1000);
	     param.put("token",DaDaLogisticsConfig.ACCESSTOKEN);
	     param.put("timestamp",timestamp+"");
	     //生成令牌
	     signature=SignUtil.buildSignature(timestamp, DaDaLogisticsConfig.ACCESSTOKEN, DaDaLogisticsConfig.RIVET);
	     
	     param.put("signature",signature);
	     param.put("order_id",order_id);
	     param.put("cancel_reason_id",cancel_reason_id);
	     param.put("cancel_reason",cancel_reason);
	     JSONObject jsonObject = httpRequest.sendGet(url, param);
	     if("fail".equals(jsonObject.getString("status"))){
		     int errorCode=jsonObject.getInt("errorCode");
		     String errorMessage=StatusCodeFactory.getMapMessage(errorCode);
		     jsonObject.put("errorMessage", errorMessage);
	     }
		return jsonObject;
	}

	@Override
	public JSONObject reAddOrder(DaDaLogisticsParam logpm) {
		 HttpRequestUtil httpRequest=HttpRequestUtil.getHttpsRequestSingleton();
		 String url = DaDaLogisticsConfig.geUrlPrefix()+LogisticsUrlFactory.getCmdUrl("reAddOrder");
	     Map<String, String> param = new HashMap<String, String>();
	     String signature="";
	     param.put("token",DaDaLogisticsConfig.ACCESSTOKEN);
	     int timestamp=(int) (System.currentTimeMillis()/1000);
	     param.put("timestamp",timestamp+"");
	     //生成令牌
	     signature=SignUtil.buildSignature(timestamp, DaDaLogisticsConfig.ACCESSTOKEN, DaDaLogisticsConfig.RIVET);
	     
	     param.put("signature",signature);	
	     param.put("origin_id",logpm.getOrigin_id());	
	     param.put("city_name",logpm.getCity_name());	
	     param.put("city_code",logpm.getCity_code());	
	     param.put("ad_code",logpm.getAd_code());
	     param.put("pay_for_supplier_fee",logpm.getPay_for_supplier_fee()+"");
	     param.put("fetch_from_receiver_fee",logpm.getFetch_from_receiver_fee()+"");
	     param.put("deliver_fee",logpm.getDeliver_fee()+"");
	     param.put("create_time",logpm.getCreate_time()+"");
	     param.put("info",logpm.getInfo());
	     param.put("cargo_type",logpm.getCargo_type()+"");	
	     param.put("cargo_weight",logpm.getCargo_weight()+"");	
	     param.put("cargo_price",logpm.getCargo_price()+"");	
	     param.put("cargo_num",logpm.getCargo_num()+"");	
	     param.put("is_prepay",logpm.getIs_prepay()+"");	
	     param.put("expected_fetch_time",logpm.getExpected_fetch_time()+"");	
	     param.put("expected_finish_time",logpm.getExpected_finish_time()+"");	
	     param.put("supplier_name",logpm.getSupplier_name());		
	     param.put("supplier_address",logpm.getSupplier_address());	
	     param.put("supplier_phone",logpm.getSupplier_phone());		
	     param.put("supplier_tel",logpm.getSupplier_tel());		
	     param.put("supplier_lat",logpm.getSupplier_lat()+"");		
	     param.put("supplier_lng",logpm.getSupplier_lng()+"");		
	     param.put("invoice_title",logpm.getInvoice_title());		
	     param.put("receiver_name",logpm.getReceiver_name());		
	     param.put("receiver_address",logpm.getReceiver_address());	
	     param.put("receiver_phone",logpm.getReceiver_phone());		
	     param.put("receiver_tel",logpm.getReceiver_tel());		
	     param.put("receiver_lat",logpm.getReceiver_lat()+"");		
	     param.put("receiver_lng",logpm.getReceiver_lng()+"");		
	     param.put("callback",logpm.getCallback());
	     param.put("transporter_phone",logpm.getTransporter_phone()+"");
	     JSONObject jsonObject = httpRequest.sendPost(url, param);
	     if("fail".equals(jsonObject.getString("status"))){
		     int errorCode=jsonObject.getInt("errorCode");
		     String errorMessage=StatusCodeFactory.getMapMessage(errorCode);
		     jsonObject.put("errorMessage", errorMessage);
	     }
		return jsonObject;
	}

	@Override
	public JSONObject addXiaoFei(Long orderId,String origin_id,String tips,String cityCode) {
		String order_id=String.valueOf(orderId);
		HttpRequestUtil httpRequest=HttpRequestUtil.getHttpsRequestSingleton();
		String url = DaDaLogisticsConfig.geUrlPrefix()+LogisticsUrlFactory.getCmdUrl("addXiaoFei");
	     Map<String, String> param = new HashMap<String, String>();
	     String signature="";
	     int timestamp=(int) (System.currentTimeMillis()/1000);
	     param.put("token",DaDaLogisticsConfig.ACCESSTOKEN);
	     param.put("timestamp",timestamp+"");
	     //生成令牌
	     signature=SignUtil.buildSignature(timestamp, DaDaLogisticsConfig.ACCESSTOKEN, DaDaLogisticsConfig.RIVET);
	     
	     param.put("signature",signature);
	     param.put("order_id",order_id);
	     param.put("origin_id",origin_id);
	     param.put("tips",tips);
	     param.put("city_code",cityCode);
	     JSONObject jsonObject = httpRequest.sendPost(url, param);
	     if("fail".equals(jsonObject.getString("status"))){
		     int errorCode=jsonObject.getInt("errorCode");
		     String errorMessage=StatusCodeFactory.getMapMessage(errorCode);
		     jsonObject.put("errorMessage", errorMessage);
	     }
		return jsonObject;
	}

	@Override
	public JSONObject getCityList() {
		HttpRequestUtil httpRequest=HttpRequestUtil.getHttpsRequestSingleton();
		String url = DaDaLogisticsConfig.geUrlPrefix()+LogisticsUrlFactory.getCmdUrl("getCityList");
	     Map<String, Object> param = new HashMap<String, Object>();
	     param.put("token",DaDaLogisticsConfig.ACCESSTOKEN);
	     JSONObject jsonObject = httpRequest.sendGet(url, param);
	     if("fail".equals(jsonObject.getString("status"))){
		     int errorCode=jsonObject.getInt("errorCode");
		     String errorMessage=StatusCodeFactory.getMapMessage(errorCode);
		     jsonObject.put("errorMessage", errorMessage);
	     }
		return jsonObject;
	}

	@Override
	public JSONObject canceOrderTest(Long orderId,String reason) {
		String order_id=String.valueOf(orderId);
		HttpRequestUtil httpRequest=HttpRequestUtil.getHttpsRequestSingleton();
		String url = DaDaLogisticsConfig.geUrlPrefix()+LogisticsUrlFactory.getCmdUrl("canceOrderTest");
	     Map<String, Object> param = new HashMap<String, Object>();
	     String signature="";
	     int timestamp=(int) (System.currentTimeMillis()/1000);
	     param.put("token",DaDaLogisticsConfig.ACCESSTOKEN);
	     param.put("timestamp",timestamp);
	     //生成令牌
	     signature=SignUtil.buildSignature(timestamp, DaDaLogisticsConfig.ACCESSTOKEN, DaDaLogisticsConfig.RIVET);
	     
	     param.put("signature",signature);
	     param.put("order_id",order_id);
	     param.put("reason",reason);
	     JSONObject jsonObject = httpRequest.sendGet(url, param);
	     if("fail".equals(jsonObject.getString("status"))){
		     int errorCode=jsonObject.getInt("errorCode");
		     String errorMessage=StatusCodeFactory.getMapMessage(errorCode);
		     jsonObject.put("errorMessage", errorMessage);
	     }
		return jsonObject;
	}

	@Override
	public JSONObject acceptOrderTest(Long orderId) {
		String order_id=String.valueOf(orderId);
		HttpRequestUtil httpRequest=HttpRequestUtil.getHttpsRequestSingleton();
		String url = DaDaLogisticsConfig.geUrlPrefix()+LogisticsUrlFactory.getCmdUrl("acceptOrderTest");
	     Map<String, Object> param = new HashMap<String, Object>();
	     String signature="";
	     int timestamp=(int) (System.currentTimeMillis()/1000);
	     param.put("token",DaDaLogisticsConfig.ACCESSTOKEN);
	     param.put("timestamp",timestamp);
	     //生成令牌
	     signature=SignUtil.buildSignature(timestamp, DaDaLogisticsConfig.ACCESSTOKEN, DaDaLogisticsConfig.RIVET);
	     
	     param.put("signature",signature);
	     param.put("order_id",order_id);
	     JSONObject jsonObject = httpRequest.sendGet(url, param);
	     if("fail".equals(jsonObject.getString("status"))){
		     int errorCode=jsonObject.getInt("errorCode");
		     String errorMessage=StatusCodeFactory.getMapMessage(errorCode);
		     jsonObject.put("errorMessage", errorMessage);
	     }
		return jsonObject;
	}

	@Override
	public JSONObject refuseOrderTest(Long orderId) {
		String order_id=String.valueOf(orderId);
		HttpRequestUtil httpRequest=HttpRequestUtil.getHttpsRequestSingleton();
		String url = DaDaLogisticsConfig.geUrlPrefix()+LogisticsUrlFactory.getCmdUrl("refuseOrderTest");
	     Map<String, Object> param = new HashMap<String, Object>();
	     String signature="";
	     int timestamp=(int)(System.currentTimeMillis()/1000);
	     param.put("token",DaDaLogisticsConfig.ACCESSTOKEN);
	     param.put("timestamp",timestamp);
	     //生成令牌
	     signature=SignUtil.buildSignature(timestamp, DaDaLogisticsConfig.ACCESSTOKEN, DaDaLogisticsConfig.RIVET);
	     
	     param.put("signature",signature);
	     param.put("order_id",order_id);
	     JSONObject jsonObject = httpRequest.sendGet(url, param);
	     if("fail".equals(jsonObject.getString("status"))){
		     int errorCode=jsonObject.getInt("errorCode");
		     String errorMessage=StatusCodeFactory.getMapMessage(errorCode);
		     jsonObject.put("errorMessage", errorMessage);
	     }
		return jsonObject;
	}

	@Override
	public JSONObject fetchOrderTest(Long orderId) {
		String order_id=String.valueOf(orderId);
		HttpRequestUtil httpRequest=HttpRequestUtil.getHttpsRequestSingleton();
		String url = DaDaLogisticsConfig.geUrlPrefix()+LogisticsUrlFactory.getCmdUrl("fetchOrderTest");
	     Map<String, Object> param = new HashMap<String, Object>();
	     String signature="";
	     int timestamp=(int) (System.currentTimeMillis()/1000);
	     param.put("token",DaDaLogisticsConfig.ACCESSTOKEN);
	     param.put("timestamp",timestamp);
	     //生成令牌
	     signature=SignUtil.buildSignature(timestamp, DaDaLogisticsConfig.ACCESSTOKEN, DaDaLogisticsConfig.RIVET);
	     
	     param.put("signature",signature);
	     param.put("order_id",order_id);
	     JSONObject jsonObject = httpRequest.sendGet(url, param);
	     if("fail".equals(jsonObject.getString("status"))){
		     int errorCode=jsonObject.getInt("errorCode");
		     String errorMessage=StatusCodeFactory.getMapMessage(errorCode);
		     jsonObject.put("errorMessage", errorMessage);
	     }
		return jsonObject;
	}

	@Override
	public JSONObject finishOrderTest(Long orderId) {
		String order_id=String.valueOf(orderId);
		HttpRequestUtil httpRequest=HttpRequestUtil.getHttpsRequestSingleton();
		String url = DaDaLogisticsConfig.geUrlPrefix()+LogisticsUrlFactory.getCmdUrl("finishOrderTest");
	     Map<String, Object> param = new HashMap<String, Object>();
	     String signature="";
	     int timestamp=(int) (System.currentTimeMillis()/1000);
	     param.put("token",DaDaLogisticsConfig.ACCESSTOKEN);
	     param.put("timestamp",timestamp);
	     //生成令牌
	     signature=SignUtil.buildSignature(timestamp, DaDaLogisticsConfig.ACCESSTOKEN, DaDaLogisticsConfig.RIVET);
	     
	     param.put("signature",signature);
	     param.put("order_id",order_id);
	     JSONObject jsonObject = httpRequest.sendGet(url, param);
	     if("fail".equals(jsonObject.getString("status"))){
		     int errorCode=jsonObject.getInt("errorCode");
		     String errorMessage=StatusCodeFactory.getMapMessage(errorCode);
		     jsonObject.put("errorMessage", errorMessage);
	     }
		return jsonObject;
	}

}
