package com.linjia.web.task;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import com.alibaba.druid.util.StringUtils;
import com.linjia.constants.Application;
import com.linjia.constants.Constants;
import com.linjia.tools.OrderToLogisOrder;
import com.linjia.web.model.ActivityPintuanBase;
import com.linjia.web.model.Address;
import com.linjia.web.model.OrderGroup;
import com.linjia.web.model.OrderGroupProduct;
import com.linjia.web.model.OrderRefund;
import com.linjia.web.model.PaoDanRecord;
import com.linjia.web.model.PinTuanOrder;
import com.linjia.web.model.ThirdOrder;
import com.linjia.web.query.PaoDanRecordQuery;
import com.linjia.web.query.ThirdLogisOrderQuery;
import com.linjia.web.service.ActivityKaiTuanMainService;
import com.linjia.web.service.ActivityPintuanBaseService;
import com.linjia.web.service.AddressService;
import com.linjia.web.service.BdOrderProductService;
import com.linjia.web.service.MtOrderProductService;
import com.linjia.web.service.OrderGroupProductService;
import com.linjia.web.service.OrderGroupService;
import com.linjia.web.service.OrderRefundService;
import com.linjia.web.service.OrderidGenerateService;
import com.linjia.web.service.PaoDanRecordService;
import com.linjia.web.service.PinTuanOrderService;
import com.linjia.web.service.ThirdOrderService;
import com.linjia.web.thirdService.baidu.model.BaiduReOrder;
import com.linjia.web.thirdService.baidu.service.BaiduOrderService;
import com.linjia.web.thirdService.meituan.model.MtReOrder;
import com.linjia.web.thirdService.meituan.service.MtOrderService;
import com.linjia.web.thirdService.meituan.vo.OrderFoodDetailParam;
import com.linjia.web.thirdService.message.service.MessageService;
import com.linjia.web.uhd123.common.Configure;
import com.linjia.web.uhd123.service.UhdOrderService;
import com.linjia.wxpay.common.Signature;
import com.linjia.wxpay.protocol.RefundQueryReqData;
import com.linjia.wxpay.protocol.RefundQueryResData;
import com.linjia.wxpay.service.RefundQueryService;

@Service
public class TaskJob {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ThirdOrderService thirdOrderService;
	@Autowired
	private PaoDanRecordService paoDanRecordService;
	@Autowired
	private OrderGroupService orderGroupService;// 邻家订单
	@Autowired
	private OrderGroupProductService orderGroupProductService;// 邻家订单商品
	@Autowired
	private UhdOrderService uhdOrderService;
	@Autowired
	private MtOrderService mtLocalOrderService;// 美团本地表操作
	@Autowired
	private MtOrderProductService mtOrderProductService;// 美团本地库订单商品
	@Autowired
	private BaiduOrderService baiduOrderService; // 百度
	@Autowired
	private BdOrderProductService bdOrderProductService;// 百度本地库订单商品
	@Autowired
	private MessageService messageService;// 发送短信
	@Autowired
	private PinTuanOrderService pinTuanOrderService; //拼图订单取消
	@Autowired
	private OrderidGenerateService orderidGenerateService;
	@Autowired
	private OrderRefundService orderRefundService;
	@Autowired
	private ActivityKaiTuanMainService activityKaiTuanMainService;
	@Autowired
	private ActivityPintuanBaseService activityPintuanBaseService;
	@Autowired
	private AddressService addressService;
	
	
	public void job1() {
		System.out.println("开始推送预约订单");
		int num = thirdOrderService.selectLjReserveOrder();
		System.out.println("推送预约订单完成。。。" + num);
	}
	

	/**
	 * 推送京东预约订单，及时提醒商家
	 * lixinling 
	 * 2017年3月10日 上午10:21:43
	 */
	public void pushJdReserveOrder() {
		System.out.println("开始推送京东预约订单");
		int num = thirdOrderService.selectJdReserveOrder();
		System.out.println("推送京东预约订单完成。。。" + num);
	}

	public void job2() {
		System.out.println("*********此刻没有任务，呵呵哒！*********");
	}

