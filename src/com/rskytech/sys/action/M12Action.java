package com.rskytech.sys.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.basedata.bo.IComVendorBo;
import com.rskytech.basedata.dao.IComAtaDao;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComVendor;
import com.rskytech.pojo.M12;
import com.rskytech.pojo.MStep;
import com.rskytech.sys.bo.IM12Bo;
import com.rskytech.sys.bo.IMStepBo;
public class M12Action extends BaseAction {

	private static final long serialVersionUID = 1L;
	public static final String M12 = "M12";
	private IMStepBo mstepBo;
	private MStep mstep;//导航栏步骤
	private String msiId;//Main的Id
	private ComAta showAta;//msi所属的ATA
	private String ataId;  //msi所属的ATA的Id
	private String pagename;//页面名称;
	private String delId;  //删除的数据的ID
	private IM12Bo m12Bo;
	private IComVendorBo comVendorBo;
	private String isMaintain; //用户权限(1:修改,0:查看)'
	private IComAreaBo comAreaBo;
	private IComAtaDao comAtaDao;
	public String init() {
		mstep = mstepBo.getMStepByMsiId(msiId);
		this.pagename = M12;
		showAta = (ComAta) this.mstepBo.loadById(ComAta.class, ataId);
		return SUCCESS;
	}

	/**
	 * 初始化M12画面
	 * 
	 * @return 初始化M12数据
	 * @author chendexu 
	 * createdate 2012-08-21
	 */
	public String loadM12() {
		List<M12> listM12 = m12Bo.getM12AListByMsiId(msiId);
		if (listM12.size() == 0) {
			// 第一次加载M12
			m12Bo.cogradientM12(msiId, getSysUser().getUserId());
			listM12 = m12Bo.getM12AListByMsiId(msiId);
		}
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> listJson = new ArrayList<HashMap<String, Object>>();
		for (M12 m12 : listM12) {
			listJson.add(getJsonFieldValueMap(m12));
		}
		json.element("m12", listJson);
		writeToResponse(json.toString());
		return null;
	}

	private HashMap<String, Object> getJsonFieldValueMap(M12 m12) {
		HashMap<String, Object> jsonFeildList = new HashMap<String, Object>();
		jsonFeildList.put("m12Id", m12.getM12Id());
		jsonFeildList.put("msiId", m12.getMMain().getMsiId());
		if(m12.getProCode()!=null){
			jsonFeildList.put("proCode", m12.getProCode());  //项目编号 
		 }else{
			 jsonFeildList.put("proCode", "");  //项目编号 
		 }
		
		if(m12.getProName()!=null){
			jsonFeildList.put("proName", m12.getProName()); //项目名称中文
		 }else{
			 jsonFeildList.put("proName", ""); //项目名称中文 
		 }
		
		if(m12.getQuantity()!=null){
			jsonFeildList.put("quantity", m12.getQuantity());  //数量 
		 }else{
			 jsonFeildList.put("quantity", "");  //数量 
		 }
		
		if( m12.getVendor()!=null){
			jsonFeildList.put("vendor", m12.getVendor());//供应商Id
		 }else{
			 jsonFeildList.put("vendor","");//供应商Id 
		 }
		
		if(m12.getPartNo()!=null){
			jsonFeildList.put("partNo", m12.getPartNo());//部件编号 
		 }else{
			 jsonFeildList.put("partNo", "");//部件编号 
		 }
		
		if(m12.getSimilar()!=null){
			jsonFeildList.put("similar", m12.getSimilar());//类似 
		 }else{
			jsonFeildList.put("similar", "");//类似 
		 }
		
		if(m12.getZonalChannel()!=null){
			jsonFeildList.put("zonalChannel", m12.getZonalChannel());//区域通道 
		 }else{
			jsonFeildList.put("zonalChannel", "");//区域通道 
		 }
		
		if(m12.getHistoricalMtbf()!=null){
			jsonFeildList.put("historicalMtbf", m12.getHistoricalMtbf());  //过去的MTBF 
		 }else{
			jsonFeildList.put("historicalMtbf", "");  //过去的MTBF 
		 }
		
		if(m12.getPredictedMtbf()!=null){
			jsonFeildList.put("predictedMtbf", m12.getPredictedMtbf());    //预测的MTBF 
		 }else{
			jsonFeildList.put("predictedMtbf", "");    //预测的MTBF 
		 }
		
		if(m12.getZonal()!=null){
			jsonFeildList.put("zonal", comAreaBo.getAreaCodeByAreaId(m12.getZonal())); //所属区域 
		 }else{
			jsonFeildList.put("zonal", ""); //所属区域 
		 }
		
		if(m12.getMmel()!=null){
			jsonFeildList.put("mmel", m12.getMmel()); 
		 }else{
			 jsonFeildList.put("mmel", ""); 
		 }
		
		if(m12.getIsAddAta()!=null){
			jsonFeildList.put("isAddAta", m12.getIsAddAta());
		 }else{
			jsonFeildList.put("isAddAta", "");
		 }
		return jsonFeildList;
	}

