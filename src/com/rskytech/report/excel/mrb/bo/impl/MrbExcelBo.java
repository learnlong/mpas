package com.rskytech.report.excel.mrb.bo.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.TaskMrb;
import com.rskytech.report.excel.mrb.bo.IMrbExcelBo;
import com.rskytech.task.dao.IMrbMaintainDao;
import com.rskytech.util.Export07Excel;
import com.rskytech.util.StringUtil;

@SuppressWarnings("rawtypes")
public class MrbExcelBo extends BaseBO implements IMrbExcelBo {

	private IMrbMaintainDao mrbMaintainDao;
	
	private IComAreaBo comAreaBo;
	
	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}

	public String createMrbExcel(String reportName, ComModelSeries comModelSeries) {
		String returnValue =null;
		String dataString = StringUtil.getDataString();
		String path = ServletActionContext.getRequest().getContextPath() + getReportPath(dataString);
		String filePath = ServletActionContext.getServletContext().getRealPath("/") + getReportPath(dataString);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("octets/stream");
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddhhmmss");
		reportName = reportName + "-" + sf.format(date) +  ".xls";
		response.addHeader("Content-Disposition", "attachment;filename="
				+ reportName);
		returnValue = path + reportName;
		
		File file = new File(filePath);
		if (!file.exists()){
			file.mkdirs();
		}
		
		Export07Excel<TaskMrb> ex = new Export07Excel<TaskMrb>();
		try {
			List taskMrbList = mrbMaintainDao.getTaskMrbListBysql(comModelSeries.getModelSeriesId());
			List<ComArea> comaAreaList = comAreaBo.findAllAreaSort(comModelSeries);
			FileOutputStream out = new FileOutputStream(filePath + reportName);
			ex.exportMrbExcel(taskMrbList, comaAreaList, out);
			out.close();
			System.out.println("excel导出成功！");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}
	
	public IMrbMaintainDao getMrbMaintainDao() {
		return mrbMaintainDao;
	}

	public void setMrbMaintainDao(IMrbMaintainDao mrbMaintainDao) {
		this.mrbMaintainDao = mrbMaintainDao;
	}

	
	public String getReportPath(String dataString){
		return ComacConstants.REPORT_FILE_PATH + "mrb/" + dataString + "/";
	}
}
