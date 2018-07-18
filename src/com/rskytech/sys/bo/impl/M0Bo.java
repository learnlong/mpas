package com.rskytech.sys.bo.impl;

import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.M0;
import com.rskytech.pojo.MMain;
import com.rskytech.pojo.MSelect;
import com.rskytech.sys.bo.IM0Bo;
import com.rskytech.sys.bo.IMsiSelectBo;
import com.rskytech.sys.dao.IM0Dao;


public class M0Bo extends BaseBO implements IM0Bo {
	private IMsiSelectBo msiSelectBo;
	private IM0Dao m0Dao;
	
	/**
	 * 查询最高可管理层是当前MSI的MSI及子ATA
	 * @param msiId   系统Main的Id
	 * @return
	 */
	public List<M0> getMsiATAListByMsiId(String msiId)throws BusinessException {
		return m0Dao.getMsiATAListByMsiId(msiId);
	}
	
	/**
	 * 第一次加载M0时从Mselect表中同步数据
	 * @param msiId   系统Main的Id
	 * @param userId  当前登录用户Id
	 * @throws BusinessException
	 */
	public void cogradientM0(String msiId,String userId,String modelSerierId) throws BusinessException {
	    M0 m0;
	    MMain mMain =(MMain)this.loadById(MMain.class, msiId);
		  //获取当前MSI的ATA及其子ATA
		  List<Object[]> list = msiSelectBo.getAtaAndfindAllChildByAtaId(mMain.getComAta().getAtaId(),modelSerierId,userId);
		for (Object[] obj : list) {
			List<MSelect> listmSelect = msiSelectBo.getMSelectByataId((String)obj[0],modelSerierId);
			if(listmSelect!=null&&listmSelect.size()>0){
				for(MSelect mSelect : listmSelect){
					//将获取的ATA的MSelect数据拷贝的M0中 只拷贝需要分析的
					if(mSelect.getIsMsi()!=null&&mSelect.getIsMsi().equals(1)){
						m0 = new M0();
						if(obj[1]!=null){
							m0.setProCode((String)obj[1]);
						}else{
							m0.setProCode(null);
						}
						if(obj[2]!=null){
							m0.setProName((String)obj[2]);
						}else{
							m0.setProName(null);
						}
						m0.setSafety(mSelect.getSafety());
						m0.setSafetyAnswer(mSelect.getSafetyAnswer());
						m0.setDetectable(mSelect.getDetectable());
						m0.setDetectableAnswer(mSelect.getDetectableAnswer());
						m0.setTask(mSelect.getTask());
						m0.setTaskAnswer(mSelect.getTaskAnswer());
						m0.setEconomic(mSelect.getEconomic());
						m0.setEconomicAnswer(mSelect.getEconomicAnswer());
						m0.setIsMsi(mSelect.getIsMsi());
						m0.setHighestLevel(mSelect.getHighestLevel());
						m0.setRemark(mSelect.getRemark());
						m0.setIsAddAta(ComacConstants.NO);
						m0.setMMain(mMain);
						this.save(m0,userId);  
					}
				}
			}else{
				m0 = new M0();
				if(obj[1]!=null){
					m0.setProCode((String)obj[1]);
				}else{
					m0.setProCode(null);
				}
				if(obj[2]!=null){
					m0.setProName((String)obj[2]);
				}else{
					m0.setProName(null);
				}
				m0.setIsAddAta(ComacConstants.NO);
				m0.setMMain(mMain);
				this.save(m0,userId);  
			}
		} 
	}
		
	
	/**
	 * 删除手动添加的ATA
	 * @param m0Id
	 * @param userId
	 * @throws BusinessException
	 * @author chendexu
	 * createdate 2012-08-19
	 */
	@Override
	public void delete(String m0Id, String userId) throws BusinessException {
		M0 m0=(M0)this.loadById(M0.class, m0Id);
		if(ComacConstants.YES.equals(m0.getIsAddAta()) ){
			//如果是手动添加的ATA
			this.delete(m0, userId);
		}
	
	}

