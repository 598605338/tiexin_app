package com.linjia.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.linjia.web.dao.BdOrderProductMapper;
import com.linjia.web.model.ThirdOrderProduct;
import com.linjia.web.service.BdOrderProductService;
import com.linjia.web.thirdService.baidu.model.Product;

@Service
public class BdOrderProductServiceImpl implements BdOrderProductService{

	@Resource
	private BdOrderProductMapper mapper;
	
	@Override
	public Integer insertBdOrderProduct(Product product) {
		int num=mapper.insertBdOrderProduct(product);
		return num;
	}

	@Override
	public Integer deleteBdOrderProduct(String id) {
		int num=mapper.deleteBdOrderProduct(id);
		return num;
	}

	@Override
	public Integer updateBdOrderProduct(Product product) {
		int num=mapper.updateBdOrderProduct(product);
		return num;
	}

	@Override
	public List<Product> selectBdOrderProductAll(Long orderId) {
		List<Product> list=mapper.selectBdOrderProductAll(orderId);
		return list;
	}

	@Override
	public List<ThirdOrderProduct> selectBdOrderProduct(Long orderId) {
		List<ThirdOrderProduct> list=mapper.selectBdOrderProduct(orderId);
		return list;
	}

}
