package com.my1stle.customer.portal.web.controller.cases;


import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.cases.CaseAttachmentService;
import com.my1stle.customer.portal.service.cases.CaseCommentService;
import com.my1stle.customer.portal.service.cases.CaseService;
import com.my1stle.customer.portal.service.cases.CaseServiceException;
import com.my1stle.customer.portal.service.cases.CaseSubmitResult;
import com.my1stle.customer.portal.service.model.NewServiceCaseAttachment;
import com.my1stle.customer.portal.service.model.NewServiceCaseComment;
import com.my1stle.customer.portal.service.model.ServiceCase;
import com.my1stle.customer.portal.service.model.ServiceCaseAttachment;
import com.my1stle.customer.portal.service.odoo.OdooHelpdeskData;
import com.my1stle.customer.portal.service.odoo.OdooInstallationData;
import com.my1stle.customer.portal.service.serviceapi.ExistingServiceCaseDto;
import com.my1stle.customer.portal.service.serviceapi.ServiceApiCategory;
import com.my1stle.customer.portal.service.serviceapi.ServiceApiException;
import com.my1stle.customer.portal.serviceImpl.serviceapi.DefaultServiceCasesApi;
import com.my1stle.customer.portal.serviceImpl.servicerequest.SalesforceServiceCase;
import com.my1stle.customer.portal.util.MimeTypeUtility;
import com.my1stle.customer.portal.web.dto.cases.CaseDto;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.mime.MimeType;
import org.baeldung.persistence.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping(value = "/case")
public class CaseController {

    private final String CREATE_CASE = "/create";
    private final String VIEW_CASE = "/{caseId}";
    private final InstallationService installationService;
    private final CaseService caseService;
    private final CaseCommentService caseCommentService;
    private final CaseAttachmentService caseAttachmentService;

    private final Logger LOGGER = LoggerFactory.getLogger(CaseController.class);

    private String currentCaseId;

    private OdooHelpdeskData odooTicketDataAll;

    @Autowired
    public CaseController(
            InstallationService installationService,
            @Qualifier("serviceApiCaseServiceDecorator") CaseService caseService,
            CaseCommentService caseCommentService,
            CaseAttachmentService caseAttachmentService) {
        this.installationService = installationService;
        this.caseService = caseService;
        this.caseCommentService = caseCommentService;
        this.caseAttachmentService = caseAttachmentService;
    }

    @GetMapping(value = "")
    public String caseHub(@AuthenticationPrincipal User user, Model model) {
        OdooInstallationData odooData = new OdooInstallationData(user.getEmail().toLowerCase());
        //OdooInstallationData odooData = new OdooInstallationData("cavlcantim@aol.com");
        OdooHelpdeskData odooTicketData = new OdooHelpdeskData(odooData.getNameList());
        this.odooTicketDataAll = odooTicketData;

        model.addAttribute("odooTicketData", odooTicketData);
        model.addAttribute("installations", this.installationService.getInstallations());
        return "case/hub";
    }

    @GetMapping(value = CREATE_CASE)
    public String createCase(@AuthenticationPrincipal User user, Model model) {
        OdooInstallationData odooData = new OdooInstallationData(user.getEmail().toLowerCase());;

        model.addAttribute("installations", odooData);
        model.addAttribute("request", new CaseDto());
        model.addAttribute("caseIssueCategories", ServiceApiCategory.getSystemOperationalCategories());
        model.addAttribute("casePreInstallIssueCategories", ServiceApiCategory.getPreInstallationCategories());

        return "case/create";
    }

    @PostMapping(value = CREATE_CASE)
    public String submitCase(
            @AuthenticationPrincipal User user,
            @ModelAttribute @Valid CaseDto request,
            RedirectAttributes redirectAttributes,
            HttpServletRequest httpServletRequest
    ) {
        CaseSubmitResult result = this.caseService.submit(request);
        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("success", StringUtils.isBlank(result.getMessage()) ? "Your issue has been submitted!" : result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("error", StringUtils.isBlank(result.getMessage()) ? "We're sorry, something went wrong. Please try once more." : result.getMessage());
            return "redirect:" + httpServletRequest.getHeader("referer");
        }
        return "redirect:/case";
    }


