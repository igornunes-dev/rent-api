package com.example.apiRent.dtos.tenant;

import com.example.apiRent.dtos.property.PropertyResponse;
import com.example.apiRent.enums.EnumUser;
import com.example.apiRent.models.Contract;

import java.util.Set;
import java.util.UUID;

public record TenantResponse(
        UUID id,
        String name,
        String email,
        EnumUser role
) { }
