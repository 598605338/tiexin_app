package com.linjia.web.uhd123.model;

/** 
 * 收货人信息
 * @author  lixinling: 
 * @date 2016年9月29日 下午2:13:49 
 * @version 1.0 
*/
public class Receiver {

	/** 联系方式* */
	private Contact contact;

	/** 地址* */
	private Address address;

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
