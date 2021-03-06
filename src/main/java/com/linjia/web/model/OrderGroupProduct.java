package com.linjia.web.model;

import java.math.BigDecimal;
import java.util.List;

public class OrderGroupProduct {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group_product.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group_product.group_id
     *
     * @mbggenerated
     */
    private Long groupId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group_product.product_id
     *
     * @mbggenerated
     */
    private Long productId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group_product.p_code
     *
     * @mbggenerated
     */
    private String pCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group_product.p_name
     *
     * @mbggenerated
     */
    private String pName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group_product.quantity
     *
     * @mbggenerated
     */
    private Integer quantity;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group_product.item_price
     *
     * @mbggenerated
     */
    private BigDecimal itemPrice;
    
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group_product.item_price
     *
     * @mbggenerated
     */
    private Integer pSendType;
    
    /**
     * 商品图片
     */
    private String imagePath;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group_product.id
     *
     * @return the value of b_order_group_product.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group_product.id
     *
     * @param id the value for b_order_group_product.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group_product.group_id
     *
     * @return the value of b_order_group_product.group_id
     *
     * @mbggenerated
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group_product.group_id
     *
     * @param groupId the value for b_order_group_product.group_id
     *
     * @mbggenerated
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group_product.product_id
     *
     * @return the value of b_order_group_product.product_id
     *
     * @mbggenerated
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group_product.product_id
     *
     * @param productId the value for b_order_group_product.product_id
     *
     * @mbggenerated
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group_product.p_code
     *
     * @return the value of b_order_group_product.p_code
     *
     * @mbggenerated
     */
    public String getpCode() {
        return pCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group_product.p_code
     *
     * @param pCode the value for b_order_group_product.p_code
     *
     * @mbggenerated
     */
    public void setpCode(String pCode) {
        this.pCode = pCode == null ? null : pCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group_product.p_name
     *
     * @return the value of b_order_group_product.p_name
     *
     * @mbggenerated
     */
    public String getpName() {
        return pName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group_product.p_name
     *
     * @param pName the value for b_order_group_product.p_name
     *
     * @mbggenerated
     */
    public void setpName(String pName) {
        this.pName = pName == null ? null : pName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group_product.quantity
     *
     * @return the value of b_order_group_product.quantity
     *
     * @mbggenerated
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group_product.quantity
     *
     * @param quantity the value for b_order_group_product.quantity
     *
     * @mbggenerated
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group_product.item_price
     *
     * @return the value of b_order_group_product.item_price
     *
     * @mbggenerated
     */
    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group_product.item_price
     *
     * @param itemPrice the value for b_order_group_product.item_price
     *
     * @mbggenerated
     */
    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

	public Integer getpSendType() {
		return pSendType;
	}

	public void setpSendType(Integer pSendType) {
		this.pSendType = pSendType;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
    
	/** 商品优惠前总价格* */
	private BigDecimal itemTotalPrice;
	/** 单件商品价格* */
	private BigDecimal productPrice;
	/** 商品优惠金额* */
	private BigDecimal discountAmount;
	/** 整单优惠分摊金额* */
	private BigDecimal apportedDiscountAmount;
	/** 是否是商品券商品：0否，1是* */
	private Integer isSpq;

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getApportedDiscountAmount() {
		return apportedDiscountAmount;
	}

	public void setApportedDiscountAmount(BigDecimal apportedDiscountAmount) {
		this.apportedDiscountAmount = apportedDiscountAmount;
	}

	public BigDecimal getItemTotalPrice() {
		return itemTotalPrice;
	}

	public void setItemTotalPrice(BigDecimal itemTotalPrice) {
		this.itemTotalPrice = itemTotalPrice;
	}

	public Integer getIsSpq() {
		return isSpq;
	}

	public void setIsSpq(Integer isSpq) {
		this.isSpq = isSpq;
	}
    
	/** b_trade_product表主键id,多个以逗号分隔* */
	private String tradeProductIds;
	
	/** 活动表的主键id(商品参加的加钱换购活动)* */
	private Long tradeActivityId;
	
	/** 换购商品列表* */
	private List<ActivityTradeProduct> activityTradeProductList;

	public String getTradeProductIds() {
		return tradeProductIds;
	}

	public void setTradeProductIds(String tradeProductIds) {
		this.tradeProductIds = tradeProductIds;
	}

	public Long getTradeActivityId() {
		return tradeActivityId;
	}

	public void setTradeActivityId(Long tradeActivityId) {
		this.tradeActivityId = tradeActivityId;
	}

	public List<ActivityTradeProduct> getActivityTradeProductList() {
		return activityTradeProductList;
	}

	public void setActivityTradeProductList(List<ActivityTradeProduct> activityTradeProductList) {
		this.activityTradeProductList = activityTradeProductList;
	}
	
	
	
}