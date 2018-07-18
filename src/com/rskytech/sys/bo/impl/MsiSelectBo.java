package com.rskytech.sys.bo.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.dao.IComAtaDao;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComReport;
import com.rskytech.pojo.MMain;
import com.rskytech.pojo.MSelect;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.report.bo.IComReportBo;
import com.rskytech.sys.bo.IMsiSelectBo;
import com.rskytech.sys.dao.IMsiMainDao;
import com.rskytech.sys.dao.IMsiSelectDao;
import com.rskytech.task.dao.ITaskMsgDao;

public class MsiSelectBo extends BaseBO implements IMsiSelectBo {
	private IMsiSelectDao msiSelectDao;
	private IComAtaDao comAtaDao;
	private IMsiMainDao msiMainDao;
	private ITaskMsgDao taskMsgDao;
	private IComReportBo comReportBo;
	
	/**
	 * 根据ATAID查询对应的MSI选择表中数据
	 * @param ataId
	 * @return MSelect
	 * @throws BusinessException
	 */
	@Override
	public List<MSelect> getMSelectByataId(String ataId,String comModelSeriesId)throws BusinessException {
			return this.msiSelectDao.getMSelectByataId(ataId,comModelSeriesId);
	}
	@Override
	public List<MSelect> getListMSelectByModelSeriesId(String modelSeriesId,String name,String isNoMsi)
			throws BusinessException {
		return this.msiSelectDao.getListMSelectByModelSeriesId(modelSeriesId, name, isNoMsi);
	}
	
	@Override
	public List<Object[]> getAtaAndfindAllChildByAtaId(String ataId,String modelSeriesId,String userId)
			throws BusinessException {
		return this.msiSelectDao.findAllAtaByAtaId(ataId, modelSeriesId, userId);
	}
	
