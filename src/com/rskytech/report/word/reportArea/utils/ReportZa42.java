package com.rskytech.report.word.reportArea.utils;

import java.awt.Color;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.Za42;

public class ReportZa42 extends AreaReportBase {

	private Za42 za42;
	
	public ReportZa42(Document document, ComModelSeries ms, ComArea area, Za42 za42) {
		super(document, ms, area);
		this.za42 = za42;
	}
	
	public Table getTableContent() throws Exception {
    	Table ta = setTableAndColumn(1, null);
    	ta.insertTable(upTable());
    	ta.insertTable(downTable());
		return ta;
	}
	
	public Table upTable() throws Exception {
    	Table ta = setTableAndColumn(2, new float[]{0.32f, 0.68f});
    	ta.insertTable(upLeftTable());
    	ta.insertTable(upRightTable());
    	return ta;
    }
	
	public Table downTable() throws Exception {
		Table ta = setTableAndColumn(2, new float[]{0.5f,0.5f});
		ta.insertTable(downLeftTable());
		ta.insertTable(downRightTable());
    	return ta;
    }
	
	//上面右边的表
    public Table upRightTable() throws Exception {
    	Table ta = setTableAndColumn(1, null);
    	ta.insertTable(upRightTable1());
    	ta.insertTable(upRightTable2());
	return ta;
    }

