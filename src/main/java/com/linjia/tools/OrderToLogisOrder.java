package com.linjia.tools;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.linjia.constants.Application;
import com.linjia.constants.Constants;
import com.linjia.web.model.OrderGroup;
import com.linjia.web.model.OrderGroupProduct;
import com.linjia.web.thirdService.baidu.model.BaiduReOrder;
import com.linjia.web.thirdService.baidu.model.Coord;
import com.linjia.web.thirdService.baidu.model.Order;
import com.linjia.web.thirdService.baidu.model.Shop;
import com.linjia.web.thirdService.baidu.model.User;
import com.linjia.web.thirdService.bdlogistics.model.OrderDetail;
import com.linjia.web.thirdService.bdlogistics.model.Product;
import com.linjia.web.thirdService.bdlogistics.post.PostData;
import com.linjia.web.thirdService.bea.config.ElemeOpenConfig;
import com.linjia.web.thirdService.bea.request.ElemeCreateOrderRequest;
import com.linjia.web.thirdService.bea.request.ElemeCreateOrderRequest.ElemeCreateRequestData;
import com.linjia.web.thirdService.bea.request.ElemeCreateOrderRequest.ItemsJson;
import com.linjia.web.thirdService.dada.model.DaDaLogisticsParam;
import com.linjia.web.thirdService.meituan.model.MtReOrder;
import com.linjia.web.thirdService.meituan.vo.OrderFoodDetailParam;
import com.linjia.web.uhd123.common.Configure;

/**
 * 发送物流,抛单适配类
 * @author xiangshouyi
 *
 */
public class OrderToLogisOrder {
//	public static String testWl_shop_id="L1609090002";

	/**
	 * 邻家生成百度物流post_data
	 */
	public static PostData ljOrderToBdLogis(OrderGroup og,List<OrderGroupProduct> list,String wl_shop_id) {
		PostData postData = new PostData();
		Long time=System.currentTimeMillis() / 1000;
        postData.setOut_order_id(og.getId()+"");
		//该店今天该平台的第几号订单
		postData.setOrder_index(21L);
		//默认设置城市为”北京“
		postData.setCity_id(131L);
		postData.setCity_name(og.getCity_name());
		postData.setUser_phone(og.getReceivePhone());
		//用户经纬度
		postData.setUser_longitude(og.getLongitude().floatValue()+"");
		postData.setUser_latitude(og.getLatitude().floatValue()+"");
		postData.setUser_address(og.getReceiveAddress());
		postData.setUser_name(og.getReceiveName());
		postData.setUser_sex(1L);
//		postData.setWl_shop_id(testWl_shop_id);
		postData.setShop_phone(og.getMallPhone());
		postData.setOrder_time(og.getCreDate().getTime()/1000);
		Integer sendHourTimes = 0;
		if(!Tools.isEmpty(og.getSendHour())){
			String[] sendHour = og.getSendHour().split(":");
			if(sendHour.length > 1){
				sendHourTimes = Integer.valueOf(sendHour[0]) * 3600 + Integer.valueOf(sendHour[1]) * 60;
				postData.setExpect_time(og.getSendDate().getTime()/1000 + sendHourTimes);
			}else{
				postData.setExpect_time(og.getSendDate().getTime()/1000+Integer.valueOf(og.getSendHour())*3600);
			}
		}
//		float realPriceF=0.0f;
//		float preferentialPriceF=0.0f;
		float sendPriceF=0.0f;
		float totalF=0.0f;
//		BigDecimal realPrice=og.getRealPrice();
//		BigDecimal preferentialPrice=og.getPreferentialPrice();
		BigDecimal sendPrice=og.getSendPrice();
		BigDecimal total=og.getTotalPrice();
//		if(realPrice!=null){
//			realPriceF=realPrice.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue(); 
//		}
//		if(preferentialPrice!=null){
//			preferentialPriceF=preferentialPrice.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue(); 
//		}
		if(total!=null){
			totalF=total.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue(); 
		}
		if(sendPrice!=null){
			sendPriceF=sendPrice.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue(); 
		}
		//{"message":"Parameter: shop_price, user_price must be equal ","error_no":110002,"responseCode":200,"result":null}
		postData.setShop_price("0");
		//在线支付时，这个钱为0
		postData.setUser_price("0");
		postData.setUser_total_price(totalF+sendPriceF+"");
		String remark=og.getRemark();
		if(remark==null||remark.isEmpty()){
			remark="无";
		}
		postData.setRemark(remark);
//		Integer pay_type=og.getPayType();
//		if(pay_type!=null){
//			payType=og.getPayType().longValue();
//			
//		}
		//只考虑默认在线已支付
		postData.setPay_type(1L);
//		Integer pay_status=og.getPayStatus();
//		Long payStatus=0L;
//		if(pay_status!=null){
//			payStatus=pay_status.longValue();
//		}
		//只考虑默认在线已支付
		postData.setPay_status(1L);
		//是否需要发票
		postData.setNeed_invoice(0L);
		//发票抬头
		postData.setInvoice_title("个人");
		postData.setPush_time(time);
		//期望送达时间模式（1：精确， 2：范围）
		postData.setExpect_time_mode(1L);
		//订单来源
		postData.setSource_name(Application.BD_SOURCE_NAME);
		
		OrderDetail orderDetail = new OrderDetail();
		//餐盒费,保留两位小数；默认0.00
		orderDetail.setBox_price("5.00");
		orderDetail.setSend_price(og.getSendPrice()+"");
		orderDetail.setProduct_price(og.getTotalProductPrice()+"");
		
		ArrayList<Product> products = new ArrayList<Product>();
		//单品个数（套餐级别下菜品分类，我们是单个商品）
//		ArrayList<DishDetail> dishDetails = new ArrayList<DishDetail>();
		for (OrderGroupProduct orderGroupProduct : list) {
			Product product = new Product();
			product.setDish_name(orderGroupProduct.getpName());
			product.setDish_unit_price(orderGroupProduct.getItemPrice()+"");
			Integer quantity=orderGroupProduct.getQuantity();
			Long qtty=0L;
			if(quantity!=null){
				qtty=orderGroupProduct.getQuantity().longValue();
			}
			product.setDish_number(qtty);
			product.setDish_total_price(og.getTotalPrice()+"");
			product.setUnit_name("");
			//打包包装盒费用
			product.setPackge_box_price("1.00");
			//打包个数
			product.setPackge_box_number(1L);
			
//			DishDetail dishDetail = new DishDetail(orderGroupProduct.getpName(), qtty, "份");
//			dishDetails.add(dishDetail);
//			product.setDish_details(dishDetails);
			products.add(product);
		}
		
		orderDetail.setProducts(products);
		postData.setOrder_detail(orderDetail);
		return postData;
	}
	
