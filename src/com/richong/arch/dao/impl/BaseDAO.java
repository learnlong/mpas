package com.richong.arch.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.richong.arch.dao.DAOException;
import com.richong.arch.dao.IDAO;
import com.richong.arch.web.Page;


/**
 * 基类DAO实现类
 * 
 * @author 侯青春
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class BaseDAO extends HibernateDaoSupport implements IDAO{

	/**
	 * 根据主键加载实体
	 * 
	 * @param entityClass
	 * @param id
	 * @return 查询到返回实体，否则返回null
	 */
	public Object loadById(Class pojoClass, Serializable id)
			throws DAOException {
		return (Object) this.getHibernateTemplate().get(pojoClass, id);
	}
	
	
	public Boolean delete(Object basePOJO) {
		this.getHibernateTemplate().delete(basePOJO);
		return Boolean.TRUE;
	}
	
	/**
	 * 执行SQL
	 * @param sql（增删改）
	 */
	public void executeBySql(String sql){
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Transaction transaction = session.beginTransaction();
		Query q = session.createSQLQuery(sql);
		q.executeUpdate();
		transaction.commit();
	}
	
	/**
	 * 
	 * @version 2012-8-17 zhouli<br>
	 *          内隐类，供分页查询使用
	 * 
	 */
	class FindPageBySQLQueryHibernateCallback implements HibernateCallback {
		private Page page;
		private String sqlString; 
		private Object[] values;
		
		/**
		 * 根据条件分页查询
		 * 
		 * @param detachedCriteria
		 * @param page
		 */
		public FindPageBySQLQueryHibernateCallback(Page page, String sqlString, Object[] values) {
			this.page = page;
			this.sqlString = sqlString;
			this.values = values;
		}
		
		public Page doInHibernate(Session session) throws HibernateException, SQLException {
			Query query = session.createSQLQuery(sqlString);
			if (values != null) {
		        for (int i = 0 ; i < values.length ; i++)
		            query.setParameter( i, values[i]);
			}
			int totalCount = 0;
			List list = query.list();
			if(list != null){
				totalCount = list.size();
			}
			page.setTotalCount(totalCount);
	        
			List result = query.setFirstResult(page.getStartIndex())
					.setMaxResults(page.getPageSize())
					.list();
			page.setResult(result);
			return page;
		}
	}
	
	public Page findBySql(Page page, String sqlString, Object[] values) {
		HibernateCallback callback = new FindPageBySQLQueryHibernateCallback(page, sqlString, values);
		return (Page) this.getHibernateTemplate().execute(callback);
	}
	


	/**
	 * 
	 * @version 2010-8-17 zhouli<br>
	 *          内隐类，供分页查询使用
	 * 
	 */
	class FindPageByQueryHibernateCallback implements HibernateCallback {
		private Page page;
		private String hqlString; 
		private Object[] values;
		
		/**
		 * 根据条件分页查询
		 * 
		 * @param detachedCriteria
		 * @param page
		 */
		public FindPageByQueryHibernateCallback(Page page, String hqlString, Object[] values) {
			this.page = page;
			this.hqlString = hqlString;
			this.values = values;
		}
		
		public Page doInHibernate(Session session) throws HibernateException, SQLException {
			Query query = session.createQuery(hqlString);
			if (values != null) {
		        for (int i = 0 ; i < values.length ; i++)
		            query.setParameter( i, values[i]);
			}
			int totalCount = 0;
			List list = query.list();
			if(list != null){
				totalCount = list.size();
			}
			page.setTotalCount(totalCount);
	        
			List result = query.setFirstResult(page.getStartIndex())
					.setMaxResults(page.getPageSize())
					.list();
			page.setResult(result);
			return page;
		}
	}
	
	public Page findByHql(Page page, String hqlString, Object[] values) {
		HibernateCallback callback = new FindPageByQueryHibernateCallback(page, hqlString, values);
		return (Page) this.getHibernateTemplate().execute(callback);
	}

	public void saveOrUpdate(Object pojo) throws DAOException {
		this.getHibernateTemplate().saveOrUpdate(pojo);
	}
	
	public void merge(Object pojo) throws DAOException {
		this.getHibernateTemplate().merge(pojo);
	}


	public Object save(Object basePOJO) {
		return (Object) this.getHibernateTemplate().save(basePOJO);
	}

	public Boolean update(Object basePOJO) {
		this.getHibernateTemplate().update(basePOJO);

		return Boolean.TRUE;
	}
	/**
	 * 通过提交SQL文来查询数据list
	 */
	public List executeQueryBySql(String sql){
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Transaction transaction = session.beginTransaction();
		Query q = session.createSQLQuery(sql);
		List list = q.list();
		transaction.commit();
		if(!list.isEmpty()&&list.size()>0){
			return list;
		}
		return null;
	}

	/**
	 * 通过提交SQL和pojo类文来查询数据list
	 */
	public List executeQueryBySql(String sql,Class pojo){
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Transaction transaction = session.beginTransaction();
		Query q = session.createSQLQuery(sql).addEntity(pojo);
		List list = q.list();
		transaction.commit();
		return list;
	}
	
	/**
	 * 加载lazy为假的实体
	 * 
	 * @param object
	 */
	public void initialLazyObject(Object object) {
		this.getHibernateTemplate().initialize(object);
	}

	/**
	 * 获取所有实体
	 * 
	 * @param entityClass
	 * @return List list of BasePOJO
	 */
	public List findAllPOJO(final Class entityClass) {
		return this.getHibernateTemplate().loadAll(entityClass);
	}

	/**
	 * 提交Hibernate缓存数据到数据库缓存数据。没有真正的Commint
	 */
	public void flush() {
		this.getHibernateTemplate().flush();
	}

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
	
	public List<T> findByHql(final String hqlString, final Object[] values) {
		return getHibernateTemplate().find(hqlString, values);
	}
	
	
	/**
	 * 执行添加，删除，修改操作，根据hql语句
	 * @param hqlString
	 * @param values
	 */
	public void execute(final String hqlString, final Object[] values) throws DAOException{
		try{
			HibernateCallback callback =  new ExcuteCallBack(hqlString, values);
			this.getHibernateTemplate().execute(callback);
		}catch( Throwable e){
			throw new DAOException("Operate Failed：", e );
		}
	}

	/**
	 * 根据条件输出指定条记录
	 * 
	 * @param detachedCriteria
	 * @param size
	 * @return
	 */
	public Page findByCriteria(final DetachedCriteria detachedCriteria,
			final int size) throws HibernateException{
		HibernateCallback callback = new FindPageByCriteriaHibernateCallback(
				detachedCriteria, size);
		return (Page) this.getHibernateTemplate().execute(callback);
	}

	/**
	 * 根据条件输出指定栏位的size条记录
	 * 
	 * @param detachedCriteria
	 * @param size
	 * @return
	 */
	public Page findByCriteria(final DetachedCriteria detachedCriteria,
			final ProjectionList projectionList, final Object entity,
			final int size) throws HibernateException {
		Page page = new Page();
		page.setPageSize(size);
		HibernateCallback callback = new FindPageByCriteriaHibernateCallback(
				detachedCriteria, page, projectionList, entity);
		return (Page) this.getHibernateTemplate().execute(callback);
	}

	/**
	 * 根据条件输出指定栏位的记录
	 * 
	 * @param detachedCriteria
	 * @param size
	 * @return
	 */
	public Page findByCriteria(final DetachedCriteria detachedCriteria,
			final ProjectionList projectionList, final Object entity) throws HibernateException{
		HibernateCallback callback = new FindPageByCriteriaHibernateCallback(
				detachedCriteria, projectionList, entity);
		return (Page) this.getHibernateTemplate().execute(callback);
	}

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
			final Page page) throws HibernateException{
		HibernateCallback callback = new FindPageByCriteriaHibernateCallback(
				detachedCriteria, page);
		return (Page) this.getHibernateTemplate().execute(callback);
	}

	/**
	 * 按照查询栏位和条件进行分页
	 * 
	 * @param detachedCriteria
	 * @param projectionList
	 * @param page
	 * @return
	 */
	public Page findByCriteria(final DetachedCriteria detachedCriteria,
			ProjectionList projectionList, final Page page, final Object entity) {
		HibernateCallback callback = new FindPageByCriteriaHibernateCallback(
				detachedCriteria, page, projectionList, entity);
		return (Page) this.getHibernateTemplate().execute(callback);
	}

	/**
	 * 根据detachedCriteria查询所有实体
	 * 
	 * @param detachedCriteria
	 * @return List
	 * @throws DaoException
	 */
	public List findByCriteria(final DetachedCriteria detachedCriteria) {
		return this.getHibernateTemplate().findByCriteria(detachedCriteria);
	}

	/**
	 * 根据DetachedCriteria获取记录集数量 查询记录数时按用户指定的栏位
	 * 
	 * @param detachedCriteria
	 *            查询条件
	 * @param propertyName
	 *            统计的栏位名称
	 * @return
	 */
	public int countEntities(final DetachedCriteria detachedCriteria) {
		return ((Integer) this.getHibernateTemplate().findByCriteria(
				detachedCriteria.setProjection(Projections.count("id"))).get(0))
				.intValue();
	}

	

}

