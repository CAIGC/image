package com.qywenji.image.commons.utils;

public class FirstCharacterConvert
{

	public static String firstCharacterUpperCase(String lower)
	{
		if (lower.charAt(0) >= 'A' && lower.charAt(0) <= 'Z')
			return lower;
		return (char) (lower.charAt(0) - 32) + lower.substring(1);
	}

	public static String firseCharactrtLowerCase(String upper)
	{
		if (upper.charAt(0) >= 'a' && upper.charAt(0) <= 'z')
			return upper;
		return (char) (upper.charAt(0) + 32) + upper.substring(1);
	}

	public static void main(String[] args)
	{
		System.out.println(firseCharactrtLowerCase("Aaaa"));
	}
}
