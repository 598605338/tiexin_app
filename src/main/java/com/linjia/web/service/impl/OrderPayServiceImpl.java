package com.linjia.web.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.constants.Application;
import com.linjia.constants.Constants;
import com.linjia.tools.DateComFunc;
import com.linjia.tools.RandUtils;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.model.DesaccountPayRecord;
import com.linjia.web.model.OrderGroup;
import com.linjia.web.model.PinTuanOrder;
import com.linjia.web.model.ScoreOrder;
import com.linjia.web.service.AccountcashChangeService;
import com.linjia.web.service.DesaccountPayRecordService;
import com.linjia.web.service.OrderGroupProductService;
import com.linjia.web.service.OrderGroupService;
import com.linjia.web.service.OrderPayService;
import com.linjia.web.service.PinTuanOrderService;
import com.linjia.web.service.ScoreOrderService;
import com.linjia.wxpay.common.Configure;
import com.linjia.wxpay.common.RandomStringGenerator;
import com.linjia.wxpay.common.Signature;
import com.linjia.wxpay.protocol.PayReqData;
import com.linjia.wxpay.protocol.PayResData;
import com.linjia.wxpay.service.PayService;

@Service
@Transactional
public class OrderPayServiceImpl extends BaseServiceImpl<Object, Long> implements OrderPayService {

	@Autowired
	private DesaccountPayRecordService desaccountPayRecordService;

	@Autowired
	private PinTuanOrderService pinTuanOrderService;

	@Autowired
	private OrderGroupService orderGroupService;

	@Autowired
	private ScoreOrderService scoreOrderService;

	@Autowired
	private AccountcashChangeService accountcashChangeService;
	
	@Autowired
	private OrderGroupProductService orderGroupProductService;
	
	@Autowired
	private ActivityKaiTuanMainServiceImpl activityKaiTuanMainServiceImpl;

	@Override
	public BaseDao<Object, Long> getDao() {
		return null;
	}

