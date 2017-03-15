package com.linjia.web.query;

import java.math.BigDecimal;

public class SumOrderQuery {
	
		//自家店铺id
		private String mall_code;
		
		//百度店铺id
		private String shop_id;
		
		//美团店铺id
		private String app_poi_code;
		
		//京东到家店铺id
		private String produceStationNoIsv;

		//统计数量
		private int orderNums;
		
		//统计来源(1,邻家;2,美团;3,百度;4,京东到家)
		private int orderSource;
		
		//统计金额
		private BigDecimal total_price;
		
		//统计类型(1,进行中;2,已完成;3,已失效;4,全部)
		private int sumType;
		
		//进行中状态
		private Integer statusing;
		
		//进行中状态(多个，用","隔开)
		private int[] statusingList;
			
		//已完成
		private Integer statused;
		
		//已完成(多个，用","隔开)
		private  int[] statusedList;
		
		//已失效
		private Integer statusUn;
		
		//已失效(多个，用","隔开)
		private int[] statusUnList;
		
		//统计时间
		private String create_time;
		
		//支付状态
		private Integer pay_status;

		public int getOrderNums() {
			return orderNums;
		}

		public void setOrderNums(int orderNums) {
			this.orderNums = orderNums;
		}

		public int getOrderSource() {
			return orderSource;
		}

		public void setOrderSource(int orderSource) {
			this.orderSource = orderSource;
		}

		public BigDecimal getTotal_price() {
			return total_price;
		}

		public void setTotal_price(BigDecimal total_price) {
			this.total_price = total_price;
		}

		public int getSumType() {
			return sumType;
		}

		public void setSumType(int sumType) {
			this.sumType = sumType;
		}

		public String getCreate_time() {
			return create_time;
		}

		public void setCreate_time(String create_time) {
			this.create_time = create_time;
		}

		public Integer getStatusing() {
			return statusing;
		}

		public void setStatusing(Integer statusing) {
			this.statusing = statusing;
		}

		public Integer getStatused() {
			return statused;
		}

		public void setStatused(Integer statused) {
			this.statused = statused;
		}

		public Integer getStatusUn() {
			return statusUn;
		}

		public void setStatusUn(Integer statusUn) {
			this.statusUn = statusUn;
		}

		public Integer getPay_status() {
			return pay_status;
		}

		public void setPay_status(Integer pay_status) {
			this.pay_status = pay_status;
		}

		public String getMall_code() {
			return mall_code;
		}

		public void setMall_code(String mall_code) {
			this.mall_code = mall_code;
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

		public int[] getStatusingList() {
			return statusingList;
		}

		public void setStatusingList(int[] statusingList) {
			this.statusingList = statusingList;
		}

		public int[] getStatusedList() {
			return statusedList;
		}

		public void setStatusedList(int[] statusedList) {
			this.statusedList = statusedList;
		}

		public int[] getStatusUnList() {
			return statusUnList;
		}

		public void setStatusUnList(int[] statusUnList) {
			this.statusUnList = statusUnList;
		}

		public String getProduceStationNoIsv() {
			return produceStationNoIsv;
		}

		public void setProduceStationNoIsv(String produceStationNoIsv) {
			this.produceStationNoIsv = produceStationNoIsv;
		}
		
}
