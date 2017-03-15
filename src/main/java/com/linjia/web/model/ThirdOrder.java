package com.linjia.web.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ThirdOrder {

	//订单id
	private Long id;
	//店铺cod
	private String mall_code;
	//店铺名称
	private String mall_name;
	//收货人地址
	private String receive_address;
	//收货人姓名
	private String receive_phone;
	//收货人姓名
	private String receive_name;
	//提货方式
	private int send_type;
	//期望送达时间
	private Timestamp send_date;
	//创建时间
	private Timestamp create_time;
	//支付方式
	private int pay_type;
	//订单总金额
	private BigDecimal  total_price;
	//订单来源
	private int orderSource;
	//接单时间
	private Timestamp recive_time;
	//物流名称
	private String logisticsName;
	//配送员电话
	private String deliverPhone;
	//当天第几单单数
	private int orderNum;
	//催单数
	private int urgeNum;
	//订单列表
	private List<ThirdOrderProduct> products;
	//送达时间（小时数）
	private String send_hour;
	//当前时间
	private Long currentTime;
	//取消原因
	private String cancelReason;
	
	//运费
	private BigDecimal send_price;
	
	//实付款
	private BigDecimal real_price;
	
	//订单优惠金额
	private BigDecimal orderDiscountMoney;
	
	//包装金额
	private BigDecimal packagingMoney;
	
	//订单状态
	private Integer status;
	
	//订单预约配送方式(订单配送方式：0立即配送；1预约配送；2立即配送+预约配送)
	private Integer order_send_type;
	
	//订单自配送方式(1,自配送；2，达达配送；3，百度配送;4,蜂鸟配送(饿了么)；5,美团配送；6,京东配送)
	private Integer send_logistics_type;
	
	private String remark;
	
	@JsonIgnore
	private String extras;
	
	private List extrasArr;
	
	/** 单据状态(0未拣货；2:拣货完成)* */
	private Integer pickMark;
	/** 用户取消申请：0未申请；1已申请* */
	private Integer applyCancel;
	/** 用户预计送达时间，“立即送达”时为0* */
	private Long deliveryTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMall_code() {
		return mall_code;
	}
	public void setMall_code(String mall_code) {
		this.mall_code = mall_code;
	}
	public String getLogisticsName() {
		return logisticsName;
	}
	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}
	public String getDeliverPhone() {
		return deliverPhone;
	}
	public void setDeliverPhone(String deliverPhone) {
		this.deliverPhone = deliverPhone;
	}
	public String getMall_name() {
		return mall_name;
	}
	public void setMall_name(String mall_name) {
		this.mall_name = mall_name;
	}
	public String getReceive_address() {
		return receive_address;
	}
	public void setReceive_address(String receive_address) {
		this.receive_address = receive_address;
	}
	public String getReceive_phone() {
		return receive_phone;
	}
	public void setReceive_phone(String receive_phone) {
		this.receive_phone = receive_phone;
	}
	public String getReceive_name() {
		return receive_name;
	}
	public void setReceive_name(String receive_name) {
		this.receive_name = receive_name;
	}
	public int getSend_type() {
		return send_type;
	}
	public void setSend_type(int send_type) {
		this.send_type = send_type;
	}
	public Timestamp getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}
	public int getPay_type() {
		return pay_type;
	}
	public void setPay_type(int pay_type) {
		this.pay_type = pay_type;
	}

	public BigDecimal getTotal_price() {
		return total_price;
	}
	public void setTotal_price(BigDecimal total_price) {
		this.total_price = total_price;
	}
	public int getOrderSource() {
		return orderSource;
	}
	public void setOrderSource(int orderSource) {
		this.orderSource = orderSource;
	}
	public Timestamp getSend_date() {
		return send_date;
	}
	public void setSend_date(Timestamp send_date) {
		this.send_date = send_date;
	}
	public List<ThirdOrderProduct> getProducts() {
		return products;
	}
	public void setProducts(List<ThirdOrderProduct> products) {
		this.products = products;
	}
	public Timestamp getRecive_time() {
		return recive_time;
	}
	public void setRecive_time(Timestamp recive_time) {
		this.recive_time = recive_time;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public int getUrgeNum() {
		return urgeNum;
	}
	public void setUrgeNum(int urgeNum) {
		this.urgeNum = urgeNum;
	}
	public String getSend_hour() {
		return send_hour;
	}
	public void setSend_hour(String send_hour) {
		this.send_hour = send_hour;
	}
	public Integer getOrder_send_type() {
		return order_send_type;
	}
	public void setOrder_send_type(Integer order_send_type) {
		this.order_send_type = order_send_type;
	}
	public Long getCurrentTime() {
		currentTime=System.currentTimeMillis();
		return currentTime;
	}
	public void setCurrentTime(Long currentTime) {
		this.currentTime = currentTime;
	}
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public BigDecimal getSend_price() {
		return send_price;
	}
	public void setSend_price(BigDecimal send_price) {
		this.send_price = send_price;
	}
	public BigDecimal getReal_price() {
		return real_price;
	}
	public void setReal_price(BigDecimal real_price) {
		this.real_price = real_price;
	}
	public Integer getStatus() { 
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSend_logistics_type() {
		return send_logistics_type;
	}
	public void setSend_logistics_type(Integer send_logistics_type) {
		this.send_logistics_type = send_logistics_type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getExtras() {
		return extras;
	}
	public void setExtras(String extras) {
		this.extras = extras;
	}
	public List getExtrasArr() {
		return extrasArr;
	}
	public void setExtrasArr(List extrasArr) {
		this.extrasArr = extrasArr;
	}
	public BigDecimal getOrderDiscountMoney() {
		return orderDiscountMoney;
	}
	public void setOrderDiscountMoney(BigDecimal orderDiscountMoney) {
		this.orderDiscountMoney = orderDiscountMoney;
	}
	public BigDecimal getPackagingMoney() {
		return packagingMoney;
	}
	public void setPackagingMoney(BigDecimal packagingMoney) {
		this.packagingMoney = packagingMoney;
	}
	public Integer getPickMark() {
		return pickMark;
	}
	public void setPickMark(Integer pickMark) {
		this.pickMark = pickMark;
	}
	public Integer getApplyCancel() {
		return applyCancel;
	}
	public void setApplyCancel(Integer applyCancel) {
		this.applyCancel = applyCancel;
	}
	public Long getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(Long deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	
}
