package com.my1stle.customer.portal.persistence.repository;

import com.my1stle.customer.portal.persistence.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findById(Long productId);

    Optional<ProductEntity> findByIdAndActiveIsTrue(Long productId);

    List<ProductEntity> findAll();

    List<ProductEntity> findAllByActiveIsTrue();

}