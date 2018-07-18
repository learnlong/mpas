package com.rskytech.util;

import java.util.HashMap;
import java.util.List;

import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.CusMatrix;

public class MatrixUtil {

	/**
	 * 将副矩阵数据转换为html
	 * @param matrixList 矩阵数据
	 * @param matrixFlg 矩阵区分flg
	 * @return 
	 */
	public static String generateHtmlMatrixData(List<CusMatrix> matrixList,Integer matrixFlg){
		String rowNm;
		String colNm;
		
		// 因为list是按行号列号升序排过的，所以list中最后一个矩阵单元对象的行号列号肯定是最大的
		Integer rowCount = matrixList.get(matrixList.size() - 1).getMatrixRow();
		Integer colCount = matrixList.get(matrixList.size() - 1).getMatrixCol(); 
		
		
			rowNm = returnRowTitle(matrixList.get(0).getMatrixRowName());
			colNm = matrixList.get(0).getMatrixColName();
	
		
		StringBuffer sb = new StringBuffer();		
		sb.append("<div id='matrixDiv" + matrixFlg + "' class='matrixDiv'>");
		sb.append("<table id='matrixTable" + matrixFlg + "' class='matrixTable'>");
        // 添加行头
		sb.append("<tr><td rowspan='2' colspan='2' class='blankCell'></td>");
	    sb.append("<td id='rowTitle' colspan='" + rowCount + "' class='rowTitle'><b>" + colNm + "</b></td>");
	    sb.append("</tr>");
		sb.append("<tr>");	    
	    for (int i = 1 ; i <= colCount ; i++){
	        sb.append("<td class='cellLevel'><b>" + i + "</b></td>");	
	    }	   
	    sb.append("</tr>");
	    
	    // 添加列头和第一行
	    sb.append("<tr><td id='colId' rowspan='" + (rowCount + 1) + "' class='cellTitle'><b>"+ rowNm +"</b></td>");
		
	    if (matrixFlg.equals(ComacConstants.FIRST_MATRIX)) {// 第一矩阵的情况下
			sb.append("<td class='rowLevel'><b>1</b></td>");
		}else if (matrixFlg.equals(ComacConstants.SECOND_MATRIX)){// 第二矩阵的情况下
			sb.append("<td class='rowLevel'><b>" + returnRowSerialTitle(matrixList.get(0).getMatrixRowName()) + "</b></td>");
		}
	
        for(int i = 1 ; i <= colCount; i++){
        	String value = (matrixList.get(0).getMatrixValue() == null) ? BasicTypeUtils.EMPTY_STR : matrixList.get(0).getMatrixValue().toString();
            sb.append("<td class='cell'><input type='text' name='" + matrixFlg + "fmCell' id='" + matrixFlg + "fmCell" + matrixList.get(0).getMatrixId() + "' value='" + value + "' class='inputTxt'/></td>");        	
            matrixList.remove(0);
        }
        sb.append("</tr>");
	 
        // 添加剩下的行
        for(int i = 2 ; i <= rowCount; i++){
        	if (matrixFlg.equals(ComacConstants.FIRST_MATRIX)) {// 第一矩阵的情况下
				sb.append("<tr><td class='rowLevel'><b>" + i + "</b></td>");
			} else if(matrixFlg.equals(ComacConstants.SECOND_MATRIX)) {// 第二矩阵的情况下
				sb.append("<tr><td class='rowLevel'><b>" + returnRowSerialTitle(matrixList.get(0).getMatrixRowName()) + "</b></td>");
			}
        	
            for(int j = 1 ; j <= colCount; j++){
            	String value = (matrixList.get(0).getMatrixValue() == null) ? BasicTypeUtils.EMPTY_STR : matrixList.get(0).getMatrixValue().toString();
            	sb.append("<td class='cell'><input type='text' name='" + matrixFlg + "fmCell' id='" + matrixFlg + "fmCell" + matrixList.get(0).getMatrixId() +"' value='" + value + "' class='inputTxt'/></td>");
                matrixList.remove(0);
            }
            sb.append("</tr>");
        }
        sb.append("</table></div>");
        return sb.toString();
	}
	
