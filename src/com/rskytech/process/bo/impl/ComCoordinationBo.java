package com.rskytech.process.bo.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComCoordination;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComProfession;
import com.rskytech.pojo.ComUser;
import com.rskytech.process.bo.IComCoordinationBo;
import com.rskytech.process.dao.IComCoordinationDao;



public class ComCoordinationBo  extends BaseBO implements IComCoordinationBo{
	private IComCoordinationDao comCoordinationDao;
	
	public IComCoordinationDao getComCoordinationDao() {
		return comCoordinationDao;
	}
	public void setComCoordinationDao(IComCoordinationDao comCoordinationDao) {
		this.comCoordinationDao = comCoordinationDao;
	}
	private IComAreaBo comAreaBo;
	/**
	 * 更新或者插入数据
	 */
	public void saveOrUpdateCoo(ComCoordination comCoordination ,String type,String comTaskId,String comcoordinationCode,
			String comcontent,String comtheme,String comsendWg,String dbOperate,String comreceiveWg,
			String comreceiveArea,String comsendUser,String comcreateDate,String comreContent,String comreceiveUser,
			String comreceiveDate,String isReceive,ComModelSeries comModelSeries,int flag,String comS6OutOrIn,ComUser sysUser)throws BusinessException {	
		    comCoordination.setCoordinationCode(comcoordinationCode);
		    comCoordination.setSendContent(comcontent);
		    comCoordination.setType(type);
		    comCoordination.setTaskId(comTaskId);
		    comCoordination.setTheme(comtheme);
		    if("1".equals(isReceive)||"2".equals(isReceive)){
		    	comCoordination.setSendWorkgroup(comsendWg);
		    	comCoordination.setReceivedWorkgroup(comreceiveWg);
		    	comCoordination.setSendUser(comsendUser);
		    	comCoordination.setReceiveUser(comreceiveUser);
		    }else{
		    	/**comCoordination.setSendWorkgroup(findProfessionIdByName(comsendWg));
		    	comCoordination.setReceivedWorkgroup(findProfessionIdByName(comreceiveWg));
		    	comCoordination.setSendUser(findUserIdByUserName(comsendUser));
		    	comCoordination.setReceiveUser(findUserIdByUserName(comreceiveUser));*/
		    }
		    String areaId = comAreaBo.getAreaIdByAreaCode(comreceiveArea, comModelSeries.getModelSeriesId());
		    comCoordination.setReceiveArea(areaId==null?comreceiveArea:areaId);
		    comCoordination.setSendDate(comcreateDate);
		    comCoordination.setReceiveContent(comreContent);
		    comCoordination.setReceiveDate(comreceiveDate);	
		    comCoordination.setIsReceived(isReceive);
		    comCoordination.setValidFlag(flag);
		    comCoordination.setS6OutOrIn(comS6OutOrIn);
		    comCoordination.setComModelSeries(comModelSeries);
			this.saveOrUpdate(comCoordination, dbOperate, sysUser.getUserId());
		}
	/**
	 * 通过taskID查询协调单
	 * @param comTaskId
	 * @return
	 * @throws BusinessException
	 */
	public List<ComCoordination> findCoordinationByTaskId(String comTaskId,String modelSeriesId,int flag)
			throws BusinessException {
		return this.comCoordinationDao.findCoordinationByTaskId(comTaskId, modelSeriesId, flag);
	}
	/**
	 * 通过SSIID和S6内外查询协调单
	 * @param comTaskId
	 * @return
	 * @throws BusinessException
	 */
	public List<ComCoordination> findCoordinationById(String comTaskId,String comS6OutOrIn,String modelSeriesId,int flag)
			throws BusinessException {
		return this.comCoordinationDao.findCoordinationById(comTaskId, comS6OutOrIn, modelSeriesId, flag);
	}
	/**
	 * 查询所有的协调单，并分页
	 * @param comTaskId
	 * @return
	 * @throws BusinessException
	 */
	public List<ComCoordination> findCoordinationNeedList(String userId ,String modelSeriesId,int flag,Page page,String type,String isReceive)
			throws BusinessException {
		List<ComCoordination> list;
		if(type==null&&isReceive==null||("".equals(type)&&"".equals(isReceive))){	
			list = this.comCoordinationDao.findCoordinationList(userId,modelSeriesId,flag,page);
			/*for (ComCoordination cc : list){
				TaskMsg task = (TaskMsg) this.loadById(TaskMsg.class, cc.getTaskId());
				if(task==null){
					this.deleteCoordination(cc.getTaskId(), userId, modelSeriesId);
					list = this.comCoordinationDao.findCoordinationList(userId,modelSeriesId,flag,page);
				}
			}*/
		}else{
			list =  this.comCoordinationDao.findCoordinationSearchList(userId,modelSeriesId,flag,page,type,isReceive);
			/*for (ComCoordination cc : list){
				TaskMsg task = (TaskMsg) this.loadById(TaskMsg.class, cc.getTaskId());
				if(task==null){
					this.deleteCoordination(cc.getTaskId(), userId, modelSeriesId);
					list = this.comCoordinationDao.findCoordinationList(userId,modelSeriesId,flag,page);
				}
			}*/
		}
		return list;
	}
	
	/**
	 * 删除msg-3任务对应的协调单
	 */
	public void  deleteCoordination(String taskId , String userId,String modelSeriesId){
		ComCoordination comCoordination;
		List<ComCoordination> listcom = findCoordinationByTaskId(taskId, modelSeriesId, ComacConstants.YES);
		if(listcom.size()>0){
			for(ComCoordination cc : listcom ){
				if(cc.getCoordinationId()!=null&&!"".equals(cc.getCoordinationId())){
					comCoordination= (ComCoordination)loadById(ComCoordination.class, cc.getCoordinationId());
					this.delete(comCoordination, userId);
				}
			}
		}
	}
	
