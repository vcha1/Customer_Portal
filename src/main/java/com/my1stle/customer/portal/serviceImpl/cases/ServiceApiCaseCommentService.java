package com.my1stle.customer.portal.serviceImpl.cases;

import com.my1stle.customer.portal.service.InstallationService;
import com.my1stle.customer.portal.service.cases.CaseCommentService;
import com.my1stle.customer.portal.service.cases.CaseServiceException;
import com.my1stle.customer.portal.service.model.NewServiceCaseComment;
import com.my1stle.customer.portal.service.model.ServiceCase;
import com.my1stle.customer.portal.service.odoo.OdooHelpdeskMessage;
import com.my1stle.customer.portal.service.odoo.OdooObjectConnection;
import com.my1stle.customer.portal.service.serviceapi.*;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.baeldung.persistence.model.User;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.Comment;
import java.util.*;
import java.util.function.Predicate;

@Service
public class ServiceApiCaseCommentService implements CaseCommentService {

    private final ServiceCasesApi serviceCasesApi;
    private final InstallationService installationService;

    private final ServiceUsersApi serviceUsersApi;
    private final OdooHelpdeskMessage odooHelpdeskMessage;
    private final OdooObjectConnection odooObjectConnection;

    @Autowired
    public ServiceApiCaseCommentService(ServiceCasesApi serviceCasesApi, InstallationService installationService,
                                        ServiceUsersApi serviceUsersApi, OdooHelpdeskMessage odooHelpdeskMessage,
                                        OdooObjectConnection odooObjectConnection) {
        this.serviceCasesApi = serviceCasesApi;
        this.installationService = installationService;
        this.serviceUsersApi = serviceUsersApi;
        this.odooHelpdeskMessage = odooHelpdeskMessage;
        this.odooObjectConnection = odooObjectConnection;
    }

    @Override
    public ServiceCase addComment(String caseId, NewServiceCaseComment comment) throws CaseServiceException, ServiceApiException {

        if (!StringUtils.isNumeric(caseId)) {
            throw new CaseServiceException("Invalid id");
        }
        long id = Long.parseLong(caseId);
        ExistingServiceCaseDto existingServiceCase = getExistingServiceCase(id);
        //validate(existingServiceCase);

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long serviceApiUserId = currentUser.getServiceApiUserId();
        String body = comment.getBody();

        CommentDto commentDto = new CommentDto(serviceApiUserId, body, false);

        //putting comments straight to Odoo from customer portal  before putting in php
        createCommentInOdoo(commentDto, caseId);

        addComment(existingServiceCase.getId(), commentDto);

        return ServiceCaseProxy.from(existingServiceCase);

    }

