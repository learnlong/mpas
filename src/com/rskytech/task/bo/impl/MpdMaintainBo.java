package com.rskytech.task.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.bo.impl.BaseBO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.TaskMpd;
import com.rskytech.task.bo.IMpdMaintainBo;
import com.rskytech.task.dao.IMpdMaintainDao;

public class MpdMaintainBo extends BaseBO implements IMpdMaintainBo {

	private IMpdMaintainDao mpdMaintainDao;
	private IComAreaBo comAreaBo;
	
	@SuppressWarnings("unchecked")
	public List<HashMap> getTaskMpdList(String msId, String sourceSystem, String taskType, String mpdCode, Page page){
		List<TaskMpd> list = mpdMaintainDao.getTaskMpdList(msId, sourceSystem, taskType, mpdCode, page);
		if (list != null){
			List<HashMap> listJsonFV = new ArrayList();
			
			for (TaskMpd tm : list) {
				HashMap hm = new HashMap();
				
				hm.put("mpdId", tm.getMpdId());
				hm.put("mpdCode", tm.getMpdCode());
				hm.put("sourceSystem", tm.getSourceSystem());
				hm.put("taskType", tm.getTaskType());
				hm.put("taskDesc", tm.getTaskDesc());
				hm.put("reachWay", tm.getReachWay());
				hm.put("taskIntervalOriginal", tm.getTaskIntervalOriginal());
				hm.put("ownArea", comAreaBo.getAreaCodeByAreaId(tm.getOwnArea()));
				hm.put("effectiveness", tm.getEffectiveness());
				hm.put("amm", tm.getAmm());
				hm.put("workTime", tm.getWorkTime());
				
				listJsonFV.add(hm);
			}
			return listJsonFV;
		}
		return null;
	}
	
	public boolean checkTaskMpdCode(String mpdId, String mpdCode,String modelId) {
		List<TaskMpd> list = mpdMaintainDao.getTaskMpdList(mpdId, mpdCode,modelId);
		return list.size() > 0 ? false : true;// size大于0表示已经存在相同的code
	}
	
	public void saveMpd(String userId, ComModelSeries ms, String jsonData){
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String id = jsonObject.getString("mpdId");
			
			String doNow;
			TaskMpd tm;
			if (id == null || "".equals(id)){
				tm = new TaskMpd();
				tm.setComModelSeries(ms);
				doNow = ComacConstants.DB_INSERT;
			} else {
				tm = (TaskMpd) this.loadById(TaskMpd.class, id);
				doNow = ComacConstants.DB_UPDATE;
			}
			
			tm.setSourceSystem(jsonObject.getString("sourceSystem"));
			tm.setMpdCode(jsonObject.getString("mpdCode"));
			tm.setTaskType(jsonObject.getString("taskType"));
			tm.setTaskDesc(jsonObject.getString("taskDesc"));
			tm.setReachWay(jsonObject.getString("reachWay"));
			tm.setTaskIntervalOriginal(jsonObject.getString("taskIntervalOriginal"));
			tm.setOwnArea(comAreaBo.getAreaIdByAreaCode(jsonObject.getString("ownArea"), ms.getModelSeriesId()));
			tm.setEffectiveness(jsonObject.getString("effectiveness"));
			tm.setAmm(jsonObject.getString("amm"));
			
			if (jsonObject.getString("workTime") != "null" && !"".equals(jsonObject.getString("workTime"))) {
				tm.setWorkTime(jsonObject.getDouble("workTime"));
			}
			
			tm.setValidFlag(1);
			this.saveOrUpdate(tm, doNow, userId);			
		}	
	}

	public IMpdMaintainDao getMpdMaintainDao() {
		return mpdMaintainDao;
	}

	public void setMpdMaintainDao(IMpdMaintainDao mpdMaintainDao) {
		this.mpdMaintainDao = mpdMaintainDao;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}
	
}
