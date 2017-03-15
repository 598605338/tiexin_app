package com.linjia.web.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.web.dao.MallMasterMapper;
import com.linjia.web.model.MallMaster;
import com.linjia.web.model.Region;
import com.linjia.web.query.MallMasterQuery;
import com.linjia.web.service.MallMasterService;

@Service
@Transactional
public class MallMasterServiceImpl extends BaseServiceImpl<MallMaster, Long> implements MallMasterService {
	
	@Resource
	private MallMasterMapper mapper;

	@Override
	public BaseDao<MallMaster, Long> getDao() {
		return mapper;
	}

	@Override
	public MallMaster selectByMallCode(String mallCode) {
		return mapper.selectByMallCode(mallCode);
	}

	@Override
	public BigDecimal selectSendPriceByMallCode(String mallCode) {
		return mapper.selectSendPriceByMallCode(mallCode);
	}

	@Override
	public List<MallMaster> selectMallListByRegion(MallMasterQuery query) {
		return mapper.selectMallListByRegion(query);
	}

	@Override
	public List<Region> selectCityList() {
		return mapper.selectCityList();
	}

	@Override
	public List<Region> selectCountyList(Integer cityId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("cityId", cityId);
		return mapper.selectCountyList(map);
	}

	@Override
	public Integer selectUseFlgByMallCode(String mallCode) {
		return mapper.selectUseFlgByMallCode(mallCode);
	}

	

	

}
