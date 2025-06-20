package com.example.apiRent.unitests.mappers.mocks;

import com.example.apiRent.dtos.contract.ContractRequest;
import com.example.apiRent.dtos.contract.ContractResponse;
import com.example.apiRent.dtos.owner.OwnerResponse;
import com.example.apiRent.dtos.property.PropertyRequest;
import com.example.apiRent.dtos.property.PropertyResponse;
import com.example.apiRent.dtos.tenant.TenantResponse;
import com.example.apiRent.enums.EnumContract;
import com.example.apiRent.enums.EnumProperty;
import com.example.apiRent.enums.EnumUser;
import com.example.apiRent.models.Contract;
import com.example.apiRent.models.Owner;
import com.example.apiRent.models.Property;
import com.example.apiRent.models.Tenant;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class ContractMock {
    public Contract mockEntity() {
        return mockEntity(0);
    }

    public ContractRequest mockRequest() {
        return mockRequest(0);
    }

    public List<Contract> mockEntityList() {
        List<Contract> contracts = new ArrayList<>();
        for(int i = 0; i < 14; i++) {
            contracts.add(mockEntity(i));
        }
        return contracts;
    }

    public Owner mockOwner(Integer number) {
        Owner owner = new Owner();

        owner.setRole(EnumUser.LOCATOR);
        owner.setName("Owner " + number);
        owner.setEmail("owner@gmail.com "+number);
        String uuidSeed = "owner-" + number;
        owner.setId(UUID.nameUUIDFromBytes(uuidSeed.getBytes()));
        return owner;
    }

    public Tenant mockTenant(Integer number) {
        Tenant tenant = new Tenant();

        tenant.setRole(EnumUser.TENANT);
        tenant.setName("Tenant " + number);
        tenant.setEmail("tenant@gmail.com "+number);
        String uuidSeed = "tenant-" + number;
        tenant.setId(UUID.nameUUIDFromBytes(uuidSeed.getBytes()));
        return tenant;
    }

    public Property mockProperty(Integer number) {
        Property property = new Property();

        property.setStatus(EnumProperty.AVAILABLE);
        property.setTitle("title " + number);
        property.setDescription("description "+number);
        property.setAddress("address " + number);
        String uuidSeed = "property-" + number;
        property.setId(UUID.nameUUIDFromBytes(uuidSeed.getBytes()));
        property.setPrice(new BigDecimal(number));
        Owner mockOwner = mockOwner(number);
        property.setOwner(mockOwner);
        return property;
    }

    public OwnerResponse mockOwnerResponse(Integer number) {
        String uuidSeed = "owner-" + number;
        UUID id = UUID.nameUUIDFromBytes(uuidSeed.getBytes());
        return new OwnerResponse(
                id,
                "Owner " + number,
                EnumUser.LOCATOR,
                "owner@gmail.com" + number

        );
    }

    public TenantResponse mockTenantResponse(Integer number) {
        String uuidSeed = "tenant-" + number;
        UUID id = UUID.nameUUIDFromBytes(uuidSeed.getBytes());
        return new TenantResponse(
                id,
                "Tenant " + number,
                "tenant@gmail.com" + number,
                EnumUser.TENANT
        );
    }

    public PropertyResponse mockPropertyResponse(Integer number) {
        String uuidSeed = "property-" + number;
        UUID id = UUID.nameUUIDFromBytes(uuidSeed.getBytes());
        return new PropertyResponse(
                id,
                "title " + number,
                "description " + number,
                "address " + number,
                new BigDecimal(number),
                EnumProperty.RENTED
        );
    }


    public Contract mockEntity(Integer number) {
        Contract contract = new Contract();
        String uuidSeed = "contract-" + number;
        contract.setId(UUID.nameUUIDFromBytes(uuidSeed.getBytes()));
        contract.setMonthly_value(new BigDecimal(number));
        contract.setStart_date(LocalDate.of(2025,1,1));
        contract.setEnd_date(null);
        contract.setStatus(EnumContract.ACTIVE);
        Owner owner = mockOwner(1);
        Tenant tenant = mockTenant(1);
        Property property = mockProperty(1);
        contract.setOwner(owner);
        contract.setTenant(tenant);
        contract.setProperty(property);
        return contract;
    }

    public ContractRequest mockRequest(Integer number) {
        UUID ownerId = mockOwner(number).getId();
        UUID tenantId = mockTenant(number).getId();
        UUID propertyId = mockProperty(1).getId();
        return new ContractRequest(
                LocalDate.of(2025,1,1),
                LocalDate.of(2025,2,2),
                new BigDecimal(number),
                tenantId,
                ownerId,
                propertyId
        );
    }

    public ContractResponse mockResponse(Integer number) {
        OwnerResponse owner = mockOwnerResponse(number);
        TenantResponse tenant = mockTenantResponse(number);
        PropertyResponse property = mockPropertyResponse(1);
        UUID id = mockEntity(1).getId();
        return new ContractResponse(
                id,
                LocalDate.of(2025,1,1),
                null,
                new BigDecimal(number),
                EnumContract.ACTIVE,
                property,
                tenant,
                owner
        );
    }
}
