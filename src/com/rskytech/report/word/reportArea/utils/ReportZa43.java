package com.rskytech.report.word.reportArea.utils;

import java.awt.Color;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.bo.ICusLevelBo;
import com.rskytech.paramdefinemanage.bo.IIncreaseRegionParamBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.CusItemS45;
import com.rskytech.pojo.CusLevel;
import com.rskytech.pojo.CusMatrix;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.Za43;

public class ReportZa43 extends AreaReportBase {

	private Za43 za43;
	private IIncreaseRegionParamBo increaseRegionParamBo;
	private ICusLevelBo cusLevelBo;
	private TaskMsg task;
	private List<CusItemS45> cusZa43EdList;
	private List<CusItemS45> cusZa43AdList;
	private List<CusMatrix> cusMatrix;
	private int edSize;
	private int adSize;
	private int matrixSize;
	private int upEdAdSize;
	private int downEdAdSize;
	int flag = 0;
	String s = "";
	
	public ReportZa43(Document document, ComModelSeries ms, ComArea area, Za43 za43, IIncreaseRegionParamBo increaseRegionParamBo, 
			ICusLevelBo cusLevelBo, TaskMsg task) {
		super(document, ms, area);
		this.za43 = za43;
		this.task = task;
		this.increaseRegionParamBo = increaseRegionParamBo;
		this.cusLevelBo = cusLevelBo;
		
		cusZa43EdList = increaseRegionParamBo.searchZa43Item(ms.getModelSeriesId(), ComacConstants.ED);
		cusZa43AdList = increaseRegionParamBo.searchZa43Item(ms.getModelSeriesId(), "AD");
		cusMatrix = increaseRegionParamBo.searchFirstMatrix(ms.getModelSeriesId());
		
		if (cusZa43EdList == null){
			edSize = 0;
		} else {
			edSize = cusZa43EdList.size();
		}
		
		if (cusZa43AdList == null){
			adSize = 0;
		} else {
			adSize = cusZa43AdList.size();
		}
		
		upEdAdSize = (edSize > adSize ? edSize : adSize);
		
		if (cusMatrix == null){
			matrixSize = 0;
			downEdAdSize = matrixSize + 5;
		} else {
			matrixSize = (int) Math.sqrt(cusMatrix.size());
			downEdAdSize = matrixSize + 5;
		}
	}
	
	public Table getTableContent() throws Exception {
    	Table ta = setTableAndColumn(1, null);
    	ta.insertTable(upTable());
    	ta.insertTable(downTable());
		return ta;
	}
	
	private Table upTable() throws Exception {
		Table ta = setTableAndColumn(4, new float[]{0.2f, 0.35f, 0.25f, 0.2f});
    	ta.insertTable(up1());
    	ta.insertTable(up2());
    	ta.insertTable(up3());
    	ta.insertTable(up4());
		return ta;
	}
	
	//上边左边的表
	private Table up1() throws Exception {
		Table ta = setTableAndColumn(1, new float[]{1.0f});
    	ta.insertTable(up11());
    	ta.insertTable(up12());
		return ta;
	}
	
