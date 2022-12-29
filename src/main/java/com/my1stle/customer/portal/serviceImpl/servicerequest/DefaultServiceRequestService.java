package com.my1stle.customer.portal.serviceImpl.servicerequest;

import com.my1stle.customer.portal.persistence.model.PaymentDetailEntity;
import com.my1stle.customer.portal.persistence.model.ProductEntity;
import com.my1stle.customer.portal.persistence.model.ServiceRequestEntity;
import com.my1stle.customer.portal.persistence.repository.PaymentDetailRepository;
import com.my1stle.customer.portal.persistence.repository.ServiceRequestRepository;
import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.UserProvider;
import com.my1stle.customer.portal.service.model.CalendarResource;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.Order;
import com.my1stle.customer.portal.service.model.PaymentDetail;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import com.my1stle.customer.portal.service.order.OrderService;
import com.my1stle.customer.portal.service.servicerequest.ServiceRequestService;
import com.my1stle.customer.portal.serviceImpl.model.CalendarResourceProxy;
import com.my1stle.customer.portal.serviceImpl.model.ServiceRequestExtended;
import com.my1stle.customer.portal.web.dto.scheduling.DateTimeSelectionDto;
import com.my1stle.customer.portal.web.exception.BadRequestException;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class DefaultServiceRequestService implements ServiceRequestService {

    private ServiceRequestRepository serviceRequestRepository;
    private OrderService orderService;
    private UserProvider userProvider;
    private InstallationService installationService;
    private PaymentDetailRepository paymentDetailRepository;
    private CrudRepository<ProductEntity, Long> productEntityRepository;

    @Autowired
    public DefaultServiceRequestService(
            ServiceRequestRepository serviceRequestRepository,
            OrderService orderService,
            UserProvider userProvider,
            InstallationService installationService,
            PaymentDetailRepository paymentDetailRepository,
            CrudRepository<ProductEntity, Long> productEntityRepository) {

        this.serviceRequestRepository = serviceRequestRepository;
        this.orderService = orderService;
        this.userProvider = userProvider;
        this.installationService = installationService;
        this.paymentDetailRepository = paymentDetailRepository;
        this.productEntityRepository = productEntityRepository;

    }

    @Override
    public Order generateOrder(ServiceRequest serviceRequest) {
        return this.orderService.generate(serviceRequest);
    }

    /**
     * @param product      product
     * @param installation installation
     * @param quantity     product quantity
     * @return service request
     * @throws BadRequestException if quantity is less than zero
     */
    /*
    @Override
    public ServiceRequest create(Product product, Installation installation, int quantity) {

        Objects.requireNonNull(product);
        Objects.requireNonNull(installation);
        if (quantity <= 0)
            throw new BadRequestException("Expected quantity be greater than zero!");

        ServiceRequestEntity request = new ServiceRequestEntity();

        Optional<ProductEntity> productEntityOptional = productEntityRepository.findById(product.getId());

        if (!productEntityOptional.isPresent()) {
            throw new ResourceNotFoundException("Product Not Found!");
        }

        //set values of new payment to service request table on phpmyadmin
        request.setProduct(productEntityOptional.get());
        request.setInstallationId(installation.getId());
        request.setCalendarResourceId(null);
        request.setStartTime(null);
        request.setCustomerNotes(null);
        request.setQuantity(quantity);

        request.setUser(this.userProvider.getCurrentUser());

        serviceRequestRepository.save(request);

        return new ServiceRequestExtended(request, new CalendarResourceProxy(request.getCalendarResourceId()), installation);
    }
    */

    /**
     * @param product      product
     * @param odooInstallationData odooInstallationData
     * @param quantity     product quantity
     * @return service request
     * @throws BadRequestException if quantity is less than zero
     */
    @Override
    public ServiceRequest create(Product product, OdooInstallationData odooInstallationData, int quantity) {

        Objects.requireNonNull(product);
        Objects.requireNonNull(odooInstallationData);
        if (quantity <= 0)
            throw new BadRequestException("Expected quantity be greater than zero!");

        ServiceRequestEntity request = new ServiceRequestEntity();

        Optional<ProductEntity> productEntityOptional = productEntityRepository.findById(product.getId());

        if (!productEntityOptional.isPresent()) {
            throw new ResourceNotFoundException("Product Not Found!");
        }

        //set values of new payment to service request table on phpmyadmin
        request.setProduct(productEntityOptional.get());
        request.setInstallationId(odooInstallationData.getId().toString());
        request.setCalendarResourceId(null);
        request.setStartTime(null);
        request.setCustomerNotes(null);
        request.setQuantity(quantity);

        request.setUser(this.userProvider.getCurrentUser());

        serviceRequestRepository.save(request);

        return new ServiceRequestExtended(request, new CalendarResourceProxy(request.getCalendarResourceId()), odooInstallationData);
    }

    @Override
    public ServiceRequest create(DateTimeSelectionDto dateTimeSelectionDto) {

        ServiceRequestEntity request = new ServiceRequestEntity();

        Optional<ProductEntity> productEntityOptional = productEntityRepository.findById(dateTimeSelectionDto.getProductId());

        if (!productEntityOptional.isPresent()) {
            throw new ResourceNotFoundException("Product Not Found!");
        }

        //Installation installation = installationService.getInstallationById(dateTimeSelectionDto.getInstallationId());
        OdooInstallationData odooInstallationData = new OdooInstallationData(dateTimeSelectionDto.getInstallationId());

        request.setProduct(productEntityOptional.get());
        request.setCalendarResourceId(dateTimeSelectionDto.getCalendarId());
        request.setInstallationId(dateTimeSelectionDto.getInstallationId());
        request.setStartTime(dateTimeSelectionDto.getStartDateTime());
        request.setCustomerNotes(dateTimeSelectionDto.getNotes());
        request.setUser(this.userProvider.getCurrentUser());

        serviceRequestRepository.save(request);

        return new ServiceRequestExtended(request, new CalendarResourceProxy(request.getCalendarResourceId()), odooInstallationData);

    }

    @Override
    public ServiceRequest getById(Long id) {

        ServiceRequestEntity entity = this.serviceRequestRepository.findByIdAndUser(id, this.userProvider.getCurrentUser());

        //Installation installation = this.installationService.getInstallationById(entity.getInstallationId());
        OdooInstallationData odooInstallationData = new OdooInstallationData(entity.getInstallationId(), "project.task");
        CalendarResource resource = new CalendarResourceProxy(entity.getCalendarResourceId());

        return new ServiceRequestExtended(entity, resource, odooInstallationData);

    }

    @Override
    public void setPaymentDetail(ServiceRequest request, PaymentDetail paymentDetail) {

        ServiceRequestEntity serviceRequestEntity = this.serviceRequestRepository.findByIdAndUser(request.getId(), this.userProvider.getCurrentUser());
        Optional<PaymentDetailEntity> paymentDetailEntityOptional = this.paymentDetailRepository.findByIdAndOwner(paymentDetail.getId(), this.userProvider.getCurrentUser());


        if (!paymentDetailEntityOptional.isPresent())
            throw new RuntimeException("Payment Detail Entity not found!");

        serviceRequestEntity.setPaymentDetail(paymentDetailEntityOptional.get());

        this.serviceRequestRepository.save(serviceRequestEntity);


    }

}