package com.linjia.web.test;

import java.util.ArrayList;

import com.linjia.web.thirdService.bdlogistics.factory.BdLogisticConfig;
import com.linjia.web.thirdService.bdlogistics.model.DishDetail;
import com.linjia.web.thirdService.bdlogistics.model.OrderDetail;
import com.linjia.web.thirdService.bdlogistics.model.Product;
import com.linjia.web.thirdService.bdlogistics.post.PostData;
import com.linjia.web.thirdService.bdlogistics.utils.BdLogisUtil;

public class BdLogisticTestData {

	/**
	 * 生成示例post_data
	 * 
	 * @return json字符串形式的post_data
	 * @throws JsonProcessingException
	 */

	public static PostData getPostData() {
		PostData postData = new PostData();
		Long time = System.currentTimeMillis() / 1000;
		postData.setOut_order_id(time.toString());
		postData.setOrder_index(21L);
		postData.setCity_id(131L);
		postData.setCity_name("北京市");
		postData.setUser_phone("15101087233");
		postData.setUser_longitude("116.345611");
		postData.setUser_latitude("40.001976");
		postData.setUser_address("天安门");
		postData.setUser_name("王小二");
		postData.setUser_sex(1L);
		postData.setWl_shop_id(BdLogisticConfig.getAppConfig("wl_shop_id"));
		postData.setShop_phone("66025198");
		postData.setOrder_time(time);
		postData.setExpect_time(time + 3000);
		postData.setShop_price("0.00");
		postData.setUser_price("0.00");
		postData.setUser_total_price("10.00");
		postData.setRemark("没零钱");
		postData.setPay_type(1L);
		postData.setPay_status(1L);
		postData.setNeed_invoice(0L);
		postData.setInvoice_title("百度时代网络技术（北京）有限公司");
		postData.setOrder_detail(getOrderDetail());
		postData.setPush_time(time);
		postData.setExpect_time_mode(2L);
		postData.setSource_name("dmall");
		return postData;
	}

	public static Product getProduct1() {
		Product product1 = new Product();
		product1.setDish_name("板烧猪肉饭");
		product1.setDish_unit_price("28.00");
		product1.setDish_number(2L);
		product1.setDish_total_price("56.00");
		product1.setUnit_name("份");
		product1.setPackge_box_price("10.00");
		product1.setPackge_box_number(2L);
		product1.setDish_details(getDishDetails1());
		return product1;
	}

	public static Product getProduct2() {
		Product product2 = new Product();
		product2.setDish_name("板烧牛肉饭");
		product2.setDish_unit_price("28.00");
		product2.setDish_number(2L);
		product2.setDish_total_price("56.00");
		product2.setUnit_name("份");
		product2.setPackge_box_price("10.00");
		product2.setPackge_box_number(2L);
		product2.setDish_details(getDishDetails2());
		return product2;
	}

	public static ArrayList<Product> getProducts() {
		ArrayList<Product> products = new ArrayList<Product>();
		products.add(getProduct1());
		products.add(getProduct2());
		return products;
	}

	public static OrderDetail getOrderDetail() {
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setBox_price("5.00");
		orderDetail.setSend_price("8.00");
		orderDetail.setProduct_price("100.45");
		orderDetail.setProducts(getProducts());
		return orderDetail;
	}

	public static ArrayList<DishDetail> getDishDetails1() {
		DishDetail dishDetail1 = new DishDetail("板烧猪肉饭", 1L, "碗");
		DishDetail dishDetail2 = new DishDetail("可乐", 1L, "大杯");
		ArrayList<DishDetail> dishDetails1 = new ArrayList<DishDetail>();
		dishDetails1.add(dishDetail1);
		dishDetails1.add(dishDetail2);
		return dishDetails1;
	}

	public static ArrayList<DishDetail> getDishDetails2() {
		DishDetail dishDetail3 = new DishDetail("板烧牛肉饭", 1L, "碗");
		DishDetail dishDetail4 = new DishDetail("可乐", 1L, "大杯");
		ArrayList<DishDetail> dishDetails2 = new ArrayList<DishDetail>();
		dishDetails2.add(dishDetail3);
		dishDetails2.add(dishDetail4);
		return dishDetails2;
	}

}