	/**
	 * 邻家生成达达物流
	 */
	public static DaDaLogisticsParam ljOrderToDaDaLogis(OrderGroup og,List<OrderGroupProduct> list) {
		DaDaLogisticsParam logpm=new DaDaLogisticsParam();
		
		//发货人经纬度
		//百度坐标转换高德坐标 
		BigDecimal mlat=og.getMall_latitude();
		BigDecimal mlon=og.getMall_longitude();
		float mlatVal=0;
		float mlonVal=0;
		if(mlat!=null&&mlon!=null){
			double mlatV=mlat.doubleValue();
			double mlonV=mlon.doubleValue();
			double[] mlatlon=Util.bdToGaoDe(mlatV,mlonV);
			mlatVal=(float)mlatlon[1];
			mlonVal=(float)mlatlon[0];
		}
		logpm.setSupplier_lng(mlonVal);
		logpm.setSupplier_lat(mlatVal);
		//配送费用
		BigDecimal sp=og.getSendPrice();
		Float sendPrice=0.0f;
		if(sp!=null){
			sendPrice=sp.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue(); 
		}
		logpm.setDeliver_fee(sendPrice);
		//配送城市
		String cityName=og.getCity_name();
		if((!cityName.isEmpty())&&cityName!=null){
			cityName=cityName.replace("市", "");
		}
		logpm.setCity_name(cityName);
		//目前城市数据未对接，默认北京市
		logpm.setCity_code("010");
		logpm.setAd_code("110000");
		//发货人座机,必须传区号，手机号和座机号 二者至少有一个
		logpm.setSupplier_tel(og.getMallPhone());
		logpm.setReceiver_address(og.getReceiveAddress());
		if(!Tools.isEmpty(og.getSendHour())){
			String[] sendHour = og.getSendHour().split(":");
			if(sendHour.length > 1){
				Integer sendHourTimes = Integer.valueOf(sendHour[0]) * 3600 + Integer.valueOf(sendHour[1]) * 60;
				logpm.setExpected_finish_time((int)(og.getSendDate().getTime()/1000)+sendHourTimes);
			}else{
				logpm.setExpected_finish_time((int)(og.getSendDate().getTime()/1000)+Integer.valueOf(og.getSendHour())*3600);
			}
		}
		//是否需要垫付 1:是 0:否
		logpm.setIs_prepay(0);
		logpm.setReceiver_phone(og.getReceivePhone());
		String remarke=og.getRemark();
		if(remarke==null||remarke.isEmpty()){
			remarke="无";
		}
		logpm.setInfo(remarke);
		//付给商家的费用, 如果无需支付,传0
		logpm.setPay_for_supplier_fee(0);
		//期望取货时间,如果无,传0,传0的情况下，默认取当前时间10分钟之后为期望取货时间
		logpm.setExpected_fetch_time(0);

		logpm.setOrigin_id(og.getId()+"");
	
		logpm.setCargo_num(list.size());
		//发货地址
		logpm.setSupplier_address(og.getMall_address());
		//收货人座机, 不需要传区号，手机号和座机号 二者至少有一个
		logpm.setReceiver_tel("");
		//收货人经纬度
		//百度坐标转换高德坐标
		BigDecimal lat=og.getLatitude();
		BigDecimal lon=og.getLongitude();
		float latVal=0l;
		float lonVal=0l;
		if(lat!=null&&lon!=null){
			double latV=lat.doubleValue();
			double lonV=lon.doubleValue();
			double[] latlon=Util.bdToGaoDe(latV, lonV);
			latVal=(float)latlon[1];
			lonVal=(float)latlon[0];
		}
		logpm.setReceiver_lng(lonVal);
		logpm.setReceiver_lat(latVal);
		//向客户收取的费 用,如果无,传0
		logpm.setFetch_from_receiver_fee(0);
		//发货人id, 如果无, 传0
		logpm.setSupplier_id(og.getMallCode());
		logpm.setSupplier_name(og.getMallName());
		//发票抬头信息
		logpm.setInvoice_title("个人");
		//默认填9，便利店
		logpm.setCargo_type(9);
		//订单商品重量,如果无,传为1
		logpm.setCargo_weight(1);
		logpm.setSupplier_phone(og.getMallPhone());
		logpm.setReceiver_name(og.getReceiveName());
		logpm.setCallback(Constants.THIRD_CALLBACK_URL.DADACALL);
		logpm.setCreate_time((int) (System.currentTimeMillis()/1000));
		BigDecimal tp=og.getTotalPrice();
		Float cargo_price=0.0f;
		if(tp!=null){
			cargo_price=tp.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue(); 
		}
		logpm.setCargo_price(cargo_price);
		return logpm;
	}
	
