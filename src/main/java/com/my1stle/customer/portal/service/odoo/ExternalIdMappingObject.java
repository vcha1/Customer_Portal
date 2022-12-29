package com.my1stle.customer.portal.service.odoo;


public class ExternalIdMappingObject implements ExternalIdMapping {
    private final String odooModelType;
    private final String odooModuleName;
    private final String externalId;
    private final Integer odooRecordId;

    public ExternalIdMappingObject(String odooModelType, String odooModuleName, String externalId, Integer odooRecordId) {
        this.odooModelType = odooModelType;
        this.odooModuleName = odooModuleName;
        this.externalId = externalId;
        this.odooRecordId = odooRecordId;
    }

    @Override
    public String getOdooModelType() {
        return this.odooModelType;
    }

    @Override
    public String getOdooModuleName() {
        return this.odooModuleName;
    }

    @Override
    public String getExternalId() {
        return this.externalId;
    }

    @Override
    public Integer getOdooRecordId() {
        return this.odooRecordId;
    }
}
