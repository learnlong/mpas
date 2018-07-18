package com.rskytech.struct.bo.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComCoordination;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.S4;
import com.rskytech.pojo.S5;
import com.rskytech.pojo.S6;
import com.rskytech.pojo.S6Ea;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.SStep;
import com.rskytech.pojo.Sy;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.process.bo.IComCoordinationBo;
import com.rskytech.struct.bo.IS1Bo;
import com.rskytech.struct.bo.IS3Bo;
import com.rskytech.struct.bo.IS6Bo;
import com.rskytech.struct.dao.IS6Dao;
import com.rskytech.struct.dao.IS7Dao;
import com.rskytech.task.dao.ITaskMsgDao;

@SuppressWarnings({"rawtypes","unchecked"})
public class S6Bo extends BaseBO implements IS6Bo {
	private IS6Dao s6Dao;
	private IS1Bo s1Bo;
	private IS3Bo s3Bo;
	private IS7Dao s7Dao;
	private IComAreaBo comAreaBo;
    private IComCoordinationBo comCoordinationBo;
    private ITaskMsgDao taskMsgDao;
	
	public IComCoordinationBo getComCoordinationBo() {
		return comCoordinationBo;
	}

	public void setComCoordinationBo(IComCoordinationBo comCoordinationBo) {
		this.comCoordinationBo = comCoordinationBo;
	}

	@Override
	public List<S4> getS4Records(String id,String region) throws BusinessException {
		return this.s6Dao.getS4Records(id,region);
	}
	
	@Override
	public List<Sy> getSyRecords(String id,String region) throws BusinessException {
		return this.s6Dao.getSyRecords(id,region);
	}

	@Override
	public List<S5> getS5Records(String id,String region) throws BusinessException {
		return this.s6Dao.getS5Records(id,region);
	}

	@Override
	public List getS6EaRecords(String ssiId,String region) throws BusinessException {
		return s6Dao.getS6EaRecords(ssiId,region);
	}
	
	
	/**
	 * 根据较小级别得到知识库s6评级表中的ed/ad间隔数据
	 * @param obs
	 * @return
	 */
	public List getS6CusInList(String inOrOut,int miniValue,String modelSeriesId){
		if(inOrOut.equals("in")){
			inOrOut ="int";
		}else{
			inOrOut ="out";
		}
		if(miniValue <=0){
			miniValue = 1;
		}
		return s6Dao.getCusInList(inOrOut,miniValue,modelSeriesId);
	}
	
	@Override
	public S6 getS6Records(String ssiId,String inorOut) throws BusinessException {
		List<S6> s6List=this.s6Dao.getS6Records(ssiId, inorOut);
		if(s6List!=null&&!s6List.isEmpty()){
			return s6List.get(0);
		}
		return null;
	}

	public IS6Dao getS6Dao() {
		return s6Dao;
	}

	public void setS6Dao(IS6Dao dao) {
		s6Dao = dao;
	}
	
