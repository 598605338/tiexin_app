package com.linjia.web.thirdService.bea.model;

public class EventlogInfo {

	 private Integer order_status;
     private Long occur_time;
     private String carrier_driver_name;
     private String carrier_driver_phone;
	public Integer getOrder_status() {
		return order_status;
	}
	public void setOrder_status(Integer order_status) {
		this.order_status = order_status;
	}
	public Long getOccur_time() {
		return occur_time;
	}
	public void setOccur_time(Long occur_time) {
		this.occur_time = occur_time;
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
     
     
}
