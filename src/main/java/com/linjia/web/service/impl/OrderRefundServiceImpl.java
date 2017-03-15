package com.linjia.web.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.constants.Application;
import com.linjia.constants.Constants;
import com.linjia.tools.DateComFunc;
import com.linjia.tools.Util;
import com.linjia.web.dao.AccountcashdepositRecordMapper;
import com.linjia.web.dao.OrderGroupMapper;
import com.linjia.web.dao.OrderRefundMapper;
import com.linjia.web.dao.ScoreChangeMapper;
import com.linjia.web.model.AccountcashdepositRecord;
import com.linjia.web.model.OrderGroup;
import com.linjia.web.model.OrderRefund;
import com.linjia.web.model.ScoreChange;
import com.linjia.web.model.ScoreOrder;
import com.linjia.web.model.UserCardCoupon;
import com.linjia.web.query.OrderRefundQuery;
import com.linjia.web.service.AccountcashChangeService;
import com.linjia.web.service.AccountcashdepositRecordService;
import com.linjia.web.service.OrderRefundService;
import com.linjia.web.service.OrderidGenerateService;
import com.linjia.web.service.UserCardCouponService;
import com.linjia.wxpay.common.Configure;
import com.linjia.wxpay.common.Signature;
import com.linjia.wxpay.protocol.RefundReqData;
import com.linjia.wxpay.protocol.RefundResData;
import com.linjia.wxpay.service.RefundService;

@Service
@Transactional
public class OrderRefundServiceImpl extends BaseServiceImpl<OrderRefund, Long> implements OrderRefundService {

	@Resource
	private OrderRefundMapper mapper;

	@Resource
	private OrderGroupMapper orderGroupMapper;

	@Autowired
	private OrderidGenerateService orderidGenerateService;

	@Resource
	private AccountcashdepositRecordMapper accountcashdepositRecordMapper;

	@Autowired
	private AccountcashChangeService accountcashChangeService;
	
	@Autowired
	private ScoreOrderServiceImpl scoreOrderServiceImpl;
	
	@Autowired
	private UserCardCouponService userCardCouponService;

	@Resource
	private ScoreChangeMapper scoreChangeMapper;

	@Override
	public BaseDao<OrderRefund, Long> getDao() {
		return mapper;
	}

