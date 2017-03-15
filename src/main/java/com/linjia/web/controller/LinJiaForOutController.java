package com.linjia.web.controller;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONArray;
import com.linjia.base.controller.BaseController;
import com.linjia.tools.JSONUtil;
import com.linjia.tools.OrderToLogisOrder;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.model.Logistics;
import com.linjia.web.model.OrderGroup;
import com.linjia.web.model.OrderGroupProduct;
import com.linjia.web.model.PaoDanRecord;
import com.linjia.web.model.PushBody;
import com.linjia.web.model.PushParam;
import com.linjia.web.service.BdOrderProductService;
import com.linjia.web.service.ElemeOrderService;
import com.linjia.web.service.LogisticsService;
import com.linjia.web.service.MtOrderProductService;
import com.linjia.web.service.OrderGroupService;
import com.linjia.web.service.PaoDanRecordService;
import com.linjia.web.service.ThirdOrderService;
import com.linjia.web.thirdService.JGpush.service.JgPushDeviceService;
import com.linjia.web.thirdService.baidu.model.BaiduData;
import com.linjia.web.thirdService.baidu.model.BaiduReOrder;
import com.linjia.web.thirdService.baidu.model.BdRecivedOrder;
import com.linjia.web.thirdService.baidu.service.BaiduOrderService;
import com.linjia.web.thirdService.bdlogistics.post.ReciveFeachOrder;
import com.linjia.web.thirdService.bdlogistics.post.ReciveFinishOrder;
import com.linjia.web.thirdService.bdlogistics.post.ReciveSendOrder;
import com.linjia.web.thirdService.bdlogistics.service.BdLogisService;
import com.linjia.web.thirdService.bea.config.ElemeOpenConfig;
import com.linjia.web.thirdService.bea.config.RequestConstant;
import com.linjia.web.thirdService.bea.model.ElemeCallEntity;
import com.linjia.web.thirdService.bea.model.ElemeStatusCallEntity;
import com.linjia.web.thirdService.bea.service.BeaService;
import com.linjia.web.thirdService.dada.model.DaDaLogisticsCallbackDto;
import com.linjia.web.thirdService.eleme.interfaces.entity.message.CallBackMessage;
import com.linjia.web.thirdService.eleme.interfaces.entity.message.OrderStatusMessage;
import com.linjia.web.thirdService.eleme.interfaces.entity.order.OOrder;
import com.linjia.web.thirdService.eleme.interfaces.enumeration.order.OOrderRefundStatus;
import com.linjia.web.thirdService.eleme.interfaces.enumeration.order.OOrderStatus;
import com.linjia.web.thirdService.meituan.model.CancelOrder;
import com.linjia.web.thirdService.meituan.model.MtOrder;
import com.linjia.web.thirdService.meituan.model.MtReOrder;
import com.linjia.web.thirdService.meituan.service.MtOrderService;
import com.linjia.web.thirdService.meituan.vo.OrderExtraParam;
import com.linjia.web.thirdService.meituan.vo.OrderFoodDetailParam;
import com.linjia.web.uhd123.common.Configure;
import com.linjia.web.uhd123.service.UhdOrderService;

/**
 *对外提供的接口类
 * 
 * @author xiangsy
 *
 */
@Controller
@RequestMapping("/forOut")
public class LinJiaForOutController extends BaseController{
	private static Logger LOG = LoggerFactory.getLogger(LinJiaForOutController.class);
	
