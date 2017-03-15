package com.linjia.web.service;


import java.util.Map;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.MallProductStore;

public interface MallProductStoreService extends BaseService<MallProductStore, Long>{
	
	/**
	 * 根据门店编码和商品编码查询库存
	 * lixinling 
	 * 2016年8月17日 下午3:46:33
	 * @param mallCode
	 * @param pCode
	 * @return
	 */
	int selectSafeQtyByMallAndPCode(String mallCode, String pCode);
	

	/**
	 * 根据门店编码和商品编码查询该商品在该门店的信息
	 * lixinling 
	 * 2016年9月12日 下午5:40:29
	 * @param map
	 * @return
	 */
	MallProductStore selectByMallAndPCode(String mallCode, String pCode);
}
