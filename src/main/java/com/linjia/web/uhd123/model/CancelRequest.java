package com.linjia.web.uhd123.model;

/** 
 * 取消订单请求信息
 * @author  lixinling: 
 * @date 2016年9月29日 下午2:13:49 
 * @version 1.0 
*/
public class CancelRequest {
	
	/** 取消类型,取值为stationRefuse（门店拒接）、customerCancel（客户取消）* */
	private String type;
	
	/** 取消的原因* */
	private String reason;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
