package com.example.apiRent.unitests.mappers.mocks;

import com.example.apiRent.dtos.owner.OwnerRequest;
import com.example.apiRent.enums.EnumUser;
import com.example.apiRent.models.Owner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OwnerMock {

    public Owner mockEntity() {
        return mockEntity(0);
    }

    public OwnerRequest mockRequest() {
        return mockRequest(0);
    }

    public List<Owner> mockEntityList() {
        List<Owner> owners = new ArrayList<>();
        for(int i = 0; i < 14; i++) {
            owners.add(mockEntity(i));
        }
        return owners;
    }

    public Owner mockEntity(Integer number) {
        Owner owner = new Owner();

        owner.setRole(EnumUser.LOCATOR);
        owner.setName("Owner " + number);
        owner.setEmail("owner@gmail.com "+number);
        String uuidSeed = "owner-" + number;
        owner.setId(UUID.nameUUIDFromBytes(uuidSeed.getBytes()));
        return owner;
    }

    public OwnerRequest mockRequest(Integer number) {
        return new OwnerRequest(
                "Owner " + number,
                "owner" + number + "@gmail.com"
        );
    }
}
