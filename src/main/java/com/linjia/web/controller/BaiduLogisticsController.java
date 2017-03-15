package com.linjia.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linjia.base.controller.BaseController;
import com.linjia.tools.Util;
import com.linjia.web.model.Logistics;
import com.linjia.web.model.OrderGroup;
import com.linjia.web.query.LogisticsQuery;
import com.linjia.web.service.LogisticsService;
import com.linjia.web.service.OrderGroupService;
import com.linjia.web.test.BdLogisticTestData;
import com.linjia.web.thirdService.bdlogistics.post.PostData;
import com.linjia.web.thirdService.bdlogistics.post.QueryIfCanSend;
import com.linjia.web.thirdService.bdlogistics.service.BdLogisService;
import com.linjia.web.thirdService.dada.service.LogisticsAccessTokenService;
import com.linjia.web.thirdService.dada.service.LogisticsOrderService;

/**
 *百度物流控制类
 * 
 * @author xiangsy
 *
 */
@Controller
@RequestMapping("/bdlogistics")
public class BaiduLogisticsController extends BaseController{
	private static Logger LOG = LoggerFactory.getLogger(BaiduLogisticsController.class);
	
	@Autowired
	private BdLogisService bdLogisService;
	@Autowired
	private OrderGroupService orderGroupService;// 邻家订单
	@Autowired
	private LogisticsService logisticsService;
	@Autowired
	private LogisticsOrderService logisticsOrderService; // 达达
	@Autowired
	private LogisticsAccessTokenService logisticsAccessTokenService;
	
	@RequestMapping(value="/createOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public Object addOrder(HttpServletRequest request,PostData postData){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			//测试使用
			PostData pd=BdLogisticTestData.getPostData();
			JSONObject reslut=bdLogisService.createOrder(pd);
			map.put("status", "ok");
			map.put("resultData",reslut.toString());
		}catch(Exception e){
			map.put("message", "创建百度物流订单出错");
			map.put("status", "fail");
			LOG.error("创建物流订单出错",e.getMessage());
		}
		return map;
	}
	
	/**
	 * 取消订单
	 * @param request
	 * @param order_id
	 * @param reason_id
	 * @param reason_detail
	 * @return
	 */
	@RequestMapping(value="/cancelOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public Object cancelOrder(HttpServletRequest request,@RequestParam Long order_id,@RequestParam String reason){
		JSONObject reslut=null;
	try{
		reslut=bdLogisService.cancelOrder(order_id,reason);
	}catch(Exception e){
		reslut=new JSONObject();
		reslut.put("status", "fail");
		reslut.put("message", "取消物流订单出错");
		LOG.error("取消物流订单出错",e.getMessage());
	}
	return reslut.toString();
	}
	
	/**
	 * 查询订单是否在配送范围内
	 * @param request
	 * @param wl_shop_id
	 * @param user_longitude
	 * @param user_latitude
	 * @return
	 */
	@RequestMapping(value="/queryIfSend",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public Object queryIfSend(HttpServletRequest request,QueryIfCanSend qsf){
		Map<String,Object> map=new HashMap<String, Object>();
	try{
		Long push_time = System.currentTimeMillis()/ 1000;
		qsf.setPush_time(push_time);
		if(!(qsf.getUser_latitude().isEmpty())&&!(qsf.getUser_longitude().isEmpty())&&!(qsf.getWl_shop_id().isEmpty())){
			JSONObject reslut=bdLogisService.queryIfSend(qsf);
			if(reslut!=null&&!(reslut.toString().isEmpty())){
				if(reslut.getInt("error_no")==0){
					Object res=reslut.get("result");
					if(res instanceof JSONObject){
						map.put("status", "ok");
						map.put("message",res.toString());
					}else{
						map.put("status", "fail");
						map.put("message","不在配送范围内!");
					}
				}else{
					map.put("status", "fail");
					map.put("message","查询结果出错");
				}
			}
		}else{
			map.put("status", "fail");
			map.put("message", "经纬度和店铺物流id不能为空!");
		}
	}catch(Exception e){
		map.put("status", "error");
		map.put("message", "取消物流订单出错");
		LOG.error("取消物流订单出错",e.getMessage());
	}
	return map;
}
	
	/**
	 * 测试取消订单
	 * @param request
	 * @param order_id
	 * @param reason_id
	 * @param reason_detail
	 * @return
	 */
	@RequestMapping(value="/myCancelOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public Object myCancelOrder(HttpServletRequest request,@RequestParam Long orderId){
		Map<String, Object> map=new HashMap<String, Object>();
	try{
		// 查询订单信息
		OrderGroup orderGroup = orderGroupService.selectById(orderId);
		if (orderGroup == null) {
			Util.writeError(map);
			map.put("message", "订单不存在");
			return map;
		}
		Integer ostatus=orderGroup.getOrderGroupStatus();
		if(ostatus==4){
			//查询物流信息
			LogisticsQuery lquery=new LogisticsQuery();
			lquery.setOrder_id(orderId);
			Logistics logisq=logisticsService.selectLogisticsById(lquery);
			Integer logType=logisq.getLogis_type();
			if(logType!=null&&logType==2){
				//大达物流取消
				JSONObject jsonObj=logisticsOrderService.canceOrderTest(orderId,"不喜欢");
//				jsonObj=logisticsOrderService.canceOrder(orderId,reasonCode,reason);
				boolean flag=logisticsAccessTokenService.initDadaCode(jsonObj);
				if(!flag){
					jsonObj=null;
//					jsonObj=logisticsOrderService.canceOrderTest(orderId,reasonCode,reason);
					jsonObj=logisticsOrderService.canceOrderTest(orderId,"不喜欢");
				}
				if(jsonObj==null||(jsonObj.toString().isEmpty())||!"ok".equals(jsonObj.getString("status"))){
					map.put("status", "fail");
					map.put("message", "达达物流退单失败!");
					return map;
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
						Object res=jsonObj2.get("result");
						if(res instanceof Boolean){
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
		}
	}catch(Exception e){
		e.printStackTrace();
		map.put("status", "fail");
		map.put("message", "取消物流订单出错");
		LOG.error("取消物流订单出错",e.getMessage());
	}
	return map;
	}
}
