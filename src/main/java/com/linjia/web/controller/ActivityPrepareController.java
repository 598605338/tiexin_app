package com.linjia.web.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
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
import com.linjia.constants.Constants;
import com.linjia.tools.DateComFunc;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.model.ActivityPrepare;
import com.linjia.web.model.Address;
import com.linjia.web.model.CardCoupon;
import com.linjia.web.model.MallMaster;
import com.linjia.web.model.OrderGroup;
import com.linjia.web.model.OrderProductModel;
import com.linjia.web.model.Product;
import com.linjia.web.model.ShoppingCar;
import com.linjia.web.model.TailGoodsClear;
import com.linjia.web.query.ActivityPrepareQuery;
import com.linjia.web.query.TailGoodsClearQuery;
import com.linjia.web.query.UserCardCouponQuery;
import com.linjia.web.service.ActivityPrepareService;
import com.linjia.web.service.AddressService;
import com.linjia.web.service.CardCouponService;
import com.linjia.web.service.MallMasterService;
import com.linjia.web.service.OrderGroupService;
import com.linjia.web.service.ProductService;
import com.linjia.web.service.TailGoodsClearService;

/**
 * 预约购买模块
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/prepare")
public class ActivityPrepareController extends BaseController {

	@Autowired
	private ActivityPrepareService activityPrepareService;

	@Autowired
	private ProductService productService;

	@Autowired
	private MallMasterService mallMasterService;

	@Autowired
	private CardCouponService cardCouponService;

	@Autowired
	private OrderGroupService orderGroupService;
	
	@Autowired
	private AddressService addressService;

	/**
	 * 预约购买商品列表
	 * lixinling 
	 * 2016年10月27日 上午10:33:04
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getActivityPrepareList")
	@ResponseBody
	public Object getActivityPrepareList(HttpServletRequest request, ActivityPrepareQuery query) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		try {
			// 预约购买商品数据
			query.setCurrentTime(new Date());
			List<ActivityPrepare> activityPrepareList = activityPrepareService.selectByPageList(query);

			resultData.put("activityPrepareList", activityPrepareList);

			resMap.put("resultData", resultData);
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
	 * 立即预约操作
	 * lixinling 
	 * 2016年10月27日 上午10:33:04
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/submitPrepare")
	@ResponseBody
	public Object submitPrepare(HttpServletRequest request, @RequestParam(value = "id", required = true) Long id, String userId,
			String mallCode) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		if (id == null || id.longValue() == 0) {
			resMap.put("message", "请选择预约的商品");
			Util.writeError(resMap);
			return resMap;
		}
		try {
			// 指定的预约商品数据
			ActivityPrepare activityPrepare = activityPrepareService.selectById(id);

			// 查询预约商品的相关信息
			Product product = productService.selectById(Long.valueOf(activityPrepare.getProductId()));
			if(product == null){
				resMap.put("message", "该商品已下架");
				Util.writeError(resMap);
				return resMap;
			}
			
			BigDecimal salePrice = activityPrepare.getActivityPrice();
			Integer currentQty = product.getQuantity();
			if (currentQty == null || currentQty <= 0) {
				resMap.put("message", "该商品库存不足，请稍后再试");
				Util.writeError(resMap);
				return resMap;
			}

			/***************添加门店信息*******************/
			// 查询门店的相关信息
			MallMaster mallMaster = mallMasterService.selectByMallCode(mallCode);
			if (mallMaster == null) {
				logger.debug("查询门店的信息为空。");
				resMap.put("message", "查询门店的信息为空");
				Util.writeError(resMap);
				return resMap;
			}

			// 设置提货时间
			if (activityPrepare.getGettimeType() == 1) {
				mallMaster.setGetbyselfBegintime(DateComFunc.addDay(new Date(), 2));
				mallMaster.setGetbyselfEndhour(activityPrepare.getGetselfEndTime());
			} else if (activityPrepare.getGettimeType() == 2) {
				// yyyy-MM-dd后上门店提货
				mallMaster.setGetbyselfBegintimeStr(DateComFunc.formatDate(
						DateComFunc.addDay(new Date(), activityPrepare.getNumDaysLater()), "yyyy-MM-dd"));
			}
			
			//设置提货时间类型(提货时间类型：1静态区间；2 N天之后)
			resultData.put("gettimeType", activityPrepare.getGettimeType());
			resultData.put("getselfStartTime", activityPrepare.getGetselfStartTime());
			resultData.put("getselfEndTime", activityPrepare.getGetselfEndTime());
			resultData.put("numDaysLater", activityPrepare.getNumDaysLater());
			resultData.put("mallMaster", mallMaster);
			/***************添加门店信息*******************/

			/***************添加预约商品信息*******************/
			ShoppingCar item = new ShoppingCar();

			// 设置单件商品的价格
			item.setProductPrice(salePrice);

			// 设置该条记录优惠前总价格
			BigDecimal itemTotalPrice = salePrice;
			item.setItemTotalPrice(itemTotalPrice);

			// 设置购物车每条记录的价格
			item.setItemPrice(salePrice);

			// 该条记录优惠金额
			item.setDiscountAmount(new BigDecimal("0.00"));

			// 整单优惠分摊金额
			item.setApportedDiscountAmount(new BigDecimal("0.00"));

			item.setItemStatus(Constants.QTY_STATUS.NORMAL);
			item.setpName(product.getName());
			item.setpSendType(product.getpSendType());
			item.setQuantity(1);
			item.setpCode(product.getpCode());
			item.setProductId(product.getId());
			item.setImagePath(product.getImagePath());

			resultData.put("shoppingCar", item);
			/***************添加预约商品信息*******************/

			/***************查询用户可用的优惠券*******************/
			// 查询用户可用的优惠券
			UserCardCouponQuery userCardCouponQuery = new UserCardCouponQuery();
			userCardCouponQuery.setUserId(userId);
			userCardCouponQuery.setUseStatus(Constants.CARD_USED_STATUS.UNUSED);
			userCardCouponQuery.setStartIndex(0);
			userCardCouponQuery.setPageSize(100);
			List<String> pCodeArray = new ArrayList<String>();
			pCodeArray.add(product.getpCode());
			List<CardCoupon> cardCouponList = null;

			// 当前时间参与的促销活动处理
			OrderProductModel prepareSendModel = new OrderProductModel();
			orderGroupService.activityCurrentTime(null, userId, item.getItemPrice(), null, prepareSendModel);

			// 可用优惠券
			cardCouponList = cardCouponService.selectCanUsedByUserId(userCardCouponQuery, mallCode, pCodeArray,
					prepareSendModel.getIsFullSubtract(), false, true, prepareSendModel.getIfUseCardCoupons(), itemTotalPrice);
			resultData.put("cardCouponList", cardCouponList);
			/***************查询用户可用的优惠券*******************/

			prepareSendModel.setProductNum(1);

			// 合计=商品总金额
			BigDecimal price = item.getItemPrice();
			prepareSendModel.setPrice(price);

			// 如果参与了订单满减活动
			if (prepareSendModel.getIsFullSubtract()) {
				price = price.subtract(prepareSendModel.getFullSubtractPrice());
			}

			// 实付款
			prepareSendModel.setRealPrice(price);
			prepareSendModel.setSendType(Constants.SEND_TYPE.SMZT);
			resultData.put("prepareSendModel", prepareSendModel);
			
			// 查询用户最近使用收货地址
			Address address = addressService.selectOneByUserId(userId);
			resultData.put("address", address);

			resMap.put("resultData", resultData);
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
	 * lixinling 
	 * 2016年10月28日 下午2:36:01
	 * @param request
	 * @param id
	 * @param userId
	 * @param mallCode
	 * @param userCardCouponId：选择的优惠券id
	 * @param remark：备注
	 * @param sendDateStr ：提货日期
	 * @param sendHour ：提货时间
	 * @return
	 */
	@RequestMapping("/confirmPrepare")
	@ResponseBody
	public Object confirmPrepare(HttpServletRequest request, @RequestParam(value = "id", required = true) Long id, String userId,
			String mallCode, Long userCardCouponId, String remark, String sendDateStr, String sendHour, Long addressId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		if (id == null || id.longValue() == 0) {
			resMap.put("message", "请选择预约的商品");
			Util.writeError(resMap);
			return resMap;
		}
		try {
			OrderGroup orderGroup = new OrderGroup();
			orderGroup.setUserId(userId);
			orderGroup.setReceivePhone(Util.getCustPhone(userId));
			
			// 指定的预约商品数据
			ActivityPrepare activityPrepare = activityPrepareService.selectById(id);

			if(activityPrepare.getGettimeType() == 1){
				orderGroup.setSendDate(DateComFunc.strToDate(sendDateStr, "yyyy-MM-dd"));
				orderGroup.setSendHour(sendHour);
			}else{
				orderGroup.setSendDate(DateComFunc.addDay(new Date(), activityPrepare.getNumDaysLater()));
				orderGroup.setSendHour("8:00");
			}
			// 订单开始时间
			orderGroup.setStartTime(new Date());
			// 订单结束时间(30分钟未支付自动取消订单)
			orderGroup.setEndTime(DateComFunc.addMinute(new Date(), 30L));
			// 对remark进行转码
			if (!Tools.isEmpty(remark)) {
				try {
					orderGroup.setRemark(URLDecoder.decode(remark, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			orderGroup.setCreDate(new Date());

			// 查询预约商品的相关信息
			Product product = productService.selectById(Long.valueOf(activityPrepare.getProductId()));
			BigDecimal salePrice = activityPrepare.getActivityPrice();
			Integer currentQty = product.getQuantity();
			if (currentQty == null || currentQty <= 0 || activityPrepare.getActivityQuantity() == null || activityPrepare.getActivityQuantity() <= 0) {
				resMap.put("message", "该商品库存不足，请稍后再试");
				Util.writeError(resMap);
				return resMap;
			}

			orderGroup.setName("预约商品--" + activityPrepare.getpName());
			/***************添加门店信息*******************/
			// 查询门店的相关信息
			MallMaster mallMaster = mallMasterService.selectByMallCode(mallCode);
			if (mallMaster == null) {
				logger.debug("查询门店的信息为空。");
				resMap.put("message", "查询门店的信息为空");
				Util.writeError(resMap);
				return resMap;
			} else {
				orderGroup.setMallCode(mallCode);
				orderGroup.setMallName(mallMaster.getMallName());
				orderGroup.setMallPhone(mallMaster.getPhone());
				orderGroup.setSendPrice(mallMaster.getSendPrice());
			}

			// resultData.put("mallMaster", mallMaster);
			/***************添加门店信息*******************/
			
			/***************添加联系人信息*******************/
			// 根据地址id查询用户地址信息
			Address address = addressService.selectById(addressId);
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
			/***************添加联系人信息*******************/
			

			/***************添加预约商品信息*******************/
			ShoppingCar item = new ShoppingCar();

			// 设置单件商品的价格
			item.setProductPrice(salePrice);

			// 设置该条记录优惠前总价格
			item.setItemTotalPrice(salePrice);

			// 设置购物车每条记录的价格
			item.setItemPrice(salePrice);

			// 该条记录优惠金额
			item.setDiscountAmount(new BigDecimal("0.00"));

			// 整单优惠分摊金额
			item.setApportedDiscountAmount(new BigDecimal("0.00"));

			item.setItemStatus(Constants.QTY_STATUS.NORMAL);
			item.setpName(product.getName());
			item.setpSendType(product.getpSendType());
			item.setQuantity(1);
			item.setpCode(product.getpCode());
			item.setProductId(product.getId());
			item.setPrepareActivityId(activityPrepare.getId());
			List<ShoppingCar> shoppingCarList = new ArrayList<ShoppingCar>();
			shoppingCarList.add(item);
			/***************添加预约商品信息*******************/

			// 当前时间参与的促销活动处理
			OrderProductModel prepareSendModel = new OrderProductModel();
			orderGroupService.activityCurrentTime(null, userId, item.getItemPrice(), null, prepareSendModel);

			// 商品总金额
			BigDecimal productPrice = item.getItemPrice();
			orderGroup.setTotalProductPrice(productPrice);
			orderGroup.setTotalProductNum(1);

			// 订单配送方式：0立即配送；1预约配送；
			orderGroup.setOrderSendType(1);

			// 订单满减
			BigDecimal fullSubtractPrice = new BigDecimal("0.00");

			// 免运费金额
			BigDecimal freeFreightPrice = new BigDecimal("0.00");

			// 卡券抵扣金额金额
			BigDecimal cardCouponPrice = new BigDecimal("0.00");

			// 满减
			if (prepareSendModel.getIsFullSubtract()) {
				fullSubtractPrice = prepareSendModel.getFullSubtractPrice();
			}

			// 校验优惠券的合法性
			if (userCardCouponId != null && userCardCouponId.longValue() > 0) {
				UserCardCouponQuery userCardCouponQuery = new UserCardCouponQuery();
				userCardCouponQuery.setId(userCardCouponId);
				userCardCouponQuery.setUserId(orderGroup.getUserId());
				userCardCouponQuery.setUseStatus(Constants.CARD_USED_STATUS.UNUSED);
				CardCoupon cardCoupon = cardCouponService.checkCouponByUserCardCouponId(userCardCouponQuery);
				if (cardCoupon != null && productPrice.compareTo(cardCoupon.getLimitMoney()) >= 0) {
					cardCouponPrice = cardCoupon.getAmount();
					orderGroup.setCardCouponPrice(cardCouponPrice);
					orderGroup.setCouponType(cardCoupon.getCardType());
				}
			}

			orderGroup.setTotalPrice(productPrice);
			orderGroup.setSendType(Constants.SEND_TYPE.SMZT);
			orderGroup.setSendPrice(new BigDecimal("0.00"));

			// 优惠金额 = 优惠券 + 订单满减 + 免运费金额
			BigDecimal preferentialPrice = cardCouponPrice.add(fullSubtractPrice).add(freeFreightPrice);
			orderGroup.setPreferentialPrice(preferentialPrice);

			// 实付款 = 合计 - 优惠金额
			BigDecimal realPrice = orderGroup.getTotalPrice().subtract(preferentialPrice);
			orderGroup.setRealPrice(realPrice);

			// 生成订单
			orderGroup = orderGroupService.insertConfirmOrder(orderGroup, shoppingCarList, true);

			if (orderGroup.getId() != null && orderGroup.getId().longValue() != 0) {
				resultData.put("groupId", orderGroup.getId());
				resultData.put("realPrice", orderGroup.getRealPrice());
				resMap.put("resultData", resultData);
				Util.writeSuccess(resMap);
				Util.writeOk(resMap);
			} else {
				resMap.put("message", "预约购买订单生成失败.");
				Util.writeError(resMap);
			}
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
			e.printStackTrace();
		}
		return resMap;
	}

}
