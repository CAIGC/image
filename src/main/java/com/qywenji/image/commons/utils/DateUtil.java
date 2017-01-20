package com.qywenji.image.commons.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2015/8/7 0007.
 */
public class DateUtil {

    public static Long changeDateToInt(Date date){
        return date.getTime()/1000;
    }
    public static Date changeIntToDate(Long dateInt) {
        SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        String dateString = format.format(dateInt*1000);
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    
    public static Date addDays(int day){
    	Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DAY_OF_MONTH, day);
		return ca.getTime();
    }
    
    public static String getFormatDate(Date date,String patten){
    	if(date == null){
    		return "";
    	}
    	SimpleDateFormat sdf = null;
    	if(StringUtils.isBlank(patten)){
    		sdf = new SimpleDateFormat("yyyy-MM-dd");
    	}else{
    		sdf = new SimpleDateFormat(patten);
    	}
    	return sdf.format(date);
    }
    
    public static Date getParseDate(String dateStr,String patten){
    	if(StringUtils.isBlank(dateStr)){
    		return new Date();
    	}
    	SimpleDateFormat sdf = null;
    	if(StringUtils.isBlank(patten)){
    		sdf = new SimpleDateFormat("yyyy-MM-dd");
    	}else{
    		sdf = new SimpleDateFormat(patten);
    	}
    	try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return new Date();
    }
    
    /**
     * 获取date的前或者后amount月份，amount为正式后面月份，为负数前面月份
     * filed 是年月日
     * @param date
     * @param patten
     * @param amount
     * @return
     */
    public static String getBeforeOrAfterMonthByDate(String date,String patten,int filed,int amount){
    	if(StringUtils.isBlank(date)){
    		return "";
    	}
    	Calendar ca = Calendar.getInstance();
    	try{
    		if(StringUtils.isBlank(patten)){
    			patten = "yyyy-MM-dd";
    		}
    		SimpleDateFormat sdf = new SimpleDateFormat(patten);
    		ca.setTime(sdf.parse(date));
    		ca.add(filed, amount);
    		return sdf.format(ca.getTime());
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return "";
    }
    
    public static void main(String[] args) {
    	System.out.println(getBeforeOrAfterMonthByDate("2016-04","yyyy-MM",Calendar.MONTH,1));
	}
}
