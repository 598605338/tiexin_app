package com.linjia.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import o2o.openplatform.sdk.dto.WebRequestDTO;
import o2o.openplatform.sdk.util.SignUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.linjia.base.controller.BaseController;
import com.linjia.tools.DateComFunc;
import com.linjia.tools.JSONUtil;
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
import com.linjia.web.service.ScoreProductService;
import com.linjia.web.service.ThirdJDdjService;
import com.linjia.web.thirdService.jddj.model.DJOrderQuery;
import com.linjia.web.thirdService.jddj.model.DjNewOrderNotify;
import com.linjia.web.thirdService.jddj.model.OrderInfoDTO;
import com.linjia.web.uhd123.common.Configure;
import com.linjia.web.uhd123.model.Topics;
import com.linjia.wxpay.common.MD5;
import com.linjia.wxpay.common.Signature;

/**
 * 第三方京东到家常规接口
 * 
 * @author lixinling
 *
 */
/**
 * @author lixinling
 * 2017年2月27日 下午2:06:10
 */
@Controller
@RequestMapping("/djapi")
public class ThirdJDdjController extends BaseController {

	@Autowired
	private ThirdJDdjService thirdJDdjService;

	/**
	 * 新版订单查询接口(远程)
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/orderQuery")
	@ResponseBody
	public Object orderQuery(HttpServletRequest request, DJOrderQuery query) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			List<OrderInfoDTO> list = thirdJDdjService.orderQuery(query);
			resMap.put("list", list);

			Util.writeOk(resMap);
			Util.writeSuccess(resMap);
		} catch (Exception e) {
			e.printStackTrace();
			Util.writeError(resMap);
			resMap.put("message", "系统错误");
			return resMap;
		}
		return resMap;
	}

	/**
	 * 商家确认接单接口
	 * 
	 * @param request
	 * @param isAgreed 操作类型 true 接单 false不接单
	 * @return
	 */
	@RequestMapping(value = "/orderAcceptOperate")
	@ResponseBody
	public Object orderAcceptOperate(HttpServletRequest request, Long orderId, String userId, Boolean isAgreed) {
		Map<String, Object> resMap = new HashMap<String, Object>();

		OrderInfoDTO order = thirdJDdjService.selectById(orderId);
		if (order != null) {

			// 修改本地订单状态并更新京东到家订单状态，然后进行鼎力云抛单操作
			if (thirdJDdjService.orderAcceptOperate(order, userId, isAgreed)) {

				//接单完成后直接召唤物流
				thirdJDdjService.orderJDZBDelivery(orderId, userId);
				
				Util.writeOk(resMap);
				Util.writeSuccess(resMap);
			} else {
				Util.writeError(resMap);
				resMap.put("message", "【京东到家】门店确认接单或取消订单操作失败");
			}
		} else {
			Util.writeError(resMap);
			resMap.put("message", "【京东到家】订单不存在");
			return resMap;
		}
		return resMap;
	}
	
	/**
	 * 拣货完成且众包配送接口
	 * 
	 * @param request
	 * @param userId 
	 * @return
	 */
	@RequestMapping(value = "/orderJDZBDelivery")
	@ResponseBody
	public Object orderJDZBDelivery(HttpServletRequest request, Long orderId, String userId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		
		OrderInfoDTO order = thirdJDdjService.selectById(orderId);
		if (order != null) {
			
			// 调用 商家后台京东众包配送接口
			if (thirdJDdjService.orderJDZBDelivery(orderId, userId)) {
				Util.writeOk(resMap);
				Util.writeSuccess(resMap);
			} else {
				Util.writeError(resMap);
				resMap.put("message", "【京东到家】召唤配送操作失败");
			}
		} else {
			Util.writeError(resMap);
			resMap.put("message", "【京东到家】订单不存在");
			return resMap;
		}
		return resMap;
	}

	/**
	 * 订单取消申请确认接口(用户发起订单取消申请，商家侧确认取消接口)
	 * lixinling 
	 * 2017年2月24日 下午3:22:18
	 * @param request
	 * @param orderId 订单号
	 * @param userId 操作人
	 * @param isAgreed 操作类型 true 同意 false驳回
	 * @param remark 操作备注
	 * @return
	 */
	@RequestMapping(value = "/orderCancelOperate")
	@ResponseBody
	public Object orderCancelOperate(HttpServletRequest request, Long orderId, String userId, Boolean isAgreed, String remark) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (orderId == null || orderId.longValue() == 0 || Tools.isEmpty(userId) || isAgreed == null) {
			logger.info("【京东到家】订单取消申请确认接口中orderId,userId,isAgreed均为必传参数.");
			Util.writeError(resMap);
			resMap.put("message", "【京东到家】订单取消申请确认接口中orderId,userId,isAgreed均为必传参数");
			return resMap;
		}

