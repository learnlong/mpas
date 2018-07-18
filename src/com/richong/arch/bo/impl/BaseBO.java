package com.richong.arch.bo.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.richong.arch.dao.IDAO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComLogDb;
import com.rskytech.pojo.ComLogOperate;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.LhMain;

@SuppressWarnings("rawtypes")
public class BaseBO implements IBo {
	protected IDAO dao;
	// db日志处理类
	LogDetail logDetail = new LogDetail();
	// db日志pojo
	ComLogDb log;

	public IDAO getDao() {
		return dao;
	}

	public void setDao(IDAO dao) {
		this.dao = dao;
	}
	
	/**
	 * 删除操作
	 * 
	 * @param pojo
	 *            待操作对象
	 * @author changyf createdate 2012-08-09
	 */
	public void delete(Object pojo) throws BusinessException {
		try {
			dao.delete(pojo);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	public void delete(Class pojoClass, Serializable id)
			throws BusinessException {
		try {
			Object obj = dao.loadById(pojoClass, id);
			dao.delete(obj);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	public void saveOrUpdate(Object pojo) throws BusinessException {
		try {
			dao.saveOrUpdate(pojo);
		} catch (Throwable e) {
			throw new BusinessException(e);
		}
	}

	public Object loadById(Class pojoClass, Serializable id)
			throws BusinessException {
		try {
			return dao.loadById(pojoClass, id);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	public List find(String query, Object[] values) throws BusinessException {
		try {
			return dao.findByHql(query, values);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	public List findAll(Class pojoClass) throws BusinessException {
		try {
			return dao.findAllPOJO(pojoClass);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	public void save(Object pojo) throws BusinessException {
		try {
			dao.save(pojo);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	public void update(Object pojo) throws BusinessException {
		try {
			dao.update(pojo);
		} catch (Exception e) {
			throw new BusinessException(e);
		}

	}

	public void merge(Object pojo) throws BusinessException {
		try {
			dao.merge(pojo);
		} catch (Exception e) {
			throw new BusinessException(e);
		}

	}

	public Page findByCritera(DetachedCriteria dc, Page page)
			throws BusinessException {
		return dao.findByCriteria(dc, page);
	}

	public List findByCritera(DetachedCriteria dc) throws BusinessException {
		try {
			return dao.findByCriteria(dc);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	// /////////////////////////////add by changyf begin////////////////////

	public void save(Object pojo, String userId) throws BusinessException {
		setDbCommonProperty(pojo, ComacConstants.DB_INSERT, userId);
		try {
			log = returnDbLogObj(pojo, ComacConstants.DB_INSERT, userId);
			dao.save(log);
			dao.save(pojo);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	public void saveOrUpdate(Object pojo, String operateFlg, String userId)
			throws BusinessException {
		setDbCommonProperty(pojo, operateFlg, userId);
		try {
			log = returnDbLogObj(pojo, operateFlg, userId);
			dao.save(log);
			dao.saveOrUpdate(pojo);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	public void update(Object pojo, String userId) throws BusinessException {
		setDbCommonProperty(pojo, ComacConstants.DB_UPDATE, userId);
		try {
			log = returnDbLogObj(pojo, ComacConstants.DB_UPDATE, userId);
			dao.save(log);
			dao.update(pojo);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	/**
	 * 删除操作
	 * 
	 * @param pojo
	 *            待操作对象
	 * @param userId
	 *            用户id
	 * @author changyf createdate 2012-08-09
	 */
	public void delete(Object pojo, String userId) throws BusinessException {
		try {
			log = returnDbLogObj(pojo, ComacConstants.DB_DELETE, userId);
			dao.save(log);
			dao.delete(pojo);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	/**
	 * 执行删除操作
	 * 
	 * @param pojoClass
	 *            待操作的pojo类型
	 * @param id
	 *            待操作对象的id
	 * @param userId
	 *            操作用户id
	 * @throws BusinessException
	 * @author changyf createdate 2012-08-09
	 */
	public void delete(Class pojoClass, Serializable id, String userId)
			throws BusinessException {
		try {
			Object obj = dao.loadById(pojoClass, id);
			log = returnDbLogObj(obj, ComacConstants.DB_DELETE, userId);
			dao.save(log);
			dao.delete(obj);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	/**
	 * 根据待操作的对象返回db日志操作对象
	 * 
	 * @param pojo
	 *            待操作的对象
	 * @param operateFlg
	 *            操作区分
	 * @param userId
	 *            操作者id
	 * @return db日志操作对象
	 * @author changyf createdate 2012-08-07
	 */
	private ComLogDb returnDbLogObj(Object pojo, String operateFlg,
			String userId) {
		ComLogDb log = new ComLogDb();
		List list = logDetail.getFieldsValue(pojo);
		log.setLogDate((Date) list.get(0));// 操作日期
		log.setDbOperate(operateFlg);// 操作类别(日志操作都是插入)
		log.setModTable((String) list.get(2));// 操作表
		//log.setOpContent((String) list.get(3));// 操作内容
		setDbCommonProperty(log, ComacConstants.DB_INSERT, userId);// 公共字段赋值
		return log;
	}

	/**
	 * 为DB操作的pojo对象添加共用属性值 （追加者、追加时间、修改者、修改时间）
	 * 
	 * @param pojo
	 *            db操作对象
	 * @param operateFlg
	 *            操作区分flg(insert/update)
	 * @param userId
	 *            操作者
	 * @author changyf createdate 2012-08-02
	 */
	private void setDbCommonProperty(Object pojo, String operateFlg,
			String userId) {
		Date sysDate = BasicTypeUtils.getCurrentDateforSQL();
		if (operateFlg.equals(ComacConstants.DB_INSERT)) { // 追加操作时
			BasicTypeUtils.setEntityObjValue(pojo, "createUser", userId);
			BasicTypeUtils.setEntityObjValue(pojo, "createDate", sysDate);
			BasicTypeUtils.setEntityObjValue(pojo, "modifyUser", userId);
			BasicTypeUtils.setEntityObjValue(pojo, "modifyDate", sysDate);
		} else if (operateFlg.equals(ComacConstants.DB_UPDATE)) {
			// 修改操作时
			BasicTypeUtils.setEntityObjValue(pojo, "modifyUser", userId);
			BasicTypeUtils.setEntityObjValue(pojo, "modifyDate", sysDate);
		}
	}
	// ///////////////////////////add by changyf end////////////////////
	
	
	/**
	 * 保存获取com_log_operate日志对象
	 * 
	 * @param user
	 *            当前用户
	 * @param op_page
	 *           操作页面
	 * @author jiayanyin createdate 2012-12-10
	 */
	public void saveComLogOperate(ComUser user, String op_page, String source_system) {
		ComLogOperate clo=new ComLogOperate();
//		clo.setComModelSeries(user.getComModelSeries());
		clo.setCreateUser(user.getUserId());
		clo.setOpPage(op_page);
		clo.setSourceSystem(source_system);
		clo.setCreateDate(BasicTypeUtils.getCurrentDateforSQL());
		clo.setLogDate(BasicTypeUtils.getCurrentDateforSQL());
		clo.setOpUser(user.getUserId());
		this.save(clo, user.getUserId());
	}


}
