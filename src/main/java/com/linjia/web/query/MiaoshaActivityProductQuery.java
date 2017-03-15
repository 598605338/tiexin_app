package com.linjia.web.query;

import java.math.BigDecimal;
import java.util.Date;

import com.linjia.base.query.Query;


/** 
 * @author  lixinling: 
 * @date 2016年7月6日 上午10:14:33 
 * @version 1.0 
 */
public class MiaoshaActivityProductQuery extends Query {
	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column b_activity_shangou_product.id
	     *
	     * @mbggenerated
	     */
	    private Long id;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column b_activity_shangou_product.shangou_base_id
	     *
	     * @mbggenerated
	     */
	    private Long shangouBaseId;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column b_activity_shangou_product.panic_buying_start_time
	     *
	     * @mbggenerated
	     */
	    private Date panicBuyingStartTime;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column b_activity_shangou_product.panic_buying_end_time
	     *
	     * @mbggenerated
	     */
	    private Date panicBuyingEndTime;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column b_activity_shangou_product.p_code
	     *
	     * @mbggenerated
	     */
	    private String pCode;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column b_activity_shangou_product.p_name
	     *
	     * @mbggenerated
	     */
	    private String pName;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column b_activity_shangou_product.market_price
	     *
	     * @mbggenerated
	     */
	    private BigDecimal marketPrice;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column b_activity_shangou_product.p_unit
	     *
	     * @mbggenerated
	     */
	    private String pUnit;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column b_activity_shangou_product.pb_price
	     *
	     * @mbggenerated
	     */
	    private BigDecimal pbPrice;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column b_activity_shangou_product.quantity
	     *
	     * @mbggenerated
	     */
	    private Integer quantity;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column b_activity_shangou_product.limit_quantity
	     *
	     * @mbggenerated
	     */
	    private Integer limitQuantity;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column b_activity_shangou_product.sort_index
	     *
	     * @mbggenerated
	     */
	    private Boolean sortIndex;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column b_activity_shangou_product.image_path
	     *
	     * @mbggenerated
	     */
	    private String imagePath;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column b_activity_shangou_product.detail_path
	     *
	     * @mbggenerated
	     */
	    private String detailPath;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column b_activity_shangou_product.deleted
	     *
	     * @mbggenerated
	     */
	    private Boolean deleted;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column b_activity_shangou_product.cre_date
	     *
	     * @mbggenerated
	     */
	    private Date creDate;

