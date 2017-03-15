package com.linjia.web.model;

import java.util.Date;

/*
 * 缺货记录表模型
 */
public class ProductLack {

	//主键id
	private int id;
	//门店code
	private String mall_code;
	//商铺id
	private String shop_id;
	//商品code
	private String p_code;
	//订单id
	private Long order_id;
	//创建时间
	private Date create_time;
	//删除标识
	private boolean deleted;
	
	//缺货平台(1,商城；2，美团；3，百度)
	private Integer platform;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getShop_id() {
		return shop_id;
	}
	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}
	public String getP_code() {
		return p_code;
	}
	public void setP_code(String p_code) {
		this.p_code = p_code;
	}
	public Long getOrder_id() {
		return order_id;
	}
	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public Integer getPlatform() {
		return platform;
	}
	public void setPlatform(Integer platform) {
		this.platform = platform;
	}
	public String getMall_code() {
		return mall_code;
	}
	public void setMall_code(String mall_code) {
		this.mall_code = mall_code;
	}

}