	/**
	 * 美团生成百度物流
	 */
	public static PostData mtOrderToBdLogis(MtReOrder mt,List<OrderFoodDetailParam> list,String wl_shop_id) {
		PostData postData = new PostData();
		try {
			Long time = System.currentTimeMillis() / 1000;
			postData.setOut_order_id(mt.getOrder_id());
			//该店今天该平台的第几号订单
			postData.setOrder_index(21L);
			Long cid=(long) mt.getCity_id();
			postData.setCity_id(cid);
			postData.setCity_name("北京市");
			postData.setUser_phone(mt.getReceive_phone());
			//用户经纬度
			postData.setUser_longitude(mt.getLongitude()+"");
			postData.setUser_latitude(mt.getLatitude()+"");
			postData.setUser_address(mt.getReceive_address());
			postData.setUser_name(mt.getReceive_name());
			postData.setUser_sex(1L);
//			postData.setWl_shop_id(testWl_shop_id);
			postData.setShop_phone(mt.getMall_phone());
			postData.setOrder_time(mt.getCtime());
			SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Long exTime = (sdf.parse(mt.getDelivery_time()).getTime())/1000;
			postData.setExpect_time(exTime);
			//{"message":"Parameter: shop_price, user_price must be equal ","error_no":110002,"responseCode":200,"result":null}
			postData.setShop_price("0");
			//默认在线支付
			postData.setUser_price("0");
			postData.setUser_total_price(mt.getTotal()+mt.getShipping_fee()+"");
			String caution=mt.getCaution();
			if(caution==null||caution.isEmpty()){
				caution="无";
			}
			postData.setRemark(caution);
//			Long payType=0L;
//			Integer pat_type=mt.getPay_type();
//			if(pat_type!=null){
//				payType=Long.valueOf(pat_type);
//			}
			postData.setPay_type(1L);
			postData.setPay_status(Long.valueOf(1L));
			//是否需要发票
			Long need=0L;
			Byte b=mt.getHas_invoiced();
			if(b!=null){
				need=Long.valueOf(b);
			}
			postData.setNeed_invoice(need);
			//发票抬头
			postData.setInvoice_title(mt.getInvoice_title());
			postData.setPush_time(time);
			//期望送达时间模式（1：精确， 2：范围）
			postData.setExpect_time_mode(1L);
			//订单来源
			postData.setSource_name(Application.BD_SOURCE_NAME);
			
			OrderDetail orderDetail = new OrderDetail();
			//餐盒费,保留两位小数；默认0.00
			orderDetail.setBox_price("5.00");
			orderDetail.setSend_price(mt.getShipping_fee()+"");
			orderDetail.setProduct_price(mt.getTotal()+"");
			
			ArrayList<Product> products = new ArrayList<Product>();
//			ArrayList<DishDetail> dishDetails = new ArrayList<DishDetail>();
			for (OrderFoodDetailParam orderFoodDetailParam : list) {
				Product product = new Product();
				product.setDish_name(orderFoodDetailParam.getFood_name());
				product.setDish_unit_price(orderFoodDetailParam.getPrice());
				Long quantity=0L;
				String qt=orderFoodDetailParam.getQuantity();
				if(qt!=null){
					quantity=Long.valueOf(qt);
				}
				product.setDish_number(quantity);
				product.setDish_total_price(orderFoodDetailParam.getPrice()+orderFoodDetailParam.getBox_price());
				product.setUnit_name(orderFoodDetailParam.getUnit());
				//打包包装盒费用
				product.setPackge_box_price("1.50");
				//打包个数
				product.setPackge_box_number(1L);
				
//				DishDetail dishDetail = new DishDetail(orderFoodDetailParam.getFood_name(),quantity,orderFoodDetailParam.getUnit());
//				dishDetails.add(dishDetail);
//				product.setDish_details(dishDetails);
				products.add(product);
			}
			orderDetail.setProducts(products);
			postData.setOrder_detail(orderDetail);
		} catch (ParseException e) {
			e.printStackTrace();
			return postData;
		}
		return postData;
	}
	
	/**
	 * 美团生成达达物流
	 */
	public static DaDaLogisticsParam mtOrderToDaDaLogis(MtReOrder mt,List<OrderFoodDetailParam> list) {
		DaDaLogisticsParam logpm=new DaDaLogisticsParam();
		try {
			//店铺经纬度(订单未提供。。。。。。)
			logpm.setSupplier_lng(517128.4f);
			logpm.setSupplier_lat(4075103.8f);
			//配送费用
			logpm.setDeliver_fee(mt.getShipping_fee());
			//配送城市???
			logpm.setCity_name("北京");
			logpm.setCity_code("010");
			logpm.setAd_code("110000");
			//发货人座机,必须传区号，手机号和座机号 二者至少有一个
			logpm.setSupplier_tel(mt.getMall_phone());
			logpm.setReceiver_address(mt.getReceive_address());
			SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int exTime = (int) ((sdf.parse(mt.getDelivery_time()).getTime())/1000);
			logpm.setExpected_finish_time(exTime);
			//是否需要垫付 1:是 0:否
			logpm.setIs_prepay(0);
			logpm.setReceiver_phone(mt.getReceive_phone());
			String caution=mt.getCaution();
			if(caution==null||caution.isEmpty()){
				caution="无";
			}
			logpm.setInfo(caution);
			//付给商家的费用, 如果无需支付,传0
			logpm.setPay_for_supplier_fee(0);
			//期望取货时间,如果无,传0,传0的情况下，默认取当前时间10分钟之后为期望取货时间
			logpm.setExpected_fetch_time(0);
	
			logpm.setOrigin_id(mt.getOrder_id());
		
			logpm.setCargo_num(list.size());
			//发货地址
			logpm.setSupplier_address(mt.getWm_poi_address());
			//收货人座机, 不需要传区号，手机号和座机号 二者至少有一个
			logpm.setReceiver_tel(mt.getReceive_phone());
			//收货人经纬度
			logpm.setReceiver_lng(mt.getLongitude());
			logpm.setReceiver_lat(mt.getLatitude());
			//向客户收取的费 用,如果无,传0
			logpm.setFetch_from_receiver_fee(0);
			//发货人id, 如果无, 传0
			logpm.setSupplier_id(mt.getMall_code());
			logpm.setSupplier_name(mt.getMall_name());
			//发票抬头信息
			logpm.setInvoice_title(mt.getInvoice_title());
			
			logpm.setCargo_type(9);
			//订单商品重量,如果无,传为1
			logpm.setCargo_weight(1);
			logpm.setSupplier_phone(mt.getMall_phone());
			logpm.setReceiver_name(mt.getReceive_name());
			logpm.setCallback(Constants.THIRD_CALLBACK_URL.DADACALL);
			logpm.setCreate_time((int) (System.currentTimeMillis()/1000));
			Float total=mt.getTotal();
			logpm.setCargo_price(total);
		} catch (ParseException e) {
			e.printStackTrace();
			return logpm;
		}
		return logpm;
	}
	
