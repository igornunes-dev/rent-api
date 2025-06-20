package com.example.apiRent.repositories;

import com.example.apiRent.enums.EnumUser;
import com.example.apiRent.models.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, UUID> {
    List<Owner> findByNameContainingIgnoreCase(String name);
    Optional<Owner> findByIdAndRole(UUID id, EnumUser role);

}
