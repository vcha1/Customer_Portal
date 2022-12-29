package com.my1stle.customer.portal.service.adobe.sign.info;

import com.adobe.sign.model.agreements.MergefieldInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MergeFieldInfoBuilder {

    Map<String, String> fieldNameValue = new HashMap<>();

    public MergeFieldInfoBuilder() {
    }

    public MergeFieldInfoBuilder(Map<String, String> fieldNameValue) {
        this.fieldNameValue = fieldNameValue;
    }

    public List<MergefieldInfo> build() {

        List<MergefieldInfo> fields = new ArrayList<>();

        for (String fieldName : fieldNameValue.keySet()) {
            MergefieldInfo info = new MergefieldInfo();
            info.setFieldName(fieldName);
            info.setDefaultValue(fieldNameValue.get(fieldName));
            fields.add(info);
        }

        return fields;

    }

    public MergeFieldInfoBuilder addMergeField(String fieldName, String value) {
        this.fieldNameValue.put(fieldName, value);
        return this;
    }

}
