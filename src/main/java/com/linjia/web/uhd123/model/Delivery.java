package com.linjia.web.uhd123.model;

/** 
 * 交付信息
 * @author  lixinling: 
 * @date 2016年9月29日 下午2:13:49 
 * @version 1.0 
*/
public class Delivery {

	/** 交付方式，默认取值:deliver。取值范围:selftake(到店自提)、deliver(送货上门)* */
	private String type = "deliver";

	/** 配送门店* */
	private Station station;

	/** 配送状态，默认取值为:none。取值范围:none(未配送)、stocked(已备货)、shipped(已发货)、signed(已妥投)、refused(已拒收)、redeliver(重投)* */
	private String state = "none";

	/** 收货人信息* */
	private Receiver receiver;

	/** 承运商* */
	private Carrier carrier;

	/** 运单号* */
	private String waybill_id;

	/** 承运商备注* */
	private String carrier_note;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

	public Receiver getReceiver() {
		return receiver;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	public Carrier getCarrier() {
		return carrier;
	}

	public void setCarrier(Carrier carrier) {
		this.carrier = carrier;
	}

	public String getWaybill_id() {
		return waybill_id;
	}

	public void setWaybill_id(String waybill_id) {
		this.waybill_id = waybill_id;
	}

	public String getCarrier_note() {
		return carrier_note;
	}

	public void setCarrier_note(String carrier_note) {
		this.carrier_note = carrier_note;
	}

}
