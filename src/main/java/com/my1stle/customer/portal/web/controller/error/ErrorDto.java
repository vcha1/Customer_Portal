package com.my1stle.customer.portal.web.controller.error;

import org.springframework.http.HttpStatus;

interface ErrorDto {

    String getId();

    HttpStatus getHttpStatus();

    String getError();

    String getMessage();

    String getTimeStamp();

    String getReferer();

    String getMethod();

    String getURI();

    String getQueryString();

    String getScheme();

    String getServerName();

    int getPortNumber();

    String getContextPath();

    String getServletPath();

    String getPathInfo();

    String getTrace();

}
