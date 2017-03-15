package com.linjia.web.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.linjia.base.controller.BaseController;
import com.linjia.constants.Constants;
import com.linjia.tools.Util;
import com.linjia.web.dao.AddressMapper;
import com.linjia.web.dao.ProductMapper;
import com.linjia.web.model.ActivityKaiTuanMain;
import com.linjia.web.model.ActivityPintuanBase;
import com.linjia.web.model.Address;
import com.linjia.web.model.OrderGroup;
import com.linjia.web.model.OrderRefund;
import com.linjia.web.model.PinTuanOrder;
import com.linjia.web.model.Product;
import com.linjia.web.query.PinTuanOrderQuery;
import com.linjia.web.service.ActivityKaiTuanMainService;
import com.linjia.web.service.ActivityPintuanBaseService;
import com.linjia.web.service.OrderRefundService;
import com.linjia.web.service.OrderidGenerateService;
import com.linjia.web.service.PinTuanOrderService;

/**
 *拼团订单控制器
 * @author xiangsy
 *
 */
@Controller
@RequestMapping("/teamBuy")
public class PintuanController extends BaseController{
	
	@Autowired
	private PinTuanOrderService pinTuanOrderService;
	@Autowired
	private OrderRefundService orderRefundService;
	@Autowired
	private OrderidGenerateService orderidGenerateService;
	@Autowired
	private ActivityKaiTuanMainService activityKaiTuanMainService;
	@Autowired
	private ActivityPintuanBaseService activityPintuanBaseService;
	@Autowired
	private AddressMapper addressMapper;
	@Autowired
	private ProductMapper productMapper;
	
