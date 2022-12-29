package com.my1stle.customer.portal.web.controller.error;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpStatus;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

class HttpServletError implements ErrorDto {

    private String id;
    private Integer code;
    private HttpStatus httpStatus;
    private String error;
    private String message;
    private String timeStamp;
    private String referer;
    private String trace;
    private String method;
    private String query;
    private String uri;
    private int portNumber;
    private String scheme;
    private String serverName;
    private String contextPath;
    private String servletPath;
    private String pathInfo;

    private HttpServletError() {

    }

    static HttpServletError from(HttpServletRequest httpServletRequest, HttpServletResponse response, Map<String, Object> errorAttributes) {

        HttpServletError error = new HttpServletError();

        error.id = UUID.randomUUID().toString();
        error.code = response.getStatus();
        error.httpStatus = HttpStatus.valueOf(response.getStatus());
        error.error = (String) errorAttributes.get("error");
        error.message = (String) errorAttributes.get("message");
        error.timeStamp = errorAttributes.get("timestamp").toString();
        error.referer = httpServletRequest.getHeader("referer");
        error.trace = (String) errorAttributes.get("trace");

        error.method = httpServletRequest.getMethod();
        error.query = (String) httpServletRequest.getAttribute(RequestDispatcher.FORWARD_QUERY_STRING);
        error.uri = (String) httpServletRequest.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);

        error.scheme = httpServletRequest.getScheme();
        error.serverName = httpServletRequest.getServerName();
        error.contextPath = httpServletRequest.getContextPath();
        error.servletPath = httpServletRequest.getServletPath();
        error.pathInfo = httpServletRequest.getPathInfo();

        return error;

    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getError() {
        return error;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String getReferer() {
        return referer;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getQueryString() {
        return query;
    }

    @Override
    public String getURI() {
        return uri;
    }

    @Override
    public String getScheme() {
        return scheme;
    }

    @Override
    public String getServerName() {
        return serverName;
    }

    @Override
    public int getPortNumber() {
        return portNumber;
    }

    @Override
    public String getContextPath() {
        return contextPath;
    }

    @Override
    public String getServletPath() {
        return servletPath;
    }

    @Override
    public String getPathInfo() {
        return pathInfo;
    }

    @Override
    public String getTrace() {
        return trace;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof HttpServletError)) return false;

        HttpServletError error = (HttpServletError) o;

        return new EqualsBuilder()
                .append(id, error.id)
                .isEquals();

    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("code", code)
                .append("httpStatus", httpStatus)
                .append("error", error)
                .append("message", message)
                .append("timeStamp", timeStamp)
                .append("referer", referer)
                .append("query", query)
                .append("uri", uri)
                .append("portNumber", portNumber)
                .append("scheme", scheme)
                .append("serverName", serverName)
                .append("contextPath", contextPath)
                .append("servletPath", servletPath)
                .append("pathInfo", pathInfo)
                .append("trace", StringEscapeUtils.escapeJson(trace))
                .toString();
    }
}
