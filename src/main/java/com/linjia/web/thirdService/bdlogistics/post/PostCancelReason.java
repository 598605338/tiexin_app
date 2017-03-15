package com.linjia.web.thirdService.bdlogistics.post;

public class PostCancelReason {

	//百度物流订单号
	private Long order_id;
	//1:用户取消 2:商户取消 3:重复订单 4:配送超时 99:其他
	private Integer reason_id;
	//详细原因，reason_id是99时候必填
	private String reason_detail;
	//推送秒级时间戳
	private Long push_time;
	
	private Long app_id;
	private String sign;
	
	public Long getOrder_id() {
		return order_id;
	}
	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}
	public Integer getReason_id() {
		return reason_id;
	}
	public void setReason_id(Integer reason_id) {
		this.reason_id = reason_id;
	}
	public String getReason_detail() {
		return reason_detail;
	}
	public void setReason_detail(String reason_detail) {
		this.reason_detail = reason_detail;
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
	public Long getPush_time() {
		return push_time;
	}
	public void setPush_time(Long push_time) {
		this.push_time = push_time;
	}
	
	public PostCancelReason(){
		
	}
	/**
	 * @param order_id
	 * @param reason_id
	 * @param reason_detail
	 * @param push_time
	 */
	public PostCancelReason(Long order_id, Integer reason_id,
			String reason_detail, Long push_time) {
		super();
		this.order_id = order_id;
		this.reason_id = reason_id;
		this.reason_detail = reason_detail;
		this.push_time = push_time;
	}
	
}
