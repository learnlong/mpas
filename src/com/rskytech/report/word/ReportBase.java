package com.rskytech.report.word;

import java.awt.Color;
import java.sql.Clob;

import org.apache.log4j.Logger;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.rtf.style.RtfFont;
import com.lowagie.text.rtf.style.RtfParagraphStyle;

public abstract class ReportBase {
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	public static BaseFont bfChinese = null;
	public static Font fontCnSmall = null; //中文普通
	public static Font fontCnNormal = null; //中文普通
	public static Font fontCnTitle = null; //中文加粗
	public static Font fontCnLarge = null; //中文大号字
	public static Font fontCnBigLarge = null; //中文特大号字
	public static Font fontEnTitle = null; //英文加粗
	public static Font fontEnItalic = null; //英文斜体
	public static Font fontEnSmall = null; //小号英文
	public static Font fontEnNormal = null; //小号英文
	public static  RtfParagraphStyle rpsTitle =null;//一级标题
	public static  RtfParagraphStyle rpsTitle2 =null;//二级标题
	public static final int TOP = 50;
	public static final int LEFT = 50;
	public static final int RIGHT = 50;
	public static final int BOTTOM  = 50;
	public static final int A4WIWTH_HORI = 842; //横向的宽度
	public static final int A4HEIGHT_HORI = 595; //横向的高度
	public static final int A4WIWTH_VERT = 595; //竖向的宽度
	public static final int A4HEIGHT_VERT = 842; //竖向的高度
	
	public static Font fontNowTitle = null; //依据当前语音判断后的加粗文字
	public static Font fontNowNormal = null; //依据当前语音判断后的普通文字
	public static Font fontNowLarge = null; //依据当前语音判断后的大号字
	public static Font fontNowBigLarge = null; //依据当前语音判断后的特大号字
	
	
	public static Color bg =  Color.green;
	public static Color bgGree =  new Color(0,255,0);
	
	protected Document document;
	
	public Boolean isTe; //是否是图文混排的特殊情况
	
	/**
	 * 定义字体
	 */
	static{		
		try{
			/*	设置标题1格式	*/ 
			 rpsTitle = RtfParagraphStyle.STYLE_HEADING_1; 
			 rpsTitle.setAlignment(Element.ALIGN_LEFT); 
			 rpsTitle.setStyle(Font.BOLD); 
			 rpsTitle.setSize(10.5f);
				/*	设置标题2格式	*/ 
			 rpsTitle2 = RtfParagraphStyle.STYLE_HEADING_2; 
			 rpsTitle2.setAlignment(Element.ALIGN_LEFT); 
			 rpsTitle2.setStyle(Font.BOLD); 
			 rpsTitle2.setSize(10.5f);
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			fontCnSmall = new Font(bfChinese, 7.5f, Font.NORMAL);
			fontCnNormal = new Font(bfChinese, 10.5f, Font.NORMAL);
			fontCnTitle = new Font(bfChinese, 10.5f, Font.BOLD);
			fontCnLarge = new Font(bfChinese, 18f, Font.BOLD);
			fontCnBigLarge = new Font(bfChinese, 40f, Font.BOLD);
			fontEnItalic = new RtfFont("Times", 10.5f, Font.ITALIC);
			fontEnTitle = new RtfFont("Times", 10.5f, Font.BOLD);
			fontEnSmall = new RtfFont("Times", 5.5f, Font.NORMAL);
			fontEnNormal = new RtfFont("Times", 10.5f, Font.NORMAL);
		}catch( Exception e ){
			e.printStackTrace();
		}
	}
	
	public ReportBase(Document document){
		this.document = document;
	}
	
	/**
	 * 设置横向A4纸张，并确定页边距
	 */
	public static void setHoriPage(Document document){
		Rectangle rectPageSize = new Rectangle(PageSize.A4);
		rectPageSize = rectPageSize.rotate();
		document.setPageSize(rectPageSize);
		document.setMargins(LEFT, RIGHT, TOP, BOTTOM);
	}
	
	/**
	 * 设置竖向A4纸张，并确定页边距
	 */
	public static void setVertPage(Document document){
		Rectangle rectPageSize = new Rectangle(PageSize.A4);
		document.setPageSize(rectPageSize);
		document.setMargins(LEFT, RIGHT, TOP, BOTTOM);
	}
	
	/**
	 * 得到页面的高度
	 * @param direction 页面的摆放类型（横向或竖向）
	 */
	public int getHeight(String direction){
		if ("VERT".equals(direction)){
			return A4HEIGHT_VERT - TOP - BOTTOM;
		} else {
			return A4HEIGHT_HORI - TOP - BOTTOM;
		}		
	}
	
