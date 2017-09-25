package com.marketing.system.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map;


public class ServletUtils {
	private static final Logger logger = LoggerFactory.getLogger(ServletUtils.class);

	public static final long ONE_DAY_SECONDS = 60L * 60 * 24;
	public static final long ONE_WEEK_SECONDS = ONE_DAY_SECONDS * 7;
	public static final long ONE_MONTH_SECONDS = ONE_DAY_SECONDS * 30;
	public static final long ONE_YEAR_SECONDS = ONE_DAY_SECONDS * 365;
	private static final String CONTENT_TYPE = "content-type";

	public static final String EXCEL_TYPE = "application/vnd.ms-excel";
	public static final String HTML_TYPE = "text/html";
	public static final String JS_TYPE = "text/javascript";
	public static final String CSS_TYPE = "text/css";
	public static final String JSON_TYPE = "application/json";
	public static final String XML_TYPE = "text/xml";
	public static final String TEXT_TYPE = "text/plain";

	public static final String CODE_UTF8 = "UTF-8";

	private static ObjectMapper mapper = new ObjectMapper();
	// public static final String CODE_GBK = "GBK";

	public static void setContentType(HttpServletResponse response,
			String contentType, String encoding) {
		setContentType(response, contentType);
		response.setCharacterEncoding(encoding);
	}

	public static void setContentType(HttpServletResponse response,
			String contentType) {
		response.setContentType(contentType);
	}

	/**
	 * 解决请求跨域问题
	 * @param response
	 * @param res
	 */
	public static void writeToResponse(HttpServletResponse response, Map<? extends Object, Object> res) {
		response.addHeader(CONTENT_TYPE, JS_TYPE);
		response.setContentType("application/json");
		response.setCharacterEncoding(CODE_UTF8);
		OutputStreamWriter out = null;
		try {
			out = new OutputStreamWriter(response.getOutputStream(), CODE_UTF8);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		write(out, res);
	}
	public static void write(Writer writer,Object o){
		try {
			mapper.writeValue(writer,o);
		}
		catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

}
