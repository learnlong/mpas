package com.rskytech.report.word.reportArea.utils;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.rskytech.paramdefinemanage.bo.ICusLevelBo;
import com.rskytech.paramdefinemanage.bo.IStandardRegionParamBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.CusItemZa5;
import com.rskytech.pojo.CusLevel;
import com.rskytech.pojo.CusMatrix;
import com.rskytech.pojo.Za5;

public class ReportZa5 extends AreaReportBase {

	private Za5 za5;
	private IStandardRegionParamBo standardRegionParamBo;
	private ICusLevelBo cusLevelBo;
	private List<CusItemZa5> firstNodeList = null;
	private List<tableLevel> nodePosList = new ArrayList<tableLevel>();//矩阵中每个节点的位置及节点下子节点的个数
	private List<CusItemZa5> leafNodeList = new ArrayList<CusItemZa5>();
	private Integer maxLevel = 0;
	private int rowsFirst = 0;//第一个副矩阵的行数
	private int rowsSecond = 0;//第二个副矩阵的行数
	private int colsSecond = 0;//第二个副矩阵的列数
	private List<CusMatrix> cusMatrixFirst = null;//第一副矩阵
	private List<CusMatrix> cusMatrixSecond = null;//第二副矩阵
	private Integer ID = 0;//暴露性等级/重要性等级
	private Integer ID2 = 0;//暴露性等级/重要性等级
	
