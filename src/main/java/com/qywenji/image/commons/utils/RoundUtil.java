package com.qywenji.image.commons.utils;

import java.math.BigDecimal;

/**
 * 
 * @ClassName: RoundUtil
 * @Description: Double类型的四舍五入算法
 * @author wei.wu
 *
 */
public class RoundUtil {

	/**
	 * 
	 * @Title: trunc
	 * @Description: 返回包含两位小数点的数字字符串(截取类型)
	 * @param @param value
	 * @param @return
	 * @return String
	 */
	public static String truncStr(BigDecimal value) {
		if (value == null)
			return null;
		value = value.setScale(2, BigDecimal.ROUND_DOWN);
		return String.format("%.2f", value.doubleValue());
	}

	/**
	 * 
	 * @Title: trunc
	 * @Description: 截取
	 * @param @param value
	 * @param @param len
	 * @param @return
	 * @return String
	 */
	public static String truncStr(BigDecimal value, Integer len) {
		if (value == null)
			return null;
		value = value.setScale(len, BigDecimal.ROUND_DOWN);
		return value.toString();
	}

	/**
	 * 
	 * @Title: round
	 * @Description: 四舍五入算法
	 * @param @param value
	 * @param @param decimalPlaces
	 * @param @return
	 * @return Double
	 */
	public static Double round(Double value, int decimalPlaces) {

		if (value == null)
			return null;
		if (decimalPlaces <= 0)
			decimalPlaces = 0;
		BigDecimal bd = new BigDecimal(value.toString());
		bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();

	}

	/**
	 * 
	 * @Title: add
	 * @Description: 加法运算
	 * @param @param d1
	 * @param @param String.valueOf(d2
	 * @param @return
	 * @return double
	 */
	public static double add(double d1, double d2) {
		// 进行加法运算
		BigDecimal b1 = new BigDecimal(String.valueOf(d1));
		BigDecimal b2 = new BigDecimal(String.valueOf(d2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 
	 * @Title: sub减法运算
	 * @Description: 减法运算
	 * @param @param d1
	 * @param @param String.valueOf(d2
	 * @param @return
	 * @return double
	 */
	public static double sub(double d1, double d2) {
		// 进行减法运算
		BigDecimal b1 = new BigDecimal(String.valueOf(d1));
		BigDecimal b2 = new BigDecimal(String.valueOf(d2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 
	 * @Title: mul
	 * @Description: 乘法运算
	 * @param @param d1
	 * @param @param String.valueOf(d2
	 * @param @return
	 * @return double
	 */
	public static double mul(double d1, double d2) {
		// 进行乘法运算
		BigDecimal b1 = new BigDecimal(String.valueOf(d1));
		BigDecimal b2 = new BigDecimal(String.valueOf(d2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 
	 * @Title: div
	 * @Description: 除法运算
	 * @param @param String.valueOf(d1
	 * @param @param String.valueOf(d2
	 * @param @param len
	 * @param @return
	 * @return double
	 */
	public static double div(double d1, double d2, int len) {
		// 进行除法运算
		BigDecimal b1 = new BigDecimal(String.valueOf(d1));
		BigDecimal b2 = new BigDecimal(String.valueOf(d2));
		return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * @Title： div
	 * @Description： TODO 两数相除不四舍五入
	 * @return double
	 * @param d1
	 * @param d2
	 * @param len
	 */
	public static double divRoundDown(double d1, double d2, int len) {
		// 进行除法运算
		BigDecimal b1 = new BigDecimal(String.valueOf(d1));
		BigDecimal b2 = new BigDecimal(String.valueOf(d2));
		return b1.divide(b2, len, BigDecimal.ROUND_DOWN).doubleValue();
	}

	/**
	 * 
	 * @Title: roundUP
	 * @Description: 含小数点的进一位
	 * @param @param d
	 * @param @param len
	 * @param @return
	 * @return double
	 */
	public static double roundUP(double d, int len) {
		BigDecimal b1 = new BigDecimal(String.valueOf(d));
		BigDecimal b2 = new BigDecimal("1");
		// 任何一个数字除以1都是原数字
		return b1.divide(b2, len, BigDecimal.ROUND_UP).doubleValue();
	}
	
	public static void main(String[] args) {
		double d1 = 10.891;
		double d2 = 5.30;
		System.out.println(sub(d1, d2));
	}
	
}
