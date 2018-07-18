package com.rskytech.struct.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAtaBo;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComReport;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.S2;
import com.rskytech.pojo.S3;
import com.rskytech.pojo.S4;
import com.rskytech.pojo.S5;
import com.rskytech.pojo.S6;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.SRemark;
import com.rskytech.pojo.SStep;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.report.bo.IComReportBo;
import com.rskytech.struct.bo.ISsiSelectBo;
import com.rskytech.struct.dao.ISsiMainDao;
import com.rskytech.struct.dao.ISsiSelectDao;
import com.rskytech.struct.dao.IUnSsiDao;
import com.rskytech.task.dao.ITaskMsgDao;

@SuppressWarnings("unchecked")
public class SsiSelectBo extends BaseBO  implements ISsiSelectBo{
	private ISsiSelectDao ssiSelectDao;
	private ISsiMainDao ssiMainDao;
	private ITaskMsgDao taskMsgDao;
	private IUnSsiDao unSsiDao;
	private IComReportBo comReportBo;
	private IComAtaBo comAtaBo;

	@Override
	public List<SMain> getSMainByAtaId(String ataId) throws BusinessException {
		return this.ssiSelectDao.getSMainByAtaId(ataId);
	}
	@Override
	public List<Object[]> getSsiListByAtaId(String ataId, String modelSeriesId,
			String userId) {
		return this.ssiSelectDao.getSsiList(ataId, modelSeriesId, userId);
	}
	
