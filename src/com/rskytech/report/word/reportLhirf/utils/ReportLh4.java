package com.rskytech.report.word.reportLhirf.utils;

import java.awt.Color;
import java.lang.reflect.Method;
import java.util.List;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Table;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.bo.ICusLevelBo;
import com.rskytech.paramdefinemanage.bo.IDefineBaseCrackLenBo;
import com.rskytech.paramdefinemanage.bo.IDefineStructureParameterBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.CusDisplay;
import com.rskytech.pojo.CusItemS45;
import com.rskytech.pojo.CusLevel;
import com.rskytech.pojo.Lh4;
import com.rskytech.pojo.LhMain;

public class ReportLh4 extends LhirfReportBase {

	private List<CusItemS45> ED_itemList;
	private List<CusItemS45> AD_itemList;
	private CusDisplay lh4show;
	private ICusLevelBo cusLevelBo;
	private Lh4 lh4;
	private IDefineBaseCrackLenBo defineBaseCrackLenBo;
	private IDefineStructureParameterBo defineStructureParameterBo;
	private int flag = 1;
	private Integer ED_size;
	private Integer AD_size;

	public Integer getMaxSize(List<CusItemS45> ED_itemList, List<CusItemS45> AD_itemList) {
		int i = 0;
		for(CusItemS45 cus : ED_itemList) {
			List<CusLevel> EDlevelList = cusLevelBo
					.getLevelList(ms.getModelSeriesId(), ComacConstants.LH4,
							cus.getItemS45Id());
			if(EDlevelList.size() >= i) {
				i = EDlevelList.size();			
			}
		}
		for(CusItemS45 cus : AD_itemList) {
			List<CusLevel> ADlevelList = cusLevelBo
					.getLevelList(ms.getModelSeriesId(), ComacConstants.LH4,
							cus.getItemS45Id());
			if(ADlevelList.size() >= i) {
				i = ADlevelList.size();			
			}
		}
		return i;
	}
	