		try {
			if (thirdJDdjService.orderCancelOperate(orderId, userId, isAgreed, remark)) {
				Util.writeOk(resMap);
				Util.writeSuccess(resMap);
			} else {
				Util.writeError(resMap);
				resMap.put("message", "【京东到家】订单取消申请确认接口操作出错");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Util.writeError(resMap);
			resMap.put("message", "系统错误");
			return resMap;
		}
		return resMap;
	}
	

	/**
	 * 商家确认收到拒收退回（或取消）的商品接口(当顾客拒收（或取消）订单后，订单状态已经变为锁定时，商品被返回到商家，由商家触发确认已收到退回的商品，完成订单取消流程。)
	 * lixinling 
	 * 2017年2月24日 下午3:22:18
	 * @param request
	 * @param orderId 订单号
	 * @param userId 操作人
	 * @param isAgreed 操作类型 true 同意 false驳回
	 * @param remark 操作备注
	 * @return
	 */
	@RequestMapping(value = "/confirmReceiveGoods")
	@ResponseBody
	public Object confirmReceiveGoods(HttpServletRequest request, Long orderId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (orderId == null || orderId.longValue() == 0) {
			logger.info("【京东到家】商家确认收到拒收退回（或取消）的商品接口中orderId为必传参数.");
			Util.writeError(resMap);
			resMap.put("message", "【京东到家】商家确认收到拒收退回（或取消）的商品接口中orderId为必传参数");
			return resMap;
		}

		try {
			if (thirdJDdjService.confirmReceiveGoods(orderId)) {
				Util.writeOk(resMap);
				Util.writeSuccess(resMap);
			} else {
				Util.writeError(resMap);
				resMap.put("message", "【京东到家】商家确认收到拒收退回（或取消）的商品接口操作出错");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Util.writeError(resMap);
			resMap.put("message", "系统错误");
			return resMap;
		}
		return resMap;
	}
	
	/**
	 * 催配送员抢单接口
	 * lixinling 
	 * 2017年2月27日 下午3:22:18
	 * @param request
	 * @param orderId 订单号
	 * @param userId 操作人
	 * @param isAgreed 操作类型 true 同意 false驳回
	 * @param remark 操作备注
	 * @return
	 */
	@RequestMapping(value = "/urgeDispatching")
	@ResponseBody
	public Object urgeDispatching(HttpServletRequest request, Long orderId, String userId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (orderId == null || orderId.longValue() == 0 || Tools.isEmpty(userId)) {
			logger.info("【京东到家】催配送员抢单接口中orderId和userId均为必传参数.");
			Util.writeError(resMap);
			resMap.put("message", "【京东到家】催配送员抢单接口中orderId和userId均为必传参数");
			return resMap;
		}
		
		try {
			if (thirdJDdjService.urgeDispatching(orderId,userId)) {
				Util.writeOk(resMap);
				Util.writeSuccess(resMap);
			} else {
				Util.writeError(resMap);
				resMap.put("message", "【京东到家】催配送员抢单接口操作出错");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Util.writeError(resMap);
			resMap.put("message", "系统错误");
			return resMap;
		}
		return resMap;
	}
	
	
	/**
	 * 订单调整接口
	 * lixinling 
	 * 2017年2月27日 下午2:06:13
	 * @param request 
	 * @param orderId 订单号
	 * @param userId 操作人
	 * @param remark 操作备注
	 * @param oaosAdjustDTOList 调整订单后的商品明细(List<OAOSAdjustDTO>)
	 * @return
	 */
	@RequestMapping(value = "/adjustOrder")
	@ResponseBody
	public Object adjustOrder(HttpServletRequest request, Long orderId, String userId, String remark, String oaosAdjustDTOList) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (orderId == null || orderId.longValue() == 0 || Tools.isEmpty(userId) || Tools.isEmpty(oaosAdjustDTOList)) {
			logger.info("【京东到家】订单调整接口中orderId、userId和oaosAdjustDTOList均为必传参数.");
			Util.writeError(resMap);
			resMap.put("message", "【京东到家】订单调整接口中orderId、userId和oaosAdjustDTOList均为必传参数");
			return resMap;
		}
		
		try {
			if (thirdJDdjService.adjustOrder(orderId,userId,remark,oaosAdjustDTOList)) {
				Util.writeOk(resMap);
				Util.writeSuccess(resMap);
			} else {
				Util.writeError(resMap);
				resMap.put("message", "【京东到家】订单调整接口操作出错");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Util.writeError(resMap);
			resMap.put("message", "系统错误");
			return resMap;
		}
		return resMap;
	}
}
