package com.rskytech.sys.bo;

import java.util.ArrayList;
import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.M13;
import com.rskytech.pojo.M13C;
import com.rskytech.pojo.M13F;
import com.rskytech.pojo.MReferMsi;
import com.rskytech.pojo.MSet;
import com.rskytech.pojo.MSetF;

public interface IMSetBo extends IBo {
	
	/**
	 * 根据msiId和ataId查询MSet
	 * 
	 * @param msiId
	 * @return
	 */
	public List<MSet> getMsetListByMsiIdAndAtaId(String msiId,String ataId)
			throws BusinessException;
	
	/**
	 * 根据msiId查询MSet
	 * 
	 * @param msiId
	 * @return
	 */
	public List<MSet> getMsetListByMsiId(String msiId) throws BusinessException;
	
	/**
	 * 根据功能ID查找故障
	 * @param msetId
	 * @return
	 * @throws BusinessException
	 */
	public List<MSetF> getMsetfListByMsetId(String msetId)throws BusinessException;
	
	/**保存mset操作以及com_log_operate
	 * @param user 当前用户
	 * @param pageId 操作页面
	 * @param source_system 操作方式(系统,area,lhirf,struct)
	 * @param msiId com_msi id
	 * @param jsonData 
	 * @param isSaveLog 
	 * @param comModelSeries 
	 */
	public void saveOrUpdateMset(  ComUser user,
			String sourceSystem, String pageId, String msiId,String jsonData,boolean isSaveLog,ComModelSeries comModelSeries);
	
	
	/**
	 *根据msiId与功能编号查询唯一一条MSet
	 * @param msiId
	 * @param functionCode
	 * @return
	 */
	public MSet getMsetByfunctionCode(String msiId,String functionCode)throws BusinessException;
	


	/**
	 * 删除mset并且重新生成编号
	 * @param deljson 删除的jsonString字符串
	 * @param user 当前用户对象
	 * @param sourceSystem 操作方式(系统/lhirf/area/struct)
	 * @param pageId 操作页面
	 * @param msiId msi 的msiId
	 * @param jsonData 保存的jsonString字符串
	 * @return 
	 */
	public void deleteMsetAndSave(String deljson, ComUser sysUser,
			String sourceSystem, String pageId, String msiId,
			String jsonData,ComModelSeries comModelSeries);
	
	/**
	 * 更新步骤
	 * @param user
	 * @param msiId
	 * @param comModelSeries
	 */
	public void updataMStep(ComUser user, String msiId,ComModelSeries comModelSeries);
	
	/**
	 * 根据该msi下的ATA的条数初始化功能、功能故障条目
	 * @param modelSeriesId
	 * @param msiAtaId
	 * @param msiId
	 * @param userId
	 */
	public void initMset(String modelSeriesId,String msiAtaId,String msiId,String userId);
	
}
