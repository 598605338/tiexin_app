package com.linjia.web.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.constants.Constants;
import com.linjia.tools.JSONUtil;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.dao.ActivityTradeProductMapper;
import com.linjia.web.dao.OrderGroupMapper;
import com.linjia.web.dao.OrderGroupProductMapper;
import com.linjia.web.dao.ScoreChangeMapper;
import com.linjia.web.dao.ShoppingCarMapper;
import com.linjia.web.model.ActivityInfo;
import com.linjia.web.model.ActivityTradeProduct;
import com.linjia.web.model.MallProductStore;
import com.linjia.web.model.MiaoshaActivityProduct;
import com.linjia.web.model.OrderGroup;
import com.linjia.web.model.OrderGroupProduct;
import com.linjia.web.model.OrderProductModel;
import com.linjia.web.model.Product;
import com.linjia.web.model.ScoreChange;
import com.linjia.web.model.ShoppingCar;
import com.linjia.web.model.TailGoodsClear;
import com.linjia.web.model.UserCardCoupon;
import com.linjia.web.query.ERPProductQtyBean;
import com.linjia.web.query.OrderGroupQuery;
import com.linjia.web.service.ActivityPrepareService;
import com.linjia.web.service.ActivityPromotionService;
import com.linjia.web.service.CardCouponProductService;
import com.linjia.web.service.MallProductStoreService;
import com.linjia.web.service.MiaoshaActivityProductService;
import com.linjia.web.service.OrderGroupProductService;
import com.linjia.web.service.OrderGroupService;
import com.linjia.web.service.OrderRefundService;
import com.linjia.web.service.ProductService;
import com.linjia.web.service.ShoppingCarService;
import com.linjia.web.service.TailGoodsClearService;
import com.linjia.web.service.UserCardCouponService;
import com.linjia.web.thirdService.JGpush.service.JgPushDeviceService;
import com.linjia.web.uhd123.common.Configure;
import com.linjia.web.uhd123.service.UhdOrderService;
import com.linjia.web.uhd123.service.impl.UhdOrderServiceImpl;

@Service
@Transactional
public class OrderGroupServiceImpl extends BaseServiceImpl<OrderGroup, Long> implements OrderGroupService {

	@Resource
	private OrderGroupMapper mapper;

	@Resource
	private OrderGroupProductMapper orderGroupProductMapper;

	@Resource
	private ScoreChangeMapper scoreChangeMapper;

	@Resource
	private ShoppingCarMapper shoppingCarMapper;

	@Autowired
	private ActivityTradeProductMapper activityTradeProductMapper;

	@Autowired
	private OrderidGenerateServiceImpl orderidGenerateServiceImpl;

	@Autowired
	private ProductService productService;

	@Autowired
	private MiaoshaActivityProductService miaoshaActivityProductService;

	@Autowired
	private TailGoodsClearService tailGoodsClearService;

	@Autowired
	private MallProductStoreService mallProductStoreService;

	@Autowired
	private UserCardCouponService userCardCouponService;

	@Autowired
	private JgPushDeviceService jgPushDeviceService;

	@Autowired
	private ActivityPromotionService activityPromotionService;

	@Autowired
	private UhdOrderServiceImpl uhdOrderServiceImpl;

	@Autowired
	private ActivityPrepareService activityPrepareService;

	@Autowired
	private CardCouponProductService cardCouponProductService;

	@Autowired
	private ShoppingCarService shoppingCarService;

	@Autowired
	private UhdOrderService uhdOrderService;
	@Autowired
	private OrderGroupProductService orderGroupProductService;
	@Autowired
	private OrderRefundService orderRefundService;

	@Override
	public BaseDao<OrderGroup, Long> getDao() {
		return mapper;
	}

	/**
	 * 确认订单操作 
	 * lixinling 
	 * 2016年7月25日 上午11:10:12
	 * 
	 * @param orderGroup
	 * @param shoppingCarList
	 * @return
	 */
	public OrderGroup insertConfirmOrder(OrderGroup orderGroup, List<ShoppingCar> shoppingCarList, boolean isPrepare) {
		try {
			if (orderGroup != null) {

				// 生成groupId
				orderGroup.setId(orderidGenerateServiceImpl.generateOrderGroupId());

				// b_order_group插入记录
				int row = mapper.insertSelective(orderGroup);
				logger.info("确认订单操作：b_order_group插入记录成功，插入条数：" + row + "条,返回主键ID=" + orderGroup.getId());

				
				//对整单优惠金额进行分摊
				BigDecimal preferentialPrice = orderGroup.getPreferentialPrice();
				BigDecimal totalProductPrice = orderGroup.getTotalProductPrice();
				if(totalProductPrice.doubleValue() > 0 && preferentialPrice != null && preferentialPrice.doubleValue() > 0 && shoppingCarList.size() > 0){
					this.apportionWholeDiscountAmount(totalProductPrice, preferentialPrice, shoppingCarList);
				}
				
				// 插入购买商品
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("list", shoppingCarList);
				map.put("groupId", orderGroup.getId());
				row = orderGroupProductMapper.insertBatchProductList(map);
				logger.info("批量插入确认下单中购买的商品数据成功，插入数据条数：" + row);

				// 更改用户优惠券状态
				if (orderGroup.getUserCardCouponId() != null && orderGroup.getUserCardCouponId().longValue() != 0) {
					UserCardCoupon userCardCoupon = new UserCardCoupon();
					userCardCoupon.setId(orderGroup.getUserCardCouponId());
					userCardCoupon.setUseStatus(Constants.CARD_USED_STATUS.USED);
					userCardCoupon.setMallCode(orderGroup.getMallCode());
					userCardCoupon.setCardCouponPrice(orderGroup.getCardCouponPrice());
					userCardCoupon.setGroupId(orderGroup.getId());
					userCardCoupon.setpCode(orderGroup.getUseCardPcode());
					userCardCoupon.setpName(orderGroup.getUseCardPname());
					userCardCoupon.setIsOnline(Constants.CARD_USED_PLACE.ONLINE);
					userCardCouponService.updateUseStatusByPrimaryKey(userCardCoupon);
				}

				// 减少下单商品库存
				if (!isPrepare) {
					// JSONArray ERPProductsArray = new JSONArray();

					// 查询订单商品的详细信息
					List<OrderGroupProduct> orderGroupProductList = orderGroupProductMapper.selectProductListByGroupId(orderGroup.getId());

					// 删除购物车中已下单的商品
					for (ShoppingCar item : shoppingCarList) {

						// 如果是商品券商品直接跳过
						if (item.getIsSpq() == 1 || !Tools.isEmpty(item.getParentPCode()))
							continue;

						if (item.getItemStatus() == Constants.QTY_STATUS.NORMAL) {
							// 如果是秒杀商品，减少秒杀商品设置的库存数
							if (item.getMiaoshaoActiveProductId() != null && item.getMiaoshaoActiveProductId().longValue() != 0) {
								miaoshaActivityProductService.updateQuantityById(item.getMiaoshaoQty(), item.getMiaoshaoActiveProductId()
										.intValue());
							}

							// 从购物车中删除已下单的商品
							shoppingCarMapper.deleteByPrimaryKey(new Long(item.getId()));
							logger.info("从购物车中删除已下单的商品");
						}
					}

					// 调用锁库存操作
					uhdOrderServiceImpl.orderstocklockToUhd(Configure.shop_id_linjia, orderGroup, orderGroupProductList, "linjia");
				} else {
					ShoppingCar shoppingCar = shoppingCarList.get(0);
					Integer buyQty = shoppingCar.getQuantity();
					Long productId = shoppingCar.getProductId();
					Integer activityPrepareId = shoppingCar.getPrepareActivityId();
					productService.updateQuantityById(productId, buyQty);
					activityPrepareService.updateQuantityById(activityPrepareId, buyQty);
				}

				// 减少ERP下单商品库存
				/*
				 * if (ERPProductsArray != null && ERPProductsArray.length() >
				 * 0) { JSONObject decResult =
				 * Util.decERPQuantity(orderGroup.getMallCode(),
				 * orderGroup.getId(), ERPProductsArray); // 0表示成功，1表示失败 if
				 * (decResult == null ||
				 * !"0".equals(decResult.optString("echoCode"))) throw new
				 * RuntimeException();
				 * 
				 * } else { throw new RuntimeException(); }
				 */

			} else {
				logger.warn("OrderGroup对象为空或购买商品列表shoppingCarList为空");
				return null;
			}
		} catch (Exception e) {
			logger.error("确认订单操作 error", e);
			throw e;
		}
		return orderGroup;
	}

