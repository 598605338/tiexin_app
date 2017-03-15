package com.linjia.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linjia.base.controller.BaseController;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.model.Address;
import com.linjia.web.service.AddressService;

/**
 * 地址管理模块
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/address")
public class AddressController extends BaseController{
	
	@Autowired
	private AddressService addressService;
	
	/**
	 * 新增地址
	 * 
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/addAddress")
	@ResponseBody
	public Object addAddress(HttpServletRequest request, Address address, String userId) throws UnsupportedEncodingException {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		if(Tools.isEmpty(address.getPhone()) || Tools.isEmpty(address.getReceiveName()) || Tools.isEmpty(address.getRegion()) || Tools.isEmpty(address.getDetailAddress())){
			logger.info("手机号和收货人姓名和区域和详细地址不能为空");
			resMap.put("message", "手机号和收货人姓名和区域和详细地址不能为空");
			Util.writeError(resMap);
			return resMap;
		}
		
		address.setUserId(userId);
		address.setReceiveName(URLDecoder.decode(address.getReceiveName(),"UTF-8"));
		address.setRegion(URLDecoder.decode(address.getRegion(), "UTF-8"));
		address.setDetailAddress(URLDecoder.decode(address.getDetailAddress(), "UTF-8"));
		address.setCreDate(new Date());
		address.setUpdateDate(new Date());
		addressService.insert(address);	
		
		resultData.put("id", address.getId());
		resMap.put("resultData",resultData);
		Util.writeSuccess(resMap);
		Util.writeOk(resMap);
		return resMap;
	}
	
	/**
	 * 编辑地址
	 * 
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/updateAddress")
	@ResponseBody
	public Object updateAddress(HttpServletRequest request, Address address) throws UnsupportedEncodingException {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if(address.getId()== null || address.getId().longValue() ==0 || Tools.isEmpty(address.getPhone()) || Tools.isEmpty(address.getReceiveName()) || Tools.isEmpty(address.getRegion()) || Tools.isEmpty(address.getDetailAddress())){
			logger.info("手机号和收货人姓名和区域和详细地址不能为空");
			resMap.put("message", "手机号和收货人姓名和区域和详细地址不能为空");
			Util.writeError(resMap);
			return resMap;
		}
		
		address.setReceiveName(URLDecoder.decode(address.getReceiveName(),"UTF-8"));
		address.setRegion(URLDecoder.decode(address.getRegion(), "UTF-8"));
		address.setDetailAddress(URLDecoder.decode(address.getDetailAddress(), "UTF-8"));
		address.setUpdateDate(new Date());
		addressService.update(address);	
		
		Util.writeSuccess(resMap);
		Util.writeOk(resMap);
		return resMap;
	}
	
	/**
	 * 删除地址
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/deleteAddress")
	@ResponseBody
	public Object deleteAddress(HttpServletRequest request, Long id) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		addressService.delete(id);	
		
		Util.writeSuccess(resMap);
		Util.writeOk(resMap);
		return resMap;
	}
	
	
	
}
