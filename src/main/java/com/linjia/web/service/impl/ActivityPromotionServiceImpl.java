package com.linjia.web.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.constants.Constants;
import com.linjia.web.dao.ActivityInfoMapper;
import com.linjia.web.dao.ActivityProductMapper;
import com.linjia.web.dao.ActivityTradeProductMapper;
import com.linjia.web.model.ActivityInfo;
import com.linjia.web.model.ActivityProduct;
import com.linjia.web.model.ActivityTradeProduct;
import com.linjia.web.model.Product;
import com.linjia.web.service.ActivityPromotionService;
import com.linjia.web.service.ProductService;

@Service
@Transactional
public class ActivityPromotionServiceImpl  implements  ActivityPromotionService {
	
	@Resource
	private ActivityInfoMapper activityInfoMapper;
	
	@Resource
	private ActivityProductMapper activityProductMapper;
	
	@Resource
	private ActivityTradeProductMapper activityTradeProductMapper;
	
	@Autowired
	private ProductService productService;

	@Override
	public boolean insertActInfo(ActivityInfo info) {
		info.setCreate_time(new Date());
		return activityInfoMapper.insertActInfo(info);
	}

	@Override
	public boolean updateActInfoById(ActivityInfo info) {
		info.setUpdate_time(new Date());
		return activityInfoMapper.updateActInfoById(info);
	}

	@Override
	public boolean deleteActInfoById(int activity_id) {
		 boolean f=activityInfoMapper.deleteActInfoById(activity_id);
		 if(f){
			 //查询是否有活动商品
			 int nump=activityProductMapper.selectActProNum(activity_id);
			 boolean am=false;
			 if(nump>0){
				 am=activityProductMapper.deleteActProductByActId(activity_id);
			 }else{
				 am=true;
			 }
			 
			 if(!am){
				 f=false; 
			 }else{
				 //查询是否有兑换商品
				 int num=activityTradeProductMapper.selectActTraNum(activity_id);
				 if(num>0){
					 boolean fpm=activityTradeProductMapper.deleteActTradeProByActId(activity_id);
					 if(!fpm){
						 f=false;  
					 }
				 }
			 }
		 }
		 return f;
	}

	@Override
	public ActivityInfo selectActInfoById(int activity_id) {
		return activityInfoMapper.selectActInfoById(activity_id);
	}

	@Override
	public List<ActivityInfo> selectActInfoAll(Map<String, Object> map) {
		return activityInfoMapper.selectActInfoAll(map);
	}

	@Override
	public boolean insertActProduct(ActivityProduct info) {
		info.setCreate_time(new Date());
		return activityProductMapper.insertActProduct(info);
	}

	@Override
	public boolean updateActProductById(ActivityProduct info) {
		info.setUpdate_time(new Date());
		return activityProductMapper.updateActProductById(info);
	}

	@Override
	public boolean deleteActProductById(int id) {
		return activityProductMapper.deleteActProductById(id);
	}

	@Override
	public ActivityProduct selectActProductById(int id) {
		return activityProductMapper.selectActProductById(id);
	}

	@Override
	public List<ActivityProduct> selectActProductAll(Map<String, Object> map) {
		return activityProductMapper.selectActProductAll(map);
	}

	@Override
	public boolean insertActTradePro(ActivityTradeProduct info) {
		info.setCreate_time(new Date());
		return activityTradeProductMapper.insertActTradePro(info);
	}

	@Override
	public boolean updateActTradeProById(ActivityTradeProduct info) {
		info.setUpdate_time(new Date());
		return activityTradeProductMapper.updateActTradeProById(info);
	}

	@Override
	public boolean deleteActTradeProById(int id) {
		return activityTradeProductMapper.deleteActTradeProById(id);
	}

	@Override
	public ActivityTradeProduct selectActTradeProById(int id) {
		return activityTradeProductMapper.selectActTradeProById(id);
	}

	@Override
	public List<ActivityTradeProduct> selectActTradeProAll(
			Map<String, Object> map) {
		String mallCode = (String)map.get("mallCode");
		List<ActivityTradeProduct> list = activityTradeProductMapper.selectActTradeProAll(map);
		if (list != null && list.size() > 0) {
			for (ActivityTradeProduct item : list) {
				if (productService.getCurrentQty(item.getId(), item.getP_code(), mallCode) > 0) {
					item.setQtyStatus(Constants.QTY_STATUS.NORMAL);
				} else {
					item.setQtyStatus(Constants.QTY_STATUS.LOW);
				}
			}
		}
		return list;
	}

	@Override
	public List<ActivityInfo> selectActInfoByCurrTime(Long productId,String mallCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("currentTime", new Date());
		map.put("productId", productId);
		map.put("mallCode", mallCode);
		return activityInfoMapper.selectActInfoByCurrTime(map);
	}

	@Override
	public List<ActivityInfo> selectOrderActInfoByCurrTime(Map<String, Object> map) {
		return activityInfoMapper.selectOrderActInfoByCurrTime(map);
	}

}
