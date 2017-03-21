package com.linjia.web.thirdService.map.constants;

public class MapUrl {

//	public static String GeocodingAPI="http://api.map.baidu.com/geocoder/v2/?address=北京市西城区北礼士路在地图中查看甲98号1幢阜成大厦B座六层&output=json&ak=";

	//地址转经纬度
	public static String ADDRESSTOLNGLAT="http://api.map.baidu.com/geocoder/v2/";
	//经纬度转地址
	public static String LNGLATTOADDRESS="http://api.map.baidu.com/geocoder/v2/";
//	http://api.map.baidu.com/geocoder/v2/?address=北京市西城区北礼士路在地图中查看甲98号1幢阜成大厦B座六层&output=json&ak=&sn=

	//web端URI API
	public static String MARKWEBURL="http://api.map.baidu.com/marker";
	
//	http://api.map.baidu.com/marker?location=39.931604327639064,116.36136408266799&title=我的位置&content=阜成大厦B座&output=html

//	驾车
	public static String DRIVEDISTANCE="http://api.map.baidu.com/routematrix/v2/driving";
//	骑行
	public static String RIDEDISTANCE="http://api.map.baidu.com/routematrix/v2/riding";
//	步行
	public static String WALKDISTANCE="http://api.map.baidu.com/routematrix/v2/walking";
}