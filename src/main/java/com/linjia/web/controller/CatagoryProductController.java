package com.linjia.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.linjia.base.controller.BaseController;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.model.LargeCatagory;
import com.linjia.web.model.MiddleCatagory;
import com.linjia.web.model.Product;
import com.linjia.web.query.ProductQuery;
import com.linjia.web.service.LargeCatagoryService;
import com.linjia.web.service.MiddleCatagoryService;
import com.linjia.web.service.ProductService;

/**
 * 分类模块
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/catagory")
public class CatagoryProductController extends BaseController {

	@Autowired
	private LargeCatagoryService largeCatagoryService;

	@Autowired
	private MiddleCatagoryService middleCatagoryService;

	@Autowired
	private ProductService productService;

	/**
	 * 根据分类id查询相关分类商品 lixinling 2016年7月13日 下午5:09:53
	 * 
	 * @param request
	 * @param largeCatagoryId
	 *            (大分类id)
	 * @param middleCatagoryId
	 *            (中分类id)
	 * @return
	 */
	@RequestMapping(value = "/getCatagoryProductList")
	@ResponseBody
	public Object getCatagoryProductList(HttpServletRequest request, ProductQuery query) {
		Map<String, Object> resMap = new HashMap<String, Object>();

		String callbackFunName = request.getParameter("callback");// 得到js函数名称

		try {
			if (Tools.isEmpty(query.getMallCode()) || query.getLargeCatagoryId() == 0) {
				resMap.put("message", "门店code和一级分类不能为空");
				Util.writeError(resMap);
				return resMap;
			}
			List<Product> catagoryProductList = productService.selectCatagoryProductList(query);
			
			//针对全部商品的折扣活动，计算商品的折扣价
			productService.handleProductList(catagoryProductList, query.getMallCode());

			resMap.put("resultData", catagoryProductList);
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
		}
		if (Tools.isEmpty(callbackFunName)) {
			return resMap;
		} else {
			return new JSONPObject(callbackFunName, resMap);
		}
	}

	/**
	 * 取得所有的大分类信息列表 lixinling 2016年7月13日 下午5:09:53
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getLargeCatagoryList")
	@ResponseBody
	public Object getLargeCatagoryList(HttpServletRequest request) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			List<LargeCatagory> largeCatagoryList = largeCatagoryService.selectList();

			if (largeCatagoryList != null) {
				resMap.put("resultData", largeCatagoryList);
			}
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
		}
		return resMap;
	}

	/**
	 * 根据大分类id取得所属于的中分类信息列表 lixinling 2016年7月13日 下午5:09:53
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getMiddleCatagoryList")
	@ResponseBody
	public Object getMiddleCatagoryList(HttpServletRequest request,
			@RequestParam(value = "largeCatagoryId", required = true) int largeCatagoryId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			List<MiddleCatagory> middleCatagoryList = middleCatagoryService.selectByLargeCatagoryId(largeCatagoryId);

			if (middleCatagoryList != null) {
				resMap.put("resultData", middleCatagoryList);
			}
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
		}
		return resMap;
	}
}
