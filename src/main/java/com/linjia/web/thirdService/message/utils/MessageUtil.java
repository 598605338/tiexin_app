package com.linjia.web.thirdService.message.utils;

import java.util.Iterator;
import java.util.Random;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.linjia.web.thirdService.message.model.XmlEntity;

public class MessageUtil {

	// XML字符串解析通用方法
		public static XmlEntity readStringXmlCommen(XmlEntity xmlentity, String xml) {
			XmlEntity xe = new XmlEntity();
			Document doc = null;
			try {
				// 将字符转化为XML
				doc = DocumentHelper.parseText(xml);
				// 获取根节点
				Element rootElt = doc.getRootElement();
				// 拿到根节点的名称
				// System.out.println("根节点：" + rootElt.getName());
				// 获取根节点下的子节点的值
				if (xmlentity.getReturnstatus() != null) {
					xe.setReturnstatus(rootElt.elementText(
							xmlentity.getReturnstatus()).trim());
				}
				if (xmlentity.getMessage() != null) {
					xe.setMessage(rootElt.elementText(xmlentity.getMessage())
							.trim());
				}
				if (xmlentity.getRemainpoint() != null) {
					xe.setRemainpoint(rootElt.elementText(
							xmlentity.getRemainpoint()).trim());
				}
				if (xmlentity.getTaskID() != null) {
					xe.setTaskID(rootElt.elementText(xmlentity.getTaskID()).trim());
				}
				if (xmlentity.getSuccessCounts() != null) {
					xe.setSuccessCounts(rootElt.elementText(
							xmlentity.getSuccessCounts()).trim());
				}
				if (xmlentity.getPayinfo() != null) {
					xe.setPayinfo(rootElt.elementText(xmlentity.getPayinfo())
							.trim());
				}
				if (xmlentity.getOverage() != null) {
					xe.setOverage(rootElt.elementText(xmlentity.getOverage())
							.trim());
				}
				if (xmlentity.getSendTotal() != null) {
					xe.setSendTotal(rootElt.elementText(xmlentity.getSendTotal())
							.trim());
				}
				// 接收状态返回的报告
				if (rootElt.hasMixedContent() == false) {
					System.out.println("无返回状态！");
				} else {
					for (int i = 1; i <= rootElt.elements().size(); i++) {
						if (xmlentity.getStatusbox() != null) {
							System.out.println("状态" + i + ":");
							// 获取根节点下的子节点statusbox
							Iterator iter = rootElt.elementIterator(xmlentity.getStatusbox());
							// 遍历statusbox节点
							while (iter.hasNext()) {
								Element recordEle = (Element) iter.next();
								xe.setMobile(recordEle.elementText("mobile").trim());
								xe.setTaskid(recordEle.elementText("taskid").trim());
								xe.setStatus(recordEle.elementText("status").trim());
								xe.setReceivetime(recordEle.elementText("receivetime").trim());
								System.out.println("对应手机号：" + xe.getMobile());
								System.out.println("同一批任务ID：" + xe.getTaskid());
								System.out.println("状态报告----10：发送成功，20：发送失败："+ xe.getStatus());
								System.out.println("接收时间：" + xe.getReceivetime());
							}
						}
					}
				}

				// 错误返回的报告
				if (xmlentity.getErrorstatus() != null) {
					// 获取根节点下的子节点errorstatus
					Iterator itererr = rootElt.elementIterator(xmlentity.getErrorstatus());
					// 遍历errorstatus节点
					while (itererr.hasNext()) {
						Element recordElerr = (Element) itererr.next();
						xe.setError(recordElerr.elementText("error").trim());
						xe.setRemark(recordElerr.elementText("remark").trim());
						System.out.println("错误代码：" + xe.getError());
						System.out.println("错误描述：" + xe.getRemark());
					}
				}

				 if(xmlentity.getCallbox()!=null){
					 //获取根节点下的子节点errorstatus
					 Iterator itercallbox = rootElt.elementIterator("errorstatus");
					 // 遍历errorstatus节点
					 while(itercallbox.hasNext()){
						 Element recordcallbox = (Element) itercallbox.next();
						 String content=recordcallbox.elementText("content").trim();
						 String receivetime=recordcallbox.elementText("receivetime").trim();
						 String mobile=recordcallbox.elementText("mobile").trim();
						 String taskid=recordcallbox.elementText("taskid").trim();
					 }
			    }
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			return xe;
		}

		// 解析xml字符串
		public void readStringXml(String xml) {
			Document doc = null;
			try {
				// 将字符转化为XML
				doc = DocumentHelper.parseText(xml);
				// 获取根节点
				Element rootElt = doc.getRootElement();

				// 拿到根节点的名称
				// System.out.println("根节点名称："+rootElt.getName());
				// 获取根节点下的子节点的值
				String returnstatus = rootElt.elementText("returnstatus").trim();
				String message = rootElt.elementText("message").trim();
				String payinfo = rootElt.elementText("payinfo").trim();
				String overage = rootElt.elementText("overage").trim();
				String sendTotal = rootElt.elementText("sendTotal").trim();
				System.out.println("返回状态为：" + returnstatus);
				System.out.println("返回信息提示：" + message);
				System.out.println("返回支付方式：" + payinfo);
				System.out.println("返回剩余短信条数：" + overage);
				System.out.println("返回总条数：" + sendTotal);

			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		
		public static int randomCheckCode(){	
			Random random = new Random();
			int randInt = random.nextInt(899999);
			int checkCode = randInt+100000;
			return checkCode;
		}
}
