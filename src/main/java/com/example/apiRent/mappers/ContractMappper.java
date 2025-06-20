package com.example.apiRent.mappers;

import com.example.apiRent.dtos.contract.ContractRequest;
import com.example.apiRent.dtos.contract.ContractResponse;
import com.example.apiRent.models.Contract;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContractMappper {
    Contract toEntity(ContractRequest request);
    ContractResponse toResponse(Contract contract);
    List<ContractResponse> toResponseList(List<Contract> contracts);
}
