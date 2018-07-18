package com.rskytech.report.word.reportSystem.utils;

import java.util.List;

import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.rskytech.basedata.bo.IComAtaBo;
import com.rskytech.basedata.bo.IComVendorBo;
import com.rskytech.basedata.bo.impl.ComVendorBo;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComVendor;
import com.rskytech.pojo.M12;

import com.rskytech.pojo.MMain;
import com.rskytech.sys.bo.IM12Bo;

public class ReportM1 extends SystemReportBase{
	    private MMain mMain;
	    private ComModelSeries ms;
	    private String userName;
	    private IComAtaBo comAtaBo;
	    private IM12Bo m12Bo;
	    private IComVendorBo comVendorBo;
	    
		public ReportM1(Document document, ComModelSeries ms, MMain mMain,
				String userName, IComAtaBo comAtaBo, IM12Bo m12Bo,
				IComVendorBo comVendorBo) {
			super(document);
			this.mMain=mMain;
			this.userName=userName;
			this.ms=ms;
			this.comAtaBo=comAtaBo;
			this.m12Bo=m12Bo;
			this.comVendorBo=comVendorBo;
		}


		@SuppressWarnings("unused")
		@Override
		public Table getTableContent() throws Exception {
			Table ta = setTableAndColumn(getCol(),getColWidth());
			Paragraph titlet = new Paragraph("                                    X X X X X X X X"); 
			titlet.setFont(fontCnLarge); 
			document.add(titlet); 
			Paragraph title = new Paragraph("1 范围"); 
			title.setFont(rpsTitle); 
			document.add(title); 
			Paragraph titlet1 = new Paragraph("        X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X X"); 
			titlet1.setFont(fontCnNormal); 
			document.add(titlet1); 
			Paragraph titlet2 = new Paragraph("        X X X X X X X X X X"); 
			titlet2.setFont(fontCnNormal); 
			document.add(titlet2); 
			Paragraph title1 = new Paragraph("2 引用文件"); 
			title1.setFont(rpsTitle); 
			document.add(title1); 
			Paragraph title2 = new Paragraph("3　功能、布置和维修需求分析"); 
			title2.setFont(rpsTitle); 
			document.add(title2); 
			Paragraph title3 = new Paragraph("3.1　系统概述"); 
			title3.setFont(rpsTitle2); 
			document.add(title3);
			Paragraph title4 = new Paragraph("4　系统组成层次图"); 
			title4.setFont(rpsTitle); 
			document.add(title4);
			Paragraph title5 = new Paragraph("4.1　系统功能框图"); 
			title5.setFont(rpsTitle2); 
			document.add(title5);
			Paragraph title6 = new Paragraph("4.2　系统设备清单"); 
			title6.setFont(rpsTitle2); 
			document.add(title6);
			ta.addCell(setCell("表1   "+getReportName()+"系统设备清单", fontCnNormal, 
		  				Element.ALIGN_CENTER, Element.ALIGN_TOP, 11, 1, 0, null));
			ta.addCell(setCell("编码", fontCnNormal,
					Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
			ta.addCell(setCell("系统", fontCnNormal,
					Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
			ta.addCell(setCell("编码", fontCnNormal,
					Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
			ta.addCell(setCell("分系统", fontCnNormal,
					Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
			ta.addCell(setCell("编码", fontCnNormal,
					Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
			ta.addCell(setCell("设备名称", fontCnNormal,
					Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
			ta.addCell(setCell("设备型号（图号）", fontCnNormal,
					Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
			ta.addCell(setCell("单机数量", fontCnNormal,
					Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
			ta.addCell(setCell("功能", fontCnNormal,
					Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
			ta.addCell(setCell("输入", fontCnNormal,
					Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
			ta.addCell(setCell("输出", fontCnNormal,
					Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
			//取M12的集合
			String msId=mMain.getMsiId();
			List<M12> m12 = m12Bo.getM12AListByMsiId(msId);
			String quantity = null;
			for(int i = 0;i<m12.size();i++)
			{
				String ataCode = m12.get(i).getProCode();
				String proNo = m12.get(i).getPartNo();
				String proName = m12.get(i).getProName(); 
				quantity = String.valueOf(m12.get(i).getQuantity());
				String ataId = "";
				String ataName = "";
				String parentId = "";
				String parentName = "";
				String parentCode = "";
				String rootCode = "";
				String rootName = "";
				List<ComAta> li=comAtaBo.getComAtaIdByCode(ataCode);
				for(int k = 0;k<li.size();k++)
				{
					ataId=li.get(k).getAtaId();
					ataName = li.get(k).getAtaName();
		            parentId = li.get(k).getComAta().getAtaId();
		            parentCode = li.get(k).getComAta().getAtaCode();
		            parentName = li.get(k).getComAta().getAtaCode();
		            rootCode = li.get(k).getComAta().getComAta().getAtaCode();
		            rootName = li.get(k).getComAta().getComAta().getAtaName();
				}
				
//				String vendorId = m12.get(i).getVendor();//供应商id
//				String venName = comVendorBo.getVendorNameById(vendorId);
//				String venCode = comVendorBo.getVendorCodeById(vendorId);
				ta.addCell(setCell(getStr(rootCode),fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
				ta.addCell(setCell(getStr(rootName),fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
				ta.addCell(setCell(getStr(parentCode),fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
				ta.addCell(setCell(getStr(parentName),fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
				ta.addCell(setCell(getStr(ataCode),fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
				ta.addCell(setCell(getStr(proName),fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
				ta.addCell(setCell(getStr(proNo),fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
				ta.addCell(setCell(getStr(quantity),fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
				ta.addCell(setCell("",fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
				ta.addCell(setCell("",fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
				ta.addCell(setCell("",fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
			}
		  		return ta;
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
		return new float[]{0.09f,0.09f,0.09f,0.09f,0.09f,0.09f,0.09f,0.09f,0.091f,0.09f,0.09f};
	}
}