	/**
	 * 百度生成百度物流
	 */
	public static PostData bdOrderToBdLogis(BaiduReOrder bd,List<com.linjia.web.thirdService.baidu.model.Product> list,String wl_shop_id) throws ParseException {
		PostData postData = new PostData();
		Long time = System.currentTimeMillis() / 1000;
		Order order=bd.getOrder();
		User user=bd.getUser();
		Shop shop=bd.getShop();
		//该店今天该平台的第几号订单
		postData.setOrder_index(21L);
		postData.setCity_id(131L);
		postData.setCity_name(shop.getCity());
		postData.setUser_phone(user.getReceive_phone());
		//用户经纬度
		postData.setUser_longitude(user.getCoord().getLongitude());
		postData.setUser_latitude(user.getCoord().getLatitude());
		postData.setUser_address(user.getReceive_address());
		postData.setUser_name(user.getReceive_name());
		postData.setUser_sex(Long.valueOf(user.getGender()));
//		postData.setWl_shop_id(testWl_shop_id);
		//店铺电话
		postData.setShop_phone(shop.getPhone());
		postData.setOrder_time((long)order.getCreate_time());
		Long exTime = (long)order.getCreate_time();
		postData.setExpect_time(exTime);
		postData.setShop_price(order.getShop_fee()+"");
		postData.setUser_price(order.getUser_fee()+"");
		postData.setUser_total_price(order.getTotal_fee()+"");
		String remark=order.getRemark();
		if(remark==null||remark.isEmpty()){
			remark="无";
		}
		postData.setRemark(remark);
//		String pay_type=order.getPay_type();
//		Long payType=0L;
//		if(pay_type!=null){
//			payType=Long.valueOf(pay_type);
//		}
		postData.setPay_type(1L);
//		Long payStatus=Long.valueOf(order.getPay_status())
		postData.setPay_status(1L);
		//是否需要发票
		postData.setNeed_invoice(Long.valueOf(order.getNeed_invoice()));
		//发票抬头
		postData.setInvoice_title(order.getInvoice_title());
		postData.setPush_time(time);
		//期望送达时间模式（1：精确， 2：范围）
		postData.setExpect_time_mode(1L);
		//订单来源
		postData.setSource_name(Application.BD_SOURCE_NAME);
		
		OrderDetail orderDetail = new OrderDetail();
		//餐盒费,保留两位小数；默认0.00
		orderDetail.setBox_price("5.00");
		orderDetail.setSend_price(order.getSend_fee()+"");
		orderDetail.setProduct_price(order.getTotal_fee()+"");
		
		ArrayList<Product> products = new ArrayList<Product>();
//		ArrayList<DishDetail> dishDetails = new ArrayList<DishDetail>();
		for (com.linjia.web.thirdService.baidu.model.Product pro : list) {
			Product product = new Product();
			product.setDish_name(pro.getProduct_name());
			product.setDish_unit_price(pro.getProduct_price()+"");
			Integer pa=pro.getProduct_amount();
			Long amount=0L;
			if(pa!=null){
				amount=Long.valueOf(pa);
			}
			product.setDish_number(amount);
			product.setDish_total_price(pro.getTotal_fee()+"");
			//份名称
			product.setUnit_name("份");
			//打包包装盒费用
			product.setPackge_box_price(pro.getPackage_fee()+"");
			//打包个数
			product.setPackge_box_number(1L);
			
//			DishDetail dishDetail = new DishDetail(pro.getProduct_name(),amount,"份");
//			dishDetails.add(dishDetail);
//			product.setDish_details(dishDetails);
			products.add(product);
		}
		orderDetail.setProducts(products);
		postData.setOrder_detail(orderDetail);
		return postData;
	}
	