	/**
	 * 得到页面的宽度
	 * @param direction 页面的摆放类型（横向或竖向）
	 */
	public int getWidth(String direction){
		if ("VERT".equals(direction)){
			return A4WIWTH_VERT - LEFT - RIGHT;
		} else {
			return A4WIWTH_HORI - LEFT - RIGHT;
		}	
	}

	/**
	 * 新增一页
	 * @param direction 页面的摆放类型（横向或竖向）
	 */
	public void newPage(String direction){
		if ("VERT".equals(direction)){
			setVertPage(document);
		} else {
			setHoriPage(document);
		}
		document.newPage();
	}
	
	/**
	 * 构建新页面中的内容
	 * @param direction 页面的摆放类型（横向或竖向）
	 * @param bool 是否是图文混排的特殊情况
	 * @param headerRow 自动换页时，重复表头的行数
	 */
	public void generate(String direction, Boolean bool, Integer headerRow){
		try {		
			Table table = setTableAndColumn(1, null);
			
			if (headerRow != null){
				table.setLastHeaderRow(headerRow);
			}
			
			this.isTe = bool;
			if (isTe){
				table.setSpacing(0.1f);
			}

			Table tableTop = this.getTableTop();
			if (tableTop != null){
				table.insertTable(tableTop);
			}	

			Table tableContent = this.getTableContent();
			if (tableContent != null){
				table.insertTable(tableContent);
			}
			
			Table tableBottom = this.getTableBottom();
			if (tableBottom != null){
				table.insertTable(tableBottom);
			}
			
			document.add(table);
			
			newPage(direction);	
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 构建MPD报表中的内容
	 * @param direction 页面的摆放类型（横向或竖向）
	 * @param bool 是否是图文混排的特殊情况
	 */
	public void generateMpd(String direction, Boolean bool){
		try {		
			Table table = setTableAndColumn(1, null);
			
			this.isTe = bool;
			if (isTe){
				table.setSpacing(0.1f);
			}

			Table tableContent = this.getTableContent();
			if (tableContent != null){
				table.insertTable(tableContent);
			}
			
			document.add(table);
			
			newPage(direction);	
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 构建表格的列，以及每列的宽度
	 * @param columnNum 列数
	 * @param columnWidth 每列的宽度（百分比）
	 */
	public Table setTableAndColumn(int columnNum, float[] columnWidth){
		Table table = null;
		try{
			table =  new Table(columnNum);
			if (columnWidth != null){
				table.setWidths(columnWidth);
			} else {
				table.setWidth(100f);
			}
		}catch( Exception e ){
			e.printStackTrace();
		}
		return table;
	}
	
	/**
	 * 得到当前报告的名称
	 */
	public abstract String getReportName();
	
	/**
	 * 得到当前表格的全名
	 */
	public abstract String getTableName();
	
	/**
	 * 得到当前表格的简称
	 */
	public abstract String getTableAbbreviation();
	
	/**
	 * 得到当前表格的列数
	 */
	public abstract int getCol();
	
	/**
	 * 得到当前表格每列的宽度（百分比）
	 */
	public abstract float[] getColWidth();
	
	/**
	 * 构建表格的表头
	 */
	public abstract Table getTableTop() throws Exception;
	
	/**
	 * 构建表格的正文
	 */
	public abstract Table getTableContent() throws Exception;
	
	/**
	 * 构建表格的表底
	 */
	public abstract Table getTableBottom() throws Exception;
	
	
	
	
	/** --------------------- 下面是做表格时经常用到的方法 --------------------- */
	
	
	/**
	 * 设置单元格，最基本样式： 上下、左右居中
	 * @param str 单元格内容
	 * @param font 单元格字体
	 */
	public Cell setCell(String str, Font font) throws Exception{
		Cell cell = new Cell(new Phrase(str, font));			
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		if (isTe) cell.enableBorderSide( Rectangle.RIGHT);
		return cell;
	}
	
	/**
	 * 设置单元格，可设定单元格的颜色，其他是最基本样式： 上下、左右居中
	 * @param str 单元格内容
	 * @param font 单元格字体
	 * @param bg 单元格背景色
	 */
	public Cell setCell(String str, Font font, Color bg) throws Exception{
		Cell cell = new Cell(new Phrase(str, font));	
		if (isTe) cell.enableBorderSide( Rectangle.RIGHT);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		if (bg != null) cell.setBackgroundColor(bg);
		return cell;
	}
	
	/**
	 * 设置单元格，可设定单元格的颜色，其他是最基本样式： 上下、左右居中
	 * @param str 单元格内容
	 * @param font 单元格字体
	 * @param colspan 合并单元格的列数
	 * @param rowspan 合并单元格的行数
	 * @param bg 单元格背景色
	 */
	public Cell setCell(String str, Font font, Integer colspan, Integer rowspan, Color bg) throws Exception{
		Cell cell = new Cell(new Phrase(str, font));	
		if (isTe) cell.enableBorderSide( Rectangle.RIGHT);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		if (colspan != null) cell.setColspan(colspan);
		if (rowspan != null) cell.setRowspan(rowspan);
		if (bg != null) cell.setBackgroundColor(bg);
		return cell;
	}
	
	/**
	 * 设置单元格，可设定上下、左右的样式
	 * @param str 单元格内容
	 * @param font 单元格字体
	 * @param hori 横向样式
	 * @param vert 竖向样式
	 */
	public Cell setCell(String str, Font font, int hori, int vert) throws Exception{
		Cell cell = new Cell(new Phrase(str, font));			
		cell.setHorizontalAlignment(hori);
		cell.setVerticalAlignment(vert);
		if (isTe) cell.enableBorderSide( Rectangle.RIGHT );
		return cell;
	}
	/**
	 * 设置单元格，可设定上下、左右的样式
	 * @param str 单元格内容
	 * @param font 单元格字体
	 * @param hori 横向样式
	 * @param vert 竖向样式
	 * @param bg 单元格背景色
	 */
	public Cell setCell(String str, Font font, int hori, int vert, Color bg) throws Exception{
		Cell cell = new Cell(new Phrase(str, font));			
		cell.setHorizontalAlignment(hori);
		cell.setVerticalAlignment(vert);
		if (isTe) cell.enableBorderSide( Rectangle.RIGHT);
		if (bg != null) cell.setBackgroundColor(bg);
		return cell;
	}
	
	/**
	 * 设置单元格，可设定上下、左右的样式，合并单元格
	 * @param str 单元格内容
	 * @param font 单元格字体
	 * @param hori 横向样式
	 * @param vert 竖向样式
	 * @param colspan 合并单元格的列数
	 * @param rowspan 合并单元格的行数
	 */
	public Cell setCell(String str, Font font, int hori, int vert, Integer colspan, Integer rowspan) throws Exception{
		Cell cell = new Cell(new Phrase(str, font));
		if (isTe) cell.enableBorderSide( Rectangle.RIGHT);
		cell.setHorizontalAlignment(hori);
		cell.setVerticalAlignment(vert);
		if (colspan != null) cell.setColspan(colspan);
		if (rowspan != null) cell.setRowspan(rowspan);
		return cell;
	}
	
	/**
	 * 设置单元格，可设定上下、左右的样式，合并单元格，单元格是否显示边框线， 调整单元格边框显示线
	 * @param str 单元格内容
	 * @param font 单元格字体
	 * @param hori 横向样式
	 * @param vert 竖向样式
	 * @param colspan 合并单元格的列数
	 * @param rowspan 合并单元格的行数
	 * @param border 单元格是否显示边框线（NULL和小于0的数显示边框线，等于0时不显示边框线）
	 * @param borderSide 单元格边框显示线（Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT | Rectangle.LEFT）
	 */
	public Cell setCell(String str, Font font, int hori, int vert, Integer colspan, Integer rowspan, Integer border, Integer borderSide) throws Exception{
		Cell cell = new Cell(new Phrase(str, font));		
		cell.setHorizontalAlignment(hori);
		cell.setVerticalAlignment(vert);
		if (colspan != null) cell.setColspan(colspan);
		if (rowspan != null) cell.setRowspan(rowspan);
		if (border != null) cell.setBorder(border);
		if (borderSide != null) cell.enableBorderSide(borderSide);
		return cell;
	}
	
	/**
	 * 设置单元格，可插入图片，设定上下、左右的样式，合并单元格
	 * @param img 图片
	 * @param hori 横向样式
	 * @param vert 竖向样式
	 * @param colspan 合并单元格的列数
	 * @param rowspan 合并单元格的行数
	 * @param border 单元格是否显示边框线（NULL和小于0的数显示边框线，等于0时不显示边框线）
	 * @param borderSide 单元格边框显示线（Rectangle.BOTTOM | Rectangle.TOP | Rectangle.RIGHT | Rectangle.LEFT）
	 */
	public Cell setCell(Image img, int hori, int vert, Integer colspan, Integer rowspan, Integer border, Integer borderSide) throws Exception{
		Cell cell = new Cell(img);		
		cell.setHorizontalAlignment(hori);
		cell.setVerticalAlignment(vert);
		if (colspan != null) cell.setColspan(colspan);
		if (rowspan != null) cell.setRowspan(rowspan);
		if (border != null) cell.setBorder(border);
		if (borderSide != null) cell.enableBorderSide(borderSide);
		cell.setLeading(2f);
		if (isTe) cell.enableBorderSide( Rectangle.RIGHT);
		return cell;
	}
	/**
	 * 设置单元格，可插入图片，设定上下、左右的样式，合并单元格
	 * @param img 图片
	 * @param hori 横向样式
	 * @param vert 竖向样式
	 * @param colspan 合并单元格的列数
	 * @param rowspan 合并单元格的行数
	 */
	public Cell setCell(Image img, int hori, int vert, Integer colspan, Integer rowspan) throws Exception{
		Cell cell = new Cell(img);		
		cell.setHorizontalAlignment(hori);
		cell.setVerticalAlignment(vert);
		if (colspan != null) cell.setColspan(colspan);
		if (rowspan != null) cell.setRowspan(rowspan);
		cell.setLeading(2f);
		if (isTe) cell.enableBorderSide( Rectangle.RIGHT);
		return cell;
	}
	
	/**
	 * 依据最大长、宽，压缩图片
	 * @param image 图片
	 * @param width 最大宽度
	 * @param height 最大高度
	 */
	public static Image getScale(Image image, float width, float height){
		if (image.getWidth() > width){
			float w = width / image.getWidth();
			image.scalePercent(w * 100);
		}
		if (image.getHeight() > height){
			float h = height / image.getHeight();
			image.scalePercent(h * 100);
		}
		if (image.getScaledWidth() > width + 1 && image.getWidth() > width){
			float w = width / image.getWidth();
			float mW = image.getWidth() * w;
			float mH = image.getHeight() * w;
			image.scaleAbsolute(mW - 2, mH - 2);
		}
		if (image.getScaledHeight() > height + 1 && image.getHeight() > height){
			float h = height / image.getHeight();
			float mW = image.getWidth() * h;
			float mH = image.getHeight() * h;
			image.scaleAbsolute(mW - 2, mH - 2);
		}
		return image;
	}
	
//	/**
//	 * 替换所有的换行符为空
//	 * @param str 需要替换的文字
//	 */
//	public String replaceAllR(String str){
//		if (str != null){
//			return str.replaceAll("\r", "");			
//		} else {
//			return str;
//		}		
//	}
	
	/**
	 * 为String去除空和null的字样，并替换所有的换行符为空
	 * @param s 字符串
	 */
	public String getStr(String s){
		return (s == null || "null".equals(s)) ? "" : s.replaceAll("\r", "");
	}
	
	/**
	 * 为Object去除空和null的字样，并替换所有的换行符为空
	 * @param s 字符串
	 */
	public String getStr(Object s){
		return (s == null || "null".equals(s.toString())) ? "" : s.toString().replaceAll("\r", "");
	}
	
	/**
	 * 为Integer去除空
	 * @param i Integer字符
	 */
	public String getStr(Integer i){
		return i == null ? "" : i.toString();
	}
	
	
	/**
	 * 通过Integer字符的1、0、-1或2，判断输出“是”、“否”或“N/A”的字样
	 * @param i Integer字符（允许输入NULL、1、0、-1、2）
	 */
	public String getYesOrNo(Integer i){
		String s = "";
		if (i == null){
			return s;
		} else if (i == 1){
			s = "是";
		} else if (i == 0){
			s = "否";
		} else if (i == 2 || i == -1){
			s = "N/A";
		}
		return s;
	}
	
	/**
	 * 转换CLOB为STRING
	 * @param clob Clob类型
	 */
	public String getStrByClob(Clob clob) throws Exception {
		if (clob == null){
			return null;
		}
		return clob.getSubString((long)1, (int)clob.length());
	}
	
	/**
	 * 将字符串转换为指定字体的样式
	 * @param str
	 * @param font 转换成的样式
	 * @param f 字符串偏移的位置 f>0 往上偏移该数值 <o往下偏移指定的数值,为0时不进行偏移操作
	 * @return
	 */
	public String chunkStr(String str,Font font,float f){
		 Chunk chunk = new Chunk(str);
		 chunk.setFont(font);
		 if(f!=0){
			 chunk.setTextRise(f);
		 }
		return chunk.toString();
	}
}
