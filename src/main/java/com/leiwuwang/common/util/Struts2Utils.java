package com.leiwuwang.common.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

/**
 * @Project api
 * @Description request请求后返回JSON格式数据结果
 * @Author leiwang
 * @Create 2018年9月23日 下午4:40:56
 * @Modified By:
 * @Modified Date:
 * @Why <修改原因描述>
 */
public class Struts2Utils {
	
	//-- header 常量定义 --//
	private static final String HEADER_ENCODING = "encoding";
	private static final String HEADER_NOCACHE = "no-cache";
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final boolean DEFAULT_NOCACHE = true;
	
	/**
	* @Description 得到Session
	* @return
	* @Author leiwang
	* @Date 2018年9月23日 下午4:58:49
	 */
	public static HttpSession getSession() {
		return ServletActionContext.getRequest().getSession();
	}
	
	/**
	* @Description 得到request
	* @return
	* @Author leiwang
	* @Date 2018年9月23日 下午4:59:24
	 */
	public static HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}
	 
	/**
	* @Description 返回数据
	* @param contentType
	* @param content
	* @param headers
	* @Author leiwang
	* @Date 2018年9月23日 下午5:14:28
	 */
	public static void render(String contentType, String content, String[] headers){
	    HttpServletResponse response = initResponseHeader(contentType, headers);
	    try {
	      response.getWriter().write(content);
	      response.getWriter().flush();
	    } catch (IOException e) {
	      throw new RuntimeException(e.getMessage(), e);
	    }
	}

	/**
	* @Description 返回文本数据
	* @param text
	* @param headers
	* @Author leiwang
	* @Date 2018年9月23日 下午5:15:05
	 */
	public static void renderText(String text, final String... headers){
	    render("text/plain", text, headers);
	}

	/**
	* @Description 返回html
	* @param html
	* @param headers
	* @Author leiwang
	* @Date 2018年9月23日 下午5:16:12
	 */
	public static void renderHtml(String html, final String... headers){
	    render("text/html", html, headers);
	}

	/**
	* @Description 返回xml
	* @param xml
	* @param headers
	* @Author leiwang
	* @Date 2018年9月23日 下午5:16:25
	 */
	public static void renderXml(String xml, final String... headers){
	    render("text/xml", xml, headers);
	}

	/**
	* @Description 返回json
	* @param jsonString
	* @param headers
	* @Author leiwang
	* @Date 2018年9月23日 下午5:16:33
	 */
	public static void renderJson(String jsonString, final String... headers){
	    render("application/json", jsonString, headers);
	}

	/**
	* @Description 初始化请返回Response
	* @param contentType
	* @param headers
	* @return
	* @Author leiwang
	* @Date 2018年9月23日 下午5:16:42
	 */
	private static HttpServletResponse initResponseHeader(final String contentType, final String... headers) {
		
		//分析headers参数
		String encoding = DEFAULT_ENCODING;
		boolean noCache = DEFAULT_NOCACHE;
		for (String header : headers) {
			String headerName = StringUtils.substringBefore(header, ":");
			String headerValue = StringUtils.substringAfter(header, ":");
 
 
			if (StringUtils.equalsIgnoreCase(headerName, HEADER_ENCODING)) {
				encoding = headerValue;
			} else if (StringUtils.equalsIgnoreCase(headerName, HEADER_NOCACHE)) {
				noCache = Boolean.parseBoolean(headerValue);
			} else {
				throw new IllegalArgumentException(headerName + "不是一个合法的header类型");
			}
		}
 
		HttpServletResponse response = ServletActionContext.getResponse();
		
		//设置headers参数
		String fullContentType = contentType + ";charset=" + encoding;
		response.setContentType(fullContentType);
		if (noCache) {
			ServletUtils.setNoCacheHeader(response);
		}
 
		return response;
	}


}
