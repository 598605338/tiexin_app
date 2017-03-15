package com.linjia.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linjia.base.controller.BaseController;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.query.HotsellProductQuery;
import com.linjia.web.service.HotsellService;

/**
 * 热门销售模块
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/hotsell")
public class ActivityHotsellController extends BaseController {

	@Autowired
	private HotsellService hotsellService;

	
	/**
	 * 取得热销活动商品列表
	 * lixinling 
	 * 2017年2月10日 下午2:00:36
	 * @param query
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/selectProductList")
	@ResponseBody
	public Object getActivityHotsellList(HotsellProductQuery query) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if(Tools.isEmpty(query.getMallCode())){
			resMap.put("message", "mallCode为比传参数。");
			Util.writeError(resMap);
			return resMap;
		}
		try {
			Map<String, Object> resultData = hotsellService.selectByPageList(query);
			if(resultData == null){
				resMap.put("message", "当前没有热销活动。");
				Util.writeError(resMap);
				return resMap;
			}
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
