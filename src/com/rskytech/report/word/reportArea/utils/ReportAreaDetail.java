package com.rskytech.report.word.reportArea.utils;

import java.util.List;

import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.rskytech.basedata.dao.IComAreaDao;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComAreaDetail;
import com.rskytech.pojo.ComModelSeries;

public class ReportAreaDetail extends AreaReportBase {

	private IComAreaDao comAreaDao;
	
	public ReportAreaDetail(Document document, ComModelSeries ms, ComArea area, IComAreaDao comAreaDao) {
		super(document, ms, area);
		this.comAreaDao = comAreaDao;
	}

	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(getCol(), getColWidth());
		ta.addCell(setCell("2  " + ms.getModelSeriesCode() + "区域分析", fontCnTitle, Element.ALIGN_LEFT,  Element.ALIGN_MIDDLE, 8, null, 0, null));
		
		ta.addCell(setCell("表1  " + area.getAreaCode() + "区域检查分析信息", fontCnTitle, Element.ALIGN_CENTER,  Element.ALIGN_MIDDLE, 8, null, 0, null));
		
		ta.addCell(setCell("子区\n域号", fontCnTitle));
		ta.addCell(setCell("子区域名称", fontCnTitle));
		ta.addCell(setCell("口盖", fontCnTitle));
		ta.addCell(setCell("设备名称", fontCnTitle));
		ta.addCell(setCell("设备图号", fontCnTitle));
		ta.addCell(setCell("设备型号", fontCnTitle));
		ta.addCell(setCell("含何种电缆电线、\n何种管路", fontCnTitle));
		ta.addCell(setCell("备注", fontCnTitle));
		
		getArea(area, ta);
		getChildArea(area, ta);
		return ta;
	}
	
	private void getChildArea(ComArea area, Table ta) throws Exception{
		List<ComArea> list = comAreaDao.loadChildArea(ms.getModelSeriesId(), area.getAreaId());
		if (list != null && list.size() > 0){
			for (ComArea ca : list){
				getArea(ca, ta);
				getChildArea(ca, ta);
			}
		}
	}
	
	private void getArea(ComArea area, Table ta) throws Exception{
		List<ComAreaDetail> list = comAreaDao.loadAreaDetail(area.getAreaId());
		if (list == null || list.size() == 0){
			ta.addCell(setCell(area.getAreaCode(), fontCnNormal));
			ta.addCell(setCell(getStr(area.getAreaName()), fontCnNormal));
			ta.addCell(setCell(getStr(area.getReachWay()), fontCnNormal));
			ta.addCell(setCell("", fontCnNormal));
			ta.addCell(setCell("", fontCnNormal));
			ta.addCell(setCell("", fontCnNormal));
			ta.addCell(setCell(getStr(area.getWirePiping()), fontCnNormal));
			ta.addCell(setCell(getStr(area.getRemark()), fontCnNormal));
		} else {
			int size = list.size();
			
			ta.addCell(setCell(area.getAreaCode(), fontCnNormal, null, size, null));
			ta.addCell(setCell(getStr(area.getAreaName()), fontCnNormal, null, size, null));
			ta.addCell(setCell(getStr(area.getReachWay()), fontCnNormal, null, size, null));
			
			for (int i = 0; i < list.size(); i++){
				ComAreaDetail cad = list.get(i);
				ta.addCell(setCell(getStr(cad.getEquipmentName()), fontCnNormal));
				ta.addCell(setCell(getStr(cad.getEquipmentPicNo()), fontCnNormal));
				ta.addCell(setCell(getStr(cad.getEquipmentTypeNo()), fontCnNormal));
				
				if (i == 0){
					ta.addCell(setCell(getStr(area.getWirePiping()), fontCnNormal, null, size, null));
					ta.addCell(setCell(getStr(area.getRemark()), fontCnNormal, null, size, null));
				}
			}
		}
	}
	
	@Override
	public int getCol() {
		return 8;
	}

	@Override
	public float[] getColWidth() {
		return new float[]{0.05f, 0.1f, 0.15f, 0.15f, 0.15f, 0.15f, 0.15f, 0.1f};
	}

	@Override
	public String getReportName() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}
	
	@Override
	public String getTableAbbreviation() {
		return null;
	}

	@Override
	public Table getTableTop() {
		return null;
	}
	
	@Override
	public Table getTableBottom() {
		return null;
	}
}