	/**
	 * 订单取消
	 * lixinling 
	 * 2016年10月4日 上午10:26:53
	 */
	public void cancelOrder() {
		logger.info("******************订单超时取消任务开始！START******************");
		// 查询订单信息
		List<OrderGroup> orderGroupList = orderGroupService.selectAllTimeOutOrder();
		if (orderGroupList == null || orderGroupList.size() == 0) {
			logger.info("******************暂时没有支付超时的订单。******************");
			return;
		} else {

			Map<String, Object> params = new HashMap<String, Object>();
			Date cancelTime = new Date();
			for (OrderGroup orderGroup : orderGroupList) {
				params.put("groupId", orderGroup.getId());
				params.put("orderGroupStatus", Constants.ORDER_GROUP_STATUS.TIMEOUT_CANCELE);
				params.put("orderCancelTime", cancelTime);

				// 更新订单状态
				boolean updateStatusFlg = orderGroupService.updateStatusById(params,"1");
				if (updateStatusFlg) {
					//进行库存恢复
					orderGroupProductService.updateProductQuantity(orderGroup.getId());
				}
			}
		}
		logger.info("******************订单超时取消任务完成,共处理:"+orderGroupList.size()+"条记录！END******************");
	}
	
	/**
	 * 拼团未支付超时取消(每分钟执行查询15分钟之前订单，未支付的取消)
	 * xiangshouyi 
	 * 2016年12月5日 上午10:26:53
	 */
	public void cancelPtOrder() {
		logger.info("******************拼图订单超时取消任务开始！START******************");
		// 查询订单信息
		List<PinTuanOrder> ptorderList = pinTuanOrderService.selectTimeOutOrderUnPay();
		if (ptorderList == null || ptorderList.size() == 0) {
			logger.info("******************暂时没有支付超时的订单。******************");
			return;
		} else {
			Date cancelTime = new Date();
			for (PinTuanOrder pt : ptorderList) {
				PinTuanOrder cancelpt=new PinTuanOrder();
				cancelpt.setOrder_id(pt.getOrder_id());
				cancelpt.setStatus(3);
				cancelpt.setPt_status(3);
				cancelpt.setCancel_time(cancelTime);
				cancelpt.setCancel_reason("超时未支付");
				// 更新订单状态
				pinTuanOrderService.updatePtOrderById(cancelpt);
			}
		}
		logger.info("******************拼团订单超时取消任务完成,共处理:"+ptorderList.size()+"条记录！END******************");
	}
	
