package com.linjia.web.thirdService.bdlogistics.service.impl;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.linjia.tools.JSONUtil;
import com.linjia.tools.NetRequest;
import com.linjia.web.thirdService.bdlogistics.factory.BdLogisticConfig;
import com.linjia.web.thirdService.bdlogistics.post.PostCancelReason;
import com.linjia.web.thirdService.bdlogistics.post.PostData;
import com.linjia.web.thirdService.bdlogistics.post.QueryIfCanSend;
import com.linjia.web.thirdService.bdlogistics.post.ReciveFeachOrder;
import com.linjia.web.thirdService.bdlogistics.post.ReciveFinishOrder;
import com.linjia.web.thirdService.bdlogistics.post.ReciveSendOrder;
import com.linjia.web.thirdService.bdlogistics.service.BdLogisService;
import com.linjia.web.thirdService.bdlogistics.utils.BdLogisUtil;

@Service
public class BdLogisServiceImpl implements BdLogisService{

	@Override
	public JSONObject createOrder(PostData pd) {
		long app_id = Long.parseLong(BdLogisticConfig.getAppConfig("app_id"));
		String app_key = BdLogisticConfig.getAppConfig("app_key");
		pd.setApp_id(app_id);
		// 生成sign
		String sign = BdLogisUtil.generateSign(pd, app_id, app_key);
		pd.setSign(sign);
		String reqUrl=BdLogisticConfig.getReqUrl("createOrder");
		JSONObject respJson=JSONUtil.ObjToJSON(pd);
		System.out.println("百度物流respJson:"+respJson);
		JSONObject reslut=NetRequest.requestPost(reqUrl, respJson, "");
		return reslut;
	}

	@Override
	public JSONObject cancelOrder(Long order_id,String reason) {
		PostCancelReason pc=new PostCancelReason();
		Long push_time = System.currentTimeMillis()/ 1000;
		pc.setPush_time(push_time);
		pc.setReason_id(2);
		pc.setOrder_id(order_id);
		pc.setReason_detail(reason);
		long app_id = Long.parseLong(BdLogisticConfig.getAppConfig("app_id"));
		String app_key = BdLogisticConfig.getAppConfig("app_key");
		pc.setApp_id(app_id);
		// 生成sign
		String sign = BdLogisUtil.generateSign(pc, app_id, app_key);
		pc.setSign(sign);
		String reqUrl=BdLogisticConfig.getReqUrl("cancelOrder");
		JSONObject respJson=JSONUtil.ObjToJSON(pc);
		System.out.println("百度物流respJson:"+respJson);
		JSONObject reslut=NetRequest.requestPost(reqUrl, respJson, "");
		return reslut;
	}
	
	@Override
	public JSONObject feachOrder(ReciveFeachOrder pfo) {
		long app_id = Long.parseLong(BdLogisticConfig.getAppConfig("app_id"));
		String app_key = BdLogisticConfig.getAppConfig("app_key");
		pfo.setApp_id(app_id);
		// 生成sign
		String sign = BdLogisUtil.generateSign(pfo, app_id, app_key);
		pfo.setSign(sign);
		String reqUrl=BdLogisticConfig.getReqUrl("cancelOrder");
		JSONObject respJson=JSONUtil.ObjToJSON(pfo);
		System.out.println("百度物流respJson:"+respJson);
		JSONObject reslut=NetRequest.requestPost(reqUrl, respJson, "");
		return reslut;
	}
	
	@Override
	public JSONObject sendOrder(ReciveSendOrder pfo) {
		long app_id = Long.parseLong(BdLogisticConfig.getAppConfig("app_id"));
		String app_key = BdLogisticConfig.getAppConfig("app_key");
		pfo.setApp_id(app_id);
		// 生成sign
		String sign = BdLogisUtil.generateSign(pfo, app_id, app_key);
		pfo.setSign(sign);
		String reqUrl=BdLogisticConfig.getReqUrl("cancelOrder");
		JSONObject respJson=JSONUtil.ObjToJSON(pfo);
		System.out.println("百度物流respJson:"+respJson);
		JSONObject reslut=NetRequest.requestPost(reqUrl, respJson, "");
		return reslut;
	}
	
	@Override
	public JSONObject finishOrder(ReciveFinishOrder pfo) {
		long app_id = Long.parseLong(BdLogisticConfig.getAppConfig("app_id"));
		String app_key = BdLogisticConfig.getAppConfig("app_key");
		pfo.setApp_id(app_id);
		// 生成sign
		String sign = BdLogisUtil.generateSign(pfo, app_id, app_key);
		pfo.setSign(sign);
		String reqUrl=BdLogisticConfig.getReqUrl("cancelOrder");
		JSONObject respJson=JSONUtil.ObjToJSON(pfo);
		System.out.println("百度物流respJson:"+respJson);
		JSONObject reslut=NetRequest.requestPost(reqUrl, respJson, "");
		return reslut;
	}
	
	@Override
	public JSONObject queryIfSend(QueryIfCanSend ps) {
		long app_id = Long.parseLong(BdLogisticConfig.getAppConfig("app_id"));
		String app_key = BdLogisticConfig.getAppConfig("app_key");
		ps.setApp_id(app_id);
		// 生成sign
		String sign = BdLogisUtil.generateSign(ps, app_id, app_key);
		ps.setSign(sign);
		String reqUrl=BdLogisticConfig.getReqUrl("queryIfSend");
		JSONObject respJson=JSONUtil.ObjToJSON(ps);
		System.out.println("百度物流respJson:"+respJson);
		JSONObject reslut=NetRequest.requestPost(reqUrl, respJson, "");
		return reslut;
	}

}
