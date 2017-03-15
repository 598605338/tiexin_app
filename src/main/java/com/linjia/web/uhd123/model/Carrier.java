package com.linjia.web.uhd123.model;

/** 
 * 承运商
 * @author  lixinling: 
 * @date 2016年9月29日 下午2:13:49 
 * @version 1.0 
*/
public class Carrier {
	
	/** 承运商类型，取值范围:self(商家自送)、thirdparty(第三方配送)* */
	private String type;
	
	/** 承运商* */
	private CarrierInfo carrier;
	
	/** 送货员* */
	private Deliveryman deliveryman;
	
	/** 送货员电话* */
	private String deliveryman_mobile;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public CarrierInfo getCarrier() {
		return carrier;
	}

	public void setCarrier(CarrierInfo carrier) {
		this.carrier = carrier;
	}

	public Deliveryman getDeliveryman() {
		return deliveryman;
	}

	public void setDeliveryman(Deliveryman deliveryman) {
		this.deliveryman = deliveryman;
	}

	public String getDeliveryman_mobile() {
		return deliveryman_mobile;
	}

	public void setDeliveryman_mobile(String deliveryman_mobile) {
		this.deliveryman_mobile = deliveryman_mobile;
	}

}
