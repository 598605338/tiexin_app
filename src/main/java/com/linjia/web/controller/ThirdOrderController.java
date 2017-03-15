package com.linjia.web.controller;

import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.JsonDeserializationContext;
import com.linjia.constants.Constants;
import com.linjia.tools.DateComFunc;
import com.linjia.tools.JSONUtil;
import com.linjia.tools.OrderToLogisOrder;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.model.Logistics;
import com.linjia.web.model.LogistisDmInfo;
import com.linjia.web.model.OrderGroup;
import com.linjia.web.model.OrderGroupProduct;
import com.linjia.web.model.PaoDanRecord;
import com.linjia.web.model.ProductLack;
import com.linjia.web.model.SumOrder;
import com.linjia.web.model.ThirdOrder;
import com.linjia.web.model.ThirdOrderProduct;
import com.linjia.web.query.LogisticsQuery;
import com.linjia.web.query.SumOrderQuery;
import com.linjia.web.query.ThirdLogisOrderQuery;
import com.linjia.web.service.BdOrderProductService;
import com.linjia.web.service.ElemeOrderService;
import com.linjia.web.service.LogisticsService;
import com.linjia.web.service.MtOrderProductService;
import com.linjia.web.service.OrderGroupProductService;
import com.linjia.web.service.OrderGroupService;
import com.linjia.web.service.OrderRefundService;
import com.linjia.web.service.PaoDanRecordService;
import com.linjia.web.service.ProductLackService;
import com.linjia.web.service.ThirdJDdjService;
import com.linjia.web.service.ThirdOrderService;
import com.linjia.web.test.DadaTestData;
import com.linjia.web.thirdService.JGpush.service.JgPushDeviceService;
import com.linjia.web.thirdService.baidu.constants.BaiduConfig;
import com.linjia.web.thirdService.baidu.model.BaiduData;
import com.linjia.web.thirdService.baidu.model.BaiduReOrder;
import com.linjia.web.thirdService.baidu.model.BdShop;
import com.linjia.web.thirdService.baidu.model.BdUser;
import com.linjia.web.thirdService.baidu.model.Coord;
import com.linjia.web.thirdService.baidu.model.Order;
import com.linjia.web.thirdService.baidu.model.Product;
import com.linjia.web.thirdService.baidu.service.BaiduOrderService;
import com.linjia.web.thirdService.bdlogistics.factory.BdLogisticConfig;
import com.linjia.web.thirdService.bdlogistics.post.PostData;
import com.linjia.web.thirdService.bdlogistics.service.BdLogisService;
import com.linjia.web.thirdService.bea.model.ElemeResData;
import com.linjia.web.thirdService.bea.request.ElemeCreateOrderRequest.ElemeCreateRequestData;
import com.linjia.web.thirdService.bea.service.BeaService;
import com.linjia.web.thirdService.dada.model.DaDaLogisticsParam;
import com.linjia.web.thirdService.dada.service.LogisticsAccessTokenService;
import com.linjia.web.thirdService.dada.service.LogisticsOrderService;
import com.linjia.web.thirdService.eleme.interfaces.entity.order.OOrder;
import com.linjia.web.thirdService.jddj.config.JDDJConfigure;
import com.linjia.web.thirdService.jddj.model.OrderInfoDTO;
import com.linjia.web.thirdService.map.service.MapService;
import com.linjia.web.thirdService.meituan.constants.MtConfig;
import com.linjia.web.thirdService.meituan.model.MtOrder;
import com.linjia.web.thirdService.meituan.model.MtReOrder;
import com.linjia.web.thirdService.meituan.service.MtOrderService;
import com.linjia.web.thirdService.meituan.service.OrderService;
import com.linjia.web.thirdService.meituan.vo.LogisticsParam;
import com.linjia.web.thirdService.meituan.vo.OrderFoodDetailParam;
import com.linjia.web.uhd123.common.Configure;
import com.linjia.web.uhd123.service.UhdOrderService;

/**
 * @author xiangsy:
 * @date 2016年7月1日 下午4:16:39
 * @version 1.0
 */
@Controller
@RequestMapping("/thirdOrder")
public class ThirdOrderController {
	private static Logger LOG = LoggerFactory.getLogger(ThirdOrderController.class);

	@Autowired
	private ThirdOrderService thirdOrderService;
	@Autowired
	private BaiduOrderService baiduOrderService; // 百度
	@Autowired
	private LogisticsAccessTokenService logisticsAccessTokenService;
	@Autowired
	private LogisticsOrderService logisticsOrderService; // 达达
	@Autowired
	private OrderService mtOrderService; // 美团
	@Autowired
	private MtOrderService mtLocalOrderService;// 美团本地表操作
	@Autowired
	private BdOrderProductService bdOrderProductService;// 百度本地库订单商品
	@Autowired
	private MtOrderProductService mtOrderProductService;// 美团本地库订单商品
	@Autowired
	private OrderGroupService orderGroupService;// 邻家订单
	@Autowired
	private OrderGroupProductService orderGroupProductService;// 邻家订单商品
	@Autowired
	private OrderRefundService orderRefundService;// 退款服务
	@Autowired
	private ProductLackService productLackService;// 缺少商品
	@Autowired
	private BdLogisService bdLogisService;
	@Autowired
	private LogisticsService logisticsService;
	@Autowired
	private JgPushDeviceService jgPushDeviceService;
	@Autowired
	private UhdOrderService uhdOrderService;
	@Autowired
	private PaoDanRecordService paoDanRecordService;
	@Autowired
	private BeaService beaService;
	@Autowired
	private  MapService mapService;
	@Autowired
	private ThirdJDdjService thirdJDdjService;
	@Autowired
	private ElemeOrderService elemeOrderService;

