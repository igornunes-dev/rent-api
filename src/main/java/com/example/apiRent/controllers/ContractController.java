package com.example.apiRent.controllers;

import com.example.apiRent.dtos.contract.ContractRequest;
import com.example.apiRent.dtos.contract.ContractResponse;
import com.example.apiRent.dtos.property.PropertyResponse;
import com.example.apiRent.services.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/contracts")
public class ContractController {

    private final ContractService contractService;


    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping
    @Operation(summary = "Find All Contracts", description = "Find All Contracts", tags = {"Contracts"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ContractResponse.class))
                    )
            }),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<CollectionModel<EntityModel<ContractResponse>>> findAllContracts() {
        List<ContractResponse> contractResponseList = contractService.findAllContracts();
        List<EntityModel<ContractResponse>> resource = contractResponseList.stream()
                .map(this::addLinksToContracts)
                .toList();
        Link selfLink = linkTo(methodOn(ContractController.class).findAllContracts()).withSelfRel();
        CollectionModel<EntityModel<ContractResponse>> collectionModel = CollectionModel.of(resource, selfLink);
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    @PostMapping("/create")
    @Operation(summary = "Create Contracts", description = "Create Contracts", tags = {"Contracts"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = ContractResponse.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<ContractResponse>> createContracts(@RequestBody ContractRequest contractRequest) {
        ContractResponse contractResponse = contractService.createContracts(contractRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(addLinksToContracts(contractResponse));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find Contracts By Id", description = "Find Contracts By Id", tags = {"Contracts"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = ContractResponse.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<ContractResponse>> findContractById(@PathVariable("id") UUID id) {
        ContractResponse contractResponse = contractService.findContractById(id);
        return ResponseEntity.status(HttpStatus.OK).body(addLinksToContracts(contractResponse));
    }

    @GetMapping("/tenants/{id}")
    @Operation(summary = "Find Contracts By Tenant", description = "Find Contracts By Tenant", tags = {"Contracts"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = ContractResponse.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<ContractResponse>> findContractByTenant(@PathVariable("id") UUID id) {
        ContractResponse contractResponse = contractService.findContractByTenant(id);
        return ResponseEntity.status(HttpStatus.OK).body(addLinksToContracts(contractResponse));
    }

    @GetMapping("/owners/{id}")
    @Operation(summary = "Find Contracts By Owner", description = "Find Contracts By Owner", tags = {"Contracts"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = ContractResponse.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<ContractResponse>> findContractByOwner(@PathVariable("id") UUID id) {
        ContractResponse contractResponse = contractService.findContractByOwner(id);
        return ResponseEntity.status(HttpStatus.OK).body(addLinksToContracts(contractResponse));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update Contracts", description = "Update Contracts", tags = {"Contracts"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = ContractResponse.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<ContractResponse>> updateContract(@PathVariable("id") UUID id, @RequestBody ContractRequest contractRequest) {
        ContractResponse contractResponse = contractService.updateContract(id, contractRequest);
        return ResponseEntity.status(HttpStatus.OK).body(addLinksToContracts(contractResponse));
    }

    @PutMapping("/terminate/{id}")
    @Operation(summary = "Terminate Contracts", description = "Terminate Contracts", tags = {"Contracts"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = ContractResponse.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<ContractResponse>> closeContract(@PathVariable("id") UUID id) {
        ContractResponse contractResponse = contractService.closeContract(id);
        return ResponseEntity.status(HttpStatus.OK).body(addLinksToContracts(contractResponse));
    }


    private EntityModel<ContractResponse> addLinksToContracts(ContractResponse contractResponse) {
        UUID contractId = contractResponse.id();
        UUID tenantId = contractResponse.tenant().id();
        UUID ownerId = contractResponse.owner().id();

        Link selfLink = linkTo(methodOn(ContractController.class).findAllContracts()).withSelfRel().withType("GET");

        Link createLink = linkTo(methodOn(ContractController.class).createContracts(null)).withRel("createContracts").withType("POST");

        Link updateLink = linkTo(methodOn(ContractController.class).updateContract(contractId, null)).withRel("updateProperty").withType("PUT");

        Link findContractByIdLink = linkTo(methodOn(ContractController.class).findContractById(contractId)).withRel("findContractById").withType("GET");

        Link findContractByTenantLink = linkTo(methodOn(ContractController.class).findContractByTenant(tenantId)).withRel("findContractByTenant").withType("GET");

        Link findContractByOwnerLink = linkTo(methodOn(ContractController.class).findContractByOwner(ownerId)).withRel("findContractByOwner").withType("GET");

        Link closeContractLink = linkTo(methodOn(ContractController.class).closeContract(contractId)).withRel("closeContract").withType("PUT");

        return EntityModel.of(contractResponse, selfLink, createLink, updateLink, findContractByIdLink, findContractByTenantLink, findContractByOwnerLink, closeContractLink);
    }
}
