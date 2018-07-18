package com.rskytech.basedata.action;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IFileUploadBo;
import com.rskytech.util.StringUtil;

public class FileUploadAction extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	
	private IFileUploadBo fileUploadBo;
	
	private File imgFile;
	// imgFileContentType属性用来封装上传文件的类型
	private String imgFileContentType;
	// imgFileFileName属性用来封装上传文件的文件名
	private String imgFileFileName;
	
	public String uploadImg() throws Exception {
		if (!BasicTypeUtils.checkImgType(imgFileContentType)) {
			writeToResponse("{\"error\":1,\"message\":\"文件非图片格式!\"}");
			return null;
		}
		if (!BasicTypeUtils.checkImgLength(imgFile)) {
			writeToResponse("{\"error\":1,\"message\":\"文件大小超过限制(最大5M)!\"}");
			return null;
		}
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
			ServletContext context = ServletActionContext.getServletContext();
			String uploadPath = context.getRealPath(ComacConstants.IMAGE_SAVE_PATH);
			String fileName = StringUtil.getRandomString() + imgFileFileName.substring(imgFileFileName.lastIndexOf("."));
			this.getFileUploadBo().saveFile(imgFile, uploadPath, fileName);
			writeToResponse("{\"error\":0,\"url\":\"" + (basePath + ComacConstants.IMAGE_SAVE_PATH + fileName) + "\"}");
		}
		catch (Exception e) {
			writeToResponse("{\"error\":1,\"message\":\"上传文件失败!\"}");
		}
		return null;
	}
	
	public IFileUploadBo getFileUploadBo() {
		return fileUploadBo;
	}
	
	public void setFileUploadBo(IFileUploadBo fileUploadBo) {
		this.fileUploadBo = fileUploadBo;
	}
	
	public File getImgFile() {
		return imgFile;
	}
	
	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}
	
	public String getImgFileContentType() {
		return imgFileContentType;
	}
	
	public void setImgFileContentType(String imgFileContentType) {
		this.imgFileContentType = imgFileContentType;
	}
	
	public String getImgFileFileName() {
		return imgFileFileName;
	}
	
	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}
	
}