    //上面左边的表
    public Table upLeftTable() throws Exception {
    	Table ta = setTableAndColumn(6, new float[]{0.04f, 0.23f, 0.23f, 0.23f, 0.23f, 0.02f});
    	
    	ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 11, 0, Rectangle.LEFT));
    	ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 5, 1, 0,null));
    	
    	ta.addCell(setCell("级号", fontCnTitle,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 3));
    	ta.addCell(setCell("区域大小", fontCnTitle,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 3));
    	ta.addCell(setCell("稠密度等级", fontCnTitle,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 3));
    	ta.addCell(setCell("潜在影响", fontCnTitle,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 3));
    	ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 3, 0, Rectangle.LEFT));
    	
    	ta.addCell(setCell("1", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 2));
    	ta.addCell(setCell("大", fontCnNormal, null, 2,isColor(za42.getSelect1(),1)));
    	ta.addCell(setCell("高", fontCnNormal, null, 2,isColor(za42.getSelect2(),1)));
    	ta.addCell(setCell("高", fontCnNormal, null, 2,isColor(za42.getSelect3(),1)));
    	ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 2, 0, Rectangle.LEFT));
    	
    	ta.addCell(setCell("2", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 2));
    	ta.addCell(setCell("中", fontCnNormal, null, 2,isColor(za42.getSelect1(),2)));
    	ta.addCell(setCell("中", fontCnNormal, null, 2,isColor(za42.getSelect2(),2)));
    	ta.addCell(setCell("中", fontCnNormal, null, 2,isColor(za42.getSelect3(),2)));
    	ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 2, 0, Rectangle.LEFT));
    	
    	ta.addCell(setCell("3", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 2));
    	ta.addCell(setCell("小", fontCnNormal, null, 2,isColor(za42.getSelect1(),3)));
    	ta.addCell(setCell("低", fontCnNormal, null, 2,isColor(za42.getSelect2(),3)));
    	ta.addCell(setCell("低", fontCnNormal, null, 2,isColor(za42.getSelect3(),3)));
    	ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 2, 0, Rectangle.LEFT));
    	
    	ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 5, 1, 0,null));
   
		return ta;
    }

    public Table upRightTable1() throws Exception {
    	Table ta = setTableAndColumn(3, new float[]{0.48f, 0.1f,0.42f});
    	ta.insertTable(upRightTable11());
    	ta.insertTable(upRightTable12());
    	ta.insertTable(upRightTable13());
    	return ta;
    }
    
    int areaDesity  = 0;
    public Table upRightTable11() throws Exception {
    	int areaSize  = za42.getSelect1();
    	int density = za42.getSelect2();
    	
    	Table ta = setTableAndColumn(6, new float[]{0.04f, 0.192f,  0.192f, 0.192f, 0.192f, 0.192f});
    	ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 6, 0, null));
    	ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 5, null, 0, null));
    	
    	ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE,2,2));
    	ta.addCell(setCell("区域大小", fontCnTitle, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE,3,null));
    	ta.addCell(setCell("1", fontCnNormal, isColor(areaSize,1)));
    	ta.addCell(setCell("2", fontCnNormal, isColor(areaSize,2)));
    	ta.addCell(setCell("3", fontCnNormal, isColor(areaSize,3)));
    	
    	ta.addCell(setCell("稠密度", fontCnTitle, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE,null,3));
    	ta.addCell(setCell("1", fontCnNormal, isColor(density,1)));
    	ta.addCell(setCell("1", fontCnNormal, ColorAreaSizeAndDensity(density,areaSize,1,1)));
    	ta.addCell(setCell("1", fontCnNormal, ColorAreaSizeAndDensity(density,areaSize,1,2)));
    	ta.addCell(setCell("2", fontCnNormal, ColorAreaSizeAndDensity(density,areaSize,1,3)));
    	
    	ta.addCell(setCell("2", fontCnNormal, isColor(density,2)));
    	ta.addCell(setCell("1", fontCnNormal, ColorAreaSizeAndDensity(density,areaSize,2,1)));
    	ta.addCell(setCell("2", fontCnNormal, ColorAreaSizeAndDensity(density,areaSize,2,2)));
    	ta.addCell(setCell("2", fontCnNormal, ColorAreaSizeAndDensity(density,areaSize,2,3)));

    	ta.addCell(setCell("3", fontCnNormal, isColor(density,3)));
    	ta.addCell(setCell("2", fontCnNormal, ColorAreaSizeAndDensity(density,areaSize,3,1)));
    	ta.addCell(setCell("2", fontCnNormal, ColorAreaSizeAndDensity(density,areaSize,3,2)));
    	ta.addCell(setCell("3", fontCnNormal, ColorAreaSizeAndDensity(density,areaSize,3,3)));
    	
    	if( density == 1 && areaSize == 1){
			areaDesity = 1;
		}
		if( density == 1 && areaSize == 2){
			areaDesity = 1;
		}
		if( density == 1 && areaSize == 3){
			areaDesity = 2;
		}
		if( density == 2 && areaSize == 1){
			areaDesity = 1;	
		}
		if( density == 2 && areaSize == 2){
			areaDesity = 2;
		}
		if( density == 2 && areaSize == 3){
			areaDesity = 2;
		}
		if( density == 3 && areaSize == 1){
			areaDesity = 2;
		}
		if( density == 3 && areaSize == 2){
			areaDesity = 2;
		}
		if( density == 3 && areaSize == 3){
			areaDesity = 3;
		}
    	return ta;
    }
   
    //图片箭头
	public Table upRightTable12() throws Exception{
		Table ta = setTableAndColumn(1, new float[]{1.0f});
		String str="/com/rskytech/report/word/reportArea/utils/imgs/arrow_4.2.png";
		InputStream stream = ReportZa42.class.getResourceAsStream(str); 
		Image img = Image.getInstance(IOUtils.toByteArray(stream));	
		stream.close();
		img = getScale(img,500, 300);
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, null, 0,null));
		
		ta.addCell(setCell(img, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE,null,5, null, Rectangle.LEFT));
		
    	return ta;
    }
	
    public Table upRightTable13() throws Exception{
    	int fire = za42.getSelect3();
    	
    	Table ta = setTableAndColumn(6, new float[]{0.19f, 0.19f, 0.19f, 0.19f, 0.19f,0.05f});
    	ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 5, null, 0,null));
    	ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 6, 0,Rectangle.RIGHT));
    	
    	ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE,2,2));
    	ta.addCell(setCell("潜在影响", fontCnTitle, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE,3,null));
    	ta.addCell(setCell("1", fontCnNormal, isColor(fire,1)));
    	ta.addCell(setCell("2", fontCnNormal, isColor(fire,2)));
    	ta.addCell(setCell("3", fontCnNormal, isColor(fire,3)));
    	
    	ta.addCell(setCell("区域大小/稠密度", fontCnTitle, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE,null,3));
    	ta.addCell(setCell("1", fontCnNormal, isColor(areaDesity,1)));
    	ta.addCell(setCell("1", fontCnNormal, ColorAreaSizeAndDensity(areaDesity,fire,1,1)));
    	ta.addCell(setCell("1", fontCnNormal, ColorAreaSizeAndDensity(areaDesity,fire,1,2)));
    	ta.addCell(setCell("2", fontCnNormal, ColorAreaSizeAndDensity(areaDesity,fire,1,3)));
    	
    	ta.addCell(setCell("2", fontCnNormal, isColor(areaDesity,2)));
    	ta.addCell(setCell("1", fontCnNormal, ColorAreaSizeAndDensity(areaDesity,fire,2,1)));
    	ta.addCell(setCell("2", fontCnNormal, ColorAreaSizeAndDensity(areaDesity,fire,2,2)));
    	ta.addCell(setCell("2", fontCnNormal, ColorAreaSizeAndDensity(areaDesity,fire,2,3)));

    	ta.addCell(setCell("3", fontCnNormal, isColor(areaDesity,3)));
    	ta.addCell(setCell("2", fontCnNormal, ColorAreaSizeAndDensity(areaDesity,fire,3,1)));
    	ta.addCell(setCell("2", fontCnNormal, ColorAreaSizeAndDensity(areaDesity,fire,3,2)));
    	ta.addCell(setCell("3", fontCnNormal, ColorAreaSizeAndDensity(areaDesity,fire,3,3)));
		return ta;
	}

	public Table upRightTable2()throws Exception {
		Table ta = setTableAndColumn(5, new float[]{0.02f,0.13f,0.15f,0.68f,0.02f});
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE,null,5,0, null));
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE,3,null,0, null));
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE,null,5,0, Rectangle.RIGHT));
		ta.addCell(setCell("注：", fontCnTitle,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE,null,3));
		ta.addCell(setCell("1级", fontCnNormal));
		ta.addCell(setCell("GVI区域内的所有导线，并对特定部位的导线进行单独的GVI或者DET", fontCnNormal, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE,isColor(za42.getResult(), 1)));
		ta.addCell(setCell("2级", fontCnNormal));
		ta.addCell(setCell("GVI区域内的所有导线，并对特定部位的导线进行单独的GVI", fontCnNormal, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE,isColor(za42.getResult(), 2)));
		ta.addCell(setCell("3级", fontCnNormal));
		ta.addCell(setCell("GVI区域内的所有导线", fontCnNormal, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE,isColor(za42.getResult(), 3)));
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE,3,null,0, null));
		return ta;
	}
	
	//下面左边的表
	private Table downLeftTable() throws Exception{
		Table ta = setTableAndColumn(1, new float[]{1.0f});
		String str="/com/rskytech/report/word/reportArea/utils/imgs/ZA42.png";
		InputStream stream = ReportZa42.class.getResourceAsStream(str); 
		Image img = Image.getInstance(IOUtils.toByteArray(stream));	
		stream.close();
		img = getScale(img, 350, 220);
		ta.addCell(setCell(img, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 3));
		return ta;
	}
	
	//下面右边的表
	private Table downRightTable() throws Exception {
		Table ta = setTableAndColumn(2, new float[]{0.2f, 0.8f});
		ta.addCell(setCell("步骤", fontCnTitle));
		ta.addCell(setCell("描述", fontCnTitle));
		
		ta.addCell(setCell("\n6\n", fontCnNormal));
		ta.addCell(setCell(getStr(za42.getStep6Desc()), fontCnNormal));
		
		ta.addCell(setCell("7", fontCnNormal));
		ta.addCell(setCell(getStr(za42.getStep7Desc()), fontCnNormal));
		return ta;
	}
	
	public Color isColor(Integer now, int param){
		if (now == param){
			return bgGree;
		}
		return null;
	}
	
	public Color ColorAreaSizeAndDensity(Integer d,Integer a, int pd, int pa){
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
		return "表4  增强区域分析表二";
	}

	@Override
	public String getTableName() {
		return "区域分析-增强区域分析";
	}
	
	@Override
	public String getTableAbbreviation() {
		return "ZA-3";
	}

}
