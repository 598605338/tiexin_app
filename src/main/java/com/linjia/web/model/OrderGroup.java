package com.linjia.web.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderGroup {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.name
     *
     * @mbggenerated
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.total_product_num
     *
     * @mbggenerated
     */
    private Integer totalProductNum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.total_product_price
     *
     * @mbggenerated
     */
    private BigDecimal totalProductPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.total_price
     *
     * @mbggenerated
     */
    private BigDecimal totalPrice;
    
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.receive_phone
     *
     * @mbggenerated
     */
    private String userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.receive_name
     *
     * @mbggenerated
     */
    private String receiveName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.receive_phone
     *
     * @mbggenerated
     */
    private String receivePhone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.receive_address
     *
     * @mbggenerated
     */
    private String receiveAddress;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.send_type
     *
     * @mbggenerated
     */
    private Integer sendType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.send_date
     *
     * @mbggenerated
     */
    private Date sendDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.send_hour
     *
     * @mbggenerated
     */
    private String sendHour;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.mall_code
     *
     * @mbggenerated
     */
    private String mallCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.mall_name
     *
     * @mbggenerated
     */
    private String mallName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.mall_phone
     *
     * @mbggenerated
     */
    private String mallPhone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.send_price
     *
     * @mbggenerated
     */
    private BigDecimal sendPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.coupon_type
     *
     * @mbggenerated
     */
    private Integer couponType;
    
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.coupon_type
     *
     * @mbggenerated
     */
    private Long userCardCouponId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.is_full_subtract
     *
     * @mbggenerated
     */
    private Boolean isFullSubtract;
    private Boolean isFreeFreight;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.full_subtract_price
     *
     * @mbggenerated
     */
    private BigDecimal cardCouponPrice;
    
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.full_subtract_price
     *
     * @mbggenerated
     */
    private BigDecimal fullSubtractPrice;
    private BigDecimal freeFreightPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.pay_type
     *
     * @mbggenerated
     */
    private Integer payType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.preferential_price
     *
     * @mbggenerated
     */
    private BigDecimal preferentialPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.real_price
     *
     * @mbggenerated
     */
    private BigDecimal realPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.score
     *
     * @mbggenerated
     */
    private Integer score;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.pay_status
     *
     * @mbggenerated
     */
    private Integer payStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.start_time
     *
     * @mbggenerated
     */
    private Date startTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.end_time
     *
     * @mbggenerated
     */
    private Date endTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.order_group_status
     *
     * @mbggenerated
     */
    private Integer orderGroupStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.remark
     *
     * @mbggenerated
     */
    private String remark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.cre_date
     *
     * @mbggenerated
     */
    private Date creDate;
    
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.cre_date
     *
     * @mbggenerated
     */
    private String transactionId;
    
    //配送员名称
    private String dmName;
    
    //配送员电话
    private String dmMobile;
    
    //物流类型(1,自配送；2，达达；3，百度)
    private String logisType;
    
    

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.id
     *
     * @return the value of b_order_group.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.id
     *
     * @param id the value for b_order_group.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.name
     *
     * @return the value of b_order_group.name
     *
     * @mbggenerated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.name
     *
     * @param name the value for b_order_group.name
     *
     * @mbggenerated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.total_product_num
     *
     * @return the value of b_order_group.total_product_num
     *
     * @mbggenerated
     */
    public Integer getTotalProductNum() {
        return totalProductNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.total_product_num
     *
     * @param totalProductNum the value for b_order_group.total_product_num
     *
     * @mbggenerated
     */
    public void setTotalProductNum(Integer totalProductNum) {
        this.totalProductNum = totalProductNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.total_product_price
     *
     * @return the value of b_order_group.total_product_price
     *
     * @mbggenerated
     */
    public BigDecimal getTotalProductPrice() {
        return totalProductPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.total_product_price
     *
     * @param totalProductPrice the value for b_order_group.total_product_price
     *
     * @mbggenerated
     */
    public void setTotalProductPrice(BigDecimal totalProductPrice) {
        this.totalProductPrice = totalProductPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.total_price
     *
     * @return the value of b_order_group.total_price
     *
     * @mbggenerated
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.total_price
     *
     * @param totalPrice the value for b_order_group.total_price
     *
     * @mbggenerated
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.receive_name
     *
     * @return the value of b_order_group.receive_name
     *
     * @mbggenerated
     */
    public String getReceiveName() {
        return receiveName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.receive_name
     *
     * @param receiveName the value for b_order_group.receive_name
     *
     * @mbggenerated
     */
    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName == null ? null : receiveName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.receive_phone
     *
     * @return the value of b_order_group.receive_phone
     *
     * @mbggenerated
     */
    public String getReceivePhone() {
        return receivePhone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.receive_phone
     *
     * @param receivePhone the value for b_order_group.receive_phone
     *
     * @mbggenerated
     */
    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone == null ? null : receivePhone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.receive_address
     *
     * @return the value of b_order_group.receive_address
     *
     * @mbggenerated
     */
    public String getReceiveAddress() {
        return receiveAddress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.receive_address
     *
     * @param receiveAddress the value for b_order_group.receive_address
     *
     * @mbggenerated
     */
    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress == null ? null : receiveAddress.trim();
    }


    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.mall_code
     *
     * @return the value of b_order_group.mall_code
     *
     * @mbggenerated
     */
    public String getMallCode() {
        return mallCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.mall_code
     *
     * @param mallCode the value for b_order_group.mall_code
     *
     * @mbggenerated
     */
    public void setMallCode(String mallCode) {
        this.mallCode = mallCode == null ? null : mallCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.mall_name
     *
     * @return the value of b_order_group.mall_name
     *
     * @mbggenerated
     */
    public String getMallName() {
        return mallName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.mall_name
     *
     * @param mallName the value for b_order_group.mall_name
     *
     * @mbggenerated
     */
    public void setMallName(String mallName) {
        this.mallName = mallName == null ? null : mallName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.mall_phone
     *
     * @return the value of b_order_group.mall_phone
     *
     * @mbggenerated
     */
    public String getMallPhone() {
        return mallPhone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.mall_phone
     *
     * @param mallPhone the value for b_order_group.mall_phone
     *
     * @mbggenerated
     */
    public void setMallPhone(String mallPhone) {
        this.mallPhone = mallPhone == null ? null : mallPhone.trim();
    }


	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.coupon_type
     *
     * @return the value of b_order_group.coupon_type
     *
     * @mbggenerated
     */
    public Integer getCouponType() {
        return couponType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.coupon_type
     *
     * @param couponType the value for b_order_group.coupon_type
     *
     * @mbggenerated
     */
    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.is_full_subtract
     *
     * @return the value of b_order_group.is_full_subtract
     *
     * @mbggenerated
     */
    public Boolean getIsFullSubtract() {
        return isFullSubtract;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.is_full_subtract
     *
     * @param isFullSubtract the value for b_order_group.is_full_subtract
     *
     * @mbggenerated
     */
    public void setIsFullSubtract(Boolean isFullSubtract) {
        this.isFullSubtract = isFullSubtract;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.full_subtract_price
     *
     * @return the value of b_order_group.full_subtract_price
     *
     * @mbggenerated
     */
    public BigDecimal getFullSubtractPrice() {
        return fullSubtractPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.full_subtract_price
     *
     * @param fullSubtractPrice the value for b_order_group.full_subtract_price
     *
     * @mbggenerated
     */
    public void setFullSubtractPrice(BigDecimal fullSubtractPrice) {
        this.fullSubtractPrice = fullSubtractPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.pay_type
     *
     * @return the value of b_order_group.pay_type
     *
     * @mbggenerated
     */
    public Integer getPayType() {
        return payType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.pay_type
     *
     * @param payType the value for b_order_group.pay_type
     *
     * @mbggenerated
     */
    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.preferential_price
     *
     * @return the value of b_order_group.preferential_price
     *
     * @mbggenerated
     */
    public BigDecimal getPreferentialPrice() {
        return preferentialPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.preferential_price
     *
     * @param preferentialPrice the value for b_order_group.preferential_price
     *
     * @mbggenerated
     */
    public void setPreferentialPrice(BigDecimal preferentialPrice) {
        this.preferentialPrice = preferentialPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.real_price
     *
     * @return the value of b_order_group.real_price
     *
     * @mbggenerated
     */
    public BigDecimal getRealPrice() {
        return realPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.real_price
     *
     * @param realPrice the value for b_order_group.real_price
     *
     * @mbggenerated
     */
    public void setRealPrice(BigDecimal realPrice) {
        this.realPrice = realPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.score
     *
     * @return the value of b_order_group.score
     *
     * @mbggenerated
     */
    public Integer getScore() {
        return score;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.score
     *
     * @param score the value for b_order_group.score
     *
     * @mbggenerated
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.pay_status
     *
     * @return the value of b_order_group.pay_status
     *
     * @mbggenerated
     */
    public Integer getPayStatus() {
        return payStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.pay_status
     *
     * @param payStatus the value for b_order_group.pay_status
     *
     * @mbggenerated
     */
    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.start_time
     *
     * @return the value of b_order_group.start_time
     *
     * @mbggenerated
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.start_time
     *
     * @param startTime the value for b_order_group.start_time
     *
     * @mbggenerated
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.end_time
     *
     * @return the value of b_order_group.end_time
     *
     * @mbggenerated
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.end_time
     *
     * @param endTime the value for b_order_group.end_time
     *
     * @mbggenerated
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.order_group_status
     *
     * @return the value of b_order_group.order_group_status
     *
     * @mbggenerated
     */
    public Integer getOrderGroupStatus() {
        return orderGroupStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.order_group_status
     *
     * @param orderGroupStatus the value for b_order_group.order_group_status
     *
     * @mbggenerated
     */
    public void setOrderGroupStatus(Integer orderGroupStatus) {
        this.orderGroupStatus = orderGroupStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_order_group.cre_date
     *
     * @return the value of b_order_group.cre_date
     *
     * @mbggenerated
     */
    public Date getCreDate() {
        return creDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_order_group.cre_date
     *
     * @param creDate the value for b_order_group.cre_date
     *
     * @mbggenerated
     */
    public void setCreDate(Date creDate) {
        this.creDate = creDate;
    }

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	/**
     * 订单关联的商品列表
     */
    private List<OrderGroupProduct> productList;

	public List<OrderGroupProduct> getProductList() {
		return productList;
	}

	public void setProductList(List<OrderGroupProduct> productList) {
		this.productList = productList;
	}

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.order_send_type
     *
     * @mbggenerated
     */
    private Integer orderSendType;
    
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_order_group.order_send_type
     *
     * @mbggenerated
     */
    private String takeGoodsCode;
    
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_address.latitude
     *
     * @mbggenerated
     */
    private BigDecimal latitude;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_address.longitude
     *
     * @mbggenerated
     */
    private BigDecimal longitude;


    private String useCardPcode;
    
    private String useCardPname;
    
    /** 订单支付时间* */
    private Date payTime;
    
    /** 商家确认时间* */
    private Date busConfirmTime;
    
    /** 骑士取货时间* */
    private Date knightPickupTime;
    
    /** 订单完成时间* */
    private Date orderSuccessTime;
    
    /** 订单取消时间* */
    private Date orderCancelTime;

    private BigDecimal mall_latitude;
    
    private BigDecimal mall_longitude;
    
    private String city_name;
    
    private String mall_address;
    
    /** 海鼎订单号* */
    private String uhdOrderId;
    
    /** 海鼎退换货单ID* */
    private String uhdRefundOrderId;
    
    /** 平台商家代码：6666[邻家便利店平台]微电汇[weidianhui]；30328[百度外卖]百度外卖[baiduwaimai]；358[美团外卖]美团外卖[meituan]* */
    private String shop_id;
    
    /** 订单积分* */
    private Integer getScore;
    
    /** 退款理由* */
    private String refundReason;

	public Integer getOrderSendType() {
		return orderSendType;
	}

	public void setOrderSendType(Integer orderSendType) {
		this.orderSendType = orderSendType;
	}

	public Long getUserCardCouponId() {
		return userCardCouponId;
	}

	public void setUserCardCouponId(Long userCardCouponId) {
		this.userCardCouponId = userCardCouponId;
	}

	public BigDecimal getCardCouponPrice() {
		return cardCouponPrice;
	}

	public void setCardCouponPrice(BigDecimal cardCouponPrice) {
		this.cardCouponPrice = cardCouponPrice;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getSendHour() {
		return sendHour;
	}

	public void setSendHour(String sendHour) {
		this.sendHour = sendHour;
	}

	public BigDecimal getSendPrice() {
		return sendPrice;
	}

	public void setSendPrice(BigDecimal sendPrice) {
		this.sendPrice = sendPrice;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTakeGoodsCode() {
		return takeGoodsCode;
	}

	public void setTakeGoodsCode(String takeGoodsCode) {
		this.takeGoodsCode = takeGoodsCode;
	}

	public String getUseCardPcode() {
		return useCardPcode;
	}

	public void setUseCardPcode(String useCardPcode) {
		this.useCardPcode = useCardPcode;
	}

	public String getUseCardPname() {
		return useCardPname;
	}

	public void setUseCardPname(String useCardPname) {
		this.useCardPname = useCardPname;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getBusConfirmTime() {
		return busConfirmTime;
	}

	public void setBusConfirmTime(Date busConfirmTime) {
		this.busConfirmTime = busConfirmTime;
	}

	public Date getKnightPickupTime() {
		return knightPickupTime;
	}

	public void setKnightPickupTime(Date knightPickupTime) {
		this.knightPickupTime = knightPickupTime;
	}

	public Date getOrderSuccessTime() {
		return orderSuccessTime;
	}

	public void setOrderSuccessTime(Date orderSuccessTime) {
		this.orderSuccessTime = orderSuccessTime;
	}

	public Date getOrderCancelTime() {
		return orderCancelTime;
	}

	public void setOrderCancelTime(Date orderCancelTime) {
		this.orderCancelTime = orderCancelTime;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}



	public BigDecimal getMall_latitude() {
		return mall_latitude;
	}

	public void setMall_latitude(BigDecimal mall_latitude) {
		this.mall_latitude = mall_latitude;
	}

	public BigDecimal getMall_longitude() {
		return mall_longitude;
	}

	public void setMall_longitude(BigDecimal mall_longitude) {
		this.mall_longitude = mall_longitude;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getMall_address() {
		return mall_address;
	}

	public void setMall_address(String mall_address) {
		this.mall_address = mall_address;
	}
    
	public Boolean getIsFreeFreight() {
		return isFreeFreight;
	}

	public void setIsFreeFreight(Boolean isFreeFreight) {
		this.isFreeFreight = isFreeFreight;
	}

	public BigDecimal getFreeFreightPrice() {
		return freeFreightPrice;
	}

	public void setFreeFreightPrice(BigDecimal freeFreightPrice) {
		this.freeFreightPrice = freeFreightPrice;
	}

	public String getShop_id() {
		return shop_id;
	}

	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}

	public String getUhdOrderId() {
		return uhdOrderId;
	}

	public void setUhdOrderId(String uhdOrderId) {
		this.uhdOrderId = uhdOrderId;
	}

	public String getUhdRefundOrderId() {
		return uhdRefundOrderId;
	}

	public void setUhdRefundOrderId(String uhdRefundOrderId) {
		this.uhdRefundOrderId = uhdRefundOrderId;
	}

	public Integer getGetScore() {
		return getScore;
	}

	public void setGetScore(Integer getScore) {
		this.getScore = getScore;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	public String getDmName() {
		return dmName;
	}

	public void setDmName(String dmName) {
		this.dmName = dmName;
	}

	public String getDmMobile() {
		return dmMobile;
	}

	public void setDmMobile(String dmMobile) {
		this.dmMobile = dmMobile;
	}

	public String getLogisType() {
		return logisType;
	}

	public void setLogisType(String logisType) {
		this.logisType = logisType;
	}
    
	
}