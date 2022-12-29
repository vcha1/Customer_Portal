package com.my1stle.customer.portal.service.cases;

import com.my1stle.customer.portal.service.model.ServiceCase;
import com.my1stle.customer.portal.service.serviceapi.ExistingServiceCaseDto;
import com.my1stle.customer.portal.web.dto.cases.CaseDto;

import java.util.List;
import java.util.Optional;

public interface CaseService {

    List<ServiceCase> getCases();

    Optional<ServiceCase> get(String id);

    List<ExistingServiceCaseDto> getByOdooIdTest(String id);

    CaseSubmitResult submit(CaseDto dto);

}
