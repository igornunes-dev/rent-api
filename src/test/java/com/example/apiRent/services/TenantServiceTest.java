package com.example.apiRent.services;

import com.example.apiRent.dtos.owner.OwnerRequest;
import com.example.apiRent.dtos.owner.OwnerResponse;
import com.example.apiRent.dtos.tenant.TenantRequest;
import com.example.apiRent.dtos.tenant.TenantResponse;
import com.example.apiRent.enums.EnumUser;
import com.example.apiRent.mappers.OwnerMapper;
import com.example.apiRent.mappers.TenantMapper;
import com.example.apiRent.models.Owner;
import com.example.apiRent.models.Tenant;
import com.example.apiRent.repositories.OwnerRepository;
import com.example.apiRent.repositories.TenantRepository;
import com.example.apiRent.unitests.mappers.mocks.OwnerMock;
import com.example.apiRent.unitests.mappers.mocks.TenantMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TenantServiceTest {

    private TenantService tenantService;

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private TenantMapper tenantMapper;

    private TenantMock input = new TenantMock();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tenantService = new TenantService(tenantMapper, tenantRepository);
    }

    @Test
    void findAllTenants() {
        List<Tenant> mockEntityList = input.mockEntityList();

        List<TenantResponse> mockDtoList = mockEntityList.stream()
                .map(tenant -> new TenantResponse(
                        tenant.getId(), tenant.getName(), tenant.getEmail(),
                        tenant.getRole()))
                .toList();

        when(tenantRepository.findAll()).thenReturn(mockEntityList);
        when(tenantMapper.toResponseList(mockEntityList)).thenReturn(mockDtoList);


        List<TenantResponse> responseEntity = tenantService.findAllTenants();

        assertNotNull(responseEntity);
        assertEquals(14, responseEntity.size());

        TenantResponse tenantOneResponse = responseEntity.get(1);
        assertNotNull(tenantOneResponse);

        assertEquals(mockDtoList.get(1).name(), tenantOneResponse.name());
        assertEquals(mockDtoList.get(1).role(), tenantOneResponse.role());
        assertEquals(mockDtoList.get(1).email(), tenantOneResponse.email());

        var TenantFour = responseEntity.get(4);
        assertNotNull(TenantFour);
        assertNotNull(TenantFour.id());

        assertEquals("Tenant 4", TenantFour.name());
        assertEquals(EnumUser.TENANT, TenantFour.role());
        assertEquals("tenant@gmail.com 4", TenantFour.email());

        var TenantSeven = responseEntity.get(7);
        assertNotNull(TenantSeven);
        assertNotNull(TenantSeven.id());

        assertEquals("Tenant 7", TenantSeven.name());
        assertEquals(EnumUser.TENANT, TenantSeven.role());
        assertEquals("tenant@gmail.com 7", TenantSeven.email());
    }

    @Test
    void createTenants() {
        Tenant tenant = input.mockEntity(1);
        TenantRequest tenantRequest = input.mockRequest(1);
        TenantResponse tenantResponse = new TenantResponse(
                tenant.getId(),
                tenant.getName(),
                tenant.getEmail(),
                tenant.getRole()
        );

        when(tenantMapper.toEntity(tenantRequest)).thenReturn(tenant);
        when(tenantRepository.save(tenant)).thenReturn(tenant);
        when(tenantMapper.toResponse(tenant)).thenReturn(tenantResponse);
        UUID expectedId = tenant.getId();

        TenantResponse responseEntity = tenantService.createTenants(tenantRequest);

        assertNotNull(responseEntity);

        assertNotNull(responseEntity.id());

        assertEquals("Tenant 1", responseEntity.name());
        assertEquals(EnumUser.TENANT, responseEntity.role());
        assertEquals("tenant@gmail.com 1", responseEntity.email());
    }


    @Test
    void updateTenants() {
        Tenant tenant = input.mockEntity(1);
        TenantRequest tenantRequest = input.mockRequest(1);
        UUID expectedId = tenant.getId();
        Tenant updatedTenant = new Tenant();
        updatedTenant.setId(expectedId);
        updatedTenant.setName(tenantRequest.name());
        updatedTenant.setEmail(tenantRequest.email());
        updatedTenant.setRole(tenant.getRole());
        TenantResponse tenantResponse = new TenantResponse(
                tenant.getId(),
                tenant.getName(),
                tenant.getEmail(),
                tenant.getRole()
        );

        when(tenantRepository.findById(expectedId)).thenReturn(Optional.of(tenant));
        when(tenantRepository.save(any(Tenant.class))).thenReturn(updatedTenant);
        when(tenantMapper.toResponse(any(Tenant.class))).thenReturn(tenantResponse);


        TenantResponse responseEntity = tenantService.updateTenants(expectedId, tenantRequest);

        assertNotNull(responseEntity);
        assertNotNull(responseEntity.id());

        assertEquals("Tenant 1", responseEntity.name());
        assertEquals(EnumUser.TENANT, responseEntity.role());
        assertEquals("tenant@gmail.com 1", responseEntity.email());
    }

    @Test
    void deleteById() {
        Tenant tenant = input.mockEntity(1);
        UUID predictableId = tenant.getId();

        when(tenantRepository.findById(predictableId)).thenReturn(Optional.of(tenant));

        tenantService.deleteById(predictableId);

        verify(tenantRepository, times(1)).findById(predictableId);
        verify(tenantRepository, times(1)).deleteById(predictableId);
    }

    @Test
    void findById() {
        Tenant tenant = input.mockEntity(1);
        UUID expectedId = tenant.getId();

        TenantResponse tenantResponse = new TenantResponse(
                tenant.getId(),
                tenant.getName(),
                tenant.getEmail(),
                tenant.getRole()
        );

        when(tenantRepository.findById(expectedId)).thenReturn(Optional.of(tenant));
        when(tenantMapper.toResponse(tenant)).thenReturn(tenantResponse);

        TenantResponse responseEntity = tenantService.findById(expectedId);

        assertNotNull(responseEntity);
        assertNotNull(responseEntity.id());

        assertEquals("Tenant 1", responseEntity.name());
        assertEquals(EnumUser.TENANT, responseEntity.role());
        assertEquals("tenant@gmail.com 1", responseEntity.email());
    }

    @Test
    void queryName() {
        String searchName = "Tenant";
        List<Tenant> mockEntityList = input.mockEntityList();
        List<TenantResponse> mockDtoList = mockEntityList.stream()
                .map(tenant -> new TenantResponse(tenant.getId(), tenant.getName(), tenant.getEmail(), tenant.getRole()))
                .toList();

        when(tenantRepository.findByNameContainingIgnoreCase(searchName)).thenReturn(mockEntityList);
        when(tenantMapper.toResponseList(mockEntityList)).thenReturn(mockDtoList);

        List<TenantResponse> responseEntity = tenantService.queryName(searchName);

        assertNotNull(responseEntity);

        TenantResponse resultData = responseEntity.get(1);

        assertNotNull(resultData);
        assertEquals("Tenant 1", resultData.name());
        assertEquals(EnumUser.TENANT, resultData.role());
        assertEquals("tenant@gmail.com 1", resultData.email());
    }
}
