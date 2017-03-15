package com.linjia.web.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.constants.Constants;
import com.linjia.tools.DateComFunc;
import com.linjia.tools.Util;
import com.linjia.web.dao.CardCouponMapper;
import com.linjia.web.dao.ScoreChangeMapper;
import com.linjia.web.dao.ScoreOrderMapper;
import com.linjia.web.dao.ScoreProductMapper;
import com.linjia.web.dao.UserCardCouponMapper;
import com.linjia.web.model.CardCoupon;
import com.linjia.web.model.ScoreChange;
import com.linjia.web.model.ScoreOrder;
import com.linjia.web.model.ScoreProduct;
import com.linjia.web.model.UserCardCoupon;
import com.linjia.web.query.ScoreOrderQuery;
import com.linjia.web.service.OrderPayService;
import com.linjia.web.service.ScoreOrderService;

@Service
@Transactional
public class ScoreOrderServiceImpl extends BaseServiceImpl<ScoreOrder, Long> implements ScoreOrderService {

	@Resource
	private ScoreOrderMapper mapper;

	@Resource
	private ScoreChangeMapper scoreChangeMapper;

	@Resource
	private UserCardCouponMapper userCardCouponMapper;

	@Resource
	private CardCouponMapper cardCouponMapper;

	@Resource
	private ScoreProductMapper scoreProductMapper;

	@Autowired
	private OrderidGenerateServiceImpl orderidGenerateServiceImpl;

	@Override
	public BaseDao<ScoreOrder, Long> getDao() {
		return mapper;
	}

	@Autowired
	private OrderPayService orderPayService;

	/**
	 * 积分兑换订单生成(纯积分兑换的情况下)
	 * 
	 * lixinling 
	 * 2016年8月10日 上午10:18:57
	 * @param query
	 * @param userId
	 * @return
	 */
	@Override
	public boolean insertScoreOrder(ScoreProduct scoreProduct, String userId, String loginCode, String comment) {
		boolean flag = false;
		ScoreOrder model = new ScoreOrder();
		model.setId(orderidGenerateServiceImpl.generateScoreOrderId());
		model.setScoreProductId(scoreProduct.getId());
		model.setCardCouponId(scoreProduct.getCardCouponId());
		model.setName(scoreProduct.getName());
		model.setQuantity(1);
		model.setType(scoreProduct.getType());
		model.setOrderStatus(Constants.SCORE_STATUS.UNPAY);
		model.setPayType(Constants.PAY_TYPE.ONLYSCORE);
		model.setPrice(scoreProduct.getPrice());
		model.setScore(scoreProduct.getScore());
		model.setComment(comment);
		model.setUserId(userId);
		model.setContent(scoreProduct.getContent());
		model.setCardType(scoreProduct.getCardType());
		model.setThirdId(scoreProduct.getThirdId());

		Date startTime = new Date();
		model.setStartTime(startTime);
		model.setEndTime(DateComFunc.addMinute(startTime, 45));
		model.setCreDate(startTime);

		synchronized (userId) {
			// 防止同一用户多点登陆同时操作
			// 查询用户现在的积分
			JSONObject resObj = Util.queryDesaccount(userId);
			if (resObj.optInt("score") >= model.getScore()) {
				// 生成积分兑换订单
				int row = mapper.insertSelective(model);
				if (row == 1) {
					if (model.getPrice() != null && model.getPrice().doubleValue() == 0 && model.getScore() > 0) {
						// 纯积分兑换的情况下直接兑换
						// boolean result = decScoreByExchange(model.getScore(),
						// Constants.SCORE_WAY.CASH_PRIZE, userId, loginCode,
						// model
						// .getCardCouponId(), "积分商城兑换");
						// if (result) {
						// 更改订单状态并将兑换的商品券放入用户我的优惠券中
						this.updatePayScoreOrderStatus(userId, model.getCardCouponId(), model.getId(), Constants.PAY_TYPE.ONLYSCORE);
						flag = true;
						// } else {
						// // 抛出异常进行回滚操作
						// throw new RuntimeException();
						// }

					}
				}
			}
		}
		return flag;
	}

