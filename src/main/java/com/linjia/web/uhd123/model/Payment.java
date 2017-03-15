package com.linjia.web.uhd123.model;

import java.math.BigDecimal;
import java.util.Date;

/** 
 * 支付信息
 * @author  lixinling: 
 * @date 2016年10月11日 下午2:13:49 
 * @version 1.0 
*/
public class Payment {
	
	/** 付款类型，默认取值:online。取值范围：cod(货到付款)、online(在线支付)、earnest(预付)* */
	private String type = "online";
	
	/** 付款状态，默认取值:notPay。取值范围：notPay(未付款)、earnest(已付定金)、paid(已付全款)* */
	private String state = "paid";
	
	/** 付款时间,格式为：yyyy-MM-dd HH:mm:ss* */
	private String pay_time;
	
	/** 付款人* */
	private Payee payee;
	
	/** 支付账单号* */
	private String pay_bill_id;
	
	/** 运费* */
	private BigDecimal freight;
	
	/** 订单总金额* */
	private BigDecimal amount;
	
	/** COD金额* */
	private BigDecimal cod_amount;
	
	/** 订金金额* */
	private BigDecimal earnest_amount;
	
	/** 支付方式详情* */
	private PaymentDetail[] details;

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

	public String getPay_time() {
		return pay_time;
	}

	public void setPay_time(String pay_time) {
		this.pay_time = pay_time;
	}

	public Payee getPayee() {
		return payee;
	}

	public void setPayee(Payee payee) {
		this.payee = payee;
	}

	public String getPay_bill_id() {
		return pay_bill_id;
	}

	public void setPay_bill_id(String pay_bill_id) {
		this.pay_bill_id = pay_bill_id;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public BigDecimal getCod_amount() {
		return cod_amount;
	}

	public void setCod_amount(BigDecimal cod_amount) {
		this.cod_amount = cod_amount;
	}

	public BigDecimal getEarnest_amount() {
		return earnest_amount;
	}

	public void setEarnest_amount(BigDecimal earnest_amount) {
		this.earnest_amount = earnest_amount;
	}

	public PaymentDetail[] getDetails() {
		return details;
	}

	public void setDetails(PaymentDetail[] details) {
		this.details = details;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}



}
