package com.linjia.web.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.linjia.web.thirdService.meituan.model.MtOrder;
import com.linjia.web.thirdService.meituan.vo.OrderExtraParam;
import com.linjia.web.thirdService.meituan.vo.OrderFoodDetailParam;

public class MtTestData {

	public static MtOrder getMtOrder(){
		MtOrder mt=new MtOrder();
		
		//订单ID
		mt.setWm_order_id_view("");	
		mt.setApp_poi_code("10005");	
		mt.setWm_poi_name("车公庄店");	
		mt.setWm_poi_address("地铁6号线车公庄");	
		mt.setWm_poi_phone("13688859872");	
		mt.setWm_order_id_view("10005");
		mt.setRecipient_address("阜城大厦B座");	
		mt.setRecipient_phone("13458487594");	
		mt.setRecipient_name("张三丰");	
		
		mt.setOrderNum(8);
		mt.setRecive_time(new Date());
		mt.setOrder_id("10001");
		mt.setShipping_fee("5.5");	
		mt.setTotal("25.50");	
		mt.setOriginal_price("30.50");	
		mt.setStatus("1");	
		mt.setCity_id("101");	
		mt.setHas_invoiced("1");	
		mt.setDelivery_time("0");	
		mt.setIs_third_shipping("0");	
		
		
		
		mt.setCaution("忌辛辣");	
		mt.setShipper_phone("13655578946");	
		mt.setInvoice_title("个人");	
		mt.setCtime(1473391638l);	
		mt.setUtime(1473391638l);	
		mt.setPay_type(2);	
		mt.setLatitude(517128.4f);	
		mt.setLongitude(4075103.8f);	
		
		mt.setDetailList(getOrderDeatailList());
		mt.setExtraList(getOrderExtras());
		return mt;
	}
	
	public static List<OrderFoodDetailParam> getOrderDeatailList(){
		List<OrderFoodDetailParam> list=new ArrayList<OrderFoodDetailParam>();
		OrderFoodDetailParam param=new OrderFoodDetailParam();
		param.setApp_food_code("25217");
		param.setFood_name("人生果1号");
		param.setQuantity("6");
		param.setPrice("5.5");
		param.setBox_num("3");
		param.setBox_price("6.58");
		param.setUnit("份");
		param.setFood_discount("0.85");
		
		list.add(param);
		return list;
		
	}
	
	public static List<OrderExtraParam> getOrderExtras(){
		List<OrderExtraParam> list=new ArrayList<OrderExtraParam>();
		OrderExtraParam Extra=new OrderExtraParam();
		Extra.setAct_detail_id("1008");
		Extra.setReduce_fee("8");
		Extra.setRemark("满10减1");
		Extra.setType("2");
		Extra.setAvg_send_time("30");
		list.add(Extra);
		return list;
		
	}
}
