package com.linjia.web.thirdService.bdlogistics.service;

import org.json.JSONObject;

import com.linjia.web.thirdService.bdlogistics.post.PostCancelReason;
import com.linjia.web.thirdService.bdlogistics.post.PostData;
import com.linjia.web.thirdService.bdlogistics.post.QueryIfCanSend;
import com.linjia.web.thirdService.bdlogistics.post.ReciveFeachOrder;
import com.linjia.web.thirdService.bdlogistics.post.ReciveFinishOrder;
import com.linjia.web.thirdService.bdlogistics.post.ReciveSendOrder;

public interface BdLogisService {

	JSONObject createOrder(PostData pd);
	
	JSONObject cancelOrder(Long order_id, String reason);
	
	JSONObject feachOrder(ReciveFeachOrder pfo);
	
	JSONObject sendOrder(ReciveSendOrder sd);

	JSONObject finishOrder(ReciveFinishOrder pfo);
	
	JSONObject queryIfSend(QueryIfCanSend ps);
}
