package com.rskytech.user.bo;

import java.util.List;

import com.richong.arch.bo.IBo;
import com.richong.arch.web.Page;

@SuppressWarnings("rawtypes")
public interface IComProfessionBo extends IBo {

	/**
	 * 得到專業組列表，超級管理員-除外
	* @Title: showProfessionList
	* @Description:
	* @param page
	* @param professionCode
	* @param professionName
	* @param includeAdminFlag 是否包括查询超级管理员用户组
	* @return
	* @author samual
	* @date 2014年11月11日 上午9:04:28
	* @throws
	 */
	public Page showProfessionList(Page page, String professionCode, String professionName, boolean includeAdminFlag);

	/**
	 * 得到所有專業組列表（管理員維護成員用）
	* @Title: getAllProsession
	* @Description:
	* @param professionCode
	* @param professionName
	* @param includeAdminFlag 是否包括查询超级管理员用户组
	* @return
	* @author samual
	* @date 2014年11月10日 下午2:28:27
	* @throws
	 */
	public List getAllProsession(String professionCode, String professionName, boolean includeAdminFlag);

	/**
	 * 根據用戶ID得到他所在的專業組
	* @Title: getProsessionByUserId
	* @Description:
	* @param userId
	* @return
	* @author samual
	* @date 2014年11月10日 下午2:29:27
	* @throws
	 */
	public List getProsessionByUserId(String userId);
	
	/**
	 * 根据用户Id查询它所在的全部专业室（无权限区分）
	 */
	public List getProsByUserId(String userId);

	/**
	 * 根据专业室id得到拥有的用户
	* @Title: getUserByProfessonId
	* @Description:
	* @param page
	* @param professionId
	* @param userCode
	* @param userName
	* @return
	* @author samual
	* @date 2014年11月10日 下午2:33:11
	* @throws
	 */
	public Page getUserByProfessonId(Page page, String professionId, String userCode, String userName);

	/**
	 * 根据专业室id得到不拥有的用户
	* @Title: getOtherUserByProfessonId
	* @Description:
	* @param page
	* @param professionId
	* @param userCode
	* @param userName
	* @return
	* @author samual
	* @date 2014年11月10日 下午2:34:50
	* @throws
	 */
	public Page getOtherUserByProfessonId(Page page, String professionId, String userCode, String userName);

	/**
	 * 插入com_user_position表,給專業組下的用戶選擇職務
	* @Title: insertUserPositionRel
	* @Description:
	* @param userId
	* @param positionId
	* @param professionId
	* @param curUserId
	* @return
	* @author samual
	* @date 2014年11月10日 下午2:35:03
	* @throws
	 */
	public boolean insertUserPositionRel(String userId, String positionId, String professionId, String curUserId);

	/**
	 * 删除com_user_position关系,刪除專業組下用戶的職務
	* @Title: deleteUserPositionRel
	* @Description:
	* @param userId
	* @param positionId
	* @param professionId
	* @param curUserId
	* @return
	* @author samual
	* @date 2014年11月10日 下午2:36:42
	* @throws
	 */
	public boolean deleteUserPositionRel(String userId, String positionId, String professionId, String curUserId);

	/**
	 * 刪除專業組下的用戶
	* @Title: delUserInPofession
	* @Description:
	* @param userId
	* @param professionId
	* @param curUserId
	* @return
	* @author samual
	* @date 2014年11月10日 下午2:37:31
	* @throws
	 */
	public boolean delUserInProfession(String userId, String professionId, String curUserId);

	/**
	 * 給專業組添加用戶
	* @Title: addUserInProfession
	* @Description:
	* @param userId
	* @param professionId
	* @param curUserId
	* @return
	* @author samual
	* @date 2014年11月10日 下午2:38:20
	* @throws
	 */
	public boolean addUserInProfession(String userId, String professionId, String curUserId);

	/**
	 * 判斷專業組是否存在
	* @Title: checkProfessionIsExist
	* @Description:
	* @param professionId
	* @param professionCode
	* @return
	* @author samual
	* @date 2014年11月10日 下午2:52:51
	* @throws
	 */
	public boolean checkProfessionIsExist(String professionId, String professionCode);

	/**
	 * 根据专业室id得到拥有的用户（为用户赋值权限）
	* @Title: getUserByProfessonIdForAuth
	* @Description:
	* @param page
	* @param professionId
	* @param userCode
	* @param userName
	* @return
	* @author samual
	* @date 2014年11月11日 下午3:51:29
	* @throws
	 */
	public Page getUserByProfessonIdForAuth(Page page, String professionId, String userCode, String userName);

}
