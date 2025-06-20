package com.example.apiRent.controllers;

import com.example.apiRent.dtos.payment.PaymentResponse;
import com.example.apiRent.dtos.property.PropertyResponse;
import com.example.apiRent.services.PaymentService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<EntityModel<PaymentResponse>> findPaymentById(@PathVariable("id") UUID id) {
        PaymentResponse paymentResponse = paymentService.findPaymentsById(id);
        return ResponseEntity.status(HttpStatus.OK).body(addLinksToPayment(paymentResponse));
    }

    @GetMapping("/contracts/{id}")
    public ResponseEntity<CollectionModel<EntityModel<PaymentResponse>>> findPaymentByContract(@PathVariable("id") UUID id) {
        List<PaymentResponse> paymentResponses = paymentService.findPaymentByContract(id);
        List<EntityModel<PaymentResponse>> resource = paymentResponses.stream()
                .map(this::addLinksToPayment)
                .toList();
        Link selfLink = linkTo(methodOn(PaymentController.class).findPaymentByContract(id)).withSelfRel();
        CollectionModel<EntityModel<PaymentResponse>> collectionModel = CollectionModel.of(resource, selfLink);
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    @PatchMapping("/{paymentId}/owner/{ownerId}/confirm")
    public ResponseEntity<EntityModel<PaymentResponse>> confirmPayment(@PathVariable("paymentId") UUID paymentId, @PathVariable("ownerId") UUID ownerId) {
        PaymentResponse paymentResponse = paymentService.confirmPayment(paymentId, ownerId);
        return ResponseEntity.status(HttpStatus.OK).body(addLinksToPayment(paymentResponse));
    }


    private EntityModel<PaymentResponse> addLinksToPayment(PaymentResponse paymentResponse) {
        UUID paymentId = paymentResponse.id();
        UUID contractId = paymentResponse.contract().id();
        UUID ownerId = paymentResponse.contract().owner().id();

        Link findPaymentByIdLink = linkTo(methodOn(PaymentController.class).findPaymentById(paymentId)).withRel("findPaymentById").withType("GET");
        Link findPaymentByContractLink = linkTo(methodOn(PaymentController.class).findPaymentByContract(contractId)).withRel("findPaymentByContract").withType("GET");
        Link confirmPaymentLink = linkTo(methodOn(PaymentController.class).confirmPayment(paymentId,ownerId)).withRel("confirmPayment").withType("PATCH");


        return EntityModel.of(paymentResponse, findPaymentByIdLink, findPaymentByContractLink, confirmPaymentLink);
    }
}
