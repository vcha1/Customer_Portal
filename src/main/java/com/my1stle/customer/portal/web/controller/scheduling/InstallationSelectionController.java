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

    private InstallationServiceOdoo installationServiceOdoo;

    public InstallationSelectionController(InstallationServiceOdoo installationServiceOdoo) {
        this.installationServiceOdoo = installationServiceOdoo;
    }

    @GetMapping(value = "/schedule/installation-selection")
    public String selectInstallationAndProduct(Model model, RedirectAttributes redirectAttributes) {

        DefaultInstallationServiceOdoo installations = this.installationServiceOdoo.getInstallationByEmail();

        if (installations.getId().isEmpty()) {
            throw new ResourceNotFoundException("No installations found!");
        }

        if (installations.getId().size() == 1) {
            redirectAttributes.addAttribute("id", installations.getId().get(0).toString());

            return "redirect:/schedule/product-selection";
        }

        model.addAttribute("installations", installations);

        return "scheduling/installation-selection";

    }

}