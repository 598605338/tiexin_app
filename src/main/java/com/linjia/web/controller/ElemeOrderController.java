package com.linjia.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.linjia.base.controller.BaseController;
import com.linjia.tools.Util;
import com.linjia.web.service.ElemeOrderService;
import com.linjia.web.thirdService.eleme.interfaces.entity.message.OMessage;
import com.linjia.web.thirdService.eleme.interfaces.entity.shop.OShop;
import com.linjia.web.thirdService.eleme.interfaces.service.ShopService;
import com.linjia.web.thirdService.eleme.oauth.Token;

/**
 * 饿了么平台订单
 * 
 * @author xiangsy
 *
 */
@Controller
@RequestMapping("/eOrder")
public class ElemeOrderController extends BaseController {

	@Autowired
	private ElemeOrderService elemeOrderService;
	
	/**
	 * 店铺信息获取
	 * @param request
	 * @return
	 */
	@RequestMapping("/getShop")
	@ResponseBody
	public Object getShop(HttpServletRequest request,@RequestParam Long shopId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try{
			//个人应用
//			OAuthClient client=new OAuthClient();
//			Token token=client.getTokenInClientCredentials();
			Token token=Util.getElemeToken();
			ShopService shopService = new ShopService(token);
			OShop shop=shopService.getShop(shopId);
			if(shop!=null){
//				System.out.println(JSONUtil.ObjToJSON(shop));
			}
			//企业应用
//			OAuthClient oAuthClient = new OAuthClient();
//			String authUrl = oAuthClient.getAuthUrl(state, scope);
//			Token tokeno = oAuthClient.getTokenByCode(tokeno);
//			ShopService shopServiceo = new ShopService(token);
//			OShop shopo = shopServiceo.getShop(shopId);
//			Token freshToken = oAuthClient.getTokenByRefreshToken(token.getRefreshToken(), scope);
			
			resMap.put("status", "ok");
			resMap.put("message", "success");
			resMap.put("resultData",shop);
			return resMap;
		}catch(Exception e){
			e.printStackTrace();
			Util.writeError(resMap);
			resMap.put("message", "系统发生异常！");
			return resMap;
		}
	}
	
	/**
	 * 订单获取
	 * @param request
	 * @return
	 */
	@RequestMapping("/getElemeOrder")
	@ResponseBody
	public Object getElemeOrder(HttpServletRequest request,@RequestParam String orderId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		boolean flag=false;
		try{
			if(orderId!=null){
				flag=elemeOrderService.insertElemeOrder(orderId);
			}
			resMap.put("status", "ok");
			resMap.put("message", "success");
			resMap.put("resultData",flag);
			return resMap;
		}catch(Exception e){
			e.printStackTrace();
			Util.writeError(resMap);
			resMap.put("message", "系统发生异常！");
			return resMap;
		}
	}
	
	/**
	 * 确认订单
	 * @param request
	 * @return
	 */
	@RequestMapping("/confirmElemeOrder")
	@ResponseBody
	public Object confirmElemeOrder(HttpServletRequest request,@RequestParam String orderId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		boolean flag=false;
		try{
			if(orderId!=null){
				int updType=1;
				flag=elemeOrderService.updateElemeOrder(orderId,null,updType,null);
			}
			resMap.put("status", "ok");
			resMap.put("message", "success");
			resMap.put("resultData",flag);
			return resMap;
		}catch(Exception e){
			e.printStackTrace();
			Util.writeError(resMap);
			resMap.put("message", "系统发生异常！");
			return resMap;
		}
	}
	
	/**
	 * 取消订单
	 * @param request
	 * @return
	 */
	@RequestMapping("/cancelElemeOrder")
	@ResponseBody
	public Object cancelElemeOrder(HttpServletRequest request,@RequestParam String orderId,@RequestParam int type,@RequestParam String remark) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		boolean flag=false;
		try{
			if(orderId!=null){
				int updType=2;
				flag=elemeOrderService.updateElemeOrder(orderId,null,updType,type);
			}
			resMap.put("status", "ok");
			resMap.put("message", "success");
			resMap.put("resultData",flag);
			return resMap;
		}catch(Exception e){
			e.printStackTrace();
			Util.writeError(resMap);
			resMap.put("message", "系统发生异常！");
			return resMap;
		}
	}
	
	/**
	 * 同意退单
	 * @param request
	 * @return
	 */
	@RequestMapping("/agreeCancelOrder")
	@ResponseBody
	public Object agreeCancelOrder(HttpServletRequest request,@RequestParam String orderId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		boolean flag=false;
		try{
			if(orderId!=null){
				int updType=3;
				flag=elemeOrderService.updateElemeOrder(orderId,null,updType,null);
			}
			resMap.put("status","ok");
			resMap.put("message","success");
			resMap.put("resultData",flag);
			return resMap;
		}catch(Exception e){
			e.printStackTrace();
			Util.writeError(resMap);
			resMap.put("message", "系统发生异常！");
			return resMap;
		}
	}
	
	/**
	 * 不同意退单
	 * @param request
	 * @return
	 */
	@RequestMapping("/disagreeCancelOrder")
	@ResponseBody
	public Object disagreeCancelOrder(HttpServletRequest request,@RequestParam String orderId,@RequestParam String reason) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		boolean flag=false;
		try{
			if(orderId!=null){
				int updType=4;
				flag=elemeOrderService.updateElemeOrder(orderId,reason,updType,null);
			}
			resMap.put("status", "ok");
			resMap.put("message", "success");
			resMap.put("resultData",flag);
			return resMap;
		}catch(Exception e){
			e.printStackTrace();
			Util.writeError(resMap);
			resMap.put("message", "系统发生异常！");
			return resMap;
		}
	}
	
	/**
	 *  获取未到达的推送消息
	 * @param request
	 * @return
	 */
	@RequestMapping("/getNonReachedMessages")
	@ResponseBody
	public Object getNonReachedMessages(HttpServletRequest request) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try{
			List<String> messages=elemeOrderService.getNonReachedMessages();
			resMap.put("status", "ok");
			resMap.put("message", "success");
			resMap.put("resultData",messages);
			return resMap;
		}catch(Exception e){
			e.printStackTrace();
			Util.writeError(resMap);
			resMap.put("message", "系统发生异常！");
			return resMap;
		}
	}
	
	/**
	 * 获取未到达的推送消息实体
	 * @param request
	 * @return
	 */
	@RequestMapping("/getNonReachedOMessages ")
	@ResponseBody
	public Object getNonReachedOMessages(HttpServletRequest request) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try{
			List<OMessage> messages=elemeOrderService.getNonReachedOMessages();
			resMap.put("status", "ok");
			resMap.put("message", "success");
			resMap.put("resultData",messages);
			return resMap;
		}catch(Exception e){
			e.printStackTrace();
			Util.writeError(resMap);
			resMap.put("message", "系统发生异常！");
			return resMap;
		}
	}
}