	/**
	 * 拼团未成团超时取消(每分钟查询10小时之前的订单，付款成团的发货状态，未成团的退款)
	 * xiangshouyi 
	 * 2016年12月5日 上午10:26:53
	 */
	public void cancelPtFailOrder() {
		logger.info("******************拼图超时未成团取消任务开始！START******************");
		// 查询订单信息
		List<PinTuanOrder> ptorderList = pinTuanOrderService.selectPtTimeOutOrder();
		if (ptorderList == null || ptorderList.size() == 0) {
			logger.info("******************暂时拼图超时未成团的订单。******************");
			return;
		} else {
			Date cancelTime = new Date();
			for (PinTuanOrder pt : ptorderList) {
				Map<String, Object> remap = new HashMap<String,Object>();
				remap.put("orderGroupId",pt.getOrder_id());
				OrderRefund re=orderRefundService.selectOneOrder(remap);
				int num=0;
				Long orderRefundId=null;
				if(pt.getPay_status()==1&&pt.getPay_time()!=null){
					if(re==null){
						//创建退款单
						OrderRefund order=new OrderRefund();
						orderRefundId = orderidGenerateService.generateRefundId();
						order.setId(orderRefundId);
						order.setRefundType(1);
						order.setRefundOnlinepayStatus(1);
						order.setMobile(order.getLoginPhone());
						order.setCreateTime(new Date());
						order.setRefundPayee(pt.getUser_id());
						order.setOrderGroupId(pt.getOrder_id());
						order.setConfirmTime(new Date());
						order.setRefundStatus(2);
						order.setCancelReason("超时未成团");
						order.setRefundAmount(pt.getReal_price());
						order.setRefundOnlinepayStatus(Constants.ORDER_REFUND_STATUS.WAIT_REFUND);
						if(pt.getPay_type()!=null){
							if (pt.getPay_type() == Constants.PAY_TYPE.WX) {
								order.setPayTypeId(0);
								order.setPayTypeName("微信支付");
							} else {
								order.setReturnBalance(pt.getReal_price());
								order.setPayTypeId(1);
								order.setPayTypeName("钱包支付");
							}
						}
						 num=orderRefundService.insert(order);
						 
						 if(pt.getPay_type()==null){
								continue;
						 }
						 
					}else{
						num=1;
						orderRefundId=re.getId();
					}
				}

				if(num>0){
					// 更新订单状态
					PinTuanOrder cancelpt=new PinTuanOrder();
					cancelpt.setOrder_id(pt.getOrder_id());
					cancelpt.setStatus(3);
					cancelpt.setPt_status(3);
					cancelpt.setCancel_time(cancelTime);
					cancelpt.setCancel_reason("超时10小时未成团");
					boolean uf=pinTuanOrderService.updatePtOrderById(cancelpt);
					//更新库存数
					if(uf){
						ActivityPintuanBase base=new ActivityPintuanBase();
						base.setId(pt.getPt_base_id());
						base.setBaseQuantyAdd(1);
						//更新拼团商品库存
						boolean f=activityPintuanBaseService.updateByBaseId(base);
						if(f){
							//退款
							String userId="system";
							Integer source=3;
							//退款接口
							// 执行退款操作
							// 查询订单详情
							OrderGroup orderGroup = new OrderGroup();
							orderGroup.setId(pt.getOrder_id());
							orderGroup.setUserId(pt.getUser_id());
							orderGroup.setRealPrice(pt.getReal_price());
							orderGroup.setPayType(pt.getPay_type());
							orderGroup.setRefundReason("超时未成团订单取消");
							// 查询拼团订单收货人信息
							Address address = addressService.selectById(Long.valueOf(pt.getAddress_id()));
							orderGroup.setReceiveName(address.getReceiveName());
							Map<String, Object> map = orderRefundService.insertRefund(orderGroup, userId, 1, source,orderRefundId);
							if ((int) map.get("return_code") == 1) {
								logger.info("【微信退款申请失败】退款请求错误，返回值为空");
							} else if ((int) map.get("return_code") == 2) {
								logger.info("【微信退款申请失败】退款API系统返回失败，请检测Post给API的数据是否规范合法");
							} else if ((int) map.get("return_code") == 3) {
								logger.info("【微信退款申请失败】退款请求API返回的数据签名验证失败，有可能数据被篡改了");
							} else if ((int) map.get("return_code") == 4) {
								logger.info("调用微信退款申请发生错误");
							} else if ((int) map.get("return_code") == 5) {
								logger.info("退款成功");
							}
						}
					}
				}else{
					//删除退款单记录
//					orderRefundService.deleteRefundOrderById(orderRefundId);
				}
			}
		}
		logger.info("******************拼图超时未成团,共处理:"+ptorderList.size()+"条记录！END******************");
	}
	
