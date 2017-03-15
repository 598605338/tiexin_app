package com.linjia.web.model;

import java.util.Date;

/**
 * 秒杀Model
 * @author lixinling
 * 2016年7月5日 下午3:31:17
 */
public class MiaoshaActivityBase {
	private Long id;
	private String name;
	private String bannerPath;
	private Date panicBuyingStartTime;
	private Date panicBuyingEndTime;
	private int qmType;
	private int checkSellType;
	private String unstartActivityTip;
	private String endedActivityTip;
	private String sellOutTip;
	private String description;
	
	//抢购状态：抢购中；即将开始；已结束
	private String panicStatus;
	//显示信息1：即将开始；抢购已开始；抢购结束，敬请期待；
	private String showLabel1;
	//显示信息2：距离开始；距离结束；
	private String showLabel2;
	//倒计时（毫秒数）
	private long countdownTimes;
	
	//时间节点(1:9点；2:12点；3:15点；4:18点)
	private int timeNode;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBannerPath() {
		return bannerPath;
	}
	public void setBannerPath(String bannerPath) {
		this.bannerPath = bannerPath;
	}
	public Date getPanicBuyingStartTime() {
		return panicBuyingStartTime;
	}
	public void setPanicBuyingStartTime(Date panicBuyingStartTime) {
		this.panicBuyingStartTime = panicBuyingStartTime;
	}
	public Date getPanicBuyingEndTime() {
		return panicBuyingEndTime;
	}
	public void setPanicBuyingEndTime(Date panicBuyingEndTime) {
		this.panicBuyingEndTime = panicBuyingEndTime;
	}
	public String getUnstartActivityTip() {
		return unstartActivityTip;
	}
	public void setUnstartActivityTip(String unstartActivityTip) {
		this.unstartActivityTip = unstartActivityTip;
	}
	public String getEndedActivityTip() {
		return endedActivityTip;
	}
	public void setEndedActivityTip(String endedActivityTip) {
		this.endedActivityTip = endedActivityTip;
	}
	public String getSellOutTip() {
		return sellOutTip;
	}
	public void setSellOutTip(String sellOutTip) {
		this.sellOutTip = sellOutTip;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public int getQmType() {
		return qmType;
	}
	public void setQmType(int qmType) {
		this.qmType = qmType;
	}
	public int getCheckSellType() {
		return checkSellType;
	}
	public void setCheckSellType(int checkSellType) {
		this.checkSellType = checkSellType;
	}
	public String getPanicStatus() {
		return panicStatus;
	}
	public void setPanicStatus(String panicStatus) {
		this.panicStatus = panicStatus;
	}
	public String getShowLabel1() {
		return showLabel1;
	}
	public void setShowLabel1(String showLabel1) {
		this.showLabel1 = showLabel1;
	}
	public String getShowLabel2() {
		return showLabel2;
	}
	public void setShowLabel2(String showLabel2) {
		this.showLabel2 = showLabel2;
	}

	public long getCountdownTimes() {
		return countdownTimes;
	}
	public void setCountdownTimes(long countdownTimes) {
		this.countdownTimes = countdownTimes;
	}
	
	
	public int getTimeNode() {
		return timeNode;
	}
	public void setTimeNode(int timeNode) {
		this.timeNode = timeNode;
	}
	public MiaoshaActivityBase() {
	}
	
	
	
	
	
	
}
