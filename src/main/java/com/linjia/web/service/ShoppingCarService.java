package com.linjia.web.service;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;





import javax.servlet.http.HttpServletRequest;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.LargeCatagory;
import com.linjia.web.model.ShoppingCar;
import com.linjia.web.query.ShoppingCarQuery;

public interface ShoppingCarService extends BaseService<ShoppingCar, Long>{
	
	/**
	 * 更新购物车商品数量
	 * lixinling
	 * 2016年7月14日 下午3:25:27
	 * @param shoppingCar
	 * @return
	 */
	boolean updateQuantityByPrimaryKey(int quantity, long id, long product_id, String p_code, String mallCode, Integer activityType, Long activityId);
	
	/**
	 * 查询用户购物车所有商品
	 * lixinling
	 * 2016年7月15日 上午10:27:58
	 * @param query
	 * @return
	 */
	List<ShoppingCar> selectCarAllProduct(ShoppingCarQuery query);
	
	/**
	 * 将商品加入购物车
	 * lixinling
	 * 2016年7月15日 上午10:27:58
	 * @param query
	 * @return
	 */
    int insertCar(ShoppingCar shoppingCar, String mallCode);
	

	/**
	 * 查询购物车中商品的数量
	 * lixinling
	 * 2016年7月18日 上午11:43:58
	 * @param map
	 * @return
	 */
	ShoppingCar selectProductByPCodeAndUserId(Map map);
	
	/**
	 * 根据商品code列表和userId查询购物车中要结算的商品
	 * lixinling
	 * 2016年7月18日 上午11:43:58
	 * @param map
	 * @return
	 */
	List<ShoppingCar> selectCarProductByChecked(Map map);
	
	/**
	 * 判断商品的状态（0库存不足；1正常；），并计算每种商品的总金额
	 * 
	 * @param totalPrice
	 *            购物车总商品数量
	 * @param totalQty
	 *            购物车总价格 lixinling 2016年7月18日 下午1:36:34
	 */
    Map<String, Object> handleProduct(HttpServletRequest request, ShoppingCar item, Map<String, Object> totalMap, String mallCode, String userId);

	/**
	 * 更新购物车商品数量
	 * lixinling
	 * 2016年7月14日 下午3:25:27
	 * @param shoppingCar
	 * @return
	 */
	boolean updateTradeProductIds(Map map);
	
	/**
	 * 将加钱换购金额计算进总商品金额中
	 * lixinling 
	 * 2016年12月30日 下午4:57:52
	 * @param tradeProductIds
	 * @param activityId
	 */
    BigDecimal calculateTradeProductPrice(BigDecimal totalProductPrice, String tradeProductIds, Long activityId);
}
