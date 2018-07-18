package com.rskytech.report.excel.mpd.bo.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.TaskMpd;
import com.rskytech.report.excel.mpd.bo.IMpdExcelBo;
import com.rskytech.task.dao.IMpdMaintainDao;
import com.rskytech.util.Export07Excel;
import com.rskytech.util.StringUtil;

public class MpdExcelBo implements IMpdExcelBo {

	private IMpdMaintainDao mpdMaintainDao;
	
	private IComAreaBo comAreaBo;
	
	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}

	public String createMpdExcel(String reportName, ComModelSeries comModelSeries) {
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
		
		Export07Excel<TaskMpd> ex = new Export07Excel<TaskMpd>();
		try {
			List taskMpdList = mpdMaintainDao.getTaskMpdListBysql(comModelSeries.getModelSeriesId());
			List<ComArea> comaAreaList = comAreaBo.findAllAreaSort(comModelSeries);
			FileOutputStream out = new FileOutputStream(filePath + reportName);
			//生成mpdexcel实现方法
			ex.exportMpdExcel(taskMpdList, comaAreaList, out);
			out.close();
			System.out.println("excel导出成功！");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}

	public IMpdMaintainDao getMpdMaintainDao() {
		return mpdMaintainDao;
	}

	public void setMpdMaintainDao(IMpdMaintainDao mpdMaintainDao) {
		this.mpdMaintainDao = mpdMaintainDao;
	}

	public String getReportPath(String dataString){
		return ComacConstants.REPORT_FILE_PATH + "mpd/" + dataString + "/";
	}

	
}
