package com.linjia.web.service;

import java.util.List;
import java.util.Map;

import com.linjia.web.model.PaoDanRecord;
import com.linjia.web.query.PaoDanRecordQuery;

public interface PaoDanRecordService{

	/**
	 * 插入抛单记录
	 * @param paoDanRecord
	 * @return
	 */
    boolean insertPaoDan(PaoDanRecord paoDanRecord);
	
	/**
	 * 删除抛单记录
	 * @param id
	 * @return
	 */
    boolean deletePaoDan(int id);
	
	/**
	 * 删除抛单订单记录
	 * @param id
	 * @return
	 */
    boolean deletePaoDanByOrderId(Long order_id);
	
	/**
	 * 更新抛单记录
	 * @return
	 */
    boolean  updatePaoDan(PaoDanRecord paoDanRecord);
	
	/**
	 * 查询单个抛单记录
	 * @param id
	 * @return
	 */
    PaoDanRecord selectPaoDan(Map<String, Object> map);
	
	/**
	 * 查询多个抛单记录
	 * @return
	 */
    List<PaoDanRecord> selectPaoDanList(PaoDanRecordQuery query);
	
}
