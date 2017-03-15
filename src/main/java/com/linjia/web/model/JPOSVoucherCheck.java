package com.linjia.web.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * JPOS券使用model
 * @author lixinling
 * 2016年9月22日 下午4:20:55
 */
@XmlRootElement(name="model")
public class JPOSVoucherCheck {
	/** 券号* */
	private String voucherNumber;
	/** 门店* */
	private String storeNumber;
	/** 账号* */
	private String requestAccount;
	/** 订单金额* */
	private String billAmount;
	/** 签名数据* */
	private String signParam;
	/** 核销请求ID* */
	private String tranId;

	public String getVoucherNumber() {
		return voucherNumber;
	}

	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}

	public String getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}

	public String getRequestAccount() {
		return requestAccount;
	}

	public void setRequestAccount(String requestAccount) {
		this.requestAccount = requestAccount;
	}

	public String getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(String billAmount) {
		this.billAmount = billAmount;
	}

	public String getSignParam() {
		return signParam;
	}

	public void setSignParam(String signParam) {
		this.signParam = signParam;
	}

	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

}