package com.example.apiRent.services;

import com.example.apiRent.dtos.payment.PaymentResponse;
import com.example.apiRent.enums.EnumPayment;
import com.example.apiRent.enums.EnumUser;
import com.example.apiRent.exceptions.ResourceNotFoundException;
import com.example.apiRent.mappers.PaymentMapper;
import com.example.apiRent.models.Contract;
import com.example.apiRent.models.Owner;
import com.example.apiRent.models.Payment;
import com.example.apiRent.repositories.OwnerRepository;
import com.example.apiRent.repositories.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OwnerRepository ownerRepository;

    public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper, OwnerRepository ownerRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.ownerRepository = ownerRepository;
    }

    @Transactional
    public void generatePaymentsForContracts(Contract contract) {
        List<Payment> paymentsToCreate = new ArrayList<>();

        LocalDate currentDueDate = contract.getStart_date();

        while (!currentDueDate.isAfter(contract.getEnd_date())) {
            Payment newPayment = new Payment();

            newPayment.setContract(contract);
            newPayment.setAmount(contract.getMonthly_value());
            newPayment.setDue_date(currentDueDate);
            newPayment.setStatus(EnumPayment.PENDING);
            newPayment.setPayment_date(null);

            paymentsToCreate.add(newPayment);

            currentDueDate = currentDueDate.plusMonths(1);
        }

        if (!paymentsToCreate.isEmpty()) {
            paymentRepository.saveAll(paymentsToCreate);
        }
    }

    @Transactional
    public PaymentResponse findPaymentsById(UUID id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Payment not found with this id " + id));
        return paymentMapper.toResponse(payment);
    }

    @Transactional
    public List<PaymentResponse> findPaymentByContract(UUID id) {
        List<Payment> payment = paymentRepository.findAllByContractId(id);
        return paymentMapper.toResponseList(payment);
    }

    @Transactional
    public PaymentResponse confirmPayment(UUID id, UUID ownerId) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found this id " + id));
        ownerRepository.findByIdAndRole(ownerId, EnumUser.LOCATOR).orElseThrow(() -> new ResourceNotFoundException("Owner not found or permission denied with this id " + id));

        UUID actualOwnerId = payment.getContract().getOwner().getId();
        if(!actualOwnerId.equals(ownerId)) {
            throw new ResourceNotFoundException("This owner does not belong to this contract");
        }

        if (payment.getStatus() != EnumPayment.PENDING && payment.getStatus() != EnumPayment.OVERDUE) {
            throw new IllegalStateException("Payment cannot be confirmed as its current status is '" + payment.getStatus() + "'.");
        }
        payment.setStatus(EnumPayment.PAID);
        payment.setPayment_date(LocalDate.now());

        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toResponse(savedPayment);
    }



}
