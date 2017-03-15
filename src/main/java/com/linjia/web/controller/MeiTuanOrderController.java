package com.linjia.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.linjia.base.controller.BaseController;
import com.linjia.web.test.MtTestData;
import com.linjia.web.thirdService.meituan.constants.PoiQualificationEnum;
import com.linjia.web.thirdService.meituan.model.MtOrder;
import com.linjia.web.thirdService.meituan.service.MtOrderService;
import com.linjia.web.thirdService.meituan.service.OrderService;
import com.linjia.web.thirdService.meituan.service.PoiService;
import com.linjia.web.thirdService.meituan.vo.LogisticsParam;
import com.linjia.web.thirdService.meituan.vo.OrderDetailParam;
import com.linjia.web.thirdService.meituan.vo.OrderSubsidyParam;
import com.linjia.web.thirdService.meituan.vo.PoiParam;
import com.linjia.web.thirdService.meituan.vo.PoiPolicyParam;
import com.linjia.web.thirdService.meituan.vo.PoiTagParam;

/**
 * 订单模块
 * 
 * @author xiangshouyi
 *
 */
@Controller
@RequestMapping("/mtOrder")
public class MeiTuanOrderController extends BaseController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private PoiService poiService;
	@Autowired
	private MtOrderService mtOrderService;

	/**
	 * 将订单设为商家已收到
	 * 
	 * @param systemParam
	 *            系统参数
	 * @param orderId
	 *            订单id
	 * @return
	 */
	@RequestMapping("/orderReceived")
	@ResponseBody
	public Object orderReceived(HttpServletRequest request,
			@RequestParam Long orderId) {
		JSONObject json = orderService.orderReceived(orderId);
		return json;
	}

	/**
	 * 商家确认订单
	 * 
	 * @param systemParam
	 *            系统参数
	 * @param orderId
	 *            订单id
	 * @return
	 */
	@RequestMapping("/orderConfirm")
	@ResponseBody
	public Object orderConfirm(HttpServletRequest request,
			@RequestParam Long orderId) {
		JSONObject json = orderService.orderConfirm(orderId);
		return json;
	}

	/**
	 * 商家取消订单
	 * 
	 * @param systemParam
	 *            系统参数
	 * @param orderId
	 *            订单id
	 * @param reason
	 *            取消原因
	 * @param reasonCode
	 *            取消原因code, 通过取消订单原因列表接口方法获取
	 * @return
	 */
	@RequestMapping("/orderCancel")
	@ResponseBody
	public Object orderCancel(HttpServletRequest request,
			@RequestParam Long orderId, @RequestParam String reason,
			@RequestParam String reasonCode) {
		JSONObject json = orderService.orderCancel(orderId, reason, reasonCode);
		return json;
	}

	/**
	 * 订单配送中
	 * 
	 * @param systemParam
	 *            系统参数
	 * @param orderId
	 *            订单id
	 * @param courierName
	 *            配送员姓名
	 * @param courierPhone
	 *            配送电话
	 * @return
	 */
	@RequestMapping("/orderDelivering")
	@ResponseBody
	public Object orderDelivering(HttpServletRequest request,
			@RequestParam Long orderId, @RequestParam String courierName,
			@RequestParam String courierPhone) {
		JSONObject json = orderService.orderDelivering(orderId, courierName,
				courierPhone);
		return json;
	}

	/**
	 * 订单已送达(如接入美团配送则无需接)
	 * 
	 * @param systemParam
	 *            系统参数
	 * @param orderId
	 *            订单id
	 * @return
	 */
	@RequestMapping("/orderArrived")
	@ResponseBody
	public Object orderArrived(HttpServletRequest request,
			@RequestParam Long orderId) {
		JSONObject json = orderService.orderArrived(orderId);
		return json;
	}

	/**
	 * 订单确认退款请求
	 * 
	 * @param systemParam
	 *            系统参数
	 * @param orderId
	 *            订单id
	 * @param reason
	 *            确认退款详情
	 * @return
	 */
	@RequestMapping("/orderRefundAgree")
	@ResponseBody
	public Object orderRefundAgree(HttpServletRequest request,
			@RequestParam Long orderId, @RequestParam String reason) {
		JSONObject json = orderService.orderRefundAgree(orderId, reason);
		return json;
	}

	/**
	 * 驳回订单退款申请
	 * 
	 * @param systemParam
	 *            系统参数
	 * @param orderId
	 *            订单id
	 * @param reason
	 *            驳回退款详情
	 * @return
	 */
	@RequestMapping("/orderRefundReject")
	@ResponseBody
	public Object orderRefundReject(HttpServletRequest request,
			@RequestParam Long orderId, @RequestParam String reason) {
		JSONObject json = orderService.orderRefundReject(orderId, reason);
		return json;
	}

	/**
	 * 获取订单商家补贴款
	 * 
	 * @param systemParam
	 *            系统参数
	 * @param orderId
	 *            订单id
	 * @return
	 */
	@RequestMapping("/orderSubsidy")
	@ResponseBody
	public Object orderSubsidy(HttpServletRequest request,
			@RequestParam Long orderId) {
		OrderSubsidyParam json = orderService.orderSubsidy(orderId);
		return json;
	}

	/**
	 * 查询订单状态
	 * 
	 * @param systemParam
	 *            系统参数
	 * @param orderId
	 *            订单id
	 * @return
	 */
	@RequestMapping("/orderViewStatus")
	@ResponseBody
	public Object orderViewStatus(HttpServletRequest request,
			@RequestParam Long orderId) {
		Integer result = orderService.orderViewStatus(orderId);
		org.json.JSONObject json = new org.json.JSONObject();
		json.put("status", "ok");
		json.put("nums", result);
		return json;
	}

	/**
	 * 查询活动信息
	 * 
	 * @param systemParam
	 *            系统参数
	 * @param actDetailId
	 *            活动ID
	 * @return
	 */
	@RequestMapping("/orderGetActDetailByAcId")
	@ResponseBody
	public Object orderGetActDetailByAcId(HttpServletRequest request,
			@RequestParam int actDetailId) {
		PoiPolicyParam json = orderService.orderGetActDetailByAcId(actDetailId);
		return json;
	}

	/**
	 * 获取订单详细信息
	 * 
	 * @param systemParam
	 *            系统参数
	 * @param orderId
	 *            订单id
	 * @param isMtLogistics
	 *            是否为美团配送（当需要查询美团配送的详细信息时此字段需要为1）
	 * @return
	 */
	@RequestMapping("/orderGetOrderDetail")
	@ResponseBody
	public Object orderGetOrderDetail(HttpServletRequest request,@RequestParam long orderId,
			@RequestParam long isMtLogistics) {
		OrderDetailParam json = orderService.orderGetOrderDetail(orderId,
				isMtLogistics);
		return json;
	}

	/**
	 * 下发美团配送订单(接入美团配送选接)
	 * 
	 * @param systemParam
	 *            系统参数
	 * @param orderId
	 *            订单id
	 * @return
	 */
	@RequestMapping("/orderLogisticsPush")
	@ResponseBody
	public Object orderLogisticsPush(HttpServletRequest request,
			@RequestParam long orderId) {
		JSONObject json = orderService.orderLogisticsPush(orderId);
		return json;
	}

	/**
	 * 取消美团配送订单(接入美团配送选接)
	 * 
	 * @param systemParam
	 *            系统参数
	 * @param orderId
	 *            订单id
	 * @return
	 */
	@RequestMapping("/orderLogisticsCancel")
	@ResponseBody
	public Object orderLogisticsCancel(HttpServletRequest request,
			@RequestParam long orderId) {
		JSONObject json = orderService.orderLogisticsCancel(orderId);
		return json;
	}

	/**
	 * 获取配送订单状态(接入美团配送选接)
	 * 
	 * @param systemParam
	 *            系统参数
	 * @param orderId
	 *            订单id
	 * @return
	 */
	@RequestMapping("/orderLogisticsStatus")
	@ResponseBody
	public Object orderLogisticsStatus(HttpServletRequest request,
			@RequestParam long orderId) {
		LogisticsParam json = orderService.orderLogisticsStatus(orderId);
		return json;
	}

	/**
	 * 保存门店
	 * 
	 * @param poiParam
	 *            应用级参数
	 * @return
	 */
	@RequestMapping("/poiSave")
	@ResponseBody
	public JSONObject poiSave(HttpServletRequest request,PoiParam poiParam) {
		JSONObject json = poiService.poiSave(poiParam);
		return json;
	}

    /**
	 * 获取所有门店codes
	 * 
	 * @return
	 */
	@RequestMapping("/poiGetIds")
	@ResponseBody
	public JSONObject poiGetIds(HttpServletRequest request) {
		JSONObject json = poiService.poiGetIds();
		return json;
	}

    /**
	 * 获取门店详细信息
	 * 
	 * @param systemParam
	 *            系统参数
	 * @param appPoiCodes
	 *            门店codes（可以多个，逗号分隔）
	 * @return
	 */
	@RequestMapping("/poiMget")
	@ResponseBody
	public List<PoiParam> poiMget(HttpServletRequest request,@RequestParam String appPoiCodes) {
		List<PoiParam> json = poiService.poiMget(appPoiCodes);
		return json;
	}

    /**
	 * 将门店设置为营业状态
	 * 
	 * @param appPoiCode
	 *            门店code
	 * @return
	 */
	@RequestMapping("/poiOpen")
	@ResponseBody
	public JSONObject poiOpen(HttpServletRequest request,@RequestParam String appPoiCode) {
		JSONObject json = poiService.poiOpen(appPoiCode);
		return json;
	}

    /**
	 * 将门店设置为休息状态
	 * 
	 * @param appPoiCode
	 *            门店code
	 * @return
	 */
	@RequestMapping("/poiClose")
	@ResponseBody
	public JSONObject poiClose(HttpServletRequest request,@RequestParam String appPoiCode) {
		JSONObject json = poiService.poiClose(appPoiCode);
		return json;
	}

    /**
	 * 将门店设置为上线状态
	 * 
	 * @param appPoiCode
	 *            门店code
	 * @return
	 */
	@RequestMapping("/poiOnline")
	@ResponseBody
	public JSONObject poiOnline(HttpServletRequest request,@RequestParam String appPoiCode) {
		JSONObject json = poiService.poiOnline(appPoiCode);
		return json;
	}

    /**
	 * 将门店设置为下线状态
	 * 
	 * @param appPoiCode
	 *            门店code
	 * @param reason
	 *            原因
	 * @return
	 */
	@RequestMapping("/poiOffline")
	@ResponseBody
	public JSONObject poiOffline(HttpServletRequest request,@RequestParam String appPoiCode,@RequestParam String reason) {
		JSONObject json = poiService.poiOffline(appPoiCode, reason);
		return json;
	}

    /**
	 * 门店资质证书上传
	 * 
	 * @param appPoiCode
	 *            门店code
	 * @param poiQualificationEnum
	 *            资质证照的类型（ BUSINESS_LICENSE：营业执照；
	 *            CATERING_SERVICE_LICENSE：餐饮服务许可证； HEALTH_CERTIFICATE：健康证；
	 *            CORPORATE_IDENTITY：法人身份证）
	 * @param qualificationUrl
	 *            资质图片ID
	 * @param validDate
	 *            资质证照的有效期截止日, 必须符合yyyy-MM-dd的格式，type为1，2，3时需要传此字段，type为4时不需要
	 * @param address
	 *            经营地址，type为1，2时需要传此字段，type为3，4时不需要
	 * @param number
	 *            证照编号，type为1，2，3，4时都需要传此字段
	 * @return
	 */
	@RequestMapping("/poiQualifySave")
	@ResponseBody
	public JSONObject poiQualifySave(HttpServletRequest request,@RequestParam String appPoiCode,
			PoiQualificationEnum poiQualificationEnum,@RequestParam String qualificationUrl,
			@RequestParam String validDate,@RequestParam String address,@RequestParam String number) {
		JSONObject json = poiService.poiQualifySave(appPoiCode,
				poiQualificationEnum, qualificationUrl, validDate, address,
				number);
		return json;
	}

    /**
	 * 同步门店预计送达时长
	 * 
	 * @param appPoiCodes
	 *            门店code
	 * @param sendTime
	 *            预计送达时长（单位分钟，如50表示50分钟送达）
	 * @return
	 */
	@RequestMapping("/poiSendTimeSave")
	@ResponseBody
	public JSONObject poiSendTimeSave(HttpServletRequest request,@RequestParam String appPoiCodes,@RequestParam int sendTime) {
		JSONObject json = poiService.poiSendTimeSave(appPoiCodes, sendTime);
		return json;
	}

    /**
	 * 更改门店附加信息
	 * 
	 * @param appPoiCode
	 *            门店code
	 * @param appPoiEmail
	 *            门店email
	 * @param appBrandCode
	 *            第三方品牌code值（对接的三方需要提前将该值告诉美团开发人员）
	 * @param appOrgId
	 *            门店的法人企业（门店的财务结算等均由与之关联的结算企业信息决定，对接的三方需要提前将该值告诉美团对接商务）
	 * @return
	 */
	@RequestMapping("/poiAdditionalSave")
	@ResponseBody
	public JSONObject poiAdditionalSave(HttpServletRequest request,@RequestParam String appPoiCode,@RequestParam String appPoiEmail,
			@RequestParam String appBrandCode,@RequestParam String appOrgId) {
		JSONObject json = poiService.poiAdditionalSave(appPoiCode, appPoiEmail,
				appBrandCode, appOrgId);
		return json;
	}

    /**
	 * 更改门店公告信息
	 * 
	 * @param appPoiCode
	 *            门店code
	 * @param promotionInfo
	 *            门店公告栏信息
	 * @return
	 */
	@RequestMapping("/poiUpdatePromotionInfo")
	@ResponseBody
	public JSONObject poiUpdatePromotionInfo(HttpServletRequest request,@RequestParam String appPoiCode,
			@RequestParam String promotionInfo) {
		JSONObject json = poiService.poiUpdatePromotionInfo(appPoiCode,
				promotionInfo);
		return json;
	}

    /**
	 * 获取门店品类列表
	 * 
	 * @return
	 */
	@RequestMapping("/poiTagList")
	@ResponseBody
	public List<PoiTagParam> poiTagList(HttpServletRequest request) {
		List<PoiTagParam> list = poiService.poiTagList();
		return list;
	}

    /**
	 * 插入订单数据
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping(value="/intMtOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody 
	//,@RequestBody MtOrder mt
	public String intMtOrder(HttpServletRequest request){
		 MtOrder mt=MtTestData.getMtOrder();
		 org.json.JSONObject json=mtOrderService.insertMtOrder(mt);
		 return json.toString();
	}

    /**
	 * 删除订单数据
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping(value="/delMtOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody 
	public String delMtOrder(HttpServletRequest request,@RequestParam Long orderId){
		return mtOrderService.deleteMtOrder(orderId).toString();
	}

    /**
	 * 查询订单数据
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping(value="/selMtOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody 
	public String selMtOrder(HttpServletRequest request,@RequestParam Long orderId,@RequestParam String app_poi_code){
		org.json.JSONObject json=mtOrderService.selectMtOrder(orderId,app_poi_code);
		return json.toString();
	}

    /**
	 * 查询订单数据
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping(value="/shopMtOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody 
	public String selectMtOrderlist(HttpServletRequest request,@RequestParam String app_poi_code){
		String json=mtOrderService.selectMtOrderlist(app_poi_code);
		return json;
	}

    /**
	 * 更新订单数据(已支付)下行接口，邻家提供给美团
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping(value="/updMtOrderPayed",produces ="application/json;charset=UTF-8")
	@ResponseBody 
	public String updateOrderPayed(HttpServletRequest request,MtOrder mt){
		org.json.JSONObject json=mtOrderService.updateOrderPayed(mt);
		return json.toString();
	}

    /**
	 * 更新订单数据(已确认)下行接口，邻家提供给美团
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping(value="/updMtOrderConfirmed",produces ="application/json;charset=UTF-8")
	@ResponseBody 
	public String updMtOrderConfirmed(HttpServletRequest request,MtOrder mt){
		org.json.JSONObject json=mtOrderService.updateOrderConfirmed(mt);
		return json.toString();
	}

    /**
	 * 更新订单数据(已完成)下行接口，邻家提供给美团
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping(value="/updMtOrderFinished",produces ="application/json;charset=UTF-8")
	@ResponseBody 
	public String updateOrderOver(HttpServletRequest request,MtOrder mt){
		return mtOrderService.updateOrderOver(mt).toString();
	}
}
