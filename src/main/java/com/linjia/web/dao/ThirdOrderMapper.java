package com.linjia.web.dao;

import java.util.List;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.SumOrder;
import com.linjia.web.model.ThirdOrder;
import com.linjia.web.model.ThirdOrderProduct;
import com.linjia.web.query.SumOrderQuery;
import com.linjia.web.query.ThirdLogisOrderQuery;

public interface ThirdOrderMapper extends BaseDao<ThirdOrder, Long>{
	
	/**
	 * 查询店铺订单数据列表
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param app_poi_code 店铺码
	 * @return
	 */
	List<ThirdOrder> selectOrderlist(ThirdLogisOrderQuery query);
	
	/**
	 * 查询店铺提醒订单数据列表
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param app_poi_code 店铺码
	 * @return
	 */
	List<ThirdOrder> selectWarnOrderlist(ThirdLogisOrderQuery query);
	
	/**
	 * 查询邻家预约订单和自配送订单,已完成，未完成，已取消
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param app_poi_code 店铺码
	 * @return
	 */
	List<ThirdOrder> selectLinJOrderlist(ThirdLogisOrderQuery query);
	
	/**
	 * 查询美团已完成，未完成，已取消
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param app_poi_code 店铺码
	 * @return
	 */
	List<ThirdOrder> selectMtOrderlist(ThirdLogisOrderQuery query);
	
	/**
	 * 查询百度已完成，未完成，已取消
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param app_poi_code 店铺码
	 * @return
	 */
	List<ThirdOrder> selectBdOrderlist(ThirdLogisOrderQuery query);
	
	/**
	 * 查询京东已完成，未完成，已取消
	 * lxl
	 * 2017年3月3日 下午16:45:33
	 * @return
	 */
	List<ThirdOrder> selectJdOrderlist(ThirdLogisOrderQuery query);
	/**
	 * 查询邻家店铺订单详情
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param app_poi_code 店铺码
	 * @return
	 */
	ThirdOrder selectMyOrder(ThirdLogisOrderQuery query);
	
	/**
	 * 查询百度店铺订单详情
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param app_poi_code 店铺码
	 * @return
	 */
	ThirdOrder selectBdOrder(ThirdLogisOrderQuery query);
	
	/**
	 * 查询京东店铺订单详情
	 * xiangsy
	 * 2017年03月01日 下午16:45:33
	 * @return
	 */
	ThirdOrder selectJdOrder(ThirdLogisOrderQuery query);
	
	/**
	 * 查询美团店铺订单详情
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param app_poi_code 店铺码
	 * @return
	 */
	ThirdOrder selectMtOrder(ThirdLogisOrderQuery query);
	
	/**
	 * 查询邻家商品详情
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param app_poi_code 店铺码
	 * @return
	 */
	List<ThirdOrderProduct> selectMyOrderDetail(Long order_id);
	
	/**
	 * 统计邻家订单数据
	 * @param query
	 * @return
	 */
	List<SumOrder> selectSumLinjia(SumOrderQuery query);
	
	/**
	 * 统计美团订单数据
	 * @param query
	 * @return
	 */
	List<SumOrder> selectSumMeiTuan(SumOrderQuery query);
	
	/**
	 * 统计百度订单数据
	 * @param query
	 * @return
	 */
	List<SumOrder> selectSumBaidu(SumOrderQuery query);
	
	/**
	 * 统计京东到家订单数据
	 * @param query
	 * @return
	 */
	List<SumOrder> selectSumJingdong(SumOrderQuery query);
	
	/**
	 * 统计所有订单数据
	 * @param query
	 * @return
	 */
	List<SumOrder> selectSumAll(ThirdLogisOrderQuery query);
	
	/**
	 * 统计当天订单数据
	 * @param query
	 * @return
	 */
	SumOrder sumCurDayData(ThirdLogisOrderQuery query);
	
	/**
	 * 查询有预约订单到时间的店铺设备id
	 * @return
	 */
	List<String> selectLjReserveOrder();
	
	/**
	 * 美团推送数据条数查询(确定推送数据是否已存在)
	 * @param orderId
	 * @return
	 */
	int selectMtOrderNum(Long orderId);
	
	/**
	 * 查询邻家抛单数据(备用)
	 * xiangsy
	 * 2017年2月9日 下午14:45:33
	 * @return
	 */
	List<ThirdOrder> selectLJpdlist(ThirdLogisOrderQuery query);
	
	/**
	 * 查询饿了么订单详情
	 * @param orderId
	 * @return
	 */
	List<ThirdOrder> selectElemeOrder(ThirdLogisOrderQuery query);
	
	/**
	 * 查询饿了么已完成，未完成，已取消
	 * xiangsy
	 * 2017年3月7日 下午16:45:33
	 * @param shopId 店铺码
	 * @return
	 */
	List<ThirdOrder> selectElemeSumlist(ThirdLogisOrderQuery query);
	
	/**
	 * 查询京东预约订单，及时提醒商家
	 * lxl
	 * 2017年3月10日 下午16:45:33
	 * @return
	 */
	List<String> selectJdReserveOrder();
}