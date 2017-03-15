package com.linjia.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linjia.base.controller.BaseController;
import com.linjia.tools.RedisUtil;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.model.Address;
import com.linjia.web.service.AddressService;
import com.linjia.wxpay.common.Configure;
import com.linjia.wxpay.common.RandomStringGenerator;
import com.linjia.wxpay.common.Signature;

/**
 * 分享模块
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/share")
public class ShareController extends BaseController {

	/**
	 * 微信分享
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/wxShare")
	@ResponseBody
	public Object wxShare(HttpServletRequest request, String url) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();

		String access_token = null;
		String jsapi_ticket = RedisUtil.get("wx_jsapi_ticket");
		String noncestr = RandomStringGenerator.getRandomStringByLength(18);
		long timestamp = System.currentTimeMillis() / 1000;
//		String url = "http://mp.weixin.qq.com?params=value";
		String signature = null;

		if (Tools.isEmpty(jsapi_ticket)) {
			JSONObject accessTokenObj = Util.getAccessToken();
			if (accessTokenObj != null) {
				access_token = accessTokenObj.optString("access_token");
				if (!Tools.isEmpty(access_token)) {
					RedisUtil.put("wx_access_token", access_token, 3600);
					JSONObject jsapiTicketObj = Util.getJsapiTicket(access_token);
					jsapi_ticket = jsapiTicketObj.optString("ticket");
					RedisUtil.put("wx_jsapi_ticket", jsapi_ticket, 3600);
				}
			}
		}

		if (!Tools.isEmpty(jsapi_ticket)) {
			// 生成签名
			resultData.put("url", url);
			resultData.put("jsapi_ticket", jsapi_ticket);
			resultData.put("noncestr", noncestr);
			resultData.put("timestamp", timestamp);
			signature = Signature.getSha1Sign(resultData);
			resultData.put("signature", signature);
			resultData.put("appId", Configure.getAppid());

			resMap.put("resultData", resultData);
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
			return resMap;
		}

		Util.writeFail(resMap);
		Util.writeError(resMap);
		return resMap;
	}
}
