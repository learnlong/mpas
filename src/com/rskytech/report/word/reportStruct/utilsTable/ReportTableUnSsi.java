package com.rskytech.report.word.reportStruct.utilsTable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.struct.dao.IUnSsiDao;

public class ReportTableUnSsi extends StructReportBase{

	private IComAreaBo comAreaBo;
	private List<SMain> sMainList;
	private ComModelSeries ms;
	private IUnSsiDao unSsiDao;
	private String ssiCode;
	private String ssiName;
	
	public ReportTableUnSsi(Document document, String ssiCode, String ssiName, List<SMain> sMainList, ComModelSeries ms, 
			IComAreaBo comAreaBo, IUnSsiDao unSsiDao) {
		super(document);
		this.ssiCode=ssiCode;
		this.ssiName=ssiName;
		this.ms=ms;
		this.sMainList=sMainList;
		this.comAreaBo=comAreaBo;
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
		return new float[]{0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f};
	}

	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(getCol(), getColWidth());
		ta.addCell(setCell("表13   非重要结构项目的检查要求", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 10, 1, 0, null));
		ta.addCell(setCell("飞机型号："+getReportName()+"                                          专业：                         " +
				"                                    共  页，第  页", fontCnNormal,Element.ALIGN_LEFT, Element.ALIGN_CENTER, 10, 1, 0, null));
		
		ta.addCell(setCell("部件名称", fontCnNormal));
		ta.addCell(setCell(ssiName, fontCnNormal,Element.ALIGN_LEFT, Element.ALIGN_CENTER, 3, 1));
		ta.addCell(setCell("图号", fontCnNormal));
		ta.addCell(setCell("", fontCnNormal));
		ta.addCell(setCell("标识码", fontCnNormal));
		ta.addCell(setCell(ssiCode,fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1, 1));
		ta.addCell(setCell("区域号", fontCnNormal));
		ta.addCell(setCell("", fontCnNormal));
		
		ta.addCell(setCell("项目标识码", fontCnNormal));
		ta.addCell(setCell("项目名称", fontCnNormal));
		ta.addCell(setCell("维修通道", fontCnNormal));
		ta.addCell(setCell("根据外场经验确定非重要结构项目检查任务说明", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 2, 1));
		ta.addCell(setCell("维修任务编号", fontCnNormal));
		ta.addCell(setCell("维修任务种类", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 2, 1));
		ta.addCell(setCell("检查周期", fontCnNormal));
		ta.addCell(setCell("备注", fontCnNormal));
		
		if (sMainList != null){
			for (SMain sMain : sMainList){
				System.out.println(sMain.getComAta().getAtaCode());
				if (sMain.getIsSsi() == 0 && sMain.getIsAna() == 1){
					List<TaskMsg> taskList = unSsiDao.searchUnSsiList(sMain.getSsiId(), ms.getModelSeriesId());
					for(TaskMsg taskMsg: taskList){
						if(sMain.getComAta()!=null){
							ta.addCell(setCell("Q"+sMain.getComAta().getAtaCode(),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
							ta.addCell(setCell(getStr(sMain.getComAta().getAtaName()),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
						}else{
							ta.addCell(setCell("Q"+sMain.getAddCode(),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
							ta.addCell(setCell(getStr(sMain.getAddName()),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
						}
						ta.addCell(setCell("", fontCnNormal));
						ta.addCell(setCell(getStr(taskMsg.getTaskDesc()), fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 2, 1));
						ta.addCell(setCell(getStr(taskMsg.getTaskCode()), fontCnNormal));
						ta.addCell(setCell(getStr(taskMsg.getTaskType()), fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 2, 1));
						ta.addCell(setCell(getStr(taskMsg.getTaskInterval()), fontCnNormal));
						ta.addCell(setCell(getStr(taskMsg.getRemark()), fontCnNormal));
					}
				}
			}
		}
		
		return ta;
	}
	
	
	public String getAreaCode(SMain sMain,List<TaskMsg> taskList){
		String areaId="";
		Set<String> areaIdSet = new HashSet<String>();
		String areaCode ="";
		for(TaskMsg taskMsg: taskList){
			if(taskMsg.getOwnArea()!=null){
				String[] areaIds= taskMsg.getOwnArea().split(",");
				for(String str : areaIds){
					areaIdSet.add(str);
				}
			}
		}
		if(areaIdSet.size()>0){
			Iterator<String> it1 = areaIdSet.iterator();
			while(it1.hasNext()){
				areaId += it1.next()+",";
			}
			areaId = areaId.substring(0, areaId.length()-1);
			areaCode = comAreaBo.getAreaCodeByAreaId(areaId);
			
		}
		return areaCode;
	}
}