	/**
	 * 百度生成达达物流
	 */
	public static DaDaLogisticsParam bdOrderToDaDaLogis(BaiduReOrder bd,List<com.linjia.web.thirdService.baidu.model.Product> list) {
		DaDaLogisticsParam logpm=new DaDaLogisticsParam();
		try {
			//店铺经纬度
			logpm.setSupplier_lng(517128.4f);
			logpm.setSupplier_lat(4075103.8f);
			Order order=bd.getOrder();
			User user=bd.getUser();
			Shop shop=bd.getShop();
			//配送费用
			logpm.setDeliver_fee(order.getSend_fee());
			//配送城市
			String sname=shop.getCity();
			if((!sname.isEmpty())&&sname!=null){
				sname=sname.replace("市", "");
			}
			logpm.setCity_name(sname);
			logpm.setCity_code("010");
			logpm.setAd_code("110000");
			//发货人座机,必须传区号，手机号和座机号 二者至少有一个
			logpm.setSupplier_tel("");
			logpm.setReceiver_address(user.getReceive_address());
			String sendTime=order.getSend_time();
			int exTime=0;
			if(sendTime!=null){
				exTime=Integer.parseInt(sendTime);
			}
			logpm.setExpected_finish_time(exTime);
			//是否需要垫付 1:是 0:否
			logpm.setIs_prepay(0);
			logpm.setReceiver_phone(user.getReceive_phone());
			String  remark=order.getRemark();
			if(remark==null||remark.isEmpty()){
				remark="无";
			}
			logpm.setInfo(remark);
			//付给商家的费用, 如果无需支付,传0
			logpm.setPay_for_supplier_fee(0);
			//期望取货时间,如果无,传0,传0的情况下，默认取当前时间10分钟之后为期望取货时间
			logpm.setExpected_fetch_time(0);
	
			logpm.setOrigin_id(order.getOrder_id());
		
			logpm.setCargo_num(list.size());
			//发货地址
			logpm.setSupplier_address(shop.getAddress());
			//收货人座机, 不需要传区号，手机号和座机号 二者至少有一个
			logpm.setReceiver_tel(user.getReceive_phone());
			String lng= user.getCoord().getLongitude();
			String lat=user.getCoord().getLatitude();
			Float lngF=0.0f;
			Float latF=0.0f;
			if(lng!=null){
				lngF=Float.valueOf(lng);
			}
			if(lat!=null){
				latF=Float.valueOf(lat);
			}
			//收货人经纬度
			logpm.setReceiver_lng(lngF);
			logpm.setReceiver_lat(latF);
			//向客户收取的费 用,如果无,传0
			logpm.setFetch_from_receiver_fee(0);
			//发货人id, 如果无, 传0
			logpm.setSupplier_id(shop.getMall_code());
			logpm.setSupplier_name(shop.getName());
			//发票抬头信息
			logpm.setInvoice_title(order.getInvoice_title());
			
			logpm.setCargo_type(9);
			//订单商品重量,如果无,传为1
			logpm.setCargo_weight(1);
			//店铺电话
			logpm.setSupplier_phone(shop.getService_phone());
			logpm.setReceiver_name(user.getReceive_name());
			logpm.setCallback(Constants.THIRD_CALLBACK_URL.DADACALL);
			logpm.setCreate_time((int) (System.currentTimeMillis()/1000));
			Float tf=0.0f;
			Integer tt=order.getTotal_fee();
			if(tt!=null){
				tf=Float.valueOf(tt);
			}
			logpm.setCargo_price(tf);
		} catch (Exception e) {
			e.printStackTrace();
			return logpm;
		}
		return logpm;
	}
	
	/**
	 * (抛单对象转换)
	 * 百度订单对象转商城订单对象
	 */
	public static OrderGroup bdOrderToLjOrder(BaiduReOrder bd,List<com.linjia.web.thirdService.baidu.model.Product> list) {
		SimpleDateFormat ysdf=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat hsdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		OrderGroup og=new OrderGroup();
	try {	
		//百度
		Shop shop=bd.getShop();
		Order order=bd.getOrder();
		User user=bd.getUser();
		Coord coord=user.getCoord();
		
		og.setId(Long.valueOf(order.getId()));
		og.setName("百度订单");
		if(list!=null){
			og.setTotalProductNum(list.size());
		}else{
			og.setTotalProductNum(0);
		}
		og.setTotalProductPrice(bigDecimalToPrice(order.getTotal_fee()));
		og.setTotalPrice(bigDecimalToPrice(order.getUser_fee()));
		og.setUserId(user.getReceive_phone());
		og.setReceiveName(user.getReceive_name());
		og.setReceivePhone(user.getReceive_phone());
		og.setReceiveAddress(user.getReceive_address());
		//百度订单默认送货上门
		og.setSendType(0);
//		if(order.getDelivery_party()==1){
//		}else if(order.getDelivery_party()==2){
//		}
		String sedstr=order.getSend_time();
		Date sed=null;
		Integer hour=null;
		Integer minitus=null;
		if(sedstr!=null&&(!"1".equals(sedstr))&&!sedstr.isEmpty()){
			Long times=Long.valueOf(sedstr)*1000; 
            String d1 =hsdf.format(times); 
			sed=ysdf.parse(d1);
			hour=hsdf.parse(d1).getHours();
			minitus=hsdf.parse(d1).getMinutes();
		}else{
			Long time=new Long(order.getCreate_time()*1000); 
            String d2 =hsdf.format(time); 
            sed =ysdf.parse(d2); 
            hour=hsdf.parse(d2).getHours();
            hour=hsdf.parse(d2).getMinutes();
		}
		og.setSendDate(sed);
		og.setSendHour(hour+":"+minitus);
		og.setMallCode(shop.getMall_code());
		og.setMallName(shop.getMall_name());
		og.setMallPhone(shop.getPhone());
		og.setSendPrice(bigDecimalToPrice(order.getSend_fee()));
//		og.setCouponType(bd);
//		og.setUserCardCouponId(bd);
//		og.setIsFullSubtract(bd);
//		og.setIsFreeFreight(bd);
//		og.setCardCouponPrice(bd);
//		og.setFullSubtractPrice(bd);
//		og.setFreeFreightPrice(bd);
		//支付方式
	    og.setPayType(3);
	    og.setPreferentialPrice(bigDecimalToPrice(order.getDiscount_fee()));
	    og.setRealPrice(bigDecimalToPrice(order.getUser_fee()));
	    //订单积分
//	    og.setScore(bd);
	    String ptype=order.getPay_type();
	    if("2".equals(ptype)){
	    	og.setPayStatus(1);
	    }else{
	    	og.setPayStatus(0);
	    }
//	    og.setStartTime(bd);
//	    og.setEndTime(bd);
	    int status=order.getStatus();
	    int ogstatus=0;
	    switch (status) {
		case 1:
			ogstatus=1;
			break;
		case 5:
			ogstatus=2;
			break;
		case 7:
			ogstatus=4;
			break;
		case 8:
			ogstatus=4;
			break;
		case 9:
			ogstatus=5;
			break;
		case 10:
			ogstatus=3;
			break;
		}
	    og.setOrderGroupStatus(ogstatus);
	    og.setRemark(order.getRemark());
	    Date cret=new Date();
	    if(order.getCreate_time()!=null){
	    	Long time=new Long(order.getCreate_time()*1000); 
            String dd =hsdf.format(time); 
				cret =hsdf.parse(dd);
	    }
	    og.setCreDate(cret);
//	    og.setTransactionId(bd);
	    
	    //订单配送方式
//		og.setOrderSendType(bd);
		//提货码
//		og.setTakeGoodsCode(bd);
	    String lat=coord.getLatitude();
	    if(lat==null||lat.isEmpty()){
	    	lat="0";
	    }
	    String lon=coord.getLongitude();
	    if(lon==null||lon.isEmpty()){
	    	lon="0";
	    }
	    og.setLatitude(new BigDecimal(lat));
	    og.setLongitude(new BigDecimal(lon));
//	    og.setUseCardPcode(bd);
//	    og.setUseCardPname(bd);
//	    og.setPayTime(bd);
	    og.setBusConfirmTime(order.getRecive_time());
//	    og.setNnightPickupTime(bd);
//	    og.setOrderSuccessTime(bd);
	    Date cancelt=null;
	    if(order.getCancel_time()!=null){
	    	Long time1=new Long(order.getCancel_time()*1000); 
            String dd1 =hsdf.format(time1); 
            cancelt =hsdf.parse(dd1); 
	    }
	    og.setOrderCancelTime(cancelt);
	    Float flat=shop.getLatitude();
	    if(flat==null){
	    	flat=0.0f;
	    }
	    Float flon=shop.getLongitude();
	    if(flon==null){
	    	flon=0.0f;
	    }
	    og.setMall_latitude(new BigDecimal(flat));
	    og.setMall_longitude(new BigDecimal(flon));
	    og.setCity_name(shop.getCity());
	    og.setMall_address(shop.getAddress());
	    og.setUhdOrderId(bd.getUhd_order_id());
	    og.setUhdRefundOrderId(bd.getUhd_refund_order_id());
	    og.setShop_id(Configure.shop_id_baiduwaimai);
	    
	    List<OrderGroupProduct> productList=new ArrayList<OrderGroupProduct>();
	    if(list!=null){
		    for (com.linjia.web.thirdService.baidu.model.Product product : list) {
		    	OrderGroupProduct ogp=new OrderGroupProduct();
		    	Long pid=null;
		    	String pidStr=product.getProduct_id();
		    	if(pidStr!=null&&!pidStr.isEmpty()){
		    		pid=Long.valueOf(pidStr);
		    	}
		    	ogp.setId(product.getOrderId());
		    	ogp.setGroupId(product.getOrderId());
		    	ogp.setProductId(pid);
		    	ogp.setpCode(product.getProduct_id());
		    	ogp.setpName(product.getProduct_name());
		    	ogp.setQuantity(product.getProduct_amount());
		    	ogp.setItemPrice(mul(bigDecimalToPrice(product.getProduct_fee())+"",product.getProduct_amount()+""));
		    	//默认
		    	ogp.setpSendType(0);
		    	
	//	    	ogp.setImagePath(product);
		    	ogp.setItemTotalPrice(bigDecimalToPrice(product.getTotal_fee()*product.getProduct_amount()));
		    	ogp.setProductPrice(bigDecimalToPrice(product.getProduct_price()));
		    	
		    	ogp.setDiscountAmount(new BigDecimal(0));
		    	ogp.setApportedDiscountAmount(new BigDecimal(0));
		    	productList.add(ogp);
			}
	    }
	    
	    og.setProductList(productList);
	} catch (ParseException e) {
		e.printStackTrace();
		og=null;
		return og;
	} 
		return og;
	}
	
