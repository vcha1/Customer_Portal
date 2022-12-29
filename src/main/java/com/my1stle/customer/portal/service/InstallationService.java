package com.my1stle.customer.portal.service;

import com.my1stle.customer.portal.service.model.Installation;

import java.util.List;

public interface InstallationService {
    Installation getInstallationById(String id);

    List<Installation> getInstallations();
}
