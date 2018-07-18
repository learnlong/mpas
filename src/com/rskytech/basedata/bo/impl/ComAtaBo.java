package com.rskytech.basedata.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAtaBo;
import com.rskytech.basedata.dao.IComAtaDao;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComReport;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.M13C;
import com.rskytech.pojo.MMain;
import com.rskytech.pojo.MSelect;
import com.rskytech.pojo.MSet;
import com.rskytech.pojo.MSetF;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.report.bo.IComReportBo;
import com.rskytech.struct.dao.ISsiSelectDao;
import com.rskytech.sys.dao.IMSetDao;
import com.rskytech.sys.dao.IMsiMainDao;
import com.rskytech.sys.dao.IMsiSelectDao;
import com.rskytech.task.dao.ITaskMsgDao;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ComAtaBo extends BaseBO implements IComAtaBo{

	private IComAtaDao comAtaDao;
	private IMsiMainDao msiMainDao;
	private ISsiSelectDao ssiSelectDao;
	private ITaskMsgDao taskMsgDao;
	private IComReportBo comReportBo;	
	private IMsiSelectDao msiSelectDao;
	private IMSetDao mSetDao;
	
	public List<HashMap> loadAtaTree(String msId, String ataId){
		List<ComAta> list = comAtaDao.loadChildAta(msId, ataId);
		if (list != null){
			List<HashMap> listJsonFV = new ArrayList();
			
			for (ComAta comAta : list) {
				HashMap hm = new HashMap();
				hm.put("id", comAta.getAtaId());
				hm.put("text", comAta.getAtaCode());
				hm.put("ataLevel", comAta.getAtaLevel());
//				if (comAta.getAtaLevel() != null && comAta.getAtaLevel() == 4) {
//					hm.put("leaf", "true");
//				} else 
				if (comAtaDao.loadChildAta(msId, comAta.getAtaId()).size() == 0) {
					hm.put("leaf", "true");
				}
				listJsonFV.add(hm);
			}
			return listJsonFV;
		}
		return null;
	}
	
	public List<HashMap> loadAtaList(String msId, String ataId, Page page){
		List<ComAta> list = comAtaDao.loadChildAta(msId, ataId, page);
		if (list != null){
			List<HashMap> listJsonFV = new ArrayList();
			
			for (ComAta comAta : list) {
				HashMap hm = new HashMap();
				hm.put("ataId", comAta.getAtaId());
				hm.put("ataCode", comAta.getAtaCode());
				hm.put("ataName", comAta.getAtaName());
				hm.put("ataLevel", comAta.getAtaLevel());
				hm.put("equipmentName", comAta.getEquipmentName());
				hm.put("equipmentPicNo", comAta.getEquipmentPicNo());
				hm.put("equipmentTypeNo", comAta.getEquipmentTypeNo());
				hm.put("equipmentPosition", comAta.getEquipmentPosition());
				hm.put("remark", comAta.getRemark());
				listJsonFV.add(hm);
			}
			return listJsonFV;
		}
		return null;
	}
	
	public HashMap<String, String> newOrUpdateMs(ComUser user, ComModelSeries ms, String jsonData, String parentId){
		HashMap<String, String> map = new HashMap<String, String>();
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		
		String msId = ms.getModelSeriesId();
		
		//判断编号是否重复
		Set<String> js = new HashSet<String>();
		for (int i = 0; i < jsonArray.size(); i++){
			String ataCode = jsonArray.getJSONObject(i).getString("ataCode").trim();					
			js.add(ataCode);
		}
		if (js.size() != jsonArray.size()){
			map.put("return", "exits");
			return map;
		}
		
		//开始保存操作
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String id = jsonObject.getString("ataId");
			
			//判断编号是否重复
			boolean bool = comAtaDao.checkAta(msId, id, jsonObject.getString("ataCode"));
			if (bool){
				map.put("return", "exits");
				return map;
			}
			
			ComAta comAta = new ComAta();
			//修改操作
			if (!BasicTypeUtils.isNullorBlank(id)) {
				comAta = (ComAta) comAtaDao.loadById(ComAta.class, id);
				
				//判断是否存在系统分析数据
				String isHave=comAtaDao.getAtaIsHaveMSI(comAta.getAtaCode(), msId);
				if("1".equals(isHave)){
					map.put("return", "havemsi");
					return map;
				}
				
				map.put("biaoshi", "update");
				map.put("ataId", id);
				map.put("ataCode", comAta.getAtaCode());
				map.put("ataName", comAta.getAtaName());
				map.put("equipmentName", comAta.getEquipmentName());
				map.put("equipmentPicNo", comAta.getEquipmentPicNo());
				map.put("equipmentTypeNo", comAta.getEquipmentTypeNo());
				map.put("equipmentPosition", comAta.getEquipmentPosition());
				map.put("remark", comAta.getRemark());
				map.put("validFlag", comAta.getValidFlag().toString());
				
				comAta.setAtaCode(jsonObject.getString("ataCode"));
				comAta.setAtaName(jsonObject.getString("ataName"));
				comAta.setEquipmentName(jsonObject.getString("equipmentName"));
				comAta.setEquipmentPicNo(jsonObject.getString("equipmentPicNo"));
				comAta.setEquipmentTypeNo(jsonObject.getString("equipmentTypeNo"));
				comAta.setEquipmentPosition(jsonObject.getString("equipmentPosition"));
				comAta.setRemark(jsonObject.getString("remark"));
				comAta.setValidFlag(2);
				this.saveOrUpdate(comAta, ComacConstants.DB_UPDATE, user.getUserId());																								
			} else {// 追加操作
//				if (parentId != null && !"0".equals(parentId)) {
//					ComAta parentAta = (ComAta) comAtaDao.loadById(ComAta.class, parentId);
//					comAta.setComAta(parentAta);
//					comAta.setAtaLevel(parentAta.getAtaLevel() + 1);
//				} else {
//					comAta.setComAta(null);
//					comAta.setAtaLevel(1);
//				}
				
				comAta.setComModelSeries(ms);
				comAta.setAtaCode(jsonObject.getString("ataCode"));
				comAta.setAtaName(jsonObject.getString("ataName"));
				comAta.setEquipmentName(jsonObject.getString("equipmentName"));
				comAta.setEquipmentPicNo(jsonObject.getString("equipmentPicNo"));
				comAta.setEquipmentTypeNo(jsonObject.getString("equipmentTypeNo"));
				comAta.setEquipmentPosition(jsonObject.getString("equipmentPosition"));
				comAta.setRemark(jsonObject.getString("remark"));
				comAta.setValidFlag(2);
				this.saveOrUpdate(comAta, ComacConstants.DB_INSERT, user.getUserId());
				
				map.put("biaoshi", "insert");
				map.put("ataId", comAta.getAtaId());
			}
		}	
		map.put("return", "success");
		return map;
	}
	
	public void deleteAta(String msId, String ataId){
		List<Object> ataList = comAtaDao.getSelfAndChildAta(msId, ataId);
		for (int i = ataList.size() - 1; i >= 0; i--){
			String dId = (String) ataList.get(i);
			
			List<MSet> mSetList = mSetDao.getMsetListByAtaId(dId);
			if (mSetList != null){
				for (MSet ms : mSetList){
					List<MSetF> msfList = mSetDao.getMsetfListByMsetId(ms.getMsetId());
					if (msfList != null){
						for (MSetF msf : msfList){
							List<M13C> m13List = mSetDao.getM13ListByMSetFId(msf.getMsetfId());
							if (m13List != null){
								for (M13C m13c : m13List){
									m13c.setMsetfId(null);
									this.update(m13c);
								}
							}
							
							this.delete(msf);
						}
					}
					
					this.delete(ms);
				}
			}
			
			List<MMain> mList = msiMainDao.getMMainByAtaIdAndModelSeries(dId, msId);
			if (mList != null){
				for (MMain m : mList){
					List<TaskMsg> taskList = this.taskMsgDao.getTaskMsgListByMainId(msId, m.getMsiId(), ComacConstants.SYSTEM_CODE, null);
					for(TaskMsg task : taskList){
						taskMsgDao.deleteTasksByTaskId(task.getTaskId());
					}
					
					List<ComReport> listReport = this.comReportBo.loadAllReportListNoPage(msId, ComacConstants.SYSTEM_CODE, m.getMsiId());
					if (listReport != null){
						for (ComReport cr : listReport){
							this.delete(cr);
						}
					}
					
					this.delete(m);//通过级联删除删除Main表产生的数据
					
				}
			}
			
			List<MSelect> msList = msiSelectDao.getMSelectByataId(dId, msId);
			if (msList != null){
				for (MSelect ms : msList){
					this.delete(ms);
				}
			}
			
			List<SMain> sList = ssiSelectDao.getSMainByAtaId(dId);			
			if (sList != null){
				for (SMain s : sList){
					List<TaskMsg> taskList = taskMsgDao.getTaskMsgListByMainId(msId, s.getSsiId(), ComacConstants.STRUCTURE_CODE, null);
					for(TaskMsg task : taskList){
						taskMsgDao.deleteTasksByTaskId(task.getTaskId());
					}
					
					List<ComReport> listReport = this.comReportBo.loadAllReportListNoPage(msId, ComacConstants.STRUCTURE_CODE, s.getSsiId());
					if (listReport != null){
						for (ComReport cr : listReport){
							this.delete(cr);
						}
					}
					
					this.delete(s);//通过级联删除删除Main表产生的数据
				}
			}
			
			List<SMain> sSelfList = ssiSelectDao.getSelfSMainByAtaId(dId);
			if (sSelfList != null){
				for (SMain s : sSelfList){
					List<TaskMsg> taskList = taskMsgDao.getTaskMsgListByMainId(msId, s.getSsiId(), ComacConstants.STRUCTURE_CODE, null);
					for(TaskMsg task : taskList){
						taskMsgDao.deleteTasksByTaskId(task.getTaskId());
					}
					this.delete(s);//通过级联删除删除Main表产生的数据
				}
			}
			
			ComAta ca = (ComAta) this.loadById(ComAta.class, dId);
			this.delete(ca);
		}
	}

	/**
	 * 根据ATA章节的ID，查询该ID记录的下一级记录集
	 * @throws BusinessException
	 * @param ataId
	 * @author zouhenglong
	 * @createDate 2014-11-10
	 */
	@Override
	public List<ComAta> loadAtaListByParentId(String modelSeriesId, String ataId) {
		DetachedCriteria dc = DetachedCriteria.forClass(ComAta.class);
		dc.addOrder(Order.asc("ataCode"));
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));
		if (ataId != null && !"0".equals(ataId)) {
			dc.add(Restrictions.eq("comAta.id", ataId));// 查询子ATA
		} else {
			dc.add(Restrictions.isNull("comAta.id"));// 查询根ATA
		}
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		return comAtaDao.findByCriteria(dc);
	}

	@Override
	public List<ComAta> findAllAtaSort(ComModelSeries comModelSeries) {
		DetachedCriteria dc = DetachedCriteria.forClass(ComAta.class);
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		dc.add(Restrictions.eq("comModelSeries", comModelSeries));
		dc.addOrder(Order.asc("ataCode"));
		return comAtaDao.findByCriteria(dc);
	}

	@Override
	public Set<Integer> importComAta(ComModelSeries serie, ArrayList<ComAta> oneList,
			ArrayList<ComAta> twoList, ArrayList<ComAta> threeList,
			ArrayList<ComAta> fourList, String curUserId) {
		Set<Integer> errorRows = new HashSet<Integer>();
		int errorRow = 0;
		//System.out.println("import ata");
		try {
			//第一级
			for (int m = 0; m < oneList.size(); m++) {
				ComAta cAtaM = oneList.get(m);
				errorRow = cAtaM.getExcelRow();//临时暂存了row号
				DetachedCriteria dc=DetachedCriteria.forClass(ComAta.class);
				dc.add(Restrictions.eq("ataCode",cAtaM.getAtaCode()));
				dc.add(Restrictions.eq("comModelSeries", serie));
				dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
				List list = comAtaDao.findByCriteria(dc);
				if(list != null && list.size() > 0){//修改
					cAtaM=(ComAta)list.get(0);
					cAtaM.setAtaName(oneList.get(m).getAtaName());
					cAtaM.setEquipmentName(oneList.get(m).getEquipmentName());
					cAtaM.setEquipmentPicNo(oneList.get(m).getEquipmentPicNo());
					cAtaM.setEquipmentTypeNo(oneList.get(m).getEquipmentTypeNo());
					cAtaM.setEquipmentPosition(oneList.get(m).getEquipmentPosition());
					cAtaM.setRemark(oneList.get(m).getRemark());
					cAtaM.setModifyDate(new Date());
					cAtaM.setModifyUser(curUserId);
				} else {//新增
					cAtaM.setAtaId(null);
					cAtaM.setCreateDate(new Date());
					cAtaM.setCreateUser(curUserId);
					cAtaM.setModifyDate(new Date());
					cAtaM.setModifyUser(curUserId);
					cAtaM.setComModelSeries(serie);
					cAtaM.setValidFlag(ComacConstants.VALIDFLAG_YES);
					cAtaM.setAtaLevel(1);
				}
				comAtaDao.saveOrUpdate(cAtaM);
			}
			//第二级
			for (int m = 0; m < twoList.size(); m++) {
				ComAta cAtaM = twoList.get(m);
				errorRow = cAtaM.getExcelRow();//临时暂存了row号
				String ataCode = cAtaM.getAtaCode();
				DetachedCriteria dc=DetachedCriteria.forClass(ComAta.class);
				dc.add(Restrictions.eq("ataCode",ataCode));
				dc.add(Restrictions.eq("comModelSeries", serie));
				dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
				List list = comAtaDao.findByCriteria(dc);
				if(list != null && list.size() > 0){
					cAtaM=(ComAta)list.get(0);
					cAtaM.setAtaName(twoList.get(m).getAtaName());
					cAtaM.setEquipmentName(twoList.get(m).getEquipmentName());
					cAtaM.setEquipmentPicNo(twoList.get(m).getEquipmentPicNo());
					cAtaM.setEquipmentTypeNo(twoList.get(m).getEquipmentTypeNo());
					cAtaM.setEquipmentPosition(twoList.get(m).getEquipmentPosition());
					cAtaM.setRemark(twoList.get(m).getRemark());
					cAtaM.setModifyDate(new Date());
					cAtaM.setModifyUser(curUserId);
				} else {//新增
					cAtaM.setAtaId(null);
					cAtaM.setCreateDate(new Date());
					cAtaM.setCreateUser(curUserId);
					cAtaM.setModifyDate(new Date());
					cAtaM.setModifyUser(curUserId);
					cAtaM.setComModelSeries(serie);
					cAtaM.setValidFlag(ComacConstants.VALIDFLAG_YES);
					cAtaM.setAtaLevel(2);
					//设置父对象
					DetachedCriteria dcParent = DetachedCriteria.forClass(ComAta.class);
					dcParent.add(Restrictions.eq("ataCode",ataCode.substring(0, 2)+"-00-00-00"));
					dcParent.add(Restrictions.eq("comModelSeries", serie));
					dcParent.add(Restrictions.eq("validFlag", ComacConstants.YES));
					List listParent = comAtaDao.findByCriteria(dcParent);
					if(listParent != null && listParent.size() > 0){
						cAtaM.setComAta((ComAta)listParent.get(0));
					}else{
						throw new Exception();
					}
				}
				comAtaDao.saveOrUpdate(cAtaM);
			}
			//第三级
			for (int m = 0; m < threeList.size(); m++) {
				ComAta cAtaM = threeList.get(m);
				errorRow = cAtaM.getExcelRow();//临时暂存了row号
				String ataCode = cAtaM.getAtaCode();
				DetachedCriteria dc=DetachedCriteria.forClass(ComAta.class);
				dc.add(Restrictions.eq("ataCode",ataCode));
				dc.add(Restrictions.eq("comModelSeries", serie));
				dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
				List list = comAtaDao.findByCriteria(dc);
				if(list != null && list.size() > 0){
					cAtaM=(ComAta)list.get(0);
					cAtaM.setAtaName(threeList.get(m).getAtaName());
					cAtaM.setEquipmentName(threeList.get(m).getEquipmentName());
					cAtaM.setEquipmentPicNo(threeList.get(m).getEquipmentPicNo());
					cAtaM.setEquipmentTypeNo(threeList.get(m).getEquipmentTypeNo());
					cAtaM.setEquipmentPosition(threeList.get(m).getEquipmentPosition());
					cAtaM.setRemark(threeList.get(m).getRemark());
					cAtaM.setModifyDate(new Date());
					cAtaM.setModifyUser(curUserId);
				} else {//新增
					cAtaM.setAtaId(null);
					cAtaM.setCreateDate(new Date());
					cAtaM.setCreateUser(curUserId);
					cAtaM.setModifyDate(new Date());
					cAtaM.setModifyUser(curUserId);
					cAtaM.setComModelSeries(serie);
					cAtaM.setValidFlag(ComacConstants.VALIDFLAG_YES);
					cAtaM.setAtaLevel(3);
					//设置父对象
					DetachedCriteria dcParent = DetachedCriteria.forClass(ComAta.class);
					dcParent.add(Restrictions.eq("ataCode", ataCode.substring(0, 5) + "-00-00"));
					dcParent.add(Restrictions.eq("comModelSeries", serie));
					dcParent.add(Restrictions.eq("validFlag", ComacConstants.YES));
					List listParent = comAtaDao.findByCriteria(dcParent);
					if(listParent != null && listParent.size()>0){
						cAtaM.setComAta((ComAta)listParent.get(0));
					}else{
						throw new Exception();
					}
				}
				comAtaDao.saveOrUpdate(cAtaM);
			}
			//第四级
			for (int m = 0; m < fourList.size(); m++) {
				ComAta cAtaM = fourList.get(m);
				errorRow = cAtaM.getExcelRow();//临时暂存了row号
				String ataCode = cAtaM.getAtaCode();
				DetachedCriteria dc=DetachedCriteria.forClass(ComAta.class);
				dc.add(Restrictions.eq("ataCode",ataCode));
				dc.add(Restrictions.eq("comModelSeries", serie));
				dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
				List list = comAtaDao.findByCriteria(dc);
				if(list != null && list.size() > 0){
					cAtaM=(ComAta)list.get(0);
					cAtaM.setAtaName(fourList.get(m).getAtaName());
					cAtaM.setEquipmentName(fourList.get(m).getEquipmentName());
					cAtaM.setEquipmentPicNo(fourList.get(m).getEquipmentPicNo());
					cAtaM.setEquipmentTypeNo(fourList.get(m).getEquipmentTypeNo());
					cAtaM.setEquipmentPosition(fourList.get(m).getEquipmentPosition());
					cAtaM.setRemark(fourList.get(m).getRemark());
					cAtaM.setModifyDate(new Date());
					cAtaM.setModifyUser(curUserId);
				} else {//新增
					cAtaM.setAtaId(null);
					cAtaM.setCreateDate(new Date());
					cAtaM.setCreateUser(curUserId);
					cAtaM.setModifyDate(new Date());
					cAtaM.setModifyUser(curUserId);
					cAtaM.setComModelSeries(serie);
					cAtaM.setValidFlag(ComacConstants.VALIDFLAG_YES);
					cAtaM.setAtaLevel(4);
					//设置父对象
					DetachedCriteria dcParent = DetachedCriteria.forClass(ComAta.class);
					dcParent.add(Restrictions.eq("ataCode", ataCode.substring(0, 8) + "-00"));
					dcParent.add(Restrictions.eq("comModelSeries", serie));
					dcParent.add(Restrictions.eq("validFlag", ComacConstants.YES));
					List listParent = comAtaDao.findByCriteria(dcParent);
					if(listParent != null && listParent.size()>0){
						cAtaM.setComAta((ComAta)listParent.get(0));
					}else{
						throw new Exception();
					}
				}
				comAtaDao.saveOrUpdate(cAtaM);
			}
		} catch (Exception e) {
			//e.printStackTrace()
			errorRows.add(errorRow);
		}
		
		return errorRows;
	}


	@Override
	public ComAta getComAtaByAtaCode(String ataCode, String msId) {
		List<ComAta> list=this.comAtaDao.getComAtaByAtaCode(ataCode, msId);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public IComAtaDao getComAtaDao() {
		return comAtaDao;
	}

	public void setComAtaDao(IComAtaDao comAtaDao) {
		this.comAtaDao = comAtaDao;
	}

	public IMsiMainDao getMsiMainDao() {
		return msiMainDao;
	}

	public void setMsiMainDao(IMsiMainDao msiMainDao) {
		this.msiMainDao = msiMainDao;
	}

	public ISsiSelectDao getSsiSelectDao() {
		return ssiSelectDao;
	}

	public void setSsiSelectDao(ISsiSelectDao ssiSelectDao) {
		this.ssiSelectDao = ssiSelectDao;
	}

	public ITaskMsgDao getTaskMsgDao() {
		return taskMsgDao;
	}

	public void setTaskMsgDao(ITaskMsgDao taskMsgDao) {
		this.taskMsgDao = taskMsgDao;
	}

	public IMsiSelectDao getMsiSelectDao() {
		return msiSelectDao;
	}

	public void setMsiSelectDao(IMsiSelectDao msiSelectDao) {
		this.msiSelectDao = msiSelectDao;
	}

	public IComReportBo getComReportBo() {
		return comReportBo;
	}

	public void setComReportBo(IComReportBo comReportBo) {
		this.comReportBo = comReportBo;
	}

	@Override
	public List<Object> getSelfAndChildAta(String msId, String ataId) {
		return this.comAtaDao.getSelfAndChildAta(msId, ataId);
	}

	public IMSetDao getmSetDao() {
		return mSetDao;
	}

	public void setmSetDao(IMSetDao mSetDao) {
		this.mSetDao = mSetDao;
	}

	@Override
	public List<ComAta> getComAtaIdByCode(String ataCode) {
		 return this.comAtaDao.getAtaIdByAtaCode(ataCode);
	}

	
}
