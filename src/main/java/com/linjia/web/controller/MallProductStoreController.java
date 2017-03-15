package com.linjia.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.linjia.base.controller.BaseController;
import com.linjia.tools.DateComFunc;
import com.linjia.tools.MallProductStoreQtyThread;
import com.linjia.tools.RedisUtil;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.model.MallMaster;
import com.linjia.web.model.MallProductSendQty;
import com.linjia.web.model.Region;
import com.linjia.web.query.MallMasterQuery;
import com.linjia.web.service.MallMasterService;
import com.linjia.web.service.MallProductSendQtyService;
import com.linjia.web.uhd123.common.Configure;
import com.linjia.web.uhd123.model.Topics;

/**
 * 门店商品库存
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/mallproduct")
public class MallProductStoreController extends BaseController {

	@Autowired
	private MallMasterService mallMasterService;
	
	@Autowired
	private MallProductSendQtyService mallProductSendQtyService;

	/**
	 * 接收鼎力云库存推送接口
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/changestore")
	@ResponseBody
	public Object getMallList(HttpServletRequest request) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		MallProductSendQty model;
		try {
			String params = IOUtils.toString(request.getInputStream(), "UTF-8");
			logger.debug("================接收鼎力云库存推送START==================");
			logger.info("params==============================:" + params);
			if (!Tools.isEmpty(params)) {
				com.alibaba.fastjson.JSONObject storeChangeObj = JSON.parseObject(params);
				if (storeChangeObj != null) {
					logger.debug("================开始处理推送的库存数据==================");
					String scope = storeChangeObj.getString("scope");
					String key = "store_" + scope;
					String mallCode = scope.split(":")[1];
					com.alibaba.fastjson.JSONObject content = storeChangeObj.getJSONObject("content");
					if (content != null) {
						JSONArray skus = content.getJSONArray("skus");
						if (skus != null && skus.size() > 0) {
							Map<String, String> storeMap = new HashMap<String, String>();
							Date date = new Date();
//							String sendTime = DateComFunc.getDateFormat(date, "yyyy-MM-dd HH:mm:ss");
							MallProductStoreQtyThread.createIns().start();
							for (int i = 0; i < skus.size(); i++) {
								com.alibaba.fastjson.JSONObject item = skus.getJSONObject(i);
								storeMap.put(item.getString("sku_id"), item.getString("qty"));
//								storeMap.put(item.getString("sku_id")+"_time", sendTime);
								
								
								//存入数据库
								model = new MallProductSendQty();
								model.setQuantity(Integer.valueOf(item.getString("qty")));
								model.setUpdateDate(date);
								model.setMallCode(mallCode);
								model.setpCode(item.getString("sku_id"));
								MallProductStoreQtyThread.createIns().addNormal(model);
							}
							if (storeMap != null) {
								RedisUtil.hmset(key, storeMap);
								resMap.put("message", "接收成功");
								Util.writeOk(resMap);
								logger.debug("================将库存数据存入redis完成==================");
							}
						}
					}
				}
			} else {
				resMap.put("message", "接收参数为空");
				Util.writeOk(resMap);
			}

			logger.debug("================接收鼎力云库存推送END==================");

		} catch (IOException e1) {
			resMap.put("message", "系统错误");
			Util.writeError(resMap);
			e1.printStackTrace();
		}
		return resMap;
	}

	/**
	 * 订阅通知
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/subscribe")
	@ResponseBody
	public Object subscribe(HttpServletRequest request, String userId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Topics topics = new Topics();
		String[] topic = { "store.skuchanged" };
		topics.setTopics(topic);

		String callback_url = "http://www.linjia-cvs.cn/linjia_1/mallproduct/changestore";

		JSONObject result = Util.subscribe(userId, callback_url, topics);
		if (result != null && result.optBoolean("success")) {
			resMap.put("message", "订阅成功！");
			Util.writeOk(resMap);
		} else {
			resMap.put("message", "订阅失败！");
			Util.writeError(resMap);
		}

		return resMap;
	}
}
