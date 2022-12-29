package com.my1stle.customer.portal.web.controller.subscription;

import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.ProductService;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.PaymentMethod;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.model.Subscription;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import com.my1stle.customer.portal.service.payment.PaymentMethodService;
import com.my1stle.customer.portal.service.payment.PaymentMethods;
import com.my1stle.customer.portal.service.pricing.ConvenienceFeeCalculator;
import com.my1stle.customer.portal.service.pricing.FeeCalculator;
import com.my1stle.customer.portal.service.pricing.PaymentSchedule;
import com.my1stle.customer.portal.service.subscription.SubscriptionException;
import com.my1stle.customer.portal.service.subscription.SubscriptionService;
import com.my1stle.customer.portal.service.util.MediaTypeUtil;
import com.my1stle.customer.portal.web.dto.subscription.SubscriptionAgreementDto;
import com.my1stle.customer.portal.web.exception.BadRequestException;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Optional;

@Controller
@RequestMapping(value = "/subscription")
public class SubscriptionController {

    private SubscriptionService subscriptionService;
    private InstallationService installationService;
    private PaymentMethodService paymentMethodService;
    private ProductService productService;
    private FeeCalculator feeCalculator;

    @Autowired
    public SubscriptionController(
            SubscriptionService subscriptionService,
            InstallationService installationService,
            PaymentMethodService paymentMethodService,
            ProductService productService,
            FeeCalculator feeCalculator) {
        this.subscriptionService = subscriptionService;
        this.installationService = installationService;
        this.paymentMethodService = paymentMethodService;
        this.productService = productService;
        this.feeCalculator = feeCalculator;
    }
    /*
    @GetMapping(value = "")
    public String viewSubscriptionAgreement(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(name = "installation_id") String installationId,
            @RequestParam(name = "product_id") Long productId,
            Model model) {

        Installation installation = installationService.getInstallationById(installationId);

        if (null == installation) {
            throw new ResourceNotFoundException("Installation Not Found!");
        }

        Product product = productService.getById(productId);

        if (null == product) {
            throw new ResourceNotFoundException("Product Not Found!");
        }

        if (product.getPaymentSchedule().equals(PaymentSchedule.SINGLE_PAYMENT)) {
            throw new BadRequestException("Expected Subscription Based Product");
        }

        if (!product.getProductAgreementDocument().isPresent()) {
            throw new BadRequestException("Product does have an agreement document!");
        }

        Optional<Subscription> existingSubscription = this.subscriptionService.getByOwnerProductAndInstallationId(
                currentUser.getId(), product.getId(), installation.getId()
        );

        if (existingSubscription.isPresent()) {
            return String.format("redirect:/subscription/%s/sign_agreement", existingSubscription.get().getId());
        }

        SubscriptionAgreementDto subscriptionAgreementDto = new SubscriptionAgreementDto();
        subscriptionAgreementDto.setInstallationId(installation.getId());
        subscriptionAgreementDto.setProductId(product.getId());

        model.addAttribute("request", subscriptionAgreementDto);
        model.addAttribute("installation", installation);
        model.addAttribute("product", product);
        model.addAttribute("total_price", product.getPricingType().calculation().apply(product, installation));

        return "subscription/agreement";

    }
    */
    //Update code with odoo below
    @GetMapping(value = "")
    public String viewSubscriptionAgreement(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(name = "installation_id") String installationId,
            @RequestParam(name = "product_id") Long productId,
            Model model) {

        OdooInstallationData odooInstallationData = new OdooInstallationData(installationId, "project.task");

        if (null == odooInstallationData) {
            throw new ResourceNotFoundException("Installation Not Found!");
        }

        Product product = productService.getById(productId);

        if (null == product) {
            throw new ResourceNotFoundException("Product Not Found!");
        }

        if (product.getPaymentSchedule().equals(PaymentSchedule.SINGLE_PAYMENT)) {
            throw new BadRequestException("Expected Subscription Based Product");
        }

        if (!product.getProductAgreementDocument().isPresent()) {
            throw new BadRequestException("Product does have an agreement document!");
        }

        Optional<Subscription> existingSubscription = this.subscriptionService.getByOwnerProductAndInstallationId(
                currentUser.getId(), product.getId(), odooInstallationData.getId().toString()
        );

        if (existingSubscription.isPresent()) {
            return String.format("redirect:/subscription/%s/sign_agreement", existingSubscription.get().getId());
        }

        SubscriptionAgreementDto subscriptionAgreementDto = new SubscriptionAgreementDto();
        subscriptionAgreementDto.setInstallationId(odooInstallationData.getId().toString());
        subscriptionAgreementDto.setProductId(product.getId());

        model.addAttribute("request", subscriptionAgreementDto);
        model.addAttribute("installation", odooInstallationData);
        model.addAttribute("product", product);
        model.addAttribute("total_price", product.getPricingType().calculation().apply(product, odooInstallationData));

        return "subscription/agreement";

    }


    @PostMapping(value = "")
    public String sendSubscriptionAgreement(@ModelAttribute @Valid SubscriptionAgreementDto request,
                                            @AuthenticationPrincipal User currentUser,
                                            Model model,
                                            RedirectAttributes redirectAttributes,
                                            HttpServletRequest httpServletRequest) {

        try {
            request.setUser(currentUser);
            Subscription subscription = this.subscriptionService.subscribe(request);
            return String.format("redirect:/subscription/%s/sign_agreement", subscription.getId());
        } catch (SubscriptionException e) {
            redirectAttributes.addAttribute("installation_id", request.getInstallationId());
            redirectAttributes.addAttribute("product_id", request.getProductId());
            redirectAttributes.addAttribute("error", e.getMessage());
            return "redirect:/subscription";
        }

    }

