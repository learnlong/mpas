package com.rskytech.basedata.action;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.richong.arch.action.BaseAction;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.basedata.bo.IComAtaBo;
import com.rskytech.basedata.dao.IComVendorDao;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComVendor;
import com.rskytech.util.Export07Excel;

/**
 * 导出基础数据
 * @author samual
 *
 */
public class ExportExcelAction  extends BaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4706512459181736151L;
	private IComAtaBo comAtaBo;
	private IComAreaBo comAreaBo;
	private IComVendorDao comVendorDao;
	
	public IComVendorDao getComVendorDao() {
		return comVendorDao;
	}

	public void setComVendorDao(IComVendorDao comVendorDao) {
		this.comVendorDao = comVendorDao;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}

	public IComAtaBo getComAtaBo() {
		return comAtaBo;
	}

	public void setComAtaBo(IComAtaBo comAtaBo) {
		this.comAtaBo = comAtaBo;
	}

	//导出ata数据
	public String ataExport() {
		Date date=new Date();
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddhhmmss");
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("octets/stream");
		response.addHeader("Content-Disposition","attachment;filename=ata_"+sf.format(date)+".xls");
		Export07Excel<ComAta> ex = new Export07Excel<ComAta>();
		try {
			List<ComAta> dataset = comAtaBo.findAllAtaSort(this.getComModelSeries());
			OutputStream out = response.getOutputStream();
			ex.exportAtaExcel(dataset, out);
			out.close();
			System.out.println("excel导出成功！");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	//导出区域数据
	public String zoneExport() {
		Date date=new Date();
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddhhmmss");
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("octets/stream");
		response.addHeader("Content-Disposition","attachment;filename=zone_"+sf.format(date)+".xls");
		Export07Excel<ComArea> ex = new Export07Excel<ComArea>();
		try {
			List<ComArea> comAreaList = comAreaBo.findAllAreaSort(this.getComModelSeries());
			OutputStream out = response.getOutputStream();
			ex.exportAreaExcel(comAreaList, out);
			out.close();
			System.out.println("excel导出成功！");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//导出供应商数据
	public String vendorExport() {
		Date date=new Date();
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddhhmmss");
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("octets/stream");
		response.addHeader("Content-Disposition","attachment;filename=vendor_"+sf.format(date)+".xls");
		Export07Excel<ComVendor> ex = new Export07Excel<ComVendor>();
		try {
			List<ComVendor> dataset = comVendorDao.loadVendorList(this.getComModelSeries().getModelSeriesId());
			OutputStream out = response.getOutputStream();
			ex.exportVendorExcel(dataset, out);
			out.close();
			System.out.println("excel导出成功！");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
