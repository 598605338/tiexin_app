package com.linjia.web.test;

import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.linjia.web.thirdService.baidu.Serializer.CmdSerializer;
import com.linjia.web.thirdService.baidu.Serializer.GoodsSerializer;
import com.linjia.web.thirdService.baidu.Serializer.BaiduDataSerializer;
import com.linjia.web.thirdService.baidu.constants.BaiduConfig;
import com.linjia.web.thirdService.baidu.model.BaiduData;
import com.linjia.web.thirdService.baidu.model.Cmd;
import com.linjia.web.thirdService.baidu.model.Goods;
import com.linjia.web.thirdService.baidu.utils.BaiduUtil;

public class Test01 {
  private static int time=(int)(System.currentTimeMillis()/1000);
	/**
	 * 签名计算的样例
	 * @param args
	 */
	public static String getReqJson(){
		BaiduData bd=BdOrderTestData.getBaiduData();
//		OrderData bd=null;
		//准备CMD签名计算数据
		Cmd cmd = new Cmd();
		cmd.setCmd("shop.create");
		cmd.setSource("test_source");
		cmd.setSecret("test_secret");
		cmd.setTicket("CBB291F6-33BE-57CC-8FE3-441FE6E7BA6C");
		cmd.setTimestamp(1430719064+"");
		cmd.setVersion("2");
		cmd.setBody(bd);
		cmd.setSign(null);
		Gson gson = new GsonBuilder().registerTypeAdapter(Cmd.class, new CmdSerializer())
				.registerTypeAdapter(BaiduData.class, new BaiduDataSerializer()).disableHtmlEscaping().create();
		String signJson = gson.toJson(cmd);
		System.out.println("signJson:"+signJson);
		//对所有/进行转义
		signJson = signJson.replace("/", "\\/");
		//中文字符转为unicode
		signJson = BaiduUtil.chinaToUnicode(signJson);
		String sign = BaiduUtil.getMD5(signJson);
		System.out.println("sign1:"+sign);
		//准备生成请求数据，此处注意secret不参与传递，故设置为null
		cmd.setSign(sign);
		cmd.setSecret(null);
		String requestJson = gson.toJson(cmd);
		//对所有/进行转义
		requestJson = requestJson.replace("/", "\\/");
		//中文字符转为unicode
		requestJson = BaiduUtil.chinaToUnicode(requestJson);
		System.out.println("requestJson1:"+requestJson);
//		System.out.println("requestJson11:"+JSON.parse(requestJson).toString());
		return requestJson;
	}
	
	/**
	 * 签名计算的样例
	 * @param args
	 */
	public static void test(){
		//准备body数据
		BaiduData bd=BdOrderTestData.getBaiduData();
//		OrderData bd=null;
		//准备CMD签名计算数据
		Cmd cmd = new Cmd();
		cmd.setCmd("shop.create");
		cmd.setSource("test_source");
		cmd.setSecret("test_secret");
		cmd.setTicket("CBB291F6-33BE-57CC-8FE3-441FE6E7BA6C");
		cmd.setTimestamp(1430719064+"");
		cmd.setVersion("2");
		cmd.setBody(bd);
		cmd.setSign(null);
		
		Gson gson = new GsonBuilder()
			.registerTypeAdapter(Cmd.class, new CmdSerializer())
			.registerTypeAdapter(BaiduData.class, new BaiduDataSerializer())
			.disableHtmlEscaping()
			.create();
		String signJson = gson.toJson(cmd);
		//对所有/进行转义
		signJson = signJson.replace("/", "\\/");
		//中文字符转为unicode
		signJson = BaiduUtil.chinaToUnicode(signJson);
//		System.out.println(signJson);
		String sign = BaiduUtil.getMD5(signJson);
		System.out.println("sign2:"+sign);
		//准备生成请求数据，此处注意secret不参与传递，故设置为null
		cmd.setSign(sign);
		cmd.setSecret(null);
		String requestJson = gson.toJson(cmd);
		//对所有/进行转义
		requestJson = requestJson.replace("/", "\\/");
		//中文字符转为unicode
		requestJson = BaiduUtil.chinaToUnicode(requestJson);
		System.out.println("requestJson2:"+requestJson);
	}
	
	public static void main(String[] args) {
		getReqJson();
		test();
	}
	
//	/**
//	 * 签名计算的样例
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		//准备body数据
//		Goods bd=BdOrderTestData.getGoods();
//		//shop.setName("测试商户<>http://abc.com/a.jpg");
//		
//		//准备CMD签名计算数据
//		Cmd cmd = new Cmd();
//		cmd.setCmd("sku.create");
//		cmd.setSource(BaiduConfig.getSource());
//		cmd.setSecret(BaiduConfig.getSecret());
//		cmd.setTicket(UUID.randomUUID().toString().toUpperCase());
//		cmd.setTimestamp((int)(System.currentTimeMillis() / 1000));
//		cmd.setVersion("2");
//		cmd.setBody(bd);
//		cmd.setSign(null);
//		
//		Gson gson = new GsonBuilder()
//			.registerTypeAdapter(Cmd.class, new CmdSerializer())
//			.registerTypeAdapter(Goods.class, new GoodsSerializer())
//			.disableHtmlEscaping()
//			.create();
//		String signJson = gson.toJson(cmd);
//		//对所有/进行转义
//		signJson = signJson.replace("/", "\\/");
//		//中文字符转为unicode
//		signJson = BaiduUtil.chinaToUnicode(signJson);
////		System.out.println(signJson);
//		String sign = BaiduUtil.getMD5(signJson);
//		
//		//准备生成请求数据，此处注意secret不参与传递，故设置为null
//		cmd.setSign(sign);
//		cmd.setSecret(null);
//		String requestJson = gson.toJson(cmd);
//		//对所有/进行转义
//		requestJson = requestJson.replace("/", "\\/");
//		//中文字符转为unicode
//		requestJson = BaiduUtil.chinaToUnicode(requestJson);
//		
//		System.out.println(requestJson);
//	}
}