	//抛单定时任务
	public void job3() {
		PaoDanRecordQuery query=new PaoDanRecordQuery();
		query.setPageIndex(1);
		query.setPageSize(100);
		query.setStatus(1);
		query.setQueryFlag(1);
		query.setDeleted(0);
		List<PaoDanRecord> list=paoDanRecordService.selectPaoDanList(query);
		System.out.println("抛单开始*************"+list);
		if(list!=null&&list.size()>0){
			for (PaoDanRecord paoDanRecord : list) {
				OrderGroup og = null;
				List<OrderGroupProduct> ogList = null;
				PaoDanRecord pd =new PaoDanRecord();
				String shop_id=null;
				String refundId=null;
				try{
					Long orderId=paoDanRecord.getOrder_id();
					Integer orderType=paoDanRecord.getOrder_type();
					
					System.out.println("orderType:"+orderType+"***************orderId:"+orderId+"**********************");
					//商城普通订单
					if(orderType!=null&&orderType==1){
						shop_id=Configure.shop_id_linjia;
						og = orderGroupService.selectLogisOrderInfo(orderId);
						if (og != null) {
							ogList = orderGroupProductService.selectProductListByGroupId(orderId);
						}
						refundId = orderRefundService.selectRefundIdByOrderId(orderId) + "";
					}
					
					//美团订单
					if(orderType!=null&&orderType==2){
						shop_id=Configure.shop_id_meituan;
						MtReOrder mtorder=mtLocalOrderService.selectMtOrderAll(orderId);
						List<OrderFoodDetailParam> mtList=mtOrderProductService.selectMtOrderProductAll(orderId);
						og = OrderToLogisOrder.mtOrderToLjOrder(mtorder, mtList);
						if (og != null) {
							ogList = og.getProductList();
						}
						refundId = orderId +"";
					}
					
					//百度订单
					if(orderType!=null&&orderType==3){
						shop_id=Configure.shop_id_baiduwaimai;
						//查询抛单对象
						BaiduReOrder bdorder=baiduOrderService.selectPdOrder(orderId);
						List<com.linjia.web.thirdService.baidu.model.Product> bdProlist=bdOrderProductService.selectBdOrderProductAll(orderId);
						og = OrderToLogisOrder.bdOrderToLjOrder(bdorder, bdProlist);
						if (og != null) {
							ogList = og.getProductList();
						}
						refundId = orderId +"";
					}
						
					if(og!=null&&ogList!=null&&ogList.size()>0){
						boolean uflag=false;
						Integer pdType=paoDanRecord.getPaodan_type();
						//抛单
						if(pdType!=null&&pdType==1){
							uflag=uhdOrderService.submitOrder(shop_id, og, ogList, og.getUserId());
						}
						if(pdType!=null&&pdType==2){
							uflag=uhdOrderService.returnserviceToUhd(shop_id, og, ogList, og.getUserId(), refundId);
						}
						if(pdType!=null&&pdType==3){
							uflag=uhdOrderService.updateOrderDeliverToUhd(paoDanRecord.getOrder_id()+"", new Date(),"linjia", "signed", "handover", "handover");
						}
						if(!uflag){
							pd.setId(paoDanRecord.getId());
							pd.setStatus(3);
							pd.setUpdate_time(new Date());
							paoDanRecordService.updatePaoDan(pd);
						}else{
							pd.setId(paoDanRecord.getId());
							pd.setStatus(2);
							pd.setUpdate_time(new Date());
							paoDanRecordService.updatePaoDan(pd);
						}
					}
					//休眠
					Thread.sleep(10);
				}catch(Exception e){
					pd.setId(paoDanRecord.getId());
					pd.setStatus(3);
					pd.setUpdate_time(new Date());
					paoDanRecordService.updatePaoDan(pd);
					//中止线程
					Thread.interrupted();
//					messageService.SendMessage("",Constants.ERROR_WARN_CONFIG.ERROR_RECIEVE_PHONE, "系统抛单异常,请查看原因！ 订单id:"+paoDanRecord.getOrder_id(), "");
				}
			}
		}

	}
	
