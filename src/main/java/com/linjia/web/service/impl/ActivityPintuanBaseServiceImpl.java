package com.linjia.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.web.dao.ActivityPintuanBaseMapper;
import com.linjia.web.model.ActivityPintuanBase;
import com.linjia.web.query.ActivityPintuanBaseQuery;
import com.linjia.web.service.ActivityPintuanBaseService;

@Service
@Transactional
public class ActivityPintuanBaseServiceImpl extends BaseServiceImpl<ActivityPintuanBase, Long> implements ActivityPintuanBaseService {
	
	@Resource
	private ActivityPintuanBaseMapper mapper;
	
	@Override
	public BaseDao<ActivityPintuanBase, Long> getDao() {
		return mapper;
	}

	@Override
	public List<ActivityPintuanBase> selectPintuanProductList(ActivityPintuanBaseQuery query) {
		return mapper.selectPintuanProductList(query);
	}

	@Override
	public ActivityPintuanBase selectDetailById(int id) {
		return mapper.selectDetailById(id);
	}

	@Override
	public boolean updateByBaseId(ActivityPintuanBase base) {
		return mapper.updateByBaseId(base);
	}


	

}
