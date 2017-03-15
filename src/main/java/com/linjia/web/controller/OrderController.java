package com.linjia.web.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linjia.base.controller.BaseController;
import com.linjia.constants.Constants;
import com.linjia.tools.DateComFunc;
import com.linjia.tools.JSONUtil;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.model.ActivityInfo;
import com.linjia.web.model.ActivityTradeProduct;
import com.linjia.web.model.Address;
import com.linjia.web.model.CardCoupon;
import com.linjia.web.model.MallMaster;
import com.linjia.web.model.OrderGroup;
import com.linjia.web.model.OrderGroupProduct;
import com.linjia.web.model.OrderProductModel;
import com.linjia.web.model.Product;
import com.linjia.web.model.ShoppingCar;
import com.linjia.web.query.OrderGroupQuery;
import com.linjia.web.query.SubmitOrderParamBean;
import com.linjia.web.query.UserCardCouponQuery;
import com.linjia.web.service.ActivityPromotionService;
import com.linjia.web.service.ActivityTradeProductService;
import com.linjia.web.service.AddressService;
import com.linjia.web.service.CardCouponProductService;
import com.linjia.web.service.CardCouponService;
import com.linjia.web.service.MallMasterService;
import com.linjia.web.service.OrderGroupProductService;
import com.linjia.web.service.OrderGroupService;
import com.linjia.web.service.OrderRefundService;
import com.linjia.web.service.ProductService;
import com.linjia.web.service.ShoppingCarService;
import com.linjia.web.service.TakeGoodsCodeGenerateService;
import com.linjia.web.uhd123.common.Configure;
import com.linjia.web.uhd123.service.UhdOrderService;

