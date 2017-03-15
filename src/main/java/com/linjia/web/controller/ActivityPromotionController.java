package com.linjia.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linjia.base.controller.BaseController;
import com.linjia.web.model.ActivityInfo;
import com.linjia.web.model.ActivityProduct;
import com.linjia.web.model.ActivityTradeProduct;
import com.linjia.web.service.ActivityPromotionService;

/**
 * 促销模块
 * 
 * @author xiangsy
 *
 */
@Controller
@RequestMapping("/promotion")
public class ActivityPromotionController extends BaseController{
	
	private static Logger LOG = LoggerFactory.getLogger(ActivityPromotionController.class);
	
	@Autowired
	private ActivityPromotionService activityPromotionService;
	
	/**
	 * 插入活动基本信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/insertActInfo")
	@ResponseBody
	public Object insertActInfo(HttpServletRequest request,@RequestBody ActivityInfo info){
		Map<String, Object> map = new HashMap<String, Object>();
		boolean b=activityPromotionService.insertActInfo(info);
		map.put("resultData", info);
		map.put("flag", b);
		return map;
	}
	
	/**
	 * 更新活动基本信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateActInfoById")
	@ResponseBody
	public Object updateActInfoById(HttpServletRequest request,@RequestBody ActivityInfo info){
		Map<String, Object> map = new HashMap<String, Object>();
		boolean b=activityPromotionService.updateActInfoById(info);
		map.put("resultData", info);
		map.put("flag", b);
		return map;
	}
	
	/**
	 * 删除活动基本信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/deleteActInfoById")
	@ResponseBody
	public Object deleteActInfoById(HttpServletRequest request,@RequestParam int activity_id){
		Map<String, Object> map = new HashMap<String, Object>();
		boolean b=activityPromotionService.deleteActInfoById(activity_id);
		map.put("flag", b);
		return map;
	}
	
	/**
	 * 查询活动基本信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectActInfoById")
	@ResponseBody
	public Object selectActInfoById(HttpServletRequest request,@RequestParam int activity_id){
		Map<String, Object> map = new HashMap<String, Object>();
		ActivityInfo info=activityPromotionService.selectActInfoById(activity_id);
		map.put("resultData", info);
		return map;
	}
	
	/**
	 * 查询多个活动基本信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectActInfoAll")
	@ResponseBody
	public Object selectActInfoAll(HttpServletRequest request,@RequestParam String activity_name,@RequestParam String status){
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> parmMap=new HashMap<String, Object>();
		map.put("activity_name", activity_name);
		int ss=0;
		if(status!=null&&status!=""){
			ss=Integer.parseInt(status);
			parmMap.put("status", ss);
		}else{
			parmMap.put("status",null);
		}
		List<ActivityInfo> list=activityPromotionService.selectActInfoAll(parmMap);
		map.put("resultData", list);
		return map;
	}
	
	
	
	/**
	 * 插入活动兑换商品基本信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/insertActTradePro")
	@ResponseBody
	public Object insertActTradePro(HttpServletRequest request,@RequestBody ActivityTradeProduct info){
		Map<String, Object> map = new HashMap<String, Object>();
		boolean b=activityPromotionService.insertActTradePro(info);
		map.put("resultData", info);
		map.put("flag", b);
		return map;
	}
	
	/**
	 * 更新活动兑换商品基本信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateActTradeProById")
	@ResponseBody
	public Object updateActTradeProById(HttpServletRequest request,@RequestBody ActivityTradeProduct info){
		Map<String, Object> map = new HashMap<String, Object>();
		boolean b=activityPromotionService.updateActTradeProById(info);
		map.put("resultData", info);
		map.put("flag", b);
		return map;
	}
	
	/**
	 * 删除活动兑换商品基本信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/deleteActTradeProById")
	@ResponseBody
	public Object deleteActTradeProById(HttpServletRequest request,@RequestParam int id){
		Map<String, Object> map = new HashMap<String, Object>();
		boolean b=activityPromotionService.deleteActTradeProById(id);
		map.put("flag", b);
		return map;
	}
	
	/**
	 * 查询活动兑换商品基本信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectActTradeProById")
	@ResponseBody
	public Object selectActTradeProById(HttpServletRequest request,@RequestParam int id){
		Map<String, Object> map = new HashMap<String, Object>();
		ActivityTradeProduct info=activityPromotionService.selectActTradeProById(id);
		map.put("resultData", info);
		return map;
	}
	
	/**
	 * 查询多个活动兑换商品基本信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectActTradeProAll")
	@ResponseBody
	public Object selectActTradeProAll(HttpServletRequest request,@RequestParam String p_name,@RequestParam String activity_id){
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> parmMap=new HashMap<String, Object>();
		parmMap.put("p_name", p_name);
		int s=0;
		if(activity_id!=null&&activity_id!=""){
			s=Integer.parseInt(activity_id);
			parmMap.put("activity_id",s);
		}else{
			parmMap.put("activity_id",null);
		}
		List<ActivityTradeProduct> list=activityPromotionService.selectActTradeProAll(parmMap);
		map.put("resultData", list);
		return map;
	}
	
	
	
	
	/**
	 * 插入活动商品基本信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/insertActProduct")
	@ResponseBody
	public Object insertActProduct(HttpServletRequest request,@RequestBody ActivityProduct info){
		Map<String, Object> map = new HashMap<String, Object>();
		boolean b=activityPromotionService.insertActProduct(info);
		map.put("resultData", info);
		map.put("flag", b);
		return map;
	}
	
	/**
	 * 更新活动商品基本信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateActProductById")
	@ResponseBody
	public Object updateActProductById(HttpServletRequest request,@RequestBody ActivityProduct info){
		Map<String, Object> map = new HashMap<String, Object>();
		boolean b=activityPromotionService.updateActProductById(info);
		map.put("resultData", info);
		map.put("flag", b);
		return map;
	}
	
	/**
	 * 删除活动商品基本信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/deleteActProductById")
	@ResponseBody
	public Object deleteActProductById(HttpServletRequest request,@RequestParam int id){
		Map<String, Object> map = new HashMap<String, Object>();
		boolean b=activityPromotionService.deleteActProductById(id);
		map.put("flag", b);
		return map;
	}
	
	/**
	 * 查询活动商品基本信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectActProductById")
	@ResponseBody
	public Object selectActProductById(HttpServletRequest request,@RequestParam int id){
		Map<String, Object> map = new HashMap<String, Object>();
		ActivityProduct info=activityPromotionService.selectActProductById(id);
		map.put("resultData", info);
		return map;
	}
	
	/**
	 * 查询多个活动商品基本信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectActProductAll")
	@ResponseBody
	public Object selectActProductAll(HttpServletRequest request,@RequestParam String p_name,@RequestParam String activity_id){
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> parmMap=new HashMap<String, Object>();
		int s=0;
		if(activity_id!=null&&activity_id!=""){
			s=Integer.parseInt(activity_id);
			parmMap.put("activity_id",s);
		}else{
			parmMap.put("activity_id",null);
		}
		parmMap.put("p_name", p_name);
		List<ActivityProduct> list=activityPromotionService.selectActProductAll(parmMap);
		map.put("resultData", list);
		return map;
	}
}
