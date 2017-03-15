package com.linjia.web.thirdService.message.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linjia.web.dao.MessageMapper;
import com.linjia.web.thirdService.message.constants.MessageConfig;
import com.linjia.web.thirdService.message.model.Message;
import com.linjia.web.thirdService.message.model.XmlEntity;
import com.linjia.web.thirdService.message.service.MessageService;
import com.linjia.web.thirdService.message.utils.MessageUtil;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageMapper mapper;
	
	// 查询余额
	public JSONObject selBalance(String userid) {
		// 使用StringBuffer的append获得xml形式的字符串
		StringBuffer sub = new StringBuffer();
		BufferedReader br = null;
		URL url = null;
		HttpURLConnection con;
		String line;
		JSONObject json = new JSONObject();
		String path = MessageConfig.BALANCEURL + "&userid=" + userid + "";
		try {
			url = new URL(path);
			con = (HttpURLConnection) url.openConnection();
			br = new BufferedReader(new InputStreamReader(con.getInputStream(),
					"UTF-8"));
			while ((line = br.readLine()) != null) {
				// 追加字符串获得XML形式的字符串
				sub.append(line + "");
				// System.out.println("提取数据 :  "+line);
			}
			br.close();

			XmlEntity xmlentity = new XmlEntity();
			String xml = sub.toString();
			System.out.println("returnStr:"+xml);
			// 赋值给xmlEntity实体类
			xmlentity.setReturnstatus("returnstatus");
			xmlentity.setMessage("message");
			xmlentity.setPayinfo("payinfo");
			xmlentity.setOverage("overage");
			xmlentity.setSendTotal("sendTotal");
			// 调用XML字符串解析通用方法
			xmlentity = MessageUtil.readStringXmlCommen(xmlentity, xml);
			int zong = Integer.parseInt(xmlentity.getSendTotal());
			int yong = Integer.parseInt(xmlentity.getOverage());
			int sheng = zong - yong;
			json.put("status", xmlentity.getReturnstatus());
			json.put("payinfo", xmlentity.getPayinfo());
			json.put("surplus", sheng);
			// System.out.println("返回状态为:" + xmlentity.getReturnstatus() + "\n"
			// + "返回付费方式:" + xmlentity.getPayinfo());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return json;
	}

	// 发送短信
	public JSONObject SendMessage(String userid, String mobile,String content,
			String sendTime) {
		if("".equals(content)){
			content=MessageConfig.SIGN+MessageConfig.MSGTYPE+MessageUtil.randomCheckCode()+MessageConfig.CONTENT;
		}else{
			content=MessageConfig.SIGN+content;
		}
		// 使用StringBuffer的append获得xml形式的字符串
		StringBuffer sub = new StringBuffer();
		BufferedReader br = null;
		URL url = null;
		HttpURLConnection con;
		String line;
		JSONObject json = new JSONObject();
		try {
			// 设置发送内容的编码方式
			String send_content = URLEncoder.encode(
					content.replaceAll("<br/>", " "), "UTF-8");// 发送内容
			String path = MessageConfig.SENDNSGURL + "&userid=" + userid
					+ "&mobile=" + mobile + "&content=" + send_content+ "&sendTime=" + sendTime + "";
			url = new URL(path);
			con = (HttpURLConnection) url.openConnection();
			br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
			while ((line = br.readLine()) != null) {
				// 追加字符串获得XML形式的字符串
				sub.append(line + "");
				// System.out.println("提取数据 :  "+line);
			}
			br.close();

			XmlEntity xmlentity = new XmlEntity();
			String xml = sub.toString();
			System.out.println("returnStr:"+xml);
			xmlentity.setReturnstatus("returnstatus");
			xmlentity.setMessage("message");
			xmlentity.setRemainpoint("remainpoint");
			xmlentity.setTaskID("taskID");
			xmlentity.setSuccessCounts("successCounts");
			xmlentity = MessageUtil.readStringXmlCommen(xmlentity, xml);
			json.put("status", xmlentity.getReturnstatus());
			json.put("message", xmlentity.getPayinfo());
			json.put("successCounts", xmlentity.getSuccessCounts());
			json.put("successCounts", xmlentity.getSuccessCounts());
			// System.out.println("状态"+xmlentity.getReturnstatus()+"返回信息"+xmlentity.getMessage()+"成功条数"+xmlentity.getSuccessCounts());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	// 状态报告接口
	public JSONObject StatusReport(String userid) {
		// 使用StringBuffer的append获得xml形式的字符串
		StringBuffer sub = new StringBuffer();
		BufferedReader br = null;
		URL url = null;
		HttpURLConnection con;
		String line;
		JSONObject json = new JSONObject();
		try {
			String path = MessageConfig.STATUSURL + "&userid=" + userid + "";
			url = new URL(path);
			con = (HttpURLConnection) url.openConnection();
			br = new BufferedReader(new InputStreamReader(con.getInputStream(),
					"UTF-8"));
			while ((line = br.readLine()) != null) {
				// 追加字符串获得XML形式的字符串
				sub.append(line + "");
				// System.out.println("提取数据 :  "+line);
			}
			br.close();

			XmlEntity xmlentity = new XmlEntity();
			String xml = sub.toString();
			System.out.println(xml);
			xmlentity.setStatusbox("statusbox");
			xmlentity.setMobile("mobile");
			xmlentity.setTaskid("taskid");
			xmlentity.setStatus("status");
			xmlentity.setReceivetime("receivetime");
			xmlentity.setErrorstatus("errorstatus");
			xmlentity.setError("error");
			xmlentity.setRemark("remark");
			xmlentity = MessageUtil.readStringXmlCommen(xmlentity, xml);
			json.put("status", xmlentity.getReturnstatus());
			json.put("mobile", xmlentity.getMobile());
			json.put("receivetime", xmlentity.getReceivetime());
			json.put("errorCode", xmlentity.getError());
			// System.out.println("对应手机号："+xmlentity.getMobile()+"对应状态"+xmlentity.getStatus()+"对应接收时间"+xmlentity.getReceivetime());
			// System.out.println("错误代码："+xmlentity.getError());}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public JSONObject callBackInfo(String userid,String account,String password,String action) {
		if(MessageConfig.USERNAME.equals(account)&&MessageConfig.PWD.equals(password)&&"query".equals(action)){}
        // 使用StringBuffer的append获得xml形式的字符串
		StringBuffer sub = new StringBuffer();
		BufferedReader br = null;
		URL url = null;
		HttpURLConnection con;
		String line;
		JSONObject json = new JSONObject();
		try {
			String path = MessageConfig.STATUSURL + "&userid=" + userid + "";
			url = new URL(path);
			con = (HttpURLConnection) url.openConnection();
			br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
				while ((line = br.readLine()) != null) {
					// 追加字符串获得XML形式的字符串
					sub.append(line + "");
					// System.out.println("提取数据 :  "+line);
				}
				br.close();

					XmlEntity xmlentity = new XmlEntity();
					String xml = sub.toString();
					System.out.println(xml);
					
					xmlentity.setStatusbox("statusbox");
					xmlentity.setMobile("mobile");
					xmlentity.setTaskid("taskid");
					xmlentity.setStatus("status");
					xmlentity.setReceivetime("receivetime");
					
					xmlentity = MessageUtil.readStringXmlCommen(xmlentity, xml);
					json.put("status", xmlentity.getReturnstatus());
					json.put("mobile", xmlentity.getMobile());
					json.put("receivetime", xmlentity.getReceivetime());
					// System.out.println("对应手机号："+xmlentity.getMobile()+"对应状态"+xmlentity.getStatus()+"对应接收时间"+xmlentity.getReceivetime());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return json;
	}

	@Override
	public Integer insertMessage(Message message) {
		Integer num=mapper.insertMessage(message);
		return num;
	}

	@Override
	public Integer deleteMessage(Integer id) {
		Integer num=mapper.deleteMessage(id);
		return num;
	}

	@Override
	public Message selectMessage(String moblie) {
		Message message=mapper.selectMessage(moblie);
		return message;
	}
	
	// 发送验证码
	public JSONObject sendCheckCode(String userid, String mobile,String content,
				String sendTime) {
			Message message=new Message();
			message.setMobile(mobile);
			if("".equals(content)){
				int checkCode=MessageUtil.randomCheckCode();
				content=MessageConfig.SIGN+MessageConfig.MSGTYPE+checkCode+MessageConfig.CONTENT;
				message.setCheckcode(checkCode+"");
			}else{
				content=MessageConfig.SIGN+content;
			}
			message.setContent(content);
			int i=this.insertMessage(message);
				// 使用StringBuffer的append获得xml形式的字符串
				StringBuffer sub = new StringBuffer();
				BufferedReader br = null;
				URL url = null;
				HttpURLConnection con;
				String line;
				JSONObject json = new JSONObject();
				try {
					if(i>0){
						// 设置发送内容的编码方式
						String send_content = URLEncoder.encode(
								content.replaceAll("<br/>", " "), "UTF-8");// 发送内容
						String path = MessageConfig.SENDNSGURL + "&userid=" + userid
								+ "&mobile=" + mobile + "&content=" + send_content+ "&sendTime=" + sendTime + "";
						url = new URL(path);
						con = (HttpURLConnection) url.openConnection();
						br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
						while ((line = br.readLine()) != null) {
							// 追加字符串获得XML形式的字符串
							sub.append(line + "");
							// System.out.println("提取数据 :  "+line);
						}
						br.close();
		
						XmlEntity xmlentity = new XmlEntity();
						String xml = sub.toString();
						System.out.println("returnStr:"+xml);
						xmlentity.setReturnstatus("returnstatus");
						xmlentity.setMessage("message");
						xmlentity.setRemainpoint("remainpoint");
						xmlentity.setTaskID("taskID");
						xmlentity.setSuccessCounts("successCounts");
						xmlentity = MessageUtil.readStringXmlCommen(xmlentity, xml);
						json.put("status", xmlentity.getReturnstatus());
						json.put("message", xmlentity.getPayinfo());
						json.put("successCounts", xmlentity.getSuccessCounts());
						json.put("successCounts", xmlentity.getSuccessCounts());
						// System.out.println("状态"+xmlentity.getReturnstatus()+"返回信息"+xmlentity.getMessage()+"成功条数"+xmlentity.getSuccessCounts());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return json;
		}
		
//	public static void main(String[] args) {
//		MessageServiceImpl impl=new MessageServiceImpl();
//		JSONObject json1=impl.selBalance("");
//		System.out.println(json1.toString());
//		【邻家便利】(邻家商城)注册验证码是631590，邻家提醒您请勿将此码发给他人，请尽快验证
//		JSONObject json=impl.SendMessage("", "13681142818", "【邻家便利】小伙子，情人节快乐!", "");
//		System.out.println(json.toString());
//		JSONObject json=impl.StatusReport("xd001142");
//		System.out.println(json.toString());
//	}
	
}
