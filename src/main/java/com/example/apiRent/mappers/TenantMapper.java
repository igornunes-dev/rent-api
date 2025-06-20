package com.example.apiRent.mappers;

import com.example.apiRent.dtos.tenant.TenantRequest;
import com.example.apiRent.dtos.tenant.TenantResponse;
import com.example.apiRent.models.Tenant;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TenantMapper {
    Tenant toEntity(TenantRequest request);
    TenantResponse toResponse(Tenant tenant);
    List<TenantResponse> toResponseList(List<Tenant> tenants);
}
