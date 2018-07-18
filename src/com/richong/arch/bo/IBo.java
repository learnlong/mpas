
package com.richong.arch.bo;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.richong.arch.dao.IDAO;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComUser;

@SuppressWarnings("rawtypes")
public interface IBo {
	
	public IDAO getDao();
	
	public void setDao(IDAO dao );

	public List findAll(Class pojoClass)throws BusinessException;

	public Object loadById(Class pojoClass, Serializable id) throws BusinessException;

	public void delete(Object pojo) throws BusinessException;	
	
	public void delete(Class pojoClass, Serializable id) throws BusinessException;	
	
	public void saveOrUpdate(Object pojo) throws BusinessException;	
	
	/**
	 * 追加或保存操作
	 * @param pojo 操作对象
	 * @param operateFlg 操作区分(追加或修改)
	 * @param userId 操作者ID
	 * @throws BusinessException
	 * @author changyf
	 * createdate 2012-08-02
	 */
	public void saveOrUpdate(Object pojo,String operateFlg,String userId) throws BusinessException;
	
	public void save(Object pojo)throws BusinessException;
	
	/**
	 * 执行追加操作
	 * @param pojo 待追加的对象
	 * @param userId 操作者id
	 * @throws BusinessException
	 * @author changyf
	 * createdate 2012-08-02
	 */
	public void save(Object pojo,String userId)throws BusinessException;
	
	public void update(Object pojo)throws BusinessException;
	
	/**
	 * 执行修改操作
	 * @param pojo 待修改的对象
	 * @param userId 操作者id
	 * @throws BusinessException
	 * @author changyf
	 * createdate 2012-08-02
	 */
	public void update(Object pojo,String userId)throws BusinessException;
	
	public void merge(Object pojo)throws BusinessException;
	
	public Page findByCritera(DetachedCriteria dc,Page page)throws BusinessException;
	
	public List findByCritera(DetachedCriteria dc)throws BusinessException;
	
	/**
	 * 保存获取com_log_operate日志对象
	 * 
	 * @param systemSystemCodeCn
	 *            操作区分操作类型(系统、结构、LHIRF、区域)
	 * @param user
	 *            当前用户
	 * @param op_page
	 *           操作页面
	 * @author jiayanyin createdate 2012-12-10
	 */
	public void saveComLogOperate( ComUser user,
			String op_page, String source_system);
	
}