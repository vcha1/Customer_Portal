package com.my1stle.customer.portal.service.odoo;

import org.baeldung.persistence.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("installationServiceOdoo")
public class InstallationServiceOdoo {

    public DefaultInstallationServiceOdoo getInstallationByEmail() {


        DefaultInstallationServiceOdoo odooData = new DefaultInstallationServiceOdoo();

        return odooData;
    }

}
