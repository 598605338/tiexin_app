package com.linjia.web.uhd123.model;

import java.util.Date;

/** 
 * 平台信息
 * @author  lixinling: 
 * @date 2016年9月29日 下午2:13:49 
 * @version 1.0 
*/
public class FrontOrder {

	/** 平台* */
	private Platform platform;

	/** 平台订单号* */
	private String order_id;

	/** 创建时间,格式为：yyyy-MM-dd HH:mm:ss* */
	private String created;

	/** 最后修改时间,格式为：yyyy-MM-dd HH:mm:ss* */
	private String modified;

	/** 状态* */
	private String state;

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}


	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
