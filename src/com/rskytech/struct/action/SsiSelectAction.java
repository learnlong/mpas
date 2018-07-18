package com.rskytech.struct.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IZa7Dao;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.SMain;
import com.rskytech.struct.bo.ISsiSelectBo;
import com.rskytech.task.bo.ITaskMsgDetailBo;
import com.rskytech.user.dao.IComUserDao;


public class SsiSelectAction extends BaseAction {

	private static final long serialVersionUID = 8149380105977586301L;
	private ISsiSelectBo ssiSelectBo;
	private String ataId;
	private String isMaintain;
	private IComUserDao comUserDao;
	private String verifyStr;//检验的ssi编号
	private String ssiId;
	private ITaskMsgDetailBo taskMsgDetailBo;
	private IZa7Dao za7Dao;

	/**
	 * 得到ssi选择数据
	 * @return
	 */
	public String init() {
		
		SMain sAna = null;
		ComUser thisUser = getSysUser();
		if (thisUser == null){
			return SUCCESS;
		}
		if(ataId!=null&&ssiId!=null&&ssiId.equals(ataId)){
			sAna = (SMain) ssiSelectBo.loadById(SMain.class, ssiId);
		}else{
			List<SMain> list = ssiSelectBo.getSMainByAtaId(ataId);
			if(list!=null){
				for (SMain smain : list) {
					sAna = smain;
				}
			}
		}
		if(sAna!=null&&(ComacConstants.ANALYZE_STATUS_APPROVED.equals(sAna.getStatus())||ComacConstants.ANALYZE_STATUS_HOLD.equals(sAna.getStatus()))){
			isMaintain = ComacConstants.NO.toString();
		}
		return SUCCESS;
	}
	
	/**
	 * 得到SSelect数据
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getSsiSelectList() {
		HashMap jsonFeildList = null;
		List listJson = new ArrayList();
		JSONObject json = new JSONObject();
		if(ataId!=null&&ssiId!=null&&ssiId.equals(ataId)){
			SMain sMain = (SMain) ssiSelectBo.loadById(SMain.class, ssiId);
			if(sMain!=null){
				jsonFeildList = new HashMap();
				jsonFeildList.put("id", sMain.getSsiId());
				jsonFeildList.put("ataCode",  sMain.getAddCode());
				jsonFeildList.put("ataName",  sMain.getAddName());
				jsonFeildList.put("isSsi",  sMain.getIsSsi()+"");
				jsonFeildList.put("isAna",sMain.getIsAna()+"");
				jsonFeildList.put("isAdd",sMain.getIsAdd()+"");
				if(sMain.getAddUser()!=null){
					ComUser cu=(ComUser)ssiSelectBo.loadById(ComUser.class, sMain.getAddUser());
					jsonFeildList.put("anaUser", cu.getUserName());
				}else{
					jsonFeildList.put("anaUser", "");
				}
				listJson.add(jsonFeildList);
			}
		}else{
			List<Object[]> list = ssiSelectBo.getSsiListByAtaId(ataId, getComModelSeries().getModelSeriesId(),getSysUser().getUserId());
			if(list!=null){
				for (Object[] obj : list) {
					jsonFeildList = new HashMap();
					jsonFeildList.put("id",  obj[0]);
					jsonFeildList.put("ataCode",  obj[1]);
					jsonFeildList.put("ataName", obj[2]);
					jsonFeildList.put("isSsi",  obj[3]+"");
					jsonFeildList.put("isAna",obj[4]+"");
					jsonFeildList.put("isAdd",obj[5]+"");
					if(obj[6]!=null){
						ComUser cu=(ComUser)ssiSelectBo.loadById(ComUser.class, obj[6].toString());
						jsonFeildList.put("anaUser", cu.getUserName());
					}else{
						jsonFeildList.put("anaUser","");
					}
					listJson.add(jsonFeildList);
				}
			}
		}
		json.element("ata", listJson);
		writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 认领任务
	 * @return
	 */
	public String claimTask(){
		SMain sMain = (SMain) ssiSelectBo.loadById(SMain.class,ssiId);
		String ataId = sMain.getParentAtaId();
		if(ssiSelectBo.searchAnalysisProByAtaId(ataId, //判断父节点id是否有分析权限
												this.getComModelSeries().getModelSeriesId(),  getSysUser().getUserId())){
			
			sMain.setAddUser(getSysUser().getUserId());
			ssiSelectBo.saveOrUpdate(sMain, ComacConstants.DB_UPDATE, getSysUser().getUserId());
			writeToResponse("{'exist':"+true+"}");
		}else{
			writeToResponse("{'exist':"+false+"}");
		}
		return null;
	}
	
