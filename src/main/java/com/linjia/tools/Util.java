package com.linjia.tools;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linjia.constants.Application;
import com.linjia.web.query.OrderDirectionBean;
import com.linjia.web.query.QueryConditionBean;
import com.linjia.web.query.QueryDefinitionBean;
import com.linjia.web.query.QueryParameterBean;
import com.linjia.web.query.ScoreSubjectBean;
import com.linjia.web.query.ScoreTypeBean;
import com.linjia.web.query.SubjectAccountsBean;
import com.linjia.web.thirdService.eleme.oauth.OAuthClient;
import com.linjia.web.thirdService.eleme.oauth.Token;
import com.linjia.web.thirdService.jddj.config.JDDJConfigure;
import com.linjia.web.uhd123.model.DeliverRequest;
import com.linjia.web.uhd123.model.Order;
import com.linjia.web.uhd123.model.OrderStockLock;
import com.linjia.web.uhd123.model.ReturnRequest;
import com.linjia.web.uhd123.model.Rtn;
import com.linjia.web.uhd123.model.Topics;
import com.linjia.wxpay.common.Configure;

public class Util {
	protected static final Logger logger = LoggerFactory.getLogger(Util.class);

	/**
	 * 取得分隔后值
	 * @param strVaue 值
	 * @param flag    标记
	 * @return 值数组
	 */
	public static String[] stringToKenizer(String strVaue, String flag) {
		StringTokenizer st = new StringTokenizer(strVaue, flag);

		int len = st.countTokens();
		String[] strs = new String[len];

		for (int k = 0; k < len; k++) {
			strs[k] = st.nextToken();
		}

		return strs;
	}

	public static String getStringFromArray(String[] strData) {
		StringBuffer sbResult = new StringBuffer("");
		if (strData == null)
			return sbResult.toString();

		for (int i = 0; i < strData.length; i++) {
			if (strData[i] != null && strData[i].trim().length() > 0)
				sbResult.append(",").append(strData[i].trim());
		}

		return sbResult.toString();
	}

