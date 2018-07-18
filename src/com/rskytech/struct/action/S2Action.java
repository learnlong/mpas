package com.rskytech.struct.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;

import com.richong.arch.action.BaseAction;
import com.rskytech.pojo.S2;
import com.rskytech.struct.bo.IS1Bo;
import com.rskytech.struct.bo.IS2Bo;

public class S2Action extends BaseAction {
	private static final long serialVersionUID = -6474407740391448086L;
	private String content;
	private String ssiId;
	private IS2Bo s2Bo;
	private String id;
	private int[] step;
	private IS1Bo s1Bo;
	public IS1Bo getS1Bo() {
		return s1Bo;
	}
	public void setS1Bo(IS1Bo bo) {
		s1Bo = bo;
	}
	/**
	 * 保存S2
	 * @return
	 */
	public String saveS2Records(){
		writeToResponse(s2Bo.saveS2Record(ssiId, id, getSysUser(), content));
		return null;
	}
	
	/**
	 * 获取S2的数据
	 * @return
	 */
	public String getS2Records(){
		List<S2> s2List = s2Bo.getS2BySssId(ssiId);
		List<HashMap<String, String>> listJson = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> jsonFeildList = null;
		JSONArray json = new JSONArray();
		if (s2List != null) {
			for (S2 s2 : s2List) {
				jsonFeildList= new HashMap<String, String>();
				if(s2.getPicContent()!=null){
					jsonFeildList.put("content",s2.getPicContent());
				}else{
					jsonFeildList.put("content","");
				}
				listJson.add(jsonFeildList);
			}
			json.addAll(listJson);
		}
		writeToResponse(json.toString());
		return null;
	
		
	}
	
	public IS2Bo getS2Bo() {
		return s2Bo;
	}
	public void setS2Bo(IS2Bo bo) {
		s2Bo = bo;
	}
	public String getSsiId() {
		return ssiId;
	}
	public void setSsiId(String ssiId) {
		this.ssiId = ssiId;
	}
	public int[] getStep() {
		return step;
	}
	public void setStep(int[] step) {
		this.step = step;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
