package com.richong.arch.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;

import com.richong.arch.web.Page;

@SuppressWarnings("rawtypes")
public interface IDAO {
	
	/**
	 * 通过ID获得实体
	 * @param pojoClass
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	public Object loadById(Class pojoClass,Serializable id) throws DAOException;
 
    /**
	 * 执行添加，删除，修改操作，根据hql语句
	 * @param hqlString
	 * @param values
	 */
    public void execute(final String hqlString, final Object[] values)throws DAOException;
    
    /**
	 * 执行SQL
	 * @param sql（增删改）
	 */
	public void executeBySql(String sql);
    
    public Page findByHql(Page page, String hqlString, Object[] values);
    
    public Page findBySql(Page page, String sqlString, Object[] values);
    
    /**
	 * 删除实体 POJO
	 * 
	 * @param basePOJO
	 */
    public Boolean delete(Object pojo) throws DAOException;
   
    
    
	public void saveOrUpdate(Object pojo) throws DAOException ;
	
	
	public void merge(Object pojo) throws DAOException;
	
	/**
	 * 创建实体
	 * 
	 * @param basePOJO
	 *            the new identifier
	 * @return 返回新实体主键
	 */
	public Object save(Object basePOJO)throws DAOException;;

	/**
	 * 更新实体
	 * 
	 * @param basePOJO
	 */
	public Boolean update(Object basePOJO)throws DAOException;;



	/**
	 * 加载lazy为假的实体
	 * 
	 * @param object
	 */
	public void initialLazyObject(Object object) throws DAOException;;

	/**
	 * 获取所有实体
	 * 
	 * @param entityClass
	 * @return List list of BasePOJO
	 */
	public List findAllPOJO(final Class entityClass)throws DAOException;
	
	/**
	 * 提交Hibernate缓存数据到数据库缓存数据。没有真正的Commint
	 */
	public void flush() ;

	/**
	 * 根据HQL查询数据集
	 * 
	 * @param hqlString
	 *            HQL语法
	 * @param parameter
	 *            参数值集合 注意参数的类型一定要跟数据库字段类型一致
	 * 
	 * @return List
	 * @throws DaoException
	 */
	public List findByHql(final String hqlString, final Object[] values)throws DAOException;;

	/**
	 * 根据条件输出指定条记录
	 * 
	 * @param detachedCriteria
	 * @param size
	 * @return
	 */
	public Page findByCriteria(final DetachedCriteria detachedCriteria,
			final int size) throws DAOException;
	
	
	/**
	 * 根据条件输出指定栏位的size条记录
	 * 
	 * @param detachedCriteria
	 * @param size
	 * @return
	 */
	public Page findByCriteria(final DetachedCriteria detachedCriteria,
			final ProjectionList projectionList, final Object entity,
			final int size) throws DAOException;

	/**
	 * 根据条件输出指定栏位的记录
	 * 
	 * @param detachedCriteria
	 * @param size
	 * @return
	 */
	public Page findByCriteria(final DetachedCriteria detachedCriteria,
			final ProjectionList projectionList, final Object entity) throws DAOException;

	/**
	 * 取一定量的记录
	 * 
	 * 
	 * 
	 * @param detachedCriteria
	 *            条件
	 * 
	 * @return List
	 * @throws DaoException
	 */
	public Page findByCriteria(final DetachedCriteria detachedCriteria,
			final Page page)throws DAOException;
	
	/**
	 * 按照查询栏位和条件进行分页
	 * 
	 * @param detachedCriteria
	 * @param projectionList
	 * @param page
	 * @return
	 */
	public Page findByCriteria(final DetachedCriteria detachedCriteria,
			ProjectionList projectionList, final Page page, final Object entity)throws DAOException;

	/**
	 * 根据detachedCriteria查询所有实体
	 * 
	 * @param detachedCriteria
	 * @return List
	 * @throws DaoException
	 */
	public List findByCriteria(final DetachedCriteria detachedCriteria);

	public List executeQueryBySql(String sql) throws DAOException;
}
