package com.rskytech.file.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.file.dao.IComFileDao;

public class ComFileDao extends BaseDAO implements IComFileDao {
	
	/**
	 * 根据上级目录ID查询相关信息
	 * 
	 * @param parentId
	 * @return
	 */
	public List getDirectoryListById(String parentId) {
		String sql = "";
		if ("0".equals(parentId)) {
			sql = "select DIRECTORY_ID,DIR_NAME from COM_DIRECTORY where PARENT_ID is null";
		}
		else {
			sql = "select DIRECTORY_ID,DIR_NAME from COM_DIRECTORY where PARENT_ID='" + parentId + "'";
		}
		sql += " and  valid_flag=1";
		List<Object[]> l = new ArrayList<Object[]>();
		l = this.executeQueryBySql(sql);
		return l;
	}
	
	/**
	 * 根据目录ID修改相关信息
	 * 
	 * @param parentId
	 * @return
	 */
	public void updateDirectoryListById(String name, String id) {
		String sql = "update COM_DIRECTORY set DIR_NAME='" + name + "' where DIRECTORY_ID= '" + id + "'";
		this.executeBySql(sql);
	}
	
	@Override
	public List getAtaSelectId(String fid) {
		String sql = "SELECT c.file_id,c.conference_type,c.select_id FROM com_conference_ata c" +
				" WHERE file_id='" + fid + "' and c.valid_flag = " + ComacConstants.VALIDFLAG_YES;
		List<Object[]> list = this.executeQueryBySql(sql);
		return list;
	}
}
