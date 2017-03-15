package com.linjia.web.model;

import java.math.BigDecimal;
import java.util.Date;

public class NewProductRecommend {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_new_product_recommend.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_new_product_recommend.product_id
     *
     * @mbggenerated
     */
    private Long productId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_new_product_recommend.p_code
     *
     * @mbggenerated
     */
    private String pCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_new_product_recommend.p_name
     *
     * @mbggenerated
     */
    private String pName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_new_product_recommend.sale_price
     *
     * @mbggenerated
     */
    private BigDecimal salePrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_new_product_recommend.market_price
     *
     * @mbggenerated
     */
    private BigDecimal marketPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_new_product_recommend.image_path
     *
     * @mbggenerated
     */
    private String imagePath;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_new_product_recommend.online
     *
     * @mbggenerated
     */
    private Boolean online;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_new_product_recommend.sort_index
     *
     * @mbggenerated
     */
    private Integer sortIndex;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_new_product_recommend.deleted
     *
     * @mbggenerated
     */
    private Boolean deleted;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_new_product_recommend.cre_date
     *
     * @mbggenerated
     */
    private Date creDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_new_product_recommend.update_date
     *
     * @mbggenerated
     */
    private Date updateDate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_new_product_recommend.id
     *
     * @return the value of b_new_product_recommend.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_new_product_recommend.id
     *
     * @param id the value for b_new_product_recommend.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_new_product_recommend.product_id
     *
     * @return the value of b_new_product_recommend.product_id
     *
     * @mbggenerated
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_new_product_recommend.product_id
     *
     * @param productId the value for b_new_product_recommend.product_id
     *
     * @mbggenerated
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_new_product_recommend.p_code
     *
     * @return the value of b_new_product_recommend.p_code
     *
     * @mbggenerated
     */
    public String getpCode() {
        return pCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_new_product_recommend.p_code
     *
     * @param pCode the value for b_new_product_recommend.p_code
     *
     * @mbggenerated
     */
    public void setpCode(String pCode) {
        this.pCode = pCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_new_product_recommend.p_name
     *
     * @return the value of b_new_product_recommend.p_name
     *
     * @mbggenerated
     */
    public String getpName() {
        return pName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_new_product_recommend.p_name
     *
     * @param pName the value for b_new_product_recommend.p_name
     *
     * @mbggenerated
     */
    public void setpName(String pName) {
        this.pName = pName == null ? null : pName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_new_product_recommend.sale_price
     *
     * @return the value of b_new_product_recommend.sale_price
     *
     * @mbggenerated
     */
    public BigDecimal getSalePrice() {
        return salePrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_new_product_recommend.sale_price
     *
     * @param salePrice the value for b_new_product_recommend.sale_price
     *
     * @mbggenerated
     */
    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_new_product_recommend.market_price
     *
     * @return the value of b_new_product_recommend.market_price
     *
     * @mbggenerated
     */
    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_new_product_recommend.market_price
     *
     * @param marketPrice the value for b_new_product_recommend.market_price
     *
     * @mbggenerated
     */
    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_new_product_recommend.image_path
     *
     * @return the value of b_new_product_recommend.image_path
     *
     * @mbggenerated
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_new_product_recommend.image_path
     *
     * @param imagePath the value for b_new_product_recommend.image_path
     *
     * @mbggenerated
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath == null ? null : imagePath.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_new_product_recommend.online
     *
     * @return the value of b_new_product_recommend.online
     *
     * @mbggenerated
     */
    public Boolean getOnline() {
        return online;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_new_product_recommend.online
     *
     * @param online the value for b_new_product_recommend.online
     *
     * @mbggenerated
     */
    public void setOnline(Boolean online) {
        this.online = online;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_new_product_recommend.sort_index
     *
     * @return the value of b_new_product_recommend.sort_index
     *
     * @mbggenerated
     */
    public Integer getSortIndex() {
        return sortIndex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_new_product_recommend.sort_index
     *
     * @param sortIndex the value for b_new_product_recommend.sort_index
     *
     * @mbggenerated
     */
    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_new_product_recommend.deleted
     *
     * @return the value of b_new_product_recommend.deleted
     *
     * @mbggenerated
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_new_product_recommend.deleted
     *
     * @param deleted the value for b_new_product_recommend.deleted
     *
     * @mbggenerated
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_new_product_recommend.cre_date
     *
     * @return the value of b_new_product_recommend.cre_date
     *
     * @mbggenerated
     */
    public Date getCreDate() {
        return creDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_new_product_recommend.cre_date
     *
     * @param creDate the value for b_new_product_recommend.cre_date
     *
     * @mbggenerated
     */
    public void setCreDate(Date creDate) {
        this.creDate = creDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_new_product_recommend.update_date
     *
     * @return the value of b_new_product_recommend.update_date
     *
     * @mbggenerated
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_new_product_recommend.update_date
     *
     * @param updateDate the value for b_new_product_recommend.update_date
     *
     * @mbggenerated
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    
    
    /**
     * 安全库存
     */
    private Integer safeQuantity;
    
    private Integer pSendType;
    
    /**
	 * 购物车每条记录的状态
	 * (0:正常；1：库存不足)
	 */
	private int itemStatus;

	public Integer getSafeQuantity() {
		return safeQuantity;
	}

	public void setSafeQuantity(Integer safeQuantity) {
		this.safeQuantity = safeQuantity;
	}

	public Integer getpSendType() {
		return pSendType;
	}

	public void setpSendType(Integer pSendType) {
		this.pSendType = pSendType;
	}

	public int getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(int itemStatus) {
		this.itemStatus = itemStatus;
	}

    
}