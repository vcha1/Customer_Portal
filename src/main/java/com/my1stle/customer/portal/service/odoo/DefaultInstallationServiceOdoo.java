package com.my1stle.customer.portal.service.odoo;

import org.baeldung.persistence.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DefaultInstallationServiceOdoo {
    private List<Object> id = new ArrayList();
    private List<String> address = new ArrayList();

    private List<String> name = new ArrayList();

    public DefaultInstallationServiceOdoo() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = user.getEmail();

        XmlRpcAuthenticationInformation xmlRpcAuthInfo = OdooConnectionConfiguration.generateDefaultXmlRpcAuthenticationInformation();
        XmlRpcClientFactory xmlRpcClientFactory = OdooConnectionConfiguration.createDefaultXmlRpcClientFactory();
        OdooObjectConnection odooConnection = new DefaultOdooObjectConnection(xmlRpcAuthInfo, xmlRpcClientFactory);

        String objectType = "project.task";
        List<String> fields = Arrays.asList("name",  "x_studio_contract_type_3", "x_studio_site_street");
        List<Object> criteria = Arrays.asList(Arrays.asList("x_studio_email_3", "=",userEmail));

        List<Map<String, ?>> results = odooConnection.findObjects(objectType, fields, criteria);

        for (int ids = 0; ids < results.size(); ids++) {
            Object integer = results.get(ids).get("id");
            String streetAddress = (String) results.get(ids).get("x_studio_site_street");
            String names =(String) results.get(ids).get("name");

            this.id.add(integer);
            this.address.add(streetAddress);
            this.name.add(names);

        }

    }

    public List<Object> getId(){
        return this.id;
    }

    public List<String> getAddress(){
        return this.address;
    }

    public List<String> getName(){
        return this.name;
    }
}
