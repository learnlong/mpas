package com.rskytech.report.word.reportArea.utils;

import java.util.List;

import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.rskytech.area.dao.IZa8Dao;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.TaskMsgDetail;

public class ReportZa8 extends AreaReportBase {

	private List<TaskMsg> za8List;
	private IZa8Dao za8Dao;
	
	public ReportZa8(Document document, ComModelSeries ms, ComArea area, List<TaskMsg> za8List, IZa8Dao za8Dao) {
		super(document, ms, area);
		this.za8List = za8List;
		this.za8Dao = za8Dao;
	}

	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(getCol(), getColWidth());
		
		ta.addCell(setCell("任务号", fontCnNormal));
		ta.addCell(setCell("任务描述", fontCnNormal));
		ta.addCell(setCell("区域", fontCnNormal));
		ta.addCell(setCell("口盖/面板", fontCnNormal));
		ta.addCell(setCell("间隔", fontCnNormal));
		ta.addCell(setCell("系统/结构任务参考号", fontCnNormal));
		ta.addCell(setCell("适用性", fontCnNormal));
		
		for (TaskMsg tm : za8List){
			ta.addCell(setCell(getStr(tm.getTaskCode()), fontCnNormal));
			ta.addCell(setCell(getStr(tm.getTaskDesc()), fontCnNormal));
			ta.addCell(setCell(getStr(area.getAreaCode()), fontCnNormal));
			ta.addCell(setCell(getStr(tm.getReachWay()), fontCnNormal));
			ta.addCell(setCell(getStr(tm.getTaskIntervalMerge() == null ? tm.getTaskInterval() : tm.getTaskIntervalMerge()), fontCnNormal));
			
			List<TaskMsgDetail> taskList = this.za8Dao.getTaskMsgDetailList(tm.getTaskId());// 查询被该任务合并的所有任务
			String code = "";
			for (TaskMsgDetail taskMsgDetail : taskList) {
				code += taskMsgDetail.getTaskMsg().getTaskCode() + "\n";
			}
			if (!"".equals(code)){
				code = code.substring(0, code.length() - 1);
			}
			
			ta.addCell(setCell(getStr(code), fontCnNormal));
			ta.addCell(setCell(getStr(tm.getEffectiveness()), fontCnNormal));
		}
		return ta;
	}
	
	@Override
	public int getCol() {
		return 7;
	}

	@Override
	public float[] getColWidth() {
		return new float[]{0.1f, 0.25f, 0.1f, 0.25f, 0.1f, 0.1f, 0.1f};
	}

	@Override
	public String getReportName() {
		return "表8  " + area.getAreaCode() + "区域检查大纲表格";
	}

	@Override
	public String getTableAbbreviation() {
		return "ZA-6";
	}

	@Override
	public String getTableName() {
		return "区域分析-区域检查大纲表格";
	}

}
