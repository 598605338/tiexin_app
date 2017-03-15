package com.linjia.web.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.linjia.web.dao.ActivityKaiTuanMainMapper;
import com.linjia.web.dao.PinTuanOrderMapper;
import com.linjia.web.model.ActivityKaiTuanMain;
import com.linjia.web.model.PinTuanOrder;
import com.linjia.web.query.PinTuanOrderQuery;
import com.linjia.web.service.PinTuanOrderService;

@Service
@Transactional
public class PinTuanOrderServiceImpl implements PinTuanOrderService {
	
	@Resource
	private PinTuanOrderMapper mapper;
	@Resource
	private  ActivityKaiTuanMainMapper akpMapper;

	@Override
	public boolean insertPtOrder(PinTuanOrder pt) {
		return mapper.insertPtOrder(pt);
	}

	@Override
	public boolean updatePtOrderById(PinTuanOrder pt) {
		boolean flag=false;
//		if(pt!=null&&pt.getStatus()==3){
//			更新拼团表人数
//			ActivityKaiTuanMain ak=new ActivityKaiTuanMain();
//			ak.setId(pt.getPt_id());
//			ak.setMul_num(1);
//			int num=akpMapper.updateOneKaiTuanActivity(ak);
//			if(num>0){
//				flag=true;
//			}
//			if(flag){
			flag=mapper.updatePtOrderById(pt);
//				if(!f){
//					flag=false;
//				}
//			}
//		}
		
		return flag;
	}

	@Override
	public boolean deletePtOrderById(int id) {
		return mapper.deletePtOrderById(id);
	}

	@Override
	public PinTuanOrder selectPtOrderById(Long id) {
		return mapper.selectPtOrderById(id);
	}

	@Override
	public List<PinTuanOrder> selectPtOrderList(PinTuanOrderQuery query) {
		return mapper.selectPtOrderList(query);
	}
	
	@Override
	public List<PinTuanOrder> selectTimeOutOrderUnPay() {
		return mapper.selectTimeOutOrderUnPay();
	}
	
	@Override
	public List<PinTuanOrder> selectPtTimeOutOrder() {
		return mapper.selectPtTimeOutOrder();
	}
	
}