	/**
	 * 获取新订单列表 pay_status支付状态为1 ，order_group_status订单状态为1
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getOrderList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getBaseInfo(HttpServletRequest request,
			@RequestParam String mall_code, @RequestParam String shopId,
			@RequestParam String app_poi_code, String produceStationNoIsv, @RequestParam String pageNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ThirdLogisOrderQuery query = new ThirdLogisOrderQuery();
			query.setMall_code(mall_code);
			query.setShop_id(shopId);
			query.setApp_poi_code(app_poi_code);
			query.setProduceStationNoIsv(produceStationNoIsv);
			query.setPay_status(1);
			query.setBdPatyStatus(2);
			query.setMtPatyStatus(2);
			query.setStatus(1);
			query.setLogStatus(10);
//			query.setSend_type(0);
			query.setBdOrderStatus(1);
			query.setMtOrderStatus(3);
			query.setJdOrderStatus(JDDJConfigure.JDDJ_ORDER_STATUS.UNHANDLE);//订单状态（实物类：20010:锁定，20020:订单取消，20040:超时未支付系统取消，31000:等待付款，31020:已付款，41000:待处理，32000:等待出库，33040:配送中，33060:已妥投 34000:京东已收款，90000:订单完成；服务类：20010:锁定，20020:订单取消，20040:超时未支付系统取消，31000:等待付款，31020:已付款，41000:待接单,41010:已接单,51010:开始服务 51020:结束服务，90000:订单完成）
			query.setPageSize(Constants.PAGE.SIZE);
			query.setPageIndex(Integer.parseInt(pageNum));
			query.setElemeOrderStatus(1);
			List<ThirdOrder> list = thirdOrderService.selectOrderlist(query);
			Util.writeResultData(map, list);
			Util.writeOk(map);
			Util.writeSuccess(map);
		} catch (Exception e) {
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}

	/**
	 * 获取订单详情
	 * @param request
	 * @param orderSource 订单来源(1:邻家；2美团；3，百度；4，京东)
	 * @param orderId
	 * @param shopId
	 * @return
	 */
	@RequestMapping("/orderDetail")
	@ResponseBody
	public Object orderDetail(HttpServletRequest request,
			@RequestParam String orderSource, @RequestParam Long orderId,
			@RequestParam String shopId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ThirdOrderProduct> list = null;
		List backExtras=new ArrayList();
		ThirdOrder thirdOrder = null;
		try {
			if ("1".equals(orderSource)) {
				thirdOrder = thirdOrderService.selectMyOrder(shopId, orderId);
				if (thirdOrder != null) {
					list = thirdOrderService.selectMyOrderDetail(orderId);
					thirdOrder.setProducts(list);
				}
			} else if ("2".equals(orderSource)) {
				thirdOrder = thirdOrderService.selectMtOrder(shopId, orderId);
				if (thirdOrder != null) {
					list = mtOrderProductService.selectMtOrderProduct(orderId);
					thirdOrder.setProducts(list);
				}
				String extras=thirdOrder.getExtras();
				//赠品过滤
				if(!Tools.isEmpty(extras)){
					List extrasArr= JSONUtil.fromJson(extras, List.class);
					if(extrasArr!=null&&extrasArr.size()>0){
						for (int i = 0; i < extrasArr.size(); i++) {
							Map op=(Map) extrasArr.get(i);
							if(op!=null&&op.size()>0){
								String remark=(String) op.get("remark");
								if(remark!=null&&!Tools.isEmpty(remark)){
									if(remark.indexOf("赠送")>0){
										backExtras.add(op);
									}
								}
							}
						}
					}
					thirdOrder.setExtrasArr(backExtras);
				}
			} else if ("3".equals(orderSource)) {
				thirdOrder = thirdOrderService.selectBdOrder(shopId, orderId);
				if (thirdOrder != null) {
					list = bdOrderProductService.selectBdOrderProduct(orderId);
					thirdOrder.setProducts(list);
				}
			} else if ("4".equals(orderSource)) {
				thirdOrder = thirdOrderService.selectJdOrder(shopId, orderId);
				if (thirdOrder != null) {
					list = thirdJDdjService.selectJdOrderProduct(orderId);
					thirdOrder.setProducts(list);
				}
			}else if ("5".equals(orderSource)) {
				ThirdLogisOrderQuery querye=new ThirdLogisOrderQuery();
				querye.setOrder_id(orderId);
				List<ThirdOrder> thirdOrderlist = thirdOrderService.selectElemeOrder(querye);
				if (thirdOrderlist != null&&thirdOrderlist.size()>0) {
					thirdOrder=thirdOrderlist.get(0);
					if (thirdOrder != null) {
						list =elemeOrderService.selectElemeOrderGoods(orderId+"");
						thirdOrder.setProducts(list);
					}
				}
			}
			Util.writeResultData(map, thirdOrder);
			Util.writeOk(map);
			Util.writeSuccess(map);
		} catch (Exception e) {
			Util.writeError(map);
			Util.writeFail(map);
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 确认订单
	 * @param request
	 * @param orderSource 订单来源(1:邻家；2美团；3，百度；4，京东)
	 * @param orderId
	 * @param shopId
	 * @return
	 */
	@RequestMapping("/confirmOrder")
	@ResponseBody
	public Object confirmOrder(HttpServletRequest request,
			@RequestParam String orderSource, @RequestParam Long orderId,
			@RequestParam String shopId) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject json = null;
		OrderGroup og = null;
		List<OrderGroupProduct> ogList = null;
		try {
			// 确认本地订
			// 调用OrderController.confirmOrder
			if ("1".equals(orderSource)) {
				//查询订单状态
				og = orderGroupService.selectLogisOrderInfo(orderId);
				if(og==null){
					Util.writeError(map);
					map.put("message", "未查询到订单信息！");
					return map;
				}
				if(og.getOrderGroupStatus()>1){
					Util.writeError(map);
					map.put("message", "订单状态已改变,请刷新查看！");
					return map;
				}
				//当天第几单
				Integer orderNum=Util.getTodayOrderNum("L",shopId);
				// 确认本地订单
				Map<String, Object> updmap = new HashMap<String, Object>();
				updmap.put("orderNum",orderNum);
				updmap.put("orderGroupStatus", 2);
				updmap.put("groupId", orderId);
				updmap.put("busConfirmTime", new Date());
				int b = thirdOrderService.updateLinjOrderStatus(updmap);
				// -------------改为配送订单到物流(达达创建订单)---------
				if (b > 0) {
					Util.writeOk(map);
					Util.writeSuccess(map);
					if (og != null) {
						ogList = orderGroupProductService.selectProductListByGroupId(orderId);
						//抛单
						boolean uflag=uhdOrderService.submitOrder(Configure.shop_id_linjia, og, ogList, shopId);
						if(!uflag){
//							map.put("message", "接单成功,抛单失败!");
							//回滚订单状态
//							updmap.put("orderGroupStatus", 1);
//							thirdOrderService.updateLinjOrderStatus(updmap);
							//查询抛单记录
							Map<String,Object> pdmap=new HashMap<String, Object>();
							pdmap.put("order_id", orderId);
							pdmap.put("order_type", 1);
							pdmap.put("paodan_type", 1);
							PaoDanRecord selPd=paoDanRecordService.selectPaoDan(pdmap);
							if(selPd==null){
								//记录抛单数据
								PaoDanRecord pd = new PaoDanRecord(orderId,1,1,1,new Date(),null,0);
								boolean flag=paoDanRecordService.insertPaoDan(pd);
								if(!flag){
									LOG.error("info:", "抛单记录失败！");
									LOG.error("订单id:", orderId+"	订单类型为1:商城订单");
								}
							}
						}
					}else{
						LOG.error("=========info:============================", "抛单查询记录失败！");
						LOG.error("订单id:", orderId+"	订单类型为1:商城订单");
					}
				} else {
					Util.writeError(map);
					map.put("message", "确认订单失败！");
				}
			} else if ("2".equals(orderSource)) {
				// -------------去掉本地订单操作，订单状态修改交给百度回调修改---------
				//查看订单状态
				MtReOrder mtorder=mtLocalOrderService.selectMtOrderAll(orderId);
				if(mtorder==null){
					Util.writeError(map);
					map.put("message", "未查询到订单信息！");
					return map;
				}
				if(mtorder.getStatus()>4){
					Util.writeError(map);
					map.put("message", "订单状态已改变,请刷新查看！");
					return map;
				}
				//当天第几单
				Integer orderNum=Util.getTodayOrderNum("M",shopId);
				// 修改本地美团订单状态
				MtOrder mt = new MtOrder();
				mt.setOrder_id(orderId + "");
				mt.setApp_poi_code(shopId);
				mt.setStatus("4");
				mt.setOrderNum(orderNum);
				JSONObject rejson = mtLocalOrderService.updateOrderConfirmed(mt);
				String status = rejson.getString("data");
				// -------------改为配送订单到物流(达达创建订单)---------
				if ("ok".equals(status)) {
					// 推送修改美团订单状态
					json = new JSONObject(mtOrderService.orderConfirm(orderId).toString());
					if (json != null&&!((json.toString()).isEmpty())) {
						String result = json.getString("data");
						if ("ok".equals(result) || "ng".equals(result)) {
							Util.writeOk(map);
							Util.writeSuccess(map);
							List<OrderFoodDetailParam> mtList=mtOrderProductService.selectMtOrderProductAll(orderId);
							og = OrderToLogisOrder.mtOrderToLjOrder(mtorder, mtList);
							if (og != null) {
								ogList = og.getProductList();
								//抛单
								boolean uflag=uhdOrderService.submitOrder(Configure.shop_id_meituan, og, ogList, og.getUserId());
								if(!uflag){
//									map.put("status","fail");
//									map.put("message", "抛单失败！");
//									return map;
									//回滚订单状态
//									mt.setStatus("3");
//									mtLocalOrderService.updateOrderConfirmed(mt);
									//查询抛单记录
									Map<String,Object> pdmap=new HashMap<String, Object>();
									pdmap.put("order_id", orderId);
									pdmap.put("order_type", 2);
									pdmap.put("paodan_type", 1);
									PaoDanRecord selPd=paoDanRecordService.selectPaoDan(pdmap);
									if(selPd==null){
										//记录抛单数据
										PaoDanRecord pd = new PaoDanRecord(orderId,2,1,1,new Date(),null,0);
										boolean flag=paoDanRecordService.insertPaoDan(pd);
										if(!flag){
											LOG.error("info:", "抛单记录失败！");
											LOG.error("订单id:", orderId+"	订单类型为2:美团订单");
										}
									}
								}
							}
						} else {
							Util.writeError(map);
							map.put("message", json.getString("msg"));
						}
					} else {
						Util.writeError(map);
						map.put("message", "美团确认失败！");
					}
				} else {
					Util.writeError(map);
					map.put("message", "更新本地表失败！");
				}
			} else if ("3".equals(orderSource)) {
				// -------------去掉本地订单操作，订单状态修改交给美团回调修改---------
				//查询订单状态
				BaiduReOrder bdorder=baiduOrderService.selectPdOrder(orderId);
				if(bdorder==null){
					Util.writeError(map);
					map.put("message", "未查询到订单信息！");
					return map;
				}
				Order reorder=bdorder.getOrder();
				if(reorder==null){
					Util.writeError(map);
					map.put("message", "未查询到订单详细信息！");
					return map;
				}
				if(reorder.getStatus()>5){
					Util.writeError(map);
					map.put("message", "订单状态已改变,请刷新查看！");
					return map;
				}
				BaiduData bd = new BaiduData();
				BdShop shop = new BdShop();
				BdUser user = new BdUser();
				user.setCoord(new Coord());
				Order order = new Order();
				order.setRecive_time(new Date());
				order.setStatus(5);
				order.setOrder_id(Long.toString(orderId));
				bd.setOrder(order);
				bd.setUser(user);
				bd.setShop(shop);
				//当天第几单
				Integer orderNum=Util.getTodayOrderNum("B",shopId);
				bd.setOrderNum(orderNum);
				JSONObject rejson = baiduOrderService.updateBdOrder(bd);
				String status = rejson.getString("status");
				// --------------------------------------
				if ("ok".equals(status)) {
					// 推送物流状态
					json = baiduOrderService.orderConfirm(orderId, "");
					JSONObject body = json.getJSONObject("body");
					//测试用
					if (body.getInt("errno") == 0 ) {
						Util.writeOk(map);
						Util.writeSuccess(map);
						//查询抛单对象
						List<com.linjia.web.thirdService.baidu.model.Product> bdProlist=bdOrderProductService.selectBdOrderProductAll(orderId);
						og = OrderToLogisOrder.bdOrderToLjOrder(bdorder, bdProlist);
						if (og != null) {
							ogList = og.getProductList();
							//抛单
							boolean uflag=uhdOrderService.submitOrder(Configure.shop_id_baiduwaimai, og, ogList, og.getUserId());
							if(!uflag){
//								map.put("status","fail");
//								map.put("message", "抛单失败！");
//								return map;
//								order.setStatus(1);
//								baiduOrderService.updateBdOrder(bd);
								//查询抛单记录
								Map<String,Object> pdmap=new HashMap<String, Object>();
								pdmap.put("order_id", orderId);
								pdmap.put("order_type", 3);
								pdmap.put("paodan_type", 1);
								PaoDanRecord selPd=paoDanRecordService.selectPaoDan(pdmap);
								if(selPd==null){
									//记录抛单数据
									PaoDanRecord pd = new PaoDanRecord(orderId,3,1,1,new Date(),null,0);
									boolean flag=paoDanRecordService.insertPaoDan(pd);
									if(!flag){
										LOG.error("info:", "抛单记录失败！");
										LOG.error("订单id:", orderId+"	订单类型为3:百度订单");
									}
								}
							}
						}
					} else {
						Util.writeError(map);
						map.put("message", body.getString("error"));
					}
				} else {
					Util.writeError(map);
					map.put("message", "更新本地表失败！");
				}
			}else if ("4".equals(orderSource)) {
				//查看订单状态
				OrderInfoDTO jdOrderInfo = thirdJDdjService.selectById(orderId);
				if(jdOrderInfo==null){
					Util.writeError(map);
					map.put("message", "未查询到订单信息！");
					return map;
				}
				if(jdOrderInfo.getOrderStatus() != 41000){//订单状态（实物类：20010:锁定，20020:订单取消，20040:超时未支付系统取消，31000:等待付款，31020:已付款，41000:待处理，32000:等待出库，33040:配送中，33060:已妥投 34000:京东已收款，90000:订单完成；）
					Util.writeError(map);
					map.put("message", "订单状态已改变,请刷新查看！");
					return map;
				}
				//当天第几单
				Integer orderNum=Util.getTodayOrderNum("J",shopId);
				OrderInfoDTO order = thirdJDdjService.selectById(orderId);
				order.setOrderNum(orderNum);
				
				// 修改本地订单状态并更新京东到家订单状态，然后进行鼎力云抛单操作
				if (thirdJDdjService.orderAcceptOperate(order, shopId, true)) {
					
					//接单完成后直接召唤物流
					thirdJDdjService.orderJDZBDelivery(orderId, shopId);
					
					map.put("resultData", new JSONArray());
					Util.writeOk(map);
					Util.writeSuccess(map);
				}else{
					Util.writeError(map);
					Util.writeFail(map);
				}
			}else if ("5".equals(orderSource)) {
				// -------------去掉本地订单操作，订单状态修改交给美团回调修改---------
				//查询订单状态
				OOrder elemorder=elemeOrderService.selectElemeOrderById(orderId+"");
				if(elemorder==null){
					Util.writeError(map);
					map.put("message", "未查询到订单信息！");
					return map;
				}
				if(elemorder.getOrder_status()>1){
					Util.writeError(map);
					map.put("message", "订单状态已改变,请刷新查看！");
					return map;
				}
				boolean flag = elemeOrderService.updateElemeOrder(orderId+"",null,1,null);
				// --------------------------------------
				if (flag) {
						Util.writeOk(map);
						Util.writeSuccess(map);
						//查询抛单对象
//						List<OGoodsItem> bdProlist=elemeOrderService.selectElemeOrderProducts(orderId+"");
//						og = OrderToLogisOrder.bdOrderToLjOrder(elemorder, bdProlist);
//						if (og != null) {
//							ogList = og.getProductList();
//							//抛单
//							boolean uflag=uhdOrderService.submitOrder(Configure.shop_id_baiduwaimai, og, ogList, og.getUserId());
//							if(!uflag){
//								//查询抛单记录
//								Map<String,Object> pdmap=new HashMap<String, Object>();
//								pdmap.put("order_id", orderId);
//								pdmap.put("order_type", 3);
//								pdmap.put("paodan_type", 1);
//								PaoDanRecord selPd=paoDanRecordService.selectPaoDan(pdmap);
//								if(selPd==null){
//									//记录抛单数据
//									PaoDanRecord pd = new PaoDanRecord(orderId,3,1,1,new Date(),null,0);
//									boolean eflag=paoDanRecordService.insertPaoDan(pd);
//									if(!eflag){
//										LOG.error("info:", "抛单记录失败！");
//										LOG.error("订单id:", orderId+"	订单类型为5:饿了么订单");
//									}
//								}
//							}
//						}
					} else {
						Util.writeError(map);
						map.put("message","error");
					}
				}
		} catch (Exception e) {
			Util.writeError(map);
			Util.writeFail(map);
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 取消订单
	 * @param request
	 * @param orderSource 订单来源(1:邻家；2美团；3，百度；)
	 * @param orderId
	 * @param shopId
	 * @return
	 */
	@RequestMapping("/cancelOrder")
	@ResponseBody
	public Object cancelOrder(HttpServletRequest request,
			@RequestParam String orderSource, @RequestParam Long orderId,
			@RequestParam String shopId, @RequestParam String reason,
			@RequestParam String reasonCode, @RequestParam String mall_code,
			@RequestParam String pCodes) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject json = null;
		OrderGroup og = null;
		List<OrderGroupProduct> ogList = null;
		String res="";
		if(reason!=null&&reason!=""){
			res=URLDecoder.decode(reason);
		}else{
			res="无";
		}
		try {
			// 调用OrderController.cancleOrder
			// 查询订单信息
			// OrderGroup orderGroup = orderGroupService.selectById(orderId);
			// if(orderGroup == null){
			// map.put("message", "订单不存在");
			// Util.writeError(map);
			// return map;
			// }
			// Map<String,Object> params = new HashMap<String,Object>();
			// params.put("groupId", orderId);
			// params.put("orderGroupStatus",
			// Constants.ORDER_GROUP_STATUS.BUSSINESS_CANCELE);
			// //更新订单状态
			// boolean updateStatusFlg =
			// orderGroupService.updateStatusById(params);
			// if(updateStatusFlg){
			// //进行库存恢复和退款操作
			// boolean qtyRecoverFlg =
			// orderGroupProductService.updateProductQuantity(orderId);
			//
			// //进行退款操作
			// if (orderGroup != null && orderGroup.getPayStatus() ==
			// Constants.PAY_STATUS.PAYD
			// && orderGroup.getOrderGroupStatus() ==
			// Constants.ORDER_GROUP_STATUS.PAYD) {
			// // 生成退款单并执行退款操作
			// Map<String,Object> mapfund =
			// orderRefundService.insertRefund(orderGroup, userId);
			// if((int)map.get("return_code") == 5){
			// Util.writeOk(mapfund);
			// }
			// }
			// }
			// 插入缺少的商品
			if (pCodes!=null&&!"".equals(pCodes)) {
				String[] pcodesArr = pCodes.split(",");
				for (int i = 0; i < pcodesArr.length; i++) {
					String code = pcodesArr[i];
					if (code != null) {
						ProductLack productLack = new ProductLack();
						productLack.setOrder_id(orderId);
						productLack.setP_code(code);
						productLack.setShop_id(shopId);
						productLack.setMall_code(mall_code);
						productLack.setPlatform(Integer.parseInt(orderSource));
						productLackService.insertLackProduct(productLack);
					}
				}
			}
			if ("1".equals(orderSource)) {
				if (orderId == null || orderId.longValue() == 0) {
					Util.writeError(map);
					map.put("message", "groupId为必传参数");
					return map;
				}

				// 查询订单信息
				OrderGroup orderGroup = orderGroupService.selectById(orderId);
				if (orderGroup == null) {
					Util.writeError(map);
					map.put("message", "订单不存在");
					return map;
				}
				
				Integer ostatus=orderGroup.getOrderGroupStatus();
				if(ostatus==5){
					map.put("status", "fail");
					map.put("message","订单已完成,请联系客服取消!");
					return map;
				}
				if(ostatus==4){
					//查询物流信息
					LogisticsQuery lquery=new LogisticsQuery();
					lquery.setOrder_id(orderId);
					Logistics logisq=logisticsService.selectLogisticsById(lquery);
					Integer logType=logisq.getLogis_type();
					if(logType!=null&&logType==2){
						//达达物流取消
//						JSONObject jsonObj=logisticsOrderService.canceOrderTest(orderId,reason);
						JSONObject jsonObj=logisticsOrderService.canceOrder(orderId,"4","顾客取消订单");
						boolean flag=logisticsAccessTokenService.initDadaCode(jsonObj);
						if(!flag){
							jsonObj=null;
							jsonObj=logisticsOrderService.canceOrder(orderId,"4","顾客取消订单");
//							jsonObj=logisticsOrderService.canceOrderTest(orderId,reason);
						}
						if(jsonObj==null||(jsonObj.toString().isEmpty())||!"ok".equals(jsonObj.getString("status"))){
							map.put("status", "fail");
							map.put("message", "商城订单已取消,请联系达达客服取消物流订单!");
						}
					}
					if(logType!=null&&logType==3){
						//百度物流取消
						String bdwlOrderId=logisq.getOuter_id();
						Long bdwlOrderIdVal=Long.valueOf(bdwlOrderId);
						JSONObject jsonObj2=bdLogisService.cancelOrder(bdwlOrderIdVal,"不喜欢");
						System.out.println("jsonObj2*********:"+jsonObj2.toString());
						if(jsonObj2!=null&&!(jsonObj2.toString().isEmpty())){
							if(jsonObj2.getInt("error_no")==0){
								Object res2=jsonObj2.get("result");
								if(res2 instanceof Boolean){
									boolean b=jsonObj2.getBoolean("result");
									if(b){
										map.put("status", "ok");
										map.put("message",res.toString());
									}else{
										map.put("status", "fail");
										map.put("message","取消百度物流异常!");
										return map;
									}
								}else{
									map.put("status", "fail");
									map.put("message","不在配送范围内!");
									return map;
								}
							}else{
								map.put("status", "fail");
								map.put("message","查询结果出错");
								return map;
							}
						}
					}
					if(logType!=null&&logType==4){
						//饿了么物流取消
						ElemeResData redata=beaService.cancelBeaOrder(orderId+"",2,"商家取消");
						if(redata!=null&&"200".equals(redata.getCode())){
							map.put("status", "ok");
							map.put("message", "商城订单已取消,饿了么物流取消订单!");
						}else{
							map.put("status", "fail");
							map.put("message", "商城订单已取消,请联系饿了么客服取消物流订单!");
						}
					}
				}
				//先查询订单状态是否是已取消状态，如果是已取消状态则不可再次进行取消
				if (ostatus == Constants.ORDER_GROUP_STATUS.CUSTOMER_CANCELE
						|| ostatus == Constants.ORDER_GROUP_STATUS.BUSSINESS_CANCELE
						|| ostatus == Constants.ORDER_GROUP_STATUS.TIMEOUT_CANCELE
						|| ostatus == Constants.ORDER_GROUP_STATUS.KFCZ_REFUND) {
					map.put("message", "订单已取消，请勿重复取消订单");
					map.put("status", "fail");
					return map;
				}
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("groupId", orderId);
				params.put("orderGroupStatus", Constants.ORDER_GROUP_STATUS.BUSSINESS_CANCELE);
				params.put("orderCancelTime", new Date());

				int b =0;
				String refundId = "";
				// 更新订单状态
				boolean updateStatusFlg = orderGroupService.updateStatusById(params, "1");
				if (updateStatusFlg) {
					// 进行库存恢复
					orderGroupProductService.updateProductQuantity(orderId);

					// 进行退款操作
					if (orderGroup != null && orderGroup.getPayStatus() == Constants.PAY_STATUS.PAYD
							&& (orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.PAYD 
							    || orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.CONFIRM
							    || orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.SENDING
							    || orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.SUCCESS
							    || orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.RECEIVED
							    || orderGroup.getOrderGroupStatus() == Constants.ORDER_GROUP_STATUS.WLQXCXFD)) {
						// 生成退款单并执行退款操作
						Map<String, Object> retmap = orderRefundService.insertRefund(orderGroup, mall_code, 0, 0, null);
						if ((int) retmap.get("return_code") == 5) {
							b = 1;
							refundId = String.valueOf(retmap.get("refundId"));
								// 如果是商家取消订单，并且是接单以后进行取消的情况下，需要抛退换货单到鼎力云
								// 查询订单商品的详细信息
								List<OrderGroupProduct> orderGroupProductList = orderGroupProductService.selectProductListByGroupId(orderId);
								boolean uflag = uhdOrderService.returnserviceToUhd(Configure.shop_id_linjia, orderGroup, orderGroupProductList, mall_code, refundId);
								if (uflag) {
									LOG.info("SUCCESS::::[订单号=" + orderId + "]抛退换货单到鼎力云成功.");
								} else {
									LOG.info("FAIL::::[订单号=" + orderId + "]抛退换货单到鼎力云失败.");
								}
						} else {
							map.put("message", "退款异常!");
							Util.writeError(map);
						}
					}
				} else {
					map.put("message", "更新订单状态异常!");
					Util.writeError(map);
				}
				
				if (b > 0) {
					Util.writeOk(map);
					Util.writeSuccess(map);
					//修改物流表状态
					Logistics lgs=new Logistics();
					lgs.setOrder_id(orderId+"");
					lgs.setOrder_status(3);
					logisticsService.updateLogistics(lgs);
					//抛单转换
					og = orderGroupService.selectLogisOrderInfo(orderId);
					if (og != null) {
						ogList = orderGroupProductService.selectProductListByGroupId(orderId);
						//抛单
						boolean uflag=uhdOrderService.returnserviceToUhd(Configure.shop_id_linjia, og, ogList, og.getUserId(), refundId);
						if(!uflag){
//							map.put("status","fail");
//							map.put("message", "抛单失败！");
//							return map;
//							updmap.put("orderGroupStatus", 2);
//							thirdOrderService.updateLinjOrderStatus(updmap);
							//查询抛单记录
							Map<String,Object> pdmap=new HashMap<String, Object>();
							pdmap.put("order_id", orderId);
							pdmap.put("order_type", 1);
							pdmap.put("paodan_type", 2);
							PaoDanRecord selPd=paoDanRecordService.selectPaoDan(pdmap);
							if(selPd==null){
								//记录抛单数据
								PaoDanRecord pd = new PaoDanRecord(orderId,1,1,2,new Date(),null,0);
								boolean flag=paoDanRecordService.insertPaoDan(pd);
								if(!flag){
									LOG.error("info:", "抛单记录失败！");
									LOG.error("订单id:", orderId+"	订单类型为1:商城订单");
								}
							}
						}
					}
				} else {
					Util.writeError(map);
					map.put("message", "退款及库存恢复异常失败！");
				}
			} else if ("2".equals(orderSource)) {
				//查询订单状态
				MtReOrder mtorder=mtLocalOrderService.selectMtOrderAll(orderId);
				if(mtorder==null){
					map.put("status", "fail");
					map.put("message","未查询到订单信息!");
					return map;
				}
				if(mtorder.getStatus()==8){
					map.put("status", "fail");
					map.put("message","订单已完成,请联系客服取消!");
					return map;
				}
				if(mtorder.getStatus()==9){
					map.put("status", "fail");
					map.put("message","订单已取消,请勿重复操作!");
					return map;
				}
				// 修改本地美团订单状态
				MtOrder mt = new MtOrder();
				mt.setOrder_id(orderId + "");
				mt.setApp_poi_code(shopId);
				mt.setCancelReason(res);
				if (!"".equals(reasonCode)) {
					mt.setCancelCode(Integer.valueOf(reasonCode));
				}
				mt.setStatus("9");
				JSONObject rejson = mtLocalOrderService.updateOrderConfirmed(mt);
				String status = rejson.getString("data");
				if ("ok".equals(status)) {
					// 推送物流状态
					json = new JSONObject(mtOrderService.orderCancel(orderId,reason, reasonCode).toString());
					if (json != null&&!((json.toString()).isEmpty())) {
						String result = json.getString("data");
						if ("ok".equals(result) || "ng".equals(result)) {
							Util.writeOk(map);
							Util.writeSuccess(map);
							List<OrderFoodDetailParam> mtList=mtOrderProductService.selectMtOrderProductAll(orderId);
							og = OrderToLogisOrder.mtOrderToLjOrder(mtorder, mtList);
							if (og != null) {
								ogList = og.getProductList();
								//抛单
								boolean uflag=uhdOrderService.returnserviceToUhd(Configure.shop_id_meituan, og, ogList, og.getUserId(), orderId+"");
								if(!uflag){
//									map.put("status","fail");
//									map.put("message", "订单取消失败，请稍后重试！");
//									return map;
//									mt.setStatus("4");
//									mtLocalOrderService.updateOrderConfirmed(mt);
									//查询抛单记录
									Map<String,Object> pdmap=new HashMap<String, Object>();
									pdmap.put("order_id", orderId);
									pdmap.put("order_type", 2);
									pdmap.put("paodan_type", 2);
									PaoDanRecord selPd=paoDanRecordService.selectPaoDan(pdmap);
									if(selPd==null){
										//记录抛单数据
										PaoDanRecord pd = new PaoDanRecord(orderId,2,1,2,new Date(),null,0);
										boolean flag=paoDanRecordService.insertPaoDan(pd);
										if(!flag){
											LOG.error("info:", "抛单记录失败！");
											LOG.error("订单id:", orderId+"	订单类型为2:美团订单");
										}
									}
								}
							}
						} else {
							Util.writeError(map);
							map.put("message", json.getString("msg"));
						}
					} else {
						Util.writeError(map);
						map.put("message", "美团取消失败！");
					}
				} else {
					Util.writeError(map);
					map.put("message", "更新本地表失败！");
				}
			} else if ("3".equals(orderSource)) {
				//查询订单状态
				BaiduReOrder bdorder=baiduOrderService.selectPdOrder(orderId);
				if(bdorder==null){
					map.put("status", "fail");
					map.put("message","未查询到订单信息!");
					return map;
				}
				Order reorder=bdorder.getOrder();
				if(reorder==null){
					Util.writeError(map);
					map.put("message", "未查询到订单详细信息！");
					return map;
				}
				if(reorder.getStatus()==9){
					map.put("status", "fail");
					map.put("message","订单已完成,请联系客服取消!");
					return map;
				}
				if(reorder.getStatus()==10){
					map.put("status", "fail");
					map.put("message","订单已取消,请勿重复操作!");
					return map;
				}
				// 修改本地百度订单状态
				BaiduData bd = new BaiduData();
				BdShop shop = new BdShop();
				BdUser user = new BdUser();
				user.setCoord(new Coord());
				Order order = new Order();
				order.setStatus(10);
				order.setOrder_id(Long.toString(orderId));
				if (!"".equals(reasonCode)) {
					order.setType(Integer.valueOf(reasonCode));
				}
				order.setReason(res);
				bd.setOrder(order);
				bd.setUser(user);
				bd.setShop(shop);
				JSONObject rejson = baiduOrderService.updateBdOrder(bd);
				String status = rejson.getString("status");
				if ("ok".equals(status)) {
					// 推送物流状态
					json = baiduOrderService.orderCancel(orderId,Integer.valueOf(reasonCode), reason, "");
					JSONObject body = json.getJSONObject("body");
					if (body.getInt("errno") == 0) {
						Util.writeOk(map);
						Util.writeSuccess(map);
						//查询抛单对象
						List<com.linjia.web.thirdService.baidu.model.Product> bdProlist=bdOrderProductService.selectBdOrderProductAll(orderId);
						og = OrderToLogisOrder.bdOrderToLjOrder(bdorder, bdProlist);
						if (og != null) {
							ogList = og.getProductList();
							//抛单
							boolean uflag=uhdOrderService.returnserviceToUhd(Configure.shop_id_baiduwaimai, og, ogList, og.getUserId(), orderId+"");
							if(!uflag){
//								map.put("status","fail");
//								map.put("message", "抛单失败！");
//								return map;
//								order.setStatus(5);
//								 baiduOrderService.updateBdOrder(bd);
								//查询抛单记录
								Map<String,Object> pdmap=new HashMap<String, Object>();
								pdmap.put("order_id", orderId);
								pdmap.put("order_type", 3);
								pdmap.put("paodan_type", 2);
								PaoDanRecord selPd=paoDanRecordService.selectPaoDan(pdmap);
								if(selPd==null){
									//记录抛单数据
									PaoDanRecord pd = new PaoDanRecord(orderId,3,1,2,new Date(),null,0);
									boolean flag=paoDanRecordService.insertPaoDan(pd);
									if(!flag){
										LOG.error("info:", "抛单记录失败！");
										LOG.error("订单id:", orderId+"	订单类型为3:百度订单");
									}
								}
							}
						}
					} else {
						Util.writeError(map);
						map.put("message", body.getString("error"));
					}
				} else {
					Util.writeError(map);
					map.put("message", "更新本地表失败！");
				}
			}else if ("5".equals(orderSource)) {
				//查询订单状态
				OOrder elemorder=elemeOrderService.selectElemeOrderById(orderId+"");
				if(elemorder==null){
					Util.writeError(map);
					map.put("message", "未查询到订单信息！");
					return map;
				}
				if(elemorder.getOrder_status()>7){
					Util.writeError(map);
					map.put("message", "订单状态已改变,请刷新查看！");
					return map;
				}
				boolean flag = elemeOrderService.updateElemeOrder(orderId+"",reason,2,9);
				// --------------------------------------
				if (flag) {
						Util.writeOk(map);
						Util.writeSuccess(map);
						//查询抛单对象
//						List<OGoodsItem> bdProlist=elemeOrderService.selectElemeOrderProducts(orderId+"");
//						og = OrderToLogisOrder.bdOrderToLjOrder(elemorder, bdProlist);
//						if (og != null) {
//							ogList = og.getProductList();
//							//抛单
//							boolean uflag=uhdOrderService.submitOrder(Configure.shop_id_baiduwaimai, og, ogList, og.getUserId());
//							if(!uflag){
//								//查询抛单记录
//								Map<String,Object> pdmap=new HashMap<String, Object>();
//								pdmap.put("order_id", orderId);
//								pdmap.put("order_type", 3);
//								pdmap.put("paodan_type", 2);
//								PaoDanRecord selPd=paoDanRecordService.selectPaoDan(pdmap);
//								if(selPd==null){
//									//记录抛单数据
//									PaoDanRecord pd = new PaoDanRecord(orderId,3,1,2,new Date(),null,0);
//									boolean flag=paoDanRecordService.insertPaoDan(pd);
//									if(!flag){
//										LOG.error("info:", "抛单记录失败！");
//										LOG.error("订单id:", orderId+"	订单类型为3:百度订单");
//									}
//								}
//							}
//						}
					} else {
						Util.writeError(map);
						map.put("message","error");
					}
				}
			// 客户端消息推送
			if ("ok".equals(map.get("status"))) {
				jgPushDeviceService.pushMessage(mall_code, "4",orderSource);
			}
		} catch (Exception e) {
			Util.writeError(map);
			Util.writeFail(map);
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 完成订单
	 * @param request
	 * @param orderSource 订单来源(1:邻家；2美团；3，百度)
	 * @param orderId
	 * @param shopId
	 * @return
	 */
	@RequestMapping("/finishOrder")
	@ResponseBody
	public Object finishOrder(HttpServletRequest request,
			@RequestParam String orderSource, @RequestParam Long orderId,
			@RequestParam String shopId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if ("1".equals(orderSource)) {
				Map<String, Object> updmap = new HashMap<String, Object>();
				updmap.put("orderGroupStatus", 5);
				updmap.put("groupId", orderId);
				updmap.put("orderSuccessTime", new Date());
				int b = thirdOrderService.updateLinjOrderStatus(updmap);
				if (b > 0) {
					Util.writeOk(map);
					Util.writeSuccess(map);
				} else {
					Util.writeError(map);
					map.put("message", "更新本地表失败！");
				}
			} else if ("2".equals(orderSource)) {
				// 修改本地美团订单状态
				MtOrder mt = new MtOrder();
				mt.setOrder_id(orderId + "");
				mt.setStatus("8");
				JSONObject rejson = mtLocalOrderService.updateOrderOver(mt);
				String status = rejson.getString("data");
				if ("ok".equals(status)) {
					// 推送物流状态
					JSONObject json = new JSONObject(mtOrderService.orderArrived(orderId).toString());
					if (json != null&&!((json.toString()).isEmpty())) {
						String result = json.getString("data");
						if ("ok".equals(result) || "ng".equals(result)) {
							Util.writeOk(map);
							Util.writeSuccess(map);
						} else {
							Util.writeError(map);
							map.put("message", json.getString("msg"));
						}
					}else {
						//回滚状态
						mt.setStatus("6");
						mtLocalOrderService.updateOrderOver(mt);
						Util.writeError(map);
						map.put("message", "美团完成订单失败！");
					}
					map.put("rusultData",JSONUtil.JSONToObj(json.toString(), Map.class));
				} else {
					Util.writeError(map);
					map.put("message", "更新本地表失败！");
				}
			} else if ("3".equals(orderSource)) {
				// 修改本地百度订单状态
				BaiduData bd = new BaiduData();
				BdShop shop = new BdShop();
				BdUser user = new BdUser();
				user.setCoord(new Coord());
				Order order = new Order();
				order.setStatus(9);
				order.setOrder_id(Long.toString(orderId));
				bd.setOrder(order);
				bd.setUser(user);
				bd.setShop(shop);
				JSONObject rejson = baiduOrderService.updateBdOrder(bd);
				String status = rejson.getString("status");
				if ("ok".equals(status)) {
					// 推送物流状态
					JSONObject json = baiduOrderService.orderComplete(orderId, "");
					JSONObject body = json.getJSONObject("body");
					if (body.getInt("errno") == 0) {
						Util.writeOk(map);
						Util.writeSuccess(map);
					} else {
						//回滚状态
						order.setStatus(8);
						baiduOrderService.updateBdOrder(bd);
						Util.writeError(map);
						map.put("message", body.getString("error"));
					}
					map.put("rusultData",JSONUtil.JSONToObj(json.toString(), Map.class));
				} else {
					Util.writeError(map);
					map.put("message", "更新本地表失败！");
				}
			}
			Util.writeOk(map);
			Util.writeSuccess(map);
		} catch (Exception e) {
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}

	/**
	 * 订单状态查询
	 * @param request
	 * @param orderSource 订单来源(1:邻家；2美团；3，百度)
	 * @param orderId
	 * @param shopId
	 * @return
	 */
	@RequestMapping("/queryLogisticInfo")
	@ResponseBody
	public Object queryOrderStatus(HttpServletRequest request,
			@RequestParam String orderSource, @RequestParam Long orderId,
			@RequestParam String shopId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			LogistisDmInfo logisticsInfo = new LogistisDmInfo();
			JSONObject json = null;
			if ("1".equals(orderSource)) {
				LogisticsQuery query = new LogisticsQuery();
				query.setOrder_id(orderId);
				Logistics logis = logisticsService.selectLogisticsById(query);
				if (logis != null) {
					logisticsInfo.setOrder_id(orderId + "");
					if (logis.getLogis_type() == 1) {
						logisticsInfo.setLogisticsName("自配送");
					} else if (logis.getLogis_type() == 2) {
						logisticsInfo.setLogisticsName("达达配送");
					} else if (logis.getLogis_type() == 3) {
						logisticsInfo.setLogisticsName("百度配送");
					}else if (logis.getLogis_type() == 4) {
						logisticsInfo.setLogisticsName("饿了么配送");
					}
					if (logis.getOrder_status() < 2) {
						logisticsInfo.setStatusName("待接单");
					} else if (logis.getOrder_status() == 2){
						logisticsInfo.setStatusName("已接单");
					} else if (logis.getOrder_status() == 3) {
						logisticsInfo.setStatusName("派送中");
					} else if (logis.getOrder_status() == 4) {
						logisticsInfo.setStatusName("已完成");
					}else if (logis.getOrder_status() == 10) {
						logisticsInfo.setStatusName("物流取消");
					}
					logisticsInfo.setStatus(logis.getOrder_status());
					logisticsInfo.setRecive_time(logis.getRecieve_time() + "");
					logisticsInfo.setSend_time(logis.getSend_time() + "");
					logisticsInfo.setFinished_time(logis.getFinish_time()+"");
					logisticsInfo.setDelivery_time(logis.getSend_time() + "");
					logisticsInfo.setDmName(logis.getDm_name());
					logisticsInfo.setDmPhone(logis.getDm_mobile());
					map.put("resultData", logisticsInfo);
					Util.writeOk(map);
					Util.writeSuccess(map);
				} else {
					map.put("resultData", logisticsInfo);
					Util.writeError(map);
					map.put("message", "没查到物流状态!");
				}
				return map;
			} else if ("2".equals(orderSource)) {
				LogisticsParam lsg=mtOrderService.orderLogisticsStatus(orderId);
				if (lsg != null&&lsg.getOrder_id()!=null) {
						logisticsInfo.setOrder_id(orderId + "");
						Integer status = lsg.getLogistics_status();
							if (status != null) {
								if (status < 10) {
									logisticsInfo.setStatusName("待接单");
								} else if (status == 20) {
									logisticsInfo.setStatusName("已取餐");
								}else if (status == 40) {
									logisticsInfo.setStatusName("已完成");
								}else if (status == 100) {
									logisticsInfo.setStatusName("美团已取消配送");
								}
							}
							logisticsInfo.setStatus(6);
							logisticsInfo.setLogisticsName("美团配送");
							logisticsInfo.setRecive_time(lsg.getFetch_time()+"");
							logisticsInfo.setSend_time(lsg.getSend_time()+"");
							logisticsInfo.setDmName(lsg.getDispatcher_name());
							logisticsInfo.setDmPhone(lsg.getDispatcher_mobile());
							Util.writeOk(map);
							Util.writeSuccess(map);
							map.put("resultData", logisticsInfo);
						}else {
							map.put("resultData", null);
							map.put("status", "fail");
							map.put("message", "对不起,未查到美团订单物流状态！");
						}
				return map;
			} else if ("3".equals(orderSource)) {
				json = (baiduOrderService.getOrderDetail(orderId, ""));
				JSONObject json1 = (baiduOrderService.orderStatusQuery(orderId, ""));
				JSONObject body = json.getJSONObject("body");
				JSONObject body1 = json1.getJSONObject("body");
				if (body.getInt("errno") == 0) {
					Util.writeOk(map);
					Util.writeSuccess(map);
					map.put("resultData", JSONUtil.JSONToObj(body.getJSONObject("data").toString(), Map.class));
					JSONObject orderObj = body.getJSONObject("data").getJSONObject("order");
					logisticsInfo.setOrder_id(orderId + "");
					String status = body1.getString("status");
					int ss = 0;
					if (status != null && status != "") {
						ss = Integer.valueOf(status);
						if (ss < 7) {
							logisticsInfo.setStatusName("待接单");
						} else if (ss == 7) {
							logisticsInfo.setStatusName("已取餐");
						} else if (ss == 8) {
							logisticsInfo.setStatusName("正在配送");
						} else if (ss == 9) {
							logisticsInfo.setStatusName("已完成");
						}
					}
					logisticsInfo.setStatus(ss);
					logisticsInfo.setLogisticsName("百度物流");
					logisticsInfo.setPickup_time(orderObj.getLong("pickup_time")+"");
					logisticsInfo.setAtshop_time(orderObj.getLong("atshop_time")+"");
					logisticsInfo.setSend_time(orderObj.getLong("delivery_time")+"");
					logisticsInfo.setDmPhone(orderObj.getString("delivery_phone"));
					logisticsInfo.setDmName("骑士");
					logisticsInfo.setFinished_time(orderObj.getString("finished_time"));
					logisticsInfo.setRecive_time(orderObj.getString("confirm_time"));
					
					Util.writeOk(map);
					Util.writeSuccess(map);
					map.put("resultData", logisticsInfo);
				} else {
					map.put("resultData", null);
					Util.writeError(map);
					map.put("message", body.getString("error"));
				}
			} else if ("4".equals(orderSource)) {
				OrderInfoDTO orderInfoDTO=thirdJDdjService.selectById(orderId);
				if (orderInfoDTO != null && orderInfoDTO.getOrderStatus() > 0) {
						logisticsInfo.setOrder_id(orderId + "");
						Integer status = orderInfoDTO.getOrderStatus();
								if (status == JDDJConfigure.JDDJ_ORDER_STATUS.UNHANDLE) {
									logisticsInfo.setStatusName("待接单");
								} else if (status == JDDJConfigure.JDDJ_ORDER_STATUS.WAIT_SEND) {
									logisticsInfo.setStatusName("等待出库");
								}else if (status == JDDJConfigure.JDDJ_ORDER_STATUS.DELIVERY || status == JDDJConfigure.JDDJ_ORDER_STATUS.FINISH) {
									logisticsInfo.setStatusName("已完成");
								}else if (status == JDDJConfigure.JDDJ_ORDER_STATUS.CANCEL) {
									logisticsInfo.setStatusName("已取消");
								}else if (status == JDDJConfigure.JDDJ_ORDER_STATUS.LOCK) {
									logisticsInfo.setStatusName("已锁定");
								}else if (status == JDDJConfigure.JDDJ_ORDER_STATUS.SENDDING) {
									logisticsInfo.setStatusName("配送中");
								}else if (status == JDDJConfigure.JDDJ_ORDER_STATUS.WAIT_CONFIRM) {
									logisticsInfo.setStatusName("等待商家确认");
								}
							logisticsInfo.setStatus(status);
							logisticsInfo.setLogisticsName("到家配送");
							logisticsInfo.setRecive_time(DateComFunc.formatDate(orderInfoDTO.getBusConfirmTime(), "yyyy-MM-dd HH:mm:ss"));
							logisticsInfo.setSend_time(DateComFunc.formatDate(orderInfoDTO.getDeliveryConfirmTime(), "yyyy-MM-dd HH:mm:ss"));
							logisticsInfo.setDmName(orderInfoDTO.getDeliveryManName());
							logisticsInfo.setDmPhone(orderInfoDTO.getDeliveryManPhone());
							Util.writeOk(map);
							Util.writeSuccess(map);
							map.put("resultData", logisticsInfo);
						}else {
							map.put("resultData", null);
							map.put("status", "fail");
							map.put("message", "对不起,未查到京东到家订单物流状态！");
						}
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}

	/**
	 * 获取取消理由
	 * @param request
	 * @param orderSource
	 * @return
	 */
	@RequestMapping("/orderCancelReason")
	@ResponseBody
	public Object orderCancelReason(HttpServletRequest request,
			@RequestParam String orderSource) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> cancelList = null;
		try {
			JSONObject json = null;
			if ("1".equals(orderSource)) {
				json = logisticsOrderService.getCanceReason();
				boolean flag = logisticsAccessTokenService.initDadaCode(json);
				if (!flag) {
					json = null;
					json = logisticsOrderService.getCanceReason();
				}
				if (json.getString("status").equals("ok")) {
					Util.writeOk(map);
					Util.writeSuccess(map);
					cancelList = (List<Map<String, Object>>) JSONUtil.JSONToObj(json.getJSONArray("result").toString(),List.class);
				} else {
					Util.writeError(map);
					map.put("message", json.getString("errorMsg"));
				}
			} else if ("2".equals(orderSource)) {
				cancelList = MtConfig.getMtCancelReason();
			} else if ("3".equals(orderSource)) {
				cancelList = BaiduConfig.getCancelReason();
			} else if ("4".equals(orderSource)) {
				cancelList = BdLogisticConfig.getBdLogisCancelReason();
			}
			map.put("resultData", cancelList);
			Util.writeOk(map);
			Util.writeSuccess(map);
		} catch (Exception e) {
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}

	/**
	 * 获取预约订单列表 send_type=0 and pay_status=1 and order_group_sattus=2
	 * send_date=DATE_FORMAT(NOW(),'%Y-%m-%d')
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getReserveOrderList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getReserveOrderList(HttpServletRequest request,
			@RequestParam String mall_code, @RequestParam String shopId,
			@RequestParam String app_poi_code, String produceStationNoIsv, @RequestParam String pageNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ThirdLogisOrderQuery query = new ThirdLogisOrderQuery();
			query.setMall_code(mall_code);
			query.setPay_status(1);
//			query.setStatus(2);
			int[] or=new int[]{2,10};
			query.setOrderStatusList(or);
//			query.setSend_type(0);
			query.setSumSend_date(new Date());
			//任意数用来控制排序字段
			query.setSend_hour(1);
			query.setPageSize(Constants.PAGE.SIZE);
			query.setPageIndex(Integer.parseInt(pageNum));
			List<ThirdOrder> list = thirdOrderService
					.selectLinJOrderlist(query);
			
			query.setProduceStationNoIsv(produceStationNoIsv);
			query.setJdOrderStatus(JDDJConfigure.JDDJ_ORDER_STATUS.WAIT_SEND);
			query.setDeliveryTime(1L);//查询预约单
			List<ThirdOrder> jdList = thirdOrderService
					.selectJdOrderlist(query);
			if(list != null && jdList != null && jdList.size() > 0){
				list.addAll(jdList);
			}else if(list == null && jdList != null){
				list = jdList;
			}
			Util.writeResultData(map, list);
			Util.writeOk(map);
			Util.writeSuccess(map);
		} catch (Exception e) {
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}

	/**
	 * 获取催单订单列表 urgeNum>0 and pay_status=1 and order_group_status=1,2,4 and send_type=1
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getUrgeOrderList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getUrgeOrderList(HttpServletRequest request,
			@RequestParam String mall_code, @RequestParam String shopId,
			@RequestParam String app_poi_code, @RequestParam String pageNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ThirdLogisOrderQuery query = new ThirdLogisOrderQuery();
			query.setMall_code(mall_code);
			query.setShop_id(shopId);
			query.setApp_poi_code(app_poi_code);
			query.setPay_status(1);
			query.setBdPatyStatus(2);
			query.setMtPatyStatus(2);
			int[] statusList = new int[] {2,4,10};
			int[] BdOrderStatusList = new int[] {5,7,8};
			int[] MtOrderStatusList = new int[] {4,6};
			int[] eOrderStatusList = new int[] {2,3,4};
			query.setOrderStatusList(statusList);
			query.setBdOrderStatusList(BdOrderStatusList);
			query.setMtOrderStatusList(MtOrderStatusList);
			query.setElemeOrderStatusList(eOrderStatusList);
			query.setUrgeNum(2);
			query.setPageSize(Constants.PAGE.SIZE);
			query.setPageIndex(Integer.parseInt(pageNum));
			List<ThirdOrder> list = thirdOrderService.selectWarnOrderlist(query);
			Util.writeResultData(map, list);
			Util.writeOk(map);
			Util.writeSuccess(map);
		} catch (Exception e) {
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}

	/**
	 * 获取取消订单列表 order_gourp_status=3,6
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getCancelOrderList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getCancelOrderList(HttpServletRequest request,
			@RequestParam String mall_code, @RequestParam String shopId,
			@RequestParam String app_poi_code, String produceStationNoIsv, @RequestParam String pageNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ThirdLogisOrderQuery query = new ThirdLogisOrderQuery();
			query.setMall_code(mall_code);
			query.setShop_id(shopId);
			query.setApp_poi_code(app_poi_code);
			query.setProduceStationNoIsv(produceStationNoIsv);
			query.setPay_status(1);
			query.setBdPatyStatus(2);
			query.setMtPatyStatus(2);
			query.setElemeOrderStatus(8);
			int[] statusList = new int[] {3,6,8,9};
			query.setOrderStatusList(statusList);
			query.setBdOrderStatus(10);
			query.setMtOrderStatus(9);
			query.setJdOrderStatus(JDDJConfigure.JDDJ_ORDER_STATUS.CANCEL);
			query.setPageSize(Constants.PAGE.SIZE);
			query.setPageIndex(Integer.parseInt(pageNum));
			List<ThirdOrder> list = thirdOrderService.selectWarnOrderlist(query);
			Util.writeResultData(map, list);
			Util.writeOk(map);
			Util.writeSuccess(map);
		} catch (Exception e) {
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}

	/**
	 * 获取自配送订单列表 send_type=0 and pay_status=1
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getSendselfOrderList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getSendselfOrderList(HttpServletRequest request,
			@RequestParam String mall_code, @RequestParam String shopId,
			@RequestParam String app_poi_code, @RequestParam String pageNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ThirdLogisOrderQuery query = new ThirdLogisOrderQuery();
			query.setMall_code(mall_code);
			query.setPay_status(1);
			query.setStatus(2);
			query.setSend_type(0);
			query.setSend_logistics_type(1);
			query.setPageSize(Constants.PAGE.SIZE);
			query.setCre_date(new Date());
			query.setPageIndex(Integer.parseInt(pageNum));
			List<ThirdOrder> list = thirdOrderService.selectLinJOrderlist(query);
			Util.writeResultData(map, list);
			Util.writeOk(map);
			Util.writeSuccess(map);
		} catch (Exception e) {
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}

	/**
	 * 获取已接订单列表 pay_status=1 and order_group_status=2
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getRecivedOrderList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getRecivedOrderList(HttpServletRequest request,
			@RequestParam String orderSource, @RequestParam String shopId,
			@RequestParam String pageNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ThirdOrder> list = null;
		try {
			ThirdLogisOrderQuery query = new ThirdLogisOrderQuery();
			query.setPageSize(Constants.PAGE.SIZE);
			query.setPageIndex(Integer.parseInt(pageNum));
			if ("1".equals(orderSource)) {
				query.setMall_code(shopId);
				query.setPay_status(1);
				query.setCre_date(new Date());
				int[] orderSList=new int[]{2,4,10};
				query.setOrderStatusList(orderSList);
				list = thirdOrderService.selectLinJOrderlist(query);
			} else if ("2".equals(orderSource)) {
				query.setApp_poi_code(shopId);
				int[] mtorderSList=new int[]{4,6};
				query.setMtPatyStatus(2);
				query.setMtOrderStatusList(mtorderSList);
				list = thirdOrderService.selectMtOrderlist(query);
			} else if ("3".equals(orderSource)) {
				query.setShop_id(shopId);
				int[] bdorderSList=new int[]{5,7,8};
				query.setBdPatyStatus(2);
				query.setBdOrderStatusList(bdorderSList);
				list = thirdOrderService.selectBdOrderlist(query);
			} else if ("4".equals(orderSource)) {
				query.setProduceStationNoIsv(shopId);
				int[] jdorderSList=new int[]{41000,32000,33040,20030,20010}; //实物类：20010:锁定，20020:订单取消，20040:超时未支付系统取消，31000:等待付款，31020:已付款，41000:待处理，32000:等待出库，33040:配送中，33060:已妥投 34000:京东已收款，90000:订单完成
				query.setJdOrderStatusList(jdorderSList);
				list = thirdOrderService.selectJdOrderlist(query);
			}else if ("5".equals(orderSource)) {
				query.setShop_id(shopId);
				int[] eorderSList=new int[]{2,3,4,5,6,7,9}; 
				query.setElemeOrderStatusList(eorderSList);
				list = thirdOrderService.selectElemeSumlist(query);
			} else {
				list = new ArrayList<ThirdOrder>();
			}
			Util.writeResultData(map, list);
			Util.writeOk(map);
			Util.writeSuccess(map);
		} catch (Exception e) {
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}

	/**
	 * 获取已完成订单列表 pay_status=1 and order_group_status=5
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getfinishedOrderList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getfinishedOrderList(HttpServletRequest request,
			@RequestParam String orderSource, @RequestParam String shopId,
			@RequestParam String pageNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ThirdOrder> list = null;
		try {
			ThirdLogisOrderQuery query = new ThirdLogisOrderQuery();
			query.setPageSize(Constants.PAGE.SIZE);
			query.setPageIndex(Integer.parseInt(pageNum));
			if ("1".equals(orderSource)) {
				query.setPay_status(1);
				query.setMall_code(shopId);
				query.setCre_date(new Date());
				query.setStatus(5);
				list = thirdOrderService.selectLinJOrderlist(query);
			} else if ("2".equals(orderSource)) {
				query.setApp_poi_code(shopId);
				query.setMtPatyStatus(2);
				query.setMtOrderStatus(8);
				list = thirdOrderService.selectMtOrderlist(query);
			} else if ("3".equals(orderSource)) {
				query.setShop_id(shopId);
				query.setBdPatyStatus(2);
				query.setBdOrderStatus(9);
				list = thirdOrderService.selectBdOrderlist(query);
			} else if ("4".equals(orderSource)) {
				query.setProduceStationNoIsv(shopId);
				int[] jdorderSList=new int[]{33060,90000}; //实物类：20010:锁定，20020:订单取消，20040:超时未支付系统取消，31000:等待付款，31020:已付款，41000:待处理，32000:等待出库，33040:配送中，33060:已妥投 34000:京东已收款，90000:订单完成
				query.setJdOrderStatusList(jdorderSList);
				list = thirdOrderService.selectJdOrderlist(query);
			}else if ("5".equals(orderSource)) {
				query.setShop_id(shopId);
				query.setElemeOrderStatus(7);
				list = thirdOrderService.selectElemeSumlist(query);
			} else {
				list = new ArrayList<ThirdOrder>();
			}
			Util.writeResultData(map, list);
			Util.writeOk(map);
			Util.writeSuccess(map);
		} catch (Exception e) {
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}

	/**
	 * 获取已失效订单列表 pay_status=1 and order_group_status=3,6
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getInvalidOrderList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getInvalidOrderList(HttpServletRequest request,
			@RequestParam String orderSource, @RequestParam String shopId,
			@RequestParam String pageNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ThirdOrder> list = null;
		try {
			ThirdLogisOrderQuery query = new ThirdLogisOrderQuery();
			query.setPageSize(Constants.PAGE.SIZE);
			query.setPageIndex(Integer.parseInt(pageNum));
			if ("1".equals(orderSource)) {
				query.setMall_code(shopId);
				query.setPay_status(1);
				query.setCre_date(new Date());
				int[] statusList = new int[] {3,6,8,9};
				query.setOrderStatusList(statusList);
				list = thirdOrderService.selectLinJOrderlist(query);
			} else if ("2".equals(orderSource)) {
				query.setApp_poi_code(shopId);
				query.setMtPatyStatus(2);
				query.setMtOrderStatus(9);
				list = thirdOrderService.selectMtOrderlist(query);
			} else if ("3".equals(orderSource)) {
				query.setShop_id(shopId);
				query.setBdPatyStatus(2);
				query.setBdOrderStatus(10);
				list = thirdOrderService.selectBdOrderlist(query);
			}  else if ("4".equals(orderSource)) {
				query.setProduceStationNoIsv(shopId);
				query.setJdOrderStatus(20020);//实物类：20010:锁定，20020:订单取消，20040:超时未支付系统取消，31000:等待付款，31020:已付款，41000:待处理，32000:等待出库，33040:配送中，33060:已妥投 34000:京东已收款，90000:订单完成
				list = thirdOrderService.selectJdOrderlist(query);
			} else if ("5".equals(orderSource)) {
				query.setShop_id(shopId);
				query.setElemeOrderStatus(8);
				list = thirdOrderService.selectElemeSumlist(query);
			} else {
				list = new ArrayList<ThirdOrder>();
			}
			Util.writeResultData(map, list);
			Util.writeOk(map);
			Util.writeSuccess(map);
		} catch (Exception e) {
			e.printStackTrace();
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}

	/**
	 * 统计订单数据(已完成已失效进行中订单) pay_status=1 and order_group_status=5
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/sumOrderData", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object sumOrderData(HttpServletRequest request,
			@RequestParam String mall_code, @RequestParam String app_poi_code,
			@RequestParam String shopId, String produceStationNoIsv) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> datamap = new HashMap<String, Object>();
		try {
			// 统计邻家订单数据
			SumOrderQuery ljQuery = new SumOrderQuery();
			ljQuery.setMall_code(mall_code);
			ljQuery.setPay_status(1);
			int[] ljtatusingList = new int[] {2,4,10};
			int[] ljtatUnList = new int[] {3,6,8,9};
			int[] mtstatusList = new int[] {4,6};
			int[] bdstatusingList = new int[] {5,7,8};
			ljQuery.setStatusingList(ljtatusingList);
			ljQuery.setStatused(5);
			ljQuery.setStatusUnList(ljtatUnList);
			List<SumOrder> ljSumList = thirdOrderService.selectSumLinjia(ljQuery);
			// 统计美团数据
			SumOrderQuery mtQuery = new SumOrderQuery();
			mtQuery.setApp_poi_code(app_poi_code);
			mtQuery.setPay_status(2);
			mtQuery.setStatusingList(mtstatusList);
			mtQuery.setStatused(8);
			mtQuery.setStatusUn(9);
			List<SumOrder> mtSumList = thirdOrderService.selectSumMeiTuan(mtQuery);
			// 统计百度数据
			SumOrderQuery bdQuery = new SumOrderQuery();
			bdQuery.setShop_id(shopId);
			bdQuery.setPay_status(2);
			bdQuery.setStatusingList(bdstatusingList);
			bdQuery.setStatused(9);
			bdQuery.setStatusUn(10);
			List<SumOrder> bdSumList = thirdOrderService.selectSumBaidu(bdQuery);
			//统计京东到家数据
			SumOrderQuery jdQuery = new SumOrderQuery();
			jdQuery.setProduceStationNoIsv(produceStationNoIsv);
			jdQuery.setStatusingList(new int[]{20010,41000,32000,33040});
			jdQuery.setStatusedList(new int[]{33060,90000});
			jdQuery.setStatusUn(20020);
			List<SumOrder> jdSumList = thirdOrderService.selectSumJingdong(jdQuery);
			if (ljSumList != null) {
				datamap.put("ljSumData", ljSumList);
			}
			if (mtSumList != null) {
				datamap.put("mtSumData", mtSumList);
			}
			if (bdSumList != null) {
				datamap.put("bdSumData", bdSumList);
			}
			if (jdSumList != null) {
				datamap.put("jdSumList", jdSumList);
			}
			Util.writeResultData(map, datamap);
			Util.writeOk(map);
			Util.writeSuccess(map);
		} catch (Exception e) {
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}

	/**
	 * 统计所有下单订单数据 pay_status=1 and order_group_status=5
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/sumAllOrderData", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object sumAllOrderData(HttpServletRequest request,
			@RequestParam String mall_code, @RequestParam String app_poi_code,
			@RequestParam String shopId, String produceStationNoIsv) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 按月统计每天所有订单数据
			ThirdLogisOrderQuery query = new ThirdLogisOrderQuery();
			query.setMall_code(mall_code);
			query.setApp_poi_code(app_poi_code);
			query.setShop_id(shopId);
			query.setProduceStationNoIsv(produceStationNoIsv);
			List<SumOrder> sumOrderList = thirdOrderService.selectSumAll(query);
			Util.writeResultData(map, sumOrderList);
			Util.writeOk(map);
			Util.writeSuccess(map);
		} catch (Exception e) {
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}

	/**
	 * 统计当天所有完成订单数据 pay_status=1 and order_group_status=5
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/sumCurDayOrder", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object sumCurDayOrder(HttpServletRequest request,
			@RequestParam String mall_code, @RequestParam String app_poi_code,
			@RequestParam String shopId, String produceStationNoIsv) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 统计当天完成的有效订单
			ThirdLogisOrderQuery curDay = new ThirdLogisOrderQuery();
			curDay.setMall_code(mall_code);
			curDay.setApp_poi_code(app_poi_code);
			curDay.setShop_id(shopId);
			curDay.setProduceStationNoIsv(produceStationNoIsv);
			curDay.setPay_status(1);
			curDay.setBdPatyStatus(2);
			curDay.setMtPatyStatus(2);
			curDay.setStatus(5);
			curDay.setMtOrderStatus(8);
			curDay.setBdOrderStatus(9);
			curDay.setJdOrderStatusList(new int[]{33060,90000});
			SumOrder sumOrder = thirdOrderService.sumCurDayData(curDay);
			Util.writeResultData(map, sumOrder);
			Util.writeOk(map);
			Util.writeSuccess(map);
		} catch (Exception e) {
			e.printStackTrace();
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}

	/**
	 * 统计所有成功订单数据 pay_status=1 and order_group_status=5
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/sumAllOkData", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object sumAllOkData(HttpServletRequest request,
			@RequestParam String mall_code, @RequestParam String app_poi_code,
			@RequestParam String shopId, String produceStationNoIsv) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 统计邻家订单数据
			ThirdLogisOrderQuery query = new ThirdLogisOrderQuery();
			query.setMall_code(mall_code);
			query.setStatus(5);
			query.setPay_status(1);
			query.setApp_poi_code(app_poi_code);
			query.setMtOrderStatus(8);
			query.setMtPatyStatus(2);
			query.setShop_id(shopId);
			query.setBdOrderStatus(9);
			query.setBdPatyStatus(2);
			query.setProduceStationNoIsv(produceStationNoIsv);
			query.setJdOrderStatusList(new int[]{33060,90000});
			List<SumOrder> sumOrderList = thirdOrderService.selectSumAll(query);
			Util.writeResultData(map, sumOrderList);
			Util.writeOk(map);
			Util.writeSuccess(map);
		} catch (Exception e) {
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}

	// 添加邻家订单到达达(字段适配)
	@RequestMapping(value = "/addDaDaOrder", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object addLjOrder(HttpServletRequest request,@RequestParam String orderId) {
		JSONObject jsonObj = null;
		try {
			// 测试使用
			DaDaLogisticsParam logpm = DadaTestData.getDadaData();
			// 产生订单id
			Random rand = new Random();
			int randNum = rand.nextInt(89999) + 10000;
			logpm.setOrigin_id(randNum + "");
			logpm.setSupplier_lng(517128.4f);
			logpm.setDeliver_fee(3.5f);
			logpm.setCity_name("北京");
			logpm.setSupplier_tel("0107856321");
			logpm.setReceiver_address("北京地铁6号线车公庄");
			logpm.setExpected_finish_time(0);
			logpm.setIs_prepay(1);
			logpm.setReceiver_phone("13654897512");
			logpm.setInfo("忌辛辣");
			logpm.setPay_for_supplier_fee(1.5f);
			logpm.setExpected_fetch_time(0);

			logpm.setCargo_num(2);
			logpm.setSupplier_address("邻里家车公庄店");
			logpm.setReceiver_tel("0105879426");
			logpm.setReceiver_lng(517128.4f);
			logpm.setFetch_from_receiver_fee(2.5f);
			logpm.setSupplier_id("kf1001");
			logpm.setInvoice_title("个人");
			logpm.setSupplier_name("邻里家小王");
			logpm.setCargo_type(9);
			logpm.setCargo_weight(1.5f);
			logpm.setSupplier_phone("18677753216");
			logpm.setReceiver_name("张三丰");
			logpm.setReceiver_lat(4075103.8f);
			logpm.setSupplier_lat(4075103.8f);
			logpm.setCallback("##");
			logpm.setCreate_time((int) (System.currentTimeMillis() / 1000));
			logpm.setCargo_price(10.5f);
			jsonObj = logisticsOrderService.addOrder(logpm);
			boolean flag = logisticsAccessTokenService.initDadaCode(jsonObj);
			if (!flag) {
				jsonObj = null;
				jsonObj = logisticsOrderService.addOrder(logpm);
			}
		} catch (Exception e) {
			LOG.error("添加订单出错", e.getMessage());
		}
		return jsonObj.toString();
	}

	/**
	 * 订单搜索 type=1表示订单号;type=2表示手机号
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/orderQuery", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object orderQuery(HttpServletRequest request,
			@RequestParam String mall_code, @RequestParam String shopId,
			@RequestParam String app_poi_code, String produceStationNoIsv, @RequestParam int type,
			@RequestParam String typeValue, @RequestParam int pageNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ThirdLogisOrderQuery query = new ThirdLogisOrderQuery();
			query.setMall_code(mall_code);
			query.setShop_id(shopId);
			query.setApp_poi_code(app_poi_code);
			query.setProduceStationNoIsv(produceStationNoIsv);
			query.setPay_status(1);
			query.setBdPatyStatus(2);
			query.setMtPatyStatus(2);
//			int[] statusList = new int[] {2,3,4,5,6,7};
			int[] BdOrderStatusList = new int[] {5,7,8,9,10};
			int[] MtOrderStatusList = new int[] {4,6,8,9};
			int[] JdOrderStatusList = new int[] {20010,20020,31020,41000,32000,33040,33060,90000}; //订单状态（实物类：20010:锁定，20020:订单取消，20040:超时未支付系统取消，31000:等待付款，31020:已付款，41000:待处理，32000:等待出库，33040:配送中，33060:已妥投 34000:京东已收款，90000:订单完成；
//			query.setOrderStatusList(statusList);
			query.setLogStatus(1);
			query.setQuerySearceDate(1);
			query.setStatus(null);
			query.setBdOrderStatusList(BdOrderStatusList);
			query.setMtOrderStatusList(MtOrderStatusList);
			query.setJdOrderStatusList(JdOrderStatusList);
			if (type == 1) {
				if (!"".equals(typeValue)) {
					query.setOrder_id(Long.decode(typeValue));
				}
			} else if (type == 2) {
				query.setReceive_phone(typeValue);
			}
			query.setPageSize(Constants.PAGE.SIZE);
			query.setPageIndex(pageNum);
			List<ThirdOrder> list = thirdOrderService
					.selectWarnOrderlist(query);
			Util.writeResultData(map, list);
			Util.writeOk(map);
			Util.writeSuccess(map);
		} catch (Exception e) {
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}

	/**
	 * 完成商城自配送订单
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/finishLJOrder", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object finishLJOrder(HttpServletRequest request,
			@RequestParam String mall_code, @RequestParam Long orderId,
			@RequestParam String orderSource) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> statusMap = new HashMap<String, Object>();
		try {
			//查询订单状态
			OrderGroup og=orderGroupService.selectLogisOrderInfo(orderId);
			if(og==null){
				Util.writeError(map);
				map.put("message", "未查询到订单信息！");
				return map;
			}
			Integer ostatus=og.getOrderGroupStatus();
			if(ostatus==3||ostatus==6||ostatus==8||ostatus==9){
				Util.writeError(map);
				map.put("message", "订单已取消！");
				return map;
			}
			statusMap.put("orderGroupStatus", 5);
			statusMap.put("groupId", orderId);
			statusMap.put("orderSuccessTime", new Date());
			statusMap.put("mallCode", mall_code);
			boolean flag = orderGroupService.updateStatusById(statusMap,"1");
			if (flag) {
				map.put("message", "更新商城订单成功！");
				Util.writeOk(map);
				Util.writeSuccess(map);
			} else {
				Util.writeError(map);
				map.put("message", "更新订单表失败！");
			}
		} catch (Exception e) {
			Util.writeError(map);
			Util.writeFail(map);
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 发送物流
	 * @param request
	 * @param mall_code
	 * @param orderId
	 * @param orderSource (1,邻家；2，美团；3，百度)
	 * @param logisType (1,邻家；2，达达；3，百度)
	 * @return
	 */
	@RequestMapping(value = "/sendLogis", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object sendLogis(HttpServletRequest request,
			@RequestParam String mall_code, @RequestParam Long orderId,
			@RequestParam String orderSource, @RequestParam String logisType,@RequestParam String bd_wl_shop_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 查询订单详情
		OrderGroup og = null;
		List<OrderGroupProduct> ogList = null;
		MtReOrder mt = null;
		List<OrderFoodDetailParam> mtList = null;
		BaiduReOrder bd = null;
		List<Product> bdList = null;
		// 物流类
		PostData postData = null;
		DaDaLogisticsParam logpm = null;
		ElemeCreateRequestData ecrd=null;
		JSONObject json = null;
		boolean logisflag=false;
		try {
			if ("1".equals(orderSource)) {
				og = orderGroupService.selectLogisOrderInfo(orderId);
				Integer stype=og.getSendType();
				if(stype==null||stype==1){
					map.put("status", "fail");
					map.put("message", "上门自提单,请勿发送物流!");
					return map;
				}
				if (og != null) {
					ogList = orderGroupProductService.selectProductListByGroupId(orderId);
					if ("1".equals(logisType)) {
//						Integer distance=mapService.getSendDistanc(mall_code, og.getLatitude().toString(), og.getLongitude().toString());
						Logistics dto = new Logistics();
						dto.setOrder_id(orderId + "");
						dto.setOuter_id(orderId + "");
						dto.setLogis_type(1);
						dto.setOrder_status(3);
						dto.setOrder_source("1");
						logisticsService.insertLogistics(dto);
						logisflag=true;
						paramMap.put("send_logistics_type", 1);
					} else if ("2".equals(logisType)) {
						logpm = OrderToLogisOrder.ljOrderToDaDaLogis(og, ogList);
					} else if ("3".equals(logisType)) {
						postData = OrderToLogisOrder.ljOrderToBdLogis(og, ogList,mall_code);
					}else if ("4".equals(logisType)) {
						ecrd = OrderToLogisOrder.ljOrderToEleme(og, ogList);
					}
				}else{
					map.put("status", "fail");
					map.put("message", "未查询到订单信息!");
					return map;
				}
			} else if ("2".equals(orderSource)) {
				mt = mtLocalOrderService.selectMtOrderAll(orderId);
				if (mt != null) {
					mtList = mtOrderProductService.selectMtOrderProductAll(orderId);
					if ("1".equals(logisType)) {
						// 修改本地美团订单状态
						MtOrder mtl = new MtOrder();
						mtl.setOrder_id(orderId + "");
						mtl.setApp_poi_code(mall_code);
						mtl.setStatus("6");
						JSONObject rejson = mtLocalOrderService.updateOrderConfirmed(mtl);
						String status = rejson.getString("data");
						if ("ok".equals(status)) {
							// 推送美团订单信息到美团
							
							// 插入物流表信息
							Logistics dto = new Logistics();
							dto.setOrder_id(orderId + "");
							dto.setOuter_id(orderId + "");
							dto.setLogis_type(1);
							dto.setOrder_status(3);
							dto.setOrder_source("2");
							int b = logisticsService.insertLogistics(dto);
							if (b > 0) {
								map.put("status", "ok");
								map.put("message", "发送物流成功");
							} else {
								map.put("status", "fail");
								map.put("message", "发送物流失败!");
							}
//							System.out.println("()()()()()()()()(())()()()01"
//									+ JSONUtil.ObjToJSON(dto).toString());
							return map;
						}
					} else if ("2".equals(logisType)) {
						logpm = OrderToLogisOrder.mtOrderToDaDaLogis(mt, mtList);
					} else if ("3".equals(logisType)) {
						postData = OrderToLogisOrder.mtOrderToBdLogis(mt, mtList,
								mall_code);
					}
				}else{
					map.put("status", "fail");
					map.put("message", "未查询到订单信息!");
					return map;
				}
			} else if ("3".equals(orderSource)) {
				bd = baiduOrderService.selectBdOrderAll(orderId, mall_code);
				if (bd != null) {
					bdList = bdOrderProductService
							.selectBdOrderProductAll(orderId);
					if ("1".equals(logisType)) {
						// -------------去掉本地订单操作，订单状态修改交给美团回调修改---------
						BaiduData bdl = new BaiduData();
						BdShop shop = new BdShop();
						BdUser user = new BdUser();
						user.setCoord(new Coord());
						Order order = new Order();
						order.setStatus(5);
						order.setOrder_id(Long.toString(orderId));
						bdl.setOrder(order);
						bdl.setUser(user);
						bdl.setShop(shop);
						JSONObject rejson = baiduOrderService
								.updateBdOrder(bdl);
						String status = rejson.getString("status");
						if ("ok".equals(status)) {
							// 推送百度订单信息到百度

							// 插入物流表信息
							Logistics dto = new Logistics();
							dto.setOrder_id(orderId + "");
							dto.setOuter_id(orderId + "");
							dto.setLogis_type(1);
							dto.setOrder_source("3");
							dto.setOrder_status(3);
							int b = logisticsService.insertLogistics(dto);
							if (b > 0) {
								map.put("status", "ok");
								map.put("message", "发送物流成功");
							} else {
								map.put("status", "fail");
								map.put("message", "发送物流失败!");
							}
//							System.out.println("()()()()()()()()(())()()()01"
//									+ JSONUtil.ObjToJSON(dto).toString());
							return map;
						}
					} else if ("2".equals(logisType)) {
						logpm = OrderToLogisOrder
								.bdOrderToDaDaLogis(bd, bdList);
					} else if ("3".equals(logisType)) {
						try {
							postData = OrderToLogisOrder.bdOrderToBdLogis(bd,bdList, mall_code);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}else{
					map.put("status", "fail");
					map.put("message", "未查询到订单信息!");
					return map;
				}
			}

			// 发送物流
			if (postData != null) {
				if(bd_wl_shop_id!=null&&!(bd_wl_shop_id.isEmpty())){
					postData.setWl_shop_id(bd_wl_shop_id);
				}else{
					map.put("stauts", "fail");
					map.put("message", "百度物流店铺id为空");
					return map;
				}
				json = bdLogisService.createOrder(postData);
				if(json!=null&&!(json.toString().isEmpty())){
					// 添加物流信息
					Logistics dto = (Logistics) JSONUtil.JSONToObj(json.toString(),Logistics.class);
					if (dto != null && "success".equals(dto.getMessage())) {
						dto.setLogis_type(3);
						dto.setOrder_source(orderSource);
						logisticsService.insertLogistics(dto);
						logisflag=true;
						map.put("status", "ok");
						map.put("message", "发送百度物流成功");
						paramMap.put("send_logistics_type", 3);
					} else {
						map.put("status", "fail");
						map.put("message", "发送百度物流失败,错误信息:"+dto.getMessage());
					}
				}else{
					map.put("status", "fail");
					map.put("message", "发送百度物流失败");
				}
			}
			if (logpm != null) {
				Integer ogstatus=og.getOrderGroupStatus();
				if(ogstatus!=null&&ogstatus==10){
					//重新发物流
					json = logisticsOrderService.reAddOrder(logpm);
					boolean flag = logisticsAccessTokenService.initDadaCode(json);
					if (!flag) {
						json = null;
						json = logisticsOrderService.reAddOrder(logpm);
					}
				}else{
					json = logisticsOrderService.addOrder(logpm);
					boolean flag = logisticsAccessTokenService.initDadaCode(json);
					if (!flag) {
						json = null;
						json = logisticsOrderService.addOrder(logpm);
					}
				}
				System.out.println("()()()()()()()()(())()()()21"+ json.toString());
				if(json!=null&&!(json.toString().isEmpty())){
					// 添加物流信息
					Logistics dto = (Logistics) JSONUtil.JSONToObj(json.toString(),Logistics.class);
					if (dto!=null&&"ok".equals(dto.getStatus())) {
						logisflag=true;
						dto.setLogis_type(2);
						dto.setOrder_source(orderSource);
						dto.setOrder_id(logpm.getOrigin_id());
						logisticsService.insertLogistics(dto);
//						System.out.println("()()()()()()()()(())()()()22"+ JSONUtil.ObjToJSON(dto).toString());
						map.put("status", "ok");
						map.put("message","发送达达物流成功");
						paramMap.put("send_logistics_type", 2);
					} else {
						map.put("status", "fail");
						map.put("message", dto.getErrorMessage());
					}
				}else{
					map.put("status", "fail");
					map.put("message","发送达达物流失败!");
				}
			}
			// 发送物流
			if (ecrd != null) {
				ElemeResData redata=beaService.beaSend(ecrd);
				if(redata!=null&&"200".equals(redata.getCode())){
					// 添加物流信息
					Logistics dto = new Logistics();
						dto.setLogis_type(4);
						dto.setOrder_source(orderSource);
						dto.setCreate_time(new Date());
						dto.setMall_code(mall_code);
						dto.setOrder_id(orderId+"");
						dto.setOrder_status(1);
						dto.setRecieve_time(new Date().getTime());
						dto.setStatus("1");
						dto.setUpdate_time(new Date().getTime());
						int ls=logisticsService.insertLogistics(dto);
					if (ls>0) {
						logisflag=true;
						map.put("status", "ok");
						map.put("message", "饿了么物流成功!");
						paramMap.put("send_logistics_type", 4);
					} else {
						map.put("status", "fail");
						map.put("message", "更新本地物流信息失败!");
					}
			}else{
				map.put("status", "fail");
				map.put("message", "发送饿了么物流失败,错误信息:"+redata.getMsg());
			}
		}
			//修改本地订单状态
			if(logisflag){
				paramMap.put("orderGroupStatus", 4);
				paramMap.put("groupId", orderId);
				paramMap.put("cancelReason","");
				boolean b = orderGroupService.updateStatusById(paramMap,"1");
				if (b) {
					map.put("status", "ok");
					map.put("message", "发送物流成功");
				} else {
					map.put("status", "fail");
					map.put("message", "发送物流失败!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "fail");
			map.put("message", "物流添加异常");
		}
		return map;
	}
	
	/**
	 *查询百度物流单号
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getBdOrderId", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getBdOrderId(HttpServletRequest request,@RequestParam Long orderId){
		Map<String, Object> map = new HashMap<String, Object>();
		LogisticsQuery log=new LogisticsQuery();
		log.setOrder_id(orderId);
		Logistics lg=logisticsService.selectLogisticsById(log);
		if(lg!=null){
			map.put("百度订单号为:", lg.getOuter_id());
		}else{
			map.put("message", "对不起，没有查到该订单的百度物流单号");
		}
		return map;
	}
	
	/**
	 *查询自配送距离
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getSendDistance", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getSendDistance(HttpServletRequest request,@RequestParam String mallCode,@RequestParam String lat,@RequestParam String lon){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			Integer distance=mapService.getSendDistanc(mallCode, lat, lon);
			if(distance==null){
				map.put("status", "fail");
				map.put("message", "查询配送距离失败!");
				return map;
			}else{
				map.put("status", "ok");
				map.put("distance",distance);
				return map;
			}
		}catch(Exception e){
			map.put("status", "fail");
			map.put("message", "查询配送距离异常!");
			return map;
		}
	}
}
