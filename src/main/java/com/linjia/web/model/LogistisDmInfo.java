package com.linjia.web.model;

public class LogistisDmInfo {

	//订单号
	private String order_id;
	
	//物流状态名称
	private String statusName;
	
	//取餐时间
	private String pickup_time;
	
	//到店时间
	private String atshop_time;
	
	//送餐时间
	private String delivery_time;
	
	//完成时间
	private String finished_time;
	
	//接单时间
	private String recive_time;
	
	//送达时间
	private String send_time;
	
	private String cancel_time;
	
	//物流名称
	private String logisticsName;
	
	//配送人员名称
	private String dmName;
	
	//配送人员电话
	private String dmPhone;
	
	private Integer status;

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getRecive_time() {
		return recive_time;
	}

	public void setRecive_time(String recive_time) {
		this.recive_time = recive_time;
	}

	public String getSend_time() {
		return send_time;
	}

	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	public String getDmName() {
		return dmName;
	}

	public void setDmName(String dmName) {
		this.dmName = dmName;
	}

	public String getDmPhone() {
		return dmPhone;
	}

	public void setDmPhone(String dmPhone) {
		this.dmPhone = dmPhone;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPickup_time() {
		return pickup_time;
	}

	public void setPickup_time(String pickup_time) {
		this.pickup_time = pickup_time;
	}

	public String getAtshop_time() {
		return atshop_time;
	}

	public void setAtshop_time(String atshop_time) {
		this.atshop_time = atshop_time;
	}

	public String getDelivery_time() {
		return delivery_time;
	}

	public void setDelivery_time(String delivery_time) {
		this.delivery_time = delivery_time;
	}

	public String getFinished_time() {
		return finished_time;
	}

	public void setFinished_time(String finished_time) {
		this.finished_time = finished_time;
	}

	public String getCancel_time() {
		return cancel_time;
	}

	public void setCancel_time(String cancel_time) {
		this.cancel_time = cancel_time;
	}
	
	
}
