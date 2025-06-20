package com.example.apiRent.services;

import com.example.apiRent.dtos.property.PropertyRequest;
import com.example.apiRent.dtos.property.PropertyResponse;
import com.example.apiRent.enums.EnumProperty;
import com.example.apiRent.enums.EnumUser;
import com.example.apiRent.exceptions.ResourceNotFoundException;
import com.example.apiRent.mappers.PropertyMapper;
import com.example.apiRent.models.Owner;
import com.example.apiRent.models.Property;
import com.example.apiRent.repositories.OwnerRepository;
import com.example.apiRent.repositories.PropertyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;
    private final OwnerRepository ownerRepository;

    public PropertyService(PropertyRepository propertyRepository, PropertyMapper propertyMapper, OwnerRepository ownerRepository) {
        this.propertyMapper = propertyMapper;
        this.propertyRepository = propertyRepository;
        this.ownerRepository = ownerRepository;
    }

    @Transactional
    public List<PropertyResponse> findAllProperties() {
        List<Property> properties = propertyRepository.findAll();
        return propertyMapper.toResponseList(properties);
    }

    @Transactional
    public PropertyResponse createProperties(PropertyRequest propertyRequest) {
        UUID ownerId = propertyRequest.ownerId();
        Owner foundOwner = ownerRepository.findByIdAndRole(ownerId, EnumUser.LOCATOR)
                .orElseThrow(() -> new ResourceNotFoundException("LOCATOR with ID " + ownerId + " not found or permission denied."));
        Property property = propertyMapper.toEntity(propertyRequest);
        property.setOwner(foundOwner);
        property.setStatus(EnumProperty.AVAILABLE);
        Property savedProperty = propertyRepository.save(property);
        return propertyMapper.toResponse(savedProperty);
    }

    @Transactional
    public PropertyResponse updateProperties(UUID id, PropertyRequest propertyRequest) {
        UUID ownerId = propertyRequest.ownerId();
        Owner foundOwner = ownerRepository.findByIdAndRole(ownerId, EnumUser.LOCATOR).orElseThrow(() -> new ResourceNotFoundException("LOCATOR with ID " + ownerId + " not found or permission denied."));
        Property property = propertyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Property not found with this id " + id));
        property.setTitle(propertyRequest.title());
        property.setDescription(propertyRequest.description());
        property.setAddress(propertyRequest.address());
        property.setPrice(propertyRequest.price());
        property.setOwner(foundOwner);
        Property updatedProperty = propertyRepository.save(property);

        return propertyMapper.toResponse(updatedProperty);
    }

    @Transactional
    public void deletePropertyById(UUID id) {
        Property property = propertyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Property not found with this id " + id));
        propertyRepository.deleteById(id);
    }

    @Transactional
    public PropertyResponse findPropertyById(UUID id) {
        Property property = propertyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Property not found with this id " + id));
        return propertyMapper.toResponse(property);
    }

    @Transactional
    public List<PropertyResponse> queryPropertyByName(String name) {
        List<Property> properties = propertyRepository.findByTitleContainingIgnoreCase(name);
        return propertyMapper.toResponseList(properties);
    }


}
