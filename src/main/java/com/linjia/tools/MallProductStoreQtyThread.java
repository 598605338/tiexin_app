package com.linjia.tools;

import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.linjia.web.model.MallProductSendQty;
import com.linjia.web.service.MallProductSendQtyService;

/** 
 * @author  lixinling: 
 * @date 2016年11月14日 上午9:54:04 
 * @version 1.0 
*/
public class MallProductStoreQtyThread implements Runnable {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	MallProductSendQtyService mallProductSendQtyService;
	ConcurrentLinkedQueue<MallProductSendQty> mallProductSendQtyVector;
	static MallProductStoreQtyThread mallProductStoreQtyThread;
	Thread th;
	boolean isRun = false;

	private MallProductStoreQtyThread() {
		mallProductSendQtyVector = new ConcurrentLinkedQueue<MallProductSendQty>();
		ApplicationContext ac = ContextLoader.getCurrentWebApplicationContext();
		mallProductSendQtyService = ac.getBean(MallProductSendQtyService.class);
	}

	public synchronized static MallProductStoreQtyThread createIns() {
		if (mallProductStoreQtyThread == null) {
			mallProductStoreQtyThread = new MallProductStoreQtyThread();
		}
		return mallProductStoreQtyThread;
	}

	public void start() {
		if (th == null && isRun == false) {
			isRun = true;
			th = new Thread(this);
			th.start();
		}
	}

	public void stop() {
		isRun = false;
		th = null;
	}

	public void addNormal(MallProductSendQty object) {
		mallProductSendQtyVector.add(object);
	}

	@Override
	public void run() {
		while (isRun) {
			try {
				Thread.sleep(10000);
				logger.debug("鼎力云数据推送存入数据库mallProductSendQtyVector的size:"+mallProductSendQtyVector.size());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (mallProductSendQtyVector.size() > 0) {
				logger.info("鼎力云数据推送存入数据库mallProductSendQtyVector存入数据库开始。");
				for (Iterator<MallProductSendQty> it = mallProductSendQtyVector.iterator();it.hasNext();){
					MallProductSendQty o = it.next();
					mallProductSendQtyService.insertOrUpdate(o);
					it.remove();
				}
				logger.info("鼎力云数据推送存入数据库mallProductSendQtyVector存入数据库结束。");
			}
		}
	}
}
