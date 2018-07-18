package com.rskytech.report.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.lhirf.bo.ILhMainBo;
import com.rskytech.pojo.ComReport;
import com.rskytech.pojo.LhMain;
import com.rskytech.pojo.MMain;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.ZaMain;
import com.rskytech.report.bo.IComReportBo;
import com.rskytech.report.excel.mpd.bo.IMpdExcelBo;
import com.rskytech.report.excel.mrb.bo.IMrbExcelBo;
import com.rskytech.report.word.reportArea.bo.IReportAreaBo;
import com.rskytech.report.word.reportLhirf.bo.ILhirfWordReportBo;
import com.rskytech.report.word.reportStruct.bo.IReportStructBo;
import com.rskytech.report.word.reportSystem.bo.IReportSystemBo;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ComReportAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4469733757127809988L;
	private IComReportBo comReportBo;
	private String generateId;//各打报告的传入的id
	private String reportType;//各打报告类型
	private String isMaintain;//是否可修改
	private String reportId;
	private IReportSystemBo reportSystemBo;
	private IReportStructBo reportStructBo;
	private IReportAreaBo reportAreaBo;
	private ILhirfWordReportBo lhirfWordReportBo;
	private ILhMainBo lhMainBo;
	private IMrbExcelBo mrbExcelBo;
	private IMpdExcelBo mpdExcelBo;

	public String init(){
		return SUCCESS;
	}

	/**
	 * 得到报告列表
	* @Title: loadComReportList
	* @Description:
	* @return
	* @author samual
	* @date 2014年11月21日 上午10:34:53
	* @throws
	 */
	public String loadComReportList(){
		if (this.getPage() == null)
			this.setPage(new com.richong.arch.web.Page());
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList();
		Page page1 = comReportBo.loadComReportList(page, this.getComModelSeries().getModelSeriesId(), reportType, generateId);
		List lst=page1.getResult();
		
		if (lst != null) {
			for (Object obj : lst) {
				Object []objs=(Object[])obj;
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("reportId", objs[0]);
				hm.put("reportName", objs[1]);
				hm.put("versionNo",  objs[2]);
				hm.put("versionUserId",  objs[3]);
				hm.put("versionUserName",  objs[4]);
				hm.put("versionDate",  objs[5]);
				hm.put("reportStatusCode",  objs[6]);
				hm.put("reportStatusShow",  getReportStatusShow(String.valueOf(objs[6])));
				hm.put("reportWordUrl",  objs[7]);
				hm.put("reportPdfUrl",  objs[8]);
				hm.put("versionDesc",  objs[9]);
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
		json.element("comReport", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	/**
	 * 修改报告信息
	* @Title: updateComReports
	* @Description:
	* @return
	* @author samual
	* @date 2014年11月21日 上午10:35:07
	* @throws
	 */
	public String updateComReports(){
		JSONObject json = new JSONObject();
		String jsonData = this.getJsonData();
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
//		System.out.println(jsonArray.size());
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			String reportId = jsonObject.getString("reportId");
			ComReport comReport;
			// db操作区分
			String dbOperate = "";
			if (!BasicTypeUtils.isNullorBlank(reportId)) {// 修改操作时
				dbOperate = ComacConstants.DB_UPDATE;
				comReport = (ComReport) comReportBo.loadById(ComReport.class, reportId);
			} else {// 追加操作时
				dbOperate = ComacConstants.DB_INSERT;
				comReport = new ComReport();
				comReport.setComModelSeries(this.getComModelSeries());
				comReport.setReportType(reportType);
				comReport.setGenerateId(generateId);
				comReport.setVersionDate(new Date());
				comReport.setVersionUser(this.getSysUser().getUserId());
				comReport.setValidFlag(Integer.valueOf(ComacConstants.VALIDFLAG_YES));
				comReport.setReportStatus(ComacConstants.REPORT_STATUS_NEW);
			}
			comReport.setReportName(jsonObject.getString("reportName"));
			comReport.setVersionNo(jsonObject.getString("versionNo"));
			comReport.setVersionDesc(jsonObject.getString("versionDesc"));
			comReportBo.saveOrUpdate(comReport, dbOperate, getSysUser().getUserId());
		}

		json.put("success", true);
		writeToResponse(json.toString());
		return null;
	}

	/**
	 * 删除报告
	* @Title: deleteComReportById
	* @Description:
	* @return
	* @author samual
	* @date 2014年11月21日 上午10:35:19
	* @throws
	 */
	public String deleteComReportById(){
		if (this.reportId != null && !"".equals(this.reportId)) {
			ComReport cmReport = (ComReport) comReportBo.loadById(ComReport.class,this.reportId);
			if (cmReport != null) {
				cmReport.setValidFlag(ComacConstants.NO);
				comReportBo.saveOrUpdate(cmReport, ComacConstants.DB_DELETE, getSysUser().getUserId());
			}
		}
		return null;
	}
	
	/**
	 * 根据状态的code 得到show
	* @Title: getReportStatusShow
	* @Description:
	* @param statusCode
	* @return
	* @author samual
	* @date 2014年11月21日 上午11:01:56
	* @throws
	 */
	private String getReportStatusShow(String statusCode){
		String statusShow = "";
		if(statusCode != null && !"".equals(statusCode)){
			if(ComacConstants.REPORT_STATUS_NEW.equals(statusCode)){
				statusShow = ComacConstants.REPORT_STATUS_NEW_SHOW;
			}else if(ComacConstants.REPORT_STATUS_NEW.equals(statusCode)){
				statusShow = ComacConstants.REPORT_STATUS_NEW_SHOW;
			}
		}
		return statusShow;
	}
	
	/**
	 * 根据报告类型生成不同区域分析的报告
	 * @return
	 */
	public String generateComReport(){
		String reportUrl =null;
		ComReport cr=(ComReport) this.comReportBo.loadById(ComReport.class, reportId);
		if(reportType!=null&&ComacConstants.SYSTEM_CODE.equals(reportType)){
			MMain mMain = (MMain) this.reportSystemBo.loadById(MMain.class, generateId);
			if(mMain.getStatus()!=null&&(mMain.getStatus().equals(ComacConstants.ANALYZE_STATUS_MAINTAINOK)
					||mMain.getStatus().equals(ComacConstants.ANALYZE_STATUS_APPROVED)
					||mMain.getStatus().equals(ComacConstants.ANALYZE_STATUS_HOLD))){
				reportUrl=reportSystemBo.createReport(generateId,cr.getReportName());
			}else{
				writeToResponse("{success:'noPermission'}");
				return null;
			}
		}else if(reportType!=null&&ComacConstants.STRUCTURE_CODE.equals(reportType)){
			SMain sMain = (SMain) this.reportSystemBo.loadById(SMain.class, generateId);
			if(sMain.getStatus()!=null&&(sMain.getStatus().equals(ComacConstants.ANALYZE_STATUS_MAINTAINOK)
					||sMain.getStatus().equals(ComacConstants.ANALYZE_STATUS_APPROVED)
					||sMain.getStatus().equals(ComacConstants.ANALYZE_STATUS_HOLD))){
				reportUrl=reportStructBo.createReport(generateId,cr.getReportName(),getSysUser().getUserId());
			}else{
				writeToResponse("{success:'noPermission'}");
				return null;
			}
		}else if(reportType!=null&&ComacConstants.ZONAL_CODE.equals(reportType)){
			ZaMain zaMain = (ZaMain) this.reportAreaBo.loadById(ZaMain.class, generateId);
			if(zaMain.getStatus()!=null&&(zaMain.getStatus().equals(ComacConstants.ANALYZE_STATUS_MAINTAINOK)
					||zaMain.getStatus().equals(ComacConstants.ANALYZE_STATUS_APPROVED)
					||zaMain.getStatus().equals(ComacConstants.ANALYZE_STATUS_HOLD))){
				reportUrl=reportAreaBo.createReport(generateId,cr.getReportName());
			}else{
				writeToResponse("{success:'noPermission'}");
				return null;
			}
		}else if(reportType!=null&&ComacConstants.LHIRF_CODE.equals(reportType)){
			LhMain lhMain = (LhMain) this.lhirfWordReportBo.loadById(LhMain.class, generateId);
			if(lhMain.getStatus()!=null&&(lhMain.getStatus().equals(ComacConstants.ANALYZE_STATUS_MAINTAINOK)
					||lhMain.getStatus().equals(ComacConstants.ANALYZE_STATUS_APPROVED)
					||lhMain.getStatus().equals(ComacConstants.ANALYZE_STATUS_HOLD))){
				reportUrl=lhirfWordReportBo.createReport(lhMain,cr.getReportName());
			}else{
				writeToResponse("{success:'noPermission'}");
				return null;
			}
		}else if(reportType!=null&&ComacConstants.MRB.equals(reportType)){			
			reportUrl = mrbExcelBo.createMrbExcel(cr.getReportName(), this.getComModelSeries());
		}else if(reportType!=null&&ComacConstants.MPD.equals(reportType)){
			reportUrl = mpdExcelBo.createMpdExcel(cr.getReportName(), this.getComModelSeries());
		}
		if (reportUrl != null){
			if (ComacConstants.MRB.equals(reportType) || ComacConstants.MPD.equals(reportType)){
				cr.setReportExcelUrl(reportUrl);
			} else {
				cr.setReportWordUrl(reportUrl);
			}
			this.comReportBo.update(cr);
			writeToResponse("{success:'yes'}");
		}else{
			writeToResponse("{success:'no'}");
		}
		return null;
	}
	
	/**
	 * 下载报告
	 */
	public String downloadReport(){
		ComReport cr=(ComReport) this.comReportBo.loadById(ComReport.class, reportId);
		if (cr != null){
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest request = ServletActionContext.getRequest();
			
			String url = "";
			String realFilePath = "";
			String kuozhanName = "";
			if ("excel".equals(reportType)){//EXCEL报表
				url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + cr.getReportExcelUrl();
			} else {//WORD报表
				url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + cr.getReportWordUrl();
			}
			
			realFilePath = ServletActionContext.getServletContext().getRealPath(url.substring(url.lastIndexOf(ComacConstants.REPORT_FILE_PATH)));
			kuozhanName = url.substring(url.lastIndexOf("."));
			
			File file = new File(realFilePath);
			if (file != null && file.exists()) {
				try {
					response.setHeader("Content-Disposition", "attachment;filename=" + new String((cr.getReportName() + kuozhanName).getBytes(
											"gb2312"), "ISO8859-1"));
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				try {
					InputStream in = new BufferedInputStream(new FileInputStream(file), 20 * 1024);
					if (in != null) {
						OutputStream out = response.getOutputStream();
						int i;
						while ((i = in.read()) != -1) {
							out.write(i);
						}
						out.flush();
						in.close();
					}	
				} catch (Exception e) {
					logger.debug(e);
				}
			}
		}
		return null;
	}
	
	
	/**
	 * 判断报表是否存在
	 */
	public String reportIsExist(){
		ComReport cr=(ComReport) this.comReportBo.loadById(ComReport.class, reportId);
		boolean isExist = true;
		if(cr!=null){
			HttpServletRequest request = ServletActionContext.getRequest();
			String url = "";
			if ("excel".equals(reportType)){//EXCEL报表
				if(cr.getReportExcelUrl()!=null){
					url = request.getScheme() + "://" + request.getServerName()+ ":" + request.getServerPort() + cr.getReportExcelUrl();
				}else{
					isExist=false;
				}
			} else {//WORD报表
				if(cr.getReportWordUrl()!=null){
					url = request.getScheme() + "://" + request.getServerName()+ ":" + request.getServerPort() + cr.getReportWordUrl();
				}else{
					isExist=false;
				}
			}
			
			if (isExist){
				String realFilePath = ServletActionContext.getServletContext().getRealPath(url.substring(url.lastIndexOf(ComacConstants.REPORT_FILE_PATH)));
				File file = new File(realFilePath);
				if (file == null || !file.exists()) {
					isExist=false;
				}
			}
		}else{
			isExist=false;
		}
		if(!isExist){
			this.writeToResponse("{'isOk':'no'}");
		}
		return null;
	}
	
	public String getGenerateId() {
		return generateId;
	}


	public void setGenerateId(String generateId) {
		this.generateId = generateId;
	}


	public String getReportType() {
		return reportType;
	}


	public void setReportType(String reportType) {
		this.reportType = reportType;
	}


	public String getIsMaintain() {
		return isMaintain;
	}


	public void setIsMaintain(String isMaintain) {
		this.isMaintain = isMaintain;
	}

	public IComReportBo getComReportBo() {
		return comReportBo;
	}

	public void setComReportBo(IComReportBo comReportBo) {
		this.comReportBo = comReportBo;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public IReportSystemBo getReportSystemBo() {
		return reportSystemBo;
	}

	public void setReportSystemBo(IReportSystemBo reportSystemBo) {
		this.reportSystemBo = reportSystemBo;
	}

	public IReportStructBo getReportStructBo() {
		return reportStructBo;
	}

	public void setReportStructBo(IReportStructBo reportStructBo) {
		this.reportStructBo = reportStructBo;
	}

	public IReportAreaBo getReportAreaBo() {
		return reportAreaBo;
	}

	public void setReportAreaBo(IReportAreaBo reportAreaBo) {
		this.reportAreaBo = reportAreaBo;
	}

	public ILhirfWordReportBo getLhirfWordReportBo() {
		return lhirfWordReportBo;
	}

	public void setLhirfWordReportBo(ILhirfWordReportBo lhirfWordReportBo) {
		this.lhirfWordReportBo = lhirfWordReportBo;
	}

	public ILhMainBo getLhMainBo() {
		return lhMainBo;
	}

	public void setLhMainBo(ILhMainBo lhMainBo) {
		this.lhMainBo = lhMainBo;
	}

	public IMrbExcelBo getMrbExcelBo() {
		return mrbExcelBo;
	}

	public void setMrbExcelBo(IMrbExcelBo mrbExcelBo) {
		this.mrbExcelBo = mrbExcelBo;
	}

	public IMpdExcelBo getMpdExcelBo() {
		return mpdExcelBo;
	}

	public void setMpdExcelBo(IMpdExcelBo mpdExcelBo) {
		this.mpdExcelBo = mpdExcelBo;
	}
	
}
