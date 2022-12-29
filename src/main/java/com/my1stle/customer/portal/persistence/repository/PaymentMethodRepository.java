package com.my1stle.customer.portal.persistence.repository;

import com.my1stle.customer.portal.persistence.model.PaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity, Long> {

    Optional<PaymentMethodEntity> findByName(String name);

}