	/**
	 * 计算MD5
	 * @param input
	 * @return
	 */
	public static String getMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);
			// Now we need to zero pad it if you actually want the full 32
			// chars.
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext.toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 删除当天之外的文件
	 * @throws Exception
	 * @author lizhl
	 */
	public static void removeFolder(String dateValue, String picPath) throws Exception {
		File fileOut = new File(picPath);
		File[] files = fileOut.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.getName().startsWith(dateValue)) {// 当天生成的文件不做操作
			} else {// 非今天生成的文件都删除
				if (file.exists()) {// 验证文件是否存在
					file.delete();
				}
			}
		}
	}

    public static String filterOffUtf8Mb4(String text) throws UnsupportedEncodingException {
		byte[] bytes = text.getBytes("utf-8");
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		int i = 0;
		while (i < bytes.length) {
			short b = bytes[i];
			if (b > 0) {
				buffer.put(bytes[i++]);
				continue;
			}

			b += 256;

			if (((b >> 5) ^ 0x6) == 0) {
				buffer.put(bytes, i, 2);
				i += 2;
			} else if (((b >> 4) ^ 0xE) == 0) {
				buffer.put(bytes, i, 3);
				i += 3;
			} else if (((b >> 3) ^ 0x1E) == 0) {
				i += 4;
			} else if (((b >> 2) ^ 0x3E) == 0) {
				i += 5;
			} else if (((b >> 1) ^ 0x7E) == 0) {
				i += 6;
			} else {
				buffer.put(bytes[i++]);
			}
		}
		buffer.flip();
		return new String(buffer.array(), "utf-8");
	}

	public static void writeOk(Map<String, Object> resMap) {
		resMap.put("status", "ok");
	}

	public static void writeError(Map<String, Object> resMap) {
		resMap.put("status", "error");
	}

	public static void writeSuccess(Map<String, Object> resMap) {
		resMap.put("message", "success");
	}

	public static void writeFail(Map<String, Object> resMap) {
		resMap.put("message", "系统错误");
	}

	/**
	 * 慎用，有覆盖的风险
	 * lixinling 
	 * 2016年8月25日 下午8:16:38
	 * @param resMap
	 * @param data
	 */
	public static void writeResultData(Map<String, Object> resMap, Object data) {
		resMap.put("resultData", data);
	}

	/********************************************************************/
	/**                        CRM系统接口调用部分                                                                 */
	/********************************************************************/
	/**
	 * 根据手机号取得会员信息(当会员绑定的手机号变更后将匹配不到原来的会员，因此尽量少用)
	 * lixinling
	 * 2016年7月21日 上午10:17:36
	 * @param pCode
	 * @return
	 * {
	"exceptionClass": "com.hd123.card.common.ejb.exception.NotFoundException",
	"message": "会员没有找到",
	"stackTrace": [],
	"cause": null,
	"error_code": 20205
	}
	 */
	public static JSONObject queryMemberByMobile(String phone) {
		String urlstr = "http://124.193.154.242:58280/hdcard-services/api/member/query_by_mobilenum?mobilenum=" + phone;
		String Authorization = "guest:guest";
		JSONObject result = HttpRequestUtils.httpGet(urlstr, Authorization);
		// JSONObject result = NetRequest.requestGet(urlstr, null,
		// Authorization);
		return result;
	}

	/**
	 * 根据"登录名"查找会员
	 * lixinling
	 * 2016年7月21日 上午10:17:36
	 * @param pCode
	 * @return
	 */
	public static JSONObject queryMemberByLogin(String login) {
		String urlstr = "http://124.193.154.242:58280/hdcard-services/api/member/query_by_login?login=" + login + "&appname=HDCARD";
		String Authorization = "guest:guest";
		// JSONObject result = NetRequest.requestGet(urlstr, null,
		// Authorization);
		JSONObject result = HttpRequestUtils.httpGet(urlstr, Authorization);
		return result;
	}

	/**
	 * 根据账户识别码查询储值账户的余额和积分
	 * lixinling
	 * 2016年7月21日 上午10:17:36
	 * @param pCode
	 * @return
	 * {
	"balance": 0, //余额
	"score": 144, //积分
	"accountAccesss": [],
	"scoreAccount": {
	"subjectAccounts": [
	  {
	    "scoreType": {
	      "uuid": "scoretypeuuid",
	      "code": "-",
	      "name": "系统默认"
	    },
	    "scoreSubject": {
	      "uuid": "-",
	      "code": "101",
	      "name": "消费"
	    },
	    "score": 144,
	    "remark": null
	  }
	]
	}
	}
	 */
	public static JSONObject queryDesaccount(String loginCode) {
		String urlstr = "http://124.193.154.242:58280/hdcard-services/api/desaccount/query?account_accesscode=" + loginCode
				+ "&account_accesstype=2";
		String Authorization = "guest:guest";
		// JSONObject result = NetRequest.requestGet(urlstr, null,
		// Authorization);
		JSONObject result = HttpRequestUtils.httpGet(urlstr, Authorization);
		return result;
	}

	/**
	 * 根据账户识别码查询积分流水
	 * lixinling
	 * 2016年7月21日 上午10:17:36
	 * @param pCode
	 * @return
	 * {
	"paging": {
	"pageSize": 2,
	"page": 1,
	"pageCount": 2,
	"recordCount": 4,
	"more": false
	},
	"records": [
	{
	        "xid":"1474352987",
	        "tranId":"4028805656e34f6701574649167a40fe",
	        "tranTime":"2016-09-20 14:29:37",
	        "terminalId":"-",
	        "store":{
	            "uuid":"40288056517fbc7901518aaec0c60032",
	            "code":"WEB",
	            "name":"线上虚拟门店"
	        },
	        "created":"2016-09-20 14:29:37",
	        "oldScore":143,
	        "occur":-50,
	        "scoreType":{
	            "uuid":"scoretypeuuid",
	            "code":"-",
	            "name":"系统默认"
	        },
	        "scoreSubject":{
	            "uuid":"-",
	            "code":"107",
	            "name":"兑奖"
	        },
	        "remark":"订单1201609200000052050扣减"
	    },
	{
	  "xid": "100017B-201607080276",
	  "tranId": "4028805655a281ba0155c98c6b663544",
	  "tranTime": "2016-07-08 16:06:56",
	  "terminalId": "100017B",
	  "store": {
	    "uuid": "23AF36FC97484B52E053D900A8C0614E",
	    "code": "100017",
	    "name": "回龙观西大街店"
	  },
	  "created": "2016-07-08 16:07:50",
	  "oldScore": 0,
	  "occur": 124,
	  "scoreType": {
	    "uuid": "scoretypeuuid",
	    "code": "-",
	    "name": "系统默认"
	  },
	  "scoreSubject": {
	    "uuid": "-",
	    "code": "101",
	    "name": "消费"
	  },
	  "remark": null
	}
	]
	}
	 */
	public static JSONObject queryScorehst(String loginCode, int pageNum) {
		String urlstr = "http://124.193.154.242:58280/hdcard-services/api/scorehst/query?begin_trandate=2015-01-01&end_trandate="
				+ DateComFunc.formatDate(new Date(), "yyyy-MM-dd") + "&account_accesscode=" + loginCode + "&account_accesstype=2";
		String Authorization = "guest:guest";
		JSONObject body = new JSONObject();
		JSONArray ordersArray = new JSONArray();
		JSONObject item = new JSONObject();
		item.put("field", "tranTime");
		item.put("direction", "desc");
		ordersArray.put(item);
		body.put("orders", ordersArray);
		body.put("pageSize", "10");
		body.put("page", pageNum);

		// JSONObject result = NetRequest.requestPost(urlstr, body,
		// Authorization);
		JSONObject result = HttpRequestUtils.httpPost(urlstr, body, Authorization);
		return result;
	}

	/**
	 * 根据自定义查询条件, 查询储值流水
	 * lixinling 
	 * 2016年8月4日 下午4:00:09
	 * @param begin_trandate  业务发生起始时间, 格式为:yyyy-MM-dd, 可为空
	 * @param end_trandate  业务发生截至时间, 格式为:yyyy-MM-dd, 可为空	
	 * @param origincode  来源会员号, 不能为空
	 * @param action  动作, 可为空, 为空时查询所有动作
	 * @return
	 * {
	"paging": {
	    "pageSize": 300,
	    "page": 0,
	    "pageCount": 1,
	    "recordCount": 20,
	    "more": false
	},
	"records": [
	    {
	        "xid": "ed312014-d2ad-4c2c-b37f-272339e7bb30",
	        "tranId": "7501e6d0a57645869456cef5b4105c68",
	        "terminalId": "100019B",
	        "created": "2016-05-11 18:33:40",
	        "originCode": "WEB151215000099",
	        "cardNum": "1000751139146997",
	        "occur": 580,
	        "state": "未撤销",
	        "tranTime": "2016-05-11 18:30:54",
	        "action": "充值",
	        "remark": null
	    },
	    {
	        "xid": "402881a0549a8b4d01549f6057f13795",
	        "tranId": "40288056549f2df201549f6415a823d8",
	        "terminalId": "100019B",
	        "created": "2016-05-11 18:36:57",
	        "originCode": "WEB151215000099",
	        "cardNum": "1000751139146997",
	        "occur": 42.2,
	        "state": "未撤销",
	        "tranTime": "2016-05-11 18:34:25",
	        "action": "消费",
	        "remark": null
	    }
	  ]
	 }
	 */
	public static JSONObject queryDesaccounthst(String begin_trandate, String end_trandate, String origincode, String action) {
		StringBuilder pamram = new StringBuilder();
		if (!Tools.isEmpty(begin_trandate)) {
			pamram.append("begin_trandate=").append(begin_trandate);
		}
		if (!Tools.isEmpty(end_trandate)) {
			pamram.append(pamram.length() == 0 ? "end_trandate=" : "&end_trandate=").append(end_trandate);
		}
		if (!Tools.isEmpty(origincode)) {
			pamram.append(pamram.length() == 0 ? "origincode=" : "&origincode=").append(origincode);
		}
		if (!Tools.isEmpty(action)) {
			pamram.append(pamram.length() == 0 ? "action=" : "&action=").append(action);
		}

		String urlstr = "http://124.193.154.242:58280/hdcard-services/api/desaccounthst/query?" + pamram.toString();

		// 组装请求参数(目前只使用了origincode，其他的条件暂时没有找到数据库对应的字段)
		QueryDefinitionBean queryBean = new QueryDefinitionBean(300, 0, -1);
		List<QueryConditionBean> conditions = new ArrayList<QueryConditionBean>();
		List<OrderDirectionBean> orders = new ArrayList<OrderDirectionBean>();

		QueryConditionBean queryConditionBean = new QueryConditionBean();
		queryConditionBean.setOperation(" origincode equals ");
		List<QueryParameterBean> parameters = new ArrayList<QueryParameterBean>();

		QueryParameterBean queryParameterBean = new QueryParameterBean();
		queryParameterBean.setType("java.lang.String");
		queryParameterBean.setValue(origincode);
		parameters.add(queryParameterBean);
		queryConditionBean.setParameters(parameters);
		conditions.add(queryConditionBean);

		OrderDirectionBean orderDirectionBean = new OrderDirectionBean();
		orderDirectionBean.setField("tranTime");
		orderDirectionBean.setDirection("desc");
		orders.add(orderDirectionBean);

		queryBean.setConditions(conditions);
		queryBean.setOrders(orders);
		// System.out.println("body:" + new JSONObject(queryBean));

		String Authorization = "guest:guest";
		// JSONObject result = NetRequest.requestPost(urlstr, new
		// JSONObject(queryBean), Authorization);
		JSONObject result = HttpRequestUtils.httpPost(urlstr, new JSONObject(queryBean), Authorization);
		return result;
	}

	/**
	 * 根据门店code和商品code取得该商品的门店库存
	 * lixinling
	 * 2016年7月21日 上午10:17:36
	 * @param mallCode
	 * @param pCode
	 * @return
	 */
	public static Integer queryQtyByStore(String mallCode, String pCode) {
		/*
		 * String urlstr =
		 * "http://124.193.154.242:37080/h4rest-server/rest/h5rest-server/core/invservice/businv/query"
		 * ; JSONObject body = new JSONObject(); JSONArray conditions = new
		 * JSONArray(); JSONArray parameters1 = new JSONArray(); JSONArray
		 * parameters2 = new JSONArray(); JSONObject condition1 = new
		 * JSONObject(); JSONObject condition2 = new JSONObject(); JSONObject
		 * parameter1 = new JSONObject(); JSONObject parameter2 = new
		 * JSONObject(); condition1.put("operation", "store code equals ");
		 * parameter1.put("type", "java.lang.String"); parameter1.put("value",
		 * mallCode); parameters1.put(parameter1); condition1.put("parameters",
		 * parameters1); conditions.put(condition1);
		 * 
		 * condition2.put("operation", "skuid equals "); parameter2.put("type",
		 * "java.lang.String"); parameter2.put("value", pCode);
		 * parameters2.put(parameter2); condition2.put("parameters",
		 * parameters2); conditions.put(condition2); body.put("conditions",
		 * conditions); JSONObject result = NetRequest.requestPost(urlstr, body,
		 * null); if (result != null) { JSONArray businvs =
		 * result.optJSONArray("businvs"); if (businvs != null &&
		 * businvs.length() > 0) { JSONObject businv = businvs.optJSONObject(0);
		 * qty = businv.optInt("qty"); } }
		 */

		// 读取Redis中商品库存
		String key = "store_store:" + mallCode;
		String strQty = RedisUtil.hget(key, pCode);
		Integer qty = 0;
		if (!Tools.isEmpty(strQty)) {
			qty = Integer.valueOf(strQty);
			if (qty < 0)
				qty = 0;
		}
		return qty;
	}

	/**
	 * 根据手机号注册会员
	 * lixinling
	 * 2016年7月21日 上午10:17:36
	 * @param pCode
	 * @return
	 */
	public static JSONObject registMember(String phone, Long xid) {
		String urlstr = "http://124.193.154.242:58280/hdcard-services/api/member/regist?xid=" + xid + "&orgcode=WEB";
		String Authorization = "guest:guest";
		JSONObject body = new JSONObject();
		body.put("name", phone);
		body.put("cellPhone", phone);
		body.put("registerDate", DateComFunc.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		JSONObject result = NetRequest.requestPut(urlstr, body, Authorization);
		if (!Tools.isEmpty(result.optString("str"))) {
			result.put("code", result.remove("str"));
		}
		return result;
	}

	/**
	 * 更新CRM会员信息
	 * lixinling
	 * 2016年7月21日 上午10:17:36
	 * @param pCode
	 * @return
	 */
	public static String updateMember(JSONObject data) {
		String urlstr = "http://124.193.154.242:58280/hdcard-services/api/member/modify?xid=" + (System.currentTimeMillis() / 1000)
				+ "&orgcode=WEB";
		String Authorization = "guest:guest";

		String dataUni = StringEscapeUtils.escapeJava(data.toString()).replace("\\\"", "\"");
		JSONObject body = new JSONObject(dataUni);
		String result = NetRequest.requestPostStr(urlstr, body, Authorization);
		return result;
	}

	/**
	 * 减积分操作：/scoreaccount/dec
	 * lixinling 
	 * 2016年8月8日 下午4:25:17
	 * @param xid 外部交易号
	 * @param account_accesscode 账户识别码 (例如：WEB160209000045)
	 * @param remark 备注
	 * @param scoreSubjectCode 积分项目code
	 * @param score 积分值
	 * 
	 * requestBody:{"subjectAccounts":[{"remark":"\u8ba2\u53551201608050000044763\u6263\u51cf","scoreType":{"code":"-"},"scoreSubject":{"code":107},"score":120}]} 
	 * @return {"subjectAccounts":[{"scoreType":{"uuid":"scoretypeuuid","code":"-","name":"系统默认"},"scoreSubject":{"uuid":"-","code":"107","name":"兑奖"},"score":-2090,"remark":null},{"scoreType":{"uuid":"scoretypeuuid","code":"-","name":"系统默认"},"scoreSubject":{"uuid":"-","code":"101","name":"消费"},"score":2649,"remark":null}]}
	 */
	public static JSONObject decScoreAccount(Long xid, String account_accesscode, String remark, int scoreSubjectCode, int score) {
		String urlstr = "http://124.193.154.242:58280/hdcard-services/api/scoreaccount/dec?xid=" + xid + "&orgcode=WEB&account_accesscode="
				+ account_accesscode + "&account_accesstype=2";
		String Authorization = "guest:guest";

		// 构造请求参数
		JSONObject data = new JSONObject();
		JSONArray subjectAccounts = new JSONArray();
		data.put("subjectAccounts", subjectAccounts);
		SubjectAccountsBean subjectAccountsBean = new SubjectAccountsBean();
		subjectAccountsBean.setRemark(remark);
		subjectAccountsBean.setScore(score);
		ScoreTypeBean scoreTypeBean = new ScoreTypeBean();
		scoreTypeBean.setCode("-");
		subjectAccountsBean.setScoreType(scoreTypeBean);
		ScoreSubjectBean scoreSubjectBean = new ScoreSubjectBean();
		scoreSubjectBean.setCode(scoreSubjectCode);
		subjectAccountsBean.setScoreSubject(scoreSubjectBean);
		subjectAccounts.put(JSONUtil.ObjToJSON(subjectAccountsBean));

		String dataUni = StringEscapeUtils.escapeJava(data.toString()).replace("\\\"", "\"");
		JSONObject body = new JSONObject(dataUni);
		// JSONObject result = NetRequest.requestPost(urlstr, body,
		// Authorization);
		JSONObject result = HttpRequestUtils.httpPost(urlstr, body, Authorization);
		return result;
	}

	/**
	 * 增积分操作：/scoreaccount/inc
	 * lixinling 
	 * 2016年8月8日 下午4:25:17
	 * @param xid 外部交易号
	 * @param account_accesscode 账户识别码
	 * @param remark 备注
	 * @param scoreSubjectCode 积分项目code
	 * @param score 积分值
	 * 
	 * requestBody:{"subjectAccounts":[{"remark":"\u8ba2\u53551201608050000044763\u6263\u51cf","scoreType":{"code":"-"},"scoreSubject":{"code":107},"score":120}]} 
	 * @return {"subjectAccounts":[{"scoreType":{"uuid":"scoretypeuuid","code":"-","name":"系统默认"},"scoreSubject":{"uuid":"-","code":"107","name":"兑奖"},"score":-2090,"remark":null},{"scoreType":{"uuid":"scoretypeuuid","code":"-","name":"系统默认"},"scoreSubject":{"uuid":"-","code":"101","name":"消费"},"score":2649,"remark":null}]}
	 */
	public static JSONObject incScoreAccount(Long xid, String account_accesscode, String remark, int scoreSubjectCode, int score) {
		String urlstr = "http://124.193.154.242:58280/hdcard-services/api/scoreaccount/inc?xid=" + xid + "&orgcode=WEB&account_accesscode="
				+ account_accesscode + "&account_accesstype=2";
		String Authorization = "guest:guest";

		// 构造请求参数
		JSONObject data = new JSONObject();
		JSONArray subjectAccounts = new JSONArray();
		data.put("subjectAccounts", subjectAccounts);
		SubjectAccountsBean subjectAccountsBean = new SubjectAccountsBean();
		subjectAccountsBean.setRemark(remark);
		subjectAccountsBean.setScore(score);
		ScoreTypeBean scoreTypeBean = new ScoreTypeBean();
		scoreTypeBean.setCode("-");
		subjectAccountsBean.setScoreType(scoreTypeBean);
		ScoreSubjectBean scoreSubjectBean = new ScoreSubjectBean();
		scoreSubjectBean.setCode(scoreSubjectCode);
		subjectAccountsBean.setScoreSubject(scoreSubjectBean);
		subjectAccounts.put(JSONUtil.ObjToJSON(subjectAccountsBean));

		String dataUni = StringEscapeUtils.escapeJava(data.toString()).replace("\\\"", "\"");
		JSONObject body = new JSONObject(dataUni);
		// JSONObject result = NetRequest.requestPost(urlstr, body,
		// Authorization);
		JSONObject result = HttpRequestUtils.httpPost(urlstr, body, Authorization);
		return result;
	}

	/**
	 * 修改钱包支付密码
	 * lixinling
	 * 2016年7月21日 上午10:17:36
	 * @param pCode
	 * @return
	 * 
	 * {
	"exceptionClass": "com.hd123.rumba.exception.BusinessException",
	"message": "输入的原密码与账户密码不一致",
	"stackTrace": [],
	"cause": null,
	"error_code": -1
	}
	 */
	public static JSONObject updatePursePayPwd(String account_accesscode, String new_password, String old_password) {
		String urlstr = "http://124.193.154.242:58280/hdcard-services/api/password/change_pay_password?xid=1&orgcode=WEB&account_accesscode="
				+ account_accesscode + "&account_accesstype=2&new_password=" + new_password + "&old_password=" + old_password;
		String Authorization = "guest:guest";

		// JSONObject result = NetRequest.requestPost(urlstr, null,
		// Authorization);
		JSONObject result = HttpRequestUtils.httpPost(urlstr, null, Authorization);

		if (result != null && result.optInt("responseCode") == 403)
			result.put("message", "输入的原密码与账户密码不一致");
		return result;
	}

	/**
	 * 根据userId从Redis中取得用户的loginCode
	 * lixinling
	 * 2016年7月21日 上午10:17:36
	 * @param pCode
	 * @return
	 * 
	 */
	/*
	 * public static String getLoginCode(String userId) { JSONObject userInfo =
	 * RedisUtil.getJSONObj(Application.USERINFO_PREFIX + userId); return
	 * userInfo.optString("login"); }
	 */

	/**
	 * 根据userId从Redis中取得用户的phone
	 * lixinling
	 * 2016年7月21日 上午10:17:36
	 * @param pCode
	 * @return
	 * 
	 */
	public static String getCustPhone(String userId) {
		JSONObject userInfo = RedisUtil.getJSONObj(Application.USERINFO_PREFIX + userId);
		return userInfo.optString("phone");
	}

	/**
	 * 根据userId从Redis中取得用户的userInfo对象
	 * lixinling
	 * 2016年8月25日 上午14:17:36
	 * @param pCode
	 * @return
	 * 
	 */
	public static JSONObject getUserInfo(String userId) {
		JSONObject userInfo = RedisUtil.getJSONObj(Application.USERINFO_PREFIX + userId);
		return userInfo;
	}

	/**
	 * ERP减库存操作
	 * lixinling
	 * 2016年8月24日 上午10:17:36
	 * @param pCode
	 * @return
	 * 
	 *{
	"echoCode": "0"
	}
	 */
	public static JSONObject decERPQuantity(String mallCode, Long groupId, JSONArray products) {
		String urlstr = "http://124.193.154.242:37080/h4rest-server/rest/h5rest-server/core/wholesaleservice/wholesale";
		String Authorization = "guest:guest";

		// 构造请求参数
		JSONObject data = new JSONObject();
		data.put("uuid", UUID.getUID());
		data.put("senderCode", mallCode);
		data.put("senderWrh", "-");
		data.put("receiverCode", "0003");
		data.put("remark", groupId + "");
		data.put("ocrDate", DateComFunc.formatDate(new Date(), "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZ"));// 2016-07-27T11:13:42.034+0800
		data.put("filler", "linjia");
		data.put("products", products);

		// String dataUni = StringEscapeUtils.escapeJava(data.toString());
		// JSONObject body = new JSONObject(dataUni);

		JSONObject result = NetRequest.requestPost(urlstr, data, Authorization);
		return result;
	}

	/**
	 * CRM钱包支付
	 * lixinling
	 * 2016年8月24日 上午10:17:36
	 * @param pCode
	 * @return
	 * 成功只返回状态码：200 
	 * 失败（余额不足）返回：403
	 * {
	"exceptionClass": "com.hd123.card.common.ejb.exception.BalanceNotEnoughException",
	"message": "账户余额不足，付款失败。",
	"stackTrace": [],
	"cause": null,
	"error_code": 20004
	}
	 */
	public static JSONObject desaccountPay(Long xid, String account_accesscode, String passsword, String occur) {
		String urlstr = "http://124.193.154.242:58280/hdcard-services/api/desaccount/pay?xid=" + xid + "&orgcode=WEB&account_accesscode="
				+ account_accesscode + "&account_accesstype=2&passsword=" + passsword + "&occur=" + occur;
		String Authorization = "guest:guest";

		// JSONObject result = NetRequest.requestPost(urlstr, null,
		// Authorization);
		JSONObject result = HttpRequestUtils.httpPost(urlstr, null, Authorization);
		return result;
	}

	/**
	 * CRM钱包支付密码重置 
	 * lixinling
	 * 2016年11月30日 上午10:17:36
	 * @param pCode
	 * @return
	 * 成功只返回状态码：200 
	 * 失败（余额不足）返回：403
	 *
	 */
	public static JSONObject resetPursePayPwd(Long xid, String account_accesscode, String new_password, String mobile_securitycode) {
		String urlstr = "http://124.193.154.242:58280/hdcard-services/api/password/forget_password_formally?xid=" + xid
				+ "&orgcode=WEB&account_accesscode=" + account_accesscode + "&account_accesstype=2&new_password=" + new_password
				+ "&mobile_securitycode=" + mobile_securitycode;
		String Authorization = "guest:guest";
		JSONObject result = HttpRequestUtils.httpPost(urlstr, null, Authorization);
		if (result != null && result.optInt("responseCode") == 403)
			result.put("message", "重置失败");
		return result;
	}

	/*****************************微信授权**********************************/
	/**
	 * （微信）检验授权凭证（access_token）是否有效
	 * lixinling
	 * 2016年8月25日 上午13:17:36
	 * @param pCode
	 * @return
	 * 正确的JSON返回结果：
	  { "errcode":0,"errmsg":"ok"} 
	错误时的JSON返回示例：
	{ "errcode":40003,"errmsg":"invalid openid"}
	 */
	public static JSONObject checkWxAccessTokenValidation(String access_token, String openid) {
		String urlstr = "https://api.weixin.qq.com/sns/auth?access_token=" + access_token + "&openid=" + openid;
		JSONObject result = NetRequest.requestGet(urlstr, null, null);
		return result;
	}

	/**
	 * （微信）通过授权code换取网页授权access_token
	 * lixinling
	 * 2016年8月25日 上午13:17:36
	 * @param pCode
	 * @return
	 * 正确的JSON返回结果：
	  { "access_token":"ACCESS_TOKEN",    
	"expires_in":7200,    
	"refresh_token":"REFRESH_TOKEN",    
	"openid":"OPENID",    
	"scope":"SCOPE" }
	   错误时微信会返回JSON数据:
	 {"errcode":40029,"errmsg":"invalid code"} 
	 */
	public static JSONObject getAccessTokenBycode(String code) {
		String urlstr = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Configure.getAppid() + "&secret="
				+ Configure.getAppSecret() + "&code=" + code + "&grant_type=authorization_code";
		JSONObject result = NetRequest.requestGet(urlstr, null, null);
		return result;
	}

	/**
	 * （微信）使用refresh_token刷新access_token（如果需要）
	 * lixinling
	 * 2016年8月25日 上午13:17:36
	 * @param pCode
	 * @return
	 * 正确的JSON返回结果：
	  { "access_token":"ACCESS_TOKEN",    
	"expires_in":7200,    
	"refresh_token":"REFRESH_TOKEN",    
	"openid":"OPENID",    
	"scope":"SCOPE" }
	   错误时微信会返回JSON数据:
	 {"errcode":40029,"errmsg":"invalid code"} 
	 */
	public static JSONObject refreshAccessToken(String refresh_token) {
		String urlstr = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + Configure.getAppid()
				+ "&grant_type=refresh_token&refresh_token= " + refresh_token;
		JSONObject result = NetRequest.requestGet(urlstr, null, null);
		return result;
	}

	/**
	 * （微信）拉取用户信息(需scope为 snsapi_userinfo)
	 * lixinling
	 * 2016年8月25日 上午13:17:36
	 * @param pCode
	 * @return
	 * 正确的JSON返回结果：
	  {    "openid":" OPENID",  
	" nickname": NICKNAME,   
	"sex":"1",   
	"province":"PROVINCE"   
	"city":"CITY",   
	"country":"COUNTRY",    
	"headimgurl":    "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ
	4eMsv84eavHiaiceqxibJxCfHe/46",  
	"privilege":[ "PRIVILEGE1" "PRIVILEGE2"     ],    
	"unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL" 
	} 
	   错误时微信会返回JSON数据:
	 {"errcode":40003,"errmsg":" invalid openid "}
	 */
	public static JSONObject getWxUserInfo(String access_token, String openid) {
		String urlstr = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN";
		JSONObject result = NetRequest.requestGet(urlstr, null, null);
		return result;
	}

	/**
	 * （微信分享）获取access_token
	 * lixinling
	 * 2016年9月30日 上午13:17:36
	 * @param pCode
	 * @return
	 * 正确的JSON返回结果：
	{
	"access_token": "KVltWpKc3zlXyOb1DMFdChg34s2hXvfwHUZDsiOOmSlmArpH2yMt1YdcnEJ74dWfnpNQz4VhDJcWKUXtGfn5rg3ifAex1wcttyxCnXGjVGMRJXfAAAZRF"
	"expires_in": 7200
	}
	 */
	public static JSONObject getAccessToken() {
		String urlstr = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + Configure.getAppid() + "&secret="
				+ Configure.getAppSecret();
		JSONObject result = NetRequest.requestGet(urlstr, null, null);
		return result;
	}

	/**
	 * （微信分享）获取jsapi_ticket
	 * lixinling
	 * 2016年9月30日 上午13:17:36
	 * @param pCode
	 * @return
	 * 正确的JSON返回结果：
	{
	"errcode": 0
	"errmsg": "ok"
	"ticket": "kgt8ON7yVITDhtdwci0qeZ2QPi1Jdtnv8l6M4R02OU9P2lTJtAeh2z2h0abYVOMm9OUJ8_kTskDaFkLxyAI3mA"
	"expires_in": 7200
	}
	 */
	public static JSONObject getJsapiTicket(String access_token) {
		String urlstr = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + access_token + "&type=jsapi";
		JSONObject result = NetRequest.requestGet(urlstr, null, null);
		return result;
	}

	/*****************************微信授权**********************************/

	/*****************************CRM充值**********************************/
	/**
	 * lixinling 
	 * 2016年9月17日 下午2:30:40
	 * @param accountAccessCode
	 * @param tranId
	 * @param occur
	 * @param realPay
	 * @param tranTime
	 * @return
	 */
	public static JSONObject accountcashdeposit(String accountAccessCode, String tranId, String occur, String realPay, String tranTime,
			Long xid) {
		// 充值准备
		String urlstr = "http://124.193.154.242:58280/hdcard-services/api/accountcashdeposit/prepare";
		JSONObject body = new JSONObject();
		body.put("accountAccessCode", accountAccessCode);
		body.put("accountAccessType", 2);
		body.put("orgCode", "WEB");
		body.put("tranId", tranId);
		body.put("occur", occur);
		body.put("realPay", realPay);
		body.put("tranTime", tranTime);
		body.put("xid", xid);
		String Authorization = "guest:guest";
		// JSONObject result = NetRequest.requestPost(urlstr, body,
		// Authorization);
		JSONObject result = HttpRequestUtils.httpPost(urlstr, body, Authorization);
		if (result != null && result.optInt("responseCode") == 204) {
			logger.info("===============================充值准备完成");
			// 充值确认
			String urlConfirm = "http://124.193.154.242:58280/hdcard-services/api//accountcashdeposit/confirm";
			body = new JSONObject();
			body.put("orgCode", "WEB");
			body.put("prepareTranId", tranId);
			body.put("tranTime", tranTime);
			JSONArray payLines = new JSONArray();
			JSONObject payLine = new JSONObject();
			payLine.put("payCode", "cash");
			payLine.put("payName", "cash");
			payLine.put("pay", realPay);
			payLines.put(payLine);
			body.put("payLines", payLines);
			// result = NetRequest.requestPost(urlConfirm, body, Authorization);
			result = HttpRequestUtils.httpPost(urlConfirm, body, Authorization);
		}
		if (result != null && result.optInt("responseCode") == 204) {
			logger.info("=============================充值确认完成");
		} else {
			logger.info("=============================充值确认失败" + result);
		}
		return result;
	}

	/*****************************CRM充值**********************************/

	/********************************************************************/
	/**                        鼎力云抛单调用部分                                                                 */
	/********************************************************************/

	/**
	 * 抛单到鼎力云
	 * lixinling 
	 * 2016年10月15日 下午5:22:09
	 * @param tenant_id 租户ID
	 * @param operator 操作人
	 * @return
	 */
	public static JSONObject submitOrderToUhd(String operator, Order order) {
		Map<String, String> infoMap = com.linjia.web.uhd123.common.Configure.getRequestBaseInfo();
		String tenant_id = infoMap.get("tenant_id");
		String baseUrl = infoMap.get("baseUrl");
		String authorization = infoMap.get("authorization");
		String urlstr = baseUrl + tenant_id + "/soms/orderservice/order?operator=" + operator;

		JSONObject body = JSONUtil.ObjToJSON(order);
		// System.out.println(body.toString());
		// JSONObject result = NetRequest.requestPost(urlstr, body,
		// authorization);
		JSONObject result = HttpRequestUtils.httpPost(urlstr, body, authorization);
		return result;
	}

	/**
	 * 更新鼎力云订单配送状态
	 * lixinling 
	 * 2016年10月15日 下午5:22:09
	 * @param tenant_id 租户ID
	 * @param operator 操作人
	 * @return
	 */
	public static JSONObject updateOrderDeliverToUhd(String operator, String uhdOrderId, DeliverRequest request) {
		Map<String, String> infoMap = com.linjia.web.uhd123.common.Configure.getRequestBaseInfo();
		String tenant_id = infoMap.get("tenant_id");
		String baseUrl = infoMap.get("baseUrl");
		String authorization = infoMap.get("authorization");
		String urlstr = baseUrl + tenant_id + "/soms/orderservice/order/" + uhdOrderId + "/action/deliver?operator=" + operator;

		JSONObject body = JSONUtil.ObjToJSON(request);
		// System.out.println(body.toString());
		// JSONObject result = NetRequest.requestPost(urlstr, body,
		// authorization);
		JSONObject result = HttpRequestUtils.httpPost(urlstr, body, authorization);
		return result;
	}

	/**
	 * 提交鼎力云退换货单
	 * lixinling 
	 * 2016年10月15日 下午5:22:09
	 * @param tenant_id 租户ID
	 * @param operator 操作人
	 * @return
	 */
	public static JSONObject returnserviceToUhd(String operator, Rtn rtn) {
		Map<String, String> infoMap = com.linjia.web.uhd123.common.Configure.getRequestBaseInfo();
		String tenant_id = infoMap.get("tenant_id");
		String baseUrl = infoMap.get("baseUrl");
		String authorization = infoMap.get("authorization");
		String urlstr = baseUrl + tenant_id + "/soms/returnservice/rtn?operator=" + operator;

		JSONObject body = JSONUtil.ObjToJSON(rtn);
		// JSONObject result = NetRequest.requestPost(urlstr, body,
		// authorization);
//		logger.debug("body============================="+body.toString());
		JSONObject result = HttpRequestUtils.httpPost(urlstr, body, authorization);
		return result;
	}

	/**
	 * 确认鼎力云退货状态
	 * lixinling 
	 * 2016年10月15日 下午5:22:09
	 * @param tenant_id 租户ID
	 * @param operator 操作人
	 * @param id 退换货id
	 * @return
	 */
	public static JSONObject confirmReturnserviceToUhd(String operator, String id, ReturnRequest request) {
		Map<String, String> infoMap = com.linjia.web.uhd123.common.Configure.getRequestBaseInfo();
		String tenant_id = infoMap.get("tenant_id");
		String baseUrl = infoMap.get("baseUrl");
		String authorization = infoMap.get("authorization");
		String urlstr = baseUrl + tenant_id + "/soms/returnservice/rtn/" + id + "/action/deliver?operator=" + operator;

		JSONObject body = JSONUtil.ObjToJSON(request);
		// JSONObject result = NetRequest.requestPost(urlstr, body,
		// authorization);
		JSONObject result = HttpRequestUtils.httpPost(urlstr, body, authorization);
		return result;
	}

	/**
	 * 鼎力云订阅通知
	 * lixinling 
	 * 2016年10月15日 下午5:22:09
	 * @param tenant_id 租户ID
	 * @param operator 操作人
	 * @param id 退换货id
	 * @return
	 */
	public static JSONObject subscribe(String operator, String callback_url, Topics topics) {
		Map<String, String> infoMap = com.linjia.web.uhd123.common.Configure.getRequestBaseInfo();
		String tenant_id = infoMap.get("tenant_id");
		String baseUrl = infoMap.get("baseUrl");
		String authorization = infoMap.get("authorization");
		String urlstr = baseUrl + tenant_id + "/notify/notifyservice/subscribe?app_id=soms&callback_url=" + callback_url + "&operator="
				+ operator;

		JSONObject body = JSONUtil.ObjToJSON(topics);
		// JSONObject result = NetRequest.requestPost(urlstr, body,
		// authorization);
		JSONObject result = HttpRequestUtils.httpPost(urlstr, body, authorization);
		return result;
	}

	/**
	 * 鼎力云订单占货
	 * lixinling 
	 * 2016年10月15日 下午5:22:09
	 * @param tenant_id 租户ID
	 * @param operator 操作人
	 * @param id 退换货id
	 * @return
	 */
	public static JSONObject orderstocklockToUhd(String operator, OrderStockLock orderStockLock) {
		Map<String, String> infoMap = com.linjia.web.uhd123.common.Configure.getRequestBaseInfo();
		String tenant_id = infoMap.get("tenant_id");
		String baseUrl = infoMap.get("baseUrl");
		String authorization = infoMap.get("authorization");
		String urlstr = baseUrl + tenant_id + "/invc/orderstockservice/lock?operator=" + operator;

		JSONObject body = JSONUtil.ObjToJSON(orderStockLock);
		JSONObject result = HttpRequestUtils.httpPost(urlstr, body, authorization);
		return result;
	}

	/**
	 * 鼎力云订单释放库存
	 * lixinling 
	 * 2016年10月15日 下午5:22:09
	 * @param tenant_id 租户ID
	 * @param operator 操作人
	 * @param id 退换货id
	 * @return
	 */
	public static JSONObject orderstockunlockToUhd(String operator, String order_id) {
		Map<String, String> infoMap = com.linjia.web.uhd123.common.Configure.getRequestBaseInfo();
		String tenant_id = infoMap.get("tenant_id");
		String baseUrl = infoMap.get("baseUrl");
		String authorization = infoMap.get("authorization");
		String urlstr = baseUrl + tenant_id + "/invc/orderstockservice/unlock?order_id=" + order_id + "&operator=" + operator;

		JSONObject result = HttpRequestUtils.httpPost(urlstr, null, authorization);
		return result;
	}

	/**  
	* 提供精确的乘法运算。  
	* @param v1 被乘数  
	* @param v2 乘数  
	* @return 两个参数的积  
	*/
	public static BigDecimal mul(BigDecimal b1, int quantity) {
		BigDecimal b2 = new BigDecimal(quantity);
		BigDecimal result = new BigDecimal(b1.multiply(b2).doubleValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
		return result;
	}
	
	/**  
	 * 提供精确的乘法运算。  
	 * @param v1 被乘数  
	 * @param v2 乘数  
	 * @return 两个参数的积  
	 */
	public static BigDecimal mul(BigDecimal b1, BigDecimal b2) {
		return b1.multiply(b2).setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/**  
	* 提供精确的加法运算。  
	* @param v1 被加数  
	* @param v2 加数  
	* @return 两个参数的和  
	*/
	public static BigDecimal add(BigDecimal b1, Integer v2) {
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	
	//高德转百度（火星坐标gcj02ll–>百度坐标bd09ll）
	public static double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
	    double[] bd_lat_lon = new double[2];
	    double PI = 3.14159265358979324 * 3000.0 / 180.0;
	    double x = gd_lon, y = gd_lat;
	    double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
	    double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
	    bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
	    bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
	    return bd_lat_lon;
	}
	
	//百度转高德（百度坐标bd09ll–>火星坐标gcj02ll）
	public static double[] bdToGaoDe(double bd_lat, double bd_lon) {
	    double[] gd_lat_lon = new double[2];
	    double PI = 3.14159265358979324 * 3000.0 / 180.0;
	    double x = bd_lon - 0.0065, y = bd_lat - 0.006;
	    double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
	    double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
	    gd_lat_lon[0] = z * Math.cos(theta);
	    gd_lat_lon[1] = z * Math.sin(theta);
	    return gd_lat_lon;
	 }
	
	//当天第几单
	public static Integer getTodayOrderNum(String orderSource,String mallCode){
		//orderSource: M:美团;L:邻家;B:百度 ;J:京东;E:饿了么;
		Integer value=null;
		try{
			String key1=DateComFunc.formatDate(new Date(),"yyyMMdd");
			String key=orderSource+mallCode+key1;
			String val=RedisUtil.get(key);
			if(val==null||val.isEmpty()){
				RedisUtil.put(key,"1",86400);
				value=1;
			}else{
				Integer vals=Integer.valueOf(RedisUtil.get(key))+1;
				RedisUtil.put(key, vals+"");
				value=vals;
			}
		}catch(Exception e){
			value=0;
		}
		return value;
	}

//	//生成密码
//	public static void main(String[] args) {
//		for (int i = 1; i < 77; i++) {
//			String s="linjia";
//			if(i<10){
//				s=s+"0";
//			}
//			s=s+i;
//			System.out.println(s+":######:"+getMD5(s));
//		}
//	}
	
	//生成密码
		public static void main(String[] args) {
//			double[] b=bdToGaoDe(39.9881065,116.321162);
//			System.out.println(b[1]);
//			System.out.println(b[0]);
			//百度  北京市海淀区中关村E世界5层  116.321162,39.9881065
//			高德    39.982184880418785,116.31461568953974
			System.out.println(Util.getMD5("linjia5168"));
		}
		
		private final static String[] hex = {  
	        "00","01","02","03","04","05","06","07","08","09","0A","0B","0C","0D","0E","0F",  
	        "10","11","12","13","14","15","16","17","18","19","1A","1B","1C","1D","1E","1F",  
	        "20","21","22","23","24","25","26","27","28","29","2A","2B","2C","2D","2E","2F",  
	        "30","31","32","33","34","35","36","37","38","39","3A","3B","3C","3D","3E","3F",  
	        "40","41","42","43","44","45","46","47","48","49","4A","4B","4C","4D","4E","4F",  
	        "50","51","52","53","54","55","56","57","58","59","5A","5B","5C","5D","5E","5F",  
	        "60","61","62","63","64","65","66","67","68","69","6A","6B","6C","6D","6E","6F",  
	        "70","71","72","73","74","75","76","77","78","79","7A","7B","7C","7D","7E","7F",  
	        "80","81","82","83","84","85","86","87","88","89","8A","8B","8C","8D","8E","8F",  
	        "90","91","92","93","94","95","96","97","98","99","9A","9B","9C","9D","9E","9F",  
	        "A0","A1","A2","A3","A4","A5","A6","A7","A8","A9","AA","AB","AC","AD","AE","AF",  
	        "B0","B1","B2","B3","B4","B5","B6","B7","B8","B9","BA","BB","BC","BD","BE","BF",  
	        "C0","C1","C2","C3","C4","C5","C6","C7","C8","C9","CA","CB","CC","CD","CE","CF",  
	        "D0","D1","D2","D3","D4","D5","D6","D7","D8","D9","DA","DB","DC","DD","DE","DF",  
	        "E0","E1","E2","E3","E4","E5","E6","E7","E8","E9","EA","EB","EC","ED","EE","EF",  
	        "F0","F1","F2","F3","F4","F5","F6","F7","F8","F9","FA","FB","FC","FD","FE","FF"  
	    };  
	    private final static byte[] val = {  
	        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
	        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
	        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
	        0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
	        0x3F,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
	        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
	        0x3F,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
	        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
	        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
	        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
	        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
	        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
	        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
	        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
	        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
	        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F  
	    };  
	    public static String escape(String s) {  
	        StringBuffer sbuf = new StringBuffer();  
	        int len = s.length();  
	        for (int i = 0; i < len; i++) {  
	            int ch = s.charAt(i);  
	            if (ch == ' ') {                        // space : map to '+'   
	                sbuf.append('+');  
	            } else if ('A' <= ch && ch <= 'Z') {    // 'A'..'Z' : as it was  
	                sbuf.append((char)ch);  
	            } else if ('a' <= ch && ch <= 'z') {    // 'a'..'z' : as it was  
	                sbuf.append((char)ch);  
	            } else if ('0' <= ch && ch <= '9') {    // '0'..'9' : as it was  
	                sbuf.append((char)ch);  
	            } else if (ch == '-' || ch == '_'       // unreserved : as it was  
	                || ch == '.' || ch == '!'  
	                || ch == '~' || ch == '*'  
	                || ch == '/' || ch == '('  
	                || ch == ')') {  
	                sbuf.append((char)ch);  
	            } else if (ch <= 0x007F) {              // other ASCII : map to %XX  
	                sbuf.append('%');  
	                sbuf.append(hex[ch]);  
	            } else {                                // unicode : map to %uXXXX  
	                sbuf.append('%');  
	                sbuf.append('u');  
	                sbuf.append(hex[(ch >>> 8)]);  
	                sbuf.append(hex[(0x00FF & ch)]);  
	            }  
	        }  
	        return sbuf.toString();  
	    }  
	    public static String unescape(String s) {  
	        StringBuffer sbuf = new StringBuffer();  
	        int i = 0;  
	        int len = s.length();  
	        while (i < len) {  
	            int ch = s.charAt(i);  
	            if (ch == '+') {                        // + : map to ' '   
	                sbuf.append(' ');  
	            } else if ('A' <= ch && ch <= 'Z') {    // 'A'..'Z' : as it was  
	                sbuf.append((char)ch);  
	            } else if ('a' <= ch && ch <= 'z') {    // 'a'..'z' : as it was  
	                sbuf.append((char)ch);  
	            } else if ('0' <= ch && ch <= '9') {    // '0'..'9' : as it was  
	                sbuf.append((char)ch);  
	            } else if (ch == '-' || ch == '_'       // unreserved : as it was  
	                || ch == '.' || ch == '!'  
	                || ch == '~' || ch == '*'  
	                || ch == '/' || ch == '('  
	                || ch == ')') {  
	                sbuf.append((char)ch);  
	            } else if (ch == '%') {  
	                int cint = 0;  
	                if ('u' != s.charAt(i+1)) {         // %XX : map to ascii(XX)  
	                    cint = (cint << 4) | val[s.charAt(i+1)];  
	                    cint = (cint << 4) | val[s.charAt(i+2)];  
	                    i+=2;  
	                } else {                            // %uXXXX : map to unicode(XXXX)  
	                    cint = (cint << 4) | val[s.charAt(i+2)];  
	                    cint = (cint << 4) | val[s.charAt(i+3)];  
	                    cint = (cint << 4) | val[s.charAt(i+4)];  
	                    cint = (cint << 4) | val[s.charAt(i+5)];  
	                    i+=5;  
	                }  
	                sbuf.append((char)cint);  
	            }  
	            i++;  
	        }  
	        return sbuf.toString();  
	    }
	    
	    //emoji表情过滤
	    public static String filterEmoji(String source) { 
	         if(source != null){
	             Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
	             Matcher emojiMatcher = emoji.matcher(source);
	             if (emojiMatcher.find()){
	                 source = emojiMatcher.replaceAll("");
	                 return source;
	             }
	         return source;
	        }
	        return source; 
	     }
	    
		
		public static void writeJDDJSuccess(Map<String, Object> resMap, String data) {
			resMap.put("code", JDDJConfigure.code_SUCCESS);
			resMap.put("msg", JDDJConfigure.MSG_SUCCESS);
			resMap.put("data", data);
		}

		public static void writeJDDJFail(Map<String, Object> resMap) {
			resMap.put("code", JDDJConfigure.code_FAIL);
			resMap.put("msg", JDDJConfigure.MSG_FAIL);
		}
	    
	    //请求信息获取
	    public static void getReqInfo(HttpServletRequest request) { 
	        String head = request.getHeader("Content-Type");
	 		String method = request.getMethod();
	 		System.out.println("head****" + head);
	 		System.out.println("method****" + method);
	 			
	    	//头部信息
	    	Enumeration headerNames = request.getHeaderNames();
	    	String headers="";
	        while (headerNames.hasMoreElements()) {
	            String key = (String) headerNames.nextElement();
	            String value = request.getHeader(key);
	            headers=headers+key+":"+value+"\n";
	        }
	        System.out.println("**************************");  
	        System.out.println("headers:"+headers);  
	        System.out.println("**************************");
	        System.out.println("queryString:"+request.getQueryString());
	        
	        Enumeration paramNames = request.getParameterNames();  
	        String params="";
	        while (paramNames.hasMoreElements()) {  
	            String paramName = (String) paramNames.nextElement();  
	  
	            String[] paramValues = request.getParameterValues(paramName);  
	            if (paramValues.length == 1) {  
	                String paramValue = paramValues[0];  
	                if (paramValue.length() != 0) {  
	                	params=params+paramName+":"+paramValues+"\n";
	                }  
	            }  
	        }  
	        System.out.println("------------------------------");  
	        System.out.println("params:" + params);  
	        System.out.println("------------------------------"); 
	    }
	    
		//饿了么token存取
		public static Token getElemeToken(){
			Token token=null;
			try{
				String eleme_access_token=RedisUtil.get("eleme_access_token");
				if(Tools.isEmpty(eleme_access_token)){
					OAuthClient client=new OAuthClient();
					token=client.getTokenInClientCredentials();
					String eleme_access_token_new=token.getAccessToken();
					String eleme_token_type=token.getTokenType();
					String eleme_refresh_token=token.getRefreshToken();
					if(eleme_refresh_token==null){
						eleme_refresh_token="";
					}
					Long eleme_expires_in=token.getExpires();
					int putTime=0;
					if(eleme_expires_in==null||eleme_expires_in<=1800){
						putTime=0;
					}else{
						putTime=eleme_expires_in.intValue()-1800;
					}
					RedisUtil.put("eleme_access_token",eleme_access_token_new,putTime);
					RedisUtil.put("eleme_token_type",eleme_token_type,putTime);
					RedisUtil.put("eleme_expires_in",eleme_expires_in+"",putTime);
					RedisUtil.put("eleme_refresh_token",eleme_refresh_token,putTime);
				}else{
					String eleme_access_token_old=RedisUtil.get("eleme_access_token");
					String eleme_token_type_old=RedisUtil.get("eleme_token_type");
					String eleme_expires_in_old=RedisUtil.get("eleme_expires_in");
//					String eleme_refresh_token_old=RedisUtil.get("eleme_refresh_token");
					Long expires_in=Long.valueOf(eleme_expires_in_old);
					token=new Token();
					token.setAccessToken(eleme_access_token_old);
					token.setExpires(expires_in);
					token.setTokenType(eleme_token_type_old);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			return token;
		}
}