/**
 * 根据sql语句执行操作
 * @author liusheng
 *
 */
class ExcuteCallBack implements HibernateCallback{
	
	private String hql ;
	
	private Object[] values;
	
	
	public ExcuteCallBack(final String hql, final Object[] values){
		this.hql = hql;
		this.values = values ;
	}

	public Object doInHibernate(Session session) throws HibernateException,
			SQLException {
		Query query = session.createQuery(hql) ;
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		query.executeUpdate() ;
		return null;
	}
	
}

/**
 * 
 * @version 2010-1-31 侯青春<br>
 *          内隐类，供分页查询使用，可指定输出栏位
 * 
 */
class FindPageByCriteriaHibernateCallback implements HibernateCallback {
	private DetachedCriteria detachedCriteria;

	// 根据条件输出制定栏位
	private ProjectionList projectionList;
	private Object entity;
	private Page page;
	private int fetchSize;

	/**
	 * 根据条件输出指定栏位的list
	 * 
	 * @param detachedCriteria
	 * @param projectionList
	 * @param entity
	 */
	public FindPageByCriteriaHibernateCallback(
			DetachedCriteria detachedCriteria, int fetchSize) {
		this.detachedCriteria = detachedCriteria;
		this.fetchSize = fetchSize;
	}

	/**
	 * 根据条件输出指定栏位的list
	 * 
	 * @param detachedCriteria
	 * @param projectionList
	 * @param entity
	 */
	public FindPageByCriteriaHibernateCallback(
			DetachedCriteria detachedCriteria, ProjectionList projectionList,
			Object entity) {
		this.detachedCriteria = detachedCriteria;
		this.projectionList = projectionList;
		this.entity = entity;
	}

