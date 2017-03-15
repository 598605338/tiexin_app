package com.linjia.web.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.constants.Constants;
import com.linjia.tools.RedisUtil;
import com.linjia.tools.Util;
import com.linjia.web.dao.ActivityInfoMapper;
import com.linjia.web.dao.ProductMapper;
import com.linjia.web.model.ActivityInfo;
import com.linjia.web.model.Product;
import com.linjia.web.model.ProductDetail;
import com.linjia.web.query.FavouriteProductQuery;
import com.linjia.web.query.ProductQuery;
import com.linjia.web.service.MallProductStoreService;
import com.linjia.web.service.ProductService;
import com.linjia.web.service.ProductSpecService;

@Service
@Transactional
public class ProductServiceImpl extends BaseServiceImpl<Product, Long> implements ProductService {

	@Resource
	private ProductMapper mapper;

	@Autowired
	private ProductSpecService productSpecService;

	@Autowired
	private MallProductStoreService mallProductStoreService;

	@Autowired
	private ActivityInfoMapper activityInfoMapper;

	@Override
	public BaseDao<Product, Long> getDao() {
		return mapper;
	}

	@Override
	public List<Product> selectCatagoryProductList(ProductQuery query) {
		String mallCode = query.getMallCode();
		List<Product> list = mapper.selectCatagoryProductList(query);
		if (list != null && list.size() > 0) {
			for (Product item : list) {
				if (getCurrentQty(item.getId(), item.getpCode(), mallCode) > 0) {
					item.setQtyStatus(Constants.QTY_STATUS.NORMAL);
				} else {
					item.setQtyStatus(Constants.QTY_STATUS.LOW);
				}
			}
		}
		return list;
	}

	/*
	 * 根据商品id取得商品当前的库存量
	 */
	@Override
	public int getCurrentQty(long product_id, String p_code, String mallCode) {
		int currentQty = 0;
		int safeQty = 0;

		// ERP库存数
		int storeQty = 0;

		// currentQty = ERP库存数 - 安全库存;
		// 取得ERP库存量
		storeQty = Util.queryQtyByStore(mallCode, p_code);

		// 从redis中取得库存
		// String key = "store_" + mallCode;
		// RedisUtil.hget(key,p_code);

		// 取得安全库存
		safeQty = mallProductStoreService.selectSafeQtyByMallAndPCode(mallCode, p_code);
		currentQty = storeQty - safeQty;
		return currentQty;
	}

	@Override
	public int selectQuantityByPrimaryKey(Long id) {
		return mapper.selectQuantityByPrimaryKey(id);
	}

	@Override
	public ProductDetail selectDetailByPrimaryKey(Long id, String mallCode, String userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("mallCode", mallCode);
		map.put("userId", userId);
		return mapper.selectDetailByPrimaryKey(map);
	}

	@Override
	public int updateQuantityById(Long productId, Integer buyQty) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("productId", productId);
		param.put("buyQty", buyQty);
		return mapper.updateQuantityById(param);
	}

	@Override
	public List<Product> selectFavouriteProductList(FavouriteProductQuery query) {
		List<Product> productList = mapper.selectFavouriteProductList(query);
		if (productList != null && productList.size() > 0) {
			for (Product item : productList) {
				int currentQty = this.getCurrentQty(item.getId(), item.getpCode(), query.getMallCode());
				// 如果库存不足，则不再进行itemPrice的计算
				if (currentQty > 0) {
					item.setQtyStatus(Constants.QTY_STATUS.NORMAL);
				} else {
					item.setQtyStatus(Constants.QTY_STATUS.LOW);
				}
			}
		}
		return productList;
	}

	@Override
	public Product selectByPCode(String pCode) {
		return mapper.selectByPCode(pCode);
	}

	@Override
	public List<Product> handleProductList(List<Product> list, String mallCode) {
		if (list != null && list.size() > 0) {
			// 查询当前针对全部商品的折扣活动，然后计算商品的折扣价
			ActivityInfo activityInfo = activityInfoMapper.selectDiscountActAllProductByNow(mallCode);
			if (activityInfo != null) {
				BigDecimal discount = activityInfo.getDiscount();
				String promotionLabel = activityInfo.getPromotion_label();
				for (Product item : list) {
					if (item.getDiscount() == null || item.getDiscount().doubleValue() == 0.0) {
						item.setSalePrice(Util.mul(item.getMarketPrice(), discount));
						item.setPromotionLabel(promotionLabel);
					}
				}
			}
		}
		return list;
	}

}
