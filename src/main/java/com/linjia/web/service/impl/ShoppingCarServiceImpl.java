package com.linjia.web.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.constants.Constants;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.dao.ActivityTradeProductMapper;
import com.linjia.web.dao.MiaoshaActivityProductMapper;
import com.linjia.web.dao.ShoppingCarMapper;
import com.linjia.web.model.ActivityInfo;
import com.linjia.web.model.ActivityTradeProduct;
import com.linjia.web.model.MallProductStore;
import com.linjia.web.model.MiaoshaActivityProduct;
import com.linjia.web.model.Product;
import com.linjia.web.model.ShoppingCar;
import com.linjia.web.model.TailGoodsClear;
import com.linjia.web.query.ShoppingCarQuery;
import com.linjia.web.service.ActivityPromotionService;
import com.linjia.web.service.MallProductStoreService;
import com.linjia.web.service.MiaoshaActivityProductService;
import com.linjia.web.service.OrderGroupService;
import com.linjia.web.service.ProductService;
import com.linjia.web.service.ShoppingCarService;
import com.linjia.web.service.TailGoodsClearService;

@Service
@Transactional
public class ShoppingCarServiceImpl extends BaseServiceImpl<ShoppingCar, Long> implements ShoppingCarService {

	@Resource
	private ShoppingCarMapper mapper;

	@Resource
	private MiaoshaActivityProductMapper miaoshaMapper;

	@Autowired
	private ProductService productService;

	@Autowired
	private MiaoshaActivityProductService miaoshaActivityProductService;

	@Autowired
	private TailGoodsClearService tailGoodsClearService;

	@Autowired
	private MallProductStoreService mallProductStoreService;

	@Autowired
	private ActivityPromotionService activityPromotionService;

	@Autowired
	private OrderGroupService orderGroupService;

	@Autowired
	private ActivityTradeProductMapper activityTradeProductMapper;

	@Override
	public BaseDao<ShoppingCar, Long> getDao() {
		return mapper;
	}

	/*
	 * 更新购物车中商品库存
	 */
	@Override
	public boolean updateQuantityByPrimaryKey(int quantity, long id, long product_id, String p_code, String mallCode, Integer activityType,
			Long activityId) {
		// 查询ERP库存进行判断
		int currentQty = productService.getCurrentQty(product_id, p_code, mallCode);
		if (quantity > currentQty) {
			return false;
		}

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("quantity", quantity);
		param.put("id", id);
		param.put("activityType", activityType);
		param.put("activityId", activityId);
		mapper.updateQuantityByPrimaryKey(param);
		return true;
	}

	/*
	 * 将商品加入购物车
	 * 
	 * return :0库存不足；1成功过；
	 */
	@Override
	public int insertCar(ShoppingCar shoppingCar, String mallCode) {
		long product_id = shoppingCar.getProductId();
		String p_code = shoppingCar.getpCode();

		// 查询ERP库存进行判断
		int currentQty = productService.getCurrentQty(product_id, p_code, mallCode);
		if (currentQty <= 0) {
			return 0;
		}

		// 判断当前购物车是否已经存在该商品，如果存在，则商品数量+1，不存在，则加入购物车
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pCode", p_code);
		map.put("userId", shoppingCar.getUserId());

		ShoppingCar resShoppingCar = mapper.selectProductByPCodeAndUserId(map);
		if (resShoppingCar == null) {
			shoppingCar.setCreDate(new Date());

			// 将商品加入购物车
			mapper.insertSelective(shoppingCar);
			return 1;
		} else {
			// 商品数量+本次购买数量
			int totalQty = resShoppingCar.getQuantity() + shoppingCar.getQuantity();

			if (totalQty > currentQty) {
				return 0;
			}

			/*
			 * Map<String,Object> param = new HashMap<String,Object>();
			 * param.put("quantity", totalQty); param.put("id",
			 * resShoppingCar.getId());
			 * mapper.updateQuantityByPrimaryKey(param);
			 */
			this.updateQuantityByPrimaryKey(totalQty, resShoppingCar.getId(), product_id, p_code, mallCode, shoppingCar.getActivityType(),
					shoppingCar.getActivityId());
			if (totalQty - currentQty == 0) {
				// 最后一件的情况
				return 1;
			} else {
				// 库存充足
				return 2;
			}
		}

	}

	/*
	 * 查询购物车商品列表
	 */
	@Override
	public List<ShoppingCar> selectCarAllProduct(ShoppingCarQuery query) {
		List<ShoppingCar> carProductList = mapper.selectCarAllProduct(query);
		if (carProductList != null && carProductList.size() > 0) {
			for (ShoppingCar item : carProductList) {
				if (!Tools.isEmpty(item.getTradeProductIds())) {
					String[] tradeProductIdArray = item.getTradeProductIds().split(",");
					Long activityId = item.getTradeActivityId();
					
					Map<String,Object> params = new HashMap<String,Object>();
					params.put("array", tradeProductIdArray);
					params.put("activityId", activityId);
					List<ActivityTradeProduct> activityTradeProductList = activityTradeProductMapper.selectTradedProduct(params);
					item.setActivityTradeProductList(activityTradeProductList);
				}
			}
		}
		return mapper.selectCarAllProduct(query);
	}

