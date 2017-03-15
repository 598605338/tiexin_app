package com.linjia.web.controller;

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
import com.linjia.web.model.LargeCatagory;
import com.linjia.web.model.Product;
import com.linjia.web.model.ProductDetail;
import com.linjia.web.service.ProductBannerImagesService;
import com.linjia.web.service.ProductService;
import com.linjia.web.service.ProductSpecService;
import com.linjia.web.service.ProductTagsService;

/**
 * 商品模块
 * 
 * @author lixinling
 *
 */
@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductBannerImagesService productBannerImagesService;

	@Autowired
	private ProductTagsService productTagsService;

	/**
	 * 查看商品详情
	 * lixinling 
	 * 2016年9月7日 上午10:03:21
	 * @param request
	 * @param id 商品id
	 * @return
	 */
	@RequestMapping("/getProductDetail")
	@ResponseBody
	public Object getProductDetail(HttpServletRequest request, @RequestParam(value = "id", required = true) Long id, String mallCode, String userId) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();
		try {
			ProductDetail productDetail = productService.selectDetailByPrimaryKey(id, mallCode, userId);

			if (productDetail != null) {
				int qty = productService.getCurrentQty(productDetail.getProductId(), productDetail.getpCode(), mallCode);
				productDetail.setQuantity(qty);
				if (qty > 0) {
					productDetail.setQtyStatus(Constants.QTY_STATUS.NORMAL);
				} else {
					productDetail.setQtyStatus(Constants.QTY_STATUS.LOW);
				}
				resultData.put("productDetail", productDetail);

				// 取得商品轮播图
				List<String> bannerImageList = productBannerImagesService.selectAllByProductId(id);
				resultData.put("bannerImageList", bannerImageList);

				// 取得商品的标签
				List<String> tagList = productTagsService.selectTagsByProductId(id);
				resultData.put("tagList", tagList);

			}
			resMap.put("resultData", resultData);
			Util.writeSuccess(resMap);
			Util.writeOk(resMap);
		} catch (Exception e) {
			Util.writeFail(resMap);
			Util.writeError(resMap);
		}
		return resMap;
	}
	

}
