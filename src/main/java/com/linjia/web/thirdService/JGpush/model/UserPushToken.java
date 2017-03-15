package com.linjia.web.thirdService.JGpush.model;

/**
 * 
 * @author xiangshouyi
 *
 */
public class UserPushToken {

	private String JpushToken;
	
	private int os;
	
	private int userId;

	public int getOs() {
		return os;
	}

	public void setOs(int os) {
		this.os = os;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getJpushToken() {
		return JpushToken;
	}

	public void setJpushToken(String jpushToken) {
		JpushToken = jpushToken;
	}
	
}
