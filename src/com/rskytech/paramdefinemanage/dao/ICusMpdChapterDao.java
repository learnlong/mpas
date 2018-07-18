package com.rskytech.paramdefinemanage.dao;

import java.util.List;

import com.richong.arch.dao.DAOException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.CusMpdChapter;
import com.rskytech.pojo.CusMpdSection;

public interface ICusMpdChapterDao extends IDAO{

	public List findByModelSeriesId(String modelSeries) throws DAOException ;
	
	public CusMpdChapter findById(String id) throws DAOException;
	
	public CusMpdSection findBySectionId(String id) throws DAOException;
}
