package com.my1stle.customer.portal.serviceImpl.cases;


import com.dev1stle.repository.specification.salesforce.WhereClause;
import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.cases.CasePreInstall;
import com.my1stle.customer.portal.service.cases.CaseSubject;
import com.my1stle.customer.portal.service.cases.CaseSubmitResult;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.serviceImpl.servicerequest.SalesforceServiceCaseRepository;
import com.my1stle.customer.portal.web.dto.cases.CaseDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCaseServiceTest {

    public DefaultCaseService service;

    @Mock
    private SalesforceServiceCaseRepository mockRepository;

    @Mock
    private InstallationService mockInstallationService;

    @Before
    public void setUp(){
       service = new DefaultCaseService(mockInstallationService, mockRepository) ;
    }



    @Test
    public void shouldGetAllCases(){

       service.getCases();

       verify(mockInstallationService, times(1)).getInstallations();
       verify(mockRepository, times(1)).query(any(WhereClause.class));
    }

    @Test
    public void shouldSubmitInstallOperationalCase(){

        CaseDto mockDto = mock(CaseDto.class);
        Installation mockInstall = mock(Installation.class);
        String installationId = "installid T3ST1D";
        String contactId = "contactId T3ST1D";
        String oppId = "oppId T3ST1D";
        String accountId= "accountId T3ST1D";
        Boolean isInstallOperational = true;
        String description = "test-desc";
        String category = CaseSubject.CATEGORY.ELECTRICAL.getViewName();

        when(mockInstallationService.getInstallationById(any(String.class))).thenReturn(mockInstall);
        when(mockInstall.getAccountId()).thenReturn(accountId);
        when(mockInstall.getContactId()).thenReturn(contactId);
        when(mockInstall.getId()).thenReturn(installationId);
        when(mockInstall.getOpportunityId()).thenReturn(oppId);

        when(mockDto.getInstallationId()).thenReturn(installationId);
        when(mockDto.getIsInstallOperational()).thenReturn(isInstallOperational);
        when(mockDto.getDescription()).thenReturn(description);
        when(mockDto.getCategory()).thenReturn(category);

        CaseSubmitResult result = service.submit(mockDto);

        verify(mockRepository, times(1)).insert(any());

        assertEquals(result.getMessage(), "Successfully uploaded case!");
        assertTrue(result.isSuccess());
    }


    @Test
    public void shouldSubmitPreInstallCase(){
        CaseDto mockDto = mock(CaseDto.class);
        Boolean isInstallOperational = false;
        String description = "test-desc";
        String preInstallIssue = CasePreInstall.ISSUE.REQUEST_CONTACT_FROM_SALES_REP.getViewName();
        Installation mockInstall = mock(Installation.class);
        String installationId = "installid T3ST1D";
        String contactId = "contactId T3ST1D";
        String oppId = "oppId T3ST1D";
        String accountId= "accountId T3ST1D";
        List<Installation> installationList = new ArrayList<>();
        installationList.add(mockInstall);

        when(mockInstallationService.getInstallations()).thenReturn(installationList);
        when(mockInstall.getAccountId()).thenReturn(accountId);
        when(mockInstall.getContactId()).thenReturn(contactId);
        when(mockInstall.getId()).thenReturn(installationId);
        when(mockInstall.getOpportunityId()).thenReturn(oppId);

        when(mockDto.getIsInstallOperational()).thenReturn(isInstallOperational);
        when(mockDto.getPreInstallDescription()).thenReturn(description);
        when(mockDto.getPreInstallIssue()).thenReturn(preInstallIssue);

        CaseSubmitResult result = service.submit(mockDto);

        verify(mockRepository, times(1)).insert(any());
        assertEquals(result.getMessage(), "Successfully uploaded case!");
        assertTrue(result.isSuccess());

    }


}
