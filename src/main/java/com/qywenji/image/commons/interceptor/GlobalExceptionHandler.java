package com.qywenji.image.commons.interceptor;

import com.alibaba.fastjson.JSON;
import com.qywenji.image.commons.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {Exception.class})
    public String handleException(HttpServletRequest request,HttpServletResponse response,Exception ex) throws ServletException, IOException {
        ex.printStackTrace();
        logger.error("{} Exception: {}",request.getRequestURL(), ex);
        return JSON.toJSONString(new HashMap<String, String>(){
        	/**
			 * 
			 */
			private static final long serialVersionUID = -1483532814946751454L;

			{
        		put(Constants.RESPONSE_CODE, Constants.RESPONSE_STATUS_FAIL);
        		put(Constants.RESPONSE_DATA, null);
        	}
        });
    }
}