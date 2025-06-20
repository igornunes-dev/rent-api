package com.example.apiRent.dtos.payment;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record PaymentRequest(
        @NotNull
        @FutureOrPresent
        LocalDate due_date,
        @NotNull
        @PastOrPresent
        LocalDate payment_date,
        @Positive @NotNull
        BigDecimal amount,
        @NotNull
        UUID contractId
) {
}