	/**
	 * 根据条件分页查询
	 * 
	 * @param detachedCriteria
	 * @param page
	 */
	public FindPageByCriteriaHibernateCallback(
			DetachedCriteria detachedCriteria, Page page) {
		this.detachedCriteria = detachedCriteria;
		this.page = page;
	}

	/**
	 * 根据条件和输出栏位制定查询
	 * 
	 * @param detachedCriteria
	 * @param page
	 * @param projectionList
	 * @param entity
	 */
	public FindPageByCriteriaHibernateCallback(
			DetachedCriteria detachedCriteria, Page page,
			ProjectionList projectionList, Object entity) {
		this.detachedCriteria = detachedCriteria;
		this.page = page;
		this.projectionList = projectionList;
		this.entity = entity;
	}

	/**
	 * 根据条件和输出栏位定制查询，并进行分页
	 * 
	 * @param detachedCriteria
	 * @param page
	 * @param projectionList
	 */
	public FindPageByCriteriaHibernateCallback(
			DetachedCriteria detachedCriteria, Page page,
			ProjectionList projectionList) {
		this.detachedCriteria = detachedCriteria;
		this.page = page;
		this.projectionList = projectionList;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page doInHibernate(Session session) throws HibernateException {
		Criteria criteria = detachedCriteria.getExecutableCriteria(session);

		// 有指定输出栏位（可实现lazy）
		if (this.projectionList != null && this.entity != null) {
			criteria.setProjection(this.projectionList).setResultTransformer(
					new AliasToBeanResultTransformer((Class) entity));
		}

		// 有分页
		if (page != null) {

            CriteriaImpl impl = (CriteriaImpl) criteria;
            List orderEntrys = new ArrayList();
            try{
                Field field = CriteriaImpl.class.getDeclaredField("orderEntries");
                field.setAccessible(true);  
                orderEntrys = (List) field.get(impl);
                field.set(criteria,new ArrayList());
            }catch(Exception ex){
                ex.printStackTrace();
            }
			
			Object obj = null;
			try{
				obj = criteria.setProjection(Projections.count("id"))
						.uniqueResult();
			}catch(Exception e){
				//没有id字段，用*代替!!!!!!!!!!!!!!
				obj = criteria.setProjection(Projections.count("*")).uniqueResult();
			}

			int totalCount = ((Integer) obj).intValue();
			page.setTotalCount(totalCount);

            try{
                Field field = CriteriaImpl.class.getDeclaredField("orderEntries");
                field.setAccessible(true);  
                for(int i=0; i<orderEntrys.size(); i++){
                    List innerOrderEntries = (List) field.get(criteria);
                    innerOrderEntries.add(orderEntrys.get(i));
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
			
			criteria.setProjection(null);

			criteria.setFirstResult(page.getStartIndex());
			criteria.setMaxResults(	page.getPageSize());

			// 有指定输出栏位（可实现lazy）
			if (this.projectionList != null && this.entity != null) {
				criteria.setProjection(this.projectionList).setResultTransformer(
						new AliasToBeanResultTransformer((Class) entity));
			}else{
				criteria.setProjection(null);
				criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
			}
		}
		// 查询前n条记录
		else if (this.fetchSize > 0) {
			criteria.setFirstResult(0).setMaxResults(this.fetchSize);
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		
		page.setResult( criteria.list() );

		return page;
	}
	
}
