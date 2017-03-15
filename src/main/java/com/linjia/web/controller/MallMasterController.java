package com.linjia.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linjia.base.controller.BaseController;
import com.linjia.tools.Util;
import com.linjia.web.model.MallMaster;
import com.linjia.web.model.Region;
import com.linjia.web.query.MallMasterQuery;
import com.linjia.web.service.MallMasterService;

/**
 * 门店模块
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/mall")
public class MallMasterController extends BaseController {

	@Autowired
	private MallMasterService mallMasterService;

	/**
	 * 取得门店列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/mallList")
	@ResponseBody
	public Object getMallList(HttpServletRequest request, MallMasterQuery query) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			List<MallMaster> mallList = mallMasterService.selectMallListByRegion(query);

			resMap.put("resultData", mallList);
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
		}
		return resMap;
	}
	
	/**
	 * 取得区划列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/regionList")
	@ResponseBody
	public Object getregionList(HttpServletRequest request,Integer cityId,Integer countyId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		try {
			List<Region> cityList = mallMasterService.selectCityList();
			List<Region> countyList = mallMasterService.selectCountyList(cityId);
			resultData.put("cityList", cityList);
			resultData.put("countyList", countyList);
			resMap.put("resultData", resultData);
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
		}
		return resMap;
	}

}
