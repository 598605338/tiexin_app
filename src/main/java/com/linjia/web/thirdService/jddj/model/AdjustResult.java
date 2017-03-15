package com.linjia.web.thirdService.jddj.model;

import java.util.List;


/** 
 * 订单调整结果
 * @author  lixinling: 
 * @date 2017年2月27日 下午4:42:52 
 * @version 1.0 
*/
public class AdjustResult {
	
	/** 订单号* */
	private Long orderId;
	
	/** 调整计数* */
	private Integer adjustCount;
	
	/** 门店编号* */
	private String ProduceStationNo;
	
	/** 订单总金额* */
	private Long orderTotalMoney;
	
	/** 订单优惠总金额* */
	private Long orderDiscountMoney;
	
	/** 订单运费总金额* */
	private Long orderFreightMoney;
	
	/** 用户应付金额* */
	private Long orderBuyerPayableMoney;
	
	/** 用户未付金额 即商家需要向用户结清的尾款* */
	private Long orderVenderChargeMoney;
	
	/** 商品List集合* */
	private List<AdjustProduct> oaList;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getAdjustCount() {
		return adjustCount;
	}

	public void setAdjustCount(Integer adjustCount) {
		this.adjustCount = adjustCount;
	}

	public String getProduceStationNo() {
		return ProduceStationNo;
	}

	public void setProduceStationNo(String produceStationNo) {
		ProduceStationNo = produceStationNo;
	}

	public Long getOrderTotalMoney() {
		return orderTotalMoney;
	}

	public void setOrderTotalMoney(Long orderTotalMoney) {
		this.orderTotalMoney = orderTotalMoney;
	}

	public Long getOrderDiscountMoney() {
		return orderDiscountMoney;
	}

	public void setOrderDiscountMoney(Long orderDiscountMoney) {
		this.orderDiscountMoney = orderDiscountMoney;
	}

	public Long getOrderFreightMoney() {
		return orderFreightMoney;
	}

	public void setOrderFreightMoney(Long orderFreightMoney) {
		this.orderFreightMoney = orderFreightMoney;
	}

	public Long getOrderBuyerPayableMoney() {
		return orderBuyerPayableMoney;
	}

	public void setOrderBuyerPayableMoney(Long orderBuyerPayableMoney) {
		this.orderBuyerPayableMoney = orderBuyerPayableMoney;
	}

	public Long getOrderVenderChargeMoney() {
		return orderVenderChargeMoney;
	}

	public void setOrderVenderChargeMoney(Long orderVenderChargeMoney) {
		this.orderVenderChargeMoney = orderVenderChargeMoney;
	}

	public List<AdjustProduct> getOaList() {
		return oaList;
	}

	public void setOaList(List<AdjustProduct> oaList) {
		this.oaList = oaList;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("orderId:"+this.getOrderId()).append(",")
		.append("adjustCount:"+this.getAdjustCount()).append(",")
		.append("ProduceStationNo:"+this.getProduceStationNo()).append(",")
		.append("orderTotalMoney:"+this.getOrderTotalMoney()).append(",")
		.append("orderDiscountMoney:"+this.getOrderDiscountMoney()).append(",")
		.append("orderFreightMoney:"+this.getOrderFreightMoney()).append(",")
		.append("orderBuyerPayableMoney:"+this.getOrderBuyerPayableMoney()).append(",")
		.append("orderVenderChargeMoney:"+this.getOrderVenderChargeMoney());
		return sb.toString();
	}
	
	
}
