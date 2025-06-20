package com.example.apiRent.services;

import com.example.apiRent.dtos.contract.ContractRequest;
import com.example.apiRent.dtos.contract.ContractResponse;
import com.example.apiRent.dtos.owner.OwnerResponse;
import com.example.apiRent.dtos.property.PropertyResponse;
import com.example.apiRent.dtos.tenant.TenantResponse;
import com.example.apiRent.enums.EnumContract;
import com.example.apiRent.enums.EnumProperty;
import com.example.apiRent.enums.EnumUser;
import com.example.apiRent.mappers.ContractMappper;
import com.example.apiRent.mappers.PropertyMapper;
import com.example.apiRent.models.Contract;
import com.example.apiRent.models.Owner;
import com.example.apiRent.models.Property;
import com.example.apiRent.models.Tenant;
import com.example.apiRent.repositories.ContractRepository;
import com.example.apiRent.repositories.OwnerRepository;
import com.example.apiRent.repositories.PropertyRepository;
import com.example.apiRent.repositories.TenantRepository;
import com.example.apiRent.unitests.mappers.mocks.ContractMock;
import com.example.apiRent.unitests.mappers.mocks.PropertyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class ConctractServiceTest {

    private ContractService contractService;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private ContractMappper contractMappper;

    @Mock
    private PaymentService paymentService;

    private ContractMock input = new ContractMock();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        contractService = new ContractService(contractRepository, contractMappper, ownerRepository, tenantRepository, propertyRepository, paymentService);
    }

    @Test
    void findAllContracts() {
        List<Contract> mockEntityList = input.mockEntityList();

        List<ContractResponse> mockDtoList = mockEntityList.stream()
                .map(contract -> {
                    int seedNumber = contract.getMonthly_value().intValue();

                    PropertyResponse correctProperty = input.mockPropertyResponse(seedNumber);
                    TenantResponse correctTenant = input.mockTenantResponse(seedNumber);
                    OwnerResponse correctOwner = input.mockOwnerResponse(seedNumber);

                    return new ContractResponse(
                            contract.getId(),
                            contract.getStart_date(),
                            contract.getEnd_date(),
                            contract.getMonthly_value(),
                            contract.getStatus(),
                            correctProperty,
                            correctTenant,
                            correctOwner
                    );
                })
                .toList();



        when(contractRepository.findAll()).thenReturn(mockEntityList);
        when(contractMappper.toResponseList(mockEntityList)).thenReturn(mockDtoList);


        List<ContractResponse> responseEntity = contractService.findAllContracts();

        assertNotNull(responseEntity);
        assertEquals(14, responseEntity.size());

        ContractResponse contractResponse = responseEntity.get(1);

        assertEquals(mockDtoList.get(1).start_date(), contractResponse.start_date());
        assertEquals(mockDtoList.get(1).end_date(), contractResponse.end_date());
        assertEquals(mockDtoList.get(1).status(), contractResponse.status());
        assertEquals(mockDtoList.get(1).monthly_value(), contractResponse.monthly_value());
        assertEquals(mockDtoList.get(1).owner(), contractResponse.owner());
        assertEquals(mockDtoList.get(1).tenant(), contractResponse.tenant());
        assertEquals(mockDtoList.get(1).property(), contractResponse.property());

        var ContractFour = responseEntity.get(4);
        OwnerResponse ownerFour = input.mockOwnerResponse(4);
        TenantResponse tenantFour = input.mockTenantResponse(4);
        PropertyResponse propertyFour = input.mockPropertyResponse(4);
        assertNotNull(ContractFour);
        assertNotNull(ContractFour.id());

        assertEquals(LocalDate.of(2025,1,1), ContractFour.start_date());
        assertEquals(EnumContract.ACTIVE, ContractFour.status());
        assertNull(ContractFour.end_date());
        assertEquals(new BigDecimal(4), ContractFour.monthly_value());
        assertEquals(ownerFour, ContractFour.owner());
        assertEquals(tenantFour, ContractFour.tenant());
        assertEquals(propertyFour, ContractFour.property());


        var ContractSeven = responseEntity.get(7);
        OwnerResponse ownerSeven = input.mockOwnerResponse(7);
        TenantResponse tenantSeven = input.mockTenantResponse(7);
        PropertyResponse propertySeven = input.mockPropertyResponse(7);
        assertNotNull(ContractSeven);
        assertNotNull(ContractSeven.id());

        assertEquals(LocalDate.of(2025,1,1), ContractSeven.start_date());
        assertEquals(EnumContract.ACTIVE, ContractSeven.status());
        assertNull(ContractSeven.end_date());
        assertEquals(new BigDecimal(7), ContractSeven.monthly_value());
        assertEquals(ownerSeven, ContractSeven.owner());
        assertEquals(tenantSeven, ContractSeven.tenant());
        assertEquals(propertySeven, ContractSeven.property());
    }


    @Test
    void createContracts() {

        ContractRequest request = input.mockRequest(1);
        Owner owner = input.mockOwner(1);
        Tenant tenant = input.mockTenant(1);
        Property property = input.mockProperty(1);
        property.setStatus(EnumProperty.AVAILABLE);

        Contract savedContract = input.mockEntity(1);
        ContractResponse expectedResponse = input.mockResponse(1);

        when(ownerRepository.findByIdAndRole(request.ownerId(), EnumUser.LOCATOR)).thenReturn(Optional.of(owner));
        when(tenantRepository.findByIdAndRole(request.tenantId(), EnumUser.TENANT)).thenReturn(Optional.of(tenant));
        when(propertyRepository.findByIdAndStatus(request.propertyId(), EnumProperty.AVAILABLE)).thenReturn(Optional.of(property));

        when(contractMappper.toEntity(request)).thenReturn(savedContract);
        when(contractRepository.save(any(Contract.class))).thenReturn(savedContract);
        when(contractMappper.toResponse(savedContract)).thenReturn(expectedResponse);

        ArgumentCaptor<Contract> contractCaptor = ArgumentCaptor.forClass(Contract.class);

        ContractResponse result = contractService.createContracts(request);
        assertNotNull(result);
        assertEquals(expectedResponse.id(), result.id());
        assertEquals(expectedResponse.start_date(), result.start_date());
        assertEquals(expectedResponse.end_date(), result.end_date());
        assertEquals(expectedResponse.monthly_value(), result.monthly_value());
        assertEquals(expectedResponse.owner().id(), result.owner().id());
        verify(paymentService, times(1)).generatePaymentsForContracts(contractCaptor.capture());

        Contract capturedContract = contractCaptor.getValue();
        assertEquals(savedContract.getId(), capturedContract.getId());
    }

    @Test
    void findContractById() {
        Contract contract = input.mockEntity(1);
        ContractResponse contractResponse = input.mockResponse(1);

        when(contractRepository.findById(contract.getId())).thenReturn(Optional.of(contract));
        when(contractMappper.toResponse(contract)).thenReturn(contractResponse);

        ContractResponse result = contractService.findContractById(contract.getId());

        assertNotNull(result);

        OwnerResponse ownerResponse = input.mockOwnerResponse(1);
        TenantResponse tenantResponse = input.mockTenantResponse(1);
        PropertyResponse propertyResponse = input.mockPropertyResponse(1);

        assertEquals(LocalDate.of(2025,1,1), result.start_date());
        assertEquals(EnumContract.ACTIVE, result.status());
        assertNull(result.end_date());
        assertEquals(new BigDecimal(1), result.monthly_value());
        assertEquals(ownerResponse, result.owner());
        assertEquals(tenantResponse, result.tenant());
        assertEquals(propertyResponse, result.property());
    }

    @Test
    void findContractByTenant() {
        Contract contract = input.mockEntity(1);
        Tenant tenant = input.mockTenant(1);
        ContractResponse contractResponse = input.mockResponse(1);

        when(contractRepository.findByTenantId(tenant.getId())).thenReturn(Optional.of(contract));
        when(contractMappper.toResponse(contract)).thenReturn(contractResponse);

        ContractResponse result = contractService.findContractByTenant(tenant.getId());

        assertNotNull(result);

        OwnerResponse ownerResponse = input.mockOwnerResponse(1);
        TenantResponse tenantResponse = input.mockTenantResponse(1);
        PropertyResponse propertyResponse = input.mockPropertyResponse(1);

        assertEquals(LocalDate.of(2025,1,1), result.start_date());
        assertEquals(EnumContract.ACTIVE, result.status());
        assertNull(result.end_date());
        assertEquals(new BigDecimal(1), result.monthly_value());
        assertEquals(ownerResponse, result.owner());
        assertEquals(tenantResponse, result.tenant());
        assertEquals(propertyResponse, result.property());
    }

    @Test
    void findContractByOwner() {
        Contract contract = input.mockEntity(1);
        Owner owner = input.mockOwner(1);
        ContractResponse contractResponse = input.mockResponse(1);

        when(contractRepository.findByOwnerId(owner.getId())).thenReturn(Optional.of(contract));
        when(contractMappper.toResponse(contract)).thenReturn(contractResponse);

        ContractResponse result = contractService.findContractByOwner(owner.getId());

        assertNotNull(result);

        OwnerResponse ownerResponse = input.mockOwnerResponse(1);
        TenantResponse tenantResponse = input.mockTenantResponse(1);
        PropertyResponse propertyResponse = input.mockPropertyResponse(1);

        assertEquals(LocalDate.of(2025,1,1), result.start_date());
        assertEquals(EnumContract.ACTIVE, result.status());
        assertNull(result.end_date());
        assertEquals(new BigDecimal(1), result.monthly_value());
        assertEquals(ownerResponse, result.owner());
        assertEquals(tenantResponse, result.tenant());
        assertEquals(propertyResponse, result.property());
    }

    @Test
    void updateContractWithoutReplace() {
        ContractRequest request = input.mockRequest(1);
        Contract existingContract = input.mockEntity(1);
        Property property = input.mockProperty(1);
        Owner owner = input.mockOwner(1);
        Tenant tenant = input.mockTenant(1);
        UUID contractId = existingContract.getId();
        existingContract.setStart_date(request.start_date());
        existingContract.setProperty(property);
        existingContract.setMonthly_value(request.monthly_value());
        existingContract.setOwner(owner);
        existingContract.setTenant(tenant);

        ContractResponse expectedResponse = input.mockResponse(1);

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(existingContract));
        when(ownerRepository.findByIdAndRole(request.ownerId(), EnumUser.LOCATOR)).thenReturn(Optional.of(existingContract.getOwner()));
        when(tenantRepository.findByIdAndRole(request.tenantId(), EnumUser.TENANT)).thenReturn(Optional.of(existingContract.getTenant()));
        when(contractRepository.save(any(Contract.class))).thenReturn(existingContract);
        when(contractMappper.toResponse(existingContract)).thenReturn(expectedResponse);

        ContractResponse result = contractService.updateContract(contractId, request);

        assertNotNull(result);

        assertEquals(request.start_date(), result.start_date());
        assertEquals(request.monthly_value(), result.monthly_value());
        assertEquals(expectedResponse.owner(), result.owner());
        assertEquals(expectedResponse.tenant(), result.tenant());
        assertEquals(expectedResponse.property(), result.property());
        assertEquals(EnumContract.ACTIVE, result.status());
    }

    @Test
    void closeContract() {
        Contract contract = input.mockEntity(1);
        contract.setStatus(EnumContract.ACTIVE);
        contract.setEnd_date(null);
        ContractResponse baseResponse = input.mockResponse(1);

        ContractResponse expectedFinalResponse = new ContractResponse(
                baseResponse.id(),
                baseResponse.start_date(),
                LocalDate.now(),
                baseResponse.monthly_value(),
                EnumContract.TERMINATED,
                baseResponse.property(),
                baseResponse.tenant(),
                baseResponse.owner()
        );

        when(contractRepository.findById(contract.getId())).thenReturn(Optional.of(contract));
        when(contractRepository.save(any(Contract.class))).thenReturn(contract);
        when(contractMappper.toResponse(any(Contract.class))).thenReturn(expectedFinalResponse);

        ContractResponse result = contractService.closeContract(contract.getId());


        assertNotNull(result);

        assertEquals(expectedFinalResponse.status(), result.status());
        assertEquals(expectedFinalResponse.end_date(), result.end_date());
        assertEquals(expectedFinalResponse.owner(), result.owner());
    }
}
