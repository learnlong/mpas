package com.rskytech.report.word.reportSystem.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;


import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;

import com.rskytech.basedata.bo.IComAtaBo;

import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.M0;
import com.rskytech.pojo.MMain;
import com.rskytech.pojo.MSet;
import com.rskytech.pojo.MSetF;

import com.rskytech.sys.bo.IM0Bo;
import com.rskytech.sys.bo.IMSetBo;
import com.rskytech.util.StringUtil;

public class ReportM131 extends SystemReportBase {
    private MMain mMain;
    private ComModelSeries ms;
    private String userName;
    private List<M0> list;
    @SuppressWarnings("unused")
	private IM0Bo m0Bo;
    private IComAtaBo comAtaBo;
    private IMSetBo  mSetBo;
  
	public ReportM131(Document document,ComModelSeries ms,MMain mMain,
			String userName,List<M0> list,IM0Bo m0Bo,
			IComAtaBo comAtaBo,IMSetBo mSetBo
			) {
		super(document);
		this.mMain=mMain;
		this.userName=userName;
		this.ms=ms;
		this.list=list;
		this.m0Bo=m0Bo;
		this.comAtaBo=comAtaBo;
	    this.mSetBo=mSetBo;
	    
	    
	}
    
	@SuppressWarnings("unused")
	@Override
	public Table getTableContent() throws Exception {
		
		Table ta1 = setTableAndColumn(getCol(),getColWidth());
		Paragraph title = new Paragraph("6　系统概述及RCMA分析"); 
		title.setFont(rpsTitle); 
		document.add(title); 
		Paragraph title1 = new Paragraph("6.1  "+getReportName()+"分系统"); 
		title1.setFont(rpsTitle2); 
		document.add(title1); 
		Paragraph title2 = new Paragraph("6.2　RCMA分析"); 
		title2.setFont(rpsTitle2); 
		document.add(title2); 
		
		  ta1.addCell(setCell("     "+getReportName()+"分系统功能、功能故障描述见下表。", fontCnNormal, 
	  				Element.ALIGN_LEFT, Element.ALIGN_TOP, 5, 1, 0, null));
	  		ta1.addCell(setCell("表3  "+getReportName()+"分系统功能/功能故障描述表格", fontCnNormal,
	  				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 5,2, 0, null));
	  		Table ta3 = setTableAndColumn(1,null);
			ta3.insertTable(ta1);
		      String msId=ms.getModelSeriesId();
		      String ProCode="";
		      String ataCode="";
		      ComAta comAta=null;
		      String functionCode="";
		      String failureCode="";
		      String failureDesc="";
		      document.add(ta3);
		      ataCode=   mMain.getComAta().getAtaCode();
		      for(int k=0;k<list.size();k++)
		      {   M0 m0=list.get(k);
		    	  Table ta = setTableAndColumn(getCol(), getColWidth());
		    	  //项目编号
		    	  ProCode=getStr(m0.getProCode());
		    	  //初始约定层次就是msi名称
		          comAta=comAtaBo.getComAtaByAtaCode(m0.getProCode(), msId);
		          ta.addCell(setCell("初始约定层次："+ataCode, fontCnNormal,
			  				Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 2,1, null, null));
			  		ta.addCell(setCell("系统：", fontCnNormal,
			  				Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 2,1, null, null));
			  		ta.addCell(setCell("分析人员："+userName, fontCnNormal,Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 1,1, null, null));
			  		ta.addCell(setCell("分析对象名称/编号："+ProCode, fontCnNormal,
			  				Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 4,1, null, null));
			  		ta.addCell(setCell("分析时间："+StringUtil.formatDate(mMain.getModifyDate(), "yyyy/MM/dd"), fontCnNormal,Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 1,1, null, null));
			  		
		        if(comAta!=null){
		        	  //遍历获取所有ata
		              List<Object> li=this.comAtaBo.getSelfAndChildAta(msId,comAta.getAtaId());
			      		ta.addCell(setCell("功能编号", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2,1, null, null));
				  		ta.addCell(setCell("功能故障编号", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
				  		ta.addCell(setCell("功能/功能故障描述", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2,1, null, null));
		              for (int i = 0; i < li.size(); i++) {
		            	  ComAta ataTemp=(ComAta) this.mSetBo.getDao().loadById(ComAta.class, li.get(i).toString());
		            	  List<MSet> listMset = this.mSetBo.getMsetListByMsiIdAndAtaId(mMain.getMsiId(), ataTemp.getAtaId());
			              for (MSet mSet : listMset) {
			      				//遍历功能
			            		  ta.addCell(setCell( getStr(mSet.getFunctionCode()), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2,1, null, null));
				      				List<MSetF> setf = mSetBo.getMsetfListByMsetId(mSet.getMsetId());
//			      		             if(setf.size()==0){
//			      		            	ta.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
//					      		  		ta.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2,1, null, null));
//				      		  		
//						      		       }else{
//						      					   
						      					  for (int j = 0; j< setf.size(); j++  ) {
						      						//遍历功能故障
						      						MSetF mSetF = setf.get(j);
						      						if(j>0){
						      							    ta.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2,1, null, null));
						      						       }
								      						failureCode= getStr(mSetF.getFailureCode());
								      						failureDesc=getStr(mSetF.getFailureDesc());
								      						ta.addCell(setCell(failureCode, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
										      		  		ta.addCell(setCell(failureDesc, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2,1, null, null));
						      					    }
//			      		                         }
		      			         }
		                  }
		           }
		        
			       if(list.size() ==(k+1)){
			    	   ta1 =    ta;
			       }else{
			    	   document.add(ta);  
			       }
		        }
		
		return ta1;
	}
     
	
	@Override
	public String getReportName() {
		return getStr(ms.getModelSeriesName());
	}

	@Override
	public int getCol() {
		return 5;
	}

	@Override
	public float[] getColWidth() {
//		return new float[]{0.2f,0.2f,0.2f,0.2f,0.2f};
		return null;
	}
}
