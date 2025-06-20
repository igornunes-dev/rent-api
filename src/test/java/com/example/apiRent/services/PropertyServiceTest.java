package com.example.apiRent.services;

import com.example.apiRent.dtos.owner.OwnerResponse;
import com.example.apiRent.dtos.property.PropertyRequest;
import com.example.apiRent.dtos.property.PropertyResponse;
import com.example.apiRent.dtos.tenant.TenantRequest;
import com.example.apiRent.dtos.tenant.TenantResponse;
import com.example.apiRent.enums.EnumProperty;
import com.example.apiRent.enums.EnumUser;
import com.example.apiRent.mappers.PropertyMapper;
import com.example.apiRent.mappers.TenantMapper;
import com.example.apiRent.models.Owner;
import com.example.apiRent.models.Property;
import com.example.apiRent.models.Tenant;
import com.example.apiRent.repositories.OwnerRepository;
import com.example.apiRent.repositories.PropertyRepository;
import com.example.apiRent.repositories.TenantRepository;
import com.example.apiRent.unitests.mappers.mocks.PropertyMock;
import com.example.apiRent.unitests.mappers.mocks.TenantMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PropertyServiceTest {

    private PropertyService propertyService;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private PropertyMapper propertyMapper;

    private PropertyMock input = new PropertyMock();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        propertyService = new PropertyService(propertyRepository, propertyMapper, ownerRepository);
    }

    @Test
    void findPropertiesAll() {
        List<Property> mockEntityList = input.mockEntityList();

        List<PropertyResponse> mockDtoList = mockEntityList.stream()
                .map(property -> new PropertyResponse(
                        property.getId(),
                        property.getTitle(),
                        property.getDescription(),
                        property.getAddress(),
                        property.getPrice(),
                        property.getStatus()
                ))
                .toList();



        when(propertyRepository.findAll()).thenReturn(mockEntityList);
        when(propertyMapper.toResponseList(mockEntityList)).thenReturn(mockDtoList);


        List<PropertyResponse> responseEntity = propertyService.findAllProperties();

        assertNotNull(responseEntity);
        assertEquals(14, responseEntity.size());

        PropertyResponse propertyResponse = responseEntity.get(1);
        assertNotNull(propertyResponse);

        assertEquals(mockDtoList.get(1).title(), propertyResponse.title());
        assertEquals(mockDtoList.get(1).description(), propertyResponse.description());
        assertEquals(mockDtoList.get(1).address(), propertyResponse.address());
        assertEquals(mockDtoList.get(1).status(), propertyResponse.status());
        assertEquals(mockDtoList.get(1).price(), propertyResponse.price());

        var PropertyFour = responseEntity.get(4);
        Owner OwnerFour = input.mockOwner(4);
        assertNotNull(PropertyFour);
        assertNotNull(PropertyFour.id());

        assertEquals("title 4", PropertyFour.title());
        assertEquals(EnumProperty.AVAILABLE, PropertyFour.status());
        assertEquals("address 4", PropertyFour.address());
        assertEquals("description 4", PropertyFour.description());
        assertEquals(new BigDecimal(4), PropertyFour.price());

        var PropertySeven = responseEntity.get(7);
        Owner OwnerSeven = input.mockOwner(7);
        assertNotNull(PropertySeven);
        assertNotNull(PropertySeven.id());

        assertEquals("title 7", PropertySeven.title());
        assertEquals(EnumProperty.AVAILABLE, PropertySeven.status());
        assertEquals("address 7", PropertySeven.address());
        assertEquals("description 7", PropertySeven.description());
        assertEquals(new BigDecimal(7), PropertySeven.price());
    }

    @Test
    void createProperties() {
        Property property = input.mockEntity(1);
        PropertyRequest propertyRequest = input.mockRequest(1);
        Owner owner = input.mockOwner(1);
        PropertyResponse propertyResponse = new PropertyResponse(
                property.getId(),
                property.getTitle(),
                property.getDescription(),
                property.getAddress(),
                property.getPrice(),
                property.getStatus()
        );

        when(ownerRepository.findByIdAndRole(owner.getId(), EnumUser.LOCATOR))
                .thenReturn(Optional.of(owner));
        when(propertyMapper.toEntity(propertyRequest)).thenReturn(property);
        when(propertyRepository.save(property)).thenReturn(property);
        when(propertyMapper.toResponse(property)).thenReturn(propertyResponse);

        PropertyResponse responseEntity = propertyService.createProperties(propertyRequest);


        assertNotNull(responseEntity);

        assertNotNull(responseEntity.id());

        assertEquals("title 1", responseEntity.title());
        assertEquals(EnumProperty.AVAILABLE, responseEntity.status());
        assertEquals("address 1", responseEntity.address());
        assertEquals("description 1", responseEntity.description());
        assertEquals(new BigDecimal(1), responseEntity.price());
    }

    @Test
    void updateProperties() {
        Property property = input.mockEntity(1);
        UUID propertyId = property.getId();
        PropertyRequest propertyRequest = input.mockRequest(1);
        Property updatedProperty = new Property();
        Owner owner = input.mockOwner(1);
        UUID ownerId = owner.getId();
        updatedProperty.setTitle(propertyRequest.title());
        updatedProperty.setDescription(propertyRequest.description());
        updatedProperty.setAddress(propertyRequest.address());
        updatedProperty.setPrice(propertyRequest.price());
        updatedProperty.setOwner(owner);
        PropertyResponse propertyResponse = new PropertyResponse(
                property.getId(),
                property.getTitle(),
                property.getDescription(),
                property.getAddress(),
                property.getPrice(),
                property.getStatus()
        );


        when(ownerRepository.findByIdAndRole(ownerId, EnumUser.LOCATOR)).thenReturn(Optional.of(owner));
        when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(property));
        when(propertyRepository.save(any(Property.class))).thenReturn(property);
        when(propertyMapper.toResponse(property)).thenReturn(propertyResponse);

        PropertyResponse responseEntity = propertyService.updateProperties(propertyId, propertyRequest);

        assertNotNull(responseEntity);

        assertNotNull(responseEntity.id());

        assertEquals("title 1", responseEntity.title());
        assertEquals(EnumProperty.AVAILABLE, responseEntity.status());
        assertEquals("address 1", responseEntity.address());
        assertEquals("description 1", responseEntity.description());
        assertEquals(new BigDecimal(1), responseEntity.price());
    }

    @Test
    void deletePropertiesById() {
        Property property = input.mockEntity(1);
        UUID propertyId = property.getId();

        when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(property));

        propertyService.deletePropertyById(propertyId);

        verify(propertyRepository, times(1)).findById(propertyId);
        verify(propertyRepository, times(1)).deleteById(propertyId);
    }

    @Test
    void findPropertyById() {
        Property property = input.mockEntity(1);
        Owner owner = input.mockOwner(1);
        UUID propertyId = property.getId();
        PropertyResponse propertyResponse = new PropertyResponse(
                property.getId(),
                property.getTitle(),
                property.getDescription(),
                property.getAddress(),
                property.getPrice(),
                property.getStatus()
        );

        when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(property));
        when(propertyMapper.toResponse(property)).thenReturn(propertyResponse);

        PropertyResponse responseEntity = propertyService.findPropertyById(propertyId);

        assertNotNull(responseEntity);

        assertNotNull(responseEntity.id());

        assertEquals("title 1", responseEntity.title());
        assertEquals(EnumProperty.AVAILABLE, responseEntity.status());
        assertEquals("address 1", responseEntity.address());
        assertEquals("description 1", responseEntity.description());
        assertEquals(new BigDecimal(1), responseEntity.price());
    }

    @Test
    void queryPropertiesByName() {
        String queryName = "Property";
        List<Property> properties = input.mockEntityList();
        Owner owner = input.mockOwner(1);
        List<PropertyResponse> propertyResponses = properties.stream()
                .map(
                        property -> new PropertyResponse(
                                property.getId(),
                                property.getTitle(),
                                property.getDescription(),
                                property.getAddress(),
                                property.getPrice(),
                                property.getStatus()
                        )
                )
                .toList();

        when(propertyRepository.findByTitleContainingIgnoreCase(queryName)).thenReturn(properties);
        when(propertyMapper.toResponseList(properties)).thenReturn(propertyResponses);

        List<PropertyResponse> responseEntity = propertyService.queryPropertyByName(queryName);

        assertNotNull(responseEntity);

        PropertyResponse resultData = responseEntity.get(1);

        assertNotNull(resultData);
        assertEquals("title 1", resultData.title());
        assertEquals(EnumProperty.AVAILABLE, resultData.status());
        assertEquals("address 1", resultData.address());
        assertEquals("description 1", resultData.description());
        assertEquals(new BigDecimal(1), resultData.price());
    }


}
