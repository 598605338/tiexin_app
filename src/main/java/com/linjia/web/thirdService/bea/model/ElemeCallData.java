package com.linjia.web.thirdService.bea.model;

public class ElemeCallData {

	 private String partner_order_code;
	 private Integer order_cancel_reason_code;
     private String order_cancel_description;
     private Long order_cancel_time;
     private String order_cancel_notify_url;
	public String getPartner_order_code() {
		return partner_order_code;
	}
	public void setPartner_order_code(String partner_order_code) {
		this.partner_order_code = partner_order_code;
	}
	public Integer getOrder_cancel_reason_code() {
		return order_cancel_reason_code;
	}
	public void setOrder_cancel_reason_code(Integer order_cancel_reason_code) {
		this.order_cancel_reason_code = order_cancel_reason_code;
	}
	public String getOrder_cancel_description() {
		return order_cancel_description;
	}
	public void setOrder_cancel_description(String order_cancel_description) {
		this.order_cancel_description = order_cancel_description;
	}
	public Long getOrder_cancel_time() {
		return order_cancel_time;
	}
	public void setOrder_cancel_time(Long order_cancel_time) {
		this.order_cancel_time = order_cancel_time;
	}
	public String getOrder_cancel_notify_url() {
		return order_cancel_notify_url;
	}
	public void setOrder_cancel_notify_url(String order_cancel_notify_url) {
		this.order_cancel_notify_url = order_cancel_notify_url;
	}
     
     
}