	/**
	 * 将za43的检查间隔矩阵数据转换为html
	 * @param za43IntervalList
	 * @param language
	 * @return
	 */
	public static String generateZa43HtmlFinalMatrixData(List<CusInterval> za43IntervalList){
		StringBuffer sb = new StringBuffer();
		sb.append("<div id='finalMatrixDiv' class='matrixDiv'>");
		sb.append("<table id='finalMatrixTable' class='matrixTable'>");
		for(CusInterval cusInt : za43IntervalList){
			String value = BasicTypeUtils.EMPTY_STR;
			if (cusInt.getIntervalValue() != null){
				value = cusInt.getIntervalValue();
			}
			sb.append("<tr>");
            sb.append("<td class='rowLevel'>" + cusInt.getIntervalLevel() + "</td>");			
			sb.append("<td class='cell cellEx'><input type='text' name='fnCell' id='fnCell" + cusInt.getIntervalId() + "' value='" + value + "' class='inputTxt inputTxtEx'/></td>");			
			sb.append("</tr>");
		}
		sb.append("</table>");
		sb.append("</div>");
		return sb.toString();
	}
	
	
	/**
	 * 返回行序号title
	 * @param orgRowNm
	 * @return title
	 */
	public static String returnRowSerialTitle(String orgRowNm){
		Integer underLineIndex = orgRowNm.lastIndexOf(ComacConstants.UNDER_LINE);
		if (underLineIndex < 0){
			return orgRowNm;	
		}else{
			return orgRowNm.substring(underLineIndex + 1);
		}
	}
	
	/**
	 * 返回行title
	 * @param orgRowNm
	 * @return title
	 */
	public static String returnRowTitle(String orgRowNm){
		Integer underLineIndex = orgRowNm.lastIndexOf(ComacConstants.UNDER_LINE);
		if (underLineIndex < 0){
			return orgRowNm;	
		}else{
			return orgRowNm.substring(0, underLineIndex);
		}
	}
	
	/**
	 * 将检查间隔矩阵数据转换为html
	 * @param finalMatrixList 检查间隔矩阵list
	 * @param language 当前语言
	 * @return
	 * @author changyf
	 * createdate 2012-08-24
	 */
	public static String generateHtmlFinalMatrixData(HashMap<String, List<CusInterval>> hMap){
		StringBuffer sb = new StringBuffer();
		List<CusInterval> inMatrixList = hMap.get(ComacConstants.INNER);
		List<CusInterval> outMatrixList = hMap.get(ComacConstants.OUTTER);
		
		String rowNm;
		String innerNm;
		String outterNm;
		
	
			rowNm = ComacConstants.INTEVAL_TITLE_CN;
			innerNm = ComacConstants.INNER_TITLE_CN;
			outterNm = ComacConstants.OUTTER_TITLE_CN;
		
		
		sb.append("<div id='finalMatrixDiv' class='matrixDiv'>");
		sb.append("<table id='finalMatrixTable' class='matrixTable'>");
        // 添加行头
		sb.append("<tr><td rowspan='2' colspan='2' class='rowLevel'></td>");
	    sb.append("</tr>");
		sb.append("<tr>");	    
	    for (int i = 0 ; i < inMatrixList.size() ; i++){
	        sb.append("<td class='cellLevel cellLevelEx'><b>" + inMatrixList.get(i).getIntervalLevel() + "</b></td>");	
	    }	   
	    sb.append("</tr>");
	    
	    // 添加列头和内部行
	    sb.append("<tr><td id='colId' rowspan='" + 2 + "' class='rowLevel'><b>"+ rowNm +"</b></td>");
		
	    sb.append("<td class='rowLevel'><b>" + innerNm + "</b></td>");	    
        for(int i = 0 ; i < inMatrixList.size(); i++){
        	String value = (inMatrixList.get(i).getIntervalValue() == null) ? BasicTypeUtils.EMPTY_STR : inMatrixList.get(i).getIntervalValue();
            sb.append("<td class='cell cellEx'><input type='text' name='fnCell' id='fnCell" + inMatrixList.get(i).getIntervalId() + "' value='" + value + "' class='inputTxt inputTxtEx'/></td>");        	
        }        
        sb.append("</tr>");
        // 添加外部行
        sb.append("<tr>");
        sb.append("<td class='rowLevel'><b>" + outterNm + "</b></td>");
        for(int i = 0 ; i < outMatrixList.size(); i++){
        	String value = (outMatrixList.get(i).getIntervalValue() == null) ? BasicTypeUtils.EMPTY_STR : outMatrixList.get(i).getIntervalValue();
            sb.append("<td class='cell cellEx'><input type='text' name='fnCell' id='fnCell" + outMatrixList.get(i).getIntervalId() + "' value='" + value + "' class='inputTxt inputTxtEx'/></td>");        	
        }               
        sb.append("</tr>");
        sb.append("</table></div>");
	    return sb.toString();
	}
	
