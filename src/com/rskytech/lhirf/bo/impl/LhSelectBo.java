package com.rskytech.lhirf.bo.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.lhirf.bo.ILhSelectBo;
import com.rskytech.lhirf.bo.ILhMainBo;
import com.rskytech.lhirf.dao.ILhSelectDao;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComReport;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.LhMain;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.report.bo.IComReportBo;
import com.rskytech.task.bo.ITaskMsgBo;
import com.rskytech.task.dao.ITaskMsgDao;


public class LhSelectBo extends BaseBO implements ILhSelectBo {
	private ILhMainBo lhMainBo;
	private ITaskMsgBo taskMsgBo;
	private ILhSelectDao lhSelectDao;
	private ITaskMsgDao taskMsgDao;
	private IComReportBo comReportBo;
	
	/**
	 * 删除LhMain时 同时删除其产生的任务和改lhMain产生的数据
	 * @param  表lh_HSI 
	 * @return 
	 * @throws BusinessException
	 */
	@Override
	public ArrayList<String> deleteHsi(LhMain lhMain, ComUser user,String modelSeriesId) throws BusinessException {
		List<TaskMsg> taskMsgList = null;
		ArrayList<String> arr = new ArrayList<String>();
		if (lhMain != null) {
			taskMsgList = taskMsgBo.getTaskMsgListByMainId(modelSeriesId,lhMain.getHsiId(),
					ComacConstants.LHIRF_CODE, ComacConstants.LHIRF_LH5);
			List<ComReport> listReport = this.comReportBo.
					loadAllReportListNoPage(modelSeriesId, ComacConstants.LHIRF_CODE, lhMain.getHsiId());
			if(listReport!=null&&listReport.size()>0){
				for(ComReport cr : listReport){
					this.delete(cr);
				}
			}
		}
		//删除该Hsi产生的任务
		if (taskMsgList.size() > 0) {
			for (TaskMsg doDeltaskMsg : taskMsgList) {
				if(doDeltaskMsg.getNeedTransfer()!=null&&doDeltaskMsg.getNeedTransfer()==1){
					arr.add(doDeltaskMsg.getOwnArea());
				}
				taskMsgDao.deleteTasksByTaskId(doDeltaskMsg.getTaskId());//
			}
		}
		this.lhSelectDao.deleteLhMain(lhMain);//用级联删除分析的数据
		return arr;
	}
	
	/**
	 * 页面认领HSI, 需替换的MSG-3的任务表 创建人 MRB 中的创建人
	 *  @param  表lh_HSI Id
	 *  @param  表ComUser user 
	 * @return 
	 */
	@Override
	public void doReplaceHsi(String replaceHsiId, ComUser user, ComModelSeries comModelSeries)
			throws BusinessException {
		LhMain reHsi = null;
		if (!BasicTypeUtils.isNullorBlank(replaceHsiId)) {
			reHsi = (LhMain) this.loadById(LhMain.class, replaceHsiId);
		}
		List<TaskMsg> taskMsgList = null;
		if (reHsi != null) {
			taskMsgList = taskMsgBo.getTaskMsgListByMainId(comModelSeries.getModelSeriesId(), replaceHsiId,
					ComacConstants.LHIRF_CODE, ComacConstants.LHIRF_LH5);
			if (taskMsgList.size() > 0) {
				for (TaskMsg doReplaceTaskMsg : taskMsgList) {
					if (!BasicTypeUtils.isNullorBlank(user.getUserId())) {
						doReplaceTaskMsg.setCreateUser(user.getUserId());
					}
					this.update(doReplaceTaskMsg, user.getUserId());
				}
			}

		}
		reHsi.setAnaUser(user.getUserId());
		this.update(reHsi, user.getUserId());
	}

	@Override
	public boolean saveOrUpdateLhHsi(ComUser user,String areaId, String jsonData, ComModelSeries comModelSeries) {
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = null;
		String dbOperate = "";
		LhMain lhMain = null;
		try {
			for (int i = 0; i < jsonArray.size(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				String hsiId = jsonObject.getString("hsiId");
				if (!BasicTypeUtils.isNullorBlank(hsiId) && !"0".equals(hsiId)) { //添加
					dbOperate = ComacConstants.DB_UPDATE;
					lhMain = (LhMain) lhMainBo.loadById(LhMain.class, hsiId);
				} else { // 追加操作
					lhMain = new LhMain();
					dbOperate = ComacConstants.DB_INSERT;
					lhMain.setStatus(ComacConstants.ANALYZE_STATUS_NEW);
					lhMain.setAnaUser(user.getUserId());
					lhMain.setAtaCode(jsonObject.getString("ataCode"));
				}
				lhMain.setHsiCode(jsonObject.getString("hsiCode"));
				lhMain.setHsiName(jsonObject.getString("hsiName"));
				lhMain.setIpvOpvpOpve(jsonObject.getString("ipvOpvpOpve"));
				lhMain.setLhCompName(jsonObject.getString("lhCompName"));
				lhMain.setRefHsiCode(jsonObject.getString("refHsiCode"));
				lhMain.setComModelSeries(comModelSeries);
				lhMain.setComArea((ComArea) this.loadById(ComArea.class, areaId));
				lhMain.setValidFlag(ComacConstants.YES);
				this.saveOrUpdate(lhMain, dbOperate, user.getUserId());
			}
			return  true;
		} catch (Exception e) {
			e.printStackTrace();
			return  false;
		}
		
	}


	/**
	 * 检查hsi编号是否存在
	 * @param hsiCode
	 */
	public boolean verifyHsiCodeExist(String hsiCode, String modelSeriesId){
		List<LhMain> list = this.lhSelectDao.searchLhMainByHsiCode(hsiCode,modelSeriesId);
		if(list!=null&&list.size()>0){
			return true;
		}else{
			return false;
		}
	}

	
	public ILhMainBo getLhMainBo() {
		return lhMainBo;
	}

	public void setLhMainBo(ILhMainBo lhMainBo) {
		this.lhMainBo = lhMainBo;
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

	public ITaskMsgDao getTaskMsgDao() {
		return taskMsgDao;
	}

	public void setTaskMsgDao(ITaskMsgDao taskMsgDao) {
		this.taskMsgDao = taskMsgDao;
	}

	public IComReportBo getComReportBo() {
		return comReportBo;
	}

	public void setComReportBo(IComReportBo comReportBo) {
		this.comReportBo = comReportBo;
	}
	
}
