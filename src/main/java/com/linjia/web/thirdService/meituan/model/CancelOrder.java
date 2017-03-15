package com.linjia.web.thirdService.meituan.model;

public class CancelOrder {

	private String app_id;
	//时间戳
	private Long timestamp;
	
	private String sig;
	//订单ID
	private String order_id;
	//规范化的订单取消code，详情请参考12.2节
	private String reason_code;	
	//取消原因
	private String reason;
	
	//通知类型，apply：发起退款
	//agree：确认退款
	//reject：驳回退款
	//cancelRefund：用户取消退款申请
	//cancelRefundComplaint :取消退款申诉
	private String notify_type;
	
	private Long time;
	
	private String dispatcher_name;
	
	private String dispatcher_mobile;
	
	private Integer logistics_status;
	
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getSig() {
		return sig;
	}
	public void setSig(String sig) {
		this.sig = sig;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getReason_code() {
		return reason_code;
	}
	public void setReason_code(String reason_code) {
		this.reason_code = reason_code;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getNotify_type() {
		return notify_type;
	}
	public void setNotify_type(String notify_type) {
		this.notify_type = notify_type;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getDispatcher_name() {
		return dispatcher_name;
	}
	public void setDispatcher_name(String dispatcher_name) {
		this.dispatcher_name = dispatcher_name;
	}
	public String getDispatcher_mobile() {
		return dispatcher_mobile;
	}
	public void setDispatcher_mobile(String dispatcher_mobile) {
		this.dispatcher_mobile = dispatcher_mobile;
	}
	public Integer getLogistics_status() {
		return logistics_status;
	}
	public void setLogistics_status(Integer logistics_status) {
		this.logistics_status = logistics_status;
	}
	
}
