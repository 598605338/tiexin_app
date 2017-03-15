package com.linjia.web.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.tools.DateComFunc;
import com.linjia.web.dao.CustMasterMapper;
import com.linjia.web.model.CustMaster;
import com.linjia.web.service.CustMasterService;

@Service
@Transactional
public class CustMasterServiceImpl extends BaseServiceImpl<CustMaster, String> implements CustMasterService {
	
	@Resource
	private CustMasterMapper mapper;

	@Override
	public BaseDao<CustMaster, String> getDao() {
		return mapper;
	}

	/**
	 * 当用户记录存在时进行更新，不存在时进行插入
	 * lixinling 
	 * 2016年8月12日 下午1:45:48
	 * @param model
	 * @return
	 */
	@Override
	public CustMaster insertOrUpdate(JSONObject data,String phone) {
		CustMaster model = new CustMaster();
		model.setUserId(data.optString("login"));
		model.setCreator(phone);
		model.setCustid(phone);
		model.setLogin(data.optString("login"));
		model.setCustname(data.optString("name"));
		model.setEmail(data.optString("email"));
		model.setRankname("普通会员");
		model.setLastlogin(new Date());
		model.setSex(data.optString("sex"));
		model.setPwd(data.optString("password"));
		model.setBirthday(DateComFunc.strToDate(data.optString("birthday"), "yyyy-MM-dd HH:mm:ss"));
		model.setPhone(data.optString("cellPhone"));
		model.setNickname(phone);
		model.setQq(data.optString("qq"));
		model.setMaritalstatus(data.optString("wedding"));
		model.setCardno(data.optString("code"));
		model.setOfflinePcustid(data.optString("code"));
		model.setAppName(data.optString("appName"));
		model.setOriginCode(data.optString("code"));
		model.setOrgCode(data.optJSONObject("org").optString("code"));
		model.setOrgName(data.optJSONObject("org").optString("name"));
		model.setRegisterDate(DateComFunc.strToDate(data.optString("registerDate"), "yyyy-MM-dd HH:mm:ss"));
		mapper.insertOrUpdate(model);
		return model;
	}

	@Override
	public String selectLoginByPrimaryKey(String userId) {
		return mapper.selectLoginByPrimaryKey(userId);
	}

	@Override
	public int updateByPrimaryKey(CustMaster model) {
		return mapper.updateByPrimaryKey(model);
	}

	
}
