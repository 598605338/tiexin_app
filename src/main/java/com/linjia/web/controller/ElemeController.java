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
import com.linjia.tools.OrderToLogisOrder;
import com.linjia.web.model.OrderGroup;
import com.linjia.web.model.OrderGroupProduct;
import com.linjia.web.service.OrderGroupProductService;
import com.linjia.web.service.OrderGroupService;
import com.linjia.web.thirdService.bea.config.ElemeOpenConfig;
import com.linjia.web.thirdService.bea.model.ElemeResData;
import com.linjia.web.thirdService.bea.request.ElemeCreateOrderRequest;
import com.linjia.web.thirdService.bea.service.BeaService;
import com.linjia.web.thirdService.bea.util.RandomUtils;

/**
 * 饿了么物流控制器 
 * 
 * @author xiangsy
 *
 */
@Controller
@RequestMapping("/eleme")
public class ElemeController extends BaseController{
	
	@Autowired
	private BeaService beaService;
	@Autowired
	private OrderGroupService orderGroupService;// 邻家订单
	@Autowired
	private OrderGroupProductService orderGroupProductService;//邻家订单商品
	
	/**
	 * 蜂鸟配送
	 * @param request
	 * @return
	 */
	@RequestMapping("/beaSend")
	@ResponseBody
	public Object beaSend(HttpServletRequest request,@RequestParam Long orderId){
		Map<String, Object> map=new HashMap<String, Object>();
		OrderGroup og = orderGroupService.selectLogisOrderInfo(orderId);
		List<OrderGroupProduct> ogList = orderGroupProductService.selectProductListByGroupId(orderId);
		ElemeCreateOrderRequest.ElemeCreateRequestData reqData=OrderToLogisOrder.ljOrderToEleme(og,ogList);
		ElemeResData data=beaService.beaSend(reqData);
		map.put("data", data);
		return map;
	}
	
	
	/**
	 * 定点次日达
	 * @param request
	 * @return
	 */
	@RequestMapping("/nextDaySend")
	@ResponseBody
	public Object nextDaySend(HttpServletRequest request,@RequestParam Long orderId){
		Map<String, Object> map=new HashMap<String, Object>();
		OrderGroup og = orderGroupService.selectLogisOrderInfo(orderId);
		List<OrderGroupProduct> ogList = orderGroupProductService.selectProductListByGroupId(orderId);
		ElemeCreateOrderRequest.ElemeCreateRequestData reqData=OrderToLogisOrder.ljOrderToEleme(og,ogList);
		ElemeResData data=beaService.nextDaySend(reqData);
		map.put("data", data);
		return map;
	}
	
	/**
	 * 查询订单状态
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryStatus")
	@ResponseBody
	public Object queryStatus(HttpServletRequest request,@RequestParam Long orderId){
		Map<String, Object> map=new HashMap<String, Object>();
		ElemeResData data=beaService.queryStatus(orderId+"");
		map.put("data", data);
		return map;
	}
	
	/**
	 * 根据订单id取消订单
	 * @param orderId 订单id
	 * @param reason_code 取消原因编码
	 * @param cancel_reson 取消原因
	 * @return
	 */
	@RequestMapping("/cancelBeaOrder")
	@ResponseBody
	public Object cancelBeaOrder(HttpServletRequest request,@RequestParam Long orderId,@RequestParam Integer reason_code,@RequestParam String cancel_reason){
		Map<String, Object> map=new HashMap<String, Object>();
		ElemeResData data=beaService.cancelBeaOrder(orderId+"",reason_code,cancel_reason);
		map.put("data", data);
		return map;
	}
	
	/**
	 * 获取token
	 * @param request
	 * @return
	 */
	@RequestMapping("/getToken")
	@ResponseBody
	public Object getToken(HttpServletRequest request){
		Map<String, Object> map=new HashMap<String, Object>();
		int salt=RandomUtils.getInstance().generateValue(1000,9999);
		map.put("token1", ElemeOpenConfig.ACCESSTOKEN);
		boolean refreshToken=ElemeOpenConfig.refreshToken(salt);
		map.put("refresh", refreshToken);
		map.put("token", ElemeOpenConfig.ACCESSTOKEN);
		return map;
	}
}