	/**
	 * 得到临时任务数据符合要求的
	 * @param modelId 飞机ID
	 * @param sourceSystem 所在系统区域
	 * @param taskValid 数据的有效性
	 * @param region 数据所属内或者外
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public List<TaskMsg> getOtherRecords(String ssiId,String modelId, String sourceSystem,Integer taskValid,String region)
			throws BusinessException {
		return this.s6Dao.getOtherRecords(ssiId, modelId, sourceSystem, taskValid, region);
	}

	@Override
	public List<S6> getS6BySsiId(String ssiId,String inOrOut) throws BusinessException {
		List<S6> s6List=this.s6Dao.getS6BySsiId(ssiId, inOrOut);
		if(s6List!=null&&!s6List.isEmpty()){
			return s6List;
		}
		return null;
	}

	@Override
	public List<CusInterval> getIntervalRecords(String inOrOut, String modelId)
			throws BusinessException {
		List<CusInterval> list = this.s6Dao.getIntervalRecords(inOrOut, modelId);
		if(list!=null&&!list.isEmpty()){
			return list;
		}
		return null;
	}

	@Override
	public List<S1> getS1IdBySssiId(String ssiId,String inOrOut) throws BusinessException {
		List<S1> list = this.s6Dao.getS1IdBySssiId(ssiId, inOrOut);
		if(list!=null&&!list.isEmpty()){
			return list;
		}
		return null;
	}

	@Override
	public List<S6Ea> getS6EaByS1Id(String inorOut, String i)   throws BusinessException {
		return s6Dao.getS6EaByS1Id(inorOut,i);
	}


	@Override
	public List<String> saveS6Records(String delId,String comModelSeriesId,String otherJsonData,String resultJsonData,
			String listJsonData,String finalRemark,String cpcp,String ssiId,
			String s6Id,String inorOut,ComUser user,String coverCpcp,String considerWear) throws BusinessException {
		String s1Id=null;
		List<String> arr = new ArrayList<String>();
		if(delId!=null){
			String[] delArray=delId.split(",");
			TaskMsg tm;
			for(String str:delArray){
				if("".equals(str.trim())){
					continue;
				}
				
				tm = (TaskMsg) this.loadById(TaskMsg.class, str.trim());
				if(tm!=null){
					if(tm.getNeedTransfer()!=null&&tm.getNeedTransfer()==1){
						arr.add(tm.getOwnArea());
					}
					taskMsgDao.deleteTasksByTaskId(tm.getTaskId());
				}
			}
		}
		String state = ComacConstants.DB_INSERT;
		S6 s6 = null;
		S6Ea s6ea = null;
		TaskMsg taskMsg = null;
		SMain sMain = (SMain) this.loadById(SMain.class, ssiId);
		if (!BasicTypeUtils.isNullorBlank(s6Id)) {
			s6=(S6)this.loadById(S6.class, s6Id);
			s6.setS6Id(s6Id);
			state = ComacConstants.DB_UPDATE;
		}else{
			s6 = new S6();
		}
		s6.setSMain(sMain);
		if ("in".equals(inorOut)) {
			s6.setInOrOut(ComacConstants.INNER);
		}
		if ("out".equals(inorOut)) {
			s6.setInOrOut(ComacConstants.OUTTER);
		}
		if("是".equals(considerWear)){
			s6.setConsiderWear("1");
		}
		if("否".equals(considerWear)){
			s6.setConsiderWear("0");
			
			//删除对应的协调单
			List<ComCoordination> list = comCoordinationBo.findCoordinationById(ssiId, inorOut, comModelSeriesId, ComacConstants.YES);
			ComCoordination comCoordination;
			if(list.size()>0){
				for(ComCoordination cc : list ){
					if(cc.getCoordinationId()!=null&&!"".equals(cc.getCoordinationId())){
						comCoordination= (ComCoordination) comCoordinationBo.loadById(ComCoordination.class, cc.getCoordinationId());
						this.delete(comCoordination, user.getUserId());
					}
				}
			}
			
		}
		if ("是".equals(coverCpcp)) {
			s6.setIsCpcp(1);
			s6.setCpcp("");
		}else if ("否".equals(coverCpcp)) {
			s6.setIsCpcp(0);
			s6.setCpcp(cpcp);
		}else{
			s6.setIsCpcp(null);
			s6.setCpcp(null);
		}
		s6.setFinalRemark(finalRemark);
		this.saveOrUpdate(s6, state, user.getUserId());
		JSONArray listjsonArray = JSONArray.fromObject(listJsonData);
		JSONArray resultJsonArray = JSONArray.fromObject(resultJsonData);
		JSONArray otherJsonArray = JSONArray.fromObject(otherJsonData);
		JSONObject jsonObject = null;
		// 保存AD/ED列表
		S1 s1 =null;
		for (int i = 0; i < listjsonArray.size(); i++) {
			String eastate = ComacConstants.DB_INSERT;
			s6ea = new S6Ea();
			jsonObject = listjsonArray.getJSONObject(i);
			String id = jsonObject.getString("id").trim();
			if (!BasicTypeUtils.isNullorBlank(id)) {
				s6ea.setEaId(id);
				eastate = ComacConstants.DB_UPDATE;
			}
			s1Id=jsonObject.getString("s1Id").trim();
			s1=(S1)this.loadById(S1.class, s1Id);
			s6ea.setS1(s1);
			s6ea.setS6(s6);
			if ("1".equals(jsonObject.getString("metal"))) {
				s6ea.setAdr(jsonObject.getDouble("adrTrue"));
				s6ea.setEdr(jsonObject.getDouble("edrTrue"));
				if(jsonObject.get("intervalTrue")!=null){
					s6ea.setInterval(jsonObject.getString("intervalTrue"));
				}
				if(jsonObject.get("eaTypeTrue")!=null){
					s6ea.setEaType(jsonObject.getString("eaTypeTrue"));
				}
			}
			if ("0".equals(jsonObject.getString("metal"))) {
				s6ea.setAdr(jsonObject.getDouble("adrFalse"));
				s6ea.setEdr(jsonObject.getDouble("edrFalse"));
				if(jsonObject.get("intervalFalse")!=null){
					s6ea.setInterval(jsonObject.getString("intervalFalse"));
				}
				if(jsonObject.get("eaTypeFalse")!=null){
					s6ea.setEaType(jsonObject.getString("eaTypeFalse"));
				}
			}
			 this.saveOrUpdate(s6ea, eastate, user.getUserId());
		}
		// 保存临时任务
		for (int i = 0; i < otherJsonArray.size(); i++) {
			taskMsg = new TaskMsg();
			String otstate = ComacConstants.DB_INSERT;
			jsonObject = otherJsonArray.getJSONObject(i);
			String id = jsonObject.getString("taskId");
			if (!BasicTypeUtils.isNullorBlank(id)) {
				taskMsg=(TaskMsg)this.loadById(TaskMsg.class, id);
				otstate = ComacConstants.DB_UPDATE;
			}
			//根据页面的属性判断是内还是外
			if ("in".equals(inorOut)&&BasicTypeUtils.isNullorBlank(id)) {
				taskMsg.setAnyContent3("0");
			}
			if ("out".equals(inorOut)&&BasicTypeUtils.isNullorBlank(id)) {
				taskMsg.setAnyContent3("1");
			}
			taskMsg.setTaskType(jsonObject.getString("taskType"));
			taskMsg.setValidFlag(ComacConstants.VALIDFLAG_YES);
			taskMsg.setTaskCode(jsonObject.getString("taskCode"));
			taskMsg.setSourceStep(jsonObject.getString("sourceStep"));
			taskMsg.setComModelSeries((ComModelSeries) this.loadById(ComModelSeries.class, comModelSeriesId));
			taskMsg.setSourceSystem(ComacConstants.STRUCTURE_CODE);
			taskMsg.setSourceAnaId(ssiId);
			taskMsg.setTaskInterval(jsonObject.getString("occures"));// 标准任务间隔
			taskMsg.setReachWay(jsonObject.getString("reachWayn"));
			if ("AD/ED".equals(jsonObject.getString("sourceStep"))&&BasicTypeUtils.isNullorBlank(id)) {
				taskMsg.setSourceStep("AD/ED");
			}
			taskMsg.setEffectiveness(jsonObject.getString("eff"));
			taskMsg.setTaskDesc(jsonObject.getString("taskDesc"));
			String areaId=comAreaBo.getAreaIdByAreaCode(jsonObject.getString("ownArea"), comModelSeriesId);
			taskMsg.setOwnArea(areaId);
			taskMsg.setAnyContent1(jsonObject.getString("s1Id"));
			if (jsonObject.get("mrbId") != null) {
				taskMsg.setAnyContent2(jsonObject.getString("mrbId"));// 最终合并后的任务编号
			}
			taskMsg.setTaskValid(3);
			 this.saveOrUpdate(taskMsg, otstate, user.getUserId());
		}
		// 保存最终任务
		for (int i = 0; i < resultJsonArray.size(); i++) {
			taskMsg = new TaskMsg();
			String otstate = ComacConstants.DB_INSERT;
			jsonObject = resultJsonArray.getJSONObject(i);
			String id = jsonObject.getString("taskId");
		   if (!BasicTypeUtils.isNullorBlank(id)) {
				taskMsg=(TaskMsg)this.loadById(TaskMsg.class, id);
				otstate = ComacConstants.DB_UPDATE;
			}
			taskMsg.setTaskCode((jsonObject.getString("taskCode")));// 最终合并后的任务编号
			taskMsg.setTaskType(jsonObject.getString("taskType"));// 任务类型
			taskMsg.setTaskInterval(jsonObject.getString("occures"));// 标准任务间隔
			taskMsg.setReachWay(jsonObject.getString("reachWayn"));
			taskMsg.setValidFlag(ComacConstants.VALIDFLAG_YES);
			taskMsg.setTaskDesc(jsonObject.getString("taskDesc"));
			taskMsg.setAnyContent3(jsonObject.getString("intOut"));
			String areaId1=comAreaBo.getAreaIdByAreaCode(jsonObject.getString("ownArea"),comModelSeriesId);
			taskMsg.setEffectiveness(jsonObject.getString("eff"));
			taskMsg.setOwnArea(areaId1);
			taskMsg.setSourceStep("S6");
			taskMsg.setComModelSeries((ComModelSeries) this.loadById(ComModelSeries.class, comModelSeriesId));
			taskMsg.setSourceSystem(ComacConstants.STRUCTURE_CODE);
			taskMsg.setSourceAnaId(ssiId);
			taskMsg.setRemark(jsonObject.getString("remark"));
			 this.saveOrUpdate(taskMsg, otstate,user.getUserId());
		}

		// 保存结束后修改流程状态
		String index = null;
		List<SStep> step1 = s1Bo.getSstepBySssiId((ssiId));
		if ("in".equals(inorOut)) {
			step1.get(0).setS6In(1);
			if (step1.get(0).getS4aOut() != 3) {
				if (step1.get(0).getS4aOut() != 1) {
					step1.get(0).setS4aOut(2);
				}
				index = "9";
			} else if (step1.get(0).getS4bOut() != 3) {
				if (step1.get(0).getS4bOut() != 1) {
					step1.get(0).setS4bOut(2);
				}
				index = "11";
			} else {
				if (step1.get(0).getS7() == 1) {
				} else {
					step1.get(0).setS7(2);
				}
				index = "14";
			}
		}
		if ("out".equals(inorOut)) {
			step1.get(0).setS6Out(1);
			index = "14";
		}
		s3Bo.saveOrUpdate(step1.get(0), ComacConstants.DB_UPDATE,
		user.getUserId());
		this.saveComLogOperate(user, "S6", ComacConstants.STRUCTURE_CODE);
		arr.add(index);
		return arr;
	}


	@Override
	public List searchSsi(String ssiId,String str) throws BusinessException {
		List list=s6Dao.searchSsi(ssiId,str);
		return list;
	}


	/**
	 * 校验任务临时任务表中任务编号是否存在
	 */
	public boolean checkTempTaskCodeExist(String tempTaskCode,String modelId, String region){
		List<TaskMsg> list = this.s6Dao.searchTempTaskCode(tempTaskCode,modelId,region);
		if(list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	public IS3Bo getS3Bo() {
		return s3Bo;
	}

	public void setS3Bo(IS3Bo bo) {
		s3Bo = bo;
	}

	public IS1Bo getS1Bo() {
		return s1Bo;
	}

	public void setS1Bo(IS1Bo bo) {
		s1Bo = bo;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}

	public IS7Dao getS7Dao() {
		return s7Dao;
	}

	public void setS7Dao(IS7Dao s7Dao) {
		this.s7Dao = s7Dao;
	}

	public ITaskMsgDao getTaskMsgDao() {
		return taskMsgDao;
	}

	public void setTaskMsgDao(ITaskMsgDao taskMsgDao) {
		this.taskMsgDao = taskMsgDao;
	}

}
