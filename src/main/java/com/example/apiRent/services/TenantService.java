package com.example.apiRent.services;

import com.example.apiRent.dtos.tenant.TenantRequest;
import com.example.apiRent.dtos.tenant.TenantResponse;
import com.example.apiRent.enums.EnumUser;
import com.example.apiRent.exceptions.ResourceNotFoundException;
import com.example.apiRent.mappers.TenantMapper;
import com.example.apiRent.models.Tenant;
import com.example.apiRent.repositories.TenantRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TenantService {

    private final TenantMapper tenantMapper;
    private final TenantRepository tenantRepository;

    public TenantService(TenantMapper tenantMapper, TenantRepository tenantRepository) {
        this.tenantMapper = tenantMapper;
        this.tenantRepository = tenantRepository;
    }

    @Transactional
    public List<TenantResponse> findAllTenants() {
        List<Tenant> tenants = tenantRepository.findAll();
        return tenantMapper.toResponseList(tenants);
    }

    @Transactional
    public TenantResponse createTenants(TenantRequest tenantRequest) {
        Tenant tenant = tenantMapper.toEntity(tenantRequest);
        tenant.setRole(EnumUser.TENANT);
        tenantRepository.save(tenant);
        return tenantMapper.toResponse(tenant);
    }

    @Transactional
    public TenantResponse updateTenants(UUID id, TenantRequest tenantRequest) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tenant not found with this id"));
        tenant.setName(tenantRequest.name());
        tenant.setEmail(tenantRequest.email());
        tenantRepository.save(tenant);
        return tenantMapper.toResponse(tenant);
    }

    @Transactional
    public void deleteById(UUID id) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tenant not found with this id"));
        tenantRepository.deleteById(id);
    }

    @Transactional
    public TenantResponse findById(UUID id) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tenant not found with this id"));
        return tenantMapper.toResponse(tenant);
    }

    @Transactional
    public List<TenantResponse> queryName(String name) {
        List<Tenant> tenants = tenantRepository.findByNameContainingIgnoreCase(name);
        return tenantMapper.toResponseList(tenants);
    }



}
