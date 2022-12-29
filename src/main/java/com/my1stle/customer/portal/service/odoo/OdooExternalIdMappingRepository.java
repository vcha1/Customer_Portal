package com.my1stle.customer.portal.service.odoo;

import java.util.List;

public interface OdooExternalIdMappingRepository {
    List<Integer> createExternalIdMappings(List<ExternalIdMapping> idMappingList);
    List<ExternalIdMapping> getExternalIdMappingsByExternalId(List<String> externalIdList);
}

