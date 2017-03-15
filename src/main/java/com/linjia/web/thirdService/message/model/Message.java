package com.linjia.web.thirdService.message.model;

import java.util.Date;

public class Message {

	//id
	private int id;
	//手机号
	private String mobile;
	//验证码
	private String checkcode;
	//短信内容
	private String content;
	//创建时间
	private Date createtime;
	//失效时间
	private Date endtime;
	public int getId() {
		return id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCheckcode() {
		return checkcode;
	}
	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	
}
