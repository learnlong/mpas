package com.rskytech.sys.bo.impl;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.basedata.dao.IComAtaDao;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.M12;
import com.rskytech.pojo.MMain;
import com.rskytech.pojo.MStep;
import com.rskytech.sys.bo.IM0Bo;
import com.rskytech.sys.bo.IM12Bo;
import com.rskytech.sys.bo.IMStepBo;
import com.rskytech.sys.dao.IM12Dao;
public class M12Bo extends BaseBO implements IM12Bo {
	private IMStepBo mstepBo;
	private IM12Dao m12Dao;
	private IComAreaBo comAreaBo;
	private IComAtaDao comAtaDao;
	
	public IComAtaDao getComAtaDao() {
		return comAtaDao;
	}

	public void setComAtaDao(IComAtaDao comAtaDao) {
		this.comAtaDao = comAtaDao;
	}

	public IMStepBo getMstepBo() {
		return mstepBo;
	}

	public void setMstepBo(IMStepBo mstepBo) {
		this.mstepBo = mstepBo;
	}

	private IM0Bo m0Bo;
	/**
	 *根据msiId查询M12
	 * @param msiId
	 * @return
	 */
	public List<M12> getM12AListByMsiId(String msiId) throws BusinessException {
		return m12Dao.getM12AListByMsiId(msiId);
	}
	
	/**
	 * 第一次加载M12时从M0表中同步数据
	 * @param msiId
	 * @param userId
	 */
//	public void cogradientM12(String msiId, String userId)
//			throws BusinessException {
//		List<M0> listM0 = m0Bo.getMsiATAListByMsiId(msiId);
//		M12 m12;
//		for (M0 m0 : listM0) {
//			m12 = new M12();
//			m12.setProCode(m0.getProCode());
//			m12.setProName(m0.getProName());
//			m12.setIsAddAta(m0.getIsAddAta());
//			m12.setMMain(new MMain());
//			m12.getMMain().setMsiId(msiId);
//			this.saveOrUpdate(m12,ComacConstants.DB_INSERT,userId); 
//		}
//	}
	