	public ReportZa5(Document document, ComModelSeries ms, ComArea area, Za5 za5, IStandardRegionParamBo standardRegionParamBo,
			ICusLevelBo cusLevelBo) {
		super(document, ms, area);
		this.za5 = za5;
		this.standardRegionParamBo = standardRegionParamBo;
		this.cusLevelBo = cusLevelBo;
		
		cusMatrixFirst = standardRegionParamBo.searchFirstMatrix(ms.getModelSeriesId());
		rowsFirst = (int) Math.sqrt(cusMatrixFirst.size());
		
		cusMatrixSecond = standardRegionParamBo.searchSecondMatrix(ms.getModelSeriesId());
		colsSecond = cusMatrixSecond.get(cusMatrixSecond.size() - 1).getMatrixCol();
		rowsSecond = cusMatrixSecond.get(cusMatrixSecond.size() - 1).getMatrixRow();
	}
	
	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(1, null);
    	ta.insertTable(upTable());
    	ta.insertTable(middleTable());
    	ta.insertTable(downTable());
		return ta;
	}
	
	private Table upTable(){
		//一级节点
		Integer tableCol = 1;
		firstNodeList = standardRegionParamBo.getMatrixList("00", ms.getModelSeriesId());
		tableCol = getTableCols(firstNodeList, tableCol);
		
		Table ta = setTableAndColumn(tableCol, null);
		
		setTableList(firstNodeList, 1);
		
		try{
			ta.addCell(setCell("区域因素评级", fontCnTitle, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, tableCol, null));
			
			ta.addCell(setCell("级号", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, maxLevel));
			
			for(int i = 1; i <= maxLevel; i++){
				for (tableLevel tl : nodePosList) {
					if (tl.getLevel() == i){
						Integer rowspan = null;
						Integer colspan = null;
						if (tl.getSize()==0){
							rowspan = maxLevel - tl.getLevel() + 1;
						} else {
							colspan = tl.getSize();
						}
						ta.addCell(setCell(tl.getCusItemZa5().getItemName(), fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, colspan, rowspan));
					}
				}
			}
			
			List<CusLevel> levelList = cusLevelBo.getLevelList(ms.getModelSeriesId(), "ZA5", leafNodeList.get(0).getItemZa5Id());
			for(Integer i = 0; i < levelList.size(); i++){
				Integer k = i + 1;
				int selectNum = 1;
				
				ta.addCell(setCell(k.toString(), fontCnNormal));
				
				for(CusItemZa5 ciz : leafNodeList){
					List<CusLevel> list = cusLevelBo.getLevelList(ms.getModelSeriesId(), "ZA5", ciz.getItemZa5Id());
					Method method;
					try {
						method = Za5.class.getMethod("getSelect" + selectNum++);
						Integer object = (Integer)method.invoke(za5);
						if (list.get(i).getLevelValue().equals(object)){
							ta.addCell(setCell(list.get(i).getLevelName(), fontCnNormal, bgGree));
						} else {
							ta.addCell(setCell(list.get(i).getLevelName(), fontCnNormal));
						}
					} catch (Exception e) {
						ta.addCell(setCell("", fontCnNormal));
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}catch (Throwable e) {
			e.printStackTrace();
		}
	
		return ta;
	}
	
	private Table middleTable() throws Exception {
		Table ta = setTableAndColumn(2, new float[]{0.5f, 0.5f});
		ta.addCell(setCell("标准区域分析评级矩阵", fontCnTitle));
		ta.addCell(setCell("区域检查间隔", fontCnTitle));
		return ta;
	}
	
	private Table downTable() throws Exception {
		Table ta = setTableAndColumn(2, new float[]{0.5f, 0.5f});
		ta.insertTable(downTable_left());
    	ta.insertTable(downTable_right());
		return ta;
	}
	
	private Table downTable_left() throws Exception {
		Table ta = null;
		if (firstNodeList.size()==3){
			ta = setTableAndColumn(3, new float []{0.45f, 0.1f, 0.45f});
	    	ta.insertTable(getOneTable());
	    	ta.insertTable(getImgTable());
	    	ta.insertTable(getTwoTable());
		}
		if (firstNodeList.size() == 2){
			ta = setTableAndColumn(1, null);
			ta.insertTable(getOneTable());
		}
		return ta;
	}
	
	private Table getOneTable() throws Exception{
		String n = "";
		
		Table ta = setTableAndColumn(rowsFirst + 3, setWidthTable(rowsFirst + 2, true, 0.01f, false, 0.00f));
		
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, rowsFirst + 3, 0, Rectangle.LEFT));
		
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, rowsFirst + 2, null, 0, null));
		    
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2, 2));
		ta.addCell(setCell(getStr(firstNodeList.get(1).getItemName()), fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, rowsFirst, null));
		for (Integer i = 1; i <= rowsFirst; i++){
			n = i.toString();
			if (i.equals(za5.getLevel2())){
				ta.addCell(setCell(n, fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, bgGree));
			} else {
				ta.addCell(setCell(n, fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
			}
		
		}
		
		ta.addCell(setCell(getStr(firstNodeList.get(0).getItemName()),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, rowsFirst));
		for (Integer i = 1; i <= rowsFirst; i++){
			n = i.toString();
			if (i.equals(za5.getLevel1())){
				ta.addCell(setCell(n, fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, bgGree));
			} else {
				ta.addCell(setCell(n, fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
			}
			
			for (Integer j = 1; j <= rowsFirst; j++){
				for (CusMatrix cm : cusMatrixFirst){
					if (cm.getMatrixRow().equals(i) && cm.getMatrixCol().equals(j)){
						n = cm.getMatrixValue().toString();
						if (i.equals(za5.getLevel1()) && j.equals(za5.getLevel2())){
							ID = cm.getMatrixValue();
							ta.addCell(setCell(n, fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, bgGree));
						}else{
							ta.addCell(setCell(n, fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
						}
						break;
					}
				}		
			}
		}

		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, rowsFirst + 3, null, 0, Rectangle.LEFT));
		
		if (rowsFirst < rowsSecond){
			for (int x = 0; x < (rowsSecond - rowsFirst); x++){
				ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, rowsFirst + 3, null, 0, Rectangle.LEFT));
			}
		}
		return ta;
	}
	
	/**
	 * 读取箭头图片
	 * @return ta
	 * @throws Exception
	 */
	private Table getImgTable() throws Exception {
		Table ta = setTableAndColumn(1, new float[]{1.0f});
		String str="/com/rskytech/report/word/reportArea/utils/imgs/arrow_4.2.png";
		InputStream stream = ReportZa5.class.getResourceAsStream(str); 
		Image img = Image.getInstance(IOUtils.toByteArray(stream));	
		stream.close();
		img = getScale(img, 400, 300);
		ta.addCell(setCell(img, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, (rowsFirst > rowsSecond ? rowsFirst : rowsSecond) + 4, 0, null));
		return ta;
	}
	
	private Table getTwoTable() throws Exception{
		String n = "";
		List<CusMatrix> tempList = new ArrayList<CusMatrix>();//左边标题的列表
		int flag = 0;
		for (CusMatrix cmx : cusMatrixFirst) {
			for (CusMatrix cusMatrix : tempList) {
				if(cmx.getMatrixValue().equals(cusMatrix.getMatrixValue())){
					flag = 1;//有重复数据
					break;
				}
			}
			if (flag == 0){
				tempList.add(cmx);
			}
			flag = 0;
		}
		
		Table ta = setTableAndColumn(colsSecond + 3, setWidthTable(colsSecond + 2, false, 0.00f, true, 0.01f));
		
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, colsSecond + 2, null, 0, null));
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, rowsSecond + 3, 0, Rectangle.RIGHT));
		
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2, 2));
		ta.addCell(setCell(getStr(firstNodeList.get(2).getItemName()), fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, colsSecond, null));
		for (Integer i = 1; i <= colsSecond; i++) {
			n = i.toString();
			if (i.equals(za5.getLevel3())){
				ta.addCell(setCell(n, fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, bgGree));
			} else {
				ta.addCell(setCell(n, fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
			}
		}
		
		ta.addCell(setCell("过渡性等级", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, rowsSecond));
		
		for (Integer i = 1; i <= rowsSecond; i++){
			Integer title = tempList.get(i-1).getMatrixValue();
			n = (title == null ? "" : title.toString());
			
			if (title.equals(ID)){
				ta.addCell(setCell(n, fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, bgGree));
			} else {
				ta.addCell(setCell(n, fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
			}
			
			for(Integer j = 1; j <= colsSecond; j++){
				for(CusMatrix cm : cusMatrixSecond){
					if(cm.getMatrixRow().equals(i)&&cm.getMatrixCol().equals(j)){
						n = cm.getMatrixValue().toString();
						if (title.equals(ID) && j.equals(za5.getLevel3())){
							ID2 = cm.getMatrixValue();
							ta.addCell(setCell(n, fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, bgGree));
						} else {
							ta.addCell(setCell(n, fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
						}
						break;
					}
				}		
			}
			
		}
		
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, colsSecond + 3, null, 0, Rectangle.RIGHT));
		
		if (rowsFirst > rowsSecond){
			for (int x = 0; x < (rowsFirst - rowsSecond); x++){
				ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, colsSecond + 3, null, 0, Rectangle.RIGHT));
			}
		}
		
		return ta;
	}
	
	private Table downTable_right() throws Exception {
		List<CusInterval> finalInMatrixList = standardRegionParamBo.searchFinalMatrix(ms.getModelSeriesId(), "IN");
	    List<CusInterval> finalOutMatrixList = standardRegionParamBo.searchFinalMatrix(ms.getModelSeriesId(), "OUT");
	    
	    String n = "";
	    int cols = finalInMatrixList.size();
	    
	    Table ta = setTableAndColumn(cols + 4, setWidthTable(cols + 2, true, 0.01f, true, 0.01f));
	    
	    int nowRow = 0;
	    if (rowsFirst > rowsSecond){
	    	nowRow = rowsFirst;
	    } else {
	    	nowRow = rowsSecond;
	    }
	    nowRow = nowRow + 4;
		 
	    ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, nowRow, 0, null));
	    ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, cols + 2, null, 0, null));		
	    ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, nowRow, 0, Rectangle.RIGHT));
	   
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2, 2));
		ta.addCell(setCell("区域总等级", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 5, null));
		
		for (CusInterval cusInterval : finalOutMatrixList) {
			n = cusInterval.getIntervalLevel().toString();
			if (ID2.equals(cusInterval.getIntervalLevel())){
				ta.addCell(setCell(n, fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, bgGree));
			} else {
				ta.addCell(setCell(n, fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
			}
		}
		
		ta.addCell(setCell("区域检查间隔", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 2));	
		
		if (za5.getStep().equals("ZA5B")){
			ta.addCell(setCell("外部", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, bgGree));
		} else {
			ta.addCell(setCell("外部", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
		}
		
		for (CusInterval cusInterval : finalOutMatrixList) {
			n = cusInterval.getIntervalValue();
			if (ID2.equals(cusInterval.getIntervalLevel()) && za5.getStep().equals("ZA5B")){
				ta.addCell(setCell(n, fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, bgGree));
			}else{
				ta.addCell(setCell(n, fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
			}
		}
		
		if (za5.getStep().equals("ZA5A")){
			ta.addCell(setCell("内部", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, bgGree));
		} else {
			ta.addCell(setCell("内部", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
		}
		
		for (CusInterval cusInterval : finalInMatrixList) {
			n = cusInterval.getIntervalValue();
			if (ID2.equals(cusInterval.getIntervalLevel()) && za5.getStep().equals("ZA5A")){
				ta.addCell(setCell(n, fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, bgGree));
			}else{
				ta.addCell(setCell(n, fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
			}
		}
		
		ta.addCell(setCell("区检查间隔", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2, null));
		ta.addCell(setCell(za5.getTaskInterval(), fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, cols, null));
		
		if (nowRow > 6){
			ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, cols + 2, nowRow - 6, 0, null));
		}
		return ta;
	}
	
	private float[] setWidthTable(int cols, boolean hasLeft, float left, boolean hasRight, float right){
		int l = 0;
		int r = 0;
		float a = 1.00f;
		float w = 0.00f;
		
		if (hasLeft){
			l = 1;
			a = a - left;
		}
		if (hasRight){
			r = 1;
			a = a - right;
		}
		float[] f = new float[cols + l + r];
		
		if (hasLeft){
			f[0] = left;
		}
		
		w = a / cols;
		
		for (int i = 0; i < cols; i++){
			f[l + i] = w;
		}
		
		if (hasRight){
			f[cols + l + r - 1] = right;
		}
		
		return f;
	}
	
	/**
	 * 查看这个list能占几个表格单元
	 * @param List 
	 * @param colSize 这个list一共能占的行数
	 */
	private Integer getTableCols(List<CusItemZa5> List, Integer colSize){
		for (CusItemZa5 cusItemZa5 : List) {
			List<CusItemZa5> childNodeList = standardRegionParamBo.getMatrixList(cusItemZa5.getItemZa5Id(), ms.getModelSeriesId());
			if (childNodeList == null || childNodeList.size() == 0){
				colSize++;
			}else{
				colSize = getTableCols(childNodeList, colSize);
			}
		}
		return colSize;
	}
	
	/**
	 * @param List 
	 * @param level 等级 也就是第几行
	 * @param colSize 这个list一共能占的行数
	 */
	private void setTableList(List<CusItemZa5> List, Integer level){
		for (CusItemZa5 cusItemZa5 : List) {
			List<CusItemZa5> childNodeList = standardRegionParamBo.getMatrixList(cusItemZa5.getItemZa5Id(), ms.getModelSeriesId());
			if (childNodeList.size() == 0){
				tableLevel tableLevel = new tableLevel(cusItemZa5, level, 0);
				leafNodeList.add(cusItemZa5);
				nodePosList.add(tableLevel);
				if (maxLevel < level){
					maxLevel = level;
				}
			} else {
				Integer childNodeSize = getTableCols(childNodeList,0);
				tableLevel tableLevel = new tableLevel(cusItemZa5, level, childNodeSize);
				nodePosList.add(tableLevel);
				Integer nextLevel = level + 1;
				if (maxLevel < nextLevel){
					maxLevel = nextLevel;
				}
				setTableList(childNodeList, nextLevel);
			}
		}
	}
	
	//用来画矩阵
	private class tableLevel{
		private CusItemZa5 cusItemZa5;//存放节点
		private Integer level;//等级
		private Integer size;//该节点下有多少个子节点
		
		public tableLevel(CusItemZa5 cusItemZa5, Integer level, Integer size) {
			super();
			this.cusItemZa5 = cusItemZa5;
			this.level = level;
			this.size = size;
		}

		public tableLevel() {
			super();
		}

		public CusItemZa5 getCusItemZa5() {
			return cusItemZa5;
		}

		public void setCusItemZa5(CusItemZa5 cusItemZa5) {
			this.cusItemZa5 = cusItemZa5;
		}

		public Integer getLevel() {
			return level;
		}

		public void setLevel(Integer level) {
			this.level = level;
		}

		public Integer getSize() {
			return size;
		}

		public void setSize(Integer size) {
			this.size = size;
		}
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
	public String getReportName() {
		return "表6  标准区域间隔分析表";
	}

	@Override
	public String getTableAbbreviation() {
		return "ZA-4";
	}

	@Override
	public String getTableName() {
		return "区域分析-标准区域分析";
	}

}
