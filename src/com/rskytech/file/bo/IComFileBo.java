package com.rskytech.file.bo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComDirectory;
import com.rskytech.pojo.ComFile;
import com.rskytech.pojo.ComUser;

@SuppressWarnings("rawtypes")
public interface IComFileBo extends IBo {
	/**
	 * 查询目录树，根据上级节点ID查找下级，判断查询到的目录是否还有下级
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<ComDirectory> getDirectoryListById(String dId, String modelSeriesId) throws BusinessException;
	
	/**
	 * 文档明细列表的所有有效的数据（根据目录ID）
	 * 
	 * @return 文档List
	 * @throws BusinessException
	 * @author zhouli createdate 2012-09-4
	 */
	public List getComFileList(String fileName, Page page, String dId);
	
	public boolean deleteDirWithFiles(String dirid, String userId);
	
	
	/**
	 * 根据目录ID修改相关信息
	 * 
	 * @param parentId
	 * @return
	 */
	public void updateDirectoryListById(String name, String id);

	/**
	 * 上传或修改文件
	 * @param comfile
	 * @param docId 目录ID
	 * @param thisFile 上传的文件
	 * @param docFileFileName 上传的文件名
	 * @param path 文件存放目录
	 * @throws Exception
	 */
	public Boolean uploadSysFile(ComFile comfile, String docId, File thisFile, String docFileFileName, String path, ComUser curUser) throws Exception;
			
	public List<ComFile> getComFileByDir(ComDirectory manDirectory);
	
	public List<ComDirectory> getComDriByComDri(ComDirectory manDirectory);
	
}
