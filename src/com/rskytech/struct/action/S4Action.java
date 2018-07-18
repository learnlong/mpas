package com.rskytech.struct.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.util.JavaScriptUtils;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.S4;
import com.rskytech.pojo.S6Ea;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.SRemark;
import com.rskytech.pojo.SStep;
import com.rskytech.pojo.Sy;
import com.rskytech.struct.bo.IS1Bo;
import com.rskytech.struct.bo.IS4Bo;
import com.rskytech.struct.bo.IS6Bo;
import com.rskytech.struct.bo.ISsiStepBo;
import com.rskytech.struct.bo.ISyBo;
@SuppressWarnings({ "unchecked", "rawtypes" })
public class S4Action extends BaseAction{
	private static final long serialVersionUID = 6159133361847655242L;
	private IS4Bo s4Bo;
	private String matrix;
	private String siCode;
	private int ssiLength;//ssi数据长度
	private String siTitle;
	private IS1Bo s1Bo;
	private String ssiId;
	private String defaultEff;
	private String remark;
	private String inorOut;
	private int inOrOut;
	private String aOrb;
	private int isMetal;
	private int[] step;
	private int levelCount;
	private int isMaintain;
	private String arrayValue;
	private String s4IdArray;
	private ISsiStepBo ssiStepBo;
	private ISyBo syBo;
	private IS6Bo s6Bo;
	
	public String initS4(){//s4a内
		aOrb="A";
		ComUser thisUser = getSysUser();
		if (thisUser == null){
			return SUCCESS;//现在返回success都是直接跳转jsp,我们共同的jsp中存在session判断与提示
		
		}
		List list=null;
		S4 s4=null;
		List<String> s4Id=null;
		SRemark sRmaker = s1Bo.getRemarkBySssi(ssiId);
		if (sRmaker != null) {
			this.remark = sRmaker.getS4aInRemark(); 
		}
		SMain sMain=(SMain)s4Bo.loadById(SMain.class, ssiId);
		defaultEff=sMain.getEffectiveness();
		if(defaultEff==null){
			defaultEff=getComModelSeries().getModelSeriesName();
		}
		if(sMain.getComAta()!=null){
			ComAta comAta = sMain.getComAta();
			this.siCode=comAta.getAtaCode();
			this.siTitle=JavaScriptUtils.javaScriptEscape(comAta.getAtaName());
		}else{
			this.siCode=sMain.getAddCode();
			this.siTitle=JavaScriptUtils.javaScriptEscape(sMain.getAddName());
		}
		List<S1> s1List=s4Bo.getS1(ssiId,isMetal,inOrOut);
		SMain sMain1=new SMain();
		s4Id=new ArrayList<String>();
		list=new ArrayList();
		for(S1 s1:s1List){ 
			s4=s4Bo.isExistForS4(ssiId, s1.getS1Id(), "IN");
			if(s4==null){
				s4=new S4();
				sMain1.setSsiId(ssiId);
				s4.setS1(s1);
				s4.setSMain(sMain1);
				s4.setInOrOut(ComacConstants.INNER);
				s4Bo.saveOrUpdate(s4, ComacConstants.DB_INSERT, getSysUser().getUserId());
				s4Id.add(s4.getS4Id());
				list.add(s4.getSelect1());
				list.add(s4.getSelect2());
				list.add(s4.getSelect3());
				list.add(s4.getSelect4());
				list.add(s4.getSelect5());
				list.add(s4.getSelect6());
				list.add(s4.getSelect7());
				list.add(s4.getSelect8());
				list.add(s4.getSelect9());
				list.add(s4.getSelect10());
				list.add(s4.getSelect11());
				list.add(s4.getSelect12());
				list.add(s4.getSelect13());
				list.add(s4.getSelect14());
				list.add(s4.getSelect15());
				list.add(s4.getSelect16());
				list.add(s4.getSelect17());
				list.add(s4.getSelect18());
				list.add(s4.getSelect19());
				list.add(s4.getSelect20());
				list.add(s4.getSelect21());
				list.add(s4.getSelect22());
				list.add(s4.getSelect23());
				list.add(s4.getSelect24());
				list.add(s4.getGdr());
			}else{
				s4Id.add(s4.getS4Id());
				list.add(s4.getSelect1());
				list.add(s4.getSelect2());
				list.add(s4.getSelect3());
				list.add(s4.getSelect4());
				list.add(s4.getSelect5());
				list.add(s4.getSelect6());
				list.add(s4.getSelect7());
				list.add(s4.getSelect8());
				list.add(s4.getSelect9());
				list.add(s4.getSelect10());
				list.add(s4.getSelect11());
				list.add(s4.getSelect12());
				list.add(s4.getSelect13());
				list.add(s4.getSelect14());
				list.add(s4.getSelect15());
				list.add(s4.getSelect16());
				list.add(s4.getSelect17());
				list.add(s4.getSelect18());
				list.add(s4.getSelect19());
				list.add(s4.getSelect20());
				list.add(s4.getSelect21());
				list.add(s4.getSelect22());
				list.add(s4.getSelect23()+"");
				list.add(s4.getSelect24()+"");
				list.add(s4.getGdr()+"");
				
			}
		}
		
		arrayValue= list.toString();
		s4IdArray=s4Id.toString();
		List<SStep> step1=s1Bo.getSstepBySssiId(ssiId);
		step=ssiStepBo.initStep(ssiId, step1.get(0),"S4AIN");
		matrix=getS4InMatrix("S4A").replaceAll("\r\n", "<br/>");
		return SUCCESS;
	
	}
	public String initS4aOut(){
		aOrb="A";
		ComUser thisUser = getSysUser();
		if (thisUser == null){
			return SUCCESS;//现在返回success都是直接跳转jsp,我们共同的jsp中存在session判断与提示
		
		}
		SRemark sRmaker = s1Bo.getRemarkBySssi(ssiId);
		if (sRmaker != null) {
			this.remark = sRmaker.getS4aOutRemark(); 
		}
		List list=null;
		List s4Id=null;
		S4 s4=null;
		SMain sMain=(SMain)s4Bo.loadById(SMain.class, ssiId);
		defaultEff=sMain.getEffectiveness();
		if(defaultEff==null){
			defaultEff="";
		}
		if(sMain.getComAta()!=null){
			ComAta comAta = sMain.getComAta();
			this.siCode=comAta.getAtaCode();
			this.siTitle=JavaScriptUtils.javaScriptEscape(comAta.getAtaName());
		}else{
			this.siCode=sMain.getAddCode();
			this.siTitle=JavaScriptUtils.javaScriptEscape(sMain.getAddName());
		}
		SMain sMain1=new SMain();
		s4Id=new ArrayList();
		list=new ArrayList();
		List<S1> s1List=s4Bo.getS1(ssiId,isMetal,inOrOut);
		for(S1 s1:s1List){ 
			s4=s4Bo.isExistForS4(ssiId, s1.getS1Id(), "OUT");
			if(s4==null){
				s4=new S4();
				sMain1.setSsiId(ssiId);
				s4.setS1(s1);
				s4.setSMain(sMain1);
				s4.setInOrOut(ComacConstants.OUTTER);
				s4Bo.saveOrUpdate(s4, ComacConstants.DB_INSERT, getSysUser().getUserId());
				s4Id.add(s4.getS4Id());
				list.add(s4.getSelect1());
				list.add(s4.getSelect2());
				list.add(s4.getSelect3());
				list.add(s4.getSelect4());
				list.add(s4.getSelect5());
				list.add(s4.getSelect6());
				list.add(s4.getSelect7());
				list.add(s4.getSelect8());
				list.add(s4.getSelect9());
				list.add(s4.getSelect10());
				list.add(s4.getSelect11());
				list.add(s4.getSelect12());
				list.add(s4.getSelect13());
				list.add(s4.getSelect14());
				list.add(s4.getSelect15());
				list.add(s4.getSelect16());
				list.add(s4.getSelect17());
				list.add(s4.getSelect18());
				list.add(s4.getSelect19());
				list.add(s4.getSelect20());
				list.add(s4.getSelect21());
				list.add(s4.getSelect22());
				list.add(s4.getSelect23());
				list.add(s4.getSelect24());
				list.add(s4.getGdr());
			}else{
				s4Id.add(s4.getS4Id());
				list.add(s4.getSelect1());
				list.add(s4.getSelect2());
				list.add(s4.getSelect3());
				list.add(s4.getSelect4());
				list.add(s4.getSelect5());
				list.add(s4.getSelect6());
				list.add(s4.getSelect7());
				list.add(s4.getSelect8());
				list.add(s4.getSelect9());
				list.add(s4.getSelect10());
				list.add(s4.getSelect11());
				list.add(s4.getSelect12());
				list.add(s4.getSelect13());
				list.add(s4.getSelect14());
				list.add(s4.getSelect15());
				list.add(s4.getSelect16());
				list.add(s4.getSelect17());
				list.add(s4.getSelect18());
				list.add(s4.getSelect19());
				list.add(s4.getSelect20());
				list.add(s4.getSelect21());
				list.add(s4.getSelect22());
				list.add(s4.getSelect23());
				list.add(s4.getSelect24());
				list.add(s4.getGdr());
				
			}
		}
		arrayValue= list.toString(); 
		s4IdArray=s4Id.toString();
		List<SStep> step1=s1Bo.getSstepBySssiId(ssiId);
		step=ssiStepBo.initStep(ssiId, step1.get(0),"S4AOUT");
		matrix=getS4InMatrix("S4A").replaceAll("\r\n", "<br/>");
		return "s4aout";
	
	}
	
