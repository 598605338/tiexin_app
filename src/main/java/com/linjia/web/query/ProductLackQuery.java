package com.linjia.web.query;

import java.util.Date;

import com.linjia.base.query.Query;

/*
 * 缺货记录表模型
 */
public class ProductLackQuery extends Query {

	//主键id
	private int id;
	//商铺id
	private String shop_id;
	//商铺code
	private String p_code;
	//订单id
	private Long order_id;
	//创建时间
	private Date create_time;
	//删除标识
	private boolean deleted;
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

}

