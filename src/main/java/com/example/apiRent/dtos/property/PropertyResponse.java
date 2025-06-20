package com.example.apiRent.dtos.property;

import com.example.apiRent.enums.EnumProperty;
import com.example.apiRent.models.Owner;

import java.math.BigDecimal;
import java.util.UUID;

public record PropertyResponse(
        UUID id,
        String title,
        String description,
        String address,
        BigDecimal price,
        EnumProperty status
) { }
