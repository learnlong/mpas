package com.rskytech.basedata.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import com.rskytech.area.dao.IZa1Dao;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.basedata.dao.IComAreaDao;
import com.rskytech.lhirf.dao.ILhMainDao;
import com.rskytech.lhirf.dao.ILhSelectDao;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComAreaDetail;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComReport;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.LhMain;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.ZaMain;
import com.rskytech.report.bo.IComReportBo;
import com.rskytech.task.bo.ITaskMsgBo;
import com.rskytech.task.dao.ITaskMsgDao;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ComAreaBo extends BaseBO implements IComAreaBo {
	
	private IComAreaDao comAreaDao;
	private ILhMainDao lhMainDao;
	private ITaskMsgDao taskMsgDao;
	private IZa1Dao za1Dao;
	private ITaskMsgBo taskMsgBo;
	private ILhSelectDao lhSelectDao;
	private IComReportBo comReportBo;
	
	public List<HashMap> loadAreaTree(String msId, String areaId){
		List<ComArea> list = comAreaDao.loadChildArea(msId, areaId);
		if (list != null){
			List<HashMap> listJsonFV = new ArrayList();
			
			for (ComArea comArea : list) {
				HashMap hm = new HashMap();
				hm.put("id", comArea.getAreaId());
				hm.put("areaCode", comArea.getAreaCode());
				hm.put("text", comArea.getAreaCode() + "-" + comArea.getAreaName());
				hm.put("areaLevel", comArea.getAreaLevel());
//				hm.put("parentAreaId", comArea.getComArea().getAreaId());
				if (comArea.getAreaLevel() != null && comArea.getAreaLevel() == 3) {
					hm.put("leaf", "true");
				} else if (comAreaDao.loadChildArea(msId, comArea.getAreaId()).size() == 0) {
					hm.put("leaf", "true");
				}
				listJsonFV.add(hm);
			}
			return listJsonFV;
		}
		return null;
	}
	
	public List<HashMap> loadAreaList(String msId, String areaId, Page page){
		List<ComArea> list = comAreaDao.loadChildArea(msId, areaId, page);
		if (list != null){
			List<HashMap> listJsonFV = new ArrayList();
			
			for (ComArea comArea : list) {
				HashMap hm = new HashMap();
				hm.put("areaId", comArea.getAreaId());
				hm.put("areaCode", comArea.getAreaCode());
				hm.put("areaName", comArea.getAreaName());
				hm.put("areaLevel", comArea.getAreaLevel());
				hm.put("reachWay", comArea.getReachWay());
				hm.put("wirePiping", comArea.getWirePiping());
				hm.put("remark", comArea.getRemark());
				
				List<ComAreaDetail> cadList = comAreaDao.loadAreaDetail(comArea.getAreaId());
				if (cadList != null){
					String equipmentName = "";
					String equipmentPicNo = "";
					String equipmentTypeNo = "";
					for (ComAreaDetail cad : cadList){
						equipmentName += cad.getEquipmentName() + ";";
						
						if (cad.getEquipmentPicNo() != null && !"".equals(cad.getEquipmentPicNo())){
							equipmentPicNo += cad.getEquipmentPicNo() + ";";
						}
						
						if (cad.getEquipmentTypeNo() != null && !"".equals(cad.getEquipmentTypeNo())){
							equipmentTypeNo += cad.getEquipmentTypeNo() + ";";
						}
					}
					
					if (!"".equals(equipmentName)){
						equipmentName = equipmentName.substring(0, equipmentName.length() - 1);
					}
					if (!"".equals(equipmentPicNo)){
						equipmentPicNo = equipmentPicNo.substring(0, equipmentPicNo.length() - 1);
					}
					if (!"".equals(equipmentTypeNo)){
						equipmentTypeNo = equipmentTypeNo.substring(0, equipmentTypeNo.length() - 1);
					}
					
					hm.put("equipmentName", equipmentName);
					hm.put("equipmentPicNo", equipmentPicNo);
					hm.put("equipmentTypeNo", equipmentTypeNo);
				}
				
				listJsonFV.add(hm);
			}
			return listJsonFV;
		}
		return null;
	}
	
	public String newOrUpdateArea(ComUser user, ComModelSeries ms, String jsonData, String parentId){
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		
		String msId = ms.getModelSeriesId();
		
		//判断编号是否重复
		Set<String> js = new HashSet<String>();
		for (int i = 0; i < jsonArray.size(); i++){
			String areaCode = jsonArray.getJSONObject(i).getString("areaCode").trim();					
			js.add(areaCode);
		}
		if (js.size() != jsonArray.size()){
			return "exits";
		}
		
		//开始保存操作
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String id = jsonObject.getString("areaId");
			
			//判断编号是否重复
			boolean bool = comAreaDao.checkArea(msId, id, jsonObject.getString("areaCode"));
			if (bool){
				return "exits";
			}
			
			ComArea comArea = new ComArea();
			//修改操作
			if (!BasicTypeUtils.isNullorBlank(id)) {
				comArea = (ComArea) comAreaDao.loadById(ComArea.class, id);
				
				comArea.setAreaCode(jsonObject.getString("areaCode"));
				comArea.setAreaName(jsonObject.getString("areaName"));
				comArea.setReachWay(jsonObject.getString("reachWay"));
				comArea.setWirePiping(jsonObject.getString("wirePiping"));
				comArea.setRemark(jsonObject.getString("remark"));
				comArea.setValidFlag(ComacConstants.VALIDFLAG_YES);
				this.saveOrUpdate(comArea, ComacConstants.DB_UPDATE, user.getUserId());																								
			} else {// 追加操作
				if (parentId != null && !"0".equals(parentId)) {
					ComArea parentArea = (ComArea) comAreaDao.loadById(ComArea.class, parentId);
					comArea.setComArea(parentArea);
					comArea.setAreaLevel(parentArea.getAreaLevel() + 1);
				} else {
					comArea.setComArea(null);
					comArea.setAreaLevel(1);
				}
				
				comArea.setComModelSeries(ms);
				comArea.setAreaCode(jsonObject.getString("areaCode"));
				comArea.setAreaName(jsonObject.getString("areaName"));
				comArea.setReachWay(jsonObject.getString("reachWay"));
				comArea.setWirePiping(jsonObject.getString("wirePiping"));
				comArea.setRemark(jsonObject.getString("remark"));
				comArea.setValidFlag(ComacConstants.VALIDFLAG_YES);
				this.saveOrUpdate(comArea, ComacConstants.DB_INSERT, user.getUserId());
			}
		}	
		return "success";
	}
	
	public List<HashMap> loadEquipList(String areaId){
		List<ComAreaDetail> list = comAreaDao.loadAreaDetail(areaId);
		if (list != null){
			List<HashMap> listJsonFV = new ArrayList();
			
			for (ComAreaDetail cad : list) {
				HashMap hm = new HashMap();
				hm.put("detailId", cad.getDetailId());
				hm.put("equipmentName", cad.getEquipmentName());
				hm.put("equipmentPicNo", cad.getEquipmentPicNo());
				hm.put("equipmentTypeNo", cad.getEquipmentTypeNo());
				
				listJsonFV.add(hm);
			}
			return listJsonFV;
		}
		return null;
	}
	
	public String newOrUpdateEquip(String jsonData, String areaId){
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		
		//开始保存操作
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String id = jsonObject.getString("detailId");
			
			ComAreaDetail cad = new ComAreaDetail();
			//修改操作
			if (!BasicTypeUtils.isNullorBlank(id)) {
				cad = (ComAreaDetail) comAreaDao.loadById(ComAreaDetail.class, id);
				
				cad.setEquipmentName(jsonObject.getString("equipmentName"));
				cad.setEquipmentPicNo(jsonObject.getString("equipmentPicNo"));
				cad.setEquipmentTypeNo(jsonObject.getString("equipmentTypeNo"));
				this.saveOrUpdate(cad);																								
			} else {// 追加操作
				ComArea ca = new ComArea();
				ca.setAreaId(areaId);
				
				cad.setComArea(ca);
				cad.setEquipmentName(jsonObject.getString("equipmentName"));
				cad.setEquipmentPicNo(jsonObject.getString("equipmentPicNo"));
				cad.setEquipmentTypeNo(jsonObject.getString("equipmentTypeNo"));
				this.saveOrUpdate(cad);
			}
		}	
		return "success";
	}
	
	public void deleteArea(String msId, String areaId){
		List<Object> areaList = comAreaDao.getSelfAndChildArea(msId, areaId);
		for (int i = areaList.size() - 1; i >= 0; i--){
			String dId = (String) areaList.get(i);
			
			List<LhMain> lhList = lhMainDao.getLhMainListByAreaId(dId, msId, null, null);
			if (lhList != null){
				for (LhMain lh : lhList){
					List<TaskMsg> taskList = this.taskMsgDao.getTaskMsgListByMainId(msId, lh.getHsiId(), ComacConstants.LHIRF_CODE, null);
					for (TaskMsg task : taskList){
						taskMsgDao.deleteTasksByTaskId(task.getTaskId());
					}
					
					List<ComReport> listReport = this.comReportBo.loadAllReportListNoPage(msId, ComacConstants.LHIRF_CODE, lh.getHsiId());
					if (listReport != null){
						for (ComReport cr : listReport){
							this.delete(cr);
						}
					}
			
					lhSelectDao.deleteLhMain(lh);
				}
			}
			
			ZaMain za = za1Dao.getZaMainByAreaId(msId, dId);
			if (za != null){
				taskMsgBo.deleteAreaTask(msId, za.getZaId(), "ALL");
				za1Dao.deleteAreaAnalysisAll(za.getZaId());
				
				List<ComReport> listReport = this.comReportBo.loadAllReportListNoPage(msId, ComacConstants.ZONAL_CODE, za.getZaId());
				if (listReport != null){
					for (ComReport cr : listReport){
						this.delete(cr);
					}
				}
				this.delete(za);//通过级联删除删除Main表产生的数据
			}
			
			
			
			List<ComAreaDetail> cadList = comAreaDao.loadAreaDetail(dId);
			if (cadList != null){
				for (ComAreaDetail cad : cadList){
					this.delete(cad);
				}
			}
			
			ComArea ca = (ComArea) this.loadById(ComArea.class, dId);
			this.delete(ca);
		}
	}
	
	/**
	 * 将所属区域的编号转为Id，并用逗号隔开
	 * @param OwnArea
	 * @param modelSeriesId
	 * @return
	 * @throws BusinessException
	 */
	public String getAreaIdByAreaCode(String OwnArea, String modelSeriesId)
				throws BusinessException {
			if (BasicTypeUtils.isNullorBlank(OwnArea)) {
				return null;
			}
			String[] areaCodes = OwnArea.split(",");
			String areaIds = "";
			for (String string : areaCodes) {
				ComArea comArea = this.getComAreaByAreaCode(string, modelSeriesId);
				if (comArea != null) {
					areaIds += "," + comArea.getAreaId();
				}
			}
			if (areaIds.length() > 1) {
				return areaIds.substring(1);
			}
			return null;
		
	}

	/**
	 * 将所属区域的Id转为编号，并用逗号隔开
	 * 
	 * @param ownArea
	 * @return 区域编号
	 * @throws BusinessException
	 * @author chendexu createdate 2012-09-04
	 */
	public String getAreaCodeByAreaId(String ownArea) throws BusinessException {

		if (BasicTypeUtils.isNullorBlank(ownArea)) {
			return "";
		}
		String[] areaIds = ownArea.split(",");
		String areaCodes = "";
		for (String string : areaIds) {
			if (!BasicTypeUtils.isNullorBlank(string)) {
				ComArea comArea = this.getAreaById(string);
				if (comArea != null) {
					areaCodes += "," + comArea.getAreaCode();
				}
			}
		}
		if (areaCodes.length() > 1) {
			return areaCodes.substring(1);
		}

		return null;
	
	}
	/**
	 * 根据区域编号及所属机型查询区域
	 * 
	 * @param areaCode
	 * @param modelSeriesId
	 * @throws BusinessException
	 * @author chendexu createdate 2012-08-29
	 */
	@Override
	public ComArea getComAreaByAreaCode(String areaCode, String modelSeriesId)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(ComArea.class);
		dc.add(Restrictions.eq("areaCode", areaCode));
		dc.add(Restrictions.eq("comModelSeries.id", modelSeriesId));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		List<ComArea> list = this.findByCritera(dc);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 通过id查询区域
	 * @param id
	 * @return
	 */
	private ComArea getAreaById(String id) {
		DetachedCriteria dc = DetachedCriteria.forClass(ComArea.class);
		dc.add(Restrictions.eq("id", id));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		List<ComArea> list = this.findByCritera(dc);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;

	}
	
	@Override
	public List<ComArea> loadAreaListByParentId(String modelSeriesId, String parentAreaId) {
		DetachedCriteria dc = DetachedCriteria.forClass(ComArea.class);
		dc.add(Restrictions.eq("comModelSeries.id", modelSeriesId));
		if (parentAreaId != null && !"0".equals(parentAreaId)) {
			dc.add(Restrictions.eq("comArea.id", parentAreaId));
		}else {
			dc.add(Restrictions.isNull("comArea.id"));
		}
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		dc.addOrder(Order.asc("areaCode"));
		return this.findByCritera(dc);
	}

	@Override
	public List<ComArea> findAllAreaSort(ComModelSeries comModelSeries) {
		DetachedCriteria dc = DetachedCriteria.forClass(ComArea.class);
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		dc.add(Restrictions.eq("comModelSeries", comModelSeries));
		dc.addOrder(Order.asc("areaCode"));
		return comAreaDao.findByCriteria(dc);
	}

	@Override
	public Set<Integer> importComArea(ComModelSeries serie,
			ArrayList<ComArea> oneList, ArrayList<ComArea> twoList,
			ArrayList<ComArea> threeList, String curUserId) {
		Set<Integer> errorRowSet = new HashSet<Integer>();
		String errorRow = "0";
		try{
			//第一级
			for (int m = 0; m < oneList.size(); m++) {
				ComArea cAreaM = oneList.get(m);
				errorRow = cAreaM.getAreaId();//临时暂存了row号
				Set comAreaDetails = cAreaM.getComAreaDetails();
				DetachedCriteria dc=DetachedCriteria.forClass(ComArea.class);
				dc.add(Restrictions.eq("areaCode",cAreaM.getAreaCode()));
				dc.add(Restrictions.eq("comModelSeries", serie));
				dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
				List list = comAreaDao.findByCriteria(dc);
				if(list != null && list.size() > 0){//修改
					String tmpCode = cAreaM.getAreaCode();
					String tmpName = cAreaM.getAreaName();
					String tmpReachWay = cAreaM.getReachWay();
					String tmpWirePiping = cAreaM.getWirePiping();
					String tmpRemark = cAreaM.getRemark();
					cAreaM = (ComArea)list.get(0);
					cAreaM.setAreaCode(tmpCode);
					cAreaM.setAreaName(tmpName);
					cAreaM.setReachWay(tmpReachWay);
					cAreaM.setWirePiping(tmpWirePiping);
					cAreaM.setRemark(tmpRemark);
					cAreaM.setModifyDate(new Date());
					cAreaM.setModifyUser(curUserId);
				} else {//新增
					cAreaM.setAreaId(null);
					cAreaM.setCreateDate(new Date());
					cAreaM.setCreateUser(curUserId);
					cAreaM.setModifyDate(new Date());
					cAreaM.setModifyUser(curUserId);
					cAreaM.setComModelSeries(serie);
					cAreaM.setValidFlag(ComacConstants.VALIDFLAG_YES);
					cAreaM.setAreaLevel(1);
				}
				comAreaDao.saveOrUpdate(cAreaM);
				//保存设备
				if(comAreaDetails.size() > 0){
					Iterator<ComAreaDetail> iterator= comAreaDetails.iterator();
					DetachedCriteria dcDetail=DetachedCriteria.forClass(ComAreaDetail.class);
					dcDetail.add(Restrictions.eq("comArea",cAreaM));
					List<ComAreaDetail> listDetail = comAreaDao.findByCriteria(dcDetail);
					while(iterator.hasNext()){
						ComAreaDetail comAreaDetail = iterator.next();
						boolean flag = true;
						if(listDetail != null && listDetail.size() > 0){
							for(ComAreaDetail oldComAreaDetail : listDetail){
								if(oldComAreaDetail.equals(comAreaDetail)){
									flag = false;
									break;
								}
							}
						}
						if(flag){
							comAreaDetail.setComArea(cAreaM);
							comAreaDao.saveOrUpdate(comAreaDetail);
						}
					}
				}
			}
			//第二级
			for (int m = 0; m < twoList.size(); m++) {
				ComArea cAreaM = twoList.get(m);
				errorRow = cAreaM.getAreaId();//临时暂存了row号
				Set comAreaDetails = cAreaM.getComAreaDetails();
				DetachedCriteria dc=DetachedCriteria.forClass(ComArea.class);
				dc.add(Restrictions.eq("areaCode",cAreaM.getAreaCode()));
				dc.add(Restrictions.eq("comModelSeries", serie));
				dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
				List list = comAreaDao.findByCriteria(dc);
				if(list != null && list.size() > 0){//修改
					String tmpCode = cAreaM.getAreaCode();
					String tmpName = cAreaM.getAreaName();
					String tmpReachWay = cAreaM.getReachWay();
					String tmpWirePiping = cAreaM.getWirePiping();
					String tmpRemark = cAreaM.getRemark();
					cAreaM = (ComArea)list.get(0);
					cAreaM.setAreaCode(tmpCode);
					cAreaM.setAreaName(tmpName);
					cAreaM.setReachWay(tmpReachWay);
					cAreaM.setWirePiping(tmpWirePiping);
					cAreaM.setRemark(tmpRemark);
					cAreaM.setModifyDate(new Date());
					cAreaM.setModifyUser(curUserId);
				} else {//新增
					cAreaM.setAreaId(null);
					cAreaM.setCreateDate(new Date());
					cAreaM.setCreateUser(curUserId);
					cAreaM.setModifyDate(new Date());
					cAreaM.setModifyUser(curUserId);
					cAreaM.setComModelSeries(serie);
					cAreaM.setValidFlag(ComacConstants.VALIDFLAG_YES);
					cAreaM.setAreaLevel(2);
					//设置父对象
					DetachedCriteria dcParent=DetachedCriteria.forClass(ComArea.class);
					dcParent.add(Restrictions.eq("areaCode", cAreaM.getAreaCode().substring(0, 1) + "00"));
					dcParent.add(Restrictions.eq("comModelSeries", serie));
					dcParent.add(Restrictions.eq("validFlag", ComacConstants.YES));
					List listParent = comAreaDao.findByCriteria(dcParent);
					if(listParent !=null && listParent.size()>0){
						cAreaM.setComArea((ComArea)listParent.get(0));
					}else {
						//未找到父区域，终结导入,一般不会找不到，在前面已经校验过是否有父区域
						throw new Exception();
					}
				}
				comAreaDao.saveOrUpdate(cAreaM);
				//保存设备
				if(comAreaDetails.size() > 0){
					Iterator<ComAreaDetail> iterator= comAreaDetails.iterator();
					DetachedCriteria dcDetail=DetachedCriteria.forClass(ComAreaDetail.class);
					dcDetail.add(Restrictions.eq("comArea",cAreaM));
					List<ComAreaDetail> listDetail = comAreaDao.findByCriteria(dcDetail);
					while(iterator.hasNext()){
						ComAreaDetail comAreaDetail = iterator.next();
						boolean flag = true;
						if(listDetail != null && listDetail.size() > 0){
							for(ComAreaDetail oldComAreaDetail : listDetail){
								if(oldComAreaDetail.equals(comAreaDetail)){
									flag = false;
									break;
								}
							}
						}
						if(flag){
							comAreaDetail.setComArea(cAreaM);
							comAreaDao.saveOrUpdate(comAreaDetail);
						}
					}
				}
			}
			//第三级
			for (int m = 0; m < threeList.size(); m++) {
				ComArea cAreaM = threeList.get(m);
				errorRow = cAreaM.getAreaId();//临时暂存了row号
				Set comAreaDetails = cAreaM.getComAreaDetails();
				DetachedCriteria dc=DetachedCriteria.forClass(ComArea.class);
				dc.add(Restrictions.eq("areaCode",cAreaM.getAreaCode()));
				dc.add(Restrictions.eq("comModelSeries", serie));
				dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
				List list = comAreaDao.findByCriteria(dc);
				if(list != null && list.size() > 0){//修改
					String tmpCode = cAreaM.getAreaCode();
					String tmpName = cAreaM.getAreaName();
					String tmpReachWay = cAreaM.getReachWay();
					String tmpWirePiping = cAreaM.getWirePiping();
					String tmpRemark = cAreaM.getRemark();
					cAreaM = (ComArea)list.get(0);
					cAreaM.setAreaCode(tmpCode);
					cAreaM.setAreaName(tmpName);
					cAreaM.setReachWay(tmpReachWay);
					cAreaM.setWirePiping(tmpWirePiping);
					cAreaM.setRemark(tmpRemark);
					cAreaM.setModifyDate(new Date());
					cAreaM.setModifyUser(curUserId);
				} else {//新增
					cAreaM.setAreaId(null);
					cAreaM.setCreateDate(new Date());
					cAreaM.setCreateUser(curUserId);
					cAreaM.setModifyDate(new Date());
					cAreaM.setModifyUser(curUserId);
					cAreaM.setComModelSeries(serie);
					cAreaM.setValidFlag(ComacConstants.VALIDFLAG_YES);
					cAreaM.setAreaLevel(3);
					//设置父对象
					DetachedCriteria dcParent=DetachedCriteria.forClass(ComArea.class);
					dcParent.add(Restrictions.eq("areaCode", cAreaM.getAreaCode().substring(0, 2) + "0"));
					dcParent.add(Restrictions.eq("comModelSeries", serie));
					dcParent.add(Restrictions.eq("validFlag", ComacConstants.YES));
					List listParent = comAreaDao.findByCriteria(dcParent);
					if(listParent !=null && listParent.size()>0){
						cAreaM.setComArea((ComArea)listParent.get(0));
					}else {
						//未找到父区域，终结导入,一般不会找不到，在前面已经校验过是否有父区域
						throw new Exception();
					}
				}
				comAreaDao.saveOrUpdate(cAreaM);
				//保存设备
				if(comAreaDetails.size() > 0){
					Iterator<ComAreaDetail> iterator= comAreaDetails.iterator();
					DetachedCriteria dcDetail=DetachedCriteria.forClass(ComAreaDetail.class);
					dcDetail.add(Restrictions.eq("comArea",cAreaM));
					List<ComAreaDetail> listDetail = comAreaDao.findByCriteria(dcDetail);
					while(iterator.hasNext()){
						ComAreaDetail comAreaDetail = iterator.next();
						boolean flag = true;
						if(listDetail != null && listDetail.size() > 0){
							for(ComAreaDetail oldComAreaDetail : listDetail){
								if(oldComAreaDetail.equals(comAreaDetail)){
									flag = false;
									break;
								}
							}
						}
						if(flag){
							comAreaDetail.setComArea(cAreaM);
							comAreaDao.saveOrUpdate(comAreaDetail);
						}
					}
				}
			}
		}catch (Exception e) {
			//e.printStackTrace();
			errorRowSet.add(Integer.parseInt(errorRow));
		}
		return errorRowSet;
	}

	@Override
	public List<ComArea> getAreaListForLevel(ComModelSeries comModelSeries, int level){
		DetachedCriteria dc = DetachedCriteria.forClass(ComArea.class);
		dc.add(Restrictions.eq("comModelSeries.id", comModelSeries.getModelSeriesId()));
		dc.add(Restrictions.eq("areaLevel", level));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		dc.addOrder(Order.asc("areaCode"));
		return this.findByCritera(dc);
	}

	public IComAreaDao getComAreaDao() {
		return comAreaDao;
	}

	public void setComAreaDao(IComAreaDao comAreaDao) {
		this.comAreaDao = comAreaDao;
	}

	public ILhMainDao getLhMainDao() {
		return lhMainDao;
	}

	public void setLhMainDao(ILhMainDao lhMainDao) {
		this.lhMainDao = lhMainDao;
	}

	public ITaskMsgDao getTaskMsgDao() {
		return taskMsgDao;
	}

	public void setTaskMsgDao(ITaskMsgDao taskMsgDao) {
		this.taskMsgDao = taskMsgDao;
	}

	public IZa1Dao getZa1Dao() {
		return za1Dao;
	}

	public void setZa1Dao(IZa1Dao za1Dao) {
		this.za1Dao = za1Dao;
	}

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}

	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}

	public ILhSelectDao getLhSelectDao() {
		return lhSelectDao;
	}

	public void setLhSelectDao(ILhSelectDao lhSelectDao) {
		this.lhSelectDao = lhSelectDao;
	}

	public IComReportBo getComReportBo() {
		return comReportBo;
	}

	public void setComReportBo(IComReportBo comReportBo) {
		this.comReportBo = comReportBo;
	}
	
}
