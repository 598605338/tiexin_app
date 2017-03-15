package com.linjia.web.service.impl;

import java.util.Date;
import java.util.HashMap;
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
import com.linjia.web.dao.ReceiveCardCouponMapper;
import com.linjia.web.dao.UserCardCouponMapper;
import com.linjia.web.model.CardCoupon;
import com.linjia.web.model.ReceiveCardCoupon;
import com.linjia.web.model.UserCardCoupon;
import com.linjia.web.query.ReceiveCardCouponQuery;
import com.linjia.web.service.OrderGroupService;
import com.linjia.web.service.ReceiveCardCouponService;
import com.linjia.web.service.UserCardCouponService;

@Service
@Transactional
public class ReceiveCardCouponServiceImpl extends BaseServiceImpl<ReceiveCardCoupon, Long> implements ReceiveCardCouponService {
	
	@Resource
	private ReceiveCardCouponMapper mapper;
	
	@Resource
	private CardCouponMapper cardCouponMapper;
	
	@Autowired
	private UserCardCouponService userCardCouponService;

	@Override
	public BaseDao<ReceiveCardCoupon, Long> getDao() {
		return mapper;
	}

	@Override
	public List<ReceiveCardCoupon> selectByPageList(ReceiveCardCouponQuery query) {
		List<ReceiveCardCoupon> list = mapper.selectByPageList(query);
		/*if(list != null && list.size() > 0){
			for(ReceiveCardCoupon item : list){
				item.getPublishNum()
				
			}
		}*/
		
		return list;
	}

	@Override
	public int updatePublishNumByPrimaryKey(Map<String, Object> param) {
		return mapper.updatePublishNumByPrimaryKey(param);
	}

	@Override
	public boolean insertCardCouponForUser(String userId, Long cardCouponId, Long id, int publishNum) {
		boolean flag = false;

		//增加用户优惠券
		int row = userCardCouponService.insertUserCardCoupon(userId, cardCouponId, Constants.SCORE_SOURCE_TYPE.LQZX, id);
		if(row == 1){
			//更新活动(领券中心)发布数量publishNum
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("publishNum", publishNum-1);
			param.put("id", id);
			mapper.updatePublishNumByPrimaryKey(param);
			
			param.clear();
			
			//更新优惠券发布总数量
			param.put("minusNum", 1);
			param.put("cardId", cardCouponId);
			row = cardCouponMapper.updateTotalNumByCardId(param);
			
			flag =true;
		}
		return flag;
	}

	

}
