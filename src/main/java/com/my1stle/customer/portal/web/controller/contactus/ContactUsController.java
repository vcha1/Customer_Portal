package com.my1stle.customer.portal.web.controller.contactus;

import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.ProductService;
import com.my1stle.customer.portal.service.contactus.ContactUsReason;
import com.my1stle.customer.portal.service.contactus.ContactUsResult;
import com.my1stle.customer.portal.service.contactus.ContactUsService;
import com.my1stle.customer.portal.service.odoo.DefaultInstallationServiceOdoo;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import com.my1stle.customer.portal.serviceImpl.task.SalesforceTaskService;
import com.my1stle.customer.portal.web.dto.contactus.ContactUsDto;
import org.baeldung.persistence.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class ContactUsController {

    private ContactUsService contactUsService;
    private ProductService productService;
   // private InstallationService installationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactUsController.class);

    @Autowired
    //public ContactUsController(ContactUsService contactUsService, ProductService productService, InstallationService installationService) {
    public ContactUsController(ContactUsService contactUsService, ProductService productService) {
        this.contactUsService = contactUsService;
        this.productService = productService;
        //this.installationService = installationService;
    }

    @GetMapping(value = "/contact-us")
    public String viewContactUsForm(Model model, @AuthenticationPrincipal User user) {
        //DefaultInstallationServiceOdoo odooInstallations = new DefaultInstallationServiceOdoo();
        OdooInstallationData odooInstallationData = new OdooInstallationData(user.getEmail().toLowerCase());
        model.addAttribute("odooInstallations", odooInstallationData);
        model.addAttribute("request", new ContactUsDto());
        model.addAttribute("products", productService.getAll());
        model.addAttribute("reasons", ContactUsReason.values());

        return "contactus/index";

    }

    @PostMapping(value = "/contact-us")
    public String submitContactUsForm(@ModelAttribute @Valid ContactUsDto request, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        ContactUsResult result = this.contactUsService.submit(request);

        if (result.isSuccessful()) {
            redirectAttributes.addAttribute("success", "We got your message! We'll respond to you as soon as possible");
        } else {
            LOGGER.error(result.errorMessage());
            redirectAttributes.addAttribute("error", "Oops! Something went wrong. Please try again.");
            return "redirect:" + httpServletRequest.getHeader("referer");
        }

        return "redirect:/contact-us";

    }

}