	    /**
	     * This field was generated by MyBatis Generator.
	     * This field corresponds to the database column b_activity_shangou_product.update_date
	     *
	     * @mbggenerated
	     */
	    private Date updateDate;

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method returns the value of the database column b_activity_shangou_product.id
	     *
	     * @return the value of b_activity_shangou_product.id
	     *
	     * @mbggenerated
	     */
	    public Long getId() {
	        return id;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method sets the value of the database column b_activity_shangou_product.id
	     *
	     * @param id the value for b_activity_shangou_product.id
	     *
	     * @mbggenerated
	     */
	    public void setId(Long id) {
	        this.id = id;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method returns the value of the database column b_activity_shangou_product.shangou_base_id
	     *
	     * @return the value of b_activity_shangou_product.shangou_base_id
	     *
	     * @mbggenerated
	     */
	    public Long getShangouBaseId() {
	        return shangouBaseId;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method sets the value of the database column b_activity_shangou_product.shangou_base_id
	     *
	     * @param shangouBaseId the value for b_activity_shangou_product.shangou_base_id
	     *
	     * @mbggenerated
	     */
	    public void setShangouBaseId(Long shangouBaseId) {
	        this.shangouBaseId = shangouBaseId;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method returns the value of the database column b_activity_shangou_product.panic_buying_start_time
	     *
	     * @return the value of b_activity_shangou_product.panic_buying_start_time
	     *
	     * @mbggenerated
	     */
	    public Date getPanicBuyingStartTime() {
	        return panicBuyingStartTime;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method sets the value of the database column b_activity_shangou_product.panic_buying_start_time
	     *
	     * @param panicBuyingStartTime the value for b_activity_shangou_product.panic_buying_start_time
	     *
	     * @mbggenerated
	     */
	    public void setPanicBuyingStartTime(Date panicBuyingStartTime) {
	        this.panicBuyingStartTime = panicBuyingStartTime;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method returns the value of the database column b_activity_shangou_product.panic_buying_end_time
	     *
	     * @return the value of b_activity_shangou_product.panic_buying_end_time
	     *
	     * @mbggenerated
	     */
	    public Date getPanicBuyingEndTime() {
	        return panicBuyingEndTime;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method sets the value of the database column b_activity_shangou_product.panic_buying_end_time
	     *
	     * @param panicBuyingEndTime the value for b_activity_shangou_product.panic_buying_end_time
	     *
	     * @mbggenerated
	     */
	    public void setPanicBuyingEndTime(Date panicBuyingEndTime) {
	        this.panicBuyingEndTime = panicBuyingEndTime;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method returns the value of the database column b_activity_shangou_product.p_code
	     *
	     * @return the value of b_activity_shangou_product.p_code
	     *
	     * @mbggenerated
	     */
	    public String getpCode() {
	        return pCode;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method sets the value of the database column b_activity_shangou_product.p_code
	     *
	     * @param pCode the value for b_activity_shangou_product.p_code
	     *
	     * @mbggenerated
	     */
	    public void setpCode(String pCode) {
	        this.pCode = pCode;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method returns the value of the database column b_activity_shangou_product.p_name
	     *
	     * @return the value of b_activity_shangou_product.p_name
	     *
	     * @mbggenerated
	     */
	    public String getpName() {
	        return pName;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method sets the value of the database column b_activity_shangou_product.p_name
	     *
	     * @param pName the value for b_activity_shangou_product.p_name
	     *
	     * @mbggenerated
	     */
	    public void setpName(String pName) {
	        this.pName = pName == null ? null : pName.trim();
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method returns the value of the database column b_activity_shangou_product.market_price
	     *
	     * @return the value of b_activity_shangou_product.market_price
	     *
	     * @mbggenerated
	     */
	    public BigDecimal getMarketPrice() {
	        return marketPrice;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method sets the value of the database column b_activity_shangou_product.market_price
	     *
	     * @param marketPrice the value for b_activity_shangou_product.market_price
	     *
	     * @mbggenerated
	     */
	    public void setMarketPrice(BigDecimal marketPrice) {
	        this.marketPrice = marketPrice;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method returns the value of the database column b_activity_shangou_product.p_unit
	     *
	     * @return the value of b_activity_shangou_product.p_unit
	     *
	     * @mbggenerated
	     */
	    public String getpUnit() {
	        return pUnit;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method sets the value of the database column b_activity_shangou_product.p_unit
	     *
	     * @param pUnit the value for b_activity_shangou_product.p_unit
	     *
	     * @mbggenerated
	     */
	    public void setpUnit(String pUnit) {
	        this.pUnit = pUnit == null ? null : pUnit.trim();
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method returns the value of the database column b_activity_shangou_product.pb_price
	     *
	     * @return the value of b_activity_shangou_product.pb_price
	     *
	     * @mbggenerated
	     */
	    public BigDecimal getPbPrice() {
	        return pbPrice;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method sets the value of the database column b_activity_shangou_product.pb_price
	     *
	     * @param pbPrice the value for b_activity_shangou_product.pb_price
	     *
	     * @mbggenerated
	     */
	    public void setPbPrice(BigDecimal pbPrice) {
	        this.pbPrice = pbPrice;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method returns the value of the database column b_activity_shangou_product.quantity
	     *
	     * @return the value of b_activity_shangou_product.quantity
	     *
	     * @mbggenerated
	     */
	    public Integer getQuantity() {
	        return quantity;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method sets the value of the database column b_activity_shangou_product.quantity
	     *
	     * @param quantity the value for b_activity_shangou_product.quantity
	     *
	     * @mbggenerated
	     */
	    public void setQuantity(Integer quantity) {
	        this.quantity = quantity;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method returns the value of the database column b_activity_shangou_product.limit_quantity
	     *
	     * @return the value of b_activity_shangou_product.limit_quantity
	     *
	     * @mbggenerated
	     */
	    public Integer getLimitQuantity() {
	        return limitQuantity;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method sets the value of the database column b_activity_shangou_product.limit_quantity
	     *
	     * @param limitQuantity the value for b_activity_shangou_product.limit_quantity
	     *
	     * @mbggenerated
	     */
	    public void setLimitQuantity(Integer limitQuantity) {
	        this.limitQuantity = limitQuantity;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method returns the value of the database column b_activity_shangou_product.sort_index
	     *
	     * @return the value of b_activity_shangou_product.sort_index
	     *
	     * @mbggenerated
	     */
	    public Boolean getSortIndex() {
	        return sortIndex;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method sets the value of the database column b_activity_shangou_product.sort_index
	     *
	     * @param sortIndex the value for b_activity_shangou_product.sort_index
	     *
	     * @mbggenerated
	     */
	    public void setSortIndex(Boolean sortIndex) {
	        this.sortIndex = sortIndex;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method returns the value of the database column b_activity_shangou_product.image_path
	     *
	     * @return the value of b_activity_shangou_product.image_path
	     *
	     * @mbggenerated
	     */
	    public String getImagePath() {
	        return imagePath;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method sets the value of the database column b_activity_shangou_product.image_path
	     *
	     * @param imagePath the value for b_activity_shangou_product.image_path
	     *
	     * @mbggenerated
	     */
	    public void setImagePath(String imagePath) {
	        this.imagePath = imagePath == null ? null : imagePath.trim();
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method returns the value of the database column b_activity_shangou_product.detail_path
	     *
	     * @return the value of b_activity_shangou_product.detail_path
	     *
	     * @mbggenerated
	     */
	    public String getDetailPath() {
	        return detailPath;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method sets the value of the database column b_activity_shangou_product.detail_path
	     *
	     * @param detailPath the value for b_activity_shangou_product.detail_path
	     *
	     * @mbggenerated
	     */
	    public void setDetailPath(String detailPath) {
	        this.detailPath = detailPath == null ? null : detailPath.trim();
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method returns the value of the database column b_activity_shangou_product.deleted
	     *
	     * @return the value of b_activity_shangou_product.deleted
	     *
	     * @mbggenerated
	     */
	    public Boolean getDeleted() {
	        return deleted;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method sets the value of the database column b_activity_shangou_product.deleted
	     *
	     * @param deleted the value for b_activity_shangou_product.deleted
	     *
	     * @mbggenerated
	     */
	    public void setDeleted(Boolean deleted) {
	        this.deleted = deleted;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method returns the value of the database column b_activity_shangou_product.cre_date
	     *
	     * @return the value of b_activity_shangou_product.cre_date
	     *
	     * @mbggenerated
	     */
	    public Date getCreDate() {
	        return creDate;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method sets the value of the database column b_activity_shangou_product.cre_date
	     *
	     * @param creDate the value for b_activity_shangou_product.cre_date
	     *
	     * @mbggenerated
	     */
	    public void setCreDate(Date creDate) {
	        this.creDate = creDate;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method returns the value of the database column b_activity_shangou_product.update_date
	     *
	     * @return the value of b_activity_shangou_product.update_date
	     *
	     * @mbggenerated
	     */
	    public Date getUpdateDate() {
	        return updateDate;
	    }

	    /**
	     * This method was generated by MyBatis Generator.
	     * This method sets the value of the database column b_activity_shangou_product.update_date
	     *
	     * @param updateDate the value for b_activity_shangou_product.update_date
	     *
	     * @mbggenerated
	     */
	    public void setUpdateDate(Date updateDate) {
	        this.updateDate = updateDate;
	    }
	    
	    private String userId;
	    private String mallCode;

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getMallCode() {
			return mallCode;
		}

		public void setMallCode(String mallCode) {
			this.mallCode = mallCode;
		}
	    
	    
	
}