	@Override
	public boolean updateStatusById(Map<String, Object> map, String platform) {
		boolean res = false;
		int row = mapper.updateStatusById(map);

		// 推送订单给商家端
		// 1;新订单；2；预约提醒；3，催单；4，取消订单；5，自配送
		// 1,邻家；2，美团；3百度
		if (Constants.ORDER_GROUP_STATUS.PAYD == (int) map.get("orderGroupStatus")) {
			String type = "1";
			Integer orderSendType = (Integer) map.get("orderSendType");
			if (orderSendType != null && orderSendType == 1) // 预约订单
				type = "2";

			jgPushDeviceService.pushMessage((String) map.get("mallCode"), type, platform);
		}
		// 增加用户积分
		if (row == 1 && Constants.ORDER_GROUP_STATUS.SUCCESS == (int) map.get("orderGroupStatus")) {
			OrderGroup orderGroup = mapper.selectByPrimaryKey((Long) map.get("groupId"));
			// 积分记录表写入数据
			ScoreChange scoreChange = new ScoreChange();
			scoreChange.setUserId(orderGroup.getUserId());
			scoreChange.setScore(orderGroup.getGetScore());
			scoreChange.setType(Constants.SCORE_WAY.SPENDING);
			scoreChange.setCreDate(new Date());
			scoreChange.setDeleted(false);
			scoreChangeMapper.insertSelective(scoreChange);

			// loginCode从redis中取得
			String loginCode = orderGroup.getUserId();
			Util.incScoreAccount(scoreChange.getId(), loginCode, "用户购买商品", scoreChange.getType(), scoreChange.getScore());
			
			// 抛单到鼎力云更改订单状态为已完成
			res = uhdOrderService.updateOrderDeliverToUhd(String.valueOf(orderGroup.getId()), new Date(), orderGroup.getUserId(), "signed",
					"handover", "handover");
			// res = true;
		} else if (row == 1) {
			res = true;
		}
		return res;
	}

	/**
	 * 查询我的全部订单
	 * lixinling 
	 * 2016年7月28日 下午3:32:16
	 * @param map
	 * @return
	 */
	@Override
	public List<OrderGroup> selectMyAllOrderList(OrderGroupQuery query) {
		return mapper.selectMyAllOrderList(query);
	}

