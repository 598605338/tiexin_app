package com.linjia.web.uhd123.model;

import java.math.BigDecimal;

/** 
 * @author  lixinling: 
 * @date 2016年9月29日 下午2:12:19 
 * @version 1.0 
*/
public class Order {

	/** 由系统自动生成，不需要填写* */
	private String id;

	/** 店铺* */
	private Shop shop;

	/** 订单状态,默认取值为:confirmed。取值范围:confirmed(已确认)、prepared(已预处理)、approved(已审核)、delivering( 配送中)、delivered(配送完成)、canceled(已取消)* */
	private String state = "confirmed";

	/** 审核子状态,取值范围:none(未审核)、serviceApproved(客服已审核)、financeApproved(财务已审核)、warehouseApproved(物流已审核)、carrierApproved(承运商已审核)* */
	private String approve_state;

	/** 商品行数* */
	private int item_count;

	/** 商品总数* */
	private int sku_quantity;

	/** 客户* */
	private Customer customer;

	/** 客户备注* */
	private String customer_note;

	/** 商家备注* */
	private String seller_note;

	/** 实际销售金额(扣除所有优惠)，等于items中pay_amount之和* */
	private BigDecimal real_total;

	/** 取消类型,取值为stationRefuse（门店拒接）、customerCancel（客户取消）* */
	private String cancel_type;

	/** 取消原因* */
	private String cancel_reason;

	/** 支付信息* */
	private Payment payment;

	/** 交付信息* */
	private Delivery delivery;

	/** 平台信息* */
	private FrontOrder front;

	/** 商品行* */
	private OrderItem[] items;

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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getApprove_state() {
		return approve_state;
	}

	public void setApprove_state(String approve_state) {
		this.approve_state = approve_state;
	}

	public int getItem_count() {
		return item_count;
	}

	public void setItem_count(int item_count) {
		this.item_count = item_count;
	}

	public int getSku_quantity() {
		return sku_quantity;
	}

	public void setSku_quantity(int sku_quantity) {
		this.sku_quantity = sku_quantity;
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

	public String getSeller_note() {
		return seller_note;
	}

	public void setSeller_note(String seller_note) {
		this.seller_note = seller_note;
	}

	public BigDecimal getReal_total() {
		return real_total;
	}

	public void setReal_total(BigDecimal real_total) {
		this.real_total = real_total;
	}

	public String getCancel_type() {
		return cancel_type;
	}

	public void setCancel_type(String cancel_type) {
		this.cancel_type = cancel_type;
	}

	public String getCancel_reason() {
		return cancel_reason;
	}

	public void setCancel_reason(String cancel_reason) {
		this.cancel_reason = cancel_reason;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public FrontOrder getFront() {
		return front;
	}

	public void setFront(FrontOrder front) {
		this.front = front;
	}

	public OrderItem[] getItems() {
		return items;
	}

	public void setItems(OrderItem[] items) {
		this.items = items;
	}

}
