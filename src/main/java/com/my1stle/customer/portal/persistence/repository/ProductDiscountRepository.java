package com.my1stle.customer.portal.persistence.repository;

import com.my1stle.customer.portal.persistence.model.ProductDiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDiscountRepository extends JpaRepository<ProductDiscountEntity, Long> {

}