package com.linjia.web.uhd123.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linjia.constants.Application;
import com.linjia.constants.Constants;
import com.linjia.tools.DateComFunc;
import com.linjia.tools.Tools;
import com.linjia.tools.Util;
import com.linjia.web.dao.BaiduLogsticsMapper;
import com.linjia.web.dao.CardCouponProductMapper;
import com.linjia.web.dao.MtLogsticsMapper;
import com.linjia.web.dao.OrderGroupMapper;
import com.linjia.web.dao.OrderGroupProductMapper;
import com.linjia.web.model.CardCouponProduct;
import com.linjia.web.model.OrderGroup;
import com.linjia.web.model.OrderGroupProduct;
import com.linjia.web.thirdService.baidu.model.BaiduData;
import com.linjia.web.thirdService.baidu.model.BdShop;
import com.linjia.web.thirdService.baidu.model.BdUser;
import com.linjia.web.thirdService.baidu.model.Coord;
import com.linjia.web.thirdService.meituan.model.MtOrder;
import com.linjia.web.uhd123.common.Configure;
import com.linjia.web.uhd123.model.Address;
import com.linjia.web.uhd123.model.Carrier;
import com.linjia.web.uhd123.model.Contact;
import com.linjia.web.uhd123.model.Customer;
import com.linjia.web.uhd123.model.DeliverRequest;
import com.linjia.web.uhd123.model.Delivery;
import com.linjia.web.uhd123.model.FrontItem;
import com.linjia.web.uhd123.model.FrontOrder;
import com.linjia.web.uhd123.model.FrontRtn;
import com.linjia.web.uhd123.model.Order;
import com.linjia.web.uhd123.model.OrderDataItem;
import com.linjia.web.uhd123.model.OrderItem;
import com.linjia.web.uhd123.model.OrderStockLock;
import com.linjia.web.uhd123.model.Payee;
import com.linjia.web.uhd123.model.Payment;
import com.linjia.web.uhd123.model.PaymentDetail;
import com.linjia.web.uhd123.model.Platform;
import com.linjia.web.uhd123.model.Receiver;
import com.linjia.web.uhd123.model.Refund;
import com.linjia.web.uhd123.model.ReturnDelivery;
import com.linjia.web.uhd123.model.ReturnRequest;
import com.linjia.web.uhd123.model.Rtn;
import com.linjia.web.uhd123.model.RtnItem;
import com.linjia.web.uhd123.model.Shop;
import com.linjia.web.uhd123.model.Station;
import com.linjia.web.uhd123.service.UhdOrderService;

