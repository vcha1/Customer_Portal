package com.my1stle.customer.portal.service.odoo;

public interface ExternalIdMapping {
    String getOdooModelType();
    String getOdooModuleName();
    String getExternalId();
    Integer getOdooRecordId();
}
