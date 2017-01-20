package com.qywenji.image.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 实体工具类
 * @author admin
 *
 */
public class EntityUtil {

	private static Logger log = LoggerFactory.getLogger(EntityUtil.class);

	public static Map<String, Object> BeanToMap(Object bean) {
		
		Class<?> type = bean.getClass();
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(type);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();
				if (!propertyName.equals("class")) {
					Method readMethod = descriptor.getReadMethod();
					Object result = readMethod.invoke(bean, new Object[0]);
					if(result == null || result.toString().equals("null") || result.toString().equals("")){
						continue;
					}
					returnMap.put(propertyName, result);
				}
			}
		} catch (Exception e) {
			log.error("This Bean[" + type.getName() + "] To Map Has Error!" + e.getMessage());
		}
		return returnMap;
	}
	
	public static void main(String[] args) {
//		Test te = new Test();
//		te.setAge(18);
//		te.setName(null);
//		
//		Map<String,Object> resultMap = BeanToMap(te);
//		for(Map.Entry<String, Object> entry : resultMap.entrySet()){
//			String key = entry.getKey();
//			Object value = entry.getValue();
//			System.out.println(key+"    "+value);
//		}
	}
}
