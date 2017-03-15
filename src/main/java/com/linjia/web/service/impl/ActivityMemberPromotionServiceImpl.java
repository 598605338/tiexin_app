package com.linjia.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.web.dao.ActivityMemberPromotionMapper;
import com.linjia.web.model.ActivityMemberPromotion;
import com.linjia.web.service.ActivityMemberPromotionService;

@Service
@Transactional
public class ActivityMemberPromotionServiceImpl extends BaseServiceImpl<ActivityMemberPromotion, Long> implements ActivityMemberPromotionService {
	
	@Resource
	private ActivityMemberPromotionMapper mapper;

	@Override
	public BaseDao<ActivityMemberPromotion, Long> getDao() {
		return mapper;
	}

	@Override
	public ActivityMemberPromotion selectByCurrentTime() {
		return mapper.selectByCurrentTime();
	}

	@Override
	public int updatePrizeNumById(int prizeNum, Long id) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("prizeNum", prizeNum);
		param.put("id", id);
		return mapper.updatePrizeNumById(param);
	}
	
}
