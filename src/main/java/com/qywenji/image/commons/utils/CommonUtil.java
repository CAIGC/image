package com.qywenji.image.commons.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * @ClassName: CommonUtil
 * @Description: TODO
 * @author
 * @date 2014年7月4日 上午11:05:30
 * 
 */
public class CommonUtil {

	/**
	 * 读取实体bean属性值
	 * 
	 * @param bean
	 *            实体对象
	 * @param propertyName
	 *            要取的属性值
	 * @return 返回实体对象的属性值
	 */
	@SuppressWarnings("finally")
	public static Object getPropertyValue(Object bean, String propertyName) {
		Object result = null;
		if (propertyName.equals("serialVersionUID")) {
			return result;
		}
		PropertyDescriptor pd;
		try {
			pd = new PropertyDescriptor(propertyName, bean.getClass());
			Method m = pd.getReadMethod();
			result = m.invoke(bean);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			return result;
		}
	}

	/**
	 * 设置实体bean的属性值
	 * @param bean实体对象
	 * @param propertyName要设置的属性
	 * @param value属性值
	 */
	public static void setProperty(Object bean, String propertyName, Object value) {
		PropertyDescriptor pd;
		try {
			pd = new PropertyDescriptor(propertyName, bean.getClass());
			Method m = pd.getWriteMethod();
			// 设置属性值
			m.invoke(bean, value);
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
