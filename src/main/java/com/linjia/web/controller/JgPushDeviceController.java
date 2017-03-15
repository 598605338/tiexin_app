package com.linjia.web.controller;

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

import com.linjia.base.controller.BaseController;
import com.linjia.tools.JSONUtil;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.model.JgPushCustomer;
import com.linjia.web.thirdService.JGpush.constant.DeviceConstant;
import com.linjia.web.thirdService.JGpush.constant.PushConstant;
import com.linjia.web.thirdService.JGpush.model.PushModel;
import com.linjia.web.thirdService.JGpush.model.UserPushToken;
import com.linjia.web.thirdService.JGpush.push.PushClientFactory;
import com.linjia.web.thirdService.JGpush.service.JgPushDeviceService;

/**
 *极光推送控制器
 * 
 * @author xiangsy
 *
 */
@Controller
@RequestMapping("/jgPush")
public class JgPushDeviceController extends BaseController{
	
	@Autowired
	private JgPushDeviceService jgPushService;
	
	/**
	 *  插入设备id信息
	 * @param request
	 * @param mall_code 店铺code
	 * @param registration_id 设备id
	 * @param os_type 设备类型
	 * @return
	 */
	@RequestMapping("/insertDevice")
	@ResponseBody
	public Object insertDevice(HttpServletRequest request,@RequestParam String mall_code,@RequestParam String registration_id,@RequestParam int os_type){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			JgPushCustomer jpush=new JgPushCustomer();
			jpush.setRegistration_id(registration_id);
			jpush.setOs_type(os_type);
			jpush.setMall_code(mall_code);
			List<JgPushCustomer> list=jgPushService.selectJgPush(mall_code,registration_id);
			if(list==null||list.size()==0){
				//如果店铺设备id不存在
				jgPushService.insertJgPush(jpush);
			}
			Util.writeOk(map);
			Util.writeSuccess(map);
		}catch(Exception e){
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}
	
	/**
	 * 删除设备id信息
	 * @param request
	 * @param mall_code
	 * @return
	 */
	@RequestMapping(value="/deleteDevice",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public Object deleteDevice(HttpServletRequest request,@RequestParam String mall_code,@RequestParam String deviceId){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			Map<String,String> parammap=new HashMap<String, String>();
			if(!Tools.isEmpty(deviceId)){
				parammap.put("registration_id",deviceId);
			}
			if(!Tools.isEmpty(mall_code)){
				parammap.put("mall_code", mall_code);
			}
			if((!Tools.isEmpty(deviceId))||(!Tools.isEmpty(mall_code))){
				jgPushService.deleteJgPush(parammap);
			}
			Util.writeOk(map);
			Util.writeSuccess(map);
		}catch(Exception e){
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}
	
	/**
	 * 修改设备id信息
	 * @param request
	 * @param jgPushCustomer
	 * @return
	 */
	@RequestMapping(value="/updateDevice",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public Object updateDevice(HttpServletRequest request,@RequestBody JgPushCustomer jgPushCustomer){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			int num=jgPushService.updateJgPush(jgPushCustomer);
			Util.writeOk(map);
			Util.writeSuccess(map);
		}catch(Exception e){
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}
	

	/**
	 * 查询设备id信息
	 * @param request
	 * @param mall_code
	 * @return
	 */
	@RequestMapping(value="/selectDevice",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public Object selectDevice(HttpServletRequest request,@RequestParam String mall_code,@RequestParam String type,@RequestParam String orderSource){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<JgPushCustomer> list=jgPushService.selectJgPush(mall_code,"");
			map.put("ResultData", list);
			PushModel model =new PushModel();
	        model.setType(type);
	        model.setOrderSource(orderSource);
	        model.setMessage(PushConstant.getMessage(type));
	        String strPush = JSONUtil.toJson(model);
	        System.out.println(strPush.getBytes().length);
	        System.out.println(model);
	        
	    	PushClientFactory pushClientFactory=new PushClientFactory();
	        model =pushClientFactory.trimForIOS(model);
	        System.out.println(model);
	        UserPushToken pushToken = new UserPushToken();
	        pushToken.setJpushToken(list.get(0).getRegistration_id());
	        pushToken.setOs(DeviceConstant.ANDROID);
	        pushToken.setUserId(10000000);
	        PushClientFactory.push(pushToken,model,false);

			Util.writeOk(map);
			Util.writeSuccess(map);
		}catch(Exception e){
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}

    /**
	 * 查询设备id集合
	 * @param request
	 * @param mall_codes
	 * @return
	 */
	@RequestMapping("/selectJgPushIds")
	@ResponseBody
	public Object selectJgPushIds(HttpServletRequest request,@RequestParam String mall_codes){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<String> list=jgPushService.selectJgPushIdList(mall_codes);
			String listArray=JSONUtil.toJson(list);
			map.put("ResultData", listArray);
			Util.writeOk(map);
			Util.writeSuccess(map);
		}catch(Exception e){
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}

    /**
	 * 查询设备id集合,并推送消息
	 * @param request
	 * @param mall_codes
	 * @return
	 */
	@RequestMapping("/pushMessage")
	@ResponseBody
	public Object pushMessage(HttpServletRequest request,@RequestParam String mall_codes,@RequestParam String type,@RequestParam String orderSource){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<String> list=jgPushService.selectJgPushIdList(mall_codes);
			if(list!=null&&list.size()>0){
				PushModel model =new PushModel();
		        model.setType(type);
		        model.setOrderSource(orderSource);
		        model.setMessage(PushConstant.getMessage(type));
		        String strPush = JSONUtil.toJson(model);
		        System.out.println(strPush.getBytes().length);
		        System.out.println(model);
		        PushClientFactory.push(list,model,false);
			}
			map.put("ResultData", list);
			Util.writeOk(map);
			Util.writeSuccess(map);
		}catch(Exception e){
			Util.writeError(map);
			Util.writeFail(map);
		}
		return map;
	}
}