	@Autowired
	private BaiduOrderService baiduOrderService;
	@Autowired
	private MtOrderService mtOrderService;
	@Autowired
	private BdLogisService bdLogisService;
	@Autowired
	private JgPushDeviceService jgPushDeviceService;
	@Autowired
	private LogisticsService logisticsService;
	@Autowired
	private ThirdOrderService thirdOrderService;
	@Autowired
	private OrderGroupService orderGroupService;
	@Autowired
	private UhdOrderService uhdOrderService;
	@Autowired
	private MtOrderService mtLocalOrderService;// 美团本地表操作
	@Autowired
	private MtOrderProductService mtOrderProductService;// 美团本地库订单商品
	@Autowired
	private PaoDanRecordService paoDanRecordService;
	@Autowired
	private BdOrderProductService bdOrderProductService;// 百度本地库订单商品
	@Autowired
	private BeaService beaService;// 饿了么订单
	@Autowired
	private ElemeOrderService elemeOrderService;
	/**
	 * 达达订单回调接口
	 * @param request
	 * @param lcbd
	 * @return
	 */
	@RequestMapping(value="/dadaPushData",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object dadaPushData(HttpServletRequest request,DaDaLogisticsCallbackDto lcbd){
		JSONObject json=new JSONObject();
		Map<String,Object> updmap=new HashMap<String, Object>();
		try{
			//更新订单状态
			System.out.println("***************Signature:"+lcbd.getSignature());
			System.out.println("#####"+lcbd.getDm_id());
			String rejson=JSONUtil.ObjToJSON(lcbd).toString();
//			System.out.println("回调信息："+rejson);
			json.put("status", "ok");
			Logistics dto=(Logistics) JSONUtil.JSONToObj(rejson, Logistics.class);
			dto.setIfDeleted(0);
			//更新物流信息
			logisticsService.updateLogistics(dto);
			//订单状态(0待发布 1待接单 2待取货 3执行中 4已完成 5已取消
			//7已过期-超过期望取货时间2小时未接单，自动变为已过期）
			int b=0;
			Integer status=null;
			int res=dto.getOrder_status();
			String oid=dto.getOrder_id();
			if(!oid.isEmpty()){
				Long orderId=Long.valueOf(oid);
				if(res==2){
					updmap.put("recieve_time", lcbd.getUpdate_time());
					updmap.put("knightPickupTime", new Date());
					status=2;
				}
				if(res==3){
					updmap.put("send_time", lcbd.getUpdate_time());
					status=4;
				}
				if(res==4){
					updmap.put("groupId",orderId);
					updmap.put("orderGroupStatus",5);
					updmap.put("orderSuccessTime", new Date());
					//完成订单改状态与抛单
					orderGroupService.updateStatusById(updmap,"1"); 
					return lcbd.toString();
				}
				if(res==2||res==3){
					updmap.put("groupId",orderId);
					updmap.put("orderGroupStatus",status);
					b=thirdOrderService.updateLinjOrderStatus(updmap);
					return lcbd.toString();
				}
				//订单取消
				if(res==5||res==7){
					status=10;
					updmap.put("groupId",orderId);
					updmap.put("orderGroupStatus",status);
//					updmap.put("knightPickupTime", "NULL");
					updmap.put("cancelReason", "达达物流取消订单");
					b=thirdOrderService.updateLinjOrderStatus(updmap);
					if(b>0){
						Logistics newdto=(Logistics) JSONUtil.JSONToObj(rejson, Logistics.class);
						if(newdto!=null&&!(newdto.getCancel_reason().isEmpty())){
							newdto.setCancel_reason("物流取消");
							newdto.setUpdate_time(new Date().getTime());
							newdto.setOrder_status(10);
							newdto.setDeleted(1);
						}
						//更新物流信息
						logisticsService.updateLogistics(newdto);
						//推送消息
						jgPushDeviceService.pushMessageByOrderId(orderId, "1","1");
//						退款及抛单操作
//						orderGroupService.cancelOrder(orderId, false, "logistics");
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			json.put("status", "fail");
			LOG.error("解析异常",e.getMessage());
			return lcbd.toString();
		}
		return lcbd.toString();
	}
	
	/**
	 * 百度添加订单(下行接口)邻家提供
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addBdOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String createOrder(HttpServletRequest request){
		JSONObject json=null;
		try{
			BdRecivedOrder recivebd =null;
			String data = IOUtils.toString(request.getInputStream(),"UTF-8");
			recivebd = JSONUtil.fromJson(data, BdRecivedOrder.class);
			if(recivebd.body!=null){
				System.out.println("****************Source:"+recivebd.getSource());
	//			BaiduData bdData=BdOrderTestData.getBaiduData();
				BaiduData bdOrder=recivebd.getBody();
		        json=baiduOrderService.createOrder(bdOrder, "");
				//客户端消息推送
		        String oid=bdOrder.getOrder().getOrder_id();
		        if("success".equals(json.getJSONObject("body").getString("error"))){
		        	if(!oid.isEmpty()){
			        	Long orderId=Long.valueOf(oid);
			        	jgPushDeviceService.pushMessageByOrderId(orderId,"1","3");
		        	}
		        }
			}
		}catch(Exception e){
			json=new JSONObject();
			return json.toString();
		}
		return json.toString();
	}
	
	/**
	 * 百度查看订单状态(下行接口)邻家提供
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/bdOrderStatusQuery",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String orderStatusQuery(HttpServletRequest request,@RequestParam Long orderId){
		JSONObject json=null;
		try{
			json=baiduOrderService.orderStatusQuery(orderId,"");
		}catch(Exception e){
			json=new JSONObject();
			return json.toString();
		}
		return json.toString();
	}

    /**
	 * 百度订单状态推送(下行接口)邻家提供
	 * @param orderId
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/bdOrderStatusPush",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String orderStatusPush(HttpServletRequest request,@RequestBody PushParam pushParam){
		JSONObject json=null;
		try{
			PushBody pb=pushParam.getBody();
			Long orderId=Long.valueOf(pb.getOrder_id());
//			System.out.println("￥￥￥￥百度推送￥￥￥￥"+JSONUtil.ObjToJSON(pushParam).toString());
			int status=pb.getStatus();
			boolean flag=baiduOrderService.updateOrderStatus(orderId,pb.getStatus(),"");
			//目前推送的状态有： 1、骑士已接单开始取餐（此时可通过订单详情接口获取骑士手机号） 2、骑士已取餐正在配送 3、订单完成 4、订单取消,此接口需要联系运营配置才生效，推送地址和创建订单地址相同。
			if(flag&&status==3){
				//完成抛单
				boolean uflag=uhdOrderService.updateOrderDeliverToUhd(pb.getOrder_id(), new Date(),"linjia", "signed", "handover", "handover");
				if(!uflag){
					//查询抛单记录
					Map<String,Object> pdmap=new HashMap<String, Object>();
					pdmap.put("order_id", orderId);
					pdmap.put("order_type",3);
					pdmap.put("paodan_type",3);
					PaoDanRecord selPd=paoDanRecordService.selectPaoDan(pdmap);
					if(selPd==null){
						//记录抛单数据
						PaoDanRecord pd = new PaoDanRecord(orderId,3,1,3,new Date(),null,0);
						boolean pflag=paoDanRecordService.insertPaoDan(pd);
						if(!pflag){
							LOG.error("info:", "抛单记录失败！");
							LOG.error("订单id:", orderId+"	订单类型为2:百度订单");
						}
					}
				
				}
			}
			if(flag&&status==4){
				//取消抛单
				BaiduReOrder bdorder=baiduOrderService.selectPdOrder(orderId);
				List<com.linjia.web.thirdService.baidu.model.Product> bdProlist=bdOrderProductService.selectBdOrderProductAll(orderId);
				OrderGroup og = OrderToLogisOrder.bdOrderToLjOrder(bdorder, bdProlist);
				if (og != null) {
					List<OrderGroupProduct> ogList = og.getProductList();
					//抛单
					boolean uflag=uhdOrderService.returnserviceToUhd(Configure.shop_id_baiduwaimai, og, ogList, og.getUserId(), String.valueOf(orderId));
					if(!uflag){
						//查询抛单记录
						Map<String,Object> pdmap=new HashMap<String, Object>();
						pdmap.put("order_id", orderId);
						pdmap.put("order_type", 3);
						pdmap.put("paodan_type", 2);
						PaoDanRecord selPd=paoDanRecordService.selectPaoDan(pdmap);
						if(selPd==null){
							//记录抛单数据
							PaoDanRecord pd = new PaoDanRecord(orderId,3,1,2,new Date(),null,0);
							boolean pflag=paoDanRecordService.insertPaoDan(pd);
							if(!pflag){
								LOG.error("info:", "抛单记录失败！");
								LOG.error("订单id:", orderId+"	订单类型为3:百度订单");
							}
						}
					}
				}
			}
		}catch(Exception e){
			json=new JSONObject();
			return json.toString();
		}
		return json.toString();
	}

    //一下三种状态是否把已支付订单推送接口改为本地创建订单接口，并将支付状态改为已支付和订单状态改为待确认。。。。。。
	/**
	 * 美团取消订单下行接口，邻家提供给美团(已对接)
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping(value="/cancelMtOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody 
	public String cancelMtOrder(HttpServletRequest request,CancelOrder cancelOrder){
		JSONObject json=new JSONObject();
		try{
//			System.out.println("%%%%%%%%"+JSONUtil.ObjToJSON(cancelOrder).toString());
			//查询订单中
			String oid=cancelOrder.getOrder_id();
			if(oid!=null){
				Long orderId=Long.valueOf(oid);
				//http://yourapp/cancelorder?app_id=1&timestamp=1300923923&order_id=1233&reason_code=1001&reason=测试&sig=234ee3232” 返回{"data":"ok"}
				MtOrder mt=new MtOrder();
				mt.setOrder_id(orderId+"");
				mt.setCancelReason(cancelOrder.getReason());
				mt.setStatus("9");
				String recode=cancelOrder.getReason_code();
				Integer code=null;
				if(recode!=null){
					code=Integer.parseInt(recode);
				}
				mt.setCancelCode(code);
				int num=mtOrderService.updateMtOrder(mt);
				json.put("data", "ok");
				//客户端消息推送
		        if(num>0){
		        	jgPushDeviceService.pushMessageByOrderId(orderId,"4","2");
		        }
		        if(num>0){
			        //抛单
			        MtReOrder mtorder=mtLocalOrderService.selectMtOrderAll(orderId);
					List<OrderFoodDetailParam> mtList=mtOrderProductService.selectMtOrderProductAll(orderId);
					OrderGroup og = OrderToLogisOrder.mtOrderToLjOrder(mtorder, mtList);
					if (og != null) {
						List<OrderGroupProduct> ogList = og.getProductList();
						//抛单
						boolean uflag=uhdOrderService.returnserviceToUhd(Configure.shop_id_meituan, og, ogList, og.getUserId(), String.valueOf(orderId));
						if(!uflag){
							//查询抛单记录
							Map<String,Object> pdmap=new HashMap<String, Object>();
							pdmap.put("order_id", orderId);
							pdmap.put("order_type",2);
							pdmap.put("paodan_type",2);
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
		        }
			}else{
				return "200";
			}
		}catch(Exception e){
			json.put("data", "fail");
			return json.toString();
		}
		return json.toString();
	}

    /**
	 * 美团完成订单下行接口，邻家提供给美团(已对接)
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping(value="/finishMtOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody 
	public String finishMtOrder(HttpServletRequest request,CancelOrder finishOrder){
		JSONObject json=new JSONObject();
		try{
			//{"sig":"54ec23be3bd5789a4f961860bc1c597f","reason_code":null,"reason":null,"app_id":"358","order_id":"2219621000","timestamp":1475205800}
//			System.out.println("********完成***"+JSONUtil.ObjToJSON(finishOrder).toString());
			//查询订单中
			String oid=finishOrder.getOrder_id();
			if(oid!=null){
				Long orderId=Long.valueOf(oid);
				MtOrder mt=new MtOrder();
				mt.setOrder_id(orderId+"");
				mt.setCancelReason(finishOrder.getReason());
				mt.setStatus("8");
				String recode=finishOrder.getReason_code();
				Integer code=null;
				if(recode!=null){
					code=Integer.parseInt(recode);
				}
				mt.setCancelCode(code);
				int num=mtOrderService.updateMtOrder(mt);
				json.put("data", "ok");
				if(num>0){
					//抛单
					boolean uflag=uhdOrderService.updateOrderDeliverToUhd(orderId+"", new Date(),"linjia", "signed", "handover", "handover");
					if(!uflag){
						//查询抛单记录
						Map<String,Object> pdmap=new HashMap<String, Object>();
						pdmap.put("order_id", orderId);
						pdmap.put("order_type",2);
						pdmap.put("paodan_type",3);
						PaoDanRecord selPd=paoDanRecordService.selectPaoDan(pdmap);
						if(selPd==null){
							//记录抛单数据
							PaoDanRecord pd = new PaoDanRecord(orderId,2,1,3,new Date(),null,0);
							boolean flag=paoDanRecordService.insertPaoDan(pd);
							if(!flag){
								LOG.error("info:", "抛单记录失败！");
								LOG.error("订单id:", orderId+"	订单类型为2:美团订单");
							}
						}
						
					}
				}
			}else{
				return "200";
			}
		}catch(Exception e){
			json.put("data", "fail");
			return json.toString();
		}
		return json.toString();
	}

    /**
	 * 美团确认订单下行接口，邻家提供给美团(已对接)
	 * xiangsy
	 * 2016年12月29日 下午11:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping(value="/confirmMtOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody 
	public String confirmMtOrder(HttpServletRequest request,CancelOrder confirmOrder){
		JSONObject json=new JSONObject();
		try{
			//{"sig":"54ec23be3bd5789a4f961860bc1c597f","reason_code":null,"reason":null,"app_id":"358","order_id":"2219621000","timestamp":1475205800}
//			System.out.println("********确认***"+JSONUtil.ObjToJSON(confirmOrder).toString());
			//查询订单中
			String oid=confirmOrder.getOrder_id();
			if(oid!=null){
				Long orderId=Long.valueOf(oid);
				//查看订单状态
				MtReOrder mtorder=mtLocalOrderService.selectMtOrderAll(orderId);
				//订单未推送，则返回
				if(mtorder==null){
					return "200";
				}
				MtOrder mt=new MtOrder();
				//当天第几单
				Integer orderNum=Util.getTodayOrderNum("M",mtorder.getMall_code());
				mt.setOrder_id(orderId+"");
				mt.setStatus("4");
				mt.setOrderNum(orderNum);
				//订单已确认，则返回
				if(mtorder.getStatus()==4){
					json.put("data", "ok");
					return json.toString();
				}
				int num=mtOrderService.updateMtOrder(mt);
				json.put("data", "ok");
				if(num>0){
					//未抛单
					if(mtorder.getUhd_order_id()==null){
						List<OrderFoodDetailParam> mtList=mtOrderProductService.selectMtOrderProductAll(orderId);
						OrderGroup og = OrderToLogisOrder.mtOrderToLjOrder(mtorder, mtList);
						if (og != null) {
							List<OrderGroupProduct> ogList = og.getProductList();
							//抛单
							boolean uflag=uhdOrderService.submitOrder(Configure.shop_id_meituan, og, ogList, og.getUserId());
							if(!uflag){
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
					}
				}
			}else{
				return "200";
			}
		}catch(Exception e){
			json.put("data", "fail");
			return json.toString();
		}
		return json.toString();
	}

    /**
	 * 美团物流状态推送接口，邻家提供给美团(已对接)
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping(value="/MtOrderLogis",produces ="application/json;charset=UTF-8")
	@ResponseBody 
	public String MtOrderLogis(HttpServletRequest request,CancelOrder finishOrder){
		JSONObject json=new JSONObject();
		try{
			//timestamp=1484818629&time=1484818629&dispatcher_name=&app_id=358&order_id=3150594992&dispatcher_mobile=&sig=c58431f5a5838fa7e0a47119c43577f8&logistics_status=0
			String data=JSONUtil.ObjToJSON(finishOrder).toString();
			System.out.println("********美团物流***"+data);
			//查询订单中
			String oid=finishOrder.getOrder_id();
			if(oid!=null){
				Long orderId=Long.valueOf(oid);
				MtOrder mt=new MtOrder();
				mt.setOrder_id(orderId+"");
				mt.setDelivery_time(finishOrder.getTime()+"");
				mt.setUtime(finishOrder.getTimestamp());
				mt.setStatus("6");
				mtOrderService.updateMtOrder(mt);
				json.put("data", "ok");
			}else{
				return "200";
			}
		}catch(Exception e){
			json.put("data", "fail");
			return json.toString();
		}
		return json.toString();
	}

    /**
	 * 美团创建订单数据下行接口，邻家提供给美团(已对接)
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping(value="/pushMtOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody 
	public String pushMtOrder(HttpServletRequest request,MtOrder mt){
//		@RequestJsonParam(value="detail") List<OrderFoodDetailParam> detail
		org.json.JSONObject json=null;
		try{
//			Util.getReqInfo(request);
			System.out.println("$$$$$$$$$$orderid:"+mt.getOrder_id());
			String oid=mt.getOrder_id();
			if(oid!=null&&oid!=""){
//				System.out.println("######"+JSONUtil.ObjToJSON(mt).toString());
				Long orderId=Long.valueOf(oid);
				//查询是否已存在
				int num=thirdOrderService.selectMtOrderNum(orderId);
				if(num>0){
					return "200";
				}
				int pt=mt.getPay_type();
				if(pt==1){
					mt.setPay_status("1");
				}else{
					//在线支付
					mt.setPay_status("2");
				}
				mt.setStatus("3");
				if(mt.getRecive_time()==null){
					mt.setRecive_time(new Date());
				}
				json=mtOrderService.insertMtOrder(mt);
				//客户端消息推送
				if("ok".equals(json.getString("data"))){
					jgPushDeviceService.pushMessageByOrderId(orderId,"1","2");
				}
			}else{
				return "200";
			}
		}catch(Exception e){
			e.printStackTrace();
			json=new JSONObject();
			json.put("status", "fail");
			json.put("data", "fail");
		}
		return json.toString();
	}

    /**
	 * 美团创建订单数据下行接口，邻家提供给美团(已对接)
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping(value="/pushMtOrderTest",produces ="application/json;charset=UTF-8")
	@ResponseBody 
	public String pushMtOrderTest(HttpServletRequest request,MtOrder mt){
		org.json.JSONObject json=null;
		try{
//			Util.getReqInfo(request);
//			if(Tools.isEmpty(mt.getDetail())||Tools.isEmpty(mt.getExtras())){
				String params = request.getQueryString();
				List<NameValuePair> urlparam=URLEncodedUtils.parse(params,Charset.forName("UTF-8"));
				 for (NameValuePair param : urlparam) {  
//			        System.out.println(param.getName() + " : " + param.getValue());  
			        if(param!=null&&"detail".equals(param.getName())){
			        	String details=param.getValue();
			        	List<OrderFoodDetailParam> detailList= JSONArray.parseArray(details,OrderFoodDetailParam.class);
			        	mt.setDetail(details);
			        	mt.setDetailList(detailList);
			        }
			        if(param!=null&&"extras".equals(param.getName())){
			        	String extras=param.getValue();
			        	mt.setExtras(extras);
			        	List<OrderExtraParam> extraList= JSONArray.parseArray(extras,OrderExtraParam.class);
			        	mt.setExtraList(extraList);
			        }
			     }  
//			}
			String oid=mt.getOrder_id();
			if(oid!=null&&oid!=""){
//				System.out.println("######"+JSONUtil.ObjToJSON(mt).toString());
				Long orderId=Long.valueOf(oid);
				//查询是否已存在
				int num=thirdOrderService.selectMtOrderNum(orderId);
				if(num>0){
					return "200";
				}
				int pt=mt.getPay_type();
				if(pt==1){
					mt.setPay_status("1");
				}else{
					//在线支付
					mt.setPay_status("2");
				}
				mt.setStatus("3");
				if(mt.getRecive_time()==null){
					mt.setRecive_time(new Date());
				}
				json=mtOrderService.insertMtOrder(mt);
				//客户端消息推送
				if("ok".equals(json.getString("data"))){
					jgPushDeviceService.pushMessageByOrderId(orderId,"1","2");
				}
			}else{
				return "200";
			}
		}catch(Exception e){
			e.printStackTrace();
			json=new JSONObject();
			json.put("status", "fail");
			json.put("data", "fail");
			return json.toString();
		}
		return json.toString();
	}

    /**
	 * 美团已支付订单数据(已完成)下行接口，邻家提供给美团(未对接，保留)
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping(value="/pushMtPayedOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody 
	public String pushMtPayedOrder(HttpServletRequest request,MtOrder mt){
		JSONObject json=null;
		try{
			if(mt.getOrder_id()!=null&&mt.getOrder_id()!=""){
				System.out.println("############Test###########"+JSONUtil.toJson(mt));
				if(mt.getRecive_time()==null){
					mt.setRecive_time(new Date());
				}
				json=mtOrderService.insertMtOrder(mt);
			}else{
				return "200";
			}
		}catch(Exception e){
			json=new JSONObject();
			return json.toString();
		}
		return json.toString();
	}

    /**
	 * 骑士接单取餐(回调接口)
	 * @param request
	 * @param order_id
	 * @param reason_id
	 * @param reason_detail
	 * @return
	 */
	@RequestMapping(value="/feachBdLOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public Object feachOrder(HttpServletRequest request,@RequestBody ReciveFeachOrder pro){
		JSONObject reslut=new JSONObject();
	try{
//		System.out.println("&&&&&&&&&"+JSONUtil.ObjToJSON(pro).toString());
		reslut.put("error_no", 0);
		reslut.put("message", "success");
		//更新物流信息
		Logistics dto=new Logistics();
		dto.setOrder_id(pro.getOut_order_id());
		dto.setOrder_status(2);
		dto.setUpdate_time(pro.getPush_time());
		dto.setRecieve_time(pro.getConfirm_time());
		dto.setDm_name(pro.getDelivery_name());
		dto.setDm_mobile(pro.getDelivery_phone());
		logisticsService.updateLogistics(dto);
		//更新邻家订单状态
		Map<String,Object> updmap=new HashMap<String, Object>();
		updmap.put("orderGroupStatus",2);
		updmap.put("groupId",pro.getOut_order_id());
		updmap.put("knightPickupTime",new Date());
		int b=thirdOrderService.updateLinjOrderStatus(updmap);
	}catch(Exception e){
		reslut.put("status", "fail");
		reslut.put("message", "物流取单出错");
		LOG.error("物流取单出错",e.getMessage());
		return reslut.toString();
	}
	return reslut.toString();
	}
	
	/**
	 * 骑士送餐(回调接口)
	 * @param request
	 * @param order_id
	 * @param reason_id
	 * @param reason_detail
	 * @return
	 */
	@RequestMapping(value="/sendBdLOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public Object sendOrder(HttpServletRequest request,@RequestBody ReciveSendOrder pso){
		JSONObject reslut=new JSONObject();
	try{
//		System.out.println("************"+JSONUtil.ObjToJSON(pso).toString());
		reslut.put("error_no", 0);
		reslut.put("message", "success");
		//更新物流信息
		Logistics dto=new Logistics();
		dto.setOrder_id(pso.getOut_order_id());
		dto.setOrder_status(3);
		dto.setUpdate_time(pso.getPush_time());
		dto.setSend_time(pso.getPickup_time());
//		dto.setDm_name(pso.getDelivery_name());
//		dto.setDm_mobile(pso.getDelivery_phone());
		logisticsService.updateLogistics(dto);
		//更新邻家订单状态
		Map<String,Object> updmap=new HashMap<String, Object>();
		updmap.put("orderGroupStatus",4);
		updmap.put("groupId",pso.getOut_order_id());
		int b=thirdOrderService.updateLinjOrderStatus(updmap);
	}catch(Exception e){
		reslut=new JSONObject();
		reslut.put("status", "fail");
		reslut.put("message", "物流取单出错");
		LOG.error("物流取单出错",e.getMessage());
		return reslut.toString();
	}
	return reslut.toString();
	}
	
	/**
	 * 骑士完成订单(回调接口)
	 * @param request
	 * @param order_id
	 * @param reason_id
	 * @param reason_detail
	 * @return
	 */
	@RequestMapping(value="/finishBdLOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public Object finishOrder(HttpServletRequest request,@RequestBody ReciveFinishOrder pfo){
		JSONObject reslut=new JSONObject();
	try{
//		System.out.println("********@@@@@@@@@@@***"+JSONUtil.ObjToJSON(pfo).toString());
		reslut.put("error_no", 0);
		reslut.put("message", "success");
		//更新物流信息
		Logistics dto=new Logistics();
		dto.setOrder_id(pfo.getOut_order_id());
		dto.setOrder_status(4);
		dto.setUpdate_time(pfo.getPush_time());
		dto.setFinish_time(pfo.getFinish_time());
//		dto.setDm_name(pfo.getDelivery_name());
//		dto.setDm_mobile(pfo.getDelivery_phone());
		logisticsService.updateLogistics(dto);
		//更新邻家订单状态
		Map<String,Object> updmap=new HashMap<String, Object>();
		updmap.put("orderGroupStatus",5);
		updmap.put("groupId",pfo.getOrder_id());
		updmap.put("orderSuccessTime",new Date());
		//int b=thirdOrderService.updateLinjOrderStatus(updmap);
		orderGroupService.updateStatusById(updmap,"1");
		
	}catch(Exception e){
		reslut=new JSONObject();
		reslut.put("status", "fail");
		reslut.put("message", "完成订单出错");
		LOG.error("完成订单出错",e.getMessage());
		return reslut.toString();
	}
	return reslut.toString();
	}
	
	/**
	 * 饿了么订单(状态回调接口)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/elemeOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public int elemeOrder(HttpServletRequest request,String data){
		try{
//			String res = URLDecoder.decode(IOUtils.toString(request.getInputStream(),"UTF-8"));
			String datastr=URLDecoder.decode(data,"UTF-8");
			System.out.println("********饿了么***"+datastr);
			ElemeStatusCallEntity esce=JSONUtil.fromJson(datastr, ElemeStatusCallEntity.class);
			if(esce!=null){
				String cancenl=esce.getError_code();
				if(cancenl!=null&&!(cancenl.isEmpty())){
					esce.setError_msg(RequestConstant.getWrongInfo(cancenl));
				}
				Integer status=esce.getOrder_status();
				Logistics dto=new Logistics();
				Map<String,Object> updmap=new HashMap<String, Object>();
				Long orderId=Long.valueOf(esce.getPartner_order_code());
				if(status==1){
//					1	系统已接单	蜂鸟配送开放平台接单后,商户接收到系统已接单状态, 支持取消
//					updmap.put("recieve_time",new Date());
//					updmap.put("orderGroupStatus",4);
//					updmap.put("groupId",orderId);
//					thirdOrderService.updateLinjOrderStatus(updmap);
					System.out.println("*********饿了么订单系统已接单**********");
				}
				if(status==20){
//					20	已分配骑手	蜂鸟配送开放平台接单后,商户接收到已分配骑手状态, 支持取消
					updmap.put("recieve_time",new Date());
					updmap.put("orderGroupStatus",4);
					updmap.put("groupId",orderId);
					thirdOrderService.updateLinjOrderStatus(updmap);
					dto.setOrder_id(orderId+"");
					dto.setStatus(esce.getOrder_status()+"");
					dto.setOrder_status(2);
					dto.setUpdate_time((new Date()).getTime());
					dto.setRecieve_time(new Date().getTime());
					dto.setDm_mobile(esce.getCarrier_driver_phone());
                    dto.setDm_name(esce.getCarrier_driver_name());
					dto.setOuter_id(esce.getOpen_order_code());
					dto.setIfDeleted(0);
					logisticsService.updateLogistics(dto);
					System.out.println("*********饿了么订单已分配骑手**********");
				}
				if(status==80){
//					80	骑手已到店	蜂鸟配送开放平台将骑手已到店状态回调给商户, 支持取消
					updmap.put("knightPickupTime", new Date());
					updmap.put("orderGroupStatus",4);
					updmap.put("groupId",orderId);
					thirdOrderService.updateLinjOrderStatus(updmap);
					System.out.println("*********饿了么订单骑手已到店**********");
				}
				if(status==2){
//					2	配送中	蜂鸟配送开放平台将骑手配送中状态回调给商户, 不支持取消
//					updmap.put("recieve_time",new Date());
//					updmap.put("orderGroupStatus",2);
//					updmap.put("groupId",orderId);
//					thirdOrderService.updateLinjOrderStatus(updmap);
					dto.setOrder_id(orderId+"");
					dto.setStatus(esce.getOrder_status()+"");
					dto.setOrder_status(3);
					dto.setUpdate_time(new Date().getTime());
					dto.setSend_time(new Date().getTime());
					dto.setIfDeleted(0);
					//更新物流信息
					logisticsService.updateLogistics(dto);
					System.out.println("*********饿了么订单配送中**********");
				}
				if(status==3){
					System.out.println("######################################3");
//					3	已送达	蜂鸟配送开放平台将已送达状态回调给商户, 不支持取消
					dto.setOrder_id(orderId+"");
					dto.setStatus(esce.getOrder_status()+"");
					dto.setOrder_status(4);
					dto.setUpdate_time((new Date()).getTime());
					dto.setFinish_time(esce.getPush_time());
					dto.setIfDeleted(0);
					updmap.put("orderGroupStatus",5);
					updmap.put("groupId",orderId);
					updmap.put("orderSuccessTime",new Date());
					//更新物流信息
					logisticsService.updateLogistics(dto);
					//更新邻家订单状态
					orderGroupService.updateStatusById(updmap,"1");
					System.out.println("*********饿了么订单已送达**********");
				}
				if(status==4){
//					4	已取消(同步取消不需要关注此状态)	商户主动取消订单请求处理成功后,蜂鸟配送开放平台将已取消状态回调给商户
					System.out.println("*********饿了么订单已取消**********");
				}
				if(status==5){
//					5	异常(蜂鸟这边取消是通过回调接口以“异常”状态返回的)
					String creason=esce.getDescription();
					if(creason==null||creason.isEmpty()){
						creason=esce.getAbnormal_desc();
					}else{
						creason=esce.getDescription()+";"+esce.getError_msg();
					}
					updmap.put("groupId",orderId);
					updmap.put("orderGroupStatus",10);
					updmap.put("cancelReason", "饿了么物流取消订单;"+creason);
					int b=thirdOrderService.updateLinjOrderStatus(updmap);
					if(b>0){
						dto.setOrder_id(orderId+"");
						if(esce.getDescription()!=null){
							dto.setCancel_reason("饿了么物流取消;"+creason);
						}
						dto.setStatus(esce.getOrder_status()+"");
						dto.setOrder_status(10);
						dto.setDm_name(esce.getCarrier_driver_name());
						dto.setDm_mobile(esce.getCarrier_driver_phone());
						dto.setDm_id(esce.getCarrier_driver_id());
						dto.setUpdate_time(new Date().getTime());
						dto.setDeleted(1);
						//更新物流信息
						logisticsService.updateLogistics(dto);
//						推送消息
						jgPushDeviceService.pushMessageByOrderId(orderId, "1","1");
					}
					System.out.println("*********饿了么订单异常**********");
				}
			}
			return 200;
		}catch(Exception e){
			e.printStackTrace();
			LOG.error("饿了么推送订单出错",e.getMessage());
			return 0;
		}
	}
	
	/**
	 * 饿了么订单(取消回调接口)（饿了么未来将废弃）
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/elemeCancelOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public Object elemeCancelOrder(HttpServletRequest request,@RequestBody ElemeCallEntity ece){
		JSONObject reslut=new JSONObject();
		try{
//			System.out.println("********饿了么***"+JSONUtil.ObjToJSON(ece).toString());
			if(ece!=null&&(ElemeOpenConfig.appId).equals(ece.getApp_id())){
				String order_id=ece.getData().getPartner_order_code();
				Long orderId=0l;
				if(order_id!=null&&!(order_id.isEmpty())){
					orderId=Long.valueOf(order_id);
				}else{
					reslut.put("code",40004);
					reslut.put("msg","订单id不正确");
					reslut.put("data",new JSONObject());
					return reslut.toString();
				}
				//更新物流信息
				Logistics dto=new Logistics();
				dto.setOrder_id(order_id);
				dto.setOrder_status(10);
				dto.setUpdate_time(ece.getData().getOrder_cancel_time());
				dto.setDeleted(1);
				logisticsService.updateLogistics(dto);
				
				//更新邻家订单状态
				Map<String,Object> updmap=new HashMap<String, Object>();
				updmap.put("orderGroupStatus",10);
				updmap.put("groupId",ece.getData().getPartner_order_code());
				updmap.put("cancelReason", "饿了么物流取消订单");
				boolean f=orderGroupService.updateStatusById(updmap,"1");
				if(f){
					//推送消息
					jgPushDeviceService.pushMessageByOrderId(orderId, "1","1");
				}
				reslut.put("code",200);
				reslut.put("msg", "token不正确或token已失效");
				reslut.put("data", new JSONObject());
			}else{
				reslut.put("code",40004);
				reslut.put("msg","token不正确或token已失效");
				reslut.put("data",new JSONObject());
			}
		}catch(Exception e){
			reslut.put("code",40004);
			reslut.put("msg", "订单处理异常!");
			reslut.put("data", new JSONObject());
			LOG.error("饿了么推送订单出错",e.getMessage());
			return reslut.toString();
		}
		return reslut.toString();
	}
	
	/**
	 * 饿了么平台订单推送接口
	 * @param request
	 * @param status;1,新订单；2，确认订单；3，派送中；4，取餐中；5，派送成功；6，派送失败；7，订单完成8，订单取消；9，争取处理中
	 * @return
	 */
	@RequestMapping(value="/elemeOrderPush",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public Object elemeOrderPush(HttpServletRequest request){
		JSONObject reslut=new JSONObject();
		try{
			System.out.println("********饿了么平台***");
			String data = IOUtils.toString(request.getInputStream(),"UTF-8");
			System.out.println("###################");  
	        System.out.println("********饿了么数据***:" + data);  
	        System.out.println("###################");  
			if(!Tools.isEmpty(data)){
				CallBackMessage message = JSONUtil.fromJson(data,CallBackMessage.class);
				Integer type=message.getType();
				String msgData=message.getMessage();
				if(type==null){
					reslut.put("message","fail");
					return reslut.toString();
				}
				if(!Tools.isEmpty(msgData)){
//*******************************订单生效*******************************
					//订单生效
					if(type==10){
						OOrder order=JSONUtil.fromJson(msgData,OOrder.class);
						//查询是否已经插入数据(去重)
						String orderId=order.getId();
						if(!Tools.isEmpty(orderId)){
							OOrder oorder=elemeOrderService.selectElemeOrderById(orderId);
							if(oorder==null){
								order.setMsg_type(type);
								order.setOrder_status(1);
								boolean flag=elemeOrderService.insertElemeLocalOrder(order);
								System.out.println("订单插入数据msg:"+flag);
								if(!flag){
									reslut.put("message","fail");
									return reslut.toString();
								}else{
									//客户端消息推送(新订单)
									jgPushDeviceService.pushMessageByOrderId(Long.valueOf(orderId),"1","5");
								}
							}else{
								reslut.put("message", "ok");
								return reslut.toString();
							}
						}
					}
//*******************************订单状态变更消息*******************************
					//商户接单
//					if(type==12){
//						OMessage oMessage=JSONUtil.fromJson(msgData,OMessage.class);
//						OOrder order=new OOrder();
//						order.setId(oMessage.getOrderId());
//						order.setStatus(OOrderStatus.valid);
//						order.setActiveAt(new Date());
//						boolean f=elemeOrderService.updateElemeLocalOrder(order);
//						System.out.println("商户接单oMessage:"+JSONUtil.ObjToJSON(oMessage));
//						System.out.println("flag:"+f);
//					}
					
					//14   订单被取消		订单状态变更消息
					//15   订单置为无效	订单状态变更消息
					//17   订单强制无效	订单状态变更消息
					//18   订单完结	订单状态变更消息
					if(type==14||type==15||type==17||type==18){
						OrderStatusMessage osMessage=JSONUtil.fromJson(msgData,OrderStatusMessage.class);
						OOrder order=new OOrder();
						order.setId(osMessage.getOrderId());
						order.setStatus(OOrderStatus.getByName(osMessage.getState()));
						order.setShopId(osMessage.getShopId());
						order.setMsg_type(type);
						if(type==18){
							order.setOrder_success_time(new Date());
							order.setOrder_status(7);
						}else{
							//取消时间
							order.setOrder_cancel_time(new Date());
							order.setCancelReason(type+"");
							order.setOrder_status(8);
						}
						boolean f=elemeOrderService.updateElemeLocalOrder(order);
						if(f&&type!=18){
							//客户端消息推送(取消)
							jgPushDeviceService.pushMessageByOrderId(Long.valueOf(osMessage.getOrderId()),"4","5");
						}
						System.out.println("订单状态变更消息flag:"+f);
					}
//*******************************订单状态变更消息*******************************
					
//*******************************取消单消息*******************************					
					//20;用户申请取消单	取消单流程;
					//21;用户取消取消单申请	取消单流程;
					//22;商户拒绝取消单	取消单流程;
					//23;商户同意取消单	取消单流程;
					//24;用户申请仲裁取消单	取消单流程;
					//25;客服仲裁取消单申请有效	取消单流程;
					//26;客服仲裁取消单申请无效	取消单流程;
					if(type>19&&type<27){
						OrderStatusMessage osMessage=JSONUtil.fromJson(msgData,OrderStatusMessage.class);
						OOrder order=new OOrder();
						order.setId(osMessage.getOrderId());
						order.setRefundStatus(OOrderRefundStatus.getByName(osMessage.getState()));
						order.setMsg_type(type);
						if(type==20||type==25){
							//取消时间
							order.setOrder_cancel_time(new Date());
							order.setCancelReason(type+"");
							order.setOrder_status(8);
						}
						if(type==21){
//							order.setOrder_status(0);
						}
						if(type==22){
							order.setOrder_status(1);
						}
						if(type==24){
							order.setOrder_status(9);
						}
						if(type==26){
//							order.setOrder_status(0);
						}
						boolean f=elemeOrderService.updateElemeLocalOrder(order);
						if(f){
							//客户端消息推送(取消)
							if(type==20||type==25){
								jgPushDeviceService.pushMessageByOrderId(Long.valueOf(osMessage.getOrderId()),"4","5");
							}
							//客户端消息推送(新订单)
							if(type==22){
								jgPushDeviceService.pushMessageByOrderId(Long.valueOf(osMessage.getOrderId()),"1","5");
							}
							//客户端消息推送(订单锁定)
							if(type==24){
								jgPushDeviceService.pushMessageByOrderId(Long.valueOf(osMessage.getOrderId()),"7","5");
							}
						}
						System.out.println("取消单消息flag:"+f);
					}
//*******************************取消单消息*******************************
					
//*******************************退单消息*******************************
					//30;用户申请退单	退单流程;
					//31;用户取消退单	退单流程;
					//32;商户拒绝退单	退单流程;
					//33;商户同意退单	退单流程;
					//34;用户申请仲裁	退单流程;
					//35;客服仲裁退单有效	退单流程;
					//36;客服仲裁退单无效	退单流程;
					if(type>29&&type<37){
						OrderStatusMessage osMessage=JSONUtil.fromJson(msgData,OrderStatusMessage.class);
						OOrder order=new OOrder();
						order.setId(osMessage.getOrderId());
						order.setRefundStatus(OOrderRefundStatus.getByName(osMessage.getState()));
						order.setMsg_type(type);
						if(type==30||type==35){
							//取消时间
							order.setOrder_cancel_time(new Date());
							order.setCancelReason(type+"");
							order.setOrder_status(8);
						}
						if(type==31){
//							order.setOrder_status(0);
						}
						if(type==34){
							order.setOrder_status(9);
						}
						if(type==36){
//							order.setOrder_status(0);
						}
						boolean f=elemeOrderService.updateElemeLocalOrder(order);
						if(type==30||type==35){
							jgPushDeviceService.pushMessageByOrderId(Long.valueOf(osMessage.getOrderId()),"4","5");
						}
						if(type==34){
							jgPushDeviceService.pushMessageByOrderId(Long.valueOf(osMessage.getOrderId()),"7","5");
						}
						System.out.println("退单消息flag:"+f);
					}
//*******************************退单消息*******************************
					
//*******************************催单消息*******************************
					//45;用户催单	催单流程;
					//46;商家回复用户催单	催单流程;
					if(type==45||type==46){
						OrderStatusMessage osMessage=JSONUtil.fromJson(msgData,OrderStatusMessage.class);
						OOrder order=new OOrder();
						order.setId(osMessage.getOrderId());
						order.setMsg_type(type);
						if(type==45){
							order.setUrgeNum(1);	    
						}
						boolean f=elemeOrderService.updateElemeLocalOrder(order);
						if(type==45){
							jgPushDeviceService.pushMessageByOrderId(Long.valueOf(osMessage.getOrderId()),"3","5");
						}
						System.out.println("催单消息flag:"+f);
					}

//*******************************催单消息*******************************

//*******************************运单状态变更消息*******************************
					//51;订单待分配配送商	运单追踪;		52;订单待分配配送员	运单追踪;		53;配送员取餐中	运单追踪;
					//54;配送员已到店	运单追踪;		55;配送员配送中	运单追踪;			56;配送成功	运单追踪;
					//57;配送取消，商户取消;			58;配送取消，用户取消;			59;配送取消，物流系统取消	运单追踪;
					//60;配送失败，呼叫配送晚	运单追踪;	61;配送失败，餐厅出餐问题	运单追踪;	62;配送失败，商户中断配送	运单追踪;
					//63;配送失败，用户不接电话	运单追踪;	64;配送失败，用户退单	运单追踪;		65;配送失败，用户地址错误	运单追踪;
					//66;配送失败，超出服务范围	运单追踪;	67;配送失败，骑手标记异常	运单追踪;	68;配送失败，系统自动标记异常	运单追踪;	
					//69;配送失败，其他异常	运单追踪;	70;配送失败，超时标记异常	运单追踪;
					if(type>50&&type<71){
						OrderStatusMessage osMessage=JSONUtil.fromJson(msgData,OrderStatusMessage.class);
						OOrder order=new OOrder();
						order.setId(osMessage.getOrderId());
						order.setShopId(osMessage.getShopId());
						order.setStatus(OOrderStatus.getByName(osMessage.getState()));
						order.setMsg_type(type);
						if(type==53){
							//取餐中
							order.setOrder_status(3);
						}else if(type==54){
							//配送员已到店
							order.setOrder_status(4);
						}else if(type==56){
							//配送成功
//							order.setOrder_status(5);
						}else if(type==57||type==58||type==59){
							//配送取消
							order.setOrder_status(6);
						}else if(type>59&&type<71){
							//配送失败
							order.setOrder_status(6);
						}
						boolean f=elemeOrderService.updateElemeLocalOrder(order);
						System.out.println("运单状态变更消息flag:"+f);
					}
				}
			}
			if(true){
//				if(ece!=null&&(ElemeOpenConfig.appId).equals(ece.getApp_id())){
//					String order_id=ece.getData().getPartner_order_code();
//					Long orderId=0l;
//					if(order_id!=null&&!(order_id.isEmpty())){
//						orderId=Long.valueOf(order_id);
//					}else{
//						reslut.put("code",40004);
//						reslut.put("msg","订单id不正确");
//						reslut.put("data",new JSONObject());
//						return reslut.toString();
//					}
//					//更新物流信息
//					Logistics dto=new Logistics();
//					dto.setOrder_id(order_id);
//					dto.setOrder_status(10);
//					dto.setUpdate_time(ece.getData().getOrder_cancel_time());
//					dto.setDeleted(1);
//					logisticsService.updateLogistics(dto);
//					
//					//更新邻家订单状态
//					Map<String,Object> updmap=new HashMap<String, Object>();
//					updmap.put("orderGroupStatus",10);
//					updmap.put("groupId",ece.getData().getPartner_order_code());
//					updmap.put("cancelReason", "饿了么物流取消订单");
//					boolean f=orderGroupService.updateStatusById(updmap,"1");
//					if(f){
//						//推送消息
//						jgPushDeviceService.pushMessageByOrderId(orderId, "1","1");
//					}
					reslut.put("message", "ok");
				}else{
					reslut.put("message","fail");
				}
		}catch(Exception e){
			e.printStackTrace();
			reslut.put("message","error");
			return reslut.toString();
		}
		return reslut.toString();
	}
}
