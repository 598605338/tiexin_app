package com.linjia.web.thirdService.bdlogistics.post;

public class ReciveSendOrder {

	//百度物流订单号
	private Long order_id;
	//商户订单号
	private String out_order_id;
	//取餐时间，秒级时间戳
	private Long pickup_time;
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
	
	public Long getPickup_time() {
		return pickup_time;
	}
	public void setPickup_time(Long pickup_time) {
		this.pickup_time = pickup_time;
	}
	/**
	 * @param order_id
	 * @param out_order_id
	 * @param pickup_time
	 * @param push_time
	 */
	public ReciveSendOrder(Long order_id, String out_order_id,
			Long pickup_time, Long push_time,String delivery_phone,String delivery_name) {
		super();
		this.order_id = order_id;
		this.out_order_id = out_order_id;
		this.pickup_time = pickup_time;
		this.push_time = push_time;
		this.delivery_phone = delivery_phone;
		this.delivery_name = delivery_name;
	}
	public ReciveSendOrder(){
		
	}
}
