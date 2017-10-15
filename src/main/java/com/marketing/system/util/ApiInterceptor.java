package com.marketing.system.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lsk on 2017/10/14. wd
 */
@SuppressWarnings({ "rawtypes" })
@Component
public class ApiInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory
			.getLogger(ApiInterceptor.class);


	public static Map<String,Object> getParams(HttpServletRequest request) {
		Map<String, String[]> rec = request.getParameterMap();
		Map<String, Object> result = new LinkedHashMap<String, Object>();

		for (Map.Entry<String, String[]> entry : rec.entrySet()) {
			String name = entry.getKey();
			Object value = entry.getValue()[0];
			result.put(name, value);
		}
		return result;
	}


	
	@Override
	public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
		String uri = request.getRequestURI();
		Map< String, Object>  requestMap = getParams(request);

		String token = request.getHeader("token");

		Map<String, Object> rec = new LinkedHashMap<String, Object>();
		String _signMsg;
		// 登录后的请求地址都带有/act/
		boolean flag;
		if (uri.contains("/act/")) {
			if (StringUtil.isEmpty(token)) {
				rec.put("code", 400);
				rec.put("msg", "没有token或signMsg");

				return false;
			}
			

			// 不需要登录的地址可能没有token
		} else {

		}

		
		// 如果带有token，则说明已经登陆
		if (StringUtil.isNotBlank(token)) {
			rec.put("code", 400);
			rec.put("msg", "没有token或signMsg");
			ApiResult<String> result = new ApiResult<>(Constant.FAIL_CODE_VALUE,Constant.OPERATION_FAIL,null,null);

			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}
}
