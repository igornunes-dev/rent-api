package com.example.apiRent.dtos.tenant;

import jakarta.validation.constraints.NotBlank;

public record TenantRequest(
        @NotBlank String name,
        @NotBlank String email
) {
}
