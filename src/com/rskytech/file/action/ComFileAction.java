package com.rskytech.file.action;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.file.bo.IComFileBo;
import com.rskytech.pojo.ComDirectory;
import com.rskytech.pojo.ComFile;
import com.rskytech.pojo.ComUser;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ComFileAction extends BaseAction {
	private static final long serialVersionUID = 7825421787426268970L;
	private IComFileBo comFileBo;
	private String cid; 
	private String modelSeriesId;
	private File docFile;
	private String docFileFileName;
	private String docFileContentType;
	private String fileName;
	private String isChooseFile;

	public String init(){		
		return SUCCESS;
	}
	
	/**
	 * 查询目录树，根据上级节点ID查找下级，判断查询到的目录是否还有下级
	 * zhouli
	 * createdate 2012/9/4 
	 * @return
	 * @throws IOException
	 */
	public String jsonComDirectoryTreeLoader() throws IOException {
		modelSeriesId = getComModelSeries().getModelSeriesId();
		List<ComDirectory> comDirectoryList = comFileBo.getDirectoryListById(cid, modelSeriesId);
		List<HashMap> listJsonFV = new ArrayList();
		for (ComDirectory directory : comDirectoryList) {
			HashMap hm = new HashMap();
			hm.put("id", directory.getDirectoryId());
			hm.put("text", directory.getDirName());
			if (comFileBo.getDirectoryListById(directory.getDirectoryId(), modelSeriesId).size() == 0) {
				hm.put("leaf", "true");
			}
			listJsonFV.add(hm);
		}
		String jsonStr = JSONArray.fromObject(listJsonFV).toString();
		this.writeToResponse(jsonStr);
		return null;
	}
	
	/**
	 * 加载文档明细表信息
	 * zhouli
	 * createdate 2012/9/4
	 * @return 返回指定页面
	 */
	public String getDataComFileList() {
		if (this.getPage() == null)
			this.setPage(new com.richong.arch.web.Page());
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		List<HashMap> listJsonFV = new ArrayList();
		List<ComFile> comFileLists = comFileBo.getComFileList(fileName, page, cid);
		if(comFileLists!=null){
			for (ComFile file : comFileLists) {
				HashMap hm = new HashMap();
				hm.put("fileId", file.getFileId());
				hm.put("fileName", file.getFileName());
				hm.put("docId", file.getComDirectory().getDirectoryId());
				String contextPath = ServletActionContext.getServletContext().getContextPath();
				String xiaZai="<a href='"+contextPath+"/common/downfile.jsp?path="+BasicTypeUtils.getServletContextPath()+file.getFileUrl()+"'>" +
						file.getAppName()+"</a>";
				hm.put("appName", xiaZai);
				listJsonFV.add(hm);
			}
		}
		
		JSONObject json = new JSONObject();
		json.element("total", this.getPage().getTotalCount());
		json.element("comFile", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 更新文档目录（删除/新增/修改）
	 * zhouli
	 * createdate 2012/9/4 
	 */
	public String jsonManDirectoryUpdate() throws Exception {
		ComUser thisUser = getSysUser();
		if (thisUser == null){
			return SUCCESS;
		}
		String userId=thisUser.getUserId();
		String deleteId = this.getProcessEntityId();
		String jsonData = this.getJsonData();
		JSONObject json= new JSONObject();
		boolean isOk=false;
		logger.debug(" delete ID:" + deleteId + "; json data submit:"
				+ jsonData);
		if ("delete".equals(this.getMethod())) {
			if (deleteId != null && !"".equals(deleteId) && !"0".endsWith(deleteId)) {
				ComDirectory manDirectory = (ComDirectory) comFileBo.loadById(ComDirectory.class, deleteId);
				List<ComFile> cfs=this.comFileBo.getComFileByDir(manDirectory);
				List<ComDirectory> cds=this.comFileBo.getComDriByComDri(manDirectory);
				if (manDirectory != null) {
					if(cfs!=null && cfs.size()>0){
						json.put("msg", "isHavaChild");
						json.put("success",true);
						writeToResponse(json.toString());
						return null;
					}else if(cds !=null && cds.size()>0){
						json.put("msg", "isHaveDirectory");
						json.put("success",true);
						writeToResponse(json.toString());
						return null;
					}else{
						try {
							comFileBo.deleteDirWithFiles(deleteId,userId);
							isOk=true;
							json.put("success", isOk);
							json.put("msg", "true");
							this.writeToResponse(json.toString());
						} catch (Exception e) {
							logger.debug(e.getMessage());
							json.put("success", isOk);
							json.put("msg", "false");
							writeToResponse(json.toString());
							return null;
						}
					}
				}
			}
		} else if ("update".equals(this.getMethod())) {// 更新目录
			JSONObject jsonObject = JSONObject.fromObject(jsonData);
			jsonData = null;
			String id = jsonObject.getString("id2");
			String parentId = jsonObject.getString("parentId");
			String name = jsonObject.getString("name");
			// 当前机型下parentId下一级子目录
			List<ComDirectory> parentMD = this.comFileBo.getDirectoryListById(parentId,getComModelSeries().getModelSeriesId());
			int i=0;
			for( i=0;i<parentMD.size();i++){
				if(parentMD.get(i).getDirName().equals(name)){
					isOk=false;
					json.put("success", isOk);
					break;
				}
			}
			if(i==parentMD.size()){
				isOk=true;
			}
			if(!isOk){//判断当前机型当前父目录下是否存在相当的子目录
				this.writeToResponse(json.toString());
				return null;
			}
			if(!"".equals(id)){//修改操作
				comFileBo.updateDirectoryListById(name, id);
				jsonObject.element("success", Boolean.TRUE);
				this.writeToResponse(jsonObject.toString());
				return null;
			}else{//增加操作
				ComDirectory dm=new ComDirectory();
				dm.setDirName(name);
				if(!"0".equals(parentId)){
					ComDirectory pdm=(ComDirectory) this.comFileBo.loadById(ComDirectory.class, parentId);
					dm.setComDirectory(pdm);
					dm.setDirLevel(pdm.getDirLevel().intValue() + 1);
				}else{
					dm.setComDirectory(null);
					dm.setDirLevel(1);
				}
				dm.setValidFlag(ComacConstants.YES);
				dm.setComModelSeries(this.getComModelSeries());
				dm.setCreateDate(BasicTypeUtils.getCurrentDateforSQL());
				String dbOperate=ComacConstants.DB_INSERT;
				this.comFileBo.saveOrUpdate(dm, dbOperate, userId);
				jsonObject.element("success", Boolean.TRUE);
				this.writeToResponse(jsonObject.toString());
				return null;
			}
			
			
		}
		
		return null;
	}
	
	/**
	 * 删除文件
	 * zhouli
	 * createdate 2012/9/4 
	 * @return
	 */
	public String SysFileDelete() {
		JSONObject json = new JSONObject();
		String id = "";
		if (!BasicTypeUtils.isNullorBlank(this.getProcessEntityId())) {
			id = this.getProcessEntityId();
		}
		//System.out.println(getProcessEntityId());
		ComFile comFile = (ComFile) this.comFileBo.loadById(ComFile.class, id);
		if (comFile != null) {
			comFile.setValidFlag(ComacConstants.VALIDFLAG_NO);
			this.comFileBo.saveOrUpdate(comFile,ComacConstants.DB_DELETE,this.getSysUser().getUserId());
			json = this.putJsonOKFlag(json, true);
		}
		this.writeToResponse(json.toString());
		return null;
	}
	

	/**
	 * 上传文件
	 * zhouli
	 * createdate 2012/9/4 
	 * @return
	 */
	public String uploadSysFile() throws Exception {
		JSONObject json = new JSONObject();
		if (isChooseFile != null && "1".equals(isChooseFile) && docFile != null) {
			if(!BasicTypeUtils.checkFileNameLength(docFileFileName)){//文件名太大上传失败!
				json = this.putJsonOKFlag(json, false);
				json.put("msg", "fileNameSoLong");
				this.writeToResponse(json.toString());
				return null;
			}
//			if(!BasicTypeUtils.checkDocType(docFileContentType,docFileFileName)){//文件类型限制
//				json = this.putJsonOKFlag(json, false);
//				json.put("msg", "fileBugName");
//				this.writeToResponse(json.toString());
//				return null;
//			}
			if(!docFile.exists()){//文件为0kb 
				json = this.putJsonOKFlag(json, false);
				json.put("msg", "fileSoSmall");
				this.writeToResponse(json.toString());
				return null;
			}else{
				if (!BasicTypeUtils.checkDocLength(docFile)) {
					json = this.putJsonOKFlag(json, false);
					json.put("msg", "uploadFile");
					this.writeToResponse(json.toString());
					return null;
				}
			}
		}
		
		try {
			if (BasicTypeUtils.isNullorBlank(this.getProcessEntityId())) {//新增操作
				if(docFile == null){//新增操作必须上传文件
					json = this.putJsonOKFlag(json, false);
					json.put("msg", "specifyFile");
					this.writeToResponse(json.toString());
					return null;
				}
				ComFile comfile=new ComFile();
				comfile.setFileName(fileName);
				Boolean flag = this.comFileBo.uploadSysFile(comfile, cid, docFile, docFileFileName, ComacConstants.SYSDOCFILEFOLDER+"/filemanager", this.getSysUser());
				json = this.putJsonOKFlag(json, flag);
			} else {//修改操作
				ComFile comFile = (ComFile) this.comFileBo.loadById(ComFile.class, this.getProcessEntityId());
				if(comFile == null){
					json = this.putJsonOKFlag(json, false);
					json.put("msg", "notFindFile");
				}
				comFile.setFileName(fileName);
				if(isChooseFile == null || !"1".equals(isChooseFile)){
					docFile = null;
				}
				Boolean flag = this.comFileBo.uploadSysFile(comFile, cid, docFile, docFileFileName, ComacConstants.SYSDOCFILEFOLDER+"/filemanager", this.getSysUser());
				json = this.putJsonOKFlag(json, flag);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			json = this.putJsonOKFlag(json, false);
			json.put("msg", e.getMessage());
		}
		this.writeToResponse(json.toString());
		return null;
	}
		
	public String getCid() {
		return cid;
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public IComFileBo getComFileBo() {
		return comFileBo;
	}

	public void setComFileBo(IComFileBo comFileBo) {
		this.comFileBo = comFileBo;
	}
	
	public String getModelSeriesId() {
		return modelSeriesId;
	}

	public void setModelSeriesId(String modelSeriesId) {
		this.modelSeriesId = modelSeriesId;
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
	
	public String getDocFileContentType() {
		return docFileContentType;
	}

	public void setDocFileContentType(String docFileContentType) {
		this.docFileContentType = docFileContentType;
	}

	public String getIsChooseFile() {
		return isChooseFile;
	}

	public void setIsChooseFile(String isChooseFile) {
		this.isChooseFile = isChooseFile;
	}
}
