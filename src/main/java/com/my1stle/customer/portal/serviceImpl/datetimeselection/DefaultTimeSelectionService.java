package com.my1stle.customer.portal.serviceImpl.datetimeselection;

import com.dev1stle.scheduling.system.v1.model.Calendar;
import com.my1stle.customer.portal.persistence.repository.calendar.CalendarAssigneeRepository;
import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.ProductService;
import com.my1stle.customer.portal.service.datetimeselection.DateTimeSelectionService;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.web.dto.suggestion.Appointment;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
class DefaultTimeSelectionService implements DateTimeSelectionService {

    private InstallationService installationService;
    private ProductService productService;
    private CalendarAssigneeRepository selector;

    @Autowired
    public DefaultTimeSelectionService(
            @Qualifier("publicInstallationService") InstallationService installationService,
            ProductService productService,
            CalendarAssigneeRepository selector) {

        this.installationService = installationService;
        this.productService = productService;
        this.selector = selector;

    }

    @Override
    public List<Calendar> getAvailableCalendars(String installationId, Long productId) {

        InstallationAndProductEagerFetcher installationAndProductEagerFetcher = new InstallationAndProductEagerFetcher(installationId, productId);
        Installation installation = installationAndProductEagerFetcher.getInstallation();
        Product product = installationAndProductEagerFetcher.getProduct();

        return this.selector.selectBySalesforceCustomerAndProduct(installation.getOperationalArea(), product);

    }

    @Override
    public List<Calendar> getAvailableCalendars(String installationId, Set<Long> skillIds) {

        Installation installation = installationService.getInstallationById(installationId);

        return this.selector.selectByOperationalAreaAndSkillIds(installation.getOperationalArea(), skillIds);

    }

    @Override
    public Appointment createAppointment(String installationId, Long productId) {

        InstallationAndProductEagerFetcher installationAndProductEagerFetcher = new InstallationAndProductEagerFetcher(installationId, productId);
        Installation installation = installationAndProductEagerFetcher.getInstallation();
        Product product = installationAndProductEagerFetcher.getProduct();

        return new Appointment(
                Long.valueOf(product.getDuration()),
                String.format("%s %s %s %s", installation.getAddress(), installation.getCity(), installation.getState(), installation.getZipCode())
        );

    }

    @Override
    public Appointment createAppointment(String installationId, Integer durationInMinutes) {

        Installation installation = installationService.getInstallationById(installationId);

        return new Appointment(
                Long.valueOf(durationInMinutes),
                String.format("%s %s %s %s", installation.getAddress(), installation.getCity(), installation.getState(), installation.getZipCode())
        );

    }


    // Helper
    private class InstallationAndProductEagerFetcher {

        private Installation installation;
        private Product product;

        InstallationAndProductEagerFetcher(String installationId, Long productId) {
            this.fetch(installationId, productId);
        }

        Installation getInstallation() {
            return installation;
        }

        Product getProduct() {
            return product;
        }

        private void fetch(String installationId, Long productId) {

            installation = DefaultTimeSelectionService.this.installationService.getInstallationById(installationId);

            if (null == installation) {
                throw new ResourceNotFoundException("Installation Not Found!");
            }

            product = DefaultTimeSelectionService.this.productService.getById(productId);

            if (null == product) {
                throw new ResourceNotFoundException("Product Not Found!");
            }

        }

    }

}
