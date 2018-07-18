package com.rskytech.sys.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.area.dao.IZa7Dao;
import com.rskytech.basedata.bo.IComAtaBo;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.MSelect;
import com.rskytech.sys.bo.IMsiSelectBo;
import com.rskytech.task.bo.ITaskMsgDetailBo;
@SuppressWarnings({ "unchecked"})
public class MsiSelectAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private IMsiSelectBo msiSelectBo;
	private IComAtaBo comAtaBo;
	private String ataId;
	private String parentId;
    private ComAta comAta;
	private String isMaintain;
	private ITaskMsgDetailBo taskMsgDetailBo;
	private IZa7Dao za7Dao;
	
	public String init(){	
		comAta=(ComAta)comAtaBo.loadById(ComAta.class, ataId);
		return SUCCESS;
	}	
	
	/**
	 * 初始化MSI选择画面
	 * @return 初始化MMEL数据
	 * @author chendexu
	 * createdate 2012-08-016
	 */
	@SuppressWarnings("rawtypes")
	public String loadMSelectList(){
		JSONObject json = new JSONObject();
		List<HashMap> listJson = new ArrayList<HashMap>();
		List<Object[]> list=msiSelectBo.getAtaAndfindAllChildByAtaId(ataId,
								getComModelSeries().getModelSeriesId(),getSysUser().getUserId());
	
		HashMap jsonFeildList = null;
		if(list!=null){
			for (Object[] obj : list) {
				List<MSelect> lists = msiSelectBo.getMSelectByataId((String)obj[0],getComModelSeries().getModelSeriesId());
				//ata有对应的msi选择记录时
				if(lists.size()>0){
					for(MSelect mSelect : lists){
						jsonFeildList = new HashMap();
						jsonFeildList.put("ataId",(String)obj[0]);
						jsonFeildList.put("proCode",(String)obj[1]);
						jsonFeildList.put("proName",(String)obj[2]);
						jsonFeildList.put("ataLevel",obj[3]);
						jsonFeildList.put("selectId",mSelect.getSelectId() );
						jsonFeildList.put("safety",mSelect.getSafety() );
						jsonFeildList.put("safetyAnswer",wipeNull(mSelect.getSafetyAnswer()));
						jsonFeildList.put("detectable",mSelect.getDetectable() );
						jsonFeildList.put("detectableAnswer",wipeNull(mSelect.getDetectableAnswer())) ;
						jsonFeildList.put("task",mSelect.getTask()) ;
						jsonFeildList.put("taskAnswer",wipeNull(mSelect.getTaskAnswer()));
						jsonFeildList.put("economic",mSelect.getEconomic());
						jsonFeildList.put("economicAnswer",wipeNull(mSelect.getTaskAnswer()));
						jsonFeildList.put("isMsi",mSelect.getIsMsi() );
						jsonFeildList.put("highestLevel",wipeNull(mSelect.getHighestLevel()));
						jsonFeildList.put("remark",wipeNull(mSelect.getRemark()));
						listJson.add(jsonFeildList);
					}
				}else{
					jsonFeildList = new HashMap();
					jsonFeildList.put("ataId",(String)obj[0]);
					jsonFeildList.put("proCode",wipeNull((String)obj[1]));
					jsonFeildList.put("proName",wipeNull((String)obj[2]));
					jsonFeildList.put("ataLevel",obj[3]);
					listJson.add(jsonFeildList);
				}
			}
		}
		json.element("mSelect",listJson);
		writeToResponse(json.toString());
		return null;
	}
 
	/**
	 * 保存数据
	 * @param ata
	 */
	public String saveMSelect(){
		ArrayList<String> errProCodes=this.msiSelectBo.checkoutHighLevel(jsonData, getComModelSeries().getModelSeriesId());
		JSONObject json = new JSONObject();
		String errMsg="";
		if(errProCodes.size()>0){
			for (String str : errProCodes) {
				errMsg=errMsg+","+str;
			}
			if(!errMsg.equals("")){
				errMsg=errMsg.substring(1);
				json.put("errProCodes",errMsg);
				this.writeToResponse(json.toString());
				return null;
			}
			
		}
		ArrayList<String> array = this.msiSelectBo.savaMSelect(jsonData,getComModelSeries(),this.getSysUser().getUserId());
		if(array!=null&&array.size()>0){
			for(String areaId : array){
				String[] arr = areaId.split(",");
				for(String string : arr){
					taskMsgDetailBo.updateZa7Status(getComModelSeries().getModelSeriesId(), getSysUser().getUserId(),string);
				}
			}
			za7Dao.cleanTaskInterval(getComModelSeries().getModelSeriesId());
		}
		json.put("errProCodes",errMsg);
		this.writeToResponse(json.toString());
		return null;
	}
	 /**
	 * 将空字符串替换为“”
	 * @param str
	 * @return
	 * @author chendexu
	 * createdate 2012-08-29
	 */
	private String wipeNull(String str){
		if(str!=null&&!"null".equals(str)){
			return str;
		}
		return "";
	}
	public IMsiSelectBo getMsiSelectBo() {
		return msiSelectBo;
	}
	public void setMsiSelectBo(IMsiSelectBo msiSelectBo) {
		this.msiSelectBo = msiSelectBo;
	}
	public String getAtaId() {
		return ataId;
	}
	public void setAtaId(String ataId) {
		this.ataId = ataId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public ComAta getComAta() {
		return comAta;
	}

	public void setComAta(ComAta comAta) {
		this.comAta = comAta;
	}

	public IComAtaBo getComAtaBo() {
		return comAtaBo;
	}

	public void setComAtaBo(IComAtaBo comAtaBo) {
		this.comAtaBo = comAtaBo;
	}
	public String getIsMaintain() {
		return isMaintain;
	}
	public void setIsMaintain(String isMaintain) {
		this.isMaintain = isMaintain;
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