	/**
	 * 对订单中及时送和预约配送进行分类,判断商品的状态（0库存不足；1正常；），并计算每种商品的总金额
	 * 
	 * @param totalPrice
	 *            购物车总商品数量
	 * @param totalQty
	 *            购物车总价格 lixinling 2016年7月18日 下午1:36:34
	 */
	public Map<String, Object> handleOrderProduct(HttpServletRequest request, List<ShoppingCar> shoppingCarList,
			Map<String, Object> totalMap, String mallCode, String userId, boolean isConfirm) {
		// (及时送达对象)库存不足的商品Code
		StringBuilder immediateLowQtyPCode = new StringBuilder();
		// (及时送达对象)库存不足的商品Name
		StringBuilder immediateLowQtyPName = new StringBuilder();
		// (预约配送对象)库存不足的商品Code
		StringBuilder prepareLowQtyPCode = new StringBuilder();
		// (预约配送对象)库存不足的商品Name
		StringBuilder prepareLowQtyPName = new StringBuilder();

		// 及时送达对象
		OrderProductModel immediateSendModel = new OrderProductModel();
		// 预约配送对象
		OrderProductModel prepareSendModel = new OrderProductModel();

		// 及时送达满减总金额
		// BigDecimal immediateFullSubtractPrice = new BigDecimal("0.00");
		// 及时送达商品数量
		int immediateProductNum = 0;
		// 及时送达商品金额
		BigDecimal immediateProductPrice = new BigDecimal("0.00");
		// 及时送达商品列表
		List<ShoppingCar> immediateProductList = new ArrayList<ShoppingCar>();

		// 预约配送满减总金额
		// BigDecimal prepareFullSubtractPrice = new BigDecimal("0.00");
		// 预约配送商品数量
		int prepareProductNum = 0;
		// 预约配送商品金额
		BigDecimal prepareProductPrice = new BigDecimal("0.00");
		// 预约配送商品列表
		List<ShoppingCar> prepareProductList = new ArrayList<ShoppingCar>();

		// 及时送达满减是否允许使用卡券（0，否；1，是）
		Integer immediateIfUseCardCoupons = 1;
		// 预约配送满减是否允许使用卡券（0，否；1，是）
		Integer prepareIfUseCardCoupons = 0;
		for (ShoppingCar item : shoppingCarList) {
			// 购物车每条记录优惠前总金额(等于salePrice * quantity)
			BigDecimal itemTotalPrice = null;

			// 购物车每条记录实际价格金额（等于itemTotalPrice减去优惠后的价格）
			BigDecimal itemPrice = null;

			// 参加秒杀活动时每条记录金额
			BigDecimal itemPrice_Miaosha = new BigDecimal("0.00");
			// 参加尾货清仓活动时每条记录金额
			BigDecimal itemPrice_Tail = new BigDecimal("0.00");
			// 参加促销管理活动时每条记录金额
			BigDecimal itemPrice_Promotion = new BigDecimal("0.00");

			// 参与的活动类型：1,满减；2，商品折扣；3第二件半价；4，加钱换购；5，免运费；6，会员促销;7,秒杀；8,尾货清仓
			Integer tmpActivityType = null;
			// 活动表的主键id
			Long tmpActivityId = null;

			int currentQty = 0;

			itemPrice = new BigDecimal("0.00");
			// 购买商品数量
			int buyQty = item.getQuantity();
			// 及时送的商品处理
			if (Constants.PRODUCT_SEND_TYPE.IMMEDIATE == item.getpSendType()) {
				// 查询商品的门店的库存
				currentQty = productService.getCurrentQty(item.getProductId(), item.getpCode(), mallCode);

				// 取得商品在门店中的价格
				MallProductStore mallProductStore = mallProductStoreService.selectByMallAndPCode(mallCode, item.getpCode());

				// 如果库存不足，则不再进行itemPrice的计算
				if (mallProductStore == null || currentQty < buyQty) {
					item.setItemStatus(Constants.QTY_STATUS.LOW);
					if (immediateLowQtyPCode != null) {
						if (immediateLowQtyPCode.length() > 0) {
							immediateLowQtyPCode.append(",").append(item.getpCode());
							immediateLowQtyPName.append(",").append(item.getpName());
						} else {
							immediateLowQtyPCode.append(item.getpCode());
							immediateLowQtyPName.append(item.getpName());
						}
					}
					continue;
				}

				BigDecimal salePrice = mallProductStore.getSalePrice();

				// 设置单件商品的价格
				item.setProductPrice(salePrice);

				// 设置该条记录优惠前总价格
				itemTotalPrice = salePrice.multiply(new BigDecimal(String.valueOf(buyQty)));
				item.setItemTotalPrice(itemTotalPrice);

				// 判断是否是正在进行的秒杀商品(如果是，则根据每个用户限制数量，按秒杀价进行计算，如果超出限制数量的则按原价进行计算)
				MiaoshaActivityProduct miaoshaActivityProduct = miaoshaActivityProductService.getPanicingProductByProductId(item
						.getProductId());

				// 判断是否是正在进行的尾货清仓
				TailGoodsClear tailGoodsClear = tailGoodsClearService.selectByProductId(item.getProductId(), mallCode);

				// 购买商品参加的促销活动列表
				List<ActivityInfo> promotionActivityList = activityPromotionService.selectActInfoByCurrTime(item.getProductId(), mallCode);
				if (miaoshaActivityProduct != null) {
					// 记录这件商品在活动表中的主键id，以便下单时扣减参与活动的库存数
					item.setMiaoshaoActiveProductId(miaoshaActivityProduct.getId());
					// 每个用户限购数量
					int limitQty = miaoshaActivityProduct.getLimitQuantity() == null ? 1 : miaoshaActivityProduct.getLimitQuantity();
					// 秒杀价
					BigDecimal panicPrice = miaoshaActivityProduct.getPbPrice();

					if (limitQty >= buyQty) {
						itemPrice_Miaosha = itemPrice_Miaosha.add(panicPrice.multiply(new BigDecimal(String.valueOf(buyQty))));

						// 参与秒杀活动所购买的数量
						item.setMiaoshaoQty(buyQty);
					} else if (limitQty < buyQty) {
						// 超出限购数量的按原价计算
						int overQty = buyQty - limitQty;
						// 秒杀价商品总和
						itemPrice_Miaosha = itemPrice_Miaosha.add(panicPrice.multiply(new BigDecimal(String.valueOf(limitQty))));

						// 商品价格总和 = 秒杀商品价*限购数量 + 原商品价*(购买数量 - 限购数量)
						itemPrice_Miaosha = itemPrice_Miaosha.add(salePrice.multiply(new BigDecimal(String.valueOf(overQty))));

						// 参与秒杀活动所购买的数量
						item.setMiaoshaoQty(limitQty);
					}

					// 设置临时活动类型和id
					// item.setActivityType(7);
					// item.setActivityId(miaoshaActivityProduct.getId());
				}

				// 参加尾货清仓活动时的价格情况
				if (tailGoodsClear != null) {
					item.setTailGoodsClearId(tailGoodsClear.getId());
					// 1.每日清仓;2.本期清仓
					int tG_type = tailGoodsClear.getType() == null ? 1 : tailGoodsClear.getType();
					BigDecimal clearPrice = tailGoodsClear.getClearPrice();
					// 清仓活动剩余数量
					int surplusQty = 0;
					if (tG_type == 1) {
						// 检索条件数
						int searchCondition = tailGoodsClear.getSearchCondition();

						// 取得erp库存数
						int storeQty = Util.queryQtyByStore(mallCode, item.getpCode());

						if ((storeQty - searchCondition) <= 0) {
							/*
							 * Map<String, Object> params = new HashMap<String,
							 * Object>(); // 不满足检索条件数时,不在列表显示;并将其状态更改为已售罄
							 * params.put("activityStatus",
							 * Constants.TAIL_GOODS_CLEAR_ACTIVITY_STATUS
							 * .SELLOUT); params.put("id", item.getId());
							 * tailGoodsClearService
							 * .updateActivityStatusByPrimaryKey(params);
							 */
							surplusQty = 0;
						} else {
							// 剩余清仓数量
							surplusQty = storeQty - searchCondition;
						}
					} else if (tG_type == 2) {
						// 本期清仓剩余数量
						surplusQty = tailGoodsClear.getQuantity();
					}
					if (surplusQty >= buyQty) {
						// 剩余数量大于购买数量，则全部按清仓价格计算
						itemPrice_Tail = itemPrice_Tail.add(clearPrice.multiply(new BigDecimal(String.valueOf(buyQty))));
					} else if (surplusQty < buyQty) {
						// 超出剩余数量的按原价计算
						int overQty = buyQty - surplusQty;
						// 清仓价价商品总和
						itemPrice_Tail = itemPrice_Tail.add(clearPrice.multiply(new BigDecimal(String.valueOf(surplusQty))));

						// 原价商品总和
						itemPrice_Tail = itemPrice_Tail.add(salePrice.multiply(new BigDecimal(String.valueOf(overQty))));
					}

					// 设置临时活动类型和id
					// item.setActivityType(8);
					// item.setActivityId(tailGoodsClear.getId());
				}

				// 处理促销活动
				Map<String, Object> data = this.handlePromotionItemPrice(mallCode, buyQty, userId, salePrice, itemPrice_Promotion,
						promotionActivityList, false);

				tmpActivityType = (Integer) data.get("tmpActivityType");
				tmpActivityId = (Long) data.get("tmpActivityId");
				itemPrice_Promotion = (BigDecimal) data.get("itemPrice_Promotion");
				Integer ifUseCardCoupons = (Integer) data.get("ifUseCardCoupons");

				Map<BigDecimal, String> priceMap = new TreeMap<BigDecimal, String>();
				// 选出价格最低的作为本条记录的价格
				if (itemPrice_Miaosha != null && itemPrice_Miaosha.doubleValue() != 0) {
					/*
					 * item.setActivityType(7);
					 * item.setActivityId(miaoshaActivityProduct.getId());
					 * itemPrice = itemPrice_Miaosha;
					 */
					priceMap.put(itemPrice_Miaosha, Constants.ACTIVITY_TYPE.MIAOSHA + "_" + miaoshaActivityProduct.getId());
				}
				if (itemPrice_Tail != null && itemPrice_Tail.doubleValue() != 0) {
					/*
					 * item.setActivityType(8);
					 * item.setActivityId(tailGoodsClear.getId()); itemPrice =
					 * itemPrice_Tail;
					 */
					priceMap.put(itemPrice_Tail, Constants.ACTIVITY_TYPE.TAIL_GOODS_CLEAR + "_" + tailGoodsClear.getId());
				}
				if (tmpActivityId != null && tmpActivityId.longValue() != 0) {
					/*
					 * item.setActivityType(tmpActivityType);
					 * item.setActivityId(tmpActivityId); itemPrice =
					 * itemPrice_Promotion;
					 */
					priceMap.put(itemPrice_Promotion, tmpActivityType + "_" + tmpActivityId);
					if (tmpActivityType == 1) {
						// 订单满减活动
						if (ifUseCardCoupons != null && ifUseCardCoupons == 0)
							immediateIfUseCardCoupons = 0;
						// immediateSendModel.setFullSubtract(true);
						// immediateFullSubtractPrice =
						// immediateFullSubtractPrice.add((BigDecimal)data.get("fullReduceMoney"));
					}
				}

				if (priceMap != null && !priceMap.isEmpty()) {
					TreeMap<BigDecimal, String> sortPriceMap = Tools.sortMapByKey(priceMap);
					Entry<BigDecimal, String> firstEntry = sortPriceMap.firstEntry();
					String value = firstEntry.getValue();
					itemPrice = firstEntry.getKey();
					if (!Tools.isEmpty(value)) {
						item.setActivityType(Integer.valueOf(value.split("_")[0]));
						item.setActivityId(Long.valueOf(value.split("_")[1]));
					}
				}

				// 任何活动都没有参加时的价格情况
				if (itemPrice.doubleValue() == 0) {
					itemPrice = itemPrice.add(salePrice.multiply(new BigDecimal(String.valueOf(buyQty))));
				}

				// 设置购物车每条记录的价格
				item.setItemPrice(itemPrice);

				// 该条记录优惠金额
				item.setDiscountAmount(itemTotalPrice.subtract(itemPrice));

				// 整单优惠分摊金额
				item.setApportedDiscountAmount(new BigDecimal("0.00"));

				item.setItemStatus(Constants.QTY_STATUS.NORMAL);
				// 购买总数量
				immediateProductNum += buyQty;
				immediateProductPrice = immediateProductPrice.add(itemPrice);

				// 如果该商品有加钱换购的商品时，应加上换购价
				if (!Tools.isEmpty(item.getTradeProductIds())) {
					String[] tradeProductIdArray = item.getTradeProductIds().split(",");
					Long activityId = item.getTradeActivityId();

					Map<String, Object> params = new HashMap<String, Object>();
					params.put("array", tradeProductIdArray);
					params.put("activityId", activityId);
					List<ActivityTradeProduct> activityTradeProductList = activityTradeProductMapper.selectTradedProduct(params);
					item.setActivityTradeProductList(activityTradeProductList);

					// 计算加钱换购商品的金额和购买商品总数
					if (activityTradeProductList != null && activityTradeProductList.size() > 0) {
						immediateProductNum += activityTradeProductList.size();
						for (ActivityTradeProduct tp : activityTradeProductList) {
							immediateProductPrice = immediateProductPrice.add(new BigDecimal(tp.getP_trade_price()));
							// 转换成订单商品数据
							if (isConfirm) {
								convertTradeProductToShoppingCar(tp, item.getpCode(), immediateProductList);
							}
						}
					}
					// immediateProductPrice =
					// shoppingCarService.calculateTradeProductPrice(immediateProductPrice,
					// item.getTradeProductIds(), item.getTradeActivityId());
				}
				/*
				 * if (activityTradeProductMap != null &&
				 * activityTradeProductMap.containsKey(item.getpCode())) {
				 * String tradeProductIds =
				 * activityTradeProductMap.get(item.getpCode());
				 * 
				 * if (!Tools.isEmpty(tradeProductIds)) { Map<String, Object>
				 * param = new HashMap<String, Object>(); param.put("list",
				 * tradeProductIds.split(",")); List<ActivityTradeProduct>
				 * tradeProductList =
				 * activityPromotionService.selectActTradeProAll(param); if
				 * (tradeProductList != null && tradeProductList.size() > 0) {
				 * 
				 * // 提交订单页面展示换购的商品
				 * item.setActivityTradeProductList(tradeProductList);
				 * 
				 * for (ActivityTradeProduct atp : tradeProductList) {
				 * immediateProductPrice = immediateProductPrice.add(new
				 * BigDecimal(atp.getP_trade_price()));
				 * 
				 * item.setParentPCode(item.getpCode()); item.setpTradePrice(new
				 * BigDecimal(atp.getP_trade_price())); } } } }
				 */

				immediateProductList.add(item);
			} else {
				// 预约配送的商品的处理
				// 预约配送的商品的价格和库存
				Product product = productService.selectById(Long.valueOf(item.getProductId()));
				BigDecimal salePrice = product.getSalePrice();
				currentQty = product.getQuantity();

				// 设置单件商品的价格
				item.setProductPrice(salePrice);

				// 设置该条记录优惠前总价格
				itemTotalPrice = salePrice.multiply(new BigDecimal(String.valueOf(buyQty)));
				item.setItemTotalPrice(itemTotalPrice);

				// 如果库存不足，则不再进行itemPrice的计算
				if (currentQty < buyQty) {
					item.setItemStatus(Constants.QTY_STATUS.LOW);
					if (prepareLowQtyPCode != null) {
						if (prepareLowQtyPCode.length() > 0) {
							prepareLowQtyPCode.append(",").append(item.getpCode());
							prepareLowQtyPName.append(",").append(item.getpName());
						} else {
							prepareLowQtyPCode.append(item.getpCode());
							prepareLowQtyPName.append(item.getpName());
						}
					}
					continue;
				}

				// 判断是否参加尾货清仓活动
				TailGoodsClear tailGoodsClear = tailGoodsClearService.selectByProductId(item.getProductId(), mallCode);
				if (tailGoodsClear != null) {
					// 1.每日清仓;2.本期清仓
					int tG_type = tailGoodsClear.getType() == null ? 1 : tailGoodsClear.getType();
					BigDecimal clearPrice = tailGoodsClear.getClearPrice();
					// 清仓活动剩余数量
					int surplusQty = 0;
					if (tG_type == 1) {
						// 检索条件数
						int searchCondition = tailGoodsClear.getSearchCondition() == null ? 1 : tailGoodsClear.getSearchCondition();

						// 取得erp库存数
						int storeQty = Util.queryQtyByStore(mallCode, item.getpCode());

						if ((storeQty - searchCondition) <= 0) {
							Map<String, Object> params = new HashMap<String, Object>();
							// 不满足检索条件数时,不在列表显示;并将其状态更改为已售罄
							params.put("activityStatus", Constants.TAIL_GOODS_CLEAR_ACTIVITY_STATUS.SELLOUT);
							params.put("id", item.getId());
							tailGoodsClearService.updateActivityStatusByPrimaryKey(params);
						} else {
							// 剩余清仓数量
							surplusQty = storeQty - searchCondition;
						}
					} else if (tG_type == 2) {
						// 本期清仓剩余数量
						surplusQty = tailGoodsClear.getQuantity();
					}
					if (surplusQty >= buyQty) {
						// 剩余数量大于购买数量，则全部按清仓价格计算
						itemPrice = itemPrice.add(clearPrice.multiply(new BigDecimal(String.valueOf(buyQty))));
					} else if (surplusQty < buyQty) {
						// 超出剩余数量的按原价计算
						int overQty = buyQty - surplusQty;
						// 清仓价商品总和
						itemPrice = itemPrice.add(clearPrice.multiply(new BigDecimal(String.valueOf(surplusQty))));

						// 原价商品总和
						itemPrice = itemPrice.add(salePrice.multiply(new BigDecimal(String.valueOf(overQty))));
					}
				} else {
					// 原价商品总和
					itemPrice = itemPrice.add(salePrice.multiply(new BigDecimal(String.valueOf(buyQty))));
				}
				// 设置购物车每条记录的价格
				item.setItemPrice(itemPrice);

				// 该条记录优惠金额
				item.setDiscountAmount(itemTotalPrice.subtract(itemPrice));

				// 整单优惠分摊金额
				item.setApportedDiscountAmount(new BigDecimal("0.00"));

				item.setItemStatus(Constants.QTY_STATUS.NORMAL);
				// 购买总数量
				prepareProductNum += buyQty;
				prepareProductPrice = prepareProductPrice.add(itemPrice);
				prepareProductList.add(item);
			}

		}
		immediateSendModel.setProductNum(immediateProductNum);
		immediateSendModel.setProductPrice(immediateProductPrice);
		immediateSendModel.setShoppingCarList(immediateProductList);
		immediateSendModel.setIfUseCardCoupons(immediateIfUseCardCoupons);
		// immediateSendModel.setFullSubtractPrice(immediateFullSubtractPrice);

		prepareSendModel.setProductNum(prepareProductNum);
		prepareSendModel.setProductPrice(prepareProductPrice);
		prepareSendModel.setShoppingCarList(prepareProductList);
		// prepareSendModel.setFullSubtractPrice(prepareFullSubtractPrice);

		totalMap.put("immediateSendModel", immediateSendModel);
		totalMap.put("prepareSendModel", prepareSendModel);
		totalMap.put("immediateLowQtyPCode", immediateLowQtyPCode.toString());
		totalMap.put("immediateLowQtyPName", immediateLowQtyPName.toString());
		totalMap.put("prepareLowQtyPCode", prepareLowQtyPCode.toString());
		totalMap.put("prepareLowQtyPName", prepareLowQtyPName.toString());
		return totalMap;
	}