	/**
	 * 将副矩阵数据转换为html(分析流程用)
	 * @param matrixList 矩阵数据
	 * @param language 当前语言
	 * @param matrixFlg 矩阵区分flg
	 * @return 
	 * @author changyf
	 * createdate 2012-08-22
	 */
	public static String generateHtmlSelectMatrix(List<CusMatrix> matrixList, Integer matrixFlg){
		String rowNm = returnRowTitle(matrixList.get(0).getMatrixRowName());
		String colNm = matrixList.get(0).getMatrixColName();
		
		// 因为list是按行号列号升序排过的，所以list中最后一个矩阵单元对象的行号列号肯定是最大的
		Integer rowCount = matrixList.get(matrixList.size() - 1).getMatrixRow();
		Integer colCount = matrixList.get(matrixList.size() - 1).getMatrixCol(); 
		
		StringBuffer sb = new StringBuffer();		
		sb.append("<div id='matrixDiv" + matrixFlg + "' class='matrixDiv'>");
		sb.append("<table id='matrixTable" + matrixFlg + "' class='matrixTable'>");
        // 添加行头
		sb.append("<tr><td rowspan='2' colspan='2' class='blankCell'></td>");
	    sb.append("<td id='rowTitle' colspan='" + rowCount + "' class='rowTitle'><b>" + colNm + "</b></td>");
	    sb.append("</tr>");
		sb.append("<tr>");	    
	    for (int i = 1 ; i <= colCount ; i++){
	        sb.append("<td class='cellLevel'><b>" + i + "</b></td>");	
	    }	   
	    sb.append("</tr>");
	    
	    // 添加列头和第一行
	    sb.append("<tr><td id='colId' rowspan='" + (rowCount + 1) + "' class='cellTitle'><b>"+ rowNm +"</b></td>");
		
	    if (matrixFlg.equals(ComacConstants.FIRST_MATRIX)) {// 第一矩阵的情况下
			sb.append("<td class='rowLevel'><b>1</b></td>");
		}else if (matrixFlg.equals(ComacConstants.SECOND_MATRIX)){// 第二矩阵的情况下
			sb.append("<td class='rowLevel'><b>" + returnRowSerialTitle(matrixList.get(0).getMatrixRowName()) + "</b></td>");
		}
	
        for(int i = 1 ; i <= colCount; i++){
        	String value = (matrixList.get(0).getMatrixValue() == null) ? BasicTypeUtils.EMPTY_STR : matrixList.get(0).getMatrixValue().toString();
            sb.append("<td class='cell' rownum='" + matrixList.get(0).getMatrixRow() + "' colnum='" + matrixList.get(0).getMatrixCol() + "'>" + value + "</td>");
            matrixList.remove(0);
        }
        sb.append("</tr>");
	 
        // 添加剩下的行
        for(int i = 2 ; i <= rowCount; i++){
        	if (matrixFlg.equals(ComacConstants.FIRST_MATRIX)) {// 第一矩阵的情况下
				sb.append("<tr><td class='rowLevel'><b>" + i + "</b></td>");
			} else if(matrixFlg.equals(ComacConstants.SECOND_MATRIX)) {// 第二矩阵的情况下
				sb.append("<tr><td class='rowLevel'><b>" + returnRowSerialTitle(matrixList.get(0).getMatrixRowName()) + "</b></td>");
			}
        	
            for(int j = 1 ; j <= colCount; j++){
            	String value = (matrixList.get(0).getMatrixValue() == null) ? BasicTypeUtils.EMPTY_STR : matrixList.get(0).getMatrixValue().toString();
            	sb.append("<td class='cell' rownum='" + matrixList.get(0).getMatrixRow() + "' colnum='" + matrixList.get(0).getMatrixCol() + "'>" + value + "</td>");
                matrixList.remove(0);
            }
            sb.append("</tr>");
        }
        sb.append("</table></div>");
        return sb.toString();
	}
	
