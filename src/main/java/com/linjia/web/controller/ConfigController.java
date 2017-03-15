package com.linjia.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.linjia.base.controller.BaseController;
import com.linjia.constants.Application;

/**
 * 系统总体配置模块
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/config")
public class ConfigController extends BaseController {


	/**
	 * 改变运行模式（测试）
	 * lixinling 
	 * 2016年11月21日 下午2:23:04
	 * @return
	 */
	@RequestMapping(value = "/setIsTest")
	public Object setIsTest(boolean isTest){
		Application.setTest(isTest);
		return null;
	}

}