	/**
	 * 更新微信退款单退款状态
	 * lixinling 
	 * 2016年10月4日 上午10:26:53
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 */
	public void updateRefundOrder() throws Exception {
		logger.info("******************[更新微信退款单退款状态]任务开始！START******************");
		// 查询订单信息
		List<OrderRefund> orderRefundIdList = orderRefundService.selectWxRefundingOrder();
		if (orderRefundIdList == null || orderRefundIdList.size() == 0) {
			logger.info("******************暂时没有正在退款的微信退款单。******************");
			return;
		} else {

			Map<String, Object> params = new HashMap<String, Object>();
			Date cancelTime = new Date();
			for (OrderRefund orderRefund : orderRefundIdList) {
				RefundQueryReqData refundQueryReqData = createRefundQueryReqData(orderRefund);
				
				String resultRefundQueryXml = new RefundQueryService().request(refundQueryReqData);

				if (StringUtils.isEmpty(resultRefundQueryXml)) {
					logger.error("【退款单号："+orderRefund.getId()+" 微信退款查询失败】请求错误，返回值为空");
					continue;
				} else {
					RefundQueryResData refundQueryResData = (RefundQueryResData) com.linjia.wxpay.common.Util.getObjectFromXML(
							resultRefundQueryXml, RefundQueryResData.class);
					if (refundQueryResData.getReturn_code() == null || refundQueryResData.getReturn_code().equals("FAIL")) {
						logger.error("【退款单号："+orderRefund.getId()+"微信退款查询失败】退款查询API系统返回失败，请检测Post给API的数据是否规范合法。返回的错误信息为：" + refundQueryResData.getReturn_msg());
						continue;
					} else if (refundQueryResData.getResult_code().equals("SUCCESS")) {
						// --------------------------------------------------------------------
						// 收到API的返回数据的时候得先验证一下数据有没有被第三方篡改，确保安全
						// --------------------------------------------------------------------
						try {
							if (!Signature.checkIsSignValidFromResponseString(resultRefundQueryXml)) {
								logger.error("【退款单号："+orderRefund.getId()+"微信退款查询失败】退款查询请求API返回的数据签名验证失败，有可能数据被篡改了");
								continue;
							}

							if ("SUCCESS".equals(refundQueryResData.getResult_code())) {
								// 更新退款状态
								Map<String, Object> param = new HashMap<String, Object>();
								
								param.put("refundOnlinepayStatus", com.linjia.wxpay.common.Util.getRefundStatus(refundQueryResData.getRefund_status_0()));
								param.put("outRefundNo", refundQueryResData.getOut_refund_no_0());
								param.put("groupId", orderRefund.getOrderGroupId());
								param.put("userId", orderRefund.getUserId());
								param.put("refundAmount", orderRefund.getRefundAmount());
								orderRefundService.updateRefundStatusById(param);
							}
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (SAXException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		logger.info("******************[更新微信退款单退款状态]任务完成,共处理:"+orderRefundIdList.size()+"条记录！END******************");
	}
	
	/**
	 * 构造退款查询数据对象
	 * lixinling 
	 * 2016年7月27日 下午2:46:06
	 * @param orderGroup
	 * @return
	 */
	public RefundQueryReqData createRefundQueryReqData(OrderRefund orderRefund) {

		/**
		 * 请求退款查询服务
		 * @param transactionID 是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。建议优先使用
		 * @param outTradeNo 商户系统内部的订单号,transaction_id 、out_trade_no 二选一，如果同时存在优先级：transaction_id>out_trade_no
		 * @param deviceInfo 微信支付分配的终端设备号，与下单一致
		 * @param outRefundNo 商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
		 * @param refundID 来自退款API的成功返回，微信退款单号refund_id、out_refund_no、out_trade_no 、transaction_id 四个参数必填一个，如果同事存在优先级为：refund_id>out_refund_no>transaction_id>out_trade_no
		 */
		RefundQueryReqData refundQueryReqData = new RefundQueryReqData(null, String.valueOf(orderRefund.getOrderGroupId()), null,
				String.valueOf(orderRefund.getId()), orderRefund.getRefundId());
		return refundQueryReqData;
	}
	
	/**
	 * 更新达达送货状态
	 * xiangsy
	 * 2016年12月10日 上午10:26:53
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 */
	public void updateDaDaStatus() throws Exception {
		logger.info("******************[更新达达完成状态]任务开始！START******************");
		List<Long> list=orderGroupService.selectDaDaFinOrder();
		System.out.println("更新达达状态开始*************"+list);
		if(list!=null&&list.size()>0){
			for (Long orderId : list) {
				try{
					Map<String,Object> updmap=new HashMap<String, Object>();
					updmap.put("groupId",orderId);
					updmap.put("orderGroupStatus",5);
					updmap.put("orderSuccessTime", new Date());
					orderGroupService.updateStatusById(updmap,"1"); 
					//休眠
					Thread.sleep(500);
				}catch(Exception e){
					//中止线程
					Thread.interrupted();
				}
			}
			logger.info("******************[更新达达完成状态]任务结束！END******************");
		}
	}
	
	//-------------------------抛单临时使用--------------------------------------
	//美团插入抛单数据
	public void job5() {
		ThirdLogisOrderQuery query = new ThirdLogisOrderQuery();
		query.setPageSize(1500);
		query.setPageIndex(Application.order_pdNum);
		query.setTemp(1);
		query.setMtOrderStatus(8);
		query.setMtPatyStatus(2);
//		int[] mtorderSList=new int[]{6,8};
//		query.setMtOrderStatusList(mtorderSList);
		List<ThirdOrder> list = thirdOrderService.selectMtOrderlist(query);
		int j=0;
		if(list!=null&&list.size()>0){
			for (ThirdOrder thirdOrder : list) {
				PaoDanRecord pd=new PaoDanRecord(thirdOrder.getId(),2,1,1,new Date(),null,0);
				boolean f=paoDanRecordService.insertPaoDan(pd);
				if(f){
					PaoDanRecord pd1=new PaoDanRecord(thirdOrder.getId(),2,1,3,new Date(),null,0);
					paoDanRecordService.insertPaoDan(pd1);
					j++;
				}else{
					paoDanRecordService.deletePaoDanByOrderId(thirdOrder.getId());
				}
			}
			Application.order_pdNum=Application.order_pdNum+1;
			System.out.println("########num:"+j);
		}
	}
	
	//抛单(备用)
	public void job4() {
		PaoDanRecordQuery query=new PaoDanRecordQuery();
		query.setPageIndex(1);
		query.setPageSize(1500);
		query.setStatus(1);
		//订单类型
		query.setOrder_type(2);
		query.setQueryFlag(1);
		query.setDeleted(0);
		query.setTimeFlag(1);
		List<PaoDanRecord> list=paoDanRecordService.selectPaoDanList(query);
		System.out.println("抛单开始*************"+list.size());
		if(list!=null&&list.size()>0){
			for (PaoDanRecord paoDanRecord : list) {
				OrderGroup og = null;
				List<OrderGroupProduct> ogList = null;
				PaoDanRecord pd =new PaoDanRecord();
				String shop_id=null;
				try{
					Long orderId=paoDanRecord.getOrder_id();
					Integer orderType=paoDanRecord.getOrder_type();
					System.out.println("orderType:"+orderType+"***************orderId:"+orderId+"**********************");
					//美团订单
					if(orderType!=null&&orderType==2){
						shop_id=Configure.shop_id_meituan;
						MtReOrder mtorder=mtLocalOrderService.selectMtOrderAll(orderId);
						List<OrderFoodDetailParam> mtList=mtOrderProductService.selectMtOrderProductAll(orderId);
						og = OrderToLogisOrder.mtOrderToLjOrder(mtorder, mtList);
						if (og != null) {
							ogList = og.getProductList();
						}
					}
					
					//商城普通订单
					if(orderType!=null&&orderType==1){
						shop_id=Configure.shop_id_linjia;
						og = orderGroupService.selectLogisOrderInfo(orderId);
						if (og != null) {
							ogList = orderGroupProductService.selectProductListByGroupId(orderId);
						}
					}
					
					if(og!=null&&ogList!=null&&ogList.size()>0){
						boolean uflag=false;
						Integer pdType=paoDanRecord.getPaodan_type();
						//抛单
						if(pdType!=null&&pdType==1){
							uflag=uhdOrderService.submitOrder(shop_id, og, ogList, og.getUserId());
						}
						if(pdType!=null&&pdType==2){
							uflag=uhdOrderService.returnserviceToUhd(shop_id, og, ogList, og.getUserId(), orderId+"");
						}
						if(pdType!=null&&pdType==3){
							uflag=uhdOrderService.updateOrderDeliverToUhd(paoDanRecord.getOrder_id()+"", new Date(),"linjia", "signed", "handover", "handover");
						}
						if(!uflag){
							pd.setId(paoDanRecord.getId());
							pd.setStatus(3);
							pd.setUpdate_time(new Date());
							paoDanRecordService.updatePaoDan(pd);
						}else{
							pd.setId(paoDanRecord.getId());
							pd.setStatus(2);
							pd.setUpdate_time(new Date());
							paoDanRecordService.updatePaoDan(pd);
						}
					}
					//休眠
					Thread.sleep(10);
				}catch(Exception e){
					pd.setId(paoDanRecord.getId());
					pd.setStatus(3);
					pd.setUpdate_time(new Date());
					paoDanRecordService.updatePaoDan(pd);
					//中止线程
					Thread.interrupted();
					e.printStackTrace();
//					messageService.SendMessage("",Constants.ERROR_WARN_CONFIG.ERROR_RECIEVE_PHONE, "系统抛单异常,请查看原因！ 订单id:"+paoDanRecord.getOrder_id(), "");
				}
			}
		}
	}
	
	//商城插入抛单数据
	public void job6() {
		ThirdLogisOrderQuery query = new ThirdLogisOrderQuery();
		query.setPageSize(1500);
		query.setPageIndex(Application.order_pdNum);
		query.setTemp(1);
		query.setStatus(5);
        query.setPay_status(1);
		query.setOrderPx(1);
//		int[] orderStatusList=new int[]{6,8};
//		query.setOrderStatusList(orderStatusList);
		List<ThirdOrder> list = thirdOrderService.selectLJpdlist(query);
		int j=0;
		if(list!=null&&list.size()>0){
			for (ThirdOrder thirdOrder : list) {
				PaoDanRecord pd=new PaoDanRecord(thirdOrder.getId(),1,1,1,new Date(),null,0);
				boolean f=paoDanRecordService.insertPaoDan(pd);
				if(f){
					PaoDanRecord pd1=new PaoDanRecord(thirdOrder.getId(),1,1,3,new Date(),null,0);
					paoDanRecordService.insertPaoDan(pd1);
					j++;
				}else{
					paoDanRecordService.deletePaoDanByOrderId(thirdOrder.getId());
				}
			}
			Application.order_pdNum=Application.order_pdNum+1;
			System.out.println("########num:"+j);
		}
	}
	//-------------------------抛单临时使用--------------------------------------

}