	/**
	 * 修改订单信息(取消订单)
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/cancelPtOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public Object cancelPtOrder(HttpServletRequest request,@RequestParam Long orderId,@RequestParam String cancelReason){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			if(orderId==null){
				map.put("status", "fail");
				map.put("message", "对不起,订单id未获取到");
				return map;
			}
			PinTuanOrder pt=pinTuanOrderService.selectPtOrderById(orderId);
			if(pt==null){
				map.put("status", "fail");
				map.put("message", "对不起,未查询到订单信息");
				return map;
			}
			if(pt.getPt_status()==2){
				map.put("status", "fail");
				map.put("message", "对不起,已成团不能取消");
				return map;
			}
			
			if(pt.getStatus()==3){
				map.put("status", "fail");
				map.put("message", "订单已取消");
				return map;
			}
			
			PinTuanOrder cancelpt=new PinTuanOrder();
			cancelpt.setCancel_time(new Date());
			cancelpt.setStatus(3);
			cancelpt.setCancel_reason(cancelReason);
			cancelpt.setPt_id(pt.getPt_id());
			cancelpt.setOrder_id(orderId);
			boolean flag=pinTuanOrderService.updatePtOrderById(cancelpt);
			if(pt.getPay_status()==1&&pt.getPay_time()!=null){
				//更新团信息
				ActivityKaiTuanMain atm=new ActivityKaiTuanMain();
				atm.setMul_num(1);
				atm.setId(pt.getPt_id());
				activityKaiTuanMainService.updateOneKaiTuanActivity(atm);
				//更新商品库存等信息
				ActivityPintuanBase apb=new ActivityPintuanBase();
				apb.setId(pt.getPt_base_id());
				apb.setBaseQuantyAdd(1);
				activityPintuanBaseService.updateByBaseId(apb);
			}
			Long refundId =0L;
			if(flag){
				//退款操作
				if(pt.getPay_status()==1&&pt.getPay_time()!=null){
					Map<String, Object> cmap = new HashMap<String,Object>();
					map.put("orderGroupId", pt.getOrder_id());
					OrderRefund re=orderRefundService.selectOneOrder(cmap);
					int num=0;
					if(re==null){
						//创建退款单
						OrderRefund reforder=new OrderRefund();
						refundId = orderidGenerateService.generateRefundId();
						reforder.setId(refundId);
						reforder.setRefundAmount(new BigDecimal(pt.getReal_price()+""));
						reforder.setRemark("客户发起退款");
						reforder.setCancelReason("客户取消订单");
						reforder.setConfirmTime(new Date());
						reforder.setConfirmor("system");
						reforder.setOrderGroupId(pt.getOrder_id());
						reforder.setRefundType(1);
						reforder.setRefundOnlinepayStatus(1);
						reforder.setConfirmTime(new Date());
						reforder.setCreateTime(new Date());
						reforder.setRefundStatus(2);
						if(pt.getPay_type()!=null){
							if (pt.getPay_type() == Constants.PAY_TYPE.WX) {
								reforder.setPayTypeId(0);
								reforder.setPayTypeName("微信支付");
							} else {
								reforder.setReturnBalance(reforder.getRefundAmount());
								reforder.setPayTypeId(1);
								reforder.setPayTypeName("钱包支付");
							}
						}
						num=orderRefundService.insert(reforder);
						
						if(pt.getPay_type()==null){
							map.put("status", "fail");
							map.put("message", "订单支付类型获取异常!");
							return map;
						}
					}else{
						map.put("status", "fail");
						map.put("message", "退款单已存在!");
						return map;
					}
					
					if(num<=0){
						map.put("message", "退款失败！");
						map.put("status", "fail");
						return map;
					}else{
						// 生成退款单并执行退款操作
						OrderGroup orderGroup = new OrderGroup();
						orderGroup.setRealPrice(pt.getReal_price());
						orderGroup.setPayType(pt.getPay_type());
						Map<String, Object> refundmap = orderRefundService.insertRefund(orderGroup, pt.getUser_id(), 1, 0, refundId);
						if (refundmap == null) {
							logger.error("生成退款单错误");
							map.put("message", "生成退款单错误");
							Util.writeError(map);
							return map;
						} else if ((int) refundmap.get("return_code") == 1) {
							map.put("message", "【微信退款申请失败】退款请求错误，返回值为空");
							Util.writeError(map);
							return map;
						} else if ((int) refundmap.get("return_code") == 2) {
							map.put("message", "【微信退款申请失败】退款API系统返回失败，请检测Post给API的数据是否规范合法");
							Util.writeError(map);
							return map;
						} else if ((int) refundmap.get("return_code") == 3) {
							map.put("message", "【微信退款申请失败】退款请求API返回的数据签名验证失败，有可能数据被篡改了");
							Util.writeError(map);
							return map;
						} else if ((int) refundmap.get("return_code") == 4) {
							map.put("message", "调用微信退款申请发生错误");
							Util.writeError(map);
							return map;
						} else if ((int) refundmap.get("return_code") == 5) {
							map.put("message", "退款成功");
							Util.writeOk(map);
							return map;
						}
					}
					Util.writeOk(map);
					Util.writeSuccess(map);
					return map;
				}
			}else{
				map.put("status", "fail");
				map.put("message", "对不起,取消失败!");
				return map;
			}
		}catch(Exception e){
			Util.writeError(map);
			Util.writeFail(map);
			e.printStackTrace();
		}
		return map;
	}
	

	/**
	 * 根据订单id查询订单信息
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/selectOrderById",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public Object selectOrderById(HttpServletRequest request,@RequestParam Long orderId){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			PinTuanOrder pt=pinTuanOrderService.selectPtOrderById(orderId);
			Address address=null;
			Product product=null;
			if(pt!=null){
				Long address_id=pt.getAddress_id();
				if(address_id!=null){
					address=addressMapper.selectByPrimaryKey(address_id);
				}else{
					map.put("message", "未获取到地址信息");
				}
				String p_code=pt.getP_code();
				if(p_code!=null&&(!p_code.isEmpty())){
					product=productMapper.selectByPCode(p_code);
				}else{
					map.put("message", "未获取到商品信息");
				}
			}
			map.put("address", address);
			map.put("product", product);
			map.put("pinTuanOrder",pt);
			Util.writeOk(map);
			Util.writeSuccess(map);
		}catch(Exception e){
			Util.writeError(map);
			Util.writeFail(map);
			e.printStackTrace();
		}
		return map;
	}

    /**
	 * 根据用户id查询拼团订单
	 * @param request
	 * @param mall_codes
	 * @return
	 */
	@RequestMapping("/selectPtOrders")
	@ResponseBody
	public Object selectPtOrders(HttpServletRequest request,PinTuanOrderQuery query){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			if(query.getUser_id()==null || query.getUser_id().isEmpty()){
				map.put("status", "fail");
				map.put("message", "对不起,未获取到用户信息!");
				return map;
			}
			List<PinTuanOrder> list=pinTuanOrderService.selectPtOrderList(query);
			map.put("ptlist", list);
			Util.writeOk(map);
			Util.writeSuccess(map);
		}catch(Exception e){
			Util.writeError(map);
			Util.writeFail(map);
			e.printStackTrace();
		}
		return map;
	}
	
}
