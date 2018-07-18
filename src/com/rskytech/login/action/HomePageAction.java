package com.rskytech.login.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.richong.arch.action.BaseAction;
import com.richong.arch.web.Page;
import com.rskytech.sys.bo.ISysTreeBo;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class HomePageAction extends BaseAction {
	
	private ISysTreeBo sysTreeBo;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8800094449085785572L;

	public String init() {
		return SUCCESS;
	}

	/**
	 * 进入代办列表的页面
	* @Title: initWaitTask
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月22日 上午10:23:45
	* @throws
	 */
	public String initWaitTask() {
		
		return "waittask";
	}
	
	/**
	 * 得到首页代办分析
	* @Title: getWaitAnalysisLis
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月23日 下午4:40:36
	* @throws
	 */
	public String getWaitAnalysisLis(){
		if (this.getPage() == null)
			this.setPage(new com.richong.arch.web.Page());
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		String contextPath = ServletActionContext.getServletContext().getContextPath();
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList();
		Page page1 = sysTreeBo.getWaitAnalysisLisForHomePage(page, this.getComModelSeries().getModelSeriesId(), getSysUser().getUserId());
		List lst=page1.getResult();
		if (lst != null) {
			for (Object obj : lst) {
				Object []objs=(Object[])obj;
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("mainId", objs[0]);
				hm.put("ataorareaId",  objs[1]);
				hm.put("ataorareaCode",  objs[2]);
				hm.put("ataorareaName",  objs[3]);
				hm.put("status",  objs[4]);
				hm.put("createDate",  objs[5]);
				hm.put("urlParam",  objs[6]);
				hm.put("analysisType",  objs[7]);
				String checkOperate = "<a title='分析' href='javascript:void(0)'><img src='"
						+ contextPath
						+ "/images/toAuditBtn.gif'"
						+ " onclick='doAnalysisOpenWindow(\""
						+ String.valueOf(objs[7])
						+ "\",\""
						+ String.valueOf(objs[6])
						+ "\")'/></a>";
				hm.put("checkOperate",  checkOperate);
				listJsonFV.add(hm);
			}
		}
		if(page1.getTotalCount()<page1.getPageSize()){
			page1.setTotalPages(1);
		}else{
			page1.setTotalPages(page1.getTotalCount()%page1.getPageSize()==0?page1.getTotalCount()/page1.getPageSize():page1.getTotalCount()/page1.getPageSize()+1);
		}
		page.setTotalPages(page1.getTotalPages());
		json.element("total", page1.getTotalCount());
		json.element("analysis", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	public ISysTreeBo getSysTreeBo() {
		return sysTreeBo;
	}

	public void setSysTreeBo(ISysTreeBo sysTreeBo) {
		this.sysTreeBo = sysTreeBo;
	}
	
}