    @GetMapping(value = VIEW_CASE)
    public String viewCase(
            @AuthenticationPrincipal User user,
            Model model,
            @PathVariable("caseId") String caseId
    ) throws ServiceApiException {
        currentCaseId = caseId;
        List<ExistingServiceCaseDto> serviceCaseOdoo = this.caseService.getByOdooIdTest(caseId);

        String caseIdPhpMyAdmin = String.valueOf(serviceCaseOdoo.get(0).getId());

        ServiceCase serviceCase = this.caseService.get(caseIdPhpMyAdmin)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found!"));

        OdooHelpdeskData odooTicketData = new OdooHelpdeskData("Installation", caseId);

        List<String> firstAndLastId = new ArrayList<>();
        //Set the IDs to navigate to
        for (int i = 0; i<this.odooTicketDataAll.getId().size(); i++){
            if (this.odooTicketDataAll.getId().size() > 1) {
                if (Objects.equals(this.odooTicketDataAll.getId().get(i).toString(), caseId)) {
                    if (i == 0) {
                        firstAndLastId.add(this.odooTicketDataAll.getId().get(i).toString());
                        //firstAndLastId.add(this.odooTicketDataAll.getId().get(this.odooTicketDataAll.getId().size()-1).toString());
                        firstAndLastId.add(this.odooTicketDataAll.getId().get(i + 1).toString());

                    } else if (i == (this.odooTicketDataAll.getId().size() - 1)) {
                        firstAndLastId.add(this.odooTicketDataAll.getId().get(i - 1).toString());
                        firstAndLastId.add(this.odooTicketDataAll.getId().get(i).toString());
                        //firstAndLastId.add(this.odooTicketDataAll.getId().get(0).toString());
                    } else {
                        firstAndLastId.add(this.odooTicketDataAll.getId().get(i - 1).toString());
                        firstAndLastId.add(this.odooTicketDataAll.getId().get(i + 1).toString());
                    }
                }
            }else if (this.odooTicketDataAll.getId().size() == 1){
                if (i == 0) {
                    firstAndLastId.add(this.odooTicketDataAll.getId().get(i).toString());
                    firstAndLastId.add(this.odooTicketDataAll.getId().get(i).toString());
                }
            }
        }

        model.addAttribute("odooTicketDataAll", this.odooTicketDataAll);
        model.addAttribute("firstAndLastId", firstAndLastId);
        model.addAttribute("odooTicketData", odooTicketData);
        model.addAttribute("case", serviceCase);
        return "case/view";
    }

    @PostMapping(value = "/{id}/comment")
    public String addComment(@PathVariable(name = "id") String caseId, @ModelAttribute NewServiceCaseComment comment, RedirectAttributes redirectAttributes) {
        try {
            this.caseCommentService.addComment(caseId, comment);
            return String.format("redirect:/case/%s", currentCaseId);
        } catch (CaseServiceException e) {
            LOGGER.error(e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Sorry, something went wrong. Please try again");
            return String.format("redirect:/case/%s", currentCaseId);
        } catch (ServiceApiException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/{id}/attachment")
    public String addAttachment(@PathVariable(name = "id") String caseId, @ModelAttribute NewServiceCaseAttachment attachment, RedirectAttributes redirectAttributes) {
        try {
            this.caseAttachmentService.addAttachment(caseId, attachment);
            return String.format("redirect:/case/%s", currentCaseId);
        } catch (CaseServiceException e) {
            LOGGER.error(e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Sorry, something went wrong. Please try again");
            return String.format("redirect:/case/%s", currentCaseId);
        } catch (ServiceApiException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ResponseBody
    @GetMapping(value = "/{caseId}/attachment/{attachmentId}")
    public ResponseEntity<Resource> viewAttachment(
            @PathVariable(name = "caseId") String caseId, @PathVariable(name = "attachmentId") String attachmentId, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {


        ServiceCase serviceCase = this.caseService.get(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found!"));

        ServiceCaseAttachment serviceCaseAttachment = serviceCase.getServiceCaseAttachments()
                .stream()
                .filter(attachment -> StringUtils.equalsIgnoreCase(attachmentId, attachment.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found!"));

        Resource resource = serviceCaseAttachment.getResource();

        MimeType mimeType = getMimeType(resource);

        return generateResponseEntity(serviceCaseAttachment, resource, mimeType);

    }


    private MimeType getMimeType(Resource resource) {
        try {
            return MimeTypeUtility.getMimeType(resource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private ResponseEntity<Resource> generateResponseEntity(ServiceCaseAttachment serviceCaseAttachment, Resource resource, MimeType mimeType) {
        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, String.format("inline;filename=%s", URLEncoder.encode(serviceCaseAttachment.getName(), "UTF-8")))
                    .contentType(MediaType.valueOf(mimeType.getName()))
                    .body(resource);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
