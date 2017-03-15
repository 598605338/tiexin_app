package com.linjia.web.thirdService.dada.model;

import java.math.BigDecimal;

public class DadaReData {

	//状态位，ok表示成功
	private int status;	
	//订单运费，不包括小费
	private BigDecimal fee;	
	//配送距离，单位米，实际计算运费是将距离向上取整
	private float distance;	
	//在达达平台发布的订单号
	private String orderid;	
	//收货码
	private String finishCode;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public float getDistance() {
		return distance;
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getFinishCode() {
		return finishCode;
	}
	public void setFinishCode(String finishCode) {
		this.finishCode = finishCode;
	}	
	
	
}
