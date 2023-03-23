package com.my1stle.customer.portal.serviceImpl.cases;


import com.dev1stle.repository.specification.salesforce.WhereClause;
import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.cases.CaseDepartment;
import com.my1stle.customer.portal.service.cases.CasePreInstall;
import com.my1stle.customer.portal.service.cases.CaseService;
import com.my1stle.customer.portal.service.cases.CaseStatus;
import com.my1stle.customer.portal.service.cases.CaseSubject;
import com.my1stle.customer.portal.service.cases.CaseSubmitResult;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.ServiceCase;
import com.my1stle.customer.portal.service.serviceapi.ExistingServiceCaseDto;
import com.my1stle.customer.portal.serviceImpl.servicerequest.SalesforceServiceCase;
import com.my1stle.customer.portal.serviceImpl.servicerequest.SalesforceServiceCaseRecordType;
import com.my1stle.customer.portal.serviceImpl.servicerequest.SalesforceServiceCaseRepository;
import com.my1stle.customer.portal.web.dto.cases.CaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Deprecated
public class DefaultCaseService implements CaseService {

    private final String PRE_INSTALLATION_ISSUE = "Pre-Installation Issue";
    private SalesforceServiceCaseRepository repository;
    private InstallationService installationService;

    @Autowired
    public DefaultCaseService(
            InstallationService installationService,
            SalesforceServiceCaseRepository repository
    ){
       this.repository = repository;
       this.installationService = installationService;
    }

    @Override
    public List<ServiceCase> getCases(){
        List<Installation> installations = this.installationService.getInstallations();
        StringBuilder ids = new StringBuilder("(");
        for(Installation install : installations){
           ids.append("'").append(install.getId()).append("'").append(",");
        }
        ids.deleteCharAt(ids.toString().length() - 1);
        ids.append(")");

       return new ArrayList<>(this.repository.query(
            new WhereClause(
                String.format("Installation__c  IN %s", ids.toString() )
            )
       ));
    }

    @Override
    public Optional<ServiceCase> get(String id){

        List<SalesforceServiceCase> cases = this.repository.query(
                        new WhereClause(
                                String.format("Id = '%s'", id)
                        )
                );
        if(cases.isEmpty())
            return Optional.empty();
        return Optional.of(cases.get(0));

    }

    @Override
    public List<ExistingServiceCaseDto> getByOdooIdTest(String id){

        return null;

    }


    @Override
    public CaseSubmitResult submit(CaseDto dto){
        SalesforceServiceCase caseToSubmit = buildCaseFromDto(dto);
        if(caseToSubmit == null){
            return new DefaultCaseSubmitResult(false, "Failed to upload case");
        }
        List<SalesforceServiceCase> cases = new ArrayList<>();
        cases.add(caseToSubmit);
        try {
            this.repository.insert(cases);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return new DefaultCaseSubmitResult(false, "Failed to upload case");
        }
        return new DefaultCaseSubmitResult(true, "Successfully uploaded case!");
    }

    private SalesforceServiceCase buildCaseFromDto(CaseDto dto){

        if(dto.getIsInstallOperational()) {
            Installation install = this.installationService.getInstallationById(dto.getInstallationId());
            String accountId = install.getAccountId();
            String contactId = install.getContactId();
            String installationId = install.getId();
            String opportunityId = install.getOpportunityId();

            CaseSubject.CATEGORY category = CaseSubject.CATEGORY_MAP.get(dto.getCategory());

            SalesforceServiceCase caseToSubmit = new SalesforceServiceCase.Builder(accountId, contactId, installationId, opportunityId)
                    .recordTypeId(SalesforceServiceCaseRecordType.getByOperationalArea(install.getOperationalArea()))
                    .subject1(category.getIdName())
                    .subject2(" ")
                    .status(CaseStatus.STATUS.NEW.getViewName())
                    .customerDescription(dto.getDescription())
                    .caseDepartment(CaseDepartment.DEPARTMENT.TECHNICAL_SERVICE.getIdName())
                    .subject(category.getIdName())
                    .problemType(category.getProblemType())
                    .build();
            return caseToSubmit;
        }
        else{
            List<Installation> installs = this.installationService.getInstallations();
            if(installs.isEmpty())
                return null;
            Installation install = installs.get(0);
            String accountId = install.getAccountId();
            String contactId = install.getContactId();
            String installationId = install.getId();
            String opportunityId = install.getOpportunityId();
            SalesforceServiceCase caseToSubmit = new SalesforceServiceCase.Builder(accountId, contactId, installationId, opportunityId)
                    .recordTypeId(SalesforceServiceCaseRecordType.getByOperationalArea(install.getOperationalArea()))
                    .subject1(PRE_INSTALLATION_ISSUE)
                    .subject2(CasePreInstall.ISSUE_MAP.get(dto.getPreInstallIssue()).getIdName())
                    .status(CaseStatus.STATUS.NEW.getViewName())
                    .customerDescription(dto.getPreInstallDescription())
                    .caseDepartment(CaseDepartment.DEPARTMENT.SALES.getIdName())
                    .subject(PRE_INSTALLATION_ISSUE)
                    .build();
            return caseToSubmit;
        }
    }


}
