package com.linjia.web.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import o2o.openplatform.sdk.util.SignUtils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import com.linjia.constants.Constants;
import com.linjia.tools.DateComFunc;
import com.linjia.tools.JSONUtil;
import com.linjia.tools.NetRequest;
import com.linjia.tools.RandUtils;
import com.linjia.tools.RedisUtil;
import com.linjia.tools.UUID;
import com.linjia.tools.Util;
import com.linjia.web.dao.UserMapper;
import com.linjia.web.model.User;
import com.linjia.web.query.ERPProductQtyBean;
import com.linjia.web.service.UserService;
import com.linjia.web.service.impl.UserServiceImpl;
import com.linjia.web.thirdService.baidu.model.Cmd;
import com.linjia.web.thirdService.baidu.utils.BaiduUtil;
import com.linjia.web.thirdService.jddj.config.JDDJConfigure;
import com.linjia.web.thirdService.jddj.config.JDDJUtil;
import com.linjia.web.thirdService.jddj.model.WebRequestDTO2;
import com.linjia.web.thirdService.message.utils.MessageUtil;
import com.linjia.wxpay.common.AES;
import com.linjia.wxpay.common.Configure;
import com.linjia.wxpay.common.MD5;
import com.linjia.wxpay.common.RandomStringGenerator;
import com.linjia.wxpay.common.Signature;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;


/** 
 * @author  lixinling: 
 * @date 2016年7月1日 下午3:28:35 
 * @version 1.0 
 */
