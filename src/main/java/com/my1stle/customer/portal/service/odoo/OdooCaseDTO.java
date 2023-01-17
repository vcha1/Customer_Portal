package com.my1stle.customer.portal.service.odoo;

import com.my1stle.customer.portal.service.cases.CaseSubmitResult;
import com.my1stle.customer.portal.service.model.Installation;
import com.my1stle.customer.portal.service.serviceapi.ServiceApiCategory;
import com.my1stle.customer.portal.service.serviceapi.ServiceApiGroup;
import com.my1stle.customer.portal.service.serviceapi.ServiceCaseDto;
import com.my1stle.customer.portal.service.serviceapi.ServiceCaseStatus;
//import com.my1stle.customer.portal.serviceImpl.cases.ServiceCaseDefaults;
import com.my1stle.customer.portal.web.dto.cases.CaseDto;
import com.my1stle.customer.portal.web.exception.ResourceNotFoundException;
import org.baeldung.persistence.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

public class OdooCaseDTO {

    private Object id;

    public OdooCaseDTO (ServiceCaseDto serviceCaseDto, String addressId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = user.getEmail();
        //OdooInstallationData odooData = new OdooInstallationData(userEmail);
        OdooInstallationData odooData = new OdooInstallationData(addressId, "project.task");
        String installationName = odooData.getName();

        XmlRpcAuthenticationInformation xmlRpcAuthInfo = OdooConnectionConfiguration.generateDefaultXmlRpcAuthenticationInformation();
        XmlRpcClientFactory xmlRpcClientFactory = OdooConnectionConfiguration.createDefaultXmlRpcClientFactory();
        OdooObjectConnection odooConnection = new DefaultOdooObjectConnection(xmlRpcAuthInfo, xmlRpcClientFactory);

        String objectType = "helpdesk.ticket";
        List<String> fields = Arrays.asList("name");
        List<Object> criteria = Arrays.asList(Arrays.asList("x_studio_installation", "=", installationName));

        List<Map<String, Object>> createdName = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", installationName);
        createdName.add(map);

        try {

            List<Integer> results = odooConnection.createObjects(objectType, createdName);

            map.clear();
            map.put("x_studio_service_description_2", serviceCaseDto.getDescription());
            map.put("x_studio_installation", odooData.getId());
            map.put("team_id", 2);

            long issueType = serviceCaseDto.getIssueType();
            long subIssueType = serviceCaseDto.getSubIssueType();

            if (issueType == 15){
                map.put("x_studio_issue_type", "Pre-Installation Issue");
            } else if (issueType == 9){
                map.put("x_studio_issue_type", "Status of Construction");
            } else if (issueType == 8){
                map.put("x_studio_issue_type", "Tax Question");
            } else if (issueType == 10){
                map.put("x_studio_issue_type", "Request copies of");
            }  else if (issueType == 11){
                map.put("x_studio_issue_type", "Request contract from");
            } else {
                map.put("x_studio_issue_type", ServiceApiCategory.valueOf(issueType).getLabel());
            }

            if (subIssueType == 15){
                map.put("x_studio_selection_field_oyOcA", "Pre-Installation Issue");
            } else if (subIssueType == 9){
                map.put("x_studio_selection_field_oyOcA", "Status of Construction");
            } else if (subIssueType == 8){
                map.put("x_studio_selection_field_oyOcA", "Tax Question");
            } else if (subIssueType == 10){
                map.put("x_studio_selection_field_oyOcA", "Request copies of");
            }  else if (subIssueType == 11){
                map.put("x_studio_selection_field_oyOcA", "Request contract from");
            } else {
                map.put("x_studio_selection_field_oyOcA", ServiceApiCategory.valueOf(subIssueType).getLabel());
            }
            odooConnection.updateObject(objectType, results.get(0), map);

            this.id = results.get(0);


        } catch (Exception e) {

        }


    }

    public Object getId() {
        return this.id;
    }

}
