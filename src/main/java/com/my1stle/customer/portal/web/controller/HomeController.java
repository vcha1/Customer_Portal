package com.my1stle.customer.portal.web.controller;

import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.odoo.DefaultInstallationServiceOdoo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    /*
    private InstallationService installationService;

    @Autowired
    public HomeController(InstallationService installationService) {
        this.installationService = installationService;
    }

    @GetMapping("/home")
    public String homeAction() {
        List<Installation> installations = this.installationService.getInstallations();
        String url = String.format("/installation/%s/details", installations.get(0).getId());

        return "redirect:" + url;
    }
    */


    @GetMapping("/home")
    public String homeAction() {
        DefaultInstallationServiceOdoo installations = new DefaultInstallationServiceOdoo();
        String url = String.format("/installation/%s/details", installations.getId().get(0));
        //url = "/create-payment-intent";
        return "redirect:" + url;
    }



    /*
    private InstallationService installationService;

    @Autowired
    public HomeController(InstallationService installationService) {
        this.installationService = installationService;
    }

    @GetMapping("/home")
    public String homeAction() {
        List<Installation> installations = this.installationService.getInstallations();
        DefaultInstallationServiceOdoo installationsOdoo = new DefaultInstallationServiceOdoo();
        String url = String.format("/installation/%s/details", installationsOdoo.getId().get(0));

        return "redirect:" + url;
    }

     */
}
