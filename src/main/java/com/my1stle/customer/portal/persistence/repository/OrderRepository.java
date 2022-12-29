package com.my1stle.customer.portal.persistence.repository;

import com.my1stle.customer.portal.persistence.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

}
