package com.rskytech.basedata.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;

@SuppressWarnings("rawtypes")
public interface IComAtaBo extends IBo {

	/**
	 * 通过当前ATA_ID，查询其下一级ATA信息（用于查询ATA树形目录）
	 * @param msId 当前机型ID
	 * @param ataId 当前ATA_ID
	 * @return 下一级ATA的显示信息
	 * @author zhangjianmin
	 */
	public List<HashMap> loadAtaTree(String msId, String ataId);
	
	/**
	 * 通过当前ATA_ID，查询其下一级ATA信息（用于查询ATA列表信息）
	 * @param msId 当前机型ID
	 * @param ataId 当前ATA_ID
	 * @param page 翻页参数
	 * @return 下一级ATA的显示信息
	 * @author zhangjianmin
	 */
	public List<HashMap> loadAtaList(String msId, String ataId, Page page);
	
	/**
	 * 新增和修改ATA操作
	 * @param user 用户实例
	 * @param ms 机型实例
	 * @param jsonData 需要操作的数据集合
	 * @param parentId 上级ATA_ID
	 * @return 
	 * @author zhangjianmin
	 */
	public HashMap<String, String> newOrUpdateMs(ComUser user, ComModelSeries ms, String jsonData, String parentId);
	
	/**
	 * 删除本级及其所有下级ATA和系统、结构的分析数据、任务
	 * @param ms 机型ID
	 * @param ataId 本级ATA ID
	 * @author zhangjianmin
	 */
	public void deleteAta(String msId, String ataId);
	
	/**
	 * 根据ATA章节的ID，查询该ID记录的下一级记录集
	 * @param ataId
	 * @throws BusinessException
	 * @author zouhenglong
	 */
	public List<ComAta> loadAtaListByParentId(String modelSeriesId, String ataId);

	/**
	 * 提供ata导出到excel的数据
	* @Title: findAllAtaSort
	* @Description:
	* @param comModelSeries
	* @return
	* @author samual
	* @date 2014年12月29日 下午1:10:33
	* @throws
	 */
	public List<ComAta> findAllAtaSort(ComModelSeries comModelSeries);

	/**
	 * 导入ata数据
	* @Title: importComAta
	* @Description:
	* @param serie
	* @param oneList
	* @param twoList
	* @param threeList
	* @param fourList
	* @param map
	* @return
	* @author samual
	* @date 2014年12月29日 下午1:17:04
	* @throws
	 */
	public Set<Integer> importComAta(ComModelSeries serie, ArrayList<ComAta> oneList,
			ArrayList<ComAta> twoList, ArrayList<ComAta> threeList,
			ArrayList<ComAta> fourList, String curUserId);
	
	
	/**
	 * 根据AtaId和Ata编号查询Ata
	 * @param ataCode
	 * @param msId
	 * @return ComAta
	 */
	public ComAta getComAtaByAtaCode(String ataCode,String msId);
	
	/**
	 * 通过当前ATA_ID，查询自己本身及其以下的所有子ATA
	 * @param msId 当前机型ID
	 * @param ataId 当前ATA_ID
	 * @return 自己本身及其以下的所有子ATA
	 * 
	 */
	public List<Object> getSelfAndChildAta(String msId, String ataId);
	
    /*
     * 通过ataCode获取ataId
     */
	public List<ComAta> getComAtaIdByCode(String ataCode);

}
