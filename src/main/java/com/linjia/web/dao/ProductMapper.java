package com.linjia.web.dao;

import java.util.List;
import java.util.Map;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.Product;
import com.linjia.web.model.ProductDetail;
import com.linjia.web.query.FavouriteProductQuery;
import com.linjia.web.query.ProductQuery;
import com.linjia.web.query.SearchQuery;

public interface ProductMapper extends BaseDao<Product, Long> {
	
	/**
	 * 根据分类id查询相关分类商品
	 * 
	 * lixinling
	 * 2016年7月13日 下午5:44:53
	 * @param query
	 * @return
	 */
	List<Product> selectCatagoryProductList(ProductQuery query);
	
	
	/**
	 * 根据商品id查询预约购买商品的库存量
	 * lixinling 
	 * 2016年8月15日 下午5:10:19
	 * @param id
	 * @return
	 */
	int selectQuantityByPrimaryKey(Long id);
	
	/**
	 * 搜索操作
	 * lixinling 
	 * 2016年8月15日 下午5:10:19
	 * @param id
	 * @return
	 */
	List<Product> selectProductByKeyword(SearchQuery query);
	
	/**
	 * 根据商品id查询商品的详情
	 * lixinling 
	 * 2016年8月15日 下午5:10:19
	 * @param id
	 * @return
	 */
	ProductDetail selectDetailByPrimaryKey(Map<String,Object> map);
	
	/**
	 * 根据商品id对预约商品进行减库存操作
	 * lixinling 
	 * 2016年8月15日 下午5:10:19
	 * @param id
	 * @return
	 */
	int updateQuantityById(Map<String,Object> param);
	
	/**
	 * 查询商品关注列表
	 * lixinling 
	 * 2016年11月14日 下午5:10:19
	 * @param id
	 * @return
	 */
	List<Product> selectFavouriteProductList(FavouriteProductQuery query);
	
	/**
	 * 根据商品code查询商品信息
	 * lixinling 
	 * 2016年11月14日 下午5:10:19
	 * @param id
	 * @return
	 */
	Product selectByPCode(String pCode);
}