	public String initS4bOut(){
		aOrb="B";
		ComUser thisUser = getSysUser();
		if (thisUser == null){
			return SUCCESS;//现在返回success都是直接跳转jsp,我们共同的jsp中存在session判断与提示
		
		}
		SRemark sRmaker = s1Bo.getRemarkBySssi(ssiId);
		if (sRmaker != null) {
			this.remark = sRmaker.getS4bOutRemark(); 
		}
		List list=null;
		S4 s4=null;
		List s4Id=null;
		SMain sMain=(SMain)s4Bo.loadById(SMain.class, ssiId);
		defaultEff=sMain.getEffectiveness();
		if(defaultEff==null){
			defaultEff="";
		}
		if(sMain.getComAta()!=null){
			ComAta comAta = sMain.getComAta();
			this.siCode=comAta.getAtaCode();
			this.siTitle=JavaScriptUtils.javaScriptEscape(comAta.getAtaName());
		}else{
			this.siCode=sMain.getAddCode();
			this.siTitle=JavaScriptUtils.javaScriptEscape(sMain.getAddName());
		}
		List<S1> s1List=s4Bo.getS1(ssiId,isMetal,inOrOut);
		SMain sMain1=new SMain();
		s4Id=new ArrayList();
		list=new ArrayList();
		for(S1 s1:s1List){ 
			s4=s4Bo.isExistForS4(ssiId, s1.getS1Id(), "OUT");
			if(s4==null){
				s4=new S4();
				sMain1.setSsiId(ssiId);
				s4.setS1(s1);
				s4.setSMain(sMain1);
				s4.setInOrOut(ComacConstants.OUTTER);
				s4Bo.saveOrUpdate(s4, ComacConstants.DB_INSERT, getSysUser().getUserId());
				s4Id.add(s4.getS4Id());
				list.add(s4.getSelect1());
				list.add(s4.getSelect2());
				list.add(s4.getSelect3());
				list.add(s4.getSelect4());
				list.add(s4.getSelect5());
				list.add(s4.getSelect6());
				list.add(s4.getSelect7());
				list.add(s4.getSelect8());
				list.add(s4.getSelect9());
				list.add(s4.getSelect10());
				list.add(s4.getSelect11());
				list.add(s4.getSelect12());
				list.add(s4.getSelect13());
				list.add(s4.getSelect14());
				list.add(s4.getSelect15());
				list.add(s4.getSelect16());
				list.add(s4.getSelect17());
				list.add(s4.getSelect18());
				list.add(s4.getSelect19());
				list.add(s4.getSelect20());
				list.add(s4.getSelect21());
				list.add(s4.getSelect22());
				list.add(s4.getSelect23());
				list.add(s4.getSelect24());
				list.add(s4.getGdr());
			}else{
				s4Id.add(s4.getS4Id());
				list.add(s4.getSelect1());
				list.add(s4.getSelect2());
				list.add(s4.getSelect3());
				list.add(s4.getSelect4());
				list.add(s4.getSelect5());
				list.add(s4.getSelect6());
				list.add(s4.getSelect7());
				list.add(s4.getSelect8());
				list.add(s4.getSelect9());
				list.add(s4.getSelect10());
				list.add(s4.getSelect11());
				list.add(s4.getSelect12());
				list.add(s4.getSelect13());
				list.add(s4.getSelect14());
				list.add(s4.getSelect15());
				list.add(s4.getSelect16());
				list.add(s4.getSelect17());
				list.add(s4.getSelect18());
				list.add(s4.getSelect19());
				list.add(s4.getSelect20());
				list.add(s4.getSelect21());
				list.add(s4.getSelect22());
				list.add(s4.getSelect23());
				list.add(s4.getSelect24());
				list.add(s4.getGdr());
				
			}
		}
		
		arrayValue= list.toString(); 
		s4IdArray=s4Id.toString();
		List<SStep> step1=s1Bo.getSstepBySssiId(ssiId);
		step=ssiStepBo.initStep(ssiId, step1.get(0),"S4BOUT");
		matrix=getS4InMatrix("S4B").replaceAll("\r\n", "<br/>");
		return "s4bout";
	
	}
	