	/**
	 * (抛单对象转换)
	 * 美团订单对象转商城订单对象
	 */
	public static OrderGroup mtOrderToLjOrder(MtReOrder mt,List<OrderFoodDetailParam> mtList) {
		SimpleDateFormat ysdf=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat hsdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		OrderGroup og=new OrderGroup();
	try {	
		String viewId=mt.getWm_order_id_view();
		if(!Tools.isEmpty(viewId)){
			Long vid=Long.valueOf(viewId);
			og.setId(vid);
		}else{
			og.setId(mt.getId());
		}
		og.setName("美团订单");
		if(mtList!=null){
			og.setTotalProductNum(mtList.size());
		}else{
			og.setTotalProductNum(0);
		}
		//商品总金额
		og.setTotalProductPrice(new BigDecimal(mt.getOriginal_price()));
		//合计金额
		og.setTotalPrice(new BigDecimal(mt.getOriginal_price()));
		og.setUserId(mt.getReceive_phone());
		og.setReceiveName(mt.getReceive_name());
		og.setReceivePhone(mt.getReceive_phone());
		og.setReceiveAddress(mt.getReceive_address());
		//百度订单默认送货上门
		og.setSendType(0);
//		if(order.getDelivery_party()==1){
//		}else if(order.getDelivery_party()==2){
//		}
		String sedstr=mt.getDelivery_time();
		Date sed=null;
		Integer hour=null;
		Integer minutes=null;
		if(sedstr!=null&&(!"0".equals(sedstr))&&!sedstr.isEmpty()){
			Long times=Long.valueOf(sedstr)*1000; 
            String dd =hsdf.format(times); 
			sed=ysdf.parse(dd);
			hour=hsdf.parse(dd).getHours();
			minutes=hsdf.parse(dd).getMinutes();
		}else{
			Long time=mt.getCtime();
			if(time==null){
				time=0L;
			}
            String d2 =hsdf.format(time*1000); 
            sed =ysdf.parse(d2); 
            hour=hsdf.parse(d2).getHours();
            minutes=hsdf.parse(d2).getMinutes();
		}
		og.setSendDate(sed);
		og.setSendHour(hour+":"+minutes);
		og.setMallCode(mt.getMall_code());
		og.setMallName(mt.getMall_name());
		og.setMallPhone(mt.getMall_phone());
		og.setSendPrice(new BigDecimal(mt.getShipping_fee()));
//		og.setCouponType(mt);
//		og.setUserCardCouponId(mt);
//		og.setIsFullSubtract(mt);
//		og.setIsFreeFreight(mt);
//		og.setCardCouponPrice(mt);
//		og.setFullSubtractPrice(mt);
//		og.setFreeFreightPrice(mt);
		//支付方式
	    og.setPayType(2);
	    //优惠金额
	    og.setPreferentialPrice(new BigDecimal(mt.getOriginal_price()-mt.getTotal()));
	    //实付款
	    og.setRealPrice(new BigDecimal(mt.getTotal()).setScale(2, BigDecimal.ROUND_HALF_UP));
	    //订单积分
//	    og.setScore(mt);
	    int ptype=mt.getPay_type();
	    if(ptype==2){
	    	og.setPayStatus(1);
	    }else{
	    	og.setPayStatus(0);
	    }
//	    og.setStartTime(mt);
//	    og.setEndTime(mt);
	    int status=mt.getStatus();
	    int ogstatus=0;
	    switch (status) {
		case 3:
			ogstatus=1;
			break;
		case 4:
			ogstatus=2;
			break;
		case 6:
			ogstatus=4;
			break;
		case 8:
			ogstatus=5;
			break;
		case 9:
			ogstatus=3;
			break;
		}
	    og.setOrderGroupStatus(ogstatus);
	    og.setRemark(mt.getCaution());
	    Date cret=new Date();
	    if(mt.getCtime()!=null){
	    	Long time=mt.getCtime()*1000; 
            String dd =hsdf.format(time); 
			cret =hsdf.parse(dd);
	    }
	    og.setCreDate(cret);
//	    og.setTransactionId(mt);
	    
	    //订单配送方式
//		og.setOrderSendType(mt);
		//提货码
//		og.setTakeGoodsCode(mt);
	    og.setLatitude(new BigDecimal(mt.getLatitude()));
	    og.setLongitude(new BigDecimal(mt.getLongitude()));
//	    og.setUseCardPcode(mt);
//	    og.setUseCardPname(mt);
//	    og.setPayTime(mt);
	    Date ut=null;
	    Long utime=mt.getUtime(); 
	    if(utime!=null){
            String dd2 =hsdf.format(utime); 
            ut =hsdf.parse(dd2);
	    }
	    og.setBusConfirmTime(ut);
//	    og.setNnightPickupTime(mt);
//	    og.setOrderSuccessTime(mt);
	    og.setOrderCancelTime(mt.getOrder_cancel_time());
	    og.setMall_latitude(null);
	    og.setMall_longitude(null);
	    //城市名称
	    og.setCity_name(mt.getCity_id()+"");
	    og.setMall_address(null);
	    og.setUhdOrderId(mt.getUhd_order_id());
	    og.setUhdRefundOrderId(mt.getUhd_refund_order_id());
	    og.setShop_id(Configure.shop_id_meituan);
	    
	    List<OrderGroupProduct> productList=new ArrayList<OrderGroupProduct>();
	    if(mtList!=null){
		    for (OrderFoodDetailParam product : mtList) {
		    	OrderGroupProduct ogp=new OrderGroupProduct();
		    	Long pid=null;
		    	String pidStr=product.getSku_id();
		    	if(pidStr!=null&&!pidStr.isEmpty()){
		    		pid=Long.valueOf(pidStr);
		    	}
		    	ogp.setId(pid);
		    	ogp.setProductId(pid);
		    	ogp.setGroupId(product.getOrderId());
		    	ogp.setpCode(product.getApp_food_code());
		    	ogp.setpName(product.getFood_name());
		    	ogp.setQuantity(Integer.parseInt(product.getQuantity()));
		    	BigDecimal discountMoney=mul(product.getFood_discount(),product.getPrice());
		    	String discountMoneyStr=null;
		    	if(discountMoney!=null){
		    		discountMoneyStr=discountMoney.setScale(2, BigDecimal.ROUND_HALF_UP).toString();   
		    	}else{
		    		discountMoneyStr="0";
		    	}
//		    	ogp.setItemPrice(sub(product.getPrice(),discountMoneyStr));
		    	ogp.setItemPrice(mul(product.getPrice()+"",product.getQuantity()+""));
		    	//默认
		    	ogp.setpSendType(0);
		    	
		    	ogp.setImagePath(product.getImage_path());
		    	ogp.setItemTotalPrice(mul(product.getPrice(),product.getQuantity()));
		    	ogp.setProductPrice(new BigDecimal(product.getPrice()));
		    	
		    	ogp.setDiscountAmount(new BigDecimal(0));
		    	ogp.setApportedDiscountAmount(new BigDecimal(0));
		    	productList.add(ogp);
			}
	    }
	    
	    og.setProductList(productList);
	} catch (ParseException e) {
		e.printStackTrace();
		og=null;
		return og;
	} 
		return og;
	}
	
