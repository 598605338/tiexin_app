package com.linjia.web.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.web.dao.MallProductSendQtyMapper;
import com.linjia.web.model.MallProductSendQty;
import com.linjia.web.service.MallProductSendQtyService;

@Service
@Transactional
public class MallProductSendQtyServiceImpl extends BaseServiceImpl<MallProductSendQty, Long> implements MallProductSendQtyService {
	
	@Resource
	private MallProductSendQtyMapper mapper;

	@Override
	public BaseDao<MallProductSendQty, Long> getDao() {
		return mapper;
	}

	@Override
	public int insertOrUpdate(MallProductSendQty model) {
		return mapper.insertOrUpdate(model);
	}
	
}
