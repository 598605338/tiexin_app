package com.linjia.web.service;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.MallMaster;
import com.linjia.web.model.Region;
import com.linjia.web.query.MallMasterQuery;

public interface MallMasterService extends BaseService<MallMaster, Long>{
	
	/**
	 * 根据门店code查询门店信息
	 * lixinling
	 * 2016年7月21日 下午5:54:30
	 * @param mallCode
	 * @return
	 */
	MallMaster selectByMallCode(String mallCode);
	
	/**
	 * 根据门店code查询该门店的配送费
	 * lixinling
	 * 2016年7月21日 下午5:54:30
	 * @param mallCode
	 * @return
	 */
	BigDecimal selectSendPriceByMallCode(String mallCode);
	
	/**
	 * 根据区域条件取得门店列表 
	 * lixinling
	 * 2016年7月21日 下午5:54:30
	 * @param mallCode
	 * @return
	 */
	List<MallMaster> selectMallListByRegion(MallMasterQuery query);
	
	/**
	 * 取得门店所属城市列表
	 * lixinling
	 * 2016年7月21日 下午5:54:30
	 * @param mallCode
	 * @return
	 */
	List<Region> selectCityList();
	
	/**
	 * 取得门店所属区县列表  
	 * lixinling
	 * 2016年7月21日 下午5:54:30
	 * @param mallCode
	 * @return
	 */
	List<Region> selectCountyList(Integer cityId);

	/**
	 * 查询门店运营状态 
	 * lixinling
	 * 2016年7月21日 下午5:54:30
	 * @param mallCode
	 * @return
	 */
	Integer selectUseFlgByMallCode(String mallCode);
}
