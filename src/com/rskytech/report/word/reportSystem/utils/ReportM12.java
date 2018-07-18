package com.rskytech.report.word.reportSystem.utils;

import java.util.List;

import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.basedata.bo.IComVendorBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.M12;

public class ReportM12 extends SystemReportBase{
	
	private ComModelSeries ms;
	private List<M12> listM12;
	private IComVendorBo comVendorBo;
	private IComAreaBo comAreaBo;
	
	public ReportM12(Document document,ComModelSeries ms, List<M12> listM12, IComVendorBo comVendorBo, IComAreaBo comAreaBo) {
		super(document);
		this.ms=ms;
		this.listM12=listM12;
		this.comVendorBo=comVendorBo;
		this.comAreaBo=comAreaBo;
	}
	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(getCol(), getColWidth());
		Paragraph title = new Paragraph(getReportName()+"系统组成和相关信息"); 
		title.setFont(rpsTitle); 
		document.add(title); 
		ta.addCell(setCell("项目编号", fontCnNormal));
		ta.addCell(setCell("项目名称", fontCnNormal));
		ta.addCell(setCell("数量", fontCnNormal));
		ta.addCell(setCell("供应商", fontCnNormal));
		ta.addCell(setCell("件号（制造商/供应商）", fontCnNormal));
		ta.addCell(setCell("类似", fontCnNormal));
		ta.addCell(setCell("区域通道", fontCnNormal));
		ta.addCell(setCell("过去的MTBF", fontCnNormal));
		ta.addCell(setCell("预测的MTBF", fontCnNormal));
		ta.addCell(setCell("区域", fontCnNormal));
		ta.addCell(setCell("MMEL", fontCnNormal));
		if(listM12.size()>0){
			for(M12 m12 : listM12){
				ta.addCell(setCell(getStr(m12.getProCode()), fontCnNormal));
				ta.addCell(setCell(getStr(m12.getProName()), fontCnNormal));
				ta.addCell(setCell(getStr(m12.getQuantity()+""), fontCnNormal));
				ta.addCell(setCell(comVendorBo.getVendorNameById(m12.getVendor())==null?"":
					comVendorBo.getVendorNameById(m12.getVendor()), fontCnNormal));
				ta.addCell(setCell(getStr(m12.getPartNo()), fontCnNormal));
				ta.addCell(setCell(getStr(m12.getSimilar()), fontCnNormal));
				ta.addCell(setCell(getStr(m12.getZonalChannel()), fontCnNormal));
				ta.addCell(setCell(getStr(m12.getHistoricalMtbf()), fontCnNormal));
				ta.addCell(setCell(getStr(m12.getPredictedMtbf()), fontCnNormal));
				ta.addCell(setCell(getStr(comAreaBo.getAreaCodeByAreaId(m12.getZonal())), fontCnNormal));
				ta.addCell(setCell(m12.getMmel()==null?"":m12.getMmel()+"", fontCnNormal));
			}
		}else{
			for(int i=0;i<22;i++){
				ta.addCell(setCell("", fontCnNormal));
			}
		}
		return ta;
	}
	
	@Override
	public String getReportName() {
		return getStr(ms.getModelSeriesName());
	}

	@Override
	public int getCol() {
		return 11;
	}

	@Override
	public float[] getColWidth() {
		return new float[]{0.09f,0.09f,0.09f,0.09f,0.09f,0.09f,0.09f,0.09f,0.09f,0.09f,0.09f};
	}


}
