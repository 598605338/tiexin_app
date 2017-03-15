package com.linjia.web.uhd123.model;

import java.math.BigDecimal;

/** 
 * 商品行
 * @author  lixinling: 
 * @date 2016年9月29日 下午2:13:49 
 * @version 1.0 
*/
public class OrderItem {

	/** 订单行号* */
	private String item_no;

	/** 商品标识* */
	private String sku_id;

	/** 商品标题* */
	private String item_title;

	/** 规格* */
	private String spec;

	/** 条形码* */
	private String barcode;

	/** 商品优惠金额* */
	private BigDecimal discount_amount;

	/** 整单优惠分摊金额* */
	private BigDecimal apported_discount_amount;

	/** 商品销售价格（扣除优惠券前的价格）* */
	private BigDecimal unit_price;

	/** 数量* */
	private BigDecimal quantity;

	/** 商品金额，等于unit_price  *   quantity* */
	private BigDecimal total;

	/** 实付金额，等于total  - apported_discount_amount  -  discount_amount* */
	private BigDecimal pay_amount;

	/** 前端商品信息* */
	private FrontItem front;

	public String getItem_no() {
		return item_no;
	}

	public void setItem_no(String item_no) {
		this.item_no = item_no;
	}

	public String getSku_id() {
		return sku_id;
	}

	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public String getItem_title() {
		return item_title;
	}

	public void setItem_title(String item_title) {
		this.item_title = item_title;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public BigDecimal getDiscount_amount() {
		return discount_amount;
	}

	public void setDiscount_amount(BigDecimal discount_amount) {
		this.discount_amount = discount_amount;
	}

	public BigDecimal getApported_discount_amount() {
		return apported_discount_amount;
	}

	public void setApported_discount_amount(BigDecimal apported_discount_amount) {
		this.apported_discount_amount = apported_discount_amount;
	}

	public BigDecimal getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(BigDecimal unit_price) {
		this.unit_price = unit_price;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getPay_amount() {
		return pay_amount;
	}

	public void setPay_amount(BigDecimal pay_amount) {
		this.pay_amount = pay_amount;
	}

	public FrontItem getFront() {
		return front;
	}

	public void setFront(FrontItem front) {
		this.front = front;
	}

}
