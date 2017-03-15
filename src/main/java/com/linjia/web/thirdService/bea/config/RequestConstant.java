package com.linjia.web.thirdService.bea.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求常量
 */
public class RequestConstant {
    /**
     * 获取token
     */
    public static final String obtainToken = "/get_access_token";

    /**
     * 创建订单
     */
    public static final String orderCreate = "/v2/order";

    /**
     * 取消 订单
     */
    public static final String orderCancel = "/v2/order/cancel";

    /**
     * 订单查询
     */
    public static final String orderQuery = "/v2/order/query";
    
    /**
     * 订单状态
     */
    private static Map<Integer, String> statusMap;
    
    /**
     * 错误码
     */
    private static Map<Integer, String> errorCodeMap;
    
    /**
     * 物流取消异常码
     */
    private static Map<String, String> cancelCodeMap;
    
    static {
		statusMap = new HashMap<Integer, String>();
		errorCodeMap = new HashMap<Integer, String>();
		cancelCodeMap = new HashMap<String, String>();
		
		statusMap.put(1,"系统已接单");	//蜂鸟配送开放平台接单后,商户接收到系统已接单状态, 支持取消
		statusMap.put(20,"已分配骑手");	//蜂鸟配送开放平台接单后,商户接收到已分配骑手状态, 支持取消
		statusMap.put(80,"骑手已到店");	//蜂鸟配送开放平台将骑手已到店状态回调给商户, 支持取消
		statusMap.put(2,"配送中");	//蜂鸟配送开放平台将骑手配送中状态回调给商户, 不支持取消
		statusMap.put(3,"已送达");	//蜂鸟配送开放平台将已送达状态回调给商户, 不支持取消
		statusMap.put(4,"已取消");//(同步取消不需要关注此状态)	商户主动取消订单请求处理成功后,蜂鸟配送开放平台将已取消状态回调给商户
		statusMap.put(5,"异常");	//推单到物流开放平台后任何阶段产生异常,蜂鸟配送开放平台将异常状态回调给商户
		
		errorCodeMap.put(40000,"请求失败");
		errorCodeMap.put(40001,"appid不存在");
		errorCodeMap.put(40002,"验证签名失败");
		errorCodeMap.put(40003,"salt不在范围内");
		errorCodeMap.put(40004,"token不正确或token已失效");
		errorCodeMap.put(50010,"缺失必填项");
		errorCodeMap.put(50011,"订单号重复提交");
		errorCodeMap.put(50012,"订单预计送达时间小于当前时间");
		errorCodeMap.put(50018,"查询订单错误");
		errorCodeMap.put(50019,"查询运单错误");
		errorCodeMap.put(50025,"订单暂未生成");
		errorCodeMap.put(50026,"运单暂未生成");
		errorCodeMap.put(50037,"订单不存在");
		errorCodeMap.put(50040,"字段值过长");
		errorCodeMap.put(50041,"字段值不符合规则");
		errorCodeMap.put(50042,"无此服务类型");
		errorCodeMap.put(50101,"商户取消订单失败");
		errorCodeMap.put(50102,"当前订单状态不允许取消");
		errorCodeMap.put(50110,"未购买服务或服务已下线");
		errorCodeMap.put(500060,"订单配送距离太远了超过阈值");
		errorCodeMap.put(500070,"没有运力覆盖");
		errorCodeMap.put(500080,"没有绑定微仓");
		errorCodeMap.put(500090,"用户绑定的微仓和运力覆盖范围不匹配");
		errorCodeMap.put(500100,"订单超重");
		errorCodeMap.put(50015,"预计送达时间过长");
		
		
		cancelCodeMap.put("SCHEDULE_ORDER_OUT_OF_TIME_ERROR","预订单超时异常");
		cancelCodeMap.put("CARRIER_OFFLINE_ERROR","超出配送商营业时间");
		cancelCodeMap.put("REQUIRE_RECEIVE_TIME_INVALID","预计送达时间不正确");
		cancelCodeMap.put("ORDER_OUT_OF_DISTANCE_ERROR","订单超区");
		cancelCodeMap.put("ORDER_OUT_OF_WEIGHT","订单超重");
		cancelCodeMap.put("ORDER_OUT_OF_LOAD_ERROR","订单超出运力");
		cancelCodeMap.put("NO_CARRIER_ERROR","当前暂无骑手接单,请稍后重试");
		cancelCodeMap.put("ORDER_OUT_OF_SERVICE","运单超服务范围");
		cancelCodeMap.put("ORDER_OUT_OF_TIME_ERROR","订单超时");
		cancelCodeMap.put("MERCHANT_INTERRUPT_DELIVERY_ERROR","商户原因中断配送");
		cancelCodeMap.put("MERCHANT_FOOD_ERROR","商家出货问题");
		cancelCodeMap.put("MERCHANT_CALL_LATE_ERROR","呼叫配送晚");
		cancelCodeMap.put("USER_NOT_ANSWER_ERROR","用户不接电话");
		cancelCodeMap.put("USER_RETURN_ORDER_ERROR","用户退单,建议为用户办理退款");
		cancelCodeMap.put("USER_ADDRESS_ERROR","用户地址错误");
		cancelCodeMap.put("DELIVERY_OUT_OF_SERVICE","超出服务范围");
		cancelCodeMap.put("SYSTEM_MARKED_ERROR","系统自动标记异常--订单超过3小时未送达");
		cancelCodeMap.put("CARRIER_REMARK_EXCEPTION_ERROR","骑手标记异常");
		cancelCodeMap.put("OTHER_ERROR","其他	建议商户改派其他配送商");
		cancelCodeMap.put("NORMAL_ORDER_OUT_OF_TIME_ERROR","众包无骑手接单");
		cancelCodeMap.put("SYSTEM_ERROR","系统错误");
		cancelCodeMap.put("UNKNOW_ERROR","未知错误");
		cancelCodeMap.put("ORDER_REPETITION","订单重复");
		cancelCodeMap.put("CURRENT_STATUS_NOT_ALLOW_CANCEL","当前订单不允许取消");
		cancelCodeMap.put("USER_CANCEL","用户发起取消");
		cancelCodeMap.put("SYSTEM_CANCEL","系统取消");
		cancelCodeMap.put("MERCHANT_CANCEL","商户发起取消");
		cancelCodeMap.put("CARRIER_CANCEL","配送商发起取消");
		cancelCodeMap.put("DELIVERY_TIMEOUT","配送超时,系统标记异常");
		cancelCodeMap.put("REJECT_ORDER","拒单");
		cancelCodeMap.put("FOR_UPDATE_TIP","加小费取消");
    }
    
    public static String getStatusInfo(Integer statusCode){
    	if(statusCode==null){
    		return "";
    	}
    	return statusMap.get(statusCode);
    }
    
    public static String getErrorInfo(Integer errorCode){
    	if(errorCode==null){
    		return "";
    	}
    	return errorCodeMap.get(errorCode);
    }
    
    public static String getWrongInfo(String WrongCode){
    	if(WrongCode==null){
    		return "";
    	}
    	return cancelCodeMap.get(WrongCode);
    }
}
