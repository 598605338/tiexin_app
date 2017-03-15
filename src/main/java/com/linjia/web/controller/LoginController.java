package com.linjia.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linjia.base.controller.BaseController;
import com.linjia.constants.Application;
import com.linjia.constants.Constants;
import com.linjia.tools.JSONUtil;
import com.linjia.tools.RedisUtil;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.model.ActivityMemberPromotion;
import com.linjia.web.model.CustMaster;
import com.linjia.web.model.ShopUser;
import com.linjia.web.service.ActivityMemberPromotionService;
import com.linjia.web.service.CustMasterService;
import com.linjia.web.service.RegistMemberService;
import com.linjia.web.service.ShopUserService;
import com.linjia.web.service.UserCardCouponService;
import com.linjia.web.thirdService.meituan.utils.PropertiesUtil;
import com.linjia.web.thirdService.message.model.Message;
import com.linjia.web.thirdService.message.service.MessageService;

/**
 * 用户登陆 
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {

	@Autowired
	private RegistMemberService registMemberService;

	@Autowired
	private CustMasterService custMasterService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ShopUserService shopUserService;
	
	@Autowired
	private ActivityMemberPromotionService activityMemberPromotionService;
	
	@Autowired
	private UserCardCouponService userCardCouponService;

	/**
	 * 用户登陆
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/login")
	@ResponseBody
	public Object login(HttpServletRequest request, String phone, String verCode) {
		// User user = new
		// User(3,28,"员工",1,"6944732100390l",6944732100390l,"lxl");
		// request.getSession().setAttribute("loginUser", user);
		// logger.info("我就是个日志....I was Info");
		// request.setAttribute("message", "登陆成功");
		// return "success";
		Map<String, Object> resMap = new HashMap<String, Object>();

		if (Tools.isEmpty(phone) || Tools.isEmpty(verCode)) {
			logger.error("phone和verCode是必传参数");
			resMap.put("message", "phone和verCode是必传参数");
			Util.writeError(resMap);
			return resMap;
		}

		CustMaster custMaster = null;
		// 验证 验证码正确性
		Message message = messageService.selectMessage(phone);
		if(message == null){
			logger.error("Message对象没有取到");
			resMap.put("message", "系统异常，请稍后重新发送");
			Util.writeError(resMap);
			return resMap;
		}
		if(!verCode.equalsIgnoreCase(message.getCheckcode())){
			logger.error("验证码输入不正确，请重新输入");
			resMap.put("message", "验证码输入不正确，请重新输入");
			Util.writeError(resMap);
			return resMap;
		}
			
		// 查询登陆用户的login(因为线上的会员如果修改过手机号信息，通过接口将查询不到用户的相关信息，因为login这个字段对于一个用户来说是不会改变的，因此用用户的login查询更准确)
		String login = null;
		// 验证 验证码的正确性 -->查询CRM系统中用户是否存在-->(存在)insertOrUpdate商城用户表-->读取商城用户表的用户数据
		// 验证 验证码的正确性 -->查询CRM系统中用户是否存在-->(不存在)注册CRM用户并注册商城用户表-->读取商城用户表的用户数据
		JSONObject memberObj = null;
//		if (Tools.isEmpty(login)) {
			memberObj = Util.queryMemberByMobile(phone);
//		} else {
//			memberObj = Util.queryMemberByLogin(login);
//		}
		if (memberObj == null) {
			logger.error("从CRM取得会员信息时出错");
			resMap.put("message", "从CRM取得会员信息时出错");
			Util.writeError(resMap);
		} else if (memberObj.optInt("error_code") == 20205 || memberObj.optInt("responseCode") == 404) {
			/*
			 * "exceptionClass":
			 * "com.hd123.card.common.ejb.exception.NotFoundException"
			 * "message": "会员没有找到" "stackTrace": [0] "cause": null
			 * "error_code":20205
			 */
			// 会员未注册，进行会员注册
			Map<String, Object> res = registMemberService.insertRegistMember(phone);
			int status = (int) res.get("status");
			if (status == Constants.STATUS.FAIL) {
				logger.info("系统错误!");
				resMap.put("message", "系统错误!");
				Util.writeError(resMap);
				return resMap;
			} else if (status == Constants.STATUS.SUCCESS) {
				logger.info("message", "手机号为" + phone + "的会员注册成功");
				
				//再次查询刚注册的用户信息
				memberObj = Util.queryMemberByMobile(phone);
				// 将注册的会员信息同步到商城用户表
				custMaster = custMasterService.insertOrUpdate(memberObj, phone);
				
				login = custMaster.getLogin();
				
				// (促销管理)首次注册送卡券 
				ActivityMemberPromotion activityMemberPromotion = activityMemberPromotionService.selectByCurrentTime();
				if(activityMemberPromotion != null){
					int row = userCardCouponService.insertUserCardCoupon(custMaster.getUserId(), activityMemberPromotion.getCardCouponId(), Constants.SCORE_SOURCE_TYPE.HYHD, null);
					if(row == 1){
						logger.info("用户："+custMaster.getUserId()+"  首次注册赠送卡券操作完成 ");
						row = activityMemberPromotionService.updatePrizeNumById(activityMemberPromotion.getPrizeNum() - 1 , activityMemberPromotion.getId());
						if(row == 1){
							logger.info("更新奖品数量操作完成");
						}else{
							logger.info("更新奖品数量操作失败");
						}
					}
				}
			} else {
				logger.info("error_code:" + status + ";message:" + res.get("message"));
				resMap.put("message", res.get("message"));
				Util.writeError(resMap);
//				resMap.put("error_code", status);
				return resMap;
			}
		} else if (!Tools.isEmpty(memberObj.optString("code"))) {
			// 将注册的会员信息同步到商城用户表
			custMaster = custMasterService.insertOrUpdate(memberObj, phone);

			login = custMaster.getLogin();
			// 因为用户是存在的，所以用户可能编辑过头像信息，因此重新从商城用户表取得用户信息
//			custMaster = custMasterService.selectById(login);
		}

		if(custMaster != null){
			resMap.put("custMaster", custMaster);
			
			//放入缓存
			RedisUtil.put(Application.USERINFO_PREFIX + login, JSONUtil.toJson(custMaster), Application.USERINFO_EXPIRE);
		} 
		//删除验证码信息
		messageService.deleteMessage(message.getId());
		
		//生成token，并将用户信息放入缓存中
		Util.writeSuccess(resMap);
		Util.writeOk(resMap);
		return resMap;

	}

	/**
	 * 商户登陆
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/shopLogin")
	@ResponseBody
	public Object shopLogin(HttpServletRequest request, String account, String password) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try{
			if (Tools.isEmpty(account) || Tools.isEmpty(password)) {
				Util.writeError(resMap);
				resMap.put("message", "用户名或密码不能为空！");
				logger.error("account和password是必传参数");
				return resMap;
			}
			//md5加密密码
			String pwd=Util.getMD5(password);
			ShopUser shopUser=shopUserService.selectShop(account, pwd);
			resMap.put("resultData", shopUser);
			if (shopUser != null) {
				Util.writeOk(resMap);
				resMap.put("message", "登录成功！");
			}else{
				Util.writeError(resMap);
				resMap.put("message", "用户名或密码错误！");
			}
		}catch(Exception e){
			Util.writeError(resMap);
			resMap.put("message", "登录系统发生异常！");
		}
		return resMap;
	}
	
	/**
	 * 商户注册
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/shopRegist")
	@ResponseBody
	public Object shopRegist(HttpServletRequest request,@RequestBody ShopUser shopUser) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try{
			//md5加密密码
			String pwd=Util.getMD5(shopUser.getPassword());
			shopUser.setPassword(pwd);
			int num=shopUserService.insertShop(shopUser);
			if (num>0) {
				resMap.put("status", "ok");
				resMap.put("message", "注册成功！");
			}else{
				resMap.put("status", "fail");
				resMap.put("message", "注册失败！");
			}
		}catch(Exception e){
			resMap.put("status", "fail");
			resMap.put("message", "注册系统发生异常！");
		}
		Util.writeOk(resMap);
		return resMap;
	}
	
	/**
	 * 商户密码修改
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/updPwd")
	@ResponseBody
	public Object updPwd(HttpServletRequest request, String account, String password,String newPwd) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try{
			if (Tools.isEmpty(password)|| Tools.isEmpty(newPwd)) {
				Util.writeError(resMap);
				resMap.put("message", "原密码或新密码不能为空！");
				logger.error("account和password是必传参数");
				return resMap;
			}
			//md5加密密码
			String pwd=Util.getMD5(password);
			//查询原密码
			ShopUser shopUser=shopUserService.selectShop(account, pwd);
			if (shopUser==null) {
				Util.writeError(resMap);
				resMap.put("message", "原密码错误！");
				logger.error("account和password是必传参数");
				return resMap;
			}
			if((shopUser.getPassword()).equals(pwd)&&(shopUser.getAccount()).equals(account)){
				ShopUser newshopUser=new ShopUser();
				//md5加密密码
				String newPassword=Util.getMD5(newPwd);
				newshopUser.setPassword(newPassword);
				newshopUser.setAccount(account);
				newshopUser.setId(shopUser.getId());
				shopUserService.updateShop(newshopUser);
			}
			Util.writeOk(resMap);
			resMap.put("message", "修改成功！");
		}catch(Exception e){
			Util.writeError(resMap);
			resMap.put("message", "系统发生异常！");
		}
		return resMap;
	}
	
	
	/**
	 * app版本信息获取
	 * @param request
	 * @return
	 */
	@RequestMapping("/updVersion")
	@ResponseBody
	public Object updVersion(HttpServletRequest request, String account, String password,String newPwd) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try{
			Map<String,Object> map= PropertiesUtil.getAppVersion();
			if(map!=null&&map.size()>0){
				resMap.put("status", "ok");
				resMap.put("message", "获取app版本信息成功！");
				resMap.put("resultData",map);
				return resMap;
			}else{
				resMap.put("status", "fail");
				resMap.put("message", "获取app版本信息失败！");
				return resMap;
			}
		}catch(Exception e){
			Util.writeError(resMap);
			resMap.put("message", "系统发生异常！");
			return resMap;
		}
	}
}