	@Override
	public Long selectCountByUserId(String userId) {
		return mapper.selectCountByUserId(userId);
	}

	/**
	 * 处理促销活动
	 * lixinling 
	 * 2016年9月27日 下午2:18:09
	 * @param mallCode
	 * @param buyQty
	 * @param userId
	 * @param salePrice
	 * @param itemPrice_Promotion
	 * @param promotionActivityList
	 * @param shoppingCarFlag 是否是查看购物车 
	 * @return
	 */
	@Override
	public Map<String, Object> handlePromotionItemPrice(String mallCode, int buyQty, String userId, BigDecimal salePrice,
			BigDecimal itemPrice_Promotion, List<ActivityInfo> promotionActivityList, boolean shoppingCarFlag) {

		Map<String, Object> data = new HashMap<String, Object>();
		// 参与的活动类型：1,满减；2，商品折扣；3第二件半价；4，加钱换购；5，免运费；6，会员促销;7,秒杀；8,尾货清仓
		Integer tmpActivityType = null;
		// 活动表的主键id
		Long tmpActivityId = null;
		// 活动表的主键id(加钱换购活动)
		Long tmpTradeActivityId = null;
		// 活动的名称(加钱换购活动)
		String tmpTradeActivityName = null;
		// 加钱换购商品列表
		// List<ActivityTradeProduct> activityTradeProductList = null;

		// 初始化该商品的总价格
		BigDecimal itemPrice = salePrice.multiply(new BigDecimal(String.valueOf(buyQty)));

		// 是否使用卡券（0，否；1，是）
		Integer ifUseCardCoupons = 1;
		// 参加促销管理活动时的价格情况
		if (promotionActivityList != null && promotionActivityList.size() > 0) {
			for (ActivityInfo activityInfo : promotionActivityList) {

				if (!Tools.isEmpty(activityInfo.getMall_codes())) {
					// 部分门店的情况时判断该当前门店是否参与其活动
					String[] mallCodes = activityInfo.getMall_codes().split(",");
					List<String> mallCodeList = Arrays.asList(mallCodes);
					if (!mallCodeList.contains(mallCode)) {
						logger.info("当前门店未参与该活动");
						continue;
					}
				}
				// 活动类型(1,满减；2，商品折扣；3第二件半价；4，加钱换购；5，免运费；6，会员促销)
				int activityType = activityInfo.getActivity_type();

				// 临时促销价(用于和促销价格进行比较，当小于促销价格时替换促销价格)
				BigDecimal tmp_itemPrice_Promotion = null;
				switch (activityType) {
				case 1:
					// 满减
					if ((activityInfo.getPromotion_condition_1() != null) || (activityInfo.getPromotion_condition_2() != null && activityInfo.getPromotion_condition_2() > buyQty)) {
						logger.info("当前活动时整单满减或购买件数不满足满减条件");
						continue;
					} else {
						if (activityInfo.getIf_first_work() == 1) {
							// 判断其是否是首单
							Long count = mapper.selectCountByUserId(userId);
							if (count > 0) {
								logger.info("此用户并非首单,不满足满减条件");
								continue;
							}
						} else {
							// 所有条件全满足的情况下
							logger.info("已达到满减条件，开始进行满减活动金额计算");
							// 该商品的总价格
							BigDecimal tmpTotleProductPrice = salePrice.multiply(new BigDecimal(String.valueOf(buyQty)));
							// 满减金额
							BigDecimal fullReduceMoney = new BigDecimal(activityInfo.getFullReduceMoney());

							if (activityInfo.getIf_add() == 0) {
								// 不可累加的情况
								// 促销管理活动价= 该商品总价 - 满减金额
								tmp_itemPrice_Promotion = tmpTotleProductPrice.subtract(fullReduceMoney);
							} else {
								// 可累加的情况
								// 倍数
								int multiple = buyQty / activityInfo.getPromotion_condition_2();
								if (multiple < 1) {
									logger.info("购买数量不符合满减条件");
									continue;
								} else {
									// 促销管理活动价= 该商品总价 - 满减金额*倍数
									fullReduceMoney = fullReduceMoney.multiply(new BigDecimal(multiple));
									tmp_itemPrice_Promotion = tmpTotleProductPrice.subtract(fullReduceMoney);
								}
							}

							if (tmp_itemPrice_Promotion.doubleValue() != 0 && tmp_itemPrice_Promotion.compareTo(itemPrice) < 0) {

								// 如果临时促销金额小于促销金额，则将促销金额的值替换为临时促销金额的值
								itemPrice_Promotion = tmp_itemPrice_Promotion;

								// 设置临时活动类型和id
								tmpActivityType = 1;
								data.put("fullReduceMoney", fullReduceMoney);
								tmpActivityId = activityInfo.getActivity_id();

								// 是否使用卡券（0，否；1，是）
								if (activityInfo.getIf_useCardCoupons() != null && activityInfo.getIf_useCardCoupons() == 0)
									ifUseCardCoupons = 0;
							}
						}
					}

					break;
				case 2:
					// 商品折扣
					// 所有条件全满足的情况下
					logger.info("已达到商品折扣条件，开始进行商品折扣活动金额计算");

					// 该商品的折扣值
					BigDecimal discount = activityInfo.getDiscount();
					if (Tools.isEmpty(discount) || discount.doubleValue() == 0) {
						logger.info("商品折扣的值不能是空或0");
						continue;
					}

					// 该商品的总价格
					BigDecimal tmpTotleProductPrice = salePrice.multiply(new BigDecimal(String.valueOf(buyQty)));

					// 促销管理活动价= 该商品总价*折扣
					tmp_itemPrice_Promotion = tmpTotleProductPrice.multiply(discount);

					if (tmp_itemPrice_Promotion.doubleValue() != 0 && tmp_itemPrice_Promotion.compareTo(itemPrice) < 0) {

						// 如果临时促销金额小于促销金额，则将促销金额的值替换为临时促销金额的值
						itemPrice_Promotion = tmp_itemPrice_Promotion;

						// 设置临时活动类型和id
						tmpActivityType = 2;
						tmpActivityId = activityInfo.getActivity_id();
					}

					break;
				case 3:
					// 第二件半价
					if (buyQty < 2) {
						logger.info("购买该商品的数量未达到商品第二件半价活动限制条件");
						continue;
					}

					// 该商品的第二件折扣值
					BigDecimal secondDiscount = activityInfo.getDiscount();
					if (Tools.isEmpty(secondDiscount) || secondDiscount.doubleValue() == 0) {
						logger.info("第二件半价的商品折扣值不能是空或0");
						continue;
					}

					// 所有条件全满足的情况下
					logger.info("已达到第二件半价条件，开始进行商品第二件半价活动金额计算");

					// 该商品的总价格
					BigDecimal tmpTotleProductPrice_3 = salePrice.multiply(new BigDecimal(String.valueOf(buyQty)));

					// 该商品打折的金额 = 该商品价*(1-折扣)
					BigDecimal discountPrice = salePrice.multiply(new BigDecimal(1 - secondDiscount.doubleValue()));

					if (activityInfo.getIf_add() == 0) {
						// 不可累加的情况
						// 促销管理活动价= 该商品总价 - 折扣金额
						tmp_itemPrice_Promotion = tmpTotleProductPrice_3.subtract(discountPrice);
					} else {
						// 可累加的情况
						// 倍数
						int multiple = buyQty / 2;
						if (multiple < 1) {
							logger.info("购买数量不符合第二件半价条件");
							continue;
						} else {
							// 促销管理活动价= 该商品总价 - 折扣金额*倍数
							tmp_itemPrice_Promotion = tmpTotleProductPrice_3.subtract(discountPrice.multiply(new BigDecimal(multiple)));
						}
					}

					if (tmp_itemPrice_Promotion.doubleValue() != 0 && tmp_itemPrice_Promotion.compareTo(itemPrice) < 0) {

						// 如果临时促销金额小于促销金额，则将促销金额的值替换为临时促销金额的值
						itemPrice_Promotion = tmp_itemPrice_Promotion;

						// 设置临时活动类型和id
						tmpActivityType = 3;
						tmpActivityId = activityInfo.getActivity_id();
					}
					break;
				case 4:
					// 加钱换购
					// if (shoppingCarFlag) {
					// 查看购物车的情况下，查询可换购的商品列表
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("activity_id", activityInfo.getActivity_id());
					// activityTradeProductList =
					// activityPromotionService.selectActTradeProAll(param);
					// tmpActivityType = 4;
					// tmpActivityId = activityInfo.getActivity_id();
					tmpTradeActivityId = activityInfo.getActivity_id();
					tmpTradeActivityName = activityInfo.getActivity_name();
					// }
					break;
				}
			}
		}

		data.put("ifUseCardCoupons", ifUseCardCoupons);
		data.put("tmpActivityType", tmpActivityType);
		data.put("tmpActivityId", tmpActivityId);
		data.put("tmpTradeActivityId", tmpTradeActivityId);
		data.put("tmpTradeActivityName", tmpTradeActivityName);
		data.put("itemPrice_Promotion", itemPrice_Promotion);
		// data.put("activityTradeProductList", activityTradeProductList);
		return data;
	}

