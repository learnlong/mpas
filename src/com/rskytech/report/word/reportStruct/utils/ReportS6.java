package com.rskytech.report.word.reportStruct.utils;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.report.word.reportStruct.dao.IReportStructDao;
import com.rskytech.struct.bo.IS6Bo;

public class ReportS6 extends StructReportBase{

	private IS6Bo s6Bo;
	private SMain sMain;
	private String ssiCode;
	private String ssiName;
	private ComModelSeries ms;
	private String areaCode;
	private IReportStructDao reportStructDao;
	
	/**
	 * 创建环境损伤和偶然损伤检查要求确定
	 * @param document
	 * @param ms
	 * @param ssiCode
	 * @param ssiName
	 * @param areaCode
	 * @param sMain
	 * @param reportStructDao
	 * @param s6Bo
	 */
	public ReportS6(Document document, ComModelSeries ms, String ssiCode, String ssiName, String areaCode,
			SMain sMain, IReportStructDao reportStructDao,IS6Bo s6Bo) {
		super(document);
		this.ssiCode=ssiCode;
		this.ssiName=ssiName;
		this.ms=ms;
		this.areaCode=areaCode;
		this.sMain=sMain;
		this.reportStructDao=reportStructDao;
		this.s6Bo=s6Bo;
	}

	@Override
	public String getReportName() {
		return getStr(ms.getModelSeriesName());
	}

	@Override
	public int getCol() {
		return 1;
	}

