package com.rskytech.sys.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.M13;
import com.rskytech.pojo.M13C;
import com.rskytech.pojo.M13F;
import com.rskytech.pojo.M2;
import com.rskytech.pojo.MMain;
import com.rskytech.pojo.MReferMsi;
import com.rskytech.sys.bo.IM2Bo;
import com.rskytech.sys.bo.IReCheckMsiBo;

public class ReCheckMsiAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IReCheckMsiBo reCheckMsiBo;
	private IM2Bo m2Bo;
	private String isMaintain;

	public String init() {
		// session过期则直接返回
		if (getSysUser() == null) {
			return SUCCESS;
		}
		// 判断是否有修改权限
		/*if (ComacConstants.ROLE_SYS_ANALYSIS.equals(getSysUser().getComRole()
				.getRoleCode()) || ComacConstants.ROLE_SUPER_ADMIN.equals(getSysUser().getComRole().getRoleCode())) {
			isMaintain = "1";
		} else {
			isMaintain = "0";
		}*/
		isMaintain = "1";
		return SUCCESS;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String loadReferMsi() {
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		List<MReferMsi> list = this.reCheckMsiBo
				.getReferListMsiByModelSeriesId(modelSeriesId);
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		for (MReferMsi referMsi : list) {
			HashMap jsonFeildList = new HashMap();
			jsonFeildList.put("id", referMsi.getRefId());
			ComAta yuanata = (ComAta) this.reCheckMsiBo
					.loadById(ComAta.class, referMsi.getM13C().getM13F()
							.getM13().getMMain().getComAta().getAtaId());
			if(yuanata!=null){
				jsonFeildList.put("yuanMsiCode", yuanata.getAtaCode());
				jsonFeildList.put("yuanFunctionDesc", wipeNull(referMsi.getM13C()
						.getM13F().getM13().getFunctionDesc()));
				jsonFeildList.put("yuanFailureDesc", wipeNull(referMsi.getM13C().getM13F()
						.getFailureDesc()));
				jsonFeildList.put("yuanEffectDesc", wipeNull(referMsi.getM13C().getM13F()
						.getEffectDesc()));
				jsonFeildList.put("yuanCauseDesc", wipeNull(referMsi.getM13C().getCauseDesc()));
			}else{
				jsonFeildList.put("yuanMsiCode","");
				jsonFeildList.put("yuanFunctionDesc", "");
				jsonFeildList.put("yuanFailureDesc", "");
				jsonFeildList.put("yuanEffectDesc", "");
				jsonFeildList.put("yuanCauseDesc", "");
			}
			M2 m2 = this.m2Bo.getM2ByM13fId(referMsi.getM13C().getM13F()
					.getM13fId());
			if(m2!=null){
				jsonFeildList.put("yuanResult", m2.getFailureCauseType());
			}else{
				jsonFeildList.put("yuanResult", "");
			}
			if (!BasicTypeUtils.isNullorBlank(referMsi.getRefMsiId())) {
				MMain mMain = (MMain) this.m2Bo.loadById(MMain.class, referMsi
						.getRefMsiId());
				ComAta ata = (ComAta) this.reCheckMsiBo.loadById(ComAta.class,
								mMain.getComAta().getAtaId());
				jsonFeildList.put("msiCode", ata.getAtaCode());
			}
			if (!BasicTypeUtils.isNullorBlank(referMsi.getRefFunctionId())) {
				M13 m13 = (M13) this.m2Bo.loadById(M13.class, referMsi
						.getRefFunctionId());
				if (m13 != null) {
					jsonFeildList.put("functionalDesc", wipeNull(m13.getFunctionDesc()));
				}
			}
			if (!BasicTypeUtils.isNullorBlank(referMsi.getRefFailureId())) {
				M13F m13f = (M13F) this.m2Bo.loadById(M13F.class, referMsi
						.getRefFailureId());
				if (m13f != null) {
					jsonFeildList.put("failureDesc", wipeNull(m13f.getFailureDesc()));
					jsonFeildList.put("effectDesc", wipeNull(m13f.getEffectDesc()));
				}
			}
			if (!BasicTypeUtils.isNullorBlank(referMsi.getRefCauseId())) {
				M13C m13c = (M13C) this.m2Bo.loadById(M13C.class, referMsi
						.getRefCauseId());
				if (m13c != null) {
					jsonFeildList.put("causeDesc",wipeNull( m13c.getCauseDesc()));
				}
			}
			jsonFeildList.put("isAna", referMsi.getIsAna());
			jsonFeildList.put("remark", wipeNull(referMsi.getRemark()));
			listJsonFV.add(jsonFeildList);
		}
		json.element("refMsi", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	public String saveReferMsi() {
		String jsonData = this.getJsonData();
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = null;
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			String id = jsonObject.getString("id");
			if (!BasicTypeUtils.isNullorBlank(id)) {
				MReferMsi mReferMsi = (MReferMsi) this.reCheckMsiBo.loadById(
						MReferMsi.class, id);
				mReferMsi.setIsAna(jsonObject.getString("isAna"));
				mReferMsi.setRemark(jsonObject.getString("remark"));
				this.reCheckMsiBo.update(mReferMsi, getSysUser().getUserId());
			}

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

	public IReCheckMsiBo getReCheckMsiBo() {
		return reCheckMsiBo;
	}

	public void setReCheckMsiBo(IReCheckMsiBo reCheckMsiBo) {
		this.reCheckMsiBo = reCheckMsiBo;
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

}