/** 
 * 鼎力云订单
 * @author  lixinling: 
 * @date 2016年10月11日 上午10:30:41 
 * @version 1.0 
*/
@Service
public class UhdOrderServiceImpl implements UhdOrderService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private OrderGroupMapper mapper;

	@Resource
	private OrderGroupProductMapper orderGroupProductMapper;

	@Resource
	private CardCouponProductMapper cardCouponProductMapper;

	@Resource
	private MtLogsticsMapper mtmapper; // 美团订单操作

	@Autowired
	private BaiduLogsticsMapper bdmapper; // 百度订单操作

	@Override
	public boolean submitOrder(String shop_id, OrderGroup orderGroup, List<OrderGroupProduct> orderGroupProductList, String userId) {
		if(true){
			return true;
		}
		// 测试环境直接返回
		/*if (Application.isTest) {
			return true;
		}*/

		// 鼎力云订单号
		String uhdOrderId = null;

		// 设置平台
		orderGroup.setShop_id(shop_id);

		if (orderGroup != null && orderGroupProductList != null && orderGroupProductList.size() > 0) {

			// 构造订单参数
			Order order;
			try {
				order = createOrder(orderGroup, orderGroupProductList);
				JSONObject result = Util.submitOrderToUhd(userId, order);
				if (result != null && !Tools.isEmpty(result.optString("id"))) {
					uhdOrderId = result.optString("id");

					// 将已抛抛单的状态更新为已发货状态（提交订单时鼎力云会将订单状态设置成未发货，因此需要再次更新订单配送状态）
					updateOrderDeliverToUhd(uhdOrderId, null, userId, "shipped", "handover", "handover");

					boolean uflag = false;
					if (shop_id.equals(Configure.shop_id_linjia)) {
						// 更新订单表中生成的鼎力云订单号
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("groupId", orderGroup.getId());
						params.put("uhdOrderId", uhdOrderId);
						int num = mapper.updateStatusById(params);
						if (num > 0) {
							uflag = true;
						}
					} else if (shop_id.equals(Configure.shop_id_meituan)) {
						MtOrder mt = new MtOrder();
						mt.setOrder_id(orderGroup.getId() + "");
						// mt.setApp_poi_code(orderGroup.getMallCode());
						mt.setUhd_order_id(uhdOrderId);
						uflag = mtmapper.updateOrderConfirmed(mt);
					} else if (shop_id.equals(Configure.shop_id_baiduwaimai)) {
						BaiduData bd = new BaiduData();
						BdShop shop = new BdShop();
						BdUser user = new BdUser();
						user.setCoord(new Coord());
						com.linjia.web.thirdService.baidu.model.Order bdorder = new com.linjia.web.thirdService.baidu.model.Order();
						bdorder.setOrder_id(orderGroup.getId() + "");
						bd.setOrder(bdorder);
						bd.setUser(user);
						bd.setShop(shop);
						bd.setUhd_order_id(uhdOrderId);
						uflag = bdmapper.updateBdOrder(bd);
					}

					if (uflag) {
						return true;
					}

				} else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return false;
	}

	/**
	 * 构造订单参数
	 * lixinling 
	 * 2016年10月11日 上午10:46:19
	 * @param orderGroup
	 * @param orderGroupProductList
	 * @return
	 * @throws Exception 
	 */
	private Order createOrder(OrderGroup orderGroup, List<OrderGroupProduct> orderGroupProductList) throws Exception {
		Order uhdOrder = new Order();

		// 如果订单使用了商品券，需要把商品券对应的商品添加到orderGroupProductList
		if (orderGroup.getCouponType() != null && orderGroup.getCouponType() == Constants.CARD_COUPON_TYPE.SPQ) {
			CardCouponProduct cardCouponProduct = cardCouponProductMapper.selectByuserCardCouponId(orderGroup.getUserCardCouponId());
			OrderGroupProduct product = new OrderGroupProduct();
			product.setGroupId(orderGroup.getId());
			product.setpCode(cardCouponProduct.getpCode());
			product.setpName(cardCouponProduct.getpName() + "[商品券]");
			product.setDiscountAmount(new BigDecimal("0.00"));
			product.setApportedDiscountAmount(new BigDecimal("0.00"));
			product.setProductPrice(new BigDecimal("0.00"));
			product.setQuantity(1);
			product.setItemTotalPrice(new BigDecimal("0.00"));
			product.setItemPrice(new BigDecimal("0.00"));
			product.setId(Long.valueOf(cardCouponProduct.getId()==null?0:cardCouponProduct.getId()));
		}

		Shop shop = new Shop();
		shop.setId(orderGroup.getShop_id());
		// 平台商家代码：6666[邻家便利店平台]微电汇[weidianhui]；30328[百度外卖]百度外卖[baiduwaimai]；358[美团外卖]美团外卖[meituan]
		if (orderGroup.getShop_id().equals(Configure.shop_id_meituan)) {
			shop.setName(Configure.shop_name_meituan);
		} else if (orderGroup.getShop_id().equals(Configure.shop_id_baiduwaimai)) {
			shop.setName(Configure.shop_name_baiduwaimai);
		} else {
			shop.setName(Configure.shop_name_linjia);
		}

		uhdOrder.setShop(shop);

		uhdOrder.setItem_count(orderGroupProductList.size());
		uhdOrder.setSku_quantity(orderGroup.getTotalProductNum());

		/*
		 * Customer customer = new Customer(); Contact contact = new Contact();
		 * customer.setId(orderGroup.getUserId());
		 * contact.setName(orderGroup.getReceiveName());
		 * contact.setMobile(orderGroup.getReceivePhone());
		 * customer.setContact(contact);
		 */
		uhdOrder.setCustomer(null);

		uhdOrder.setCustomer_note(orderGroup.getRemark());
		uhdOrder.setReal_total(orderGroup.getRealPrice());

		// 支付信息
		Payment payment = new Payment();
		payment.setType("online");
		payment.setState("paid");
		payment.setPay_time(DateComFunc.formatDate(orderGroup.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
		/*
		 * Payee payee = new Payee(); if (Tools.isEmpty(orderGroup.getUserId()))
		 * throw new Exception("payment.payee.id不能为空");
		 * payee.setId(orderGroup.getUserId());
		 * payee.setName(orderGroup.getReceiveName());
		 */
		payment.setPayee(null);
		payment.setPay_bill_id(orderGroup.getTransactionId());
		payment.setFreight(orderGroup.getSendPrice());
		payment.setAmount(orderGroup.getRealPrice());
		payment.setCod_amount(new BigDecimal("0.00"));
		payment.setEarnest_amount(new BigDecimal("0.00"));

		PaymentDetail[] details = new PaymentDetail[1];
		PaymentDetail detail = new PaymentDetail();
		if (orderGroup.getPayType() == 1) {
			detail.setName("钱包支付");
		} else if (orderGroup.getPayType() == 2) {
			detail.setName("美团支付");
		} else if (orderGroup.getPayType() == 3) {
			detail.setName("百度支付");
		} else {
			detail.setName("微信支付");
		}
		detail.setAmount(orderGroup.getRealPrice());
		details[0] = detail;
		payment.setDetails(details);

		uhdOrder.setPayment(payment);

		// 交付信息
		Delivery delivery = new Delivery();
		// type取值范围:selftake(到店自提)、deliver(送货上门)
		if (orderGroup.getSendType() == 0) {
			delivery.setType("deliver");
			
			//承运商类型，取值范围:self(商家自送)、thirdparty(第三方配送)、express(快递配送)
			Carrier carrier = new Carrier();
			carrier.setType("thirdparty");
			delivery.setCarrier(carrier);
		} else if (orderGroup.getSendType() == 1) {
			delivery.setType("selftake");
		}
		Station station = new Station();
		station.setId(orderGroup.getMallCode());
		station.setName(orderGroup.getMallName());
		delivery.setStation(station);

		// 配送状态，默认取值为:none。取值范围:none(未配送)、stocked(已备货)、shipped(已发货)、signed(已妥投)、refused(已拒收)、redeliver(重投)
		delivery.setState("shipped");

		Receiver receiver = new Receiver();
		receiver.setContact(null);
		Address address = new Address();
		address.setCountry("中国");
		address.setStreet(orderGroup.getReceiveAddress());
		receiver.setAddress(address);
		delivery.setReceiver(receiver);

		uhdOrder.setDelivery(delivery);

		FrontOrder front = new FrontOrder();
		Platform platform = new Platform();

		// 平台商家代码：6666[邻家便利店平台]微电汇[weidianhui]；30328[百度外卖]百度外卖[baiduwaimai]；358[美团外卖]美团外卖[meituan]
		if (orderGroup.getShop_id().equals(Configure.shop_id_meituan)) {
			platform.setId(Configure.platform_id_meituan);
			platform.setName(Configure.platform_name_meituan);
		} else if (orderGroup.getShop_id().equals(Configure.shop_id_baiduwaimai)) {
			platform.setId(Configure.platform_id_baiduwaimai);
			platform.setName(Configure.platform_name_baiduwaimai);
		} else {
			platform.setId(Configure.platform_id_linjia);
			platform.setName(Configure.platform_name_linjia);
		}
		front.setPlatform(platform);
		front.setOrder_id(String.valueOf(orderGroup.getId()));
		front.setCreated(DateComFunc.formatDate(orderGroup.getCreDate(), "yyyy-MM-dd HH:mm:ss"));
		front.setModified(DateComFunc.formatDate(orderGroup.getCreDate(), "yyyy-MM-dd HH:mm:ss"));
		front.setState("已付款");

		uhdOrder.setFront(front);

		// 商品行数据
		OrderItem[] items = new OrderItem[orderGroupProductList.size()];
		for (int i = 0; i < orderGroupProductList.size(); i++) {
			OrderGroupProduct product = orderGroupProductList.get(i);
			OrderItem item = new OrderItem();
			item.setItem_no(String.valueOf(product.getGroupId()));
			item.setSku_id(product.getpCode());
			item.setItem_title(product.getpName());
			item.setBarcode(product.getpCode());
			if (product.getDiscountAmount() == null) {
				throw new Exception("商品行：[" + product.getpName() + "]的商品优惠金额为null");
			} else {
				item.setDiscount_amount(product.getDiscountAmount());
			}
			if (product.getApportedDiscountAmount() == null) {
				throw new Exception("商品行：[" + product.getpName() + "]的整单优惠分摊金额为null");
			} else {
				item.setApported_discount_amount(product.getApportedDiscountAmount());
			}
			if (product.getProductPrice() == null) {
				throw new Exception("商品行：[" + product.getpName() + "]的商品销售价格为null");
			} else {
				item.setUnit_price(product.getProductPrice());
			}
			item.setQuantity(new BigDecimal(product.getQuantity()));
			if (product.getItemTotalPrice() == null) {
				throw new Exception("商品行：[" + product.getpName() + "]的商品金额为null");
			} else {
				item.setTotal(product.getItemTotalPrice());
			}
			item.setPay_amount(product.getItemPrice());

			FrontItem frontItem = new FrontItem();
			frontItem.setSku_id(product.getpCode());
			frontItem.setItem_id(String.valueOf(product.getId()));
			item.setFront(frontItem);

			items[i] = item;
		}

		uhdOrder.setItems(items);

		return uhdOrder;
	}

	@Override
	public boolean testSubmitOrder(Long orderGroupId, String userId) {
		// 查询订单的相关信息
		OrderGroup orderGroup = mapper.selectByPrimaryKey(orderGroupId);

		// 查询订单商品的详细信息
		List<OrderGroupProduct> orderGroupProductList = orderGroupProductMapper.selectProductListByGroupId(orderGroupId);
		return submitOrder(Configure.shop_id_linjia, orderGroup, orderGroupProductList, userId);
	}

	@Override
	public boolean testReturnservice(Long orderGroupId, String userId, String refundId) {
		// 查询订单的相关信息
		OrderGroup orderGroup = mapper.selectByPrimaryKey(orderGroupId);

		// 查询订单商品的详细信息
		List<OrderGroupProduct> orderGroupProductList = orderGroupProductMapper.selectProductListByGroupId(orderGroupId);

		// 测试提交退换单
		 return returnserviceToUhd(Configure.shop_id_linjia, orderGroup, orderGroupProductList,userId,refundId);

		// 测试订单占货
		//return orderstocklockToUhd(Configure.shop_id_linjia, orderGroup, orderGroupProductList, userId);

		// 测试订单释放库存
		// return orderstockunlockToUhd(1, orderGroupId.toString(), userId);
	}

	@Override
	public boolean updateOrderDeliverToUhd(String uhdOrderId, Date orderSuccessTime, String userId, String deliver_status,
			String operation, String operation_state) {
		if(true){
			return true;
		}
		// 测试环境直接返回
		/*if (Application.isTest) {
			return true;
		}*/

		DeliverRequest deliver = new DeliverRequest();

		// 配送状态，取值范围:none(未配送)、shipped(已发货)、signed(已妥投)、refused(已拒收)、redeliver(重投)
		deliver.setDelivery_state(deliver_status);
		if (orderSuccessTime == null)
			orderSuccessTime = new Date();
		deliver.setFront_modified(DateComFunc.formatDate(orderSuccessTime, "yyyy-MM-dd HH:mm:ss"));

		// 门店作业,取值范围：shipping（已集货）、handover（已交接）、cancelShipping(取消集货)
		// deliver.setOperation(operation_state);

		// 门店作业状态,取值范围：is null（待集货）、none（空）、shipping（已集货）、handover（已交接）
		// deliver.setOperation_state(operation_state);

		JSONObject result = Util.updateOrderDeliverToUhd(userId, uhdOrderId, deliver);
		if (result != null && result.optBoolean("success")) {
			logger.info("SUCCESS:::::::更新订单号：" + uhdOrderId + "的状态为[" + deliver_status + "]状态");
			return true;
		} else {
			logger.info("ERROR:::::::更新订单号：" + uhdOrderId + "的状态为[" + deliver_status + "]状态");
			return false;
		}
	}

	@Override
	public boolean returnserviceToUhd(String shop_id, OrderGroup orderGroup, List<OrderGroupProduct> orderGroupProductList, String userId, String refundId) {
		if(true){
			return true;
		}
		// 测试环境直接返回
		/*if (Application.isTest) {
			return true;
		}*/

		// OrderGroup orderGroup = mapper.selectByUhdOrderId(uhdOrderId);
		// // 查询订单商品的详细信息
		// List<OrderGroupProduct> orderGroupProductList =
		// orderGroupProductMapper.selectProductListByGroupId(orderGroup.getId());

		orderGroup.setShop_id(shop_id);

		Rtn rtn = new Rtn();
		//退货订单号，不能重复，为空自动生成
		rtn.setId(refundId);
		
		Shop shop = new Shop();
		shop.setId(String.valueOf(orderGroup.getShop_id()));
		shop.setName("邻家便利店");
		rtn.setShop(shop);
		rtn.setType("refundAndRtn");
		rtn.setState("finished");

		Customer customer = new Customer();
		Contact contact = new Contact();
		contact.setMobile(orderGroup.getReceivePhone());
		contact.setName(orderGroup.getReceiveName());
		customer.setContact(contact);
		customer.setId(orderGroup.getUserId());
		rtn.setCustomer(customer);

		rtn.setOrder_id(orderGroup.getUhdOrderId());
		rtn.setApprove_state("none");

		ReturnDelivery delivery = new ReturnDelivery();
		Station station = new Station();
		station.setId(orderGroup.getMallCode());
		station.setName(orderGroup.getMallName());
		delivery.setStation(station);

		// 退货状态，取值范围:none(未退货)、customerShipped(客户已发货)、received(已收货)
		delivery.setState("received");

		Receiver receiver = new Receiver();
		receiver.setContact(contact);
		Address address = new Address();
		address.setCountry("中国");
		address.setStreet(orderGroup.getReceiveAddress());
		receiver.setAddress(address);
		delivery.setReturner(receiver);
		rtn.setDelivery(delivery);

		Refund refund = new Refund();
		refund.setState("refunded");
		refund.setFreight(orderGroup.getSendPrice());
		refund.setAmount(orderGroup.getRealPrice());
		refund.setSettle_amount(orderGroup.getRealPrice());
		rtn.setRefund(refund);

		FrontRtn front = new FrontRtn();
		Platform platform = new Platform();
		// 平台商家代码：6666[邻家便利店平台]微电汇[weidianhui]；30328[百度外卖]百度外卖[baiduwaimai]；358[美团外卖]美团外卖[meituan]
		if (orderGroup.getShop_id().equals(Configure.shop_id_meituan)) {
			platform.setId(Configure.platform_id_meituan);
			platform.setName(Configure.platform_name_meituan);
		} else if (orderGroup.getShop_id().equals(Configure.shop_id_baiduwaimai)) {
			platform.setId(Configure.platform_id_baiduwaimai);
			platform.setName(Configure.platform_name_baiduwaimai);
		} else {
			platform.setId(Configure.platform_id_linjia);
			platform.setName(Configure.platform_name_linjia);
		}
		front.setPlatform(platform);
		Date date = new Date();
		front.setCreated(DateComFunc.formatDate(date, "yyyy-MM-dd HH:mm:ss"));
		front.setModified(DateComFunc.formatDate(date, "yyyy-MM-dd HH:mm:ss"));
		front.setState("已退货");
		rtn.setFront(front);

		RtnItem[] items = new RtnItem[orderGroupProductList.size()];
		for (int i = 0; i < orderGroupProductList.size(); i++) {
			OrderGroupProduct product = orderGroupProductList.get(i);
			RtnItem item = new RtnItem();
			if(!Tools.isEmpty(product.getId())){
				item.setItem_no(product.getId().toString());
			}
			item.setSku_id(product.getpCode());
			item.setItem_title(product.getpName());
			item.setQuantity(new BigDecimal(product.getQuantity()));
			item.setUnit_price(product.getProductPrice());
			item.setTotal(product.getItemPrice());
			item.setRefund_amount(product.getItemPrice());

			items[i] = item;
		}
		rtn.setItems(items);

		JSONObject result = Util.returnserviceToUhd(userId, rtn);

		if (result != null && !Tools.isEmpty(result.optString("id"))) {
			String uhdRefundOrderId = result.optString("id");

			boolean uflag = false;
			if (shop_id.equals(Configure.shop_id_linjia)) {
				// 更新订单表中生成的鼎力云订单号
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("groupId", orderGroup.getId());
				params.put("uhdRefundOrderId", uhdRefundOrderId);
				int num = mapper.updateStatusById(params);
				if (num > 0) {
					uflag = true;
				}
			} else if (shop_id.equals(Configure.shop_id_meituan)) {
				MtOrder mt = new MtOrder();
				mt.setOrder_id(orderGroup.getId() + "");
				// mt.setApp_poi_code(orderGroup.getMallCode());
				mt.setUhd_refund_order_id(uhdRefundOrderId);
				uflag = mtmapper.updateOrderConfirmed(mt);
			} else if (shop_id.equals(Configure.shop_id_baiduwaimai)) {
				BaiduData bd = new BaiduData();
				BdShop bdshop = new BdShop();
				BdUser user = new BdUser();
				user.setCoord(new Coord());
				com.linjia.web.thirdService.baidu.model.Order bdorder = new com.linjia.web.thirdService.baidu.model.Order();
				bdorder.setOrder_id(orderGroup.getId() + "");
				bd.setOrder(bdorder);
				bd.setUser(user);
				bd.setShop(bdshop);
				bd.setUhd_refund_order_id(uhdRefundOrderId);
				uflag = bdmapper.updateBdOrder(bd);
			}

			if (uflag) {
				// 更新退货单状态
				return confirmReturnserviceToUhd(uhdRefundOrderId, userId);
			}
		}

		return false;
	}

	@Override
	public boolean confirmReturnserviceToUhd(String returnserviceId, String userId) {
		if(true){
			return true;
		}
		// 测试环境直接返回
		/*if (Application.isTest) {
			return true;
		}*/

		ReturnRequest returnRequest = new ReturnRequest();
		returnRequest.setReturn_state("received");

		JSONObject result = Util.confirmReturnserviceToUhd(userId, returnserviceId, returnRequest);
        return result != null && result.optBoolean("success");
    }

	@Override
	public boolean orderstocklockToUhd(String shop_id, OrderGroup orderGroup, List<OrderGroupProduct> orderGroupProductList, String userId) {
		// 测试环境直接返回
		/*if (Application.isTest) {
			return true;
		}*/

		orderGroup.setShop_id(shop_id);
		OrderStockLock orderStockLock = new OrderStockLock();
		orderStockLock.setOrder_id(String.valueOf(orderGroup.getId()));
		// 平台商家代码：6666[邻家便利店平台]微电汇[weidianhui]；30328[百度外卖]百度外卖[baiduwaimai]；358[美团外卖]美团外卖[meituan]
		if (orderGroup.getShop_id().equals(Configure.shop_id_meituan)) {
			orderStockLock.setPlatform_id(Configure.platform_id_meituan);
		} else if (orderGroup.getShop_id().equals(Configure.shop_id_baiduwaimai)) {
			orderStockLock.setPlatform_id(Configure.platform_id_baiduwaimai);
		} else {
			orderStockLock.setPlatform_id(Configure.platform_id_linjia);
		}
		orderStockLock.setShop_id(String.valueOf(orderGroup.getShop_id()));
		orderStockLock.setShop_name("邻家便利店");
		orderStockLock.setStore_id(orderGroup.getMallCode());

		OrderDataItem[] items = new OrderDataItem[orderGroupProductList.size()];
		for (int i = 0; i < orderGroupProductList.size(); i++) {
			OrderGroupProduct product = orderGroupProductList.get(i);
			OrderDataItem item = new OrderDataItem();
			item.setSku_id(product.getpCode());
			item.setQty(product.getQuantity().toString());

			items[i] = item;
		}

		orderStockLock.setItems(items);

		JSONObject result = Util.orderstocklockToUhd(userId, orderStockLock);

		return false;
	}

	@Override
	public boolean orderstockunlockToUhd(String order_id, String userId) {
		// 测试环境直接返回
		/*if (Application.isTest) {
			return true;
		}*/

		JSONObject result = Util.orderstockunlockToUhd(userId, order_id);
        return result != null && result.optBoolean("success");
    }

}
