package com.my1stle.customer.portal.serviceImpl;

import com.my1stle.customer.portal.persistence.repository.ProductRepository;
import com.my1stle.customer.portal.service.ProductService;
import com.my1stle.customer.portal.service.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultProductService implements ProductService {

    private ProductRepository productRepository;

    public DefaultProductService(@Autowired ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAll() {
        return new ArrayList<>(this.productRepository.findAllByActiveIsTrue());
    }

    @Override
    public Product getById(Long id) {
        return productRepository.getOne(id);
    }

}
