package com.linjia.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import com.alibaba.druid.util.StringUtils;
import com.linjia.base.controller.BaseController;
import com.linjia.constants.Application;
import com.linjia.constants.Constants;
import com.linjia.tools.DateComFunc;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.model.AccountcashdepositRecord;
import com.linjia.web.model.Address;
import com.linjia.web.model.OrderGroup;
import com.linjia.web.model.OrderRefund;
import com.linjia.web.model.PinTuanOrder;
import com.linjia.web.model.ScoreOrder;
import com.linjia.web.model.User;
import com.linjia.web.service.AccountcashChangeService;
import com.linjia.web.service.AccountcashdepositRecordService;
import com.linjia.web.service.AddressService;
import com.linjia.web.service.OrderGroupService;
import com.linjia.web.service.OrderPayService;
import com.linjia.web.service.OrderRefundService;
import com.linjia.web.service.PinTuanOrderService;
import com.linjia.web.service.ProductService;
import com.linjia.web.service.ProductSpecService;
import com.linjia.web.service.ScoreOrderService;
import com.linjia.web.service.impl.ActivityKaiTuanMainServiceImpl;
import com.linjia.wxpay.common.Configure;
import com.linjia.wxpay.common.HttpsRequest;
import com.linjia.wxpay.common.Signature;
import com.linjia.wxpay.common.XMLParser;
import com.linjia.wxpay.protocol.PayReqData;
import com.linjia.wxpay.protocol.PayResData;
import com.linjia.wxpay.protocol.RefundQueryReqData;
import com.linjia.wxpay.protocol.RefundQueryResData;
import com.linjia.wxpay.protocol.RefundReqData;
import com.linjia.wxpay.protocol.RefundResData;
import com.linjia.wxpay.service.PayService;
import com.linjia.wxpay.service.RefundQueryService;
import com.linjia.wxpay.service.RefundService;

