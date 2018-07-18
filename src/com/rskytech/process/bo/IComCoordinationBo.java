package com.rskytech.process.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComCoordination;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;

public interface IComCoordinationBo extends IBo{
	/**
	 * 保存或者更新协调单数据
	 * @param comS6OutOrIn 
	 * @param flag 
	 */
	public void saveOrUpdateCoo(ComCoordination comCoordination ,String type,String comTaskId,String comcoordinationCode,
			String comcontent,String comtheme,String comsendWg,String dbOperate,String comreceiveWg,
			String comreceiveArea,String comsendUser,String comcreateDate,String comreContent,
            String comreceiveUser,String comreceiveDate,String isReceive,ComModelSeries comModelSeries, int flag,String comS6OutOrIn, ComUser sysUser)throws BusinessException;
	
	/**
	 * 通过taskID查询协调单
	 * @param comTaskId
	 * @return
	 * @throws BusinessException
	 */
	public List<ComCoordination> findCoordinationByTaskId(String comTaskId,String modelSeriesId,int flag) throws BusinessException;
	/**
	 * 通过SSiID和s6内外查询协调单
	 * @param comTaskId
	 * @return
	 * @throws BusinessException
	 */
	public List<ComCoordination> findCoordinationById(String comTaskId,String comS6OutOrIn,String modelSeriesId,int flag)
			throws BusinessException;
	/**
	 * 查询协调单数据，并分页
	 * @param comTaskId
	 * @return
	 * @throws BusinessException
	 */
	public List<ComCoordination> findCoordinationNeedList(String userId ,String modelSeriesId,int flag, Page page,String type,String isReceive)
			throws BusinessException;
	/**
	 * 根据用户id，机型id，msg-3任务Id删除对应的协调单
	 * @param taskId
	 * @param userId
	 * @param modelSeriesId
	 */
	public void  deleteCoordination(String taskId ,String userId,String modelSeriesId);
	/**
	 * 将msg任务中的area同步到协调单表中
	 * @param taskId
	 * @param userId
	 * @param area
	 * @param modelSeriesId
	 * @return
	 */
	public boolean modifyCoordination(String taskId , String userId,String area,String modelSeriesId);
	/**
	 * 通过UserID查询用户名
	 * @param userId
	 * @return userName
	 * @throws BusinessException
	 */
	public String findUserNameByUserId(String userId)throws BusinessException;
	/**
	 * 通过专业室Id查询专业室名称
	 * @param profession
	 * @return ProfessionName
	 * @throws BusinessException
	 */
	public String findProfessionNameById(String profession)throws BusinessException;
	/**
	 * 查询所有的专业室不包括超级管理员专业室
	 */
	public List<Object[]> getAllProfessionList();
	
	/**
	 * 根据专业室id得到除超级管理员之外的所有用户
	 */
	public List<Object[]> getUserByProfessonId(String professionId);
	/**
	 * 根据区域编号查询具有分析权限的专业室
	 * @param areaCode
	 * @param modelSeriesId
	 * @return
	 */
	public List<Object[]> getProfessionIdByAreaCode(String areaCode,String modelSeriesId);
}
