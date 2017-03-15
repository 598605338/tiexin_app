package com.linjia.web.uhd123.model;


/** 
 * 联系方式
 * @author  lixinling: 
 * @date 2016年9月29日 下午2:24:14 
 * @version 1.0 
*/
public class Contact {

	/** 姓名* */
	private String name;
	/** 电话* */
	private String telephone;
	/** 移动电话* */
	private String mobile;
	/** 邮箱* */
	private String email;
	/** 传真* */
	private String fax;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	
}
