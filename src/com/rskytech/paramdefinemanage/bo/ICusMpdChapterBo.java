package com.rskytech.paramdefinemanage.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.CusMpdChapter;
import com.rskytech.pojo.CusMpdSection;

public interface ICusMpdChapterBo extends IBo {
	
	/**
	 * 根据机型号查询所有章节
	 * 
	 * @param modelSeriesId
	 * @return
	 * @throws BusinessException
	 */
	public List<CusMpdChapter> findMPDListByModelSeriesId(String modelSeriesId) throws BusinessException;
	
	public boolean saveChapter(CusMpdChapter c,String operateFlg,String userid) throws BusinessException;
	
	public CusMpdChapter findById(String id) throws BusinessException;
	
	@SuppressWarnings("rawtypes")
	public boolean deleteChapterById(Class clazz,String chapterId,String userId) throws BusinessException;
	
	public boolean saveSection(CusMpdSection s,String operateFlg,String userid) throws BusinessException;
	
	public CusMpdSection findSectionById(String id) throws BusinessException;
	
	@SuppressWarnings("rawtypes")
	public boolean deleteSectionById(Class clazz,String sectionId,String userId) throws BusinessException;
	
	/**
	 * 生成MPD PDF文件
	 * @param modelSeriesId 机型系列号
	 * @param language 当前语言（Cn or En）
	 * @return 生成的MPD PDF文件URL
	 * @throws Exception
	 */
	public String generateMpdPdf(String modelSeriesId) throws Exception;
	
	/**
	 * 检查章的Code是否存在
	 * @param chapterCode
	 * @param
	 * @return
	 * @throws BusinessException
	 */
	public boolean checkChapterCode(String chapterId,String chapterCode,String msId)throws BusinessException;
	
	/**
	 * 检查"章节内容区分"是否存在
	 * @param chapterCode
	 * @param
	 * @return
	 * @throws BusinessException
	 */
	public boolean checkChapterFlg(String chapterId,String chapterFlg,String msId)throws BusinessException;
	
	/**
	 * 检查在同一章下的节的code是否存在
	 * @param sectionId
	 * @param chapterId
	 * @param sectionCode
	 * @return
	 * @throws BusinessException
	 */
	public boolean checkSectionCode(String sectionId,String chapterId,String sectionCode)throws BusinessException;
	
	/**
	 * 检查在同一机型下的MPD附录的code是否存在
	 * @param msId 机型
	 * @param mrbPsId 附录Id
	 * @param mrbPsCode 附录code
	 * @return true 是不存在，false是存在
	 * @throws BusinessException
	 */
	public boolean checkMrbPsCode(String msId,String mrbPsId,Integer mrbPsCode)throws BusinessException;
	

}
