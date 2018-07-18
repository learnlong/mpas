package com.rskytech.util;

import com.lowagie.text.Document;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class CustomPageNumHelper extends PdfPageEventHelper{

protected PdfTemplate total;
	
	protected BaseFont helv;
	
	private String chapterName;
	private String chapterCode;
	private int startPageNum;
	
	public CustomPageNumHelper(String chapterName, String chapterCode, int startPageNum) {
		this.chapterCode = chapterCode;
		this.chapterName = chapterName;
		this.startPageNum = startPageNum;
	}
	
	public void onOpenDocument(PdfWriter writer, Document document) {
		total = writer.getDirectContent().createTemplate(300, 400);
		total.setBoundingBox(new Rectangle(-20, -20, 300, 400));
		try {
			//helv = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);// 设置中文字体
			helv = BaseFont.createFont("resource/SIMKAI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);// 设置中文字体
		}
		catch (Exception e) {
			try {
				helv = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);// 默认使用宋体
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
			throw new ExceptionConverter(e);
		}
	}
	
	public void onEndPage(PdfWriter writer, Document document) {
		PdfContentByte cb = writer.getDirectContent();
		cb.saveState();
		String text = this.chapterName;
		float textBase = document.bottom() - 1;
		int fontSize = 20;
		float textSize = helv.getWidthPoint(text, fontSize);
		cb.beginText();
		cb.setFontAndSize(helv, fontSize);
		float adjust = helv.getWidthPoint("0", fontSize);
		cb.setTextMatrix(document.right() - textSize - adjust, textBase);
		cb.showText(text);
		cb.endText();
		cb.addTemplate(total, document.right() - adjust, textBase);
		
		// ==================
		text = this.chapterCode + "-" + (writer.getPageNumber() + this.startPageNum);
		textBase = document.bottom() - 15;
		textSize = helv.getWidthPoint(text, 12);
		cb.setFontAndSize(helv, 12);
		cb.beginText();
		cb.setTextMatrix(document.right() - textSize - adjust, textBase);
		cb.showText(text);
		cb.endText();
		cb.addTemplate(total, document.right() - adjust, textBase);
		
		// ==================
		text = StringUtil.getNowDate();
		textBase = document.bottom() - 30;
		textSize = helv.getWidthPoint(text, 12);
		cb.setFontAndSize(helv, 12);
		cb.beginText();
		cb.setTextMatrix(document.right() - textSize - adjust, textBase);
		cb.showText(text);
		cb.endText();
		cb.addTemplate(total, document.right() - adjust, textBase);
		cb.restoreState();
	}
	
	/**
	 * @see com.lowagie.text.pdf.PdfPageEvent#onCloseDocument(com.lowagie.text.pdf.PdfWriter,
	 *      com.lowagie.text.Document)
	 */
	public void onCloseDocument(PdfWriter writer, Document document) {
		// total.beginText();
		// total.setFontAndSize(helv, 12);
		// total.setTextMatrix(0, 0);
		// //total.showText(String.valueOf(writer.getPageNumber() - 1));
		// total.endText();
	}
	
	
	
}
