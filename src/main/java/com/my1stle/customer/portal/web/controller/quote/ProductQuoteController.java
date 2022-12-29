package com.my1stle.customer.portal.web.controller.quote;

import com.my1stle.customer.portal.service.ProductService;
import com.my1stle.customer.portal.service.UserProvider;
import com.my1stle.customer.portal.service.contactus.ContactUsReason;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductQuoteController {

    private ProductService productService;
    private UserProvider userProvider;

    @Autowired
    public ProductQuoteController(ProductService productService, UserProvider userProvider) {
        this.productService = productService;
        this.userProvider = userProvider;
    }

    @GetMapping(value = "/product/{product_id}/quote")
    public String getQuote(@PathVariable("product_id") Long productId,
                           @RequestParam(name = "installation_id", required = false) String installationId,
                           RedirectAttributes redirectAttributes) {

        Product product = productService.getById(productId);

        if (null == product) {
            throw new ResourceNotFoundException("Product Not Found!");
        }

        addContactInfo(redirectAttributes);
        redirectAttributes.addAttribute("installation_id", installationId);
        redirectAttributes.addAttribute("reason", ContactUsReason.REQUESTING_QUOTE.name());
        redirectAttributes.addAttribute("product", product.getId());
        redirectAttributes.addAttribute("contact_message", generateQuoteContactMessage(product));

        return "redirect:/contact-us";

    }

    @GetMapping(value = "/product/{product_id}/enquire")
    public String enquire(@PathVariable("product_id") Long productId,
                          @RequestParam(name = "installation_id", required = false) String installationId,
                          RedirectAttributes redirectAttributes) {

        Product product = productService.getById(productId);

        if (null == product) {
            throw new ResourceNotFoundException("Product Not Found!");
        }

        addContactInfo(redirectAttributes);
        redirectAttributes.addAttribute("installation_id", installationId);
        redirectAttributes.addAttribute("reason", ContactUsReason.PRODUCT_ENQUIRY.name());
        redirectAttributes.addAttribute("product", product.getId());
        redirectAttributes.addAttribute("contact_message", generateEnquireContactMessage(product));

        return "redirect:/contact-us";

    }

    /**
     * Adds basic contact info to redirect attributes based on the current user
     *
     * @param redirectAttributes
     */
    private void addContactInfo(RedirectAttributes redirectAttributes) {

        User user = userProvider.getCurrentUser();

        redirectAttributes.addAttribute("first_name", user.getFirstName());
        redirectAttributes.addAttribute("last_name", user.getLastName());
        redirectAttributes.addAttribute("email", user.getEmail());

    }

    private static String generateEnquireContactMessage(Product product) {
        return String.format("Hello 1st Light Energy,\n\nI am would like to know more about %s\n\n", product.getName());
    }

    private static String generateQuoteContactMessage(Product product) {
        return String.format("Hello 1st Light Energy,\n\nI am requesting a quote for %s\n\n", product.getName());
    }

}