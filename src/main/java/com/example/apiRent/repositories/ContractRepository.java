package com.example.apiRent.repositories;

import com.example.apiRent.models.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID> {
    Optional<Contract> findByTenantId(UUID tenantId);
    Optional<Contract> findByOwnerId(UUID ownerId);
}
