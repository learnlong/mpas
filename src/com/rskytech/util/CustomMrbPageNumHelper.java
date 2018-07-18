package com.rskytech.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class CustomMrbPageNumHelper extends PdfPageEventHelper{

	PdfContentByte pdfContendByte;
	BaseFont baseFont;
	public PdfTemplate pdfTemplate;  
	float width;
	float height;
	int startPageNum;
	public CustomMrbPageNumHelper(float width, float height,int startPageNum){
		this.width = width;
		this.height= height;
		this.startPageNum = startPageNum;
	}

	
	@Override
	public void onOpenDocument(PdfWriter pdfWriter, Document document) {
		pdfContendByte = pdfWriter.getDirectContent();  
		pdfTemplate =   pdfContendByte.createTemplate(100, 100);  
		try{
			baseFont = BaseFont.createFont("resource/SIMKAI.TTF", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
		}
		catch( Exception e){	
			
		}
		super.onOpenDocument(pdfWriter, document);
	}

	
	//只有一页写到尾部了才能触发，第二页开始时触发第一页的onEndPage事件
	@Override
	public void onEndPage(PdfWriter pdfWriter, Document document){
		int pageN = pdfWriter.getPageNumber();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String text = null;
		if(startPageNum<=0){
			text = sdf.format(new Date());
		}else{
			text = sdf.format(new Date()) + "                                               "+(startPageNum+pageN);
		}
		pdfContendByte.beginText();
		pdfContendByte.setFontAndSize(baseFont, 12.5f);
		pdfContendByte.setTextMatrix(60, document.bottom()-13.5f);
		pdfContendByte.showText(text);
		pdfContendByte.endText();
		pdfContendByte.addTemplate(pdfTemplate, width - 70, document.bottom()-13.5f);
	}
	
	@Override
	public void onCloseDocument(PdfWriter pdfWriter, Document document) {
		pdfTemplate.beginText();
		pdfTemplate.setFontAndSize(baseFont, 10.5f);
		pdfTemplate.showText(String.valueOf(" "));
		pdfTemplate.endText();
		super.onCloseDocument(pdfWriter, document);
	}
	
}
