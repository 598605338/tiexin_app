package com.linjia.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linjia.base.controller.BaseController;
import com.linjia.constants.CardCouponEnum;
import com.linjia.constants.Constants;
import com.linjia.jpos.common.Configure;
import com.linjia.tools.DateComFunc;
import com.linjia.tools.JSONUtil;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.model.CardCoupon;
import com.linjia.web.model.CardCouponProduct;
import com.linjia.web.model.JPOSVoucherCheck;
import com.linjia.web.model.ReceiveCardCoupon;
import com.linjia.web.model.UserCardCoupon;
import com.linjia.web.query.ReceiveCardCouponQuery;
import com.linjia.web.query.UserCardCouponQuery;
import com.linjia.web.service.CardCouponProductService;
import com.linjia.web.service.CardCouponService;
import com.linjia.web.service.ReceiveCardCouponService;
import com.linjia.web.service.UserCardCouponService;

/**
 * 我的优惠券模块
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/userCardCoupon")
public class UserCardCouponController extends BaseController {

	@Autowired
	private CardCouponService cardCouponService;

	@Autowired
	private UserCardCouponService userCardCouponService;

	@Autowired
	private ReceiveCardCouponService receiveCardCouponService;

	@Autowired
	private CardCouponProductService cardCouponProductService;

	/**
	 * 查询用户未过期的优惠券
	 * lixinling 
	 * 2016年8月15日 上午9:49:32
	 * @param userId
	 * @return
	 */
	@RequestMapping("/getNotExpiredByUserId")
	@ResponseBody
	public Object getNotExpiredByUserId(HttpServletRequest request, UserCardCouponQuery query) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			List<CardCoupon> cardCouponList = cardCouponService.selectNotExpiredByUserId(query);
			resMap.put("resultData", cardCouponList);
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
			e.printStackTrace();
		}
		return resMap;
	}

	/**
	 * 查询用户已过期的优惠券
	 * lixinling 
	 * 2016年8月15日 上午9:49:32
	 * @param userId
	 * @return
	 */
	@RequestMapping("/getExpiredByUserId")
	@ResponseBody
	public Object getExpiredByUserId(HttpServletRequest request, UserCardCouponQuery query) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			List<CardCoupon> cardCouponList = cardCouponService.selectExpiredByUserId(query);
			resMap.put("resultData", cardCouponList);
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
			e.printStackTrace();
		}
		return resMap;
	}

	/**
	 * 查询用户已使用的优惠券
	 * lixinling 
	 * 2016年8月15日 上午9:49:32
	 * @param userId
	 * @return
	 */
	@RequestMapping("/getUsedByUserId")
	@ResponseBody
	public Object getUsedByUserId(HttpServletRequest request, UserCardCouponQuery query) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			List<CardCoupon> cardCouponList = cardCouponService.selectUsedByUserId(query);
			resMap.put("resultData", cardCouponList);
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
			e.printStackTrace();
		}
		return resMap;
	}

	/**
	 * 根据Id更新用户优惠券使用状态
	 * lixinling 
	 * 2016年8月15日 上午9:49:32
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/updateUseStatusByPrimaryKey", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Object updateUseStatusByPrimaryKey(HttpServletRequest request, @RequestBody UserCardCoupon model) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		// 验证参数
		if (model.getId() == null || model.getId().longValue() == 0) {
			resMap.put("message", "id为必传参数且应为有效优惠券id");
			Util.writeError(resMap);
			return resMap;
		}
		if (Tools.isEmpty(model.getMallCode())) {
			resMap.put("message", "mallCode为必传参数");
			Util.writeError(resMap);
			return resMap;
		}
		if (model.getCardCouponPrice() == null || model.getCardCouponPrice().doubleValue() == 0.0) {
			resMap.put("message", "cardCouponPrice为必传参数");
			Util.writeError(resMap);
			return resMap;
		}
		if (model.getGroupId() == null || model.getGroupId().longValue() == 0) {
			resMap.put("message", "groupId为必传参数");
			Util.writeError(resMap);
			return resMap;
		}
		if (model.getIsOnline() == null || model.getIsOnline() > 1 || model.getIsOnline() < 0) {
			resMap.put("message", "isOnline为必传参数且符合要求");
			Util.writeError(resMap);
			return resMap;
		}
		if (Tools.isEmpty(model.getUserId())) {
			resMap.put("message", "userId为必传参数");
			Util.writeError(resMap);
			return resMap;
		}

		try {
			int count = userCardCouponService.checkValid(model.getId(), model.getUserId());
			if (count == 0) {
				resMap.put("message", "优惠券已使用或已过期，请选择别的优惠券。");
				Util.writeError(resMap);
				return resMap;
			}

			model.setUseStatus(Constants.CARD_USED_STATUS.USED);
			int row = userCardCouponService.updateUseStatusByPrimaryKey(model);
			if (row == 1) {
				Util.writeSuccess(resMap);
				Util.writeOk(resMap);
			} else {
				resMap.put("message", "更新失败");
				Util.writeError(resMap);
			}
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
		}
		return resMap;
	}

	/**
	 * 用户领取优惠券
	 * lixinling 
	 * 2016年8月15日 上午9:49:32
	 * @param userId
	 * @return
	 */
	@RequestMapping("/receiveCardCoupon")
	@ResponseBody
	public Object receiveCardCoupon(HttpServletRequest request, Long id, String userId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (id == null || id.longValue() == 0 || Tools.isEmpty(userId)) {
			resMap.put("message", "id和userId都为必传参数");
			Util.writeError(resMap);
			return resMap;
		}
		ReceiveCardCoupon receiveCardCoupon = receiveCardCouponService.selectById(id);
		if (receiveCardCoupon == null) {
			resMap.put("message", "id无效");
			Util.writeError(resMap);
			return resMap;
		}
		try {
			if (receiveCardCoupon.getPublishNum() != null && receiveCardCoupon.getPublishNum() >= 1) {
				// 新增用户领取优惠券操作并更新发布数量
				receiveCardCouponService.insertCardCouponForUser(userId, receiveCardCoupon.getCardCouponId(), id,
						receiveCardCoupon.getPublishNum());
				Util.writeSuccess(resMap);
				Util.writeOk(resMap);
			} else {
				resMap.put("message", "优惠券已领完");
				Util.writeError(resMap);
			}
		} catch (Exception e) {
			resMap.put("message", "系统错误");
			Util.writeError(resMap);
		}
		return resMap;
	}

	/**
	 * 查询领券中心数据列表
	 * lixinling 
	 * 2016年8月15日 上午9:49:32
	 * @param userId
	 * @return
	 */
	@RequestMapping("/getReceiveCardPageList")
	@ResponseBody
	public Object getReceiveCardPageList(HttpServletRequest request, ReceiveCardCouponQuery query) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			List<ReceiveCardCoupon> receiveCardCouponList = receiveCardCouponService.selectByPageList(query);
			resMap.put("resultData", receiveCardCouponList);
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
			e.printStackTrace();
		}
		return resMap;
	}

	/**
	 * 验证券是否符合门店限制
	 * lixinling 
	 * 2016年9月23日 上午9:49:32
	 * @param userId
	 * @return true 符合；false 不符合
	 */
	public boolean checkCouponMallValid(Long cardId, String mallCode) {
		CardCoupon cardCoupon = cardCouponService.cardCheckByPrimaryKey(cardId);

		// 门店支持类型：1.全部门店 2.部分门店
		if (cardCoupon.getMallSupportType() == 2) {
			if (cardCoupon.getMallCode() == null) {
				logger.info("券不符合规则,取得的支持门店列表为空");
				return false;
			} else {
				// 判断支持的门店列表中是否包含此门店
				String[] mallCodeArray = cardCoupon.getMallCode().split(",");
				if (!Arrays.asList(mallCodeArray).contains(mallCode)) {
					logger.info("券不符合规则,取得的支持门店列表为空");
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 券校验接口
	 * lixinling 
	 * 2016年9月21日 下午3:42:04
	 * @param request
	 * @param voucherNumber 券号
	 * @param storeNumber 门店
	 * @param requestAccount 账号
	 * @param signParam 签名数据
	 * @return
	 */
	@RequestMapping(value = "/checkcodea")
	@ResponseBody
	public Object checkcodea(HttpServletRequest request, JPOSVoucherCheck model) {

		try {
			String params = IOUtils.toString(request.getInputStream(), "UTF-8");
			params = URLDecoder.decode(params, "UTF-8");
			logger.debug("params============================" + params);
			if (!Tools.isEmpty(params)) {
				model = JSONUtil.fromJson(params, JPOSVoucherCheck.class);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (model == null || Tools.isEmpty(model.getVoucherNumber()) || Tools.isEmpty(model.getStoreNumber())
				|| Tools.isEmpty(model.getRequestAccount()) || Tools.isEmpty(model.getSignParam())) {
			logger.debug("===============================================voucherNumber:" + model.getVoucherNumber() + "storeNumber:"
					+ model.getStoreNumber() + "requestAccount:" + model.getRequestAccount() + "signParam:" + model.getSignParam());
			logger.debug("===============================================request.getContentType();" + request.getContentType());
			resMap.put("errorMsg", "访问必要参数验证错误");
			resMap.put("resultCode", "x6005");
			return resMap;
		}

		if (Configure.checkSign(model.getStoreNumber(), model.getSignParam())) {
			// if (true) {
			logger.info("数据签名验证成功");
			UserCardCoupon userCardCoupon = userCardCouponService.selectById(Long.valueOf(model.getVoucherNumber()));

			if (userCardCoupon != null) {
				// 验证优惠券使用状态
				if (userCardCoupon.getUseStatus() == Constants.CARD_USED_STATUS.UNUSED) {
					// 验证是否有门店限制
					if (!checkCouponMallValid(userCardCoupon.getCardCouponId(), model.getStoreNumber())) {
						resMap.put("errorMsg", "券不符合规则");
						resMap.put("resultCode", "x6004");
						return resMap;
					}
					// 商品的限制交给门店人员判断（目前运营部都是全部商品的情况）

					Date useStartTime = userCardCoupon.getUseStartTime();
					Date useEndTime = userCardCoupon.getUseEndTime();
					Date currentTime = new Date();
					if (DateComFunc.compareDate(useStartTime, currentTime) < 0 && DateComFunc.compareDate(useEndTime, currentTime) > 0) {
						logger.info("优惠券校验正常。");
						resMap.put("voucherNumber", model.getVoucherNumber());
						resMap.put("storeNumber", model.getStoreNumber());
						resMap.put("requestAccount", model.getRequestAccount());
						resMap.put("signParam", Configure.getSign(model.getStoreNumber()));
						resMap.put("resultCode", "1");
						resMap.put("errorMsg", "校验成功");
						resMap.put("totalMoney", String.valueOf(userCardCoupon.getCardCouponPrice()));
						resMap.put("onLineMoney", String.valueOf(userCardCoupon.getCardCouponPrice()));
						resMap.put("offLineMoney", "0");
						resMap.put("voucherTypeName", CardCouponEnum.getNameByCode(userCardCoupon.getCardType()));
						resMap.put("voucherTypeCode", "0" + userCardCoupon.getCardType());

						Long cardCouponId = userCardCoupon.getCardCouponId();
						CardCouponProduct cardCouponProduct = cardCouponProductService.selectDetailBycardCouponId(cardCouponId,
								model.getStoreNumber());
						List<Map<String, Object>> orderProducts = new ArrayList<Map<String, Object>>();
						Map<String, Object> orderProduct = new HashMap<String, Object>();
						// 如果是商品券，返回商品行数据
						if (userCardCoupon.getCardType() == Constants.CARD_COUPON_TYPE.SPQ) {
							// 该商品券关联的商品信息
							if (cardCouponProduct != null) {
								orderProduct.put("lineNumber", String.valueOf(cardCouponProduct.getProductId()));// 排序序号
								orderProduct.put("productNumber", cardCouponProduct.getpCode());
								orderProduct.put("productName", cardCouponProduct.getpName());
								orderProduct.put("norm", "");// 规格
								orderProduct.put("quantity", "1");
								orderProduct.put("initPrice", String.valueOf(cardCouponProduct.getSalePrice()));
								orderProduct.put("nowPrice", String.valueOf(new BigDecimal("0")));
								orderProduct.put("payMoney", String.valueOf(new BigDecimal("0")));
								orderProducts.add(orderProduct);
								resMap.put("orderProducts", orderProducts);
							} else {
								logger.info("获取商品行数据为空");
								resMap.put("errorMsg", "券不符合规则");
								resMap.put("resultCode", "x6004");
								return resMap;
							}
						}
						/*
						 * else if (userCardCoupon.getCardType() ==
						 * Constants.CARD_COUPON_TYPE.DJQ) { // 该代金券关联的商品信息
						 * orderProduct.put("lineNumber",
						 * cardCouponProduct.getProductId());
						 * orderProduct.put("productNumber",
						 * cardCouponProduct.getpCode());
						 * orderProduct.put("productName",
						 * cardCouponProduct.getpName());
						 * orderProduct.put("norm",
						 * cardCouponProduct.getpName());
						 * orderProduct.put("quantity", "1");
						 * orderProduct.put("initPrice",
						 * cardCouponProduct.getSalePrice());
						 * 
						 * // 计算所需实付款金额 BigDecimal nowPrice =
						 * cardCouponProduct.getSalePrice
						 * ().subtract(userCardCoupon.getCardCouponPrice());
						 * orderProduct.put("nowPrice", nowPrice);
						 * orderProduct.put("payMoney", nowPrice);
						 * orderProducts.add(orderProduct); }
						 */
					} else {
						logger.info("优惠券:" + model.getVoucherNumber() + "---该优惠券已过期");
						resMap.put("errorMsg", "该优惠券已过期");
						resMap.put("resultCode", "x6002");
						return resMap;
					}

				} else {
					logger.info("优惠券:" + model.getVoucherNumber() + "---该优惠券已使用,不能重复使用");
					resMap.put("errorMsg", "券已经使用过");
					resMap.put("resultCode", "x6003");
					return resMap;
				}
			} else {
				logger.info("优惠券:" + model.getVoucherNumber() + "---该优惠券不存在");
				resMap.put("errorMsg", "券号不存在");
				resMap.put("resultCode", "x6001");
				return resMap;
			}
		} else {
			logger.error("【JPOS请求失败】数据签名验证失败，有可能数据被篡改了");
			resMap.put("errorMsg", "【JPOS请求失败】数据签名验证失败，有可能数据被篡改了");
			resMap.put("resultCode", "x6005");
			return resMap;
		}

		return resMap;
	}

	/**
	 * 券消费接口
	 * lixinling 
	 * 2016年9月22日 下午3:42:04
	 * @param request
	 * @param voucherNumber 券号
	 * @param storeNumber 门店
	 * @param requestAccount 账号
	 * @param billAmount 订单金额
	 * @param signParam 签名数据
	 * @return
	 */
	@RequestMapping("/voucherPayCheck")
	@ResponseBody
	public Object voucherPayCheck(HttpServletRequest request, JPOSVoucherCheck model) {

		try {
			String params = IOUtils.toString(request.getInputStream(), "UTF-8");
			params = URLDecoder.decode(params, "UTF-8");
			logger.debug("params============================" + params);

			if (!Tools.isEmpty(params)) {
				model = JSONUtil.fromJson(params, JPOSVoucherCheck.class);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<String, Object> resMap = new HashMap<String, Object>();
		if (model == null || Tools.isEmpty(model.getVoucherNumber()) || Tools.isEmpty(model.getStoreNumber())
				|| Tools.isEmpty(model.getRequestAccount()) || Tools.isEmpty(model.getBillAmount()) || Tools.isEmpty(model.getSignParam())) {
			logger.info("voucherNumber:" + model.getVoucherNumber() + "storeNumber:" + model.getStoreNumber() + "requestAccount:"
					+ model.getRequestAccount() + "signParam:" + model.getSignParam());
			resMap.put("errorMsg", "访问必要参数验证错误");
			resMap.put("resultCode", "x6005");
			return resMap;
		}

		if (Configure.checkSign(model.getStoreNumber(), model.getSignParam())) {
			// if (true) {
			boolean flag = false;
			logger.info("数据签名验证成功");
			UserCardCoupon userCardCoupon = userCardCouponService.selectById(Long.valueOf(model.getVoucherNumber()));

			if (userCardCoupon != null) {
				if (userCardCoupon.getUseStatus() == Constants.CARD_USED_STATUS.UNUSED) {
					Date useStartTime = userCardCoupon.getUseStartTime();
					Date useEndTime = userCardCoupon.getUseEndTime();
					Date currentTime = new Date();
					if (DateComFunc.compareDate(useStartTime, currentTime) < 0 && DateComFunc.compareDate(useEndTime, currentTime) > 0) {
						logger.info("优惠券校验正常。");

						if (userCardCoupon.getCardType() == Constants.CARD_COUPON_TYPE.SPQ) {
							if (new BigDecimal(model.getBillAmount()).doubleValue() == 0) {
								logger.info("券消费检查校验成功。");
								flag = true;
							} else {
								logger.info("券消费检查订单金额不相符。");
								resMap.put("errorMsg", "访问必要参数验证错误");
								resMap.put("resultCode", "x6005");
								return resMap;
							}
						} else {
							// 计算所需实付款金额
							// BigDecimal nowPrice =
							// cardCouponProduct.getSalePrice().subtract(userCardCoupon.getCardCouponPrice());
							// 优惠券的金额==传入的金额
							if (userCardCoupon.getCardCouponPrice().doubleValue() == (new BigDecimal(model.getBillAmount())).doubleValue()) {
								logger.info("券消费检查校验成功。");
								flag = true;
							} else {
								logger.info("券消费传入的订单金额和优惠券金额不相等。");
								resMap.put("errorMsg", "访问必要参数验证错误");
								resMap.put("resultCode", "x6005");
								return resMap;
							}
						}

						if (flag) {
							resMap.put("storeNumber", model.getStoreNumber());
							resMap.put("requestAccount", model.getRequestAccount());
							resMap.put("signParam", Configure.getSign(model.getStoreNumber()));
							resMap.put("resultCode", "1");
							resMap.put("errorMsg", "校验成功");
						}
					} else {
						logger.info("优惠券:" + model.getVoucherNumber() + "---该优惠券已过期");
						resMap.put("errorMsg", "该优惠券已过期");
						resMap.put("resultCode", "x6002");
						return resMap;
					}

				} else {
					logger.info("优惠券:" + model.getVoucherNumber() + "---该优惠券已使用,不能重复使用");
					resMap.put("errorMsg", "券已经使用过");
					resMap.put("resultCode", "x6003");
					return resMap;
				}
			} else {
				logger.info("优惠券:" + model.getVoucherNumber() + "---该优惠券不存在");
				resMap.put("errorMsg", "券号不存在");
				resMap.put("resultCode", "x6001");
				return resMap;
			}
		} else {
			logger.error("【JPOS请求失败】数据签名验证失败，有可能数据被篡改了");
			resMap.put("errorMsg", "【JPOS请求失败】数据签名验证失败，有可能数据被篡改了");
			resMap.put("resultCode", "x6005");
			return resMap;
		}

		return resMap;
	}

	/**
	 * 券核销接口
	 * lixinling 
	 * 2016年9月22日 下午3:42:04
	 * @param request
	 * @param voucherNumber 券号
	 * @param storeNumber 门店
	 * @param requestAccount 账号
	 * @param signParam 签名数据
	 * @return
	 */
	@RequestMapping("/voucherVerification")
	@ResponseBody
	public Object voucherVerification(HttpServletRequest request, JPOSVoucherCheck model) {

		try {
			String params = IOUtils.toString(request.getInputStream(), "UTF-8");
			params = URLDecoder.decode(params, "UTF-8");
			logger.debug("params============================" + params);

			if (!Tools.isEmpty(params)) {
				model = JSONUtil.fromJson(params, JPOSVoucherCheck.class);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<String, Object> resMap = new HashMap<String, Object>();
		if (model == null || Tools.isEmpty(model.getVoucherNumber()) || Tools.isEmpty(model.getStoreNumber())
				|| Tools.isEmpty(model.getRequestAccount()) || Tools.isEmpty(model.getSignParam())) {
			logger.info("voucherNumber:" + model.getVoucherNumber() + "storeNumber:" + model.getStoreNumber() + "requestAccount:"
					+ model.getRequestAccount() + "signParam:" + model.getSignParam());
			resMap.put("errorMsg", "访问必要参数验证错误");
			resMap.put("resultCode", "x6005");
			return resMap;
		}

		if (Configure.checkSign(model.getStoreNumber(), model.getSignParam())) {
			// if (true) {
			logger.info("数据签名验证成功");
			// 更改券状态
			UserCardCoupon userCardCoupon = new UserCardCoupon();
			userCardCoupon.setId(Long.valueOf(model.getVoucherNumber()));
			userCardCoupon.setUseStatus(Constants.CARD_USED_STATUS.USED);
			userCardCoupon.setMallCode(model.getStoreNumber());
			userCardCoupon.setIsOnline(Constants.CARD_USED_PLACE.OFFLINE);
			if (!Tools.isEmpty(model.getTranId())) {
				userCardCoupon.setTranId(model.getTranId());
			}
			int row = userCardCouponService.updateUseStatusByPrimaryKey(userCardCoupon);
			if (row == 1) {
				resMap.put("storeNumber", model.getStoreNumber());
				resMap.put("requestAccount", model.getRequestAccount());
				resMap.put("voucherNumber", model.getVoucherNumber());
				resMap.put("signParam", Configure.getSign(model.getStoreNumber()));
				resMap.put("resultCode", "1");
				resMap.put("errorMsg", "校验成功");
			}

		} else {
			logger.error("【JPOS请求失败】数据签名验证失败，有可能数据被篡改了");
			resMap.put("errorMsg", "【JPOS请求失败】数据签名验证失败，有可能数据被篡改了");
			resMap.put("resultCode", "x6005");
			return resMap;
		}

		return resMap;
	}

	/**
	 * 券冲正接口
	 * 1.	终端 进行冲账撤销.
	 * 2.	终端 异步call VoucherCancel,直到 return成功. 
	 * lixinling 
	 * 2016年9月22日 下午3:42:04
	 * @param request
	 * @param voucherNumber 券号
	 * @param storeNumber 门店
	 * @param requestAccount 账号
	 * @param signParam 签名数据
	 * @return
	 */
	@RequestMapping("/voucherCancel")
	@ResponseBody
	public Object voucherCancel(HttpServletRequest request, JPOSVoucherCheck model) {

		try {
			String params = IOUtils.toString(request.getInputStream(), "UTF-8");
			params = URLDecoder.decode(params, "UTF-8");
			logger.debug("params============================" + params);

			if (!Tools.isEmpty(params)) {
				model = JSONUtil.fromJson(params, JPOSVoucherCheck.class);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<String, Object> resMap = new HashMap<String, Object>();
		if (model == null || Tools.isEmpty(model.getVoucherNumber()) || Tools.isEmpty(model.getStoreNumber())
				|| Tools.isEmpty(model.getRequestAccount()) || Tools.isEmpty(model.getSignParam())) {
			logger.info("voucherNumber:" + model.getVoucherNumber() + "storeNumber:" + model.getStoreNumber() + "requestAccount:"
					+ model.getRequestAccount() + "signParam:" + model.getSignParam());
			resMap.put("errorMsg", "访问必要参数验证错误");
			resMap.put("resultCode", "x6005");
			return resMap;
		}

		if (Configure.checkSign(model.getStoreNumber(), model.getSignParam())) {
			// if (true) {
			logger.info("数据签名验证成功");
			// 更改券状态
			UserCardCoupon userCardCoupon = new UserCardCoupon();
			userCardCoupon.setId(Long.valueOf(model.getVoucherNumber()));
			userCardCoupon.setUseStatus(Constants.CARD_USED_STATUS.UNUSED);
			// userCardCoupon.setMallCode(model.getStoreNumber());
			// userCardCoupon.setIsOnline(Constants.CARD_USED_PLACE.OFFLINE);
			// if(!Tools.isEmpty(model.getTranId())){
			// userCardCoupon.setGroupId(Long.valueOf(model.getTranId()));
			// }
			int row = userCardCouponService.updateUseStatusByPrimaryKey(userCardCoupon);
			if (row == 1) {
				resMap.put("storeNumber", model.getStoreNumber());
				resMap.put("requestAccount", model.getRequestAccount());
				resMap.put("voucherNumber", model.getVoucherNumber());
				resMap.put("signParam", Configure.getSign(model.getStoreNumber()));
				resMap.put("resultCode", "1");
				resMap.put("errorMsg", "校验成功");
			}

		} else {
			logger.error("【JPOS请求失败】数据签名验证失败，有可能数据被篡改了");
			resMap.put("errorMsg", "【JPOS请求失败】数据签名验证失败，有可能数据被篡改了");
			resMap.put("resultCode", "x6005");
			return resMap;
		}

		return resMap;
	}

	/**
	 * 用户使用商品券
	 * lixinling 
	 * 2016年11月8日 上午11:22:00
	 * @param id
	 * @param mallCode
	 * @return
	 */
	@RequestMapping("/useProductCardCoupon")
	@ResponseBody
	public Object useProductCardCoupon(@RequestParam(value = "id", required = true) Long id, String mallCode) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();

		UserCardCoupon userCardCoupon = userCardCouponService.selectById(id);

		if (userCardCoupon != null) {
			// 验证优惠券使用状态
			if (userCardCoupon.getUseStatus() == Constants.CARD_USED_STATUS.UNUSED) {
				// 验证是否有门店限制
				if (!checkCouponMallValid(userCardCoupon.getCardCouponId(), mallCode)) {
					resMap.put("message", "该门店不支持此券使用");
					resMap.put("status", "error");
					return resMap;
				}

				// 商品的限制交给门店人员判断（目前运营部都是全部商品的情况）
				Date useStartTime = userCardCoupon.getUseStartTime();
				Date useEndTime = userCardCoupon.getUseEndTime();
				Date currentTime = new Date();
				if (DateComFunc.compareDate(useStartTime, currentTime) < 0 && DateComFunc.compareDate(useEndTime, currentTime) > 0) {
					logger.info("优惠券校验正常。");

					Long cardCouponId = userCardCoupon.getCardCouponId();
					CardCouponProduct cardCouponProduct = cardCouponProductService.selectDetailBycardCouponId(cardCouponId, mallCode);
					// 如果是商品券，返回商品行数据
					if (userCardCoupon.getCardType() == Constants.CARD_COUPON_TYPE.SPQ) {
						// 该商品券关联的商品信息
						if (cardCouponProduct != null) {
							resultData.put("cardCouponProduct", cardCouponProduct);
							resMap.put("resultData", resultData);
						} else {
							logger.info("商品券:" + id + "---对应的商品不存在");
							resMap.put("message", "该商品券对应的商品不存在");
							resMap.put("status", "error");
							return resMap;
						}
					} else {
						logger.info("优惠券:" + id + "---该优惠券不是商品券");
						resMap.put("message", "该优惠券不是商品券");
						resMap.put("status", "error");
						return resMap;
					}

				} else {
					logger.info("优惠券:" + id + "---该优惠券已过期,不能使用");
					resMap.put("message", "券已过期");
					resMap.put("status", "error");
					return resMap;
				}
			} else {
				logger.info("优惠券:" + id + "---该优惠券已使用过,不可重复使用");
				resMap.put("message", "该优惠券已使用过,不可重复使用");
				resMap.put("status", "error");
				return resMap;
			}
		} else {
			resMap.put("message", "取得的用户卡券信息为空");
			resMap.put("status", "error");
			return resMap;
		}

		Util.writeOk(resMap);
		Util.writeSuccess(resMap);
		return resMap;
	}
}
