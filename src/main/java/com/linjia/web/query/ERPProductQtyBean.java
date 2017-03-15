package com.linjia.web.query;

import java.math.BigDecimal;

/** 
 * 操作请求ERP库存Bean
 * @author  lixinling: 
 * @date 2016年8月4日 下午5:12:38 
 * @version 1.0 
*/
public class ERPProductQtyBean {
	private String skuId;
	private int qty;
	private BigDecimal price;
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	
}
