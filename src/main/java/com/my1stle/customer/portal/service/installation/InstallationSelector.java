package com.my1stle.customer.portal.service.installation;

import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;

import java.time.LocalDate;
import java.util.List;

public interface InstallationSelector {

    List<Installation> selectByCustomerEmail(String email);

    List<Installation> selectByCustomerEmailAndContractApprovedDateIsBefore(String email, LocalDate date);


    List<Object> selectByCustomerEmailOdoo(String email);

}