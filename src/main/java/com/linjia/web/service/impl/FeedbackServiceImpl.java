package com.linjia.web.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.web.dao.FeedbackMapper;
import com.linjia.web.model.Feedback;
import com.linjia.web.service.FeedbackService;

@Service
@Transactional
public class FeedbackServiceImpl extends BaseServiceImpl<Feedback, Long> implements FeedbackService {
	
	@Resource
	private FeedbackMapper mapper;

	@Override
	public BaseDao<Feedback, Long> getDao() {
		return mapper;
	}
	
}