	//左边上面的ED表
	private Table up11() throws Exception {
		Table ta = setTableAndColumn(3, new float[]{0.1f, 0.45f, 0.45f});
		
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, upEdAdSize + 2, 0, Rectangle.LEFT));
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2, null, 0, null));
		
		ta.addCell(setCell("环境损伤", fontCnTitle, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2, null));
		
		for (CusItemS45 cis : cusZa43EdList) {
			ta.addCell(setCell(getStr(cis.getItemName()), fontCnNormal));
			List<CusLevel> itemList = cusLevelBo.getLevelList(ms.getModelSeriesId(), ComacConstants.ZA43, cis.getItemS45Id());	
			Method method;
			try {
				method = Za43.class.getMethod("getSelect" + ++flag);
				Integer object = (Integer) method.invoke(za43);
				for (CusLevel cusLevel : itemList) {
					if (cusLevel.getLevelValue().equals(object)) {
						s = cusLevel.getLevelName();
						break;
					}
				}
			} catch (Exception e) {
				s = "";
			} finally {
				ta.addCell(setCell(getStr(s), fontCnNormal));
			}
		}
		
		if (upEdAdSize - edSize > 0){
			for (int i = 0; i < upEdAdSize - edSize; i++){
				ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, null, 0, null));
				ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, null, 0, null));
			}
		}
		return ta;
	}
	
	//左边的箭头图片
	private Table up12() throws Exception {
		Table ta = setTableAndColumn(1, new float[]{1.0f});
		String str="/com/rskytech/report/word/reportArea/utils/imgs/arrow_4.3_right.png";
		InputStream stream = ReportZa43.class.getResourceAsStream(str); 
		Image img = Image.getInstance(IOUtils.toByteArray(stream));	
		stream.close();
		img = getScale(img, 400, 300);
		
		ta.addCell(setCell(img, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 5, null, Rectangle.LEFT));
		ta.addCell(setCell("", null, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, downEdAdSize - 5, null, Rectangle.LEFT));
		return ta;
	}
	
	//上边中间的ED/AD表
	private Table up2() throws Exception {
		Table ta = setTableAndColumn(matrixSize + 2, null);
		
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, matrixSize + 2, upEdAdSize + 2, 0, null));
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, matrixSize + 2, null, 0, null));
		
		ta.addCell(setCell( "", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2, 2));
		ta.addCell(setCell("偶然损伤概率",  fontCnTitle, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, matrixSize, null));
		
		CusMatrix result = null;// 结果的CusMatrix
		for (CusMatrix cm : cusMatrix) {
			if (za43.getResult().equals(cm.getMatrixValue())) {
				result = cm;
				break;
			}
		}
		for (Integer i = 1; i <= matrixSize; i++) {
			ta.addCell(setCell(i.toString(), fontCnNormal, isColor(result.getMatrixCol(), i)));
		}
		
		ta.addCell(setCell("环境损伤概率", fontCnTitle, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, matrixSize));
		for (Integer i = 1; i <= matrixSize; i++) {
			ta.addCell(setCell(i.toString(), fontCnNormal, isColor(result.getMatrixRow(),i)));
			for (Integer j = 1; j <= matrixSize; j++) {
				for (CusMatrix cm : cusMatrix) {
					if (cm.getMatrixRow().equals(i) && cm.getMatrixCol().equals(j)) {
						ta.addCell(setCell(cm.getMatrixValue().toString(), fontCnNormal,ColoraAndb(result.getMatrixRow(),result.getMatrixCol(),i,j)));
						break;
					}
				}
			}
		}
		ta.addCell(setCell("结果", fontCnTitle, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
		ta.addCell(setCell(za43.getResult() + "", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, matrixSize + 1, null));
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, matrixSize + 2, null, 0, null));
		return ta;
	}
	
	//上边中间的表
	private Table up3() throws Exception {
		Table ta = setTableAndColumn(1,new float[]{1.0f});
    	ta.insertTable(up31());
    	ta.insertTable(up32());
		return ta;
	
	}
	//上边中间的AD表
	private Table up31() throws Exception {
		Table ta = setTableAndColumn(2,new float[]{0.5f, 0.5f});
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2, null, 0, null));
		
		ta.addCell(setCell("偶然损伤", fontCnTitle, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2, null));
			
		for (CusItemS45 cusItemS45 : cusZa43AdList) {
			ta.addCell(setCell(getStr(cusItemS45.getItemName()), fontCnNormal));
			List<CusLevel> itemList = cusLevelBo.getLevelList(ms.getModelSeriesId(), "ZA44", cusItemS45.getItemS45Id());	
			Method method;
			try {
				method = Za43.class.getMethod("getSelect" + ++flag);
				Integer object = (Integer) method.invoke(za43);
				for (CusLevel cusLevel : itemList) {
					if (cusLevel.getLevelValue().equals(object)) {
						s = cusLevel.getLevelName();
						break;
					}
				}
			} catch (Exception e) {
				s = "";
			} finally {
				ta.addCell(setCell(getStr(s), fontCnNormal));
			}
		}
		
		if (upEdAdSize - adSize > 0){
			for (int i = 0; i < upEdAdSize - adSize; i++){
				ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, null, 0, null));
				ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, null, 0, null));
			}
		}
		return ta;
	}
		
	//右边的箭头图片
	private Table up32() throws Exception {
		Table ta = setTableAndColumn(1, new float[]{1.0f});
		String str="/com/rskytech/report/word/reportArea/utils/imgs/arrow_4.3_left.png";
		InputStream stream = ReportZa43.class.getResourceAsStream(str); 
		Image img = Image.getInstance(IOUtils.toByteArray(stream));	
		stream.close();
		img = getScale(img, 400, 300);
		
		ta.addCell(setCell(img, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 3, 0, null));
		ta.addCell(setCell("", null, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, downEdAdSize - 3, 0, null));
		return ta;
	}

	//上边右边的表
	private Table up4() throws Exception {
		List<CusInterval> cusInterval = increaseRegionParamBo.searchFinalMatrix(ms.getModelSeriesId());
		
		String str = "注：\n";
		for (CusInterval cusInterval2 : cusInterval) {
			str += cusInterval2.getIntervalLevel().toString() + "=" + cusInterval2.getIntervalValue() + "\n";	
		}
		if (!"".equals(str)){
			str = str.substring(0, str.length() - 1);
		}
		
		Table ta = setTableAndColumn(3, new float[]{0.02f, 0.96f, 0.02f});
		ta.addCell(setCell("", null, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, upEdAdSize + downEdAdSize + 2, 0, null));
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, upEdAdSize + downEdAdSize + 2 - cusInterval.size() - 1 - 1, 0, null));
		ta.addCell(setCell("", null, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, upEdAdSize + downEdAdSize + 2, 0, Rectangle.RIGHT));
		
		ta.addCell(setCell(str, fontCnNormal, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, null, cusInterval.size() + 1));
		ta.addCell(setCell("", null, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, null, 0, null));
		return ta;
	}
	
	//下面的表
	private Table downTable() throws Exception {
		Table ta = setTableAndColumn(6, new float[]{0.1f, 0.1f, 0.4f, 0.1f, 0.2f, 0.1f});
		
		String x = "";
		if (task.getTaskValid() == null){
			x = "进入维修大纲";
		} else if (task.getTaskValid() == 1){
			x = "合并到";
			if (task.getDestTask() != null){
				TaskMsg tm = (TaskMsg) increaseRegionParamBo.loadById(TaskMsg.class, task.getDestTask());
				if (tm != null){
					x = x + tm.getTaskCode();
				}
			}
		} else if (task.getTaskValid() == 0){
			x = "转移到ATA20章";
		}
		
		ta.addCell(setCell("任务选择", fontCnTitle, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 2));
		
		ta.addCell(setCell("任务号", fontCnTitle));
		ta.addCell(setCell("维修任务描述", fontCnTitle));
		ta.addCell(setCell("间隔", fontCnTitle));
		ta.addCell(setCell("维修通道", fontCnTitle));
		ta.addCell(setCell("转移任务", fontCnTitle));
		
		ta.addCell(setCell(task.getTaskCode(), fontEnNormal));
		ta.addCell(setCell(getStr(task.getTaskDesc()), fontCnNormal));
		ta.addCell(setCell(getStr(task.getTaskInterval()), fontCnNormal));
		ta.addCell(setCell(getStr(task.getReachWay()), fontCnNormal));
		ta.addCell(setCell(getStr(x), fontCnNormal));
		return ta;
	}
	
	public Color isColor(Integer now, int param){
		if (now == param){
			return bgGree;
		}
		return null;
	}
	
	public Color ColoraAndb(Integer d,Integer a, int pd, int pa){
		if (a == pa && d == pd){
			return bgGree;
		}
		return null;
	}

	@Override
	public int getCol() {
		return 1;
	}

	@Override
	public float[] getColWidth() {
		return new float[]{1f};
	}

	@Override
	public String getReportName() {
		return "表5  增强区域确定间隔分析表";
	}

	@Override
	public String getTableName() {
		return "区域分析-增强区域分析";
	}
	
	@Override
	public String getTableAbbreviation() {
		return "ZA-4";
	}

}
