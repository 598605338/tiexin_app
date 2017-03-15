package com.linjia.web.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.tools.DateComFunc;
import com.linjia.web.dao.ActivityKaiTuanMainMapper;
import com.linjia.web.dao.ActivityPintuanBaseMapper;
import com.linjia.web.dao.PinTuanOrderMapper;
import com.linjia.web.model.ActivityKaiTuanMain;
import com.linjia.web.model.ActivityPintuanBase;
import com.linjia.web.model.PinTuanOrder;
import com.linjia.web.query.ActivityKaiTuanMainQuery;
import com.linjia.web.service.ActivityKaiTuanMainService;

@Service
@Transactional
public class ActivityKaiTuanMainServiceImpl extends BaseServiceImpl<ActivityKaiTuanMain, Long> implements ActivityKaiTuanMainService {
	
	@Resource
	private ActivityKaiTuanMainMapper mapper;
	@Resource
	private PinTuanOrderMapper ptmapper; //拼图订单取消
	@Resource
	private ActivityPintuanBaseMapper apbmapper;//拼团商品信息

	@Override
	public int insertOneKaiTuanActivity(ActivityKaiTuanMain model) {
		return mapper.insertOneKaiTuanActivity(model);
	}

	@Override
	public int updateOneKaiTuanActivity(ActivityKaiTuanMain query) {
		return mapper.updateOneKaiTuanActivity(query);
	}

	@Override
	public List<ActivityKaiTuanMain> selectUserAllKaiTuan(
			ActivityKaiTuanMainQuery model) {
		return mapper.selectUserAllKaiTuan(model);
	}
	
	@Override
	public ActivityKaiTuanMain selectKaiTuanActivity(
			ActivityKaiTuanMainQuery query) {
		return mapper.selectKaiTuanActivity(query);
	}

	@Override
	public BaseDao<ActivityKaiTuanMain, Long> getDao() {
		return null;
	}
	
	public int selectSumSalesNum(int baseId){
		return mapper.selectSumSalesNum(baseId);
	}

	@Override
	public List<ActivityKaiTuanMain> selectOrderList() {
		return mapper.selectOrderList();
	}

	@Override
	public Map<String,Object> ptBeforePayed(Long OrderId) {
		Map<String,Object> map=new HashMap<String, Object>();
		try{
			PinTuanOrder pt=ptmapper.selectPtOrderById(OrderId);
			ActivityPintuanBase	baseInfo=null;
			if(pt==null){
				map.put("status", "fail");
				map.put("message", "未查询到订单信息");
				return map;
			}else{
				Integer buyPersonNum=pt.getBuyPersonNum();
				//查询团购商品信息
				Long baseId=pt.getPt_base_id();
				if(baseId!=null&&baseId>0){
					//查询团购商品信息
					baseInfo = apbmapper.selectDetailById(baseId.intValue());
					if(baseInfo==null){
						map.put("status", "fail");
						map.put("message", "未查询到团购商品信息");
						return map;
					}
					//当前商品库存
					int kuCun=baseInfo.getQuantity();
					if(kuCun<1){
						map.put("status", "fail");
						map.put("message", "对不起，库存不够,补货中！");	
						return map;
					}
					//当前参团商品结束时间
					Date endDate=baseInfo.getEndTime();
					//获取当前时间
					Date curTime=new Date();
					long timeDiff=DateComFunc.dateDiff(endDate,curTime);
					if(timeDiff<30000){
						map.put("status", "fail");
						map.put("message", "对不起，该商品团购时间已结束!");	
						return map;
					}
				}else{
					map.put("status", "fail");
					map.put("message", "无团购商品信息");
					return map;
				}
				
				Long address_id=pt.getAddress_id();
				if(address_id==null){
					map.put("status", "fail");
					map.put("message", "订单无地址信息!");
					return map;
				}
				if(buyPersonNum!=null&&buyPersonNum>1){
					//查询团信息
					ActivityKaiTuanMainQuery qtm=new ActivityKaiTuanMainQuery();
					qtm.setId(pt.getPt_id());
					ActivityKaiTuanMain st=this.selectKaiTuanActivity(qtm);
					if(st==null){
						map.put("status", "fail");
						map.put("message", "未查询到组团信息");
						return map;
					}
					int curNum=st.getCur_num();
					int limitNum=baseInfo.getUpperLimit();
					if(curNum+1>limitNum){
						map.put("status", "fail");
						map.put("message", "对不起,本次团购已达人数上限!");
						return map;
					}
				}
				map.put("status", "ok");
				map.put("message", "可以组团支付!");
				return map;
			}
		}catch(Exception e){
			map.put("status", "error");
			map.put("message", "组团异常!");
			e.printStackTrace();
			return map;
		}
	}