	/**
	 * 通过兑换扣减积分
	 * lixinling 
	 * 2016年8月10日 下午2:57:11
	 * @param score
	 * @param type
	 * @param userId
	 * @param loginCode
	 * @return
	 */
	public boolean decScoreByExchange(int score, int type, String userId, String loginCode, Long cardCouponId, String content) {
		// 积分记录表写入数据
		ScoreChange scoreChange = new ScoreChange();
		scoreChange.setUserId(userId);
		scoreChange.setScore(-score);
		scoreChange.setType(type);
		scoreChange.setCreDate(new Date());
		scoreChange.setDeleted(false);
		scoreChangeMapper.insertSelective(scoreChange);

		JSONObject result = Util.decScoreAccount(scoreChange.getId(), loginCode, content, scoreChange.getType(), score);

        return result.optJSONArray("subjectAccounts") != null;

    }

	/**
	 * 积分兑换订单生成(积分+金钱兑换的情况下)
	 * 
	 * lixinling 
	 * 2016年8月10日 上午10:18:57
	 * @param query
	 * @param userId
	 * @return
	 */
	@Override
	public Map<String, Object> insertPayScoreOrder(ScoreProduct scoreProduct, ScoreOrder model, String userId, String loginCode) {
		model.setScoreProductId(scoreProduct.getId());
		model.setCardCouponId(scoreProduct.getCardCouponId());
		model.setName(scoreProduct.getName());
		model.setQuantity(1);
		model.setType(scoreProduct.getType());
		model.setOrderStatus(Constants.SCORE_STATUS.UNPAY);
		model.setPrice(scoreProduct.getPrice());
		model.setScore(scoreProduct.getScore());
		model.setImagePath(scoreProduct.getImagePath());
		try {
			model.setComment(URLDecoder.decode(model.getComment(), "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		model.setUserId(userId);
		model.setContent(scoreProduct.getContent());
		model.setCardType(scoreProduct.getCardType());
		model.setThirdId(scoreProduct.getThirdId());

		Date startTime = new Date();
		model.setStartTime(startTime);
		model.setEndTime(DateComFunc.addMinute(startTime, 45));
		model.setCreDate(startTime);
		Map<String, Object> resMap = new HashMap<String, Object>();
		// 查询用户现在的积分
		JSONObject resObj = Util.queryDesaccount(userId);
		if (resObj.optInt("score") >= model.getScore()) {
			// 生成积分兑换订单
			model.setId(orderidGenerateServiceImpl.generateScoreOrderId());
			int row = mapper.insertSelective(model);
			if (row == 1 && model.getPrice() != null && model.getPrice().doubleValue() > 0 && model.getScore() > 0) {
				logger.info("生成积分兑换订单已完成。");
				resMap.put("scoreOrder", model);
			}
		} else {
			Util.writeError(resMap);
			resMap.put("error_code", "30001");
		}
		return resMap;
	}

	/**
	 * 更改订单状态并将兑换的商品券放入用户我的优惠券中
	 * 
	 * lixinling 
	 * 2016年8月10日 上午10:18:57
	 * @param query
	 * @param userId
	 * @return
	 */
	@Override
	public boolean updatePayScoreOrderStatus(String userId, Long cardCouponId, Long scoreOrderId, Integer payType) {
		boolean flag = false;
		logger.info("积分兑换订单金钱支付已完成。");

		ScoreOrder model = mapper.selectByPrimaryKey(scoreOrderId);

		// 进行积分支付
		boolean result = decScoreByExchange(model.getScore(), Constants.SCORE_WAY.CASH_PRIZE, userId, userId, model.getCardCouponId(),
				"积分商城兑换");
		if (result) {
			try {
				logger.info("积分兑换订单积分支付已完成。");
				// 更新订单状态为已支付
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("orderStatus", Constants.SCORE_STATUS.SUCCESS);
				params.put("payType", payType);// 支付状态
				params.put("id", scoreOrderId);
				mapper.updateStatusById(params);
				logger.info("积分兑换订单状态更新已完成。");
				
				//更新兑换商品的数量(更新券码券的兑换日期和状态)
				updateScoreProductQtyAndTime(model.getScoreProductId(),model.getCardType(),model.getThirdId());

				// 将兑换的商品券放入用户我的优惠券中
				CardCoupon cardCoupon = cardCouponMapper.selectByPrimaryKey(cardCouponId);
				if (cardCoupon != null) {
					if(Constants.CARD_COUPON_TYPE.THIRD_LJQ == cardCoupon.getCardType() || Constants.CARD_COUPON_TYPE.THIRD_QM == cardCoupon.getCardType()){
						//如果是链接券或券码券则不往用户优惠券中放
						flag =true;
						logger.info("积分兑换操作已完成。");
						return flag;
					}
					UserCardCoupon userCardCoupon = new UserCardCoupon();
					userCardCoupon.setUserId(userId);
					userCardCoupon.setCardCouponId(cardCoupon.getCardId());
					userCardCoupon.setCardType(cardCoupon.getCardType());
					userCardCoupon.setReceiveTime(new Date());
					userCardCoupon.setSourceType(Constants.SCORE_SOURCE_TYPE.JFDH);
					// 使用时间类型：1.静态区间 2.动态时间（天）
					if (cardCoupon.getTimeType() == 1) {
						userCardCoupon.setUseStartTime(cardCoupon.getUseStartTime());
						userCardCoupon.setUseEndTime(cardCoupon.getUseEndTime());
					} else if (cardCoupon.getTimeType() == 2) {
						Date useStartTime = DateComFunc.addDay(new Date(), cardCoupon.getValidBeginDay());
						Date useEndTime = DateComFunc.addDay(useStartTime, cardCoupon.getValidDay());
						userCardCoupon.setUseStartTime(useStartTime);
						userCardCoupon.setUseEndTime(useEndTime);
					}
					userCardCoupon.setUseStatus(Constants.CARD_USED_STATUS.UNUSED);
					userCardCoupon.setUpdateDate(new Date());
					userCardCoupon.setDeleted(false);
					userCardCoupon.setCardCouponPrice(cardCoupon.getAmount());
					int r = userCardCouponMapper.insertSelective(userCardCoupon);

					if (r == 1) {
						logger.info("将兑换的商品券放入用户我的优惠券中操作已完成。");
						Map<String, Object> map = new HashMap<String, Object>();
						params.put("userCardCouponId", userCardCoupon.getId());
						params.put("id", scoreOrderId);
						mapper.updateUserCardCouponIdById(map);
						flag = true;
					}
					
				}
			} catch (Exception e) {
				logger.error("[更改订单状态并将兑换的商品券放入用户我的优惠券中]操作出错!", e);
			}
			//flag = true;
		} else {
			logger.info("进行积分支付操作失败。");
		}

		return flag;
	}

	/**
	 * 更新积分订单状态
	 * lixinling 
	 * 2016年8月11日 下午1:21:56
	 * @param id
	 * @param orderStatus
	 * @return
	 */
	@Override
	public boolean updateStatusById(Long id, int orderStatus) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("orderStatus", orderStatus);
		return mapper.updateStatusById(params);
	}

	/**
	 * 更新兑换商品的数量(更新券码券的兑换日期和状态)
	 * lixinling 
	 * 2016年12月6日 上午11:01:12
	 * @param scoreProduct
	 * @return
	 */
	public boolean updateScoreProductQtyAndTime(Long scoreProductId, Integer cardType, Long thirdId) {
		// 更新兑换商品的数量(更新券码券的兑换日期和状态)
		scoreProductMapper.updateQtyById(scoreProductId);

		// 如果是券码的情况下，更新该券码记录的兑换时间和状态
		if (Constants.CARD_COUPON_TYPE.THIRD_QM == cardType) {
			scoreProductMapper.updateThirdStatusById(thirdId);
		}
		return true;
	}

	@Override
	public List<ScoreOrder> selectExchangeRecordList(ScoreOrderQuery query) {
		return mapper.selectExchangeRecordList(query);
	}
}
