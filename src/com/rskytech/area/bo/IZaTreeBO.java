package com.rskytech.area.bo;


import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComUser;

@SuppressWarnings("rawtypes")
public interface IZaTreeBO extends IBo {
	/**
	 * 组装树节点的显示信息：有维护权限，则加小图片；显示文字=编号+名称；不同状态显示不同颜色。
	 * @param Object nodeCode 树节点编号
	 * @param Object nodeName 树节点名称
	 * @param String language 当前选择的语言
	 * @param Object isOwnAna 当前用户是否有修改权限  1：有权限  0：没有权限
	 * @param Object nodeStatus 树节点的分析状态
	 * @return String
	 */
	public String getTreeNodeStatus(Object nodeCode, Object nodeName, Object isOwnAna, Object nodeStatus);
	/**
	 * 通过分析状态编号取得显示的中文或英文名称
	 * @param String language 当前选择的语言
	 * @param String statusCode 分析状态编号
	 * @return String
	 * @author 张建民
	 * @createdate 2012-8-28
	 */
	public String getStatusNameByStatusCode( String statusCode);
	
	/**
	 * 查询指定区域节点的下级子节点信息
	 * @param ComUser user 用户信息
	 * @param String searchType 当前页面的查询信息   版本页面：VERSION   分析页面：ANALYSIY
	 * @param String parentAreaId 当前区域节点，即父节点
	 * @return List
	 * @author 张建民
	 * @createdate 2012-8-28
	 */
	public List searchSubAreaTreeList(ComUser user, String parentAreaId, String modelSeriesId, String searchType) throws BusinessException;
	
	/**
	 * 查询该用户是否有需要维护的区域
	 * @param ComUser user 用户信息
	 * @param String searchType 当前页面的查询信息   版本页面：VERSION   分析页面：ANALYSIY
	 * @return List
	 * @author 张建民
	 * @createdate 2012-8-28
	 */
	public List searchMyMaintainList(ComUser user, String modelSeriesId, String searchType) throws BusinessException;
	
	/**
	 * 状态为审核完成或者冻结的，转为分析完成
	* @Title: openAnalysisStatus
	* @Description:
	* @param zaId
	* @return
	* @author samual
	* @date 2014年12月15日 下午1:25:20
	* @throws
	 */
	public boolean openAnalysisStatus(String zaId);
	
}