	/**
	 * 退款
	 * lixinling 
	 * 2016年7月29日 上午11:31:40
	 * @param source 默认为整单退款操作，1为客服退款操作
	 * @param orderType 1:普通订单,2:拼团订单,3:积分订单,其他为普通订单
	 * @return
	 */
	@Override
	public Map<String, Object> insertRefund(OrderGroup orderGroup, String userId, Integer orderType, Integer source, Long refundId) {
		OrderRefund orderRefund = null;
		if((source != null && source == 1) ||(orderType != null && (orderType == 2 || orderType == 3)) && refundId != null){
			//客服退款操作或拼团退款单
			orderRefund = mapper.selectByPrimaryKey(refundId);
		}else{
			orderRefund = this.createRefundParams(orderGroup, userId);
			mapper.insertSelective(orderRefund);
		}
		Map<String, Object> resMap = new HashMap<String, Object>();
		if (orderRefund != null && Constants.ORDER_REFUND_STATUS.WAIT_REFUND == orderRefund.getRefundOnlinepayStatus()) {
			// 微信退款
			if (orderGroup.getPayType() == Constants.PAY_TYPE.WX) {
				// 构造退款请求数据对象
				RefundReqData refundReqData = createRefundReqData(orderGroup, orderRefund);
				try {
					// 发起退款请求
					String resultRefundXml = new RefundService().request(refundReqData);
					if (StringUtils.isEmpty(resultRefundXml)) {
						logger.error("【微信退款申请失败】退款请求错误，返回值为空");
						resMap.put("return_code", 1);
						return resMap;
					} else {
						RefundResData refundResData = (RefundResData) com.linjia.wxpay.common.Util.getObjectFromXML(resultRefundXml,
								RefundResData.class);
						if (refundResData.getReturn_code() == null || refundResData.getReturn_code().equals("FAIL")) {
							logger.error("【微信退款申请失败】退款API系统返回失败，请检测Post给API的数据是否规范合法。返回的错误信息为：" + refundResData.getReturn_msg());
							resMap.put("return_code", 2);
							return resMap;
						} else if (refundResData.getResult_code().equals("SUCCESS")) {
							// --------------------------------------------------------------------
							// 收到API的返回数据的时候得先验证一下数据有没有被第三方篡改，确保安全
							// --------------------------------------------------------------------
							if (!Signature.checkIsSignValidFromResponseString(resultRefundXml)) {
								logger.error("【微信退款申请失败】退款请求API返回的数据签名验证失败，有可能数据被篡改了");
								resMap.put("return_code", 3);
								return resMap;
							}

							if ("SUCCESS".equals(refundResData.getResult_code())) {
								// 更新退款单和订单状态
								Map<String, Object> refundParam = new HashMap<String, Object>();
								refundParam.put("groupId", orderRefund.getOrderGroupId());
								refundParam.put("refundOnlinepayStatus", Constants.ORDER_REFUND_STATUS.REFUNDDING);
								refundParam.put("outRefundNo", orderRefund.getId());
								refundParam.put("refundId", refundResData.getRefund_id());
								refundParam.put("transactionId", refundResData.getTransaction_id());
								refundParam.put("refundAmount", orderRefund.getRefundAmount());
								refundParam.put("userId", orderRefund.getUserId());
								this.updateRefundStatusById(refundParam);
								resMap.put("return_code", 5);
								resMap.put("refundId", orderRefund.getId());
							}

						} else if (refundResData.getResult_code().equals("FAIL")) {
							logger.error("【微信退款申请失败】退款API系统返回失败，提交业务失败。返回的错误信息为：" + refundResData.getErr_code_des());
							resMap.put("return_code", 2);
							return resMap;
						}

					}
				} catch (Exception e) {
					logger.error("调用微信退款申请发生错误：", e);
					e.printStackTrace();
					resMap.put("return_code", 4);
					return resMap;
				}
			} else if (orderGroup.getPayType() == Constants.PAY_TYPE.PURSE) {
				// 钱包支付退款(因为海鼎没有退款接口，因此用充值接口代替)
				AccountcashdepositRecord accountcashdepositRecord = new AccountcashdepositRecord();
				accountcashdepositRecord.setAccountaccesscode(orderRefund.getUserId());
				accountcashdepositRecord.setAccountaccesstype(2);
				accountcashdepositRecord.setOccur(orderRefund.getRefundAmount());
				accountcashdepositRecord.setRealpay(orderRefund.getRefundAmount());
				accountcashdepositRecord.setTrantime(new Date());
				accountcashdepositRecord.setXid(System.currentTimeMillis() / 1000);
				accountcashdepositRecord.setAction(Constants.ACCOUNTCASH_ACTION.REFUND_ACCOUNT);
				accountcashdepositRecord.setOrderGroupId(orderRefund.getOrderGroupId());
				int r = accountcashdepositRecordMapper.insertSelective(accountcashdepositRecord);
				if (r == 1) {

					// CRM会员充值
					JSONObject result = Util.accountcashdeposit(accountcashdepositRecord.getAccountaccesscode(),
							String.valueOf(accountcashdepositRecord.getTranId()), accountcashdepositRecord.getOccur().toString(),
							accountcashdepositRecord.getRealpay().toString(),
							DateComFunc.formatDate(accountcashdepositRecord.getTrantime(), "yyyy-MM-dd HH:mm:ss"),
							accountcashdepositRecord.getXid());
					if (result != null && result.optInt("responseCode") == 204) {
						logger.info("调用CRM接口钱包充值成功。");

						// 更改充值记录为已成功
						accountcashdepositRecord.setTranstatus(Constants.TRAN_STATUS.SUCCESSED);
						accountcashdepositRecord.setTransactionId(null);
						accountcashdepositRecordMapper.updateByPrimaryKeySelective(accountcashdepositRecord);

						// 插入用户钱包交易变化记录
						accountcashChangeService.insert(Constants.ACCOUNTCASH_ACTION.REFUND_ACCOUNT,
								accountcashdepositRecord.getAccountaccesscode(), accountcashdepositRecord.getOccur());

						// 更新退款单和订单状态
						Map<String, Object> refundParam = new HashMap<String, Object>();
						refundParam.put("groupId", orderRefund.getOrderGroupId());
						refundParam.put("refundOnlinepayStatus", Constants.ORDER_REFUND_STATUS.REFUNDED);
						refundParam.put("outRefundNo", orderRefund.getId());
						refundParam.put("refundId", orderRefund.getRefundId());
						refundParam.put("refundAmount", orderRefund.getRefundAmount());
						refundParam.put("userId", orderRefund.getUserId());
						this.updateRefundStatusById(refundParam);
						resMap.put("return_code", 5);
						resMap.put("refundId", orderRefund.getId());
					} else {
						// 充值失败
						logger.info("(订单退款操作)调用CRM接口执行钱包退款充值失败。");
						// 更新钱包充值记录的状态为未完成
						accountcashdepositRecord.setTranstatus(Constants.TRAN_STATUS.UNSUCCESS);
						accountcashdepositRecord.setTransactionId(null);
						accountcashdepositRecordMapper.updateByPrimaryKeySelective(accountcashdepositRecord);
						resMap.put("return_code", 6);
					}
				}
			}

			return resMap;
		}
		return null;
	}

