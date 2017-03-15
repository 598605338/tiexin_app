package com.linjia.web.uhd123.model;

/** 
 * 确认退货状态
 * @author  lixinling: 
 * @date 2016年10月18日 下午2:13:49 
 * @version 1.0 
*/
public class ReturnRequest {
	
	/** 承运商* */
	private Carrier carrier;
	
	/** 退货状态，取值范围:none(未退货)、customerShipped(客户已发货)、received(已收货)* */
	private String return_state;
	
	/** 承运商备注* */
	private String carrier_note;

	public Carrier getCarrier() {
		return carrier;
	}

	public void setCarrier(Carrier carrier) {
		this.carrier = carrier;
	}

	public String getReturn_state() {
		return return_state;
	}

	public void setReturn_state(String return_state) {
		this.return_state = return_state;
	}

	public String getCarrier_note() {
		return carrier_note;
	}

	public void setCarrier_note(String carrier_note) {
		this.carrier_note = carrier_note;
	}


}
