package com.linjia.web.query;

import java.util.List;
import java.util.Map;

import com.linjia.web.model.ActivityTradeProduct;


/**
 * SubmitOrder使用参数
 * @author lixinling
 * 2016年7月22日 上午11:43:08
 */
public class SubmitOrderParamBean{
	private String pCodeArray;
	private String mallCode;
	//加钱换购商品列表
	private Map<String,String> activityTradeProductMap;
	
	public String getpCodeArray() {
		return pCodeArray;
	}
	public void setpCodeArray(String pCodeArray) {
		this.pCodeArray = pCodeArray;
	}
	public String getMallCode() {
		return mallCode;
	}
	public void setMallCode(String mallCode) {
		this.mallCode = mallCode;
	}
	public SubmitOrderParamBean(String pCodeArray, String mallCode) {
		this.pCodeArray = pCodeArray;
		this.mallCode = mallCode;
	}
	public SubmitOrderParamBean() {
	}
	public Map<String, String> getActivityTradeProductMap() {
		return activityTradeProductMap;
	}
	public void setActivityTradeProductMap(Map<String, String> activityTradeProductMap) {
		this.activityTradeProductMap = activityTradeProductMap;
	}
	
	
}