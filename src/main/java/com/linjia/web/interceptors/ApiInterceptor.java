package com.linjia.web.interceptors;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.linjia.constants.Application;
import com.linjia.tools.JSONUtil;
import com.linjia.tools.Util;

public class ApiInterceptor extends HandlerInterceptorAdapter {
	private final Logger log = LoggerFactory.getLogger(ApiInterceptor.class);

	// 拦截前处理
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object obj) throws Exception {
		String head = request.getHeader("Content-Type");
		String method = request.getMethod();
		System.out.println("head****" + head);
		System.out.println("method****" + method);
		try {
			// 用来排列请求参数的map
			SortedMap<String, Object> prePayParams = new TreeMap<String, Object>();
			// 获取url传进来的秘钥
			String sig = request.getParameter("sign");
			if (sig == null || sig.isEmpty()) {
				request.setAttribute("status", "fail");
				request.setAttribute("message", "对不起，签名不对,拒绝访问！");
				request.getRequestDispatcher("/jsp/reqError.jsp").forward(request, response);
				// response.sendRedirect("/linjia_1/jsp/reqError.jsp");
				return false;
			}
//			String str = "";
			// 如果提交过来的是json数据，获取json数据
			if ("application/json".equals(head)) {
				InputStream inputStream = request.getInputStream();
				if (inputStream != null) {
					Map<String, Object> map = JSONUtil.jsonToMap(inputStream);
					if (map != null) {
						for (String key : map.keySet()) {
							Object value = map.get(key);
							// 剔除传入的签名
							if (!"sign".equals(key)) {
//								str = str + key + "=" + value + "&";
								prePayParams.put(key, value);
							}
						}
					}
				}
			}

			// 如果是form提交过来的数据，获取url数据
			// 获取请求参数列表
			Enumeration<String> keys = request.getParameterNames();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				Object value = request.getParameter(key);
				// 剔除传入的签名
				if (!"sign".equals(key)) {
//					str = str + key + "=" + value + "&";
					prePayParams.put(key, value);
				}
			}

			// 获取排序请求列表字符串
			String reqUrl = "";
			if (prePayParams.size() > 0) {
				for (Entry<String, Object> p : prePayParams.entrySet()) {
					String key = p.getKey();
					Object value = prePayParams.get(key);
					reqUrl = reqUrl + key + "=" + value + "&";
				}
			}

			reqUrl = reqUrl + Application.getljKey();
			System.out.println("排序后请求参数为:" + reqUrl);
			// 生成秘钥
			String rightSign = Util.getMD5(reqUrl);
			System.out.println("秘钥为:" + rightSign);
			if (!rightSign.equals(sig)) {
				request.setAttribute("message", "对不起，签名不对,拒绝访问！");
				request.getRequestDispatcher("/jsp/reqError.jsp").forward(request, response);
				// response.sendRedirect("/linjia_1/jsp/reqError.jsp");
				return false;
			}
		} catch (Exception ie) {
			request.setAttribute("status", "fail");
			request.setAttribute("message", "访问异常！");
		}
		request.setAttribute("status", "ok");
		return true;
	}

	// 拦截后处理
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler, ModelAndView mav)
			throws Exception {
		System.out.println("拦截后处理");
	}

	// 全部完成后处理
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception e)
			throws Exception {
		System.out.println("全部完成后处理");
		super.afterCompletion(request, response, handler, e);
	}
}
