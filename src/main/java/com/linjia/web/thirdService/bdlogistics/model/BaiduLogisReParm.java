package com.linjia.web.thirdService.bdlogistics.model;

public class BaiduLogisReParm {

	//发送时间,值为接口发送时的秒级时间戳；
	private Integer push_time;
	
	private String app_id;
	
	private String sign;

	public Integer getPush_time() {
		return push_time;
	}

	public void setPush_time(Integer push_time) {
		this.push_time = push_time;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
}
