package com.linjia.web.model;

import java.util.Date;

public class MallProductSendQty{
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_mall_product_send_qty.quantity
     *
     * @mbggenerated
     */
    private Integer quantity;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_mall_product_send_qty.update_date
     *
     * @mbggenerated
     */
    private Date updateDate;
    
    private String mallCode;
    private String pCode;
    private int count;
    
    

    public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_mall_product_send_qty.quantity
     *
     * @return the value of b_mall_product_send_qty.quantity
     *
     * @mbggenerated
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_mall_product_send_qty.quantity
     *
     * @param quantity the value for b_mall_product_send_qty.quantity
     *
     * @mbggenerated
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_mall_product_send_qty.update_date
     *
     * @return the value of b_mall_product_send_qty.update_date
     *
     * @mbggenerated
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_mall_product_send_qty.update_date
     *
     * @param updateDate the value for b_mall_product_send_qty.update_date
     *
     * @mbggenerated
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

	public String getMallCode() {
		return mallCode;
	}

	public void setMallCode(String mallCode) {
		this.mallCode = mallCode;
	}

	public String getpCode() {
		return pCode;
	}

	public void setpCode(String pCode) {
		this.pCode = pCode;
	}
    
    
}