package com.linjia.web.query;

import java.math.BigDecimal;
import java.util.Date;

import com.linjia.base.query.Query;

public class PinTuanOrderQuery extends Query{

	//主键
		private Integer id;
		//订单号
		private Long order_id;
		//订单开始时间
		private Date start_time;
		//订单结束时间
		private Date end_time;
		//创建时间
		private Date cre_date;
		//用户id
		private String user_id;
		//支付类型
		private Integer pay_type;
		//支付状态
		private Integer pay_status;
		//商品价格
		private BigDecimal price;
		//商品名称
		private String p_name;
		//地址id
		private Integer address_id;
		//订单状态
		private Integer status;
		//商品id
		private Long product_id;
		//商品code
		private String p_code;
		//拼团状态
		private Integer pt_status;
		//商品数量
		private Integer quanty;
		//
		private String remark;
		
		private Integer add_score;
		
		private BigDecimal onebuy_price;
		
		private Date pay_time;
		
		private BigDecimal real_price;
		
		private String third_logistics_no;
		
		//配送费
		private BigDecimal send_fee;
		
		private Integer pt_id;
		
		private Integer buyPersonNum;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Long getOrder_id() {
			return order_id;
		}

		public void setOrder_id(Long order_id) {
			this.order_id = order_id;
		}

		public Date getStart_time() {
			return start_time;
		}

		public void setStart_time(Date start_time) {
			this.start_time = start_time;
		}

		public Date getEnd_time() {
			return end_time;
		}

		public void setEnd_time(Date end_time) {
			this.end_time = end_time;
		}

		public Date getCre_date() {
			return cre_date;
		}

		public void setCre_date(Date cre_date) {
			this.cre_date = cre_date;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public Integer getPay_type() {
			return pay_type;
		}

		public void setPay_type(Integer pay_type) {
			this.pay_type = pay_type;
		}

		public Integer getPay_status() {
			return pay_status;
		}

		public void setPay_status(Integer pay_status) {
			this.pay_status = pay_status;
		}

		public BigDecimal getPrice() {
			return price;
		}

		public void setPrice(BigDecimal price) {
			this.price = price;
		}

		public String getP_name() {
			return p_name;
		}

		public void setP_name(String p_name) {
			this.p_name = p_name;
		}

		public Integer getAddress_id() {
			return address_id;
		}

		public void setAddress_id(Integer address_id) {
			this.address_id = address_id;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public Long getProduct_id() {
			return product_id;
		}

		public void setProduct_id(Long product_id) {
			this.product_id = product_id;
		}

		public String getP_code() {
			return p_code;
		}

		public void setP_code(String p_code) {
			this.p_code = p_code;
		}

		public Integer getPt_status() {
			return pt_status;
		}

		public void setPt_status(Integer pt_status) {
			this.pt_status = pt_status;
		}

		public Integer getQuanty() {
			return quanty;
		}

		public void setQuanty(Integer quanty) {
			this.quanty = quanty;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public BigDecimal getSend_fee() {
			return send_fee;
		}

		public void setSend_fee(BigDecimal send_fee) {
			this.send_fee = send_fee;
		}

		public Integer getAdd_score() {
			return add_score;
		}

		public void setAdd_score(Integer add_score) {
			this.add_score = add_score;
		}

		public BigDecimal getOnebuy_price() {
			return onebuy_price;
		}

		public void setOnebuy_price(BigDecimal onebuy_price) {
			this.onebuy_price = onebuy_price;
		}

		public Date getPay_time() {
			return pay_time;
		}

		public void setPay_time(Date pay_time) {
			this.pay_time = pay_time;
		}

		public BigDecimal getReal_price() {
			return real_price;
		}

		public void setReal_price(BigDecimal real_price) {
			this.real_price = real_price;
		}

		public String getThird_logistics_no() {
			return third_logistics_no;
		}

		public void setThird_logistics_no(String third_logistics_no) {
			this.third_logistics_no = third_logistics_no;
		}

		public Integer getPt_id() {
			return pt_id;
		}

		public void setPt_id(Integer pt_id) {
			this.pt_id = pt_id;
		}

		public Integer getBuyPersonNum() {
			return buyPersonNum;
		}

		public void setBuyPersonNum(Integer buyPersonNum) {
			this.buyPersonNum = buyPersonNum;
		}
		
		
		
}
