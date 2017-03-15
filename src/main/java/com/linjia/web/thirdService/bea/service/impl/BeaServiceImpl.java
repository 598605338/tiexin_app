package com.linjia.web.thirdService.bea.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.linjia.web.thirdService.bea.config.ElemeOpenConfig;
import com.linjia.web.thirdService.bea.config.RequestConstant;
import com.linjia.web.thirdService.bea.model.ElemeResData;
import com.linjia.web.thirdService.bea.request.CancelOrderRequest;
import com.linjia.web.thirdService.bea.request.ElemeCreateOrderRequest;
import com.linjia.web.thirdService.bea.request.ElemeCreateOrderRequest.ElemeCreateRequestData;
import com.linjia.web.thirdService.bea.request.ElemeQueryOrderRequest;
import com.linjia.web.thirdService.bea.service.BeaService;
import com.linjia.web.thirdService.bea.sign.OpenSignHelper;
import com.linjia.web.thirdService.bea.util.HttpClient;
import com.linjia.web.thirdService.bea.util.JsonUtils;
import com.linjia.web.thirdService.bea.util.RandomUtils;

@Service
public class BeaServiceImpl implements BeaService{

	@Override
	public ElemeResData beaSend(ElemeCreateRequestData data) {
		ElemeResData reqData=null;
		try {
			String url=ElemeOpenConfig.API_URL+RequestConstant.orderCreate;
			//获取token
			String appId=ElemeOpenConfig.appId;
			int salt=RandomUtils.getInstance().generateValue(1000,10000);
			String access_token=ElemeOpenConfig.ACCESSTOKEN;
			ElemeCreateOrderRequest eor=new ElemeCreateOrderRequest();
			eor.setData(data);
			eor.setApp_id(appId);
			eor.setSalt(salt);
			//生成签名
			Map<String, Object> sigStr=new LinkedHashMap<String, Object>();
			sigStr.put("app_id", appId);
			sigStr.put("access_token",access_token);
			sigStr.put("data",eor.getData());
			sigStr.put("salt", salt);
			String sign=OpenSignHelper.generateBusinessSign(sigStr);
			eor.setSignature(sign);
			String requestJson = JsonUtils.getInstance().objectToJson(eor);
			String req=HttpClient.postBody(url,requestJson);
			reqData=ElemeOpenConfig.reqStrToObj(req);
			//token获取失败重新设置发送请求
			if(reqData!=null&&("40004".equals(reqData.getCode())||"40002".equals(reqData.getCode()))){
				boolean refreshToken=ElemeOpenConfig.refreshToken(salt);
				if(refreshToken){
					//修改token重新请求
					String access_token1=ElemeOpenConfig.ACCESSTOKEN;
					sigStr.put("access_token",access_token1);
					String sign1=OpenSignHelper.generateBusinessSign(sigStr);
					eor.setSignature(sign1);
					requestJson = JsonUtils.getInstance().objectToJson(eor);
					req=HttpClient.postBody(url,requestJson);
					reqData=ElemeOpenConfig.reqStrToObj(req);
				}else{
					System.out.println("access_token刷新错误！");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return reqData;
		}
		return reqData;
	}

	@Override
	public ElemeResData nextDaySend(ElemeCreateRequestData data) {
		ElemeResData reqData=null;
		try {
			String url=ElemeOpenConfig.API_URL+RequestConstant.orderCreate;
			ElemeCreateOrderRequest eor=new ElemeCreateOrderRequest();
			//获取token
			String appId=ElemeOpenConfig.appId;
			int salt=RandomUtils.getInstance().generateValue(1000,10000);
			String access_token=ElemeOpenConfig.ACCESSTOKEN;
//			ElemeCreateOrderRequest.ElemeCreateRequestData data=OrderToLogisOrder.ljOrderToEleme(order,list);
			data.setTransport_info(null);
			data.setOrder_type(2);
			eor.setApp_id(appId);
			eor.setSalt(salt);
			eor.setData(data);
			//生成签名
			Map<String, Object> sigStr=new LinkedHashMap<String, Object>();
			sigStr.put("app_id", appId);
			sigStr.put("access_token",access_token);
			sigStr.put("data",eor.getData());
			sigStr.put("salt", salt);
			String sign=OpenSignHelper.generateBusinessSign(sigStr);
			eor.setSignature(sign);
			String requestJson = JsonUtils.getInstance().objectToJson(eor);
			String req=HttpClient.postBody(url,requestJson);
			reqData=ElemeOpenConfig.reqStrToObj(req);
			//token获取失败重新设置发送请求
			if(reqData!=null&&("40004".equals(reqData.getCode())||"40002".equals(reqData.getCode()))){
				boolean refreshToken=ElemeOpenConfig.refreshToken(salt);
				if(refreshToken){
					//修改token重新请求
					String access_token1=ElemeOpenConfig.ACCESSTOKEN;
					sigStr.put("access_token",access_token1);
					String sign1=OpenSignHelper.generateBusinessSign(sigStr);
					eor.setSignature(sign1);
					requestJson = JsonUtils.getInstance().objectToJson(eor);
					req=HttpClient.postBody(url,requestJson);
					reqData=ElemeOpenConfig.reqStrToObj(req);
				}else{
					System.out.println("access_token刷新错误！");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return reqData;
		}
		return reqData;
	}

	@Override
	public ElemeResData queryStatus(String orderId) {
		ElemeResData reqData=null;
		try {
			String url=ElemeOpenConfig.API_URL+RequestConstant.orderQuery;
			//获取token
			String appId=ElemeOpenConfig.appId;
			int salt=RandomUtils.getInstance().generateValue(1000,10000);
			String access_token=ElemeOpenConfig.ACCESSTOKEN;
			
			ElemeQueryOrderRequest eoo=new ElemeQueryOrderRequest();
			ElemeQueryOrderRequest.ElemeQueryRequestData data=new ElemeQueryOrderRequest.ElemeQueryRequestData();
			data.setPartner_order_code(orderId);
			eoo.setData(data);
			eoo.setApp_id(appId);
			eoo.setSalt(salt);
			
			//生成签名
			Map<String, Object> sigStr=new LinkedHashMap<String, Object>();
			sigStr.put("app_id", appId);
			sigStr.put("access_token",access_token);
			sigStr.put("data",eoo.getData());
			sigStr.put("salt", salt);
			String sign=OpenSignHelper.generateBusinessSign(sigStr);
			eoo.setSignature(sign);
			
			String requestJson = JsonUtils.getInstance().objectToJson(eoo);
			String req=HttpClient.postBody(url,requestJson);
			reqData=ElemeOpenConfig.reqStrToObj(req);
			//token获取失败重新设置发送请求
			if(reqData!=null&&("40004".equals(reqData.getCode())||"40002".equals(reqData.getCode()))){
				boolean refreshToken=ElemeOpenConfig.refreshToken(salt);
				if(refreshToken){
					//修改token重新请求
					String access_token1=ElemeOpenConfig.ACCESSTOKEN;
					sigStr.put("access_token",access_token1);
					String sign1=OpenSignHelper.generateBusinessSign(sigStr);
					eoo.setSignature(sign1);
					requestJson = JsonUtils.getInstance().objectToJson(eoo);
					req=HttpClient.postBody(url,requestJson);
					reqData=ElemeOpenConfig.reqStrToObj(req);
				}else{
					System.out.println("access_token刷新错误！");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return reqData;
		}
		return reqData;
	}

	@Override
	public ElemeResData cancelBeaOrder(String orderId, Integer reason_code,String cancel_reason) {
		ElemeResData reqData=null;
		try {
			String url=ElemeOpenConfig.API_URL+RequestConstant.orderCancel;
			//获取token
			String appId=ElemeOpenConfig.appId;
			int salt=RandomUtils.getInstance().generateValue(1000,10000);
			String access_token=ElemeOpenConfig.ACCESSTOKEN;
			CancelOrderRequest.CancelOrderRequstData data=new CancelOrderRequest.CancelOrderRequstData();
			data.setPartner_order_code(orderId+"");
			data.setOrder_cancel_reason_code(reason_code);
			data.setOrder_cancel_description(cancel_reason);
			data.setOrder_cancel_time((new Date()).getTime());
			
			CancelOrderRequest eod=new CancelOrderRequest();
			eod.setApp_id(appId);
			eod.setSalt(salt);
			eod.setData(data);
//			data.setOrder_cancel_notify_url("");
			//生成签名
			Map<String, Object> sigStr=new LinkedHashMap<String, Object>();
			sigStr.put("app_id", appId);
			sigStr.put("access_token",access_token);
			sigStr.put("data",eod.getData());
			sigStr.put("salt", salt);
			String sign=OpenSignHelper.generateBusinessSign(sigStr);
			eod.setSignature(sign);
			String requestJson = JsonUtils.getInstance().objectToJson(eod);
			String req=HttpClient.postBody(url,requestJson);
			reqData=ElemeOpenConfig.reqStrToObj(req);
			//token获取失败重新设置发送请求
			if(reqData!=null&&("40004".equals(reqData.getCode())||"40002".equals(reqData.getCode()))){
				boolean refreshToken=ElemeOpenConfig.refreshToken(salt);
				if(refreshToken){
					//修改token重新请求
					String access_token1=ElemeOpenConfig.ACCESSTOKEN;
					sigStr.put("access_token",access_token1);
					String sign1=OpenSignHelper.generateBusinessSign(sigStr);
					eod.setSignature(sign1);
					requestJson = JsonUtils.getInstance().objectToJson(eod);
					req=HttpClient.postBody(url,requestJson);
					reqData=ElemeOpenConfig.reqStrToObj(req);
				}else{
					System.out.println("access_token刷新错误！");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return reqData;
		}
		return reqData;
	}

}
