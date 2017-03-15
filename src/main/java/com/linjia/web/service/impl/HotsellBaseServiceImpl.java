package com.linjia.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linjia.base.dao.BaseDao;
import com.linjia.base.service.impl.BaseServiceImpl;
import com.linjia.constants.Constants;
import com.linjia.web.dao.HotsellBaseMapper;
import com.linjia.web.dao.HotsellProductMapper;
import com.linjia.web.model.HotsellBase;
import com.linjia.web.model.HotsellProduct;
import com.linjia.web.model.Product;
import com.linjia.web.query.HotsellProductQuery;
import com.linjia.web.service.HotsellService;

@Service
@Transactional
public class HotsellBaseServiceImpl extends BaseServiceImpl<HotsellBase, Long> implements HotsellService {

	@Resource
	private HotsellBaseMapper mapper;

	@Resource
	private HotsellProductMapper hotsellProductMapper;
	
	@Autowired
	private ProductServiceImpl productServiceImpl;

	@Override
	public BaseDao<HotsellBase, Long> getDao() {
		return mapper;
	}

	@Override	
	public Map<String,Object> selectByPageList(HotsellProductQuery query) {
		Map<String,Object> result = new HashMap<String,Object>();
		if(query.getBaseId() == null || query.getBaseId() == 0){
			//查询当前正在参加活动的baseId
			HotsellBase hotsellBase = mapper.selectCurrentActivity();
			if(hotsellBase == null){
				return null;
			}
			
			query.setBaseId(hotsellBase.getId());
			result.put("bannerPath", hotsellBase.getBannerPath());
		}
		List<HotsellProduct> productList = hotsellProductMapper.selectByBaseId(query);
		if (productList != null && productList.size() > 0) {
			for (HotsellProduct item : productList) {
				if (productServiceImpl.getCurrentQty(item.getId(), item.getpCode(), query.getMallCode()) > 0) {
					item.setQtyStatus(Constants.QTY_STATUS.NORMAL);
				} else {
					item.setQtyStatus(Constants.QTY_STATUS.LOW);
				}
			}
		}
		result.put("productList", productList);
		return result;
	}


}
