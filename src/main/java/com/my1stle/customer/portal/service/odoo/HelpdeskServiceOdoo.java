package com.my1stle.customer.portal.service.odoo;


import org.baeldung.persistence.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service("helpdeskServiceOdoo")
public class HelpdeskServiceOdoo {

    public DefaultHelpdeskService getHelpdeskByTicketId(String odooTicketId) {


        DefaultHelpdeskService odooData = new DefaultHelpdeskService(odooTicketId);

        return odooData;
    }

}
