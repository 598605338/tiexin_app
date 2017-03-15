package com.linjia.web.test;

import java.util.ArrayList;
import java.util.List;
import com.linjia.web.thirdService.baidu.model.BusinessTime;
import com.linjia.web.thirdService.baidu.model.Category;
import com.linjia.web.thirdService.baidu.model.Coord;
import com.linjia.web.thirdService.baidu.model.Delivery_region;
import com.linjia.web.thirdService.baidu.model.Shop;

public class BaiduShopData {

	public static Shop getShop(){
		Shop shop=new Shop();
		shop.setMall_code("hd00001");
		shop.setName("海鼎测试店铺2");
		shop.setShop_logo("http://img.waimai.baidu.com/pb/1df4dce8e7edbf7d68de386553481e7cf4");
		shop.setPhone("010-58888888");
		shop.setDelivery_region(getDelivery_regionList());
		shop.setBusiness_time(getBusinessTimeList());
		shop.setPackage_box_price(100);
		shop.setLongitude(97.182099f);
		shop.setLatitude(31.149943f);
		shop.setAddress("西藏昌都气象局");
		return shop;
		
	}
	
	public static Shop getShopTest(){
		Shop shop=new Shop();
		shop.setMall_code("hd00001");
		shop.setDelivery_region(getDelivery_regionList());
		shop.setLongitude(97.182099f);
		shop.setLatitude(31.149943f);
		shop.setAddress("西藏昌都气象局");
		
		shop.setName("海鼎测试店铺2");
		shop.setShop_logo("http://img.waimai.baidu.com/pb/1df4dce8e7edbf7d68de386553481e7cf4");
		shop.setProvince("北京市");
		shop.setCity("北京市");
		shop.setCounty("海淀区");
		shop.setBrand("");
		shop.setCategorys(getCategorys());
		shop.setPhone("18121072175");
		shop.setService_phone("18121072175");
		shop.setCoord_type("bdll");
		shop.setDelivery_region(getDelivery_regionList());
		shop.setBusiness_time(getBusinessTimeList());
		shop.setBook_ahead_time("0");
//		shop.setInvoice_support(0);
		shop.setMin_order_price(100);
		shop.setPackage_box_price(200);
		return shop;
		
	}
	
	public static Shop getShopTest01(){
		Shop shop=new Shop();
		shop.setMall_code("hd00001");
		shop.setLongitude(31.149943f);
		shop.setLatitude(97.182099f);
		shop.setAddress("西藏昌都气象局");
		
		shop.setName("海鼎测试店铺2");
		shop.setShop_logo("http://img.waimai.baidu.com/pb/1df4dce8e7edbf7d68de386553481e7cf4");
		shop.setProvince("西藏自治区");
		shop.setCity("昌都市");
		shop.setCounty("昌都县");
		shop.setBrand("");
		shop.setCategorys(getCategorys());
		shop.setPhone("18121072175");
		shop.setService_phone("18121072175");
		shop.setCoord_type("bdll");
		shop.setDelivery_region(getDelivery_regionList());
		shop.setBusiness_time(getBusinessTimeList());
		shop.setBook_ahead_time("0");
//		加上报错
//		shop.setInvoice_support(0);
		shop.setMin_order_price(100);
		shop.setPackage_box_price(200);
		
		return shop;
		
	}
	
	public static List<Category> getCategorys(){
		List<Category> list=new ArrayList<Category>();
		Category cat=new Category();
		cat.setCategory1("水果生鲜");
		cat.setCategory2("");
		cat.setCategory3("水果");
		
		list.add(cat);
		return list;
	}
	
	public static List<Delivery_region> getDelivery_regionList(){
		List<Delivery_region> list=new ArrayList<Delivery_region>();
		Delivery_region region=new Delivery_region();
		region.setName("海鼎测试01");
		region.setRegion(getRegion());
		region.setDelivery_time("60");
		region.setDelivery_fee(0);
		list.add(region);
		return list;
	}
	
	public static List<List<Coord>> getRegion(){
		List<List<Coord>> list=new ArrayList<List<Coord>>() ;
		
		List<Coord> clist=new ArrayList<Coord>() ;
		Coord coord1=new Coord();
		coord1.setLatitude("97.181771");
		coord1.setLongitude("31.149039");
		
		Coord coord2=new Coord();
		coord2.setLatitude("97.182898");
		coord2.setLongitude("31.15041");
		
		Coord coord3=new Coord();
		coord3.setLatitude("97.182512");
		coord3.setLongitude("31.150665");
		
		clist.add(coord1);
		clist.add(coord2);
		clist.add(coord3);
		list.add(clist);
		return list;
		
	}
	
	public static List<BusinessTime> getBusinessTimeList(){
		List<BusinessTime> list=new ArrayList<BusinessTime>();
		BusinessTime btime=new BusinessTime();
		btime.setEnd("23:00");
		btime.setStart("00:00");

		list.add(btime);
		return list;
		
	}
}
