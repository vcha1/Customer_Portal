package com.my1stle.customer.portal.persistence.repository;

import com.my1stle.customer.portal.persistence.model.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

    Optional<SubscriptionEntity> findByIdAndOwnerId(Long id, Long ownerId);

    Optional<SubscriptionEntity> findByOwnerIdAndProductIdAndInstallationId(Long ownerId, Long productId, String installationId);

    List<SubscriptionEntity> findByOwnerIdAndInstallationIdAndActiveIsTrue(Long ownerId, String installationId);

}