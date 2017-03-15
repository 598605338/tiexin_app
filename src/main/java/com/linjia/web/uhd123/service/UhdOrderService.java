package com.linjia.web.uhd123.service;

import java.util.Date;
import java.util.List;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.FavouriteProduct;
import com.linjia.web.model.OrderGroup;
import com.linjia.web.model.OrderGroupProduct;

/** 
 * 鼎力云订单
 * @author  lixinling: 
 * @date 2016年10月11日 上午10:30:41 
 * @version 1.0 
*/
public interface UhdOrderService {

	/**
	 * 提交订单到鼎力云
	 * lixinling 
	 * 2016年10月17日 下午2:09:52
	 * @param shop_id 平台商家：6666[邻家便利店平台]微电汇[weidianhui]；30328[百度外卖]百度外卖[baiduwaimai]；358[美团外卖]美团外卖[meituan]
	 * @param orderGroup
	 * @param orderGroupProductList
	 * @param userId 抛单者userId
	 * @return
	 */
	boolean submitOrder(String shop_id, OrderGroup orderGroup, List<OrderGroupProduct> orderGroupProductList, String userId);
	
	/**
	 * 更新鼎力云订单配送状态
	 * lixinling 
	 * 2016年10月17日 下午2:09:52
	 * @param platformId:平台号：1商城；2百度；3美团
	 * @param orderSuccessTime:订单完成时间
	 * @return
	 */
	boolean updateOrderDeliverToUhd(String uhdOrderId, Date orderSuccessTime, String userId, String deliver_status, String operation, String operation_state);
	
	/**
	 * 提交鼎力云退换货单
	 * lixinling 
	 * 2016年10月17日 下午2:09:52
	 * @param shop_id 平台商家：6666[邻家便利店平台]微电汇[weidianhui]；30328[百度外卖]百度外卖[baiduwaimai]；358[美团外卖]美团外卖[meituan]
	 * @return
	 */
	boolean returnserviceToUhd(String shop_id, OrderGroup orderGroup, List<OrderGroupProduct> orderGroupProductList, String userId, String refundId);
	
	/**
	 * 鼎力云确认退货状态
	 * lixinling 
	 * 2016年10月17日 下午2:09:52
	 * @param platformId:平台号：1商城；2百度；3美团
	 * @param returnserviceId:退换货id
	 * @return
	 */
	boolean confirmReturnserviceToUhd(String returnserviceId, String userId);
	
	/**
	 * 订单占货
	 * lixinling 
	 * 2016年10月17日 下午2:09:52
	 * @param shop_id 平台商家：6666[邻家便利店平台]微电汇[weidianhui]；30328[百度外卖]百度外卖[baiduwaimai]；358[美团外卖]美团外卖[meituan]
	 * @param orderSuccessTime:订单完成时间
	 * @return
	 */
	boolean orderstocklockToUhd(String shop_id, OrderGroup orderGroup, List<OrderGroupProduct> orderGroupProductList, String userId);
	
	/**
	 * 订单释放库存
	 * lixinling 
	 * 2016年10月17日 下午2:09:52
	 * @param platformId:平台号：6666商城；2百度；3美团
	 * @param orderSuccessTime:订单完成时间
	 * @return
	 */
	boolean orderstockunlockToUhd(String order_id, String userId);

	/**
	 * 测试提交订单到鼎力云
	 * lixinling 
	 * 2016年10月11日 上午10:35:20
	 * @param orderGroupId
	 * @return
	 */
	boolean testSubmitOrder(Long orderGroupId, String userId);
	
	/**
	 * 测试提交退换货单到鼎力云
	 * lixinling 
	 * 2016年10月11日 上午10:35:20
	 * @param orderGroupId
	 * @return
	 */
	boolean testReturnservice(Long orderGroupId, String userId, String refundId);
}
