package com.linjia.web.thirdService.map.service;

import org.json.JSONObject;

public interface MapService {
	
	/**
	 * 获取地址经纬度
	 * @param address
	 * @return
	 */
    JSONObject getLatitude(String address);
	
	String getLocationMap(String location, String title, String content);
	
	/**
	 * 计算配送距离
	 * @param beginLat 开始点纬度
	 * @param beiginLon 开始点经度
	 * @param mall_code 门店code
	 * @param endLat 结束点纬度
	 * @param endLon 结束点经度
	 * @return
	 */
    Integer getSendDistanc(String mall_code, String endLat, String endLon);

}
