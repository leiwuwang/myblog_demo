package com.leiwuwang.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

public class LoginFilter implements Filter{
	
	private FilterConfig config;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		config = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        
        String noLoginPaths = config.getInitParameter("noLoginPaths");  //过滤不需要登录即可访问的地址
        
        if(StringUtils.isNotEmpty(noLoginPaths)){
			String[] strArray = noLoginPaths.split(";");
			for (int i = 0; i < strArray.length; i++) {
				
				if(strArray[i]==null || "".equals(strArray[i]))continue;
				
				if(request.getRequestURI().indexOf(strArray[i])!=-1 ){
					chain.doFilter(req, resp);
					return;
				}
			}
			
		}
        
        String token = request.getParameter("token");//获取用户token
        
        if(StringUtils.isBlank(token)) {
        	this.noLoginMsg(response);
        	return;
        }
        
        token = Constants.LOGIN_BEAN_KEY + token;
        
        byte[] tokenByte = token.getBytes("utf-8");
        
        //从redis中取值
        byte[] val = RedisDbUtil.get(tokenByte, Constants.REDIS_USER_INFO_DB);
        
        if(null == val || val.length == 0) {
        	
        	this.noLoginMsg(response);
        	return;
        	
        }else {
        	//因为有可能不止这一个过滤器,所以需要将所有的过滤器执行
            chain.doFilter(req, resp);
        }
        
	}
	
	private void noLoginMsg(HttpServletResponse response) {
		String json="{\"code\":1001,\"message\":\"您还没有登陆，请先登陆！\"}";
    	
    	try {
   	      response.getWriter().write(json);
   	      response.getWriter().flush();
   	    } catch (IOException e) {
   	      throw new RuntimeException(e.getMessage(), e);
   	    }
	}

	@Override
	public void destroy() {
		
	}
	
	
	//通过重写getParameter方法,实现get方法自动转码
    class MyWrapper extends HttpServletRequestWrapper{
        private HttpServletRequest request;
        public MyWrapper(HttpServletRequest request) {
            super(request);
            this.request = request;
        }

        @Override
        public String getParameter(String name) {
            String value = this.request.getParameter(name);
            if(value == null){
                return null;
            }

            //如果是get方法提交过来的
            if(this.request.getMethod().equalsIgnoreCase("get")){
                try {
                    // 进行转码
                    value = new String(value.getBytes("ISO-8859-1"),"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            //屏蔽尖括号 换成 &gt; &lt;
            value = value.replace(">","&gt;");
            value = value.replace("<","&lt;");

            return value;
        }
    }

}
