package com.linjia.web.uhd123.model;

/** 
 * 平台信息
 * @author  lixinling: 
 * @date 2016年9月29日 下午2:13:49 
 * @version 1.0 
*/
public class FrontRtn {
	
	/** 平台* */
	private Platform platform;
	
	/** 平台退货单号* */
	private String rtn_id;
	
	/** 订单生成时间,格式为：yyyy-MM-dd HH:mm:ss* */
	private String created;
	
	/** 订单最后修改时间,格式为：yyyy-MM-dd HH:mm:ss* */
	private String modified;
	
	/** 平台退换货状态* */
	private String state;

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public String getRtn_id() {
		return rtn_id;
	}

	public void setRtn_id(String rtn_id) {
		this.rtn_id = rtn_id;
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
