package com.linjia.web.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import o2o.openplatform.sdk.util.HttpUtil;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.constants.Application;
import com.linjia.tools.DateComFunc;
import com.linjia.tools.HttpRequestUtils;
import com.linjia.tools.JSONUtil;
import com.linjia.tools.Tools;
import com.linjia.web.dao.JddjDeliveryStatusMapper;
import com.linjia.web.dao.JddjOrderDiscountMapper;
import com.linjia.web.dao.JddjOrderMapper;
import com.linjia.web.dao.JddjOrderProductMapper;
import com.linjia.web.model.OrderGroup;
import com.linjia.web.model.OrderGroupProduct;
import com.linjia.web.model.PaoDanRecord;
import com.linjia.web.model.ThirdOrderProduct;
import com.linjia.web.service.PaoDanRecordService;
import com.linjia.web.service.ThirdJDdjService;
import com.linjia.web.thirdService.JGpush.service.JgPushDeviceService;
import com.linjia.web.thirdService.jddj.config.JDDJConfigure;
import com.linjia.web.thirdService.jddj.config.JDDJUtil;
import com.linjia.web.thirdService.jddj.model.AdjustResult;
import com.linjia.web.thirdService.jddj.model.DJOrderQuery;
import com.linjia.web.thirdService.jddj.model.JddjDeliveryStatus;
import com.linjia.web.thirdService.jddj.model.OrderDiscountDTO;
import com.linjia.web.thirdService.jddj.model.OrderInfoDTO;
import com.linjia.web.thirdService.jddj.model.OrderProductDTO;
import com.linjia.web.uhd123.service.UhdOrderService;

@Service
@Transactional
public class ThirdJDdjServiceImpl extends BaseServiceImpl<OrderInfoDTO, Long> implements ThirdJDdjService {

	@Resource
	private JddjOrderMapper jddjOrderMapper;
	@Resource
	private JddjOrderProductMapper jddjOrderProductMapper;
	@Resource
	private JddjOrderDiscountMapper jddjOrderDiscountMapper;
	@Resource
	private JddjDeliveryStatusMapper jddjDeliveryStatusMapper;
	@Autowired
	private UhdOrderService uhdOrderService;
	@Autowired
	private PaoDanRecordService paoDanRecordService;
	@Autowired
	private JgPushDeviceService jgPushDeviceService;

	@Override
	public BaseDao<OrderInfoDTO, Long> getDao() {
		return jddjOrderMapper;
	}

