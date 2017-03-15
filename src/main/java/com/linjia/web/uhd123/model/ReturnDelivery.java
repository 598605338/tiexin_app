package com.linjia.web.uhd123.model;

/** 
 * 交付信息
 * @author  lixinling: 
 * @date 2016年9月29日 下午2:13:49 
 * @version 1.0 
*/
public class ReturnDelivery {

	/** 退货站点* */
	private Station station;

	/** 退货状态，取值范围:none(未退货)、customerShipped(客户已发货)、received(已收货)* */
	private String state;

	/** 退货人* */
	private Receiver returner;

	/** 承运商* */
	private Carrier carrier;

	/** 承运商备注* */
	private String carrier_note;


	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}


	public Carrier getCarrier() {
		return carrier;
	}

	public void setCarrier(Carrier carrier) {
		this.carrier = carrier;
	}


	public String getCarrier_note() {
		return carrier_note;
	}

	public void setCarrier_note(String carrier_note) {
		this.carrier_note = carrier_note;
	}

	public Receiver getReturner() {
		return returner;
	}

	public void setReturner(Receiver returner) {
		this.returner = returner;
	}

	
}