	/*
	 * 根据商品code和userId查询要结算中商品
	 */
	@Override
	public ShoppingCar selectProductByPCodeAndUserId(Map map) {
		return mapper.selectProductByPCodeAndUserId(map);
	}

	/**
	 * 判断商品的状态（0库存不足；1正常；），并计算每种商品的总金额
	 * 
	 * @param totalPrice
	 *            购物车总商品数量
	 * @param totalQty
	 *            购物车总价格 lixinling 2016年7月18日 下午1:36:34
	 */
	public Map<String, Object> handleProduct(HttpServletRequest request, ShoppingCar item, Map<String, Object> totalMap, String mallCode,
			String userId) {
		// 库存不足的商品Code
		StringBuilder lowQtyPName;
		BigDecimal totalProductPrice;
		int totalProductNum;
		if (totalMap == null) {
			totalMap = new HashMap<String, Object>();
			totalProductPrice = new BigDecimal("0.00");
			totalProductNum = 0;
			lowQtyPName = new StringBuilder();
		} else {
			totalProductPrice = (BigDecimal) totalMap.get("totalProductPrice");
			totalProductNum = (int) totalMap.get("totalProductNum");
			lowQtyPName = (StringBuilder) totalMap.get("lowQtyPName");
			if (lowQtyPName == null) {
				lowQtyPName = new StringBuilder();
			}
		}

		BigDecimal itemPrice = new BigDecimal("0.00");
		System.out.println("totalProductPrice:" + totalProductPrice + "   ___totalProductNum:" + totalProductNum);

		// 商品库存
		int currentQty = 0;
		// 商品价格
		BigDecimal salePrice = new BigDecimal("0.00");
		try {
			if (item.getpSendType() == Constants.PRODUCT_SEND_TYPE.IMMEDIATE) {
				// 及时送商品查询库存和价格
				// 查询商品的库存
				currentQty = productService.getCurrentQty(item.getProductId(), item.getpCode(), mallCode);

				// 取得商品在门店中的价格
				MallProductStore mallProductStore = mallProductStoreService.selectByMallAndPCode(mallCode, item.getpCode());
				salePrice = mallProductStore.getSalePrice();
			} else if (item.getpSendType() == Constants.PRODUCT_SEND_TYPE.PREPARE) {
				// 预约商品查询库存和价格
				Product product = productService.selectById(item.getProductId());
				// 库存
				currentQty = product.getQuantity();
				salePrice = product.getSalePrice();
			}
		} catch (Exception e) {
			item.setItemStatus(Constants.QTY_STATUS.LOW);
			// e.printStackTrace();
			logger.error("==========================>>>该门店没有对应的商品");
			return null;
		}

		// 购买商品数量
		int buyQty = item.getQuantity();

		// 判断是否是正在进行的秒杀商品(如果是，则根据每个用户限制数量，按秒杀价进行计算，如果超出限制数量的则按原价进行计算)
		MiaoshaActivityProduct miaoshaActivityProduct = miaoshaActivityProductService.getPanicingProductByProductId(item.getProductId());
		// 判断是否参加尾货清仓活动
		TailGoodsClear tailGoodsClear = tailGoodsClearService.selectByProductId(item.getProductId(), mallCode);
		// 购买商品参加的促销活动列表
		List<ActivityInfo> promotionActivityList = activityPromotionService.selectActInfoByCurrTime(item.getProductId(), mallCode);

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
		// 活动表的主键id(加钱换购活动)
		Long tmpTradeActivityId = null;
		// 活动的名称(加钱换购活动)
		String tmpTradeActivityName = null;

		if (miaoshaActivityProduct != null) {
			// 每个用户限购数量
			int limitQty = miaoshaActivityProduct.getLimitQuantity() == null ? 1 : miaoshaActivityProduct.getLimitQuantity();
			// 秒杀价
			BigDecimal panicPrice = miaoshaActivityProduct.getPbPrice();

			if (limitQty >= buyQty) {
				itemPrice_Miaosha = itemPrice_Miaosha.add(panicPrice.multiply(new BigDecimal(String.valueOf(buyQty))));
			} else if (limitQty < buyQty) {
				// Product product =
				// productService.selectById(item.getProductId());
				// BigDecimal salePrice = product.getSalePrice();
				// 超出限购数量的按原价计算
				int overQty = buyQty - limitQty;
				// 秒杀价商品总和
				itemPrice_Miaosha = itemPrice_Miaosha.add(panicPrice.multiply(new BigDecimal(String.valueOf(limitQty))));

				// 原价商品总和
				itemPrice_Miaosha = itemPrice_Miaosha.add(salePrice.multiply(new BigDecimal(String.valueOf(overQty))));
			}
		}
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
					/*
					 * Map<String, Object> params = new HashMap<String,
					 * Object>(); // 不满足检索条件数时,不在列表显示;并将其状态更改为已售罄
					 * params.put("activityStatus",
					 * Constants.TAIL_GOODS_CLEAR_ACTIVITY_STATUS.SELLOUT);
					 * params.put("id", item.getId());
					 * tailGoodsClearService.updateActivityStatusByPrimaryKey
					 * (params);
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
		}
		/*
		 * else { // Product product =
		 * productService.selectById(item.getProductId()); // BigDecimal
		 * salePrice = product.getSalePrice();
		 * 
		 * // 原价商品总和 itemPrice = itemPrice.add(salePrice.multiply(new
		 * BigDecimal(String.valueOf(buyQty)))); }
		 */

		// 处理促销活动
		Map<String, Object> data = orderGroupService.handlePromotionItemPrice(mallCode, buyQty, userId, salePrice, itemPrice_Promotion,
				promotionActivityList, true);

		tmpActivityType = (Integer) data.get("tmpActivityType");
		tmpActivityId = (Long) data.get("tmpActivityId");
		itemPrice_Promotion = (BigDecimal) data.get("itemPrice_Promotion");
		tmpTradeActivityId =  (Long) data.get("tmpTradeActivityId");
		tmpTradeActivityName =  (String) data.get("tmpTradeActivityName");
		item.setTradeActivityId(tmpTradeActivityId);
		item.setTradeActivityName(tmpTradeActivityName);
		// item.setActivityTradeProductList((ArrayList)
		// data.get("activityTradeProductList"));

		Map<BigDecimal, String> priceMap = new TreeMap<BigDecimal, String>();
		// 选出价格最低的作为本条记录的价格
		if (itemPrice_Miaosha != null && itemPrice_Miaosha.doubleValue() != 0.0) {
			/*
			 * item.setActivityType(7);
			 * item.setActivityId(miaoshaActivityProduct.getId()); itemPrice =
			 * itemPrice_Miaosha;
			 */
			priceMap.put(itemPrice_Miaosha, Constants.ACTIVITY_TYPE.MIAOSHA + "_" + miaoshaActivityProduct.getId());
		}
		if (itemPrice_Tail != null && itemPrice_Tail.doubleValue() != 0.0) {
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

		if (itemPrice.doubleValue() == 0) {
			itemPrice = itemPrice.add(salePrice.multiply(new BigDecimal(String.valueOf(buyQty))));
		}
		// 设置购物车每条记录的价格
		item.setItemPrice(itemPrice);

		if (currentQty >= buyQty) {
			item.setItemStatus(Constants.QTY_STATUS.NORMAL);
			// 购买总数量
			totalProductNum += buyQty;
			totalProductPrice = totalProductPrice.add(itemPrice);
			
			if(item.getActivityId() != null && !Tools.isEmpty(item.getTradeProductIds())){
				//计算加钱换购商品的金额
				totalProductPrice = calculateTradeProductPrice(totalProductPrice, item.getTradeProductIds(), item.getTradeActivityId());
			}
		} else {
			item.setItemStatus(Constants.QTY_STATUS.LOW);
			if (lowQtyPName != null) {
				if (lowQtyPName.length() > 0) {
					lowQtyPName.append(",").append(item.getpName());
				} else {
					lowQtyPName.append(item.getpName());
				}
			}
		}

		totalMap.put("totalProductPrice", totalProductPrice);
		totalMap.put("totalProductNum", totalProductNum);

		return totalMap;
	}

	/**
	 * 根据商品code列表和userId查询购物车中要结算的商品
	 * lixinling
	 * 2016年7月18日 上午11:43:58
	 * @param map
	 * @return
	 */
	@Override
	public List<ShoppingCar> selectCarProductByChecked(Map map) {
		return mapper.selectCarProductByChecked(map);
	}

	@Override
	public boolean updateTradeProductIds(Map map) {
		return mapper.updateTradeProductIds(map);
	}

	/**
	 * lixinling 
	 * 2016年12月30日 下午4:57:52
	 * @param tradeProductIds
	 * @param activityId
	 */
	@Override
	public BigDecimal calculateTradeProductPrice(BigDecimal totalProductPrice, String tradeProductIds, Long activityId){
		String[] tradeProductIdArray = tradeProductIds.split(",");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("array", tradeProductIdArray);
		params.put("activityId", activityId);
		List<ActivityTradeProduct> activityTradeProductList = activityTradeProductMapper.selectTradedProduct(params);
		for(ActivityTradeProduct tp : activityTradeProductList){
			totalProductPrice = totalProductPrice.add(new BigDecimal(tp.getP_trade_price()));
		}
		return totalProductPrice;
	}

}
