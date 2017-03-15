package com.linjia.web.query;

import com.linjia.base.query.Query;

public class ShopUserQuery extends Query{

	//id
		private Integer  id;
		//账户名
		private String account;
		//密码
		private String password;
		//百度账号id
		private String baiduShopId;
		//美团账号id
		private String meituanShopId;
		//店铺code
		private String mall_code;
		//店铺名称
		private String mall_name;
		//店铺电话
		private String mall_phone;
		//店铺地址
		private String mall_address;
		//百度物流id
		private String bd_wl_shop_id;
		
		public String getMall_code() {
			return mall_code;
		}
		public void setMall_code(String mall_code) {
			this.mall_code = mall_code;
		}
		public String getMall_name() {
			return mall_name;
		}
		public void setMall_name(String mall_name) {
			this.mall_name = mall_name;
		}
		public String getMall_phone() {
			return mall_phone;
		}
		public void setMall_phone(String mall_phone) {
			this.mall_phone = mall_phone;
		}
		public String getMall_address() {
			return mall_address;
		}
		public void setMall_address(String mall_address) {
			this.mall_address = mall_address;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getAccount() {
			return account;
		}
		public void setAccount(String account) {
			this.account = account;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getBaiduShopId() {
			return baiduShopId;
		}
		public void setBaiduShopId(String baiduShopId) {
			this.baiduShopId = baiduShopId;
		}
		public String getMeituanShopId() {
			return meituanShopId;
		}
		public void setMeituanShopId(String meituanShopId) {
			this.meituanShopId = meituanShopId;
		}
		public String getBd_wl_shop_id() {
			return bd_wl_shop_id;
		}
		public void setBd_wl_shop_id(String bd_wl_shop_id) {
			this.bd_wl_shop_id = bd_wl_shop_id;
		}
		
}