	/**
	 * 调起支付
	 * lixinling 
	 * 2016年8月10日 下午4:17:42
	 * @param payType
	 * @param body
	 * @param totalFee
	 * @param outTradeNo
	 * @param spbill_create_ip
	 * @return
	 */
	public Map<String, Object> payHandle(int payType, String body, String attach, BigDecimal realPrice, long outTradeNo,
			String spbill_create_ip, String userId, String openid, String notify_url, String password) {
		Map<String, Object> resMap = new HashMap<String, Object>();

		if (payType == Constants.PAY_TYPE.WX) {
			// 微信支付
			// 构造支付请求数据对象
			// 订单总金额，单位为“分”，只能整数
			int totalFee = realPrice.multiply(new BigDecimal("100")).intValue();
			PayReqData payReqData = createPayReqData(body, attach, totalFee, outTradeNo, spbill_create_ip, userId, openid, notify_url);
			if (payReqData == null) {
				logger.error("构造支付请求数据对象错误，请重新进行微信授权.");
				resMap.put("error_code", "10001");// 重新进行微信授权
				Util.writeError(resMap);
				return resMap;
			}
			try {
				String resultDataXML = new PayService().request(payReqData);
				if (StringUtils.isEmpty(resultDataXML)) {
					System.out.println("resultDataXML:" + resultDataXML);
					logger.error("【支付失败】支付请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问");
					resMap.put("error_code", "10002");// 支付请求错误
					Util.writeError(resMap);
					return resMap;
				} else {
					PayResData payResData = (PayResData) com.linjia.wxpay.common.Util.getObjectFromXML(resultDataXML, PayResData.class);
					if (payResData.getReturn_code() == null || payResData.getReturn_code().equals("FAIL")) {
						logger.error("【支付失败】支付API系统返回失败，请检测Post给API的数据是否规范合法。返回的错误信息为：" + payResData.getReturn_msg());
						resMap.put("error_code", "10002");// 支付请求错误
						Util.writeError(resMap);
						return resMap;
					} else if (payResData.getResult_code().equals("SUCCESS")) {
						// --------------------------------------------------------------------
						// 收到API的返回数据的时候得先验证一下数据有没有被第三方篡改，确保安全
						// --------------------------------------------------------------------
						if (!Signature.checkIsSignValidFromResponseString(resultDataXML)) {
							logger.error("【支付失败】支付请求API返回的数据签名验证失败，有可能数据被篡改了");
							resMap.put("error_code", "10003");// 支付请求API返回的数据签名验证失败，有可能数据被篡改了
							Util.writeError(resMap);
							return resMap;
						}

						if ("SUCCESS".equals(payResData.getResult_code())) {
							String prepay_id = payResData.getPrepay_id();

							// 组装H5调起微信支付所需的参数
							/*
							 * "appId" ： "wx2421b1c4370ec43b", //公众号名称，由商户传入
							 * "timeStamp"：" 1395712654", //时间戳，自1970年以来的秒数
							 * "nonceStr" ： "e61463f8efa94090b1f366cccfbbb444",
							 * //随机串 "package" ：
							 * "prepay_id=u802345jgfjsdfgsdg888", "signType" ：
							 * "MD5", //微信签名方式： "paySign" ：
							 * "70EA570631E4BB79628FBCA90534C63FF7FADD89" //微信签名
							 */
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("appId", Configure.getAppid());
							params.put("timeStamp", (System.currentTimeMillis() / 1000) + "");
							params.put("nonceStr", RandomStringGenerator.getRandomStringByLength(32));
							params.put("package", "prepay_id=" + prepay_id);
							params.put("signType", "MD5");

							// 根据API给的签名规则进行签名
							String sign = Signature.getSign(params);
							params.put("paySign", sign);

							resMap.put("params", params);
						}
					}
				}
			} catch (Exception e) {
				logger.error("调用微信支付统一下单时发生错误：", e);
				resMap.put("error_code", "10004");// 系统错误
				Util.writeError(resMap);
				return resMap;
			}
		} else if (payType == Constants.PAY_TYPE.PURSE) {

			// 钱包支付
			// 生成钱包消费记录
			DesaccountPayRecord desaccountPayRecord = new DesaccountPayRecord();
			desaccountPayRecord.setAccountaccesscode(userId);
			desaccountPayRecord.setAccountaccesstype(2);// 会员号充值:2
			desaccountPayRecord.setGroupId(outTradeNo);
			desaccountPayRecord.setOccur(realPrice);
			desaccountPayRecord.setOrgcode("WEB");
			desaccountPayRecord.setTranstatus(Constants.TRAN_STATUS.UNSUCCESS);
			desaccountPayRecord.setCreDate(new Date());
			desaccountPayRecord.setXid(Long.valueOf(System.currentTimeMillis() + RandUtils.getNRandomNum(5)));
			int row = desaccountPayRecordService.insert(desaccountPayRecord);
			if (row == 1) {

				// 进行钱包支付
				try {
					JSONObject result = Util.desaccountPay(desaccountPayRecord.getXid(), desaccountPayRecord.getAccountaccesscode(),
							password, desaccountPayRecord.getOccur().toString());
					if (result != null && result.optInt("responseCode") == 200) {
						logger.info("钱包支付完成");
						try {
							desaccountPayRecord.setTranstatus(Constants.TRAN_STATUS.SUCCESSED);
							// 更新钱包消费记录状态
							desaccountPayRecordService.update(desaccountPayRecord);

							// 插入用户钱包交易变化记录
							accountcashChangeService.insert(Constants.ACCOUNTCASH_ACTION.SPEND_ACCOUNT,
									desaccountPayRecord.getAccountaccesscode(), desaccountPayRecord.getOccur());

							// 支付完成后进行更改订单状态
							String out_trade_no = String.valueOf(outTradeNo);
							if (out_trade_no.charAt(6) == '2') {
								logger.info("钱包支付已完成，进行处理正常订单...");

								// 处理正常订单
								// 查询订单详情
								OrderGroup orderGroup = orderGroupService.selectById(Long.valueOf(out_trade_no));
								if (orderGroup.getPayStatus() == Constants.PAY_STATUS.UNPAY) {
									this.updateOrderGroupByPaySuccessed(orderGroup, String.valueOf(desaccountPayRecord.getXid()),
											out_trade_no, Constants.PAY_TYPE.PURSE, Constants.PAY_STATUS.PAYD,
											Constants.ORDER_GROUP_STATUS.PAYD, orderGroup.getMallCode());
									logger.info("钱包支付已完成，进行处理正常订单,更新group订单状态已完成.");
								}
							} else if (out_trade_no.charAt(6) == '4') {
								logger.info("钱包支付已完成，进行处理积分兑换订单...");

								// 查询积分兑换订单详情
								ScoreOrder scoreOrder = scoreOrderService.selectById(Long.valueOf(out_trade_no));
								if (scoreOrder.getOrderStatus() == Constants.SCORE_STATUS.UNPAY) {

									// 处理积分商城积分兑换订单
									// 传入参数
									scoreOrderService.updatePayScoreOrderStatus(userId, scoreOrder.getCardCouponId(), scoreOrder.getId(),
											Constants.PAY_TYPE.PURSE);
									logger.info("钱包支付已完成，进行处理积分兑换订单,积分兑换订单所有处理操作已完成.");
								} else {
									logger.error("积分订单已支付，不再进行重复支付");
								}
							} else if (out_trade_no.charAt(6) == '5') {
								logger.info("钱包支付已完成，进行处理拼团订单...");

								// 查询拼团订单详情
								PinTuanOrder pinTuanOrder = pinTuanOrderService.selectPtOrderById(Long.valueOf(out_trade_no));
								if (pinTuanOrder.getPay_status() == Constants.PAY_STATUS.UNPAY) {
									boolean flag = activityKaiTuanMainServiceImpl.ptAfterPayed(Long.valueOf(out_trade_no), Constants.PAY_TYPE.PURSE);
									if(flag){
										logger.info("微信支付已完成，调起notifyUrl进行处理拼团订单,更新拼团订单状态操作已完成.");
									}else{
										logger.info("【ERROR:::::::::::::::::::::::::::::::::】微信支付已完成，调起notifyUrl进行处理拼团订单,更新拼团订单状态操作失败.");
									}

									/*this.updatePintuanOrderByPaySuccessed(pinTuanOrder, Constants.PAY_TYPE.PURSE, Constants.PAY_STATUS.PAYD);
									logger.info("钱包支付已完成，进行处理拼团订单,更新拼团订单状态操作已完成.");*/
								}
							}

						} catch (Exception e) {
							logger.error("钱包支付完成,更新钱包支付记录时发生错误：", e);
							resMap.put("error_code", "10004");// 系统错误
							Util.writeError(resMap);
							return resMap;
						}
					} else if (result != null && result.optInt("responseCode") == 403) {
						if (Tools.isEmpty(password)) {
							resMap.put("error_code", "20706");// 会员账户识别码支付密码错误。
						} else {
							resMap.put("error_code", "20004");// 账户余额不足，付款失败。
						}
						Util.writeError(resMap);
						return resMap;
					}
				} catch (Exception e) {
					logger.error("调用钱包支付时发生错误：", e);
					resMap.put("error_code", "10004");// 系统错误
					Util.writeError(resMap);
					return resMap;
				}
			}
		}

		Util.writeSuccess(resMap);
		return resMap;
	}

