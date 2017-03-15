package com.linjia.web.thirdService.bdlogistics.post;

public class ReciveFeachOrder {

	//百度物流订单号
	private Long order_id;
	//商户订单号
	private String out_order_id;
	//骑士接单秒级时间戳
	private Long confirm_time;
	//推送秒级时间戳
	private Long push_time;
	
	private String delivery_phone;
	
	private String delivery_name;
		
	private Long app_id;
	private String sign;
	
	public Long getOrder_id() {
		return order_id;
	}
	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}
	public String getOut_order_id() {
		return out_order_id;
	}
	public void setOut_order_id(String out_order_id) {
		this.out_order_id = out_order_id;
	}
	public Long getConfirm_time() {
		return confirm_time;
	}
	public void setConfirm_time(Long confirm_time) {
		this.confirm_time = confirm_time;
	}
	public Long getPush_time() {
		return push_time;
	}
	public void setPush_time(Long push_time) {
		this.push_time = push_time;
	}
	public Long getApp_id() {
		return app_id;
	}
	public void setApp_id(Long app_id) {
		this.app_id = app_id;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public ReciveFeachOrder(){
		
	}
	
	
	public String getDelivery_phone() {
		return delivery_phone;
	}
	public void setDelivery_phone(String delivery_phone) {
		this.delivery_phone = delivery_phone;
	}
	
	
	public String getDelivery_name() {
		return delivery_name;
	}
	public void setDelivery_name(String delivery_name) {
		this.delivery_name = delivery_name;
	}
	/**
	 * @param order_id
	 * @param out_order_id
	 * @param confirm_time
	 * @param push_time
	 */
	public ReciveFeachOrder(Long order_id, String out_order_id,
			Long confirm_time, Long push_time,String delivery_phone,String delivery_name) {
		super();
		this.order_id = order_id;
		this.out_order_id = out_order_id;
		this.confirm_time = confirm_time;
		this.push_time = push_time;
		this.delivery_phone = delivery_phone;
		this.delivery_name = delivery_name;
	}
	
}
