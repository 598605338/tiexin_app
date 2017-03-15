package com.linjia.web.uhd123.model;

import java.math.BigDecimal;

/** 
 * 退款商品行
 * @author  lixinling: 
 * @date 2016年9月29日 下午2:13:49 
 * @version 1.0 
*/
public class RtnItem {
	
	/** 行号* */
	private String item_no;
	
	/** 商品标识* */
	private String sku_id;
	
	/** 商品标题* */
	private String item_title;
	
	/** 退货数量* */
	private BigDecimal quantity;
	
	/** 单价* */
	private BigDecimal unit_price;
	
	/** 退款金额* */
	private BigDecimal total;
	
	/** 退款金额（订单优惠后的金额)* */
	private BigDecimal refund_amount;
	
	/** 平台退货结算金额* */
	private BigDecimal settle_total;
	
	/** 平台退货结算单价* */
	private BigDecimal settle_price;

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

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(BigDecimal unit_price) {
		this.unit_price = unit_price;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getRefund_amount() {
		return refund_amount;
	}

	public void setRefund_amount(BigDecimal refund_amount) {
		this.refund_amount = refund_amount;
	}

	public BigDecimal getSettle_total() {
		return settle_total;
	}

	public void setSettle_total(BigDecimal settle_total) {
		this.settle_total = settle_total;
	}

	public BigDecimal getSettle_price() {
		return settle_price;
	}

	public void setSettle_price(BigDecimal settle_price) {
		this.settle_price = settle_price;
	}

}
