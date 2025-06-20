package com.example.apiRent.controllers;

import com.example.apiRent.dtos.owner.OwnerRequest;
import com.example.apiRent.dtos.owner.OwnerResponse;
import com.example.apiRent.exceptions.ResourceNotFoundException;
import com.example.apiRent.services.OwnerService;
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
@RequestMapping("/owners")
@Tag(name = "Owners", description = "Endpoints for Managing Owners")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    @Operation(summary = "Find All Owners", description = "Find All Owners", tags = {"Owners"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = OwnerResponse.class))
                    )
            }),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<CollectionModel<EntityModel<OwnerResponse>>> findAllOwners() {
        List<OwnerResponse> ownerResponses = ownerService.findAllOwners();
        List<EntityModel<OwnerResponse>> resources = ownerResponses.stream()
                .map(this::addLinksToOwner)
                .toList();

        Link selfLink = linkTo(methodOn(OwnerController.class).findAllOwners()).withSelfRel();
        CollectionModel<EntityModel<OwnerResponse>> collectionModel = CollectionModel.of(resources, selfLink);
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);

    }

    @PostMapping("/create")
    @Operation(summary = "Create Owners", description = "Create Owners", tags = {"Owners"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = OwnerRequest.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<OwnerResponse>> createOwners(@RequestBody OwnerRequest ownerRequest) {
        OwnerResponse ownerResponse = ownerService.createOwners(ownerRequest);
        EntityModel<OwnerResponse> resource = addLinksToOwner(ownerResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update Owners", description = "Update Owners", tags = {"Owners"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = OwnerRequest.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<OwnerResponse>> updateOwners(@PathVariable("id") UUID id, @RequestBody OwnerRequest ownerRequest) {
        OwnerResponse ownerResponse = ownerService.updateOwners(id, ownerRequest);
        return ResponseEntity.status(HttpStatus.OK).body(addLinksToOwner(ownerResponse));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete By Owners", description = "Delete By Owners", tags = {"Owners"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = OwnerResponse.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<Void> deleteOwnerById(@PathVariable("id") UUID id) {
        ownerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find Owners By Id", description = "Find Owners By Id", tags = {"Owners"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = OwnerResponse.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<OwnerResponse>> findOwnerById(@PathVariable("id") UUID id) {
        OwnerResponse ownerResponse = ownerService.findOwnerById(id);
        return ResponseEntity.status(HttpStatus.OK).body(addLinksToOwner(ownerResponse));
    }

    @GetMapping("/search/findByName")
    @Operation(summary = "Find Owners By Name", description = "Find Owners By Name", tags = {"Owners"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = OwnerResponse.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<CollectionModel<EntityModel<OwnerResponse>>> queryNameLike(@RequestParam("name") String name) {
        List<OwnerResponse> ownerResponses = ownerService.queryByName(name);
        if (ownerResponses.isEmpty()) {
            throw new ResourceNotFoundException("No owner found with name containing: " + name);
        }

        List<EntityModel<OwnerResponse>> resources = ownerResponses.stream()
                .map(this::addLinksToOwner)
                .toList();

        Link selfLink = linkTo(methodOn(OwnerController.class).queryNameLike(name)).withSelfRel();
        CollectionModel<EntityModel<OwnerResponse>> collectionModel = CollectionModel.of(resources, selfLink);
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }


    private EntityModel<OwnerResponse> addLinksToOwner(OwnerResponse ownerResponse) {
        UUID ownerId = ownerResponse.id();

        Link selfLink = linkTo(methodOn(OwnerController.class).findOwnerById(ownerId)).withSelfRel().withType("GET");

        Link queryName = linkTo(methodOn(OwnerController.class).queryNameLike(ownerResponse.name())).withRel("queryNameLike").withType("GET");

        Link allOwnersLink = linkTo(methodOn(OwnerController.class).findAllOwners()).withRel("findAllOwners").withType("GET");

        Link deleteLink = linkTo(methodOn(OwnerController.class).deleteOwnerById(ownerId)).withRel("delete").withType("DELETE");

        Link updateLink = linkTo(methodOn(OwnerController.class).updateOwners(ownerId, null)).withRel("update").withType("PUT");

        Link createLink = linkTo(methodOn(OwnerController.class).createOwners(null)).withRel("createOwners").withType("POST");

        return EntityModel.of(ownerResponse, selfLink, allOwnersLink, deleteLink,createLink, updateLink, queryName);
    }

}
