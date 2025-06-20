package com.example.apiRent.dtos.owner;

import com.example.apiRent.dtos.property.PropertyResponse;
import com.example.apiRent.enums.EnumUser;
import com.example.apiRent.models.Property;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

public record OwnerResponse(
        UUID id,
        String name,
        EnumUser role,
        String email
){ }