	/**
	 * 构造退款单参数
	 * 
	 * lixinling 
	 * 2016年7月29日 上午11:51:15
	 * @param orderGroup
	 * @param userId
	 * @param loginPhone
	 * @return
	 */
	public OrderRefund createRefundParams(OrderGroup orderGroup, String userId) {
		long orderRefundId = orderidGenerateService.generateRefundId();
		JSONObject userInfo = Util.getUserInfo(userId);
		OrderRefund orderRefund = new OrderRefund();
		orderRefund.setId(orderRefundId);
		orderRefund.setOrderGroupId(orderGroup.getId());
		orderRefund.setUserId(orderGroup.getUserId());
		orderRefund.setLoginPhone(userInfo == null ? userId : userInfo.optString("phone"));
		orderRefund.setRefundType(Constants.REFUND_TYPE.AUTO);
		orderRefund.setRefundAmount(orderGroup.getRealPrice());
		orderRefund.setRefundSource(Constants.REFUND_SOURCE.CANCEL);
		orderRefund.setRefundStatus(Constants.REFUND_STATUS.REVIEWED);
		orderRefund.setConfirmor("system");
		orderRefund.setConfirmTime(new Date());
		orderRefund.setMobile(userInfo == null ? userId : userInfo.optString("phone"));
		orderRefund.setRefundPayee(orderGroup.getReceiveName());
		orderRefund.setRefundReason(orderGroup.getRefundReason());
		orderRefund.setMallCode(orderGroup.getMallCode());
		orderRefund.setMallName(orderGroup.getMallName());
		orderRefund.setMallPhone(orderGroup.getMallPhone());
		orderRefund.setRefundOnlinepayStatus(Constants.ORDER_REFUND_STATUS.WAIT_REFUND);
		if (orderGroup.getPayType() == Constants.PAY_TYPE.WX) {
			orderRefund.setPayTypeId(0);
			orderRefund.setPayTypeName("微信支付");
		} else {
			orderRefund.setReturnBalance(orderGroup.getRealPrice());
			orderRefund.setPayTypeId(1);
			orderRefund.setPayTypeName("钱包支付");
		}
		orderRefund.setCreateTime(new Date());
		return orderRefund;
	}

