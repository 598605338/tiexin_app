package com.linjia.constants;


/** 
 * @author  lixinling: 
 * @date 2016年8月8日 下午3:44:11 
 * @version 1.0 
*/
public enum ScoreEnum {
	Spending("消费",101),StoredValue("储值",102);
	
	private String name;
	private int code;
	ScoreEnum(String name, int code) {
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

	
	
}
