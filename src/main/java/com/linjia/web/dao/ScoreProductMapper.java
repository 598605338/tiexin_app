package com.linjia.web.dao;

import java.util.List;
import java.util.Map;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.ScoreProduct;
import com.linjia.web.query.ScoreProductQuery;


public interface ScoreProductMapper extends BaseDao<ScoreProduct, Long> {
	
	/**
	 * 分页查询积分商城页商品
	 * lixinling 
	 * 2016年8月9日 下午5:25:53
	 * @param query
	 * @return
	 */
	List<ScoreProduct> selectList(ScoreProductQuery query);
	
	/**
	 * 查询我能兑换的积分商品列表
	 * lixinling 
	 * 2016年9月18日 下午8:40:17
	 * @param query
	 * @return
	 */
	List<ScoreProduct> selectCanExchangeList(ScoreProductQuery query);
	
	/**
	 * 查询积分商品相关信息 
	 * lixinling 
	 * 2016年9月18日 下午8:40:17
	 * @param query
	 * @return
	 */
	ScoreProduct selectInfoByPrimaryKey(Long id);
	
	/**
	 * 根据积分商品id查询第三方卡券的链接和券码
	 * lixinling 
	 * 2016年9月18日 下午8:40:17
	 * @param query
	 * @return
	 */
	ScoreProduct selectThirdContentByScoreProductId(Long id);
	
	/**
	 * 更新积分商品剩余数量
	 * lixinling 
	 * 2016年9月18日 下午8:40:17
	 * @param query
	 * @return
	 */
	int updateQtyById(Long id);
	
	/**
	 * 更新券码记录的兑换时间和状态
	 * lixinling 
	 * 2016年9月18日 下午8:40:17
	 * @param query
	 * @return
	 */
	int updateThirdStatusById(Long id);
}