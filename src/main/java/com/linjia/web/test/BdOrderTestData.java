package com.linjia.web.test;

import java.util.ArrayList;
import java.util.List;

import com.linjia.web.thirdService.baidu.model.BaiduData;
import com.linjia.web.thirdService.baidu.model.Coord;
import com.linjia.web.thirdService.baidu.model.Discount;
import com.linjia.web.thirdService.baidu.model.Goods;
import com.linjia.web.thirdService.baidu.model.Order;
import com.linjia.web.thirdService.baidu.model.Photo;
import com.linjia.web.thirdService.baidu.model.Product;
import com.linjia.web.thirdService.baidu.model.BdShop;
import com.linjia.web.thirdService.baidu.model.BdUser;

public class BdOrderTestData {

	public static String getShopId(){
		String shopId="hd00001";
		return shopId;
	}
	
	public static String getBaiShopId(){
		String baiduShopId="1656859523";
		return baiduShopId;
	}
	
	public static String getTicket(){
		String ticket="CBB291F6-33BE-57CC-8FE3-441FE6E7BA6C";
		return ticket;
	}
	
	public static String getSource(){
		String Source="65400";
		return Source;
	}
	
	public static String getVersion(){
		String version="2";
		return version;
	}
	
	public static Order getOrder(){
		Order order=new Order();
		order.setOrder_id("14526785626180");
		order.setSend_immediately(1);
		order.setSend_time("1");
		order.setSend_fee(13);
		order.setPackage_fee(12);
		order.setDiscount_fee(11);
		order.setTotal_fee(23);
		order.setShop_fee(16);
		order.setUser_fee(19);
		order.setPay_type("1");
		order.setPay_status(1);
		order.setNeed_invoice(1);
		order.setInvoice_title("个人");
		order.setRemark("测试订单");
		order.setDelivery_party(2);
		order.setCreate_time(1434701230);
		order.setCancel_time(1470978231);
		return order;
	}
	
	public static Coord getCoord(){
		Coord coord=new Coord();
		coord.setLatitude("74.000022");
		coord.setLongitude("40.054065");
		return coord;
	}
	
	public static BdUser getUser(){
		BdUser user=new BdUser();
//		user.setId(007);
		user.setName("向先生");
		user.setPhone("13555554892");
		user.setGender(1);
		user.setAddress("地铁6号线五路居");
		user.setCoord(getCoord());
		return user;
	}
	
	public static BdShop getShop(){
		BdShop shop=new BdShop();
		shop.setId(getShopId());
		shop.setName("海鼎测试店铺2");
		shop.setBaidu_shop_id(getBaiShopId());
		return shop;
	}
	
	
	public static List<Product> getProductsList(){
		List<Product> products=new ArrayList<Product>();
		Product product=new Product();
		product.setProduct_id("7305179757366483271");
//		product.setUpc("6920584471017");
		product.setProduct_name("南瓜百合粥");
		product.setProduct_price(700);
		product.setProduct_amount(1);
		product.setProduct_fee(100);
		product.setPackage_price(50);
		product.setPackage_amount(2);
		product.setPackage_fee(10);
		product.setTotal_fee(680);
//		product.setUpc("000");
		products.add(product);
		return products;
	}
	
	public static List<Discount> getDiscountsList(){
		List<Discount> discounts=new ArrayList<Discount>();
		Discount discount=new Discount();
		discount.setType("jian");
		discount.setActicity_id("50105");
		discount.setRule_id("103145");
		discount.setFee(1500);
		discount.setBaidu_rate("1300");
		discount.setShop_rate("200");
		discount.setAgent_rate("0");
		discount.setLogistics_rate("0");
		discount.setDesc("立减优惠");
		discounts.add(discount);
		return discounts;
	}
	
	public static BaiduData getBaiduData(){
		BaiduData orderData = new BaiduData();
		orderData.setSource(getSource());
		orderData.setOrder(getOrder());
		orderData.setUser(getUser());
		orderData.setShop(getShop());
		orderData.setProducts(getProductsList());
		orderData.setDiscount(getDiscountsList());
		return orderData;
	}
	
	public static Goods getGoods(){
		Goods goods=new Goods();
		goods.setShop_id("hd00001");
		goods.setBrand_id(10);
		goods.setCat1_id(1);
		goods.setCat2_id(11);
		goods.setCat3_id(111);
		goods.setDesc("测试商品001");
		goods.setLeft_num(99);
		goods.setMarket_price(3700);
		goods.setName("\u84dd\u8393\u5c71\u836f\u996e\u6599");
		goods.setPhotos(getPhotoList());
	    goods.setSale_price(3600);
	    goods.setShop_id("hd00001");
	    goods.setStatus(1);
	    goods.setUpc("6920584471017");
	    return goods;
	}
	
	public static List<Photo> getPhotoList(){
		List<Photo> list=new ArrayList<Photo>();
		Photo photo=getPhoto();
		list.add(photo);
		return list;
	}
	
	public static Photo getPhoto(){
		Photo photo=new Photo();
		photo.setUrl("http://image.it168.com/n/640x480/1/1233/1233735.jpg");
		photo.setIs_master(1);
		return photo;
	}
	
}
