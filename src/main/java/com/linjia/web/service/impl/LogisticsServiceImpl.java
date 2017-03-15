package com.linjia.web.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.web.dao.LogisticsMapper;
import com.linjia.web.model.BdLogisResult;
import com.linjia.web.model.Logistics;
import com.linjia.web.query.LogisticsQuery;
import com.linjia.web.service.LogisticsService;

@Service
public class LogisticsServiceImpl extends BaseServiceImpl<Logistics, Long> implements LogisticsService {
	
	@Resource
	private LogisticsMapper mapper;

	@Override
	public int insertLogistics(Logistics dto) {
		if(dto.getResult()==null){
			dto.setOuter_id(dto.getOrderid()+"");
		}else{
			BdLogisResult result=dto.getResult();
			dto.setOuter_id(result.getOrder_id()+"");
			dto.setOrder_id(result.getOut_order_id());
		}
		return mapper.insertLogistics(dto);
	}

	@Override
	public int deleteLogistics(Logistics dto) {
		return mapper.deleteLogistics(dto);
	}

	@Override
	public int updateLogistics(Logistics dto) {
		return mapper.updateLogistics(dto);
	}

	@Override
	public Logistics selectLogisticsById(LogisticsQuery query) {
		return mapper.selectLogisticsById(query);
	}

	@Override
	public BaseDao<Logistics, Long> getDao() {
		return null;
	}

}
