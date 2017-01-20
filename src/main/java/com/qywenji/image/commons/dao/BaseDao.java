package com.qywenji.image.commons.dao;


import com.qywenji.image.commons.Constants;
import com.qywenji.image.commons.bean.DataTableReturnObject;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;


public abstract class BaseDao<T> extends HibernateDaoSupport
{

	private Class<T> entityClass;

	@Resource(name = "sessionFactory")
	protected SessionFactory sessionFactory;

	public Session getSession()
	{
		return sessionFactory.getCurrentSession();
	}

	@Autowired
	public void setSessionFactoryOverride(SessionFactory sessionFactory)
	{
		super.setSessionFactory(sessionFactory);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BaseDao()
	{
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		entityClass = (Class) params[0];
	}

	public T save(T t)
	{
		super.getHibernateTemplate().saveOrUpdate(t);
		return t;
	}

	public void del(T t)
	{
		super.getHibernateTemplate().delete(t);
	}

	@SuppressWarnings("unchecked")
	public List<T> list(String hql, Object... values)
	{
		return (List<T>) super.getHibernateTemplate().find(hql, values);
	}

	public List<T> list(){
		String hql = "from "+entityClass.getName()+" ";
		return list(hql);
	}
	public T getById(Serializable id)
	{
		return super.getHibernateTemplate().get(entityClass, id);
	}
	
	@SuppressWarnings("unchecked")
    public T get(String hql ,Object... values){
		Query q = super.currentSession().createQuery(hql);
		if(values !=null && values.length>0){
			for(int i =0 ;i<values.length;i++){
				q.setParameter(i, values[i]);
			}
		}
		return (T) q.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
    public List<T> page(String hql,int first,int pageSize,Object... values)
	{
		Query q = super.currentSession().createQuery(hql);
		q.setFirstResult(first);
		q.setMaxResults(pageSize);
		if(values !=null && values.length>0){
			for(int i =0 ;i<values.length;i++){
				q.setParameter(i, values[i]);
			}
		}
		return q.list();
	}
	
	@SuppressWarnings("unchecked")
	private DetachedCriteria createSearchCondition(Map<String, Map<String, Object>> all_search,
			DetachedCriteria criteria)
	{
		Map<String,List<Criterion>> relateTable = new HashMap<>();
		if (all_search.get(Constants.SEARCHCONDITION_EQ) != null
		    && !all_search.get(Constants.SEARCHCONDITION_EQ).isEmpty())
		{
			Map<String, Object> searchCondition = all_search.get(Constants.SEARCHCONDITION_EQ);
			for (Entry<String, Object> entity : searchCondition.entrySet())
			{
				if (entity.getKey().contains("."))
				{
					String[] foo = entity.getKey().split("\\.");
					if(relateTable.get(foo[0]) != null){
						List<Criterion> ls = relateTable.get(foo[0]);
						ls.add(Restrictions.eq(foo[1], entity.getValue()));
						relateTable.put(foo[0], ls);
					}else{
						List<Criterion> ls = new ArrayList<>();
						ls.add(Restrictions.eq(foo[1], entity.getValue()));
						relateTable.put(foo[0], ls);
					}
				}
				else
				{
					criteria.add(Restrictions.eq(entity.getKey(), entity.getValue()));
				}
			}
		}
		if (all_search.get(Constants.SEARCHCONDITION_BETWEEN) != null
		    && !all_search.get(Constants.SEARCHCONDITION_BETWEEN).isEmpty())
		{
			Map<String, Object> searchCondition = all_search.get(Constants.SEARCHCONDITION_EQ);
			for (Entry<String, Object> entity : searchCondition.entrySet())
			{
				List<Object> data = (List<Object>) entity.getValue();
				if (data == null || data.size() != 2)
					continue;
				if (entity.getKey().contains("."))
				{
					String[] foo = entity.getKey().split("\\.");
					if(relateTable.get(foo[0]) != null){
						List<Criterion> ls = relateTable.get(foo[0]);
						ls.add(Restrictions.between(foo[1], data.get(0), data.get(1)));
						relateTable.put(foo[0], ls);
					}else{
						List<Criterion> ls = new ArrayList<>();
						ls.add(Restrictions.between(foo[1], data.get(0), data.get(1)));
						relateTable.put(foo[0], ls);
					}
				}
				else
				{
					criteria.add(Restrictions.between(entity.getKey(), data.get(0), data.get(1)));
				}
			}
		}
		if (all_search.get(Constants.SEARCHCONDITION_GT) != null
		    && !all_search.get(Constants.SEARCHCONDITION_GT).isEmpty())
		{
			Map<String, Object> searchCondition = all_search.get(Constants.SEARCHCONDITION_GT);
			for (Entry<String, Object> entity : searchCondition.entrySet())
			{
				if (entity.getKey().contains("."))
				{
					String[] foo = entity.getKey().split("\\.");
					if(relateTable.get(foo[0]) != null){
						List<Criterion> ls = relateTable.get(foo[0]);
						ls.add(Restrictions.gt(foo[1], entity.getValue()));
						relateTable.put(foo[0], ls);
					}else{
						List<Criterion> ls = new ArrayList<>();
						ls.add(Restrictions.gt(foo[1], entity.getValue()));
						relateTable.put(foo[0], ls);
					}
				}
				else
				{
					criteria.add(Restrictions.ge(entity.getKey(), entity.getValue()));
				}
			}
		}
		if (all_search.get(Constants.SEARCHCONDITION_IN) != null
		    && !all_search.get(Constants.SEARCHCONDITION_IN).isEmpty())
		{
			Map<String, Object> searchCondition = all_search.get(Constants.SEARCHCONDITION_IN);
			for (Entry<String, Object> entity : searchCondition.entrySet())
			{
				List<Object> data = (List<Object>) entity.getValue();
				if (entity.getKey().contains("."))
				{
					String[] foo = entity.getKey().split("\\.");
					if(relateTable.get(foo[0]) != null){
						List<Criterion> ls = relateTable.get(foo[0]);
						ls.add(Restrictions.in(foo[1], data));
						relateTable.put(foo[0], ls);
					}else{
						List<Criterion> ls = new ArrayList<>();
						ls.add(Restrictions.in(foo[1], data));
						relateTable.put(foo[0], ls);
					}
				}
				else
				{
					criteria.add(Restrictions.in(entity.getKey(), data));
				}
			}
		}
		if (all_search.get(Constants.SEARCHCONDITION_LIKE) != null
		    && !all_search.get(Constants.SEARCHCONDITION_LIKE).isEmpty())
		{
			Map<String, Object> searchCondition = all_search.get(Constants.SEARCHCONDITION_LIKE);
			for (Entry<String, Object> entity : searchCondition.entrySet())
			{
				if (entity.getKey().contains("."))
				{
					String[] foo = entity.getKey().split("\\.");
					if(relateTable.get(foo[0]) != null){
						List<Criterion> ls = relateTable.get(foo[0]);
						ls.add( Restrictions.like(foo[1], String.valueOf(entity.getValue()),
						        MatchMode.ANYWHERE));
						relateTable.put(foo[0], ls);
					}else{
						List<Criterion> ls = new ArrayList<>();
						ls.add( Restrictions.like(foo[1], String.valueOf(entity.getValue()),
						        MatchMode.ANYWHERE));
						relateTable.put(foo[0], ls);
					}
				}
				else
				{
					criteria.add(Restrictions.like(entity.getKey(),
					    String.valueOf(entity.getValue()), MatchMode.ANYWHERE));
				}
			}
		}
		if (all_search.get(Constants.SEARCHCONDITION_LT) != null
		    && !all_search.get(Constants.SEARCHCONDITION_LT).isEmpty())
		{
			Map<String, Object> searchCondition = all_search.get(Constants.SEARCHCONDITION_LT);
			for (Entry<String, Object> entity : searchCondition.entrySet())
			{
				if (entity.getKey().contains("."))
				{
					String[] foo = entity.getKey().split("\\.");
					if(relateTable.get(foo[0]) != null){
						List<Criterion> ls = relateTable.get(foo[0]);
						ls.add(Restrictions.le(foo[1], entity.getValue()));
						relateTable.put(foo[0], ls);
					}else{
						List<Criterion> ls = new ArrayList<>();
						ls.add( Restrictions.le(foo[1], entity.getValue()));
						relateTable.put(foo[0], ls);
					}
				}
				else
				{
					criteria.add(Restrictions.le(entity.getKey(), entity.getValue()));
				}
			}
		}
		if (all_search.get(Constants.SEARCHCONDITION_NOT) != null
		    && !all_search.get(Constants.SEARCHCONDITION_NOT).isEmpty())
		{
			Map<String, Object> searchCondition = all_search.get(Constants.SEARCHCONDITION_NOT);
			for (Entry<String, Object> entity : searchCondition.entrySet()){
				if (entity.getKey().contains("."))
				{
					String[] foo = entity.getKey().split("\\.");
					
					if(relateTable.get(foo[0]) != null){
						List<Criterion> ls = relateTable.get(foo[0]);
						ls.add(Restrictions.neOrIsNotNull(foo[1], entity.getValue()));
						relateTable.put(foo[0], ls);
					}else{
						List<Criterion> ls = new ArrayList<>();
						ls.add(Restrictions.neOrIsNotNull(foo[1], entity.getValue()));
						relateTable.put(foo[0], ls);
					}
				}
				else
				{
					criteria.add(Restrictions.neOrIsNotNull(entity.getKey(), entity.getValue()));
				}
			}
		}
		
		if(!relateTable.isEmpty()){
			Set<Entry<String, List<Criterion>>> set = relateTable.entrySet();
			for(Entry<String, List<Criterion>> s : set){
				DetachedCriteria c = criteria.createCriteria(s.getKey(),s.getKey());
				for(Criterion foo : s.getValue() ){
					c.add(foo);
				}
			}
		}
		return criteria;
	}

	private DetachedCriteria createOrderCondition(DetachedCriteria criteria, Map<String, List<String>> all_order)
	{
		if (all_order.get(Constants.ORDER_ASC) != null
		    && !all_order.get(Constants.ORDER_ASC).isEmpty())
			for (String property : all_order.get(Constants.ORDER_ASC))
				criteria.addOrder(Order.asc(property));
		if (all_order.get(Constants.ORDER_DESC) != null
		    && !all_order.get(Constants.ORDER_DESC).isEmpty())
			for (String property : all_order.get(Constants.ORDER_DESC))
				criteria.addOrder(Order.desc(property));
		return criteria;
	}

	/**
	 * Constants.SEARCHCONDITION > Constants.SEARCHCONDITION_EQ ... > key >
	 * value Constants.ORDER_CONDITION > (Constants.ORDER_ASC ... > propertylist
	 * 
	 * @param dataMap
	 * @param start
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DataTableReturnObject<T> getPage(Map<String, Map<String, ?>> dataMap, int start,
			Integer pageSize)
			{
		DetachedCriteria criteria = createBaseCriteria(dataMap);
		start = start < 0 ? 0 : start;
		Criteria c = criteria.getExecutableCriteria(super.currentSession());
		List<T> list = null;
		if(pageSize != null)
			list = c.setFirstResult(start).setMaxResults(pageSize).list(); 
		else
			list = c.setFirstResult(start).list(); 
		Number totalCount = (Number) c.setProjection(Projections.rowCount()).setFirstResult(0).setMaxResults(1).uniqueResult();   
		return new DataTableReturnObject<T>(totalCount.longValue(), list);
			}
	
	@SuppressWarnings("unchecked")
	public List<T> getData(Map<String, Map<String, ?>> dataMap)
	{
		DetachedCriteria criteria = createBaseCriteria(dataMap);
		Criteria c = criteria.getExecutableCriteria(super.currentSession());
		return c.list();
	}
	
	@SuppressWarnings("unchecked")
	public DataTableReturnObject<T> getPageDistinct(Map<String, Map<String, ?>> dataMap, int start,
	    int pageSize,String id)
	{
		DetachedCriteria criteria = createBaseCriteria(dataMap);
		criteria.setProjection(Projections.countDistinct(id == null?"id" : id));
		Criteria c = criteria.getExecutableCriteria(super.currentSession());
		Number totalCount = (Number) c.setFirstResult(0).setMaxResults(1).uniqueResult();   
		List<Number> list = c.setProjection(Projections.distinct(Projections.id())).setFirstResult(start).setMaxResults(pageSize).list();
		List<T> result = new ArrayList<>();
		if(list != null && list.size()>0){
			Criteria res = super.currentSession().createCriteria(entityClass);
			res.add(Restrictions.in(id == null?"id" : id, list));
			result = res.list();
		}
		return new DataTableReturnObject<T>(totalCount.longValue(), result);
	}
	
	public DataTableReturnObject<T> getPage(Map<String, Map<String, ?>> dataMap, int start,
	    int pageSize,boolean distinct,String idName){
		if(distinct){
			return getPageDistinct(dataMap, start, pageSize, idName);
		}else{
			return getPage(dataMap, start, pageSize);
		}
	}

	@SuppressWarnings("unchecked")
	protected DetachedCriteria createBaseCriteria(Map<String, Map<String, ?>> dataMap)
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(entityClass,"main");
		if (dataMap == null || dataMap.isEmpty())
			return criteria;
		if (dataMap.get(Constants.SEARCHCONDITION) != null
		    && !dataMap.get(Constants.SEARCHCONDITION).isEmpty())
		{
			Map<String, Map<String, Object>> all_search =
			    (Map<String, Map<String, Object>>) dataMap.get(Constants.SEARCHCONDITION);
			criteria = createSearchCondition(all_search, criteria);
		}
		if (dataMap.get(Constants.ORDER_CONDITION) != null
		    && !dataMap.get(Constants.ORDER_CONDITION).isEmpty())
		{
			Map<String, List<String>> all_order =
			    (Map<String, List<String>>) dataMap.get(Constants.ORDER_CONDITION);
			criteria = createOrderCondition(criteria, all_order);
		}
		return criteria;
	}
	
	public List<T> findBySQL(final String sql, final Class<T> clazz, final Object... params) {
		return (List<T>) getHibernateTemplate().execute(new HibernateCallback<List<T>>() {
			@SuppressWarnings("unchecked")
			public List<T> doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(sql);
				query.addEntity("bean", clazz);
				buildParameters(query, params);
				return query.list();
			}
		});
	}
	
	protected void buildParameters(Query query, Object ... params) {
		int flag = 0;
		if (params == null || params.length == 0) {
			return;
		}
		for (Object item : params) {
			query.setParameter(flag++, item);
		}
	}
	
	/**
	* @Title: update 
	* @Description: 修改操作 
	* @param @param clazz
	* @param @param mapSet
	* @param @param mapWhere  
	* @return void
	 */
	public int update(Map<String, Object> mapSet, Map<String, Object> mapWhere){
		if (mapSet == null || mapSet.size() == 0 || mapWhere == null || mapWhere.size() == 0 ) {
			return 0;
		}
		StringBuffer _hql = new StringBuffer("update");
		List<Object> _objs = new ArrayList<Object>();
		_hql.append(" " + entityClass.getName());
		int m = 0;
		_hql.append(" set");
		for (Entry<String, Object> entry : mapSet.entrySet()) {
			if(m++>0)_hql.append(" ,");
			_hql.append(" "+entry.getKey()+"=?");
			_objs.add(entry.getValue());
		}
		m = 0;
		_hql.append(" where");
		for (Entry<String, Object> entry : mapWhere.entrySet()) {
			if(m++>0)_hql.append(" and");
			_hql.append(" "+entry.getKey()+"=?");
			_objs.add(entry.getValue());
		}
		
		String hql = _hql.toString();
		Object[] objs = _objs.toArray();
		return executeUpdate(hql, objs);
	}
	
	/**
	 * 更新
	 * @param hql
	 * @param params
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int executeUpdate(final String hql, final Object[] params) {
		return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				buildParameters(query, params);
				return query.executeUpdate();
			}
		});
	}

	public List<?> executeSql(String sql) {
		return (List<?>) super.currentSession().createSQLQuery(sql).list();
	}
	
	public Integer count(StringBuffer sql,Map<String, Map<String, ?>> dataMap){
		return Integer.valueOf(super.currentSession().createSQLQuery(createBaseSql(sql,dataMap).toString()).uniqueResult().toString());
	}
	
	@SuppressWarnings("unchecked")
	public List<T> page(final Map<String, Class<?>> clazzMap,StringBuffer sql,Map<String, Map<String, ?>> dataMap,Integer pageNo,Integer pageSize){
		sql = createBaseSql(sql,dataMap);
		SQLQuery q = super.currentSession().createSQLQuery(sql.toString());
		if(clazzMap != null){
			for (Entry<String, Class<?>> entity : clazzMap.entrySet())
				q.addEntity(entity.getKey(),entity.getValue());
		}
		if(pageNo != null && pageSize != null){
			return q.setFirstResult((pageNo-1)*pageSize).setMaxResults(pageSize).list();
		}
		return q.list();
	}

	@SuppressWarnings("unchecked")
	protected StringBuffer createBaseSql(StringBuffer sql,Map<String, Map<String, ?>> dataMap)
	{
		sql.append(" where 1=1");
		if (dataMap == null || dataMap.isEmpty())
			return sql;
		if (dataMap.get(Constants.SEARCHCONDITION) != null && !dataMap.get(Constants.SEARCHCONDITION).isEmpty())
		{
			Map<String, Map<String, Object>> all_search = (Map<String, Map<String, Object>>) dataMap.get(Constants.SEARCHCONDITION);
			sql = createSearchSql(sql,all_search);
		}
		if (dataMap.get(Constants.ORDER_CONDITION) != null && !dataMap.get(Constants.ORDER_CONDITION).isEmpty())
		{
			Map<String, List<String>> all_order = (Map<String, List<String>>) dataMap.get(Constants.ORDER_CONDITION);
			createOrderSql(sql, all_order);
		}
		return sql;
	}
	
	private StringBuffer createSearchSql(StringBuffer sql,Map<String, Map<String, Object>> all_search)
	{
		if (all_search.get(Constants.SEARCHCONDITION_EQ) != null
		    && !all_search.get(Constants.SEARCHCONDITION_EQ).isEmpty())
		{
			Map<String, Object> searchCondition = all_search.get(Constants.SEARCHCONDITION_EQ);
			for (Entry<String, Object> entity : searchCondition.entrySet())
			{
				sql.append(" and ").append(beanNameToSqlName(entity.getKey())).append("='").append(entity.getValue()).append("'");
			}
		}
		if (all_search.get(Constants.SEARCHCONDITION_GT) != null  && !all_search.get(Constants.SEARCHCONDITION_GT).isEmpty())
		{
			Map<String, Object> searchCondition = all_search.get(Constants.SEARCHCONDITION_GT);
			for (Entry<String, Object> entity : searchCondition.entrySet())
			{
				sql.append(" and ").append(beanNameToSqlName(entity.getKey())).append(">'").append(entity.getValue()).append("'");
			}
		}
		if (all_search.get(Constants.SEARCHCONDITION_LT) != null && !all_search.get(Constants.SEARCHCONDITION_LT).isEmpty())
		{
			Map<String, Object> searchCondition = all_search.get(Constants.SEARCHCONDITION_LT);
			for (Entry<String, Object> entity : searchCondition.entrySet())
			{
				sql.append(" and ").append(beanNameToSqlName(entity.getKey())).append("<'").append(entity.getValue()).append("'");
			}
		}
		if (all_search.get(Constants.SEARCHCONDITION_IN) != null && !all_search.get(Constants.SEARCHCONDITION_IN).isEmpty())
		{
			Map<String, Object> searchCondition = all_search.get(Constants.SEARCHCONDITION_IN);
			for (Entry<String, Object> entity : searchCondition.entrySet())
			{
				@SuppressWarnings("unchecked")
				List<String> list = (ArrayList<String>) entity.getValue();
				sql.append(" and ").append(beanNameToSqlName(entity.getKey())).append(" in (").append(StringUtils.join(list.toArray(),",")).append(")");;
			}
		}
		if (all_search.get(Constants.SEARCHCONDITION_LIKE) != null
		    && !all_search.get(Constants.SEARCHCONDITION_LIKE).isEmpty())
		{
			Map<String, Object> searchCondition = all_search.get(Constants.SEARCHCONDITION_LIKE);
			for (Entry<String, Object> entity : searchCondition.entrySet())
			{
				sql.append(" and ").append(beanNameToSqlName(entity.getKey())).append(" like '%").append(entity.getValue()).append("%'");
			}
		}
		if (all_search.get(Constants.SEARCHCONDITION_NOT) != null  && !all_search.get(Constants.SEARCHCONDITION_NOT).isEmpty())
		{
			Map<String, Object> searchCondition = all_search.get(Constants.SEARCHCONDITION_NOT);
			for (Entry<String, Object> entity : searchCondition.entrySet()){
				sql.append(" and ").append(beanNameToSqlName(entity.getKey())).append(" not ").append(entity.getValue());
			}
		}
		return sql;
	}
	
	private StringBuffer createOrderSql(StringBuffer sql, Map<String, List<String>> all_order)
	{
		if (all_order.get(Constants.ORDER_ASC) != null && !all_order.get(Constants.ORDER_ASC).isEmpty()){
			sql.append(" order by ");
			for (String property : all_order.get(Constants.ORDER_ASC))
				sql.append(property);
			sql.append(" asc");
		}
		if (all_order.get(Constants.ORDER_DESC) != null  && !all_order.get(Constants.ORDER_DESC).isEmpty()){
			sql.append(" order by ");
			for (String property : all_order.get(Constants.ORDER_DESC))
				sql.append(property);
			sql.append(" desc");
		}
		return sql;
	}
	
	private String beanNameToSqlName(String name)
	{
		StringBuffer sb = new StringBuffer();
		char[] arr = name.toCharArray();
		for(char c : arr){
			if(c >= 'A' && c <= 'Z'){
				sb.append("_").append((char)(c+32));
			}else{
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getUniqueSQLResult(final String sql, final Object... params) {
		return (T) getHibernateTemplate().execute(new HibernateCallback<T>() {
			public T doInHibernate(Session session) throws HibernateException {
				String limitSql = sql;
				if (!sql.contains("limit")) {
					limitSql += " limit 1";
				}
				Query query = session.createSQLQuery(limitSql);
				buildParameters(query, params);
				return (T) query.uniqueResult();
			}
		});
	}
	
	public <T> List<T> executeSQLQuery(final String sql, final Class<T> clazz, final Object... params) {
		List<T> list = (List<T>) getHibernateTemplate().execute(new HibernateCallback<List<T>>() {
			@SuppressWarnings("unchecked")
			public List<T> doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.aliasToBean(clazz));
				buildParameters(query, params);
				return query.list();
			}
		});
		return list;
	}
	
	public <T> List<T> executeSQLQuery(final String sql, final Object... params) {
		return (List<T>) getHibernateTemplate().execute(new HibernateCallback<List<T>>() {
			public List<T> doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(sql);
				buildParameters(query, params);
				return query.list();
			}
		});
	}
	
	public <T> int executeSQLUpdate(final String sql, final Object... params) {
		return (Integer) getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(sql);
				buildParameters(query, params);
				return query.executeUpdate();
			}
		});
	}
}