	/**
	 * 检查ata编号是否存在
	 * @return
	 */
	public String verifySsiCode(){
		boolean flag=ssiSelectBo.verifySsiCode(verifyStr.trim(),getComModelSeries().getModelSeriesId());
		if(!flag){
			writeToResponse(verifyStr);
			return null;
		}
		return null;
	}
	
	/**
	 * 保存ssi数据
	 * @return
	 */
	public String saveSsilist() { 
		String jsonData = this.getJsonData();
		ArrayList<String> array = ssiSelectBo.saveSsi(jsonData, ataId, getSysUser().getUserId(),getComModelSeries());
		if(array!=null&&array.size()>0){
			for(String areaId : array){
				String[] arr = areaId.split(",");
				for(String string : arr){
					taskMsgDetailBo.updateZa7Status(getComModelSeries().getModelSeriesId(), getSysUser().getUserId(),string);
				}
			}
			za7Dao.cleanTaskInterval(getComModelSeries().getModelSeriesId());
		}
		return null;
	}
	/**
	 * 删除ssi数据
	 * @return
	 */
	public String delRecord() {
		ArrayList<String> array = ssiSelectBo.delRecord(ssiId,getSysUser().getUserId(),getComModelSeries().getModelSeriesId());
		if(array!=null&&array.size()>0){
			for(String areaId : array){
				String[] arr = areaId.split(",");
				for(String string : arr){
					taskMsgDetailBo.updateZa7Status(getComModelSeries().getModelSeriesId(), getSysUser().getUserId(),string);
				}
			}
			za7Dao.cleanTaskInterval(getComModelSeries().getModelSeriesId());
		}
		return null;
	}
	public String getAtaId() {
		return ataId;
	}

	public void setAtaId(String ataId) {
		this.ataId = ataId;
	}

	public ISsiSelectBo getSsiSelectBo() {
		return ssiSelectBo;
	}

	public void setSsiSelectBo(ISsiSelectBo ssiSelectBo) {
		this.ssiSelectBo = ssiSelectBo;
	}

	public String getIsMaintain() {
		return isMaintain;
	}
	public void setIsMaintain(String isMaintain) {
		this.isMaintain = isMaintain;
	}

	public IComUserDao getComUserDao() {
		return comUserDao;
	}

	public void setComUserDao(IComUserDao comUserDao) {
		this.comUserDao = comUserDao;
	}

	public String getSsiId() {
		return ssiId;
	}

	public void setSsiId(String ssiId) {
		this.ssiId = ssiId;
	}

	public String getVerifyStr() {
		return verifyStr;
	}

	public void setVerifyStr(String verifyStr) {
		this.verifyStr = verifyStr;
	}

	public ITaskMsgDetailBo getTaskMsgDetailBo() {
		return taskMsgDetailBo;
	}

	public void setTaskMsgDetailBo(ITaskMsgDetailBo taskMsgDetailBo) {
		this.taskMsgDetailBo = taskMsgDetailBo;
	}

	public IZa7Dao getZa7Dao() {
		return za7Dao;
	}

	public void setZa7Dao(IZa7Dao za7Dao) {
		this.za7Dao = za7Dao;
	}
	
}
	
