package com.my1stle.customer.portal.service.odoo;

import org.baeldung.persistence.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DefaultHelpdeskService {
    private Object id;
    private String installationName;

    public DefaultHelpdeskService(String odooTicketId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = user.getEmail().toLowerCase();

        XmlRpcAuthenticationInformation xmlRpcAuthInfo = OdooConnectionConfiguration.generateDefaultXmlRpcAuthenticationInformation();
        XmlRpcClientFactory xmlRpcClientFactory = OdooConnectionConfiguration.createDefaultXmlRpcClientFactory();
        OdooObjectConnection odooConnection = new DefaultOdooObjectConnection(xmlRpcAuthInfo, xmlRpcClientFactory);

        String objectType = "helpdesk.ticket";
        List<String> fields = Arrays.asList("x_studio_installation");
        //List<Object> criteria = Arrays.asList(Arrays.asList("id", "=", odooTicketId));

        Object[] installationNames = (Object[]) odooConnection.findObjects(objectType, fields,
                Arrays.asList(Arrays.asList("id", "=", odooTicketId))).get(0).get("x_studio_installation");

        this.installationName = installationNames[1].toString();

    }

    public Object getId(){
        return this.id;
    }

    public String getInstallationName(){
        return this.installationName;
    }

}
