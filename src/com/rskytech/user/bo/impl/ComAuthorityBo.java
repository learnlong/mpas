package com.rskytech.user.bo.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComAuthority;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComProfession;
import com.rskytech.user.bo.IComAuthorityBo;
import com.rskytech.user.dao.IComAuthorityDao;

@SuppressWarnings({"rawtypes","unchecked"})
public class ComAuthorityBo extends BaseBO implements IComAuthorityBo {
	
	private IComAuthorityDao comAuthorityDao;
	
	/**
	 * 得到专业室的权限
	 */
	@Override
	public List<ComAuthority> findAuthorityByParam(String modelSeriesId, String professionId, String analysisType) {
		DetachedCriteria dc = DetachedCriteria.forClass(ComAuthority.class);
		if(professionId != null && !"".equals(professionId)){
			dc.add(Restrictions.eq("comProfession.professionId", professionId));
		}
		if(modelSeriesId != null && !"".equals(modelSeriesId)){
			dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));
		}
		if(analysisType != null && !"".equals(analysisType)){
			dc.add(Restrictions.eq("authorityType", analysisType));
		}
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		return this.findByCritera(dc);
	}

	@Override
	public boolean updateAuthorityForProfession(String modelSeriesId, String professionId, String analysisType, String choosedIds,String choosedNoChilrdIds, String curUserId) {
		if(professionId == null || "".equals(professionId) || modelSeriesId == null || "".equals(modelSeriesId) || analysisType == null || "".equals(analysisType)){
			return false;
		}
		if(choosedNoChilrdIds!=null&&!choosedNoChilrdIds.equals("")){//用来存储还没有渲染子节点且选中的节点的ids不为空
			String formartIds = "'" + choosedNoChilrdIds.replace(" ", "").replace(",", "','") + "'";
			String ataOrAreaIds=this.getAtaIdsBychoosedNoChilrdIds(modelSeriesId, formartIds,analysisType);
			if(null!=ataOrAreaIds&&!ataOrAreaIds.equals("")){
				if(choosedIds.equals("")){
					ataOrAreaIds=ataOrAreaIds.substring(1);
				}
				choosedIds=choosedIds+ataOrAreaIds;
			}
			
		}
		StringBuffer sb = null;
		if(choosedIds == null || "".equals(choosedIds)){
			//1.删除用户工作权限关联表（COM_USER_AUTHORITY）的数据
			sb = new StringBuffer();
			sb.append("delete com_user_authority");
			sb.append(" where authority_id in (select authority_id");
			sb.append("                          from com_authority");
			sb.append("                         where model_series_id = '" + modelSeriesId + "'");
			sb.append("                           and profession_id = '" + professionId + "'");
			sb.append("                           and authority_type = '" + analysisType + "'");
			sb.append("                           and valid_flag = " + ComacConstants.VALIDFLAG_YES + ")");
			this.comAuthorityDao.executeBySql(sb.toString());
			//2.删除存在未选择的
			sb = new StringBuffer();
			sb.append("update com_authority set valid_flag = " + ComacConstants.VALIDFLAG_NO);
			sb.append(" where model_series_id = '" + modelSeriesId + "'");
			sb.append("   and profession_id = '" + professionId + "'");
			sb.append("   and authority_type = '" + analysisType + "'");
			sb.append("   and valid_flag = " + ComacConstants.VALIDFLAG_YES);
			this.comAuthorityDao.executeBySql(sb.toString());
		}else{
			List<ComAuthority> oldComAuthorityList = this.findAuthorityByParam(modelSeriesId, professionId, analysisType);
			String[] selectIds = choosedIds.replace(" ", "").split(",");
			String inId = "'" + choosedIds.replace(" ", "").replace(",", "','") + "'";
			String[] arr=inId.split(",");
			List<String> listIn=new ArrayList<String>();
			for (int i = 0; i < arr.length; i++) {
				listIn.add(arr[i]);
			}
			int size=900;//一个IN里面可以存放多少个范围值
			int inNums=arr.length/size+(arr.length%900==0 ?0:1); //有多少个IN
			
			//1.删除用户工作权限关联表（COM_USER_AUTHORITY）的数据
			sb = new StringBuffer();
			sb.append("delete com_user_authority");
			sb.append(" where authority_id in (select authority_id");
			sb.append("                          from com_authority");
			sb.append("                         where model_series_id = '" + modelSeriesId + "'");
			sb.append("                           and profession_id = '" + professionId + "'");
			sb.append("                           and authority_type = '" + analysisType + "'");
			sb.append("                           and valid_flag = " + ComacConstants.VALIDFLAG_YES);
			if(inNums==1){
				sb.append("                           and content not in (" + inId + "))");
			}else{
				List<String> tempList=null;
				for (int i = 0; i < inNums; i++) {
					tempList=listIn.subList(i*size, ((i+1)*size>arr.length?arr.length:(i+1)*size));
					String tempStr="";
					for (int j = 0; j < tempList.size(); j++) {
						tempStr=tempStr+tempList.get(j)+",";
					}
					tempStr=tempStr.substring(0, tempStr.lastIndexOf(","));
					if(i==0){
						sb.append("                           and (content not in (" + tempStr + ")");
					}else if(i==(inNums-1)){
						sb.append("                           and content not in (" + tempStr + ")))");
					}else{
						sb.append("                           and content not in (" + tempStr + ")");
					}
					
				}
				
			}
			this.comAuthorityDao.executeBySql(sb.toString());
			//2.删除存在未选择的
			sb = new StringBuffer();
			sb.append("update com_authority set valid_flag = " + ComacConstants.VALIDFLAG_NO);
			sb.append(" where model_series_id = '" + modelSeriesId + "'");
			sb.append("   and profession_id = '" + professionId + "'");
			sb.append("   and authority_type = '" + analysisType + "'");
			sb.append("   and valid_flag = " + ComacConstants.VALIDFLAG_YES);
			if(inNums==1){
				sb.append("   and content not in (" + inId + ")");
			}else{
				List<String> tempList=null;
				for (int i = 0; i < inNums; i++) {
					tempList=listIn.subList(i*size, ((i+1)*size>arr.length?arr.length:(i+1)*size));
					String tempStr="";
					for (int j = 0; j < tempList.size(); j++) {
						tempStr=tempStr+tempList.get(j)+",";
					}
					tempStr=tempStr.substring(0, tempStr.lastIndexOf(","));
					if(i==0){
						sb.append("                           and (content not in (" + tempStr + ")");
					}else if(i==(inNums-1)){
						sb.append("                           and content not in (" + tempStr + "))");
					}else{
						sb.append("                           and content not in (" + tempStr + ")");
					}
					
				}
				
			}
			this.comAuthorityDao.executeBySql(sb.toString());
			//3.插入
			for(int i=0; i<selectIds.length; i++){
				String tmpId = selectIds[i];
				if(!isExistFlag(oldComAuthorityList, tmpId)){
					ComAuthority comAuthority = new ComAuthority();
					ComModelSeries comModelSeries = (ComModelSeries)comAuthorityDao.loadById(ComModelSeries.class, modelSeriesId);
					comAuthority.setComModelSeries(comModelSeries);
					ComProfession comProfession = (ComProfession)comAuthorityDao.loadById(ComProfession.class, professionId);
					comAuthority.setComProfession(comProfession);
					comAuthority.setAuthorityType(analysisType);
//					System.out.println("tmpId===="+tmpId);
					comAuthority.setContent(tmpId);
					comAuthority.setValidFlag(ComacConstants.VALIDFLAG_YES);
					this.save(comAuthority, curUserId);
				}
			}
		}
		return true;
	}

	private boolean isExistFlag(List<ComAuthority> comAuthorityList, String content){
		boolean flag = false;
		for(int i=0; i<comAuthorityList.size(); i++){
			if(comAuthorityList.get(i).getContent().equals(content)){
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	@Override
	public List getContentsByProsessionIdAndUserID(String modelSeriesId, String professionId, String userId, String analysisType) {
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct a.authority_id, a.content");
		sb.append("  from com_authority a, com_user_authority b");
		sb.append(" where a.authority_id = b.authority_id");
		sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		if(modelSeriesId != null && !"".equals(modelSeriesId)){
			sb.append("   and a.model_series_id = '" + modelSeriesId + "'");
		}
		if(professionId != null && !"".equals(professionId)){
			sb.append("   and a.profession_id = '" + professionId + "'");
		}
		if(analysisType != null && !"".equals(analysisType)){
			sb.append("   and a.authority_type = '" + analysisType + "'");
		}
		if(userId != null){
			sb.append("   and b.user_id = '" + userId + "'");
		}
		return this.comAuthorityDao.executeQueryBySql(sb.toString());
	}

	@Override
	public boolean updateAuthorityForUser(String modelSeriesId,
			String professionId, String userId, String analysisType,
			String choosedIds,String choosedNoChilrdIds, String curUserId) {
		if(professionId == null || "".equals(professionId) || modelSeriesId == null || "".equals(modelSeriesId) || userId == null || "".equals(userId) || analysisType == null || "".equals(analysisType)){
			return false;
		}
		if(choosedNoChilrdIds!=null&&!choosedNoChilrdIds.equals("")){//用来存储还没有渲染子节点且选中的节点的ids不为空
			String formartIds = "'" + choosedNoChilrdIds.replace(" ", "").replace(",", "','") + "'";
			String ataOrAreaIds=this.getAtaIdsBychoosedNoChilrdIds(modelSeriesId, formartIds,analysisType);
			if(null!=ataOrAreaIds&&!ataOrAreaIds.equals("")){
				if(choosedIds.equals("")){
					ataOrAreaIds=ataOrAreaIds.substring(1);
				}
				choosedIds=choosedIds+ataOrAreaIds;
			}
			
		}
		StringBuffer sb = null;
		if(choosedIds == null || "".equals(choosedIds)){
			//1.删除不需要的权限数据（COM_USER_AUTHORITY）的数据
			sb = new StringBuffer();
			sb.append("delete com_user_authority");
			sb.append(" where authority_id in (select a.authority_id");
			sb.append("                          from com_authority a, com_user_authority b");
			sb.append("                         where a.authority_id = b.authority_id");
			sb.append("                           and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("                           and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("                           and a.model_series_id = '" + modelSeriesId + "'");
			sb.append("                           and a.profession_id = '" + professionId + "'");
			sb.append("                           and a.authority_type = '" + analysisType + "'");
			sb.append("                           and b.user_id = '" + userId + "')");
			sb.append("   and user_id = '" + userId + "'");
			this.comAuthorityDao.executeBySql(sb.toString());
		} else {
			String inId = "'" + choosedIds.replace(" ", "").replace(",", "','") + "'";
			String[] arr=inId.split(",");
			List<String> listIn=new ArrayList<String>();
			for (int i = 0; i < arr.length; i++) {
				listIn.add(arr[i]);
			}
			int size=900;//一个IN里面可以存放多少个范围值
			int inNums=arr.length/size+(arr.length%900==0 ?0:1); //有多少个IN
			//1.删除不需要的权限数据（COM_USER_AUTHORITY）的数据
			sb = new StringBuffer();
			sb.append("delete com_user_authority");
			sb.append(" where authority_id in (select a.authority_id");
			sb.append("                          from com_authority a, com_user_authority b");
			sb.append("                         where a.authority_id = b.authority_id");
			sb.append("                           and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("                           and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("                           and a.model_series_id = '" + modelSeriesId + "'");
			sb.append("                           and a.profession_id = '" + professionId + "'");
			sb.append("                           and a.authority_type = '" + analysisType + "'");
			sb.append("                           and b.user_id = '" + userId + "'");
			if(inNums==1){
				sb.append("                           and a.content not in (" + inId + "))");
			}else{
				List<String> tempList=null;
				for (int i = 0; i < inNums; i++) {
					tempList=listIn.subList(i*size, ((i+1)*size>arr.length?arr.length:(i+1)*size));
					String tempStr="";
					for (int j = 0; j < tempList.size(); j++) {
						tempStr=tempStr+tempList.get(j)+",";
					}
					tempStr=tempStr.substring(0, tempStr.lastIndexOf(","));
					if(i==(inNums-1)){
						sb.append("                           and a.content not in (" + tempStr + "))");
					}else{
						sb.append("                           and a.content not in (" + tempStr + ")");
					}
					
				}
				
			}
			sb.append("   and user_id = '" + userId + "'");
			this.comAuthorityDao.executeBySql(sb.toString());
			//2.插入选中的数据
			sb = new StringBuffer();
			sb.append("insert into com_user_authority");
			sb.append("  (user_id,");
			sb.append("   authority_id,");
			sb.append("   valid_flag,");
			sb.append("   create_user,");
			sb.append("   create_date,");
			sb.append("   modify_user,");
			sb.append("   modify_date)");
			sb.append("  select '" + userId + "',");
			sb.append("         authority_id,");
			sb.append("         " + ComacConstants.VALIDFLAG_YES + ",");
			sb.append("         '" + curUserId + "',");
			sb.append("         sysdate,");
			sb.append("         '" + curUserId + "',");
			sb.append("         sysdate");
			sb.append("    from com_authority");
			sb.append("   where valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("     and model_series_id = '" + modelSeriesId + "'");
			sb.append("     and profession_id = '" + professionId + "'");
			sb.append("     and authority_type = '" + analysisType + "'");
			if(inNums==1){
				sb.append("     and content in (" + inId + ")");
			}else{
				List<String> tempList=null;
				for (int i = 0; i < inNums; i++) {
					tempList=listIn.subList(i*size, ((i+1)*size>arr.length?arr.length:(i+1)*size));
					String tempStr="";
					for (int j = 0; j < tempList.size(); j++) {
						tempStr=tempStr+tempList.get(j)+",";
					}
					tempStr=tempStr.substring(0, tempStr.lastIndexOf(","));
					if(i==0){
						sb.append("                           and (content in (" + tempStr + ")");
					}else if(i==(inNums-1)){
						sb.append("                           or content in (" + tempStr + "))");
					}else{
						sb.append("                           or content in (" + tempStr + ")");
					}
					
				}
				
			}
			sb.append("     and authority_id not in");
			sb.append("         (select authority_id from com_user_authority where user_id = '" + userId + "')");
			this.comAuthorityDao.executeBySql(sb.toString());
		}
		return true;
	}

	@Override
	public boolean getTreeIsCheckBeforeLoad(String modelSeriesId,
			String professionId, String analysisType) {
		if(professionId == null || "".equals(professionId) || analysisType == null || "".equals(analysisType)){
			return false;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select 1");
		sb.append("  from com_authority a");
		sb.append(" where a.model_series_id = '" + modelSeriesId + "'");
		sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and a.profession_id = '" + professionId + "'");
		sb.append("   and a.authority_type = '" + analysisType + "'");
		List dataList = this.comAuthorityDao.executeQueryBySql(sb.toString());
		if(dataList == null || dataList.size() == 0){
			return false;
		}else {
			return true;
		}
	}

	@Override
	public boolean getTreeIsCheckBeforeLoadForUser(String modelSeriesId,
			String professionId, String analysisType, String userId) {
		if(professionId == null || "".equals(professionId) || analysisType == null || "".equals(analysisType) || userId == null || "".equals(userId)){
			return false;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select 1");
		sb.append("  from com_authority a, com_user_authority b");
		sb.append(" where a.authority_id = b.authority_id");
		sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and a.model_series_id = '" + modelSeriesId + "'");
		sb.append("   and a.profession_id = '" + professionId + "'");
		sb.append("   and a.authority_type = '" + analysisType + "'");
		sb.append("   and b.user_id = '" + userId + "'");
		List dataList = this.comAuthorityDao.executeQueryBySql(sb.toString());
		if(dataList == null || dataList.size() == 0){
			return false;
		}else {
			return true;
		}
	}

	public IComAuthorityDao getComAuthorityDao() {
		return comAuthorityDao;
	}

	public void setComAuthorityDao(IComAuthorityDao comAuthorityDao) {
		this.comAuthorityDao = comAuthorityDao;
	}

	@Override
	public String getAtaIdsBychoosedNoChilrdIds(String modelSeriesId,
			String choosedNoChilrdIds,String analysisType) {
		List<Object[]> list=null;
		StringBuffer sb=new StringBuffer();
		if (analysisType.equals(ComacConstants.SYSTEM_CODE)||analysisType.equals(ComacConstants.STRUCTURE_CODE)) {//查ATA
			sb.append("select * from COM_ATA t where t.model_series_id='"+modelSeriesId+"' and t.valid_flag='1' and t.ata_id in("+choosedNoChilrdIds+") ");
			sb.append(" or t.parent_ata_id in("+choosedNoChilrdIds+") ");
			sb.append(" or exists ");
			sb.append(" (select * from (select * from COM_ATA t where t.parent_ata_id in("+choosedNoChilrdIds+")) s where t.parent_ata_id=s.ata_id) ");
			sb.append(" or exists (");
			sb.append(" select * from ");
			sb.append(" (select * from COM_ATA t where exists ");
			sb.append(" (select * from (select * from COM_ATA t where t.parent_ata_id in("+choosedNoChilrdIds+")) b");
			sb.append(" where t.parent_ata_id=b.ata_id)) c where  t.parent_ata_id=c.ata_id )");
			sb.append("or exists ");
			sb.append("(select * from (select * from COM_ATA t where exists(select * from (select * from COM_ATA t where exists (select * from (select * from COM_ATA t where t.parent_ata_id in ");
			sb.append("(" +choosedNoChilrdIds+")) b where t.parent_ata_id = b.ata_id)) c where t.parent_ata_id = c.ata_id)) d  where t.parent_ata_id = d.ata_id) ");
			sb.append("or exists (select * from (select * from COM_ATA t where exists (select * from (select * from COM_ATA t where exists(select * from (select * from COM_ATA t where exists (select * ");
			sb.append("from (select * from COM_ATA t where t.parent_ata_id in ("+choosedNoChilrdIds+")) b ");
			sb.append("where t.parent_ata_id = b.ata_id)) c where t.parent_ata_id = c.ata_id)) d  where t.parent_ata_id = d.ata_id)) e where t.parent_ata_id = e.ata_id) ");
			sb.append("or exists (select * from (select * from COM_ATA t where exists(select * from (select * from COM_ATA t where exists (select * from (select * from COM_ATA t where exists ");
			sb.append("(select * from (select * from COM_ATA t where exists (select * from (select * from COM_ATA t where t.parent_ata_id in ("+choosedNoChilrdIds+")) b ");
			sb.append("where t.parent_ata_id = b.ata_id)) c where t.parent_ata_id = c.ata_id)) d where t.parent_ata_id = d.ata_id)) e where t.parent_ata_id = e.ata_id)) f where t.parent_ata_id = f.ata_id) ");
		} else {//查AREA
			sb.append("select * from COM_AREA t where t.model_series_id='"+modelSeriesId+"' and t.valid_flag='1' and t.area_id in("+choosedNoChilrdIds+") ");
			sb.append(" or t.parent_area_id in("+choosedNoChilrdIds+") ");
			sb.append(" or exists ");
			sb.append(" (select * from (select * from COM_AREA t where t.parent_area_id in("+choosedNoChilrdIds+")) s where t.parent_area_id=s.area_id) ");
			sb.append(" or exists (");
			sb.append(" select * from ");
			sb.append(" (select * from COM_AREA t where exists ");
			sb.append(" (select * from (select * from COM_AREA t where t.parent_area_id in("+choosedNoChilrdIds+")) b");
			sb.append(" where t.parent_area_id=b.area_id)) c where  t.parent_area_id=c.area_id )");
		}

		list=this.comAuthorityDao.executeQueryBySql(sb.toString());
		StringBuffer strBuffer=new StringBuffer();
		if(list != null || list.size() > 0){
			for (Object[] objects : list) {
				strBuffer.append(","+objects[0]);
			}
			return strBuffer.toString();
		}
		return null;
	}
	
	
	
}
