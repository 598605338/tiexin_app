package com.linjia.web.service;


import java.util.List;
import java.util.Map;

import com.linjia.base.service.BaseService;
import com.linjia.web.model.Product;
import com.linjia.web.model.ProductDetail;
import com.linjia.web.query.FavouriteProductQuery;
import com.linjia.web.query.ProductQuery;

public interface ProductService extends BaseService<Product, Long>{
	
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
	 * 根据商品id,商品code,门店id,取得商品的当前库存
	 * 
	 * @param request
	 * @return
	 */
	int getCurrentQty(long product_id, String p_code, String mallCode);
	
	/**
	 * 根据商品id查询预约购买商品的库存量
	 * lixinling 
	 * 2016年8月15日 下午5:10:19
	 * @param id
	 * @return
	 */
	int selectQuantityByPrimaryKey(Long id);

	/**
	 * 根据商品id查询商品的详情
	 * lixinling 
	 * 2016年8月15日 下午5:10:19
	 * @param id
	 * @return
	 */
	ProductDetail selectDetailByPrimaryKey(Long id, String mallCode, String userId);
	
	/**
	 * 根据商品id对预约商品进行减库存操作
	 * lixinling 
	 * 2016年8月15日 下午5:10:19
	 * @param id
	 * @return
	 */
	int updateQuantityById(Long productId, Integer buyQty);

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
	
	/**
	 * 处理查询的商品列表
	 * lixinling 
	 * 2016年12月14日 下午5:10:19
	 * @param id
	 * @return
	 */
	List<Product> handleProductList(List<Product> list, String mallCode);
}
