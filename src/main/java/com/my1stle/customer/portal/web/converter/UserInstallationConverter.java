package com.my1stle.customer.portal.web.converter;

import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.model.Installation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class UserInstallationConverter implements Converter<String, Installation> {

    private InstallationService installationService;

    @Autowired
    public UserInstallationConverter(InstallationService installationService) {
        this.installationService = installationService;
    }

    @Override
    public Installation convert(String s) {
        return this.installationService.getInstallationById(s);
    }
}
