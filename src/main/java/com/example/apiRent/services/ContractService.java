package com.example.apiRent.services;

import com.example.apiRent.dtos.contract.ContractRequest;
import com.example.apiRent.dtos.contract.ContractResponse;
import com.example.apiRent.enums.EnumContract;
import com.example.apiRent.enums.EnumProperty;
import com.example.apiRent.enums.EnumUser;
import com.example.apiRent.exceptions.IllegalStateException;
import com.example.apiRent.exceptions.ResourceNotFoundException;
import com.example.apiRent.mappers.ContractMappper;
import com.example.apiRent.models.Contract;
import com.example.apiRent.models.Owner;
import com.example.apiRent.models.Property;
import com.example.apiRent.models.Tenant;
import com.example.apiRent.repositories.ContractRepository;
import com.example.apiRent.repositories.OwnerRepository;
import com.example.apiRent.repositories.PropertyRepository;
import com.example.apiRent.repositories.TenantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final ContractMappper contractMappper;
    private final OwnerRepository ownerRepository;
    private final TenantRepository tenantRepository;
    private final PropertyRepository propertyRepository;
    private final PaymentService paymentService;

    public ContractService(ContractRepository contractRepository, ContractMappper contractMappper, OwnerRepository ownerRepository, TenantRepository tenantRepository, PropertyRepository propertyRepository, PaymentService paymentService) {
        this.contractRepository = contractRepository;
        this.contractMappper = contractMappper;
        this.ownerRepository = ownerRepository;
        this.tenantRepository = tenantRepository;
        this.propertyRepository = propertyRepository;
        this.paymentService = paymentService;
    }

    private Owner findAndValidateOwner(UUID ownerId) {
        return ownerRepository.findByIdAndRole(ownerId, EnumUser.LOCATOR).orElseThrow(() -> new ResourceNotFoundException("Owner not found with this id " + ownerId));
    }

    private Tenant findAndValidateTenant(UUID tenantId) {
        return tenantRepository.findByIdAndRole(tenantId, EnumUser.TENANT).orElseThrow(() -> new ResourceNotFoundException("Owner not found with this id " + tenantId));
    }

    private Property findAndValidateProperty(UUID propertyId) {
        return propertyRepository.findByIdAndStatus(propertyId, EnumProperty.AVAILABLE).orElseThrow(() -> new ResourceNotFoundException("Property not found this id " + propertyId));
    }

    @Transactional
    public List<ContractResponse> findAllContracts() {
        List<Contract> contract = contractRepository.findAll();
        return contractMappper.toResponseList(contract);
    }

    @Transactional
    public ContractResponse createContracts(ContractRequest contractRequest) {
        Contract contract = contractMappper.toEntity(contractRequest);
        Owner owner = findAndValidateOwner(contractRequest.ownerId());
        Tenant tenant = findAndValidateTenant(contractRequest.tenantId());
        Property property = findAndValidateProperty(contractRequest.propertyId());
        property.setStatus(EnumProperty.RENTED);
        contract.setStatus(EnumContract.ACTIVE);
        contract.setEnd_date(contractRequest.end_date());
        contract.setOwner(owner);
        contract.setTenant(tenant);
        contract.setProperty(property);
        contractRepository.save(contract);
        paymentService.generatePaymentsForContracts(contract);
        return contractMappper.toResponse(contract);
    }

    @Transactional
    public ContractResponse findContractById(UUID id) {
        Contract contract = contractRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contract not found with this id " + id));
        return contractMappper.toResponse(contract);
    }

    @Transactional
    public ContractResponse findContractByTenant(UUID id) {
        Contract contract = contractRepository.findByTenantId(id).orElseThrow(() -> new ResourceNotFoundException("Tenant not found with this id " + id));
        return contractMappper.toResponse(contract);
    }

    @Transactional
    public ContractResponse findContractByOwner(UUID id) {
        Contract contract = contractRepository.findByOwnerId(id).orElseThrow(() -> new ResourceNotFoundException("Owner not found with this id " + id));
        return contractMappper.toResponse(contract);
    }

    @Transactional
    public ContractResponse updateContract(UUID id, ContractRequest contractRequest) {
        Contract contractToUpdate = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found with this id " + id));

        UUID newPropertyId = contractRequest.propertyId();
        UUID currentPropertyId = contractToUpdate.getProperty().getId();

        if (!newPropertyId.equals(currentPropertyId)) {

            Property oldProperty = contractToUpdate.getProperty();
            oldProperty.setStatus(EnumProperty.AVAILABLE);

            Property newProperty = findAndValidateProperty(newPropertyId);
            newProperty.setStatus(EnumProperty.RENTED);
            contractToUpdate.setProperty(newProperty);
        }
        Owner owner = findAndValidateOwner(contractRequest.ownerId());
        Tenant tenant = findAndValidateTenant(contractRequest.tenantId());
        contractToUpdate.setOwner(owner);
        contractToUpdate.setTenant(tenant);

        contractToUpdate.setStart_date(contractRequest.start_date());
        contractToUpdate.setMonthly_value(contractRequest.monthly_value());

        Contract savedContract = contractRepository.save(contractToUpdate);
        return contractMappper.toResponse(savedContract);
    }

    @Transactional
    public ContractResponse closeContract(UUID id) {
        Contract contract = contractRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contract not found with this id " + id));
        if(contract.getStatus() != EnumContract.ACTIVE) {
            throw new IllegalStateException("Only active contracts can be closed.");
        }
        contract.setStatus(EnumContract.TERMINATED);
        Contract closeContract = contractRepository.save(contract);
        return contractMappper.toResponse(closeContract);
    }

}
