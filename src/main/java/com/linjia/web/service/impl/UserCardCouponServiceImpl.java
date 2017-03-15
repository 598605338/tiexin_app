package com.linjia.web.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.constants.Constants;
import com.linjia.tools.DateComFunc;
import com.linjia.web.dao.CardCouponMapper;
import com.linjia.web.dao.UserCardCouponMapper;
import com.linjia.web.model.CardCoupon;
import com.linjia.web.model.UserCardCoupon;
import com.linjia.web.service.UserCardCouponService;

@Service
@Transactional
public class UserCardCouponServiceImpl extends BaseServiceImpl<UserCardCoupon, Long> implements UserCardCouponService {

	@Resource
	private UserCardCouponMapper mapper;

	@Resource
	private CardCouponMapper cardCouponMapper;

	@Override
	public BaseDao<UserCardCoupon, Long> getDao() {
		return mapper;
	}

	@Override
	public int updateUseStatusByPrimaryKey(UserCardCoupon model) {
		return mapper.updateUseStatusByPrimaryKey(model);
	}

	@Override
	public long selectCount(String userId, String mallCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("useStatus", Constants.CARD_USED_STATUS.UNUSED);
		params.put("mallCode", mallCode);
		return mapper.selectCount(params);
	}

	@Override
	public int checkValid(Long id, String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("userId", userId);
		return mapper.checkValid(params);
	}

	@Override
	public int insertUserCardCoupon(String userId, Long cardCouponId, int sourceType, Long id) {
		CardCoupon cardCoupon = cardCouponMapper.selectByPrimaryKey(cardCouponId);
		if (cardCoupon != null) {
			UserCardCoupon userCardCoupon = new UserCardCoupon();
			userCardCoupon.setUserId(userId);
			userCardCoupon.setCardCouponId(cardCoupon.getCardId());
			userCardCoupon.setCardType(cardCoupon.getCardType());
			userCardCoupon.setReceiveTime(new Date());
			userCardCoupon.setSourceType(sourceType);
			userCardCoupon.setReceiveRecordId(id);

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
			return mapper.insertSelective(userCardCoupon);
		}
		return 0;
	}

}
