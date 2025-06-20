package com.example.apiRent.repositories;

import com.example.apiRent.enums.EnumUser;
import com.example.apiRent.models.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, UUID> {
    List<Tenant> findByNameContainingIgnoreCase(String name);
    Optional<Tenant> findByIdAndRole(UUID id, EnumUser role);
}
