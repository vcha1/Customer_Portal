package com.my1stle.customer.portal.persistence.repository;

import com.my1stle.customer.portal.persistence.model.PaymentDetailEntity;
import org.baeldung.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentDetailRepository extends JpaRepository<PaymentDetailEntity, Long> {

    Optional<PaymentDetailEntity> findByIdAndOwner(Long id, User user);

    List<PaymentDetailEntity> findAll();

}
