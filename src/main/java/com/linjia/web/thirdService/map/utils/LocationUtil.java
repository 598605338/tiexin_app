package com.linjia.web.thirdService.map.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.linjia.web.thirdService.map.constants.MapConfig;
import com.linjia.web.thirdService.map.constants.MapUrl;

public class LocationUtil {

//	private static final String BAIDU_APP_KEY = "42b8ececa9cd6fe72ae4cddd77c0da5d";

	/**
	 * 返回输入地址的经纬度坐标 key lng(经度),lat(纬度)
	 */
	public static Map<String, String> getLatitude(String address) {
		try {
			// 将地址转换成utf-8的16进制
			address = URLEncoder.encode(address, "UTF-8");
			// 如果有代理，要设置代理，没代理可注释
			// System.setProperty("http.proxyHost","192.168.172.23");
			// System.setProperty("http.proxyPort","3209");
			String url="http://api.map.baidu.com/geocoder/v2/?address="+ address + "&output=json&ak=" + MapConfig.AK;
			URL resjson = new URL(url);
			System.out.println("url:"+url);
			BufferedReader in = new BufferedReader(new InputStreamReader(resjson.openStream()));
			String res;
			StringBuilder sb = new StringBuilder("");
			while ((res = in.readLine()) != null) {
				sb.append(res.trim());
			}
			in.close();
			String str = sb.toString();
			System.out.println("return json:" + str);
			if (str != null && !str.equals("")) {
				Map<String, String> map = null;
				int lngStart = str.indexOf("lng\":");
				int lngEnd = str.indexOf(",\"lat");
				int latEnd = str.indexOf("},\"precise");
				if (lngStart > 0 && lngEnd > 0 && latEnd > 0) {
					String lng = str.substring(lngStart + 5, lngEnd);
					String lat = str.substring(lngEnd + 7, latEnd);
					map = new HashMap<String, String>();
					map.put("lng", lng);
					map.put("lat", lat);
					return map;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 返回输入地址的经纬度坐标 key lng(经度),lat(纬度)
	 */
	public static Map<String, String> getLatitude1(String address) {
		try {
			Map paramsMap = new LinkedHashMap<String, String>();
			paramsMap.put("address", "百度大厦");
			paramsMap.put("output", "json");
			paramsMap.put("ak", "BG21Xi53lMAAc6qHbIG63kyGs9UBHHO6");
			String sn=SnCal.createGetSn(paramsMap);
			String url=MapUrl.ADDRESSTOLNGLAT;
			if(paramsMap!=null){
		        Iterator<Map.Entry<String, Object>> iter = paramsMap.entrySet().iterator();
		        StringBuffer urlParamsBuffer = new StringBuffer();
		        while(iter.hasNext()) {
		            Map.Entry<String, Object> entry = iter.next();
		            urlParamsBuffer.append(entry.getKey()+"="+entry.getValue()+"&");
		        }
		        if(urlParamsBuffer.length() > 0) {
		            urlParamsBuffer.deleteCharAt(urlParamsBuffer.length() - 1);
		            url += '?'+ urlParamsBuffer.toString()+"&sn="+sn;
		        }
	    	}
			System.out.println("url:"+url);
			// 将地址转换成utf-8的16进制
			url = URLEncoder.encode(url, "UTF-8");
			// 如果有代理，要设置代理，没代理可注释
			// System.setProperty("http.proxyHost","192.168.172.23");
			// System.setProperty("http.proxyPort","3209");
//			String url="http://api.map.baidu.com/geocoder/v2/?address="+ address + "&output=json&ak=" + MapConfig.AK;
			System.out.println("url:"+url);
			URL resjson = new URL(url);
			BufferedReader in = new BufferedReader(new InputStreamReader(resjson.openStream()));
			String res;
			StringBuilder sb = new StringBuilder("");
			while ((res = in.readLine()) != null) {
				sb.append(res.trim());
			}
			in.close();
			String str = sb.toString();
			System.out.println("return json:" + str);
			if (str != null && !str.equals("")) {
				Map<String, String> map = null;
				int lngStart = str.indexOf("lng\":");
				int lngEnd = str.indexOf(",\"lat");
				int latEnd = str.indexOf("},\"precise");
				if (lngStart > 0 && lngEnd > 0 && latEnd > 0) {
					String lng = str.substring(lngStart + 5, lngEnd);
					String lat = str.substring(lngEnd + 7, latEnd);
					map = new HashMap<String, String>();
					map.put("lng", lng);
					map.put("lat", lat);
					return map;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String args[]) {
		Map<String, String> map = LocationUtil.getLatitude1("北京市西城区北礼士路在地图中查看甲98号1幢阜成大厦B座六层");
		if (null != map) {
			System.out.println(map.get("lng"));
			System.out.println(map.get("lat"));
		}
	}
}
