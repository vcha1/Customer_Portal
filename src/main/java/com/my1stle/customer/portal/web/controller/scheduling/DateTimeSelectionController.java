package com.my1stle.customer.portal.web.controller.scheduling;

import com.dev1stle.scheduling.system.v1.model.Calendar;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.ProductService;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import com.my1stle.customer.portal.service.servicerequest.ServiceRequestService;
import com.my1stle.customer.portal.service.datetimeselection.DateTimeSelectionService;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.model.ServiceRequest;
import com.my1stle.customer.portal.web.dto.scheduling.DateTimeSelectionDto;
import com.my1stle.customer.portal.web.dto.suggestion.Appointment;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class DateTimeSelectionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeSelectionController.class);

    private ObjectMapper objectMapper;
    private DateTimeSelectionService dateTimeSelectionService;
    private ServiceRequestService serviceRequestService;
    private ProductService productService;
    private InstallationService installationService;

    @Autowired
    public DateTimeSelectionController(
            ObjectMapper objectMapper,
            DateTimeSelectionService dateTimeSelectionService,
            ServiceRequestService serviceRequestService, ProductService productService,
            InstallationService installationService) {

        this.objectMapper = objectMapper;
        this.dateTimeSelectionService = dateTimeSelectionService;
        this.serviceRequestService = serviceRequestService;
        this.productService = productService;
        this.installationService = installationService;
    }

    @GetMapping(value = "/schedule/date-time-selection")
    public String selectDateTime(
            @RequestParam(name = "id") String installationId,
            @RequestParam(name = "product") Long productId,
            @RequestParam(name = "quantity", required=false, defaultValue = "1") int quantity,
            @RequestParam(name = "render_events", required = false) Boolean renderEvents,
            RedirectAttributes redirectAttributes,
            Model model) throws JsonProcessingException {

        OdooInstallationData odooInstallationData =  new OdooInstallationData(installationId, "project.task");

        if (null == odooInstallationData) {
            throw new ResourceNotFoundException("Installation not found!");
        }

        model.addAttribute("installation", odooInstallationData);

        Product product = productService.getById(productId);

        if (null == product) {
            throw new ResourceNotFoundException("Product not found!");
        }

        model.addAttribute("product", product);

        BigDecimal price = product.getPricingType().calculation().apply(product, odooInstallationData);
        int compareTo = price.compareTo(BigDecimal.ZERO);

        if (compareTo <= 0) {
            redirectAttributes.addAttribute("installation_id", odooInstallationData.getId());
            return String.format("redirect:/product/%s/quote", product.getId());
        }

        if (product.getIsSubscriptionBased()) {
            redirectAttributes.addAttribute("installation_id", odooInstallationData.getId());
            redirectAttributes.addAttribute("product_id", product.getId());
            return "redirect:/subscription";
        }

        if (product.getIsSchedulable()) {

            List<Calendar> assignees = this.dateTimeSelectionService.getAvailableCalendars(odooInstallationData.getId().toString(), product.getId());

            if (assignees.isEmpty()) {
                return "scheduling/product-unavailable";
            }

            Appointment appointment = this.dateTimeSelectionService.createAppointment(installationId, productId);

            model.addAttribute("request", new DateTimeSelectionDto());
            model.addAttribute("appointment", objectMapper.writeValueAsString(appointment));
            model.addAttribute("resources", objectMapper.writeValueAsString(assignees));
            model.addAttribute("render_events", renderEvents == null ? false : renderEvents);

            return "scheduling/date-time-selection";

        } else {

            ServiceRequest serviceRequest = this.serviceRequestService.create(product, odooInstallationData, quantity);
            return String.format("redirect:/service-request/checkout/payment/%d", serviceRequest.getId());

        }

    }

    @PostMapping(value = "/date-time-selection")
    public String fowardRequest(@ModelAttribute @Valid DateTimeSelectionDto request) {

        ServiceRequest serviceRequest = this.serviceRequestService.create(request);

        return String.format("redirect:/service-request/checkout/payment/%d", serviceRequest.getId());

    }

}