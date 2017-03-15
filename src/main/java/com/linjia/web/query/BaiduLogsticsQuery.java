package com.linjia.web.query;

import java.util.Date;

import com.linjia.base.query.Query;
import com.linjia.web.thirdService.baidu.model.Order;
import com.linjia.web.thirdService.baidu.model.Shop;
import com.linjia.web.thirdService.baidu.model.User;


public class BaiduLogsticsQuery extends Query{

	//id
	private int id;
	
    //订单id
	private String order_id;
	
	//合作方账号
	private String source;
	
	//商户信息
	private Shop shop;
	
	//订单信息
	private Order order;
	
	//顾客信息
	private User user;
	
	//
	private String products;
	
	//优惠信息
	private String discount;

	//
	private String ticket;
	
	//
	private Long timestamp;
	
	//
	private String version;
	
	//
	private Date createtime;
	
	private Integer deleted;
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

}