	/**
	 * 将za43的检查间隔矩阵数据转换为html(分析流程用)
	 * @param za43IntervalList
	 * @param language
	 * @return
	 */
	public static String generateZa43HtmlSelectFinalMatrixData(List<CusInterval> za43IntervalList) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div id='finalMatrixDiv' class='matrixDiv'>");
		sb.append("<table id='finalMatrixTable' class='matrixTable'>");		
		sb.append("<tr>");
		for (CusInterval cusInt : za43IntervalList) {
			sb.append("<td class='rowLevel rowLevelEx'>" + cusInt.getIntervalLevel()
					+ "</td>");
		}
		sb.append("</tr>");
		sb.append("<tr>");
		for (CusInterval cusInt : za43IntervalList) {
			String value = BasicTypeUtils.EMPTY_STR;
			if (cusInt.getIntervalValue() != null) {
				value = cusInt.getIntervalValue();
			}
			sb.append("<td class='cell'>" + value + "</td>");
		}
		sb.append("</tr>");
		sb.append("</table>");
		sb.append("</div>");
		return sb.toString();
	}
	
	/**
	 * 将za5的检查间隔矩阵数据转换为html(分析流程用)
	 * @param finalMatrixList 检查间隔矩阵list
	 * @param language 当前语言
	 * @return
	 * @author changyf
	 * createdate 2012-08-24
	 */
	public static String generateZa5HtmlSelectFinalMatrixData(HashMap<String, List<CusInterval>> hMap){
		StringBuffer sb = new StringBuffer();
		List<CusInterval> inMatrixList = hMap.get(ComacConstants.INNER);
		List<CusInterval> outMatrixList = hMap.get(ComacConstants.OUTTER);
		
		String rowNm = ComacConstants.INTEVAL_TITLE_CN;;
		String innerNm = ComacConstants.INNER_TITLE_CN;;
		String outterNm = ComacConstants.OUTTER_TITLE_CN;;
		
		sb.append("<div id='finalMatrixDiv' class='matrixDiv'>");
		sb.append("<table id='finalMatrixTable' class='matrixTable'>");
        // 添加行头
		sb.append("<tr><td rowspan='2' colspan='2' class='blankCell'></td>");
	    sb.append("</tr>");
		sb.append("<tr>");	    
	    for (int i = 0 ; i < inMatrixList.size() ; i++){
	        sb.append("<td class='cellLevel'><b>" + inMatrixList.get(i).getIntervalLevel() + "</b></td>");	
	    }	   
	    sb.append("</tr>");
	    
	    // 添加列头和内部行
	    sb.append("<tr><td id='colId' rowspan='" + 2 + "' class='rowTitle'><b>"+ rowNm +"</b></td>");
		
	    sb.append("<td class='rowLevel'><b>" + innerNm + "</b></td>");	    
        for(int i = 0 ; i < inMatrixList.size(); i++){
        	String value = (inMatrixList.get(i).getIntervalValue() == null) ? BasicTypeUtils.EMPTY_STR : inMatrixList.get(i).getIntervalValue();
            sb.append("<td class='cell' id='" + inMatrixList.get(0).getIntervalId() + "'>"+ value +"</td>");        	
        }        
        sb.append("</tr>");
        // 添加外部行
        sb.append("<tr>");
        sb.append("<td class='rowLevel'><b>" + outterNm + "</b></td>");
        for(int i = 0 ; i < outMatrixList.size(); i++){
        	String value = (outMatrixList.get(i).getIntervalValue() == null) ? BasicTypeUtils.EMPTY_STR : outMatrixList.get(i).getIntervalValue();
        	sb.append("<td class='cell' id='" + outMatrixList.get(0).getIntervalId() + "'>"+ value +"</td>");        	
        }               
        sb.append("</tr>");
        sb.append("</table></div>");
	    return sb.toString();
	}
}
