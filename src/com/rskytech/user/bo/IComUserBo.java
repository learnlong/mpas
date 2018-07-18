package com.rskytech.user.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.richong.arch.web.Page;

@SuppressWarnings("rawtypes")
public interface IComUserBo extends IBo {
	
	/**
	 * 查询用户列表
	* @Title: getComUserList
	* @Description:
	* @param userCode 用户code
	* @param keyword 用户名
	* @param page 翻页信息
	* @param validFlag 用户是否有效
	* @return
	* @throws BusinessException
	* @author samual
	* @date 2014年11月10日 下午12:33:42
	* @throws
	 */
	public List getComUserList(String userCode, String keyword,Page page,String validFlag)  throws BusinessException;

	/**
	 * 修改密码
	* @Title: jsonUserChangePassWord
	* @Description:
	* @param userId 用户ID
	* @param getSysUserId 当前登陆用户id
	* @param oldPassWord 原密码
	* @param newPassWord 新密码
	* @return
	* @author samual
	* @date 2014年11月10日 下午12:35:14
	* @throws
	 */
	public boolean jsonChangeUserPassWord(String userId,String curUserId,String oldPassWord,String newPassWord);

	/**
	 * 确认密码是否正确
	* @Title: checkPassWord
	* @Description:
	* @param userId 用户ID
	* @param passWord 密码
	* @return
	* @author samual
	* @date 2014年11月10日 下午12:36:34
	* @throws
	 */
	public boolean checkPassWord(String userId,String passWord);

	/**
	 * 根据用户Id重新设置密码
	* @Title: resetUserPassWord
	* @Description:
	* @param userId 需要修改密码用户Id
	* @param curUserId 当前用户Id
	* @return
	* @author samual
	* @date 2014年11月10日 下午12:51:20
	* @throws
	 */
	public boolean resetUserPassWord(String userId, String curUserId);

	/**
	 * 管理员直接修改密码（不需要校验原始密码）
	* @Title: changeUserPassWordByAdmin
	* @Description:
	* @param userId 需要需要的用户id
	* @param newPassWord 新密码
	* @param curUserId 当前用id
	* @return
	* @author samual
	* @date 2014年11月26日 上午9:20:25
	* @throws
	 */
	public boolean changeUserPassWordByAdmin(String userId, String newPassWord, String curUserId);
	
}
