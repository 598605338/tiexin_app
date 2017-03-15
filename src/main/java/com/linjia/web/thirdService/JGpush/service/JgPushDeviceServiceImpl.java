package com.linjia.web.thirdService.JGpush.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.constants.Application;
import com.linjia.tools.JSONUtil;
import com.linjia.web.dao.JgPushDeviceMappe;
import com.linjia.web.model.JgPushCustomer;
import com.linjia.web.thirdService.JGpush.constant.PushConstant;
import com.linjia.web.thirdService.JGpush.model.PushModel;
import com.linjia.web.thirdService.JGpush.push.PushClientFactory;

@Service
@Transactional
public class JgPushDeviceServiceImpl implements JgPushDeviceService {

	@Resource
	private JgPushDeviceMappe mapper;

	@Override
	public int insertJgPush(JgPushCustomer jgPushCustomer) {
		Map<String,String> paramMap=new HashMap<String, String>();
		paramMap.put("registration_id",jgPushCustomer.getRegistration_id());
		mapper.deleteJgPush(paramMap);
		int num=mapper.insertJgPush(jgPushCustomer);
		return num;
	}

	@Override
	public int deleteJgPush(Map<String,String> map) {
		return mapper.deleteJgPush(map);
	}

	@Override
	public int updateJgPush(JgPushCustomer jgPushCustomer) {
		return mapper.updateJgPush(jgPushCustomer);
	}

	@Override
	public List<JgPushCustomer> selectJgPush(String shop_id, String regId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mall_code", shop_id);
		map.put("registration_id", regId);
		List<JgPushCustomer> list = mapper.selectJgPush(map);
		return list;
	}

	@Override
	public List<String> selectJgPushIdList(String shop_ids) {
		return mapper.selectJgPushIdList(shop_ids);
	}

	@Override
	public int pushMessage(String shop_ids, String type, String orderSource) {
		int num = 0;

		// 测试环境直接返回
		if (Application.isTest) {
			return 1;
		}

		try {
			List<String> ids = mapper.selectJgPushIdList(shop_ids);
			if (ids != null && ids.size() > 0) {
				PushModel model = new PushModel();
				model.setType(type);
				model.setOrderSource(orderSource);
				model.setMessage(PushConstant.getMessage(type));
				String strPush = JSONUtil.toJson(model);
				System.out.println(strPush);
				num = PushClientFactory.push(ids, model, false);
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return num;
	}

	@Override
	public int pushMessageByOrderId(Long orderId, String type, String orderSource) {
		int num = 0;

		// 测试环境直接返回
		if (Application.isTest && !"4".endsWith(orderSource)) {
			return 1;
		}

		try {
			String shop_id = "";
			if ("1".equals(orderSource)) {
				shop_id = mapper.selectLjShopIdbyOrderid(orderId + "");
			} else if ("2".equals(orderSource)) {
				shop_id = mapper.selectMtShopIdbyOrderid(orderId + "");
			} else if ("3".equals(orderSource)) {
				shop_id = mapper.selectBdShopIdbyOrderid(orderId + "");
			} else if ("4".equals(orderSource)) {
				shop_id = mapper.selectJdShopIdbyOrderid(orderId);
			} else if ("5".equals(orderSource)) {
				shop_id = mapper.selectElemeShopIdbyOrderid(orderId+"");
			}
			// 如果查询到的店铺id不为空，则查询设备id，推送消息
			if (!shop_id.isEmpty()) {
				List<String> ids = mapper.selectJgPushIdList(shop_id);
				if (ids != null && ids.size() > 0) {
					PushModel model = new PushModel();
					model.setType(type);
					model.setOrderSource(orderSource);
					model.setMessage(PushConstant.getMessage(type));
					String strPush = JSONUtil.toJson(model);
					System.out.println(strPush);
					num = PushClientFactory.push(ids, model, false);
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return num;
	}

	@Override
	public String selectLjShopIdbyOrderid(String order_id) {
		return mapper.selectLjShopIdbyOrderid(order_id);
	}

	@Override
	public String selectMtShopIdbyOrderid(String order_id) {
		return mapper.selectMtShopIdbyOrderid(order_id);
	}

	@Override
	public String selectBdShopIdbyOrderid(String order_id) {
		return mapper.selectBdShopIdbyOrderid(order_id);
	}

	@Override
	public String selectElemeShopIdbyOrderid(String order_id) {
		return mapper.selectElemeShopIdbyOrderid(order_id);
	}
}
