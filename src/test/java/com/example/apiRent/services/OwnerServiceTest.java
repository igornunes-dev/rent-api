package com.example.apiRent.services;

import com.example.apiRent.dtos.owner.OwnerRequest;
import com.example.apiRent.dtos.owner.OwnerResponse;
import com.example.apiRent.enums.EnumUser;
import com.example.apiRent.mappers.OwnerMapper;
import com.example.apiRent.models.Owner;
import com.example.apiRent.repositories.OwnerRepository;
import com.example.apiRent.unitests.mappers.mocks.OwnerMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class OwnerServiceTest {

    private OwnerService ownerService;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private OwnerMapper ownerMapper;

    private OwnerMock input = new OwnerMock();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ownerService = new OwnerService(ownerRepository, ownerMapper);
    }

    @Test
    void findAllOwners() {
        List<Owner> mockOwnerList = input.mockEntityList();
        Owner owners = input.mockEntity(0);

        List<OwnerResponse> mockDtoList = mockOwnerList.stream()
                .map(owner -> new OwnerResponse(
                        owner.getId(), owner.getName(), owner.getRole(),
                        owner.getEmail()))
                .toList();

        when(ownerRepository.findAll()).thenReturn(mockOwnerList);
        when(ownerMapper.toResponseList(mockOwnerList)).thenReturn(mockDtoList);


        List<OwnerResponse> responseEntity = ownerService.findAllOwners();

        assertNotNull(responseEntity);
        assertEquals(14, responseEntity.size());

        OwnerResponse ownerOneResource = responseEntity.get(1);
        assertNotNull(ownerOneResource);

        assertEquals(mockDtoList.get(1).name(), ownerOneResource.name());
        assertEquals(mockDtoList.get(1).role(), ownerOneResource.role());
        assertEquals(mockDtoList.get(1).email(), ownerOneResource.email());

        var OwnerFour = responseEntity.get(4);
        assertNotNull(OwnerFour);
        assertNotNull(OwnerFour.id());

        assertEquals("Owner 4", OwnerFour.name());
        assertEquals(EnumUser.LOCATOR, OwnerFour.role());
        assertEquals("owner@gmail.com 4", OwnerFour.email());

        var OwnerSeven = responseEntity.get(7);
        assertNotNull(OwnerSeven);
        assertNotNull(OwnerSeven.id());

        assertEquals("Owner 7", OwnerSeven.name());
        assertEquals(EnumUser.LOCATOR, OwnerSeven.role());
        assertEquals("owner@gmail.com 7", OwnerSeven.email());
    }

    @Test
    void createOwners() {
        Owner owner = input.mockEntity(1);
        OwnerRequest ownerRequest = input.mockRequest(1);
        OwnerResponse ownerResponseDTO = new OwnerResponse(
                owner.getId(),
                owner.getName(),
                owner.getRole(),
                owner.getEmail()
        );

        when(ownerMapper.toEntity(ownerRequest)).thenReturn(owner);
        when(ownerRepository.save(owner)).thenReturn(owner);
        when(ownerMapper.toResponse(owner)).thenReturn(ownerResponseDTO);
        UUID expectedId = owner.getId();

        OwnerResponse responseEntity = ownerService.createOwners(ownerRequest);

        assertNotNull(responseEntity);

        assertNotNull(responseEntity.id());

        assertEquals("Owner 1", responseEntity.name());
        assertEquals(EnumUser.LOCATOR, responseEntity.role());
        assertEquals("owner@gmail.com 1", responseEntity.email());
    }

    @Test
    void updateOwners() {
        Owner owner = input.mockEntity(1);
        OwnerRequest ownerRequest = input.mockRequest(1);
        UUID expectedId = owner.getId();
        Owner updatedOwner = new Owner();
        updatedOwner.setId(expectedId);
        updatedOwner.setName(ownerRequest.name());
        updatedOwner.setEmail(ownerRequest.email());
        updatedOwner.setRole(owner.getRole());
        OwnerResponse ownerResponseDTO = new OwnerResponse(
                owner.getId(),
                owner.getName(),
                owner.getRole(),
                owner.getEmail()
        );

        when(ownerRepository.findById(expectedId)).thenReturn(Optional.of(owner));
        when(ownerRepository.save(any(Owner.class))).thenReturn(updatedOwner);
        when(ownerMapper.toResponse(updatedOwner)).thenReturn(ownerResponseDTO);


        OwnerResponse responseEntity = ownerService.updateOwners(expectedId, ownerRequest);

        assertNotNull(responseEntity);
        assertNotNull(responseEntity.id());

        assertEquals("Owner 1", responseEntity.name());
        assertEquals(EnumUser.LOCATOR, responseEntity.role());
        assertEquals("owner@gmail.com 1", responseEntity.email());
    }

    @Test
    void delete() {
        Owner owner = input.mockEntity(1);
        UUID predictableId = owner.getId();

        when(ownerRepository.findById(predictableId)).thenReturn(Optional.of(owner));

        ownerService.delete(predictableId);

        verify(ownerRepository, times(1)).findById(predictableId);
        verify(ownerRepository, times(1)).delete(owner);
    }

    @Test
    void findOwnerById() {
        Owner owner = input.mockEntity(1);
        UUID expectedId = owner.getId();

        OwnerResponse ownerResponseDTO = new OwnerResponse(
                owner.getId(),
                owner.getName(),
                owner.getRole(),
                owner.getEmail()
        );

        when(ownerRepository.findById(expectedId)).thenReturn(Optional.of(owner));
        when(ownerMapper.toResponse(owner)).thenReturn(ownerResponseDTO);

        OwnerResponse responseEntity = ownerService.findOwnerById(expectedId);

        assertNotNull(responseEntity);
        assertNotNull(responseEntity.id());

        assertEquals("Owner 1", responseEntity.name());
        assertEquals(EnumUser.LOCATOR, responseEntity.role());
        assertEquals("owner@gmail.com 1", responseEntity.email());
    }

    @Test
    void queryByName() {
        String searchName = "Owner";
        List<Owner> mockOwnerList = input.mockEntityList();
        List<OwnerResponse> mockDtoList = mockOwnerList.stream()
                .map(owner -> new OwnerResponse(owner.getId(), owner.getName(), owner.getRole(), owner.getEmail()))
                .toList();

        when(ownerRepository.findByNameContainingIgnoreCase(searchName)).thenReturn(mockOwnerList);
        when(ownerMapper.toResponseList(mockOwnerList)).thenReturn(mockDtoList);

        List<OwnerResponse> responseEntity = ownerService.queryByName(searchName);

        assertNotNull(responseEntity);

        OwnerResponse resultData = responseEntity.get(1);

        assertNotNull(resultData);
        assertEquals("Owner 1", resultData.name());
        assertEquals(EnumUser.LOCATOR, resultData.role());
        assertEquals("owner@gmail.com 1", resultData.email());
    }

}
