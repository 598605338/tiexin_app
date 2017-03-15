package com.linjia.web.uhd123.model;

/** 
 * 订单商品锁库存明细
 * @author  lixinling: 
 * @date 2016年9月29日 下午2:13:49 
 * @version 1.0 
*/
public class OrderDataItem {
	
	/** 商品ID* */
	private String sku_id;
	
	/** 数量* */
	private String qty;

	public String getSku_id() {
		return sku_id;
	}

	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}


}
