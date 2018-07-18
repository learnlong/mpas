package com.rskytech.basedata.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;

@SuppressWarnings("rawtypes")
public interface IComAreaBo extends IBo {
	/**
	 * 将所属区域的编号转为Id，并用逗号隔开
	 * @param OwnArea
	 * @param modelSeriesId
	 * @return
	 * @throws BusinessException
	 */
	public String getAreaIdByAreaCode(String OwnArea,String modelSeriesId)throws BusinessException;
    /**
     * 将所属区域的Id转为编号，并用逗号隔开
     * @param ownArea
     * @return
     * @throws BusinessException
     */
     
	public String getAreaCodeByAreaId(String ownArea)throws BusinessException;
	
	/**
	 * 根据区域编号及所属机型查询区域
	 * @param areaCode
	 * @param modelSeriesId
	 * @throws BusinessException
	 * @author chendexu
	 * createdate 2012-08-29
	 */
	public ComArea getComAreaByAreaCode(String areaCode,String modelSeriesId)throws BusinessException;
	
	/**
	 * 通过当前区域ID，查询其下一级区域信息（用于查询区域树形目录）
	 * @param msId 当前机型ID
	 * @param areaId 当前区域ID
	 * @return 下一级区域的显示信息
	 * @author zhangjianmin
	 */
	public List<HashMap> loadAreaTree(String msId, String areaId);
	
	/**
	 * 通过当前区域ID，查询其下一级区域信息（用于查询区域列表信息）
	 * @param msId 当前机型ID
	 * @param areaId 当前区域ID
	 * @param page 翻页参数
	 * @return 下一级区域的显示信息
	 * @author zhangjianmin
	 */
	public List<HashMap> loadAreaList(String msId, String areaId, Page page);
	
	/**
	 * 新增和修改区域操作
	 * @param user 用户实例
	 * @param ms 机型实例
	 * @param jsonData 需要操作的数据集合
	 * @param parentId 上级区域ID
	 * @return 
	 * @author zhangjianmin
	 */
	public String newOrUpdateArea(ComUser user, ComModelSeries ms, String jsonData, String parentId);
	
	/**
	 * 通过区域ID，查询其设备信息
	 * @param areaId 当前区域ID
	 * @return 设备信息
	 * @author zhangjianmin
	 */
	public List<HashMap> loadEquipList(String areaId);
	
	/**
	 * 新增和修改区域明细操作
	 * @param jsonData 需要操作的数据集合
	 * @param areaId 区域ID
	 * @return 
	 * @author zhangjianmin
	 */
	public String newOrUpdateEquip(String jsonData, String areaId);
	
	/**
	 * 删除本级及其所有下级区域和LHIRF、区域的分析数据、任务
	 * @param ms 机型ID
	 * @param areaId 本级区域ID
	 * @author zhangjianmin
	 */
	public void deleteArea(String msId, String areaId);
	
	/**
	 * 根据机型和父id查询字areaid
	* @Title: loadAreaListByParentId
	* @Description:
	* @param modelSeriesId
	* @param parentAreaId
	* @return
	* @author samual
	* @date 2014年11月11日 上午11:16:00
	* @throws
	 */
	public List<ComArea> loadAreaListByParentId(String modelSeriesId, String parentAreaId);
	
	/**
	 * 提供区域导出到excel的数据
	* @Title: findAllAtaSort
	* @Description:
	* @param comModelSeries
	* @return
	* @author samual
	* @date 2014年12月29日 下午1:11:53
	* @throws
	 */
	public List<ComArea> findAllAreaSort(ComModelSeries comModelSeries);
	
	/**
	 * 导入区域表数据，返回有误的行号（行号暂存在区域id中）
	* @Title: importComArea
	* @Description:
	* @param serie 机型
	* @param oneList 第一级区域
	* @param twoList 第二级区域
	* @param threeList 第三局区域
	* @return
	* @author samual
	* @date 2014年12月31日 上午10:07:16
	* @throws
	 */
	public Set<Integer> importComArea(ComModelSeries serie,
			ArrayList<ComArea> oneList, ArrayList<ComArea> twoList,
			ArrayList<ComArea> threeList, String curUserId);
	
	/**
	 * 获取第一区域
	* @Title: getAreaListForFirstLevel
	* @Description:
	* @param comModelSeries
	* @param level
	* @return
	* @author samual
	* @date 2014年12月31日 上午9:44:49
	* @throws
	 */
	public List<ComArea> getAreaListForLevel(ComModelSeries comModelSeries, int level);
}
