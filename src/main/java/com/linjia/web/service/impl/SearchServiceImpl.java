package com.linjia.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.constants.Constants;
import com.linjia.web.dao.HistoryKeywordMapper;
import com.linjia.web.dao.HotKeywordMapper;
import com.linjia.web.dao.ProductMapper;
import com.linjia.web.model.HistoryKeyword;
import com.linjia.web.model.HotKeyword;
import com.linjia.web.model.Product;
import com.linjia.web.query.SearchQuery;
import com.linjia.web.service.ProductService;
import com.linjia.web.service.ProductTagsService;
import com.linjia.web.service.SearchService;

@Service
@Transactional
public class SearchServiceImpl extends BaseServiceImpl<Object, Long> implements SearchService {
	
	@Resource
	private HistoryKeywordMapper historyKeywordMapper;
	
	@Resource
	private HotKeywordMapper hotKeywordMapper;
	
	@Resource
	private ProductMapper productMapper;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductTagsService productTagsService;
	
	@Override
	public BaseDao<Object, Long> getDao() {
		return null;
	}
	
	
	@Override
	public List<HistoryKeyword> selectHistoryByUserId(String userId) {
		return historyKeywordMapper.selectByUserId(userId);
	}


	@Override
	public List<HotKeyword> selectHotKeyword() {
		return hotKeywordMapper.selectAll();
	}


	@Override
	public List<Product> search(SearchQuery query) {
		List<Product> list = productMapper.selectProductByKeyword(query);
		
		if (list != null && list.size() > 0) {
			for (Product item : list) {
				
				//判断商品库存
				int qty = productService.getCurrentQty(item.getId(), item.getpCode(), query.getMallCode());
				if (qty > 0) {
					item.setQuantity(qty);
					item.setQtyStatus(Constants.QTY_STATUS.NORMAL);
				} else {
					item.setQtyStatus(Constants.QTY_STATUS.LOW);
				}
				
				//添加商品标签列表
				List<String> tagList = productTagsService.selectTagsByProductId(item.getId());
				item.setTagList(tagList);
			}
		}
		
		return list;
	}


}
