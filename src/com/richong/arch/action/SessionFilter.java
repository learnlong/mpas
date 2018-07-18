package com.richong.arch.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.rskytech.ComacConstants;

/**
 * Session过滤器
 * @author changyf
 * createdate 2012-08-12
 */
public class SessionFilter implements Filter {

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filter) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;//获取httprequest对象
		HttpServletResponse httpResponse = (HttpServletResponse) response;//获取httpresponse对象
		HttpSession session = httpRequest.getSession();
		String url=((HttpServletRequest)request).getRequestURI();//获取请求页面的url
		url=url.substring(url.lastIndexOf("/")+1,url.length());//进行截断
		List<String>doNotFilter=new ArrayList<String>();//不经过filter 的url地址集合
		doNotFilter.add("logout.do");//不想经过过滤器的页面写这里
		
		boolean isFilter=true;
		for(String str:doNotFilter){
			if(str.equals(url)){
				filter.doFilter(httpRequest, httpResponse);//屏蔽页面直接去实际action,不再运行过滤器
				isFilter=false;
			}
		}
		if(isFilter){//判断是否屏蔽过滤器
			// 获取用户session
			Object userSession = session
					.getAttribute(ComacConstants.SESSION_USER_KEY);
			// 获取request头部信息
			String head = httpRequest.getHeader("x-requested-with");		        
			// 头部信息不为空并且是ajax请求时	    
		       if(head != null){
				// 判断用户session为空时
				if (userSession == null) {			
					// 添加respone报错标识
					httpResponse.setStatus(999);
				} else {
					filter.doFilter(httpRequest, httpResponse);
				}
			} else {
				filter.doFilter(httpRequest, httpResponse);
			}
		}
	}

	public void init(FilterConfig arg0) throws ServletException {

	}
}