	/**
	 * 构造支付请求数据对象
	 * lixinling 
	 * 2016年7月27日 下午2:46:06
	 * @param orderGroup
	 * @param wxOauth2Code微信授权code
	 * @return
	 */
	public PayReqData createPayReqData(String body, String attach, int totalFee, long outTradeNo, String spbill_create_ip, String userId,
			String openid, String notify_url) {

		/**
		 * @param authCode 这个是扫码终端设备从用户手机上扫取到的支付授权号，这个号是跟用户用来支付的银行卡绑定的，有效期是1分钟
		 * @param body 要支付的商品的描述信息，用户会在支付成功页面里看到这个信息
		 * @param attach 支付订单里面可以填的附加数据，API会将提交的这个附加数据原样返回
		 * @param outTradeNo 商户系统内部的订单号,32个字符内可包含字母, 确保在商户系统唯一
		 * @param totalFee 订单总金额，单位为“分”，只能整数
		 * @param deviceInfo 商户自己定义的扫码支付终端设备号，方便追溯这笔交易发生在哪台终端设备上(注意：PC网页或公众号内支付请传"WEB")
		 * @param spBillCreateIP 订单生成的机器IP
		 * @param timeStart 订单生成时间， 格式为yyyyMMddHHmmss，如2009年12 月25 日9 点10 分10 秒表示为20091225091010。时区为GMT+8 beijing。该时间取自商户服务器
		 * @param timeExpire 订单失效时间，格式同上
		 * @param goodsTag 商品标记，微信平台配置的商品标记，用于优惠券或者满减使用
		 */

		// int totalFee = orderGroup.getRealPrice().multiply(new
		// BigDecimal("100")).intValue();
		Date startDate = new Date();
		Date endDate = DateComFunc.addHour(startDate, 1L);
		String timeStart = DateComFunc.formatDate(startDate, "yyyyMMddHHmmss");
		String timeExpire = DateComFunc.formatDate(endDate, "yyyyMMddHHmmss");
		if (Tools.isEmpty(notify_url)) {
			notify_url = Application.getRBasePath() + "/pay/notifyUrl";
		}
		// spbill_create_ip = "123.206.8.85";
		PayReqData payReqData = null;
		payReqData = new PayReqData(null, body, attach, String.valueOf(outTradeNo), totalFee, "WEB", spbill_create_ip, timeStart,
				timeExpire, null, notify_url, "JSAPI", openid);

		return payReqData;
	}