	/**
	 * 如果任务区域和协调单区域不相同，将协调单的接收区域改为msg-3任务区域
	 */
	public boolean modifyCoordination(String id , String userId,String area,String modelSeriesId){
		boolean flag =true;
		String areaId = comAreaBo.getAreaIdByAreaCode(area, modelSeriesId);
		List<ComCoordination> listcom = findCoordinationByTaskId(id, modelSeriesId, ComacConstants.YES);
		if(listcom.size()>0){
			for(ComCoordination cc : listcom ){
				if(cc.getReceiveArea()!=null&&!"".equals(cc.getReceiveArea())){
					if(!cc.getReceiveArea().equals(areaId)){
						if(cc.getCoordinationId()!=null&&!"".equals(cc.getCoordinationId())){
							cc.setReceiveArea(areaId);
							this.update(cc,userId);
						}
					}
				}else{
					flag=false;
				}
			}
		}else{
			flag=false;
		}
		return flag;
	}
	
	/**
	 * 通过UserID查询用户名
	 * @param comTaskId
	 * @return
	 * @throws BusinessException
	 */
	public String findUserNameByUserId(String userId) throws BusinessException {
		String userName = "";
		ComUser user= (ComUser) this.loadById(ComUser.class, userId);
		if(user!=null){
			userName = user.getUserName();
		}
		return userName;
	}
	
	/**
	 * 通过专业室Id查询专业室名称
	 * @param profession
	 * @return professionName
	 * @throws BusinessException
	 */
	public String findProfessionNameById(String profession) throws BusinessException {
		String professionName = "";
		ComProfession comProfession= (ComProfession) this.loadById(ComProfession.class, profession);
		if(comProfession!=null){
			professionName = comProfession.getProfessionName();
		}
		return professionName;
	}
	/**
	 * 通过用户名查询UserID
	 * @param comTaskId
	 * @return
	 * @throws BusinessException
	 */
	private String findUserIdByUserName(String userName) throws BusinessException {
		String userId = "";
		String sql = "SELECT c.user_id FROM Com_User c WHERE c.user_name='"+userName+"'";
		List list = this.dao.executeQueryBySql(sql);
		if(list!=null){
			for(Object obj : list){
				userId = obj.toString();
			}
		}
		return userId;
	}
	
	/**
	 * 通过专业室名称查询专业室Id
	 * @param profession
	 * @return professionId
	 * @throws BusinessException
	 */
	private String findProfessionIdByName(String professionName) throws BusinessException {
		String professionId = "";
		String sql = "SELECT c.profession_id FROM Com_Profession c WHERE c.profession_name='"+professionName+"'";
		List list = this.dao.executeQueryBySql(sql);
		if(list!=null){
			for(Object obj : list){
				professionId = obj.toString();
			}
		}
		return professionId;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAllProfessionList() {
		StringBuffer sb = new StringBuffer();
		sb.append("select profession_id, profession_name");
		sb.append("  from COM_PROFESSION");
		sb.append(" where valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append(" and profession_id <> '" + ComacConstants.PROEFSSION_ID_ADMIN + "'");
		sb.append(" order by profession_code");
		return this.dao.executeQueryBySql(sb.toString());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getProfessionIdByAreaCode(String areaCode,String modelSeriesId) {
		 if(areaCode!=null&&!"".equals(areaCode.trim())){
			 List<Object[]> resultlist = new ArrayList<Object[]>();
			 List<Object[]> list;
			 Set<String> professionIdSet = new TreeSet<String>();
			 String[] areaCodes = areaCode.split(",");
			 for(String str : areaCodes){
				 if("".equals(str.trim())){
					 continue;
				 }
				 StringBuffer sb = new StringBuffer();
				 sb.append("SELECT t.profession_id");
				 sb.append(" FROM COM_AUTHORITY t, com_area a");
				 sb.append(" WHERE a.area_id=t.content");
				 sb.append(" AND a.model_series_id=t.model_series_id");
				 sb.append(" AND a.valid_flag=t.valid_flag");
				 sb.append(" AND a.valid_flag="+ComacConstants.VALIDFLAG_YES);
				 sb.append(" AND a.area_code='"+str+"'");
				 sb.append(" AND a.model_series_id='"+modelSeriesId+"'");
				 list = this.dao.executeQueryBySql(sb.toString());
				 if(list!=null){
					 for(Object obj : list){
						 professionIdSet.add(obj.toString());
					 }
				 }
			 }
			 Iterator<String> it = professionIdSet.iterator();
			 while(it.hasNext()){
				 String sql ="SELECT c.profession_id,c.profession_name FROM com_profession c WHERE c.profession_id='"+it.next()+"'";
				 resultlist.addAll(this.dao.executeQueryBySql(sql));
			 }
			 return resultlist;
		 }else{
			 return getAllProfessionList();
		 }
	}
	
	/**
	 * 根据专业室id得到除超级管理员之外的所有用户
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getUserByProfessonId(String professionId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.user_id,");
		sb.append("       a.user_name");
		sb.append("  from com_user a, com_profession_user b");
		sb.append(" where a.user_id = b.user_id");
		sb.append("   and b.profession_id = '" + professionId + "'");
		sb.append("   and b.user_id <> '" + ComacConstants.USER_ID_ADMIN + "'");//admin不顯示
		sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		return this.dao.executeQueryBySql(sb.toString());
	}
	
	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}
	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}	
	
}
