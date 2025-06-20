package com.example.apiRent.mappers;

import com.example.apiRent.dtos.owner.OwnerResponse;
import com.example.apiRent.dtos.payment.PaymentRequest;
import com.example.apiRent.dtos.payment.PaymentResponse;
import com.example.apiRent.models.Owner;
import com.example.apiRent.models.Payment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    Payment toEntity(PaymentRequest request);
    PaymentResponse toResponse(Payment payment);
    List<PaymentResponse> toResponseList(List<Payment> payments);
}
