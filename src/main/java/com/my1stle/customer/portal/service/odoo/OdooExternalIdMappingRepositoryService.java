package com.my1stle.customer.portal.service.odoo;


import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OdooExternalIdMappingRepositoryService implements OdooExternalIdMappingRepository {
    private static final String MODEL_NAME = "ir.model.data";
    private static final List<String> MODEL_FIELDS = Arrays.asList("model", "name", "res_id");

    private final OdooObjectConnection connection;
    private final ObjectConverter converter;

    public OdooExternalIdMappingRepositoryService(OdooObjectConnection connection) {
        this.connection = connection;
        this.converter = new ObjectConverter();
    }

    @Override
    public List<Integer> createExternalIdMappings(List<ExternalIdMapping> idMappingList) {
        List<Map<String, Object>> plainObjects = this.converter.plainObjectsFromMappings(idMappingList);

        return this.connection.createObjects(MODEL_NAME, plainObjects);
    }

    @Override
    public List<ExternalIdMapping> getExternalIdMappingsByExternalId(List<String> externalIdList) {
        if(externalIdList.isEmpty())
            return Collections.emptyList();

        List<Map<String, ?>> plainObjects = this.connection.findObjects(
                MODEL_NAME,
                MODEL_FIELDS,
                Collections.singletonList(
                        Arrays.asList("name", "in", externalIdList)
                )
        );

        return this.converter.mappingsFromPlainObjects(plainObjects);
    }


    private static class ObjectConverter {
        public List<ExternalIdMapping> mappingsFromPlainObjects(List<Map<String, ?>> objects) {
            return objects.stream()
                    .map(this::mappingFromPlainObject)
                    .collect(Collectors.toList());
        }

        public ExternalIdMapping mappingFromPlainObject(Map<String, ?> plainObject) {
            return new ExternalIdMappingObject(
                    (String)plainObject.get("model"),
                    (String)plainObject.get("module"),
                    (String)plainObject.get("name"),
                    (Integer)plainObject.get("res_id")
            );
        }

        public List<Map<String, Object>> plainObjectsFromMappings(List<ExternalIdMapping> mappings) {
            return mappings.stream()
                    .map(this::plainObjectFromMapping)
                    .collect(Collectors.toList());
        }

        public Map<String, Object> plainObjectFromMapping(ExternalIdMapping mapping) {
            Map<String, Object> plainObject = new HashMap<>();
            plainObject.put("model", mapping.getOdooModelType());
            plainObject.put("module", mapping.getOdooModuleName());
            plainObject.put("name", mapping.getExternalId());
            plainObject.put("res_id", mapping.getOdooRecordId());

            return plainObject;
        }
    }
}

