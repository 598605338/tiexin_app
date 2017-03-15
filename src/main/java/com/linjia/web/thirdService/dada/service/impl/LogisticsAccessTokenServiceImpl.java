package com.linjia.web.thirdService.dada.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.linjia.web.thirdService.dada.constants.DaDaLogisticsConfig;
import com.linjia.web.thirdService.dada.factory.LogisticsUrlFactory;
import com.linjia.web.thirdService.dada.service.LogisticsAccessTokenService;
import com.linjia.web.thirdService.dada.utils.HttpRequestUtil;

@Service
public class LogisticsAccessTokenServiceImpl implements
		LogisticsAccessTokenService {
	private static Logger LOG = LoggerFactory.getLogger(LogisticsAccessTokenServiceImpl.class);
	
	@Override
	public boolean setGrantCode() {
		 boolean flag=false;
		 HttpRequestUtil httpRequest=HttpRequestUtil.getHttpsRequestSingleton();
		 String url = DaDaLogisticsConfig.geUrlPrefix()+LogisticsUrlFactory.getCmdUrl("getGrantCode");
	     Map<String, Object> param = new HashMap<String, Object>();
	     param.put("app_key",DaDaLogisticsConfig.APP_KEY);
	     param.put("scope",DaDaLogisticsConfig.SCOPE);
	     JSONObject jsonObject = httpRequest.sendGet(url, param);
	     //请求成功，则设置授权码到LogisticsConfig中的GRANTCODE
	     if("ok".equals(jsonObject.getString("status"))){
	    	 JSONObject obj = (JSONObject) jsonObject.get("result");
	    	 int grant_code=obj.getInt("grant_code");
	    	 if(grant_code!=0){
              DaDaLogisticsConfig.GRANTCODE=grant_code;
              flag=true;
             }
	     }
	     System.out.println("grant_code:"+DaDaLogisticsConfig.GRANTCODE);
		return flag;
	}

	@Override
	public boolean setAccessToken() {
		boolean flag=false;
		 HttpRequestUtil httpRequest=HttpRequestUtil.getHttpsRequestSingleton();
		 String url = DaDaLogisticsConfig.geUrlPrefix()+LogisticsUrlFactory.getCmdUrl("getAccessToken");
	     Map<String, Object> param = new HashMap<String, Object>();
	     param.put("app_key",DaDaLogisticsConfig.APP_KEY);
	     param.put("grant_type", DaDaLogisticsConfig.GRANTTYPE);
	     param.put("grant_code",String.valueOf(DaDaLogisticsConfig.GRANTCODE));
	     JSONObject jsonObject = httpRequest.sendGet(url, param);
	   //请求成功，则设置access_token和refresh_token到LogisticsConfig中
	     if("ok".equals(jsonObject.getString("status"))){
	    	 JSONObject obj = (JSONObject) jsonObject.get("result");
	    	 String access_token=obj.getString("access_token");
	    	 String refresh_token=obj.getString("refresh_token");
	    	 if(!"".equals(access_token)&&!"".equals(refresh_token)){
              DaDaLogisticsConfig.ACCESSTOKEN=access_token;
              DaDaLogisticsConfig.REFRESHTOKEN=refresh_token;
              flag=true;
             }
	     }else{
	    	 int errorCode=jsonObject.getInt("errerCode");
	    	 if(errorCode==2208||errorCode==2209||errorCode==2212){
	    		 setGrantCode();
	    	 }
	     }
	     System.out.println("access_token:"+DaDaLogisticsConfig.ACCESSTOKEN);
	     System.out.println("refresh_token:"+DaDaLogisticsConfig.REFRESHTOKEN);
		return flag;
	}

	@Override
	public boolean refreshAccessToken() {
		boolean flag=false;
		 HttpRequestUtil httpRequest=HttpRequestUtil.getHttpsRequestSingleton();
		 String url = DaDaLogisticsConfig.geUrlPrefix()+LogisticsUrlFactory.getCmdUrl("reFreshAccessToken");
	     Map<String, Object> param = new HashMap<String, Object>();
	     param.put("app_key",DaDaLogisticsConfig.APP_KEY);
	     param.put("grant_type", DaDaLogisticsConfig.REGRANTTYPE);
	     param.put("refresh_token",DaDaLogisticsConfig.REFRESHTOKEN);
	     JSONObject jsonObject = httpRequest.sendGet(url, param);
	   //请求成功，则设置access_token和refresh_token到LogisticsConfig中
	     if("ok".equals(jsonObject.getString("status"))){
	    	 JSONObject obj = (JSONObject) jsonObject.get("result");
	    	 String access_token=obj.getString("access_token");
	    	 String refresh_token=obj.getString("refresh_token");
	    	 if(!"".equals(access_token)&&!"".equals(refresh_token)){
             DaDaLogisticsConfig.ACCESSTOKEN=access_token;
             DaDaLogisticsConfig.REFRESHTOKEN=refresh_token;
             flag=true;
            }
	     }else{
	    	 int errorCode=jsonObject.getInt("errorCode");
	    	 if(errorCode==2214||errorCode==2215||errorCode==2216){
	    		 setAccessToken();
	    	 }
	     }
		return flag;
	}

	@Override
	public boolean setAccessTokenTimeOutTest() {
		 boolean flag=false;
		 HttpRequestUtil httpRequest=HttpRequestUtil.getHttpsRequestSingleton();
		 String url = DaDaLogisticsConfig.geUrlPrefix()+LogisticsUrlFactory.getCmdUrl("setAccessTokentimeoutTest");
	     Map<String, Object> param = new HashMap<String, Object>();
	     param.put("app_key",DaDaLogisticsConfig.APP_KEY);
	     JSONObject jsonObject = httpRequest.sendGet(url, param);
	     //请求成功，则设置授权码到LogisticsConfig中的GRANTCODE
	     if("ok".equals(jsonObject.getString("status"))){
              flag=true;
	     }
	     System.out.println("flag:"+flag);
		return flag;
	}

	@Override
	public boolean setRefreshAccessTokenTimeOutTest() {
		 boolean flag=false;
		 HttpRequestUtil httpRequest=HttpRequestUtil.getHttpsRequestSingleton();
		 String url = DaDaLogisticsConfig.geUrlPrefix()+LogisticsUrlFactory.getCmdUrl("setRefreshTokentimeoutTest");
	     Map<String, Object> param = new HashMap<String, Object>();
	     param.put("app_key",DaDaLogisticsConfig.APP_KEY);
	     JSONObject jsonObject = httpRequest.sendGet(url, param);
	     //请求成功，则设置授权码到LogisticsConfig中的GRANTCODE
	     if("ok".equals(jsonObject.getString("status"))){
              flag=true;
	     }
	     System.out.println("flag:"+flag);
		return flag;
	}
	
	//如果返回错误结果是因为grant_code或refresh_Token过期或不符合要求，发出请求初始化参数
	public boolean initDadaCode(JSONObject jsonObj){
		boolean flag=true;
		String status=jsonObj.getString("status");
		if(!"ok".equals(status)){
			int errorCode=jsonObj.getInt("errorCode");
			if(errorCode==2217){
				boolean reflag=refreshAccessToken();
				if(!reflag){
					refreshAccessToken();
				}
				flag=false;
			}else if(errorCode==2102||errorCode==2103||errorCode==2224){
				boolean setflag=setAccessToken();
				if(!setflag){
					setAccessToken();
				}
				flag=false;
			}else if(errorCode==2203||errorCode==2204){
				LOG.error("APP KEY出错，请重新申请！");
				flag=false;
			}
		}
		return flag;
	}

}
