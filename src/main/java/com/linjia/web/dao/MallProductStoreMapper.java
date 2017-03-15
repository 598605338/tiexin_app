package com.linjia.web.dao;

import java.util.Map;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.MallProductStore;

public interface MallProductStoreMapper extends BaseDao<MallProductStore, Long> {
	
	/**
	 * 根据门店编码和商品编码查询安全库存
	 * lixinling 
	 * 2016年8月17日 下午3:46:33
	 * @param mallCode
	 * @param pCode
	 * @return
	 */
	Object selectSafeQtyByMallAndPCode(Map<String,Object> map);
	
	/**
	 * 根据门店编码和商品编码查询该商品在该门店的信息
	 * lixinling 
	 * 2016年9月12日 下午5:40:29
	 * @param map
	 * @return
	 */
	MallProductStore selectByMallAndPCode(Map<String,Object> map);
}