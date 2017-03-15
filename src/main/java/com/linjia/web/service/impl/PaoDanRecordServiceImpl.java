package com.linjia.web.service.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.linjia.web.dao.PaoDanRecordMapper;
import com.linjia.web.model.PaoDanRecord;
import com.linjia.web.query.PaoDanRecordQuery;
import com.linjia.web.service.PaoDanRecordService;

@Service
@Transactional
public class PaoDanRecordServiceImpl implements  PaoDanRecordService {
	
	@Resource
	private  PaoDanRecordMapper mapper;

	@Override
	public boolean insertPaoDan(PaoDanRecord paoDanRecord) {
		return mapper.insertPaoDan(paoDanRecord);
	}

	@Override
	public boolean deletePaoDan(int id) {
		return mapper.deletePaoDan(id);
	}

	@Override
	public boolean updatePaoDan(PaoDanRecord paoDanRecord) {
		return mapper.updatePaoDan(paoDanRecord);
	}

	@Override
	public PaoDanRecord selectPaoDan(Map<String,Object> map) {
		return mapper.selectPaoDan(map);
	}

	@Override
	public List<PaoDanRecord> selectPaoDanList(PaoDanRecordQuery query) {
		return mapper.selectPaoDanList(query);
	}

	@Override
	public boolean deletePaoDanByOrderId(Long order_id) {
		// TODO Auto-generated method stub
		return false;
	}

}
