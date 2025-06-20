package com.example.apiRent.controllers;

import com.example.apiRent.dtos.owner.OwnerRequest;
import com.example.apiRent.dtos.owner.OwnerResponse;
import com.example.apiRent.dtos.property.PropertyRequest;
import com.example.apiRent.dtos.property.PropertyResponse;
import com.example.apiRent.services.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.coyote.Response;
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
@RequestMapping("/properties")
public class PropertyController {

    private final PropertyService propertyService;


    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping
    @Operation(summary = "Find All Properties", description = "Find All Properties", tags = {"Properties"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = PropertyResponse.class))
                    )
            }),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<CollectionModel<EntityModel<PropertyResponse>>> findPropertiesAll() {
        List<PropertyResponse> propertyResponses = propertyService.findAllProperties();
        List<EntityModel<PropertyResponse>> resource = propertyResponses.stream()
                .map(this::addLinksToProperty)
                .toList();

        Link selfLink = linkTo(methodOn(PropertyController.class).findPropertiesAll()).withSelfRel();
        CollectionModel<EntityModel<PropertyResponse>> collectionModel = CollectionModel.of(resource, selfLink);
        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

    @PostMapping("/create")
    @Operation(summary = "Create Properties", description = "Create Properties", tags = {"Properties"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = PropertyResponse.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<PropertyResponse>> createProperty(@RequestBody PropertyRequest propertyRequest) {
        PropertyResponse propertyResponse = propertyService.createProperties(propertyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(addLinksToProperty(propertyResponse));
    }


    @PutMapping("/update/{id}")
    @Operation(summary = "Update Properties", description = "Update Properties", tags = {"Properties"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = PropertyResponse.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<PropertyResponse>> updateProperty(@PathVariable("id") UUID id, @RequestBody PropertyRequest propertyRequest) {
        PropertyResponse propertyResponse = propertyService.updateProperties(id, propertyRequest);
        return ResponseEntity.status(HttpStatus.OK).body(addLinksToProperty(propertyResponse));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete By Properties", description = "Delete By Properties", tags = {"Properties"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = PropertyResponse.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<Void> deletePropertyById(@PathVariable("id") UUID id) {
        propertyService.deletePropertyById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find Properties By Id", description = "Find Properties By Id", tags = {"Properties"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = PropertyResponse.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<PropertyResponse>> findPropertyById(@PathVariable("id") UUID id) {
        PropertyResponse propertyResponse = propertyService.findPropertyById(id);
        return ResponseEntity.status(HttpStatus.OK).body(addLinksToProperty(propertyResponse));
    }

    @GetMapping("/search/query")
    @Operation(summary = "Find Title Properties", description = "Find Title Properties", tags = {"Properties"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = PropertyResponse.class))
                    )
            }),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<CollectionModel<EntityModel<PropertyResponse>>> queryPropertyByName(@RequestParam("name") String name) {
        List<PropertyResponse> propertyResponses = propertyService.queryPropertyByName(name);
        List<EntityModel<PropertyResponse>> resource = propertyResponses.stream()
                .map(this::addLinksToProperty)
                .toList();

        Link selfLink = linkTo(methodOn(PropertyController.class).queryPropertyByName(name)).withSelfRel();
        CollectionModel<EntityModel<PropertyResponse>> collectionModel = CollectionModel.of(resource, selfLink);

        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }


    private EntityModel<PropertyResponse> addLinksToProperty(PropertyResponse propertyResponse) {
        UUID propertyId = propertyResponse.id();
        String propertyTitle = propertyResponse.title();

        Link selfLink = linkTo(methodOn(PropertyController.class).findPropertiesAll()).withSelfRel().withType("GET");

        Link createLink = linkTo(methodOn(PropertyController.class).createProperty(null)).withRel("createProperty").withType("POST");

        Link updateLink = linkTo(methodOn(PropertyController.class).updateProperty(propertyId, null)).withRel("updateProperty").withType("PUT");

        Link deleteLink = linkTo(methodOn(PropertyController.class).deletePropertyById(propertyId)).withRel("deletePropertyById").withType("DELETE");

        Link findPropertyByIdLink = linkTo(methodOn(PropertyController.class).findPropertyById(propertyId)).withRel("findPropertyById").withType("GET");

        Link queryPropertyByNameLink = linkTo(methodOn(PropertyController.class).queryPropertyByName(propertyTitle)).withRel("queryPropertyByName").withType("GET");


        return EntityModel.of(propertyResponse, selfLink, createLink, updateLink, deleteLink, findPropertyByIdLink, queryPropertyByNameLink);
    }
}
