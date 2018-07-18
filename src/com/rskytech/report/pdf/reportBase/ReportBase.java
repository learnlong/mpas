package com.rskytech.report.pdf.reportBase;

import org.apache.log4j.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public abstract class ReportBase {

protected Logger logger = Logger.getLogger(this.getClass());
	
	public static Font fontEnTitle = null;//new RtfFont("Times", 10.5f, Font.BOLD);
	public static Font fontEnNormal = null;//new RtfFont("Times", 10.5f, Font.NORMAL);
	public static Font fontEnS4OrS5Normal = null;//new RtfFont("Times", 10.5f, Font.NORMAL);
	public static BaseFont bfChinese = null;
	public static Font fontCnNormal = null;
	public static Font fontS4OrS5Normal = null;
	public static Font fontCnTitle = null;
	public static Font fontCnTitleSmall = null;
	
	public static final int TOP = 55;
	public static final int LEFT = 55;
	public static final int RIGHT = 55;
	public static final int BOTTOM  = 55;
	public static final int A4WIWTH = 842;
	public static final int A4HEIGHT = 595;
	
	public static BaseColor bg =  new BaseColor(0,255,0);
	public static BaseColor bgGree =  new BaseColor(0,255,0);
	
	static{		
		fontEnTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10.5f, Font.BOLD);		
		fontEnNormal = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10.5f, Font.NORMAL);
		fontEnS4OrS5Normal = FontFactory.getFont(FontFactory.TIMES_ROMAN, 7.5f, Font.NORMAL);
		try{
			bfChinese = BaseFont.createFont("resource/SIMKAI.TTF", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
			fontCnNormal = new Font(bfChinese, 10.5f, Font.NORMAL);
			fontS4OrS5Normal = new Font(bfChinese, 7.5f, Font.NORMAL);
			fontCnTitle = new Font(bfChinese, 10.5f, Font.BOLD);
			fontCnTitleSmall = new Font(bfChinese, 7f, Font.NORMAL);
		}catch( Exception e ){
			e.printStackTrace();
		}
	}
	
	protected Document document;
	
	
	public ReportBase(Document document){
		this.document = document;
	}
	
	public static void setPage(Document document){
		Rectangle rectPageSize = new Rectangle(PageSize.A4);
		rectPageSize = rectPageSize.rotate();
		document.setPageSize(rectPageSize);
		document.setMargins(LEFT, RIGHT, TOP, BOTTOM);
	}

	public void newPage(){
		document.newPage();
		setPage(document);
	}
	
	public int getHeight(){
		return A4HEIGHT - TOP - BOTTOM;
	}
	
	public int getWidth(){
		return A4WIWTH - LEFT -  RIGHT;
	}
	
	public abstract PdfPTable getContentTotalTable();
	
	public abstract int getCol();
	
	public abstract PdfPTable getHeadTable();
	
	public void addBottom(){
	}
	
	public void addTop(){
	}
	
	public void generate(){
		try{
			newPage();
			
			addTop();
			
			PdfPTable table = getContentTotalTable();
			
			table.setWidthPercentage(100);
			table.setSplitLate(false);   
			table.setSplitRows(true);  

		
			PdfPTable headTable =  this.getHeadTable();
			if( headTable != null ){
				PdfPCell cell = new PdfPCell(headTable);
				cell.setColspan( this.getCol() );
				table.addCell( cell );
			}
			
			generate( table );
			document.add( table );
			addBottom();
			
		}catch( Exception e){
			e.printStackTrace();
			logger.debug( e );
		}
	}
	
	public abstract void generate(PdfPTable table);
	
	public abstract String getTitle();
	
	public abstract String getTableTitle();
	
	public abstract String getTableName();
	
	public void setHeadTableWidths(PdfPTable table){
		
	}
}
