package com.linjia.web.service;

import com.linjia.web.model.Logistics;
import com.linjia.web.query.LogisticsQuery;

public interface LogisticsService{

	/**
	 * 插入物流信息
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param app_poi_code 店铺码
	 * @return
	 */
	int insertLogistics(Logistics dto);
	
	/**
	 *删除物流信息
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @return
	 */
	int deleteLogistics(Logistics dto);
	
	/**
	 * 修改物流信息
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @return
	 */
	int updateLogistics(Logistics dto);
	
	/**
	 * 查询物流信息
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @return
	 */
	Logistics selectLogisticsById(LogisticsQuery query);
}
