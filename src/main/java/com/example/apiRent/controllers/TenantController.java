package com.example.apiRent.controllers;

import com.example.apiRent.dtos.owner.OwnerRequest;
import com.example.apiRent.dtos.owner.OwnerResponse;
import com.example.apiRent.dtos.tenant.TenantRequest;
import com.example.apiRent.dtos.tenant.TenantResponse;
import com.example.apiRent.exceptions.ResourceNotFoundException;
import com.example.apiRent.services.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/tenants")
@Tag(name = "Tenants", description = "Endpoints for Managing Tenants")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping
    @Operation(summary = "Find All Tenants", description = "Find All Tenants", tags = {"Tenants"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TenantResponse.class))
                    )
            }),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<CollectionModel<EntityModel<TenantResponse>>> findAllTenants() {
        List<TenantResponse> tenantList = tenantService.findAllTenants();
        List<EntityModel<TenantResponse>> entityModels = tenantList.stream()
                .map(this::addLinksToTenant)
                .toList();
        Link selfLink = linkTo(methodOn(TenantController.class).findAllTenants()).withSelfRel();
        CollectionModel<EntityModel<TenantResponse>> collectionModel = CollectionModel.of(entityModels, selfLink);
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    @PostMapping("/create")
    @Operation(summary = "Create Tenants", description = "Create Tenants", tags = {"Tenants"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = TenantResponse.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<TenantResponse>> createTenants(@RequestBody TenantRequest tenantRequest) {
        TenantResponse tenantResponse = tenantService.createTenants(tenantRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(addLinksToTenant(tenantResponse));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update Tenants", description = "Update Tenants", tags = {"Tenants"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = TenantRequest.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<TenantResponse>> updateTenants(@PathVariable("id") UUID id, @RequestBody TenantRequest tenantRequest) {
        TenantResponse tenantResponse = tenantService.updateTenants(id, tenantRequest);
        return ResponseEntity.status(HttpStatus.OK).body(addLinksToTenant(tenantResponse));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete By Tenants", description = "Delete By Tenants", tags = {"Tenants"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = TenantResponse.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<Void> deleteTenantById(@PathVariable("id") UUID id) {
        tenantService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find Tenants By Id", description = "Find Tenants By Id", tags = {"Tenants"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = TenantResponse.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<TenantResponse>> findTenantById(@PathVariable("id") UUID id) {
        TenantResponse tenantResponse = tenantService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(addLinksToTenant(tenantResponse));
    }

    @GetMapping("/search/findByName")
    @Operation(summary = "Find Tenants By Name", description = "Find Tenants By Name", tags = {"Tenants"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = TenantResponse.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<CollectionModel<EntityModel<TenantResponse>>> findTenantByName(@RequestParam("name") String name) {
        List<TenantResponse> tenantResponses = tenantService.queryName(name);
        if(tenantResponses.isEmpty()) throw new ResourceNotFoundException("No tenant found with name containing: \" + name");

        List<EntityModel<TenantResponse>> resources = tenantResponses.stream()
                .map(this::addLinksToTenant)
                .toList();

        Link selfLink = linkTo(methodOn(TenantController.class).findTenantByName(name)).withSelfRel();
        CollectionModel<EntityModel<TenantResponse>> collectionModel = CollectionModel.of(resources, selfLink);
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);

    }


    private EntityModel<TenantResponse> addLinksToTenant(TenantResponse tenantResponse) {
        UUID tenantId = tenantResponse.id();
        Link selfLink = linkTo(methodOn(TenantController.class).findAllTenants()).withSelfRel().withType("GET");
        Link createLink = linkTo(methodOn(TenantController.class).createTenants(null)).withRel("createTenants").withType("POST");
        Link updateLink = linkTo(methodOn(TenantController.class).updateTenants(tenantId, null)).withRel("updateTenants").withType("PUT");
        Link findTenantByIdLink = linkTo(methodOn(TenantController.class).findTenantById(tenantId)).withRel("findTenantById").withType("GET");
        Link findTenantByNameLink = linkTo(methodOn(TenantController.class).findTenantByName(tenantResponse.name())).withRel("findTenantByName").withType("GET");
        Link deleteTenantByIdLink = linkTo(methodOn(TenantController.class).deleteTenantById(tenantId)).withRel("deleteTenantById").withType("DELETE");
        return EntityModel.of(tenantResponse, selfLink, createLink, updateLink, findTenantByIdLink, findTenantByNameLink, deleteTenantByIdLink);
    }

}
