package com.rskytech.paramdefinemanage.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.richong.arch.dao.DAOException;
import com.rskytech.pojo.CusMrbChapter;
import com.rskytech.pojo.CusMrbPs;
import com.rskytech.pojo.CusMrbSection;

public interface IMrbrReportBo extends IBo {

	/**
	 * 通过Code取章
	 * @param chapterCode 章的code
	 * @param msId 当前机型
	 * @throws BusinessException
	 */
	public List<CusMrbChapter> getChapterByCode(String msId,String chapterCode)throws BusinessException;

	/**
	 * 根据章的ID取节
	 * @param chapterId 章Id
	 * @param msId 机型
	 * @return
	 * @throws BusinessException
	 */
	public List<CusMrbSection> getSectionByChapter(String chapterId,String msId)throws BusinessException;
	
	public boolean saveChapter(CusMrbChapter c,String operateFlg,String userid) throws BusinessException;
	
	public boolean saveSection(CusMrbSection s,String operateFlg,String userid) throws BusinessException;
	
	public boolean saveMrbPs(CusMrbPs ps,String operateFlg,String userid) throws BusinessException;
	
	@SuppressWarnings("unchecked")
	public boolean deleteMrbPsById(Class clazz,String id,String userid) throws BusinessException;
	
	@SuppressWarnings("unchecked")
	public boolean deleteSectionById(Class clazz,String sectionId,String userId) throws BusinessException;
	/**
	 * 检查在同一章下的节的code是否存在
	 * @param sectionId
	 * @param chapterId
	 * @param sectionCode
	 * @return true 是不存在，false是存在
	 * @throws BusinessException
	 */
	public boolean checkSectionCode(String sectionId,String chapterId,String sectionCode)throws BusinessException;
	
	/**
	 * 取该机型下MRB附录
	 * @param msid 机型id
	 * @return
	 * @throws BusinessException
	 */
	public List<CusMrbPs> findByModelSeriesId(String msid) throws BusinessException;
	
	/**
	 * 当前机型是否存在唯一一条MPD附录类型为"报表首页"的记录
	 * 
	 * @param modelSeriesId
	 * @param flg
	 * @param isUpdate
	 * @return true是已存在，false不存在
	 * @throws DAOException
	 */
	public boolean isPsFlgUnique(String modelSeriesId, String cusMpdPsId) throws BusinessException;
	
	/**
	 * 检查在同一机型下的MRB附录的code是否存在
	 * @param msId 机型
	 * @param mrbPsId 附录Id
	 * @param mrbPsCode 附录code
	 * @return true 是不存在，false是存在
	 * @throws BusinessException
	 */
	public boolean checkMrbPsCode(String msId,String mrbPsId,Integer mrbPsCode)throws BusinessException;
	
	/**
	 * 生成MRB PDF文件
	 * @param modelSeriesId 机型系列号
	 * @param language 当前语言（Cn or En）
	 * @return 生成的MPD PDF文件URL
	 * @throws Exception
	 */
	public String generateMrbPdf(String modelSeriesId,String language) throws BusinessException;
	
	/**
	 * 获取mrb附录数据
	 * @param msid 机型
	 * @param psFlag 附录区分（0报表首页、1报表附录）
	 * @return
	 * @throws BusinessException
	 */
	public List<CusMrbPs> getCusMrbPs(String msid,Integer psFlag) throws BusinessException;
	
	
	
}
