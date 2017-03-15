package com.linjia.web.model;

import java.math.BigDecimal;

public class SumOrder {

	//统计数量
	private int orderNums;
	
	//统计来源(1,邻家;2,美团;3,百度;4京东到家)
	private int orderSource;
	
	//统计金额
	private BigDecimal total_price;
	
	//统计类型(1,进行中;2,已完成;3,已失效;4,全部)
	private int sumType;
	
	//统计时间
	private String create_time;

	public int getOrderNums() {
		return orderNums;
	}

	public void setOrderNums(int orderNums) {
		this.orderNums = orderNums;
	}

	public int getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(int orderSource) {
		this.orderSource = orderSource;
	}

	public BigDecimal getTotal_price() {
		return total_price;
	}

	public void setTotal_price(BigDecimal total_price) {
		this.total_price = total_price;
	}

	public int getSumType() {
		return sumType;
	}

	public void setSumType(int sumType) {
		this.sumType = sumType;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	
}
