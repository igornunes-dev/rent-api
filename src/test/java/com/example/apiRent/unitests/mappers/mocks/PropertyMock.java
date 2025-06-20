package com.example.apiRent.unitests.mappers.mocks;

import com.example.apiRent.dtos.owner.OwnerRequest;
import com.example.apiRent.dtos.property.PropertyRequest;
import com.example.apiRent.enums.EnumProperty;
import com.example.apiRent.enums.EnumUser;
import com.example.apiRent.models.Owner;
import com.example.apiRent.models.Property;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class PropertyMock {
    public Property mockEntity() {
        return mockEntity(0);
    }

    public PropertyRequest mockRequest() {
        return mockRequest(0);
    }

    public List<Property> mockEntityList() {
        List<Property> properties = new ArrayList<>();
        for(int i = 0; i < 14; i++) {
            properties.add(mockEntity(i));
        }
        return properties;
    }

    public Owner mockOwner(Integer number) {
        Owner owner = new Owner();
        String uuidSeed = "owner-" + number;
        owner.setId(UUID.nameUUIDFromBytes(uuidSeed.getBytes()));
        owner.setName("Owner " + number);
        owner.setEmail("owner" + number + "@test.com");
        owner.setRole(EnumUser.LOCATOR);
        owner.setProperties(new HashSet<>());
        return owner;
    }

    public Property mockEntity(Integer number) {
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

    public PropertyRequest mockRequest(Integer number) {
        UUID ownerId = mockOwner(number).getId();
        return new PropertyRequest(
                "Title " + number,
                "description " + number,
                "address" + number,
                new BigDecimal(number),
                ownerId
        );
    }
}