	@Override
	public boolean updateOrderGroupByPaySuccessed(OrderGroup orderGroup, String transaction_id, String out_trade_no, int payType,
			int payStatus, int orderGroupStatus, String mallCode) {
		boolean flag = false;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("transaction_id", transaction_id);
		paramMap.put("groupId", out_trade_no);
		paramMap.put("payType", payType);
		paramMap.put("payStatus", payStatus);
		paramMap.put("orderGroupStatus", orderGroupStatus);
		paramMap.put("orderSendType", orderGroup.getOrderSendType());
		paramMap.put("payTime", new Date());
		paramMap.put("receivePhone", orderGroup.getReceivePhone());
		paramMap.put("realPrice", orderGroup.getRealPrice());
		paramMap.put("userId", orderGroup.getUserId());
		paramMap.put("mallCode", mallCode);
		
		int score = orderGroup.getRealPrice().intValue();
		
		//查询订单商品赠送的总积分数
		Integer sumProductScore = orderGroupProductService.selectSumScoreByGroupId(orderGroup.getId());
		if(sumProductScore != null && sumProductScore.intValue() != 0)
			score = score + sumProductScore;
		
		paramMap.put("getScore", score);

		// 更新group订单状态，同时增加用户积分
		flag = orderGroupService.updateStatusById(paramMap,"1");
		return flag;
	}

	@Override
	public boolean updatePintuanOrderByPaySuccessed(PinTuanOrder pinTuanOrder, int payType, int payStatus) {
		boolean flag = false;
		PinTuanOrder pt = new PinTuanOrder();
		pt.setId(pinTuanOrder.getId());
		pt.setPay_status(payStatus);
		pt.setPay_type(payType);
		pt.setStatus(2);// 拼团订单状态(1,未确认；2,已确认；3，待取货；4，派送中；5，已完成；6，已取消)

		// 更新订单状态
		flag = pinTuanOrderService.updatePtOrderById(pt);
		return flag;
	}

}
