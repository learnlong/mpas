package com.rskytech.file.bo.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IFileUploadBo;
import com.rskytech.file.bo.IComFileBo;
import com.rskytech.file.dao.IComFileDao;
import com.rskytech.pojo.ComDirectory;
import com.rskytech.pojo.ComFile;
import com.rskytech.pojo.ComUser;

@SuppressWarnings({"unchecked","rawtypes"})
public class ComFileBo extends BaseBO implements IComFileBo {
	private IComFileDao comFileDao;
		
	// 文件上传用
	private IFileUploadBo fileUploadBo;
	
	/**
	 * 查询目录树，根据上级节点ID查找下级，判断查询到的目录是否还有下级
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<ComDirectory> getDirectoryListById(String dId, String modelSeriesId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(ComDirectory.class);
		dc.add(Restrictions.eq("comModelSeries.id", modelSeriesId));
		if (!BasicTypeUtils.isNullorBlank(dId) && !"0".equals(dId)) {
			dc.add(Restrictions.eq("comDirectory.directoryId", dId));
		}else {
			dc.add(Restrictions.isNull("comDirectory.directoryId"));
		}
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		dc.addOrder(Order.asc("createDate"));
		return comFileDao.findByCriteria(dc);
	}
	
	/**
	 * 文档明细列表的所有有效的数据（根据目录ID）
	 * 
	 * @return 文档List
	 * @throws BusinessException
	 * @author zhouli createdate 2012-09-4
	 */
	public List getComFileList(String fileName, Page page, String dId) {
		DetachedCriteria dc = DetachedCriteria.forClass(ComFile.class);
		if (!"0".equals(dId)) {
			if (fileName != null && !"".equals(fileName)) {
				dc.add(Restrictions.like("fileName", "%" + fileName + "%"));
			}
			dc.add(Restrictions.eq("comDirectory.directoryId", dId));
			dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
			dc.addOrder(Order.asc("fileName"));
			return this.findByCritera(dc, page).getResult();
		}
		return null;
		
	}

	public boolean deleteDirWithFiles(String dirid, String userId) {
		ComDirectory dir = (ComDirectory) this.loadById(ComDirectory.class, dirid);
		if (dir == null) {
			return false;
		}
		try {
			List<ComFile> files = new ArrayList<ComFile>();
			files.addAll(dir.getComFiles());
			for (ComFile file : files) {
				String fID = file.getFileId();
				ComFile fi = file;
				fi.setValidFlag(ComacConstants.VALIDFLAG_NO);
				this.saveOrUpdate(fi, fID, userId);
			}
			List<ComDirectory> childDirs = new ArrayList<ComDirectory>();
			childDirs.addAll(dir.getComDirectories());
			for (ComDirectory childDir : childDirs) {
				this.deleteDirWithFiles(childDir.getDirectoryId(), userId);
			}
			dir.setValidFlag(ComacConstants.VALIDFLAG_NO);
			this.saveOrUpdate(dir, ComacConstants.DB_DELETE, userId);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	/**
	 * 根据目录ID修改相关信息
	 * 
	 * @param parentId
	 * @return
	 */
	public void updateDirectoryListById(String name, String id) {
		comFileDao.updateDirectoryListById(name, id);
	}

	/**
	 * 上传或修改文件
	 * @param comfile
	 * @param docId 目录ID
	 * @param thisFile 上传的文件
	 * @param docFileFileName 上传的文件名字
	 * @param path 文件存放目录
	 * @throws Exception
	 */
	public Boolean uploadSysFile(ComFile comfile, String cid, File thisFile, String docFileFileName, String path, ComUser curUser) throws Exception {
		String id = comfile.getFileId();
		// 文件上传
		String fileUrl = null;
		if (thisFile != null) {
			fileUrl = fileUploadBo.uploadFile(thisFile, docFileFileName, path, "1");
			comfile.setFileUrl(fileUrl);
			comfile.setAppName(docFileFileName);
		}
		String dbOperate = ComacConstants.DB_UPDATE;
		if (id == null || BasicTypeUtils.isNullorBlank(id)) {
			ComDirectory md = (ComDirectory) this.loadById(ComDirectory.class, cid);
			if (md == null) {
				throw new Exception("没有指定有效的目录，不能上传文件！");
			}
			dbOperate = ComacConstants.DB_INSERT;
			comfile.setComDirectory(md);
			comfile.setValidFlag(ComacConstants.VALIDFLAG_YES);
		}
		this.saveOrUpdate(comfile, dbOperate, curUser.getUserId());
		return Boolean.TRUE;
	}
	
	/**
	 * 根据文件夹找到所有有效的子文件集合
	 */
	@Override
	public List<ComFile> getComFileByDir(ComDirectory manDirectory) {
		DetachedCriteria dc = DetachedCriteria.forClass(ComFile.class);
		dc.add(Restrictions.eq("comDirectory.directoryId", manDirectory.getDirectoryId()));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		return this.findByCritera(dc);
	}
	
	/**
	 * 根绝文件夹找到所有有效子文件夹
	 */
	@Override
	public List<ComDirectory> getComDriByComDri(ComDirectory manDirectory) {
		DetachedCriteria dc = DetachedCriteria.forClass(ComDirectory.class);
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		dc.add(Restrictions.eq("comDirectory.directoryId", manDirectory.getDirectoryId()));
		return this.findByCritera(dc);
	}	
	
	public IComFileDao getComFileDao() {
		return comFileDao;
	}
	
	public void setComFileDao(IComFileDao comFileDao) {
		this.comFileDao = comFileDao;
	}
	
	public IFileUploadBo getFileUploadBo() {
		return fileUploadBo;
	}
	
	public void setFileUploadBo(IFileUploadBo fileUploadBo) {
		this.fileUploadBo = fileUploadBo;
	}
}
