package com.linjia.web.thirdService.bea.config;

import org.json.JSONObject;

import com.linjia.tools.JSONUtil;
import com.linjia.web.thirdService.bea.model.ElemeResData;
import com.linjia.web.thirdService.bea.model.OrderDataInfo;
import com.linjia.web.thirdService.bea.model.TokenEntity;
import com.linjia.web.thirdService.bea.sign.OpenSignHelper;
import com.linjia.web.thirdService.bea.util.HttpClient;

/**
 * 基础配置类
 */
public class ElemeOpenConfig {
    /**
     * 饿了么开放平台开放平台api(测试)
     */

//    public static final String API_URL = "https://exam-anubis.ele.me/anubis-webapi";
    
    /**
     * 饿了么开放平台开放平台api(正式)
     */
    public static final String API_URL = "https://open-anubis.ele.me/anubis-webapi";
    
    /**
     * 饿了么开放平台回调接口
     */

    public static final String CALL_URL = "http://www.linjia-cvs.cn/linjia_1/forOut/elemeOrder";
    
    /**
     * 饿了么开放平台回调接口(测试)
     */

//    public static final String CALL_URL = "http://b2c.linjia-cvs.cn/linjia_1/forOut/elemeOrder";

    /**
     * 第三方平台 app_id
     */
    public static final String appId = "72130663-7666-47e6-a095-67ba4e0774db";

    /**
     * 第三方平台 secret_key
     */
    public static final String secretKey = "3e69688b-2f1b-4d52-9c3d-589d2c619d58";
    
    //access_token的有效时间,单位为秒，默认24小时有效期 expire_time
    public static String ACCESSTOKEN = "0e4d61d3-726d-4892-a88e-9cfd83cb2e2c";
    
    public static boolean refreshToken(int salt){
    	boolean freshflag=false;
    	//token签名
		String token_signature=OpenSignHelper.generateSign(appId,salt+"",secretKey);
		String token_url=ElemeOpenConfig.API_URL+RequestConstant.obtainToken+"?app_id="+appId+"&salt="+salt+"&signature="+token_signature;
		String result=HttpClient.get(token_url);
		if(result!=null){
			TokenEntity tokenEntity= JSONUtil.fromJson(result, TokenEntity.class);
			if(tokenEntity!=null&&"200".equals(tokenEntity.getCode())){
				ACCESSTOKEN=tokenEntity.getData().getAccess_token();
				freshflag=true;
			}
		}
    	return freshflag;
    }
    
    public static ElemeResData reqStrToObj(String req){
    	ElemeResData reqData=null;
    	try{
	    	if(req!=null){
	    		reqData=new ElemeResData();
				JSONObject rejson=new JSONObject(req);
				String code=rejson.getString("code");
				String msg=rejson.getString("msg");
				Object dataobj=rejson.get("data");
				if(dataobj instanceof JSONObject){
					OrderDataInfo orderInfo=JSONUtil.fromJson(dataobj.toString(),OrderDataInfo.class);
					reqData.setData(orderInfo);
				}else if(dataobj instanceof String){
					reqData.setDatamsg(dataobj.toString());
				}
				reqData.setCode(code);
				String datamsg=reqData.getDatamsg();
				if(datamsg!=null){
					msg=msg+datamsg;
				}
				reqData.setMsg(msg);
			}
    	}catch(Exception e){
    		e.printStackTrace();
    		return reqData;
    	}
    	return reqData;
    }
}
