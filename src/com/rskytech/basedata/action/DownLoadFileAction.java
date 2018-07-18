package com.rskytech.basedata.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.richong.arch.action.BaseAction;
/**
 * 下载导入基础数据模板
 * @author fys
 * @createDate 2013-04-08
 */
public class DownLoadFileAction  extends BaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1425039696104344787L;
	private static final int BUFFER_SIZE = 20 * 1024;
	//下载区域导入模板
	public String zoneDownloadFile() {
		boolean isExists = true;
		HttpServletResponse response = ServletActionContext.getResponse();

		File file = new File(ServletActionContext.getServletContext().getRealPath("/download/zone.xlsx"));
		if (file != null && file.exists()) {
			try {
				response.setHeader("Content-Disposition",
						"attachment;filename="+new String(file.getName().getBytes("gb2312"),"ISO8859-1"));
				InputStream in = new BufferedInputStream(new FileInputStream(
						file), BUFFER_SIZE);
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
		} else {
			isExists = false;
		}
		if (!isExists) {
			ServletActionContext.getRequest().setAttribute("msg", "fileno");
			return SUCCESS;
		}
		return null;
	}
	
	//下载ATA导入模板
	public String ataDownloadFile() {
		boolean isExists = true;
		HttpServletResponse response = ServletActionContext.getResponse();
		File file = new File(ServletActionContext.getServletContext().getRealPath("/download/ata.xlsx"));
		if (file != null && file.exists()) {
			try {
				response.setHeader("Content-Disposition",
						"attachment;filename="+new String(file.getName().getBytes("gb2312"),"ISO8859-1"));
				InputStream in = new BufferedInputStream(new FileInputStream(
						file), BUFFER_SIZE);
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
		} else {
			isExists = false;
		}
		if (!isExists) {
			ServletActionContext.getRequest().setAttribute("msg", "fileno");
			return SUCCESS;
		}
		return null;
	}
	
	//下载供应商导入模板
	public String vendorDownloadFile() {
		boolean isExists = true;
		HttpServletResponse response = ServletActionContext.getResponse();

		File file = new File(ServletActionContext.getServletContext().getRealPath("/download/vendor.xlsx"));
		if (file != null && file.exists()) {
			try {
				response.setHeader("Content-Disposition",
						"attachment;filename="+new String(file.getName().getBytes("gb2312"),"ISO8859-1"));
				InputStream in = new BufferedInputStream(new FileInputStream(
						file), BUFFER_SIZE);
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
		} else {
			isExists = false;
		}
		if (!isExists) {
			ServletActionContext.getRequest().setAttribute("msg", "fileno");
			return SUCCESS;
		}
		return null;
	}
}
