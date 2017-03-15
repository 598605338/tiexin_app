package com.linjia.web.service.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.linjia.web.dao.BannerInfoMapper;
import com.linjia.web.model.BannerInfo;
import com.linjia.web.service.BannerInfoService;

@Service
@Transactional
public class BannerInfoServiceImpl implements BannerInfoService{

	@Resource
	private BannerInfoMapper mapper;

	@Override
	public boolean insertBannerInfo(BannerInfo info) {
		return mapper.insertBannerInfo(info);
	}

	@Override
	public boolean updateBannerInfoById(BannerInfo info) {
		return mapper.updateBannerInfoById(info);
	}

	@Override
	public boolean deleteBannerInfoById(int id) {
		return mapper.deleteBannerInfoById(id);
	}

	@Override
	public BannerInfo selectBannerInfoById(int id) {
		return mapper.selectBannerInfoById(id);
	}

	@Override
	public List<BannerInfo> selectBannerInfoAll(Map<String, Object> map) {
		return mapper.selectBannerInfoAll(map);
	}

	@Override
	public boolean updBatchBaseInfos(List<BannerInfo> list) {
		return mapper.updBatchBaseInfos(list);
	}

	
}
