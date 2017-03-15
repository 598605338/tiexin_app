package com.linjia.web.query;

import com.linjia.base.query.Query;

public class JgPushCustomerQuery extends Query{

	//主键id
	private Integer id;
	//店铺id
	private String mall_code;
	//极光推送中设备id
	private String registration_id;
	//设备类型 1，安卓 2，ios
	private Integer os_type;
	//店铺类型(1，邻家；2，美团；3，百度)
	private Integer shop_type;
	
	//是否删除
	private boolean deleted;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRegistration_id() {
		return registration_id;
	}
	public void setRegistration_id(String registration_id) {
		this.registration_id = registration_id;
	}
	public Integer getOs_type() {
		return os_type;
	}
	public void setOs_type(Integer os_type) {
		this.os_type = os_type;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public Integer getShop_type() {
		return shop_type;
	}
	public void setShop_type(Integer shop_type) {
		this.shop_type = shop_type;
	}
	public String getMall_code() {
		return mall_code;
	}
	public void setMall_code(String mall_code) {
		this.mall_code = mall_code;
	}
	
}
