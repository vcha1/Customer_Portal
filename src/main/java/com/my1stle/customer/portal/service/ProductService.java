package com.my1stle.customer.portal.service;

import com.my1stle.customer.portal.service.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAll();

    Product getById(Long id);

}