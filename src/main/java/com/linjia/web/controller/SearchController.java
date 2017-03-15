package com.linjia.web.controller;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linjia.base.controller.BaseController;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.model.HistoryKeyword;
import com.linjia.web.model.HotKeyword;
import com.linjia.web.model.Product;
import com.linjia.web.query.ProductQuery;
import com.linjia.web.query.SearchQuery;
import com.linjia.web.service.ProductService;
import com.linjia.web.service.SearchService;

/**
 * 搜索模块
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/search")
public class SearchController extends BaseController {

	@Autowired
	private SearchService searchService;
	@Autowired
	private ProductService productService;

	/**
	 * 取得热门搜索和历史搜索
	 * lixinling 
	 * 2016年8月26日 下午1:59:34
	 * @param request
	 * @param userId
	 * @return
	 */
	@RequestMapping("/init")
	@ResponseBody
	public Object init(HttpServletRequest request) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		/*if(Tools.isEmpty(userId)){
			resMap.put("message", "userId不能为空");
			Util.writeError(resMap);
			return resMap;
		}*/
		try {
//			List<HistoryKeyword> historyList = searchService.selectHistoryByUserId(userId);
			List<HotKeyword> hotList = searchService.selectHotKeyword();
//			resultData.put("historyList", historyList);
			resultData.put("hotList", hotList);
			resMap.put("resultData", resultData);
			Util.writeOk(resMap);
			Util.writeSuccess(resMap);
		} catch (Exception e) {
			e.printStackTrace();
			Util.writeError(resMap);
		}
		return resMap;
	}
	

	/**
	 * 查询搜索列表
	 * lixinling 
	 * 2016年8月26日 下午1:59:34
	 * @param request
	 * @param userId
	 * @return
	 */
	@RequestMapping("/searchList")
	@ResponseBody
	public Object searchList(HttpServletRequest request, SearchQuery query) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			if(Tools.isEmpty(query.getKeyword())){
				resMap.put("message", "查询条件不能为空");
				Util.writeError(resMap);
				return resMap;
			}else{
				query.setKeyword("%"+URLDecoder.decode(query.getKeyword(),"utf-8")+"%");
			}
			List<Product> productList = searchService.search(query);
			
			//针对全部商品的折扣活动，计算商品的折扣价
			productService.handleProductList(productList, query.getMallCode());
			
			resMap.put("resultData", productList);
			Util.writeOk(resMap);
			Util.writeSuccess(resMap);
		} catch (Exception e) {
			e.printStackTrace();
			Util.writeError(resMap);
		}
		return resMap;
	}

}