	@Override
	public int updateUrgeNumById(String mallCode, Long groupId) {
		int row = mapper.updateUrgeNumById(groupId);
		if (row == 1) {
			// 推送订单给商家端
			// 1;新订单；2；预约提醒；3，催单；4，取消订单；5，自配送
			// 1,邻家；2，美团；3百度
			jgPushDeviceService.pushMessage(mallCode, "3", "1");
		}
		return row;
	}

	@Override
	public OrderGroup selectLogisOrderInfo(Long order_id) {
		return mapper.selectLogisOrderInfo(order_id);
	}

	@Override
	public List<OrderGroup> selectAllTimeOutOrder() {
		Date currentTime = new Date();
		return mapper.selectAllTimeOutOrder(currentTime);
	}

	/**
	 * 当前时间参与的促销活动
	 * lixinling 
	 * 2016年9月28日 上午11:16:56
	 * @param mallCode
	 * @param userId
	 * @param productPrice
	 * @param sendPrice
	 * @param immediateSendModel
	 * @return
	 */
	@Override
	public OrderProductModel activityCurrentTime(String mallCode, String userId, BigDecimal productPrice, BigDecimal sendPrice,
			OrderProductModel immediateSendModel) {
		// 查询当前时间是否有满减促销活动
		List<ActivityInfo> activityInfoList = activityPromotionService.selectOrderActInfoByCurrTime(null);
		if (activityInfoList != null && activityInfoList.size() > 0) {
			for (ActivityInfo activity : activityInfoList) {

				// 判断当前门店是否参与活动
				if (!Tools.isEmpty(activity.getMall_codes()) && !Tools.isEmpty(mallCode)) {

					// 部分门店的情况时判断该当前门店是否参与其活动
					String[] mallCodes = activity.getMall_codes().split(",");
					List<String> mallCodeList = Arrays.asList(mallCodes);
					if (!mallCodeList.contains(mallCode)) {
						logger.info("当前门店未参与该活动");
						continue;
					}
				}

				// 满xx元条件
				BigDecimal promotionCondition_1 = new BigDecimal("0.00");
				promotionCondition_1 = activity.getPromotion_condition_1();
				if (activity.getIf_first_work() != null && activity.getIf_first_work() == 1) {

					// 判断其是否是首单
					Long count = this.selectCountByUserId(userId);
					if (count > 0) {
						logger.info("此用户并非首单,不满足条件");
						continue;
					}
				} else {
					if (activity.getActivity_type() == Constants.ACTIVITY_TYPE.FULL_REDUCE) {
						if (productPrice.compareTo(promotionCondition_1) >= 0) {

							// 订单满减
							BigDecimal fullSubtractPrice = new BigDecimal("0.00");
							fullSubtractPrice = new BigDecimal(
									(activity.getFullReduceMoney() == null ? "0" : activity.getFullReduceMoney()));
							immediateSendModel.setIsFullSubtract(true);
							immediateSendModel.setFullSubtractPrice(fullSubtractPrice);
						}
					} else if (sendPrice != null && activity.getActivity_type() == Constants.ACTIVITY_TYPE.FREE_FREIGHT) {
						if (productPrice.compareTo(promotionCondition_1) >= 0) {
							// 免运费
							immediateSendModel.setIsFreeFreight(true);
							immediateSendModel.setFreeFreightPrice(sendPrice);
						}
					}
				}
			}
		}

		return immediateSendModel;
	}

