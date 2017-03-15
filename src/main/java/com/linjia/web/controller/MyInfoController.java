package com.linjia.web.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.Utils;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.linjia.base.controller.BaseController;
import com.linjia.constants.Application;
import com.linjia.constants.Constants;
import com.linjia.tools.DateComFunc;
import com.linjia.tools.JSONUtil;
import com.linjia.tools.RedisUtil;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.model.AccountcashdepositConfigAmount;
import com.linjia.web.model.AccountcashdepositConfigBase;
import com.linjia.web.model.AccountcashdepositRecord;
import com.linjia.web.model.ActivityTradeProduct;
import com.linjia.web.model.Address;
import com.linjia.web.model.CustMaster;
import com.linjia.web.model.FavouriteProduct;
import com.linjia.web.model.Feedback;
import com.linjia.web.model.Member;
import com.linjia.web.model.OrderGroup;
import com.linjia.web.model.OrderGroupProduct;
import com.linjia.web.model.OrderRefund;
import com.linjia.web.model.Product;
import com.linjia.web.query.FavouriteProductQuery;
import com.linjia.web.query.OrderGroupQuery;
import com.linjia.web.query.OrderRefundQuery;
import com.linjia.web.service.AccountcashdepositConfigService;
import com.linjia.web.service.AccountcashdepositRecordService;
import com.linjia.web.service.ActivityTradeProductService;
import com.linjia.web.service.AddressService;
import com.linjia.web.service.CustMasterService;
import com.linjia.web.service.FavouriteProductService;
import com.linjia.web.service.FeedbackService;
import com.linjia.web.service.OrderGroupProductService;
import com.linjia.web.service.OrderGroupService;
import com.linjia.web.service.OrderPayService;
import com.linjia.web.service.OrderRefundService;
import com.linjia.web.service.ProductService;
import com.linjia.web.service.RegistMemberService;
import com.linjia.web.service.UserCardCouponService;
import com.linjia.web.thirdService.message.model.Message;
import com.linjia.web.thirdService.message.service.MessageService;
import com.linjia.wxpay.common.Signature;

