package com.linjia.web.query;

import java.util.Date;

import com.linjia.base.query.Query;


/** 
 * @author  lixinling: 
 * @date 2016年7月6日 上午10:14:33 
 * @version 1.0 
 */
public class MiaoshaActivityBaseQuery extends Query {
	private Long id;
	private String name;
	private String bannerPath;
	private Date panicBuyingStartTime;
	private Date panicBuyingEndTime;
	private boolean qmType;
	private boolean checkSellType;
	private String unstartActivityTip;
	private String endedActivityTip;
	private String sellOutTip;
	private String description;
	
	/**
	 * 当前时间(1:9点；2:12点；3:15点；4:18点)
	 */
	private Date currentTime;
	/**
	 * 时间节点(1:9点；2:12点；3:15点；4:18点)
	 */
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
	public boolean isQmType() {
		return qmType;
	}
	public void setQmType(boolean qmType) {
		this.qmType = qmType;
	}
	public boolean isCheckSellType() {
		return checkSellType;
	}
	public void setCheckSellType(boolean checkSellType) {
		this.checkSellType = checkSellType;
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
	public int getTimeNode() {
		return timeNode;
	}
	public void setTimeNode(int timeNode) {
		this.timeNode = timeNode;
	}
	public Date getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(Date currentTime) {
		this.currentTime = currentTime;
	}
	
	/** 发布日期* */
    private String publishDate;
    
    /** 时间段* */
    private String timeSlot;

	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public String getTimeSlot() {
		return timeSlot;
	}
	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}
    
    
}
