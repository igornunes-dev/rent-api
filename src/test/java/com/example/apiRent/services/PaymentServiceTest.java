package com.example.apiRent.services;


import com.example.apiRent.dtos.payment.PaymentResponse;
import com.example.apiRent.enums.EnumPayment;
import com.example.apiRent.enums.EnumUser;
import com.example.apiRent.mappers.PaymentMapper;
import com.example.apiRent.mappers.PropertyMapper;
import com.example.apiRent.models.*;
import com.example.apiRent.repositories.ContractRepository;
import com.example.apiRent.repositories.OwnerRepository;
import com.example.apiRent.repositories.PaymentRepository;
import com.example.apiRent.repositories.PropertyRepository;
import com.example.apiRent.unitests.mappers.mocks.PaymentMock;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private PaymentMapper paymentMapper;

    private PaymentMock input = new PaymentMock();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentService = new PaymentService(paymentRepository,paymentMapper, ownerRepository);
    }

    @Test
    void generatePaymentsForContracts() {
        Contract mockContract = input.mockContract(1);

        ArgumentCaptor<List<Payment>> paymentListCaptor = ArgumentCaptor.forClass(List.class);

        paymentService.generatePaymentsForContracts(mockContract);


        verify(paymentRepository, times(1)).saveAll(paymentListCaptor.capture());

        List<Payment> capturedPayments = paymentListCaptor.getValue();

        assertNotNull(capturedPayments);
        assertEquals(3, capturedPayments.size());

        Payment firstPayment = capturedPayments.get(0);
        assertEquals(mockContract, firstPayment.getContract());
        assertEquals(new BigDecimal(1), firstPayment.getAmount());
        assertEquals(LocalDate.of(2025, 1, 15), firstPayment.getDue_date());
        assertEquals(EnumPayment.PENDING, firstPayment.getStatus());

        Payment lastPayment = capturedPayments.get(2);
        assertEquals(LocalDate.of(2025, 3, 15), lastPayment.getDue_date());
    }

    @Test
    void findPaymentsById() {
        Payment payment = input.mockEntity(1);
        PaymentResponse paymentResponse = input.mockResponse(1);

        when(paymentRepository.findById(payment.getId())).thenReturn(Optional.of(payment));
        when(paymentMapper.toResponse(payment)).thenReturn(paymentResponse);

        PaymentResponse resource = paymentService.findPaymentsById(payment.getId());

        assertNotNull(resource);

        assertEquals(paymentResponse.id(), resource.id());
        assertEquals(paymentResponse.amount(), resource.amount());
        assertEquals(paymentResponse.due_date(), resource.due_date());
    }

    @Test
    void findPaymentByContract() {
        UUID contractId = input.mockContract(1).getId();

        List<Payment> mockPaymentEntities = input.mockEntityList();

        List<PaymentResponse> mockPaymentResponses = input.mockEntityResponseList();

        when(paymentRepository.findAllByContractId(contractId)).thenReturn(mockPaymentEntities);
        when(paymentMapper.toResponseList(mockPaymentEntities)).thenReturn(mockPaymentResponses);


        List<PaymentResponse> actualResponses = paymentService.findPaymentByContract(contractId);

        assertNotNull(actualResponses);
        assertEquals(mockPaymentResponses.size(), actualResponses.size());

        assertEquals(mockPaymentResponses, actualResponses);
    }

    @Test
    void confirmPayment() {
        Owner owner = input.mockOwner(1);
        Contract mockContract = input.mockContract(1);
        Payment mockPayment = input.mockEntity(1);
        mockPayment.setContract(mockContract);
        PaymentResponse expectedResponse = input.mockResponse(1);


        when(paymentRepository.findById(mockPayment.getId())).thenReturn(Optional.of(mockPayment));
        when(ownerRepository.findByIdAndRole(owner.getId(), EnumUser.LOCATOR)).thenReturn(Optional.of(owner));
        when(paymentRepository.save(any(Payment.class))).thenReturn(mockPayment);
        when(paymentMapper.toResponse(any(Payment.class))).thenReturn(expectedResponse);

        PaymentResponse actualResponse = paymentService.confirmPayment(mockPayment.getId(), owner.getId());

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);

        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository, times(1)).save(paymentCaptor.capture());

        Payment savedPayment = paymentCaptor.getValue();
        assertEquals(EnumPayment.PAID, savedPayment.getStatus());
        assertNotNull(savedPayment.getPayment_date());
    }
}
