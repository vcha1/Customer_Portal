package com.my1stle.customer.portal.serviceImpl.contactus;


import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.ProductService;
import com.my1stle.customer.portal.service.contactus.ContactUsReason;
import com.my1stle.customer.portal.service.contactus.ContactUsResult;
import com.my1stle.customer.portal.service.contactus.ContactUsService;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.task.TaskService;
import com.my1stle.customer.portal.service.task.TaskServiceException;
import com.my1stle.customer.portal.serviceImpl.task.SalesforceTask;
import com.my1stle.customer.portal.serviceImpl.task.SalesforceTaskSpecification;
import com.my1stle.customer.portal.web.dto.contactus.ContactUsDto;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit Test for {@link SalesforceContactUsService}
 */
/*
@RunWith(MockitoJUnitRunner.class)
public class SalesforceContactUsServiceTest {

    private ContactUsService contactUsService;

    @Mock
    private TaskService<SalesforceTask, SalesforceTaskSpecification> mockTaskService;

    @Mock
    private ProductService mockProductService;

    @Mock
    private InstallationService mockInstallationService;

    @Before
    public void setUp() throws Exception {

        contactUsService = new SalesforceContactUsService(mockTaskService, mockProductService, mockInstallationService);

    }

    @Test
    public void shouldCreateASalesforceTask() throws TaskServiceException {

        // Given
        Long productId = 1L;
        String installationId = "installation id";
        Product stubProduct = mock(Product.class);
        Installation stubInstallation = mock(Installation.class);

        when(stubInstallation.getId()).thenReturn(installationId);
        when(stubProduct.getName()).thenReturn("Stub Product Name");

        ContactUsDto request = new ContactUsDto();
        request.setReason(ContactUsReason.REQUESTING_QUOTE);
        request.setProductId(productId);
        request.setInstallationId(installationId);

        when(mockProductService.getById(eq(productId))).thenReturn(stubProduct);
        when(mockInstallationService.getInstallationById(eq(installationId))).thenReturn(stubInstallation);

        // When
        ContactUsResult submit = contactUsService.submit(request);

        // Then
        verify(mockProductService, times(1)).getById(eq(productId));
        verify(mockTaskService, times(1)).add(any(SalesforceTaskSpecification.class));
        assertTrue(submit.isSuccessful());
        assertTrue(StringUtils.isBlank(submit.errorMessage()));

    }

    @Test
    public void shouldAttemptToCreateASalesforceTask() throws TaskServiceException {

        // Given
        Long productId = 1L;
        String installationId = "installation id";
        Product stubProduct = mock(Product.class);
        Installation stubInstallation = mock(Installation.class);

        ContactUsDto request = new ContactUsDto();
        request.setReason(ContactUsReason.REQUESTING_QUOTE);
        request.setProductId(productId);
        request.setInstallationId(installationId);

        when(mockProductService.getById(eq(productId))).thenReturn(stubProduct);
        when(mockInstallationService.getInstallationById(eq(installationId))).thenReturn(stubInstallation);


        String stubErrorMessage = "Stub Error!";

        when(mockTaskService.add(any(SalesforceTaskSpecification.class))).thenThrow(new TaskServiceException(stubErrorMessage));

        // When
        ContactUsResult submit = contactUsService.submit(request);

        // Then
        verify(mockProductService, times(1)).getById(eq(productId));
        verify(mockTaskService, times(1)).add(any(SalesforceTaskSpecification.class));
        assertFalse(submit.isSuccessful());
        assertEquals(stubErrorMessage, submit.errorMessage());

    }

    @Test
    public void shouldThrowResourceNotFoundExceptionWhenProductNotFound() throws TaskServiceException {

        // Given
        Long productId = 1L;
        String installationId = "installation id";
        Product stubProduct = mock(Product.class);
        Installation stubInstallation = mock(Installation.class);

        ContactUsDto request = new ContactUsDto();
        request.setReason(ContactUsReason.REQUESTING_QUOTE);
        request.setProductId(productId);
        request.setInstallationId(installationId);

        when(mockProductService.getById(eq(productId))).thenReturn(null);
        when(mockInstallationService.getInstallationById(eq(installationId))).thenReturn(stubInstallation);

        // When
        try {
            ContactUsResult submit = contactUsService.submit(request);
        } catch (ResourceNotFoundException e) {

            // Then
            verify(mockProductService, times(1)).getById(eq(productId));
            verify(mockTaskService, times(0)).add(any(SalesforceTaskSpecification.class));
            return;

        }

        fail("Expected Resource Not Found Exception!");

    }



}
*/
