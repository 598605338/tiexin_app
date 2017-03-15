package com.linjia.web.thirdService.bea.model;

public class TokenData {

	private String access_token;
	
	private Long expire_time;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public Long getExpire_time() {
		return expire_time;
	}

	public void setExpire_time(Long expire_time) {
		this.expire_time = expire_time;
	}
	
	
}
