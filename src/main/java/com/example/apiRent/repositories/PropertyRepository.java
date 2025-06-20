package com.example.apiRent.repositories;

import com.example.apiRent.enums.EnumProperty;
import com.example.apiRent.enums.EnumUser;
import com.example.apiRent.models.Owner;
import com.example.apiRent.models.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PropertyRepository extends JpaRepository<Property, UUID> {
    List<Property> findByTitleContainingIgnoreCase(String title);
    Optional<Property> findByIdAndStatus(UUID id, EnumProperty role);
}
