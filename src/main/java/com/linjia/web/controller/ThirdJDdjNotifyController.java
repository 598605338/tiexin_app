package com.linjia.web.controller;

import java.io.IOException;
import java.net.URLDecoder;
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
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.linjia.web.service.ThirdJDdjService;
import com.linjia.web.thirdService.JGpush.service.JgPushDeviceService;
import com.linjia.web.thirdService.jddj.config.JDDJConfigure;
import com.linjia.web.thirdService.jddj.config.JDDJUtil;
import com.linjia.web.thirdService.jddj.model.DJOrderQuery;
import com.linjia.web.thirdService.jddj.model.JddjDeliveryStatus;
import com.linjia.web.thirdService.jddj.model.DjNewOrderNotify;
import com.linjia.web.thirdService.jddj.model.OrderInfoDTO;
import com.linjia.web.thirdService.jddj.model.WebRequestDTO2;
import com.linjia.web.uhd123.common.Configure;
import com.linjia.web.uhd123.model.Topics;
import com.linjia.web.uhd123.service.UhdOrderService;
import com.linjia.wxpay.common.MD5;
import com.linjia.wxpay.common.Signature;

/**
 * 第三方京东到家接收通知消息接口
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/djsw")
public class ThirdJDdjNotifyController extends BaseController {

	@Autowired
	private ThirdJDdjService thirdJDdjService;

	@Autowired
	private UhdOrderService uhdOrderService;
	
	@Autowired
	private JgPushDeviceService jgPushDeviceService;

	/**
	 * 接收授权code和token
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/receiveCodeAndToken", method = RequestMethod.POST)
	@ResponseBody
	public Object receiveCodeAndToken(HttpServletRequest request, String token) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		logger.info("================接收京东到家授权code或token开始==================");
		logger.info("token==============================:" + token);
		if (!Tools.isEmpty(token)) {
			com.alibaba.fastjson.JSONObject storeChangeObj = JSON.parseObject(token);

		} else if (!Tools.isEmpty(token)) {

		} else {
			resMap.put("code", "FAIL");
			resMap.put("message", "没有接收到appid");
		}

		logger.info("================接收京东到家授权code或token结束==================");
		return resMap;
	}

	/**
	 * 通知消息>创建新订单消息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/newOrder", method = RequestMethod.POST)
	@ResponseBody
	public Object newOrder(HttpServletRequest request, @ModelAttribute WebRequestDTO2 webRequestDTO) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (webRequestDTO != null && webRequestDTO.getJd_param_json() != null && !StringUtils.isEmpty(webRequestDTO.getSign())) {
			logger.info("djsw[newOrder]传入参数：：：：：：：：：：：：：：：：：：：：：：：" + webRequestDTO.toString() + "::::sign:" + webRequestDTO.getSign());
			try {
				logger.info("本地计算签名：" + SignUtils.getSignByMD5(webRequestDTO, JDDJConfigure.getApp_Secret()));
				if (webRequestDTO.getSign().equals(SignUtils.getSignByMD5(webRequestDTO, JDDJConfigure.getApp_Secret()))) {
					synchronized (this) {
						DjNewOrderNotify djNewOrderNotify = JSONUtil.fromJson((String) webRequestDTO.getJd_param_json(), DjNewOrderNotify.class);

						// 如果订单已存在时，直接返回
						if (thirdJDdjService.countByOrderId(Long.valueOf(djNewOrderNotify.getBillId())) == 0) {

							DJOrderQuery query = new DJOrderQuery();
							query.setOrderId(Long.valueOf(djNewOrderNotify.getBillId()));
							List<OrderInfoDTO> list = thirdJDdjService.orderQuery(query);
							if (list != null && list.size() > 0) {

								// 创建订单数据
								thirdJDdjService.insertOrder(list.get(0));

								Util.writeJDDJSuccess(resMap, "");
								return resMap;
							}
						} else {
							Util.writeJDDJSuccess(resMap, "");
							return resMap;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Util.writeJDDJFail(resMap);
		return resMap;
	}

	/**
	 * 通知消息>开始配送消息
	 * 众包人员门店取货后，推送开始配送消息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deliveryOrder", method = RequestMethod.POST)
	@ResponseBody
	public Object deliveryOrder(HttpServletRequest request, @ModelAttribute WebRequestDTO2 webRequestDTO) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (webRequestDTO != null && webRequestDTO.getJd_param_json() != null && !StringUtils.isEmpty(webRequestDTO.getSign())) {
			logger.info("djsw[deliveryOrder]传入参数：：：：：：：：：：：：：：：：：：：：：：：" + webRequestDTO.toString() + "::::sign:" + webRequestDTO.getSign());
			try {
				if (webRequestDTO.getSign().equals(SignUtils.getSignByMD5(webRequestDTO, JDDJConfigure.getApp_Secret()))) {
					DjNewOrderNotify djNewOrderNotify = JSONUtil.fromJson((String) webRequestDTO.getJd_param_json(), DjNewOrderNotify.class);

					// 更新订单状态
					Map<String, Object> params = new HashMap<String, Object>();
					Date date = new Date();
					params.put("orderStatus", djNewOrderNotify.getStatusId());
					params.put("orderStatusTime", date);
					params.put("orderId", djNewOrderNotify.getBillId());
					params.put("knightPickupTime", date);
					thirdJDdjService.updateByPrimaryKey(params);
					Util.writeJDDJSuccess(resMap, "");
					return resMap;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Util.writeJDDJFail(resMap);
		return resMap;
	}

	/**
	 * 通知消息>订单妥投消息
	 * 客户签收货物后，推送订单妥投消息。
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/finishOrder", method = RequestMethod.POST)
	@ResponseBody
	public Object finishOrder(HttpServletRequest request, @ModelAttribute WebRequestDTO2 webRequestDTO) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (webRequestDTO != null && webRequestDTO.getJd_param_json() != null && !StringUtils.isEmpty(webRequestDTO.getSign())) {
			logger.info("djsw[finishOrder]传入参数：：：：：：：：：：：：：：：：：：：：：：：" + webRequestDTO.toString() + "::::sign:" + webRequestDTO.getSign());
			try {
				if (webRequestDTO.getSign().equals(SignUtils.getSignByMD5(webRequestDTO, JDDJConfigure.getApp_Secret()))) {
					DjNewOrderNotify djNewOrderNotify = JSONUtil.fromJson((String) webRequestDTO.getJd_param_json(), DjNewOrderNotify.class);

					// 更新商城订单状态
					Map<String, Object> params = new HashMap<String, Object>();
					Date date = new Date();
					params.put("orderStatus", djNewOrderNotify.getStatusId());
					params.put("orderStatusTime", date);
					params.put("orderId", djNewOrderNotify.getBillId());
					params.put("orderPreEndDeliveryTime", date);
					params.put("deliveryConfirmTime", date);
					thirdJDdjService.updateByPrimaryKey(params);
					Util.writeJDDJSuccess(resMap, "");
					return resMap;
					// 抛单到鼎力云更改订单状态为已完成 TODO
					/*
					 * uhdOrderService.updateOrderDeliverToUhd(djNewOrderNotify.
					 * getBillId(), date, "linjia", "signed", "handover",
					 * "handover");
					 */
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Util.writeJDDJFail(resMap);
		return resMap;
	}

	/**
	 * 通知消息>用户取消申请消息
	 * 从商家接单到众包配送交接或商家自送拣货完成或上门服务中期间，用户可以申请取消订单，到家推送订单取消申请消息，商家对用户取消申请进行取消确认或驳回
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/applyCancelOrder", method = RequestMethod.POST)
	@ResponseBody
	public Object applyCancelOrder(HttpServletRequest request, @ModelAttribute WebRequestDTO2 webRequestDTO) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (webRequestDTO != null && webRequestDTO.getJd_param_json() != null && !StringUtils.isEmpty(webRequestDTO.getSign())) {
			logger.info("djsw[applyCancelOrder]传入参数：：：：：：：：：：：：：：：：：：：：：：：" + webRequestDTO.toString() + "::::sign:" + webRequestDTO.getSign());
			try {
				if (webRequestDTO.getSign().equals(SignUtils.getSignByMD5(webRequestDTO, JDDJConfigure.getApp_Secret()))) {
					DjNewOrderNotify djNewOrderNotify = JSONUtil.fromJson((String) webRequestDTO.getJd_param_json(), DjNewOrderNotify.class);

					// 更新商城订单状态
					Map<String, Object> params = new HashMap<String, Object>();
					Date date = new Date();
					params.put("orderStatus", djNewOrderNotify.getStatusId());
					params.put("orderStatusTime", date);
					params.put("orderId", djNewOrderNotify.getBillId());
					params.put("applyCancel", 1);/** 用户取消申请：0未申请；1已申请* */
					thirdJDdjService.updateByPrimaryKey(params);
					
					//用户申请取消时推送取消申请到商家端
					jgPushDeviceService.pushMessageByOrderId(Long.valueOf((String)params.get("orderId")),"6","4");
					Util.writeJDDJSuccess(resMap, "");
					return resMap;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Util.writeJDDJFail(resMap);
		return resMap;
	}

	/**
	 * 通知消息>用户取消消息
	 * 拣货完成前，用户取消订单，推送订单取消消息；锁定状态的订单，商家确认收到货后，推送订单取消消息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/userCancelOrder", method = RequestMethod.POST)
	@ResponseBody
	public Object userCancelOrder(HttpServletRequest request, @ModelAttribute WebRequestDTO2 webRequestDTO) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (webRequestDTO != null && webRequestDTO.getJd_param_json() != null && !StringUtils.isEmpty(webRequestDTO.getSign())) {
			logger.info("djsw[userCancelOrder]传入参数：：：：：：：：：：：：：：：：：：：：：：：" + webRequestDTO.toString() + "::::sign:" + webRequestDTO.getSign());
			try {
				if (webRequestDTO.getSign().equals(SignUtils.getSignByMD5(webRequestDTO, JDDJConfigure.getApp_Secret()))) {
					DjNewOrderNotify djNewOrderNotify = JSONUtil.fromJson((String) webRequestDTO.getJd_param_json(), DjNewOrderNotify.class);

					// 更新商城订单状态
					Map<String, Object> params = new HashMap<String, Object>();
					Date date = new Date();
					params.put("orderStatus", djNewOrderNotify.getStatusId());
					params.put("orderStatusTime", date);
					params.put("orderId", djNewOrderNotify.getBillId());
					thirdJDdjService.updateByPrimaryKey(params);
					
					//取消时推送取消申请到商家端
					jgPushDeviceService.pushMessageByOrderId(Long.valueOf((String)params.get("orderId")),"4","4");
					Util.writeJDDJSuccess(resMap, "");
					return resMap;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Util.writeJDDJFail(resMap);
		return resMap;
	}

	/**
	 * 通知消息>订单锁定消息
	 * 拣货完成未开始配送前，用户取消订单，推送订单锁定消息；客户拒收货物后，推送订单锁定消息。
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/lockOrder", method = RequestMethod.POST)
	@ResponseBody
	public Object lockOrder(HttpServletRequest request, @ModelAttribute WebRequestDTO2 webRequestDTO) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (webRequestDTO != null && webRequestDTO.getJd_param_json() != null && !StringUtils.isEmpty(webRequestDTO.getSign())) {
			logger.info("djsw[lockOrder]传入参数：：：：：：：：：：：：：：：：：：：：：：：" + webRequestDTO.toString() + "::::sign:" + webRequestDTO.getSign());
			try {
				if (webRequestDTO.getSign().equals(SignUtils.getSignByMD5(webRequestDTO, JDDJConfigure.getApp_Secret()))) {
					DjNewOrderNotify djNewOrderNotify = JSONUtil.fromJson((String) webRequestDTO.getJd_param_json(), DjNewOrderNotify.class);

					// 更新商城订单状态
					Map<String, Object> params = new HashMap<String, Object>();
					Date date = new Date();
					params.put("orderStatus", djNewOrderNotify.getStatusId());
					params.put("orderStatusTime", date);
					params.put("orderId", djNewOrderNotify.getBillId());
					thirdJDdjService.updateByPrimaryKey(params);
					
					//推送订单锁定消息到商家端
					jgPushDeviceService.pushMessageByOrderId(Long.valueOf((String)params.get("orderId")),"7","4");
					Util.writeJDDJSuccess(resMap, "");
					return resMap;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Util.writeJDDJFail(resMap);
		return resMap;
	}

	/**
	 * 通知消息>拣货完成消息
	 * 商家门店拣货完成后，推送拣货完成消息。
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/pickFinishOrder", method = RequestMethod.POST)
	@ResponseBody
	public Object pickFinishOrder(HttpServletRequest request, @ModelAttribute WebRequestDTO2 webRequestDTO) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (webRequestDTO != null && webRequestDTO.getJd_param_json() != null && !StringUtils.isEmpty(webRequestDTO.getSign())) {
			logger.info("djsw[pickFinishOrder]传入参数：：：：：：：：：：：：：：：：：：：：：：：" + webRequestDTO.toString() + "::::sign:" + webRequestDTO.getSign());
			try {
				if (webRequestDTO.getSign().equals(SignUtils.getSignByMD5(webRequestDTO, JDDJConfigure.getApp_Secret()))) {
					DjNewOrderNotify djNewOrderNotify = JSONUtil.fromJson((String) webRequestDTO.getJd_param_json(), DjNewOrderNotify.class);

					// 更新订单拣货状态
					Map<String, Object> params = new HashMap<String, Object>();
					Date date = new Date();
					params.put("pickMark", djNewOrderNotify.getStatusId());
					params.put("orderStatusTime", date);
					params.put("orderId", djNewOrderNotify.getBillId());
					thirdJDdjService.updateByPrimaryKey(params);
					Util.writeJDDJSuccess(resMap, "");
					return resMap;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Util.writeJDDJFail(resMap);
		return resMap;
	}

	/**
	 * 通知消息>运单状态消息
	 * 订单到门店生成运单，配送员维护运单状态，推送运单状态消息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/pushDeliveryStatus", method = RequestMethod.POST)
	@ResponseBody
	public Object pushDeliveryStatus(HttpServletRequest request, @ModelAttribute WebRequestDTO2 webRequestDTO) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (webRequestDTO != null && webRequestDTO.getJd_param_json() != null && !StringUtils.isEmpty(webRequestDTO.getSign())) {
			logger.info("djsw[pushDeliveryStatus]传入参数：：：：：：：：：：：：：：：：：：：：：：：" + webRequestDTO.toString() + "::::sign:" + webRequestDTO.getSign());
			try {
				// logger.info("实际计算签名sign："+SignUtils.getSignByMD5(webRequestDTO,JDDJConfigure.getApp_Secret()));
				Map param = new HashMap();
				param.put("token", JDDJUtil.getURLDecoderUTF8(webRequestDTO.getToken()));
				param.put("app_key", JDDJUtil.getURLDecoderUTF8(webRequestDTO.getApp_key()));
				param.put("timestamp", JDDJUtil.getURLDecoderUTF8(webRequestDTO.getTimestamp()));
				param.put("format", "json");
				param.put("v", "1.0");
				param.put("jd_param_json", JDDJUtil.getURLDecoderUTF8((String) webRequestDTO.getJd_param_json()));

				// logger.info("计算签名的参数："+JDDJUtil.getSignByMD5(param,
				// JDDJConfigure.getApp_Secret()));
				if (webRequestDTO.getSign().equals(JDDJUtil.getSignByMD5(param, JDDJConfigure.getApp_Secret()))) {
					JddjDeliveryStatus deliveryStatus = JSONUtil.fromJson(JDDJUtil.getURLDecoderUTF8((String) webRequestDTO.getJd_param_json()),
							JddjDeliveryStatus.class);

					thirdJDdjService.insertDeliveryStatus(deliveryStatus);
					Util.writeJDDJSuccess(resMap, "");
					return resMap;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Util.writeJDDJFail(resMap);
		return resMap;
	}
}
