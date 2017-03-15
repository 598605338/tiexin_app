package com.linjia.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.web.dao.MallProductStoreMapper;
import com.linjia.web.model.MallProductStore;
import com.linjia.web.service.MallProductStoreService;

@Service
@Transactional
public class MallProductStoreServiceImpl extends BaseServiceImpl<MallProductStore, Long> implements MallProductStoreService {
	
	@Resource
	private MallProductStoreMapper mapper;

	@Override
	public BaseDao<MallProductStore, Long> getDao() {
		return mapper;
	}

	@Override
	public int selectSafeQtyByMallAndPCode(String mallCode, String pCode) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("mallCode", mallCode);
		map.put("pCode", pCode);
		Object res = mapper.selectSafeQtyByMallAndPCode(map);
		return res == null?9999:(int)res;
	}

	@Override
	public MallProductStore selectByMallAndPCode(String mallCode, String pCode) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("mallCode", mallCode);
		map.put("pCode", pCode);
		return mapper.selectByMallAndPCode(map);
	}
	

}
