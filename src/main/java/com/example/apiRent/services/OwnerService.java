package com.example.apiRent.services;

import com.example.apiRent.controllers.OwnerController;
import com.example.apiRent.dtos.owner.OwnerRequest;
import com.example.apiRent.dtos.owner.OwnerResponse;
import com.example.apiRent.enums.EnumUser;
import com.example.apiRent.exceptions.ResourceNotFoundException;
import com.example.apiRent.mappers.OwnerMapper;
import com.example.apiRent.models.Owner;
import com.example.apiRent.repositories.OwnerRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@Transactional
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    public OwnerService(OwnerRepository ownerRepository, OwnerMapper ownerMapper) {
        this.ownerRepository = ownerRepository;
        this.ownerMapper = ownerMapper;
    }

    @Transactional
    public List<OwnerResponse> findAllOwners() {
        List<Owner> owners = ownerRepository.findAll();
        return ownerMapper.toResponseList(owners);
    }

    @Transactional
    public OwnerResponse createOwners(OwnerRequest ownerRequest) {
        Owner owner = ownerMapper.toEntity(ownerRequest);
        owner.setRole(EnumUser.LOCATOR);
        ownerRepository.save(owner);
        return ownerMapper.toResponse(owner);
    }

    @Transactional
    public OwnerResponse updateOwners(UUID id, OwnerRequest ownerRequest) {
        Owner ownerUpdate = ownerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found."));

        ownerUpdate.setName(ownerRequest.name());
        ownerUpdate.setEmail(ownerRequest.email());

        Owner updatedOwner = ownerRepository.save(ownerUpdate);
        return ownerMapper.toResponse(updatedOwner);
    }

    @Transactional
    public void delete(UUID id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id " + id));
        ownerRepository.delete(owner);
    }

    @Transactional
    public OwnerResponse findOwnerById(UUID id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id " + id));
        return ownerMapper.toResponse(owner);
    }

    @Transactional
    public List<OwnerResponse> queryByName(String name) {
        List<Owner> owners = ownerRepository.findByNameContainingIgnoreCase(name);
        return ownerMapper.toResponseList(owners);
    }
}
