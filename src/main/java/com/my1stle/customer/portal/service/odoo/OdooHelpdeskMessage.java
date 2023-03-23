package com.my1stle.customer.portal.service.odoo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OdooHelpdeskMessage implements OdooHelpdeskMessageApi {

    @Autowired
    public OdooHelpdeskMessage() {
    }

    @Override
    public List<Map<String, ?>> getTicketMessage(Integer odooId, OdooObjectConnection odooConnection, String objectType){
        List<String> fields = Arrays.asList("body", "attachment_ids", "author_id", "email_from");
        List<Object> criteria = Arrays.asList(Arrays.asList("res_id", "=", odooId.toString()), Arrays.asList("model", "=", "helpdesk.ticket"));
        List<Map<String, ?>> results = odooConnection.findObjects(objectType, fields, criteria);
        return results;
    }


    @Override
    public List<Integer> createTicketMessage(Integer odooId, OdooObjectConnection odooConnection, String objectType, String messengerBody){
        List<Map<String, Object>> variables = new ArrayList<>();
        Map<String, Object> maps = new HashMap<>();
        maps.put("body", messengerBody);
        maps.put("model", "helpdesk.ticket");
        maps.put("res_id", odooId);
        variables.add(maps);
        List<Integer> results = odooConnection.createObjects(objectType, variables);
        return results;
    }


    @Override
    public List<Integer> createTicketMessageAuthor(Integer odooId, OdooObjectConnection odooConnection, String objectType,
                                                   String messengerBody, Integer mainFollowerId, String from){
        List<Map<String, Object>> variables = new ArrayList<>();
        Map<String, Object> maps = new HashMap<>();
        maps.put("body", messengerBody);
        maps.put("model", "helpdesk.ticket");
        maps.put("res_id", odooId);
        maps.put("author_id", mainFollowerId);
        maps.put("email_from", from);
        variables.add(maps);
        List<Integer> results = odooConnection.createObjects(objectType, variables);
        return results;
    }



    @Override
    public void updateTicketMessage(Integer messageId, OdooObjectConnection odooConnection, String objectType, String messengerBody){
        Map<String, Object> values = new HashMap<>();
        values.put("body", messengerBody);
        odooConnection.updateObject(objectType, messageId, values);
    }

    @Override
    public List<Map<String, ?>> getTicketAttachment(Integer odooId, OdooObjectConnection odooConnection, String objectType){
        List<String> fields = Arrays.asList("name", "datas");
        List<Object> criteria = Arrays.asList(Arrays.asList("res_id", "=", odooId.toString()), Arrays.asList("res_model", "=", "helpdesk.ticket"));
        List<Map<String, ?>> results = odooConnection.findObjects(objectType, fields, criteria);
        return results;
    }

    @Override
    public List<Map<String, ?>> getTicketAttachmentId(Integer id, OdooObjectConnection odooConnection, String objectType){
        List<String> fields = Arrays.asList("name");
        List<Object> criteria = Arrays.asList(Arrays.asList("id", "=", id));
        List<Map<String, ?>> results = odooConnection.findObjects(objectType, fields, criteria);
        return results;
    }


    @Override
    public List<Integer> createTicketAttachment(Integer odooId, OdooObjectConnection odooConnection, String objectType, String fileContent, String attachmentNames){
        List<Map<String, Object>> variables = new ArrayList<>();
        Map<String, Object> maps = new HashMap<>();
        maps.put("name", attachmentNames);
        maps.put("res_model", "helpdesk.ticket");
        maps.put("res_id", odooId);
        maps.put("datas", fileContent);
        variables.add(maps);
        List<Integer> results = odooConnection.createObjects(objectType, variables);
        return results;
    }


    @Override
    public List<Integer> createTicketAttachmentMessage(Integer odooId, OdooObjectConnection odooConnection, String objectType, Integer attachmentId){

        List<Map<String, Object>> variables = new ArrayList<>();
        Map<String, Object> maps = new HashMap<>();
        maps.put("attachment_ids",Arrays.asList(attachmentId));
        maps.put("model", "helpdesk.ticket");
        maps.put("res_id", odooId);
        variables.add(maps);
        List<Integer> results = odooConnection.createObjects(objectType, variables);
        return results;
    }



    @Override
    public List<Map<String, ?>> getAllTicketMessage(OdooObjectConnection odooConnection, String objectType){
        List<String> fields = Arrays.asList("body", "attachment_ids");
        List<Object> criteria = Arrays.asList(Arrays.asList("model", "=", "helpdesk.ticket"));
        List<Map<String, ?>> results = odooConnection.findObjects(objectType, fields, criteria);
        return results;
    }

    @Override
    public List<Map<String, ?>> getAllTicketAttachment(OdooObjectConnection odooConnection, String objectType){
        List<String> fields = Arrays.asList("name");
        List<Object> criteria = Arrays.asList(Arrays.asList("res_model", "=", "helpdesk.ticket"));
        List<Map<String, ?>> results = odooConnection.findObjects(objectType, fields, criteria);
        return results;
    }

    @Override
    public List<Map<String, ?>> getMultilineText(Integer odooId, OdooObjectConnection odooConnection, String objectType){
        List<String> fields = Arrays.asList("x_studio_text_field_Tonmo");
        List<Object> criteria = Arrays.asList(Arrays.asList("id", "=", odooId));
        List<Map<String, ?>> results = odooConnection.findObjects(objectType, fields, criteria);
        return results;
    }

    @Override
    public void setMultilineText(Integer odooId, OdooObjectConnection odooConnection, String objectType, String multiLineText){

        Map<String, Object> values = new HashMap<>();
        values.put("x_studio_text_field_Tonmo", multiLineText);
        odooConnection.updateObject(objectType, odooId, values);
    }

    @Override
    public List<Map<String, ?>> getAllTickets(OdooObjectConnection odooConnection){
        List<String> fieldsHelpdesk = Arrays.asList("name", "x_studio_oxpecker_id");
        List<Object> criteriaHelpdesk = Arrays.asList(Arrays.asList("id", ">", 0));
        List<Map<String, ?>> results = odooConnection.findObjects("helpdesk.ticket", fieldsHelpdesk, criteriaHelpdesk);
        return results;
    }


    @Override
    public List<Map<String, ?>> getAllTicketsNoOxpecker(OdooObjectConnection odooConnection){
        List<String> fieldsHelpdesk = Arrays.asList("name", "x_studio_installation", "description");
        List<Object> criteriaHelpdesk = Arrays.asList(Arrays.asList("x_studio_oxpecker_id", "=", 0));
        List<Map<String, ?>> results = odooConnection.findObjects("helpdesk.ticket", fieldsHelpdesk, criteriaHelpdesk);
        return results;
    }


    @Override
    public List<Map<String, ?>> getInstallationName(OdooObjectConnection odooConnection, Integer odooId){
        List<String> fieldsHelpdesk = Arrays.asList("name", "x_studio_installation");
        List<Object> criteriaHelpdesk = Arrays.asList(Arrays.asList("id", "=", odooId));
        List<Map<String, ?>> results = odooConnection.findObjects("helpdesk.ticket", fieldsHelpdesk, criteriaHelpdesk);
        return results;
    }

    @Override
    public List<Map<String, ?>> getFollower(OdooObjectConnection odooConnection, Integer odooId, String mainFollowerId){
        List<String> fieldsFollower = Arrays.asList("res_model", "partner_id", "res_id", "subtype_ids");
        Integer mainFollowerIdInt = Integer.valueOf(mainFollowerId);
        List<Object> criteriaFollower = Arrays.asList(Arrays.asList("res_id", "=", odooId),
                Arrays.asList("res_model", "=", "helpdesk.ticket"), Arrays.asList("partner_id", "=", mainFollowerIdInt));
        List<Map<String, ?>> results = odooConnection.findObjects("mail.followers", fieldsFollower, criteriaFollower);
        return results;
    }


    @Override
    public List<Map<String, ?>> getMainEmailFollower(OdooObjectConnection odooConnection, String posterEmail){
        List<String> fieldsFollower = Arrays.asList("name", "email");
        List<Object> criteriaFollower = Arrays.asList(Arrays.asList("email", "=", posterEmail));
        List<Map<String, ?>> results = odooConnection.findObjects("res.partner", fieldsFollower, criteriaFollower);
        return results;
    }

    @Override
    public List<Integer> createMainFollower(OdooObjectConnection odooConnection, String name, String posterEmail){


        List<Map<String, Object>> variables = new ArrayList<>();
        Map<String, Object> maps = new HashMap<>();
        maps.put("name", name);
        maps.put("email", posterEmail);
        variables.add(maps);
        List<Integer> results = odooConnection.createObjects("res.partner", variables);

        return results;
    }

    @Override
    public List<Integer> createFollower(OdooObjectConnection odooObjectConnection, String resModel, Integer odooIdInt, String subtype, String mainFollowerId){
        List<Map<String, Object>> variables = new ArrayList<>();
        Map<String, Object> maps = new HashMap<>();
        maps.put("res_model", resModel);
        maps.put("res_id", odooIdInt);
        maps.put("partner_id", mainFollowerId);
        maps.put("subtype_ids", Arrays.asList(43));
        variables.add(maps);
        List<Integer> results = odooObjectConnection.createObjects("mail.followers", variables);

        return results;

    }
}
