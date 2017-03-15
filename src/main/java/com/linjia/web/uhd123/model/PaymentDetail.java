package com.linjia.web.uhd123.model;

import java.math.BigDecimal;

/** 
 * 支付方式详情
 * @author  lixinling: 
 * @date 2016年9月29日 下午2:13:49 
 * @version 1.0 
*/
public class PaymentDetail {
	
	/** 支付方式名称* */
	private String name;
	
	/** 金额* */
	private BigDecimal amount;
	
	/** 是否缴款 ,此字段用于刻画收款人收的钱是否需要上缴，如收的是现金就需要上缴，刷卡则就不需要上缴* */
	private boolean cashier = false;
	
	/** 账号* */
	private String account_no;
	
	/** 账号名称* */
	private String account_name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public boolean isCashier() {
		return cashier;
	}

	public void setCashier(boolean cashier) {
		this.cashier = cashier;
	}

	public String getAccount_no() {
		return account_no;
	}

	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

}
