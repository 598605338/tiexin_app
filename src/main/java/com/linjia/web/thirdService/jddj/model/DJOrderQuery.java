package com.linjia.web.thirdService.jddj.model;

import java.util.Date;


/** 
 * 
 * @author  lixinling: 
 * @date 2017年1月9日 下午3:20:43 
 * @version 1.0 
*/
public class DJOrderQuery {

	/** 当前页数,默认：1* */
	private Long pageNo;
	/** 每页条数,默认：20，超过100也只返回100条* */
	private Integer pageSize;
	/** 订单号* */
	private Long orderId;
	/** 客户名* */
	private String buyerFullName;
	/** 客户名（模糊查询）* */
	private String buyerFullName_like;
	/** 手机号* */
	private String buyerMobile;
	/** 订单支付类型* */
	private Integer orderPayType;
	/** 买家账号* */
	private Integer buyerPin;
	/** 订单开始时间(开始)* */
	private Date orderStartTime_begin;
	/** 订单开始时间(结束)* */
	private Date orderStartTime_end;
	/** 购买成交时间-支付(开始)* */
	private Date orderPurchaseTime_begin;
	/** 购买成交时间-支付(结束)* */
	private Date orderPurchaseTime_end;
	/** 妥投时间(开始)* */
	private Date deliveryConfirmTime_begin;
	/** 妥投时间(结束)* */
	private Date deliveryConfirmTime_end;
	/** 订单关闭时间(开始)* */
	private Date orderCloseTime_begin;
	/** 订单关闭时间(结束)* */
	private Date orderCloseTime_end;
	/** 订单取消时间(开始)* */
	private Date orderCancelTime_begin;
	/** 订单取消时间(结束)* */
	private Date orderCancelTime_end;
	/** 订单状态 实物类：20010:锁定，20020:订单取消，20040:超时未支付系统取消，31000:等待付款，31020:已付款，41000:待处理，32000:等待出库，33040:配送中，33060:已妥投 34000:京东已收款，90000:订单完成 服务类： 20010:锁定 20020:用户取消 20040:系统取消 31000:等待付款 31020:已付款 32000：等待出库 34000:京东已收款 34010:商家已收款 41000:待处理 41010:已接单 51010:开始服务 51020:结束服务 90000:服务完成 美食类:20010:锁定 20020:用户取消 20040:系统取消 41000:待接单 41010:已接单 33040:配送中 33060:已妥投* */
	private Integer orderStatus;
	/** 订单状态* */
	private Integer orderStatus_list;
	/** 城市* */
	private Integer buyerCity_list;
	/** 运单号* */
	private Integer deliveryBillNo;
	/** 订单类型 10000:从门店出的订单 20000:服务订单 30000:美食订单 40000:厂商直送类型* */
	private Integer orderType;
	/** 配送门店编号* */
	private Integer deliveryStationNo;
	public Long getPageNo() {
		return pageNo;
	}
	public void setPageNo(Long pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getBuyerFullName() {
		return buyerFullName;
	}
	public void setBuyerFullName(String buyerFullName) {
		this.buyerFullName = buyerFullName;
	}
	public String getBuyerFullName_like() {
		return buyerFullName_like;
	}
	public void setBuyerFullName_like(String buyerFullName_like) {
		this.buyerFullName_like = buyerFullName_like;
	}
	public String getBuyerMobile() {
		return buyerMobile;
	}
	public void setBuyerMobile(String buyerMobile) {
		this.buyerMobile = buyerMobile;
	}
	public Integer getOrderPayType() {
		return orderPayType;
	}
	public void setOrderPayType(Integer orderPayType) {
		this.orderPayType = orderPayType;
	}
	public Integer getBuyerPin() {
		return buyerPin;
	}
	public void setBuyerPin(Integer buyerPin) {
		this.buyerPin = buyerPin;
	}
	public Date getOrderStartTime_begin() {
		return orderStartTime_begin;
	}
	public void setOrderStartTime_begin(Date orderStartTime_begin) {
		this.orderStartTime_begin = orderStartTime_begin;
	}
	public Date getOrderStartTime_end() {
		return orderStartTime_end;
	}
	public void setOrderStartTime_end(Date orderStartTime_end) {
		this.orderStartTime_end = orderStartTime_end;
	}
	public Date getOrderPurchaseTime_begin() {
		return orderPurchaseTime_begin;
	}
	public void setOrderPurchaseTime_begin(Date orderPurchaseTime_begin) {
		this.orderPurchaseTime_begin = orderPurchaseTime_begin;
	}
	public Date getOrderPurchaseTime_end() {
		return orderPurchaseTime_end;
	}
	public void setOrderPurchaseTime_end(Date orderPurchaseTime_end) {
		this.orderPurchaseTime_end = orderPurchaseTime_end;
	}
	public Date getDeliveryConfirmTime_begin() {
		return deliveryConfirmTime_begin;
	}
	public void setDeliveryConfirmTime_begin(Date deliveryConfirmTime_begin) {
		this.deliveryConfirmTime_begin = deliveryConfirmTime_begin;
	}
	public Date getDeliveryConfirmTime_end() {
		return deliveryConfirmTime_end;
	}
	public void setDeliveryConfirmTime_end(Date deliveryConfirmTime_end) {
		this.deliveryConfirmTime_end = deliveryConfirmTime_end;
	}
	public Date getOrderCloseTime_begin() {
		return orderCloseTime_begin;
	}
	public void setOrderCloseTime_begin(Date orderCloseTime_begin) {
		this.orderCloseTime_begin = orderCloseTime_begin;
	}
	public Date getOrderCloseTime_end() {
		return orderCloseTime_end;
	}
	public void setOrderCloseTime_end(Date orderCloseTime_end) {
		this.orderCloseTime_end = orderCloseTime_end;
	}
	public Date getOrderCancelTime_begin() {
		return orderCancelTime_begin;
	}
	public void setOrderCancelTime_begin(Date orderCancelTime_begin) {
		this.orderCancelTime_begin = orderCancelTime_begin;
	}
	public Date getOrderCancelTime_end() {
		return orderCancelTime_end;
	}
	public void setOrderCancelTime_end(Date orderCancelTime_end) {
		this.orderCancelTime_end = orderCancelTime_end;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Integer getOrderStatus_list() {
		return orderStatus_list;
	}
	public void setOrderStatus_list(Integer orderStatus_list) {
		this.orderStatus_list = orderStatus_list;
	}
	public Integer getBuyerCity_list() {
		return buyerCity_list;
	}
	public void setBuyerCity_list(Integer buyerCity_list) {
		this.buyerCity_list = buyerCity_list;
	}
	public Integer getDeliveryBillNo() {
		return deliveryBillNo;
	}
	public void setDeliveryBillNo(Integer deliveryBillNo) {
		this.deliveryBillNo = deliveryBillNo;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public Integer getDeliveryStationNo() {
		return deliveryStationNo;
	}
	public void setDeliveryStationNo(Integer deliveryStationNo) {
		this.deliveryStationNo = deliveryStationNo;
	}
	
	
}
