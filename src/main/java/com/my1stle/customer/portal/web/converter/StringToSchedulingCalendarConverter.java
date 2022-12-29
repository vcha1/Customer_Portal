package com.my1stle.customer.portal.web.converter;

import com.my1stle.customer.portal.web.exception.BadRequestException;
import com.dev1stle.scheduling.system.v1.model.Calendar;
import com.dev1stle.scheduling.system.v1.service.assignee.AssigneeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
@Deprecated
public class StringToSchedulingCalendarConverter implements Converter<String, Calendar> {

    private AssigneeService service;

    @Autowired
    public StringToSchedulingCalendarConverter(AssigneeService service) {
        this.service = service;
    }

    @Override
    public Calendar convert(String source) {

        if (StringUtils.isBlank(source))
            return null;

        try {
            return this.service.findById(Long.parseLong(source)).orElse(null);
        } catch (NumberFormatException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
