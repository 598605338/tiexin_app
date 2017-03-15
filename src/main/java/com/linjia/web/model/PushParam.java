package com.linjia.web.model;

public class PushParam {
   private String cmd;
   private String  timestamp;
   private String  version;
   private String  ticket;
   private String  source;
   private String sign;
   private PushBody body;
   
public String getCmd() {
	return cmd;
}
public void setCmd(String cmd) {
	this.cmd = cmd;
}
public String getTimestamp() {
	return timestamp;
}
public void setTimestamp(String timestamp) {
	this.timestamp = timestamp;
}
public String getVersion() {
	return version;
}
public void setVersion(String version) {
	this.version = version;
}
public String getTicket() {
	return ticket;
}
public void setTicket(String ticket) {
	this.ticket = ticket;
}
public String getSource() {
	return source;
}
public void setSource(String source) {
	this.source = source;
}
public String getSign() {
	return sign;
}
public void setSign(String sign) {
	this.sign = sign;
}
public PushBody getBody() {
	return body;
}
public void setBody(PushBody body) {
	this.body = body;
}
   
   
}