	public ArrayList<String> savaMSelect(String jsonData,ComModelSeries comModelSeries,String userId) {

		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = new JSONObject();
		  // db操作区分
		String dbOperate = "";
		MSelect mSelect;
		MMain mMain=null;
		ArrayList<String> arr = new ArrayList<String>();
		for (int i = 0; i < jsonArray.size(); i++){
			mSelect=new MSelect();
			jsonObject = jsonArray.getJSONObject(i);
			String id = jsonObject.getString("selectId");
			if (!BasicTypeUtils.isNullorBlank(id)){
				//修改操作
				dbOperate = ComacConstants.DB_UPDATE;
				mSelect=(MSelect)this.loadById(MSelect.class, id);
			}else{
			     //添加操作
				 dbOperate = ComacConstants.DB_INSERT; 
				 mSelect.setComModelSeries(comModelSeries);
			}
			ComAta comAta = (ComAta)loadById(ComAta.class, jsonObject.getString("ataId"));
			mSelect.setComAta(comAta);
			// 对安全性影响回答时
			if (BasicTypeUtils.isNumberString(jsonObject.getString("safety"))){
			   mSelect.setSafety(jsonObject.getInt("safety"));
			}
			mSelect.setSafetyAnswer(jsonObject.getString("safetyAnswer"));
			// 对空勤人员是否容易发现回答时
		   if (BasicTypeUtils.isNumberString(jsonObject.getString("detectable"))){
			    mSelect.setDetectable(jsonObject.getInt("detectable"));
			}
			mSelect.setDetectableAnswer(jsonObject.getString("detectableAnswer"));
			// 任务影响回答时
			if (BasicTypeUtils.isNumberString(jsonObject.getString("task"))){
			   mSelect.setTask(jsonObject.getInt("task"));
			}
			mSelect.setTaskAnswer(jsonObject.getString("taskAnswer"));
			// 对经济性影响回答时
			if (BasicTypeUtils.isNumberString(jsonObject.getString("economic"))){
			    mSelect.setEconomic(jsonObject.getInt("economic"));
			}
			mSelect.setEconomicAnswer(jsonObject.getString("economicAnswer"));
			// 对是否是MSI做出选择时
			if (BasicTypeUtils.isNumberString(jsonObject.getString("isMsi"))){
			   mSelect.setIsMsi(jsonObject.getInt("isMsi"));
			}
			mSelect.setHighestLevel(jsonObject.getString("highestLevel"));
			mSelect.setRemark(jsonObject.getString("remark"));
			mSelect.setValidFlag(ComacConstants.YES);
			List<MMain> list  = msiMainDao.getMMainByAtaIdAndModelSeries(jsonObject.getString("ataId"),comModelSeries.getModelSeriesId());
			if(list!=null&&list.size()>0){
				mMain = list.get(0);
			}else{
				mMain = null;
			}
			if(mSelect.getIsMsi().equals(ComacConstants.YES)&&mSelect.getHighestLevel().equals(jsonObject.getString("proCode"))){//是msi且最高管理层为自己
				if (mMain == null){
					//在系统分析主表中没有生成数据
				    mMain=new MMain();
					mMain.setComModelSeries(comModelSeries);
				    mMain.setComAta(comAta);
				    mMain.setStatus(ComacConstants.ANALYZE_STATUS_NEW);
				    mMain.setValidFlag(ComacConstants.YES);
				    mMain.setEffectiveness(comModelSeries.getModelSeriesName());
				    this.saveOrUpdate(mMain, ComacConstants.DB_INSERT , userId);
				}/*else{
					mMain.setValidFlag(ComacConstants.YES);
					update(mMain,userId);
				}*/
			}else{// 不是MSi或最高管理层不为自己
				if(mMain != null){
					// 在系统分析主表中有数据时
						MMain mMain1=(MMain)loadById(MMain.class, mMain.getMsiId());
						List<TaskMsg> taskList = this.taskMsgDao.getTaskMsgListByMainId(comModelSeries.getModelSeriesId(),
														mMain.getMsiId(), ComacConstants.SYSTEM_CODE, null);
						for(TaskMsg task : taskList){
							if(task.getNeedTransfer()!=null&&task.getNeedTransfer()==1){//更新Za7的状态
								arr.add(task.getOwnArea());
							}
							taskMsgDao.deleteTasksByTaskId(task.getTaskId());
						}
						//删除产生的报告数据
						List<ComReport> listReport = this.comReportBo.
							loadAllReportListNoPage(comModelSeries.getModelSeriesId(), ComacConstants.SYSTEM_CODE, mMain1.getMsiId());
						if(listReport!=null&&listReport.size()>0){
							for(ComReport cr : listReport){
								this.delete(cr);
							}
						}
						this.delete(mMain1);//通过级联删除删除Main表产生的数据
				}
			}
			this.saveOrUpdate(mSelect, dbOperate, userId);
//			if(mSelect.getHighestLevel().equals(jsonObject.getString("proCode"))){//如果该条ata是重要msi分析，且最高可管理层是自己，则变更该条所有下级ata的最高可管理层为它
//				List<Object> li=this.comAtaDao.getSelfAndChildAta(comModelSeries.getModelSeriesId(),jsonObject.getString("ataId"));
//				for (int j = 1; j < li.size(); j++) {
//					String ata=li.get(j).toString();
//					List<MSelect> msli=this.msiSelectDao.getMSelectByataId(ata, comModelSeries.getModelSeriesId());
//					if(msli!=null&&msli.size()>0){
//						MSelect ms=msli.get(0);
//						if(ms.getHighestLevel()!=null&&!ms.getHighestLevel().equals("")){
//							ms.setHighestLevel(jsonObject.getString("highestLevel"));
//							List<MMain> mMains=this.msiMainDao.getMMainByAtaIdAndModelSeries(ms.getComAta().getAtaId(), comModelSeries.getModelSeriesId());
//							MMain mMain1=null;
//							if(null!=mMains&&mMains.size()>0){
//								mMain1=mMains.get(0);
//							}
//							if(mMain1 != null){
//								// 在系统分析主表中有数据时
//									List<TaskMsg> taskList = this.taskMsgDao.getTaskMsgListByMainId(comModelSeries.getModelSeriesId(),
//											mMain1.getMsiId(), ComacConstants.SYSTEM_CODE, null);
//									for(TaskMsg task : taskList){
//										if(task.getNeedTransfer()!=null&&task.getNeedTransfer()==1){//更新Za7的状态
//											arr.add(task.getOwnArea());
//										}
//										taskMsgDao.deleteTasksByTaskId(task.getTaskId());
//									}
//									//删除产生的报告数据
//									List<ComReport> listReport = this.comReportBo.
//										loadAllReportListNoPage(comModelSeries.getModelSeriesId(), ComacConstants.SYSTEM_CODE, mMain1.getMsiId());
//									if(listReport!=null&&listReport.size()>0){
//										for(ComReport cr : listReport){
//											this.delete(cr);
//										}
//									}
//									this.delete(mMain1);//通过级联删除删除Main表产生的数据
//							}
//							this.saveOrUpdate(ms, dbOperate, userId);
//						}
//					}
//				}
//			}else if(mSelect.getHighestLevel()==null||mSelect.getHighestLevel().equals("")){
//				List<Object[]> current=this.msiSelectDao.getHighLevelByAta(mSelect.getComAta().getAtaId(), comModelSeries.getModelSeriesId());//检验数据库里原来的数据是不是符合最高可管理层规则
//				if(null!=current&&current.size()>0){
//					List<Object> li=this.comAtaDao.getSelfAndChildAta(comModelSeries.getModelSeriesId(),jsonObject.getString("ataId"));
//					for (int j = 1; j < li.size(); j++) {
//						String ata=li.get(j).toString();
//						List<MSelect> msli=this.msiSelectDao.getMSelectByataId(ata, comModelSeries.getModelSeriesId());
//						if(msli!=null&&msli.size()>0){
//							MSelect ms=msli.get(0);
//							ms.setHighestLevel(null);
//							this.saveOrUpdate(ms, dbOperate, userId);
//						}
//					}
//				}
//			}
		}
		return arr;
	}
	
