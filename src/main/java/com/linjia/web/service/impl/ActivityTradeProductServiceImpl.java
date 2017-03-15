package com.linjia.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.web.dao.ActivityTradeProductMapper;
import com.linjia.web.model.ActivityTradeProduct;
import com.linjia.web.service.ActivityTradeProductService;

@Service
@Transactional
public class ActivityTradeProductServiceImpl extends BaseServiceImpl<ActivityTradeProduct, Long> implements ActivityTradeProductService {

	@Resource
	private ActivityTradeProductMapper mapper;

	@Override
	public BaseDao<ActivityTradeProduct, Long> getDao() {
		return mapper;
	}

	@Override
	public List<ActivityTradeProduct> selectTradedProduct(String[] tradeProductIdArray, Long activityId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("array", tradeProductIdArray);
		params.put("activityId", activityId);
		List<ActivityTradeProduct> activityTradeProductList = mapper.selectTradedProduct(params);
		return activityTradeProductList;
	}

}
