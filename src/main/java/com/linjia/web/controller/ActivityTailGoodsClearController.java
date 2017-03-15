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
import com.linjia.web.model.TailGoodsClear;
import com.linjia.web.query.TailGoodsClearQuery;
import com.linjia.web.service.TailGoodsClearService;

/**
 * 尾货清仓模块
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/tailGoodsClear")
public class ActivityTailGoodsClearController extends BaseController {

	@Autowired
	private TailGoodsClearService tailGoodsClearService;

	/**
	 * 尾货清仓推荐列表
	 * 
	 * @param request
	 * @return
	 */
	/**
	 * lixinling 
	 * 2016年9月10日 上午10:33:04
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getTailGoodsClearList")
	@ResponseBody
	public Object getTailGoodsClearList(HttpServletRequest request,TailGoodsClearQuery query) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		try {
			//每日清仓数据
			List<TailGoodsClear> everyDayClearList = tailGoodsClearService.selectEveryDayClearList(query);
			
			//本期清仓数据
			List<TailGoodsClear> currentClearList = tailGoodsClearService.selectCurrentClearList(query);
			
			resultData.put("everyDayClearList", everyDayClearList);
			resultData.put("currentClearList", currentClearList);
			
			resMap.put("resultData", resultData);
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
			e.printStackTrace();
		}
		return resMap;
	}

}
