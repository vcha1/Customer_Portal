package com.my1stle.customer.portal.web.converter;

import com.dev1stle.repository.salesforce.SalesforceObjectRepository;
import com.dev1stle.repository.salesforce.model.SalesforceObject;
import org.springframework.core.convert.converter.Converter;

@Deprecated
public abstract class SalesforceObjectConverter<T extends SalesforceObject> implements Converter<String, T> {

    private SalesforceObjectRepository<T> repository;

    public SalesforceObjectConverter(SalesforceObjectRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    public T convert(String source) {
        return repository.findById(source).orElse(null);
    }

}
