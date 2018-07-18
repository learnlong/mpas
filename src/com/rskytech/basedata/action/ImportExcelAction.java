package com.rskytech.basedata.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.basedata.bo.IComAtaBo;
import com.rskytech.basedata.dao.IComAtaDao;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComAreaDetail;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComVendor;

/**
 * 导入基础数据
 * @author samual
 */
public class ImportExcelAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8108382220399878187L;
	
	private File excelFile;// File对象，目的是获取页面上传的文件
	private IComAreaBo comAreaBo;
	private IComAtaBo comAtaBo;
	private IComAtaDao comAtaDao;

	public IComAtaDao getComAtaDao() {
		return comAtaDao;
	}

	public void setComAtaDao(IComAtaDao comAtaDao) {
		this.comAtaDao = comAtaDao;
	}

	public IComAtaBo getComAtaBo() {
		return comAtaBo;
	}

	public void setComAtaBo(IComAtaBo comAtaBo) {
		this.comAtaBo = comAtaBo;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}

	public File getExcelFile() {
		return excelFile;
	}

	public void setExcelFile(File excelFile) {
		this.excelFile = excelFile;
	}
	
	/**
	 * 批量导入区域
	* @Title: importZone
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月29日 下午1:31:43
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public String importZone() {
		String msg="导入成功！";//导入成功或失败的提示信息
		ComModelSeries serie = this.getComModelSeries();//登录选中的机型
		Workbook workbook = null;
		if (excelFile != null) {
			String path = excelFile.getAbsolutePath();// 获取文件的路径
			try {
				workbook = WorkbookFactory.create(new File(path));// 初始化workbook对象
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			msg= "导入的文件为空或不存在";
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setAttribute("msg", msg);
			return "zone";
		}
	    //定义三个list分别装三级区域
		ArrayList<ComArea> allList = new ArrayList<ComArea>();
		ArrayList<ComArea> oneList = new ArrayList<ComArea>();
		ArrayList<ComArea> twoList = new ArrayList<ComArea>();
		ArrayList<ComArea> threeList = new ArrayList<ComArea>();
		Set<Integer> errorRowNumSet= new HashSet<Integer>();//错误记录行号		
		try {
			//得到第一个sheet
			Sheet sheet = workbook.getSheetAt(0);
			errorRowNumSet.addAll(this.getMergedErrorRowsForArea(sheet));//合并行跨列的为有误行
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {//循环读取每一行
				Row row = sheet.getRow(i);
				if (row != null) {
					//1.判断合并单元格是否有误
					boolean isMergeCode = isMergedRegion(sheet, i, 0);//第一列区域号，是否合并单元格
					boolean isMergeName = isMergedRegion(sheet, i, 1);//第二列区域名称，是否合并单元格
					boolean isMergeReachWay = isMergedRegion(sheet, i, 2);//第三列口盖，是否合并单元格
					boolean isMergeEqName = isMergedRegion(sheet, i, 3);//第四列设备名称，是否合并单元格
					boolean isMergeEqPicNo = isMergedRegion(sheet, i, 4);//第五列设备图号，是否合并单元格
					boolean isMergeEqTypeNo = isMergedRegion(sheet, i, 5);//第六列设备型号，是否合并单元格
					boolean isMergeWirePiping = isMergedRegion(sheet, i, 6);//第七列含何种电缆电线、何种管路盖，是否合并单元格
					boolean isMergeReMark = isMergedRegion(sheet, i, 7);//第八列备注，是否合并单元格
					//设备有合并行出错
					if(isMergeEqName || isMergeEqPicNo || isMergeEqTypeNo){
						errorRowNumSet.add(i+1);
					}
					//区域号，区域名称...五列，要么都有合并单元格，要么都没有
					if(isMergeCode){
						if(isMergeName && isMergeReachWay && isMergeWirePiping && isMergeReMark){
						}else {
							errorRowNumSet.add(i+1);
						}
					} else {
						if(isMergeName || isMergeReachWay || isMergeWirePiping || isMergeReMark){
							errorRowNumSet.add(i+1);
						}
					}
					//2.获取值(总List)
					ComArea area = new ComArea();
					area.setAreaId(String.valueOf(i+1));//区域id先存放行号，在后面会清除
					if(isMergeCode) {
						area.setAreaCode(this.getMergedRegionValue(sheet, i, 0));
						area.setAreaName(this.getMergedRegionValue(sheet, i, 1));
						area.setReachWay(this.getMergedRegionValue(sheet, i, 2));
						area.setWirePiping(this.getMergedRegionValue(sheet, i, 6));
						area.setRemark(this.getMergedRegionValue(sheet, i, 7));
					}else{
						area.setAreaCode(this.getCellValue(row.getCell(0)));
						area.setAreaName(this.getCellValue(row.getCell(1)));
						area.setReachWay(this.getCellValue(row.getCell(2)));
						area.setWirePiping(this.getCellValue(row.getCell(6)));
						area.setRemark(this.getCellValue(row.getCell(7)));
					}
					String eqName = this.getCellValue(row.getCell(3))==null?"":this.getCellValue(row.getCell(3));
					String eqPicNo = this.getCellValue(row.getCell(4))==null?"":this.getCellValue(row.getCell(4));
					String eqTypeNo = this.getCellValue(row.getCell(5))==null?"":this.getCellValue(row.getCell(5));
					//设备名称，设备图号，设备型号，只要有一个有值就保存
					if(!"".equals(eqName) || !"".equals(eqPicNo) || !"".equals(eqTypeNo)){
						ComAreaDetail comAreaDetail = new ComAreaDetail();
						comAreaDetail.setEquipmentName(eqName);
						comAreaDetail.setEquipmentPicNo(eqPicNo);
						comAreaDetail.setEquipmentTypeNo(eqTypeNo);
						area.getComAreaDetails().add(comAreaDetail);
					}
					allList.add(area);
				}
			}
			//3.数据分到3个list中
			for(int i=0;i<allList.size(); i++){
				ComArea area = allList.get(i);
				//在获取code可能把把cell格式转为数字，会出现110.0
				area.setAreaCode(this.changeDoubelToIntergerForStr(area.getAreaCode()));
				String areaCode = area.getAreaCode();
				if(this.checkAreaCode(areaCode)){//判读区域号是否有效
					int sameListRow = -1;
					if (Integer.valueOf(areaCode) % 100 == 0) {//第一级区域
						for(int j=0; j<oneList.size(); j++){
							if(oneList.get(j).getAreaCode().equals(areaCode)){
								sameListRow = j;
								break;
							}
						}
						if(sameListRow == -1){
							oneList.add(area);
						} else {
							oneList.get(sameListRow).getComAreaDetails().addAll(area.getComAreaDetails());
						}
					} else if (Integer.valueOf(areaCode) % 10 == 0) {//第二级区域
						for(int j=0; j<twoList.size(); j++){
							if(twoList.get(j).getAreaCode().equals(areaCode)){
								sameListRow = j;
								break;
							}
						}
						if(sameListRow == -1){
							twoList.add(area);
						} else {
							twoList.get(sameListRow).getComAreaDetails().addAll(area.getComAreaDetails());
						}
					} else {//第三级区域
						for(int j=0; j<threeList.size(); j++){
							if(threeList.get(j).getAreaCode().equals(areaCode)){
								sameListRow = j;
								break;
							}
						}
						if(sameListRow == -1){
							threeList.add(area);
						} else {
							threeList.get(sameListRow).getComAreaDetails().addAll(area.getComAreaDetails());
						}
					}
				} else {
					errorRowNumSet.add(Integer.valueOf(area.getAreaId()));//记录错误行，前面在区域id中暂存行号
				}
			}
			//4.判断第二级区域是否都有父区域
			errorRowNumSet.addAll(getRowNoParentForSecondArea(oneList, twoList, serie));
			//5.判断第三级区域是否都有父区域
			errorRowNumSet.addAll(getRowNoParentForThridArea(twoList, threeList, serie));
			//errorRowNumSet大于0时，不保存数据，提示错误的行号
			if(errorRowNumSet.size() == 0){
				errorRowNumSet.addAll(comAreaBo.importComArea(serie,oneList,twoList,threeList,this.getSysUser().getUserId()));
			}
			if(errorRowNumSet.size() > 0){
				msg ="导入失败！第" + this.getErrorRowForHashset(errorRowNumSet) + "行有误；可能的错误：合并单元有误、区域号格式有误或者区域不存在父区域";
			}
		} catch (Exception e) {
			//e.printStackTrace();		
			msg="读取excel文件失败！";
		}
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("msg", msg);
		return "zone";
	}
	
	/**
	 * 批量导入ATA
	* @Title: importAta
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月29日 下午1:31:54
	* @ModifiedBy zhaojunwei
	* @ModifiedOn 2015年3月30日
	* @throws
	 */
	public String importAtaExcel() {
		System.out.println("批量导入ATA");
		String msg="导入成功！";//导入成功或失败的提示信息
		ComModelSeries serie = this.getComModelSeries();//登录选中的机型
		Workbook workbook = null;
		if (excelFile != null) {
			String path = excelFile.getAbsolutePath();// 获取文件的路径
			try {
				workbook = WorkbookFactory.create(new File(path));// 初始化workbook对象
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			msg= "导入的文件为空或不存在";
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setAttribute("msg", msg);
			return "ata";
		}
	    //定义三个list分别装三级区域
		ArrayList<ComAta> allList = new ArrayList<ComAta>();
		Set<Integer> noAtaNameSet= new HashSet<Integer>();//ata名称为空
		Set<Integer> mergedErrorSet= new HashSet<Integer>();//出现了合并单元格错误的行号
		Set<Integer> codeFormatErrorSet= new HashSet<Integer>();//code格式有误
		Set<Integer> noParentErrorSet= new HashSet<Integer>();//code格式有误
		Set<Integer> noSameCodeErrorSet= new HashSet<Integer>();//code相同
		try {
			//得到第一个sheet
			Sheet sheet = workbook.getSheetAt(0);
			mergedErrorSet = this.getMergedErrorRowsForAta(sheet);//不能有合并行
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {//循环读取每一行
				Row row = sheet.getRow(i);
				if (row != null) {
					ComAta ata = new ComAta();
					String tmpcode = this.getCellValueForCode(row.getCell(0));
					ata.setTmpCode(tmpcode);
					ata.setAtaName(this.getCellValue(row.getCell(1)));
					ata.setEquipmentName(this.getCellValue(row.getCell(2)));
					ata.setEquipmentPicNo(this.getCellValue(row.getCell(3)));
					ata.setEquipmentTypeNo(this.getCellValue(row.getCell(4)));
					ata.setEquipmentPosition(this.getCellValue(row.getCell(5)));
					ata.setRemark(this.getCellValue(row.getCell(6)));
					ata.setExcelRow(i+1);//excel第几行
					ata.setValidFlag(ComacConstants.VALIDFLAG_YES);
					allList.add(ata);
					if("".equals(ata.getAtaName())){//判断ata的名称是否为空
						noAtaNameSet.add(ata.getExcelRow());
					}
				 }
			}
			codeFormatErrorSet = this.getCodeFormatErrorRowsForAta(allList);//code
			noParentErrorSet = this.getNotParentErrorRowsForAta(allList);
			noSameCodeErrorSet = this.getSameCodeErrorRowsForAta(allList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String errorStr = ""; 
		if(mergedErrorSet.size() != 0){
			errorStr  += "；出现合并单元格的行号：" + this.getErrorRowForHashset(mergedErrorSet);
		}
		if(noAtaNameSet.size() != 0){
			errorStr  += "；ata名称为空的行号：" + this.getErrorRowForHashset(noAtaNameSet);
		}
		if(codeFormatErrorSet.size() != 0){
			errorStr  += "；编号出错的行号：" + this.getErrorRowForHashset(codeFormatErrorSet);
		}
		if(noParentErrorSet.size() != 0){
			errorStr  += "；没有父ATA的行号：" + this.getErrorRowForHashset(noParentErrorSet);
		}
		if(noSameCodeErrorSet.size() != 0){
			errorStr  += "；ATA编号相同的行号：" + this.getErrorRowForHashset(noSameCodeErrorSet);
		}
		if(!"".equals(errorStr)){
			errorStr = errorStr.substring(1);
			msg = "导入失败！"+errorStr;
		}else {
			ArrayList<ComAta> oneList = new ArrayList<ComAta>();
			ArrayList<ComAta> twoList = new ArrayList<ComAta>();
			ArrayList<ComAta> threeList = new ArrayList<ComAta>();
			ArrayList<ComAta> fourList = new ArrayList<ComAta>();
			for(ComAta comAta : allList){
				if(comAta.getAtaLevel() == 1){
					oneList.add(comAta);
				}else if(comAta.getAtaLevel() == 2){
					twoList.add(comAta);
				}else if(comAta.getAtaLevel() == 3){
					threeList.add(comAta);
				}else if(comAta.getAtaLevel() == 4){
					fourList.add(comAta);
				}
			}
			Set<Integer> errorImportAta = comAtaBo.importComAta(serie,oneList,twoList,threeList,fourList, this.getSysUser().getUserId());
			if(errorImportAta.size() > 0){
				msg ="导入失败！第" + this.getErrorRowForHashset(errorImportAta) + "行有误";
			}
		}
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("msg", msg);
		return "ata";
	}

	/**
	 * 批量导入ATA
	* @Title: importAta
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月29日 下午1:31:54
	* @ModifiedBy zhaojunwei
	* @ModifiedOn 2015年3月30日
	* @throws
	 */
	public String importAta() {
		String msg="导入成功！";//导入成功或失败的提示信息
		ComModelSeries serie = this.getComModelSeries();//登录选中的机型
		String userId = this.getSysUser().getUserId();
		Workbook workbook = null;
		if (excelFile != null) {
			String path = excelFile.getAbsolutePath();// 获取文件的路径
			try {
				workbook = WorkbookFactory.create(new File(path));// 初始化workbook对象
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			msg= "导入的文件为空或不存在";
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setAttribute("msg", msg);
			return "ata";
		}
		
		/*List<ComAta> ataList = comAtaBo.findAllAtaSort(serie);
		if (ataList != null && ataList.size() > 0){
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setAttribute("msg", "当前机型系列已经存在ATA数据，不能再做导入操作");
			return "ata";
		}*/
		
		ArrayList<ComAta> allList = new ArrayList<ComAta>();
		Set<String> ataCodeSet= new HashSet<String>();//保存所有ata编号
		Set<Integer> noAtaNameSet= new HashSet<Integer>();//ata名称为空
		Set<Integer> mergedErrorSet= new HashSet<Integer>();//出现了合并单元格错误的行号
		Set<Integer> codeFormatErrorSet= new HashSet<Integer>();//code格式有误
		String noSameCodeError = "";//code相同
		try {
			//得到第一个sheet
			Sheet sheet = workbook.getSheetAt(0);
			mergedErrorSet = this.getMergedErrorRowsForAta(sheet);//不能有合并行
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {//循环读取每一行
				Row row = sheet.getRow(i);
				if (row != null) {
					ComAta ata = new ComAta();
					ata.setComModelSeries(serie);
					String tmpcode = this.getCellValueForCode(row.getCell(0));
					if (checkAtaCode(tmpcode)){//判断编号是否符合规则
						String s = tmpcode.substring(0,2) + "-" + tmpcode.substring(2,4) + "-" + tmpcode.substring(4,6) + "-" + tmpcode.substring(6);
						ata.setAtaCode(s);
						if (ataCodeSet.contains(s)){//判断ATA编号是否重复
							noSameCodeError = noSameCodeError + "," + tmpcode;
						} else {
							ataCodeSet.add(s);
						}
					} else {
						codeFormatErrorSet.add(i+1);
					}
					
					ata.setAtaName(this.getCellValue(row.getCell(1)));
					if("".equals(ata.getAtaName())){//判断ata的名称是否为空
						noAtaNameSet.add(i+1);
					}
					ata.setEquipmentName(this.getCellValue(row.getCell(2)));
					ata.setEquipmentPicNo(this.getCellValue(row.getCell(3)));
					ata.setEquipmentTypeNo(this.getCellValue(row.getCell(4)));
					ata.setEquipmentPosition(this.getCellValue(row.getCell(5)));
					ata.setRemark(this.getCellValue(row.getCell(6)));
					ata.setValidFlag(2);
					ata.setCreateDate(new Date());
					ata.setCreateUser(userId);
					allList.add(ata);
				 }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String errorStr = ""; 
		if(mergedErrorSet.size() != 0){
			errorStr  += "；出现合并单元格的行号：" + this.getErrorRowForHashset(mergedErrorSet);
		}
		if(noAtaNameSet.size() != 0){
			errorStr  += "；ata名称为空的行号：" + this.getErrorRowForHashset(noAtaNameSet);
		}
		if(codeFormatErrorSet.size() != 0){
			errorStr  += "；编号出错的行号：" + this.getErrorRowForHashset(codeFormatErrorSet);
		}
		/*if(!"".equals(noSameCodeError)){
			errorStr  += "；ATA编号相同的编号：" + noSameCodeError.substring(1);
		}*/
		if(!"".equals(errorStr)){
			errorStr = errorStr.substring(1);
			msg = "导入失败！"+errorStr;
		}else {
			for (ComAta ata : allList){
				comAtaBo.save(ata);
			}
			
			HashMap<String, String> map = comAtaDao.importAta(serie.getModelSeriesId(), "1");
			if ("导入失败".equals(map.get("res"))){
				msg = "导入失败！" + map.get("msg");
			}
		}
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("msg", msg);
		return "ata";
	}
	
	/**
	 * 批量导入供应商
	* @Title: importVendor
	* @Description:
	* @return
	* @author 张建民
	* @date 2015年6月11日
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public String importVendor() {
		String msg="导入成功！";//导入成功或失败的提示信息
		ComModelSeries ms = this.getComModelSeries();//登录选中的机型
		String msId = ms.getModelSeriesId();
		Workbook workbook = null;
		if (excelFile != null) {
			String path = excelFile.getAbsolutePath();// 获取文件的路径
			try {
				workbook = WorkbookFactory.create(new File(path));// 初始化workbook对象
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			msg= "导入的文件为空或不存在";
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setAttribute("msg", msg);
			return "vendor";
		}
		
		String[] columnName = new String[]{"供应商编号", "供应商名称", "备注"};
		String[] columnCode = new String[]{"vendorCode", "vendorName", "remark"};
		Integer[] columnLength = new Integer[]{20, 500, 4000};
		
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();//保存EXCEL中的数据
		List<String> key = new ArrayList<String>();//保存EXCEL中数据的主键
		
		try {
			Sheet sheet = workbook.getSheetAt(0);//读取工作簿
			
			//读取EXCEL中数据，并且做数据验证，验证通过的数据保存到list中
			for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++){//rowNum=1，是因为要去除EXCEL中1行表头
				Row row = sheet.getRow(rowNum);
				Map<String, String> map = new HashMap<String, String>();
				
				if (row != null){
					//读取一行记录的所有数据
					for (int i = 0; i < columnCode.length; i++){
						Cell cell = row.getCell(i);
						map.put(columnCode[i], getCellValueForCode(cell).trim());
					}
					
					//判断这行记录是否为空。这种判断是用在EXCEL中有格式信息，但是没有数据的情况下
					int y = 0;
					for (int i = 0; i < columnCode.length; i++){
						if ("".equals(map.get(columnCode[i]))){
							y++;
						}
					}
					if (y == columnCode.length){
						continue;
					}
					
					//验证数据
					for (int i = 0; i < columnCode.length; i++){
						String s = map.get(columnCode[i]);
						
						if (s.length() > columnLength[i]){
							msg = "第" + (rowNum + 1) + "行第" + (i + 1) + "列的" + columnName[i] + "的长度过长!";
							HttpServletRequest request = ServletActionContext.getRequest();
							request.setAttribute("msg", msg);
							return "vendor";
						}
						
						if (i == 0){
							if (null == s || "".equals(s)){
								msg = "第" + (rowNum + 1) + "行第" + (i + 1) + "列的" + columnName[i] + "不能为空!";
								HttpServletRequest request = ServletActionContext.getRequest();
								request.setAttribute("msg", msg);
								return "vendor";
							}
						} else if (i == 1){
							if(null == s || "".equals(s)){
								msg = "第" + (rowNum + 1) + "行第" + (i + 1) + "列的" + columnName[i] + "不能为空!";
								HttpServletRequest request = ServletActionContext.getRequest();
								request.setAttribute("msg", msg);
								return "vendor";
							}
						}
					}
					
					//验证EXCEL中的数据是否有供应商编号重复
					if (key.contains(map.get(columnCode[0]))){
						msg =  "第" + (rowNum + 1) + "行" + columnName[0] + "重复!";
						HttpServletRequest request = ServletActionContext.getRequest();
						request.setAttribute("msg", msg);
						return "vendor";
					} else {
						list.add(map);
						key.add(map.get(columnCode[0]));
					}
				} else {
					continue;
				}
			}
			
			//对EXCEL数据做增量导入
			for (Map<String, String> map : list){
				ComVendor info = new ComVendor();
				
				DetachedCriteria dc = DetachedCriteria.forClass(ComVendor.class);
				dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
				dc.add(Restrictions.eq("vendorCode", map.get(columnCode[0])));
				dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
				List<ComVendor> vList = comAtaBo.findByCritera(dc);
				//如果数据库中数据已经存在，则做更新操作；如果数据库中数据不存在，则做插入操作
				if (vList != null && vList.size() > 0){
					info = vList.get(0);
					info.setVendorName(map.get(columnCode[1]));
					info.setRemark(map.get(columnCode[2]));
					comAtaBo.saveOrUpdate(info);
				} else {
					info.setComModelSeries(ms);
					info.setVendorCode(map.get(columnCode[0]));
					info.setVendorName(map.get(columnCode[1]));
					info.setRemark(map.get(columnCode[2]));
					info.setValidFlag(1);
					comAtaBo.saveOrUpdate(info);
				}
			} 
		} catch (Exception e) {
			//e.printStackTrace();		
			msg="读取excel文件失败！";
		}
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("msg", msg);
		return "vendor";
	}
	
	private Set<Integer> getSameCodeErrorRowsForAta(ArrayList<ComAta> comAtaList) {
		Set<Integer> errorRows = new HashSet<Integer>();
		if(comAtaList == null || comAtaList.size() == 0){
			return errorRows;
		}
		for(int i=0; i<comAtaList.size()-1; i++){
			if(null==comAtaList.get(i).getAtaCode()) continue;
			for(int j=i+1; j<comAtaList.size(); j++){
				if(null==comAtaList.get(j).getAtaCode()) continue;
				if(comAtaList.get(i).getAtaCode().equals(comAtaList.get(j).getAtaCode())){
					errorRows.add(comAtaList.get(i).getExcelRow());
					errorRows.add(comAtaList.get(j).getExcelRow());
				}
			}
		}
		return errorRows;
	}

	/**
	 * 获取没父ata的行号
	* @Title: getNotParentErrorRowsForAta
	* @Description:
	* @param allList
	* @return
	* @author samual
	* @date 2015年1月6日 下午2:16:16
	* @throws
	 */
	private Set<Integer> getNotParentErrorRowsForAta(ArrayList<ComAta> comAtaList) {
		Set<Integer> errorRows = new HashSet<Integer>();
		List<ComAta> dataAtaList = comAtaBo.findAllAtaSort(this.getComModelSeries());
		for(ComAta comAta : comAtaList){
			if(comAta.getValidFlag() == ComacConstants.VALIDFLAG_YES){
				if(comAta.getAtaLevel() == 1){
					continue;
				}else if(this.existParentAta(comAta, dataAtaList)){//判断数据库中存不存在该ATA的父级
					continue;
				}else if (this.existParentAta(comAta, comAtaList)) {//判断导入的Excel中存不存在该ATA的父级
					continue;
				}else {
					errorRows.add(comAta.getExcelRow());
				}
			}
		}
		return errorRows;
	}
	
	/**
	 * 判断是否有父类的ata
	* @Title: existParentAta
	* @Description:
	* @param comAta
	* @param ataList
	* @return
	* @author samual
	* @date 2015年1月6日 下午3:02:25
	* @ModifiedBy zhaojunwei
	* @ModifiedOn 2015年3月30日
	* @throws
	 */
	private boolean existParentAta(ComAta comAta, List<ComAta> ataList){
		if(ataList == null || ataList.size() == 0){
			return false;
		}
		if(comAta.getAtaLevel() == 2){
			for(ComAta comAtaT : ataList){
				if(comAtaT.getAtaLevel() == 1 && comAtaT.getAtaCode() != null && comAtaT.getAtaCode().length() == 11){
					if(comAtaT.getAtaCode().equals(comAta.getAtaCode().substring(0, 2)+"-00-00-00")){
						return true;
					}
				}
			}
		}else if(comAta.getAtaLevel() == 3){
			for(ComAta comAtaT : ataList){
				if(comAtaT.getAtaLevel() == 2 && comAtaT.getAtaCode() != null && comAtaT.getAtaCode().length() == 11){
					if(comAtaT.getAtaCode().equals(comAta.getAtaCode().substring(0, 5) + "-00-00")){
						return true;
					}
				}
			}
		}else if(comAta.getAtaLevel() == 4){
			for(ComAta comAtaT : ataList){
				if(comAtaT.getAtaLevel() == 3 && comAtaT.getAtaCode() != null && comAtaT.getAtaCode().length() == 11){
					if(comAtaT.getAtaCode().equals(comAta.getAtaCode().substring(0, 8) + "-00")){
						return true;
					}
				}
			}
		}
		return false;
	}
	/**
	 * 得到五列code有误的行号
	* @Title: getCodeFormatErrorRowsForAta
	* @Description:
	* @param comAtaList
	* @return
	* @author samual
	* @date 2015年1月6日 下午12:47:23
	* @ModifiedBy zhaojunwei
	* @ModifiedOn 2015年3月30日
	* @throws
	 */
	private Set<Integer> getCodeFormatErrorRowsForAta(ArrayList<ComAta> comAtaList) {
		Set<Integer> errorRows = new HashSet<Integer>();
		for(ComAta comAta : comAtaList){
			if(!checkAtaCode(comAta)){
				errorRows.add(comAta.getExcelRow());
				comAta.setValidFlag(ComacConstants.VALIDFLAG_NO);
				comAta.setAtaLevel(9);
				continue;
			}
			String tmpcode = comAta.getTmpCode();
			comAta.setAtaCode(tmpcode.substring(0,2) + "-" + tmpcode.substring(2,4) + "-" + tmpcode.substring(4,6) + "-" + tmpcode.substring(6));
		}
		return errorRows;
	}

	/**
	 * 判断code是否有效
	* @Title: checkAtaCode
	* @Description:
	* @param ataCode
	* @return
	* @author samual
	* @date 2015年1月6日 下午12:44:06
	* @ModifiedBy zhaojunwei
	* @ModifiedOn 2015年3月30日
	* @throws
	 */
	private boolean checkAtaCode(ComAta comAta) {
		//1.ataCode格式
		String tmpCode = comAta.getTmpCode();
		if(tmpCode == null || "".equals(tmpCode)){
			return false;
		}
		if(tmpCode.length() != 8){
			return false;
		}
		try {
			Integer.parseInt(tmpCode);
		} catch (Exception e) {
			return false;
		}
		
		//判断ATA的级别
//		if(tmpCode.substring(2).equals("000000")){
//			comAta.setAtaLevel(1);
//		}else if(Integer.parseInt(tmpCode.substring(2,4))>0&&tmpCode.substring(4).equals("0000")){
//			comAta.setAtaLevel(2);
//		}else if(Integer.parseInt(tmpCode.substring(2,4))>0&&Integer.parseInt(tmpCode.substring(4,6))>0&&tmpCode.substring(6).equals("00")){
//			comAta.setAtaLevel(3);
//		}else if(Integer.parseInt(tmpCode.substring(2,4))>0&&Integer.parseInt(tmpCode.substring(4,6))>0&&Integer.parseInt(tmpCode.substring(6))>0){
//			comAta.setAtaLevel(4);
//		}else{
//			return false;
//		}

		return true;
	}
	
	/**
	 * 判断code是否有效
	* @Title: checkAtaCode
	* @Description:
	* @param ataCode
	* @return
	* @author samual
	* @date 2015年1月6日 下午12:44:06
	* @ModifiedBy zhaojunwei
	* @ModifiedOn 2015年3月30日
	* @throws
	 */
	private boolean checkAtaCode(String ataCode) {
		if(ataCode == null || "".equals(ataCode)){
			return false;
		}
		if(ataCode.length() != 8){
			return false;
		}
		try {
			Integer.parseInt(ataCode);
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 获取单元格的值
	* @Title: getCellValue
	* @Description:
	* @param cell
	* @return
	* @author samual
	* @date 2014年12月30日 上午11:06:39
	* @throws
	 */
	public String getCellValueForCode(Cell cell){
	    if(cell == null) return "";
	    if(cell.getCellType() == Cell.CELL_TYPE_STRING){
	        return cell.getStringCellValue().trim();
	    }else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
	        return String.valueOf(cell.getBooleanCellValue()).trim();
	    }else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA){
	        return cell.getCellFormula().trim();
	    }else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
	        return String.valueOf((int)cell.getNumericCellValue()).trim();
	    }
	    return "";
	}
	
	/**
	 * 获取单元格的值
	* @Title: getCellValue
	* @Description:
	* @param cell
	* @return
	* @author samual
	* @date 2014年12月30日 上午11:06:39
	* @throws
	 */
	public String getCellValue(Cell cell){
	    if(cell == null) return "";
	    if(cell.getCellType() == Cell.CELL_TYPE_STRING){
	        return cell.getStringCellValue().trim();
	    }else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
	        return String.valueOf(cell.getBooleanCellValue()).trim();
	    }else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA){
	        return cell.getCellFormula().trim();
	    }else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
	        return String.valueOf(cell.getNumericCellValue()).trim();
	    }
	    return "";
	}
	

	/**
	 * 获取ata有合并单元格的行号
	* @Title: getMergedErrorRowsForArea
	* @Description:
	* @param sheet
	* @return
	* @author samual
	* @date 2014年12月30日 下午3:21:49
	* @throws
	 */
	private Set<Integer> getMergedErrorRowsForAta(Sheet sheet){
		Set<Integer> errorRows = new HashSet<Integer>();
		//1.列没有跨度合并，只有1,2,3,7,8列有行合并
		//2.合并开始行相同时，结束行也相同
		for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
//			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
//			int lastRow = range.getLastRow();
			if(firstColumn <= 10 || firstRow <= sheet.getLastRowNum()){
				errorRows.add(firstRow + 1);
			}
		}
		return errorRows;
	}
	
	/**
	 * 获取区域合并有误的单元格行
	* @Title: getMergedErrorRowsForArea
	* @Description:
	* @param sheet
	* @return
	* @author samual
	* @date 2014年12月30日 下午3:21:49
	* @throws
	 */
	private Set<Integer> getMergedErrorRowsForArea(Sheet sheet){
		Set<Integer> errorRows = new HashSet<Integer>();
		//1.列没有跨度合并，只有1,2,3,7,8列有行合并
		//2.合并开始行相同时，结束行也相同
		for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
			CellRangeAddress range1 = sheet.getMergedRegion(i);
			int firstColumn1 = range1.getFirstColumn();
			int lastColumn1 = range1.getLastColumn();
			int firstRow1 = range1.getFirstRow();
			int lastRow1 = range1.getLastRow();
			if(firstColumn1 <= 7 || firstRow1 <= sheet.getLastRowNum()){
				if(firstColumn1 != lastColumn1){
					errorRows.add(firstRow1 + 1);
				}
				if(firstColumn1 >= 3 && firstColumn1 <= 5){
					errorRows.add(firstRow1 + 1);
				}
				for (int j = i+1; j < sheet.getNumMergedRegions(); j++) {
					CellRangeAddress range2 = sheet.getMergedRegion(j);
					int firstColumn2 = range2.getFirstColumn();
					int lastColumn2 = range2.getLastColumn();
					int firstRow2 = range2.getFirstRow();
					int lastRow2 = range2.getLastRow();
					if(firstColumn2 <= 7 || firstRow2 <= sheet.getLastRowNum()){
						//开始行相同，结束行业要相同
						if(firstRow1 == firstRow2 && lastRow1 != lastRow2){
							errorRows.add(firstRow1 + 1);
						}
						if(firstRow1 != firstRow2 && lastRow1 == lastRow2){
							errorRows.add(firstRow1 + 1);
						}
						//开始列相同，结束列也要相同
						if(firstColumn1 == firstColumn2 && lastColumn1 != lastColumn2){
							errorRows.add(firstRow1 + 1);
						}
						if(firstColumn1 != firstColumn2 && lastColumn1 == lastColumn2){
							errorRows.add(firstRow1 + 1);
						}
					}
				}
			}
		}
		return errorRows;
	}
	
	/**
	 * 判断指定的单元格是否是合并单元格
	* @Title: isMergedRegion
	* @Description:
	* @param sheet
	* @param row
	* @param column
	* @return
	* @author samual
	* @date 2014年12月30日 上午11:02:54
	* @throws
	 */
	private boolean isMergedRegion(Sheet sheet,int row ,int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if(row >= firstRow && row <= lastRow){
				if(column >= firstColumn && column <= lastColumn){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 获取合并单元格的值
	* @Title: getMergedRegionValue
	* @Description:
	* @param sheet
	* @param row
	* @param column
	* @return
	* @author samual
	* @date 2014年12月30日 上午10:58:22
	* @throws
	 */
	public String getMergedRegionValue(Sheet sheet ,int row , int column){
	    int sheetMergeCount = sheet.getNumMergedRegions();
	    for(int i=0; i<sheetMergeCount; i++){
	        CellRangeAddress ca = sheet.getMergedRegion(i);
	        int firstColumn = ca.getFirstColumn();
	        int lastColumn = ca.getLastColumn();
	        int firstRow = ca.getFirstRow();
	        int lastRow = ca.getLastRow();
	        if(row >= firstRow && row <= lastRow){
	            if(column >= firstColumn && column <= lastColumn){
	                Row fRow = sheet.getRow(firstRow);
	                Cell fCell = fRow.getCell(firstColumn);
	                return getCellValue(fCell).trim();
	            }
	        }
	    }
	    return null;
	}
	
	/**
	 * 判读区域号是否有效
	 * 1.3位
	 * 2.能转integet
	 * 3.首数字不能为0
	* @Title: checkAreaCode
	* @Description:
	* @param areaCode
	* @return
	* @author samual
	* @date 2014年12月30日 下午4:23:19
	* @throws
	 */
	private boolean checkAreaCode(String areaCode){
		if(areaCode == null || "".equals(areaCode)){
			return false;
		}
		if(areaCode.length() != 3){
			return false;
		}
		try {
			Integer.parseInt(areaCode);
		} catch (Exception e) {
			return false;
		}
		if("0".equals(areaCode.subSequence(0, 1))){
			return false;
		}
		return true;
	}
	
	/**
	 * 获取没有父区域的行号(第二级)
	* @Title: getRowNoParentForSecondArea
	* @Description:
	* @param parentAreaList 父区域
	* @param childAreaLIst
	* @param comModelSeries
	* @return
	* @author samual
	* @date 2014年12月31日 上午9:09:36
	* @throws
	 */
	private Set<Integer> getRowNoParentForSecondArea(ArrayList<ComArea> parentAreaList, ArrayList<ComArea> childAreaLIst, ComModelSeries comModelSeries){
		Set<Integer> noParentAreaRow = new HashSet<Integer>();
		List<ComArea> databaseOneAreaList = comAreaBo.getAreaListForLevel(comModelSeries, 1);
		for(ComArea childArea : childAreaLIst){
			boolean flag = false;
			//1.是否在excel中存在
			for(ComArea parentArea : parentAreaList){
				if(parentArea.getAreaCode().substring(0, 1).equals(childArea.getAreaCode().substring(0, 1))){
					flag = true;
					break;
				}
			}
			//2.是否在数据库中存在
			if(!flag){
				for(ComArea databaseArea : databaseOneAreaList){
					if(checkAreaCode(databaseArea.getAreaCode()) && databaseArea.getAreaCode().substring(0, 1).equals(childArea.getAreaCode().substring(0, 1))){
						flag = true;
						break;
					}
				}
			}
			if(!flag){
				noParentAreaRow.add(Integer.parseInt(childArea.getAreaId()));
			}
		}
		return noParentAreaRow;
	}

	/**
	 * 获取没有父区域的行号(第三级)
	* @Title: getRowNoParentForThridArea
	* @Description:
	* @param parentAreaList
	* @param childAreaLIst
	* @param comModelSeries
	* @return
	* @author samual
	* @date 2014年12月31日 上午9:18:23
	* @throws
	 */
	private Set<Integer> getRowNoParentForThridArea(ArrayList<ComArea> parentAreaList, ArrayList<ComArea> childAreaLIst, ComModelSeries comModelSeries){
		Set<Integer> noParentAreaRow = new HashSet<Integer>();
		List<ComArea> databaseOneAreaList = comAreaBo.getAreaListForLevel(comModelSeries, 2);
		for(ComArea childArea : childAreaLIst){
			boolean flag = false;
			//1.是否在excel中存在
			for(ComArea parentArea : parentAreaList){
				if(parentArea.getAreaCode().substring(0, 2).equals(childArea.getAreaCode().substring(0, 2))){
					flag = true;
					break;
				}
			}
			//2.是否在数据库中存在
			if(!flag){
				for(ComArea databaseArea : databaseOneAreaList){
					if(checkAreaCode(databaseArea.getAreaCode()) && databaseArea.getAreaCode().substring(0, 2).equals(childArea.getAreaCode().substring(0, 2))){
						flag = true;
						break;
					}
				}
			}
			if(!flag){
				noParentAreaRow.add(Integer.parseInt(childArea.getAreaId()));
			}
		}
		return noParentAreaRow;
	}
	
	private String getErrorRowForHashset(Set<Integer> errorRowNumSet){
		String errorRow = "";
		if(errorRowNumSet.size() != 0){
			List<Integer> list = new ArrayList<Integer>();
			for(Integer value : errorRowNumSet){
	            list.add(value);
	        }
	        Collections.sort(list);
	        for(Integer value : list){
	        	errorRow += value + ",";
	        }
	        if(errorRow.length() > 0){
	        	errorRow = errorRow.substring(0, errorRow.length() - 1);
	        }
		}
        return errorRow;
	}

	
	private String changeDoubelToIntergerForStr(String areaCode) {
		int index = areaCode.indexOf(".");
		if(index > 0){
			return areaCode.substring(0, index);
		}else{
			return areaCode;
		}
	}
	
//	public static void main(String[] args){
//		ImportExcelAction aa = new ImportExcelAction();
//		System.out.println(aa.changeDoubelToIntergerForStr("123f.s"));
//	}
}
