package com.linjia.web.test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.linjia.web.thirdService.baidu.Serializer.CmdSerializer;
import com.linjia.web.thirdService.baidu.Serializer.ShopSerializer;
import com.linjia.web.thirdService.baidu.model.Cmd;
import com.linjia.web.thirdService.baidu.model.Shop;

public class Test {
	/**
	 * 合作方唯一标示，由百度外卖分配
	 */
	public static final String SOURCE = "65514";
	/**
	 * 合作方密钥，由百度外卖分配
	 */
	public static final String SECRET = "53eb868ccacd5cee";
	
	/**
	 * 签名计算的样例
	 * @param args
	 */
	public static void main(String[] args) {
		//准备body数据
		Shop shop = new Shop();
		shop.setMall_code("10027821");
		//shop.setName("测试商户<>http://abc.com/a.jpg");
		
		//准备CMD签名计算数据
		Cmd cmd = new Cmd();
		cmd.setCmd("shop.get");
		cmd.setSource(SOURCE);
		cmd.setSecret(SECRET);
		cmd.setTicket(UUID.randomUUID().toString().toUpperCase());
		cmd.setTimestamp((int)(System.currentTimeMillis() / 1000)+"");
		cmd.setVersion("2");
		cmd.setBody(shop);
		cmd.setSign(null);
		
		Gson gson = new GsonBuilder()
			.registerTypeAdapter(Cmd.class, new CmdSerializer())
			.registerTypeAdapter(Shop.class, new ShopSerializer())
			.disableHtmlEscaping()
			.create();
		String signJson = gson.toJson(cmd);
		//对所有/进行转义
		signJson = signJson.replace("/", "\\/");
		//中文字符转为unicode
		signJson = chinaToUnicode(signJson);
		System.out.println(signJson);
		String sign = getMD5(signJson);
		System.out.println(signJson);
		//准备生成请求数据，此处注意secret不参与传递，故设置为null
		cmd.setSign(sign);
		cmd.setSecret(null);
		String requestJson = gson.toJson(cmd);
		//对所有/进行转义
		requestJson = requestJson.replace("/", "\\/");
		//中文字符转为unicode
		requestJson = chinaToUnicode(requestJson);
		
		System.out.println(requestJson);
	}
	
	/**
	 * 计算MD5
	 * @param input
	 * @return
	 */
	public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext.toUpperCase();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 把中文转成Unicode码
	 * @param str
	 * @return
	 */
	public static String chinaToUnicode(String str){
		String result="";
		for (int i = 0; i < str.length(); i++){
            int chr1 = str.charAt(i);
            if(chr1>=19968&&chr1<=171941){//汉字范围 \u4e00-\u9fa5 (中文)
                result+="\\u" + Integer.toHexString(chr1);
            }else{
            	result+=str.charAt(i);
            }
        }
		return result;
	}
}
