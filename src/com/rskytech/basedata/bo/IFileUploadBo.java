package com.rskytech.basedata.bo;

import java.io.File;
import java.io.IOException;

import com.richong.arch.bo.IBo;

public interface IFileUploadBo extends IBo {
	
	/**
	 * 保存上传的文件
	 * @param imgFile action中的上传文件对象
	 * @param uploadPath 文件存放的绝对路径
	 * @param fileName action中的文件名
	 * @throws Exception
	 */
	public void saveFile(File imgFile, String uploadPath, String fileName) throws Exception;
	
	/**
	 * 将上传的文件保存到指定位置
	 * @param src
	 * @param fileName
	 * @param saveFolder
	 * @return
	 */
	public String uploadFile(File src, String fileName, String saveFolder) throws IOException;
	
	/**
	 * 将上传的文件保存到指定位置(是否需要随机码作为文件名)
	 * @param src
	 * @param fileName
	 * @param saveFolder
	 * @param flag 是否需要随机码(不需要就传null)
	 * @return
	 * @throws IOException
	 */
	public String uploadFile(File src, String fileName, String saveFolder,String flag) throws IOException; 

}
