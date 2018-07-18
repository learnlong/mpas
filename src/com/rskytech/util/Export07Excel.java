package com.rskytech.util;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComAreaDetail;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComMmel;
import com.rskytech.pojo.ComVendor;
/**
 * 利用开源组件POI3.7动态导出EXCEL文档
 * 转载时请保留以下信息，注明出处！
 * @author fys
 * @version v1.0
 * @param <T> 应用泛型，代表任意一个符合javabean风格的类
 *  注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx()
 *   byte[]表jpg格式的图片数据
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Export07Excel<T> {
	public void exportExcel(Collection<T> dataset, OutputStream out) {
		exportExcel("导出数据", null, dataset, out, "yyyy-MM-dd");
	}
	public void exportExcel(String[] headers, Collection<T> dataset,OutputStream out) {
		exportExcel("导出数据", headers, dataset, out, "yyyy-MM-dd");
	}
	public void exportExcel(String[] headers, Collection<T> dataset,OutputStream out, String pattern) {
		exportExcel("导出数据", headers, dataset, out, pattern);
	}
	/**
	 * 
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 * @param title 表格标题名
	 * @param headers 表格属性列名数组
	 * @param dataset需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out 与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 */

	public void exportExcel(String title, String[] headers,Collection<T> dataset, OutputStream out, String pattern) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);
		// 声明一个画图的顶级管理器
//		XSSFDrawing patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		//XSSFComment comment = patriarch.createCellComment(new XSSFClientAnchor(0,
		//		0, 0, 0, (short) 4, 2, (short) 6, 5));
		
		// 设置注释内容
		//comment.setString(new XSSFRichTextString("可以在POI中添加注释！"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		//comment.setAuthor("fys");
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// 遍历集合数据，产生数据行

		Iterator<T> it = dataset.iterator();
		int index = 0;
		Field[] fieldsShort=null;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			T t = (T) it.next();