	public ReportLh4(Document document, ComModelSeries ms, ComArea area,
			LhMain lhMain, String effective, List<CusItemS45> ED_itemList,
			List<CusItemS45> AD_itemList, CusDisplay lh4show, Lh4 lh4,
			ICusLevelBo cusLevelBo) {
		super(document, ms, area, lhMain, effective);
		this.ED_itemList = ED_itemList;
		this.AD_itemList = AD_itemList;
		this.lh4show = lh4show;
		this.cusLevelBo = cusLevelBo;
		this.lh4 = lh4;

		if (ED_itemList != null) {
			this.ED_size = ED_itemList.size();
		}

		if (AD_itemList != null) {
			this.AD_size = AD_itemList.size();
		}
	}

	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(1, null);
		ta.insertTable(getEDADTable());
		ta.insertTable(getShowTable());
		ta.insertTable(getRepairTable());
		return ta;
	}

	// 报表上部的ED/AD总表格
	public Table getEDADTable() throws Exception {
		Table ta = setTableAndColumn(1, new float[] { 1.0f });

		ta.insertTable(getADAndEDTable());
		// ta.insertTable(getEDTable());
		// ta.insertTable(getADAndEDTable());
		return ta;
	}

	public Table getADAndEDTable() throws Exception {
		int matrixSize = ED_size * 2 + AD_size * 2;
		int colNum = 1 + matrixSize;
		Table ta = setTableAndColumn(colNum, null);
		Integer maxSize = getMaxSize(ED_itemList, AD_itemList);
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER,
				Element.ALIGN_CENTER, 1, 1));
		ta.addCell(setCell("环境损伤（ED）", fontCnNormal, Element.ALIGN_CENTER,
				Element.ALIGN_CENTER, 2 * ED_size, 1));
		ta.addCell(setCell("偶然损伤（AD）", fontCnNormal, Element.ALIGN_CENTER,
				Element.ALIGN_CENTER, 2 * AD_size, 1));
		ta.addCell(setCell("环境/敏感度等级", fontCnNormal, Element.ALIGN_CENTER,
				Element.ALIGN_CENTER, 1, 2));
		for (CusItemS45 cus : ED_itemList) {
			ta.addCell(setCell(cus.getItemName(), fontCnNormal,
					Element.ALIGN_CENTER, Element.ALIGN_CENTER, 2, 1));
		}

		for (CusItemS45 cus : AD_itemList) {
			ta.addCell(setCell(cus.getItemName(), fontCnNormal,
					Element.ALIGN_CENTER, Element.ALIGN_CENTER, 2, 1));
		}

		for (int i = 0; i < matrixSize / 2; i++) {
			ta.addCell(setCell("ER", fontCnNormal, Element.ALIGN_CENTER,
					Element.ALIGN_CENTER, 1, 1));
			ta.addCell(setCell("SR", fontCnNormal, Element.ALIGN_CENTER,
					Element.ALIGN_CENTER, 1, 1));
		}

		Table tab0 = new Table(1, maxSize);
		
		tab0.addCell(setCell("1", fontCnNormal, Element.ALIGN_CENTER,
				Element.ALIGN_CENTER, 1, 1));
		tab0.addCell(setCell("2", fontCnNormal, Element.ALIGN_CENTER,
				Element.ALIGN_CENTER, 1, 1));
		tab0.addCell(setCell("3", fontCnNormal, Element.ALIGN_CENTER,
				Element.ALIGN_CENTER, 1, 1));
		tab0.addCell(setCell("4", fontCnNormal, Element.ALIGN_CENTER,
				Element.ALIGN_CENTER, 1, 1));
		tab0.addCell(setCell("5", fontCnNormal, Element.ALIGN_CENTER,
				Element.ALIGN_CENTER, 1, 1));
		
		ta.insertTable(tab0);
		
		for (CusItemS45 e : ED_itemList) {
			List<CusLevel> EDlevelList = cusLevelBo
					.getLevelList(ms.getModelSeriesId(), ComacConstants.LH4,
							e.getItemS45Id());
			for (int i = flag; i < flag + 2; i++) {
				Method method;
				Table tab1 = new Table(1,maxSize);
				try {
					method = Lh4.class.getMethod("getSelect" + i);
					Integer object = (Integer) method.invoke(lh4);
					for (CusLevel cusLevel : EDlevelList) {
						if (cusLevel.getLevelValue().equals(object)) {
							
							Cell cell = setCell(
									getStr(cusLevel.getLevelName()),
									fontCnNormal, Element.ALIGN_CENTER,
									Element.ALIGN_CENTER, 1, 1);
							cell.setBackgroundColor(Color.green);
							tab1.addCell(cell);
						} else {
							tab1.addCell(setCell(getStr(cusLevel.getLevelName()),
									fontCnNormal, Element.ALIGN_CENTER,
									Element.ALIGN_CENTER, 1, 1));
						}
					}
					ta.insertTable(tab1);
				} catch (Exception x) {
					tab1.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER,
							Element.ALIGN_CENTER, 1, 1));
					ta.insertTable(tab1);
				}
			}
			flag = flag + 2;
		}

		for (CusItemS45 e : AD_itemList) {
			List<CusLevel> ADlevelList = cusLevelBo
					.getLevelList(ms.getModelSeriesId(), ComacConstants.LH4,
							e.getItemS45Id());
			for (int i = flag; i < flag + 2; i++) {
				Method method;
				Table tab2 = new Table(1,maxSize);
				try {
					method = Lh4.class.getMethod("getSelect" + i);
					Integer object = (Integer) method.invoke(lh4);
					for (CusLevel cusLevel : ADlevelList) {
						if (cusLevel.getLevelValue().equals(object)) {
							Cell cell = setCell(
									getStr(cusLevel.getLevelName()),
									fontCnNormal, Element.ALIGN_CENTER,
									Element.ALIGN_CENTER, 1, 1);
							cell.setBackgroundColor(Color.green);
							tab2.addCell(cell);
						} else {
							tab2.addCell(setCell(getStr(cusLevel.getLevelName()),
									fontCnNormal, Element.ALIGN_CENTER,
									Element.ALIGN_CENTER, 1, 1));
						}
					}
					ta.insertTable(tab2);
				} catch (Exception x) {
					tab2.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER,
							Element.ALIGN_CENTER, 1, 1));
					ta.insertTable(tab2);
				}
			}
			flag += 2;
		}
		
		ta.addCell(setCell("ED/AD: " + lh4.getResult(), fontCnNormal, Element.ALIGN_CENTER,
				Element.ALIGN_CENTER, 13, 1));
		ta.addCell(setCell("备注：", fontCnNormal, Element.ALIGN_CENTER,
				Element.ALIGN_CENTER, 1, 3));
		ta.addCell(setCell(lh4.getRemark(), fontCnNormal, Element.ALIGN_CENTER,
				Element.ALIGN_CENTER, (AD_size+ED_size) * 2, 3));

		return ta;
	}

	/*
	 * //报表上部的ED/AD总表格的右部的AD和汇总表 public Table getADAndEDTable() throws Exception
	 * { Table ta = setTableAndColumn(1, null); ta.insertTable(getADTable());
	 * ta.insertTable(getEATable()); return ta; }
	 * 
	 * //报表上部的ED/AD总表格中的ED表格 public Table getEDTable() throws Exception { Table
	 * ta = setTableAndColumn(5, new float[]{0.02f, 0.32f, 0.32f, 0.32f,
	 * 0.02f});
	 * 
	 * ta.addCell(setCell("", fontNowNormal, Element.ALIGN_CENTER,
	 * Element.ALIGN_MIDDLE, 5, null, 0, Rectangle.LEFT));
	 * 
	 * ta.addCell(setCell("", fontNowNormal, Element.ALIGN_CENTER,
	 * Element.ALIGN_MIDDLE, null, null, 0, Rectangle.LEFT));
	 * ta.addCell(setCell(getStr("环境损伤(ED)"), fontNowTitle,
	 * Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 3, null));
	 * ta.addCell(setCell("", fontNowNormal, Element.ALIGN_CENTER,
	 * Element.ALIGN_MIDDLE, null, null, 0, null));
	 * 
	 * ta.addCell(setCell("", fontNowNormal, Element.ALIGN_CENTER,
	 * Element.ALIGN_MIDDLE, null, null, 0, Rectangle.LEFT));
	 * ta.addCell(setCell(getStr("因素"), fontNowNormal));
	 * ta.addCell(setCell(getStr("ER:环境等级"), fontNowNormal));
	 * ta.addCell(setCell(getStr("SR:敏感度等级"), fontNowNormal));
	 * ta.addCell(setCell("", fontNowNormal, Element.ALIGN_CENTER,
	 * Element.ALIGN_MIDDLE, null, null, 0, null));
	 * 
	 * for (CusItemS45 e : ED_itemList) { ta.addCell(setCell("", fontNowNormal,
	 * Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, null, 0,
	 * Rectangle.LEFT)); ta.addCell(setCell(getStr(e.getItemName()),
	 * fontNowNormal));
	 * 
	 * List<CusLevel> EDlevelList =
	 * cusLevelBo.getLevelList(ms.getModelSeriesId(), ComacConstants.LH4,
	 * e.getItemS45Id());
	 * 
	 * for (int i = flag; i < flag + 2; i++){ Method method; try { method =
	 * Lh4.class.getMethod("getSelect" + i); Integer object =
	 * (Integer)method.invoke(lh4); for (CusLevel cusLevel : EDlevelList) { if
	 * (cusLevel.getLevelValue().equals(object)){
	 * ta.addCell(setCell(getStr(cusLevel.getLevelName()), fontNowNormal)); } }
	 * } catch (Exception x) { ta.addCell(setCell("", fontNowNormal)); } } flag
	 * = flag + 2;
	 * 
	 * ta.addCell(setCell("", fontNowNormal, Element.ALIGN_CENTER,
	 * Element.ALIGN_MIDDLE, null, null, 0, null)); }
	 * 
	 * ta.addCell(setCell("", fontNowNormal, Element.ALIGN_CENTER,
	 * Element.ALIGN_MIDDLE, 5, null, 0, Rectangle.LEFT)); return ta; }
	 * 
	 * //报表上部的ED/AD总表格中的AD表格 public Table getADTable() throws Exception { Table
	 * ta = setTableAndColumn(5, new float[]{0.02f, 0.32f, 0.32f, 0.32f,
	 * 0.02f});
	 * 
	 * ta.addCell(setCell("", fontNowNormal, Element.ALIGN_CENTER,
	 * Element.ALIGN_MIDDLE, 5, null, 0, Rectangle.RIGHT));
	 * 
	 * ta.addCell(setCell("", fontNowNormal, Element.ALIGN_CENTER,
	 * Element.ALIGN_MIDDLE, null, null, 0, null));
	 * ta.addCell(setCell(getStr("偶然损伤(AD)"), fontNowTitle,
	 * Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 3, null));
	 * ta.addCell(setCell("", fontNowNormal, Element.ALIGN_CENTER,
	 * Element.ALIGN_MIDDLE, null, null, 0, Rectangle.RIGHT));
	 * 
	 * ta.addCell(setCell("", fontNowNormal, Element.ALIGN_CENTER,
	 * Element.ALIGN_MIDDLE, null, null, 0, null));
	 * ta.addCell(setCell(getStr("因素") , fontNowNormal));
	 * ta.addCell(setCell(getStr("ER:环境等级"), fontNowNormal));
	 * ta.addCell(setCell(getStr("SR:敏感度等级"), fontNowNormal));
	 * ta.addCell(setCell("", fontNowNormal, Element.ALIGN_CENTER,
	 * Element.ALIGN_MIDDLE, null, null, 0, Rectangle.RIGHT));
	 * 
	 * for (CusItemS45 e : AD_itemList) { ta.addCell(setCell("", fontNowNormal,
	 * Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, null, 0, null));
	 * ta.addCell(setCell(getStr(e.getItemName()), fontNowNormal));
	 * 
	 * List<CusLevel> ADlevelList =
	 * cusLevelBo.getLevelList(ms.getModelSeriesId(), ComacConstants.LH4,
	 * e.getItemS45Id());
	 * 
	 * for (int i = flag; i < flag + 2; i++){ Method method; try { method =
	 * Lh4.class.getMethod("getSelect" + i); Integer object =
	 * (Integer)method.invoke(lh4); for (CusLevel cusLevel : ADlevelList) { if
	 * (cusLevel.getLevelValue().equals(object)){
	 * ta.addCell(setCell(getStr(cusLevel.getLevelName()), fontNowNormal)); } }
	 * } catch (Exception x) { ta.addCell(setCell("", fontNowNormal)); } } flag
	 * = flag + 2;
	 * 
	 * ta.addCell(setCell("", fontNowNormal, Element.ALIGN_CENTER,
	 * Element.ALIGN_MIDDLE, null, null, 0, Rectangle.RIGHT)); }
	 * 
	 * return ta; }
	 * 
	 * //报表上部的ED/AD总表格中的ED/AD结果汇总表格 public Table getEATable() throws Exception {
	 * Table ta = setTableAndColumn(4, new float[]{0.02f, 0.48f, 0.48f, 0.02f});
	 * 
	 * ta.addCell(setCell("", fontNowNormal, Element.ALIGN_CENTER,
	 * Element.ALIGN_MIDDLE, 4, null, 0, Rectangle.RIGHT));
	 * 
	 * ta.addCell(setCell("", fontNowNormal, Element.ALIGN_CENTER,
	 * Element.ALIGN_MIDDLE, null, null, 0, null));
	 * ta.addCell(setCell(getStr("综合ED/AD"), fontNowTitle, Element.ALIGN_CENTER,
	 * Element.ALIGN_MIDDLE, 2, null)); ta.addCell(setCell("", fontNowNormal,
	 * Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, null, 0,
	 * Rectangle.RIGHT));
	 * 
	 * ta.addCell(setCell("", fontNowNormal, Element.ALIGN_CENTER,
	 * Element.ALIGN_MIDDLE, null, null, 0, null));
	 * ta.addCell(setCell(getStr("综合ED/AD"), fontNowNormal));
	 * ta.addCell(setCell(getStr(lh4.getResult()), fontNowNormal));
	 * ta.addCell(setCell("", fontNowNormal, Element.ALIGN_CENTER,
	 * Element.ALIGN_MIDDLE, null, null, 0, Rectangle.RIGHT));
	 * 
	 * ta.addCell(setCell("", fontNowNormal, Element.ALIGN_CENTER,
	 * Element.ALIGN_MIDDLE, null, null, 0, null));
	 * ta.addCell(setCell(getStr("备注"), fontNowNormal));
	 * ta.addCell(setCell(getStr(lh4.getRemark()), fontNowNormal));
	 * ta.addCell(setCell("", fontNowNormal, Element.ALIGN_CENTER,
	 * Element.ALIGN_MIDDLE, null, null, 0, Rectangle.RIGHT));
	 * 
	 * ta.addCell(setCell("", fontNowNormal, Element.ALIGN_CENTER,
	 * Element.ALIGN_MIDDLE, 4, null, 0, Rectangle.RIGHT)); return ta; }
	 */
	// 报表中部的ED/AD敏感性说明
	public Table getShowTable() throws Exception {
		Table ta = setTableAndColumn(2, new float[] { 0.3f, 0.7f });

		ta.addCell(setCell(getStr("ED/AD敏感性"), fontNowTitle,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2, null));

		ta.addCell(setCell(getStr("综合的ED/AD(备注)"), fontNowNormal));
		ta.addCell(setCell(getStr(lh4show.getDisplayContent()), fontNowNormal));
		return ta;
	}

	// 报表下部的是否需要维修
	public Table getRepairTable() throws Exception {
		Table ta = setTableAndColumn(3, new float[] { 0.4f, 0.4f, 0.2f });

		ta.addCell(setCell(getStr("是否需要L/Hirf维护任务"), fontNowTitle,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 3, null));

		ta.addCell(setCell(getStr("是"), fontNowNormal, 2, null,
				isColor(lh4.getNeedLhTask(), 1)));
		ta.addCell(setCell(getStr("否"), fontNowNormal,
				isColor(lh4.getNeedLhTask(), 0)));

		ta.addCell(setCell(
				getStr("L/Hirf防护功能部件退化(包含局部区域的共同模式)结合L/Hirf事件是否会妨碍飞机持续安全飞行或着落"),
				fontNowNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2,
				null));
		ta.addCell(setCell(getStr("分析关闭"), fontNowNormal, null, 3,
				isColor(lh4.getNeedLhTask(), 0)));

		ta.addCell(setCell(getStr("是(继续LH-5A)"), fontNowNormal,
				isColor(lh4.getIsSafe(), 1)));
		ta.addCell(setCell(getStr("否(取消LH-5A)"), fontNowNormal,
				isColor(lh4.getIsSafe(), 0)));

		if (lh4.getIsSafe() == 1) {
			ta.addCell(setCell(
					getStr("说明理由:\n"
							+ (lh4.getSafeReason() == null ? "" : lh4
									.getSafeReason())), fontNowNormal,
					Element.ALIGN_LEFT, Element.ALIGN_MIDDLE));
			ta.addCell(setCell(getStr("说明理由:\n"), fontNowNormal,
					Element.ALIGN_LEFT, Element.ALIGN_MIDDLE));
		} else {
			ta.addCell(setCell(getStr("说明理由:\n"), fontNowNormal,
					Element.ALIGN_LEFT, Element.ALIGN_MIDDLE));
			ta.addCell(setCell(
					getStr("说明理由:\n"
							+ (lh4.getSafeReason() == null ? "" : lh4
									.getSafeReason())), fontNowNormal,
					Element.ALIGN_LEFT, Element.ALIGN_MIDDLE));
		}

		return ta;
	}

	public Color isColor(Integer now, int param) {
		if (now == param) {
			return bg;
		}
		return null;
	}

	@Override
	public int getCol() {
		return 0;
	}

	@Override
	public float[] getColWidth() {
		return null;
	}

	@Override
	public String getTableAbbreviation() {
		return "LH-4";
	}

	@Override
	public String getTableName() {
		return "L/HIRF防护项目的ED/AD敏感性去确定";
	}

	public List<CusItemS45> getED_itemList() {
		return ED_itemList;
	}

	public void setED_itemList(List<CusItemS45> eD_itemList) {
		ED_itemList = eD_itemList;
	}

	public List<CusItemS45> getAD_itemList() {
		return AD_itemList;
	}

	public void setAD_itemList(List<CusItemS45> aD_itemList) {
		AD_itemList = aD_itemList;
	}

	public CusDisplay getLh4show() {
		return lh4show;
	}

	public void setLh4show(CusDisplay lh4show) {
		this.lh4show = lh4show;
	}

	public ICusLevelBo getCusLevelBo() {
		return cusLevelBo;
	}

	public void setCusLevelBo(ICusLevelBo cusLevelBo) {
		this.cusLevelBo = cusLevelBo;
	}

	public Lh4 getLh4() {
		return lh4;
	}

	public void setLh4(Lh4 lh4) {
		this.lh4 = lh4;
	}

	public IDefineBaseCrackLenBo getDefineBaseCrackLenBo() {
		return defineBaseCrackLenBo;
	}

	public void setDefineBaseCrackLenBo(
			IDefineBaseCrackLenBo defineBaseCrackLenBo) {
		this.defineBaseCrackLenBo = defineBaseCrackLenBo;
	}

	public IDefineStructureParameterBo getDefineStructureParameterBo() {
		return defineStructureParameterBo;
	}

	public void setDefineStructureParameterBo(
			IDefineStructureParameterBo defineStructureParameterBo) {
		this.defineStructureParameterBo = defineStructureParameterBo;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public Integer getED_size() {
		return ED_size;
	}

	public void setED_size(Integer eD_size) {
		ED_size = eD_size;
	}

	public Integer getAD_size() {
		return AD_size;
	}

	public void setAD_size(Integer aD_size) {
		AD_size = aD_size;
	}
}
