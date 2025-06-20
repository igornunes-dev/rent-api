package com.example.apiRent.dtos.contract;

import com.example.apiRent.dtos.owner.OwnerResponse;
import com.example.apiRent.dtos.property.PropertyResponse;
import com.example.apiRent.dtos.tenant.TenantResponse;
import com.example.apiRent.enums.EnumContract;
import com.example.apiRent.models.Owner;
import com.example.apiRent.models.Property;
import com.example.apiRent.models.Tenant;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ContractResponse(
        UUID id,
        LocalDate start_date,
        LocalDate end_date,
        BigDecimal monthly_value,
        EnumContract status,
        PropertyResponse property,
        TenantResponse tenant,
        OwnerResponse owner
) { }
