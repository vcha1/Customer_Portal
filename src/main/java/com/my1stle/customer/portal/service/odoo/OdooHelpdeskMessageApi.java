package com.my1stle.customer.portal.service.odoo;

import java.util.List;
import java.util.Map;

public interface OdooHelpdeskMessageApi {

    List<Map<String, ?>> getTicketMessage(Integer odooId, OdooObjectConnection odooConnection, String objectType);

    List<Integer> createTicketMessage(Integer odooId, OdooObjectConnection odooConnection, String objectType, String messengerBody);

    List<Integer> createTicketMessageAuthor(Integer odooId, OdooObjectConnection odooConnection, String objectType,
                                            String messengerBody, Integer mainFollowerId, String from);

    void updateTicketMessage(Integer messageId, OdooObjectConnection odooConnection, String objectType, String messengerBody);

    List<Map<String, ?>> getTicketAttachment(Integer odooId, OdooObjectConnection odooConnection, String objectType);

    List<Map<String, ?>> getTicketAttachmentId(Integer id, OdooObjectConnection odooConnection, String objectType);

    List<Integer> createTicketAttachment(Integer odooId, OdooObjectConnection odooConnection, String objectType, String fileContent, String attachmentNames);

    List<Integer> createTicketAttachmentMessage(Integer odooId, OdooObjectConnection odooConnection, String objectType, Integer attachmentId);

    List<Map<String, ?>> getAllTicketMessage(OdooObjectConnection odooConnection, String objectType);

    List<Map<String, ?>> getAllTicketAttachment(OdooObjectConnection odooConnection, String objectType);

    List<Map<String, ?>> getMultilineText(Integer odooId, OdooObjectConnection odooConnection, String objectType);

    void setMultilineText(Integer odooId, OdooObjectConnection odooConnection, String objectType, String multiLineText);

    List<Map<String, ?>> getAllTickets(OdooObjectConnection odooConnection);

    List<Map<String, ?>> getAllTicketsNoOxpecker(OdooObjectConnection odooConnection);

    List<Map<String, ?>> getInstallationName(OdooObjectConnection odooConnection, Integer odooId);

    List<Map<String, ?>> getFollower(OdooObjectConnection odooConnection, Integer odooId, String mainFollowerId);

    List<Map<String, ?>> getMainEmailFollower(OdooObjectConnection odooConnection, String posterEmail);

    List<Integer> createMainFollower(OdooObjectConnection odooConnection, String name, String posterEmail);

    List<Integer> createFollower(OdooObjectConnection odooObjectConnection, String resModel, Integer odooIdInt, String subtype, String mainFollowerId);
}