	@Override
	public boolean searchAnalysisProByAtaId(String ataId, String modelSeriesId,String userId) {
		List<Object> list = this.ssiSelectDao.searchAnalysisProByAtaId(ataId, modelSeriesId, userId);
		boolean flag = false;
		if(list!=null){
			for(Object obj : list){
				if(obj.toString().equals("1")){
					flag = true;
				}
			}
		}
		return flag;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean verifySsiCode(String SsiCode, String modelSerierId) throws BusinessException {
		List list = ssiSelectDao.SearchSsiListAndAtaListByAtaCode(SsiCode, modelSerierId);
		if(list!=null&&list.size()>0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public ArrayList<String> saveSsi(String jsonData, String ataId, String userId,ComModelSeries comModelSeries) {
		SMain sMain = null;
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = null;
		String dbOperate = null;
		Integer isSsi = null;
		Integer isAna = null;
		ArrayList<String> arr = new ArrayList<String>();
		Object[] obj = null;
		for(int i=0;i<jsonArray.size();i++){
			jsonObject = jsonArray.getJSONObject(i);
			if (BasicTypeUtils.isNumberString(jsonObject.getString("isSsi"))) {
				isSsi=jsonObject.getInt("isSsi");
			}
			if (jsonObject.get("isAna")!=null) {
				if(!jsonObject.getString("isAna").equals("")&&!"null".equals(jsonObject.getString("isAna"))){
					isAna=Integer.parseInt(jsonObject.getString("isAna"));
				}
			}
			if (jsonObject.get("id") != null&&!"".equals(jsonObject.get("id").toString())
					&& !"null".equals(jsonObject.get("id").toString())) {
				String ssiId = jsonObject.get("id").toString();
				sMain=(SMain)this.loadById(SMain.class,ssiId);
				dbOperate = ComacConstants.DB_UPDATE;
				if(isSsi!=null&&sMain.getIsSsi()!=null&&!sMain.getIsSsi().equals(isSsi)){
					arr.addAll(this.deleteSsiAnalysis(sMain,userId,comModelSeries.getModelSeriesId()));
					obj = sMain.getS1s().toArray();
					for(Object o:obj){
						this.delete((S1)o);
					}
					obj = sMain.getS1s().toArray();
					for(Object o:obj){
						this.delete((S1)o);
					}
					obj = sMain.getS2s().toArray();
					for(Object o:obj){
						this.delete((S2)o);
					}
					obj = sMain.getS3s().toArray();
					for(Object o:obj){
						this.delete((S3)o);
					}
					obj = sMain.getS4s().toArray();
					for(Object o:obj){
						this.delete((S4)o);
					}
					obj = sMain.getS5s().toArray();
					for(Object o:obj){
						this.delete((S5)o);
					}
					obj = sMain.getS6s().toArray();
					for(Object o:obj){
						this.delete((S6)o);
					}
					obj = sMain.getSRemarks().toArray();
					for(Object o:obj){
						this.delete((SRemark)o);
					}
					sMain.setStatus(ComacConstants.ANALYZE_STATUS_NEW);
					//删除产生的报告数据
					List<ComReport> listReport = this.comReportBo.
							loadAllReportListNoPage(comModelSeries.getModelSeriesId(), ComacConstants.STRUCTURE_CODE, sMain.getSsiId());
					if(listReport!=null&&listReport.size()>0){
						for(ComReport cr : listReport){
							this.delete(cr);
						}
					}
				}else if(isAna!=null&&sMain.getIsAna()!=null&&!sMain.getIsAna().equals(isAna)){
					List<TaskMsg> listTask = this.unSsiDao.searchUnSsiList(ssiId, comModelSeries.getModelSeriesId());
					if(listTask!=null&&listTask.size()>0){
						for(TaskMsg task : listTask){
							this.delete(task);
						}
					}
					//删除产生的报告数据
					List<ComReport> listReport = this.comReportBo.
							loadAllReportListNoPage(comModelSeries.getModelSeriesId(), ComacConstants.STRUCTURE_CODE, sMain.getSsiId());
					if(listReport!=null&&listReport.size()>0){
						for(ComReport cr : listReport){
							this.delete(cr);
						}
					}
				}
				if(sMain.getIsAdd()!=null&&sMain.getIsAdd().equals(1)){
					sMain.setAddCode(jsonObject.getString("ataCode").trim());
					sMain.setAddName(jsonObject.getString("ataName").trim());
				}
				if(sMain.getIsAna()==null||(isAna!=null&&!sMain.getIsAna().equals(isAna))){
					this.ssiSelectDao.deleteUnSsiAnalysis(ssiId,comModelSeries.getModelSeriesId());//删除录入的非ssi数据
					//删除产生的报告数据
					List<ComReport> listReport = this.comReportBo.
							loadAllReportListNoPage(comModelSeries.getModelSeriesId(), ComacConstants.STRUCTURE_CODE, sMain.getSsiId());
					if(listReport!=null&&listReport.size()>0){
						for(ComReport cr : listReport){
							this.delete(cr);
						}
					}
				}
			}else{
				dbOperate=ComacConstants.DB_INSERT;
				sMain = new SMain();
				Integer isAdd = null;
				if (BasicTypeUtils.isNumberString(jsonObject.getString("isAdd"))) {
					isAdd=jsonObject.getInt("isAdd");
				}
				if (BasicTypeUtils.isNumberString(jsonObject.getString("isSsi"))) {
					sMain.setIsSsi(jsonObject.getInt("isSsi"));
				}
				sMain.setStatus(ComacConstants.ANALYZE_STATUS_NEW);
				sMain.setValidFlag(ComacConstants.VALIDFLAG_YES);
				sMain.setEffectiveness(comModelSeries.getModelSeriesName());
				if(isAdd!=null&&isAdd.equals(1)){
					sMain.setIsAdd(isAdd);
					sMain.setAddCode(jsonObject.getString("ataCode").trim());
					sMain.setAddName(jsonObject.getString("ataName").trim());
					sMain.setAddUser(userId);
					sMain.setParentAtaId(ataId);
				}else{
					ComAta comAta = comAtaBo.getComAtaByAtaCode(jsonObject.getString("ataCode").trim(), comModelSeries.getModelSeriesId());
					sMain.setComAta(comAta);
					sMain.setParentAtaId(comAta.getComAta().getAtaId());
				}
			}
			sMain.setIsAna(isAna);
			sMain.setIsSsi(isSsi);
			sMain.setComModelSeries(comModelSeries);
			this.saveOrUpdate(sMain, dbOperate, userId);
		}
		return arr;
	}
	
	
	@Override
	public ArrayList<String> delRecord(String ssiId, String userId, String modelSeriesId) {
		SMain sMain=(SMain)loadById(SMain.class, ssiId);
		ArrayList<String> arr = new ArrayList<String>();
		arr.addAll(deleteSsiAnalysis(sMain,userId,modelSeriesId));
		List<ComReport> listReport = this.comReportBo.
				loadAllReportListNoPage(modelSeriesId, ComacConstants.STRUCTURE_CODE, sMain.getSsiId());
		if(listReport!=null&&listReport.size()>0){
			for(ComReport cr : listReport){
				this.delete(cr);
			}
		}
		delete(sMain,userId);
		return arr;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public JSONObject getSsiRecords(Integer isSsi, Integer isOwn, String ssiName,
			String model,int start,int limit) throws BusinessException {
		int totalCount = 0;
		List<Object[]> list = this.ssiSelectDao.getSsiRecords(isSsi, isOwn, ssiName, model, start, limit);
		List<Object> total = this.ssiSelectDao.getSsiTotalRecords(isSsi, isOwn, ssiName, model);
		if(total!=null){
			for(Object obj : total){
				totalCount = Integer.parseInt(obj.toString());
			}
		}
		HashMap jsonFieldList = null;
		List<HashMap> jsonList = new ArrayList();
		JSONObject json = new JSONObject();
		if (list != null) {
			for (Object[] obj : list) {
				jsonFieldList=new HashMap();
				jsonFieldList.put("ssiId", obj[0]);// ssiId
				jsonFieldList.put("model", ((ComModelSeries)this.loadById(ComModelSeries.class, obj[3].toString())).getModelSeriesName());
				if(Integer.parseInt(obj[4].toString())==1){
					jsonFieldList.put("ssiCode", obj[1]);//ssi编号
					jsonFieldList.put("isAna","1");//是否需要分析
				}else if(Integer.parseInt(obj[4].toString())==0){
					jsonFieldList.put("ssiCode", "Q"+obj[1]);//ssi编号
					jsonFieldList.put("isAna",obj[5]+"");
				}
				jsonFieldList.put("ssiName",obj[2]);//中文名称
				jsonFieldList.put("isOwn", obj[6]+"");// 是否自增
				jsonList.add(jsonFieldList);
			}
		}
		json.element("ssi", jsonList);
		json.element("totleCount",totalCount);
		return json;
	}
	
	
	/**
	 * 根据SsiId删除ssi后续分析内容
	 * @param ssiId
	 */
	private ArrayList<String> deleteSsiAnalysis(SMain sMain,String userId,String modelSeriesId){
		//把分析对象走SSI变成非SSI
		String ssiId = sMain.getSsiId();
		List<SStep> stepList=this.ssiMainDao.getSStepListBySsiId(ssiId);
		ArrayList<String> arr = new ArrayList<String>();
		if(stepList!=null&&!stepList.isEmpty()){
			for(SStep step:stepList){
				delete(step,userId);//删除所有操作的步骤
			}
		}
		//删除产生的临时任务和最终任务
		List<TaskMsg> list = taskMsgDao.getTaskMsgListByMainId(modelSeriesId, ssiId, ComacConstants.STRUCTURE_CODE, null);
		for(TaskMsg task : list){
			if(task.getNeedTransfer()!=null&&task.getNeedTransfer()==1){
				arr.add(task.getOwnArea());
			}
			taskMsgDao.deleteTasksByTaskId(task.getTaskId());
		}
		return arr;
		
	}
	public ISsiSelectDao getSsiSelectDao() {
		return ssiSelectDao;
	}
	public void setSsiSelectDao(ISsiSelectDao ssiSelectDao) {
		this.ssiSelectDao = ssiSelectDao;
	}
	public ISsiMainDao getSsiMainDao() {
		return ssiMainDao;
	}
	public void setSsiMainDao(ISsiMainDao ssiMainDao) {
		this.ssiMainDao = ssiMainDao;
	}
	public ITaskMsgDao getTaskMsgDao() {
		return taskMsgDao;
	}
	public void setTaskMsgDao(ITaskMsgDao taskMsgDao) {
		this.taskMsgDao = taskMsgDao;
	}
	public IUnSsiDao getUnSsiDao() {
		return unSsiDao;
	}
	public void setUnSsiDao(IUnSsiDao unSsiDao) {
		this.unSsiDao = unSsiDao;
	}
	public IComReportBo getComReportBo() {
		return comReportBo;
	}
	public void setComReportBo(IComReportBo comReportBo) {
		this.comReportBo = comReportBo;
	}
	public IComAtaBo getComAtaBo() {
		return comAtaBo;
	}
	public void setComAtaBo(IComAtaBo comAtaBo) {
		this.comAtaBo = comAtaBo;
	}
}
