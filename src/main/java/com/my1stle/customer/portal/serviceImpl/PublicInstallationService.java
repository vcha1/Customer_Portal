package com.my1stle.customer.portal.serviceImpl;

import com.my1stle.customer.portal.service.installation.configuration.salesforce.repository.InstallationRepository;
import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @implSpec this implementation returns installations without the context of
 * the current user and is meant to be used in public endpoints (i.e endpoints that
 * do not require login
 */
@Service
public class PublicInstallationService implements InstallationService {

    private InstallationRepository installationRepository;

    @Autowired
    public PublicInstallationService(InstallationRepository installationRepository) {
        this.installationRepository = installationRepository;
    }

    @Override
    public Installation getInstallationById(String installationId) {
        return this.installationRepository.findById(installationId)
                .orElseThrow(() -> new ResourceNotFoundException("Installation Not Found!"));
    }

    @Override
    public List<Installation> getInstallations() {
        throw new UnsupportedOperationException("Not Implemented!");
    }

}
