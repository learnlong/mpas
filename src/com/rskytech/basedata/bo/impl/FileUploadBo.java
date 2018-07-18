package com.rskytech.basedata.bo.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.basedata.bo.IFileUploadBo;

/**
 * 文件上传用共通bo
 * @author changyf
 */
public class FileUploadBo extends BaseBO implements IFileUploadBo {
	
	@Override
	public void saveFile(File imgFile, String uploadPath, String fileName) throws Exception {
		File saveDir = new File(uploadPath);
		if (!saveDir.exists()) {
			saveDir.mkdirs();
		}
		InputStream is = new FileInputStream(imgFile);
		File toFile = new File(uploadPath, fileName);
		OutputStream os = new FileOutputStream(toFile);
		byte[] buffer = new byte[1024 * 10];
		int length = 0;
		while ((length = is.read(buffer)) > 0) {
			os.write(buffer, 0, length);
		}
		is.close();
		os.close();
	}
	
	@Override
	public String uploadFile(File src, String fileName, String saveFolder)
			throws IOException {
		String realFilePath = BasicTypeUtils.getServletContextPath();
		int index = fileName.lastIndexOf(".");
		String fileName2 = fileName;
		fileName = fileName.substring(index);//取文件的后缀名
		String targetFileName = fileName2;
		String uri = saveFolder + "/";
		FileUtils.copyFile(src, new File(realFilePath + uri +targetFileName));
		return uri + targetFileName;
	}
	
	@Override
	public String uploadFile(File src, String fileName, String saveFolder,String flag)
			throws IOException {
		String realFilePath = BasicTypeUtils.getServletContextPath();
		int index = fileName.lastIndexOf(".");
		String fileName2 = fileName;
		fileName = fileName.substring(index);//取文件的后缀名
		String targetFileName = fileName2;
		long time=new Date().getTime();
		Random random=new Random();
		targetFileName=time+""+random.nextInt(9999)+fileName;
		String uri = saveFolder + "/";
		FileUtils.copyFile(src, new File(realFilePath + uri +targetFileName));
		return uri + targetFileName;
	}
}
