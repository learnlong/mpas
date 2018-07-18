package com.rskytech.report.word.reportStruct.utils;

import java.util.List;

import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.struct.dao.IS8Dao;
import com.rskytech.struct.dao.IUnSsiDao;

public class ReportS7 extends StructReportBase{
	private ComModelSeries ms;
	private List<SMain> list;
	private IS8Dao s8Dao;
	private IComAreaBo comAreaBo;
	private int isSsi;
	private IUnSsiDao unSsiDao;
	
	/**
	 *  结构预防性维修大纲汇总表
	 * @param document
	 * @param ms
	 * @param list
	 * @param s7Dao
	 * @param comAreaBo
	 * @param isSsi
	 */
	public ReportS7(Document document,ComModelSeries ms, List<SMain> list,IS8Dao s8Dao, 
			IComAreaBo comAreaBo, int isSsi,IUnSsiDao unSsiDao) {
		super(document);
		this.ms=ms;
		this.list=list;
		this.s8Dao=s8Dao;
		this.comAreaBo=comAreaBo;
		this.isSsi=isSsi;
		this.unSsiDao=unSsiDao;
	}
	@Override
	public String getReportName() {
		return getStr(ms.getModelSeriesName());
	}
	@Override
	public int getCol() {
		return 10;
	}
	@Override
	public float[] getColWidth() {
		float[] f = new float[10];
		for(int i=0;i<10;i++){
			f[i]= 0.1f;
		}
		return f;
	}
	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(getCol(), getColWidth());
		if(isSsi==0){
			ta.addCell(setCell("2.2.7    结构预防性维修大纲汇总表", fontCnTitle,Element.ALIGN_LEFT, Element.ALIGN_CENTER, 10, 1, 0, null));
			ta.addCell(setCell("                结构预防性维修大纲汇总表见表9。", fontCnNormal,Element.ALIGN_LEFT, Element.ALIGN_CENTER, 10, 1, 0, null));
			ta.addCell(setCell("表9     "+getReportName()+"结构预防性维修工作汇总表", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 10, 1, 0, null));
			ta.addCell(setCell("专业：结构                      " , fontCnNormal,Element.ALIGN_LEFT, Element.ALIGN_CENTER, 10, 1, 0, null));
		}else{
			ta.addCell(setCell("    结构预防性维修大纲汇总表", fontCnTitle,Element.ALIGN_LEFT, Element.ALIGN_CENTER, 10, 1, 0, null));
			ta.addCell(setCell("                结构预防性维修大纲汇总表见下表。", fontCnNormal,Element.ALIGN_LEFT, Element.ALIGN_CENTER, 10, 1, 0, null));
			ta.addCell(setCell(getReportName()+"结构预防性维修工作汇总表", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 10, 1, 0, null));
			ta.addCell(setCell("专业：结构                      " , fontCnNormal,Element.ALIGN_LEFT, Element.ALIGN_CENTER, 10, 1, 0, null));
		}
		ta.addCell(setCell("SSI/NSSI 标识码", fontCnNormal));
		ta.addCell(setCell("SSI/NSSI 名称", fontCnNormal));
		ta.addCell(setCell("区域", fontCnNormal));
		ta.addCell(setCell("维修任务编号", fontCnNormal));
		ta.addCell(setCell("维修工作类型", fontCnNormal));
		ta.addCell(setCell("维修工作描述", fontCnNormal));
		ta.addCell(setCell("首检期", fontCnNormal));
		ta.addCell(setCell("检查间隔期", fontCnNormal));
		ta.addCell(setCell("维修通道", fontCnNormal));
		ta.addCell(setCell("备注", fontCnNormal));
		if(list!=null&&list.size()>0){
			List<TaskMsg> taskList;
			for(SMain sMain : list){
				if(sMain.getIsSsi()==1){
					taskList = s8Dao.getS8Records(sMain.getSsiId(), "S8");
					int row =1;
					if(taskList!=null&&taskList.size()>0){
						row = taskList.size();
						if(sMain.getComAta()!=null){
							ta.addCell(setCell(sMain.getComAta().getAtaCode(),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, row, null, null));
							ta.addCell(setCell(getStr(sMain.getComAta().getAtaName()),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, row, null, null));
						}else{
							ta.addCell(setCell(sMain.getAddCode(),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, row, null, null));
							ta.addCell(setCell(getStr(sMain.getAddName()),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER,1, row, null, null));
						}
						for(TaskMsg taskMsg: taskList){
							ta.addCell(setCell(getStr(comAreaBo.getAreaCodeByAreaId(taskMsg.getOwnArea())), fontCnNormal));
							ta.addCell(setCell(getStr(taskMsg.getTaskCode()), fontCnNormal));
							ta.addCell(setCell(getStr(taskMsg.getTaskType()), fontCnNormal));
							ta.addCell(setCell(getStr(taskMsg.getTaskDesc()), fontCnNormal));
							ta.addCell(setCell(getStr(""), fontCnNormal));
							ta.addCell(setCell(getStr(taskMsg.getTaskInterval()), fontCnNormal));
							ta.addCell(setCell(getStr(""), fontCnNormal));
							ta.addCell(setCell(getStr(taskMsg.getRemark()), fontCnNormal));
						}
					}else{
						if(sMain.getComAta()!=null){
							ta.addCell(setCell(sMain.getComAta().getAtaCode(),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
							ta.addCell(setCell(getStr(sMain.getComAta().getAtaName()),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
						}else{
							ta.addCell(setCell(sMain.getAddCode(),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
							ta.addCell(setCell(getStr(sMain.getAddName()),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER,1, 1, null, null));
						}
						for(int i=0;i<8;i++){
							ta.addCell(setCell("", fontCnNormal));
						}
					}
				}else if(sMain.getIsSsi()==0&&sMain.getIsAna()!=null&&sMain.getIsAna()==1){
					taskList = unSsiDao.searchUnSsiList(sMain.getSsiId(), ms.getModelSeriesId());
					int row =1;
					if(taskList!=null&&taskList.size()>0){
						row = taskList.size();
						if(sMain.getComAta()!=null){
							ta.addCell(setCell("Q"+sMain.getComAta().getAtaCode(),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, row, null, null));
							ta.addCell(setCell(getStr(sMain.getComAta().getAtaName()),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, row, null, null));
						}else{
							ta.addCell(setCell("Q"+sMain.getAddCode(),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, row, null, null));
							ta.addCell(setCell(getStr(sMain.getAddName()),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, row, null, null));
						}
						for(TaskMsg taskMsg: taskList){
							ta.addCell(setCell(getStr(comAreaBo.getAreaCodeByAreaId(taskMsg.getOwnArea())), fontCnNormal));
							ta.addCell(setCell(getStr(taskMsg.getTaskCode()), fontCnNormal));
							ta.addCell(setCell(getStr(taskMsg.getTaskType()), fontCnNormal));
							ta.addCell(setCell(getStr(taskMsg.getTaskDesc()), fontCnNormal));
							ta.addCell(setCell(getStr(""), fontCnNormal));
							ta.addCell(setCell(getStr(taskMsg.getTaskInterval()), fontCnNormal));
							ta.addCell(setCell(getStr(""), fontCnNormal));
							ta.addCell(setCell(getStr(taskMsg.getRemark()), fontCnNormal));
						}
					}else{
						if(sMain.getComAta()!=null){
							ta.addCell(setCell("Q"+sMain.getComAta().getAtaCode(),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
							ta.addCell(setCell(getStr(sMain.getComAta().getAtaName()),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
						}else{
							ta.addCell(setCell("Q"+sMain.getAddCode(),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
							ta.addCell(setCell(getStr(sMain.getAddName()),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
						}
						for(int i=0;i<8;i++){
							ta.addCell(setCell("", fontCnNormal));
						}
					}
				}
			}
		}else{
			for(int i=0;i<10;i++){
				ta.addCell(setCell("", fontCnNormal));
			}
		}
		return ta;
	}
}
