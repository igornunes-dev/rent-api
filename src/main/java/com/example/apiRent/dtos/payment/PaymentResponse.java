package com.example.apiRent.dtos.payment;

import com.example.apiRent.dtos.contract.ContractResponse;
import com.example.apiRent.models.Contract;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        LocalDate due_date,
        LocalDate payment_date,
        BigDecimal amount,
        ContractResponse contract
) {
}
