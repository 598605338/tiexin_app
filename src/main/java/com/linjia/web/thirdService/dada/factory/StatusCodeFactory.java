package com.linjia.web.thirdService.dada.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 常量类，放置返回状态吗和表示的信息键值对
 * 
 * @author xiangsy Create By 2016年7月27日 上午11:19:53
 */
public class StatusCodeFactory {

		private static Map<Integer, String> statusMAP;

		static {
			statusMAP = new HashMap<Integer, String>();
			statusMAP.put(2001,"请求参数错误");
			statusMAP.put(2100,"请求参数错误");
			statusMAP.put(2101,"暂不接受价格超过1000的商品");
			statusMAP.put(2102,"token不能为空");
			statusMAP.put(2103,"token没有正确匹配");
			statusMAP.put(2104,"原始订单id不能为空");
			statusMAP.put(2105,"订单说明信息不能为空");
			statusMAP.put(2106,"付给商家的费用不能为空");
			statusMAP.put(2107,"向客户收取的费用不能为空");
			statusMAP.put(2108,"订单运费不能为空");
			statusMAP.put(2109,"垫付不能为空");
			statusMAP.put(2110,"订单创建时间不能为空");
			statusMAP.put(2111,"订单商品类型不能为空");
			statusMAP.put(2112,"订单商品重量不能为空");
			statusMAP.put(2113,"订单商品价格不能为空");
			statusMAP.put(2114,"发货人姓名不能为空");
			statusMAP.put(2115,"发货地址不能为空");
			statusMAP.put(2116,"发货人手机不能为空");
			statusMAP.put(2117,"发货人座机不能为空");
			statusMAP.put(2118,"发货人(商家)纬度不能为空");
			statusMAP.put(2119,"发货人(商家)经度不能为空");
			statusMAP.put(2120,"收货人姓名不能为空");
			statusMAP.put(2121,"收货人地址不能为空");
			statusMAP.put(2122,"收货人手机不能为空");
			statusMAP.put(2123,"回调URL不能为空");
			statusMAP.put(2124,"期望取货时间不能为空");
			statusMAP.put(2125,"期望送达时间不能为空");
			statusMAP.put(2126,"订单所在城市不能为空");
			statusMAP.put(2127,"发票抬头不能为空");
			statusMAP.put(2128,"没有该订单");
			statusMAP.put(2129,"没有该配送员");
			statusMAP.put(2130,"订单ID不能为空");
			statusMAP.put(2131,"配送员ID不能为负值");
			statusMAP.put(2132,"订单ID和配送员ID至少填一个");
			statusMAP.put(2133,"订单已存在");
			statusMAP.put(2134,"城市权限不够(请确认申请的城市和参数city_code值一致)");
			statusMAP.put(2140,"错误的消息体签名参数");
			statusMAP.put(2141,"非对外接口");
			statusMAP.put(2142,"当前订单不是待取货状态");
			statusMAP.put(2143,"当前订单不是待完成状态");
			statusMAP.put(2144,"权限不够，禁止发单，请联系达达平台");
			statusMAP.put(2145,"city_name参数格式错误(如“北京市”,请传“北京”)");
			statusMAP.put(2146,"该订单状态不是待接单或待取货，不允许取消，如果需取消请联系客服");
			statusMAP.put(2147,"达达在取货路上，只有超过15分钟达达未来取货，才允许取消");
			statusMAP.put(2148,"该订单已被取消，不允许重复取消");
			statusMAP.put(2149,"缺少取消原因");
			statusMAP.put(2150,"该账号没有高级接口权限");
			statusMAP.put(2151,"订单号origin_id没有发过订单，不能再次发布订单");
			statusMAP.put(2152,"订单号origin_id对应的订单，没有被取消或过期，不允许再次发单");
			statusMAP.put(2155,"该账户余额不足,请充值");
			statusMAP.put(2156,"该账户未开通扫码取货权限");
			statusMAP.put(2157,"缺少仓库ID");
			statusMAP.put(2158,"缺少配送员ID");
			statusMAP.put(2159,"缺少分仓ID或没有找到该仓库");
			statusMAP.put(2160,"该仓库没有绑定班车");
			statusMAP.put(2161,"该账户非预充值商家，没有添加小费的权限");
			statusMAP.put(2163,"没有对应的达达配送员");
			statusMAP.put(2164,"此达达配送员不符合追单的条件");
			statusMAP.put(2165,"该账户非预充值商家，没有取消已接单订单的权限");
			statusMAP.put(2167,"配送距离超出系统限制，请检查配送地址或经纬度信息");
			statusMAP.put(2170,"期望完成时间不合法（必须小于明天的当前时间点）");
			statusMAP.put(2201,"缺少response_type值");
			statusMAP.put(2202,"不合法的response_type值");
			statusMAP.put(2203,"缺少app_key值");
			statusMAP.put(2204,"不合法的app_key值");
			statusMAP.put(2205,"缺少scope值");
			statusMAP.put(2206,"不合法的scope");
			statusMAP.put(2208,"缺少grant_code值");
			statusMAP.put(2209,"不合法的grant_code值");
			statusMAP.put(2210,"缺少grant_type值");
			statusMAP.put(2211,"不合法的grant_type值");
			statusMAP.put(2212,"已过期或错误的grant_code");
			statusMAP.put(2214,"缺少refresh_token值");
			statusMAP.put(2215,"参数refresh_token值不正确");
			statusMAP.put(2216,"已过期的refresh_token值");
			statusMAP.put(2217,"已过期的token值");
			statusMAP.put(2218,"缺少signature值");
			statusMAP.put(2219,"不合法的signature值");
			statusMAP.put(2222,"缺少timestamp值");
			statusMAP.put(2223,"不合法的timestamp值");
			statusMAP.put(2224,"不合法的token");
			statusMAP.put(2300,"发货地址无法解析");
			statusMAP.put(2301,"收货地址无法解析");
			statusMAP.put(2302,"发货地址与城市不匹配");
			statusMAP.put(2303,"配送距离大于限定距离");
			statusMAP.put(2304,"收货地址超出配送区域");
			statusMAP.put(2410,"订单已被抢，不允许修改");
			statusMAP.put(2702,"订单号不存在");
			statusMAP.put(3000,"发单服务异常，请联系技术");
			statusMAP.put(20001,"请求参数错误，请联系技术");
			statusMAP.put(20002,"疑似重复订单，请三分钟后再试");
		}

    public static String getMapMessage(int key){
			String message=statusMAP.get(key);
			return message;
		}
}