    private ExistingServiceCaseDto getExistingServiceCase(long id) throws CaseServiceException {
        try {
            return serviceCasesApi.get(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Case Not Found!"));
        } catch (ServiceApiException e) {
            throw new CaseServiceException(e.getMessage(), e);
        }
    }

    private void validate(ExistingServiceCaseDto existingServiceCase) {
        Predicate<ExistingServiceCaseDto> currentUserOwnsExistingServiceCase = new CurrentUserOwnsExistingServiceCase(this.installationService);
        if (!currentUserOwnsExistingServiceCase.test(existingServiceCase)) {
            throw new ResourceNotFoundException("Case Not Found!");
        }
    }

    private void addComment(long id, CommentDto commentDto) throws CaseServiceException {
        try {
            this.serviceCasesApi.addComment(id, commentDto);
        } catch (ServiceApiException e) {
            throw new CaseServiceException(e.getMessage(), e);
        }
    }


    //This function create comment in odoo after checking to make sure there are followers
    private void createCommentInOdoo(CommentDto commentDto,String caseId) throws ServiceApiException {
        Optional<ExistingServiceCaseDto> caseFound = serviceCasesApi.get(Long.parseLong(caseId));
        if (caseFound.isPresent()) {
            String caseFoundOdooId = caseFound.get().getOdooId();
            Long posterId = commentDto.getPosterId();
            Optional<ExistingServiceUserDto> userFound = serviceUsersApi.findById(posterId);
            if(userFound.isPresent()) {
                String userFoundEmail = userFound.get().getEmail();
                String userFoundFirstName = userFound.get().getFirstName();
                String userFoundLastName = userFound.get().getLastName();
                String userFoundName = userFoundFirstName + " " + userFoundLastName;
                createMainFollowerAndFollower(userFoundEmail, userFoundName, caseFoundOdooId);
                createCommentsInOdoo(commentDto.getBody(), userFoundEmail, caseFoundOdooId);
            }
        }
    }


    //This function create all the necessary main follower and sub follower for the provided poster emails.
    private void createMainFollowerAndFollower(String userFoundEmail, String userFoundName, String odooId){

        //Check to see if the Main Follower of the sub follower exists in Odoo
        List<Map<String, ?>> mainFollower = this.odooHelpdeskMessage.getMainEmailFollower(this.odooObjectConnection, userFoundEmail.toLowerCase());

        //If they do not exist, create a main follower account, then after that, we can get the main follower ID
        String mainFollowerId;
        if (mainFollower.isEmpty()){
            List<Integer> newMainFollowerId = this.odooHelpdeskMessage.createMainFollower(this.odooObjectConnection, userFoundName, userFoundEmail.toLowerCase());
            mainFollowerId = newMainFollowerId.get(0).toString();
        }else {
            mainFollowerId = mainFollower.get(0).get("id").toString();
        }

        Integer odooIdInt = Integer.valueOf(odooId);
        //Check if the one that post the comment is a follower of the ticket
        List<Map<String, ?>> followersFound = this.odooHelpdeskMessage.getFollower(this.odooObjectConnection, odooIdInt, mainFollowerId);

        //If the person that post the comment cannot be found as a follower, create them as a follower in the ticket
        if (followersFound.isEmpty()){
            String resModel = "helpdesk.ticket";
            String subtype = "43";
            List<Integer> newFollowerId = this.odooHelpdeskMessage.createFollower(this.odooObjectConnection, resModel, odooIdInt, subtype, mainFollowerId);
            followersFound = this.odooHelpdeskMessage.getFollower(this.odooObjectConnection, odooIdInt, mainFollowerId);
        }

    }

    //This function puts the comments that are in PHP to the log notes in Odoo
    private void createCommentsInOdoo(String commentBody, String posterEmails, String odooId){
        List<String> odooCommentsCleaned = new ArrayList<>();
        List<Map<String, ?>> odooComments = this.odooHelpdeskMessage.getTicketMessage(Integer.valueOf(odooId), this.odooObjectConnection, "mail.message");
        for (Map<String, ?> odooComment: odooComments) {
            String odooCommentBody = Jsoup.parse(odooComment.get("body").toString()).text();
            odooCommentsCleaned.add(odooCommentBody);
        }

        String odooCommentBodyCleaned = Jsoup.parse(commentBody.toString()).text();
        if (!odooCommentsCleaned.contains(odooCommentBodyCleaned)){
            //Check to see if the Main Follower of the sub follower exists in Odoo
            List<Map<String, ?>> mainFollower = this.odooHelpdeskMessage.getMainEmailFollower(this.odooObjectConnection, posterEmails.toLowerCase());
            //This should exist as the previous function create all the main followers and sub followers
            String mainFollowerId = mainFollower.get(0).get("id").toString();
            String mainFollowerName = mainFollower.get(0).get("name").toString();
            String mainFollowerEmail = mainFollower.get(0).get("email").toString();
            String from = "\"" + mainFollowerName + "\"<" + mainFollowerEmail + ">";
            this.odooHelpdeskMessage.createTicketMessageAuthor(Integer.valueOf(odooId), this.odooObjectConnection, "mail.message", commentBody, Integer.valueOf(mainFollowerId), from);
        }
    }

}
