package com.rskytech.paramdefinemanage.dao.impl;

import java.util.List;

import com.richong.arch.dao.DAOException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.paramdefinemanage.dao.ICusMpdChapterDao;
import com.rskytech.pojo.CusMpdChapter;
import com.rskytech.pojo.CusMpdSection;

public class CusMpdChapterDao extends BaseDAO implements ICusMpdChapterDao{

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List findByModelSeriesId(String modelSeries) throws DAOException {
		String hql = "from CusMpdChapter c where c.comModelSeries.modelSeriesId = '" + modelSeries + "' order by c.chapterCode asc";
		return this.findByHql(hql, null);
	}
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public CusMpdChapter findById(String id) throws DAOException {
		List list = this.findByHql("from CusMpdChapter c where c.chapterId = '" + id+"'", null);
		if (list.size() == 1)
			return (CusMpdChapter) list.get(0);
		return null;
	}
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public CusMpdSection findBySectionId(String id) throws DAOException {
		List list = this.findByHql("from CusMpdSection c where c.sectionId = '" + id +"'", null);
		if (list.size() == 1)
			return (CusMpdSection) list.get(0);
		return null;
	}
	
	
	
}
