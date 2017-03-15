package com.linjia.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.web.dao.ScoreProductMapper;
import com.linjia.web.model.ScoreProduct;
import com.linjia.web.query.ScoreProductQuery;
import com.linjia.web.service.ScoreProductService;

@Service
@Transactional
public class ScoreProductServiceImpl extends BaseServiceImpl<ScoreProduct, Long> implements ScoreProductService {
	
	@Resource
	private ScoreProductMapper mapper;

	@Override
	public BaseDao<ScoreProduct, Long> getDao() {
		return mapper;
	}

	@Override
	public List<ScoreProduct> selectList(ScoreProductQuery query) {
		return mapper.selectList(query);
	}

	@Override
	public List<ScoreProduct> selectCanExchangeList(ScoreProductQuery query) {
		return mapper.selectCanExchangeList(query);
	}

	@Override
	public ScoreProduct selectInfoByPrimaryKey(Long id) {
		return mapper.selectInfoByPrimaryKey(id);
	}

	@Override
	public ScoreProduct selectThirdContentByScoreProductId(Long id) {
		return mapper.selectThirdContentByScoreProductId(id);
	}

	@Override
	public int updateQtyById(Long id) {
		return mapper.updateQtyById(id);
	}

	@Override
	public int updateThirdStatusById(Long id) {
		return mapper.updateThirdStatusById(id);
	}
	
}
