package com.linjia.web.thirdService.bdlogistics.post;

import com.linjia.web.thirdService.bdlogistics.model.OrderDetail;

public class PostData {
	// 商户订单号，不允许重复
	private String out_order_id;
	// 该店今天该平台的第几号订单
	private Long order_index;
	// 用户手机号
	private String user_phone;
	// 用户配送地址
	private String user_address;
	// 用户姓名
	private String user_name;
	// 用户经度坐标，请使用百度坐标(BD-09)保留6位小数
	private String user_longitude;
	// 用户纬度坐标，请使用百度坐标(BD-09) 保留6位小数
	private String user_latitude;
	// 百度物流店铺号
	private String wl_shop_id;
	// 商户电话
	private String shop_phone;
	// 用户下单时间,秒级时间戳
	private Long order_time;
	// 期望送达时间模式（1：精确， 2：范围）
	private Long expect_time_mode;
	// 用户期望送达时间(开始时间，expect_time_mode为2时，必填)
	private Integer expect_time_start;
	// 用户期望送达时间,秒级时间戳（expect_time_mode为2时，为截止时间，expect_time_mode为1时，为精确时间）
	private Long expect_time;
	// 应付给商户的金额（单位：元），保留两位小数
	private String shop_price;
	// 实收用户的金额（单位：元），保留两位小数
	private String user_price;
	// 用户支付的金额（单位：元），保留两位小数
	private String user_total_price;
	// 用户订单备注
	private String remark;
	// 支付方式；1：在线支付，0：货到付款
	private Long pay_type;
	// 是否需要发票；1：需要，0：不需要（默认）
	private Long need_invoice;
	// 发票抬头
	private String invoice_title;
	// 订单来源
	private String source_name;
	// 订单详情，是一个多层结构；详见order_detail层级参数表
	private OrderDetail order_detail;
	// 订单推送时间，秒级时间戳
	private Long push_time;
	private Long city_id;
	private String city_name;
	private Long user_sex;
	private Long pay_status;
	private Long app_id;
	private String sign;

	public String getOut_order_id() {
		return out_order_id;
	}

	public void setOut_order_id(String out_order_id) {
		this.out_order_id = out_order_id;
	}

	public Long getOrder_index() {
		return order_index;
	}

	public void setOrder_index(Long order_index) {
		this.order_index = order_index;
	}

	public Long getCity_id() {
		return city_id;
	}

	public void setCity_id(Long city_id) {
		this.city_id = city_id;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getUser_phone() {
		return user_phone;
	}

	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}

	public String getUser_longitude() {
		return user_longitude;
	}

	public void setUser_longitude(String user_longitude) {
		this.user_longitude = user_longitude;
	}

	public String getUser_latitude() {
		return user_latitude;
	}

	public void setUser_latitude(String user_latitude) {
		this.user_latitude = user_latitude;
	}

	public String getUser_address() {
		return user_address;
	}

	public void setUser_address(String user_address) {
		this.user_address = user_address;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public Long getUser_sex() {
		return user_sex;
	}

	public void setUser_sex(Long user_sex) {
		this.user_sex = user_sex;
	}

	public String getWl_shop_id() {
		return wl_shop_id;
	}

	public void setWl_shop_id(String wl_shop_id) {
		this.wl_shop_id = wl_shop_id;
	}

	public String getShop_phone() {
		return shop_phone;
	}

	public void setShop_phone(String shop_phone) {
		this.shop_phone = shop_phone;
	}

	public Long getOrder_time() {
		return order_time;
	}

	public void setOrder_time(Long order_time) {
		this.order_time = order_time;
	}

	public Long getExpect_time() {
		return expect_time;
	}

	public void setExpect_time(Long expect_time) {
		this.expect_time = expect_time;
	}

	public String getShop_price() {
		return shop_price;
	}

	public void setShop_price(String shop_price) {
		this.shop_price = shop_price;
	}

	public String getUser_price() {
		return user_price;
	}

	public void setUser_price(String user_price) {
		this.user_price = user_price;
	}

	public String getUser_total_price() {
		return user_total_price;
	}

	public void setUser_total_price(String user_total_price) {
		this.user_total_price = user_total_price;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getPay_type() {
		return pay_type;
	}

	public void setPay_type(Long pay_type) {
		this.pay_type = pay_type;
	}

	public Long getPay_status() {
		return pay_status;
	}

	public void setPay_status(Long pay_status) {
		this.pay_status = pay_status;
	}

	public Long getNeed_invoice() {
		return need_invoice;
	}

	public void setNeed_invoice(Long need_invoice) {
		this.need_invoice = need_invoice;
	}

	public String getInvoice_title() {
		return invoice_title;
	}

	public void setInvoice_title(String invoice_title) {
		this.invoice_title = invoice_title;
	}

	public OrderDetail getOrder_detail() {
		return order_detail;
	}

	public void setOrder_detail(OrderDetail order_detail) {
		this.order_detail = order_detail;
	}

	public Long getPush_time() {
		return push_time;
	}

	public void setPush_time(Long push_time) {
		this.push_time = push_time;
	}

	public Long getExpect_time_mode() {
		return expect_time_mode;
	}

	public void setExpect_time_mode(Long expect_time_mode) {
		this.expect_time_mode = expect_time_mode;
	}

	public Long getApp_id() {
		return app_id;
	}

	public void setApp_id(Long app_id) {
		this.app_id = app_id;
	}

	public String getSource_name() {
		return source_name;
	}

	public void setSource_name(String source_name) {
		this.source_name = source_name;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Integer getExpect_time_start() {
		return expect_time_start;
	}

	public void setExpect_time_start(Integer expect_time_start) {
		this.expect_time_start = expect_time_start;
	}
	
}
