package com.my1stle.customer.portal.service.installation;

import com.my1stle.customer.portal.service.model.Installation;

import java.time.LocalDate;
import java.util.List;

public interface InstallationSelector {

    List<Installation> selectByCustomerEmail(String email);

    List<Installation> selectByCustomerEmailAndContractApprovedDateIsBefore(String email, LocalDate date);

}