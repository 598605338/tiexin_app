package com.linjia.web.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.constants.Constants;
import com.linjia.tools.DateComFunc;
import com.linjia.tools.Util;
import com.linjia.web.dao.ProductMapper;
import com.linjia.web.dao.TailGoodsClearMapper;
import com.linjia.web.model.TailGoodsClear;
import com.linjia.web.query.TailGoodsClearQuery;
import com.linjia.web.service.ProductService;
import com.linjia.web.service.TailGoodsClearService;

@Service
@Transactional
public class TailGoodsClearServiceImpl extends BaseServiceImpl<TailGoodsClear, Long> implements TailGoodsClearService {

	@Resource
	private TailGoodsClearMapper mapper;

	@Resource
	private ProductMapper productMapper;

	@Autowired
	private ProductService productService;

	@Override
	public BaseDao<TailGoodsClear, Long> getDao() {
		return mapper;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<TailGoodsClear> selectEveryDayClearList(TailGoodsClearQuery query) {
		List<TailGoodsClear> list = mapper.selectEveryDayClearList(query);

		// 已售罄的商品列表
		List<TailGoodsClear> lowList = new ArrayList<TailGoodsClear>();
		if (list != null && list.size() > 0) {
			Map<String, Object> params = new HashMap<String, Object>();
			for (int i = 0; i < list.size(); i++) {
				TailGoodsClear item = list.get(i);

				// 检索条件数
				int searchCondition = item.getSearchCondition();
				
				//检索开始时间
				Date searchTime = item.getSearchTime();
				Date now = new Date();
				// 取得erp库存数
				int storeQty = productService.getCurrentQty(item.getProductId(), item.getpCode(), query.getMallCode());
				if ((storeQty - searchCondition) <= 0) {
					// 不满足检索条件数时,在列表最后显示;
					// params.put("activityStatus",
					// Constants.TAIL_GOODS_CLEAR_ACTIVITY_STATUS.SELLOUT);
					// params.put("id", item.getId());
					// this.updateActivityStatusByPrimaryKey(params);

					item.setActivityStatus(Constants.TAIL_GOODS_CLEAR_ACTIVITY_STATUS.SELLOUT);
					// 将该商品从该列表中移除并追加到列表最后
					lowList.add(item);
				} else {
					// 设置剩余件数
					item.setQuantity(storeQty - searchCondition);
					
					//判断活动是否开始
					if(DateComFunc.compareDate(now, searchTime) < 0){
						item.setActivityStatus(Constants.TAIL_GOODS_CLEAR_ACTIVITY_STATUS.WAITTING);
					}
				}
			}

			if (lowList.size() > 0) {
				list.removeAll(lowList);
				list.addAll(lowList);
			}
		}
		return list;
	}

	@Override
	public List<TailGoodsClear> selectCurrentClearList(TailGoodsClearQuery query) {
		List<TailGoodsClear> list = mapper.selectCurrentClearList(query);

		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				TailGoodsClear item = list.get(i);

				int storeQty = 0;
				// 商品发货类型：0及时送；1次日达
				Integer pSendType = item.getpSendType();
				if (pSendType != null && pSendType == 0) {
					// 取得门店库存数
					storeQty = productService.getCurrentQty(item.getProductId(), item.getpCode(), query.getMallCode());
				} else {
					storeQty = productMapper.selectQuantityByPrimaryKey(item.getProductId());
				}
				if (storeQty <= 0) {
					item.setActivityStatus(Constants.TAIL_GOODS_CLEAR_ACTIVITY_STATUS.SELLOUT);
					item.setQuantity(0);
				} else {
					// 设置剩余件数
					item.setQuantity(item.getQuantity() <= storeQty ? item.getQuantity() : storeQty);
				}
			}
		}
		return list;
	}

	@Override
	public int updateActivityStatusByPrimaryKey(Map<String, Object> map) {
		return mapper.updateActivityStatusByPrimaryKey(map);
	}

	@Override
	public TailGoodsClear selectByProductId(Long productId, String mallCode) {
		TailGoodsClearQuery query = new TailGoodsClearQuery();
		query.setProductId(productId);
		query.setMallCode(mallCode);
		return mapper.selectByProductId(query);
	}

}
