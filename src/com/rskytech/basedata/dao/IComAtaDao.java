package com.rskytech.basedata.dao;

import java.util.HashMap;
import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComAta;

public interface IComAtaDao extends IDAO {

	/**
	 * 通过当前ATA_ID，查询其下一级ATA信息
	 * @param msId 当前机型ID
	 * @param ataId 当前ATA_ID
	 * @return 下一级ATA的显示信息
	 * @author zhangjianmin
	 */
	public List<ComAta> loadChildAta(String msId, String ataId) throws BusinessException;
	
	/**
	 * 通过当前ATA_ID，查询其下一级ATA信息
	 * @param msId 当前机型ID
	 * @param ataId 当前ATA_ID
	 * @param page 翻页参数
	 * @return 下一级ATA的显示信息
	 * @author zhangjianmin
	 */
	public List<ComAta> loadChildAta(String msId, String ataId, Page page) throws BusinessException;
	
	/**
	 * 验证ATA编号是否重复
	 * @param msId 当前机型ID
	 * @param ataId 当前需要变更的ATA_ID
	 * @param ataCode 当前需要变更的编号
	 * @return true：重复；false：不重复
	 * @author zhangjianmin
	 */
	public boolean checkAta(String msId, String ataId, String ataCode) throws BusinessException;
	
	/**
	 * 通过当前ATA_ID，查询自己本身及其以下的所有子ATA
	 * @param msId 当前机型ID
	 * @param ataId 当前ATA_ID
	 * @return 自己本身及其以下的所有子ATA
	 * @author zhangjianmin
	 */
	public List<Object> getSelfAndChildAta(String msId, String ataId);
	
	/**
	 * 检查ata是否已经在msi分析中
	 * @param ataCode
	 * @param msId
	 * @return
	 */
	public String getAtaIsHaveMSI(String ataCode,String msId);
	
	/**
	 * 删除ATA
	 * @param ataId 当前ATA_ID
	 * @author zhangjianmin
	 */
	public void deleteAta(String ataId);
	
	/**
	 * 根据AtaId和Ata编号查询Ata
	 * @param ataCode
	 * @param msId
	 * @return
	 */
	public List<ComAta> getComAtaByAtaCode(String ataCode,String msId);
	
	/**
	 * 导入ATA时调用的存储过程
	 * @param msId 机型系列ID
	 * @param biaoshi 1导入、2添加修改
	 * @return
	 */
	public HashMap<String, String> importAta(String msId, String biaoshi);
    
	 /*
     * 通过ataCode获取ataId
     */
	public List<ComAta> getAtaIdByAtaCode(String ataCode);
}
