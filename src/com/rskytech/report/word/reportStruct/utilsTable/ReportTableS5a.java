package com.rskytech.report.word.reportStruct.utilsTable;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Table;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.S5;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.SStep;
import com.rskytech.struct.bo.IS5Bo;

public class ReportTableS5a extends StructReportBase{
	
	private String ssiCode;
	private String ssiName;
	private ComModelSeries ms;
	private String areaCode;
	private Font fontNow;
	private IS5Bo s5Bo;
	private List<SStep> sStepList;
	
	public ReportTableS5a(Document document, ComModelSeries ms, String ssiCode, String ssiName, String areaCode, IS5Bo s5Bo, List<SStep> sStepList) {
		super(document);
		this.ssiCode=ssiCode;
		this.ssiName=ssiName;
		this.ms=ms;
		this.areaCode=areaCode;
		this.s5Bo=s5Bo;
		this.sStepList=sStepList;
	}

		@Override
		public String getReportName() {
			return getStr(ms.getModelSeriesName());
		}

		@Override
		public int getCol() {
			return 1;
		}

		@Override
		public float[] getColWidth() {
			return new float[]{1.0f};
		}

		@Override
		public Table getTableContent() throws Exception {
			Table ta = setTableAndColumn(getCol(), getColWidth());
			ta.insertTable(getTableTitle());
			ta.insertTable(getTableCenter());
			return ta;
		}