//			String className=t.getClass().getName();
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			Field[] fields = t.getClass().getDeclaredFields();
			if(ComAta.class==t.getClass()){
				fieldsShort=new Field[5];
				for(int m=0;m<fields.length;m++){
					String fieldName=fields[m].getName();
					if("ataCode".equals(fieldName)){
						fieldsShort[0]=fields[m];
					}else if("ataNameCn".equals(fieldName)){
						fieldsShort[1]=fields[m];
					}else if("ataNameEn".equals(fieldName)){
						fieldsShort[2]=fields[m];
					}else if("remarkCn".equals(fieldName)){
						fieldsShort[3]=fields[m];
					}
					else if("remarkEn".equals(fieldName)){
						fieldsShort[4]=fields[m];
					}
				}
			}else if(ComArea.class==t.getClass()){
				fieldsShort=new Field[5];
				for(int m=0;m<fields.length;m++){
					String fieldName=fields[m].getName();
					if("areaCode".equals(fieldName)){
						fieldsShort[0]=fields[m];
					}else if("areaNameCn".equals(fieldName)){
						fieldsShort[1]=fields[m];
					}else if("areaNameEn".equals(fieldName)){
						fieldsShort[2]=fields[m];
					}else if("remarkCn".equals(fieldName)){
						fieldsShort[3]=fields[m];
					}
					else if("remarkEn".equals(fieldName)){
						fieldsShort[4]=fields[m];
					}
				}
			}else if(ComMmel.class==t.getClass()){
				fieldsShort=new Field[5];
				for(int m=0;m<fields.length;m++){
					String fieldName=fields[m].getName();
					if("mmelCode".equals(fieldName)){
						fieldsShort[0]=fields[m];
					}else if("mmelNameCn".equals(fieldName)){
						fieldsShort[1]=fields[m];
					}else if("mmelNameEn".equals(fieldName)){
						fieldsShort[2]=fields[m];
					}else if("remarkCn".equals(fieldName)){
						fieldsShort[3]=fields[m];
					}
					else if("remarkEn".equals(fieldName)){
						fieldsShort[4]=fields[m];
					}
				}
			}else if(ComVendor.class==t.getClass()){
				fieldsShort=new Field[4];
				for(int m=0;m<fields.length;m++){
					String fieldName=fields[m].getName();
					if("vendorNameCn".equals(fieldName)){
						fieldsShort[0]=fields[m];
					}else if("vendorNameEn".equals(fieldName)){
						fieldsShort[1]=fields[m];
					}
					else if("remarkCn".equals(fieldName)){
						fieldsShort[2]=fields[m];
					}
					else if("remarkEn".equals(fieldName)){
						fieldsShort[3]=fields[m];
					}
				}
			}
			if(fieldsShort==null){
				return;
			}
			for (int i = 0; i < fieldsShort.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style2);
				Field field = fieldsShort[i];
				String fieldName = field.getName();
				String getMethodName = "get"
				+ fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
				try {
					Class tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName,new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					// 判断值的类型后进行强制类型转换
					String textValue = null;
					if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						textValue = sdf.format(date);
					}else {
						// 其它数据类型都当作字符串简单处理
						if(value==null){
							continue;
						}else{
							textValue = value.toString();
						}
					}
					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						Pattern p = Pattern.compile("^//d+(//.//d+)?$");
						Matcher matcher = p.matcher(textValue);
						if (matcher.matches()) {
							// 是数字当作double处理
							cell.setCellValue(Double.parseDouble(textValue));
						} else {
							HSSFRichTextString richString = new HSSFRichTextString(textValue);
							HSSFFont font3 = workbook.createFont();
//							font3.setColor(new HSSFColor());
							richString.applyFont(font3);
							cell.setCellValue(richString);
						}
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} finally {
					// 清理资源
				}
			}
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(out!=null){
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 导出区域数据
	* @Title: exportAreaExcel
	* @Description:
	* @param comAreaList
	* @param out
	* @author samual
	* @date 2014年12月29日 下午3:17:01
	* @throws
	 */
	public void exportAreaExcel(List<ComArea> comAreaList, OutputStream out) {
		String title = "导出数据";
//		String pattern = "yyyy-MM-dd";
		String[] headers = { "子区域号", "子区域名称", "口盖","设备名称","设备图号","设备型号","含何种电缆电线、何种管路","备注"};//中文列头
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); 
		HSSFDataFormat format = workbook.createDataFormat();
		style2.setDataFormat(format.getFormat("@"));
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);
		// 生成并设置另一个样式
		HSSFCellStyle style3 = workbook.createCellStyle();
		style3.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style3.setDataFormat(format.getFormat("@"));
		style3.setWrapText(true);//自动换行
		style3.setFont(font2);
		
		// 声明一个画图的顶级管理器
