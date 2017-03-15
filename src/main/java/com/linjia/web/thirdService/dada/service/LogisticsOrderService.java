package com.linjia.web.thirdService.dada.service;

import org.json.JSONObject;

import com.linjia.web.thirdService.dada.model.DaDaLogisticsParam;

public interface LogisticsOrderService {
	
		//添加订单(post)
		JSONObject addOrder(DaDaLogisticsParam logpm);
		
		//获取配送员信息(GET)
		JSONObject getDmInfo(String dm_id,Long order_id);
		
		//获取小票信息(GET)
		JSONObject getXiaoPiao(Long order_id);
		
		//获取订单状态信息(GET)
		JSONObject getOrderStatus(Long order_id);
		
		//获取取消理由(GET)
		JSONObject getCanceReason();
		
		//重新发布订单(POST)
		JSONObject rePublisOrder(DaDaLogisticsParam logpm);
		
		//取消订单-正式环境(POST)
		JSONObject canceOrder(Long order_id,String cancel_reason_id,String cancel_reason);
		
		//追加订单
		JSONObject reAddOrder(DaDaLogisticsParam logpm);
		
		//增加小费
		JSONObject addXiaoFei(Long order_id,String origin_id,String tips,String cityCode);
		
		//获取城市信息列表
		JSONObject getCityList();
		
		//取消订单(仅在测试环境供调试使用)(GET)
		JSONObject canceOrderTest(Long order_id,String reason);
		
		//接受订单(仅在测试环境供调试使用)(GET)
		JSONObject acceptOrderTest(Long order_id);
		
		//拒绝订单(仅在测试环境供调试使用)(GET)
		JSONObject refuseOrderTest(Long order_id);
		
		//完成取货(仅在测试环境供调试使用)(GET)
		JSONObject fetchOrderTest(Long order_id);
		
		//订单已送达(仅在测试环境供调试使用)(GET)
		JSONObject finishOrderTest(Long order_id);

}