	@Override
	public float[] getColWidth() {
		return null;
	}

	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(getCol(), getColWidth());
		ta.insertTable(getTableTitle());
		ta.insertTable(getTableCenter());
		return ta;
	}
	
	@SuppressWarnings({ "unchecked"})
	private Table getTableCenter() throws Exception {
		float[] f = new float[15];
		for(int i=0;i<15;i++){
			if(i==2||i==3){
				f[i] = 0.05f;
			}else{
				f[i]= 0.9f/13;
			}
		}
		Table tac = setTableAndColumn(15, f);
		tac.addCell(setCell("项目标识码", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
		tac.addCell(setCell("项目名称", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
		tac.addCell(setCell("内部或外部项目", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 2, 1));
		tac.addCell(setCell("SSI的材料", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
		tac.addCell(setCell("一般环境损伤级号GDR", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
		tac.addCell(setCell("应力腐蚀级号SCR", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
		tac.addCell(setCell("环境损伤级号EDR", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
		tac.addCell(setCell("偶然损伤级号ADR", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
		tac.addCell(setCell("EDR和ADR较小的级别", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
		tac.addCell(setCell("维修任务", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 3, 1));
		tac.addCell(setCell("检查周期", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
		tac.addCell(setCell("备注", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
		tac.addCell(setCell("内部", fontCnNormal));
		tac.addCell(setCell("外部", fontCnNormal));
		tac.addCell(setCell("编号", fontCnNormal));
		tac.addCell(setCell("种类", fontCnNormal));
		tac.addCell(setCell("说明", fontCnNormal));
		List<Object[]> s6eaList = s6Bo.getS6EaRecords(sMain.getSsiId(), "inAndOut");
		S1 s1;
		if(s6eaList!=null&&s6eaList.size()>0){
			double adrFalse = 0 ;
			double edrFalse = 0 ;
			double miniValue = 0;
			List<TaskMsg> taskMsgList;
			String inOrOut="";
			for(Object[] obj : s6eaList){
				int col = 1;
				s1 = (S1) s6Bo.loadById(S1.class,getStr(obj[10]));
				if(s1!=null){
					if(s1.getInternal()==1&&s1.getOuternal()==0){
						inOrOut="in";
						taskMsgList = this.reportStructDao.getTaskByS1Id(s1.getS1Id(), "S6", ms.getModelSeriesId(), "0");
						tac.addCell(setCell(ssiCode, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
						tac.addCell(setCell(ssiName, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
						tac.addCell(setCell("√", fontEnTitle,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
						tac.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
						tac.addCell(setCell(getStr(s1.getSsiForm()), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
						getEdrAndAdr(tac, obj, edrFalse, adrFalse, miniValue, taskMsgList,col,inOrOut);
					}else if(s1.getInternal()==0&&s1.getOuternal()==1){
						inOrOut="out";
						taskMsgList = this.reportStructDao.getTaskByS1Id(s1.getS1Id(), "S6", ms.getModelSeriesId(), "1");
						tac.addCell(setCell(ssiCode, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
						tac.addCell(setCell(ssiName, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
						tac.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
						tac.addCell(setCell("√", fontEnTitle,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
						tac.addCell(setCell(getStr(s1.getSsiForm()), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
						getEdrAndAdr(tac, obj, edrFalse, adrFalse, miniValue, taskMsgList,col,inOrOut);
					}else if(s1.getInternal()==1&&s1.getOuternal()==1){
						if(obj[14]!=null&&obj[14].toString().trim().equals("IN")){
							inOrOut="in";
							taskMsgList = this.reportStructDao.getTaskByS1Id(s1.getS1Id(), "S6", ms.getModelSeriesId(), "0");
							tac.addCell(setCell(ssiCode, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
							tac.addCell(setCell(ssiName, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
							tac.addCell(setCell("√", fontEnTitle,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
							tac.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
							tac.addCell(setCell(getStr(s1.getSsiForm()), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
							getEdrAndAdr(tac, obj, edrFalse, adrFalse, miniValue, taskMsgList,col,inOrOut);
						}else if(obj[14]!=null&&obj[14].toString().trim().equals("OUT")){
							inOrOut="out";
							taskMsgList = this.reportStructDao.getTaskByS1Id(s1.getS1Id(), "S6", ms.getModelSeriesId(), "1");
							tac.addCell(setCell(ssiCode, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
							tac.addCell(setCell(ssiName, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
							tac.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
							tac.addCell(setCell("√", fontEnTitle,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
							tac.addCell(setCell(getStr(s1.getSsiForm()), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
							getEdrAndAdr(tac, obj, edrFalse, adrFalse, miniValue, taskMsgList,col,inOrOut);
						}
					}
				}
			}
		}else{
			for(int m=0;m<30;m++){
				tac.addCell(setCell("", fontCnNormal));
			}
		}
		
		return tac;
	}

	public void getEdrAndAdr(Table tac,Object[] obj,double edrFalse,double adrFalse,
			double miniValue,List<TaskMsg> taskMsgList, int col, String inOrOut) throws Exception{
		if ("0".equals(obj[11].toString())) {
			if (obj[2] == null) {
				tac.addCell(setCell(getStr(s6Bo.getS4Records(
						obj[10].toString(),inOrOut).get(0).getGdr()), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				tac.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				tac.addCell(setCell(getStr(s6Bo.getS4Records(
						obj[10].toString(),inOrOut).get(0).getGdr()), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				edrFalse =  Double.parseDouble(getStr(s6Bo.getS4Records(
						obj[10].toString(),inOrOut).get(0).getGdr()));
			} else {
				tac.addCell(setCell(getStr(obj[2]), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				tac.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				tac.addCell(setCell(getStr(obj[2]), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				edrFalse =  Double.parseDouble(getStr(obj[2]));
			}
			if (obj[3] == null) {
				tac.addCell(setCell(getStr(s6Bo.getS5Records(
						obj[10].toString(),inOrOut).get(0).getAdr()), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				adrFalse =  Double.parseDouble(getStr(s6Bo.getS5Records(
						obj[10].toString(),inOrOut).get(0).getAdr()));
			} else {
				tac.addCell(setCell(getStr(obj[3]), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				adrFalse =  Double.parseDouble(getStr(obj[3]));
			}
			if(adrFalse>=edrFalse){
				miniValue = edrFalse;
			}else{
				miniValue = adrFalse;
			}
			tac.addCell(setCell(getStr(miniValue), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
			//任务
			if(taskMsgList!=null&&taskMsgList.size()>0){
				for(TaskMsg task : taskMsgList){
					tac.addCell(setCell(getStr(task.getTaskCode()), fontCnNormal));
					tac.addCell(setCell(getStr(task.getTaskType()), fontCnNormal));
					tac.addCell(setCell(getStr(task.getTaskDesc()), fontCnNormal));
					tac.addCell(setCell(getStr(task.getTaskInterval()), fontCnNormal));
					tac.addCell(setCell(getStr(task.getRemark()), fontCnNormal));
				}
			}else{
				tac.addCell(setCell("", fontCnNormal));
				tac.addCell(setCell("", fontCnNormal));
				tac.addCell(setCell("", fontCnNormal));
				tac.addCell(setCell("", fontCnNormal));
				tac.addCell(setCell("", fontCnNormal));
			}
		} else if("1".equals(obj[11].toString())){
			if (obj[2] == null) {
				tac.addCell(setCell(getStr(s6Bo.getS4Records(
						obj[10].toString(),inOrOut).get(0).getGdr()), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				tac.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				tac.addCell(setCell(getStr(s6Bo.getS4Records(
						obj[10].toString(),inOrOut).get(0).getGdr()), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				edrFalse =  Double.parseDouble(getStr(s6Bo.getS4Records(
						obj[10].toString(),inOrOut).get(0).getGdr()));
			} else {
				tac.addCell(setCell(getStr(obj[2]), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				tac.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				tac.addCell(setCell(getStr(obj[2]), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				edrFalse =  Double.parseDouble(getStr(obj[2]));
			}
			if (obj[3] == null) {
				tac.addCell(setCell(getStr(s6Bo.getS5Records(
						obj[10].toString(),inOrOut).get(0).getAdr()), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				adrFalse =  Double.parseDouble(getStr(s6Bo.getS5Records(
						obj[10].toString(),inOrOut).get(0).getAdr()));
			} else {
				tac.addCell(setCell(getStr(obj[3]), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				adrFalse =  Double.parseDouble(getStr(obj[3]));
			}
			if(adrFalse>=edrFalse){
				miniValue = edrFalse;
			}else{
				miniValue = adrFalse;
			}
			tac.addCell(setCell(getStr(miniValue), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
			//任务
			if(taskMsgList!=null&&taskMsgList.size()>0){
				for(TaskMsg task : taskMsgList){
					tac.addCell(setCell(getStr(task.getTaskCode()), fontCnNormal));
					tac.addCell(setCell(getStr(task.getTaskType()), fontCnNormal));
					tac.addCell(setCell(getStr(task.getTaskDesc()), fontCnNormal));
					tac.addCell(setCell(getStr(task.getTaskInterval()), fontCnNormal));
					tac.addCell(setCell(getStr(task.getRemark()), fontCnNormal));
				}
			}else{
				tac.addCell(setCell("", fontCnNormal));
				tac.addCell(setCell("", fontCnNormal));
				tac.addCell(setCell("", fontCnNormal));
				tac.addCell(setCell("", fontCnNormal));
				tac.addCell(setCell("", fontCnNormal));
			}
		}
	}
	
	public Table getTableTitle() throws Exception{
		Table taTitle = setTableAndColumn(8, new float[]{0.05f,0.15f,0.05f,0.25f,0.05f,0.15f,0.1f,0.2f});
		taTitle.addCell(setCell("2.2.6    环境损伤和偶然损伤检查要求确定", fontCnTitle,Element.ALIGN_LEFT, Element.ALIGN_CENTER, 8, 1, 0, null));
		taTitle.addCell(setCell("               重要结构项目环境损伤和偶然损伤检查要求确定见表8。", fontCnNormal,Element.ALIGN_LEFT, Element.ALIGN_CENTER, 8, 1, 0, null));
		taTitle.addCell(setCell("表8    重要结构项目环境损伤和偶然损伤检查要求确定表", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 8, 1, 0, null));
		taTitle.addCell(setCell("飞机型号："+getReportName()+"                                          专业：起落架                      " +
				"                                    共  页，第  页", fontCnNormal,Element.ALIGN_LEFT, Element.ALIGN_CENTER, 8, 1, 0, null));
		taTitle.addCell(setCell("部件名称", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1));
		taTitle.addCell(setCell(ssiName, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1));
		taTitle.addCell(setCell("图号", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1));
		taTitle.addCell(setCell(getStr(sMain.getComAta()==null?"":sMain.getComAta().getEquipmentPicNo()), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1));
		taTitle.addCell(setCell("标识码", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1));
		taTitle.addCell(setCell(ssiCode, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1));
		taTitle.addCell(setCell("区域号", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1));
		taTitle.addCell(setCell(areaCode, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1));
		return taTitle;
	}
}
