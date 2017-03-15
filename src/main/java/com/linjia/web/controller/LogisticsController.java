package com.linjia.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linjia.web.model.Logistics;
import com.linjia.web.query.LogisticsQuery;
import com.linjia.web.service.LogisticsService;


/** 
 * @author  xiangsy: 
 * @date 2016年9月13日 下午9:16:39 
 * @version 1.0 
 */
@Controller
@RequestMapping("/logistics")
public class LogisticsController {
	
	@Autowired
	private LogisticsService logisticsService;
	
	/**
	 * 添加物流信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/addLogistics")
	@ResponseBody
	public Object addLogistics(HttpServletRequest request,@RequestBody Logistics logistics){
		int num=logisticsService.insertLogistics(logistics);
		return num;
	}
	
	/**
	 * 删除物流信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/delLogistics")
	@ResponseBody
	public Object delLogistics(HttpServletRequest request,@RequestParam Long orderId){
		Logistics logistics=new Logistics();
		logistics.setOrder_id(orderId+"");
		int num=logisticsService.deleteLogistics(logistics);
		return num;
	}
	
	/**
	 * 修改物流信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/updLogistics")
	@ResponseBody
	public Object updLogistics(HttpServletRequest request,@RequestBody Logistics logistics){
		int num=logisticsService.updateLogistics(logistics);
		return num;
	}
	
	/**
	 * 修改物流信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/selLogistics")
	@ResponseBody
	public Object selLogistics(HttpServletRequest request,@RequestParam Long orderId){
		LogisticsQuery query=new LogisticsQuery();
		query.setOrder_id(orderId);
		Logistics logistics=logisticsService.selectLogisticsById(query);
		return logistics;
	}
}
