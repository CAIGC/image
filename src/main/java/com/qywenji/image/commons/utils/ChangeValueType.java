package com.qywenji.image.commons.utils;



import com.qywenji.image.commons.Constants;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class ChangeValueType {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	public static  Object changeValueType(String key, Object value)
	{
		try
		{
			if (key.startsWith(Constants.INT_FLAG))
				return Integer.valueOf(String.valueOf(value));
			if (key.startsWith(Constants.LONG_FLAG))
				return Long.valueOf(String.valueOf(value));
			if (key.startsWith(Constants.DATATIME_FLAG))
				return sdf.parse(String.valueOf(value));
			if (key.startsWith(Constants.FLOAT_FLAG))
				return Float.valueOf(String.valueOf(value));
			if(key.startsWith(Constants.BYTE_FLAG)){
				return Byte.valueOf(String.valueOf(value));
			}
            if(key.startsWith(Constants.BIGDECIMAL_FLAG)){
                return new BigDecimal(String.valueOf(value));
            }
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return String.valueOf(value);
	}
	
	public static List<Object> changeValueType(String key, List<String> value)
	{
		List<Object> resulet = new ArrayList<>();
		for (String s : value)
		{
			resulet.add(changeValueType(key, s));
		}
		return resulet;
	}
}