	public IMsiSelectBo getMsiSelectBo() {
		return msiSelectBo;
	}
	public void setMsiSelectBo(IMsiSelectBo msiSelectBo) {
		this.msiSelectBo = msiSelectBo;
	}
	
	/**
	 * 保存m0的数据,其中save保存com_log_operate,saveorupdate保存com_log_db以及保存m0
	 * @param m0 m0数据
	 * @param dbOperate 数据操作类型
	 * @param user 当前用户
	 * @param pageId 操作页面
	 * @param source_system 操作方式(系统,area,lhirf,struct)
	 */
	public void saveOrUpdateM0(String jsonData,ComUser user,String defaultEff) {

		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = new JSONObject();
		// db操作区分
		String dbOperate = "";
		M0 m0;
		jsonObject = jsonArray.getJSONObject(0);
		MMain mMain =(MMain)this.loadById(MMain.class, jsonObject.getString("msiId"));
		mMain.setEffectiveness(defaultEff);
		Set<M0> setM0=mMain.getM0s();
		for (M0 m02 : setM0) {
			this.saveOrUpdate(m02, ComacConstants.DB_UPDATE, user.getUserId());
		}
		for (int i = 0; i < jsonArray.size(); i++) {
			m0 = new M0();
			jsonObject = jsonArray.getJSONObject(i);
			String id = jsonObject.getString("m0Id");
			
			if (!BasicTypeUtils.isNullorBlank(id)) {
				// 修改操作
				dbOperate = ComacConstants.DB_UPDATE;
				m0 = (M0) msiSelectBo.loadById(M0.class, id);
			} else {
				// 添加操作
				dbOperate = ComacConstants.DB_INSERT;
				m0.setIsAddAta(ComacConstants.YES);
				m0.setMMain(new MMain());
				m0.getMMain().setMsiId(jsonObject.getString("msiId"));
			}
			m0.setProCode(jsonObject.getString("proCode"));
			m0.setProName(jsonObject.getString("proName"));
			// 对安全性影响回答时
			if (BasicTypeUtils.isNumberString(jsonObject.getString("safety"))) {
				m0.setSafety(jsonObject.getInt("safety"));
			}
			m0.setSafetyAnswer(jsonObject.getString("safetyAnswer"));
			// 对空勤人员是否容易发现回答时
			if (BasicTypeUtils.isNumberString(jsonObject.getString("detectable"))) {
				m0.setDetectable(jsonObject.getInt("detectable"));
			}
			m0.setDetectableAnswer(jsonObject.getString("detectableAnswer"));
			// 对任务影响回答时
			if (BasicTypeUtils.isNumberString(jsonObject.getString("task"))) {
				m0.setTask(jsonObject.getInt("task"));
			}
			m0.setTaskAnswer(jsonObject.getString("taskAnswer"));
			// 对经济性影答时
			if (BasicTypeUtils.isNumberString(jsonObject.getString("economic"))) {
				m0.setEconomic(jsonObject.getInt("economic"));
			}
			m0.setEconomicAnswer(jsonObject.getString("economicAnswer"));
			// 对是否是MSI做出选择时
			if (BasicTypeUtils.isNumberString(jsonObject.getString("isMsi"))) {
				m0.setIsMsi(jsonObject.getInt("isMsi"));
			}
			m0.setHighestLevel(jsonObject.getString("highestLevel"));
			m0.setRemark(jsonObject.getString("remark"));
			String pageId="M0";
			this.saveComLogOperate(user, pageId, ComacConstants.SYSTEM_CODE);
			this.saveOrUpdate(mMain, ComacConstants.DB_UPDATE, user.getUserId());
			this.saveOrUpdate(m0, dbOperate, user.getUserId());
		}
	}

	public IM0Dao getM0Dao() {
		return m0Dao;
	}

	public void setM0Dao(IM0Dao m0Dao) {
		this.m0Dao = m0Dao;
	}

}
