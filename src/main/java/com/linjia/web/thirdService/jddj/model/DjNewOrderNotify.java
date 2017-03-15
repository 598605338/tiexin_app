package com.linjia.web.thirdService.jddj.model;

/** 
 * 创建新订单通知消息
 * @author  lixinling: 
 * @date 2017年1月9日 下午2:18:30 
 * @version 1.0 
*/
public class DjNewOrderNotify {

	/** 单据ID* */
	private String billId;
	/** 单据状态(32000 如果商家自动接单，状态含义为等待出库；如果商家手动接单，状态含义为待处理)* */
	private String statusId;
	/** 日期格式为"yyyy-MM-dd HH:mm:ss"* */
	private String timestamp;

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
