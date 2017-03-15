package com.linjia.web.thirdService.bea.model;

public class ElemeCallEntity {
	
	private String app_id;
	
	private Integer salt;
	
	private String signature;
	
	private ElemeCallData data;

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public Integer getSalt() {
		return salt;
	}

	public void setSalt(Integer salt) {
		this.salt = salt;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public ElemeCallData getData() {
		return data;
	}

	public void setData(ElemeCallData data) {
		this.data = data;
	}

}
