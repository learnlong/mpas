package com.rskytech.struct.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.util.JavaScriptUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.area.bo.IZa1Bo;
import com.rskytech.area.dao.IZa7Dao;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.S2;
import com.rskytech.pojo.S4;
import com.rskytech.pojo.S5;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.SRemark;
import com.rskytech.pojo.SStep;
import com.rskytech.pojo.Sy;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.ZaMain;
import com.rskytech.process.bo.IComCoordinationBo;
import com.rskytech.struct.bo.IS1Bo;
import com.rskytech.struct.bo.IS2Bo;
import com.rskytech.struct.bo.IS3Bo;
import com.rskytech.struct.bo.IS4Bo;
import com.rskytech.struct.bo.IS5Bo;
import com.rskytech.struct.bo.IS6Bo;
import com.rskytech.struct.bo.ISsiStepBo;
import com.rskytech.struct.bo.ISyBo;
import com.rskytech.task.bo.ITaskMsgBo;
import com.rskytech.task.bo.ITaskMsgDetailBo;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class S1Action extends BaseAction {
	private static final long serialVersionUID = 2879127245175858960L;
	private String ssiId;
	private String siCode;
	private String verifyStr;
	private String siTitle;
	private int isMaintain;
	private int[] step;
	private IS1Bo s1Bo;
	private String id;
	private IS2Bo s2Bo;
	private String s2Id;
	private String s1Remark;
	private String content;
	private String defaultEff;
	private String ssiEff;
	private IS4Bo s4Bo;
	private Integer welcomeIsHaveS45;
	private IComAreaBo comAreaBo;
	private String isSsiChoosed;
	private ISsiStepBo ssiStepBo;
	private IComCoordinationBo comCoordinationBo;
	private IZa1Bo za1Bo;
	private ITaskMsgDetailBo taskMsgDetailBo;
	private IZa7Dao za7Dao;
	private ITaskMsgBo taskMsgBo;
	private String modifiedJson;
	private IS3Bo s3Bo;
	private IS5Bo s5Bo;
	private IS6Bo s6Bo;
	private ISyBo syBo;
	
	
	public String initS1() {
		ComUser thisUser = getSysUser();
		if (thisUser == null){
			return SUCCESS;//现在返回success都是直接跳转jsp,我们共同的jsp中存在session判断与提示
		}
		SMain sMain =null;
		if(ssiId!=null||"".equals(ssiId)||"null".equals(ssiId)||"undefined".equals(ssiId)){
			sMain = (SMain) s1Bo.loadById(SMain.class, ssiId);
		}else{
			isSsiChoosed = "1";
			return "welcome";
		}
		if(sMain!=null){
			int count=0;
			String[] S45 = {"S4A", "S4B", "S5A", "S5B","SYA", "SYB"};
			this.ssiId = sMain.getSsiId();
			List<SStep> step1 = s1Bo.getSstepBySssiId(sMain.getSsiId());
			if(step1.size()!=0){
				if(step1.get(0).getS1()==1&& step1.get(0).getS2()==1){
					if(step1.get(0).getS3()==3){
						if(step1.get(0).getS4aIn()==3){
							if(step1.get(0).getS4bIn()==3){
								if(step1.get(0).getS4aOut()==3){
									if(step1.get(0).getS4bOut()==3){
										
									}else if(step1.get(0).getS4bOut()==0){
										step1.get(0).setS4bOut(2);
									}
								}else if(step1.get(0).getS4aOut()==0){
									step1.get(0).setS4aOut(2);
								}
							}else if(step1.get(0).getS4bIn()==0){
								step1.get(0).setS4bIn(2);
							}
						}else if(step1.get(0).getS4aIn()==0){
							step1.get(0).setS4aIn(2);
						}
					}else if(step1.get(0).getS3()==0){
						step1.get(0).setS3(2);
					}
					for (int j = 0; j < S45.length; j++) {
						String step = S45[j];
						Integer	flag =  s4Bo.getCusStep(step,this.getComModelSeries().getModelSeriesId());
						if(flag == null || flag == 0){
							count++;
						}
					}
					if(count==0){
						s1Bo.saveOrUpdate(step1.get(0), ComacConstants.DB_UPDATE, getSysUser().getUserId());
					}	
				}
			}
			if(sMain.getComAta()!=null){
				ComAta comAta = sMain.getComAta();
				this.siCode=comAta.getAtaCode();
				this.siTitle=JavaScriptUtils.javaScriptEscape(comAta.getAtaName());
			}else{
				this.siCode=sMain.getAddCode();
				this.siTitle=JavaScriptUtils.javaScriptEscape(sMain.getAddName());
			}
			
			getS1Remark(sMain.getSsiId());
			String state = ComacConstants.DB_INSERT;
			SStep sStep = null;
			if (step1.size() == 0) {
				sMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
				sStep = new SStep();
				//初始化step的数据
				int[] initStep = { 1, 2, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,3,3,3};
				sStep.setSMain(sMain);
				sStep.setS1(2);
				sStep.setS2(0);
				sStep.setS3(3);
				sStep.setS4aIn(3);
				sStep.setS4aOut(3);
				sStep.setS4bIn(3);
				sStep.setS4bOut(3);
				sStep.setS5aIn(3);
				sStep.setS5aOut(3);
				sStep.setS5bIn(3);
				sStep.setS5bOut(3);
				sStep.setS6In(3);
				sStep.setS6Out(3);
				sStep.setS7(3);
				sStep.setS8(3);
				sStep.setSyaIn(3);
				sStep.setSyaOut(3);
				sStep.setSybIn(3);
				sStep.setSybOut(3);
				step = initStep;
				for (int j = 0; j < S45.length; j++) {
					String steps = S45[j];
					Integer	flag =  s4Bo.getCusStep(steps,this.getComModelSeries().getModelSeriesId());
					if(flag == null || flag == 0){
						count++;
					}
				}
				if(count==0){
					s1Bo.saveOrUpdate(sStep, state, getSysUser().getUserId());
				}	
			} else {
				step = ssiStepBo.initStep(ssiId, step1.get(0),"S1");
			}
			if(count>0){  ///当自定义S45  矩阵没有维护时
				welcomeIsHaveS45 = 2;  ///s45自定义矩阵没有维护 拦截
				return "welcome";
			}
			s1Bo.saveOrUpdate(sMain, ComacConstants.DB_UPDATE, getSysUser().getUserId());
			Object[] obj=sMain.getS1s().toArray();
			if(obj.length==0){
				ssiEff=getComModelSeries().getModelSeriesName();//结构有效性
			}else{
				ssiEff=sMain.getEffectiveness();
			}
			if(ComacConstants.ANALYZE_STATUS_APPROVED.equals(sMain.getStatus())||ComacConstants.ANALYZE_STATUS_HOLD.equals(sMain.getStatus())){
				//状态为 审批完成时只有查看权限
				isMaintain = ComacConstants.NO;
			}
		}
		return SUCCESS;
	}
	
	public String getS1Remark(String ssiId){
		SRemark sRmaker =null;
		sRmaker = s1Bo.getRemarkBySssi(ssiId);
		if (sRmaker != null) {
			List listJson = new ArrayList<HashMap>();
			HashMap jsonFeildList = null;
			JSONArray json = new JSONArray();
			if (sRmaker != null) {
					s1Remark=JavaScriptUtils.javaScriptEscape(wipeNull(sRmaker.getS1Remark()));
					jsonFeildList= new HashMap();
					jsonFeildList.put("remark",wipeNull(sRmaker.getS1Remark()));
					listJson.add(jsonFeildList);
				json.addAll(listJson);
			}
			writeToResponse(json.toString());
			return null;
		}
		if (sRmaker == null) {
			sRmaker=new SRemark();
			sRmaker.setSMain((SMain)s1Bo.loadById(SMain.class,ssiId));
			s1Bo.saveOrUpdate(sRmaker, ComacConstants.DB_INSERT, getSysUser().getUserId());
		}
		return null;
		
	}
	
	public String getS1Record() {
		List<HashMap> listJson = new ArrayList<HashMap>();
		JSONObject json = new JSONObject();
		HashMap jsonFieldList = null;
		this.getPage().setPageSize(getLimit());
		this.getPage().setStartIndex(getStart());
		List<S1> list = s1Bo.getS1Records(ssiId);
		if (list != null) {
			for (S1 s1 : list) {
				jsonFieldList = new HashMap();
				jsonFieldList.put("id", s1.getS1Id());
				jsonFieldList.put("ssiName", wipeNull(s1.getSsiForm()));
				jsonFieldList.put("material", wipeNull(s1.getMaterial()));
				jsonFieldList.put("surface", wipeNull(s1.getSurface()));
				jsonFieldList.put("ownArea",comAreaBo.getAreaCodeByAreaId(s1.getOwnArea()));
				jsonFieldList.put("isMetal", s1.getIsMetal());
				jsonFieldList.put("internal", s1.getInternal());
				jsonFieldList.put("outernal", s1.getOuternal());
				jsonFieldList.put("designPri", s1.getDesignPri());
				jsonFieldList.put("isFD", s1.getIsFD());
				jsonFieldList.put("repairPassageway", s1.getRepairPassageway());
				listJson.add(jsonFieldList);
			}
		}
		json.element("s1", listJson);
		json.element("totleCount", this.getPage().getTotalCount());
		writeToResponse(json.toString());
		return null;
	}
	
	
	public String saveS1Records() {
		ArrayList<String> array = s1Bo.saveS1(ssiId, s1Remark, jsonData, defaultEff, 
								this.getSysUser(), this.getComModelSeries().getModelSeriesId(),modifiedJson);
		if(array.size()>0){
			for(String areaId : array){
				String[] arr = areaId.split(",");
				for(String string : arr){
					taskMsgDetailBo.updateZa7Status(getComModelSeries().getModelSeriesId(), getSysUser().getUserId(),string);
				}
			}
			za7Dao.cleanTaskInterval(getComModelSeries().getModelSeriesId());
		}
		this.ssiStepBo.changeStatus(ssiId);
		JSONObject json= new JSONObject();
		json.element("success", "true");
		writeToResponse(json.toString());
		return null;
	}

	public String initS2() {
		ComUser thisUser = getSysUser();
		if (thisUser == null){
			return SUCCESS;//现在返回success都是直接跳转jsp,我们共同的jsp中存在session判断与提示
		}
		SMain sMain=(SMain)s1Bo.loadById(SMain.class,ssiId);
		if(sMain.getComAta()!=null){
			ComAta comAta = sMain.getComAta();
			this.siCode=comAta.getAtaCode();
			this.siTitle=JavaScriptUtils.javaScriptEscape(comAta.getAtaName());
		}else{
			this.siCode=sMain.getAddCode();
			this.siTitle=JavaScriptUtils.javaScriptEscape(sMain.getAddName());
		}

		List<SStep> step1 = s1Bo.getSstepBySssiId(ssiId);
		List<S2> s2List = s2Bo.getS2BySssId(ssiId);
		if (s2List != null) {
			for (S2 s2 : s2List) {
				s2Id = s2.getPicId();
				content = s2.getPicContent();
			}

		}
		step = this.ssiStepBo.initStep(ssiId, step1.get(0),"S2");

		return "s2";
	}
	
	/**
	 * 删除S1
	 * @return
	 */
	public String delS1Record(){
		ArrayList<String> array = s1Bo.deleteS1Record(id,this.getComModelSeries().getModelSeriesId(),ssiId);
		if(array.size()>0){
			for(String areaId : array){
				String[] arr = areaId.split(",");
				for(String string : arr){
					taskMsgDetailBo.updateZa7Status(getComModelSeries().getModelSeriesId(), getSysUser().getUserId(),string);
				}
			}
			za7Dao.cleanTaskInterval(getComModelSeries().getModelSeriesId());
		}
		List<S1> list = s1Bo.getS1Records(ssiId);
		if(list==null||list.isEmpty()){
			this.s1Bo.deleteAnalysisData(ssiId,getComModelSeries().getModelSeriesId(),getSysUser().getUserId());
			return null;
		}else{
			updateDelStep(list,ssiId);
		}
		this.ssiStepBo.changeStatus(ssiId);
		return null;
	}
	
	private void updateDelStep(List<S1> list,String ssiId){
		int in = 0;
		int out = 0;
		int nonmetalIn = 0;
		int nonmetalOut = 0;
		int metalOut =0;
		int metalIn =0;
		int metalDamage = 0;
		for(S1 s1 : list){
			if(s1.getIsMetal() == 1){//是金属
				if(s1.getDesignPri()==2&&s1.getIsFD()==1){
					metalDamage++;
				}
				if(s1.getInternal() == 1){
					metalIn++;
				}
				if(s1.getOuternal() == 1){
					metalOut++;
				}
			}else{
				if(s1.getInternal() == 1){
					nonmetalIn++;
				}
				if(s1.getOuternal() == 1){
					nonmetalOut++;
				}
			}
			//属于内部
			if(s1.getInternal() == 1){
				in++;
			}
			//属于外部
			if(s1.getOuternal() == 1){
				out++;
			}
		}
		SStep step3 = s1Bo.getSstepBySssiId(ssiId).get(0);
		SMain sMain = (SMain) this.s1Bo.loadById(SMain.class, ssiId);
		if(metalOut==0){
			step3.setS4aOut(3);
			step3.setSyaOut(3);
			step3.setS5aOut(3);
		}else{
			List<S1> s1List=s4Bo.getS1(ssiId,1,1);
			S4 s4;
			int s4Size=0;
			for(S1 s1:s1List){ 
				s4=s4Bo.isExistForS4(ssiId, s1.getS1Id(), "OUT");
				if(s4!=null){
					s4Size++;
				}
			}
			if(s4Size==metalOut){
				step3.setS4aOut(1);
			}
			
			List<S1> s1Listy=syBo.getS1(ssiId,1,1);
			Sy sy;
			int sySize=0;
			for(S1 s1:s1Listy){ 
				sy=syBo.isExistForSy(ssiId, s1.getS1Id(), "OUT");
				if(sy!=null){
					sySize++;
				}
			}
			if(sySize==metalOut){
				step3.setSyaOut(1);
			}
			
			List<S1> s1List5=s5Bo.getS1(ssiId,1,1);
			S5 s5;
			int s5Size=0;
			for(S1 s1:s1List5){ 
				s5=s5Bo.isExistForS5(ssiId, s1.getS1Id(), "OUT");
				if(s5!=null){
					s5Size++;
				}
			}
			if(s5Size==metalOut){
				step3.setS5aOut(1);
			}
		}
		
		if(metalIn==0){
			step3.setS4aIn(3);
			step3.setSyaIn(3);
			step3.setS5aIn(3);
		}else{
			List<S1> s1List=s4Bo.getS1(ssiId,1,0);
			S4 s4;
			int s4Size=0;
			for(S1 s1:s1List){ 
				s4=s4Bo.isExistForS4(ssiId, s1.getS1Id(), "IN");
				if(s4!=null){
					s4Size++;
				}
			}
			if(s4Size==metalIn){
				step3.setS4aIn(1);
			}
			
			List<S1> s1Listy=syBo.getS1(ssiId,1,0);
			Sy sy;
			int sySize=0;
			for(S1 s1:s1Listy){ 
				sy=syBo.isExistForSy(ssiId, s1.getS1Id(), "IN");
				if(sy!=null){
					sySize++;
				}
			}
			if(sySize==metalIn){
				step3.setSyaIn(1);
			}
			
			List<S1> s1List5=s5Bo.getS1(ssiId,1,0);
			S5 s5;
			int s5Size=0;
			for(S1 s1:s1List5){ 
				s5=s5Bo.isExistForS5(ssiId, s1.getS1Id(), "IN");
				if(s5!=null){
					s5Size++;
				}
			}
			if(s5Size==metalIn){
				step3.setS5aIn(1);
			}
		}
		if(nonmetalOut==0){
			step3.setS4bOut(3);
			step3.setSybOut(3);
			step3.setS5bOut(3);
		}else{
			List<S1> s1List=s4Bo.getS1(ssiId,0,1);
			S4 s4;
			int s4Size=0;
			for(S1 s1:s1List){ 
				s4=s4Bo.isExistForS4(ssiId, s1.getS1Id(), "OUT");
				if(s4!=null){
					s4Size++;
				}
			}
			if(s4Size==nonmetalOut){
				step3.setS4bOut(1);
			}
			
			List<S1> s1Listy=syBo.getS1(ssiId,0,1);
			Sy sy;
			int sySize=0;
			for(S1 s1:s1Listy){ 
				sy=syBo.isExistForSy(ssiId, s1.getS1Id(), "OUT");
				if(sy!=null){
					sySize++;
				}
			}
			if(sySize==nonmetalOut){
				step3.setSybOut(1);
			}
			
			List<S1> s1List5=s5Bo.getS1(ssiId,0,1);
			S5 s5;
			int s5Size=0;
			for(S1 s1:s1List5){ 
				s5=s5Bo.isExistForS5(ssiId, s1.getS1Id(), "OUT");
				if(s5!=null){
					s5Size++;
				}
			}
			if(s5Size==nonmetalOut){
				step3.setS5bOut(1);
			}
		}
		if(nonmetalIn==0){
			step3.setS4bIn(3);
			step3.setSybIn(3);
			step3.setS5bIn(3);
		}else{
			List<S1> s1List=s4Bo.getS1(ssiId,0,0);
			S4 s4;
			int s4Size=0;
			for(S1 s1:s1List){ 
				s4=s4Bo.isExistForS4(ssiId, s1.getS1Id(), "IN");
				if(s4!=null){
					s4Size++;
				}
			}
			if(s4Size==nonmetalIn){
				step3.setS4bIn(1);
			}
			
			List<S1> s1Listy=syBo.getS1(ssiId,0,0);
			Sy sy;
			int sySize=0;
			for(S1 s1:s1Listy){ 
				sy=syBo.isExistForSy(ssiId, s1.getS1Id(), "IN");
				if(sy!=null){
					sySize++;
				}
			}
			if(sySize==nonmetalIn){
				step3.setSybIn(1);
			}
			
			List<S1> s1List5=s5Bo.getS1(ssiId,0,0);
			S5 s5;
			int s5Size=0;
			for(S1 s1:s1List5){ 
				s5=s5Bo.isExistForS5(ssiId, s1.getS1Id(), "IN");
				if(s5!=null){
					s5Size++;
				}
			}
			if(s5Size==nonmetalIn){
				step3.setS5bIn(1);
			}
		}
		if(metalDamage==0){
			step3.setS3(3);
		}else{
			int s3Size=0;
			List s3List  = this.s3Bo.getS3Records(ssiId);
			if(s3List!=null){
				s3Size = s3List.size();
			}
			int setS3 = sMain.getS3s().size();
			if(s3Size==setS3){
				step3.setS3(1);
			}
		}
		if(in==0){
			step3.setS6In(3);
		}else{
			List<S1> listS1 = s6Bo.getS1IdBySssiId(ssiId, ComacConstants.INNER);
			List<Object[]> s6eaList = s6Bo.getS6EaRecords(ssiId, "in");
			if(s6eaList!=null&&listS1!=null&&s6eaList.size()==listS1.size()){
				if (step3.getS6In() == 2&&step3.getS4aIn()==1&&step3.getS4bIn()==1
						&&step3.getSyaIn()==1&&step3.getSybIn()==1
						&&step3.getS5aIn()==1&&step3.getS5bIn()==1) {
					step3.setS6In(1);
				}
			}
		}
		if(out==0){
			step3.setS6Out(3);
		}else{
			List<S1> listS1 = s6Bo.getS1IdBySssiId(ssiId, ComacConstants.OUTTER);
			List<Object[]> s6eaList = s6Bo.getS6EaRecords(ssiId, "out");
			if(s6eaList!=null&&listS1!=null&&s6eaList.size()==listS1.size()){
				if (step3.getS6Out() == 2&&step3.getS4aOut()==1&&step3.getS4bOut()==1
						&&step3.getSyaOut()==1&&step3.getSybOut()==1
						&&step3.getS5aOut()==1&&step3.getS5bOut()==1) {
					step3.setS6Out(1);
				}
			}
		}
		if(step3.getS2()==1){
			if(step3.getS4aIn()==0){
				step3.setS4aIn(2);
				step3.setSyaIn(0);
				step3.setS5aIn(0);
			}else if(step3.getS4bIn()==0){
				step3.setS4bIn(2);
				step3.setSybIn(0);
				step3.setS5bIn(0);
			}else if(step3.getS4aOut()==0){
				step3.setS4aOut(2);
				step3.setSyaOut(0);
				step3.setS5aOut(0);
			}else if(step3.getS5bOut()==0){
				step3.setS4bOut(2);
				step3.setSybOut(0);
				step3.setS5bOut(0);
			}
		}
		s1Bo.update(step3, this.getSysUser().getUserId());
	}
	
	/**
	 * 验证S1区域是否存在并能修改
	 * @return
	 */
	public String verifyS1Area(){
		if(id!=null&&!"".equals(id)){
			List<TaskMsg> taskList = taskMsgBo.getTempTaskMsgByS1Id(id,null,null);
			if(taskList!=null&&taskList.size()>0){
				writeToResponse("{modify:'no'}");
				return null;
			}
		}
		String[] str=verifyStr.split(",");
		int count = 0;
		for(int i=0;i<str.length;i++){
			for(int j=i+1;j<str.length;j++){
				if(str[i].equals(str[j])){
					count++;
				}
				if(count>0){
					if(str[i].equals("0")){
						str[i]="'0'";
					}
					writeToResponse("{exists:"+str[i]+"}");
					return null;
				}
			}
		}
		for(String areaCode:str){
			ComArea comArea = comAreaBo.getComAreaByAreaCode(areaCode,getComModelSeries().getModelSeriesId());
			if(areaCode.equals("0")){
				areaCode="'0'";
			}
			if(comArea==null){
				writeToResponse("{unExists:"+areaCode+"}");
				return null;
			}else{
				ZaMain zaMain = za1Bo.selectZaMain(this.getComModelSeries(), comArea);
				if(zaMain!=null){
					if(zaMain.getStatus().equals(ComacConstants.ANALYZE_STATUS_APPROVED)||zaMain.getStatus().equals(ComacConstants.ANALYZE_STATUS_HOLD)){
						writeToResponse("{success:"+areaCode+"}");
						return null;
					}
				}
			}
			
		}
		return null;
	}
	
	/**
	 * 验证区域是否存在
	 * @return
	 */
	public String verifyArea(){
		
		String[] str=verifyStr.split(",");
		int count = 0;
		for(int i=0;i<str.length;i++){
			for(int j=i+1;j<str.length;j++){
				if(str[i].equals(str[j])){
					count++;
				}
				if(count>0){
					if(str[i].equals("0")){
						str[i]="'0'";
					}
					writeToResponse("{exists:"+str[i]+"}");
					return null;
				}
			}
		}
		for(String areaCode:str){
			ComArea comArea = comAreaBo.getComAreaByAreaCode(areaCode,getComModelSeries().getModelSeriesId());
			if(areaCode.equals("0")){
				areaCode="'0'";
			}
			if(comArea==null){
				writeToResponse("{unExists:"+areaCode+"}");
				return null;
			}else{
				ZaMain zaMain = za1Bo.selectZaMain(this.getComModelSeries(), comArea);
				if(zaMain!=null){
					if(zaMain.getStatus().equals(ComacConstants.ANALYZE_STATUS_APPROVED)||zaMain.getStatus().equals(ComacConstants.ANALYZE_STATUS_HOLD)){
						writeToResponse("{success:"+areaCode+"}");
						return null;
					}
				}
			}
			
		}
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
	public String getSsiId() {
		return ssiId;
	}

	public void setSsiId(String ssiId) {
		this.ssiId = ssiId;
	}

	public String getSiCode() {
		return siCode;
	}

	public void setSiCode(String siCode) {
		this.siCode = siCode;
	}

	public String getVerifyStr() {
		return verifyStr;
	}

	public void setVerifyStr(String verifyStr) {
		this.verifyStr = verifyStr;
	}

	public String getSiTitle() {
		return siTitle;
	}

	public void setSiTitle(String siTitle) {
		this.siTitle = siTitle;
	}

	public int getIsMaintain() {
		return isMaintain;
	}

	public void setIsMaintain(int isMaintain) {
		this.isMaintain = isMaintain;
	}

	public int[] getStep() {
		return step;
	}

	public void setStep(int[] step) {
		this.step = step;
	}

	public IS1Bo getS1Bo() {
		return s1Bo;
	}

	public void setS1Bo(IS1Bo s1Bo) {
		this.s1Bo = s1Bo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public IS2Bo getS2Bo() {
		return s2Bo;
	}

	public void setS2Bo(IS2Bo s2Bo) {
		this.s2Bo = s2Bo;
	}

	public String getS2Id() {
		return s2Id;
	}

	public void setS2Id(String s2Id) {
		this.s2Id = s2Id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDefaultEff() {
		return defaultEff;
	}

	public void setDefaultEff(String defaultEff) {
		this.defaultEff = defaultEff;
	}

	public IS4Bo getS4Bo() {
		return s4Bo;
	}

	public void setS4Bo(IS4Bo s4Bo) {
		this.s4Bo = s4Bo;
	}

	public Integer getWelcomeIsHaveS45() {
		return welcomeIsHaveS45;
	}

	public void setWelcomeIsHaveS45(Integer welcomeIsHaveS45) {
		this.welcomeIsHaveS45 = welcomeIsHaveS45;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}

	public void setS1Remark(String s1Remark) {
		this.s1Remark = s1Remark;
	}

	public String getS1Remark() {
		return s1Remark;
	}

	public String getSsiEff() {
		return ssiEff;
	}

	public void setSsiEff(String ssiEff) {
		this.ssiEff = ssiEff;
	}

	public String getIsSsiChoosed() {
		return isSsiChoosed;
	}

	public void setIsSsiChoosed(String isSsiChoosed) {
		this.isSsiChoosed = isSsiChoosed;
	}

	public ISsiStepBo getSsiStepBo() {
		return ssiStepBo;
	}

	public void setSsiStepBo(ISsiStepBo ssiStepBo) {
		this.ssiStepBo = ssiStepBo;
	}

	public IComCoordinationBo getComCoordinationBo() {
		return comCoordinationBo;
	}

	public void setComCoordinationBo(IComCoordinationBo comCoordinationBo) {
		this.comCoordinationBo = comCoordinationBo;
	}

	public IZa1Bo getZa1Bo() {
		return za1Bo;
	}

	public void setZa1Bo(IZa1Bo za1Bo) {
		this.za1Bo = za1Bo;
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

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}

	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}

	public String getModifiedJson() {
		return modifiedJson;
	}

	public void setModifiedJson(String modifiedJson) {
		this.modifiedJson = modifiedJson;
	}

	public IS3Bo getS3Bo() {
		return s3Bo;
	}

	public void setS3Bo(IS3Bo s3Bo) {
		this.s3Bo = s3Bo;
	}

	public IS5Bo getS5Bo() {
		return s5Bo;
	}

	public void setS5Bo(IS5Bo s5Bo) {
		this.s5Bo = s5Bo;
	}

	public IS6Bo getS6Bo() {
		return s6Bo;
	}

	public void setS6Bo(IS6Bo s6Bo) {
		this.s6Bo = s6Bo;
	}

	public ISyBo getSyBo() {
		return syBo;
	}

	public void setSyBo(ISyBo syBo) {
		this.syBo = syBo;
	}

}
