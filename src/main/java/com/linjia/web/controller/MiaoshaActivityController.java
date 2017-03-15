package com.linjia.web.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linjia.base.controller.BaseController;
import com.linjia.constants.Constants;
import com.linjia.tools.DateComFunc;
import com.linjia.tools.Util;
import com.linjia.web.model.MiaoshaActivityBase;
import com.linjia.web.model.MiaoshaActivityProduct;
import com.linjia.web.query.MiaoshaActivityBaseQuery;
import com.linjia.web.query.MiaoshaActivityProductQuery;
import com.linjia.web.service.MiaoshaActivityProductService;
import com.linjia.web.service.MiaoshaActivityService;

/**
 * 秒杀模块
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaActivityController extends BaseController {

	@Autowired
	private MiaoshaActivityService miaoshaActivityService;
	@Autowired
	private MiaoshaActivityProductService miaoshaActivityProductService;

	/**
	 * 根据当前时间或取得秒杀数据
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/getMiaoshaRecord")
	@ResponseBody
	public Object getMiaoshaRecord(HttpServletRequest request, MiaoshaActivityBaseQuery query, String userId, String mallCode) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		try {
			// 根据所选的时间节点，取得相应时间节点的数据信息；
			// 初次进来根据当前时间来设置相应的节点数据
			setTimeNode(query);

			MiaoshaActivityBase miaoshaActivityBase = miaoshaActivityService.selectOne(query);
			if (miaoshaActivityBase != null) {

				// 进行状态和倒计时的赋值
				setPanicStatus(miaoshaActivityBase);

				// 设置时间节点值
				miaoshaActivityBase.setTimeNode(query.getTimeNode());

				// 取得当前时间节点第一页商品数据
				MiaoshaActivityProductQuery productQuery = new MiaoshaActivityProductQuery();
				productQuery.setShangouBaseId(miaoshaActivityBase.getId());
//				productQuery.setPageIndex(0);
//				productQuery.setPageSize(10);
				productQuery.setUserId(userId);
				productQuery.setMallCode(mallCode);
				List<MiaoshaActivityProduct> productList = miaoshaActivityProductService.selectAllByBaseId(productQuery);

				resultData.put("miaoshaActivityBase", miaoshaActivityBase);
				resultData.put("productList", productList);
				resMap.put("resultData", resultData);
				Util.writeSuccess(resMap);
				Util.writeOk(resMap);
			}else{
				resMap.put("message", "当前时间点没有正在进行的秒杀活动");
				Util.writeError(resMap);
			}
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
			e.printStackTrace();
		}
		return resMap;
	}

	/**
	 * 根据shangouBaseId取得每页秒杀数据
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/getMiaoshaProductList")
	@ResponseBody
	public Object getMiaoshaProductList(HttpServletRequest request, MiaoshaActivityProductQuery query) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			List<MiaoshaActivityProduct> productList = miaoshaActivityProductService.selectAllByBaseId(query);
			if (productList != null) {
				resMap.put("resultData", productList);
			}
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
		}
		return resMap;
	}

	/**
	 * 根据商品id，判断该商品是否正在进行秒杀
	 * lixinling
	 * 2016年7月18日 上午10:14:46
	 * 
	 * @param request
	 * @param product_id
	 * @return
	 */
	public MiaoshaActivityProduct getPanicingProductById(HttpServletRequest request, int product_id) {

		return miaoshaActivityProductService.getPanicingProductByProductId(product_id);
	}

	/**
	 * 根据所选的时间节点，取得相应时间节点的数据信息；初次进来根据当前时间来设置相应的节点数据 
	 * lixinling 
	 * 2016年7月6日下午2:58:47
	 * 
	 * @param query
	 */
	public void setTimeNode(MiaoshaActivityBaseQuery query) {
		// 时间节点(1:9点；2:12点；3:15点；4:18点)
		int timeNode = query.getTimeNode();

		// 初次进来根据当前时间来显示相应的节点数据
		if (timeNode == 0) {
			// 当前小时点
			int hour = DateComFunc.getHour();

			if (hour < Constants.MIAOSHA_TIME.TWELVE) {
				timeNode = 1;
			} else if (Constants.MIAOSHA_TIME.TWELVE <= hour && hour < Constants.MIAOSHA_TIME.FIFTEEN) {
				timeNode = 2;
			} else if (Constants.MIAOSHA_TIME.FIFTEEN <= hour && hour <= Constants.MIAOSHA_TIME.EIGHTEEN) {
				timeNode = 3;
			} else if (Constants.MIAOSHA_TIME.EIGHTEEN <= hour) {
				timeNode = 4;
			}

			// 设置时间节点
			query.setTimeNode(timeNode);
		}

		// 对每个时间节点取得相应的节点数据信息
		switch (timeNode) {
		case 1:
			query.setPanicBuyingStartTime(DateComFunc.getSpecifyHourDate(new Date(), Constants.MIAOSHA_TIME.NINE));
			query.setPanicBuyingEndTime(DateComFunc.getSpecifyHourDate(new Date(), Constants.MIAOSHA_TIME.TWELVE));
			break;
		case 2:
			query.setPanicBuyingStartTime(DateComFunc.getSpecifyHourDate(new Date(), Constants.MIAOSHA_TIME.TWELVE));
			query.setPanicBuyingEndTime(DateComFunc.getSpecifyHourDate(new Date(), Constants.MIAOSHA_TIME.FIFTEEN));
			break;
		case 3:
			query.setPanicBuyingStartTime(DateComFunc.getSpecifyHourDate(new Date(), Constants.MIAOSHA_TIME.FIFTEEN));
			query.setPanicBuyingEndTime(DateComFunc.getSpecifyHourDate(new Date(), Constants.MIAOSHA_TIME.EIGHTEEN));
			break;
		case 4:
			query.setPanicBuyingStartTime(DateComFunc.getSpecifyHourDate(new Date(), Constants.MIAOSHA_TIME.EIGHTEEN));
			// 每个时间段为三小时
			query.setPanicBuyingEndTime(DateComFunc.getSpecifyHourDate(new Date(), Constants.MIAOSHA_TIME.EIGHTEEN + 3));
			break;
		default:
			query.setPanicBuyingStartTime(DateComFunc.getSpecifyHourDate(new Date(), Constants.MIAOSHA_TIME.NINE));
			query.setPanicBuyingEndTime(DateComFunc.getSpecifyHourDate(new Date(), Constants.MIAOSHA_TIME.TWELVE));
		}
	}

	/**
	 * 根据当前时间和抢购时间对抢购状态进行赋值
	 * 
	 * lixinling 2016年7月6日 下午2:58:47
	 * 
	 * @param miaoshaActivityBase
	 */
	public void setPanicStatus(MiaoshaActivityBase miaoshaActivityBase) {
		long panicStartTimes = miaoshaActivityBase.getPanicBuyingStartTime().getTime();
		long panicEndTimes = miaoshaActivityBase.getPanicBuyingEndTime().getTime();
		long nowTimes = Calendar.getInstance().getTimeInMillis();

		if (nowTimes < panicStartTimes) {
			miaoshaActivityBase.setPanicStatus(Constants.MIAOSHA_PANIC_STATUS.UNSTART);
			miaoshaActivityBase.setShowLabel1(Constants.MIAOSHA_SHOW_LABEL1.UNSTART);
			miaoshaActivityBase.setShowLabel2(Constants.MIAOSHA_SHOW_LABEL2.UNSTART);
			miaoshaActivityBase.setCountdownTimes(panicStartTimes - nowTimes);
		} else if (nowTimes > panicStartTimes && nowTimes < panicEndTimes) {
			miaoshaActivityBase.setPanicStatus(Constants.MIAOSHA_PANIC_STATUS.PANICING);
			miaoshaActivityBase.setShowLabel1(Constants.MIAOSHA_SHOW_LABEL1.PANICING);
			miaoshaActivityBase.setShowLabel2(Constants.MIAOSHA_SHOW_LABEL2.PANICING);
			miaoshaActivityBase.setCountdownTimes(panicEndTimes - nowTimes);
		} else if (nowTimes > panicEndTimes) {
			miaoshaActivityBase.setPanicStatus(Constants.MIAOSHA_PANIC_STATUS.ENDED);
			miaoshaActivityBase.setShowLabel1(Constants.MIAOSHA_SHOW_LABEL1.ENDED);
		}
	}
}