	public Map<String, Object> cancelOrder(Long groupId, boolean isCustomer, String userId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		// 查询订单信息
		OrderGroup orderGroup = mapper.selectByPrimaryKey(groupId);
		if (orderGroup == null) {
			resMap.put("message", "订单不存在");
			Util.writeError(resMap);
			return resMap;
		}
		// 先查询订单状态是否是已取消状态，如果是已取消状态则不可再次进行取消
		Integer ostatus = orderGroup.getOrderGroupStatus();
		if (ostatus == Constants.ORDER_GROUP_STATUS.CUSTOMER_CANCELE || ostatus == Constants.ORDER_GROUP_STATUS.BUSSINESS_CANCELE
				|| ostatus == Constants.ORDER_GROUP_STATUS.TIMEOUT_CANCELE || ostatus == Constants.ORDER_GROUP_STATUS.KFCZ_REFUND) {
			resMap.put("message", "订单已取消，请勿重复取消订单");
			Util.writeError(resMap);
			return resMap;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("groupId", groupId);
		if (isCustomer) {
			params.put("orderGroupStatus", Constants.ORDER_GROUP_STATUS.CUSTOMER_CANCELE);
		} else {
			params.put("orderGroupStatus", Constants.ORDER_GROUP_STATUS.BUSSINESS_CANCELE);
		}
		params.put("orderCancelTime", new Date());

		// 更新订单状态(已取消) --> 执行退款操作 --> 更新退款单状态
		boolean updateStatusFlg = this.updateStatusById(params, "1");
		if (updateStatusFlg) {
			// 进行库存恢复
			orderGroupProductService.updateProductQuantity(groupId);
			// 进行退款操作
			if (orderGroup != null
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
						boolean uflag = uhdOrderService.returnserviceToUhd(Configure.shop_id_linjia, orderGroup, orderGroupProductList,
								userId, String.valueOf(map.get("refundId")));
						if (uflag) {
							logger.info("SUCCESS::::[订单号=" + groupId + "]抛退换货单到鼎力云成功.");
						} else {
							logger.info("FAIL::::[订单号=" + groupId + "]抛退换货单到鼎力云失败.");
						}
					}

					Util.writeSuccess(resMap);
					Util.writeOk(resMap);
				} else {
					Util.writeFail(resMap);
					Util.writeError(resMap);
				}
			} else {
				Util.writeSuccess(resMap);
				Util.writeOk(resMap);
			}
		} else {
			Util.writeFail(resMap);
			Util.writeError(resMap);
		}
		return resMap;
	}

	@Override
	public List<Long> selectDaDaFinOrder() {
		return mapper.selectDaDaFinOrder();
	}

	@Override
	public Integer selectStatusByPrimaryKey(Long groupId) {
		return mapper.selectStatusByPrimaryKey(groupId);
	}

	/**
	 * 将价钱换购商品转成购物车商品存入订单商品表
	 * lixinling 
	 * 2017年1月4日 下午2:59:50
	 * @param tp
	 * @param pCode
	 * @param immediateProductList
	 */
	public void convertTradeProductToShoppingCar(ActivityTradeProduct tp, String pCode, List<ShoppingCar> immediateProductList) {
		ShoppingCar sc = new ShoppingCar();
		sc.setProductId(Long.valueOf(tp.getProduct_id()));
		sc.setpCode(tp.getP_code());
		sc.setpName(tp.getP_name()+"--加钱换购");
		sc.setQuantity(1);
		BigDecimal itemPrice = new BigDecimal(tp.getP_trade_price().toString());
		BigDecimal productPrice = new BigDecimal(tp.getP_price().toString());
		sc.setItemPrice(itemPrice);
		sc.setProductPrice(productPrice);
		sc.setDiscountAmount(productPrice.subtract(itemPrice));
		sc.setApportedDiscountAmount(new BigDecimal("0.00"));
		sc.setItemTotalPrice(productPrice);
		sc.setpSendType(tp.getP_send_type());
		sc.setParentPCode(pCode);

		immediateProductList.add(sc);
	}
	
	/**
	 * 对整单优惠金额进行分摊
	 * lixinling 
	 * 2017年2月17日 下午1:40:01
	 * @param totalProductPrice 订单商品总金额
	 * @param preferentialPrice 整单优惠总金额
	 * @param shoppingCarList 商品列表
	 */
	public void apportionWholeDiscountAmount(BigDecimal totalProductPrice, BigDecimal preferentialPrice, List<ShoppingCar> shoppingCarList){
		
		//订单商品数
		int pNum = shoppingCarList.size();
		if(pNum == 1){
			//单条商品记录
			ShoppingCar item = shoppingCarList.get(0);
			item.setApportedDiscountAmount(preferentialPrice);
		}else{
			//多条商品记录
			//最后一条记录分摊金额
			BigDecimal lastItemApportionAmount = preferentialPrice;
			//单条商品记录分摊金额
			BigDecimal itemApportionAmount;
			for(int i =0;i<pNum;i++){
				ShoppingCar item = shoppingCarList.get(i);
				if(i == pNum-1){
					item.setApportedDiscountAmount(lastItemApportionAmount);
				}else{
					//单条商品记录分摊金额 = 整单优惠总金额 * (单条记录总金额/商品总金额)
					itemApportionAmount = preferentialPrice.multiply(item.getItemPrice().divide(totalProductPrice,2,BigDecimal.ROUND_HALF_UP)).setScale(2,BigDecimal.ROUND_HALF_UP);
					item.setApportedDiscountAmount(itemApportionAmount);
					lastItemApportionAmount = lastItemApportionAmount.subtract(itemApportionAmount);
				}
			}
		}
	}
}
