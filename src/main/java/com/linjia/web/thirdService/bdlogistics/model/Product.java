package com.linjia.web.thirdService.bdlogistics.model;

import java.util.ArrayList;

public class Product {
	// 套餐(菜品)ID
	private int dish_id;
	// 套餐(菜品)名称
	private String dish_name;
	// 套餐(菜品)单价
	private String dish_unit_price;
	// 套餐(菜品)数量
	private Long dish_number;
	// 套餐(菜品)总价
	private String dish_total_price;
	// 套餐(菜品)单位；例如：份
	private String unit_name;
	private String packge_box_price;
	private Long packge_box_number;
	// 套餐(菜品)中单品列表，是一个多层结构；详见dish_details层级参数表
	private ArrayList<DishDetail> dish_details;

	public Product() {
	}

	public Product(String dish_name, String dish_unit_price, Long dish_number,
			String dish_total_price, String unit_name, String packge_box_price,
			Long packge_box_number) {
		this.dish_name = dish_name;
		this.dish_unit_price = dish_unit_price;
		this.dish_number = dish_number;
		this.dish_total_price = dish_total_price;
		this.unit_name = unit_name;
		this.packge_box_price = packge_box_price;
		this.packge_box_number = packge_box_number;
	}

	public String getDish_name() {
		return dish_name;
	}

	public void setDish_name(String dish_name) {
		this.dish_name = dish_name;
	}

	public String getDish_unit_price() {
		return dish_unit_price;
	}

	public void setDish_unit_price(String dish_unit_price) {
		this.dish_unit_price = dish_unit_price;
	}

	public Long getDish_number() {
		return dish_number;
	}

	public void setDish_number(Long dish_number) {
		this.dish_number = dish_number;
	}

	public String getDish_total_price() {
		return dish_total_price;
	}

	public void setDish_total_price(String dish_total_price) {
		this.dish_total_price = dish_total_price;
	}

	public String getUnit_name() {
		return unit_name;
	}

	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}

	public String getPackge_box_price() {
		return packge_box_price;
	}

	public void setPackge_box_price(String packge_box_price) {
		this.packge_box_price = packge_box_price;
	}

	public Long getPackge_box_number() {
		return packge_box_number;
	}

	public void setPackge_box_number(Long packge_box_number) {
		this.packge_box_number = packge_box_number;
	}

	public ArrayList<DishDetail> getDish_details() {
		return dish_details;
	}

	public void setDish_details(ArrayList<DishDetail> dish_details) {
		this.dish_details = dish_details;
	}

	public int getDish_id() {
		return dish_id;
	}

	public void setDish_id(int dish_id) {
		this.dish_id = dish_id;
	}
	
}
