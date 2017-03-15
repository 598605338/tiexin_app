package com.linjia.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.linjia.base.controller.BaseController;
import com.linjia.web.model.PaoDanRecord;
import com.linjia.web.query.PaoDanRecordQuery;
import com.linjia.web.service.PaoDanRecordService;

/**
 * 抛单
 * @author xiangsy
 *
 */
@Controller
@RequestMapping("/paodan")
public class PaoDanController extends BaseController{
	
	@Autowired
	private PaoDanRecordService paoDanRecordService;
	
	/**
	 * @param request
	 * @return
	 */
	@RequestMapping("/addpd")
	@ResponseBody
	public Object addpd(HttpServletRequest request){
		Map<String, Object> map=new HashMap<String, Object>();
		PaoDanRecord pd = new PaoDanRecord(16112722277L,1,1,1,new Date(),null,0);
		try{
			boolean b=paoDanRecordService.insertPaoDan(pd);
			System.out.println("添加操作:"+b);
			map.put("pd", pd);
			map.put("status", "ok");
			map.put("result",b);
		}catch(Exception e){
			e.printStackTrace();
			map.put("status", "error");
			logger.debug("异常");
		}
		return map;
	}
	
	
	/**
	 * @param request
	 * @return
	 */
	@RequestMapping("/updatepd")
	@ResponseBody
	public Object updatepd(HttpServletRequest request,@RequestParam int id){
		Map<String, Object> map=new HashMap<String, Object>();
		PaoDanRecord pd = new PaoDanRecord(16112722277L,1,1,1,null,new Date(),1);
		pd.setId(id);
		try{
			boolean b=paoDanRecordService.updatePaoDan(pd);
			System.out.println("更新操作:"+b);
			map.put("pd", pd);
			map.put("status", "ok");
			map.put("result",b);
		}catch(Exception e){
			e.printStackTrace();
			map.put("status", "error");
			logger.debug("异常");
		}
		return map;
	}
	
	/**
	 * @param request
	 * @return
	 */
	@RequestMapping("/delpd")
	@ResponseBody
	public Object delpd(HttpServletRequest request,@RequestParam int id){
		Map<String, Object> map=new HashMap<String, Object>();
		try{
			boolean b=paoDanRecordService.deletePaoDan(id);
			System.out.println("删除操作:"+b);
			map.put("id", id);
			map.put("result", b);
			map.put("status", "ok");
		}catch(Exception e){
			e.printStackTrace();
			map.put("status", "error");
			logger.debug("异常");
		}
		return map;
	}
	
	/**
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectOne")
	@ResponseBody
	public Object selectOne(HttpServletRequest request,@RequestParam Long id){
		Map<String, Object> map=new HashMap<String, Object>();
		try{
			//查询抛单记录
			Map<String,Object> pdmap=new HashMap<String, Object>();
			pdmap.put("order_id", id);
			pdmap.put("order_type", 1);
			pdmap.put("paodan_type", 1);
			PaoDanRecord selPd=paoDanRecordService.selectPaoDan(pdmap);
			if(selPd!=null){
				map.put("other", "haha");
			}
			map.put("paoDanRecord",selPd);
			map.put("status", "ok");
		}catch(Exception e){
			e.printStackTrace();
			map.put("status", "error");
			logger.debug("异常");
		}
		return map;
	}
	
	/**
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectList")
	@ResponseBody
	public Object selectList(HttpServletRequest request,PaoDanRecordQuery query){
		Map<String, Object> map=new HashMap<String, Object>();
		try{
			List<PaoDanRecord> list=paoDanRecordService.selectPaoDanList(query);
			map.put("list", list);
			map.put("status", "ok");
		}catch(Exception e){
			e.printStackTrace();
			map.put("status", "error");
			logger.debug("异常");
		}
		return map;
	}
	
	
}