/**
 * 订单模块
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {

	@Autowired
	private OrderGroupService orderGroupService;
	@Autowired
	private OrderGroupProductService orderGroupProductService;
	@Autowired
	private ShoppingCarService shoppingCarService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private MallMasterService mallMasterService;
	@Autowired
	private CardCouponService cardCouponService;
	@Autowired
	private TakeGoodsCodeGenerateService takeGoodsCodeGenerateService;
	@Autowired
	private OrderRefundService orderRefundService;
	@Autowired
	private ActivityPromotionService activityPromotionService;
	@Autowired
	private CardCouponProductService cardCouponProductService;
	@Autowired
	private ProductService productService;
	@Autowired
	private UhdOrderService uhdOrderService;
	@Autowired
	private ActivityTradeProductService activityTradeProductService;

	/**
	 * 提交订单
	 * lixinling 
	 * 2016年8月25日 下午7:33:50
	 * @param request
	 * @param param
	 * @param userId
	 * @return
	 */
	@RequestMapping("/submitOrder")
	@ResponseBody
	public Object submitOrder(HttpServletRequest request, SubmitOrderParamBean param, String userId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		try {
			@SuppressWarnings("unchecked")
			List<String> pCodeArray = JSONUtil.fromJson(param.getpCodeArray(), List.class);
			String mallCode = param.getMallCode();

			if (pCodeArray == null || pCodeArray.size() == 0) {
				logger.debug("pCodeArray is null 请先选择结算的商品");
				resMap.put("message", "请先选择结算的商品");
				Util.writeError(resMap);
				return resMap;
			}

			if (Tools.isEmpty(userId) || Tools.isEmpty(mallCode)) {
				logger.debug("userId或mallCode为空");
				resMap.put("message", "userId或mallCode为空");
				Util.writeError(resMap);
				return resMap;
			}

			Map<String, Object> queryP = new HashMap<String, Object>();
			queryP.put("userId", userId);
			queryP.put("pCodeArray", pCodeArray);

			// 查询结算商品信息
			List<ShoppingCar> shoppingCarList = shoppingCarService.selectCarProductByChecked(queryP);
			if (shoppingCarList == null || shoppingCarList.size() == 0) {
				logger.debug("需要结算的商品列表为空。");
				resMap.put("message", "请先添加商品");
				Util.writeError(resMap);
				return resMap;
			} else {
				Map<String, Object> totalMap = new HashMap<String, Object>();

//				Map<String, String> activityTradeProductMap = param.getActivityTradeProductMap();

				// 对订单中及时送和预约配送进行分类,判断商品的状态（0库存不足；1正常；），并计算每种商品的总金额
				orderGroupService.handleOrderProduct(request, shoppingCarList, totalMap, mallCode, userId, false);

				// 查询用户最近使用收货地址
				Address address = addressService.selectOneByUserId(userId);
				resultData.put("address", address);

				// 查询门店的相关信息
				MallMaster mallMaster = mallMasterService.selectByMallCode(mallCode);
				if (mallMaster == null) {
					logger.debug("查询门店的信息为空。");
					resMap.put("message", "查询门店的信息为空");
					Util.writeError(resMap);
					return resMap;
				}
				resultData.put("mallMaster", mallMaster);

				// 及时送达对象
				OrderProductModel immediateSendModel = (OrderProductModel) totalMap.get("immediateSendModel");
				// 预约配送对象
				OrderProductModel prepareSendModel = (OrderProductModel) totalMap.get("prepareSendModel");
				// 补货中的商品code
				String immediateLowQtyPCode = (String) totalMap.get("immediateLowQtyPCode");
				String prepareLowQtyPCode = (String) totalMap.get("prepareLowQtyPCode");

				// 如果全部都在补货中，则返回信息
				if (immediateSendModel.getProductNum() == 0 && prepareSendModel.getProductNum() == 0) {
					resMap.put("message", "全部补货中");
					Util.writeError(resMap);
					return resMap;
				} else {
					// 配送费
					BigDecimal sendPrice = mallMaster.getSendPrice();
					if (sendPrice == null)
						sendPrice = new BigDecimal("0.00");

					// 查询用户可用的优惠券
					UserCardCouponQuery userCardCouponQuery = new UserCardCouponQuery();
					userCardCouponQuery.setUserId(userId);
					userCardCouponQuery.setUseStatus(Constants.CARD_USED_STATUS.UNUSED);
					userCardCouponQuery.setStartIndex(0);
					userCardCouponQuery.setPageSize(100);
					List<CardCoupon> cardCouponList = null;
					if (immediateSendModel.getProductNum() > 0) {
						// 商品总金额
						BigDecimal productPrice = immediateSendModel.getProductPrice();
						if (productPrice != null && productPrice.compareTo(new BigDecimal("25")) < 0) {
							logger.debug("商品总金额不足25元，不能下单。");
							resMap.put("message", "商品总金额不足25元，不能下单");
							Util.writeError(resMap);
							return resMap;
						}

						// 当前时间参与的促销活动处理
						orderGroupService.activityCurrentTime(mallCode, userId, productPrice, sendPrice, immediateSendModel);

						// 如果参与了免运费活动，则将配送费改为0.00
						if (immediateSendModel.getIsFreeFreight()) {
							mallMaster.setSendPrice(new BigDecimal("0.00"));
							sendPrice = new BigDecimal("0.00");
						}

						// 合计=商品总金额 + 配送费
						BigDecimal price = sendPrice.add(productPrice);
						immediateSendModel.setPrice(price);

						// 如果参与了订单满减活动
						if (immediateSendModel.getIsFullSubtract()) {
							price = price.subtract(immediateSendModel.getFullSubtractPrice());
						}

						// 实付款
						immediateSendModel.setRealPrice(price);
						immediateSendModel.setSendType(Constants.SEND_TYPE.SHSM);

						// 可用优惠券
						cardCouponList = cardCouponService.selectCanUsedByUserId(userCardCouponQuery, mallCode, pCodeArray,
								immediateSendModel.getIsFullSubtract(), immediateSendModel.getIsFreeFreight(), false,
								immediateSendModel.getIfUseCardCoupons(), productPrice);
						immediateSendModel.setCardCouponList(cardCouponList);

						resultData.put("immediateSendModel", immediateSendModel);
						resultData.put("immediateLowQtyPCode", immediateLowQtyPCode);
					}
					if (prepareSendModel.getProductNum() > 0) {
						// 商品总金额
						BigDecimal productPrice = prepareSendModel.getProductPrice();

						// 当前时间参与的促销活动处理
						orderGroupService.activityCurrentTime(mallCode, userId, productPrice, sendPrice, prepareSendModel);

						// 如果参与了免运费活动，则将配送费改为0.00
						if (prepareSendModel.getIsFreeFreight()) {
							mallMaster.setSendPrice(new BigDecimal("0.00"));
							sendPrice = new BigDecimal("0.00");
						}

						// 合计=商品总金额 + 配送费
						BigDecimal price = sendPrice.add(productPrice);
						prepareSendModel.setPrice(price);

						// 如果参与了订单满减活动
						if (prepareSendModel.getIsFullSubtract()) {
							price = price.subtract(prepareSendModel.getFullSubtractPrice());
						}

						// 实付款
						prepareSendModel.setRealPrice(price);
						prepareSendModel.setSendType(Constants.SEND_TYPE.SHSM);

						// 可用优惠券
						cardCouponList = cardCouponService.selectCanUsedByUserId(userCardCouponQuery, mallCode, pCodeArray,
								prepareSendModel.getIsFullSubtract(), prepareSendModel.getIsFreeFreight(), true,
								immediateSendModel.getIfUseCardCoupons(), productPrice);
						immediateSendModel.setCardCouponList(cardCouponList);

						resultData.put("prepareSendModel", prepareSendModel);
						resultData.put("prepareLowQtyPCode", prepareLowQtyPCode);
					}

					resultData.put("mallMaster", mallMaster);
					resultData.put("cardCouponList", cardCouponList);
				}

			}

			resMap.put("resultData", resultData);
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} catch (Exception e) {
			logger.error("取得提交订单页面数据出错：", e);
			Util.writeFail(resMap);
			Util.writeError(resMap);
		}
		return resMap;
	}

	/**
	 * 确认下单
	 * lixinling 
	 * 2016年8月16日 上午10:18:27
	 * @param request
	 * @param session
	 * @param query
	 * @param userId
	 * @param keepSubmitFlg 继续下单Flg
	 * @return
	 */
	@RequestMapping("/confirmOrder")
	@ResponseBody
	public Object confirmOrder(HttpServletRequest request, OrderGroupQuery query, String userId,
			Map<String, String> activityTradeProductMap, boolean keepSubmitFlg) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		try {
			OrderGroup orderGroup = new OrderGroup();
			orderGroup.setUserId(userId);
			// 订单配送方式：0立即配送；1预约配送；
			int orderSendType = query.getOrderSendType();
			@SuppressWarnings("unchecked")
			List<String> pCodeArray = JSONUtil.fromJson(query.getpCodeArray(), List.class);
			String mallCode = query.getMallCode();

			if (pCodeArray == null || pCodeArray.size() == 0) {
				logger.debug("pCodeArray is null 请先选择结算的商品");
				resMap.put("message", "请先选择结算的商品");
				Util.writeError(resMap);
				return resMap;
			}

			if (Tools.isEmpty(userId) || Tools.isEmpty(mallCode)) {
				logger.debug("userId或mallCode为空");
				resMap.put("message", "userId或mallCode为空");
				Util.writeError(resMap);
				return resMap;
			}

			// 门店营业状态 1、启用 0、禁用
			Integer useFlg = mallMasterService.selectUseFlgByMallCode(mallCode);
			if (useFlg == 0) {
				logger.debug("该门店处于休息状态，暂不支持下单。");
				resultData.put("error_code", Constants.ORDER_RETURN_CODE.MALL_CLOSED);
				resultData.put("error_msg", "该门店处于休息状态，暂不支持下单");
				resMap.put("resultData", resultData);
				resMap.put("message", "该门店处于休息状态，暂不支持下单");
				Util.writeError(resMap);
				return resMap;
			}

			// 使用的优惠券Id
			Long userCardCouponId = query.getUserCardCouponId();
			CardCoupon cardCoupon = null;
			// 商品券对应的商品
			ShoppingCar spqShoppingCar = null;
			// 校验优惠券的合法性
			if (userCardCouponId != null && userCardCouponId.longValue() > 0) {
				UserCardCouponQuery userCardCouponQuery = new UserCardCouponQuery();
				userCardCouponQuery.setId(userCardCouponId);
				userCardCouponQuery.setUserId(query.getUserId());
				userCardCouponQuery.setUseStatus(Constants.CARD_USED_STATUS.UNUSED);
				cardCoupon = cardCouponService.checkCouponByUserCardCouponId(userCardCouponQuery);

				if (cardCoupon == null) {
					logger.debug("传入的优惠券不存在。");
					resultData.put("error_code", Constants.ORDER_RETURN_CODE.NO_CARD_COUPON);
					resultData.put("error_msg", "传入的优惠券不存在");
					resMap.put("resultData", resultData);
					resMap.put("message", "传入的优惠券不存在");
					Util.writeError(resMap);
					return resMap;
				}
				// 判断商品券中的商品是否有库存
				if (cardCoupon != null && cardCoupon.getCardType() == Constants.CARD_COUPON_TYPE.SPQ) {
					String pCode = cardCouponProductService.selectPCodeByuserCardCouponId(cardCoupon.getCardId());
					int qty = productService.getCurrentQty(0L, pCode, mallCode);
					if (qty <= 0) {
						logger.debug("商品券对应的商品库存不足。");
						resultData.put("error_code", Constants.ORDER_RETURN_CODE.LOW_CARD_COUPON_PRODUCT);
						resultData.put("error_msg", "商品券对应的商品库存不足,请稍后再试");
						resMap.put("resultData", resultData);
						resMap.put("message", "商品券对应的商品库存不足,请稍后再试");
						Util.writeError(resMap);
						return resMap;
					} else {
						Product product = productService.selectByPCode(pCode);
						if (product == null) {
							logger.debug("该商品券对应的商品不存在。");
							resultData.put("error_code", Constants.ORDER_RETURN_CODE.NO_CARD_COUPON_PRODUCT);
							resultData.put("error_msg", "该商品券对应的商品不存在,请稍后再试");
							resMap.put("resultData", resultData);
							resMap.put("message", "该商品券对应的商品不存在,请稍后再试");
							Util.writeError(resMap);
							return resMap;
						} else {
							spqShoppingCar = new ShoppingCar();
							spqShoppingCar.setProductId(product.getId());
							spqShoppingCar.setpCode(product.getpCode());
							spqShoppingCar.setpName(product.getName());
							spqShoppingCar.setUserId(userId);
							spqShoppingCar.setQuantity(1);
							// spqShoppingCar.setCreDate(new Date());
							spqShoppingCar.setpSendType(product.getpSendType());
							BigDecimal spqProductPrice = new BigDecimal("0.00");
							spqShoppingCar.setProductPrice(spqProductPrice);
							spqShoppingCar.setItemPrice(spqProductPrice);
							spqShoppingCar.setDiscountAmount(spqProductPrice);
							spqShoppingCar.setApportedDiscountAmount(spqProductPrice);
							spqShoppingCar.setItemTotalPrice(spqProductPrice);
							spqShoppingCar.setIsSpq(1);
						}
					}
				}
			}

			Map<String, Object> queryP = new HashMap<String, Object>();
			queryP.put("userId", userId);

			queryP.put("pCodeArray", pCodeArray);
			queryP.put("orderSendType", orderSendType);

			// 查询结算商品信息
			List<ShoppingCar> shoppingCarList = shoppingCarService.selectCarProductByChecked(queryP);
			if (shoppingCarList == null || shoppingCarList.size() == 0) {
				logger.debug("需要结算的商品列表为空。");
				resMap.put("message", "请先添加商品");
				Util.writeError(resMap);
				return resMap;
			} else {
				// 将query中的信息赋值到orderGroup中
				copyProperties(orderGroup, query);

				Map<String, Object> totalMap = new HashMap<String, Object>();

				// 对订单中及时送和预约配送进行分类,判断商品的状态（0库存不足；1正常；），并计算每种商品的总金额
				orderGroupService.handleOrderProduct(request, shoppingCarList, totalMap, mallCode, userId, true);

				// 查询门店的相关信息
				MallMaster mallMaster = mallMasterService.selectByMallCode(mallCode);
				if (mallMaster == null) {
					logger.debug("查询门店的信息为空。");
					resMap.put("message", "查询门店的信息为空");
					Util.writeError(resMap);
					return resMap;
				} else {
					if(mallMaster.getIsSupportThirdDeliver() == 0 && orderGroup.getSendType() == Constants.SEND_TYPE.SHSM){
						logger.debug("该门店不支持送货上门。");
						resMap.put("message", "该门店不支持送货上门");
						Util.writeError(resMap);
						return resMap;
					}
					if(mallMaster.getIsSupportSelfDeliver() == 0 && orderGroup.getSendType() == Constants.SEND_TYPE.SMZT){
						logger.debug("该门店不支持上门自提。");
						resMap.put("message", "该门店不支持上门自提");
						Util.writeError(resMap);
						return resMap;
					}
					
					orderGroup.setName(mallMaster.getMallName());
					orderGroup.setMallName(mallMaster.getMallName());
					orderGroup.setMallPhone(mallMaster.getPhone());
					orderGroup.setSendPrice(mallMaster.getSendPrice());
				}

				// 根据地址id查询用户地址信息
				Address address = addressService.selectById(query.getAddressId());
				if (address == null) {
					logger.debug("所选的地址信息不存在。");
					resMap.put("message", "所选的地址信息不存在");
					Util.writeError(resMap);
					return resMap;
				} else {
					orderGroup.setReceiveName(address.getReceiveName());
					orderGroup.setReceivePhone(address.getPhone());
					orderGroup.setLatitude(address.getLatitude());
					orderGroup.setLongitude(address.getLongitude());
					orderGroup.setReceiveAddress(address.getRegion() + address.getDetailAddress());
				}

				Long groupId = null;
				if (Constants.PRODUCT_SEND_TYPE.IMMEDIATE == orderSendType) {
					// 及时送达对象
					OrderProductModel immediateSendModel = (OrderProductModel) totalMap.get("immediateSendModel");
					if (immediateSendModel.getProductNum() == 0) {
						logger.info(totalMap.get("immediateLowQtyPName") + "的商品全部补货中");
						resultData.put("error_code", Constants.ORDER_RETURN_CODE.PRODUCT_ALL_LOW_STOCK);
						resultData.put("error_msg", "全部补货中");
						resMap.put("resultData", resultData);
						resMap.put("message", totalMap.get("immediateLowQtyPName"));
						Util.writeError(resMap);
						return resMap;
					} else if (!Tools.isEmpty((String) totalMap.get("immediateLowQtyPCodeStr")) && !keepSubmitFlg) {
						// 当有补货中的商品时弹出提示框
						logger.info(totalMap.get("immediateLowQtyPName") + "的商品正在补货中,订单中已减去该商品的金额.");
						resultData.put("error_code", Constants.ORDER_RETURN_CODE.PRODUCT_LOW_STOCK);
						resultData.put("error_msg", "商品库存不足");
						resMap.put("resultData", resultData);
						resMap.put("message", totalMap.get("immediateLowQtyPName") + "的商品正在补货中,订单中已减去该商品的金额.");
						Util.writeError(resMap);
						return resMap;
					}
					if (Constants.LIMIT_SEND_AMOUNT.compareTo(immediateSendModel.getProductPrice()) <= 0) {
						// 计算实际付款额
						this.calcuateRealPrice(orderGroup, immediateSendModel, mallMaster.getSendPrice(), orderSendType, mallCode, userId,
								cardCoupon);
					} else {
						// 订单金额小于起送金额
						logger.info("本次订单金额小于起送金额.");
						resultData.put("error_code", Constants.ORDER_RETURN_CODE.LOW_LIMIT_AMOUNT);
						resultData.put("error_msg", "订单金额小于起送金额");
						resMap.put("resultData", resultData);
						resMap.put("message", "本次订单金额已不足起送金额，请挑选别的商品");
						Util.writeError(resMap);
						return resMap;
					}

					// 生成订单
					List<ShoppingCar> orderProductList = immediateSendModel.getShoppingCarList();
					if (spqShoppingCar != null) {
						// 添加商品券对应的商品
						orderProductList.add(spqShoppingCar);
					}
					orderGroup = orderGroupService.insertConfirmOrder(orderGroup, orderProductList, false);

				} else if (Constants.PRODUCT_SEND_TYPE.PREPARE == orderSendType) {
					// 预约配送对象
					OrderProductModel prepareSendModel = (OrderProductModel) totalMap.get("prepareSendModel");
					if (prepareSendModel.getProductNum() == 0) {
						logger.info(totalMap.get("prepareLowQtyPName") + "的商品全部补货中");
						resultData.put("error_code", Constants.ORDER_RETURN_CODE.PRODUCT_ALL_LOW_STOCK);
						resultData.put("error_msg", "全部补货中");
						resMap.put("resultData", resultData);
						resMap.put("message", totalMap.get("prepareLowQtyPName"));
						Util.writeError(resMap);
						return resMap;
					} else if (!Tools.isEmpty((String) totalMap.get("prepareLowQtyPCodeStr")) && !keepSubmitFlg) {
						// 当有补货中的商品时弹出提示框
						logger.info(totalMap.get("prepareLowQtyPName") + "的商品正在补货中,订单中已减去该商品的金额.");
						resultData.put("error_code", Constants.ORDER_RETURN_CODE.PRODUCT_LOW_STOCK);
						resultData.put("error_msg", "商品库存不足");
						resMap.put("resultData", resultData);
						resMap.put("message", totalMap.get("prepareLowQtyPName") + "的商品正在补货中,订单中已减去该商品的金额.");
						Util.writeError(resMap);
						return resMap;
					}

					if (Constants.LIMIT_SEND_AMOUNT.compareTo(prepareSendModel.getProductPrice()) <= 0) {
						// 计算实际付款额
						this.calcuateRealPrice(orderGroup, prepareSendModel, mallMaster.getSendPrice(), orderSendType, mallCode, userId,
								cardCoupon);
					} else {
						// 订单金额小于起送金额
						logger.info("本次订单金额小于起送金额.");
						resultData.put("error_code", Constants.ORDER_RETURN_CODE.LOW_LIMIT_AMOUNT);
						resultData.put("error_msg", "订单金额小于起送金额");
						resMap.put("resultData", resultData);
						resMap.put("message", "本次订单金额已不足起送金额，请挑选别的商品");
						Util.writeError(resMap);
						return resMap;
					}

					// 生成订单
					orderGroup = orderGroupService.insertConfirmOrder(orderGroup, prepareSendModel.getShoppingCarList(), false);
				}

				if (orderGroup.getId() != null && orderGroup.getId().longValue() != 0) {
					resultData.put("groupId", orderGroup.getId());
					resultData.put("realPrice", orderGroup.getRealPrice());
					resMap.put("resultData", resultData);
					Util.writeSuccess(resMap);
					Util.writeOk(resMap);
				} else {
					Util.writeFail(resMap);
					Util.writeError(resMap);
				}

			}

		} catch (Exception e) {
			logger.error("确认下单操作出错：", e);
			resMap.put("message", "确认下单操作出错");
			Util.writeError(resMap);
		}

		return resMap;
	}

	/**
	 * 计算实际付款额
	 * lixinling 
	 * 2016年8月17日 上午11:12:18
	 * @param orderGroup
	 * @param orderProductModel
	 * @param sendPrice  配送费
	 * @param orderSendType  订单配送方式：0立即配送；1预约配送；
	 * @return
	 */
	public OrderGroup calcuateRealPrice(OrderGroup orderGroup, OrderProductModel orderProductModel, BigDecimal sendPrice,
			int orderSendType, String mallCode, String userId, CardCoupon cardCoupon) {
		// 商品总金额
		BigDecimal productPrice = orderProductModel.getProductPrice();
		orderGroup.setTotalProductPrice(productPrice);
		orderGroup.setTotalProductNum(orderProductModel.getProductNum());

		// 订单配送方式：0立即配送；1预约配送；
		orderGroup.setOrderSendType(orderSendType);

		// 原购物消费金额（不包含优惠券和满减等活动）
		BigDecimal expendsAmount = productPrice;

		// 订单满减
		BigDecimal fullSubtractPrice = new BigDecimal("0.00");

		// 免运费金额
		BigDecimal freeFreightPrice = new BigDecimal("0.00");

		// 卡券抵扣金额金额
		BigDecimal cardCouponPrice = new BigDecimal("0.00");

		// 临时配送金额
		BigDecimal tmpSendPrice = sendPrice;

		// 校验优惠券使用的条件合法性
		if (cardCoupon != null) {
			if (productPrice.compareTo(cardCoupon.getLimitMoney()) >= 0) {
				if (cardCoupon.getCardType() == Constants.CARD_COUPON_TYPE.DJQ) {
					cardCouponPrice = cardCoupon.getAmount();
				} else if (cardCoupon.getCardType() == Constants.CARD_COUPON_TYPE.MYFQ) {
					// 免运费(优惠券)
					if (Constants.SEND_TYPE.SHSM == orderGroup.getSendType()) {
						// 送货上门的情况下免运费金额才会计算;上门自提时只使用优惠券但不会再减去运费金额
						freeFreightPrice = sendPrice;
					}
					sendPrice = new BigDecimal("0.00");
					orderGroup.setIsFreeFreight(true);
					orderGroup.setFreeFreightPrice(freeFreightPrice);
				}
			}
			orderGroup.setCardCouponPrice(cardCouponPrice);
			orderGroup.setCouponType(cardCoupon.getCardType());
		}

		// 当前时间参与的促销活动处理
		orderGroupService.activityCurrentTime(mallCode, userId, productPrice, tmpSendPrice, orderProductModel);

		// 满减
		if (orderProductModel.getIsFullSubtract()) {
			fullSubtractPrice = orderProductModel.getFullSubtractPrice();

			orderGroup.setIsFullSubtract(orderProductModel.getIsFullSubtract());
			orderGroup.setFullSubtractPrice(fullSubtractPrice);
		}

		// 免运费(活动)
		if (orderProductModel.getIsFreeFreight()) {
			sendPrice = new BigDecimal("0.00");
			freeFreightPrice = orderProductModel.getFreeFreightPrice();
		}

		// 如果配送方式是门店自提的情况
		if (Constants.SEND_TYPE.SMZT == orderGroup.getSendType()) {
			// 生成提货码
			// TakeGoodsCodeGenerate takeGoodsCodeGenerate =
			// takeGoodsCodeGenerateService.generateCode(mallCode);
			// orderGroup.setTakeGoodsCode(takeGoodsCodeGenerate.getTakeGoodsCode());
			orderGroup.setTotalPrice(productPrice);
			orderGroup.setSendType(Constants.SEND_TYPE.SMZT);
			orderGroup.setSendPrice(new BigDecimal("0.00"));
		} else if (Constants.SEND_TYPE.SHSM == orderGroup.getSendType()) {
			// 合计 = 商品总金额 + 配送费
			orderGroup.setSendPrice(sendPrice);
			orderGroup.setTotalPrice(tmpSendPrice.add(productPrice));
			orderGroup.setSendType(Constants.SEND_TYPE.SHSM);
		}

		// (整单)优惠金额 = 优惠券 + 订单满减 + 免运费金额
		BigDecimal preferentialPrice = cardCouponPrice.add(fullSubtractPrice).add(freeFreightPrice);
		orderGroup.setPreferentialPrice(preferentialPrice);

		// 实付款 = 合计 - 优惠金额
		BigDecimal realPrice = orderGroup.getTotalPrice().subtract(preferentialPrice);
		orderGroup.setRealPrice(realPrice);
		return orderGroup;
	}

	/**
	 * 将query的值赋值给model 
	 * lixinling 
	 * 2016年7月25日 上午9:59:16
	 * 
	 * @param orderGroup
	 * @param query
	 */
	public void copyProperties(OrderGroup orderGroup, OrderGroupQuery query) {

		// 将query中属性值赋值给model中的同名属性
		BeanCopier bc = BeanCopier.create(OrderGroupQuery.class, OrderGroup.class, false);
		bc.copy(query, orderGroup, null);

		// 对特殊字段进行处理
		orderGroup.setSendDate(DateComFunc.strToDate(query.getSendDateStr(), "yyyy-MM-dd"));
		// 订单开始时间
		orderGroup.setStartTime(new Date());
		// 订单结束时间(30分钟未支付自动取消订单)
		orderGroup.setEndTime(DateComFunc.addMinute(new Date(), 15L));
		// 对remark进行转码
		if (!Tools.isEmpty(query.getRemark())) {
			try {
				orderGroup.setRemark(URLDecoder.decode(query.getRemark(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		orderGroup.setCreDate(new Date());
	}

	/**
	 * 取消订单
	 * lixinling 
	 * 2016年8月19日 下午1:12:44
	 * @param request
	 * @param groupId
	 * @param isCustomer 客户：true;商家：false
	 * @return
	 */
	@RequestMapping("/cancleOrder")
	@ResponseBody
	public Object cancleOrder(HttpServletRequest request, Long groupId, boolean isCustomer, String userId, String cancelReason) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		if (groupId == null || groupId.longValue() == 0) {
			logger.info("groupId为必传参数");
			resMap.put("message", "groupId为必传参数");
			Util.writeError(resMap);
			return resMap;
		}

		// 查询订单信息
		OrderGroup orderGroup = orderGroupService.selectById(groupId);
		if (orderGroup == null) {
			logger.info("订单不存在");
			resMap.put("message", "订单不存在");
			Util.writeError(resMap);
			return resMap;
		}
		if (orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.CUSTOMER_CANCELE
				|| orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.BUSSINESS_CANCELE
				|| orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.TIMEOUT_CANCELE
				|| orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.KFCZ_REFUND) {
			logger.info("订单已取消，请勿重复取消订单");
			resMap.put("message", "订单已取消，请勿重复取消订单");
			Util.writeError(resMap);
			return resMap;
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("groupId", groupId);
		if (isCustomer) {
			if (orderGroup.getOrderGroupStatus() >= Constants.ORDER_GROUP_STATUS.CONFIRM) {
				logger.info("商家已接单，不能取消订单");
				resMap.put("message", "商家已接单，不能取消订单");
				Util.writeError(resMap);
				return resMap;
			}
			params.put("orderGroupStatus", Constants.ORDER_GROUP_STATUS.CUSTOMER_CANCELE);
		} else {
			params.put("orderGroupStatus", Constants.ORDER_GROUP_STATUS.BUSSINESS_CANCELE);
		}
		params.put("orderCancelTime", new Date());
		
		if(!Tools.isEmpty(cancelReason)){
			cancelReason = Tools.URLDecoderStr(cancelReason);
			params.put("cancelReason", cancelReason);
		}

		// 更新订单状态(已取消) --> 执行退款操作 --> 更新退款单状态
		boolean updateStatusFlg = orderGroupService.updateStatusById(params, "1");
		if (updateStatusFlg) {
			// 进行库存恢复
			orderGroupProductService.updateProductQuantity(groupId);
			if (orderGroup != null && orderGroup.getPayStatus() == Constants.PAY_STATUS.UNPAY) {
				logger.info("取消订单成功(未支付)。");
				Util.writeSuccess(resMap);
				Util.writeOk(resMap);
				return resMap;
			} else if (orderGroup != null
					&& orderGroup.getPayStatus() == Constants.PAY_STATUS.PAYD
					&& (orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.PAYD
							|| orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.CONFIRM
							|| orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.SENDING
							|| orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.SUCCESS
							|| orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.RECEIVED
							|| orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.WLQXCXFD)) {

				// 生成退款单并执行退款操作
				Map<String, Object> map = orderRefundService.insertRefund(orderGroup, userId, 0, 0, null);
				if ((Integer) map.get("return_code") == 5) {

					if (!isCustomer) {
						// 如果是商家取消订单，并且是接单以后进行取消的情况下，需要抛退换货单到鼎力云
						// 查询订单商品的详细信息
						List<OrderGroupProduct> orderGroupProductList = orderGroupProductService.selectProductListByGroupId(groupId);
						boolean uflag = uhdOrderService.returnserviceToUhd(Configure.shop_id_linjia, orderGroup, orderGroupProductList, userId, String.valueOf(map.get("refundId")));
						if (uflag) {
							logger.info("SUCCESS::::[订单号=" + groupId + "]抛退换货单到鼎力云成功.");
						} else {
							logger.info("FAIL::::[订单号=" + groupId + "]抛退换货单到鼎力云失败.");
						}
					}

					Util.writeSuccess(resMap);
					Util.writeOk(resMap);
				} else {
					logger.info("退款操作出错");
					Util.writeFail(resMap);
					Util.writeError(resMap);
				}
			} else {
				logger.info("不满足取消条件");
				resMap.put("message", "不满足取消条件");
				Util.writeError(resMap);
			}
		} else {
			Util.writeFail(resMap);
			Util.writeError(resMap);
		}

		return resMap;
	}

	/**
	 * 取得订单详情数据
	 * lixinling 
	 * 2016年8月19日 下午1:12:44
	 * @param request
	 * @param groupId
	 * @return
	 */
	@RequestMapping("/getOrderDetail")
	@ResponseBody
	public Object getOrderDetail(HttpServletRequest request, Long groupId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (groupId == null || groupId.longValue() == 0) {
			resMap.put("message", "groupId为必传参数");
			Util.writeError(resMap);
			return resMap;
		}

		// 查询订单信息
		OrderGroup orderGroup = orderGroupService.selectById(groupId);
		if (orderGroup == null) {
			resMap.put("message", "订单不存在");
			Util.writeError(resMap);
			return resMap;
		}

		// 查询订单商品列表
		List<OrderGroupProduct> productList = orderGroupProductService.selectProductListByGroupId(orderGroup.getId());
		if (productList != null && productList.size() > 0) {
			for (OrderGroupProduct item : productList) {
				if (!Tools.isEmpty(item.getTradeProductIds())) {
					String[] tradeProductIdArray = item.getTradeProductIds().split(",");
					Long activityId = item.getTradeActivityId();
					
					List<ActivityTradeProduct> activityTradeProductList = activityTradeProductService.selectTradedProduct(tradeProductIdArray,activityId);
					item.setActivityTradeProductList(activityTradeProductList);
				}
			}
		}
		orderGroup.setProductList(productList);
		resMap.put("resultData", orderGroup);
		Util.writeSuccess(resMap);
		Util.writeOk(resMap);
		return resMap;
	}

	/**
	 * 催单
	 * lixinling 
	 * 2016年9月27日 下午17:12:44
	 * @param request
	 * @param groupId
	 * @return
	 */
	@RequestMapping("/updateUrgeNumById")
	@ResponseBody
	public Object updateUrgeNumById(HttpServletRequest request, String mallCode, Long groupId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (groupId == null || groupId.longValue() == 0) {
			resMap.put("message", "groupId为必传参数");
			Util.writeError(resMap);
			return resMap;
		}

		// 查询订单信息
		orderGroupService.updateUrgeNumById(mallCode, groupId);

		// 查询订单商品列表
		Util.writeSuccess(resMap);
		Util.writeOk(resMap);
		return resMap;
	}

}
