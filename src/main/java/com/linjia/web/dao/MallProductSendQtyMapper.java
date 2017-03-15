package com.linjia.web.dao;


import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.MallProductSendQty;

public interface MallProductSendQtyMapper extends BaseDao<MallProductSendQty, Long> {
	
	/**
	 * 插入或更新库存数据
	 * lixinling 
	 * 2016年11月10日 下午7:57:25
	 * @param model
	 * @return
	 */
	int insertOrUpdate(MallProductSendQty model);
}