package com.rskytech.user.bo;

import java.util.List;

import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComAuthority;

@SuppressWarnings("rawtypes")
public interface IComAuthorityBo extends IBo {

	/**
	 * 查询专业室的权限
	* @Title: findAuthorityByParam
	* @Description:
	* @param modelSeriesId 机型id
	* @param professionId 专业室ID
	* @param analysisType 四大分析类型
	* @return
	* @author samual
	* @date 2014年11月11日 上午10:32:24
	* @throws
	 */
	public List<ComAuthority> findAuthorityByParam(String modelSeriesId, String professionId, String analysisType);

	/**
	 * 修改专业室的权限（包括新增和修改）
	* @Title: jsonAuthorityUpdateForProfession
	* @Description:
	* @param modelSeriesId
	* @param professionId
	* @param analysisType
	* @param choosedIds
	* @param curUserId
	* @return
	* @author samual
	* @date 2014年11月11日 下午1:54:58
	* @throws
	 */
	public boolean updateAuthorityForProfession(String modelSeriesId, String professionId, String analysisType, String choosedIds,String choosedNoChilrdIds, String curUserId);

	/**
	 * 获取用户的权限
	* @Title: getAuthIdsByProsessionIdAndUserID
	* @Description:
	* @param modelSeriesId 机型ID
	* @param professionId 用户所属的专业室ID
	* @param userId 用户ID
	* @param analysisType 四大分析类型
	* @return
	* @author samual
	* @date 2014年11月12日 下午12:48:19
	* @throws
	 */
	public List getContentsByProsessionIdAndUserID(String modelSeriesId, String professionId, String userId, String analysisType);

	/**
	 * 维护用户的权限（包括新增和修改）
	* @Title: updateAuthorityForUser
	* @Description:
	* @param modelSeriesId 机型ID
	* @param professionId 专业室ID
	* @param userId 用户ID
	* @param analysisType 四大分析类型
	* @param choosedIds 选中的权限ID
	* @param curUserId 当前用户ID
	* @return
	* @author samual
	* @date 2014年11月12日 下午1:59:55
	* @throws
	 */
	public boolean updateAuthorityForUser(String modelSeriesId,
			String professionId, String userId, String analysisType,
			String choosedIds,String choosedNoChilrdIds, String curUserId);

	/**
	 * 判断专业室以前是否有权限
	* @Title: getTreeIsCheckBeforeLoad
	* @Description:
	* @param modelSeriesId
	* @param professionId
	* @param analysisType
	* @return
	* @author samual
	* @date 2014年11月26日 上午10:07:43
	* @throws
	 */
	public boolean getTreeIsCheckBeforeLoad(String modelSeriesId, String professionId, String analysisType);

	/**
	 * 判断用户以前是否有权限
	* @Title: getTreeIsCheckBeforeLoadForUser
	* @Description:
	* @param modelSeriesId
	* @param professionId
	* @param analysisType
	* @param userId
	* @return
	* @author samual
	* @date 2014年11月26日 下午1:28:21
	* @throws
	 */
	public boolean getTreeIsCheckBeforeLoadForUser(String modelSeriesId, String professionId, String analysisType, String userId);
	
	
	/**
	 * 根据传入的ataID找到所有的子ataID以及它们自己
	 * @param modelSeriesId
	 * @param choosedNoChilrdIds
	 * @return
	 */
	public String getAtaIdsBychoosedNoChilrdIds(String modelSeriesId, String choosedNoChilrdIds ,String analysisType);
	
	
}