	public String initS4b(){
		aOrb="B";
		ComUser thisUser = getSysUser();
		if (thisUser == null){
			return SUCCESS;//现在返回success都是直接跳转jsp,我们共同的jsp中存在session判断与提示
		
		}
		SRemark sRmaker = s1Bo.getRemarkBySssi(ssiId);
		if (sRmaker != null) {
			this.remark = sRmaker.getS4bInRemark(); 
		}
		List list=null;
		S4 s4=null;
		List s4Id=null;
		SMain sMain=(SMain)s4Bo.loadById(SMain.class, ssiId);
		defaultEff=sMain.getEffectiveness();
		if(defaultEff==null){
			defaultEff="";
		}
		if(sMain.getComAta()!=null){
			ComAta comAta = sMain.getComAta();
			this.siCode=comAta.getAtaCode();
			this.siTitle=JavaScriptUtils.javaScriptEscape(comAta.getAtaName());
		}else{
			this.siCode=sMain.getAddCode();
			this.siTitle=JavaScriptUtils.javaScriptEscape(sMain.getAddName());
		}
		List<S1> s1List=s4Bo.getS1(ssiId,isMetal,inOrOut);
		SMain sMain1=new SMain();
		s4Id=new ArrayList();
		list=new ArrayList();
		for(S1 s1:s1List){ 
			s4=s4Bo.isExistForS4(ssiId, s1.getS1Id(), "IN");
			if(s4==null){
				s4=new S4();
				sMain1.setSsiId(ssiId);
				s4.setS1(s1);
				s4.setSMain(sMain1);
				s4.setInOrOut(ComacConstants.INNER);
				s4Bo.saveOrUpdate(s4, ComacConstants.DB_INSERT, getSysUser().getUserId());
				s4Id.add(s4.getS4Id());
				list.add(s4.getSelect1());
				list.add(s4.getSelect2());
				list.add(s4.getSelect3());
				list.add(s4.getSelect4());
				list.add(s4.getSelect5());
				list.add(s4.getSelect6());
				list.add(s4.getSelect7());
				list.add(s4.getSelect8());
				list.add(s4.getSelect9());
				list.add(s4.getSelect10());
				list.add(s4.getSelect11());
				list.add(s4.getSelect12());
				list.add(s4.getSelect13());
				list.add(s4.getSelect14());
				list.add(s4.getSelect15());
				list.add(s4.getSelect16());
				list.add(s4.getSelect17());
				list.add(s4.getSelect18());
				list.add(s4.getSelect19());
				list.add(s4.getSelect20());
				list.add(s4.getSelect21());
				list.add(s4.getSelect22());
				list.add(s4.getSelect23());
				list.add(s4.getSelect24());
				list.add(s4.getGdr());
			}else{
				s4Id.add(s4.getS4Id());
				list.add(s4.getSelect1());
				list.add(s4.getSelect2());
				list.add(s4.getSelect3());
				list.add(s4.getSelect4());
				list.add(s4.getSelect5());
				list.add(s4.getSelect6());
				list.add(s4.getSelect7());
				list.add(s4.getSelect8());
				list.add(s4.getSelect9());
				list.add(s4.getSelect10());
				list.add(s4.getSelect11());
				list.add(s4.getSelect12());
				list.add(s4.getSelect13());
				list.add(s4.getSelect14());
				list.add(s4.getSelect15());
				list.add(s4.getSelect16());
				list.add(s4.getSelect17());
				list.add(s4.getSelect18());
				list.add(s4.getSelect19());
				list.add(s4.getSelect20());
				list.add(s4.getSelect21());
				list.add(s4.getSelect22());
				list.add(s4.getSelect23());
				list.add(s4.getSelect24());
				list.add(s4.getGdr());
				
			}
		}
	//	}
		arrayValue= list.toString(); 
		s4IdArray=s4Id.toString();
		List<SStep> step1=s1Bo.getSstepBySssiId(ssiId);
		step =ssiStepBo.initStep(ssiId, step1.get(0),"S4BIN");
		matrix=getS4InMatrix("S4B").replaceAll("\r\n", "<br/>");
		return "s4bin";
	
	}
	/**
	 * 初始化矩阵表格
	 * @return
	 */
	public String getS4InMatrix(String step){
		String edrAlgorithm=null;
		StringBuffer html= new StringBuffer();
		List<Object[]> titleName=s4Bo.getItemName("0", getComModelSeries().getModelSeriesId(), aOrb);
		List cnNameList=new ArrayList();
		List<String> idList=new ArrayList();
		List<Integer> itemLength=new ArrayList();
		List thisAlgorithm=new ArrayList();
		Integer flag=s4Bo.getCusStep(step,getComModelSeries().getModelSeriesId());
		if(flag==null||flag==0){
			return null;
		}
		//得到所有的数据,放在一个list里面,后面可以直接使用
		if(titleName==null){
			return null;
		}
		/**
		 * 一次性去取出所有的需要数据,放在集合里面.方便取出
		 */
		for(Object[] obj:titleName){
			cnNameList.add(obj[0]);
			idList.add(obj[1].toString());
			thisAlgorithm.add((String) obj[2]);
		
		}
		//返回前台,得知有多少个自定义的一级ID
		levelCount=idList.size();
		//得到所有数据的长度.来初始化备注输入框需要的长度
		for(Object i :idList){
			itemLength.add(s4Bo.getItemCount(i.toString(),getComModelSeries().getModelSeriesId(),aOrb));
		}
		List<S1> s1List=s4Bo.getS1(ssiId,isMetal,inOrOut);
		if(s1List==null){
			return null;
		}
		ssiLength=s1List.size();//获得ssi数据长度
		html.append("<table  border='0' cellpadding='1' cellspacing='0' style='text-align:center; padding:0; margin:0;' >");	
		html.append("<div id='tableDiv' style='overflow:auto;'>");
		html.append("<tr>");
		html.append("<td>");
		html.append("<table width='100%' border='0' cellpadding='1' cellspacing='0' style='text-align:center; padding:0; margin:0;' id='edittable'>");		
		html.append("<tr>");
		html.append("<td mytag=0 width='80px' style='background-color:#DFE8F6' rowspan='2' id ='lRemark'>备注(备注E)：</td>");
		html.append("<td mytag=0 style='background-color:#DFE8F6;text-align:left' colspan='"+getItemsLength(itemLength)+"' rowspan='2'><textarea   cols='60' rows='3'   name='remark'>"+JavaScriptUtils.javaScriptEscape((remark==null?"":remark))+"</textarea></td>");
		html.append("<td mytag=0  width='80px' style='background-color:#DFE8F6' colspan='5' id ='lArea'>区域(区域E)：</td>");
		html.append("<td mytag=0 width='550px' style='background-color:#DFE8F6;text-align:left' colspan='"+getItemsLength(itemLength)+"'><font id='lAreaPrice'>"+(inOrOut==0?"内部":"外部")+"</font><input type='hidden' name='ssiId' value='"+ssiId+"'/> <input type='hidden' name='inorout' value='${inorout }'/><input type='hidden' id='listSize' value='${size }'/><input type='hidden' name='str' id='str'/></td>");
		html.append("</tr>");
		html.append("<tr>");
		html.append("<td mytag=0  width='100px' style='background-color:#DFE8F6' colspan='5' id ='lFeasibility'>适用性(适用性E)：</td>");
		html.append("<td mytag=0 style='background-color:#DFE8F6;text-align:left' colspan='99'>"+defaultEff+"</td>");
		html.append("</tr>");
		html.append("<tr>");
		html.append("</table>");
		html.append("</tr>");
		html.append("</td>");
		html.append("</tr>");
		html.append("<tr>");
		html.append("<td>");
		html.append("<table width='100%' border='0' cellpadding='1' cellspacing='0' style='text-align:center; padding:0; margin:0;' id='edittable1'>");
		html.append("<tr>");
		html.append("<td mytag=0 width='100px' style='background-color:#DFE8F6;'  id ='lAttribute'>属性</td>");
		for(int i=0;i<idList.size();i++){
			html.append("<td mytag=0 colspan='"+itemLength.get(i)+"' style='background-color:#DFE8F6;word-break: keep-all;'>"+cnNameList.get(i)+"</td>");
		}
		if(cnNameList!=null){
			for(int i=0;i<cnNameList.size();i++){
				html.append("<td width='50px' mytag=0 rowspan='2' style='background-color:#DFE8F6;word-break: keep-all;'>&nbsp;&nbsp;"+cnNameList.get(i)+"&nbsp;&nbsp;</td>");
			}
		}
		html.append("<td  mytag=0 rowspan='2' style='background-color:#DFE8F6;word-break: keep-all;'>&nbsp;&nbsp;GDR&nbsp;&nbsp;</td>");
		html.append("</tr>");
		html.append("<tr>");
		html.append("<td mytag=0 width='100px' style='background-color:#DFE8F6' id='lComponent'>组成部分</td>");
		List levelList=new ArrayList();
		List thisId=new ArrayList();
		List<Object[]> itemName=null;
		List<Integer> itemsLength=new ArrayList();
		Map map4=new HashMap();
		Map map = new HashMap();
		Map map1 = new HashMap();//用来区分属于哪一个
		Map map2= new HashMap();//key是第二层的ID value是第一层ID 
		for(int i = 0;i < idList.size();i++){
			itemName=s4Bo.getItemName(idList.get(i),getComModelSeries().getModelSeriesId(),aOrb);
			if(itemName!=null){
				for(Object[] obj:itemName){
					levelList=s4Bo.getLevelCount(((String)obj[1]),getComModelSeries().getModelSeriesId(),aOrb);
					if(levelList==null){
						return null;
					}
					html.append("<td mytag=0 width='auto' colspan="+levelList.size()+" style='background-color:#DFE8F6;'>"+JavaScriptUtils.javaScriptEscape(obj[0].toString())+"</td>");
					thisId.add(obj[1].toString());
					edrAlgorithm=(String) obj[3];
					map.put(obj[1].toString(),thisAlgorithm.get(i));
					map1.put(obj[1].toString(), (char)65+i);
					map2.put(obj[1].toString(), idList.get(i));
				}
				map4.put(idList.get(i), itemName.size());
				itemsLength.add(itemName.size());
			}else{
				return null;
			}
		}
		
		S1 s1 = null;
		if(s1List!=null){
			ssiLength=s1List.size();
			int onIndex=0;
			int index=0;
			int count=0;
			int tempCount=0;
			for(int j=0;j<s1List.size();j++){
				s1=s1List.get(j);
				if(j==1&&j!=0){
					onIndex=25;
					index=25;
					
				}else{
					onIndex=25*j;
					index=25*j;
				}
				if(j>0){
					count=25*j;
					tempCount=count;
				}
				if(j%2!=0){
					html.append("<tr style='background-color:rgb(200,232,240)' id="+s1.getS1Id()+" edralgorithm="+edrAlgorithm+">");
				}else{
					html.append("<tr  id="+s1.getS1Id()+" edralgorithm="+edrAlgorithm+">");
				}
				//得到S1的名称
				html.append("<td style='border-right:1px red solid;' mytag=0>"+(s1.getSsiForm().length()>12?s1.getSsiForm().subSequence(0, 10)+"...":s1.getSsiForm())+"</td>");
				String tempId=null;
					for(int m=0;m<this.getItemsLength(itemsLength);m++){
						//查询出所有的组成部分下面的级别
						if(tempId!=map2.get(thisId.get(m)).toString()){
							count=count+BasicTypeUtils.parseInt(map4.get(map2.get(thisId.get(m))).toString());
							index=onIndex;
						}
						List<Object[]> vr=s4Bo.getLevelCount((String)thisId.get(m),getComModelSeries().getModelSeriesId(),aOrb);
							for(int i =0 ;i<vr.size();i++){
								Object[] obj=(Object[])vr.get(i);
								if(i==vr.size()-1){
									html.append("<td style='border-right:1px red solid;word-break: keep-all;' mytag=1 myflag=0 onIndex="+ (onIndex)+ " algorithm="+map.get(thisId.get(m).toString())+ " s1Id="+ s1.getS1Id()+ " from="+ index+ " to="+ (count)+ " value="+ obj[2]+ " id='_"+ m	+ "_"+ obj[2]+ "' doName="+ (char) BasicTypeUtils.parseInt(map1.get(thisId.get(m)).toString())+ " name='editItem' onclick='select(this);'>"+ JavaScriptUtils.javaScriptEscape(obj[0].toString()) + "</td>");
								}else{
									html.append("<td style='white-space:nowrap;word-break: keep-all;' mytag=1 myflag=0 onIndex="+ (onIndex)+ " algorithm="+map.get(thisId.get(m).toString())+ " s1Id="+ s1.getS1Id()+ " from="+ index+ " to="+ (count)+ " value="+ obj[2]+ " id='_"+ m	+ "_"+ obj[2]+ "' doName="+ (char) BasicTypeUtils.parseInt(map1.get(thisId.get(m)).toString())+ " name='editItem' onclick='select(this);'>"+ JavaScriptUtils.javaScriptEscape(obj[0].toString()) + "</td>");
								}
								
							}
							onIndex++;
							tempId=map2.get(thisId.get(m).toString()).toString();
						}
				
				for(int n=24-idList.size();n<24;n++){
					char a=(char)(n+65-(24-idList.size()));
					html.append("<td mytag=1 value=-1 name='editItem' myflag=1 onIndex="+(n+(j==0?0:tempCount))+"   id='"+(s1.getS1Id())+""+a+"'></td>");
				}
				html.append("<td mytag=1 value=-1 name='editItem' myflag=1 onIndex="+(24+(j==0?0:tempCount))+" algorithm="+edrAlgorithm+"  id='"+s1.getS1Id()+"edr'></td>");
				html.append("</tr>");
				
			}
		}
		html.append("</tr>");
		html.append("</table>");
		html.append("</td>");
		html.append("</tr>");
		html.append("</div>");
		html.append("</table>");
		
		return html.toString();
	}
	Object[] str=null;
	/**
	 * 保存S4
	 * @return
	 */
	public String saveS4(){
		str=new Object[3];
		List saveList = new ArrayList();
		SRemark sRmaker = s1Bo.getRemarkBySssi(ssiId);
		sRmaker.setS4aInRemark(remark);
		str[0]=sRmaker;
		str[1]=ComacConstants.DB_UPDATE;
		str[2]=getSysUser().getUserId();
		saveList.add(str);
		S4 s4 =null;
		String[] s=arrayValue.split(",");
		String[] id=s4IdArray.substring(1, s4IdArray.length()-1).split(",");
		
		int index=0;
		String value=null;
		for(int i =0;i<id.length;i++){
			str=new Object[3];
			s4=(S4)s4Bo.loadById(S4.class,id[i].trim());
			s4.setSelect1(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect2(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect3(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect4(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect5(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect6(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect7(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect8(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect9(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect10(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect11(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect12(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect13(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect14(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect15(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect16(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect17(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect18(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect19(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect20(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect21(BasicTypeUtils.parseToDouble((value=s[index++])==null?null:value.trim()));
			s4.setSelect22(BasicTypeUtils.parseToDouble((value=s[index++])==null?null:value.trim()));
			s4.setSelect23(BasicTypeUtils.parseToDouble((value=s[index++])==null?null:value.trim()));
			s4.setSelect24(BasicTypeUtils.parseToDouble((value=s[index++])==null?null:value.trim()));
			value=s[index++];
			if(value==null||"null".equals(value==null?null:value.trim())||"".equals(value==null?null:value.trim())){
				writeToResponse("false");
				return null;
			}
			s4.setGdr(BasicTypeUtils.parseToDouble(value==null?null:value.trim()));
			
			Sy sy = syBo.isExistForSy(ssiId, s4.getS1().getS1Id(), s4.getInOrOut());
			if (sy != null){
				List<S6Ea> s6eaList = s6Bo.getS6EaByS1Id(s4.getInOrOut(), s4.getS1().getS1Id());
				if(s6eaList!=null&&!s6eaList.isEmpty()){
					S6Ea s6ea = s6eaList.get(0);
					if (sy.getScr() < s4.getGdr()){
						s6ea.setEdr(sy.getScr());
					} else {
						s6ea.setEdr(s4.getGdr());
					}
					s4Bo.saveOrUpdate(s6ea, ComacConstants.DB_UPDATE, getSysUser().getUserId());
				}
			}
			
			str[0]=s4;
			str[1]=ComacConstants.DB_UPDATE;
			str[2]=getSysUser().getUserId();
			saveList.add(str);
		}
		String lcindex=null;;
		List<SStep> step1=s1Bo.getSstepBySssiId((ssiId));
			if (step1.get(0).getSyaIn() == 1) {
				step1.get(0).setS4aIn(1);
		} else {
			step1.get(0).setS4aIn(1);
			step1.get(0).setSyaIn(2);
		}
		str=new Object[3];
		str[0]=step1.get(0);
		str[1]=ComacConstants.DB_UPDATE;
		str[2]=getSysUser().getUserId();
		saveList.add(str);
		s4Bo.saveList(saveList,getSysUser(),"S4AIN");
		this.ssiStepBo.changeStatus(ssiId);
		writeToResponse(lcindex);
		return null;
	}
	/**
	 * 保存S4A外
	 * @return
	 */
	public String saveS4aout(){
		str=new Object[3];
		List saveList = new ArrayList();
		SRemark sRmaker = s1Bo.getRemarkBySssi(ssiId);
		sRmaker.setS4aOutRemark(remark);
		str[0]=sRmaker;
		str[1]=ComacConstants.DB_UPDATE;
		str[2]=getSysUser().getUserId();
		saveList.add(str);
		S4 s4 =null;
		String[] s=arrayValue.split(",");
		String[] id=s4IdArray.substring(1, s4IdArray.length()-1).split(",");
		int index=0;
		String value=null;
		for(int i =0;i<id.length;i++){
			s4=(S4)s4Bo.loadById(S4.class, id[i].trim());
			s4.setSelect1(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect2(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect3(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect4(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect5(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect6(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect7(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect8(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect9(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect10(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect11(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect12(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect13(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect14(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect15(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect16(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect17(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect18(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect19(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect20(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect21(BasicTypeUtils.parseToDouble((value=s[index++])==null?null:value.trim()));
			s4.setSelect22(BasicTypeUtils.parseToDouble((value=s[index++])==null?null:value.trim()));
			s4.setSelect23(BasicTypeUtils.parseToDouble((value=s[index++])==null?null:value.trim()));
			s4.setSelect24(BasicTypeUtils.parseToDouble((value=s[index++])==null?null:value.trim()));
			value=s[index++];
			if(value==null||"null".equals(value==null?null:value.trim())||"".equals(value==null?null:value.trim())){
				writeToResponse("false");
				return null;
			}
			s4.setGdr(BasicTypeUtils.parseToDouble(value==null?null:value.trim()));
			
			Sy sy = syBo.isExistForSy(ssiId, s4.getS1().getS1Id(), s4.getInOrOut());
			if (sy != null){
				List<S6Ea> s6eaList = s6Bo.getS6EaByS1Id(s4.getInOrOut(), s4.getS1().getS1Id());
				if(s6eaList!=null&&!s6eaList.isEmpty()){
					S6Ea s6ea = s6eaList.get(0);
					if (sy.getScr() < s4.getGdr()){
						s6ea.setEdr(sy.getScr());
					} else {
						s6ea.setEdr(s4.getGdr());
					}
					s4Bo.saveOrUpdate(s6ea, ComacConstants.DB_UPDATE, getSysUser().getUserId());
				}
			}
			
			str[0]=s4;
			str[1]=ComacConstants.DB_UPDATE;
			str[2]=getSysUser().getUserId();
			saveList.add(str);
		}
		String lcindex=null;;
		List<SStep> step1=s1Bo.getSstepBySssiId((ssiId));
			if(step1.get(0).getSyaOut()==1){
				step1.get(0).setS4aOut(1);
			}else{
			step1.get(0).setS4aOut(1);
			step1.get(0).setSyaOut(2);}
		s4Bo.saveOrUpdate(step1.get(0), ComacConstants.DB_UPDATE, getSysUser().getUserId());
		str=new Object[3];
		str[0]=step1.get(0);
		str[1]=ComacConstants.DB_UPDATE;
		str[2]=getSysUser().getUserId();
		saveList.add(str);
		s4Bo.saveList(saveList,getSysUser(),"S4AOUT");
		this.ssiStepBo.changeStatus(ssiId);
		writeToResponse(lcindex);
		return null;
	}
	/**
	 * 保存S4B外
	 * @return
	 */
	public String saveS4bout(){
		
		str=new Object[3];
		List saveList = new ArrayList();
		SRemark sRmaker = s1Bo.getRemarkBySssi(ssiId);
		sRmaker.setS4bOutRemark(remark);
		str[0]=sRmaker;
		str[1]=ComacConstants.DB_UPDATE;
		str[2]=getSysUser().getUserId();
		saveList.add(str);
		S4 s4 =null;
		String[] s=arrayValue.split(",");
		String[] id=s4IdArray.substring(1, s4IdArray.length()-1).split(",");
		int index=0;
		String value=null;
		for(int i =0;i<id.length;i++){
			s4=(S4)s4Bo.loadById(S4.class, id[i].trim());
			s4.setSelect1(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect2(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect3(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect4(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect5(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect6(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect7(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect8(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect9(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect10(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect11(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect12(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect13(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect14(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect15(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect16(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect17(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect18(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect19(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect20(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect21(BasicTypeUtils.parseToDouble((value=s[index++])==null?null:value.trim()));
			s4.setSelect22(BasicTypeUtils.parseToDouble((value=s[index++])==null?null:value.trim()));
			s4.setSelect23(BasicTypeUtils.parseToDouble((value=s[index++])==null?null:value.trim()));
			s4.setSelect24(BasicTypeUtils.parseToDouble((value=s[index++])==null?null:value.trim()));
			value=s[index++];
			if(value==null||"null".equals(value==null?null:value.trim())||"".equals(value==null?null:value.trim())){
				writeToResponse("false");
				return null;
			}
			s4.setGdr(BasicTypeUtils.parseToDouble(value==null?null:value.trim()));
			
			Sy sy = syBo.isExistForSy(ssiId, s4.getS1().getS1Id(), s4.getInOrOut());
			if (sy != null){
				List<S6Ea> s6eaList = s6Bo.getS6EaByS1Id(s4.getInOrOut(), s4.getS1().getS1Id());
				if(s6eaList!=null&&!s6eaList.isEmpty()){
					S6Ea s6ea = s6eaList.get(0);
					if (sy.getScr() < s4.getGdr()){
						s6ea.setEdr(sy.getScr());
					} else {
						s6ea.setEdr(s4.getGdr());
					}
					s4Bo.saveOrUpdate(s6ea, ComacConstants.DB_UPDATE, getSysUser().getUserId());
				}
			}
			
			str[0]=s4;
			str[1]=ComacConstants.DB_UPDATE;
			str[2]=getSysUser().getUserId();
			saveList.add(str);
		}
		String lcindex=null;;
		List<SStep> step1=s1Bo.getSstepBySssiId((ssiId));
			if(step1.get(0).getSybOut()==1){
				step1.get(0).setS4bOut(1);
			}else{
				step1.get(0).setS4bOut(1);
				step1.get(0).setSybOut(2);
			}
			str=new Object[3];
			str[0]=step1.get(0);
			str[1]=ComacConstants.DB_UPDATE;
			str[2]=getSysUser().getUserId();
			saveList.add(str);
			s4Bo.saveList(saveList,getSysUser(),"S4BIN");
			this.ssiStepBo.changeStatus(ssiId);
			writeToResponse(lcindex);
		return null;
	}
	/**
	 * 保存S4B
	 * @return
	 */
	public String saveS4b(){
		str=new Object[3];
		List saveList = new ArrayList();
		SRemark sRmaker = s1Bo.getRemarkBySssi(ssiId);
		sRmaker.setS4bInRemark(remark);
		str[0]=sRmaker;
		str[1]=ComacConstants.DB_UPDATE;
		str[2]=getSysUser().getUserId();
		saveList.add(str);
		S4 s4 =null;
		String[] s=arrayValue.split(",");
		String[] id=s4IdArray.substring(1, s4IdArray.length()-1).split(",");
		int index=0;
		String value=null;
		for(int i =0;i<id.length;i++){
			s4=(S4)s4Bo.loadById(S4.class, id[i].trim());
			s4.setSelect1(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect2(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect3(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect4(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect5(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect6(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect7(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect8(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect9(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect10(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect11(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect12(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect13(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect14(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect15(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect16(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect17(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect18(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect19(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect20(BasicTypeUtils.parseInt((value=s[index++])==null?null:value.trim()));
			s4.setSelect21(BasicTypeUtils.parseToDouble((value=s[index++])==null?null:value.trim()));
			s4.setSelect22(BasicTypeUtils.parseToDouble((value=s[index++])==null?null:value.trim()));
			s4.setSelect23(BasicTypeUtils.parseToDouble((value=s[index++])==null?null:value.trim()));
			s4.setSelect24(BasicTypeUtils.parseToDouble((value=s[index++])==null?null:value.trim()));
			value=s[index++];
			if(value==null||"null".equals(value==null?null:value.trim())||"".equals(value==null?null:value.trim())){
				writeToResponse("false");
				return null;
			}
			s4.setGdr(BasicTypeUtils.parseToDouble(value==null?null:value.trim()));
			
			Sy sy = syBo.isExistForSy(ssiId, s4.getS1().getS1Id(), s4.getInOrOut());
			if (sy != null){
				List<S6Ea> s6eaList = s6Bo.getS6EaByS1Id(s4.getInOrOut(), s4.getS1().getS1Id());
				if(s6eaList!=null&&!s6eaList.isEmpty()){
					S6Ea s6ea = s6eaList.get(0);
					if (sy.getScr() < s4.getGdr()){
						s6ea.setEdr(sy.getScr());
					} else {
						s6ea.setEdr(s4.getGdr());
					}
					s4Bo.saveOrUpdate(s6ea, ComacConstants.DB_UPDATE, getSysUser().getUserId());
				}
			}
			
			str[0]=s4;
			str[1]=ComacConstants.DB_UPDATE;
			str[2]=getSysUser().getUserId();
			saveList.add(str);
		}
		List<SStep> step1=s1Bo.getSstepBySssiId(ssiId);
		if(step1.get(0).getSybIn()==1){
			step1.get(0).setS4bIn(1);
		}else{
			step1.get(0).setS4bIn(1);
			step1.get(0).setSybIn(2);}
			str=new Object[3];
			str[0]=step1.get(0);
			str[1]=ComacConstants.DB_UPDATE;
			str[2]=getSysUser().getUserId();
			saveList.add(str);
			s4Bo.saveList(saveList,getSysUser(),"S4BOUT");
			this.ssiStepBo.changeStatus(ssiId);
			return null;
	}
	
	List<String[]> combine(Object a[], int n, int m) {
		  m = m > n ? n : m;
		  int[] order = new int[m + 1];
		  for (int i = 0; i <= m; i++) {
		   order[i] = i - 1;// order[0]=-1用来作为循环判断标识
		  }
		  int k = m;
		  boolean flag = true; // 标志找到一个有效组合
		  List<String[]> strList = new ArrayList<String[]>();
		  String[] strString = null;
		  while (order[0] == -1) {
		   strString = new String[m];
		   if (flag) // 输出符合要求的组合
		   {
		    for (int i = 1; i <= m; i++) {
		     strString[i - 1] = a[order[i]].toString();
		    }
		    flag = false;
		    strList.add(strString);
		   }
		   order[k]++; // 在当前位置选择新的数字
		   if (order[k] == n) // 当前位置已无数字可选，回溯
		   {
		    order[k--] = 0;
		    continue;
		   }
		   if (k < m) // 更新当前位置的下一位置的数字
		   {
		    order[++k] = order[k - 1];
		    continue;
		   }
		   if (k == m) {
		    flag = true;
		   }
		  }
		  return strList;
		 }

	/**
	 * 得到所有元素的总长度
	 * @param list
	 * @return
	 */
	public int getItemsLength(List<Integer> list){
		int count=0;
		for(int i:list){
			count+=i;
		}
		return count;
	}
//	/**
//	 * 得到当前的属于哪一个
//	 * @param map
//	 * @param id
//	 * @return
//	 */
//	public Object getAlgorithmById(Map map,int id){
//		Object s = map.get(id);
//		return s;
//	}
//	
//	
	public Map getLengthMap(List<String[]> list){
		Map<Object,Object[]> map = new HashMap<Object,Object[]>();
		Map<Object,Object[]> map2 = new HashMap<Object,Object[]>();
		int count=0;
		Object[] str=null;
		for(Object[] obj:list){
			if(map.containsKey(obj[1])){
				str=map.get(obj[1]);
				count= BasicTypeUtils.parseInt(str[2].toString());
				count++;
				obj[2]=count+"";
				map.put(obj[1],obj);
				map2.put(obj[1], obj);
			}else{
				count=1;
				obj[2]=count+"";
				map.put(obj[1],obj);
				map2.put(obj[1], obj);
			}
		}
		return map2;
	}
	public IS4Bo getS4Bo() {
		return s4Bo;
	}
	public void setS4Bo(IS4Bo s4Bo) {
		this.s4Bo = s4Bo;
	}
	public String getMatrix() {
		return matrix;
	}
	public void setMatrix(String matrix) {
		this.matrix = matrix;
	}
	public String getSiCode() {
		return siCode;
	}
	public void setSiCode(String siCode) {
		this.siCode = siCode;
	}
	public int getSsiLength() {
		return ssiLength;
	}
	public void setSsiLength(int ssiLength) {
		this.ssiLength = ssiLength;
	}
	public String getSiTitle() {
		return siTitle;
	}
	public void setSiTitle(String siTitle) {
		this.siTitle = siTitle;
	}
	public IS1Bo getS1Bo() {
		return s1Bo;
	}
	public void setS1Bo(IS1Bo s1Bo) {
		this.s1Bo = s1Bo;
	}
	public String getSsiId() {
		return ssiId;
	}
	public void setSsiId(String ssiId) {
		this.ssiId = ssiId;
	}
	public String getDefaultEff() {
		return defaultEff;
	}
	public void setDefaultEff(String defaultEff) {
		this.defaultEff = defaultEff;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getInorOut() {
		return inorOut;
	}
	public void setInorOut(String inorOut) {
		this.inorOut = inorOut;
	}
	public int getInOrOut() {
		return inOrOut;
	}
	public void setInOrOut(int inOrOut) {
		this.inOrOut = inOrOut;
	}
	public String getaOrb() {
		return aOrb;
	}
	public void setaOrb(String aOrb) {
		this.aOrb = aOrb;
	}
	public int getIsMetal() {
		return isMetal;
	}
	public void setIsMetal(int isMetal) {
		this.isMetal = isMetal;
	}
	public int[] getStep() {
		return step;
	}
	public void setStep(int[] step) {
		this.step = step;
	}
	public int getLevelCount() {
		return levelCount;
	}
	public void setLevelCount(int levelCount) {
		this.levelCount = levelCount;
	}
	public int getIsMaintain() {
		return isMaintain;
	}
	public void setIsMaintain(int isMaintain) {
		this.isMaintain = isMaintain;
	}
	public String getArrayValue() {
		return arrayValue;
	}
	public void setArrayValue(String arrayValue) {
		this.arrayValue = arrayValue;
	}
	public String getS4IdArray() {
		return s4IdArray;
	}
	public void setS4IdArray(String s4IdArray) {
		this.s4IdArray = s4IdArray;
	}
	public ISsiStepBo getSsiStepBo() {
		return ssiStepBo;
	}
	public void setSsiStepBo(ISsiStepBo ssiStepBo) {
		this.ssiStepBo = ssiStepBo;
	}
	public ISyBo getSyBo() {
		return syBo;
	}
	public void setSyBo(ISyBo syBo) {
		this.syBo = syBo;
	}
	public IS6Bo getS6Bo() {
		return s6Bo;
	}
	public void setS6Bo(IS6Bo s6Bo) {
		this.s6Bo = s6Bo;
	}
	
}
