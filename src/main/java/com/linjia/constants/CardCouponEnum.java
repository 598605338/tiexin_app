package com.linjia.constants;


/** 
 * @author  lixinling: 
 * @date 2016年9月21日 下午3:44:11 
 * @version 1.0 
*/
public enum CardCouponEnum {
	DJQ("现金券",03),SPQ("提货券",01),MYFQ("免运费券",05);
	
	private String name;
	private int code;
	CardCouponEnum(String name, int code) {
		this.name = name;
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	
	/**
	 * 根据本地券码code取得券类型名
	 * lixinling 
	 * 2016年9月23日 下午3:50:16
	 * @param code
	 * @return
	 */
	public static String getNameByCode(int code){
		switch(code){
		case 1:
			return SPQ.name;
		case 3:
			return DJQ.name;
		case 5:
			return MYFQ.name;
		default:
			return null;
		}
	}
	
}
