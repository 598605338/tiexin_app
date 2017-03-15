package com.linjia.web.thirdService.bdlogistics.post;

public class QueryIfCanSend {

	private String wl_shop_id;
	
	private String user_longitude;
	
	private String user_latitude;
	
	private Long app_id;
	private String sign;
	
	//推送秒级时间戳
	private Long push_time;

	public String getWl_shop_id() {
		return wl_shop_id;
	}

	public void setWl_shop_id(String wl_shop_id) {
		this.wl_shop_id = wl_shop_id;
	}

	public String getUser_longitude() {
		return user_longitude;
	}

	public void setUser_longitude(String user_longitude) {
		this.user_longitude = user_longitude;
	}

	public String getUser_latitude() {
		return user_latitude;
	}

	public void setUser_latitude(String user_latitude) {
		this.user_latitude = user_latitude;
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
	
	
}
