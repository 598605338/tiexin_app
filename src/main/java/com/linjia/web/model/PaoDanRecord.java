package com.linjia.web.model;

import java.util.Date;

public class PaoDanRecord {

	private Integer id;
	private Long order_id;
	private Integer order_type;
	private Integer status;
	private Integer paodan_type;
	private Date create_time;
	private Date update_time;
	private Integer deleted;
	
	public PaoDanRecord(){
		
	}
	/**
	 * @param order_id
	 * @param order_type
	 * @param status
	 * @param paodan_type
	 * @param crate_time
	 * @param update_time
	 * @param deleted
	 */
	public PaoDanRecord(Long order_id, Integer order_type, Integer status,
			Integer paodan_type, Date create_time, Date update_time,
			Integer deleted) {
		super();
		this.order_id = order_id;
		this.order_type = order_type;
		this.status = status;
		this.paodan_type = paodan_type;
		this.create_time = create_time;
		this.update_time = update_time;
		this.deleted = deleted;
	}
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
	
	
}
