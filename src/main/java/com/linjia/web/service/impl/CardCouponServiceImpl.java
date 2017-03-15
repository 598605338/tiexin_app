package com.linjia.web.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.constants.Constants;
import com.linjia.web.dao.CardCouponMapper;
import com.linjia.web.model.ActivityInfo;
import com.linjia.web.model.CardCoupon;
import com.linjia.web.model.ShoppingCar;
import com.linjia.web.query.UserCardCouponQuery;
import com.linjia.web.service.ActivityPromotionService;
import com.linjia.web.service.CardCouponService;
import com.linjia.web.service.TailGoodsClearService;

@Service
@Transactional
public class CardCouponServiceImpl extends BaseServiceImpl<CardCoupon, Long> implements CardCouponService {

	@Resource
	private CardCouponMapper mapper;

	@Autowired
	private ActivityPromotionService activityPromotionService;

	@Override
	public BaseDao<CardCoupon, Long> getDao() {
		return mapper;
	}

	@Override
	public List<CardCoupon> selectNotExpiredByUserId(UserCardCouponQuery userCardCouponQuery) {
		userCardCouponQuery.setUseStatus(Constants.CARD_USED_STATUS.UNUSED);
		return mapper.selectNotExpiredByUserId(userCardCouponQuery);
	}

	@Override
	public List<CardCoupon> selectExpiredByUserId(UserCardCouponQuery userCardCouponQuery) {
		userCardCouponQuery.setUseStatus(Constants.CARD_USED_STATUS.UNUSED);
		return mapper.selectExpiredByUserId(userCardCouponQuery);
	}

	@Override
	public List<CardCoupon> selectUsedByUserId(UserCardCouponQuery userCardCouponQuery) {
		return mapper.selectUsedByUserId(userCardCouponQuery);
	}

	@Override
	public CardCoupon checkCouponByUserCardCouponId(UserCardCouponQuery userCardCouponQuery) {
		return mapper.checkCouponByUserCardCouponId(userCardCouponQuery);
	}

	@Override
	public CardCoupon cardCheckByPrimaryKey(Long cardId) {
		return mapper.cardCheckByPrimaryKey(cardId);
	}

	@Override
	public List<CardCoupon> selectCanUsedByUserId(UserCardCouponQuery userCardCouponQuery, String mallCode, List<String> pCodeArray,
			boolean isFullSubtract, boolean isFreeFreight, boolean isPrepare, Integer ifUseCardCoupons, BigDecimal productPrice) {
		userCardCouponQuery.setUseStatus(Constants.CARD_USED_STATUS.UNUSED);
		userCardCouponQuery.setProductCodes(pCodeArray);
		// 查询用户未过期的优惠券
		List<CardCoupon> list = mapper.selectCanUsedByUserId(userCardCouponQuery);

		// 记录移除的代金券
		List<CardCoupon> removeList = new ArrayList<CardCoupon>();

		for (CardCoupon item : list) {

			// 如果是预约商品，则只能用代金券
			if (isPrepare && item.getCardType() != Constants.CARD_COUPON_TYPE.DJQ) {
				removeList.add(item);
				continue;
			}

			// 如果参与了订单满减活动，则根据活动条件能否使用优惠券来判断优惠券的使用情况
			// if(isFullSubtract){
			if (item.getCardType() == Constants.CARD_COUPON_TYPE.DJQ) {
				// 是否允许使用卡券（0，否；1，是）
				if (ifUseCardCoupons != null && ifUseCardCoupons == 0) {
					removeList.add(item);
					continue;
				}
				if (productPrice.compareTo(item.getLimitMoney()) < 0) {
					// 商品总价大于满减条件才可用
					removeList.add(item);
//					item.setCanUseFlag(1);
					continue;
				}
				// item.setCanUseFlag(1);
			}
			// }

			// 如果参与了免运费活动，则所有免运费券不可使用
			if (isFreeFreight) {
				if (item.getCardType() == Constants.CARD_COUPON_TYPE.MYFQ)
					// 卡券在订单中能否使用:0能使用；1不能使用
					item.setCanUseFlag(1);
			}
		}

		if (removeList.size() > 0) {
			list.removeAll(removeList);
		}
		// 查询当前时间该门店的促销活动信息
		/*
		 * List<ActivityInfo> promotionActivityList =
		 * activityPromotionService.selectActInfoByCurrTime(null, mallCode);
		 * 
		 * if (promotionActivityList != null && promotionActivityList.size() > 0
		 * && list != null && list.size() > 0) { // for (ActivityInfo item :
		 * promotionActivityList) {
		 * 
		 * } }
		 */

		return list;
	}

	@Override
	public int updateTotalNumByCardId(Map<String, Object> param) {
		return mapper.updateTotalNumByCardId(param);
	}

}
