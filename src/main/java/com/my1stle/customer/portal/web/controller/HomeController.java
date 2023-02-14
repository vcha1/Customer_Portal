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

    @GetMapping("/home")
    public String homeAction() {
        DefaultInstallationServiceOdoo installations = new DefaultInstallationServiceOdoo();
        System.out.println(installations.getName());
        System.out.println(installations.getAddress());
        String url = String.format("/installation/%s/details", installations.getId().get(0));
        return "redirect:" + url;
    }

}