	/**
	 * 邻家生成饿了么物流
	 */
	public static ElemeCreateRequestData ljOrderToEleme(OrderGroup og,List<OrderGroupProduct> list) {
		ElemeCreateOrderRequest.ElemeCreateRequestData eleme=new ElemeCreateOrderRequest.ElemeCreateRequestData();
		try {
			// 配送点信息
			ElemeCreateOrderRequest.TransportInfo transport_info=new ElemeCreateOrderRequest.TransportInfo();
			transport_info.setTransport_name(og.getMallName());
			transport_info.setTransport_address(og.getMall_address());
			transport_info.setTransport_tel(og.getMallPhone());
			transport_info.setTransport_longitude(og.getMall_longitude());
			transport_info.setTransport_latitude(og.getMall_latitude());
			transport_info.setTransport_remark("无");
			//取货点经纬度来源, 1:腾讯地图, 2:百度地图, 3:高德地图
			transport_info.setPosition_source(2);
			
			 //收货人信息
	        ElemeCreateOrderRequest.ReceiverInfo receiver_info=new ElemeCreateOrderRequest.ReceiverInfo();
	        receiver_info.setReceiver_address(og.getReceiveAddress());
	        receiver_info.setReceiver_name(og.getReceiveName());
	        receiver_info.setReceiver_primary_phone(og.getReceivePhone());
	        //收货人备用联系电话
	//        receiver_info.setReceiver_second_phone("");
//	        receiver_info.setReceiver_city_code("");
//	        receiver_info.setReceiver_city_name("");
	        receiver_info.setReceiver_longitude(og.getLongitude());
	        receiver_info.setReceiver_latitude(og.getLatitude());
	        //经纬度来源 1:腾讯地图, 2:百度地图, 3:高德地图
	        receiver_info.setPosition_source(2);
	        
	      //商品信息
	        Integer size=list.size();
	        if(size==null){
	        	size=0;
	        }
	        ElemeCreateOrderRequest.ItemsJson[] items_json=new ElemeCreateOrderRequest.ItemsJson[size];
	        int pnum=0;
	        for (int j = 0; j < list.size(); j++) {
	        	ItemsJson itemsJson=new ItemsJson();
	        	OrderGroupProduct ogpro=list.get(j);
	        	itemsJson.setItem_name(ogpro.getpName());
	        	itemsJson.setItem_quantity(ogpro.getQuantity());
	        	itemsJson.setItem_actual_price(ogpro.getItemPrice());
	        	itemsJson.setItem_price(ogpro.getItemTotalPrice());
	        	//是否代购 0:否 1:是
	        	itemsJson.setIs_agent_purchase(0);
	        	//代购进价, 如果需要代购 此项必填
	        	//itemsJson.setAgent_purchase_price(new BigDecimal(""));
	        	//是否需要ele打包 0:否 1:是
	        	itemsJson.setIs_need_package(1);
	        	pnum=pnum+ogpro.getQuantity();
	        	items_json[j]=itemsJson;
			}
	        
			eleme.setTransport_info(transport_info);
			eleme.setReceiver_info(receiver_info);
			eleme.setItems_json(items_json);
			 
			eleme.setPartner_remark(og.getRemark());
			eleme.setPartner_order_code(og.getId()+"");
			eleme.setNotify_url(ElemeOpenConfig.CALL_URL);
			
			/**
	         * 1: 蜂鸟配送, 未向饿了么物流平台查询过站点的订单，支持两小时送达
	         * 2: 定点次日达, 提前向饿了么物流平台查询过配送站点的订单，支持次日送达
	         */
			//订单类型 1: 蜂鸟配送，支持90分钟内送达
			eleme.setOrder_type(1);
	        eleme.setOrder_total_amount(og.getTotalPrice());
            eleme.setOrder_actual_amount(og.getRealPrice());
	        //店超市、其他三类时必填，大于0kg并且小于等于6kg
	    	eleme.setOrder_weight(new BigDecimal(2));
	        eleme.setOrder_remark(og.getRemark());
	        //是否需要发票, 0:不需要, 1:需要
	        eleme.setIs_invoiced(0);
	//      eleme.setInvoice("");
	        Integer pstatus=og.getPayStatus();
	        if(pstatus==null){
	        	pstatus=0;
	        }
	        //订单支付状态 0:未支付 1:已支付
	        eleme.setOrder_payment_status(pstatus);
	        //订单支付方式 1:在线支付
	        eleme.setOrder_payment_method(1);
	        //是否需要ele代收 0:否 1:是
	        eleme.setIs_agent_payment(0);
	        //需要代收时客户应付金额, 如需代收款 此项必填
	//      eleme.setRequire_payment_pay(new BigDecimal(""));    //传个默认0.0
	        eleme.setGoods_count(pnum);
	        Long sendHourTimes = 0L;
			Long receive_time=0L;
			Date sendDate=og.getSendDate();
			String ogHour=og.getSendHour();
			if(sendDate==null){
				  Date d = new Date();
				  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				  String dateNowStr = sdf.format(d);
				  sendDate=sdf.parse(dateNowStr);
			}
			if(ogHour==null||ogHour.isEmpty()){
				ogHour="0";
			}
			if(!Tools.isEmpty(ogHour)){
				String[] sendHour = ogHour.split(":");
				if(sendHour.length > 1){
					sendHourTimes = Long.valueOf(sendHour[0])*3600*1000 + Long.valueOf(sendHour[1])*60*1000;
					receive_time=sendDate.getTime()+sendHourTimes;
				}else{
					receive_time=sendDate.getTime()+Long.valueOf(ogHour)*3600*1000;
				}
			}
	        eleme.setRequire_receive_time(receive_time);
	        Date creTime=og.getCreDate();
	        if(creTime==null){
	        	creTime=new Date();
	        }
	        eleme.setOrder_add_time(creTime.getTime());
		} catch (ParseException e) {
			eleme=null;
			e.printStackTrace();
			return eleme;
		}
		return eleme;
	}
	
	// 进行除法运算
	public static BigDecimal bigDecimalToPrice(Integer b){
		if(b==null){
			return new BigDecimal(0);
		}
		BigDecimal bf=new BigDecimal(b);
		BigDecimal cf=new BigDecimal(100);
		return bf.divide(cf, 2, RoundingMode.HALF_UP);
	}
	
	// 进行乘法运算
	public static BigDecimal mul(String d1, String d2){  
		if(d1.isEmpty()){
			d1="0";
		}
		if(d2.isEmpty()){
			d2="0";
		}
	    BigDecimal b1 = new BigDecimal(d1);
	    BigDecimal b2 = new BigDecimal(d2);
	 return b1.multiply(b2);
	}
	
	// 进行减法运算
	 public static BigDecimal sub(String d1, String d2){   
		 if(d1.isEmpty()){
				d1="0";
			}
			if(d2.isEmpty()){
				d2="0";
			}
		 // 进行减法运算
	     BigDecimal b1 = new BigDecimal(d1);
	     BigDecimal b2 = new BigDecimal(d2);
	  return b1.subtract(b2);
	 }
	 
}
