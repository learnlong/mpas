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

public interface IM13Bo extends IBo {
	/**
	 *根据msiId查询M13
	 * @param msiId 
	 * @return
	 * @author chendexu
	 * createdate 2012-08-22
	 */
	public List<M13> getM13ListByMsiId(String msiId)throws BusinessException ;
	/**
	 *根据msiId与功能编号查询唯一一条M13
	 * @param msiId
	 * @param functionCode
	 * @return
	 * @author chendexu
	 * createdate 2012-08-22
	 */
	public M13 getM13ByfunctionCode(String msiId,String functionCode)throws BusinessException;
	/**
	 *根据msiId与故障影响编号查询唯一一条M13F
	 * @param msiId   系统MainId
	 * @param effectCode //故障影响编号
	 * @return
	 * @author chendexu
	 * createdate 2012-08-22
	 */
	public M13F getM13FByEffectCode(String msiId,String effectCode)throws BusinessException;
	/**
	 * 
	 * @param m13cId  功能原因Id
	 * @return
	 * @throws BusinessException
	 */
	public MReferMsi getMReferMsiByM13cId(String m13cId)throws BusinessException;
	/**
	 * 
	 * @param m13Id 功能Id
	 * @return
	 * @throws BusinessException
	 */
	public List<M13F> getM13fListByM13Id(String m13Id)throws BusinessException;
	/**
	 * 
	 * @param m13fId 功能故障Id
	 * @return
	 * @throws BusinessException
	 */
	public List<M13C> getM13cListByM13FId(String m13fId)throws BusinessException;
	/**
	 * 删除是否参考MSI
	 * @param refId   MSI参考表Id
	 * @param m13cId  故障原因Id
	 * @throws BusinessException
	 */

	public void deleteRef(String refId,String m13cId, ComUser user)throws BusinessException;
	/**
	 * 根据MsiId查询所有的故障影响
	 * @param msiId 
	 * @throws BusinessException
	 */
	public List<M13F> getM13fListByMsiId(String msiId)throws BusinessException;
	/**
	 * 根据MsiId查询所有的故障影响原因
	 * @param msiId
	 * @throws BusinessException
	 */
	public List<M13C> getM13cListByMsiId(String msiId)throws BusinessException;
	/**
	 * 查询没有参考其他MSI的故障原因
	 * @param msiId
	 * @return
	 * @throws BusinessException
	 */
	public List<M13C>   getM13cListByMsiIdNoidNoisRef(String msiId)throws BusinessException;
	/**保存m13操作以及com_log_operate
	 * @param m13c  m13对象
	 * @param user 当前用户
	 * @param pageId 操作页面
	 * @param source_system 操作方式(系统,area,lhirf,struct)
	 * @param msiId com_msi id
	 * @param dbOperate 操作类型
	 */
	public void saveOrUpdateM13(  ComUser user,
			String sourceSystem, String pageId, String msiId,String jsonData,boolean isSaveLog,ComModelSeries comModelSeries);
	/**
	 * 删除m13并且重新生成编号
	 * @param deljson 删除的jsonString字符串
	 * @param user 当前用户对象
	 * @param sourceSystem 操作方式(系统/lhirf/area/struct)
	 * @param pageId 操作页面
	 * @param msiId msi 的msiId
	 * @param jsonData 保存的jsonString字符串
	 * @return 
	 */
	public ArrayList<String> deleteM13AndSave(String deljson, ComUser sysUser,
			String sourceSystem, String pageId, String msiId,
			String jsonData,ComModelSeries comModelSeries);
	/**
	 * 保存ref操作,并且保存结束调用save方法把编号更新
	 * @param jsonData保存json字符串
	 * @param m13cId 
	 * @param user 当前用户
	 * @return 
	 */
	public ArrayList<String> saveRef(String jsonData, String m13cId,ComUser user,String msiId,ComModelSeries comModelSeries);
	
	/**
	 * 更新步骤
	 * @param user
	 * @param msiId
	 * @param comModelSeries
	 */
	public void updataMStep(ComUser user, String msiId,ComModelSeries comModelSeries);
	
	/**
	 * 根据msetfId和msiId查询m13c
	 */
	public List<M13C> getM13cByMsetfIdAndmsId(String msetfId,String msiId)throws BusinessException;
}
