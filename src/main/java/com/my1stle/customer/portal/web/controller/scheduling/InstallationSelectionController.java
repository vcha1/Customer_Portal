package com.my1stle.customer.portal.web.controller.scheduling;

import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.odoo.DefaultInstallationServiceOdoo;
import com.my1stle.customer.portal.service.odoo.InstallationServiceOdoo;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class InstallationSelectionController {

    //private InstallationService installationService;

    private InstallationServiceOdoo installationServiceOdoo;

    /*
    @Autowired
    public InstallationSelectionController(InstallationService installationService) {
        this.installationService = installationService;
    }
    */

    public InstallationSelectionController(InstallationServiceOdoo installationServiceOdoo) {
        this.installationServiceOdoo = installationServiceOdoo;
    }

    @GetMapping(value = "/schedule/installation-selection")
    public String selectInstallationAndProduct(Model model, RedirectAttributes redirectAttributes) {

        //List<Installation> installations = this.installationService.getInstallations();

        DefaultInstallationServiceOdoo installations = this.installationServiceOdoo.getInstallationByEmail();

        if (installations.getId().isEmpty()) {
            throw new ResourceNotFoundException("No installations found!");
        }

        if (installations.getId().size() == 1) {
            //redirectAttributes.addAttribute("id", installations.get(0).getId());
            redirectAttributes.addAttribute("id", installations.getId().toString());
            //redirectAttributes.addAttribute("id", "a064u00001nqPxOAAU");

            return "redirect:/schedule/product-selection";
        }

        model.addAttribute("installations", installations);

        return "scheduling/installation-selection";

    }

}