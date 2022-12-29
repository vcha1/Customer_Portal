package com.my1stle.customer.portal.web.controller.error;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ErrorController extends AbstractErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

    private static final Map<HttpStatus, String> message;

    static {
        Map<HttpStatus, String> temp = new HashMap<>();
        temp.put(HttpStatus.FORBIDDEN, ErrorMessage.FORBIDDEN);
        temp.put(HttpStatus.NOT_FOUND, ErrorMessage.NOT_FOUND);
        temp.put(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.INTERNAL_SERVER_ERROR);
        message = Collections.unmodifiableMap(temp);
    }

    @Autowired
    public ErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE,
            RequestMethod.OPTIONS, RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.TRACE, RequestMethod.HEAD})
    public String handleError(Model model, HttpServletRequest request, HttpServletResponse response) {

        ErrorDto error = HttpServletError.from(request, response, getErrorAttributes(request, true));

        if (error.getHttpStatus().is5xxServerError()) {
            LOGGER.error("Error Id : {}\nStatus : {}\nTime Stamp : {} \nMessage : {}\nReferer : {}\nMethod : {}\nURI : {}\nQuery : {}\nStack Trace : {}",
                    error.getId(),
                    error.getHttpStatus(),
                    error.getTimeStamp(),
                    error.getMessage(),
                    error.getReferer(),
                    error.getMethod(),
                    error.getURI(),
                    error.getQueryString(),
                    error.getTrace());
        } else if (error.getHttpStatus().is2xxSuccessful()) { // this should only occur if user directly visits /error
            return "redirect:/";
        } else {
            LOGGER.debug("{}", error);
        }

        String msg = message.get(error.getHttpStatus());

        model.addAttribute("error", error);
        model.addAttribute("message", StringUtils.isBlank(msg) ? String.format("%s %s", error.getHttpStatus(), error.getHttpStatus().getReasonPhrase()) : msg);

        return "error/error";

    }

}
