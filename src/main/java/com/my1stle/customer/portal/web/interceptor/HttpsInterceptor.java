package com.my1stle.customer.portal.web.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class HttpsInterceptor extends HandlerInterceptorAdapter {


	final String xForwardedProtoHeader = "x-forwarded-proto";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception
	{
		
		Map<String,String> headerMap = new HashMap<String,String>();
		
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
	            String key = (String) headerNames.nextElement();
	            String value = request.getHeader(key);
	            headerMap.put(key, value);
	    }
		
		/*
		for(String key : headerMap.keySet())
		{
			System.out.println(key + " : " + headerMap.get(key));
		} 
		/**/
		
		String header = headerMap.get(xForwardedProtoHeader);
		
		//Precheck if the x-forwarded-proto header exists;
		if(header != null) 
		{
			if(header.equals("http"))
			{
				response.sendRedirect("https://" + request.getHeader("host"));
				return false;
			}
			else
				return true;
		}
		else
			return true;
		
		/**/

	}
}
