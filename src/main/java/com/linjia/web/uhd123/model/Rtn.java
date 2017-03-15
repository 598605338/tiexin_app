package com.linjia.web.uhd123.model;

/** 
 * 退换货单
 * @author  lixinling: 
 * @date 2016年9月29日 下午2:13:49 
 * @version 1.0 
*/
public class Rtn {
	private String id;
	private Shop shop;
	
	/** 退货单类型,取值范围：refund(仅退款)、refundAndType(退款退货)；为空默认为退款退货* */
	private String type;
	
	/** 退货单状态,取值范围：confirmed(已生成)、finished(已完成)、canceled(已取消),为空时取默认值confirmed* */
	private String state;
	
	/** 客户* */
	private Customer customer;
	
	/** 客户留言* */
	private String customer_note;
	
	/** 原订单号* */
	private String order_id;
	
	/** 审批状态，取值范围:none(未审核)、serviceApproved(客服已审核)、serviceRefuse(客服已拒绝)、financeApproved(财务已审核)、financeRefuse(财务已拒绝)* */
	private String approve_state;
	
	/** 退货信息* */
	private ReturnDelivery delivery;
	
	/** 退款信息* */
	private Refund refund;
	
	/** 平台信息* */
	private FrontRtn front;
	
	/** 商品行* */
	private RtnItem[] items;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getCustomer_note() {
		return customer_note;
	}

	public void setCustomer_note(String customer_note) {
		this.customer_note = customer_note;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getApprove_state() {
		return approve_state;
	}

	public void setApprove_state(String approve_state) {
		this.approve_state = approve_state;
	}

	public ReturnDelivery getDelivery() {
		return delivery;
	}

	public void setDelivery(ReturnDelivery delivery) {
		this.delivery = delivery;
	}

	public Refund getRefund() {
		return refund;
	}

	public void setRefund(Refund refund) {
		this.refund = refund;
	}

	public FrontRtn getFront() {
		return front;
	}

	public void setFront(FrontRtn front) {
		this.front = front;
	}

	public RtnItem[] getItems() {
		return items;
	}

	public void setItems(RtnItem[] items) {
		this.items = items;
	}



}
