package com.linjia.web.uhd123.model;

/** 
 * 更新订单配送状态
 * @author  lixinling: 
 * @date 2016年9月29日 下午2:13:49 
 * @version 1.0 
*/
public class DeliverRequest {
	
	/** 配送状态，取值范围:none(未配送)、shipped(已发货)、signed(已妥投)、refused(已拒收)、redeliver(重投)* */
	private String delivery_state;
	
	/** 平台订单最后修改时间,非空时将更新,格式为：yyyy-MM-dd HH:mm:ss* */
	private String front_modified;
	
	/** 承运单号，非空时将更新* */
	private String waybill_id;
	
	/** 承运商，非空时将更新* */
	private Carrier carrier;
	
	/** 承运商备注* */
	private String carrier_note;
	
	/** 门店作业,取值范围：shipping（已集货）、handover（已交接）、cancelShipping(取消集货)* */
	private String operation;
	
	/** 门店作业状态,取值范围：is null（待集货）、none（空）、shipping（已集货）、handover（已交接）* */
	private String operation_state;

	public String getDelivery_state() {
		return delivery_state;
	}

	public void setDelivery_state(String delivery_state) {
		this.delivery_state = delivery_state;
	}

	public String getFront_modified() {
		return front_modified;
	}

	public void setFront_modified(String front_modified) {
		this.front_modified = front_modified;
	}

	public String getWaybill_id() {
		return waybill_id;
	}

	public void setWaybill_id(String waybill_id) {
		this.waybill_id = waybill_id;
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

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getOperation_state() {
		return operation_state;
	}

	public void setOperation_state(String operation_state) {
		this.operation_state = operation_state;
	}

	
}
