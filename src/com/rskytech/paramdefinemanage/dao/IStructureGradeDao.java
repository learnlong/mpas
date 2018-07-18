package com.rskytech.paramdefinemanage.dao;

import java.util.List;

import com.richong.arch.dao.DAOException;
import com.richong.arch.dao.IDAO;

public interface IStructureGradeDao extends IDAO{

	public List getCusInList(Object[] ob) throws DAOException;
}
