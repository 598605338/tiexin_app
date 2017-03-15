package com.linjia.web.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.linjia.tools.JSONUtil;
import com.linjia.web.dao.OrderGroupMapper;
import com.linjia.web.dao.ThirdOrderMapper;
import com.linjia.web.model.SumOrder;
import com.linjia.web.model.ThirdOrder;
import com.linjia.web.model.ThirdOrderProduct;
import com.linjia.web.query.SumOrderQuery;
import com.linjia.web.query.ThirdLogisOrderQuery;
import com.linjia.web.service.ThirdOrderService;
import com.linjia.web.thirdService.JGpush.constant.PushConstant;
import com.linjia.web.thirdService.JGpush.model.PushModel;
import com.linjia.web.thirdService.JGpush.push.PushClientFactory;

/**
 * 订单操作服务类
 * @author xiangshouyi
 *
 */
@Service
public class ThirdOrderServiceImpl implements ThirdOrderService{

	@Resource
	private ThirdOrderMapper mapper;
	
	@Resource
	private OrderGroupMapper ljmapper;

	@Override
	public List<ThirdOrder> selectOrderlist(ThirdLogisOrderQuery query) {
		List<ThirdOrder> list=null;
		try{
			list=mapper.selectOrderlist(query);
		}catch(Exception e){
			 e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public List<ThirdOrder> selectWarnOrderlist(ThirdLogisOrderQuery query) {
		List<ThirdOrder> list=null;
		try{
			list=mapper.selectWarnOrderlist(query);
		}catch(Exception e){
			 e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public List<ThirdOrder> selectLinJOrderlist(ThirdLogisOrderQuery query) {
		List<ThirdOrder> list=mapper.selectLinJOrderlist(query);
		return list;
	}

	@Override
	public ThirdOrder selectMyOrder(String shopId,Long orderId) {
		ThirdLogisOrderQuery query=new ThirdLogisOrderQuery();
		query.setOrder_id(orderId);
		query.setMall_code(shopId);
		ThirdOrder tirdOrder=mapper.selectMyOrder(query);
		return tirdOrder;
	}

	@Override
	public ThirdOrder selectBdOrder(String shopId,Long orderId) {
		ThirdLogisOrderQuery query=new ThirdLogisOrderQuery();
		query.setOrder_id(orderId);
		query.setShop_id(shopId);
		ThirdOrder tirdOrder=mapper.selectBdOrder(query);
		return tirdOrder;
	}

	@Override
	public ThirdOrder selectJdOrder(String shopId, Long orderId) {
		ThirdLogisOrderQuery query=new ThirdLogisOrderQuery();
		query.setOrder_id(orderId);
		query.setShop_id(shopId);
		ThirdOrder tirdOrder=mapper.selectJdOrder(query);
		return tirdOrder;
	}

	@Override
	public ThirdOrder selectMtOrder(String shopId,Long orderId) {
		ThirdLogisOrderQuery query=new ThirdLogisOrderQuery();
		query.setOrder_id(orderId);
		query.setApp_poi_code(shopId);
		ThirdOrder tirdOrder=mapper.selectMtOrder(query);
		return tirdOrder;
	}

	@Override
	public List<ThirdOrderProduct> selectMyOrderDetail(Long order_id) {
		List<ThirdOrderProduct> list=mapper.selectMyOrderDetail(order_id);
		return list;
	}

	@Override
	public int updateLinjOrderStatus(Map<String, Object> map) {
		int num=ljmapper.updateStatusById(map);
		return num;
	}

	@Override
	public List<ThirdOrder> selectMtOrderlist(ThirdLogisOrderQuery query) {
		List<ThirdOrder> list=mapper.selectMtOrderlist(query);
		return list;
	}

	@Override
	public List<ThirdOrder> selectBdOrderlist(ThirdLogisOrderQuery query) {
		List<ThirdOrder> list=mapper.selectBdOrderlist(query);
		return list;
	}

	@Override
	public List<ThirdOrder> selectJdOrderlist(ThirdLogisOrderQuery query) {
		List<ThirdOrder> list=mapper.selectJdOrderlist(query);
		return list;
	}

	@Override
	public List<SumOrder> selectSumLinjia(SumOrderQuery query) {
		List<SumOrder> sumOrderList=mapper.selectSumLinjia(query);
		return sumOrderList;
	}

	@Override
	public List<SumOrder> selectSumMeiTuan(SumOrderQuery query) {
		List<SumOrder> sumOrderList=mapper.selectSumMeiTuan(query);
		return sumOrderList;
	}

	@Override
	public List<SumOrder> selectSumBaidu(SumOrderQuery query) {
		List<SumOrder> sumOrderList=mapper.selectSumBaidu(query);
		return sumOrderList;
	}

	@Override
	public List<SumOrder> selectSumJingdong(SumOrderQuery query) {
		List<SumOrder> sumOrderList=mapper.selectSumJingdong(query);
		return sumOrderList;
	}

	@Override
	public List<SumOrder> selectSumAll(ThirdLogisOrderQuery query) {
		List<SumOrder> sumOrderList=mapper.selectSumAll(query);
		return sumOrderList;
	}

	@Override
	public SumOrder sumCurDayData(ThirdLogisOrderQuery query) {
		SumOrder sumOrder=mapper.sumCurDayData(query);
		return sumOrder;
	}

	@Override
	public Integer selectLjReserveOrder() {
		int num=0;
		List<String> list=mapper.selectLjReserveOrder();
		if(list!=null&&list.size()>0){
			PushModel model =new PushModel();
			String type="2";
	        model.setType(type);
	        model.setOrderSource("1");
	        model.setMessage(PushConstant.getMessage(type));
	        String strPush = JSONUtil.toJson(model);
	        System.out.println(strPush.getBytes().length);
	        System.out.println(model);
	        if(list!=null&&list.size()>0){
	        	num=PushClientFactory.push(list,model,false);
	        }
		}
		return num;
	}

	@Override
	public int selectMtOrderNum(Long orderId) {
		return mapper.selectMtOrderNum(orderId);
	}

	@Override
	public List<ThirdOrder> selectLJpdlist(ThirdLogisOrderQuery query) {
		return mapper.selectLJpdlist(query);
	}

	@Override
	public List<ThirdOrder> selectElemeOrder(ThirdLogisOrderQuery query) {
		return mapper.selectElemeOrder(query);
	}

	@Override
	public List<ThirdOrder> selectElemeSumlist(ThirdLogisOrderQuery query) {
		return mapper.selectElemeSumlist(query);
	}

	@Override
	public int selectJdReserveOrder() {
		int num = 0;
		List<String> list = mapper.selectJdReserveOrder();
		if(list != null && list.size() > 0){
			PushModel model =new PushModel();
			String type="2";
	        model.setType(type);
	        model.setOrderSource("4");
	        model.setMessage(PushConstant.getMessage(type));
	        if(list!=null&&list.size()>0){
	        	num=PushClientFactory.push(list,model,false);
	        }
		}
		return num;
	}

}

