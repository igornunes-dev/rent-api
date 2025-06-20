package com.example.apiRent.mappers;

import com.example.apiRent.dtos.property.PropertyRequest;
import com.example.apiRent.dtos.property.PropertyResponse;
import com.example.apiRent.models.Payment;
import com.example.apiRent.models.Property;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PropertyMapper {
    Property toEntity(PropertyRequest request);
    PropertyResponse toResponse(Property property);
    List<PropertyResponse> toResponseList(List<Property> properties);
}