public class TestP {
	
	
	@Test
	public void test() throws IOException {
//		Date d = DateComFunc.getHourDate(new Date());
		
//		System.out.println(DateComFunc.formatDate(d, "yyyy-MM-dd HH:mm:ss.sss"));
//		System.out.println(DateComFunc.getSpecifyHourDate(new Date(), Constants.MIAOSHA_TIME.TWELVE));
//		System.out.println(DateComFunc.formatDate(DateComFunc.getSpecifyHourDate(new Date(), Constants.MIAOSHA_TIME.TWELVE), "yyyy-MM-dd HH:mm:ss.sss"));
		
		
		//倒计时时间
		/*long diff = 7690548;
		long hour = diff/(60*60*1000);
		long min = diff/(60*1000) - hour*60;
		long sec = diff/1000 - hour*60*60 - min*60;
		System.out.println(hour + ":"+min+":"+sec);*/
		
		
		/*BigDecimal totalPrice = new BigDecimal("1.00");
		BigDecimal salePrice = new BigDecimal("7.20");
		int buyQty = 3;
		totalPrice = totalPrice.add(salePrice.multiply(new BigDecimal(String.valueOf(buyQty))));
//		BigDecimal result = salePrice.multiply(new BigDecimal(String.valueOf(buyQty)));
//		totalPrice.add(result);
		System.out.println("totalPrice:"+totalPrice);*/
		
		/*1、转换符
		%s: 字符串类型，如："ljq"
		%b: 布尔类型，如：true
		%d: 整数类型(十进制)，如：99
		%f: 浮点类型，如：99.99
		%%: 百分比类型，如：％
		%n: 换行符
		 */
		/*String str=null;
	    str=String.format("Hi, %s", "林计钦"); // 格式化字符串
	    System.out.println(str); // 输出字符串变量str的内容
	    System.out.printf("3>7的结果是：%b %n", 3>7);
	    System.out.printf("100的一半是：%d %n", 100/2);
	    System.out.printf("50元的书打8.5折扣是：%f 元%n", 50*0.85);
	    System.out.printf("上面的折扣是%d%% %n", 85);*/
		
		/*String urlstr = "http://124.193.154.242:37080/h4rest-server/rest/h5rest-server/core/mdataservice/store/100007";
		String urlstr = "http://124.193.154.242:58280/hdcard-services/api/member/query_by_mobilenum?mobilenum=18510576310";
//		String urlstr = "http://yeeda-app-server.iyeeda.com/YidaAppServer_1_1/getMainPageList?usrid=13332&page=0&appv=235";
		String Authorization = "guest:guest";
		JSONObject obj = NetRequest.requestGet(urlstr, null, Authorization);
		System.out.println("result:"+obj.toString());*/
		
		//取得门店库存
//		String mallCode = "100007";
//		String pCode = "6944732100390";
//		int qty = Util.queryQtyByStore(mallCode, pCode);
//		System.out.println(mallCode+"门店库存数："+qty);
		
		//取得会员信息
//		String phone ="18510576310";
//		JSONObject result = Util.queryMemberByMobile(phone);
//		System.out.println("会员信息:"+result);
		
//		System.out.println(DateComFunc.formatDate(new Date(), "yyMMdd"));
		
		/*User u = new User(2, 25, "LXL", 1, "123", 111L,"13332");
		XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		xStreamForRequestPostData.alias("xml", User.class);
        String postDataXML = xStreamForRequestPostData.toXML(u);
        System.out.println(postDataXML);
        
        User u2 = (User)com.linjia.wxpay.common.Util.getObjectFromXML(postDataXML, User.class);
        System.out.println(u2.toString());
        */
		
		//根据自定义查询条件, 查询储值流水
//		JSONObject result = Util.queryDesaccounthst(null, null, "WEB151215000099", null);
//		System.out.println(result.toString());
		
		//将汉字转成unicode字符
		String s = "wojiuceshi其他未说明原因";
//		System.out.println("\u8ba2\u53551201608050000044763\u6263\u51cf");
//		String result = StringEscapeUtils.escapeJava(s);
//		System.out.println("result:" + result);
		
//		System.out.println(s.charAt(10)=='其');
		
	/*	System.out.println(s.toString() + System.currentTimeMillis());
		
		
		String signJson = "{\"body\":{\"shop\":{\"id\":\"2520\",\"name\":\"百度外卖（测试店）\"},\"order\":{\"order_id\":\"14347012309352\",\"send_immediately\":1,\"send_time\":\"1\",\"send_fee\":500,\"package_fee\":200,\"discount_fee\":0,\"total_fee\":3700,\"shop_fee\":3200,\"user_fee\":3700,\"pay_type\":1,\"pay_status\":1,\"need_invoice\":2,\"invoice_title\":\"\",\"remark\":\"请提供餐具\",\"delivery_party\":1,\"create_time\":\"1434701230\"},\"user\":{\"name\":\"测试订单请勿操作\",\"phone\":\"18912345678\",\"gender\":1,\"address\":\"北京海淀区奎科科技大厦 测试\",\"coord\":{\"longitude\":116.143145,\"latitude\":39.741426}},\"products\":[{\"product_id\":\"12277320\",\"product_name\":\"酱肉包（/份）\",\"product_price\":1200,\"product_amount\":1,\"product_fee\":1200,\"package_price\":100,\"package_amount\":1,\"package_fee\":100,\"total_fee\":1300,\"upc\":\"\"}],\"discount\":[]},\"cmd\":\"order.create\",\"sign\":\"E362B8AACE4F7A77047A885C0C0D230D\",\"source\":\"65400\",\"ticket\":\"909C3B92-8CDD-AF2C-C887-5B660233E2B2\",\"timestamp\":1434701234,\"version\":\"2.0\"}";
		
		//中文字符转为unicode
		String requestJson = BaiduUtil.chinaToUnicode(signJson);
		JSONObject body = new JSONObject(requestJson);
		JSONObject res = NetRequest.requestPost("http://api.waimai.baidu.com", body, null);
		System.out.println(res.toString());*/
		
//		int r = MessageUtil.randomCheckCode();
//		RedisUtil.put("name", "lxl");
//		System.out.println(RedisUtil.get("name"));
//		System.out.println("TakeGoodsCode :"+RandUtils.getTakeGoodsCode());
		
		/*JSONArray ERPProductsArray = new JSONArray();
		ERPProductQtyBean erpProductQtyBean = new ERPProductQtyBean();*/
//		JSONObject pa = new JSONObject();
//		pa.put("skuId", "2506000029112");
//		pa.put("qty", 1);
//		pa.put("price", 10);
//		erpProductQtyBean.setSkuId("2506000029112");
//		erpProductQtyBean.setQty(1);
//		erpProductQtyBean.setPrice(new BigDecimal("7.5"));
//		ERPProductsArray.put(JSONUtil.ObjToJSON(erpProductQtyBean));
//		JSONObject res = Util.decERPQuantity("100001", 2016082412563248l, ERPProductsArray);
//		System.out.println(res.toString());
//		String result="appid=wxb995a054c8743068&nonceStr=imm3awmh5303zhnhr3gf87z9vcxisl8p&package=prepay_id=wx201609141442329c6221097a0568415320&signType=MD5&timeStamp=1473835303&key=1e4fa96daCb04664B4580346267851f6";
//		System.out.println(MD5.MD5Encode(result).toUpperCase());
//		System.out.println(UUID.getUID());
		
//		System.out.println(System.currentTimeMillis()/1000);
		/*Map<String,Object> params = new HashMap<String, Object>();
		params.put("appId", "wxb995a054c8743068");
		params.put("timeStamp", (1473835303) + "");
		params.put("nonceStr", "imm3awmh5303zhnhr3gf87z9vcxisl8p");
		params.put("package", "prepay_id=wx201609141442329c6221097a0568415320");
		params.put("signType", "MD5");
		
		//根据API给的签名规则进行签名
        String sign = Signature.getSign(params);
		System.out.println(sign);*/
//		Util.incScoreAccount(System.currentTimeMillis()/1000, "WEB160628000066", "测试", 101, 200);
		
//		List<Double> l = new ArrayList<Double>();
//		l.add(2.3);
//		l.add(3.5);
//		l.add(1.2);
//Collections.sort(l);
//		System.out.println(l.get(0));
//		BigDecimal tmp_itemPrice_Promotion = new BigDecimal("2.5");
//		BigDecimal itemPrice_Promotion = new BigDecimal("2.5");
//		
//		System.out.println(tmp_itemPrice_Promotion.compareTo(itemPrice_Promotion));
		
//		JSONObject result = Util.accountcashdeposit("WEB160628000066", "2000072", "1.00", "1.00", "2016-09-30 17:16:02", 1475226962L);
//		System.out.println(Util.queryQtyByStore("100019", "6928494805533"));
//		System.out.println(Util.queryDesaccounthst(null, null, "WEB160923002321", null).toString());
		/*Map<String,String> hash = new HashMap<String,String>();
		hash.put("name", "LXL");
		hash.put("age", "18");
		hash.put("sex", "男");
		RedisUtil.hmset("user", hash);
		System.out.println("========="+RedisUtil.hget("user","age"));*/
		/*
		hash.put("age", "20");
		hash.put("name", "LYX");
		RedisUtil.hmset("user", hash);
		System.out.println("========="+RedisUtil.hget("user","name")); */
		/*String[] field = {"10001","10002","10003"};
		System.out.println("========="+RedisUtil.hmget("store_store:0001",field));*/
//		System.out.println("========="+RedisUtil.hgetAll("store_store:0001"));
		
		/*JSONObject re = Util.updatePursePayPwd("WEB160628000066", "123456", "665544");
		System.out.println(re.toString());*/
		
//		String encrypted = AES.aesEncrypt("test", "12345678");
//		String encrypted = Signature.getAESEncrypt("test", "12345678");
//		System.out.println("加密后:"+encrypted);
//		System.out.println("解密后:"+Signature.getAESDecrypt("D61C72CF09F143AB5DAE836253655594", "WEB160628000066"));
		
		/*StringBuilder sb = new StringBuilder();
		char[] ch = decrypted.toCharArray();
		for(int i=ch.length-1; i>=0 ;i--){
			sb.append(ch[i]);
		}
		System.out.println("解密后111:" + sb.toString());*/
//		System.out.println(DateComFunc.getDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
		
		/*List<String> users=new ArrayList<String>();
		users.add("100002151203000001");
		users.add("100002151203000002");
		users.add("100002151203000003");
		users.add("100002151203000004");
		users.add("100002151203000005");
		System.out.println(users.size());
		users.remove(0);
		System.out.println(users.size());
		System.out.println(users.get(0).toString());*/
		
		//DecimalFormat默认是四舍六入的
		/*DecimalFormat df = new DecimalFormat("#.00");
		System.out.println(df.format(2.368));
		System.out.println(df.format(3.234));
		System.out.println(df.format(3.235));
		System.out.println(df.format(3.236));
		
		//真正的四舍五入
		BigDecimal bdnum = new BigDecimal("5.245");
		System.out.println(bdnum.setScale(2, BigDecimal.ROUND_HALF_UP));*/
		
//		Assert.hasText("aa","参数错误");
		
		/*-------------------------京东到家签名验证 START------------------------------*/
		
/*		Map<String,Object> param = new HashMap<String,Object>();
		param.put("app_key", "49e6c8e5ece34545b1329640c4e56a69");
		param.put("v", "1.0");
		param.put("format", "json");
		param.put("jd_param_json", "{\"billId\":\"10003129\",\"statusId\":\"32000\",\"timestamp\":\"2015-10-16 13:23:30\"}");
		param.put("timestamp", "2017-02-22 09:52:43");
		param.put("token", "057336cb-8f0c-41a0-963a-c1e0c3c63446");
		
		WebRequestDTO2 webRequestDTO = new WebRequestDTO2();
		webRequestDTO.setApp_key("49e6c8e5ece34545b1329640c4e56a69");
		webRequestDTO.setV("1.0");
		webRequestDTO.setFormat("json");
		webRequestDTO.setSign("E5B7D18EF434E43AF6A951B44DB76485");
		webRequestDTO.setJd_param_json("{\"billId\":\"10003129\",\"statusId\":\"32000\",\"timestamp\":\"2015-10-16 13:23:30\"}");
		webRequestDTO.setTimestamp("2017-02-22 09:52:43");
		webRequestDTO.setToken("057336cb-8f0c-41a0-963a-c1e0c3c63446");
		
		try {
			System.out.println("原："+SignUtils.getSignByMD5(webRequestDTO,JDDJConfigure.getApp_Secret()));
			System.out.println("新："+JDDJUtil.getSignByMD5(param, JDDJConfigure.getApp_Secret()));
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		/*-------------------------京东到家签名验证 END------------------------------*/
		/*int i=1;
		byte b =1;
		while(b<=100){
			b--;
			System.out.println("i=" + i);
			i++;
		}*/
		
		System.out.println(DateComFunc.strToDate("2017-03-09 19:08:00", "yyyy-MM-dd HH:mm:ss").getTime() - System.currentTimeMillis());
		
	}
	

}
