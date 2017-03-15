package com.linjia.web.uhd123.model;

import java.math.BigDecimal;

/** 
 * 退款信息
 * @author  lixinling: 
 * @date 2016年9月29日 下午2:13:49 
 * @version 1.0 
*/
public class Refund {
	
	/** 退款状态，取值范围:none(未退款)、refunded(已退款)* */
	private String state;
	
	/** 退运费* */
	private BigDecimal freight;
	
	/** 实际退货金额（包含运费）* */
	private BigDecimal amount;
	
	/** 平台退货结算金额* */
	private BigDecimal settle_amount;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getSettle_amount() {
		return settle_amount;
	}

	public void setSettle_amount(BigDecimal settle_amount) {
		this.settle_amount = settle_amount;
	}


}
