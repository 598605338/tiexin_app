package com.linjia.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linjia.base.controller.BaseController;
import com.linjia.web.test.BaiduShopData;
import com.linjia.web.test.BdOrderTestData;
import com.linjia.web.thirdService.baidu.model.BaiduData;
import com.linjia.web.thirdService.baidu.model.Goods;
import com.linjia.web.thirdService.baidu.model.Shop;
import com.linjia.web.thirdService.baidu.service.BaiduGoodsService;
import com.linjia.web.thirdService.baidu.service.BaiduOrderService;
import com.linjia.web.thirdService.baidu.service.BaiduShopService;

/**
 *百度订单控制器
 * 
 * @author xiangsy
 *
 */
@Controller
@RequestMapping("/bdOrder")
public class BaiduOrderController extends BaseController{
	
	@Autowired
	private BaiduGoodsService baiduGoodsService;
	@Autowired
	private BaiduOrderService baiduOrderService;
	@Autowired
	private BaiduShopService baiduShopService;
	
	/**
	 * 添加商品
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addGood",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String addGood(HttpServletRequest request){
		Goods goods=BdOrderTestData.getGoods();
        JSONObject json=baiduGoodsService.skuCreate(goods,"");
		return json.toString();
	}
	
	/**
	 * 商品修改
	 * @param goods
	 * @return
	 */
	@RequestMapping(value="/goodUpdate",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String skuUpdate(HttpServletRequest request,@RequestBody Goods goods){
		Goods goods1=BdOrderTestData.getGoods();
        JSONObject json=baiduGoodsService.skuUpdate(goods1,"");
		return json.toString();
	}

    /**
	 * 商品列表
	 * @return
	 */
	@RequestMapping(value="/skuList",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String skuList(HttpServletRequest request){
        JSONObject json=baiduGoodsService.skuList("");
		return json.toString();
	}

    /**
	 * 商品上线
	 * @param shopId
	 * @param sku_id
	 * @return
	 */
	@RequestMapping(value="/skuOnline",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String skuOnline(HttpServletRequest request,@RequestParam String shopId,@RequestParam String sku_id){
		 JSONObject json=baiduGoodsService.skuOnline(shopId, sku_id, "");
		return json.toString();
	}

    /**
	 * 商品下线
	 * @param shopId
	 * @param sku_id
	 * @return
	 */
	@RequestMapping(value="/skuOffline",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String skuOffline(HttpServletRequest request,@RequestParam String shopId,@RequestParam String sku_id){
		JSONObject json=baiduGoodsService.skuOffline(shopId, sku_id,"");
		return json.toString();
	}

    /**
	 * 批量修改商品库存
	 * @param shopId
	 * @param skuid_stocks
	 * @return
	 */
	@RequestMapping(value="/skuStockUpdateMatch",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String skuStockUpdateMatch(HttpServletRequest request,@RequestParam String shopId,@RequestParam String skuid_stocks){
		JSONObject json=baiduGoodsService.skuStockUpdateMatch(shopId, skuid_stocks,"");
		return json.toString();
	}

    /**
	 * 批量修改商品价格
	 * @param shopId
	 * @param skuid_price
	 * @return
	 */
	@RequestMapping(value="/skuPriceUpdateBatch",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String skuPriceUpdateBatch(HttpServletRequest request,@RequestParam String shopId,@RequestParam String skuid_price){
		JSONObject json=baiduGoodsService.skuPriceUpdateBatch(shopId, skuid_price,"");
		return json.toString();
	}

    /**
	 * 查询店铺
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getShop",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String getShop(HttpServletRequest request,@RequestParam String shopId){
       JSONObject json=baiduShopService.shopGet(shopId, "");
		return json.toString();
	}

    /**
	 * 创建商户
	 * @param shop
	 * @return
	 */
	@RequestMapping(value="/shopCreate",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String shopCreate(HttpServletRequest request,@RequestBody Shop shop){
		
	       JSONObject json=baiduShopService.shopCreate(shop,"");
			return json.toString();
		}

    /**
	 * 修改商户
	 * @param shop
	 * @return
	 */
	@RequestMapping(value="/shopUpdate",produces ="application/json;charset=UTF-8")
	@ResponseBody
	//,@RequestBody Shop shop
	public String shopUpdate(HttpServletRequest request){
			Shop shop=BaiduShopData.getShopTest01();
	       JSONObject json=baiduShopService.shopUpdate(shop,"");
			return json.toString();
		}

    /**
	 * 商户列表
	 * @return
	 */
	@RequestMapping(value="/shopList",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String shopList(HttpServletRequest request){
	       JSONObject json=baiduShopService.shopList("");
			return json.toString();
		}

    /**
	 * 下线商户
	 * @param shopId
	 * @return
	 */
	@RequestMapping(value="/shopOffline",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String shopOffline(HttpServletRequest request,@RequestParam String shopId){
	       JSONObject json=baiduShopService.shopOffline(shopId, "");
			return json.toString();
		}

    /**
	 * 商户开业
	 * @param shopId
	 * @return
	 */
	@RequestMapping(value="/shopOpen",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String shopOpen(HttpServletRequest request,@RequestParam String shopId){
	       JSONObject json=baiduShopService.shopOpen(shopId, "");
			return json.toString();
		}

    /**
	 * 商户歇业
	 * @param shopId
	 * @return
	 */
	@RequestMapping(value="/shopClose",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String shopClose(HttpServletRequest request,@RequestParam String shopId){
	       JSONObject json=baiduShopService.shopClose(shopId,"");
			return json.toString();
		}

    /**
	 * 商户订单阈值设置(已下线)	
	 * @param shopId
	 * @param list
	 * @return
	 */
	@RequestMapping(value="/shopThresholdSet",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String shopThresholdSet(HttpServletRequest request,@RequestParam String shopId,List list){
	       JSONObject json=baiduShopService.shopThresholdSet(shopId,list,"");
			return json.toString();
		}

    /**
	 * 商户配送时延设置
	 * @param shopId
	 * @param delivery_delay_time
	 * @return
	 */
	@RequestMapping(value="/shopDeliveryDelay",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String shopDeliveryDelay(HttpServletRequest request,@RequestParam String shopId,@RequestParam int delivery_delay_time){
	       JSONObject json=baiduShopService.shopDeliveryDelay(shopId,delivery_delay_time,"");
			return json.toString();
		}

    /**
	 * 商户资质图片上传
	 * @param shopId
	 * @param list
	 * @return
	 */
	@RequestMapping(value="/shopPicUpload",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String shopPicUpload(HttpServletRequest request,@RequestParam String shopId,List list){
	       JSONObject json=baiduShopService.shopPicUpload(shopId,list,"");
			return json.toString();
		}

    /**
	 * 商户公告
	 * @param shopId
	 * @param content
	 * @return
	 */
	@RequestMapping(value="/shopAnnouncementSet",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String shopAnnouncementSet(HttpServletRequest request,@RequestParam String shopId,@RequestParam String content){
	       JSONObject json=baiduShopService.shopAnnouncementSet(shopId,content,"");
			return json.toString();
		}

    /**
	 * 查看商户代码
	 * @param shopId
	 * @return
	 */
	@RequestMapping(value="/shopCode",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String shopCode(HttpServletRequest request,@RequestParam String shopId){
	       JSONObject json=baiduShopService.shopCode(shopId,"");
			return json.toString();
		}

    /**
	 * 确认订单
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/orderConfirm",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String orderConfirm(HttpServletRequest request,@RequestParam Long orderId){
        JSONObject json=baiduOrderService.orderConfirm(orderId,"");
		return json.toString();
	}

    /**
	 * 取消订单
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/orderCancel",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String orderCancel(HttpServletRequest request,@RequestParam Long orderId,@RequestParam int type,String reason){
        JSONObject json=baiduOrderService.orderCancel(orderId,type,reason,"");
		return json.toString();
	}

    /**
	 * 完成订单
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/orderComplete",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String orderComplete(HttpServletRequest request,@RequestParam Long orderId){
        JSONObject json=baiduOrderService.orderComplete(orderId,"");
		return json.toString();
	}

    /**
	 * 查看订单详情
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/getOrderDetail",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String getOrderDetail(HttpServletRequest request,@RequestParam Long orderId){
        JSONObject json=baiduOrderService.getOrderDetail(orderId,"");
		return json.toString();
	}

    /**
	 * 查看订单列表
	 * @param start_time
	 * @param end_time
	 * @param status
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/getOrderList",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String getOrderList(HttpServletRequest request,@RequestParam int start_time,@RequestParam int end_time,@RequestParam String status,@RequestParam int page){
        JSONObject json=baiduOrderService.getOrderList(start_time,end_time,status,page,"");
		return json.toString();
	}

    /**
	 * 扫码取餐
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/orderGetScan",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String orderGetScan(HttpServletRequest request,@RequestParam Long orderId){
        JSONObject json=baiduOrderService.orderGetScan(orderId,"");
		return json.toString();
	}

    /**
	 * 锁定订单（非开放）
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/orderLock",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String orderLock(HttpServletRequest request,@RequestParam Long orderId){
        JSONObject json=baiduOrderService.orderLock(orderId,"");
		return json.toString();
	}

    /**
	 * 订单退款
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/orderRefund",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String orderRefund(HttpServletRequest request,@RequestParam Long orderId){
        JSONObject json=baiduOrderService.orderRefund(orderId,"");
		return json.toString();
	}

    /**
	 * 添加订单(下行接口)邻家提供
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String createOrder(HttpServletRequest request){
		BaiduData bdData=BdOrderTestData.getBaiduData();
        JSONObject json=baiduOrderService.createOrder(bdData, "");
		return json.toString();
	}
	
	/**
	 * 查看订单状态(下行接口)邻家提供
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/orderStatusQuery",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String orderStatusQuery(HttpServletRequest request,@RequestParam Long orderId){
        JSONObject json=baiduOrderService.orderStatusQuery(orderId,"");
		return json.toString();
	}

    /**
	 * 订单状态推送(下行接口)邻家提供
	 * @param orderId
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/orderStatusPush",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String orderStatusPush(HttpServletRequest request,@RequestParam Long orderId,@RequestParam int status){
        JSONObject json=baiduOrderService.orderStatusPush(orderId,status,"");
		return json.toString();
	}

    /**
	 * 插入订单数据
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping("/insertBdOrder")
	@ResponseBody
	public String insertBdOrder(HttpServletRequest request,@RequestBody BaiduData bd){
        JSONObject json=baiduOrderService.insertBdOrder(bd);
		return json.toString();
	}

    /**
	 * 更新订单数据
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping(value="/updateBdOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String updateBdOrder(HttpServletRequest request,@RequestBody BaiduData bd){
        JSONObject json=baiduOrderService.updateBdOrder(bd);
		return json.toString();
	}

    /**
	 * 删除订单数据
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping(value="/deleteBdOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String deleteBdOrder(HttpServletRequest request,@RequestParam Long orderId){
        JSONObject json=baiduOrderService.deleteBdOrder(orderId);
		return json.toString();
	}

    /**
	 * 查询订单数据
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping(value="/selectBdOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String selectBdOrder(HttpServletRequest request,@RequestParam Long orderId,@RequestParam String shopId){
        JSONObject json=baiduOrderService.selectBdOrder(orderId,shopId);
		return json.toString();
	}

    /**
	 * 查询订单数据
	 * xiangsy
	 * 2016年7月29日 下午16:45:33
	 * @param query
	 * @return
	 */
	@RequestMapping(value="/shopBdOrder",produces ="application/json;charset=UTF-8")
	@ResponseBody
	public String shopBdOrder(HttpServletRequest request,@RequestParam String shopId){
        return baiduOrderService.selectBdOrderlist(shopId);
	}
}
