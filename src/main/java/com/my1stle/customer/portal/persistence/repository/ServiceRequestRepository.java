package com.my1stle.customer.portal.persistence.repository;

import com.my1stle.customer.portal.persistence.model.ServiceRequestEntity;
import org.baeldung.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequestEntity,Long> {

    ServiceRequestEntity findByIdAndUser(Long productId, User user);
}