	/**
	 * 保存M12页面数据
	 * @return
	 * @author chendexu 
	 * createdate 2012-08-21
	 */
	public String saveM12() {
		
		// db操作区分
		String pageId="M1.2";
		HashMap<String, String> map = this.m12Bo.SaveOrUpdateM12(this.getSysUser(),pageId,ComacConstants.SYSTEM_CODE,jsonData,getComModelSeries(),"1");
		
		if (!map.isEmpty()){
			HashMap<String, String> mapPro = comAtaDao.importAta(getComModelSeries().getModelSeriesId(), "2");
			if ("导入失败".equals(mapPro.get("res"))){
				m12Bo.delete(ComAta.class, map.get("ataId"));
			}
		}
		
		return null;
	}
	
	public String saveZan(){
		String pageId="M1.2";
		HashMap<String, String> map = this.m12Bo.SaveOrUpdateM12(this.getSysUser(),pageId,ComacConstants.SYSTEM_CODE,jsonData,getComModelSeries(),"0");
		
		if (!map.isEmpty()){
			HashMap<String, String> mapPro = comAtaDao.importAta(getComModelSeries().getModelSeriesId(), "2");
			if ("导入失败".equals(mapPro.get("res"))){
				m12Bo.delete(ComAta.class, map.get("ataId"));
			}
		}
		
		return null;
	}
	
	/**
	 * 根据Id删除M12
	 * @return
	 * @author chendexu 
	 * createdate 2012-08-21
	 */
	public String delM12() {
		this.m12Bo.delete(delId, getSysUser().getUserId());
		return null;
	}

	/**
	 * 获取供应商列表
	 * @return
	 * @author chendexu 
	 * createdate 2012-08-21
	 */
	@SuppressWarnings("rawtypes")
	public String searchVendor() {
		String	modelSeriesId = getComModelSeries().getModelSeriesId();
		JSONObject json = new JSONObject();
		List<HashMap> listJson = new ArrayList<HashMap>();
		List<ComVendor> venList = comVendorBo.getVendorList(modelSeriesId);
		HashMap<String, Object> jsonFeildList ;
		for (ComVendor ven : venList) {
			jsonFeildList = new HashMap<String, Object>();
			jsonFeildList.put("id", ven.getVendorId());
			jsonFeildList.put("name", ven.getVendorName());
			listJson.add(jsonFeildList);
		}
		json.element("vendor", listJson);
		writeToResponse(json.toString());
		return null;
	}

	public IMStepBo getMstepBo() {
		return mstepBo;
	}

	public void setMstepBo(IMStepBo mstepBo) {
		this.mstepBo = mstepBo;
	}

	public MStep getMstep() {
		return mstep;
	}

	public void setMstep(MStep mstep) {
		this.mstep = mstep;
	}

	public ComAta getShowAta() {
		return showAta;
	}

	public void setShowAta(ComAta showAta) {
		this.showAta = showAta;
	}

	public String getPagename() {
		return pagename;
	}

	public void setPagename(String pagename) {
		this.pagename = pagename;
	}

	public IM12Bo getM12Bo() {
		return m12Bo;
	}

	public void setM12Bo(IM12Bo m12Bo) {
		this.m12Bo = m12Bo;
	}

	public IComVendorBo getComVendorBo() {
		return comVendorBo;
	}

	public void setComVendorBo(IComVendorBo comVendorBo) {
		this.comVendorBo = comVendorBo;
	}

	public String getIsMaintain() {
		return isMaintain;
	}

	public void setIsMaintain(String isMaintain) {
		this.isMaintain = isMaintain;
	}

	public String getMsiId() {
		return msiId;
	}

	public void setMsiId(String msiId) {
		this.msiId = msiId;
	}

	public String getAtaId() {
		return ataId;
	}

	public void setAtaId(String ataId) {
		this.ataId = ataId;
	}

	public String getDelId() {
		return delId;
	}

	public void setDelId(String delId) {
		this.delId = delId;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}

	public IComAtaDao getComAtaDao() {
		return comAtaDao;
	}

	public void setComAtaDao(IComAtaDao comAtaDao) {
		this.comAtaDao = comAtaDao;
	}
	
	
}
