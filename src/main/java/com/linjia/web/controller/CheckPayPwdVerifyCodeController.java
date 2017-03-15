package com.linjia.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.linjia.base.controller.BaseController;
import com.linjia.tools.DateComFunc;
import com.linjia.tools.MallProductStoreQtyThread;
import com.linjia.tools.RedisUtil;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.model.MallMaster;
import com.linjia.web.model.MallProductSendQty;
import com.linjia.web.model.Region;
import com.linjia.web.query.MallMasterQuery;
import com.linjia.web.service.MallMasterService;
import com.linjia.web.service.MallProductSendQtyService;
import com.linjia.web.uhd123.common.Configure;
import com.linjia.web.uhd123.model.Topics;
import com.linjia.wxpay.common.MD5;
import com.linjia.wxpay.common.Signature;

/**
 * 第三方验证码校验接口
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/checkThirdVerifyCode")
public class CheckPayPwdVerifyCodeController extends BaseController {
	
     private static String appid="linjia";
     private static String appKey="8e32zeq6xqsajdfctcwqu84yavqfnwlp";
     
     /** 成功* */
     private static Integer SUCCESS = 0;
     /** 失败* */
     private static Integer FAIL = -1;
     /** 未授权* */
     private static Integer UNAUTHORIZATION = -2;
     
	/**
	 * HDCARD对接验证码校验接口
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/checkHdCardVerifyCode",method=RequestMethod.POST,consumes="application/json")
	@ResponseBody
	public Object checkHdCardVerifyCode(HttpServletRequest request, String appid, String timestamp, String sign, String mbrCode, String verifyCode) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			logger.info("================HDCARD验证码校验接口START==================");
			if(!Tools.isEmpty(appid)){
				String params = IOUtils.toString(request.getInputStream(), "UTF-8");
				logger.info("params==============================:" + params);
				com.alibaba.fastjson.JSONObject storeChangeObj = JSON.parseObject(params);
				if (storeChangeObj != null) {
					mbrCode = storeChangeObj.getString("mbrCode");
					verifyCode = storeChangeObj.getString("verifyCode");
					if(CheckPayPwdVerifyCodeController.appid.equals(appid))
						checkVerifyCode(timestamp, sign, mbrCode, verifyCode, resMap);
				}
			}else{
				resMap.put("code", UNAUTHORIZATION);
				resMap.put("message", "没有接收到appid");
			}

			logger.info("================HDCARD验证码校验接口END==================");

		} catch (IOException e) {
			resMap.put("code", FAIL);
			resMap.put("message", "系统错误");
			return resMap;
		}
		return resMap;
	}
	
	/**
	 * 校验签名
	 * lixinling 
	 * 2016年12月1日 下午12:00:13
	 * @param timestamp
	 * @param sign
	 * @return
	 */
	private boolean checkIsSignValid(String timestamp, String sign){
		if (sign == "" || sign == null) {
			logger.info("API返回的数据签名不存在，有可能被第三方篡改!!!");
			return false;
		}
		logger.info("服务器回包里面的签名是:" + sign);
		
		// 用签名算法进行计算新的签名，用来跟API返回的签名进行比较
		String str1 = appid + timestamp;
		String str2 = str1 + appKey;
		String resultSign = MD5.MD5Encode(str2).toUpperCase();

		if (!sign.equals(resultSign)) {
			// 签名验不过，表示这个API返回的数据有可能已经被篡改了
			logger.info("API返回的数据签名验证不通过，有可能被第三方篡改!!!");
			return false;
		}
		logger.info("恭喜，API返回的数据签名验证通过!!!");
		return true;
	}
	
	/**
	 * 校验验证码
	 * lixinling 
	 * 2016年12月1日 下午12:00:13
	 * @param timestamp
	 * @param sign
	 * @return
	 */
	private void checkVerifyCode(String timestamp, String sign, String mbrCode, String verifyCode, Map<String, Object> resMap){
		if(checkIsSignValid(timestamp,sign)){
			//从redis中取出用户输入的验证码
			String mobile_securitycode = RedisUtil.get("verCode_"+mbrCode);
			if(verifyCode.equals(mobile_securitycode)){
				resMap.put("code", SUCCESS);
				resMap.put("message", "验证成功");
			}else{
				resMap.put("code", FAIL);
				resMap.put("message", "验证码不正确");
			}
		}else{
			resMap.put("code", UNAUTHORIZATION);
			resMap.put("message", "签名验证失败");
		}
	}

}
