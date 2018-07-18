package com.rskytech.struct.action;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.util.JavaScriptUtils;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IZa7Dao;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.basedata.bo.IFileUploadBo;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusMatrix;
import com.rskytech.pojo.S3Crack;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.SStep;
import com.rskytech.struct.bo.IS1Bo;
import com.rskytech.struct.bo.IS3Bo;
import com.rskytech.struct.bo.IS4Bo;
import com.rskytech.struct.bo.ISsiStepBo;
import com.rskytech.task.bo.ITaskMsgDetailBo;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class S3Action extends BaseAction {
	private static final long serialVersionUID = -3404915867521340482L;
	private IS3Bo s3Bo;
	private String siCode;
	private String siTitle;
	private File docFile;
	private String docFileFileName;
	private IS1Bo s1Bo;
	private String ssiId;
	private int isMaintain;
	private String anaFlg;
	private int[] step;
	private String table1RowValue=null;//稠密度等级
	private String table1CellValue=null;//目视等级
	private String table3RowValue=null;//接近等级
	private String table2CellValue=null;//光照等级
	private String table4RowValue=null;//条件等级
	private String table2RowValue=null;//表面等级  ?
	private String table3CellValue=null;//尺寸等级
	private String table4CellValue=null;//实用性等级
	private IComAreaBo comAreaBo;
	private ISsiStepBo ssiStepBo;
	private ITaskMsgDetailBo taskMsgDetailBo;
	private IZa7Dao za7Dao;
	
	public String initS3(){
		ComUser thisUser = getSysUser();
		if (thisUser == null){
			return SUCCESS;//现在返回success都是直接跳转jsp,我们共同的jsp中存在session判断与提示
		}
		SMain sMain=(SMain)s1Bo.loadById(SMain.class, ssiId);
		if(sMain.getComAta()!=null){
			ComAta comAta = sMain.getComAta();
			this.siCode=comAta.getAtaCode();
			this.siTitle=JavaScriptUtils.javaScriptEscape(comAta.getAtaName());
		}else{
			this.siCode=sMain.getAddCode();
			this.siTitle=JavaScriptUtils.javaScriptEscape(sMain.getAddName());
		}
		List<SStep> step1=s1Bo.getSstepBySssiId(ssiId);
		step=ssiStepBo.initStep(ssiId, step1.get(0), "S3");
		return SUCCESS;
	}
	
	/**
	 * 得到矩阵
	 * @return
	 */
	public String getMatrix(){
		JSONObject jsonObject=new JSONObject();
		List jsonList = new ArrayList();
		HashMap jsonFieldList=null;
		List<CusMatrix> list=s3Bo.getMatrixByAnaFlg(anaFlg,getComModelSeries().getModelSeriesId());
			if(list!=null){
				for(CusMatrix cusMatrix:list){
					jsonFieldList=new HashMap();
					jsonFieldList.put("anaFlg", cusMatrix.getAnaFlg());
					jsonFieldList.put("matrixId", cusMatrix.getMatrixId());
					jsonFieldList.put("matrixRow", cusMatrix.getMatrixRow());
					jsonFieldList.put("matrixCol", cusMatrix.getMatrixCol());
					jsonFieldList.put("matrixValue", cusMatrix.getMatrixValue());	
					jsonList.add(jsonFieldList);
				}
				jsonObject.element("matrix", jsonList);
			}else{
				jsonObject.element("matrix", "null");
			}
		writeToResponse(jsonObject.toString());
		return null;
	}
	
	/**
	 * 得到Sdi数据
	 * @return
	 * @throws Exception
	 */
	public String getSdiRecords() throws Exception{
		List<Object[]> list=s3Bo.getS3Records(ssiId);
		List<HashMap> listJson = new ArrayList<HashMap>();
		JSONObject json = new JSONObject();
		HashMap jsonFieldList=null;
		String areaCode=null;
		if(list!=null){
			for(Object[] obj:list){
				if(ComacConstants.SDI.equals(obj[3])){
					jsonFieldList = new HashMap();
					jsonFieldList.put("id", obj[0]);
					jsonFieldList.put("ssiName",obj[1]);
					jsonFieldList.put("isAdEffect",obj[2]);
					if(obj[3]==null){
						jsonFieldList.put("taskType",ComacConstants.GVI);
					}else{
						jsonFieldList.put("taskType",obj[3]);
					}
					jsonFieldList.put("basicCrack",obj[4]);
					jsonFieldList.put("materialSize",obj[5]);
					jsonFieldList.put("edgeEffect",obj[6]);
					jsonFieldList.put("detectCrack",obj[7]);
					jsonFieldList.put("lc",obj[8]);
					jsonFieldList.put("lo",obj[9]);
					jsonFieldList.put("detailCrack",obj[10]);
					if(obj[11]!=null){
						jsonFieldList.put("taskIntervalRepeat",obj[11]); 
					 }else{
						 jsonFieldList.put("taskIntervalRepeat",""); 
					 }
					
					jsonFieldList.put("isOk",obj[12]);
					if(obj[13]!=null){
						jsonFieldList.put("remark",obj[13]); 
					 }else{
						 jsonFieldList.put("remark",""); 
					 }
					
					jsonFieldList.put("picUrl",obj[14]);
					jsonFieldList.put("detailSdi",obj[15]);
					if(obj[16]!=null){
						jsonFieldList.put("taskInterval",obj[16]); 
					 }else{
						 jsonFieldList.put("taskInterval",""); 
					 }
					
					jsonFieldList.put("s1Id", obj[17]);
					jsonFieldList.put("intOut", obj[18]);
					areaCode=comAreaBo.getAreaCodeByAreaId(obj[19].toString());
					if(areaCode!=null){
						jsonFieldList.put("ownArea",areaCode);
					}else{
						jsonFieldList.put("ownArea","");
					}
					if(obj[20]!=null){
						jsonFieldList.put("taskId",obj[20].toString());
						jsonFieldList.put("tempTaskId",obj[20].toString());
					}else{
						jsonFieldList.put("taskId","");
						jsonFieldList.put("tempTaskId","");
					}
					listJson.add(jsonFieldList);
				}
			}
		}
		json.element("s3", listJson);
		writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 上传图片
	 * @return
	 */
	private IFileUploadBo fileUploadBo;
	public IFileUploadBo getFileUploadBo() {
		return fileUploadBo;
	}

	public void setFileUploadBo(IFileUploadBo fileUploadBo) {
		this.fileUploadBo = fileUploadBo;
	}

	public String uploadPic() throws Exception{
		JSONObject json = new JSONObject();
		FileInputStream docFileSize = null;
		if (docFile != null) {
			docFileSize = new FileInputStream(docFile);
			if (docFileSize.available() > ComacConstants.ALLOW_IMAGE_FILE_SIZE) {
				json = this.putJsonOKFlag(json, false);
				json.put("msg", "上传文件大小不能超过5M！");
				this.writeToResponse(json.toString());
				return null;
			}
		}

		try {
			String fileUrl = null;
			if (docFile != null && docFileFileName != null) {
				fileUrl = fileUploadBo.uploadFile(docFile, docFileFileName,
						ComacConstants.FILE_SAVE_PATH+"s3/",null);
				json.put("success", true);
				
				json.put("msg",fileUrl);
			}
			
		} catch (Exception e) {
			json.put("failure", true);
			json.put("msg", e.getMessage());
		}
		this.writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 保存S3数据
	 * @return
	 */
	private IS4Bo s4Bo;
	Object[] str=null;
	public String saveS3Records(){
		String jsonData = this.getJsonData();
		List<String> arr=s3Bo.saveS3Records(jsonData, ssiId, getSysUser(),getComModelSeries());
		if(arr.size()>1){
			String[] areaId=null;
			for(int i =0;i<arr.size()-1;i++){
				areaId = arr.get(i).split(",");
				for(String str : areaId){
					taskMsgDetailBo.updateZa7Status(getComModelSeries().getModelSeriesId(), getSysUser().getUserId(), str);
				}
			}
			za7Dao.cleanTaskInterval(getComModelSeries().getModelSeriesId());
		}
		this.ssiStepBo.changeStatus(ssiId);
		writeToResponse(arr.get(arr.size()-1));
		return null;
	}
	
	/**
	 * 得到gd数据
	 * @return
	 * @throws Exception
	 */
	public String getGDRecords() throws Exception{
		List<Object[]> list=s3Bo.getS3Records(ssiId);
		List<HashMap> listJson = new ArrayList<HashMap>();
		JSONObject json = new JSONObject();
		S3Crack s3c= null;
		HashMap jsonFieldList=null;
		String areaCode=null;
		if(list!=null){
			for(Object[] obj:list){
				if(!ComacConstants.SDI.equals(obj[3])){
				jsonFieldList = new HashMap();
					if (obj[0]!= null) {
						s3c = s3Bo.getS3Crack((obj[0].toString()));
						if (s3c != null) {
							jsonFieldList.put("s3cId", s3c.getCrackId());
							jsonFieldList.put("densityLevel",
									s3c.getDensityLevel()+"");// 稠密度等级
							jsonFieldList.put("lookLevel", s3c.getLookLevel()+"");// 目视等级
							jsonFieldList
									.put("reachLevel", s3c.getReachLevel()+"");// 接近等级
							jsonFieldList
									.put("lightLevel", s3c.getLightLevel()+"");// 光照等级
							jsonFieldList.put("conditionLevel",
									s3c.getConditionLevel()+"");// 条件等级
							jsonFieldList.put("surfaceLevel",
									s3c.getSurfaceLevel()+"");// 表面等级
							jsonFieldList.put("sizeLevel", s3c.getSizeLevel()+"");// 尺寸等级
							jsonFieldList.put("doLevel", s3c.getDoLevel()+"");// 实用性等级
						}
					}
				jsonFieldList.put("id", obj[0]);
				jsonFieldList.put("ssiName",obj[1]);
				jsonFieldList.put("isAdEffect",obj[2]);
				if(obj[3]==null){
					jsonFieldList.put("taskType",ComacConstants.GVI);
				}else{
					jsonFieldList.put("taskType",obj[3]);
				}
				jsonFieldList.put("basicCrack",obj[4]);
				jsonFieldList.put("materialSize",obj[5]);
				jsonFieldList.put("edgeEffect",obj[6]);
				jsonFieldList.put("detectCrack",obj[7]);
				jsonFieldList.put("lc",obj[8]);
				jsonFieldList.put("lo",obj[9]);
				jsonFieldList.put("detailCrack",obj[10]);
				if(obj[11]!=null){
					jsonFieldList.put("taskIntervalRepeat",obj[11]); 
				 }else{
					 jsonFieldList.put("taskIntervalRepeat",""); 
				 }
				
				jsonFieldList.put("isOk",obj[12]);
				if(obj[13]!=null){
					jsonFieldList.put("remark",obj[13]);
				 }else{
					 jsonFieldList.put("remark",""); 
				 }
				jsonFieldList.put("picUrl",obj[14]);
				jsonFieldList.put("picUrlIsNull",obj[14]==null||obj[14]==""?"无":"有");
//				jsonFieldList.put("detailSdi",obj[15]);
				if(obj[16]!=null){
					jsonFieldList.put("taskInterval",obj[16]);
				 }else{
					jsonFieldList.put("taskInterval","");
				 }
				jsonFieldList.put("s1Id", obj[17]);
				jsonFieldList.put("intOut", obj[18]);
				areaCode=comAreaBo.getAreaCodeByAreaId(obj[19].toString());
				if(areaCode!=null){
					jsonFieldList.put("ownArea",areaCode);
				}else{
					jsonFieldList.put("ownArea","");
				}
				if(obj[20]!=null){
					jsonFieldList.put("taskId",obj[20].toString());
				}else{
					jsonFieldList.put("taskId","");
				}
				listJson.add(jsonFieldList);
				}
			}
		}
		json.element("s3", listJson);
		writeToResponse(json.toString());
		return null;
	}
	public IS3Bo getS3Bo() {
		return s3Bo;
	}

	public void setS3Bo(IS3Bo s3Bo) {
		this.s3Bo = s3Bo;
	}

	public String getSiCode() {
		return siCode;
	}

	public void setSiCode(String siCode) {
		this.siCode = siCode;
	}

	public String getSiTitle() {
		return siTitle;
	}

	public void setSiTitle(String siTitle) {
		this.siTitle = siTitle;
	}

	public File getDocFile() {
		return docFile;
	}

	public void setDocFile(File docFile) {
		this.docFile = docFile;
	}

	public String getDocFileFileName() {
		return docFileFileName;
	}

	public void setDocFileFileName(String docFileFileName) {
		this.docFileFileName = docFileFileName;
	}

	public IS1Bo getS1Bo() {
		return s1Bo;
	}

	public void setS1Bo(IS1Bo s1Bo) {
		this.s1Bo = s1Bo;
	}

	public String getSsiId() {
		return ssiId;
	}

	public void setSsiId(String ssiId) {
		this.ssiId = ssiId;
	}

	public int getIsMaintain() {
		return isMaintain;
	}

	public void setIsMaintain(int isMaintain) {
		this.isMaintain = isMaintain;
	}

	public String getAnaFlg() {
		return anaFlg;
	}

	public void setAnaFlg(String anaFlg) {
		this.anaFlg = anaFlg;
	}

	public int[] getStep() {
		return step;
	}

	public void setStep(int[] step) {
		this.step = step;
	}

	public String getTable1RowValue() {
		return table1RowValue;
	}

	public void setTable1RowValue(String table1RowValue) {
		this.table1RowValue = table1RowValue;
	}

	public String getTable1CellValue() {
		return table1CellValue;
	}

	public void setTable1CellValue(String table1CellValue) {
		this.table1CellValue = table1CellValue;
	}

	public String getTable3RowValue() {
		return table3RowValue;
	}

	public void setTable3RowValue(String table3RowValue) {
		this.table3RowValue = table3RowValue;
	}

	public String getTable2CellValue() {
		return table2CellValue;
	}

	public void setTable2CellValue(String table2CellValue) {
		this.table2CellValue = table2CellValue;
	}

	public String getTable4RowValue() {
		return table4RowValue;
	}

	public void setTable4RowValue(String table4RowValue) {
		this.table4RowValue = table4RowValue;
	}

	public String getTable2RowValue() {
		return table2RowValue;
	}

	public void setTable2RowValue(String table2RowValue) {
		this.table2RowValue = table2RowValue;
	}

	public String getTable3CellValue() {
		return table3CellValue;
	}

	public void setTable3CellValue(String table3CellValue) {
		this.table3CellValue = table3CellValue;
	}

	public String getTable4CellValue() {
		return table4CellValue;
	}

	public void setTable4CellValue(String table4CellValue) {
		this.table4CellValue = table4CellValue;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}

	public IS4Bo getS4Bo() {
		return s4Bo;
	}

	public void setS4Bo(IS4Bo s4Bo) {
		this.s4Bo = s4Bo;
	}

	public ISsiStepBo getSsiStepBo() {
		return ssiStepBo;
	}

	public void setSsiStepBo(ISsiStepBo ssiStepBo) {
		this.ssiStepBo = ssiStepBo;
	}

	public ITaskMsgDetailBo getTaskMsgDetailBo() {
		return taskMsgDetailBo;
	}

	public void setTaskMsgDetailBo(ITaskMsgDetailBo taskMsgDetailBo) {
		this.taskMsgDetailBo = taskMsgDetailBo;
	}

	public IZa7Dao getZa7Dao() {
		return za7Dao;
	}

	public void setZa7Dao(IZa7Dao za7Dao) {
		this.za7Dao = za7Dao;
	}
	
}
