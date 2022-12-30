package com.my1stle.customer.portal.service.odoo;

import com.my1stle.customer.portal.service.serviceapi.ServiceApiCategory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
//import org.joda.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.*;

public class OdooHelpdeskData {

    private List<Object> id = new ArrayList();
    private List<String> name = new ArrayList();
    private List<String> description = new ArrayList();
    private List<String> issueType = new ArrayList();
    private List<String> subIssueType = new ArrayList();
    private List<String> scheduledDateTime = new ArrayList();
    private List<String> address = new ArrayList();
    private List<Object> status = new ArrayList();
    private List<Object> category = new ArrayList();
    private List<String> customerAddress = new ArrayList();

    public OdooHelpdeskData(String installation) {
        XmlRpcAuthenticationInformation xmlRpcAuthInfo = OdooConnectionConfiguration.generateDefaultXmlRpcAuthenticationInformation();
        XmlRpcClientFactory xmlRpcClientFactory = OdooConnectionConfiguration.createDefaultXmlRpcClientFactory();
        OdooObjectConnection odooConnection = new DefaultOdooObjectConnection(xmlRpcAuthInfo, xmlRpcClientFactory);

        String objectType = "helpdesk.ticket";
        List<String> fields = Arrays.asList("name");
        List<Object> criteria = Arrays.asList(Arrays.asList("x_studio_installation", "=", installation));
        try {
            List<Map<String, ?>> results = odooConnection.findObjects(objectType, fields, criteria);

            for (int i = 0; i < results.size(); i++){
                this.id.add(results.get(i).get("id"));
                this.name.add(odooConnection.findObjects(objectType, Arrays.asList("name"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("name").toString());
                this.description.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_service_description_2"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_service_description_2").toString());
                this.issueType.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_issue_type"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_issue_type").toString());

                if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_selection_field_oyOcA"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_selection_field_oyOcA").toString() != "false") {
                    this.subIssueType.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_selection_field_oyOcA"),
                            Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_selection_field_oyOcA").toString());
                } else if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_selection_field_oyOcA"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_selection_field_oyOcA").toString() == "false") {
                    this.subIssueType.add(null);
                }
                if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_scheduled_date_time"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_scheduled_date_time").toString() != "false") {


                    String testing = odooConnection.findObjects(objectType, Arrays.asList("x_studio_scheduled_date_time"),
                                    Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_scheduled_date_time").toString();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US);
                    LocalDateTime localDate = LocalDateTime.parse(testing, formatter);
                    localDate = localDate.minusHours(8);

                    this.scheduledDateTime.add(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a").format(localDate));
                    //this.scheduledDateTime.add(dtf.parseDateTime(odooConnection.findObjects(objectType, Arrays.asList("x_studio_scheduled_date_time"),
                    //        Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_scheduled_date_time").toString()));
                } else if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_scheduled_date_time"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_scheduled_date_time").toString() == "false") {
                    this.scheduledDateTime.add(null);
                }
                this.address.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_street"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_site_street").toString());

                Object[] statusids = (Object[]) odooConnection.findObjects(objectType, Arrays.asList("stage_id"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("stage_id");
                this.status.add(statusids[1]);
                //this.status.add(odooConnection.findObjects(objectType, Arrays.asList("stage_id"),
                //        Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("stage_id").toString());
                Object[] teamids = (Object[]) odooConnection.findObjects(objectType, Arrays.asList("team_id"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("team_id");
                this.category.add(teamids[1]);

                this.customerAddress.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_customer_address"),
                        Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_customer_address").toString());

            }
        } catch (Exception e) {
            //throw new RuntimeException(e);
        }


    }

    public OdooHelpdeskData(List<String> installation) {
        XmlRpcAuthenticationInformation xmlRpcAuthInfo = OdooConnectionConfiguration.generateDefaultXmlRpcAuthenticationInformation();
        XmlRpcClientFactory xmlRpcClientFactory = OdooConnectionConfiguration.createDefaultXmlRpcClientFactory();
        OdooObjectConnection odooConnection = new DefaultOdooObjectConnection(xmlRpcAuthInfo, xmlRpcClientFactory);

        String objectType = "helpdesk.ticket";
        List<String> fields = Arrays.asList("name");
        for (int install = 0; install < installation.size(); install++) {


            List<Object> criteria = Arrays.asList(Arrays.asList("x_studio_installation", "=", installation.get(install)));
            try {
                List<Map<String, ?>> results = odooConnection.findObjects(objectType, fields, criteria);
                //System.out.println(results.get(0).get("name"));
                for (int i = 0; i < results.size(); i++) {
                    this.id.add(results.get(i).get("id"));
                    this.name.add(odooConnection.findObjects(objectType, Arrays.asList("name"),
                            Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("name").toString());
                    this.description.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_service_description_2"),
                            Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_service_description_2").toString());
                    this.issueType.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_issue_type"),
                            Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_issue_type").toString());

                    if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_selection_field_oyOcA"),
                            Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_selection_field_oyOcA").toString() != "false") {
                        this.subIssueType.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_selection_field_oyOcA"),
                                Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_selection_field_oyOcA").toString());
                    } else if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_selection_field_oyOcA"),
                            Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_selection_field_oyOcA").toString() == "false") {
                        this.subIssueType.add(null);
                    }
                    if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_scheduled_date_time"),
                            Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_scheduled_date_time").toString() != "false") {


                        String testing = odooConnection.findObjects(objectType, Arrays.asList("x_studio_scheduled_date_time"),
                                Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_scheduled_date_time").toString();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US);
                        LocalDateTime localDate = LocalDateTime.parse(testing, formatter);
                        localDate = localDate.minusHours(8);

                        this.scheduledDateTime.add(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a").format(localDate));
                        //this.scheduledDateTime.add(dtf.parseDateTime(odooConnection.findObjects(objectType, Arrays.asList("x_studio_scheduled_date_time"),
                        //        Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_scheduled_date_time").toString()));
                    } else if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_scheduled_date_time"),
                            Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_scheduled_date_time").toString() == "false") {
                        this.scheduledDateTime.add(null);
                    }
                    this.address.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_street"),
                            Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_site_street").toString());

                    Object[] statusids = (Object[]) odooConnection.findObjects(objectType, Arrays.asList("stage_id"),
                            Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("stage_id");
                    this.status.add(statusids[1]);
                    //this.status.add(odooConnection.findObjects(objectType, Arrays.asList("stage_id"),
                    //        Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("stage_id").toString());
                    Object[] teamids = (Object[]) odooConnection.findObjects(objectType, Arrays.asList("team_id"),
                            Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("team_id");
                    this.category.add(teamids[1]);

                    this.customerAddress.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_customer_address"),
                            Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_customer_address").toString());

                }

            } catch (Exception e) {
                //throw new RuntimeException(e);
            }
        }


    }



    public OdooHelpdeskData(String installation, String caseId) {
        XmlRpcAuthenticationInformation xmlRpcAuthInfo = OdooConnectionConfiguration.generateDefaultXmlRpcAuthenticationInformation();
        XmlRpcClientFactory xmlRpcClientFactory = OdooConnectionConfiguration.createDefaultXmlRpcClientFactory();
        OdooObjectConnection odooConnection = new DefaultOdooObjectConnection(xmlRpcAuthInfo, xmlRpcClientFactory);

        String objectType = "helpdesk.ticket";
        //List<String> fields = Arrays.asList("name");
        //List<Object> criteria = Arrays.asList(Arrays.asList("x_studio_installation", "=", installation));
        try {
            //List<Map<String, ?>> results = odooConnection.findObjects(objectType, fields, criteria);

            this.id.add(caseId);
            this.name.add(odooConnection.findObjects(objectType, Arrays.asList("name"),
                    Arrays.asList(Arrays.asList("id", "=", caseId))).get(0).get("name").toString());
            this.description.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_service_description_2"),
                    Arrays.asList(Arrays.asList("id", "=", caseId))).get(0).get("x_studio_service_description_2").toString());
            this.issueType.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_issue_type"),
                    Arrays.asList(Arrays.asList("id", "=", caseId))).get(0).get("x_studio_issue_type").toString());

            if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_selection_field_oyOcA"),
                    Arrays.asList(Arrays.asList("id", "=", caseId))).get(0).get("x_studio_selection_field_oyOcA").toString() != "false") {
                this.subIssueType.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_selection_field_oyOcA"),
                        Arrays.asList(Arrays.asList("id", "=", caseId))).get(0).get("x_studio_selection_field_oyOcA").toString());
            } else if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_selection_field_oyOcA"),
                    Arrays.asList(Arrays.asList("id", "=", caseId))).get(0).get("x_studio_selection_field_oyOcA").toString() == "false") {
                this.subIssueType.add(null);
            }
            if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_scheduled_date_time"),
                    Arrays.asList(Arrays.asList("id", "=", caseId))).get(0).get("x_studio_scheduled_date_time").toString() != "false") {


                String testing = odooConnection.findObjects(objectType, Arrays.asList("x_studio_scheduled_date_time"),
                        Arrays.asList(Arrays.asList("id", "=", caseId))).get(0).get("x_studio_scheduled_date_time").toString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US);
                LocalDateTime localDate = LocalDateTime.parse(testing, formatter);
                localDate = localDate.minusHours(8);

                this.scheduledDateTime.add(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a").format(localDate));
                //this.scheduledDateTime.add(dtf.parseDateTime(odooConnection.findObjects(objectType, Arrays.asList("x_studio_scheduled_date_time"),
                //        Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("x_studio_scheduled_date_time").toString()));
            } else if (odooConnection.findObjects(objectType, Arrays.asList("x_studio_scheduled_date_time"),
                    Arrays.asList(Arrays.asList("id", "=", caseId))).get(0).get("x_studio_scheduled_date_time").toString() == "false") {
                this.scheduledDateTime.add(null);
            }
            this.address.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_site_street"),
                    Arrays.asList(Arrays.asList("id", "=", caseId))).get(0).get("x_studio_site_street").toString());

            Object[] statusids = (Object[]) odooConnection.findObjects(objectType, Arrays.asList("stage_id"),
                    Arrays.asList(Arrays.asList("id", "=", caseId))).get(0).get("stage_id");
            this.status.add(statusids[1]);
            //this.status.add(odooConnection.findObjects(objectType, Arrays.asList("stage_id"),
            //        Arrays.asList(Arrays.asList("id", "=", this.id))).get(i).get("stage_id").toString());
            Object[] teamids = (Object[]) odooConnection.findObjects(objectType, Arrays.asList("team_id"),
                    Arrays.asList(Arrays.asList("id", "=", caseId))).get(0).get("team_id");
            this.category.add(teamids[1]);

            this.customerAddress.add(odooConnection.findObjects(objectType, Arrays.asList("x_studio_customer_address"),
                    Arrays.asList(Arrays.asList("id", "=", caseId))).get(0).get("x_studio_customer_address").toString());


        } catch (Exception e) {
            //throw new RuntimeException(e);
        }

    }



    public List<Object> getId() {
        return id;
    }
    public List<String> getName() {
        return name;
    }
    public void setName(String name) {
        this.name.add(name);
    }
    public List<String> getDescription() {
        return description;
    }
    public List<String> getIssueType() {
        return issueType;
    }
    public List<String> getSubIssueType(){
        return subIssueType;
    }
    public List<String> getScheduledDateTime() {
        return scheduledDateTime;
    }
    public List<String> getAddress() {
        return address;
    }
    public List<Object> getStatus() {
        return status;
    }
    public List<Object> getCategory() {
        return category;
    }
    public List<String> getCustomerAddress() {
        return customerAddress;
    }

}


