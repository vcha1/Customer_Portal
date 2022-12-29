package com.my1stle.customer.portal.serviceImpl.contactus;

import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.ProductService;
import com.my1stle.customer.portal.service.contactus.ContactUsResult;
import com.my1stle.customer.portal.service.contactus.ContactUsService;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.model.Product;
import com.my1stle.customer.portal.service.task.TaskService;
import com.my1stle.customer.portal.service.task.TaskServiceException;
import com.my1stle.customer.portal.serviceImpl.task.SalesforceTask;
import com.my1stle.customer.portal.serviceImpl.task.SalesforceTaskSpecification;
import com.my1stle.customer.portal.web.dto.contactus.ContactUsDto;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;


/**
 * This implementation of {@link ContactUsService} creates a task
 * record within Salesforce
 *
 * @implNote owner is set to <a href="https://1stlight.my.salesforce.com/_ui/core/userprofile/UserProfilePage?u=00570000001XoMa&tab=sfdc.ProfilePlatformFeed">Admin</a>
 */
@Service
public class SalesforceContactUsService implements ContactUsService {

    private TaskService<SalesforceTask, SalesforceTaskSpecification> taskService;
    private ProductService productService;
    private InstallationService installationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesforceContactUsService.class);

    // TODO remove hardcoded value and move value into someplace more configurable
    private static final String DEFAULT_OWNER = "00570000003pLNSAA2"; // Salesforce User : Brandon Nakae
    private static final String DEFAULT_STATUS = "Not Started"; // The default status in production org
    private static final String DEFAULT_PRIORITY = "Normal"; // The default priority in production org

    @Autowired
    public SalesforceContactUsService(
            TaskService<SalesforceTask, SalesforceTaskSpecification> taskService,
            ProductService productService,
            InstallationService installationService) {
        this.taskService = taskService;
        this.productService = productService;
        this.installationService = installationService;
    }

    @Override
    public ContactUsResult submit(ContactUsDto request) {

        Product product = this.productService.getById(request.getProductId());
        Installation installation = this.installationService.getInstallationById(request.getInstallationId());

        if (null == product) {
            throw new ResourceNotFoundException("Product Not Found!");
        }

        if (null == installation) {
            throw new ResourceNotFoundException("Installation Not Found!");
        }

        String subject = String.format("%s for %s", request.getReason().getLabel(), product.getName());
        String relatedTo = installation.getId();
        String message = request.getMessage();

        SalesforceTaskSpecification specification = new SalesforceTaskSpecification.Builder(DEFAULT_OWNER, subject, DEFAULT_STATUS, relatedTo)
                .priority(DEFAULT_PRIORITY)
                .description(StringUtils.append(message, String.format("\n\nContact Info\nPhone : %s\nEmail : %s", request.getPhone(), request.getEmail())))
                .build();

        try {
            SalesforceTask salesforceTask = this.taskService.add(specification);
        } catch (TaskServiceException e) {
            LOGGER.error(e.getMessage(), e);
            return new SalesforceContactUsResult(false, e.getMessage());
        }

        return new SalesforceContactUsResult(true, null);

    }

}