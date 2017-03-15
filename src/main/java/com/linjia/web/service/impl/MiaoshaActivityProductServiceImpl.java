package com.linjia.web.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.constants.Constants;
import com.linjia.web.dao.MiaoshaActivityProductMapper;
import com.linjia.web.model.MiaoshaActivityProduct;
import com.linjia.web.query.MiaoshaActivityProductQuery;
import com.linjia.web.service.MiaoshaActivityProductService;
import com.linjia.web.service.ProductService;

@Service
public class MiaoshaActivityProductServiceImpl extends BaseServiceImpl<MiaoshaActivityProduct, Long> implements MiaoshaActivityProductService {
	
	@Resource
	private MiaoshaActivityProductMapper mapper;
	
	@Autowired
	private ProductService productService;
	
	@Override
	public BaseDao<MiaoshaActivityProduct, Long> getDao() {
		return mapper;
	}

	@Override
	public List<MiaoshaActivityProduct> selectAllByBaseId(MiaoshaActivityProductQuery query) {
		List<MiaoshaActivityProduct> list = mapper.selectAllByBaseId(query);
		if(list != null && list.size() > 0){
			int storeQty = 0;
			for(MiaoshaActivityProduct item: list){
				//秒杀活动库存数
				Integer quantity = item.getQuantity();
				// 取得erp库存数
				storeQty = productService.getCurrentQty(item.getProductId(), item.getpCode(), query.getMallCode());
				if(storeQty <= 0 || quantity <= 0){
					item.setPanicStatus(Constants.QTY_STATUS.LOW);
				}
			}
		}
		
		return list;
	}

	@Override
	public MiaoshaActivityProduct getPanicingProductByProductId(long product_id) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("productId", product_id);
		map.put("currentDate", new Date());
		return mapper.getPanicingProductByProductId(map);
	}

	@Override
	public int updateQuantityById(int miaoshaoQty, int miaoshaoActiveProductId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("miaoshaoQty", miaoshaoQty);
		map.put("miaoshaoActiveProductId", miaoshaoActiveProductId);
		return mapper.updateQuantityById(map);
	}

}