/**
 * 我的模块
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/my")
public class MyInfoController extends BaseController {

	@Autowired
	private OrderGroupService orderGroupService;

	@Autowired
	private OrderRefundService orderRefundService;

	@Autowired
	private OrderGroupProductService orderGroupProductService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private RegistMemberService registMemberService;

	@Autowired
	private FeedbackService feedbackService;

	@Autowired
	private UserCardCouponService userCardCouponService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private CustMasterService custMasterService;

	@Autowired
	private AccountcashdepositRecordService accountcashdepositRecordService;

	@Autowired
	private OrderPayService orderPayService;

	@Autowired
	private AccountcashdepositConfigService accountcashdepositConfigService;

	@Autowired
	private FavouriteProductService favouriteProductService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ActivityTradeProductService activityTradeProductService;

	/**
	 * 取得我的页面信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getMyPageInfo")
	@ResponseBody
	public Object getMyPageInfo(HttpServletRequest request, @RequestParam(value = "userId", required = true) String userId, String mallCode) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();

		// 取得账户积分和余额
		JSONObject accountInfo = Util.queryDesaccount(userId);
		if (accountInfo != null) {
			if (accountInfo.optInt("error_code") == 20305) {
				logger.info("储值账户没有找到");
				// 账户积分
				resultData.put("score", 0);
				// 账户余额
				resultData.put("balance", 0);
			}else{
			// 账户积分
			resultData.put("score", accountInfo.optInt("score"));
			// 账户余额
			resultData.put("balance", accountInfo.optDouble("balance"));
			}
		} else {
			resMap.put("message", "用户不存在");
			Util.writeError(resMap);
			return resMap;
		}

		// 取得优惠券数量
		Long cardCouponCount = userCardCouponService.selectCount(userId, mallCode);
		resultData.put("cardCouponCount", cardCouponCount);
		resMap.put("resultData", resultData);
		Util.writeSuccess(resMap);
		Util.writeOk(resMap);
		return resMap;
	}

	/**
	 * 取得我的全部订单订单
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getMyAllOrderList")
	@ResponseBody
	public Object getMyAllOrderList(HttpServletRequest request, OrderGroupQuery query) {
		Map<String, Object> resMap = new HashMap<String, Object>();

		if (Tools.isEmpty(query.getUserId())) {
			resMap.put("message", "userId为必传参数！");
			Util.writeError(resMap);
			return resMap;
		}
		List<OrderGroup> orderGroupList = orderGroupService.selectMyAllOrderList(query);
		if (orderGroupList != null && orderGroupList.size() > 0) {
			// 取得订单关联的商品列表
			relateProductListForOrder(orderGroupList);
		}

		resMap.put("resultData", orderGroupList);
		Util.writeSuccess(resMap);
		Util.writeOk(resMap);
		return resMap;
	}

	/**
	 * 为订单关联相关的商品记录
	 * 
	 * lixinling 
	 * 2016年8月2日 下午1:19:27
	 * @param orderGroupList
	 */
	public void relateProductListForOrder(List<OrderGroup> orderGroupList) {
		for (OrderGroup item : orderGroupList) {
			List<OrderGroupProduct> productList = orderGroupProductService.selectProductListByGroupId(item.getId());
			if (productList != null && productList.size() > 0) {
				for (OrderGroupProduct item2 : productList) {
					if (!Tools.isEmpty(item2.getTradeProductIds())) {
						String[] tradeProductIdArray = item2.getTradeProductIds().split(",");
						Long activityId = item2.getTradeActivityId();
						
						List<ActivityTradeProduct> activityTradeProductList = activityTradeProductService.selectTradedProduct(tradeProductIdArray,activityId);
						item2.setActivityTradeProductList(activityTradeProductList);
					}
				}
			}
			item.setProductList(productList);
		}
	}

	/**
	 * 取得我的退款单
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getMyRefundOrderList")
	@ResponseBody
	public Object getMyRefundOrderList(HttpServletRequest request, OrderRefundQuery query) {
		Map<String, Object> resMap = new HashMap<String, Object>();

		if (Tools.isEmpty(query.getUserId())) {
			resMap.put("message", "userId为必传参数！");
			Util.writeError(resMap);
			return resMap;
		}

		List<OrderRefund> orderRefundList = orderRefundService.selectMyRefundOrderList(query);

		resMap.put("orderRefundList", orderRefundList);
		Util.writeSuccess(resMap);
		Util.writeOk(resMap);
		return resMap;
	}

	/**
	 * 取得我的账户管理信息
	 * 
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/getMyAccountInfo")
	@ResponseBody
	public Object getMyAccountInfo(HttpServletRequest request, @RequestParam(value = "userId", required = true) String userId)
			throws UnsupportedEncodingException {
		Map<String, Object> resMap = new HashMap<String, Object>();
		String callbackFunName = request.getParameter("callback");// 得到js函数名称

		String userInfo = RedisUtil.get(Application.USERINFO_PREFIX + userId);
		resMap.put("resultData", JSONUtil.jsonToMap(userInfo.getBytes("UTF-8")));
		Util.writeSuccess(resMap);
		Util.writeOk(resMap);
		if (Tools.isEmpty(callbackFunName)) {
			return resMap;
		} else {
			return new JSONPObject(callbackFunName, resMap);
		}
	}

	/**
	 * 更新我的账户管理信息
	 * 
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
	 */
	@RequestMapping("/updateMyAccountInfo")
	@ResponseBody
	public Object updateMyAccountInfo(HttpServletRequest request, Member m, @RequestParam(value = "userId", required = true) String userId)
			throws JSONException, UnsupportedEncodingException {
		Map<String, Object> resMap = new HashMap<String, Object>();
		String callbackFunName = request.getParameter("callback");// 得到js函数名称
		if (Tools.isEmpty(m.getName()) && Tools.isEmpty(m.getSex()) && m.getBirthDayStr() == null && Tools.isEmpty(m.getBindPhone())) {
			resMap.put("message", "请输入要更新的信息");
			Util.writeError(resMap);
			if (Tools.isEmpty(callbackFunName)) {
				return resMap;
			} else {
				return new JSONPObject(callbackFunName, resMap);
			}
		}

		// 通过userId从缓存中取出用户信息
		// 参数格式：{"code":"WEB160628000066","name":"LXL333","nameSpell":null,"birthday":"1989-06-10 00:00:00","memorialDay":null,"paperType":"OTHERPAPER","paperTypeCh":"\u5176\u5b83\u8bc1\u4ef6","paperCode":"WEB160628000066","cellPhone":"18510576310","occupation":null,"specialty":null,"nationality":null,"presenter":null,"memberGrade":null,"fixedPhone":null,"email":"","postCode":null,"address":null,"remark":null,"sex":"male","sexCh":"\u7537","wedding":"K","weddingCh":"\u4fdd\u5bc6","org":{"uuid":"40288056517fbc7901518aaec0c60032","code":"WEB","name":"\u7ebf\u4e0a\u865a\u62df\u95e8\u5e97"},"originCode":"WEB160628000066","appName":"HDCARD","login":"WEB160628000066","activityHeat":0,"memberSource":null,"registerDate":"2016-06-28 09:52:17","extAttributes":[],"wx":null,"memberGradeOrder":0,"password":null,"qq":null}

		// memberObj = Util.queryMemberByMobile(userId);
		// String memberStr =Util.getUserInfo(userId);
		CustMaster custMaster = new CustMaster();
		JSONObject jsonData = Util.queryMemberByLogin(userId);
		if (!Tools.isEmpty(m.getName())) {
			jsonData.put("name", URLDecoder.decode(m.getName(), "UTF-8"));
			custMaster.setCustname(URLDecoder.decode(m.getName(), "UTF-8"));
		}
		if (!Tools.isEmpty(m.getSex())) {
			jsonData.put("sex", m.getSex());
			custMaster.setSex(m.getSex());
		}
		if (m.getBirthDayStr() != null) {
			jsonData.put("birthday", DateComFunc.formatDate(DateComFunc.strToDate(m.getBirthDayStr(), "yyyy-MM-dd"), "yyyy-MM-dd HH:mm:ss"));
			custMaster.setBirthday(DateComFunc.strToDate(m.getBirthDayStr(), "yyyy-MM-dd"));
		}

		// 调用CRM用户更新
		jsonData.remove("responseCode");
		String result = Util.updateMember(jsonData);
		if (result == null) {
			resMap.put("message", "更新失败");
			Util.writeError(resMap);
			if (Tools.isEmpty(callbackFunName)) {
				return resMap;
			} else {
				return new JSONPObject(callbackFunName, resMap);
			}
		}

		// 更新商城用户信息
		custMaster.setUserId(userId);
		custMasterService.updateByPrimaryKey(custMaster);

		// 更新用户信息缓存
		updateRedisCache(userId);

		resMap.put("message", "更新成功");
		Util.writeOk(resMap);

		if (Tools.isEmpty(callbackFunName)) {
			return resMap;
		} else {
			return new JSONPObject(callbackFunName, resMap);
		}
	}

	/**
	 * 上传用户头像
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/uploadHeadPic")
	@ResponseBody
	public Object uploadHeadPic(HttpServletRequest request, Member m, @RequestParam(value = "userId", required = true) String userId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		MultipartFile f = m.getHeadPic();

		if (f.getOriginalFilename() != null && !"".equals(f.getOriginalFilename())) {
			m.setHeadPicFileName(uploadHeadPic(f, userId));
		}

		// 更新用户头像路径
		CustMaster custMaster = new CustMaster();
		custMaster.setHeadPic(m.getHeadPicFileName());
		custMaster.setUserId(userId);
		custMasterService.updateByPrimaryKey(custMaster);

		// 更新用户信息缓存
		updateRedisCache(userId);

		resMap.put("message", "上传成功");
		Util.writeOk(resMap);
		return resMap;
	}

	/**
	 * 查看我的余额和储值流水
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getMyBalanceInfo")
	@ResponseBody
	public Object getMyBalanceInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "userId", required = true) String userId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		String origincode = userId;
		// 查询储值流水
		JSONObject desaccounthst = Util.queryDesaccounthst(null, null, origincode, null);
		if (desaccounthst == null) {
			resMap.put("message", "我的余额和储值流水查询为空");
			Util.writeError(resMap);
			return resMap;
		}
		resultData.put("strDesaccounthst", JSONUtil.fromJson(desaccounthst.toString(), Map.class));
		resMap.put("resultData", resultData);
		Util.writeSuccess(resMap);
		Util.writeOk(resMap);
		return resMap;
		/*
		 * String result = "{\"status\":\"ok\",\"msg\":\"更新成功\",\"desaccount\":"
		 * + strDesaccoun + ",\"desaccounthst\":" + strDesaccounthst + "}"; try
		 * { response.setCharacterEncoding("utf-8");
		 * response.getWriter().print(result); } catch (IOException e) {
		 * e.printStackTrace(); }
		 */
	}

	/**
	 * 查看我的积分详情
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getMyScoreInfo")
	@ResponseBody
	public Object getMyScoreInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "userId", required = true) String userId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();

		// 查询账户的余额和积分
		JSONObject desaccount = Util.queryDesaccount(userId);
		if (desaccount == null) {
			resMap.put("message", "积分详情为空");
			Util.writeError(resMap);
			return resMap;
		}
		resultData.put("strDesaccoun", JSONUtil.fromJson(desaccount.toString(), Map.class));
		resMap.put("resultData", resultData);
		Util.writeSuccess(resMap);
		Util.writeOk(resMap);
		return resMap;
	}

	/**
	 * 查看我的积分详情
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getMyScoreHst")
	@ResponseBody
	public Object getMyScoreHst(HttpServletRequest request, Integer pageNum, @RequestParam(value = "userId", required = true) String userId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();

		if (pageNum == null)
			pageNum = 0;

		// 查询账户的积分流水
		JSONObject scorehst = Util.queryScorehst(userId, pageNum);
		resultData.put("scorehst", JSONUtil.fromJson(scorehst.toString(), Map.class));
		resMap.put("resultData", resultData);
		Util.writeSuccess(resMap);
		Util.writeOk(resMap);
		return resMap;
	}

	/**
	 * 修改钱包支付密码(验证短信验证码)
	 * 
	 * @param request
	 * @param verCode 用户输入的短信验证码
	 * @return
	 */
	@RequestMapping("/checkVerificationCode")
	@ResponseBody
	public Object checkVerificationCode(HttpServletRequest request, String userId, String verCode,
			@RequestParam(value = "phone", required = true) String phone) {
		Map<String, Object> resMap = new HashMap<String, Object>();

		if (Tools.isEmpty(verCode)) {
			resMap.put("message", "验证码不能为空.");
			Util.writeError(resMap);
			return resMap;
		}
		// 验证短信验证码反馈验证结果
		Message message = messageService.selectMessage(phone);
		if (verCode.equals(message.getCheckcode())) {
			messageService.deleteMessage(message.getId());

			// 将短信验证码存入redis中10分钟自动超时
			RedisUtil.put("verCode_" + userId, verCode, 600);
			resMap.put("message", "验证成功");
			Util.writeOk(resMap);
		} else {
			resMap.put("message", "验证码输入不正确.");
			Util.writeError(resMap);
		}

		return resMap;
	}

	/**
	 * 更新钱包支付密码
	 * 
	 * @param request
	 * @param oldPwd 原密码
	 * @param newPwd 新密码
	 * @param confirmPwd 确认新密码
	 * @return
	 */
	@RequestMapping("/updatePayPassword")
	@ResponseBody
	public Object updatePayPassword(HttpServletRequest request, String oldPwd, String newPwd, String confirmPwd,
			@RequestParam(value = "userId", required = true) String userId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		String callbackFunName = request.getParameter("callback");// 得到js函数名称

		if (Tools.isEmpty(oldPwd) || Tools.isEmpty(newPwd) || Tools.isEmpty(confirmPwd)) {
			logger.info("原密码和新密码都不能为空");
			resMap.put("message", "原密码和新密码都不能为空");
			Util.writeError(resMap);
			return resMap;
		} else {
			/*
			 * JSONObject param = new JSONObject(body); String oldPwd =
			 * param.optString("oldPwd"); String newPwd =
			 * param.optString("newPwd"); String confirmPwd =
			 * param.optString("confirmPwd");
			 */
			if (Tools.isEmpty(oldPwd) || Tools.isEmpty(newPwd) || Tools.isEmpty(confirmPwd)) {
				logger.info("原密码和新密码都不能为空");
				resMap.put("message", "原密码和新密码都不能为空");
				Util.writeError(resMap);
				if (Tools.isEmpty(callbackFunName)) {
					return resMap;
				} else {
					return new JSONPObject(callbackFunName, resMap);
				}

			} else if (!newPwd.equals(confirmPwd)) {
				// 新密码和确认密码一致性
				logger.info("新密码和确认密码不一致");
				resMap.put("message", "新密码和确认密码不一致，请重新输入");
				Util.writeError(resMap);
				if (Tools.isEmpty(callbackFunName)) {
					return resMap;
				} else {
					return new JSONPObject(callbackFunName, resMap);
				}
			}

			// 进行CRM密码修改
			JSONObject result = Util.updatePursePayPwd(userId, newPwd, oldPwd);
			if (result != null && result.optInt("responseCode") != 204) {
				resMap.put("message", result.optString("message"));
				Util.writeError(resMap);
				if (Tools.isEmpty(callbackFunName)) {
					return resMap;
				} else {
					return new JSONPObject(callbackFunName, resMap);
				}
			}

			// 更新本地密码记录
			String encryptPwd = Signature.getAESEncrypt(newPwd, userId);
			CustMaster custMaster = new CustMaster();
			custMaster.setUserId(userId);
			custMaster.setPaypwd(encryptPwd);
			custMasterService.updateByPrimaryKey(custMaster);

			resMap.put("message", "更新成功");
			Util.writeOk(resMap);
			if (Tools.isEmpty(callbackFunName)) {
				return resMap;
			} else {
				return new JSONPObject(callbackFunName, resMap);
			}

		}
	}

	/**
	 * 重置钱包支付密码
	 * 
	 * @param request
	 * @param oldPwd 原密码
	 * @param newPwd 新密码
	 * @param confirmPwd 确认新密码
	 * @return
	 */
	@RequestMapping("/resetPayPassword")
	@ResponseBody
	public Object resetPayPassword(HttpServletRequest request, String newPwd, String confirmPwd,
			@RequestParam(value = "userId", required = true) String userId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		String callbackFunName = request.getParameter("callback");// 得到js函数名称

		if (Tools.isEmpty(newPwd) || Tools.isEmpty(confirmPwd)) {
			logger.info("新密码和确认密码都不能为空");
			resMap.put("message", "新密码和确认密码都不能为空");
			Util.writeError(resMap);
			return resMap;
		} else if (!newPwd.equals(confirmPwd)) {
			// 新密码和确认密码一致性
			logger.info("新密码和确认密码不一致");
			resMap.put("message", "新密码和确认密码不一致，请重新输入");
			Util.writeError(resMap);
			if (Tools.isEmpty(callbackFunName)) {
				return resMap;
			} else {
				return new JSONPObject(callbackFunName, resMap);
			}
		}

		// 从redis中取出用户输入的验证码
		String mobile_securitycode = RedisUtil.get("verCode_" + userId);
		if (Tools.isEmpty(mobile_securitycode)) {
			logger.info("重置密码超时，请在10分钟之内完成");
			resMap.put("message", "重置密码超时，请在10分钟之内完成");
			Util.writeError(resMap);
			if (Tools.isEmpty(callbackFunName)) {
				return resMap;
			} else {
				return new JSONPObject(callbackFunName, resMap);
			}
		}

		// 进行CRM密码修改
		JSONObject result = Util.resetPursePayPwd(System.currentTimeMillis() / 1000, userId, newPwd, mobile_securitycode);
		if (result != null && result.optInt("responseCode") != 204) {
			resMap.put("message", result.optString("message"));
			Util.writeError(resMap);
			if (Tools.isEmpty(callbackFunName)) {
				return resMap;
			} else {
				return new JSONPObject(callbackFunName, resMap);
			}
		}

		// 更新本地密码记录
		String encryptPwd = Signature.getAESEncrypt(newPwd, userId);
		CustMaster custMaster = new CustMaster();
		custMaster.setUserId(userId);
		custMaster.setPaypwd(encryptPwd);
		custMasterService.updateByPrimaryKey(custMaster);

		resMap.put("message", "更新成功");
		Util.writeOk(resMap);
		if (Tools.isEmpty(callbackFunName)) {
			return resMap;
		} else {
			return new JSONPObject(callbackFunName, resMap);
		}
	}

	/**
	 * 地址管理
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getMyAddressList")
	@ResponseBody
	public Object getMyAddressList(HttpServletRequest request, @RequestParam(value = "userId", required = true) String userId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Address> addressList = addressService.selectListByUserId(userId);

		resMap.put("resultData", addressList);
		Util.writeSuccess(resMap);
		Util.writeOk(resMap);

		return resMap;
	}

	/**
	 * 上传用户头像
	 * 
	 * lixinling 
	 * 2016年8月2日 下午4:59:45
	 * @param f
	 * @param userId
	 * @return
	 */
	public String uploadHeadPic(MultipartFile f, String userId) {
		String fileName = f.getOriginalFilename();
		String ext = fileName.substring(fileName.lastIndexOf("."), fileName.length());
		String newFileName = userId + System.currentTimeMillis() + ext;

		System.out.println("name:" + fileName + " fileName :" + fileName + " ext:" + ext);

		// 将文件上传到指定的位置
		File file = null;

		file = new File("/mnt/headImg/");
		if (file.exists() == false) {
			file.mkdirs();
		}
		file = new File(file, newFileName);
		try {
			// 保存上传的文件
			f.transferTo(file);
			System.out.println("文件保存成功");
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}

		return newFileName;
	}

	/**
	 * 意见反馈
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/feedback")
	@ResponseBody
	public Object feedback(HttpServletRequest request, Feedback model) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (model == null || Tools.isEmpty(model.getUserId())) {
			resMap.put("message", "反馈内容或联系方式不能为空");
			Util.writeError(resMap);
			return resMap;
		}
		if (!Tools.isEmpty(model.getComment()))
			model.setComment(Tools.URLDecoderStr(model.getComment()));
		model.setCustname(Util.getUserInfo(model.getUserId()).optString("custname"));
		model.setCreDate(new Date());
		int row = feedbackService.insert(model);
		if (row == 1) {
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} else {
			Util.writeFail(resMap);
			Util.writeError(resMap);
		}

		return resMap;
	}

	/**
	 * 更新缓存信息
	 * 
	 * @param request
	 * @return
	 */
	public void updateRedisCache(String userId) {
		// 因为用户是存在的，所以用户可能编辑过头像信息，因此重新从商城用户表取得用户信息
		CustMaster custMaster = custMasterService.selectById(userId);
		// 放入缓存
		RedisUtil.put(Application.USERINFO_PREFIX + userId, JSONUtil.toJson(custMaster), Application.USERINFO_EXPIRE);
	}

	/**
	 * 钱包充值
	 * 
	 * @param request
	 * @param userId  用户id
	 * @param occur  充值金额
	 * @return
	 */
	@RequestMapping("/accountcashdeposit")
	@ResponseBody
	public Object accountcashdeposit(HttpServletRequest request, String userId, String occur, String openid) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		String spbill_create_ip = request.getRemoteAddr();

		if (Tools.isEmpty(userId) || Tools.isEmpty(occur) || "0".equals(occur)) {
			resMap.put("message", "userId和充值金额不能为空");
			Util.writeError(resMap);
			return resMap;
		}

		AccountcashdepositRecord accountcashdepositRecord = new AccountcashdepositRecord();
		accountcashdepositRecord.setAccountaccesscode(userId);
		accountcashdepositRecord.setAccountaccesstype(2);
		accountcashdepositRecord.setOccur(new BigDecimal(occur));
		accountcashdepositRecord.setRealpay(new BigDecimal(occur));
		accountcashdepositRecord.setTrantime(new Date());
		accountcashdepositRecord.setXid(System.currentTimeMillis() / 1000);
		accountcashdepositRecord.setAction(Constants.ACCOUNTCASH_ACTION.DEPOSIT_ACCOUNT);
		AccountcashdepositRecord model = accountcashdepositRecordService.insertRecord(accountcashdepositRecord, spbill_create_ip, openid);

		if (model != null) {
			// 进行微信支付
			String notify_url = Application.getRBasePath() + "/pay/notifyUrlAccountcashdeposit";
			resMap = orderPayService.payHandle(Constants.PAY_TYPE.WX, "钱包充值", null, model.getRealpay(), model.getTranId(),
					spbill_create_ip, model.getAccountaccesscode(), openid, notify_url, null);

			if ("error".equals(resMap.get("status"))) {
				if ("10001".equals(resMap.get("error_code"))) {
					resMap.put("message", "构造支付请求数据对象错误，请重新进行微信授权.");
				} else if ("10003".equals(resMap.get("error_code"))) {
					resMap.put("message", "支付请求API返回的数据签名验证失败，有可能数据被篡改了.");
				} else {
					resMap.put("message", "系统错误.");
				}
				Util.writeError(resMap);
				return resMap;
			}
		}

		Util.writeSuccess(resMap);
		Util.writeOk(resMap);
		return resMap;
	}

	/**
	 * 取得钱包充值配置信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getAccountcashdepositConfig")
	@ResponseBody
	public Object getAccountcashdepositConfig(HttpServletRequest request) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		AccountcashdepositConfigBase accountcashdepositConfigBase = accountcashdepositConfigService.selectExistRecord();
		if (accountcashdepositConfigBase == null) {
			resMap.put("message", "充值配置信息不存在");
			Util.writeError(resMap);
			return resMap;
		} else {
			List<AccountcashdepositConfigAmount> amountList = accountcashdepositConfigService
					.selectAllByBaseId(accountcashdepositConfigBase.getId());
			accountcashdepositConfigBase.setAmountList(amountList);
			resMap.put("resultData", accountcashdepositConfigBase);
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		}

		return resMap;
	}

	/**
	 * 取得商品关注列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getFavouriteProduct")
	@ResponseBody
	public Object getFavouriteProduct(HttpServletRequest request, FavouriteProductQuery query) {
		Map<String, Object> resMap = new HashMap<String, Object>();

		List<Product> favouriteProductList = productService.selectFavouriteProductList(query);
		
		//针对全部商品的折扣活动，计算商品的折扣价
		productService.handleProductList(favouriteProductList, query.getMallCode());

		resMap.put("resultData", favouriteProductList);
		Util.writeSuccess(resMap);
		Util.writeOk(resMap);

		return resMap;
	}

	/**
	 * 加入商品关注
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/addFavouriteProduct")
	@ResponseBody
	public Object addFavouriteProduct(HttpServletRequest request, FavouriteProduct favouriteProduct) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (favouriteProduct == null || Tools.isEmpty(favouriteProduct.getUserId()) || Tools.isEmpty(favouriteProduct.getProductId())
				|| Tools.isEmpty(favouriteProduct.getpCode())) {
			resMap.put("message", "传入参数不正确");
			Util.writeError(resMap);
			return resMap;
		}

		favouriteProduct.setCreDate(new Date());

		favouriteProductService.insert(favouriteProduct);

		Util.writeSuccess(resMap);
		Util.writeOk(resMap);

		return resMap;
	}

	/**
	 * 删除商品关注中的记录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/delFavouriteProduct")
	@ResponseBody
	public Object delFavouriteProduct(HttpServletRequest request, @RequestParam(value = "userId", required = true) String userId,
			String productId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (Tools.isEmpty(productId)) {
			resMap.put("message", "传入的productId为空");
			Util.writeError(resMap);
			return resMap;
		} else {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userId", userId);
			String[] pIdArray = productId.split(",");
			params.put("array", pIdArray);
			favouriteProductService.deleteByProIDAndUserId(params);
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);

			return resMap;
		}
	}

}
