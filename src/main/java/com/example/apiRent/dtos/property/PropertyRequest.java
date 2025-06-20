package com.example.apiRent.dtos.property;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record PropertyRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String address,
        @Positive BigDecimal price,
        @NotNull UUID ownerId
) {
}
