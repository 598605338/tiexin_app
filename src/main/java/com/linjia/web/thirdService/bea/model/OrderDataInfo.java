package com.linjia.web.thirdService.bea.model;

import java.math.BigDecimal;
import java.util.List;

public class OrderDataInfo {

	 private Integer transport_station_id;
     private String transport_station_tel;
     private Integer carrier_driver_id;
     private String carrier_driver_name;
     private String carrier_driver_phone;
     private Long estimate_arrive_time;
     private BigDecimal order_total_delivery_cost;
     private BigDecimal order_total_delivery_discount;
     private Integer order_status;
     private String abnormal_code;
     private String abnormal_desc;
     private List<EventlogInfo> event_log_details;
     
	public Integer getTransport_station_id() {
		return transport_station_id;
	}
	public void setTransport_station_id(Integer transport_station_id) {
		this.transport_station_id = transport_station_id;
	}
	public String getTransport_station_tel() {
		return transport_station_tel;
	}
	public void setTransport_station_tel(String transport_station_tel) {
		this.transport_station_tel = transport_station_tel;
	}
	public Integer getCarrier_driver_id() {
		return carrier_driver_id;
	}
	public void setCarrier_driver_id(Integer carrier_driver_id) {
		this.carrier_driver_id = carrier_driver_id;
	}
	public String getCarrier_driver_name() {
		return carrier_driver_name;
	}
	public void setCarrier_driver_name(String carrier_driver_name) {
		this.carrier_driver_name = carrier_driver_name;
	}
	public String getCarrier_driver_phone() {
		return carrier_driver_phone;
	}
	public void setCarrier_driver_phone(String carrier_driver_phone) {
		this.carrier_driver_phone = carrier_driver_phone;
	}
	public Long getEstimate_arrive_time() {
		return estimate_arrive_time;
	}
	public void setEstimate_arrive_time(Long estimate_arrive_time) {
		this.estimate_arrive_time = estimate_arrive_time;
	}
	public BigDecimal getOrder_total_delivery_cost() {
		return order_total_delivery_cost;
	}
	public void setOrder_total_delivery_cost(BigDecimal order_total_delivery_cost) {
		this.order_total_delivery_cost = order_total_delivery_cost;
	}
	public BigDecimal getOrder_total_delivery_discount() {
		return order_total_delivery_discount;
	}
	public void setOrder_total_delivery_discount(
			BigDecimal order_total_delivery_discount) {
		this.order_total_delivery_discount = order_total_delivery_discount;
	}
	public Integer getOrder_status() {
		return order_status;
	}
	public void setOrder_status(Integer order_status) {
		this.order_status = order_status;
	}
	public String getAbnormal_code() {
		return abnormal_code;
	}
	public void setAbnormal_code(String abnormal_code) {
		this.abnormal_code = abnormal_code;
	}
	public String getAbnormal_desc() {
		return abnormal_desc;
	}
	public void setAbnormal_desc(String abnormal_desc) {
		this.abnormal_desc = abnormal_desc;
	}
	public List<EventlogInfo> getEvent_log_details() {
		return event_log_details;
	}
	public void setEvent_log_details(List<EventlogInfo> event_log_details) {
		this.event_log_details = event_log_details;
	}
     
     
}
