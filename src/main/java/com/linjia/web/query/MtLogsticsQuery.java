package com.linjia.web.query;

import com.linjia.base.query.Query;

public class MtLogsticsQuery extends Query{

		private Integer id;
		
		//订单ID
		private String order_id;
		
		//订单展示ID
		private String wm_order_id_view;	
		
		//APP方商家ID
		private String app_poi_code;	
		
		//美团商家名称
		private String wm_poi_name;	
		
		//美团商家地址
		private String wm_poi_address;	
		
		//美团商家电话
		private String wm_poi_phone;	
		
		//收件人地址
		private String recipient_address;	
		
		//收件人电话
		private String recipient_phone;	
		
		//收件人姓名
		private String recipient_name;	
		
		//门店配送费
		private float shipping_fee;	
		
		//总价
		private float total;	
		
		//原价
		private float original_price;	
		
		//忌口或备注
		private String caution;	
		
		//送餐员电话
		private String shipper_phone;	
		
		//订单状态
		private int status;	
		
		//城市ID（目前暂时用不到此信息）
		private int city_id;	
		
		//是否开发票
		private byte has_invoiced;	
		
		//发票抬头
		private String invoice_title;	
		
		//创建时间
		private Long ctime;	
		
		//更新时间
		private Long utime;	

		//用户预计送达时间，“立即送达”时为0
		private byte delivery_time;	
		
		//是否是第三方配送平台配送（0：否；1：是）
		private byte is_third_shipping;	
		
		//支付类型（1：货到付款；2：在线支付）
		private int pay_type;	
		
		//实际送餐地址纬度
		private float latitude;	
		
		//实际送餐地址经度
		private float longitude;	
		
		//门店当天的推单流水号，该信息默认不推送，如有需求请联系美团 
		private String day_seq;	
		
		private String detail;
		
		private String extras;
		
		private Integer deleted;

		public String getOrder_id() {
			return order_id;
		}

		public void setOrder_id(String order_id) {
			this.order_id = order_id;
		}

		public String getWm_order_id_view() {
			return wm_order_id_view;
		}

		public void setWm_order_id_view(String wm_order_id_view) {
			this.wm_order_id_view = wm_order_id_view;
		}

		public String getApp_poi_code() {
			return app_poi_code;
		}

		public void setApp_poi_code(String app_poi_code) {
			this.app_poi_code = app_poi_code;
		}

		public String getWm_poi_name() {
			return wm_poi_name;
		}

		public void setWm_poi_name(String wm_poi_name) {
			this.wm_poi_name = wm_poi_name;
		}

		public String getWm_poi_address() {
			return wm_poi_address;
		}

		public void setWm_poi_address(String wm_poi_address) {
			this.wm_poi_address = wm_poi_address;
		}

		public String getWm_poi_phone() {
			return wm_poi_phone;
		}

		public void setWm_poi_phone(String wm_poi_phone) {
			this.wm_poi_phone = wm_poi_phone;
		}

		public String getRecipient_address() {
			return recipient_address;
		}

		public void setRecipient_address(String recipient_address) {
			this.recipient_address = recipient_address;
		}

		public String getRecipient_phone() {
			return recipient_phone;
		}

		public void setRecipient_phone(String recipient_phone) {
			this.recipient_phone = recipient_phone;
		}

		public String getRecipient_name() {
			return recipient_name;
		}

		public void setRecipient_name(String recipient_name) {
			this.recipient_name = recipient_name;
		}

		public float getShipping_fee() {
			return shipping_fee;
		}

		public void setShipping_fee(float shipping_fee) {
			this.shipping_fee = shipping_fee;
		}

		public float getTotal() {
			return total;
		}

		public void setTotal(float total) {
			this.total = total;
		}

		public float getOriginal_price() {
			return original_price;
		}

		public void setOriginal_price(float original_price) {
			this.original_price = original_price;
		}

		public String getCaution() {
			return caution;
		}

		public void setCaution(String caution) {
			this.caution = caution;
		}

		public String getShipper_phone() {
			return shipper_phone;
		}

		public void setShipper_phone(String shipper_phone) {
			this.shipper_phone = shipper_phone;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public int getCity_id() {
			return city_id;
		}

		public void setCity_id(int city_id) {
			this.city_id = city_id;
		}

		public byte getHas_invoiced() {
			return has_invoiced;
		}

		public void setHas_invoiced(byte has_invoiced) {
			this.has_invoiced = has_invoiced;
		}

		public String getInvoice_title() {
			return invoice_title;
		}

		public void setInvoice_title(String invoice_title) {
			this.invoice_title = invoice_title;
		}

		public Long getCtime() {
			return ctime;
		}

		public void setCtime(Long ctime) {
			this.ctime = ctime;
		}

		public Long getUtime() {
			return utime;
		}

		public void setUtime(Long utime) {
			this.utime = utime;
		}

		public byte getDelivery_time() {
			return delivery_time;
		}

		public void setDelivery_time(byte delivery_time) {
			this.delivery_time = delivery_time;
		}

		public byte getIs_third_shipping() {
			return is_third_shipping;
		}

		public void setIs_third_shipping(byte is_third_shipping) {
			this.is_third_shipping = is_third_shipping;
		}

		public int getPay_type() {
			return pay_type;
		}

		public void setPay_type(int pay_type) {
			this.pay_type = pay_type;
		}

		public float getLatitude() {
			return latitude;
		}

		public void setLatitude(float latitude) {
			this.latitude = latitude;
		}

		public float getLongitude() {
			return longitude;
		}

		public void setLongitude(float longitude) {
			this.longitude = longitude;
		}

		public String getDay_seq() {
			return day_seq;
		}

		public void setDay_seq(String day_seq) {
			this.day_seq = day_seq;
		}

		public String getDetail() {
			return detail;
		}

		public void setDetail(String detail) {
			this.detail = detail;
		}

		public String getExtras() {
			return extras;
		}

		public void setExtras(String extras) {
			this.extras = extras;
		}

		public Integer getDeleted() {
			return deleted;
		}

		public void setDeleted(Integer deleted) {
			this.deleted = deleted;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		
}
