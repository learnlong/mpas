package com.rskytech.sys.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.M13F;
import com.rskytech.pojo.M2;
import com.rskytech.pojo.MMain;
import com.rskytech.pojo.MReferAfm;
import com.rskytech.sys.bo.IM2Bo;
import com.rskytech.sys.bo.IMsiMainBo;
@SuppressWarnings("unchecked")
public class ReCheckAfmAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private IMsiMainBo msiMainBo;
	private IM2Bo m2Bo;
	private String isMaintain;

	public String init() {
		if (getSysUser() == null){
			return SUCCESS;
		}
		//判断是否有修改权限
	/*	if(ComacConstants.ROLE_SYS_ANALYSIS.equals(getSysUser().getComRole().getRoleCode()) 
				|| ComacConstants.ROLE_SUPER_ADMIN.equals(getSysUser().getComRole().getRoleCode())){
			isMaintain = "1";
		}else{
			isMaintain = "0";
		}*/
		isMaintain = "1";
		return SUCCESS;
	}

	@SuppressWarnings("rawtypes")
	public String loadReferAfm() {
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		String modelSeriesId = getComModelSeries().getModelSeriesId();
		List<MMain> list = this.msiMainBo.getMMainByStatus(modelSeriesId);
		for (MMain main : list) {
			ComAta comAta = (ComAta) this.msiMainBo.loadById(ComAta.class,main.getComAta().getAtaId());
			List<M2> list1 = this.m2Bo.getM2ListByMsiId(main.getMsiId());
			for (M2 m2 : list1) {
				if (ComacConstants.YES.equals(m2.getIsRefAfm())) {
					// 参考了AFM
					HashMap<String, Object> jsonFeildList = new HashMap<String, Object>();
					jsonFeildList.put("msiCode", comAta.getAtaCode());
					jsonFeildList.put("msiName", comAta.getAtaName());
					jsonFeildList.put("failureCauseType", m2.getFailureCauseType());
					jsonFeildList.put("q1Desc", wipeNull(m2.getQ1Desc()));
					M13F m13f = m2.getM13F();
					if(m13f!=null){
						jsonFeildList.put("effectCode", m13f.getEffectCode());
					}
					Set<MReferAfm> set = m2.getMReferAfms();
					if(set!=null){
						for (MReferAfm afm : set) {
							jsonFeildList.put("id", afm.getAfmId());
							jsonFeildList.put("refAfm", wipeNull(afm.getRefAfm()));
							jsonFeildList.put("reviewResult", wipeNull(afm.getReviewResult()));
							if (afm.getReviewDate() != null) {
								jsonFeildList.put("reviewDate", BasicTypeUtils
										.getShortFmtDate(afm.getReviewDate()));
							} else {
								jsonFeildList.put("reviewDate", "");
							}
							jsonFeildList.put("remark", wipeNull(afm.getRemark()));
						}
					}
					listJsonFV.add(jsonFeildList);
				}
			}
		}
		json.element("refAfm", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	
	
	public String saveReferAfm(){
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = null;
		MReferAfm afm ;
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			String id = jsonObject.getString("id");
			afm =(MReferAfm)this.m2Bo.loadById(MReferAfm.class,id);
			afm.setRefAfm(jsonObject.getString("refAfm"));
			afm.setReviewResult(jsonObject.getString("reviewResult"));
			if(!BasicTypeUtils.isNullorBlank(jsonObject.getString("reviewDate"))){
			try {
				Date date = BasicTypeUtils.getCurrentDateforSQL(jsonObject.getString("reviewDate"));
				afm.setReviewDate(date);
			 } catch (ParseException e) {
				e.printStackTrace();
			 }
			}
			afm.setRemark(jsonObject.getString("remark"));
			this.m2Bo.update(afm, getSysUser().getUserId());
		}
		return null;
	}
	
	/**
	 * 将null替换为“”
	 * 
	 * @param str
	 * @return
	 */
	private String wipeNull(String str) {
		if (str != null) {
			return str;
		}
		return "";
	}
	
	public IM2Bo getM2Bo() {
		return m2Bo;
	}

	public void setM2Bo(IM2Bo bo) {
		m2Bo = bo;
	}

	public String getIsMaintain() {
		return isMaintain;
	}

	public void setIsMaintain(String isMaintain) {
		this.isMaintain = isMaintain;
	}

	public IMsiMainBo getMsiMainBo() {
		return msiMainBo;
	}

	public void setMsiMainBo(IMsiMainBo msiMainBo) {
		this.msiMainBo = msiMainBo;
	}
	

}