	/**
	 * 更新退款单状态
	 * lixinling 
	 * 2016年7月29日 上午11:31:40
	 * @return
	 */
	@Override
	public boolean updateRefundStatusById(Map<String, Object> map) {

		int row = mapper.updateRefundStatusById(map);
		Long groupId = (Long)map.get("groupId");
		if (row == 1 && String.valueOf(groupId).charAt(6) == '2'){
			//商城订单处理
			//退款减积分
			Integer status = orderGroupMapper.selectStatusByPrimaryKey(groupId);
			if(Constants.ORDER_GROUP_STATUS.SUCCESS == status){
				//只有已完成的订单退款时才进行减积分操作
				
				String userId = (String)map.get("userId");
				Integer score = ((BigDecimal)map.get("refundAmount")).intValue();
				boolean result = scoreOrderServiceImpl.decScoreByExchange(score, Constants.SCORE_WAY.DEDUCT, userId, userId, null,"订单退款减积分");
				if(result){
					logger.info("【订单退款减积分】操作完成.");
				}else{
					logger.info("【订单退款减积分】操作失败.");
				}
			}
			return true;
		}if (row == 1 && String.valueOf(groupId).charAt(6) == '4'){
			logger.info("【积分订单退款】操作........................................");
			
			//积分订单处理(将用户兑换的商品券作废，并给用户增加积分)
			ScoreOrder scoreOrder = scoreOrderServiceImpl.selectById(groupId);
			if(scoreOrder != null){
				//订单总状态：0未支付；1已完成；
				if(scoreOrder.getOrderStatus() == 1){
					int score = scoreOrder.getScore();
					Long userCardCouponId = scoreOrder.getUserCardCouponId();
					
					//1.将用户兑换的商品券作废
					UserCardCoupon userCardCoupon = new UserCardCoupon();
					userCardCoupon.setId(userCardCouponId);
					userCardCoupon.setUseStatus(Constants.CARD_USED_STATUS.KEFU_VERIFICATIONED);
					userCardCouponService.updateUseStatusByPrimaryKey(userCardCoupon);
					
					//2.给用户增加积分
					String userId = scoreOrder.getUserId();
					
					// 积分记录表写入数据
					ScoreChange scoreChange = new ScoreChange();
					scoreChange.setUserId(userId);
					scoreChange.setScore(score);
					scoreChange.setType(Constants.SCORE_WAY.TURN_IN);
					scoreChange.setCreDate(new Date());
					scoreChange.setDeleted(false);
					scoreChangeMapper.insertSelective(scoreChange);
					Util.incScoreAccount(scoreChange.getId(), userId, "积分订单退款", scoreChange.getType(), scoreChange.getScore());
					logger.info("【积分订单退款增积分】操作完成.");
				}
			}else{
				logger.info("【积分订单】不存在.");
			}
		}

		return false;
	}

	/**
	 * 查询我的退款单
	 * lixinling 
	 * 2016年7月29日 上午11:31:40
	 * @return
	 */
	@Override
	public List<OrderRefund> selectMyRefundOrderList(OrderRefundQuery query) {
		return mapper.selectMyRefundOrderList(query);
	}

	/**
	 * 构造退款请求数据对象
	 * lixinling 
	 * 2016年7月27日 下午2:46:06
	 * @param orderGroup
	 * @return
	 */
	public RefundReqData createRefundReqData(OrderGroup orderGroup, OrderRefund orderRefund) {

		/**
		 * 请求退款查询服务
		 * @param transactionID 是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。建议优先使用
		 * @param outTradeNo 商户系统内部的订单号,transaction_id 、out_trade_no 二选一，如果同时存在优先级：transaction_id>out_trade_no
		 * @param deviceInfo 微信支付分配的终端设备号，与下单一致
		 * @param outRefundNo 商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
		 * @param refundID 来自退款API的成功返回，微信退款单号refund_id、out_refund_no、out_trade_no 、transaction_id 四个参数必填一个，如果同事存在优先级为：refund_id>out_refund_no>transaction_id>out_trade_no
		 */

		int totalFee = orderGroup.getRealPrice().multiply(new BigDecimal("100")).intValue();
		int refundFee = orderRefund.getRefundAmount().multiply(new BigDecimal("100")).intValue();
		String opUserID = Configure.getMchid();
		RefundReqData refundReqData = new RefundReqData(orderGroup.getTransactionId(), String.valueOf(orderRefund.getOrderGroupId()), null,
				String.valueOf(orderRefund.getId()), totalFee, refundFee, opUserID, "CNY");

		return refundReqData;
	}

	@Override
	public List<OrderRefund> selectWxRefundingOrder() {
		return mapper.selectWxRefundingOrder();
	}

	@Override
	public boolean deleteRefundOrderById(Long id) {
		return mapper.deleteRefundOrderById(id);
	}
	
	@Override
	public OrderRefund selectOneOrder(Map<String, Object> map) {
		return mapper.selectOneOrder(map);
	}

	@Override
	public Long selectRefundIdByOrderId(Long orderGroupId) {
		return mapper.selectRefundIdByOrderId(orderGroupId);
	}

}
