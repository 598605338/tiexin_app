package com.linjia.web.test;

import com.linjia.web.thirdService.dada.model.DaDaLogisticsParam;

public class DadaTestData {
	
	public static DaDaLogisticsParam getDadaData(){
		DaDaLogisticsParam logpm=new DaDaLogisticsParam();
		logpm.setSupplier_lng(517128.4f);
		logpm.setDeliver_fee(3.5f);
		logpm.setCity_name("北京");
		logpm.setSupplier_tel("0107856321");
		logpm.setReceiver_address("北京地铁6号线车公庄");
		logpm.setExpected_finish_time(0);
		logpm.setIs_prepay(1);
		logpm.setReceiver_phone("13654897512");
		logpm.setInfo("忌辛辣");
		logpm.setPay_for_supplier_fee(1.5f);
		logpm.setExpected_fetch_time(0);

	//修改订单id
	logpm.setOrigin_id("100036");
	
		logpm.setCargo_num(2);
		logpm.setSupplier_address("邻里家车公庄店");
		logpm.setReceiver_tel("0105879426");
		logpm.setReceiver_lng(517128.4f);
		logpm.setFetch_from_receiver_fee(2.5f);
		logpm.setSupplier_id("kf1001");
		logpm.setInvoice_title("个人");
		logpm.setSupplier_name("邻里家小王");
		logpm.setCargo_type(9);
		logpm.setCargo_weight(1.5f);
		logpm.setSupplier_phone("18677753216");
		logpm.setReceiver_name("张三丰");
		logpm.setReceiver_lat(4075103.8f);
		logpm.setSupplier_lat(4075103.8f);
		logpm.setCallback("http://www.linjia-cvs.cn/linjia_1/forOut/dadaPushData");
		logpm.setCreate_time((int) (System.currentTimeMillis()/1000));
		logpm.setCargo_price(10.5f);
		return logpm;
	}
	
	

}
