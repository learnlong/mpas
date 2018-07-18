package com.rskytech.report.word.reportSystem.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;

import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.M3;
import com.rskytech.pojo.M3Additional;
import com.rskytech.pojo.MMain;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.sys.bo.IM3Bo;

public class ReportM3 extends SystemReportBase{
	private ComModelSeries ms;
	private M3 m3;
	private String code;
	private String failureCauseType;
	private MMain mMain;
	private IM3Bo m3Bo;
	private IComAreaBo comAreaBo;
	
	public ReportM3(Document document, ComModelSeries ms, M3 m3,String code,String failureCauseType, MMain mMain, IM3Bo m3Bo, IComAreaBo comAreaBo){
		super(document);
		this.ms=ms;
		this.m3=m3;
		this.code = code;
		this.failureCauseType=failureCauseType;
		this.mMain=mMain;
		this.m3Bo=m3Bo;
		this.comAreaBo=comAreaBo;
	}
	
	@Override
	public Table getTableContent() throws Exception {
		String failureCauseValue="";
		if(failureCauseType.equals("6")){
			failureCauseValue = "明显安全性";
		}else if(failureCauseType.equals("7")){
			failureCauseValue = "明显任务性";
		}else if(failureCauseType.equals("8")){
			failureCauseValue = "明显经济性";
		}else if(failureCauseType.equals("9")){
			failureCauseValue = "隐蔽安全性";
		}else if(failureCauseType.equals("10")){
			failureCauseValue = "隐蔽任务性";
		}else if(failureCauseType.equals("11")){
			failureCauseValue = "隐蔽经济性";
		}
		Table ta = setTableAndColumn(getCol(), getColWidth());
		ta.addCell(setCell(getReportName()+"预防性维修工作类型选择表（第二层次决断）", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 11,2, 0, null));
		ta.addCell(setCell("系统名称", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2,2, null, null));
		ta.addCell(setCell(getStr(mMain.getComAta().getAtaName()), fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 4,2, null, null));
		ta.addCell(setCell("系统图（代）号", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,2, null, null));
		ta.addCell(setCell(getStr(mMain.getComAta().getEquipmentPicNo()), fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 4,2, null, null));
		ta.addCell(setCell("产品标识码", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2,2, null, null));
		ta.addCell(setCell(getStr(mMain.getComAta().getAtaCode()), fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 4,2, null, null));
		ta.addCell(setCell("产品名称", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,2, null, null));
		ta.addCell(setCell(getStr(mMain.getComAta().getEquipmentName()), fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 4,2, null, null));
		
		ta.addCell(setCell("产品功能、故障模式、故障原因编码（来源于FMEA）", fontCnNormal,
				Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 6,2, null, null));
		ta.addCell(setCell(code, fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 5,2, null, null));
		
		ta.addCell(setCell("故障影响类别", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2,1, null, null));
		ta.addCell(setCell(failureCauseValue, fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 4,1, null, null));
		ta.addCell(setCell("故障影响类别编号", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell(failureCauseType, fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 4,1, null, null));
		
		ta.addCell(setCell("明显\n安全", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_BOTTOM, 1,2, null, null));
		ta.addCell(setCell("明显\n任务", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_BOTTOM, 1,2, null, null));
		ta.addCell(setCell("明显\n经济", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_BOTTOM, 1,2, null, null));
		ta.addCell(setCell("隐蔽\n安全", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_BOTTOM, 1,2, null, null));
		ta.addCell(setCell("隐蔽\n任务", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_BOTTOM, 1,2, null, null));
		ta.addCell(setCell("隐蔽\n经济", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_BOTTOM, 1,2, null, null));
		ta.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_BOTTOM, 5,2, null, null));
		
		
		Integer catagory = Integer.valueOf(failureCauseType);
		Color[] c = {null, null, null, null, null,null};
		if (catagory != null){
			c[catagory - 6] = bgGree;
		}
		ta.addCell(setCell("6",fontCnNormal,c[0]));
		ta.addCell(setCell("7",fontCnNormal,c[1]));
		ta.addCell(setCell("8",fontCnNormal,c[2]));
		ta.addCell(setCell("9",fontCnNormal,c[3]));
		ta.addCell(setCell("10",fontCnNormal,c[4]));
		ta.addCell(setCell("11",fontCnNormal,c[5]));
		ta.addCell(setCell("问题", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2,1, null, null));
		ta.addCell(setCell("是", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell("否", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell("所选维修工作类型", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		
		ta.addCell(setCell("A",fontCnNormal,c[0]));
		ta.addCell(setCell("A",fontCnNormal,c[1]));
		ta.addCell(setCell("A",fontCnNormal,c[2]));
		ta.addCell(setCell("A",fontCnNormal,c[3]));
		ta.addCell(setCell("A",fontCnNormal,c[4]));
		ta.addCell(setCell("A",fontCnNormal,c[5]));
		ta.addCell(setCell("保养是适用和有效的吗？", fontCnNormal,
				Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 2,1, null, null));
		String baoyang = "";
		String baoyang1 = "";
		if(m3.getBaoyang()==1){
			baoyang = "√";
		}else if(m3.getBaoyang()==0){
			baoyang1 = " ";
		}
		ta.addCell(setCell(baoyang, fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell(baoyang1, fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell(m3.getBaoyangDesc(), fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		
		ta.addCell(setCell("",fontCnNormal,c[0]));
		ta.addCell(setCell("",fontCnNormal,c[1]));
		ta.addCell(setCell("",fontCnNormal,c[2]));
		ta.addCell(setCell("B",fontCnNormal,c[3]));
		ta.addCell(setCell("B*",fontCnNormal,c[4]));
		ta.addCell(setCell("B*",fontCnNormal,c[5]));
		ta.addCell(setCell("使用状态的检验是适用和有效的吗？", fontCnNormal,
				Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 2,1, null, null));
		String jianyan = "";
		String jianyan1 = "";
		if(m3.getJianyan()==1){
			jianyan = "√";
		}else if(m3.getJianyan()==0){
			jianyan1 = " ";
		}
		ta.addCell(setCell(jianyan, fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell(jianyan1, fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell(m3.getJianyanDesc(), fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		
		ta.addCell(setCell("B",fontCnNormal,c[0]));
		ta.addCell(setCell("B*",fontCnNormal,c[1]));
		ta.addCell(setCell("B*",fontCnNormal,c[2]));
		ta.addCell(setCell("",fontCnNormal,c[3]));
		ta.addCell(setCell("",fontCnNormal,c[4]));
		ta.addCell(setCell("",fontCnNormal,c[5]));
		ta.addCell(setCell("用正常的操作人员监控来探测功能恶化是适用和有效的吗？", fontCnNormal,
				Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 2,1, null, null));
		String jiankong = "";
		String jiankong1 = "";
		if(m3.getJiankong()==1){
			jiankong = "√";
		}else if(m3.getJiankong()==0){
			jiankong = " ";
		}
		ta.addCell(setCell(jiankong, fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell(jiankong1, fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell(m3.getJiankongDesc(), fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		
		ta.addCell(setCell("C",fontCnNormal,c[0]));
		ta.addCell(setCell("C*",fontCnNormal,c[1]));
		ta.addCell(setCell("C*",fontCnNormal,c[2]));
		ta.addCell(setCell("C",fontCnNormal,c[3]));
		ta.addCell(setCell("C*",fontCnNormal,c[4]));
		ta.addCell(setCell("C*",fontCnNormal,c[5]));
		ta.addCell(setCell("用原位或离位检查来探测功能恶化是适用和有效的吗？", fontCnNormal,
				Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 2,1, null, null));
		String jiancha = "";
		String jiancha1 = "";
		if(m3.getJiancha()==1){
			jiancha = "√";
		}else if(m3.getJiancha()==0){
			jiancha1 = " ";
		}
		ta.addCell(setCell(jiancha, fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell(jiancha1, fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell(m3.getJianchaDesc(), fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		
		ta.addCell(setCell("D",fontCnNormal,c[0]));
		ta.addCell(setCell("D*",fontCnNormal,c[1]));
		ta.addCell(setCell("D*",fontCnNormal,c[2]));
		ta.addCell(setCell("D",fontCnNormal,c[3]));
		ta.addCell(setCell("D*",fontCnNormal,c[4]));
		ta.addCell(setCell("D*",fontCnNormal,c[5]));
		ta.addCell(setCell("定时拆修是适用和有效的吗？", fontCnNormal,
				Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 2,1, null, null));
		String chaixiu = "";
		String chaixiu1 = "";
		if(m3.getChaixiu()==1){
			chaixiu = "√";
		}else if(m3.getChaixiu()==0){
			chaixiu1 = " ";
		}
		ta.addCell(setCell(chaixiu, fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell(chaixiu1, fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell(m3.getChaixiuDesc(), fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		
		ta.addCell(setCell("E",fontCnNormal,c[0]));
		ta.addCell(setCell("E*",fontCnNormal,c[1]));
		ta.addCell(setCell("E*",fontCnNormal,c[2]));
		ta.addCell(setCell("E",fontCnNormal,c[3]));
		ta.addCell(setCell("E*",fontCnNormal,c[4]));
		ta.addCell(setCell("E*",fontCnNormal,c[5]));
		ta.addCell(setCell("定时报废是适用和有效的吗？", fontCnNormal,
				Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 2,1, null, null));
		String baofei = "";
		String baofei1 = "";
		if(m3.getBaofei()==1){
			baofei = "√";
		}else if(m3.getBaofei()==0){
			baofei1 = " ";
		}
		ta.addCell(setCell(baofei, fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell(baofei1, fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell(m3.getBaofeiDesc(), fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		
		ta.addCell(setCell("F",fontCnNormal,c[0]));
		ta.addCell(setCell("F",fontCnNormal,c[1]));
		ta.addCell(setCell("",fontCnNormal,c[2]));
		ta.addCell(setCell("F",fontCnNormal,c[3]));
		ta.addCell(setCell("F",fontCnNormal,c[4]));
		ta.addCell(setCell("",fontCnNormal,c[5]));
		ta.addCell(setCell("有一种工作或综合工作是适用和有效的吗？", fontCnNormal,
				Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 2,1, null, null));
		String zonghe = "";
		String zonghe1 = "";
		if(m3.getZonghe()==1){
			zonghe = "√";
		}else if(m3.getZonghe()==0){
			zonghe1 = "";
		}
		ta.addCell(setCell(zonghe, fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell(zonghe1, fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell(m3.getZongheDesc(), fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		String gaijin ="";
		if(m3.getGaijin()==1){
			gaijin = "是     [√]                      		     否    [ ]";
		}else{
			gaijin = "是     [ ]                      		     否    [√]";
		}
		ta.addCell(setCell("是否需要改进设计？                    "+gaijin, fontCnNormal,
				Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 11,1, null, null));
		Table ta1 =setTableAndColumn(1, null); 
		ta1.insertTable(ta);
		List<TaskMsg> listmsg = searchTask();
		if(listmsg!=null&&listmsg.size()>0){
			ta1.insertTable(downTable(searchTask()));
		}
		return ta1;
	}
	
	
	private Table downTable(List<TaskMsg> list) throws Exception{
		Table ta =setTableAndColumn(8, null);
		ta.addCell(setCell("维修任务", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 8,1, 0, Rectangle.LEFT|Rectangle.RIGHT));
		ta.addCell(setCell(getStr("问题"), fontCnNormal));
		ta.addCell(setCell(getStr("任务编号"), fontCnNormal));
		ta.addCell(setCell(getStr("任务类型"), fontCnNormal));
		ta.addCell(setCell(getStr("任务描述"), fontCnNormal));
		ta.addCell(setCell(getStr("接近方式"), fontCnNormal));
		ta.addCell(setCell(getStr("故障影响类别"), fontCnNormal));
		ta.addCell(setCell(getStr("适用性"), fontCnNormal));
		ta.addCell(setCell(getStr("区域"), fontCnNormal));
		for(TaskMsg task : list){
			ta.addCell(setCell(getStr(getQuestionName(task.getAnyContent4())), fontCnNormal));
			ta.addCell(setCell(getStr(task.getTaskCode()), fontCnNormal));
			ta.addCell(setCell(getStr(task.getTaskType()), fontCnNormal));
			ta.addCell(setCell(getStr(task.getTaskDesc()), fontCnNormal));
			ta.addCell(setCell(getStr(task.getReachWay()), fontCnNormal));
			ta.addCell(setCell(getStr(failureCauseType), fontCnNormal));
			ta.addCell(setCell(getStr(task.getEffectiveness()), fontCnNormal));
			ta.addCell(setCell(getStr(this.comAreaBo.getAreaCodeByAreaId(task.getOwnArea())), fontCnNormal));
		}
		return ta;
	}
	
	private String getQuestionName(String str){
		String questionName ="";
		if (!BasicTypeUtils.isNullorBlank(str)) {
			if(str.equals("1")){
				questionName = "保养是适用和有效的吗？";
			}else if(str.equals("2")){
				questionName = "使用状态的检验是适用和有效的吗？";
			}else if(str.equals("3")){
				questionName = "用正常的操作人员监控来探测功能恶化是适用和有效的吗？";
			}else if(str.equals("4")){
				questionName = "用原位或离位检查来探测功能恶化是适用和有效的吗？";
			}else if(str.equals("5")){
				questionName = "定时拆修是适用和有效的吗？";
			}else if(str.equals("6")){
				questionName = "定时报废是适用和有效的吗？";
			}else if(str.equals("7")){
				questionName = "有一种工作或综合工作是适用和有效的吗？";
			}else{
				questionName = "附加任务";
			}
		}
		return questionName;
		
	}
	
	 /**
     * 加载由当M3产生的任务
     */
	private List<TaskMsg> searchTask() {
		List<TaskMsg> list2 = new ArrayList<TaskMsg>();
		if (m3 != null) {
			List<String> list = new ArrayList<String>();
			//获取由当前M3产生任务问题顺序及任务ID
			if (!BasicTypeUtils.isNullorBlank(m3.getBaoyangTaskId())) {
				//1保养是适用和有效的吗？的回答,解释,产生的任务Id
				list.add(m3.getBaoyangTaskId());
			}
			if (!BasicTypeUtils.isNullorBlank(m3.getJianyanTaskId())) {
				//2状态检验是适用和有效的吗？的回答,解释,产生的任务Id
				list.add(m3.getJianyanTaskId());
			}
			if (!BasicTypeUtils.isNullorBlank(m3.getJiankongTaskId())) {
				//3用正常的操作人员监控来探测功能恶化是适用和有效的吗？的回答,解释,产生的任务Id
				list.add(m3.getJiankongTaskId());
			}
			if (!BasicTypeUtils.isNullorBlank(m3.getJianchaTaskId())) {
				//4用原位或离位检查来探测功能恶化是适用和有效的吗？的回答,解释,产生的任务Id
				list.add(m3.getJianchaTaskId());
			}
			if (!BasicTypeUtils.isNullorBlank(m3.getChaixiuTaskId())) {
				//5定时拆修是适用和有效的吗？的回答,解释,产生的任务Id
				list.add(m3.getChaixiuTaskId());
			}
			if (!BasicTypeUtils.isNullorBlank(m3.getBaofeiTaskId())) {
				//6定时报废是适用的吗？的回答,解释,产生的任务Id
				list.add(m3.getBaofeiTaskId());
			}
			if (!BasicTypeUtils.isNullorBlank(m3.getZongheTaskId())) {
				//7有一种工作或综合工作是适用和有效的吗？的回答,解释,产生的任务Id
				list.add(m3.getZongheTaskId());
			}
			for (int i = 0; i < list.size(); i++) {
				//加载由问题一到问题7产生的任务
				TaskMsg msg = (TaskMsg) this.m3Bo.loadById(TaskMsg.class, list.get(i));
				list2.add(msg);
			}

			Set<M3Additional> set = m3.getM3Additionals();
			if(set.size()>0){
				for (M3Additional additional : set) {
					//获取附加任务
					TaskMsg msg = (TaskMsg) this.m3Bo.loadById(TaskMsg.class,additional.getAddTaskId());
					list2.add(msg);
				}
			}
		}
		return list2;
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
		return new float[]{0.075f,0.075f,0.075f,0.075f,0.075f,0.075f,0.25f,0.11f,0.025f,0.025f,0.14f};
	}
}
