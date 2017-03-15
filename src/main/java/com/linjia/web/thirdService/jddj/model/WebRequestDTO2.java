package com.linjia.web.thirdService.jddj.model;


/** 
 * @author  lixinling: 
 * @date 2017年2月21日 上午9:25:25 
 * @version 1.0 
*/
public class WebRequestDTO2 {

	private String token;
    private String app_key;
    private String timestamp;
    private String v;
    private String format;
    private Object jd_param_json;
    private String sign;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getApp_key() {
		return app_key;
	}
	public void setApp_key(String app_key) {
		this.app_key = app_key;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getV() {
		return v;
	}
	public void setV(String v) {
		this.v = v;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public Object getJd_param_json() {
		return jd_param_json;
	}
	public void setJd_param_json(Object jd_param_json) {
		this.jd_param_json = jd_param_json;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("app_key:").append(this.getApp_key()).append(",")
			.append("token:").append(this.getToken()).append(",")
			.append("timestamp:").append(this.getTimestamp()).append(",")
			.append("v:").append(this.getV()).append(",")
			.append("format:").append(this.getFormat()).append(",")
			.append("jd_param_json:").append(this.getJd_param_json());
		return sb.toString();
	}
	
	
}
