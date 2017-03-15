package com.linjia.web.thirdService.bdlogistics.model;

public class DishDetail {
	// 单品名称
	private String name;
	// 单品数量
	private Long number;
	// 单品单位
	private String unit_name;

	public DishDetail() {
	}

	public DishDetail(String name, Long number, String unit_name) {
		this.name = name;
		this.number = number;
		this.unit_name = unit_name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public String getUnit_name() {
		return unit_name;
	}

	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}
}
