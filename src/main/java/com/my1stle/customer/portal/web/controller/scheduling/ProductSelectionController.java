package com.my1stle.customer.portal.web.controller.scheduling;

import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.ProductService;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.model.Subscription;
import com.my1stle.customer.portal.service.odoo.DefaultInstallationServiceOdoo;
import com.my1stle.customer.portal.service.odoo.InstallationServiceOdoo;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import com.my1stle.customer.portal.service.subscription.SubscriptionService;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class ProductSelectionController {

    private InstallationService installationService;
    private InstallationServiceOdoo installationServiceOdoo;
    private ProductService productService;
    private SubscriptionService subscriptionService;

    /*
    @Autowired
    public ProductSelectionController(InstallationService installationService, ProductService productService, SubscriptionService subscriptionService) {
        this.installationService = installationService;
        this.productService = productService;
        this.subscriptionService = subscriptionService;
    }
    */
    @Autowired
    public ProductSelectionController(InstallationServiceOdoo installationServiceOdoo, ProductService productService,
                                      SubscriptionService subscriptionService, InstallationService installationService) {
        this.installationServiceOdoo = installationServiceOdoo;
        this.productService = productService;
        this.subscriptionService = subscriptionService;
        this.installationService= installationService;
    }


    @GetMapping(value = "/schedule/product-selection")
    public String selectProduct(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(name = "id", required = true) String selectedInstallation,
            Model model) {

        List<Product> products = this.productService.getAll();
        //List<Installation> installations = this.installationService.getInstallations();
        //Installation installation = this.installationService.getInstallationById(selectedInstallation);
        Installation installationsdad = this.installationService.getInstallationById("a064u00001nqPxOAAU");

        DefaultInstallationServiceOdoo installations = this.installationServiceOdoo.getInstallationByEmail();
        OdooInstallationData odooInstallationData = new OdooInstallationData(selectedInstallation, "project.task");
        String address = "";
        String id = "";
        String selectedInstallation2 = "";
        for(int i = 0; i<installations.getId().size(); i++){
            selectedInstallation2 = selectedInstallation.replaceAll("\\[", "").replaceAll("\\]","");
            if (installations.getId().get(i).toString().equals(selectedInstallation2)){
                address = installations.getAddress().get(i);
                id = installations.getId().get(i).toString();
            }
        }
        model.addAttribute("products", products);
        //model.addAttribute("selected_installation", installation);
        model.addAttribute("address", address);
        //model.addAttribute("selected_installation", installationsdad);
        model.addAttribute("selected_installation", odooInstallationData);
        model.addAttribute("installations", installations);
        model.addAttribute("subscription",
                //this.subscriptionService.getOwnersActiveSubscriptions(currentUser.getId(), installation.getId())
                this.subscriptionService.getOwnersActiveSubscriptions(currentUser.getId(), id)
                        .stream()
                        .collect(Collectors.toMap(Subscription::getProduct, Function.identity()))

        );

        return "scheduling/product-selection";

    }

}