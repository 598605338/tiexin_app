package com.linjia.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.linjia.base.controller.BaseController;
import com.linjia.web.model.BaoXiang;
import com.linjia.web.test.DadaTestData;
import com.linjia.web.thirdService.dada.model.DaDaLogisticsCallbackDto;
import com.linjia.web.thirdService.dada.model.DaDaLogisticsParam;
import com.linjia.web.thirdService.dada.service.LogisticsAccessTokenService;
import com.linjia.web.thirdService.dada.service.LogisticsOrderService;

/**
 *达达物流控制类
 * 
 * @author xiangsy
 *
 */
@Controller
@RequestMapping("/dada")
public class DadaLogisticsController extends BaseController{
	private static Logger LOG = LoggerFactory.getLogger(DadaLogisticsController.class);
	
	@Autowired
	private LogisticsAccessTokenService logisticsAccessTokenService;
	@Autowired
	private LogisticsOrderService logisticsOrderService;
	
	@RequestMapping(value="/addDdLOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public Object addOrder(HttpServletRequest request,DaDaLogisticsParam lopm){
		JSONObject jsonObj=null;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			//测试使用
//			Long time=System.currentTimeMillis()/1000;
//			DaDaLogisticsParam lopm=DadaTestData.getDadaData();
//			lopm.setOrigin_id(time+"");
			if(lopm!=null){
				jsonObj=logisticsOrderService.addOrder(lopm);
				boolean flag=logisticsAccessTokenService.initDadaCode(jsonObj);
				if(!flag){
					jsonObj=null;
					jsonObj=logisticsOrderService.addOrder(lopm);
				}
			}
			map.put("status", "ok");
			map.put("resultData",jsonObj.toString());
		}catch(Exception e){
			map.put("status", "fail");
			map.put("message", "添加达达物流订单出错");
			LOG.error("添加订单出错",e.getMessage());
		}
		return map;
	}
	
	@RequestMapping(value="/getDmInfo",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getDmInfo(HttpServletRequest request,@RequestParam String dm_id,@RequestParam Long order_id){
		JSONObject jsonObj=null;
		try{
			jsonObj=logisticsOrderService.getDmInfo(dm_id, order_id);
			boolean flag=logisticsAccessTokenService.initDadaCode(jsonObj);
			if(!flag){
				jsonObj=null;
				jsonObj=logisticsOrderService.getDmInfo(dm_id, order_id);
			}
		}catch(Exception e){
			LOG.error("获取配送员信息出错",e.getMessage());
		}
		return jsonObj.toString();
	}
	
	@RequestMapping(value="/getXiaoPiao",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getXiaoPiao(HttpServletRequest request,@RequestParam Long order_id){
		JSONObject jsonObj=null;
		try{
			jsonObj=logisticsOrderService.getXiaoPiao(order_id);
			boolean flag=logisticsAccessTokenService.initDadaCode(jsonObj);
			if(!flag){
				jsonObj=null;
				jsonObj=logisticsOrderService.getXiaoPiao(order_id);
			}
		}catch(Exception e){
			LOG.error("获取小票信息出错",e.getMessage());
		}
		return jsonObj.toString();
	}
	
	@RequestMapping(value="/getOrderStatus",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getOrderStatus(HttpServletRequest request,@RequestParam Long order_id){
		JSONObject jsonObj=null;
		try{
			jsonObj=logisticsOrderService.getOrderStatus(order_id);
			boolean flag=logisticsAccessTokenService.initDadaCode(jsonObj);
			if(!flag){
				jsonObj=null;
				jsonObj=logisticsOrderService.getOrderStatus(order_id);
			}
		}catch(Exception e){
			LOG.error("获取订单状态出错",e.getMessage());
		}
		return jsonObj.toString();
	}
	
	@RequestMapping(value="/getCanceReason",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getCanceReason(HttpServletRequest request){
		JSONObject jsonObj=null;
		try{
			jsonObj=logisticsOrderService.getCanceReason();
			boolean flag=logisticsAccessTokenService.initDadaCode(jsonObj);
			if(!flag){
				jsonObj=null;
				jsonObj=logisticsOrderService.getCanceReason();
			}
		}catch(Exception e){
			LOG.error("获取取消理由出错",e.getMessage());
		}
		return jsonObj.toString();
	}
	
	@RequestMapping(value="/canceOrderTest",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object canceOrderTest(HttpServletRequest request,@RequestParam Long order_id,@RequestParam String reason){
		JSONObject jsonObj=null;
		try{
			jsonObj=logisticsOrderService.canceOrderTest(order_id, reason);
			boolean flag=logisticsAccessTokenService.initDadaCode(jsonObj);
			if(!flag){
				jsonObj=null;
				jsonObj=logisticsOrderService.canceOrderTest(order_id, reason);
			}
		}catch(Exception e){
			LOG.error("取消订单出错Test",e.getMessage());
		}
		return jsonObj.toString();
	}
	
	@RequestMapping(value="/acceptOrderTest",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object acceptOrderTest(HttpServletRequest request,@RequestParam Long order_id){
		JSONObject jsonObj=null;
		try{
			jsonObj=logisticsOrderService.acceptOrderTest(order_id);
			boolean flag=logisticsAccessTokenService.initDadaCode(jsonObj);
			if(!flag){
				jsonObj=null;
				jsonObj=logisticsOrderService.acceptOrderTest(order_id);
			}
		}catch(Exception e){
			LOG.error("接收订单出错Test",e.getMessage());
		}
		return jsonObj.toString();
	}
	
	@RequestMapping(value="/refuseOrderTest",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object refuseOrderTest(HttpServletRequest request,@RequestParam Long order_id){
		JSONObject jsonObj=null;
		try{
			jsonObj=logisticsOrderService.refuseOrderTest(order_id);
			boolean flag=logisticsAccessTokenService.initDadaCode(jsonObj);
			if(!flag){
				jsonObj=null;
				jsonObj=logisticsOrderService.refuseOrderTest(order_id);
			}
		}catch(Exception e){
			LOG.error("拒绝订单出错Test",e.getMessage());
		}
		return jsonObj.toString();
	}
	
	@RequestMapping(value="/fetchOrderTest",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object fetchOrderTest(HttpServletRequest request,@RequestParam Long order_id){
		JSONObject jsonObj=null;
		try{
			jsonObj=logisticsOrderService.fetchOrderTest(order_id);
			boolean flag=logisticsAccessTokenService.initDadaCode(jsonObj);
			if(!flag){
				jsonObj=null;
				jsonObj=logisticsOrderService.fetchOrderTest(order_id);
			}
		}catch(Exception e){
			LOG.error("完成取货出错Test",e.getMessage());
		}
		return jsonObj.toString();
	}
	
	@RequestMapping(value="/finishOrderTest",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object finishOrderTest(HttpServletRequest request,@RequestParam Long order_id){
		JSONObject jsonObj=null;
		try{
			jsonObj=logisticsOrderService.finishOrderTest(order_id);
			boolean flag=logisticsAccessTokenService.initDadaCode(jsonObj);
			if(!flag){
				jsonObj=null;
				jsonObj=logisticsOrderService.finishOrderTest(order_id);
			}
		}catch(Exception e){
			LOG.error("完成订单出错Test",e.getMessage());
		}
		return jsonObj.toString();
	}
	
	@RequestMapping(value="/rePublisOrder",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object rePublisOrder(HttpServletRequest request,DaDaLogisticsParam logp){
		JSONObject jsonObj=null;
		try{
			jsonObj=logisticsOrderService.rePublisOrder(logp);
			boolean flag=logisticsAccessTokenService.initDadaCode(jsonObj);
			if(!flag){
				jsonObj=null;
				jsonObj=logisticsOrderService.rePublisOrder(logp);
            }
		}catch(Exception e){
			LOG.error("重新发布订单出错",e.getMessage());
		}
		return jsonObj.toString();
	}
	
	@RequestMapping(value="/canceOrder",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object canceOrder(HttpServletRequest request,@RequestParam Long order_id,@RequestParam String cancel_reason_id,@RequestParam String cancel_reason){
		JSONObject jsonObj=null;
		try{
			jsonObj=logisticsOrderService.canceOrder(order_id, cancel_reason_id, cancel_reason);
			boolean flag=logisticsAccessTokenService.initDadaCode(jsonObj);
			if(!flag){
				jsonObj=null;
				jsonObj=logisticsOrderService.canceOrder(order_id, cancel_reason_id, cancel_reason);
			}
		}catch(Exception e){
			LOG.error("取消订单出错",e.getMessage());
		}
		return jsonObj.toString();
	}
	
	@RequestMapping(value="/reAddOrder",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object reAddOrder(HttpServletRequest request,DaDaLogisticsParam logp){
		JSONObject jsonObj=null;
		try{
			jsonObj=logisticsOrderService.reAddOrder(logp);
			boolean flag=logisticsAccessTokenService.initDadaCode(jsonObj);
			if(!flag){
				jsonObj=null;
				jsonObj=logisticsOrderService.reAddOrder(logp);
			}
		}catch(Exception e){
			LOG.error("追加订单出错",e.getMessage());
		}
		return jsonObj.toString();
	}
	
	@RequestMapping(value="/addXiaoFei",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object addXiaoFei(HttpServletRequest request,@RequestParam Long order_id,@RequestParam String origin_id,@RequestParam String tips,@RequestParam String cityCode){
		JSONObject jsonObj=null;
		try{
			jsonObj=logisticsOrderService.addXiaoFei(order_id, origin_id, tips, cityCode);
			boolean flag=logisticsAccessTokenService.initDadaCode(jsonObj);
			if(!flag){
				jsonObj=null;
				jsonObj=logisticsOrderService.addXiaoFei(order_id, origin_id, tips, cityCode);
			}
		}catch(Exception e){
			LOG.error("增加消费出错",e.getMessage());
		}
		return jsonObj.toString();
	}
	
	@RequestMapping(value="/getCitysInfo",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getCitysInfo(HttpServletRequest request){
		JSONObject jsonObj=null;
		try{
			jsonObj=logisticsOrderService.getCityList();
			boolean flag=logisticsAccessTokenService.initDadaCode(jsonObj);
			if(!flag){
				jsonObj=null;
				jsonObj=logisticsOrderService.getCityList();
			}
		}catch(Exception e){
			LOG.error("查询城市信息出错",e.getMessage());
		}
		return jsonObj.toString();
	}
	
	
	@RequestMapping(value="/getDmInfoTest",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getDmInfoTest(HttpServletRequest request,DaDaLogisticsCallbackDto lcbd){
		JSONObject jsonObj=null;
		try{
			System.out.println("#####"+lcbd.getDm_id());
			System.out.println(request.getParameter("ddddddd"));
		}catch(Exception e){
			LOG.error("查询城市信息出错",e.getMessage());
		}
		return lcbd.toString();
	}
	
}
