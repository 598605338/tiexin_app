package com.linjia.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linjia.base.controller.BaseController;
import com.linjia.constants.Application;
import com.linjia.constants.Constants;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.model.NewProductRecommend;
import com.linjia.web.service.NewProductRecommendService;
import com.linjia.web.service.ProductService;
import com.linjia.web.uhd123.service.UhdOrderService;

/**
 * 首页模块
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/index")
public class MainPageController extends BaseController {

	@Autowired
	private NewProductRecommendService newProductRecommendService;
	@Autowired
	private UhdOrderService uhdOrderService;
	@Autowired
	private ProductService productService;

	@RequestMapping("/index")
	public String index(HttpServletRequest request, String code, String orderId, String pt, String source) {
//		boolean r = uhdOrderService.testSubmitOrder(Long.valueOf(orderId),"WEB160628000066");
//		boolean r =
		// uhdOrderService.updateOrderDeliverToUhd(orderId,null,"WEB160628000066","shipped");
//		  r = uhdOrderService.updateOrderDeliverToUhd(orderId, new Date(), "system", "signed","handover", "handover");
//		 boolean r = uhdOrderService.testReturnservice(Long.valueOf(orderId),"WEB160628000066",orderId);
		// boolean r = uhdOrderService.confirmReturnserviceToUhd(orderId,
		// "WEB160628000066");
//		 System.out.println(r);

		String openid = "";
		try {
			if (Tools.isEmpty(code)) {
				logger.error("用户未同意微信授权，取得code为空.");
			} else {
				// 通过code换取网页授权access_token
				JSONObject result = Util.getAccessTokenBycode(code);
				if (result != null) {
					if (Tools.isEmpty(result.optString("openid"))) {
						logger.error("微信授权发生错误：errcode =" + result.optString("errcode") + ";errmsg =" + result.optString("errmsg"));
					} else {
						openid = result.optString("openid");
					}
				} else {
					logger.error("微信授权取得结果为空，未知错误！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String redi_url = Application.getServerBasePath()+"?openid=" + openid;
		if(!Tools.isEmpty(pt)){
			//如果是参与拼团页过来，则跳转回拼团页
			//bid=51$id=519$uid=WEB160825001290
			String[] ptArray = pt.split("\\$");
			redi_url = Application.getServerBasePath()+"/html/fightShareGroups.html?openid=" + openid+"&bId="+ptArray[0]+"&id="+ptArray[1]+"&uId="+ptArray[2];
		}else if(!Tools.isEmpty(source) && "login".equals(source)){
			redi_url = Application.getServerBasePath()+"/html/login.html?openid=" + openid;
		}
		//return "redirect:http://www.linjia-cvs.cn?openid=" + openid;
		logger.debug("ServerBasePath:::::::::::" + redi_url);
		return "redirect:" + redi_url;
	}

	/**
	 * 取得首页新品推荐列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getNewProductRecommendList")
	@ResponseBody
	public Object getNewProductRecommendList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "mallCode", required = true) String mallCode) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			List<NewProductRecommend> newProductRecommendList = newProductRecommendService.selectNewProductList(mallCode);

			// 取得ERP系统的门店库存 - 安全库存 = 线上售卖的库存数
			if (newProductRecommendList != null) {
				for (NewProductRecommend item : newProductRecommendList) {
					// 查询ERP的库存
					int currentQty = productService.getCurrentQty(item.getProductId(), item.getpCode(), mallCode);
					if (currentQty > 0) {
						item.setItemStatus(Constants.QTY_STATUS.NORMAL);
					} else {
						item.setItemStatus(Constants.QTY_STATUS.LOW);
					}
				}
				resMap.put("resultData", newProductRecommendList);
				Util.writeSuccess(resMap);
			}
			/*
			 * response.setHeader("Content-Type","application:json");
			 * response.setHeader("Access-Control-Allow-Origin","*");
			 * response.setHeader("Access-Control-Allow-Methods","POST");
			 */

			Util.writeOk(resMap);
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
			e.printStackTrace();
		}
		return resMap;
	}

}
