package com.linjia.web.controller;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linjia.base.controller.BaseController;
import com.linjia.constants.Constants;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.model.ScoreOrder;
import com.linjia.web.model.ScoreProduct;
import com.linjia.web.query.ScoreOrderQuery;
import com.linjia.web.query.ScoreProductQuery;
import com.linjia.web.service.OrderPayService;
import com.linjia.web.service.ScoreOrderService;
import com.linjia.web.service.ScoreProductService;

/**
 * 积分模块
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/score")
public class ScoreMallController extends BaseController {

	@Autowired
	private ScoreProductService scoreProductService;

	@Autowired
	private ScoreOrderService scoreOrderService;

	@Autowired
	private OrderPayService orderPayService;

	/**
	 * 取得积分商城页商品列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getScoreProductList")
	@ResponseBody
	public Object getScoreProductList(HttpServletRequest request, ScoreProductQuery query) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			List<ScoreProduct> scoreProductList = scoreProductService.selectList(query);
			resMap.put("resultData", scoreProductList);
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
		}
		return resMap;
	}

	/**
	 * 立即兑换(纯积分兑换)
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/doExchange")
	@ResponseBody
	public Object doExchange(HttpServletRequest request, ScoreProductQuery query, String userId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			ScoreProduct scoreProduct = scoreProductService.selectInfoByPrimaryKey(query.getId());
			if (scoreProduct == null) {
				Util.writeFail(resMap);
				Util.writeError(resMap);
				return resMap;
			}

			if (scoreProduct.getQuantity() != null && scoreProduct.getQuantity() > 0) {
				if (scoreProduct.getCardType() == Constants.CARD_COUPON_TYPE.THIRD_QM) {
					// 如果是券码的情况下，查询是否还有未兑换的券码
					ScoreProduct thirdInfo = scoreProductService.selectThirdContentByScoreProductId(scoreProduct.getId());
					if (Tools.isEmpty(thirdInfo.getThirdId())) {
						resMap.put("message", "该积分商品库存不足");
						Util.writeError(resMap);
						return resMap;
					} else {
						scoreProduct.setContent(thirdInfo.getContent());
						scoreProduct.setThirdId(thirdInfo.getThirdId());
					}
				}
				/*
				 * if(scoreProduct.getCardType() ==
				 * Constants.CARD_COUPON_TYPE.THIRD_LJQ){ content =
				 * scoreProduct.getContent(); }
				 */

				String loginCode = userId;
				// 生成积分订单
				if (scoreOrderService.insertScoreOrder(scoreProduct, userId, loginCode, URLDecoder.decode(query.getComment(), "UTF-8"))) {
					Util.writeSuccess(resMap);
					Util.writeOk(resMap);
				} else {
					resMap.put("message", "积分不足！");
					Util.writeError(resMap);
				}
			} else {
				resMap.put("message", "库存不足，请稍后再试！");
				Util.writeError(resMap);
			}
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
			e.printStackTrace();
		}
		return resMap;
	}

	/**
	 * 立即兑换(积分+金钱兑换)
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/submitScoreOrder")
	@ResponseBody
	public Object submitScoreOrder(HttpServletRequest request, ScoreOrder scoreOrder, String userId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		try {
			ScoreProduct scoreProduct = scoreProductService.selectInfoByPrimaryKey(scoreOrder.getScoreProductId());
			if (scoreProduct == null) {
				Util.writeFail(resMap);
				Util.writeError(resMap);
				return resMap;
			}
			if (scoreProduct.getQuantity() != null && scoreProduct.getQuantity() > 0) {
				if (scoreProduct.getType() == Constants.CARD_COUPON_TYPE.THIRD_QM) {
					// 如果是券码的情况下，查询是否还有未兑换的券码
					ScoreProduct thirdInfo = scoreProductService.selectThirdContentByScoreProductId(scoreProduct.getId());
					if (Tools.isEmpty(thirdInfo.getThirdId())) {
						resMap.put("message", "该积分商品库存不足");
						Util.writeError(resMap);
						return resMap;
					} else {
						scoreProduct.setContent(thirdInfo.getContent());
						scoreProduct.setThirdId(thirdInfo.getThirdId());
					}
				}

				String loginCode = userId; // 通过userId取得
				// 生成积分订单
				resMap = scoreOrderService.insertPayScoreOrder(scoreProduct, scoreOrder, userId, loginCode);
				if ("error".equals(resMap.get("status"))) {
					if ("30001".equals(resMap.get("error_code"))) {
						resMap.put("message", "账户积分不足，兑换失败.");
					} else {
						resMap.put("message", "系统错误.");
					}
					Util.writeError(resMap);
					return resMap;
				}

				resultData.put("scoreOrderId", scoreOrder.getId());
				resMap.put("resultData", resultData);
				Util.writeSuccess(resMap);
				Util.writeOk(resMap);
			} else {
				resMap.put("message", "库存不足，请稍后再试！");
				Util.writeError(resMap);
			}
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
			e.printStackTrace();
		}
		return resMap;
	}

	/**
	 * 确认下单(积分+金钱兑换)
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/confirmScoreOrder")
	@ResponseBody
	public Object confirmScoreOrder(HttpServletRequest request, Long scoreOrderId, String userId, int payType, String openid,
			String passsword) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			ScoreOrder model = scoreOrderService.selectById(scoreOrderId);
			if (model == null) {
				Util.writeFail(resMap);
				Util.writeError(resMap);
				return resMap;
			}
			String spbill_create_ip = request.getRemoteAddr();
			String loginCode = userId; // 通过userId取得
			// 积分+金钱兑换的情况下先进行金钱支付，再进行积分支付 (防止用户金钱支付时突然不想兑换的情况)
			String body = model.getName();
			JSONObject attObj = new JSONObject();
			attObj.put("score", model.getScore());
			attObj.put("userId", userId);
			attObj.put("loginCode", loginCode);
			attObj.put("cardCouponId", model.getCardCouponId());
			// 进行支付
			resMap = orderPayService.payHandle(payType, body, attObj.toString(), model.getPrice(), model.getId(), spbill_create_ip, userId,
					openid, null, passsword);

			if ("error".equals(resMap.get("status"))) {
				if ("10001".equals(resMap.get("error_code"))) {
					resMap.put("message", "构造支付请求数据对象错误，请重新进行微信授权.");
				} else if ("10003".equals(resMap.get("error_code"))) {
					resMap.put("message", "支付请求API返回的数据签名验证失败，有可能数据被篡改了.");
				} else if ("20004".equals(resMap.get("error_code"))) {
					resMap.put("message", "账户余额不足，付款失败.");
				} else {
					resMap.put("message", "系统错误.");
				}
				Util.writeError(resMap);
				return resMap;
			}

			// 如果是钱包支付完成后更新订单状态
			/*
			 * if (payType == Constants.PAY_TYPE.PURSE) { boolean result =
			 * scoreOrderService.updatePayScoreOrderStatus(userId,
			 * model.getCardCouponId(), model.getId(),
			 * Constants.PAY_TYPE.PURSE); }
			 */
			logger.info("钱包支付已完成，积分兑换订单所有处理操作已完成.");
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
		}
		return resMap;
	}

	/**
	 * 查询我能兑换的积分商品列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getCanExchangeList")
	@ResponseBody
	public Object getCanExchangeList(HttpServletRequest request, ScoreProductQuery query) {
		Map<String, Object> resMap = new HashMap<String, Object>();

		if (query.getScore() == null) {
			resMap.put("message", "score为必传字段");
			Util.writeError(resMap);
			return resMap;
		}
		try {
			List<ScoreProduct> scoreProductList = scoreProductService.selectCanExchangeList(query);
			resMap.put("resultData", scoreProductList);
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
		}
		return resMap;
	}

	/**
	 * 查看积分商品详情
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getScoreProductDetail")
	@ResponseBody
	public Object getScoreProductDetail(HttpServletRequest request, @RequestParam(value = "id", required = true) Long id) {
		Map<String, Object> resMap = new HashMap<String, Object>();

		try {
			ScoreProduct scoreProduct = scoreProductService.selectById(id);
			resMap.put("resultData", scoreProduct);
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
		}
		return resMap;
	}

	/**
	 * 兑换记录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getScoreOrderList")
	@ResponseBody
	public Object getScoreOrderList(HttpServletRequest request, ScoreOrderQuery query) {
		Map<String, Object> resMap = new HashMap<String, Object>();

		if (Tools.isEmpty(query.getUserId())) {
			resMap.put("message", "userId为必传字段");
			Util.writeFail(resMap);
			return resMap;
		}
		try {
			List<ScoreOrder> scoreOrderList = scoreOrderService.selectExchangeRecordList(query);
			resMap.put("resultData", scoreOrderList);
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
		}
		return resMap;
	}

	/**
	 * 查看积分订单详情
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getScoreOrderDetail")
	@ResponseBody
	public Object getScoreOrderDetail(HttpServletRequest request, Long scoreOrderId) {
		Map<String, Object> resMap = new HashMap<String, Object>();

		if (scoreOrderId == null || scoreOrderId.longValue() == 0) {
			resMap.put("message", "scoreOrderId为必传字段");
			Util.writeFail(resMap);
			return resMap;
		}
		try {
			ScoreOrder scoreOrder = scoreOrderService.selectById(scoreOrderId);
			resMap.put("resultData", scoreOrder);
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
		}
		return resMap;
	}
}