		@SuppressWarnings("unchecked")
		public Table getTableCenter() throws Exception{
			int total =  getTaCenterCols();
			float[] f = new float[total]; 
			if(total>35){
				fontNow = fontCnSmall;
				for(int i =0;i<total;i++){
					if(total>6){
						if(i<5||i==total-1){
							f[i] = 0.03f;
						}else{
							f[i]=0.82f/(total-6);
						}
					}else{
						f[i]=1.0f/total;
					}
				}
			}else{
				fontNow = fontCnNormal;
				for(int i =0;i<total;i++){
					if(total>6){
						if(i<5||i==total-1){
							f[i] = 0.05f;
						}else{
							f[i]=0.7f/(total-6);
						}
					}else{
						f[i]=1.0f/total;
					}
				}
			}
			Table tac = setTableAndColumn(total, f);
			tac.addCell(setCell("项目标识码", fontNow, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
			tac.addCell(setCell("项目名称", fontNow, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
			tac.addCell(setCell("内部或外部项目", fontNow, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 2, 1));
			tac.addCell(setCell("SSI的材料", fontNow, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
			
			List<Object[]> listItemNames = s5Bo.getItemName("0", ms.getModelSeriesId(), "A");
			List<Object> itemIdList = new ArrayList<Object>();
			if(listItemNames!=null){
				int rows =0;
				List<Object[]> listItemNames1;
				for(Object[] obj : listItemNames){
					listItemNames1 = s5Bo.getItemName(obj[1].toString(), ms.getModelSeriesId(), "A");
					if(listItemNames1!=null){
						for(Object[] obj1 : listItemNames1){
							rows=s5Bo.getLevelCount(obj1[1].toString(), ms.getModelSeriesId(), "A").size();
							tac.addCell(setCell(getStr(obj1[0]), fontNow, Element.ALIGN_CENTER, Element.ALIGN_CENTER, rows, 1));
							itemIdList.add(obj1[1]);
						}
					}
					tac.addCell(setCell(getStr(obj[0]), fontNow, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
				}
				tac.addCell(setCell("偶然损伤级号ADR", fontNow, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2));
				
				tac.addCell(setCell("内部", fontNow, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1));
				tac.addCell(setCell("外部", fontNow, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1));
				List<Object[]> levelList;
				for(int t= 0;t<itemIdList.size();t++){
					levelList = s5Bo.getLevelCount(itemIdList.get(t).toString(), ms.getModelSeriesId(), "A");
					if(levelList!=null){
						for(Object[] objLevel : levelList){
							tac.addCell(setCell(getStr(objLevel[0]), fontNow, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1));
						}
					}
				}
				
				if (sStepList != null){
					for (SStep sStep : sStepList){
						if (sStep.getSStepId() == null){
							continue;
						}
						
						String code = "";
						SMain sMain = sStep.getSMain();
						if (sMain.getIsAdd() == null){
							code = sMain.getComAta().getAtaCode();
						} else {
							code = sMain.getAddCode();
						}
						
						List<S5> s5List = s5Bo.getS5BySsiId(sMain.getSsiId(), 2);
						Method method;
						Method method2;
						Double object2 = null ;
						if (s5List != null){
							for (S5 s5 : s5List){
								S1 s1 = s5.getS1();
								
								if(s1.getIsMetal()==1){
									tac.addCell(setCell(code, fontNow));
									tac.addCell(setCell(getStr(s1.getSsiForm()), fontNow));
									if(s5.getInOrOut()!=null&&"IN".equals(s5.getInOrOut())){
										tac.addCell(setCell("√", fontEnTitle));
										tac.addCell(setCell("", fontNow));
									}else if(s5.getInOrOut()!=null&&"OUT".equals(s5.getInOrOut())){
										tac.addCell(setCell("", fontNow));
										tac.addCell(setCell("√", fontEnTitle));
									}
									tac.addCell(setCell(getStr(s1.getMaterial()), fontNow));
									int t=0;
									int cou=0;
									for(Object[] obj : listItemNames){
										t++;
										listItemNames1 = s5Bo.getItemName(obj[1].toString(), ms.getModelSeriesId(), "A");
										if(listItemNames1!=null){
											for(Object[] obj1 : listItemNames1){
												cou++;
												method = S5.class.getMethod("getSelect" + cou);
												Integer object = (Integer) method.invoke(s5);
												List<Object[]> levelLists = s5Bo.getLevelCount(obj1[1].toString(), ms.getModelSeriesId(), "A");
												if(levelLists!=null){
													for(Object[] lev :levelLists){
														if(((BigDecimal) lev[2]).intValue()==object){
															tac.addCell(setCell("√", fontEnTitle));
														}else{
															tac.addCell(setCell("", fontNow));
														}
													}
												}else{
													System.out.println(obj1[1].toString());
												}
											}
											int n=24 - listItemNames.size() + t;
											method2 = S5.class.getMethod("getSelect" + n);
											object2 = (Double) method2.invoke(s5);
											tac.addCell(setCell(getStr(object2), fontNow));
										}
									}
									tac.addCell(setCell(getStr(s5.getAdr()), fontNow));
								}
							}
						}
					}
				}
			}
			return tac;
		}
		
		public Table getTableTitle() throws Exception{
			Table taTitle = setTableAndColumn(8, new float[]{0.05f,0.15f,0.05f,0.25f,0.05f,0.15f,0.1f,0.2f});
			taTitle.addCell(setCell("表9   重要结构项目偶然损伤评级表（金属）", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 8, 1, 0, null));
			taTitle.addCell(setCell("飞机型号："+getReportName()+"                                          专业：                      " +
					"                                    共  页，第  页", fontCnNormal,Element.ALIGN_LEFT, Element.ALIGN_CENTER, 8, 1, 0, null));
			taTitle.addCell(setCell("部件名称", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1));
			taTitle.addCell(setCell(ssiName, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1));
			taTitle.addCell(setCell("图号", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1));
			taTitle.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1));
			taTitle.addCell(setCell("标识码", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1));
			taTitle.addCell(setCell(ssiCode, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1));
			taTitle.addCell(setCell("区域号", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1));
			taTitle.addCell(setCell(areaCode, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1));
			return taTitle;
		}
		
		@SuppressWarnings("unchecked")
		public int getTaCenterCols(){
			int totalCount=0;
			List<Object[]> listItemNames = s5Bo.getItemName("0", ms.getModelSeriesId(), "A");
			if(listItemNames!=null){
				for(Object[] obj : listItemNames){
					totalCount += s5Bo.getItemCount(obj[1].toString(), ms.getModelSeriesId(), "A");
				}
			}
			int itemCount = listItemNames==null?0:listItemNames.size();
			totalCount = 6+totalCount+itemCount;
			return totalCount;
		}
}