	/**
	 * 第一次加载M12时从ATA表中同步数据，只同步该MSI的下级ATA，不包括下下级
	 * @param msiId
	 * @param userId
	 */
	public void cogradientM12(String msiId, String userId)
			throws BusinessException {
		MMain m = (MMain) comAtaDao.loadById(MMain.class, msiId);
		ComAta ca = m.getComAta();
		
		/*M12 m12 = new M12();
		m12.setMMain(m);
		m12.setProCode(ca.getAtaCode());
		m12.setProName(ca.getAtaName());
		m12.setIsAddAta(0);		
		this.saveOrUpdate(m12,ComacConstants.DB_INSERT,userId); */
		
		List<ComAta> ataList = comAtaDao.loadChildAta(m.getComModelSeries().getModelSeriesId(), ca.getAtaId());		
		for (ComAta ata : ataList) {
			M12 m12 = new M12();
			m12.setMMain(m);
			m12.setProCode(ata.getAtaCode());
			m12.setProName(ata.getAtaName());
			m12.setIsAddAta(0);
			this.saveOrUpdate(m12,ComacConstants.DB_INSERT,userId); 
		}
	}
	
	
	public void delete(String m12Id, String userId) throws BusinessException {
		this.delete(M12.class,m12Id, userId);
		
	}
	public IM0Bo getM0Bo() {
		return m0Bo;
	}
	public void setM0Bo(IM0Bo m0Bo) {
		this.m0Bo = m0Bo;
	}
	/**
	 * 保存m12的数据,其中save保存com_log_operate,saveorupdate保存com_log_db以及保存m12
	 * @param user 当前用户
	 * @param pageId 操作页面
	 * @param source_system 操作方式(系统,area,lhirf,struct)
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> SaveOrUpdateM12(ComUser user,
			String pageId, String source_system,String jsonData,ComModelSeries comModelSeries,String isUpdateStep) {
		HashMap<String, String> map = new HashMap<String, String>();
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = new JSONObject();
		String dbOperate = "";
		MMain m = new MMain();
		//保存M1.2中新增的数据，再判断ATA中是否存在，如没有则新增到ATA中
		HashMap<String, String> hm = new HashMap<String, String>();
		
		M12 m12=new M12();
		for (int i = 0;i < jsonArray.size();i++) {
			jsonObject = jsonArray.getJSONObject(i);
			if (i == 0){
				m = (MMain) this.loadById(MMain.class, jsonObject.getString("msiId"));
			}
			
			m12 = new M12();			
			String id = jsonObject.getString("m12Id");
			if (!BasicTypeUtils.isNullorBlank(id)) {
				// 修改操作
				dbOperate = ComacConstants.DB_UPDATE;
				m12 = (M12) this.loadById(M12.class, id);
			} else {
				// 添加操作
				dbOperate = ComacConstants.DB_INSERT;
				m12.setMMain(new MMain());
				m12.getMMain().setMsiId(jsonObject.getString("msiId"));
				
				hm.put(jsonObject.getString("proCode").trim(), jsonObject.getString("proName"));
			}
			m12.setProCode(jsonObject.getString("proCode"));
			m12.setProName(jsonObject.getString("proName"));
			if (BasicTypeUtils.isNumberString(jsonObject.getString("quantity"))) {
				m12.setQuantity(jsonObject.getInt("quantity"));
			}
			m12.setVendor(jsonObject.getString("vendor"));
			m12.setPartNo(jsonObject.getString("partNo"));
			m12.setSimilar(jsonObject.getString("similar"));
			m12.setZonalChannel(jsonObject.getString("zonalChannel"));
			m12.setHistoricalMtbf(jsonObject.getString("historicalMtbf"));
			m12.setPredictedMtbf(jsonObject.getString("predictedMtbf"));
			m12.setZonal(comAreaBo.getAreaIdByAreaCode(jsonObject.getString("zonal"),comModelSeries.getModelSeriesId()));
			if (BasicTypeUtils.isNumberString(jsonObject.getString("isAddAta"))) {
				m12.setIsAddAta(jsonObject.getInt("isAddAta"));
			}
			if (BasicTypeUtils.isNumberString(jsonObject.getString("mmel"))) {
				m12.setMmel(jsonObject.getInt("mmel"));
			}
			this.saveOrUpdate(m12,dbOperate,user.getUserId());
		}
		
		ComAta ca = m.getComAta();
		
		//添加新增的记录的项目编号到ATA中
		for (String s : hm.keySet()){
			DetachedCriteria dc = DetachedCriteria.forClass(ComAta.class);
			dc.add(Restrictions.eq("ataCode", s));
			dc.add(Restrictions.eq("comModelSeries.id", comModelSeries.getModelSeriesId()));
			dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
			List<ComAta> caList = this.findByCritera(dc);
			if (caList != null && caList.size() > 0){//ATA表中已经存在该ATA，不做添加
				continue;
			} else {//ATA表中不存在该ATA，添加ATA数据
				ComAta ata = new ComAta();
				ata.setComModelSeries(comModelSeries);
				ata.setComAta(ca);
				ata.setAtaCode(s);
				ata.setAtaName(hm.get(s));
//				ata.setAtaLevel(ataLevel + 1);
				ata.setValidFlag(2);
				comAreaBo.saveOrUpdate(ata, ComacConstants.DB_INSERT, user.getUserId());
				
				map.put("ataId", ata.getAtaId());
			}
		}
		
		this.saveComLogOperate(user, pageId, source_system);
		if("1".equals(isUpdateStep)||"0".equals(isUpdateStep)){
		   MStep mstep = mstepBo.getMStepByMsiId(m12.getMMain().getMsiId());
		   if(mstep.getM13().equals(ComacConstants.STEP_NO)){
			   //M1.3的状态为'未完成'
			   //m1.2的状态改为完成
			   mstep.setM12(ComacConstants.STEP_FINISH);
			   //m1.3的状态改为正在分析
			   mstep.setMset(ComacConstants.STEP_NOW);
			   //mstep.setM13(ComacConstants.STEP_NOW);
			   this.mstepBo.saveOrUpdate(mstep,ComacConstants.DB_UPDATE, user.getUserId());
		   }
		}
		return map;
	}

	public IM12Dao getM12Dao() {
		return m12Dao;
	}

	public void setM12Dao(IM12Dao m12Dao) {
		this.m12Dao = m12Dao;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}
}
