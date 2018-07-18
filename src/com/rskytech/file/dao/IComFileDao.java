package com.rskytech.file.dao;

import java.util.List;

import com.richong.arch.dao.IDAO;

/**
 * 文档表接口
 * @author zhouli
 * createdate 2012-09-3
 */
public interface IComFileDao extends IDAO{
	/**
	 * 根据上级目录ID查询相关信息
	 * @param parentId
	 * @return
	 */
	public List getDirectoryListById(String parentId);
	
	/**
	 * 根据目录ID修改相关信息
	 * @param parentId
	 * @return
	 */
	public void updateDirectoryListById(String name,String id);
	
	public List getAtaSelectId(String dirId);
}
