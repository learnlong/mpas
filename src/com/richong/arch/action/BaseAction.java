package com.richong.arch.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;

public class BaseAction extends ActionSupport {

	protected Logger logger = Logger.getLogger(this.getClass());

	private static final long serialVersionUID = -4970270818904709095L;

	protected String keyword; // 查询关键字
	protected String method; // 操作
	protected String jsonData; // 前台传递json字符串
	private String processEntityId; // 要操作的实体类的ID

	/**
	 * 分页对象
	 */
	protected Page page = new Page();

	protected int start;

	protected int limit;

	/**
	 *用于初始化获得当前登录用户信息
	 * @author changyf
	 * @throws IOException 
	 * @createdate 2012-08-02
	 */
	protected ComUser getSysUser() {
		return (ComUser) ServletActionContext.getContext().getSession().get(ComacConstants.SESSION_USER_KEY);
	}
	
	/**
	 *用于获得当前使用的机型
	 * @throws IOException 
	 * @createdate 2012-08-02
	 */
	protected ComModelSeries getComModelSeries() {
		Map<String, Object> sessionMap = ServletActionContext.getContext()
				.getSession();
		if (sessionMap != null) {
			Object userSessionObj = sessionMap
					.get(ComacConstants.SESSION_NOW_MODEL_SERIES);
			if (userSessionObj != null) {
				return (ComModelSeries)userSessionObj;
			}
		}
		return null;
	}
	
	/**
	 * 将指定字符串输出到页面
	 * 
	 * @param str
	 * @throws IOException
	 */
	public void writeToResponse(String str) {
		try {
			ActionContext ctx = ActionContext.getContext();
			HttpServletResponse response = (HttpServletResponse) ctx
					.get(ServletActionContext.HTTP_RESPONSE);
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = null;
			out = response.getWriter();
			out.write(str);
		} catch (Exception e) {
			logger.debug("页面输出失败！\n" + e.getMessage());
		}
	}

	public String getContextPath() {
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		return request.getContextPath();
	}
	
	public static String getServletContextPath() {
		return ServletActionContext.getServletContext().getRealPath("/");
	}

	public JSONObject putJsonOKFlag(JSONObject json, boolean isOk) {
		if (json == null) {
			json = new JSONObject();
		}
		json.put("success", isOk);
		json.put("failure", !isOk);
		return json;
	}
	
	/**
	 * 通过ascii码来获取字符串的长度,其中中文状态下的输入为2,英文状态下的为1
	 * @param str
	 * @return
	 */
	public static int findNumberByAscii(String str){
		char[]cas=str.toCharArray();
		int count=cas.length;
		for(char c:cas){
			if(((int)c)>160){
				count++;				
			}	
		}
		return count;		
	}
	
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public String getProcessEntityId() {
		return processEntityId;
	}

	public void setProcessEntityId(String processEntityId) {
		this.processEntityId = processEntityId;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
}
