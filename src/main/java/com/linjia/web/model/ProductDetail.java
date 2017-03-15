package com.linjia.web.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品详情Model
 * @author lixinling
 * 2016年9月7日 上午10:52:06
 */
public class ProductDetail {
	
	/**商品Id * */
	private Long productId;

	/**商品code * */
	private String pCode;

	/**商品名 * */
	private String name;

	/**商品描述 * */
	private String description;
	
	/**商品详情路径 * */
	private String detailPath;


	/**销售价格 * */
	private BigDecimal salePrice;

	/**市场价格 * */
	private BigDecimal marketPrice;
	
	/**秒杀价格 * */
	private BigDecimal pbPrice;

	/**商品发货类型：0及时送；1次日达 * */
	private Integer pSendType;

	/**安全库存数 * */
	private Integer safeQuantity;
	
	/**秒杀结束时间 * */
	private Date panicBuyingEndTime;
	
	/** 商品单位id* */
	private Integer unitId;
	
	/** 商品单位名* */
	private String unitName;
	
	/** 品牌id* */
	private Long brandId;

	/** 品牌名* */
	private String brandName;
	
	/** 保质期* */
	private String shelfLife;
	
	/** 库存状态(0:正常；1：库存不足)* */
	private Integer qtyStatus;
	
	/** 库存数量* */
	private Integer quantity;
	
	/** 本月销量* */
	private Integer saleNum;
	
	/** 是否关注:0未关注；大于0是已关注 */
	private Integer isFavourite;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getpCode() {
		return pCode;
	}

	public void setpCode(String pCode) {
		this.pCode = pCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public BigDecimal getPbPrice() {
		return pbPrice;
	}

	public void setPbPrice(BigDecimal pbPrice) {
		this.pbPrice = pbPrice;
	}

	public Integer getpSendType() {
		return pSendType;
	}

	public void setpSendType(Integer pSendType) {
		this.pSendType = pSendType;
	}

	public Integer getSafeQuantity() {
		return safeQuantity;
	}

	public void setSafeQuantity(Integer safeQuantity) {
		this.safeQuantity = safeQuantity;
	}

	public Date getPanicBuyingEndTime() {
		return panicBuyingEndTime;
	}

	public void setPanicBuyingEndTime(Date panicBuyingEndTime) {
		this.panicBuyingEndTime = panicBuyingEndTime;
	}

	public String getDetailPath() {
		return detailPath;
	}

	public void setDetailPath(String detailPath) {
		this.detailPath = detailPath;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getShelfLife() {
		return shelfLife;
	}

	public void setShelfLife(String shelfLife) {
		this.shelfLife = shelfLife;
	}

	public Integer getQtyStatus() {
		return qtyStatus;
	}

	public void setQtyStatus(Integer qtyStatus) {
		this.qtyStatus = qtyStatus;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(Integer saleNum) {
		this.saleNum = saleNum;
	}

	public Integer getIsFavourite() {
		return isFavourite;
	}

	public void setIsFavourite(Integer isFavourite) {
		this.isFavourite = isFavourite;
	}


}