//		HSSFDrawing patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		//HSSFComment comment = patriarch.createCellComment(new HSSFClientAnchor(0,
		//		0, 0, 0, (short) 4, 2, (short) 6, 5));
		
		// 设置注释内容
		//comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		//comment.setAuthor("fys");
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// 遍历集合数据，产生数据行
		if(comAreaList == null || comAreaList.size() == 0){
			return;
		}
		int index = 0;//总行下标
		for(ComArea comArea : comAreaList){
			Set comAreaDetails = comArea.getComAreaDetails();
			//1.插入设备数据
			int comAreaDetailCount = 0;//设备的行数
			HSSFRow areaMainRow = null;
			if(comAreaDetails != null && comAreaDetails.size() > 0){//有设备数据
				Iterator<ComAreaDetail> it = comAreaDetails.iterator();
				while (it.hasNext()) {
					comAreaDetailCount ++;
					index++;
					ComAreaDetail comAreaDetail = it.next();
					try {
						row = sheet.createRow(index);
						if(comAreaDetailCount == 1){
							areaMainRow = row;
						}
						HSSFCell cellCol0 = row.createCell(0);
						cellCol0.setCellStyle(style2);
						HSSFCell cellCol1 = row.createCell(1);
						cellCol1.setCellStyle(style3);
						HSSFCell cellCol2 = row.createCell(2);
						cellCol2.setCellStyle(style3);
						HSSFCell cellCol6 = row.createCell(6);
						cellCol6.setCellStyle(style3);
						HSSFCell cellCol7= row.createCell(7);
						cellCol7.setCellStyle(style3);
						HSSFRichTextString richString = null;
						//第三列：设备名称
						HSSFCell cellCol3 = row.createCell(3);
						cellCol3.setCellStyle(style3);
						richString = new HSSFRichTextString(comAreaDetail.getEquipmentName()==null?"":comAreaDetail.getEquipmentName());
						cellCol3.setCellValue(richString);
						//第四列：设备图号
						HSSFCell cellCol4 = row.createCell(4);
						cellCol4.setCellStyle(style3);
						richString = new HSSFRichTextString(comAreaDetail.getEquipmentPicNo()==null?"":comAreaDetail.getEquipmentPicNo());
						cellCol4.setCellValue(richString);
						//第五列：设备型号
						HSSFCell cellCol5 = row.createCell(5);
						cellCol5.setCellStyle(style3);
						richString = new HSSFRichTextString(comAreaDetail.getEquipmentTypeNo()==null?"":comAreaDetail.getEquipmentTypeNo());
						cellCol5.setCellValue(richString);
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						// 清理资源
					}
				}
			}else {//没有设备数据--插入空数据
				comAreaDetailCount ++;
				index++;
				try {
					row = sheet.createRow(index);
					areaMainRow = row;
					HSSFCell cellCol0 = row.createCell(0);
					cellCol0.setCellStyle(style2);
					HSSFCell cellCol1 = row.createCell(1);
					cellCol1.setCellStyle(style3);
					HSSFCell cellCol2 = row.createCell(2);
					cellCol2.setCellStyle(style3);
					HSSFCell cellCol3 = row.createCell(3);
					cellCol3.setCellStyle(style3);
					HSSFCell cellCol4 = row.createCell(4);
					cellCol4.setCellStyle(style3);
					HSSFCell cellCol5 = row.createCell(5);
					cellCol5.setCellStyle(style3);
					HSSFCell cellCol6 = row.createCell(6);
					cellCol6.setCellStyle(style3);
					HSSFCell cellCol7= row.createCell(7);
					cellCol7.setCellStyle(style3);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// 清理资源
				}
			}
			//2.插入区域的数据
			try {
				HSSFRichTextString richString = null;
				if(comAreaDetailCount != 1){
					sheet.addMergedRegion(new CellRangeAddress(index - comAreaDetailCount + 1, index, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(index - comAreaDetailCount + 1, index, 1, 1));
					sheet.addMergedRegion(new CellRangeAddress(index - comAreaDetailCount + 1, index, 2, 2));
					sheet.addMergedRegion(new CellRangeAddress(index - comAreaDetailCount + 1, index, 6, 6));
					sheet.addMergedRegion(new CellRangeAddress(index - comAreaDetailCount + 1, index, 7, 7));
				}
				//第一列：区域号
				HSSFCell cellCol0 = areaMainRow.getCell(0);
				richString = new HSSFRichTextString(comArea.getAreaCode()==null?"":comArea.getAreaCode());
				cellCol0.setCellValue(richString);
				//第二列：区域名称
				HSSFCell cellCol1 = areaMainRow.getCell(1);
				richString = new HSSFRichTextString(comArea.getAreaName()==null?"":comArea.getAreaName());
				cellCol1.setCellValue(richString);
				//第三列：口盖
				HSSFCell cellCol2 = areaMainRow.getCell(2);
				richString = new HSSFRichTextString(comArea.getReachWay()==null?"":comArea.getReachWay());
				cellCol2.setCellValue(richString);
				//第七列：含何种电缆电线、何种管路
				HSSFCell cellCol6 = areaMainRow.getCell(6);
				richString = new HSSFRichTextString(comArea.getWirePiping()==null?"":comArea.getWirePiping());
				cellCol6.setCellValue(richString);
				//第八列：备注
				HSSFCell cellCol7 = areaMainRow.getCell(7);
				richString = new HSSFRichTextString(comArea.getRemark()==null?"":comArea.getRemark());
				cellCol7.setCellValue(richString);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 清理资源
			}
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(out!=null){
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 导出ata数据
	* @Title: exportAtaExcel
	* @Description:
	* @param comAtaList
	* @param out
	* @author samual
	* @date 2015年1月9日 上午9:52:55
	* @ModifiedBy zhaojunwei
	* @ModifiedOn 2015年3月30日
	* @throws
	 */
	public void exportAtaExcel(List<ComAta> comAtaList, OutputStream out) {
		String title = "导出数据";
//		String pattern = "yyyy-MM-dd";
		String[] headers = { "ATA编号","ATA名称","产品名称","产品图号","产品型号","安装位置","备注"};//中文列头
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); 
		HSSFDataFormat format = workbook.createDataFormat();
		style2.setDataFormat(format.getFormat("@"));
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);
		// 生成并设置另一个样式
		HSSFCellStyle style3 = workbook.createCellStyle();
		style3.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style3.setDataFormat(format.getFormat("@"));
		style3.setWrapText(true);//自动换行
		style3.setFont(font2);
		// 声明一个画图的顶级管理器
//		HSSFDrawing patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		//HSSFComment comment = patriarch.createCellComment(new HSSFClientAnchor(0,
		//		0, 0, 0, (short) 4, 2, (short) 6, 5));
		
		// 设置注释内容
		//comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		//comment.setAuthor("fys");
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// 遍历集合数据，产生数据行
		if(comAtaList == null || comAtaList.size() == 0){
			return;
		}
		List<ComAta> allList = this.getEffectiveComAtaList(comAtaList);
		for(int i=0; i<allList.size(); i++){
			ComAta tmpAta = allList.get(i);
			try {
				row = sheet.createRow(i+1);
				HSSFRichTextString richString = null;
				HSSFCell cellCol0 = row.createCell(0);//ataCode
				cellCol0.setCellStyle(style2);
				richString = new HSSFRichTextString(tmpAta.getAtaCode()==null?"":tmpAta.getAtaCode().replace("-", ""));
				cellCol0.setCellValue(richString);
				HSSFCell cellCol1 = row.createCell(1);//ataName
				cellCol1.setCellStyle(style3);
				richString = new HSSFRichTextString(tmpAta.getAtaName()==null?"":tmpAta.getAtaName());
				cellCol1.setCellValue(richString);
				HSSFCell cellCol2 = row.createCell(2);//产品名称
				cellCol2.setCellStyle(style3);
				richString = new HSSFRichTextString(tmpAta.getEquipmentName()==null?"":tmpAta.getEquipmentName());
				cellCol2.setCellValue(richString);
				HSSFCell cellCol3 = row.createCell(3);//产品图号
				cellCol3.setCellStyle(style3);
				richString = new HSSFRichTextString(tmpAta.getEquipmentPicNo()==null?"":tmpAta.getEquipmentPicNo());
				cellCol3.setCellValue(richString);
				HSSFCell cellCol4 = row.createCell(4);//产品型号
				cellCol4.setCellStyle(style3);
				richString = new HSSFRichTextString(tmpAta.getEquipmentTypeNo()==null?"":tmpAta.getEquipmentTypeNo());
				cellCol4.setCellValue(richString);
				HSSFCell cellCol5 = row.createCell(5);//安装位置
				cellCol5.setCellStyle(style3);
				richString = new HSSFRichTextString(tmpAta.getEquipmentPosition()==null?"":tmpAta.getEquipmentPosition());
				cellCol5.setCellValue(richString);
				HSSFCell cellCol6 = row.createCell(6);//备注
				cellCol6.setCellStyle(style3);
				richString = new HSSFRichTextString(tmpAta.getRemark()==null?"":tmpAta.getRemark());
				cellCol6.setCellValue(richString);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 清理资源
			}
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(out!=null){
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 导出供应商数据
	* @Title: exportVendorExcel
	* @Description:
	* @param comAtaList
	* @param out
	* @author 张建民
	* @date 2015年6月11日
	* @throws
	 */
	public void exportVendorExcel(List<ComVendor> comVendorList, OutputStream out) {
		String title = "导出数据";
//		String pattern = "yyyy-MM-dd";
		String[] headers = { "供应商编号","供应商名称","备注"};//中文列头
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); 
		HSSFDataFormat format = workbook.createDataFormat();
		style2.setDataFormat(format.getFormat("@"));
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);
		// 生成并设置另一个样式
		HSSFCellStyle style3 = workbook.createCellStyle();
		style3.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style3.setDataFormat(format.getFormat("@"));
		style3.setWrapText(true);//自动换行
		style3.setFont(font2);
		// 声明一个画图的顶级管理器
//		HSSFDrawing patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		//HSSFComment comment = patriarch.createCellComment(new HSSFClientAnchor(0,
		//		0, 0, 0, (short) 4, 2, (short) 6, 5));
		
		// 设置注释内容
		//comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		//comment.setAuthor("fys");
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// 遍历集合数据，产生数据行
		if(comVendorList == null || comVendorList.size() == 0){
			return;
		}
		for(int i=0; i<comVendorList.size(); i++){
			ComVendor cv = comVendorList.get(i);
			try {
				row = sheet.createRow(i+1);
				HSSFRichTextString richString = null;
				HSSFCell cellCol0 = row.createCell(0);
				cellCol0.setCellStyle(style2);
				richString = new HSSFRichTextString(cv.getVendorCode()==null?"":cv.getVendorCode());
				cellCol0.setCellValue(richString);
				HSSFCell cellCol1 = row.createCell(1);
				cellCol1.setCellStyle(style3);
				richString = new HSSFRichTextString(cv.getVendorName()==null?"":cv.getVendorName());
				cellCol1.setCellValue(richString);
				HSSFCell cellCol6 = row.createCell(2);//备注
				cellCol6.setCellStyle(style3);
				richString = new HSSFRichTextString(cv.getRemark()==null?"":cv.getRemark());
				cellCol6.setCellValue(richString);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 清理资源
			}
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(out!=null){
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取有效的ata
	* @Title: getEffectiveComAtaList
	* @Description:
	* @param comAtaList
	* @return
	* @author samual
	* @date 2015年1月9日 上午9:55:31
	* @throws
	 */
	public List<ComAta> getEffectiveComAtaList(List<ComAta> comAtaList){
		List<ComAta> allList = new ArrayList<ComAta>();//去处code不符合规则的
		for(ComAta comAta : comAtaList){
			String tempAtaCode = comAta.getAtaCode();
			if(this.checkAtaCode(tempAtaCode)){
				ComAta newAta = comAta;
//				tempAtaCode = tempAtaCode.replaceAll("-", "");
////				newAta.setAtaCode(tempAtaCode);
//				newAta.setAta1l(tempAtaCode.substring(0, 2));
//				if(tempAtaCode.length() == 6){
//					if(newAta.getAtaLevel() == 4){
//						newAta.setAta2l(tempAtaCode.substring(0, 3));
//						newAta.setAta3l(tempAtaCode.substring(0, 4));
//						newAta.setAta4l(tempAtaCode);
//					}else if (newAta.getAtaLevel() == 3) {
//						newAta.setAta2l(tempAtaCode.substring(0, 3));
//						newAta.setAta3l(tempAtaCode.substring(0, 4));
//					}else if (newAta.getAtaLevel() == 2) {
//						newAta.setAta2l(tempAtaCode.substring(0, 3));
//					}
//				}
				allList.add(newAta);
			}
		}
		return allList;
	}
	
	/**
	 * 校验atacode
	* @Title: checkAtaCode
	* @Description:
	* @param ataCode
	* @return
	* @author samual
	* @date 2015年1月9日 上午9:55:52
	* @ModifiedBy zhaojunwei
	* @ModifiedOn 2015年3月30日
	* @throws
	 */
	private boolean checkAtaCode(String ataCode) {
		if(ataCode == null || "".equals(ataCode)){
			return false;
		}
		if(ataCode.length() != 11){
			return false;
		}
		String tmpAtaCode = ataCode.replaceAll("-", "");
		if(tmpAtaCode.length() != 8){
			return false;
		}
		try {
			Integer.parseInt(tmpAtaCode);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 根据四大分析code获取中文名
	* @Title: getSourceSystem
	* @Description:
	* @param sourceSystem
	* @return
	* @author samual
	* @date 2015年1月9日 上午9:57:14
	* @throws
	 */
	private String getSourceSystem(String sourceSystem) {
		String sourceSystemValue = "";
		if("".equals(sourceSystem)) {
			sourceSystemValue = "全部";
		} else if("SYS".equals(sourceSystem)) {
			sourceSystemValue = "系统";
		} else if("STRUCTURE".equals(sourceSystem)) {
			sourceSystemValue = "结构";
		} else if("ZONE".equals(sourceSystem)) {
			sourceSystemValue = "区域";
		} else if("LHIRF".equals(sourceSystem)) {
			sourceSystemValue = "L/HIRF";
		} else if("ONESELFADD".equals(sourceSystem)) {
			sourceSystemValue = "自加";
		}
		return sourceSystemValue;
	}
	
	
	/**
	 * @author wangpengfei
	 * 为String去除空和null的字样，并替换所有的换行符为空
	 * @param s 字符串
	 */
	public String getStr(String s){
		return (s == null || "null".equals(s)) ? "" : s.replaceAll("\r", "");
	}
	
	
	/**
	 * 导出MRB数据
	* @Title: exportMrbExcel
	* @Description:
	* @param taskMrbList 所有MRB数据列表
	* @param comAreaList 所有area数据列表
	* @param out
	* @author wangpengfei
	* @date 2015年1月9日 上午9:57:43
	* @throws
	 */
	public void exportMrbExcel(List taskMrbList,List<ComArea> comAreaList, OutputStream out) {
		String title = "导出数据";
		String[] headers = { "任务来源*", "MRB编号*", "任务类型","标准任务间隔","任务描述","接近方式","所属区域","适用性","MSG-3任务编号"};//中文列头
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); 
		HSSFDataFormat format = workbook.createDataFormat();
		style2.setDataFormat(format.getFormat("@"));
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);
		//字体三
		HSSFFont font3 = workbook.createFont();
//		font3.setColor(new HSSFColor());
		// 声明一个画图的顶级管理器
//				HSSFDrawing patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		//HSSFComment comment = patriarch.createCellComment(new HSSFClientAnchor(0,
		//		0, 0, 0, (short) 4, 2, (short) 6, 5));
		
		// 设置注释内容
		//comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		//comment.setAuthor("fys");
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// 遍历集合数据，产生数据行
		if(taskMrbList == null || taskMrbList.size() == 0){
			return;
		}
		for(int i=0; i<taskMrbList.size(); i++){
			Object[] taskMrb = (Object[])taskMrbList.get(i);
			try {
				row = sheet.createRow(i+1);
				HSSFRichTextString richString = null;
				HSSFCell cellCol0 = row.createCell(0);
				cellCol0.setCellStyle(style2);
				richString = new HSSFRichTextString(getSourceSystem(String.valueOf(taskMrb[0])));
				richString.applyFont(font3);
				cellCol0.setCellValue(richString);
				HSSFCell cellCol1 = row.createCell(1);
				cellCol1.setCellStyle(style2);
				richString = new HSSFRichTextString(getStr(String.valueOf(taskMrb[1])));
				richString.applyFont(font3);
				cellCol1.setCellValue(richString);
				HSSFCell cellCol2 = row.createCell(2);
				cellCol2.setCellStyle(style2);
				richString = new HSSFRichTextString(getStr(String.valueOf(taskMrb[2])));
				richString.applyFont(font3);
				cellCol2.setCellValue(richString);
				HSSFCell cellCol3 = row.createCell(3);
				cellCol3.setCellStyle(style2);
				richString = new HSSFRichTextString(getStr(String.valueOf(taskMrb[3])));
				richString.applyFont(font3);
				cellCol3.setCellValue(richString);
				HSSFCell cellCol4 = row.createCell(4);
				cellCol4.setCellStyle(style2);
				richString = new HSSFRichTextString(getStr(String.valueOf(taskMrb[4])));
				richString.applyFont(font3);
				cellCol4.setCellValue(richString);
				HSSFCell cellCol5 = row.createCell(5);
				cellCol5.setCellStyle(style2);
				richString = new HSSFRichTextString(getStr(String.valueOf(taskMrb[5])));
				richString.applyFont(font3);
				cellCol5.setCellValue(richString);
				HSSFCell cellCol6 = row.createCell(6);
				cellCol6.setCellStyle(style2);
				richString = new HSSFRichTextString(getStr(this.getAreaCodesByAreaIds(String.valueOf(taskMrb[6]), comAreaList)));
				richString.applyFont(font3);
				cellCol6.setCellValue(richString);
				HSSFCell cellCol7 = row.createCell(7);
				cellCol7.setCellStyle(style2);
				richString = new HSSFRichTextString(getStr(String.valueOf(taskMrb[7])));
				richString.applyFont(font3);
				cellCol7.setCellValue(richString);
				HSSFCell cellCol8 = row.createCell(8);
				cellCol8.setCellStyle(style2);
				richString = new HSSFRichTextString(getStr(String.valueOf(taskMrb[8])));
				richString.applyFont(font3);
				cellCol8.setCellValue(richString);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 清理资源
			}
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(out!=null){
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 根据区域id组（逗号分开），获取区域编号（逗号分开）
	* @Title: getAreaCodesByAreaIds
	* @Description:
	* @param areaIds
	* @param comAreaList
	* @return
	* @author wangpengfei
	* @date 2015年1月9日 上午9:58:43
	* @throws
	 */
	private String getAreaCodesByAreaIds(String areaIds,
			List<ComArea> comAreaList) {
		if(areaIds == null || "".equals(areaIds)){
			return "";
			
		}
		if(comAreaList == null || comAreaList.size() == 0){
			return "";
		}
		String codes = "";
		String[] ids = areaIds.split(",");
		for(String id : ids){
			for(ComArea comArea : comAreaList){
				if(id != null && !"".equals(id) && id.equals(comArea.getAreaId())){
					codes += "," + comArea.getAreaCode();
					break;
				}
			}
		}
		if(!"".equals(codes)){
			codes = codes.substring(1);
		}
		return codes;
	}
	
	/**
	 * 导出MPD数据
	* @Title: exportMpdExcel
	* @Description:
	* @param taskMpdList
	* @param comAreaList
	* @param out
	* @author wangpengfei
	* @date 2015年1月9日 上午9:59:31
	* @throws
	 */
	public void exportMpdExcel(List taskMpdList,List<ComArea> comAreaList, FileOutputStream out) {
		String title = "导出数据";
		String[] headers = { "任务来源*", "MPD编号*", "任务类型","标准任务间隔","任务描述","接近方式","所属区域","适用性","AMM参考*","工时*"};//中文列头
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); 
		HSSFDataFormat format = workbook.createDataFormat();
		style2.setDataFormat(format.getFormat("@"));
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);
		//字体三
		HSSFFont font3 = workbook.createFont();
//		font3.setColor(new HSSFColor());
		// 声明一个画图的顶级管理器
//				HSSFDrawing patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		//HSSFComment comment = patriarch.createCellComment(new HSSFClientAnchor(0,
		//		0, 0, 0, (short) 4, 2, (short) 6, 5));
		
		// 设置注释内容
		//comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		//comment.setAuthor("fys");
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// 遍历集合数据，产生数据行
		if(taskMpdList == null || taskMpdList.size() == 0){
			return;
		}
		for(int i=0; i<taskMpdList.size(); i++){
			//查询到的taskMpdList遍历获得实体数组，将实体属性插入excel表格
			Object[] taskMpd = (Object[])taskMpdList.get(i);
			try {
				row = sheet.createRow(i+1);
				HSSFRichTextString richString = null;
				HSSFCell cellCol0 = row.createCell(0);
				cellCol0.setCellStyle(style2);
				richString = new HSSFRichTextString(getStr(getSourceSystem(String.valueOf(taskMpd[0]))));
				richString.applyFont(font3);
				cellCol0.setCellValue(richString);
				HSSFCell cellCol1 = row.createCell(1);
				cellCol1.setCellStyle(style2);
				richString = new HSSFRichTextString(getStr(String.valueOf(taskMpd[1])));
				richString.applyFont(font3);
				cellCol1.setCellValue(richString);
				HSSFCell cellCol2 = row.createCell(2);
				cellCol2.setCellStyle(style2);
				richString = new HSSFRichTextString(getStr(String.valueOf(taskMpd[2])));
				richString.applyFont(font3);
				cellCol2.setCellValue(richString);
				HSSFCell cellCol3 = row.createCell(3);
				cellCol3.setCellStyle(style2);
				richString = new HSSFRichTextString(getStr(String.valueOf(taskMpd[3])));
				richString.applyFont(font3);
				cellCol3.setCellValue(richString);
				HSSFCell cellCol4 = row.createCell(4);
				cellCol4.setCellStyle(style2);
				richString = new HSSFRichTextString(getStr(String.valueOf(taskMpd[4])));
				richString.applyFont(font3);
				cellCol4.setCellValue(richString);
				HSSFCell cellCol5 = row.createCell(5);
				cellCol5.setCellStyle(style2);
				richString = new HSSFRichTextString(getStr(String.valueOf(taskMpd[5])));
				richString.applyFont(font3);
				cellCol5.setCellValue(richString);
				HSSFCell cellCol6 = row.createCell(6);
				cellCol6.setCellStyle(style2);
				richString = new HSSFRichTextString(getStr(this.getAreaCodesByAreaIds(String.valueOf(taskMpd[6]), comAreaList)));
				richString.applyFont(font3);
				cellCol6.setCellValue(richString);
				HSSFCell cellCol7 = row.createCell(7);
				cellCol7.setCellStyle(style2);
				richString = new HSSFRichTextString(getStr(String.valueOf(taskMpd[7])));
				richString.applyFont(font3);
				cellCol7.setCellValue(richString);
				HSSFCell cellCol8 = row.createCell(8);
				cellCol8.setCellStyle(style2);
				richString = new HSSFRichTextString(getStr(String.valueOf(taskMpd[8])));
				richString.applyFont(font3);
				cellCol8.setCellValue(richString);
				HSSFCell cellCol9 = row.createCell(9);
				cellCol9.setCellStyle(style2);
				richString = new HSSFRichTextString(getStr(String.valueOf(taskMpd[9])));
				richString.applyFont(font3);
				cellCol9.setCellValue(richString);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 清理资源
			}
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(out!=null){
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		// 测试学生
	/*	Export07Excel<Student> ex = new Export07Excel<Student>();
		String[] headers = { "学号", "姓名", "年龄", "性别", "出生日期" };
		List<Student> dataset = new ArrayList<Student>();
		dataset.add(new Student(10000001, "张三", 20, true, new Date()));
		dataset.add(new Student(20000002, "李四", 24, false, new Date()));
		dataset.add(new Student(30000003, "王五", 22, true, new Date()));
		// 测试图书
		Export07Excel<Book> ex2 = new Export07Excel<Book>();
		String[] headers2 = { "图书编号", "图书名称", "图书作者", "图书价格", "图书ISBN",
		"图书出版社", "封面图片" };
		List<Book> dataset2 = new ArrayList<Book>();
		try {
			File file=new File("d:\\workfile\\book.jpg");
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			byte[] buf = new byte[bis.available()];
			while ((bis.read(buf)) != -1) {
				//
			}
			dataset2.add(new Book(1, "jsp", "leno", 300.33f, "1234567",
			"清华出版社", buf));
			dataset2.add(new Book(2, "java编程思想", "brucl", 300.33f, "1234567",
			"阳光出版社", buf));
			dataset2.add(new Book(3, "DOM艺术", "lenotang", 300.33f, "1234567",
			"清华出版社", buf));
			dataset2.add(new Book(4, "c++经典", "leno", 400.33f, "1234567",
			"清华出版社", buf));
			dataset2.add(new Book(5, "c#入门", "leno", 300.33f, "1234567",
			"汤春秀出版社", buf));
			OutputStream out = new FileOutputStream("E://a.xlsx");
			OutputStream out2 = new FileOutputStream("E://b.xlsx");
			ex.exportExcel(headers, dataset, out);
			ex2.exportExcel(headers2, dataset2, out2);
			out.close();
			JOptionPane.showMessageDialog(null, "导出成功!");
			System.out.println("excel导出成功！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		/*String aa = "123456";
		System.out.println(aa.substring(0,2) + "-" + aa.substring(2,4) + "-" + aa.substring(4));*/
	}
}
