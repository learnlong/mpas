package com.rskytech.paramdefinemanage.bo;

import java.util.HashMap;
import java.util.List;

import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.CusItemZa5;
import com.rskytech.pojo.CusLevel;
import com.rskytech.pojo.CusMatrix;

public interface IStandardRegionParamBo extends IBo{

	/**
	 * 得到za5项目列表
	 * @param parentId 父节点ID
	 * @param modelSeriesId 机型系列ID
	 * @return za5项目list
	 */
	public List<CusItemZa5> getMatrixList(String parentId,String modelSeriesId);
	
	/**
	 * 根据项目id获取其下的级别list
	 * @param itemId 项目id
	 * @param stepFlg 分析步骤flg(ZA5或ZA43)
	 * @param modelSeriesId 机型系列ID
	 * @return za5级别list
	 */
	public List<CusLevel> getLevelList(String itemId,String stepFlg,String modelSeriesId);
	
	/**
	 * 判断所有叶节点级别个数是否相同
	 * @param modelSeriesId 机型编号
	 * @return -1 不相同 ；其他值 相同
	 */
	public Integer isLevelNumberSame(String modelSeriesId);  
	
	/**
	 * 生成第一副矩阵数据并返回副矩阵数据list
	 * @param modelSeriesId 机型id
	 * @param userId 操作者id
	 * @return 副矩阵数据list
	 */
	public List<CusMatrix> generateFirstMatrix(String modelSeriesId,String userId);
	
	/**
	 * 生成第二副矩阵
	 * @param modelSeries 机型对象 
	 * @param userId 用户id
	 * @param queryList 第一矩阵数据list
	 * @param valueList 不重复并且升序排列的第一矩阵级别值list
	 * @param level1List 一级节点list
	 * @param levelCount 级别节点个数
	 * @return
	 */
	public List<CusMatrix> generateSecondMatrix(ComModelSeries modelSeries,String userId,List<CusMatrix> queryList,List<Integer> valueList,List<CusItemZa5> level1List,Integer levelCount);
	
	/**
	 * 生成自定义检查区间矩阵
	 * @param modelSeries 机型对象
	 * @param userId 用户id
	 * @param queryList 第二矩阵(第一矩阵)数据list
	 * @param valueList 不重复并且升序排列的第一或第二副矩阵的值
	 * @return
	 */
	public HashMap<String, List<CusInterval>> generateFinalMatrix(ComModelSeries modelSeries,String userId,List<CusMatrix> queryList,List<Integer> valueList);
	
	/**
	 * 保存副矩阵数据
	 * @param firstMatrixList 副矩阵数据list
	 * @param userId 用户编号
	 */
	public void saveMatrix(List<CusMatrix> matrixList,String userId);
	
    /**
     * 更新检查间隔矩阵
     * @param queryList 检查间隔矩阵数据
     */
    public void updateFinalMatrix(List<CusInterval> queryList,String userId,ComModelSeries modelSeries);
	
    /**
     * 追加/更新ZA5矩阵自定义完成状态
     * @param userId 用户id
     * @param modelSeries 机型对象
     */
    public void insertCusMatrixFinishFlg(String userId,ComModelSeries modelSeries);
    
	/**
	 * 查询指定机型下所有一级项目节点的项目节点list
	 * @param modelSeriesId 机型系列编号
	 * @return 项目节点list
	 */
	public List<CusItemZa5> getLevel1ItemData(String modelSeriesId);
	
	/**
	 * 判断检查间隔矩阵数据是否存在
	 * @param modelSeriesId 机型id
	 * @return true 存在；false 不存在
	 */
	public boolean isFinalMatrixExist(String modelSeriesId);

	/**
	 * 判断第二副矩阵数据是否存在
	 * @param modelSeriesId 机型id
	 * @return true 存在；false 不存在
	 */
    public boolean isSecondMatrixExist(String modelSeriesId);
    
    /**
	 * 判断第一副矩阵数据是否存在
	 * @param modelSeriesId 机型id
	 * @return true 存在；false 不存在
	 */
    public boolean isFirstMatrixExist(String modelSeriesId);
    
    /**
	 * 获取检查间隔矩阵数据
	 * @param modelSeriesId 机型id
	 * @param intevalFlg 内部外部区分flg
	 * @return
	 */
	public List<CusInterval> searchFinalMatrix(String modelSeriesId,String intevalFlg);

	/**
	 * 获取第二副矩阵数据
	 * @param modelSeriesId 机型id
	 * @return 
	 */
    public List<CusMatrix> searchSecondMatrix(String modelSeriesId);
    
    /**
	 * 获取第一副矩阵数据
	 * @param modelSeriesId 机型id
	 * @return 
	 */
    public List<CusMatrix> searchFirstMatrix(String modelSeriesId);
    
    /**
	 * 查询指定机型下所有一级项目节点的项目节点list
	 * @param parentId 父节点
	 * @param isLeafNode 是不是叶子节点
	 * @param modelSeriesId 机型系列编号
	 * @return 项目节点list
	 */
    public List<CusItemZa5> getMatrix(String parentId,Integer isLeafNode,String modelSeriesId);
    
    /**************************************待删除
	 * 查询指定机型下每级的叶子项目节点
	 * @param rowNum 节点的级数
	 * @param itemZa5Id 项目ID
	 * @param modelSeriesId 机型系列编号
	 * @return 项目节点list
	 */
    @SuppressWarnings("unchecked")
	public List getAllLeaf(Integer rowNum,String itemZa5Id,String modelSeriesId);
    
    /**
	 * 查询指定机型下所有项目节点的项目节点的数量
	 * @param parentId 父节点
	 * @param modelSeriesId 机型系列编号
	 * @return 所有的叶子项目list
	 */
    public List<CusItemZa5> getAllLeafNode(String parentId,String modelSeriesId);
    
    /**
     * 保存ZA5主矩阵的item树节点
     * @param itemNode 主矩阵节点
     * @param operateFlg 操作区分
     * @param userId 用户id
     */
    public void saveOrUpdateZa5Item(CusItemZa5 itemNode, String operateFlg, String userId);
    
    /**
     * 删除ZA5主矩阵item树节点
     * @param itemNode 待删除节点
     * @param userId 用户id
     */
    public void deleteZa5Item(CusItemZa5 itemNode,String userId,String modelSeriesId);

    /**
     * 判断区域分析是否存在分析至指定步骤的数据
     * @param modelSeriesId 机型Id 
     * @param step 分析步骤
     * @return
     */
    public boolean isExistMatrixAnalyze(String modelSeriesId,String step);
    
    /**
     * 删除当前机型的ZA5检查间隔矩阵数据
     * @param modelSeriesId 机型id
     * @param anaFlg ZA5/ZA43flg
     */
    public void deleteFinalMatrix(String modelSeriesId,String anaFlg);

    /*
     * 删除ZA5/ZA43副矩阵数据
     * @param modelSeriesId 机型id
     * @param anaFlg ZA5/ZA43flg
     * @param matrixFlg 矩阵flg
     */
    public void deleteMatrix(String modelSeriesId,String anaFlg,Integer matrixFlg);
}
