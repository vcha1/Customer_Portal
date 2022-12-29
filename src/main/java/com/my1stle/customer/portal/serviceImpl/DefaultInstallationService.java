package com.my1stle.customer.portal.serviceImpl;

import com.my1stle.customer.portal.persistence.repository.InstallationSalesforceRepository;
import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.model.Installation;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("installationService")
public class DefaultInstallationService implements InstallationService {

    private InstallationSalesforceRepository installationSelector;

    @Autowired
    public DefaultInstallationService(InstallationSalesforceRepository installationSelector) {
        this.installationSelector = installationSelector;
    }

    @Override
    public Installation getInstallationById(String id) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = user.getEmail();

        return installationSelector.selectByIdAndCustomerEmail(id, userEmail);

    }

    @Override
    public List<Installation> getInstallations() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = user.getEmail();

        return new ArrayList<>(installationSelector.selectByCustomerEmail(userEmail));
    }

}
