package com.qywenji.image.commons.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class Global extends HttpServlet {  
      
      
      
    /**
	 * 
	 */
	private static final long serialVersionUID = 2313160250090552044L;

	@Override  
    public void init() throws ServletException {  
    	InputStream inputStream = getClass().getResourceAsStream("/config.properties");
    	Properties props = new Properties();
		try {
			props.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Set<Entry<Object, Object>> set = props.entrySet();
		for(Entry<Object, Object> entity : set){
            this.getServletContext().setAttribute(String.valueOf(entity.getKey()),entity.getValue());  
		}
    }  
  
}  