package com.richong.arch.bo.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;

import com.richong.arch.dao.DAOException;
import com.richong.arch.base.BasicTypeUtils;

@SuppressWarnings({"rawtypes","unchecked"})
public class LogDetail {	
	// 获取数据操作详细信息
	public String getPOJOSql(Object pojo) {
		String lTName = null;
		String totalName = null;
		String entityName = pojo.getClass().getName();
		Configuration cfg = new Configuration();
		PersistentClass persistentClass = cfg.getClassMapping(entityName);
		if (persistentClass == null) {
			cfg = cfg.addClass(pojo.getClass());
			persistentClass = cfg.getClassMapping(entityName);
			totalName = persistentClass.getTable() + "";

		}
		lTName = totalName.substring(totalName.lastIndexOf("(") + 1, totalName
				.lastIndexOf(")"));
		// String pkName = persistentClass.getIdentifierProperty().getName();

		String sql = "   ";
		List<String> mList = new ArrayList<String>();
		List vList = new ArrayList();

		Class c = pojo.getClass();
		Method[] methods = c.getMethods();

		Field[] fields = c.getDeclaredFields();
		Field.setAccessible(fields, true);
		for (Field field : fields) {
			String str = field.getName();
			mList.add(str);

		}
		sql += lTName + "(";

		for (Method method : methods) {
			String mName = method.getName();
			if (mName.startsWith("get") && !mName.startsWith("getClass")
					&& !mName.startsWith("getJsonFieldValueMap")
					&& !mName.startsWith("getStatusZh")) {
				try {
					Object value = method.invoke(pojo, null);
					if (value instanceof String) {
						vList.add("\"" + value + "\"");

					} else {
						vList.add(value);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		for (int i = 0; i < mList.size() && i < vList.size(); i++) {
			if ((i < mList.size() - 1) && (i < vList.size() - 1)) {

				sql += mList.get(i) + ":";
				sql += vList.get(i) + ";";
			} else if ((i == (mList.size() - 1)) && (i == (vList.size() - 1))) {

				sql += vList.get(i) + ":";
				sql += vList.get(i) + "。)";
			}
		}
		return sql;
	}
    

	
	
	// 管理日志表字段及字段值
	public static HashMap<String, Object> getPOJOSql123(Object pojo) {
		HashMap<String, Object> hm = new HashMap<String, Object>();
		
        // 通过反射获取实体类的相关字段及他的值	
		Class c = pojo.getClass();
		Method[] methods = c.getMethods();
		List<String> mList = new ArrayList<String>();
		List vList = new ArrayList();

		for (Method method : methods) {
			String mName = method.getName();
			if (mName.startsWith("get") && !mName.startsWith("getClass")
					&& !mName.startsWith("getJsonFieldValueMap")
					&& !mName.startsWith("getStatusZh")) {
				String fieldName = mName.substring(3, mName.length());
				mList.add(fieldName);
				try {
					Object value = method.invoke(pojo, null);
					if (value instanceof String) {
						vList.add("\"" + value + "\"");

					} else {
						vList.add(value);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}

		}

		for (int i = 0; i < mList.size() && i < vList.size(); i++) {
			if ((i < mList.size() - 1) && (i < vList.size() - 1)) {

				hm.put(mList.get(i), vList.get(i));

			} else if ((i == (mList.size() - 1)) && (i == (vList.size() - 1))) {
				hm.put(mList.get(i), vList.get(i));

			}
		}
		return hm;
	}

	// 获取日志表字段以及字段值
	public List getFieldsValue(Object pojo) {

		List list = new LinkedList();
		// 获取时间
		Date lDate = BasicTypeUtils.getCurrentDateforSQL();		

		// 获取操作详细信息
		//String lDetail = getPOJOSql(pojo);
		// 获取日志操作类型
		String lType = judgePOJO(pojo);

		// 获取操作表

		String entityName = pojo.getClass().getName();
		String lTName = null;
		String totalName = null;
		Configuration cfg = new Configuration();
		PersistentClass persistentClass = cfg.getClassMapping(entityName);
		if (persistentClass == null) {
			cfg = cfg.addClass(pojo.getClass());
			persistentClass = cfg.getClassMapping(entityName);
			totalName = persistentClass.getTable() + "";

		}
		lTName = totalName.substring(totalName.lastIndexOf("(") + 1, totalName
				.lastIndexOf(")"));
		// String pkName = persistentClass.getIdentifierProperty().getName();

		//list.add(userName);
		list.add(lDate);
		list.add(lType);
		list.add(lTName);
		//list.add(lDetail);

		return list;
	}

	// 判断插入/更新操作
	public String judgePOJO(Object pojo) throws DAOException {
		// 根据实体类得到实体名字
		String entityName = pojo.getClass().getName();

		Configuration cfg = new Configuration();
		PersistentClass persistentClass = cfg.getClassMapping(entityName);
		// String str =null;
		if (persistentClass == null) {
			cfg = cfg.addClass(pojo.getClass());
			persistentClass = cfg.getClassMapping(entityName);

		}

		String pkName = persistentClass.getIdentifierProperty().getName();

		HashMap hm = getPOJOSql123(pojo);
		String temPKName = null;

		temPKName = pkName.replaceFirst(pkName.substring(0, 1), pkName
				.substring(0, 1).toUpperCase());

		Serializable temId = (Serializable) hm.get(temPKName);

		if (temId == null) {
			return "insert";
		} else {
			return "update";
		}
		
	}	

}
