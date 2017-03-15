package com.linjia.web.uhd123.model;


/** 
 * 客户
 * @author  lixinling: 
 * @date 2016年9月29日 下午2:22:40 
 * @version 1.0 
*/
public class Customer {

	/** 客户/买家ID* */
	private String id;
	/** 联系方式* */
	private Contact contact;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	
	
}
