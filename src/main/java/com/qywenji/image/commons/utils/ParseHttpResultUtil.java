package com.qywenji.image.commons.utils;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

public class ParseHttpResultUtil {
	
	public static Map<String,Object> parseJsonMap(Object jsonMap){
		if(jsonMap == null){
			return null;
		}
		Map<String,Object> resultMap = null;
		try{
			resultMap = JSON.parseObject(String.valueOf(jsonMap));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return resultMap;
	}
	
	public static <T> List<T> parseJsonList(Object jsonList,Class<T> clazz){
		try{
			List<T> list = JSON.parseArray(String.valueOf(jsonList), clazz);
			return list;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	public static <T> T parseJsonBean(Object jsonBean,Class<T> clazz){
		try{
			T t = JSON.parseObject(String.valueOf(jsonBean), clazz);
			return t;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
}
