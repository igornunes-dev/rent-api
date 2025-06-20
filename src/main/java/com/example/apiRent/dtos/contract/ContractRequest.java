package com.example.apiRent.dtos.contract;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ContractRequest(
        @NotNull
        @FutureOrPresent
        LocalDate start_date,

        @NotNull
        @Future
        LocalDate end_date,

        @NotNull
        @Positive
        BigDecimal monthly_value,

        @NotNull
        UUID tenantId,

        @NotNull
        UUID ownerId,

        @NotNull
        UUID propertyId
) {
}
