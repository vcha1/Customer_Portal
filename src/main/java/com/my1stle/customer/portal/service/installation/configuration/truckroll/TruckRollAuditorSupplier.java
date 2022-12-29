package com.my1stle.customer.portal.service.installation.configuration.truckroll;

import com.dev1stle.scheduling.system.v1.service.truck.roll.api.model.Auditor;
import com.my1stle.customer.portal.service.UserProvider;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
class TruckRollAuditorSupplier implements Supplier<Auditor> {

    private static final String SOURCE = "CUSTOMER_PORTAL";

    private UserProvider userProvider;

    @Autowired
    public TruckRollAuditorSupplier(UserProvider userProvider) {
        this.userProvider = userProvider;
    }

    @Override
    public Auditor get() {
        return AuditorAdapter.from(userProvider.getCurrentUser());
    }

    private static class AuditorAdapter implements Auditor {

        private String referenceId;

        private AuditorAdapter() {

        }

        static AuditorAdapter from(User user) {
            AuditorAdapter adapter = new AuditorAdapter();
            adapter.referenceId = String.valueOf(user.getId());
            return adapter;
        }

        @Override
        public String getId() {
            return referenceId;
        }

        @Override
        public String getSource() {
            return SOURCE;
        }

    }

}


