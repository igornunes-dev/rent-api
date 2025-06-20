package com.example.apiRent.mappers;

import com.example.apiRent.dtos.owner.OwnerRequest;
import com.example.apiRent.dtos.owner.OwnerResponse;
import com.example.apiRent.models.Owner;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OwnerMapper {
    Owner toEntity(OwnerRequest request);
    OwnerResponse toResponse(Owner owner);
    List<OwnerResponse> toResponseList(List<Owner> owners);
}
