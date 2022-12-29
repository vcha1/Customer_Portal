package com.my1stle.customer.portal.web.converter;

import com.my1stle.customer.portal.persistence.repository.ProductRepository;
import com.my1stle.customer.portal.persistence.model.ProductEntity;
import com.my1stle.customer.portal.web.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter implements Converter<String, ProductEntity> {

    private ProductRepository productRepository;

    @Autowired
    public ProductConverter(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductEntity convert(String s) {

        try {
            return this.productRepository.findById(Long.parseLong(s)).orElse(null);
        } catch (NumberFormatException e) {
            throw new BadRequestException(e.getMessage());
        }

    }
}