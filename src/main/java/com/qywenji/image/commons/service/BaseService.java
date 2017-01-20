package com.qywenji.image.commons.service;

import com.qywenji.image.commons.bean.DataTableReturnObject;
import com.qywenji.image.commons.dao.BaseDao;
import com.qywenji.image.commons.utils.FirstCharacterConvert;
import com.qywenji.image.commons.utils.SpringContextUtil;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


@Transactional
public class BaseService<T>
{

	private Class<T> entityClass;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BaseService()
	{
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		entityClass = (Class) params[0];
	}

	@SuppressWarnings("unchecked")
	private BaseDao<T> getDaoApplicationContext()
	{
		String className =
		    entityClass.getSimpleName();
		StringBuilder sb =
		    new StringBuilder(FirstCharacterConvert.firseCharactrtLowerCase(className))
		        .append("Dao");
		return (BaseDao<T>) SpringContextUtil.getBean(sb.toString());
	}
	
	public List<T> findAll(){
		return this.getDaoApplicationContext().list();
	}

	public T save(T t)
	{
		return this.getDaoApplicationContext().save(t);
	}

	public void del(T t)
	{
		this.getDaoApplicationContext().del(t);
	}

	public T getById(Serializable id)
	{
		return this.getDaoApplicationContext().getById(id);
	}

	public DataTableReturnObject<T> getPage(Map<String, Map<String, ?>> dataMap, int pageNo,
			Integer pageSize)
			{
		return this.getDaoApplicationContext().getPage(dataMap, pageNo, pageSize);
			}
	
	public List<T> getData(Map<String, Map<String, ?>> dataMap)
	{
		return this.getDaoApplicationContext().getData(dataMap);
	}
	
	public DataTableReturnObject<T> getPage(Map<String, Map<String, ?>> dataMap, int start,
		    int pageSize,boolean distinct,String idName)
	{
		return this.getDaoApplicationContext().getPage(dataMap, start, pageSize, distinct, idName);
	}

}
