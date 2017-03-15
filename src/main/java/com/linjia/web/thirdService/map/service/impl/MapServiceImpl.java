package com.linjia.web.thirdService.map.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.linjia.web.dao.MallMasterMapper;
import com.linjia.web.model.MallMaster;
import com.linjia.web.thirdService.dada.utils.HttpRequestUtil;
import com.linjia.web.thirdService.map.constants.MapConfig;
import com.linjia.web.thirdService.map.constants.MapUrl;
import com.linjia.web.thirdService.map.service.MapService;
import com.linjia.web.thirdService.map.utils.SnCal;

@Service
public class MapServiceImpl implements MapService{
	
	@Resource
	private MallMasterMapper mapper;

	@Override
	public JSONObject getLatitude(String address) {
		try {
			// 将地址转换成utf-8的16进制
//			address = URLEncoder.encode(address, "UTF-8");
			String url=MapUrl.ADDRESSTOLNGLAT;
			Map<String, Object> paramsMap = new LinkedHashMap<String, Object>();
			paramsMap.put("address", address);
			paramsMap.put("output", "json");
			paramsMap.put("ak", MapConfig.AK);
			String sn=SnCal.createGetSn(paramsMap);
			paramsMap.put("sn",sn);
			JSONObject json=HttpRequestUtil.getHttpsRequestSingleton().sendGet(url, paramsMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String getLocationMap(String location, String title,
			String content) {
		String json=null;
		try {
			String url=MapUrl.MARKWEBURL;
			Map<String, Object> paramsMap = new LinkedHashMap<String, Object>();
			paramsMap.put("location", location);
			paramsMap.put("title", title);
			paramsMap.put("content",content);
			paramsMap.put("output", "html");
			json=HttpRequestUtil.getHttpsRequestSingleton().sendMapGet(url, paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
		@Override
		public Integer getSendDistanc(String mall_code,String endLat, String endLon) {
			try {
				MallMaster mall=mapper.selectByMallCode(mall_code);
				if(mall==null){
					return null;
				}
				String beginLat=mall.getLatitude().toString();
				String beiginLon=mall.getLongitude().toString();
				String url=MapUrl.WALKDISTANCE;
				Map<String, Object> paramsMap = new LinkedHashMap<String, Object>();
				paramsMap.put("output","json");
				paramsMap.put("origins", beginLat+","+beiginLon);
				paramsMap.put("destinations",endLat+","+endLon);
				paramsMap.put("coord_type",MapConfig.COORDTYPE);
				paramsMap.put("ak", MapConfig.AK);
				JSONObject json=HttpRequestUtil.getHttpsRequestSingleton().sendGet(url, paramsMap);
				System.out.println(json.toString());
				if(json!=null&&!(json.toString().isEmpty())){
					if(json.getInt("status")==0){
						JSONArray result=json.getJSONArray("result");
						JSONObject valre=result.getJSONObject(0);
						JSONObject dis=valre.getJSONObject("distance");
						int disVal=dis.getInt("value");
						return disVal;
					}
				}
				return 1;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public static void main(String[] args) {
			MapServiceImpl impl=new MapServiceImpl();
//			JSONObject json=impl.getLatitude("北京市西城区北礼士路在地图中查看甲98号1幢阜成大厦B座六层");
//			System.out.println(json.toString());
			
//			MapServiceImpl impl=new MapServiceImpl();
//			String json=impl.getLocationMap("39.916979519873,116.41004950566", "我的位置","百度奎科大厦");
//			System.out.println(json);
			
			Integer dis=impl.getSendDistanc("10019","39.931604327639064","116.36136408266799");
			System.out.println(dis);
		}
}
