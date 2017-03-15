package com.linjia.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linjia.base.controller.BaseController;
import com.linjia.tools.DateComFunc;
import com.linjia.web.thirdService.message.model.Message;
import com.linjia.web.thirdService.message.service.MessageService;

/**
 *短信控制器
 * 
 * @author xiangsy
 *
 */
@Controller
@RequestMapping("/message")
public class MessageController extends BaseController{
	
	@Autowired
	private MessageService messageService;
	
	/**
	 * 发送短信
	 * @param request
	 * @param userid  用户id
	 * @param mobile  手机号
	 * @param content 发送内容
	 * @param sendTime 发送时间
	 * @return
	 */
	@RequestMapping(value="/send",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String sendMessage(HttpServletRequest request,@RequestParam String mobile){
        JSONObject json=messageService.sendCheckCode("", mobile, "", "");
		return json.toString();
	}
	
	/**
	 * 查询余额
	 * 
	 * @param userid
	 * @return
	 */
	@RequestMapping(value="/balance",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String getBalance(HttpServletRequest request,@RequestParam String userid){
       JSONObject json=messageService.selBalance(userid);
		return json.toString();
	}
	
	/**
	 * 短信推送
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pushMessgae",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String pushMessgae(HttpServletRequest request,@RequestParam String userid){
        JSONObject json=messageService.StatusReport(userid);
		return json.toString();
	}
	

	/**
	 * 插入短信验证码数据
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping(value="/insertMessage",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String insertMessage(HttpServletRequest request,Message message){
		  Integer num=messageService.insertMessage(message);
		  JSONObject json=new JSONObject();
		  if(num!=null&&num>0){
			  json.put("status","ok");
			  json.put("num",num);
		  }else{
			  json.put("status","fail");
		  }
		 return json.toString();
	}

    /**
	 * 删除验证码
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping(value="/deleteMessage",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String deleteMessage(HttpServletRequest request,@RequestParam int id ){
		 Integer num=messageService.deleteMessage(id);
		  JSONObject json=new JSONObject();
		  if(num!=null&&num>0){
			  json.put("status","ok");
			  json.put("num",num);
		  }else{
			  json.put("status","fail");
		  }
		 return json.toString();
	}

    /**
	 * 查询验证码
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping("/selectMessage")
	@ResponseBody
	public Object selectMessage(HttpServletRequest request,@RequestParam String moblie){
		Message message=messageService.selectMessage(moblie);
		Long difftime=DateComFunc.dateDiff(new Date(), message.getEndtime());
		if(difftime>20){
			
		}
		return message;
	}
}
