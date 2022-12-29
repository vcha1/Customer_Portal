package com.my1stle.customer.portal.serviceImpl.subscription;

import com.adobe.sign.model.agreements.AgreementCreationInfo;
import com.my1stle.customer.portal.persistence.model.SubscriptionEntity;
import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.ProductAgreementDocument;
import com.my1stle.customer.portal.service.product.ProductAgreementDocumentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * Unit Test for {@link SubscriptionAgreementCreationInfoFactory}
 */
@RunWith(MockitoJUnitRunner.class)
public class SubscriptionAgreementCreationInfoFactoryTest {

    private SubscriptionAgreementCreationInfoFactory subscriptionAgreementCreationInfoFactory;

    @Mock
    private ProductAgreementDocumentService mockProductAgreementDocumentService;

    @Mock
    private InstallationService mockInstallationService;


    @Before
    public void setUp() throws Exception {

        this.subscriptionAgreementCreationInfoFactory = new SubscriptionAgreementCreationInfoFactory(mockProductAgreementDocumentService, mockInstallationService);
        this.subscriptionAgreementCreationInfoFactory.setServerPort(8081);

    }

    @Test
    public void shouldCreateAgreementCreationInfo() {

        // Given
        SubscriptionEntity subscriptionEntity = mock(SubscriptionEntity.class, Answers.RETURNS_DEEP_STUBS);

        // Stubbing
        when(subscriptionEntity.getInstallationId()).thenReturn("some installation id");
        Installation stubbedInstallation = mock(Installation.class);
        ProductAgreementDocument stubbedProductAgreementDocument = mock(ProductAgreementDocument.class);
        when(mockInstallationService.getInstallationById(anyString())).thenReturn(stubbedInstallation);
        when(mockProductAgreementDocumentService.getByProductId(anyLong())).thenReturn(Optional.of(stubbedProductAgreementDocument));

        // Whekkn
        AgreementCreationInfo agreementCreationInfo = this.subscriptionAgreementCreationInfoFactory.create(subscriptionEntity);

        // Then
        verify(mockInstallationService, times(1)).getInstallationById(anyString());
        verify(mockProductAgreementDocumentService, times(1)).getByProductId(anyLong());
        assertNotNull(agreementCreationInfo);


    }
}