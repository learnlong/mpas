package com.rskytech.sys.action;

import java.util.List;

import org.springframework.web.util.JavaScriptUtils;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComVendorBo;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.ComVendor;
import com.rskytech.pojo.M11;
import com.rskytech.pojo.MMain;
import com.rskytech.pojo.MStep;
import com.rskytech.sys.bo.IM11Bo;
import com.rskytech.sys.bo.IMStepBo;
public class M11Action extends BaseAction {

	private static final long serialVersionUID = 1L;
	public static final String M11="M11"; 
	private IMStepBo mstepBo;
	public IMStepBo getMstepBo() {
		return mstepBo;
	}
	public void setMstepBo(IMStepBo mstepBo) {
		this.mstepBo = mstepBo;
	}
	private MStep mstep;//导航栏步骤
	private String msiId;//Main的Id
	private ComAta showAta;//msi所属的ATA
	private String ataId;  //msi所属的ATA的Id
	private String pagename;//页面名称
	private IM11Bo m11Bo;
	private M11 m11;
	private String m11Id;//图文混排Id
	private String picContent;//图文混排内容
	private String isMaintain;//用户权限(1:修改,0:查看)
	private IComVendorBo comVendorBo;
	private Boolean noM12 ;
	/**
	 * 初始化M11画面
	 * @return 
	 * @author chendexu
	 * createdate 2012-08-21
	 */
	public String init(){
	    mstep = mstepBo.getMStepByMsiId(msiId);
		this.pagename = M11;
		showAta = (ComAta)this.mstepBo.loadById(ComAta.class, ataId);
		 m11 = m11Bo.getM11ByMsiId(msiId);
		 if(m11 != null){
		 picContent = JavaScriptUtils.javaScriptEscape(m11.getPicContent());
		 }
		 List<ComVendor> venList = comVendorBo.getVendorList(this.getComModelSeries().getModelSeriesId());
		 if(venList.size()>0){
			 noM12 = true;
		 }else{
			 noM12 = false;
		 }
		return SUCCESS;
	}
	/**
	 * 保存或修改M11
	 * @return 
	 * @author chendexu
	 * createdate 2012-08-21
	 */
    public String saveM11(){
		ComUser user=this.getSysUser();//获取当前用户
    	// db操作区分
    	String dbOperate = "";
      	this.m11 = (M11)this.m11Bo.getM11ByMsiId(msiId);
    	if(m11 != null){
    		// 修改操作
     	dbOperate = ComacConstants.DB_UPDATE;
    	}else{// 添加操作
    		dbOperate = ComacConstants.DB_INSERT;
    		m11 =new M11();
    		m11.setMMain(new MMain());
    		m11.getMMain().setMsiId(msiId);
    	}
    	m11.setPicContent(picContent);
    	String pageId="M1.1";
    	this.m11Bo.saveM11(m11, user, dbOperate,pageId , msiId,this.getComModelSeries());
    	return null;
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
	public IM11Bo getM11Bo() {
		return m11Bo;
	}
	public void setM11Bo(IM11Bo bo) {
		m11Bo = bo;
	}
	public M11 getM11() {
		return m11;
	}
	public void setM11(M11 m11) {
		this.m11 = m11;
	}

	public String getPicContent() {
		return picContent;
	}
	public void setPicContent(String picContent) {
		this.picContent = picContent;
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
	public String getM11Id() {
		return m11Id;
	}
	public void setM11Id(String id) {
		m11Id = id;
	}
	public IComVendorBo getComVendorBo() {
		return comVendorBo;
	}
	public void setComVendorBo(IComVendorBo comVendorBo) {
		this.comVendorBo = comVendorBo;
	}
	public Boolean getNoM12() {
		return noM12;
	}
	public void setNoM12(Boolean noM12) {
		this.noM12 = noM12;
	}

	

}
