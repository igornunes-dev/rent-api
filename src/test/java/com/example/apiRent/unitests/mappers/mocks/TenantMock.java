package com.example.apiRent.unitests.mappers.mocks;

import com.example.apiRent.dtos.owner.OwnerRequest;
import com.example.apiRent.dtos.tenant.TenantRequest;
import com.example.apiRent.enums.EnumUser;
import com.example.apiRent.models.Owner;
import com.example.apiRent.models.Tenant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TenantMock {

    public Tenant mockEntity() {
        return mockEntity(0);
    }

    public TenantRequest mockRequest() {
        return mockRequest(0);
    }

    public List<Tenant> mockEntityList() {
        List<Tenant> tenant = new ArrayList<>();
        for(int i = 0; i < 14; i++) {
            tenant.add(mockEntity(i));
        }
        return tenant;
    }

    public Tenant mockEntity(Integer number) {
        Tenant tenant = new Tenant();

        tenant.setRole(EnumUser.TENANT);
        tenant.setName("Tenant " + number);
        tenant.setEmail("tenant@gmail.com "+number);
        String uuidSeed = "tenant-" + number;
        tenant.setId(UUID.nameUUIDFromBytes(uuidSeed.getBytes()));
        return tenant;
    }

    public TenantRequest mockRequest(Integer number) {
        return new TenantRequest(
                "Tenant " + number,
                "tenant@gmail.com " + number
        );
    }
}
