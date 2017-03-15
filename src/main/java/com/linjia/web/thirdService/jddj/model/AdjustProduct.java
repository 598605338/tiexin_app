package com.linjia.web.thirdService.jddj.model;


/** 
 * 调整单商品信息
 * @author  lixinling: 
 * @date 2017年2月27日 下午4:47:31 
 * @version 1.0 
*/
public class AdjustProduct {
	
	
	/** 商品ID* */
	private Long skuId;
	/** 商品数量* */
	private Integer skuCount;
	/** 商品的名称+sku规格* */
	private String skuName;
	/** 京东销售价格* */
	private Long skuJdPrice;
	/** POP的skuid* */
	private String outSkuId;
	/** POP的upc* */
	private String upc;
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	public Integer getSkuCount() {
		return skuCount;
	}
	public void setSkuCount(Integer skuCount) {
		this.skuCount = skuCount;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public Long getSkuJdPrice() {
		return skuJdPrice;
	}
	public void setSkuJdPrice(Long skuJdPrice) {
		this.skuJdPrice = skuJdPrice;
	}
	public String getOutSkuId() {
		return outSkuId;
	}
	public void setOutSkuId(String outSkuId) {
		this.outSkuId = outSkuId;
	}
	public String getUpc() {
		return upc;
	}
	public void setUpc(String upc) {
		this.upc = upc;
	}
	
	
}
