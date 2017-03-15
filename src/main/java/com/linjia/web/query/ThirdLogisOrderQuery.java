package com.linjia.web.query;

import java.util.Date;

import com.linjia.base.query.Query;

/**
 * 第三方订单查询封装参数类
 * @author xiangshouyi
 *
 */
public class ThirdLogisOrderQuery extends Query{

	//自家店铺id
	private String mall_code;
	
	//百度店铺id
	private String shop_id;
	
	//美团店铺id
	private String app_poi_code;
	
	//京东店铺id
	private String produceStationNoIsv;
	
	//订单id
	private Long order_id;
	
	//订单状态
	private Integer status;
	
	//支付状态
	private Integer pay_status;

	//订单状态集合
	private int[] orderStatusList;
	
	//催单数
	private Integer urgeNum;
	
	//美团订单支付状态
	private Integer mtPatyStatus;
	
	//美团订单状态
	private Integer mtOrderStatus;
		
	//美团订单状态id集合(,隔开)
	private int[] mtOrderStatusList;
	
	//百度订单支付状态
	private Integer bdPatyStatus;
	
	//百度订单状态
	private Integer bdOrderStatus;
	
	//京东订单状态
	private Integer jdOrderStatus;
	
	//饿了么订单状态
	private Integer elemeOrderStatus;
	private int[] elemeOrderStatusList;
	
	//百度订单状态id集合(,隔开)
	private int[] bdOrderStatusList;
	
	//京东订单状态集合(,隔开)
	private int[] jdOrderStatusList;
	
	//订单配送类型（1级）
	private Integer order_send_type;
	
	//送货类型(2级)
	private Integer send_type;
	
	//订单配送方式（3级）
	private Integer send_logistics_type;
	
	//收货人手机号
	private String receive_phone;
	
	//预约时间、
	private Date send_date;
	
	//创建时间
	private Date cre_date;
	
	//预约计算时间
	private Date sumSend_date;
	
	//预约小时
	private Integer send_hour;
	
	//物流取消订单状态
	private Integer logStatus;
	
	private Integer querySearceDate;
	
	private Integer orderPx;
	
	private Integer temp;
	
	/** 用户预计送达时间，“立即送达”时为0* */
	private Long deliveryTime;
	
	public Integer getPay_status() {
		return pay_status;
	}

	public void setPay_status(Integer pay_status) {
		this.pay_status = pay_status;
	}

	public Integer getSend_type() {
		return send_type;
	}

	public void setSend_type(Integer send_type) {
		this.send_type = send_type;
	}

	public String getShop_id() {
		return shop_id;
	}

	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}

	public String getApp_poi_code() {
		return app_poi_code;
	}

	public void setApp_poi_code(String app_poi_code) {
		this.app_poi_code = app_poi_code;
	}

	public Long getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}

	public String getMall_code() {
		return mall_code;
	}

	public void setMall_code(String mall_code) {
		this.mall_code = mall_code;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getUrgeNum() {
		return urgeNum;
	}

	public void setUrgeNum(Integer urgeNum) {
		this.urgeNum = urgeNum;
	}

	public Integer getBdPatyStatus() {
		return bdPatyStatus;
	}

	public void setBdPatyStatus(Integer bdPatyStatus) {
		this.bdPatyStatus = bdPatyStatus;
	}

	public Integer getMtPatyStatus() {
		return mtPatyStatus;
	}

	public void setMtPatyStatus(Integer mtPatyStatus) {
		this.mtPatyStatus = mtPatyStatus;
	}

	public Integer getBdOrderStatus() {
		return bdOrderStatus;
	}

	public void setBdOrderStatus(Integer bdOrderStatus) {
		this.bdOrderStatus = bdOrderStatus;
	}

	public Integer getMtOrderStatus() {
		return mtOrderStatus;
	}

	public void setMtOrderStatus(Integer mtOrderStatus) {
		this.mtOrderStatus = mtOrderStatus;
	}

	public Integer getOrder_send_type() {
		return order_send_type;
	}

	public void setOrder_send_type(Integer order_send_type) {
		this.order_send_type = order_send_type;
	}

	public Integer getSend_logistics_type() {
		return send_logistics_type;
	}

	public void setSend_logistics_type(Integer send_logistics_type) {
		this.send_logistics_type = send_logistics_type;
	}

	public String getReceive_phone() {
		return receive_phone;
	}

	public void setReceive_phone(String receive_phone) {
		this.receive_phone = receive_phone;
	}

	public Date getSend_date() {
		return send_date;
	}

	public void setSend_date(Date send_date) {
		this.send_date = send_date;
	}

	public Date getCre_date() {
		return cre_date;
	}

	public void setCre_date(Date cre_date) {
		this.cre_date = cre_date;
	}

	public int[] getOrderStatusList() {
		return orderStatusList;
	}

	public void setOrderStatusList(int[] orderStatusList) {
		this.orderStatusList = orderStatusList;
	}

	public int[] getMtOrderStatusList() {
		return mtOrderStatusList;
	}

	public void setMtOrderStatusList(int[] mtOrderStatusList) {
		this.mtOrderStatusList = mtOrderStatusList;
	}

	public int[] getBdOrderStatusList() {
		return bdOrderStatusList;
	}

	public void setBdOrderStatusList(int[] bdOrderStatusList) {
		this.bdOrderStatusList = bdOrderStatusList;
	}

	public Date getSumSend_date() {
		return sumSend_date;
	}

	public void setSumSend_date(Date sumSend_date) {
		this.sumSend_date = sumSend_date;
	}

	public Integer getSend_hour() {
		return send_hour;
	}

	public void setSend_hour(Integer send_hour) {
		this.send_hour = send_hour;
	}

	public Integer getLogStatus() {
		return logStatus;
	}

	public void setLogStatus(Integer logStatus) {
		this.logStatus = logStatus;
	}

	public Integer getQuerySearceDate() {
		return querySearceDate;
	}

	public void setQuerySearceDate(Integer querySearceDate) {
		this.querySearceDate = querySearceDate;
	}

	public Integer getOrderPx() {
		return orderPx;
	}

	public void setOrderPx(Integer orderPx) {
		this.orderPx = orderPx;
	}

	public Integer getTemp() {
		return temp;
	}

	public void setTemp(Integer temp) {
		this.temp = temp;
	}

	public String getProduceStationNoIsv() {
		return produceStationNoIsv;
	}

	public void setProduceStationNoIsv(String produceStationNoIsv) {
		this.produceStationNoIsv = produceStationNoIsv;
	}

	public Integer getJdOrderStatus() {
		return jdOrderStatus;
	}

	public void setJdOrderStatus(Integer jdOrderStatus) {
		this.jdOrderStatus = jdOrderStatus;
	}

	public Integer getElemeOrderStatus() {
		return elemeOrderStatus;
	}

	public void setElemeOrderStatus(Integer elemeOrderStatus) {
		this.elemeOrderStatus = elemeOrderStatus;
	}

	public int[] getElemeOrderStatusList() {
		return elemeOrderStatusList;
	}

	public void setElemeOrderStatusList(int[] elemeOrderStatusList) {
		this.elemeOrderStatusList = elemeOrderStatusList;
	}

	public int[] getJdOrderStatusList() {
		return jdOrderStatusList;
	}

	public void setJdOrderStatusList(int[] jdOrderStatusList) {
		this.jdOrderStatusList = jdOrderStatusList;
	}

	public Long getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Long deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

}
