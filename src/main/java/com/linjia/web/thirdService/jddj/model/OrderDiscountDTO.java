package com.linjia.web.thirdService.jddj.model;


/** 
 * 订单的优惠信息
 * @author  lixinling: 
 * @date 2017年1月10日 上午10:11:45 
 * @version 1.0 
*/
public class OrderDiscountDTO {
	/** 订单主表订单id* */
	private Long orderId;
	/** 调整单记录id（0:原单商品明细;非0:调整单id 或者 确认单id)* */
	private Long adjustId;
	/** 京东SKUID* */
	private Long skuId;
	/** 记录参加活动的sku数组* */
	private String skuIds;
	/** 优惠类型(1:优惠码;3:优惠劵;4:满减;5:满折;6:首单优惠)* */
	private Integer discountType;
	/** 小优惠类型(优惠码(1:满减;2:立减;3:满折;);优惠券(1:满减;2:立减;3:免运费劵;4:运费优惠N元;))* */
	private Integer discountDetailType;
	/** 优惠券编号* */
	private String discountCode;
	/** 优惠金额* */
	private Long discountPrice;
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getAdjustId() {
		return adjustId;
	}
	public void setAdjustId(Long adjustId) {
		this.adjustId = adjustId;
	}
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	public String getSkuIds() {
		return skuIds;
	}
	public void setSkuIds(String skuIds) {
		this.skuIds = skuIds;
	}
	public Integer getDiscountType() {
		return discountType;
	}
	public void setDiscountType(Integer discountType) {
		this.discountType = discountType;
	}
	public Integer getDiscountDetailType() {
		return discountDetailType;
	}
	public void setDiscountDetailType(Integer discountDetailType) {
		this.discountDetailType = discountDetailType;
	}
	public String getDiscountCode() {
		return discountCode;
	}
	public void setDiscountCode(String discountCode) {
		this.discountCode = discountCode;
	}
	public Long getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(Long discountPrice) {
		this.discountPrice = discountPrice;
	}
	
	
}
