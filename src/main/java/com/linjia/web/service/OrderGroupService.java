package com.linjia.web.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.ActivityInfo;
import com.linjia.web.model.OrderGroup;
import com.linjia.web.model.OrderProductModel;
import com.linjia.web.model.ShoppingCar;
import com.linjia.web.query.OrderGroupQuery;

public interface OrderGroupService extends BaseService<OrderGroup, Long> {

	/**
	 * 确认订单操作
	 * lixinling
	 * 2016年7月25日 上午11:10:12
	 * @param orderGroup
	 * @param shoppingCarList
	 * @param isPrepare 是否是预约购买下单
	 * @return
	 */
    OrderGroup insertConfirmOrder(OrderGroup orderGroup, List<ShoppingCar> shoppingCarList, boolean isPrepare);

	/**
	 * 更改订单状态
	 * lixinling 
	 * 2016年7月28日 下午3:32:16
	 * @param map
	 * @return
	 */
	boolean updateStatusById(Map<String, Object> map, String platform);

	/**
	 * 查询我的全部订单
	 * lixinling 
	 * 2016年7月28日 下午3:32:16
	 * @param map
	 * @return
	 */
	List<OrderGroup> selectMyAllOrderList(OrderGroupQuery query);

	/**
	 * 判断商品的状态（0库存不足；1正常；），并计算每种商品的总金额
	 * 
	 * @param totalPrice
	 *            购物车总商品数量
	 * @param totalQty
	 *            购物车总价格 lixinling 2016年7月18日 下午1:36:34
	 */
    Map<String, Object> handleOrderProduct(HttpServletRequest request, List<ShoppingCar> shoppingCarList,
                                           Map<String, Object> totalMap, String mallCode, String userId, boolean isConfirm);

	/**
	 * 查询用户当前订单数量
	 * lixinling 
	 * 2016年9月26日 下午4:14:30
	 * @param userId
	 * @return
	 */
	Long selectCountByUserId(String userId);

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
    Map<String, Object> handlePromotionItemPrice(String mallCode, int buyQty, String userId, BigDecimal salePrice,
                                                 BigDecimal itemPrice_Promotion, List<ActivityInfo> promotionActivityList, boolean shoppingCarFlag);

	/**
	 * 催单
	 * lixinling 
	 * 2016年9月26日 下午4:14:30
	 * @param userId
	 * @return
	 */
	int updateUrgeNumById(String mallCode, Long groupId);

	/**
	 * 查询我的发送物流的订单全部信息
	 * xiangsy 
	 * 2016年7月28日 下午3:32:16
	 * @param map
	 * @return
	 */
	OrderGroup selectLogisOrderInfo(Long order_id);

	/**
	 * 查询所有已超时的订单
	 * lixinling 
	 * 2016年10月04日 下午3:32:16
	 * @return
	 */
	List<OrderGroup> selectAllTimeOutOrder();
	
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
    OrderProductModel activityCurrentTime(String mallCode, String userId, BigDecimal productPrice, BigDecimal sendPrice,
                                          OrderProductModel immediateSendModel);
	
	/**
	 * 退款服务
	 * 
	 * @param groupId
	 * @param isCustomer
	 * @param userId
	 * @return
	 */
    Map<String, Object> cancelOrder(Long groupId, boolean isCustomer, String userId);
	
	/**
	 * 查询达达1取货1小时以后订单未完成的订单号
	 */
    List<Long> selectDaDaFinOrder();

	/**
	 * 查询订单状态
	 * lixinling 
	 * 2016年12月13日 下午3:32:16
	 * @param map
	 * @return
	 */
	Integer selectStatusByPrimaryKey(Long groupId);
}
