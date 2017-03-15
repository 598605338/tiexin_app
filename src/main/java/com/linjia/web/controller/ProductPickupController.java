package com.linjia.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linjia.base.controller.BaseController;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.model.ProductPickup;
import com.linjia.web.query.ProductPickupQuery;
import com.linjia.web.service.ProductPickupService;

/**
 * 商品关注模块
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/productPickup")
public class ProductPickupController extends BaseController {

	@Autowired
	private ProductPickupService productPickupService;

	/**
	 * 取得商品关注列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getMyProductPickupList")
	@ResponseBody
	public Object getMyProductPickupList(HttpServletRequest request, ProductPickupQuery query) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		if(Tools.isEmpty(query.getUserId())){
			Util.writeFail(resMap);
			Util.writeError(resMap);
			return resMap;
		}
		try {
			//关注的商品列表
			List<ProductPickup> productPickupList = productPickupService.selectProductPickupList(query);
			
			//取得购物车商品

			resultData.put("resultData", productPickupList);
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
			e.printStackTrace();
		}
		return resMap;
	}

	
	/**
	 * 删除关注列表中的商品
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/delMyProductPickupItem")
	@ResponseBody
	public Object delMyProductPickupItem(HttpServletRequest request, String itemIds, String userId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if(Tools.isEmpty(itemIds) || Tools.isEmpty(userId)){
			resMap.put("message", "itemIds和userId为必传参数！");
			Util.writeError(resMap);
			return resMap;
		}
		try {
			productPickupService.delMyProductPickupItem(itemIds);
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
			e.printStackTrace();
		}
		return resMap;
	}
	
	/**
	 * 加入商品到商品关注
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/addMyProductPickupItem")
	@ResponseBody
	public Object addMyProductPickupItem(HttpServletRequest request, ProductPickup model) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if(model == null || Tools.isEmpty(model.getUserId()) || model.getProductId() == null || Tools.isEmpty(model.getpCode())){
			resMap.put("message", "userId、productId和pCode为必传参数!");
			Util.writeError(resMap);
			return resMap;
		}
		try {
			model.setCreDate(new Date());
			productPickupService.insert(model);
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
