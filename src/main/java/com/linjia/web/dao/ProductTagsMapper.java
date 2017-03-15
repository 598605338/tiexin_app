package com.linjia.web.dao;

import java.util.List;

import com.linjia.base.dao.BaseDao;
import com.linjia.web.model.ProductTags;

public interface ProductTagsMapper extends BaseDao<ProductTags, Long> {
	List<String> selectTagsByProductId(long productId);
}