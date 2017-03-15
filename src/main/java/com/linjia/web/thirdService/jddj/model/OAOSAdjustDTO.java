package com.linjia.web.thirdService.jddj.model;


/** 
 * 调整订单后的商品明细
 * @author  lixinling: 
 * @date 2017年2月27日 下午2:07:50 
 * @version 1.0 
*/
public class OAOSAdjustDTO {

	/** 到家商品编号* */
	private Long skuId;
	
	/** 商家商品编号* */
	private String outSkuId;
	
	/** 商品数量* */
	private Integer skuCount;

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public String getOutSkuId() {
		return outSkuId;
	}

	public void setOutSkuId(String outSkuId) {
		this.outSkuId = outSkuId;
	}

	public Integer getSkuCount() {
		return skuCount;
	}

	public void setSkuCount(Integer skuCount) {
		this.skuCount = skuCount;
	}
	
	
}