	@Override
	public List<OrderInfoDTO> orderQuery(DJOrderQuery query) {
		String jd_param_json = JSONObject.toJSONString(query);
		Map<String, Object> param = this.commonParam(jd_param_json);

		logger.debug("JDDJ订单列表查询接口:::::::::::::::::param:" + param.toString());
		String result = HttpRequestUtils.sendSimplePostRequest(JDDJConfigure.getBaseURI() + JDDJConfigure.orderQuery, param);
		// result = result.replaceAll("\\\\", "");
		logger.debug("JDDJ订单列表查询接口:::::::::::::::::result:" + result);
		if (!Tools.isEmpty(result)) {
			try {
				/*
				 * JSONObject obj = JSONObject.parseObject(result);
				 * logger.debug(
				 * "JDDJ订单列表查询接口:::::::::::::::::obj:"+obj.toString()); String
				 * aaa = new String(result); JSONObject obj3 =
				 * JSONObject.parseObject(aaa);
				 * logger.debug("JDDJ订单列表查询接口:::::::::::::::::aaa:"
				 * +obj3.toString());
				 */
				Map<String, Object> m = JSONUtil.jsonToMap(IOUtils.toInputStream(result, "UTF-8"));
				/*
				 * logger.debug("JDDJ订单列表查询接口:::::::::::::::::m:"+m);//可以取值，
				 * 只是把最外层的key--value解析了
				 * logger.debug("JDDJ订单列表查询接口:::::::::::::::::m:data:"
				 * +m.get("data"));//可以取值
				 * logger.debug("JDDJ订单列表查询接口:::::::::::::::::m:data:result:"
				 * +m.get("result"));//取得null
				 * logger.debug("JDDJ订单列表查询接口:::::::::::::::::m:data:result2:"
				 * +m.get("data.result"));//取得null
				 */
				if (m != null && JDDJConfigure.code_SUCCESS.equals(m.get("code"))) {
					String dataStr = (String) m.get("data");
					Map<String, Object> mData = JSONUtil.jsonToMap(IOUtils.toInputStream(dataStr, "UTF-8"));
					if (JDDJConfigure.code_SUCCESS.equals(mData.get("code"))) {
						String resultStr = (String) mData.get("result");
						// logger.debug("JDDJ订单列表查询接口:::::::::::::::::m:data:result3:"+resultStr);
						// JSONObject obj2 = JSONObject.parseObject(resultStr);
						// logger.debug("JDDJ订单列表查询接口:::::::::::::::::obj2:"+obj2.toString());
						if (!StringUtils.isEmpty(resultStr)) {
							JSONObject resultObj = JSONObject.parseObject(resultStr);
							String array = resultObj.getString("resultList");
							List<OrderInfoDTO> list = JSONArray.parseArray(array, OrderInfoDTO.class);
							return list;
						}
					}
				} else {
					logger.info("JDDJ订单列表查询接口取得数据为空。");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public int insertOrder(OrderInfoDTO order) {
		Long orderId = order.getOrderId();
		Integer count = jddjOrderMapper.countByOrderId(orderId);
		if (count > 0) {

			// 订单已存在
			return 2;
		}

		//判断是否是预约订单
		if((order.getOrderPreStartDeliveryTime().getTime() - System.currentTimeMillis())/1000 > 5400){
			order.setDeliveryTime(order.getOrderPreStartDeliveryTime().getTime());
		}
		
		int row = jddjOrderMapper.insertSelective(order);
		if (row == 1) {

			// 插入订单商品数据
			List<OrderProductDTO> productList = order.getProduct();

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("orderId", orderId);
			params.put("adjustId", order.getAdjustId());
			params.put("list", productList);
			row = jddjOrderProductMapper.insertBatch(params);
			if (row > 0) {

				// 插入订单商品优惠列表信息
				List<OrderDiscountDTO> discountList = order.getDiscount();
				if (discountList != null && discountList.size() > 0) {
					params.put("list", discountList);
					jddjOrderDiscountMapper.insertBatch(params);
				}
			}
			
			//商家端消息推送
			if(row > 0){
				jgPushDeviceService.pushMessageByOrderId(orderId,"1","4");
			}
			return 1;
		} else {

			// 插入数据失败
			return 3;
		}
	}

	@Override
	public OrderInfoDTO orderQueryById(Long orderId) {
		return jddjOrderMapper.selectByPrimaryKey(orderId);
	}

	@Override
	public boolean orderAcceptOperate(OrderInfoDTO order, String userId, Boolean isAgreed) {

		// 默认确认接单
		int status = JDDJConfigure.ORDER_STATUS.UN_SEND;
		if (!isAgreed) {
			status = JDDJConfigure.ORDER_STATUS.CANCEL;
		}

		// 更新本地订单状态
		Map<String, Object> params = new HashMap<String, Object>();
		Date date = new Date();
		params.put("orderStatus", status);
		params.put("orderStatusTime", date);
		params.put("orderId", order.getOrderId());
		params.put("busConfirmTime", date);
		params.put("orderNum", order.getOrderNum());
		// params.put("knightPickupTime", order.getOrderId());
		boolean r = this.updateByPrimaryKey(params);
		if (r) {
			
			//测试模式
			if(Application.isTest)
				return true;
			
			// 更新京东到家订单状态
			// 调用 商家确认接单接口
			JSONObject jdParam = new JSONObject();
			jdParam.put("orderId", order.getOrderId() + "");
			jdParam.put("isAgreed", isAgreed);
			jdParam.put("operator", userId);
			Map<String, Object> param = this.commonParam(jdParam.toJSONString());
			String result = HttpRequestUtils.sendSimplePostRequest(JDDJConfigure.getBaseURI() + JDDJConfigure.orderAcceptOperate, param);
			logger.debug("JDDJ订单确认接口:::::::::::::::::result:" + result);
			if (!StringUtils.isEmpty(result)) {
				try {
					Map<String, Object> m = JSONUtil.jsonToMap(IOUtils.toInputStream(result, "UTF-8"));
					if (JDDJConfigure.code_SUCCESS.equals(m.get("code"))) {
						String dataStr = (String) m.get("data");
						Map<String, Object> mData = JSONUtil.jsonToMap(IOUtils.toInputStream(dataStr, "UTF-8"));
						if (JDDJConfigure.code_SUCCESS.equals(mData.get("code"))) {

							// 更新商家确认接单接口成功,进行鼎力云抛单操作
							if (isAgreed) {
								// TODO 将京东到家订单转成商城订单进行抛单
								// 查询抛单记录
								/*
								 * Map<String,Object> pdmap=new HashMap<String,
								 * Object>(); pdmap.put("order_id",
								 * order.getOrderId()); pdmap.put("order_type",
								 * 5); pdmap.put("paodan_type", 1); PaoDanRecord
								 * selPd
								 * =paoDanRecordService.selectPaoDan(pdmap);
								 * if(selPd==null){ //记录抛单数据 PaoDanRecord pd =
								 * new PaoDanRecord(order.getOrderId(),5,1,1,new
								 * Date(),null,0); boolean
								 * flag=paoDanRecordService.insertPaoDan(pd);
								 * if(!flag){ logger.error("info:", "抛单记录失败！");
								 * logger.error("订单id:",
								 * order.getOrderId()+"	订单类型为2:美团订单"); } }
								 */
							}
							return true;
						} else {
							throw new RuntimeException("JDDJ订单确认接口:::::::::::::::::商家确认接单通知京东到家操作失败;code=" + mData.get("code") + ",msg:" + mData.get("msg"));
						}
					} else {
						throw new RuntimeException("JDDJ订单确认接口:::::::::::::::::商家通知确认接单通知京东到家操作失败;code=" + m.get("code") + ",msg:" + m.get("msg"));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	@Override
	public boolean updateByPrimaryKey(Map<String, Object> params) {
		boolean result = jddjOrderMapper.updateByPrimaryKey(params);
		return result;
	}

	/**
	 * 调用接口系统级参数
	 * lixinling 
	 * 2017年1月12日 下午5:34:06
	 * @return
	 */
	public Map<String, Object> commonParam(String jd_param_json) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("token", JDDJConfigure.getToken());
		param.put("app_key", JDDJConfigure.getApp_key());
		param.put("timestamp", DateComFunc.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		param.put("format", "json");
		param.put("v", "1.0");
		param.put("jd_param_json", jd_param_json);
		try {
			param.put("sign", JDDJUtil.getSignByMD5(param, JDDJConfigure.getApp_Secret()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return param;
	}

	/**
	 * 将京东到家订单转成商城订单(进行抛单使用)
	 * lixinling 
	 * 2017年1月12日 下午5:34:06
	 * @return
	 */
	public Map<String, Object> convertJDOrderToshopOrder(OrderInfoDTO order) {
		Map<String, Object> param = new HashMap<String, Object>();

		OrderGroup og = new OrderGroup();
		List<OrderGroupProduct> ogList = new ArrayList<OrderGroupProduct>();
		param.put("og", og);
		param.put("ogList", ogList);

		return param;
	}

	@Override
	public boolean orderJDZBDelivery(Long orderId, String userId) {
		// 更新京东到家订单状态
		// 更新本地订单状态
//		Map<String, Object> params = new HashMap<String, Object>();
//		Date date = new Date();
//		params.put("orderStatus", JDDJConfigure.ORDER_STATUS.UN_SEND);
//		params.put("orderStatusTime", date);
//		params.put("orderId", orderId);
		// params.put("busConfirmTime", date);
		// params.put("knightPickupTime", order.getOrderId());
//		boolean r = this.updateByPrimaryKey(params);
		
		//测试模式
		if(Application.isTest)
			return true;

		JSONObject jdParam = new JSONObject();
		jdParam.put("orderId", "" + orderId);
		jdParam.put("operator", userId);
		// 调用 拣货完成且众包配送接口
		Map<String, Object> param = this.commonParam(jdParam.toJSONString());
		String result = HttpRequestUtils.sendSimplePostRequest(JDDJConfigure.getBaseURI() + JDDJConfigure.orderJDZBDelivery, param);
		if (!StringUtils.isEmpty(result)) {
			Map<String, Object> m;
			try {
				m = JSONUtil.jsonToMap(IOUtils.toInputStream(result, "UTF-8"));
				if (JDDJConfigure.code_SUCCESS.equals(m.get("code"))) {
					String dataStr = (String) m.get("data");
					Map<String, Object> mData = JSONUtil.jsonToMap(IOUtils.toInputStream(dataStr, "UTF-8"));
					if (JDDJConfigure.code_SUCCESS.equals(mData.get("code"))) {
						return true;
					} else {
						throw new RuntimeException("JDDJ订单众包配送接口:::::::::::::::::商家通知京东到家进行众包配送操作失败;code=" + mData.get("code") + ",msg:" + mData.get("msg"));
					}
				} else {
					throw new RuntimeException("JDDJ订单众包配送接口:::::::::::::::::商家通知京东到家进行众包配送操作失败;code=" + m.get("code") + ",msg:" + m.get("msg"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean orderCancelOperate(Long orderId, String userId, Boolean isAgreed, String remark) {
		JSONObject jdParam = new JSONObject();
		jdParam.put("orderId", "" + orderId);
		jdParam.put("operator", userId);
		jdParam.put("isAgreed", isAgreed);
		jdParam.put("remark", remark);
		// 调用 拣货完成且众包配送接口
		Map<String, Object> param = this.commonParam(jdParam.toJSONString());
		String result = HttpRequestUtils.sendSimplePostRequest(JDDJConfigure.getBaseURI() + JDDJConfigure.orderCancelOperate, param);
		if (!StringUtils.isEmpty(result)) {
			Map<String, Object> m;
			try {
				m = JSONUtil.jsonToMap(IOUtils.toInputStream(result, "UTF-8"));
				if (JDDJConfigure.code_SUCCESS.equals(m.get("code"))) {
					String dataStr = (String) m.get("data");
					Map<String, Object> mData = JSONUtil.jsonToMap(IOUtils.toInputStream(dataStr, "UTF-8"));
					if (JDDJConfigure.code_SUCCESS.equals(mData.get("code"))) {
						return true;
					} else {
						throw new RuntimeException("JDDJ订单取消申请确认接口:::::::::::::::::商家通知京东到家进行订单取消申请确认操作失败;code=" + mData.get("code") + ",msg:"
								+ mData.get("msg"));
					}
				} else {
					throw new RuntimeException("JDDJ订单取消申请确认接口:::::::::::::::::商家通知京东到家进行订单取消申请确认操作失败;code=" + m.get("code") + ",msg:" + m.get("msg"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	@Override
	public boolean confirmReceiveGoods(Long orderId) {
		//测试模式
		if(Application.isTest)
			return true;
		
		JSONObject jdParam = new JSONObject();
		jdParam.put("orderId", "" + orderId);
		jdParam.put("operateTime", DateComFunc.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		// 调用 拣货完成且众包配送接口
		Map<String, Object> param = this.commonParam(jdParam.toJSONString());
		String result = HttpRequestUtils.sendSimplePostRequest(JDDJConfigure.getBaseURI() + JDDJConfigure.confirmReceiveGoods, param);
		if (!StringUtils.isEmpty(result)) {
			Map<String, Object> m;
			try {
				m = JSONUtil.jsonToMap(IOUtils.toInputStream(result, "UTF-8"));
				if (JDDJConfigure.code_SUCCESS.equals(m.get("code"))) {
					String dataStr = (String) m.get("data");
					Map<String, Object> mData = JSONUtil.jsonToMap(IOUtils.toInputStream(dataStr, "UTF-8"));
					if (JDDJConfigure.code_SUCCESS.equals(mData.get("code"))) {
						return true;
					} else {
						throw new RuntimeException("JDDJ商家确认收到拒收退回（或取消）的商品接口:::::::::::::::::商家通知京东到家操作失败;code=" + mData.get("code") + ",msg:"
								+ mData.get("msg"));
					}
				} else {
					throw new RuntimeException("JDDJ商家确认收到拒收退回（或取消）的商品接口:::::::::::::::::商家通知京东到家操作失败;code=" + m.get("code") + ",msg:" + m.get("msg"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	@Override
	public int insertDeliveryStatus(JddjDeliveryStatus deliveryStatus) {
		return jddjDeliveryStatusMapper.insertSelective(deliveryStatus);
	}

	@Override
	public boolean urgeDispatching(Long orderId, String userId) {
		JSONObject jdParam = new JSONObject();
		jdParam.put("orderId", "" + orderId);
		jdParam.put("updatePin", userId);
		// 调用 催配送员抢单接口
		Map<String, Object> param = this.commonParam(jdParam.toJSONString());
		String result = HttpRequestUtils.sendSimplePostRequest(JDDJConfigure.getBaseURI() + JDDJConfigure.urgeDispatching, param);
		if (!StringUtils.isEmpty(result)) {
			Map<String, Object> m;
			try {
				m = JSONUtil.jsonToMap(IOUtils.toInputStream(result, "UTF-8"));
				if (JDDJConfigure.code_SUCCESS.equals(m.get("code"))) {
					String dataStr = (String) m.get("data");
					Map<String, Object> mData = JSONUtil.jsonToMap(IOUtils.toInputStream(dataStr, "UTF-8"));
					if (JDDJConfigure.code_SUCCESS.equals(mData.get("code"))) {
						return true;
					} else {
						throw new RuntimeException("JDDJ催配送员抢单接口:::::::::::::::::商家通知京东到家操作失败;code=" + mData.get("code") + ",msg:" + mData.get("msg"));
					}
				} else {
					throw new RuntimeException("JDDJ催配送员抢单接口:::::::::::::::::商家通知京东到家操作失败;code=" + m.get("code") + ",msg:" + m.get("msg"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean adjustOrder(Long orderId, String userId, String remark, String oaosAdjustDTOList) {
		JSONObject jdParam = new JSONObject();
		jdParam.put("orderId", "" + orderId);
		jdParam.put("operPin", userId);
		jdParam.put("remark", remark);
		jdParam.put("oaosAdjustDTOList", oaosAdjustDTOList);
		// 调用 订单调整接口
		Map<String, Object> param = this.commonParam(jdParam.toJSONString());
		String result = HttpRequestUtils.sendSimplePostRequest(JDDJConfigure.getBaseURI() + JDDJConfigure.adjustOrder, param);
		if (!StringUtils.isEmpty(result)) {
			Map<String, Object> m;
			try {
				m = JSONUtil.jsonToMap(IOUtils.toInputStream(result, "UTF-8"));
				if (JDDJConfigure.code_SUCCESS.equals(m.get("code"))) {
					String dataStr = (String) m.get("data");
					Map<String, Object> mData = JSONUtil.jsonToMap(IOUtils.toInputStream(dataStr, "UTF-8"));
					if (JDDJConfigure.code_SUCCESS.equals(mData.get("code"))) {
						String resultStr = (String) mData.get("result");
						logger.info("resultStr:" + resultStr);
						// URLEncodedUtils.parse(resultStr, "UTF-8");
						AdjustResult adjustResult = JSONObject.parseObject(resultStr, AdjustResult.class);
						logger.info("adjustResult:" + adjustResult.toString());
						if (adjustResult.getOrderId() != null && adjustResult.getOrderId().longValue() > 0) {
							DJOrderQuery query = new DJOrderQuery();
							query.setOrderId(Long.valueOf(adjustResult.getOrderId()));
							List<OrderInfoDTO> list = this.orderQuery(query);
							logger.info("调整单重新取得订单数据：" + list.get(0).toString());
						}
						return true;
					} else {
						throw new RuntimeException("JDDJ订单调整接口:::::::::::::::::商家通知京东到家操作失败;code=" + mData.get("code") + ",msg:" + mData.get("msg"));
					}
				} else {
					throw new RuntimeException("JDDJ订单调整接口:::::::::::::::::商家通知京东到家操作失败;code=" + m.get("code") + ",msg:" + m.get("msg"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * 更新调整单数据信息(在原订单的基础上进行更新)
	 * lixinling 
	 * 2017年2月27日 下午5:15:04
	 * @param adjustResult
	 * @return
	 */
	private boolean updateAdjustOrderInfo(AdjustResult adjustResult) {
		if (adjustResult != null) {

		}
		return false;
	}

	/**
	 * 根据订单Id取得订单商品列表
	 * lixinling 
	 * 2017年03月01日 下午5:15:04
	 * @param orderId
	 * @return
	 */
	@Override
	public List<OrderProductDTO> getOrderProductByOrderId(Long orderId) {
		List<OrderProductDTO> orderProductList = jddjOrderProductMapper.selectListByOrderId(orderId);
		return orderProductList;
	}

	@Override
	public List<ThirdOrderProduct> selectJdOrderProduct(Long orderId) {
		return jddjOrderProductMapper.selectJdOrderProduct(orderId);
	}

	@Override
	public int countByOrderId(Long orderId) {
		return jddjOrderMapper.countByOrderId(orderId);
	}

	@Override
	public Integer selectOrderStatusByOrderId(Long orderId) {
		return jddjOrderMapper.selectOrderStatusByOrderId(orderId);
	}

	
}