	public boolean ptAfterPayed(Long orderId,int pay_type) {
		boolean flag=false;
		try{
			//查询团订单信息
			PinTuanOrder pt=ptmapper.selectPtOrderById(orderId);
			Integer buyPersonNum=null;
			if(pt!=null){
				buyPersonNum= pt.getBuyPersonNum();
			}else{
				return false;
			}
			//查询团商品信息
			int base_id=pt.getPt_base_id().intValue();
			ActivityPintuanBase baseInfo = apbmapper.selectDetailById(base_id);
			int ptv=0;
			
			ActivityKaiTuanMain atm=new ActivityKaiTuanMain();
			if(buyPersonNum!=null&&buyPersonNum>1){
				//查询团信息
				ActivityKaiTuanMainQuery qtm=new ActivityKaiTuanMainQuery();
				qtm.setId(pt.getPt_id());
				ActivityKaiTuanMain st=this.selectKaiTuanActivity(qtm);
				
				//更新团状态和人数
				//提交订单
				atm.setId(pt.getPt_id());
				atm.setAdd_num(1);
				if(st.getCur_num()+1>=baseInfo.getLowerLimit()){
					atm.setState(1);
				}
				if(st.getCur_num()>=baseInfo.getUpperLimit()){
					return flag;
				}
				atm.setBaseId(base_id);
				//更新团状态
				ptv=this.updateOneKaiTuanActivity(atm);
			}else{
				ptv=1;
			}
			//更新订单状态
			if(ptv>0){
				PinTuanOrder spt=new PinTuanOrder();
				spt.setOrder_id(orderId);
				spt.setPay_status(1);
				if(buyPersonNum!=null&&buyPersonNum==1){
					spt.setPt_status(2);
				}else{
					spt.setPt_status(1);
				}
				spt.setPay_time(new Date());
				spt.setPay_type(pay_type);
				//更新单个订单状态
				boolean bopr=ptmapper.updatePtOrderById(spt);
				if(!bopr){
					return flag;
				}
			}else{
				return flag;
			}
			
			ActivityPintuanBase base=new ActivityPintuanBase();
			base.setId(pt.getPt_base_id());
			base.setBaseQuantyMul(1);
			//更新拼团商品库存
			boolean bu=apbmapper.updateByBaseId(base);
			if(!bu){
				return flag;
			}
			//单人购买返回
			if(buyPersonNum!=null&&buyPersonNum==1){
				flag=true;
				return flag;
			}
			
			//多人购买,如果成团，更新该团订单拼团状态
			if(ptv>0&&atm!=null&&buyPersonNum==2&&atm.getState()==1){
				PinTuanOrder upbatch=new PinTuanOrder();
				upbatch.setPt_status(atm.getState());
				upbatch.setPt_id(atm.getId());
				upbatch.setOrder_success_time(new Date());
				upbatch.setPt_base_id(Long.valueOf(atm.getBaseId()));
				//待发货
				upbatch.setStatus(0);
				boolean b=ptmapper.updatePtOrderByPtId(upbatch);
				if(b){
					flag=true;
					return flag;
				}else{
					return flag;
				}
			}else{
				return flag;
			}
		}catch(Exception e){
			e.printStackTrace();
			return flag;
		}
	}
}
