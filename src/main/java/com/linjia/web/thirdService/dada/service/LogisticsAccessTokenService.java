package com.linjia.web.thirdService.dada.service;

import org.json.JSONObject;

public interface LogisticsAccessTokenService {
	
	boolean setGrantCode();
	
	boolean setAccessToken();

	boolean refreshAccessToken();
	
	boolean setAccessTokenTimeOutTest();

	boolean setRefreshAccessTokenTimeOutTest();
	
	boolean initDadaCode(JSONObject jsonObj);
}
