package com.linjia.web.model;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.linjia.tools.DateSerializeUtils;

public class ActivityKaiTuanMain {
	
	//主键，开团id
	private Integer id;
	
	//拼团基本表Id
	private Integer baseId;
	
	//开团用户Id
	private String kt_userId;
	
	//当前参团人数
	private Integer cur_num;
	
	//当前拼团状态
	private Integer state;
	
	//创建时间
	private Date create_time;
	
	//加入拼团时间
	private Date joinTime;
		
	//拼团商品标题
	private String p_name;
		
	//拼团商品价格
	private BigDecimal pt_price;
		
	//参团用户Id
	private String userId;
	
	//团购结束时间
	private Date end_time;
	
	private Long address_id;
	
	private String p_code;
	
	private String remark;
	
	private Integer buyPersonNums;
	
	private Integer mul_num;
	private Integer add_num;
	
	private String image_path;
	
	private Integer online;
	
	//是否已删除标识负
	@JsonIgnore
	private Boolean deleted;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBaseId() {
		return baseId;
	}

	public void setBaseId(Integer baseId) {
		this.baseId = baseId;
	}

	public String getKt_userId() {
		return kt_userId;
	}

	public void setKt_userId(String kt_userId) {
		this.kt_userId = kt_userId;
	}

	public Integer getCur_num() {
		return cur_num;
	}

	public void setCur_num(Integer cur_num) {
		this.cur_num = cur_num;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@JsonSerialize(using = DateSerializeUtils.class)
	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@JsonSerialize(using = DateSerializeUtils.class)
	public Date getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}

	public String getP_name() {
		return p_name;
	}

	public void setP_name(String p_name) {
		this.p_name = p_name;
	}

	public BigDecimal getPt_price() {
		return pt_price;
	}

	public void setPt_price(BigDecimal pt_price) {
		this.pt_price = pt_price;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public Long getAddress_id() {
		return address_id;
	}

	public void setAddress_id(Long address_id) {
		this.address_id = address_id;
	}

	public String getP_code() {
		return p_code;
	}

	public void setP_code(String p_code) {
		this.p_code = p_code;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getBuyPersonNums() {
		return buyPersonNums;
	}

	public void setBuyPersonNums(Integer buyPersonNums) {
		this.buyPersonNums = buyPersonNums;
	}

	public Integer getMul_num() {
		return mul_num;
	}

	public void setMul_num(Integer mul_num) {
		this.mul_num = mul_num;
	}

	public Integer getAdd_num() {
		return add_num;
	}

	public void setAdd_num(Integer add_num) {
		this.add_num = add_num;
	}

	public String getImage_path() {
		return image_path;
	}

	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}

	public Integer getOnline() {
		return online;
	}

	public void setOnline(Integer online) {
		this.online = online;
	}
	
	
}
