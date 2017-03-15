package com.linjia.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.linjia.web.dao.MtOrderProductMapper;
import com.linjia.web.model.ThirdOrderProduct;
import com.linjia.web.service.MtOrderProductService;
import com.linjia.web.thirdService.meituan.vo.OrderExtraParam;
import com.linjia.web.thirdService.meituan.vo.OrderFoodDetailParam;

@Service
public class MtOrderProductServiceImpl implements MtOrderProductService{

	@Resource
	private MtOrderProductMapper mapper;
	
	@Override
	public Integer insertMtOrderProduct(OrderFoodDetailParam product) {
		int num=mapper.insertMtOrderProduct(product);
		return num;
	}

	@Override
	public Integer deleteMtOrderProduct(Long orderId) {
		int num=mapper.deleteMtOrderProduct(orderId);
		return num;
	}

	@Override
	public Integer updateMtOrderProduct(OrderFoodDetailParam detail) {
		int num=mapper.updateMtOrderProduct(detail);
		return num;
	}

	@Override
	public List<ThirdOrderProduct> selectMtOrderProduct(Long orderId) {
		List<ThirdOrderProduct> list=mapper.selectMtOrderProduct(orderId);
		return list;
	}

	@Override
	public List<OrderFoodDetailParam> selectMtOrderProductAll(Long order_Id) {
		List<OrderFoodDetailParam> list=mapper.selectMtOrderProductAll(order_Id);
		return list;
	}

	@Override
	public boolean insertMtOrderExtras(OrderExtraParam extras) {
		return mapper.insertMtOrderExtras(extras);
	}

}