    @GetMapping(value = "/{subscription_id}")
    public String viewSubscriptionDetails(
            @AuthenticationPrincipal User currentUser,
            @PathVariable("subscription_id") Long subscriptionId,
            Model model,
            RedirectAttributes redirectAttributes) {

        Subscription subscription = getSubscription(currentUser.getId(), subscriptionId);

        model.addAttribute("subscription", subscription);

        return "subscription/subscription-detail";

    }

    @GetMapping(value = "/{subscription_id}/sign_agreement")
    public String signingAgreement(@AuthenticationPrincipal User currentUser, @PathVariable("subscription_id") Long subscriptionId, Model model) {

        Subscription subscription = getSubscription(currentUser.getId(), subscriptionId);

        if (subscription.getSubscriptionTermsOfService().getHasAgreedToTermsOfService()) {
            return String.format("redirect:/subscription/%s/payment_information", subscription.getId());
        }

        model.addAttribute("subscription", subscription);

        return "subscription/sign-agreement";

    }

    @GetMapping(value = "/{subscription_id}/payment_information")
    public String collectPaymentInformation(
            @AuthenticationPrincipal User currentUser,
            @PathVariable("subscription_id") Long subscriptionId,
            Model model,
            RedirectAttributes redirectAttributes,
            @Value("${paypal.client.id}")String payPalClientId) {

        Subscription subscription = getSubscription(currentUser.getId(), subscriptionId);

        if (!subscription.getSubscriptionTermsOfService().getHasAgreedToTermsOfService()) {
            redirectAttributes.addAttribute("error", "Please sign agreement before entering payment information");
            return String.format("redirect:/subscription/%s/sign_agreement", subscription.getId());
        }

        if (subscription.isActivated()) {
            return String.format("redirect:/subscription/%s", subscription.getId());
        }

        Product product = subscription.getProduct();
        //Installation installation = subscription.getInstallation();
        OdooInstallationData odooInstallationData = subscription.getOdooInstallationData();

        PaymentMethod paymentMethod = this.paymentMethodService.getByName(PaymentMethods.PAYPAL)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("%s Payment Not Found!", PaymentMethods.PAYPAL)));

        model.addAttribute("subscription", subscription);
        model.addAttribute("convenience_fee", feeCalculator.calculate(product, odooInstallationData, paymentMethod));
        model.addAttribute("total_price", this.subscriptionService.getTotalPrice(subscription));
        model.addAttribute("payPalClientId",payPalClientId) ;

        return "subscription/collect-payment-information";

    }

    @GetMapping(value = "/{subscription_id}/agreement_complete")
    public String viewAgreementCompletedLandingPage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable("subscription_id") Long subscriptionId, Model model, RedirectAttributes redirectAttributes) {

        Subscription subscription = getSubscription(currentUser.getId(), subscriptionId);

        if (!subscription.getSubscriptionTermsOfService().getHasAgreedToTermsOfService()) {
            redirectAttributes.addAttribute("error", "Please sign agreement");
            return String.format("redirect:/subscription/%s/sign_agreement", subscription.getId());
        }

        model.addAttribute("subscription", subscription);

        return "subscription/agreement-complete";

    }

    @GetMapping(value = "/{subscription_id}/view_agreement")
    public ResponseEntity<InputStreamResource> viewAgreement(
            @AuthenticationPrincipal User currentUser,
            @PathVariable("subscription_id") Long subscriptionId, Model model, RedirectAttributes redirectAttributes) throws IOException {

        Subscription subscription = getSubscription(currentUser.getId(), subscriptionId);

        if (!subscription.getSubscriptionTermsOfService().getHasAgreedToTermsOfService()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Resource> agreement = this.subscriptionService.getSubscriptionAgreementResource(currentUser.getId(), subscriptionId);

        if (!agreement.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        InputStream is = agreement.get().getInputStream();
        MediaType mediaType = MediaTypeUtil.getMediaType(is);
        String extension = MediaTypeUtil.getExtension(mediaType);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("inline;filename=%s", URLEncoder.encode("tos_agreement", "UTF-8")))
                .contentType(mediaType)
                .body(new InputStreamResource(is));

    }

    @PostMapping(value = "/{subscription_id}/order/{order_id}/confirm")
    public String confirmSubscriptionOrder(
            @AuthenticationPrincipal User currentUser,
            @PathVariable("subscription_id") Long subscriptionId,
            @PathVariable("order_id") String orderId,
            Model model, RedirectAttributes redirectAttributes) {

        Subscription subscription = getSubscription(currentUser.getId(), subscriptionId);

        if (!subscription.getSubscriptionTermsOfService().getHasAgreedToTermsOfService()) {
            redirectAttributes.addAttribute("error", "Order cannot be confirmed! Agreement has not been signed. Please sign agreement");
            return String.format("redirect:/subscription/%s/sign_agreement", subscription.getId());
        }

        try {
            subscription = this.subscriptionService.activateSubscription(currentUser.getId(), subscription.getId(), orderId);
        } catch (SubscriptionException e) {
            //redirectAttributes.addAttribute("id", subscription.getInstallation().getId());
            redirectAttributes.addAttribute("id", subscription.getOdooInstallationData().getId());
            redirectAttributes.addAttribute("error", e.getMessage());
            return "redirect:/schedule/product-selection";
        }

        redirectAttributes.addAttribute("success", String.format("Thank you for subscribing to %s. The following are your subscription detail.", subscription.getProduct().getName()));

        return String.format("redirect:/subscription/%s", subscription.getId());

    }

    private Subscription getSubscription(Long ownerId, @PathVariable("subscription_id") Long subscriptionId) {
        return this.subscriptionService.getByOwnerAndSubscriptionId(ownerId, subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription Not Found!"));
    }

}