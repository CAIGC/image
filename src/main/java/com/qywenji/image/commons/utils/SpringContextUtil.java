package com.qywenji.image.commons.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtil implements ApplicationContextAware
{
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext contex) throws BeansException
	{
		SpringContextUtil.context = contex;
	}

	public static Object getBean(String beanName)
	{
		return context.getBean(beanName);
	}

}
