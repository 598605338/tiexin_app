package com.linjia.web.thirdService.dada.model;

public class DaDaLogisticsCallbackDto {
	
	private String client_id;		
	private String order_id;		
	//订单状态(0待发布 1待接单 2待取货 3执行中 4已完成 5已取消 7已过期-超过期望取货时间2小时未接单，自动变为已过期）
	private int order_status;		
	private String cancel_reason;	
	private int dm_id;	
	private String dm_name;	
	private String dm_mobile;	
	private int update_time;	
	//表明事件触发类型（4:表示追加订单被拒绝，7:表示指派订单过期导致，8:表示第三方主动取消）
	private int action_type;	
	private String signature;
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public int getOrder_status() {
		return order_status;
	}
	public void setOrder_status(int order_status) {
		this.order_status = order_status;
	}
	public String getCancel_reason() {
		return cancel_reason;
	}
	public void setCancel_reason(String cancel_reason) {
		this.cancel_reason = cancel_reason;
	}
	public int getDm_id() {
		return dm_id;
	}
	public void setDm_id(int dm_id) {
		this.dm_id = dm_id;
	}
	public String getDm_name() {
		return dm_name;
	}
	public void setDm_name(String dm_name) {
		this.dm_name = dm_name;
	}
	public String getDm_mobile() {
		return dm_mobile;
	}
	public void setDm_mobile(String dm_mobile) {
		this.dm_mobile = dm_mobile;
	}
	public int getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(int update_time) {
		this.update_time = update_time;
	}
	public int getAction_type() {
		return action_type;
	}
	public void setAction_type(int action_type) {
		this.action_type = action_type;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}	
	
}
