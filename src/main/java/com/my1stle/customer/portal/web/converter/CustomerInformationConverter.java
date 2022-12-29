package com.my1stle.customer.portal.web.converter;

import com.dev1stle.repository.salesforce.SalesforceObjectRepository;
import com.dev1stle.scheduling.system.v1.model.salesforce.Case;
import com.dev1stle.scheduling.system.v1.model.salesforce.Installation;
import com.dev1stle.scheduling.system.v1.model.salesforce.Opportunity;
import com.dev1stle.scheduling.system.v1.model.salesforce.SalesforceCustomerInformation;
import com.dev1stle.scheduling.system.v1.service.util.SalesforceIdPrefixUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Deprecated
public class CustomerInformationConverter implements Converter<String, SalesforceCustomerInformation> {

    private SalesforceObjectRepository<Installation> installationSalesforceObjectRepository;
    private SalesforceObjectRepository<Opportunity> opportunitySalesforceObjectRepository;
    private SalesforceObjectRepository<Case> caseSalesforceObjectRepository;

    @Autowired
    public CustomerInformationConverter(SalesforceObjectRepository<Installation> installationSalesforceObjectRepository, SalesforceObjectRepository<Opportunity> opportunitySalesforceObjectRepository, SalesforceObjectRepository<Case> caseSalesforceObjectRepository) {
        this.installationSalesforceObjectRepository = installationSalesforceObjectRepository;
        this.opportunitySalesforceObjectRepository = opportunitySalesforceObjectRepository;
        this.caseSalesforceObjectRepository = caseSalesforceObjectRepository;
    }

    @Override
    public SalesforceCustomerInformation convert(String salesforceId) {

        SalesforceIdPrefixUtil.IdPrefix idPrefix = SalesforceIdPrefixUtil.get(salesforceId);

        if (idPrefix == SalesforceIdPrefixUtil.IdPrefix.Opportunity) {
            return this.opportunitySalesforceObjectRepository.findById(salesforceId).orElse(null);
        } else if (idPrefix == SalesforceIdPrefixUtil.IdPrefix.Installation) {
            return this.installationSalesforceObjectRepository.findById(salesforceId).orElse(null);
        } else if (idPrefix == SalesforceIdPrefixUtil.IdPrefix.Case) {
            return this.caseSalesforceObjectRepository.findById(salesforceId).orElse(null);
        }

        return null;
    }

}
