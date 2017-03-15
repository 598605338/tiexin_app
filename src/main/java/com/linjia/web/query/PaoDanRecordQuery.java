package com.linjia.web.query;

import java.util.Date;

import com.linjia.base.query.Query;

public class PaoDanRecordQuery extends Query{

	private Integer id;
	private Long order_id;
	private Integer order_type;
	private Integer status;
	private Integer paodan_type;
	private Date create_time;
	private Date update_time;
	private Integer deleted;
	private Integer queryFlag;
	private Integer timeFlag;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getOrder_id() {
		return order_id;
	}
	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}
	public Integer getOrder_type() {
		return order_type;
	}
	public void setOrder_type(Integer order_type) {
		this.order_type = order_type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	public Integer getDeleted() {
		return deleted;
	}
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	public Integer getPaodan_type() {
		return paodan_type;
	}
	public void setPaodan_type(Integer paodan_type) {
		this.paodan_type = paodan_type;
	}
	public Integer getQueryFlag() {
		return queryFlag;
	}
	public void setQueryFlag(Integer queryFlag) {
		this.queryFlag = queryFlag;
	}
	public Integer getTimeFlag() {
		return timeFlag;
	}
	public void setTimeFlag(Integer timeFlag) {
		this.timeFlag = timeFlag;
	}
	
}
