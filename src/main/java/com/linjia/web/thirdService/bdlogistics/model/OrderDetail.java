package com.linjia.web.thirdService.bdlogistics.model;

import java.util.ArrayList;

public class OrderDetail {
	// 餐盒费,保留两位小数；默认0.00
	private String box_price;
	// 配送费,保留两位小数；默认0.00
	private String send_price;
	// 菜品总价，保留两位小数
	private String product_price;
	// 菜品详情，是一个多层结构；详见products层级参数表
	private ArrayList<Product> products;

	public OrderDetail() {
	}

	public OrderDetail(String box_price, String send_price, String product_price) {
		this.box_price = box_price;
		this.send_price = send_price;
		this.product_price = product_price;
	}

	public String getBox_price() {
		return box_price;
	}

	public void setBox_price(String box_price) {
		this.box_price = box_price;
	}

	public String getSend_price() {
		return send_price;
	}

	public void setSend_price(String send_price) {
		this.send_price = send_price;
	}

	public String getProduct_price() {
		return product_price;
	}

	public void setProduct_price(String product_price) {
		this.product_price = product_price;
	}

	public ArrayList<Product> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}
}
