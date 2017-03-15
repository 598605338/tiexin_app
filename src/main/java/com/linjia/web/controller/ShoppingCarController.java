package com.linjia.web.controller;

import java.math.BigDecimal;
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

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.linjia.base.controller.BaseController;
import com.linjia.constants.Constants;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.model.ActivityTradeProduct;
import com.linjia.web.model.ShoppingCar;
import com.linjia.web.query.ShoppingCarQuery;
import com.linjia.web.service.ActivityPromotionService;
import com.linjia.web.service.MiaoshaActivityProductService;
import com.linjia.web.service.ProductService;
import com.linjia.web.service.ShoppingCarService;

/**
 * 购物车模块
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/shoppingcar")
public class ShoppingCarController extends BaseController {

	@Autowired
	private ShoppingCarService shoppingCarService;

	@Autowired
	private ProductService productService;

	@Autowired
	private MiaoshaActivityProductService miaoshaActivityProductService;

	@Autowired
	private ActivityPromotionService activityPromotionService;

	/**
	 * 更改购物车商品数量
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateQuantityByPrimaryKey")
	@ResponseBody
	public Object updateQuantityByPrimaryKey(HttpServletRequest request, int quantity, int id, int productId, String pCode, String mallCode) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			if (quantity == 0) {
				resMap.put("message", "库存数量参数不正确");
				Util.writeError(resMap);
			}
			boolean result = shoppingCarService.updateQuantityByPrimaryKey(quantity, id, productId, pCode, mallCode, null, null);
			if (result) {
				Util.writeSuccess(resMap);
				Util.writeOk(resMap);
			} else {
				resMap.put("message", "库存不足");
				Util.writeError(resMap);
			}
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
		}
		return resMap;
	}

	/**
	 * 将商品加入购物车
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/insertSelective")
	@ResponseBody
	public Object insertSelective(HttpServletRequest request, ShoppingCar shoppingCar, String mallCode) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		String callbackFunName = request.getParameter("callback");// 得到js函数名称
		Map<String, Object> resultData = new HashMap<String, Object>();
		try {
			int result = shoppingCarService.insertCar(shoppingCar, mallCode);
			if (result == 1) {
				// 加入购物车成功并且是最后一件
				resultData.put("itemStatus", Constants.QTY_STATUS.LOW);
				resMap.put("resultData", resultData);
				Util.writeSuccess(resMap);
				Util.writeOk(resMap);
			} else if (result <= 0) {
				resMap.put("message", "商品库存不足。");
				Util.writeError(resMap);
			} else if (result == 2) {
				// 加入购物车成功，并且还可以继续购买
				resultData.put("itemStatus", Constants.QTY_STATUS.NORMAL);
				resMap.put("resultData", resultData);
				Util.writeSuccess(resMap);
				Util.writeOk(resMap);
			} else if (result == 3) {
				resMap.put("message", "加钱换购的商品不存在。");
				Util.writeError(resMap);
			}
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
			e.printStackTrace();
		}
		if (Tools.isEmpty(callbackFunName)) {
			return resMap;
		} else {
			return new JSONPObject(callbackFunName, resMap);
		}
	}

	/**
	 * 清空购物车商品
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/deleteByPrimaryKey")
	@ResponseBody
	public Object deleteByPrimaryKey(HttpServletRequest request, String ids) {
		List<Long> idList = JSONArray.parseArray(ids, Long.class);
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			for (Long id : idList) {
				shoppingCarService.delete(id);
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
	 * 查看用户购物车
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectCarAllProduct")
	@ResponseBody
	public Object selectCarAllProduct(HttpServletRequest request, ShoppingCarQuery query, String mallCode) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		String callbackFunName = request.getParameter("callback");// 得到js函数名称
		try {
			List<ShoppingCar> shoppingCarList = shoppingCarService.selectCarAllProduct(query);

			if (shoppingCarList != null) {
				// 购物车总商品数量
				int totalProductNum = 0;
				// 购物车总价格
				BigDecimal totalProductPrice = new BigDecimal("0.00");
				Map<String, Object> totalMap = new HashMap<String, Object>();
				totalMap.put("totalProductPrice", totalProductPrice);
				totalMap.put("totalProductNum", totalProductNum);

				resultData.put("shoppingCarList", shoppingCarList);
				for (ShoppingCar item : shoppingCarList) {
					shoppingCarService.handleProduct(request, item, totalMap, mallCode, query.getUserId());
				}

				resultData.put("totalProductPrice", totalMap.get("totalProductPrice"));
				resultData.put("totalProductNum", totalMap.get("totalProductNum"));

				resMap.put("resultData", resultData);
				Util.writeSuccess(resMap);
				Util.writeOk(resMap);
			}
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
			e.printStackTrace();
		}

		if (Tools.isEmpty(callbackFunName)) {
			return resMap;
		} else {
			return new JSONPObject(callbackFunName, resMap);
		}
	}

	/**
	 * 查看换购商品列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectTradeProductList")
	@ResponseBody
	public Object selectTradeProductList(HttpServletRequest request, @RequestParam(value = "activity_id", required = true) Long activity_id, String mallCode) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("activity_id", activity_id);
		param.put("mallCode", mallCode);
		List<ActivityTradeProduct> activityTradeProductList = activityPromotionService.selectActTradeProAll(param);
		resultData.put("activityTradeProductList", activityTradeProductList);
		Util.writeSuccess(resMap);
		Util.writeOk(resMap);
		resMap.put("resultData", resultData);
		return resMap;
	}

	/**
	 * 提交选择的换购商品
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/addTradeProduct")
	@ResponseBody
	public Object addTradeProduct(HttpServletRequest request, @RequestParam(value = "parentId", required = true) Long parentId,
			String tradeProductId, Long activityId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("parentId", parentId);
		param.put("activityId", activityId);
		param.put("tradeProductIds", Tools.isEmpty(tradeProductId) || tradeProductId.equals("0") ? null : tradeProductId);
		shoppingCarService.updateTradeProductIds(param);
		Util.writeSuccess(resMap);
		Util.writeOk(resMap);
		return resMap;
	}

}
