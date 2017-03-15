package com.linjia.web.uhd123.model;

/** 
 * 前端商品信息
 * @author  lixinling: 
 * @date 2016年9月29日 下午2:13:49 
 * @version 1.0 
*/
public class FrontItem {
	
	/** 平台SKU ID* */
	private String sku_id;
	
	/** 平台订单行ID* */
	private String item_id;

	public String getSku_id() {
		return sku_id;
	}

	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}


}