/**
 * 微信支付模块
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/pay")
public class PayController extends BaseController {

	@Autowired
	private OrderGroupService orderGroupService;

	@Autowired
	private PinTuanOrderService pinTuanOrderService;

	@Autowired
	private OrderRefundService orderRefundService;

	@Autowired
	private OrderPayService orderPayService;

	@Autowired
	private ScoreOrderService scoreOrderService;

	@Autowired
	private AccountcashChangeService accountcashChangeService;

	@Autowired
	private AccountcashdepositRecordService accountcashdepositRecordService;

	@Autowired
	private AddressService addressService;
	
	@Autowired
	private ActivityKaiTuanMainServiceImpl activityKaiTuanMainServiceImpl;

	/**
	 * 确认支付
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/confirmPay")
	@ResponseBody
	public Object confirmPay(HttpServletRequest request, Long groupId, String userId, String openid, int payType, int orderType,
			String password) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		if (groupId == null || groupId.longValue() == 0 || Tools.isEmpty(userId)) {
			logger.info("参数值：groupId=" + groupId + ";userId=" + userId + ";openid=" + openid);
			resMap.put("message", "传入参数错误.");
			Util.writeError(resMap);
			return resMap;
		} else if (payType == Constants.PAY_TYPE.WX && Tools.isEmpty(openid)) {
			logger.info("参数值：openid不能为空");
			resMap.put("message", "参数值：openid不能为空.");
			Util.writeError(resMap);
			return resMap;
		}

		BigDecimal realPrice = null;
		String body = null;
		Long outTradeNo = null;
		String orderUserId = null;
		// 查询订单详情
		// 0:商城订单支付；1：拼团订单支付
		if (orderType == 0) {
			OrderGroup orderGroup = orderGroupService.selectById(groupId);
			if (orderGroup != null) {
				if (orderGroup.getPayStatus() == Constants.PAY_STATUS.UNPAY && orderGroup.getPayTime() == null) {
					realPrice = orderGroup.getRealPrice();
					body = orderGroup.getName();
					outTradeNo = orderGroup.getId();
					orderUserId = orderGroup.getUserId();
				} else {
					logger.info("该订单已支付，请勿重复支付");
					resMap.put("message", "该订单已支付，请勿重复支付");
					Util.writeError(resMap);
					return resMap;
				}
			}
		} else if (orderType == 1) {
			
			//拼团支付前校验
			Map<String,Object> ptCheckMap = activityKaiTuanMainServiceImpl.ptBeforePayed(groupId);
			if(ptCheckMap == null || "fail".equals(ptCheckMap.get("status"))){
				logger.info((String)ptCheckMap.get("message"));
				resMap.put("message", ptCheckMap.get("message"));
				Util.writeError(resMap);
				return resMap;
			}
			
			PinTuanOrder pinTuanOrder = pinTuanOrderService.selectPtOrderById(groupId);
			if (pinTuanOrder != null) {
				if (pinTuanOrder.getPay_status() == Constants.PAY_STATUS.UNPAY && pinTuanOrder.getPay_time() == null) {
					realPrice = pinTuanOrder.getReal_price();
					body = "拼团订单" + pinTuanOrder.getP_name();
					outTradeNo = pinTuanOrder.getOrder_id();
					orderUserId = pinTuanOrder.getUser_id();
				} else {
					logger.info("该订单已支付，请勿重复支付");
					resMap.put("message", "该订单已支付，请勿重复支付");
					Util.writeError(resMap);
					return resMap;
				}
			}
		}

		if (realPrice != null && realPrice.doubleValue() > 0 && outTradeNo != null && outTradeNo.longValue() > 0) {
			String spbill_create_ip = request.getRemoteAddr();

			// 校验这个订单是否是该用户的
			if (!userId.equals(orderUserId)) {
				logger.info("支付请求的数据验证失败，有可能数据被篡改了");
				resMap.put("message", "支付请求的数据验证失败，有可能数据被篡改了.");
				Util.writeError(resMap);
				return resMap;
			}

			// 调起支付
			resMap = orderPayService
					.payHandle(payType, body, null, realPrice, outTradeNo, spbill_create_ip, userId, openid, null, password);
			if ("error".equals(resMap.get("status"))) {
				if ("10001".equals(resMap.get("error_code"))) {
					resMap.put("message", "构造支付请求数据对象错误，请重新进行微信授权.");
				} else if ("10003".equals(resMap.get("error_code"))) {
					resMap.put("message", "支付请求API返回的数据签名验证失败，有可能数据被篡改了.");
				} else if ("20004".equals(resMap.get("error_code"))) {
					resMap.put("message", "账户余额不足或支付密码错误，付款失败.");
				} else if ("20706".equals(resMap.get("error_code"))) {
					resMap.put("message", "会员账户识别码支付密码错误或账户余额不足.");
				} else {
					resMap.put("message", "系统错误.");
				}
				Util.writeError(resMap);
				return resMap;
			}
		} else {
			resMap.put("message", "订单信息有误！");
			Util.writeError(resMap);
			return resMap;
		}

		// 返回订单id
		resultData.put("groupId", groupId);
		resMap.put("resultData", resultData);
		Util.writeSuccess(resMap);
		Util.writeOk(resMap);
		return resMap;
	}

	/**
	 * 申请退款
	 * 
	 * @param request
	 * @param orderType 1:普通订单,2:拼团订单,3:积分订单,其他为普通订单
	 * @param source 默认为整单退款操作，1为客服退款操作
	 * @param userId 执行退款操作用户
	 * @param refundId 退款单id
	 * @return
	 */
	@RequestMapping("/payRefund")
	@ResponseBody
	public Object payRefund(HttpServletRequest request, Long groupId, String userId, Integer orderType, Integer source, Long refundId) {
		Map<String, Object> resMap = new HashMap<String, Object>();

		// 查询订单详情
		OrderGroup orderGroup = null;
		if (orderType != null && orderType == 2) {
			// 拼团订单
			PinTuanOrder pinTuanOrder = pinTuanOrderService.selectPtOrderById(groupId);
			if (pinTuanOrder != null) {
				orderGroup = new OrderGroup();
				orderGroup.setId(groupId);
				orderGroup.setUserId(pinTuanOrder.getUser_id());
				orderGroup.setRealPrice(pinTuanOrder.getReal_price());
				orderGroup.setPayType(pinTuanOrder.getPay_type());
				orderGroup.setRefundReason("超时未成团订单取消");
				// 查询拼团订单收货人信息
				Address address = addressService.selectById(Long.valueOf(pinTuanOrder.getAddress_id()));
				orderGroup.setReceiveName(address.getReceiveName());
			}
		} else if (orderType != null && orderType == 3) {
			//积分订单
			ScoreOrder scoreOrder = scoreOrderService.selectById(groupId);
			if(scoreOrder != null && scoreOrder.getPrice().doubleValue() > 0 ){
				orderGroup = new OrderGroup();
				orderGroup.setId(groupId);
				orderGroup.setUserId(scoreOrder.getUserId());
				orderGroup.setRealPrice(scoreOrder.getPrice());
				orderGroup.setPayType(scoreOrder.getPayType());
				orderGroup.setRefundReason("与客户沟通后取消订单");
				orderGroup.setPayStatus(scoreOrder.getOrderStatus());
				orderGroup.setOrderGroupStatus(Constants.ORDER_GROUP_STATUS.SUCCESS);
			}
			
		}else {
			// 普通订单
			orderGroup = orderGroupService.selectById(groupId);
			orderGroup.setRefundReason("订单取消");
		}

		if (orderGroup != null
				&& orderGroup.getPayStatus() == Constants.PAY_STATUS.PAYD
				&& (orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.PAYD
						|| orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.CONFIRM
						|| orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.SENDING
						|| orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.SUCCESS
						|| orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.RECEIVED
						|| orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.WLQXCXFD)) {

			//防止钱包支付重复退款
			if(orderGroup.getPayType() == Constants.PAY_TYPE.PURSE){
				Integer count = accountcashdepositRecordService.selectRefundByOrderGroupId(groupId);
				if(count > 0){
					logger.error("该订单已退款，请勿重复退款");
					resMap.put("message", "该订单已退款，请勿重复退款");
					Util.writeError(resMap);
					return resMap;
				}
			}
			// 生成退款单并执行退款操作
			Map<String, Object> map = orderRefundService.insertRefund(orderGroup, userId, orderType, source, refundId);

			if (map == null) {
				logger.error("生成退款单错误");
				resMap.put("message", "生成退款单错误");
				Util.writeError(resMap);
				return resMap;
			} else if ((int) map.get("return_code") == 1) {
				resMap.put("message", "【微信退款申请失败】退款请求错误，返回值为空");
				Util.writeError(resMap);
				return resMap;
			} else if ((int) map.get("return_code") == 2) {
				resMap.put("message", "【微信退款申请失败】退款API系统返回失败，请检测Post给API的数据是否规范合法");
				Util.writeError(resMap);
				return resMap;
			} else if ((int) map.get("return_code") == 3) {
				resMap.put("message", "【微信退款申请失败】退款请求API返回的数据签名验证失败，有可能数据被篡改了");
				Util.writeError(resMap);
				return resMap;
			} else if ((int) map.get("return_code") == 4) {
				resMap.put("message", "调用微信退款申请发生错误");
				Util.writeError(resMap);
				return resMap;
			} else if ((int) map.get("return_code") == 5) {
				resMap.put("message", "退款成功");
				Util.writeOk(resMap);
				return resMap;
			}
		}
		resMap.put("message", "订单id不正确");
		Util.writeError(resMap);
		return resMap;
	}

	/**
	 * 微信支付异步通知
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/notifyUrl")
	public void wxNotifyUrl(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 验证请求发自微信
			String notifyXML = IOUtils.toString(request.getInputStream(), "UTF-8");
			// postXML = URLDecoder.decode(postXML);
			logger.info("微信调起notifyUrl接口");
			logger.info(notifyXML);
			if (!StringUtils.isEmpty(notifyXML)) {

				// 校验签名
				try {
					if (!Signature.checkIsSignValidFromResponseString(notifyXML)) {
						logger.error("【微信支付异步通知失败】异步通知返回的数据签名验证失败，有可能数据被篡改了");
						return;
					}
				} catch (ParserConfigurationException | SAXException e) {
					e.printStackTrace();
				}

				Map<String, Object> map;
				try {
					map = XMLParser.getMapFromXML(notifyXML);
				} catch (ParserConfigurationException | SAXException e) {
					logger.error("异步通知结果解析成Map时发生错误", e);
					return;
				}

				// 业务处理
				if ("SUCCESS".equals(map.get("return_code")) && "SUCCESS".equals(map.get("result_code"))
						&& Configure.getAppid().equals(map.get("appid")) && Configure.getMchid().equals(map.get("mch_id"))) {

					// 微信支付订单号
					String transaction_id = (String) map.get("transaction_id");
					// 商户订单号
					String out_trade_no = (String) map.get("out_trade_no");

					/*
					 * type:1订单号；2订单组号；3退款单号 对于订单号
					 * orderId的生成规则：yyMMdd+1+b_orderid_generate表自增主键id
					 * (例如：16072611001) 对于订单号
					 * orderGroupId的生成规则：yyMMdd+2+b_orderid_generate表自增主键id
					 * (例如：16072621002) 对于订单号
					 * refundId的生成规则：yyMMdd+3+b_refundid_generate表自增主键id
					 * (例如：16072631003) 对于订单号
					 * scoreOrderId的生成规则：yyMMdd+4+b_orderid_generate表自增主键id
					 * (例如：16072641003)
					 */
					if (out_trade_no.charAt(6) == '2') {
						logger.info("微信支付已完成，调起notifyUrl进行处理正常订单...");
						// 处理正常订单
						// 查询订单详情
						OrderGroup orderGroup = orderGroupService.selectById(Long.valueOf(out_trade_no));
						if (orderGroup.getPayStatus() == Constants.PAY_STATUS.UNPAY) {
							orderPayService.updateOrderGroupByPaySuccessed(orderGroup, transaction_id, out_trade_no, Constants.PAY_TYPE.WX,
									Constants.PAY_STATUS.PAYD, Constants.ORDER_GROUP_STATUS.PAYD, orderGroup.getMallCode());
							logger.info("微信支付已完成，调起notifyUrl进行处理正常订单,更新group订单状态已完成.");
						}
					} else if (out_trade_no.charAt(6) == '4') {
						logger.info("微信支付已完成，调起notifyUrl进行处理积分兑换订单...");
						// 查询积分兑换订单详情
						ScoreOrder scoreOrder = scoreOrderService.selectById(Long.valueOf(out_trade_no));
						if (scoreOrder.getOrderStatus() == Constants.SCORE_STATUS.UNPAY) {
							// 处理积分商城积分兑换订单
							// 传入参数
							String attach = (String) map.get("attach");
							if (!Tools.isEmpty(attach)) {
								JSONObject attObj = new JSONObject(attach);
								boolean result = scoreOrderService.updatePayScoreOrderStatus(attObj.optString("userId"),
										attObj.getLong("cardCouponId"), scoreOrder.getId(), Constants.PAY_TYPE.WX);
								if (result) {
									logger.info("微信支付已完成，调起notifyUrl进行处理积分兑换订单,积分兑换订单所有处理操作已完成.");
								} else {
									logger.info("微信支付已完成，调起notifyUrl进行处理积分兑换订单,积分兑换订单处理过程中出现错误，等待微信再次调起notifyUrl接口.");
									return;
								}
							} else {
								logger.error("attach参数为空，无法进行扣减积分操作！主动抛出异常终止程序！");
							}
						}

					} else if (out_trade_no.charAt(6) == '5') {
						logger.info("微信支付已完成，调起notifyUrl进行处理拼团订单...");
						// 查询拼团订单详情
						PinTuanOrder pinTuanOrder = pinTuanOrderService.selectPtOrderById(Long.valueOf(out_trade_no));
						if (pinTuanOrder.getPay_status() == Constants.PAY_STATUS.UNPAY) {
							boolean flag = activityKaiTuanMainServiceImpl.ptAfterPayed(Long.valueOf(out_trade_no), Constants.PAY_TYPE.WX);
							/*orderPayService
									.updatePintuanOrderByPaySuccessed(pinTuanOrder, Constants.PAY_TYPE.WX, Constants.PAY_STATUS.PAYD);*/
							if(flag){
								logger.info("微信支付已完成，调起notifyUrl进行处理拼团订单,更新拼团订单状态操作已完成.");
							}else{
								logger.info("【ERROR:::::::::::::::::::::::::::::::::】微信支付已完成，调起notifyUrl进行处理拼团订单,更新拼团订单状态操作失败.");
							}
						}

					}
					String result = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
					response.getWriter().print(result);
					logger.info("notifyUrl返回微信异步通知成功");
				}

			}
		} catch (IOException e) {
			logger.error("notifyUrl返回微信异步通知失败", e);
			e.printStackTrace();
		}
	}

	/**
	 * 微信退款查询
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/wxRefundQuery")
	@ResponseBody
	public Object wxRefundQuery(HttpServletRequest request, Long outRefundNo) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		OrderRefund orderRefund = orderRefundService.selectById(outRefundNo);
		RefundQueryReqData refundQueryReqData = createRefundQueryReqData(orderRefund);
		try {
			String resultRefundQueryXml = new RefundQueryService().request(refundQueryReqData);

			if (StringUtils.isEmpty(resultRefundQueryXml)) {
				logger.error("【微信退款查询失败】退款请求错误，返回值为空");
				resMap.put("message", "【微信退款查询失败】退款请求错误");
				Util.writeError(resMap);
				return resMap;
			} else {
				RefundQueryResData refundQueryResData = (RefundQueryResData) com.linjia.wxpay.common.Util.getObjectFromXML(
						resultRefundQueryXml, RefundQueryResData.class);
				if (refundQueryResData.getReturn_code() == null || refundQueryResData.getReturn_code().equals("FAIL")) {
					logger.error("【微信退款查询失败】退款查询API系统返回失败，请检测Post给API的数据是否规范合法。返回的错误信息为：" + refundQueryResData.getReturn_msg());
					resMap.put("message", "【微信退款查询失败】退款查询API系统返回失败，请检测Post给API的数据是否规范合法");
					Util.writeError(resMap);
					return resMap;
				} else if (refundQueryResData.getResult_code().equals("SUCCESS")) {
					// --------------------------------------------------------------------
					// 收到API的返回数据的时候得先验证一下数据有没有被第三方篡改，确保安全
					// --------------------------------------------------------------------
					if (!Signature.checkIsSignValidFromResponseString(resultRefundQueryXml)) {
						logger.error("【微信退款查询失败】退款查询请求API返回的数据签名验证失败，有可能数据被篡改了");
						resMap.put("message", "【微信退款查询失败】退款查询请求API返回的数据签名验证失败，有可能数据被篡改了");
						Util.writeError(resMap);
						return resMap;
					}

					if ("SUCCESS".equals(refundQueryResData.getResult_code())) {
						// 更新退款状态
						Map<String, Object> param = new HashMap<String, Object>();

						param.put("refundOnlinepayStatus",
								com.linjia.wxpay.common.Util.getRefundStatus(refundQueryResData.getRefund_status_0()));
						param.put("outRefundNo", refundQueryResData.getOut_refund_no_0());
						orderRefundService.updateRefundStatusById(param);
						resMap.put("resultDate", refundQueryResData.getRefund_status_0());
						Util.writeSuccess(resMap);
						Util.writeOk(resMap);
					}
				}else{
					logger.error("【微信退款查询失败】。返回的错误信息为：" + refundQueryResData.getErr_code_des());
					resMap.put("message", "退款订单查询失败");
					Util.writeError(resMap);
					return resMap;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resMap;
	}

	/**
	 * 构造退款查询数据对象
	 * lixinling 
	 * 2016年7月27日 下午2:46:06
	 * @param orderGroup
	 * @return
	 */
	public RefundQueryReqData createRefundQueryReqData(OrderRefund orderRefund) {

		/**
		 * 请求退款查询服务
		 * @param transactionID 是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。建议优先使用
		 * @param outTradeNo 商户系统内部的订单号,transaction_id 、out_trade_no 二选一，如果同时存在优先级：transaction_id>out_trade_no
		 * @param deviceInfo 微信支付分配的终端设备号，与下单一致
		 * @param outRefundNo 商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
		 * @param refundID 来自退款API的成功返回，微信退款单号refund_id、out_refund_no、out_trade_no 、transaction_id 四个参数必填一个，如果同事存在优先级为：refund_id>out_refund_no>transaction_id>out_trade_no
		 */
		RefundQueryReqData refundQueryReqData = new RefundQueryReqData(null, String.valueOf(orderRefund.getOrderGroupId()), null,
				String.valueOf(orderRefund.getId()), orderRefund.getRefundId());
		return refundQueryReqData;
	}

	/**
	 * 会员钱包充值微信支付异步通知
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/notifyUrlAccountcashdeposit")
	public void wxNotifyUrlAccountcashdeposit(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 验证请求发自微信
			String notifyXML = IOUtils.toString(request.getInputStream(), "UTF-8");
			// postXML = URLDecoder.decode(postXML);
			logger.info("微信调起notifyUrlAccountcashdeposit接口");
			logger.info(notifyXML);
			if (!StringUtils.isEmpty(notifyXML)) {

				// 校验签名
				try {
					if (!Signature.checkIsSignValidFromResponseString(notifyXML)) {
						logger.error("钱包充值------【微信支付异步通知失败】异步通知返回的数据签名验证失败，有可能数据被篡改了");
						return;
					}
				} catch (ParserConfigurationException | SAXException e) {
					e.printStackTrace();
				}

				Map<String, Object> map;
				try {
					map = XMLParser.getMapFromXML(notifyXML);
				} catch (ParserConfigurationException | SAXException e) {
					logger.error("钱包充值------异步通知结果解析成Map时发生错误", e);
					return;
				}

				// 业务处理
				if ("SUCCESS".equals(map.get("return_code")) && "SUCCESS".equals(map.get("result_code"))
						&& Configure.getAppid().equals(map.get("appid")) && Configure.getMchid().equals(map.get("mch_id"))) {

					// 微信支付订单号
					String transaction_id = (String) map.get("transaction_id");
					// 商户订单号
					String out_trade_no = (String) map.get("out_trade_no");

					// 查询钱包充值记录
					AccountcashdepositRecord accountcashdepositRecord = accountcashdepositRecordService.selectById(Long
							.valueOf(out_trade_no));
					if (accountcashdepositRecord.getTranstatus() == Constants.TRAN_STATUS.UNSUCCESS) {
						// 逻辑规则，先更新本地记录表的状态为充值成功，防止CRM充值成功后更新本地充值记录失败造成重复调用CRM充值接口，直接造成给用户钱包重复充值的情况，
						// 当CRM充值失败时，再次更新本地记录表的交易状态为未完成，再次让微信调用本接口notifyUrlAccountcashdeposit进行再次CRM钱包充值
						// 更新钱包充值记录的状态
						accountcashdepositRecord.setTranstatus(Constants.TRAN_STATUS.SUCCESSED);
						accountcashdepositRecord.setTransactionId(transaction_id);
						int row = accountcashdepositRecordService.update(accountcashdepositRecord);
						if (row == 1) {
							// 支付完成后进行CRM会员充值
							JSONObject result = Util.accountcashdeposit(accountcashdepositRecord.getAccountaccesscode(),
									String.valueOf(accountcashdepositRecord.getTranId()), accountcashdepositRecord.getOccur().toString(),
									accountcashdepositRecord.getRealpay().toString(),
									DateComFunc.formatDate(accountcashdepositRecord.getTrantime(), "yyyy-MM-dd HH:mm:ss"),
									accountcashdepositRecord.getXid());
							if (result != null && result.optInt("responseCode") == 204) {
								logger.info("调用CRM接口钱包充值成功。");

								// 插入用户钱包交易变化记录
								accountcashChangeService.insert(Constants.ACCOUNTCASH_ACTION.DEPOSIT_ACCOUNT,
										accountcashdepositRecord.getAccountaccesscode(), accountcashdepositRecord.getOccur());
							} else {
								// 充值失败
								logger.info("====调用CRM接口钱包充值失败。" + result);
								// 更新钱包充值记录的状态为未完成
								accountcashdepositRecord.setTranstatus(Constants.TRAN_STATUS.UNSUCCESS);
								accountcashdepositRecord.setTransactionId(null);
								accountcashdepositRecordService.update(accountcashdepositRecord);
								return;
							}
						} else {
							logger.info("更新钱包充值记录的状态为已完成时发生错误。");
							return;
						}
					}
					String result = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
					response.getWriter().print(result);
					logger.info("钱包充值------notifyUrlAccountcashdeposit返回微信异步通知成功");
				}

			}
		} catch (IOException e) {
			logger.error("钱包充值------notifyUrlAccountcashdeposit返回微信异步通知失败", e);
			e.printStackTrace();
		}
	}
}