	@Override
	public ArrayList<String> checkoutHighLevel(String jsonData, String comModelSeriesId) {
		ArrayList<String> errProCodes=new ArrayList<String>();//用来存放最高可管理层不符合规则的项目编号
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = new JSONObject();
		
		for (int i = 0; i < jsonArray.size(); i++){
			boolean flagGrid=false;//根据grid检测是否符合最高可管理层规则
			jsonObject = jsonArray.getJSONObject(i);
			String highestLevel=jsonObject.getString("highestLevel");
			String proCode=jsonObject.getString("proCode");
			
			if (highestLevel == null || "".equals(highestLevel)){
				flagGrid = true;
			} else {
				List<ComAta> ataList = comAtaDao.getComAtaByAtaCode(highestLevel, comModelSeriesId);
				if (ataList != null && ataList.size() > 0){
					flagGrid = true;
				}
			}
			
			if(flagGrid){//grid验证通过
				continue;
			} else {
				errProCodes.add(proCode);
			}
		}
		return errProCodes;
	}	
	
	public ArrayList<String> checkoutHighLevelOld(String jsonData,
			String comModelSeriesId) {
		ArrayList<String> errProCodes=new ArrayList<String>();//用来存放最高可管理层不符合规则的项目编号
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = new JSONObject();
		List<JSONObject> Level2Li=new ArrayList<JSONObject>();//grid修改过的ata为2级的数据行
		List<JSONObject> Level3Li=new ArrayList<JSONObject>();//grid修改过的ata为3级的数据行
		List<JSONObject> Level4Li=new ArrayList<JSONObject>();//grid修改过的ata为4级的数据行
		for (int i = 0; i < jsonArray.size(); i++){
			jsonObject = jsonArray.getJSONObject(i);
//			String highestLevel=jsonObject.getString("highestLevel");
			int isMsi=jsonObject.getInt("isMsi");
			int ataLevel=jsonObject.getInt("ataLevel");
//			if(highestLevel.equals("")){//最高可管理层为空
//				continue;
//			}
			if(isMsi==1){
				if(ataLevel==2){
					Level2Li.add(jsonObject);
				}else if(ataLevel==3){
					Level3Li.add(jsonObject);
				}else{
					Level4Li.add(jsonObject);
				}
			}
		}
		for (int i = 0; i < jsonArray.size(); i++){
			boolean flagData=true;//根据数据库检测是否符合最高可管理层规则
			boolean flagGrid=false;//根据grid检测是否符合最高可管理层规则
			jsonObject = jsonArray.getJSONObject(i);
			String ataId=jsonObject.getString("ataId");
			String highestLevel=jsonObject.getString("highestLevel");
			int isMsi=jsonObject.getInt("isMsi");
			String proCode=jsonObject.getString("proCode");
			int ataLevel=jsonObject.getInt("ataLevel");
			if(isMsi!=1){//最高可管理层为空
				continue;
			}
			
			/*
			 * 以下代码为grid验证
			 */
			if(ataLevel==2&&Level2Li.size()>0){
				if(highestLevel==null||highestLevel.equals("")){
					flagGrid=true;
				}
				if(proCode.equals(highestLevel)){
					flagGrid=true;
				}
			}
			if(ataLevel==3){
				if(highestLevel==null||highestLevel.equals("")){
					flagGrid=true;
				}
				if(Level2Li.size()>0){
					if (Level2Li.get(0).getString("highestLevel")==null||Level2Li.get(0).getString("highestLevel").equals("")) {
						if(proCode.equals(highestLevel)){
							flagGrid=true;
						} 
					}
					if (Level2Li.get(0).getString("proCode").equals(Level2Li.get(0).getString("highestLevel"))) {
						if(Level2Li.get(0).getString("proCode").equals(highestLevel)){
							flagGrid=true;
						}
					}
					
				}
			}
			if(ataLevel==4){
				ComAta comAta=this.msiSelectDao.getSupAtaByAta(ataId, comModelSeriesId);
				JSONObject tempJb = null;
				for (JSONObject jo : Level3Li) {
					if(comAta.getAtaId().equals(jo.getString("ataId"))){
						tempJb=jo;
						break;
					} 
				}
				if(highestLevel==null||highestLevel.equals("")){
					flagGrid=true;
				}
				if(Level2Li.size()>0){
					if (Level2Li.get(0).getString("highestLevel")==null||Level2Li.get(0).getString("highestLevel").equals("")) {
						if(tempJb!=null){
							if (tempJb.getString("highestLevel")==null||tempJb.getString("highestLevel").equals("")) {
								if(proCode.equals(highestLevel)){
									flagGrid=true;
								} 
							}
							if (tempJb.getString("proCode").equals(tempJb.getString("highestLevel"))) {
								if(tempJb.getString("proCode").equals(highestLevel)){
									flagGrid=true;
								}
							}
						}
					}
					if(Level2Li.get(0).getString("proCode").equals(Level2Li.get(0).getString("highestLevel"))){
						if(Level2Li.get(0).getString("proCode").equals(highestLevel)){
							flagGrid=true;
						}
					}
					
				}else{
					if(tempJb!=null){
						if (tempJb.getString("highestLevel")==null||tempJb.getString("highestLevel").equals("")) {
							if(proCode.equals(highestLevel)){
								flagGrid=true;
							} 
						}
						if (tempJb.getString("proCode").equals(tempJb.getString("highestLevel"))) {
							if(tempJb.getString("proCode").equals(highestLevel)){
								flagGrid=true;
							}
						}
					}
				}
			}
			
			if(flagGrid){//grid验证通过
				continue;
			}
			
			
			/*
			 * 以下代码为进数据库验证
			 */
			ComAta comAta=this.msiSelectDao.getSupAtaByAta(ataId, comModelSeriesId);//得到上级ATA
			ComAta comAta2=null;
			if(null!=comAta&&comAta.getAtaLevel()==1){//上级ATA为一级，则最高可管理层须为自己
				if(!proCode.equals(highestLevel)){
					flagData=false;
				}
			} 
			if(null!=comAta&&comAta.getAtaLevel()==2){//上级ATA为二级
				List<Object[]> li=this.msiSelectDao.getHighLevelByAta(comAta.getAtaId(), comModelSeriesId);
				if(null!=li&&li.size()>0){
					if(!comAta.getAtaCode().equals(highestLevel)){
						flagData=false;
					}
				}else{//最高可管理层须为自己
					if(!proCode.equals(highestLevel)){
						flagData=false;
					} 
				}
			} 
			if(null!=comAta&&comAta.getAtaLevel()==3){//上级ATA为三级
				comAta2=this.msiSelectDao.getSupAtaByAta(comAta.getAtaId(), comModelSeriesId);//得到2级ata
				List<Object[]> li2Level=null;
				if(null!=comAta2){
					li2Level=this.msiSelectDao.getHighLevelByAta(comAta2.getAtaId(), comModelSeriesId);
				}
				List<Object[]> li3Level=this.msiSelectDao.getHighLevelByAta(comAta.getAtaId(), comModelSeriesId);;
				if(null!=li2Level&&li2Level.size()>0){
					if (!comAta2.getAtaCode().equals(highestLevel)) {
						flagData=false;
					}
					
				}else if(null!=li3Level&&li3Level.size()>0){
					if (!comAta.getAtaCode().equals(highestLevel)) {
						flagData=false;
					}
					
				}else{//最高可管理层须为自己
					if(!proCode.equals(highestLevel)){
						flagData=false;
					}
				}
			} 
			if(!flagData){//添加不符合最高可管理层规则的项目编号
				errProCodes.add(proCode);
			}
		}
		return errProCodes;
	}
	
	public IMsiSelectDao getMsiSelectDao() {
		return msiSelectDao;
	}

	public void setMsiSelectDao(IMsiSelectDao msiSelectDao) {
		this.msiSelectDao = msiSelectDao;
	}
	public IComAtaDao getComAtaDao() {
		return comAtaDao;
	}
	public void setComAtaDao(IComAtaDao comAtaDao) {
		this.comAtaDao = comAtaDao;
	}
	public ITaskMsgDao getTaskMsgDao() {
		return taskMsgDao;
	}
	public void setTaskMsgDao(ITaskMsgDao taskMsgDao) {
		this.taskMsgDao = taskMsgDao;
	}
	public IMsiMainDao getMsiMainDao() {
		return msiMainDao;
	}
	public void setMsiMainDao(IMsiMainDao msiMainDao) {
		this.msiMainDao = msiMainDao;
	}
	public IComReportBo getComReportBo() {
		return comReportBo;
	}
	public void setComReportBo(IComReportBo comReportBo) {
		this.comReportBo = comReportBo;
	}




}
