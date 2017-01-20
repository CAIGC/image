package com.qywenji.image.commons.utils;

import java.text.DecimalFormat;

public class MoneyUtil {

	public static String numToCapChnCha(Object ob) {
		String s = new DecimalFormat("#.00").format(ob);
		s = s.replaceAll("\\.", "");// 将字符串中的"."去掉
		char d[] = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' }; // 数字表示
		String unit = "仟佰拾兆仟佰拾亿仟佰拾万仟佰拾元角分";
		int c = unit.length();
		StringBuffer sb = new StringBuffer(unit);
		for (int i = s.length() - 1; i >= 0; i--) {
			sb.insert(c - s.length() + i, d[s.charAt(i) - 0x30]);
		}
		s = sb.substring(c - s.length(), c + s.length());
//		s = s.replaceAll("零[仟佰拾]", "零").replaceAll("零{2,}", "零").replaceAll("零([兆万元Ԫ])", "$1").replaceAll("零[角分]", "");
		return s;
	}
	
	
//	public static void main(String[] args) {
//		Scanner s = new Scanner(System.in);
//		System.out.println("请输入你要转换的金额：");
//		double number = s.nextDouble();
//		String s1 = numToCapChnCha(new Double(number));
//		System.out.println("转换成大写后的金额是：" + s1);
//
//	}
}
