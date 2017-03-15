package com.linjia.web.uhd123.model;

/** 
 * 订单占货
 * @author  lixinling: 
 * @date 2016年9月29日 下午2:13:49 
 * @version 1.0 
*/
public class OrderStockLock {
	
	/** 订单号* */
	private String order_id;
	
	/** 平台id* */
	private String platform_id;
	
	/** 平台商家id* */
	private String shop_id;
	
	/** 平台商家名称* */
	private String shop_name;
	
	/** 门店id* */
	private String store_id;
	
	/** 订单商品锁库存明细* */
	private OrderDataItem[] items;

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getPlatform_id() {
		return platform_id;
	}

	public void setPlatform_id(String platform_id) {
		this.platform_id = platform_id;
	}

	public String getShop_id() {
		return shop_id;
	}

	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public OrderDataItem[] getItems() {
		return items;
	}

	public void setItems(OrderDataItem[] items) {
		this.items = items;
	}



}
