package com.rskytech.report.word.reportStruct.utils;

import java.util.List;
import java.util.Set;

import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.S3;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.report.word.reportStruct.dao.IReportStructDao;

public class ReportS3 extends StructReportBase{
	private String ssiCode;
	private String ssiName;
	private ComModelSeries ms;
	private String areaCode;
	private SMain sMain;
	private IReportStructDao reportStructDao;
	
	public ReportS3(Document document, ComModelSeries ms, String ssiCode, String ssiName, String areaCode,
			SMain sMain, IReportStructDao reportStructDao) {
		super(document);
		this.ssiCode=ssiCode;
		this.ssiName=ssiName;
		this.ms=ms;
		this.areaCode=areaCode;
		this.sMain=sMain;
		this.reportStructDao=reportStructDao;
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
	
	@SuppressWarnings("unchecked")
	private Table getTableCenter() throws Exception {
		float[] f = new float[18];
		for(int i=0;i<18;i++){
			f[i]= 1.0f/18;
		}
		Table tac = setTableAndColumn(18, f);
		tac.addCell(setCell("项目标识码", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
		tac.addCell(setCell("项目名称", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
		tac.addCell(setCell("SSI类别", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 2, 1));
		tac.addCell(setCell("内部或外部项目", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 2, 1));
		tac.addCell(setCell("SSI的平均裂纹形成寿命 N"+chunkStr("i",fontEnSmall,-15f), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
		tac.addCell(setCell("飞机设计使用寿命L", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
		tac.addCell(setCell("n=N"+chunkStr("i",fontEnSmall,-6f)+"/L", fontEnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
		tac.addCell(setCell("疲劳损伤级号FDR", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
		tac.addCell(setCell("SSI的裂纹扩展寿命 N"+chunkStr("p",fontEnSmall,-6f), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
		tac.addCell(setCell("维修任务", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 3, 1));
		tac.addCell(setCell("首检期", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
		tac.addCell(setCell("检查周期", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
		tac.addCell(setCell("安全寿命", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
		tac.addCell(setCell("备注", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
		
		tac.addCell(setCell("安全寿命", fontCnNormal));
		tac.addCell(setCell("损伤容限或耐久性", fontCnNormal));
		tac.addCell(setCell("内部", fontCnNormal));
		tac.addCell(setCell("外部", fontCnNormal));
		tac.addCell(setCell("编号", fontCnNormal));
		tac.addCell(setCell("种类", fontCnNormal));
		tac.addCell(setCell("说明", fontCnNormal));
		
		Set<S3> setS3 = sMain.getS3s();
		List<TaskMsg> taskMsgList;
		int col=1;
		if(setS3!=null&&setS3.size()>0){
			for(S3 s3 : setS3){
				taskMsgList=reportStructDao.getTaskByS1Id(s3.getS1().getS1Id(),"S3",ms.getModelSeriesId(),null);
				if(taskMsgList!=null&&taskMsgList.size()>0){
					col = taskMsgList.size();
				}
				tac.addCell(setCell(ssiCode, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				tac.addCell(setCell(ssiName, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				tac.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				tac.addCell(setCell("√", fontEnTitle,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				if(s3.getIntOut()==0){
					tac.addCell(setCell("√", fontEnTitle,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
					tac.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				}else if(s3.getIntOut()==1){
					tac.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
					tac.addCell(setCell("√", fontEnTitle,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				}else if(s3.getIntOut()==2){
					tac.addCell(setCell("√", fontEnTitle,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
					tac.addCell(setCell("√", fontEnTitle,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				}
				tac.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				tac.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				tac.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				tac.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				tac.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER,1,col));
				//任务
				if(taskMsgList!=null&&taskMsgList.size()>0){
					for(TaskMsg taskMsg : taskMsgList){
						tac.addCell(setCell(getStr(taskMsg.getTaskCode()), fontCnNormal));
						tac.addCell(setCell(getStr(taskMsg.getTaskType()), fontCnNormal));
						tac.addCell(setCell(getStr(taskMsg.getTaskDesc()), fontCnNormal));
						tac.addCell(setCell(getStr(taskMsg.getTaskInterval()), fontCnNormal));
						tac.addCell(setCell(getStr(taskMsg.getTaskInterval()), fontCnNormal));
						tac.addCell(setCell("", fontCnNormal));
						tac.addCell(setCell(getStr(taskMsg.getRemark()), fontCnNormal));
					}
				}else{
					tac.addCell(setCell("", fontCnNormal));
					tac.addCell(setCell("", fontCnNormal));
					tac.addCell(setCell("", fontCnNormal));
					tac.addCell(setCell("", fontCnNormal));
					tac.addCell(setCell("", fontCnNormal));
					tac.addCell(setCell("", fontCnNormal));
					tac.addCell(setCell("", fontCnNormal));
				}
			}
		}else{
			for(int m=0;m<36;m++){
				tac.addCell(setCell("", fontCnNormal));
			}
		}
		return tac;
	}
	
	
	public Table getTableTitle() throws Exception{
		Table taTitle = setTableAndColumn(8, new float[]{0.05f,0.15f,0.05f,0.25f,0.05f,0.15f,0.1f,0.2f});
		taTitle.addCell(setCell("2.2.4    疲劳损伤检查", fontCnTitle,Element.ALIGN_LEFT, Element.ALIGN_CENTER, 8, 1,0, null));
		taTitle.addCell(setCell("2.2.5    重要结构项目疲劳损伤检查要求见表7。", fontCnTitle,Element.ALIGN_LEFT, Element.ALIGN_CENTER, 8, 1, 0, null));
		taTitle.addCell(setCell("表7   重要结构项目疲劳损伤检查要求确定表", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 8, 1, 0, null));
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
