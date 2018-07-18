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
import com.rskytech.pojo.MReferMmel;
import com.rskytech.sys.bo.IM2Bo;
import com.rskytech.sys.bo.IMsiMainBo;
@SuppressWarnings("unchecked")
public class ReCheckMmelAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private IMsiMainBo msiMainBo;
	private IM2Bo m2Bo;
	private String isMaintain;
	public String init() {
		// session过期则直接返回
		if (getSysUser() == null){
			return SUCCESS;
		}
		//判断是否有修改权限
//		if(ComacConstants.ROLE_SYS_ANALYSIS.equals(getSysUser().getComRole().getRoleCode())
//				|| ComacConstants.ROLE_SUPER_ADMIN.equals(getSysUser().getComRole().getRoleCode())){
			isMaintain = "1";
//		}else{
//			isMaintain = "0";
//		}
		return SUCCESS;
	}

	@SuppressWarnings("rawtypes")
	public String loadReferMmel() {
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		List<MMain> list = this.msiMainBo.getMMainByStatus(modelSeriesId);
		for (MMain main : list) {
			ComAta comAta = (ComAta) this.msiMainBo.loadById(ComAta.class, main.getComAta().getAtaId());
			List<M2> list1 = this.m2Bo.getM2ListByMsiId(main.getMsiId());
			for (M2 m2 : list1) {
				if (ComacConstants.YES.equals(m2.getIsRefMmel())) {
					// 参考了MMEL
					HashMap<String, Object> jsonFeildList = new HashMap<String, Object>();
					jsonFeildList.put("msiCode", comAta.getAtaCode());
					jsonFeildList.put("msiName", comAta.getAtaName());
					jsonFeildList.put("failureCauseType", m2.getFailureCauseType());
					jsonFeildList.put("q4Desc", wipeNull(m2.getQ4Desc()));
					M13F m13f = m2.getM13F();
					if(m13f!=null){
						jsonFeildList.put("effectCode", m13f.getEffectCode());
					}
					Set<MReferMmel> set = m2.getMReferMmels();
					if(set!=null){
						for (MReferMmel referMmel : set) {
							jsonFeildList.put("id", referMmel.getMmelId());
							jsonFeildList.put("isRefPmmel", referMmel.getIsRefPmmel());
							jsonFeildList.put("reviewResult", wipeNull(referMmel.getReviewResult()));
							if (referMmel.getReviewDate() != null) {
								jsonFeildList.put("reviewDate", BasicTypeUtils.getShortFmtDate(referMmel.getReviewDate()));
							} else {
								jsonFeildList.put("reviewDate", "");
							}
							jsonFeildList.put("remark", wipeNull(referMmel.getRemark()));
							jsonFeildList.put("pmmelId", referMmel.getPmmelId());
						}
					}
					listJsonFV.add(jsonFeildList);
				}
			}
		}
		json.element("refMmel", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	public String saveReferMmel() {
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = null;
		MReferMmel mmel ;
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			String id = jsonObject.getString("id");
		    mmel =(MReferMmel)this.m2Bo.loadById(MReferMmel.class, id);
		    if (BasicTypeUtils.isNumberString(jsonObject
					.getString("isRefPmmel"))) {
			mmel.setIsRefPmmel(jsonObject.getInt("isRefPmmel"));
		    }
			mmel.setReviewResult(jsonObject.getString("reviewResult"));
			Date date;
			if(!BasicTypeUtils.isNullorBlank(jsonObject.getString("reviewDate"))){
			try {
				date = BasicTypeUtils.getCurrentDateforSQL(jsonObject.getString("reviewDate"));
				mmel.setReviewDate(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			}
			mmel.setRemark(jsonObject.getString("remark"));
			mmel.setPmmelId(jsonObject.getString("pmmelId"));
			this.m2Bo.update(mmel,getSysUser().getUserId());
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
