package ca.bc.gov.farms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.farms.common.controllers.CommonController;
import ca.bc.gov.farms.data.models.StructureGroupAttributeModel;
import ca.bc.gov.farms.services.StructureGroupAttributeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/structureGroupAttributes")
public class StructureGroupAttributeController extends CommonController {

    protected StructureGroupAttributeController() {
        super(StructureGroupAttributeController.class.getName());
    }

    @Autowired
    private StructureGroupAttributeService structureGroupAttributeService;

    @GetMapping
    @Operation(
            operationId = "Get Structure Group Attribute resource by Structure Group Code.",
            summary = "Get Structure Group Attribute resource by Structure Group Code."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = StructureGroupAttributeModel.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<StructureGroupAttributeModel> getStructureGroupAttributesByStructureGroupCode(
            @RequestParam String structureGroupCode) {
        log.debug(" >> getStructureGroupAttributesByStructureGroupCode: {}", structureGroupCode);

        try {
            StructureGroupAttributeModel resource = structureGroupAttributeService.getStructureGroupAttributesByStructureGroupCode(structureGroupCode);
            return ok(resource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Structure Group Attributes", e);
            return internalServerError();
        }
    }

    @GetMapping("/{structureGroupAttributeId}")
    @Operation(
            operationId = "Get Structure Group Attribute resource by Structure Group Attribute Id.",
            summary = "Get Structure Group Attribute resource by Structure Group Attribute Id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = StructureGroupAttributeModel.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<StructureGroupAttributeModel> getStructureGroupAttribute(
            @PathVariable Long structureGroupAttributeId) {
        log.debug(" >> getStructureGroupAttribute: {}", structureGroupAttributeId);

        try {
            StructureGroupAttributeModel resource = structureGroupAttributeService.getStructureGroupAttribute(structureGroupAttributeId);
            return ok(resource);
        } catch (NotFoundException e) {
            log.warn(" ### Structure Group Attribute not found: {}", structureGroupAttributeId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Structure Group Attribute", e);
            return internalServerError();
        }
    }

    @PostMapping
    @Operation(
            operationId = "Create Structure Group Attribute resource.",
            summary = "Create Structure Group Attribute resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = StructureGroupAttributeModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<StructureGroupAttributeModel> createStructureGroupAttribute(
            @Valid @RequestBody StructureGroupAttributeModel resource) {
        log.debug(" >> createStructureGroupAttribute");

        try {
            StructureGroupAttributeModel newResource = structureGroupAttributeService.createStructureGroupAttribute(resource);
            return ResponseEntity.status(201).body(newResource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while creating Structure Group Attribute", e);
            return internalServerError();
        }
    }

    @PutMapping("/{structureGroupAttributeId}")
    @Operation(
            operationId = "Update Structure Group Attribute resource.",
            summary = "Update Structure Group Attribute resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = StructureGroupAttributeModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<StructureGroupAttributeModel> updateStructureGroupAttribute(
            @PathVariable Long structureGroupAttributeId,
            @Valid @RequestBody StructureGroupAttributeModel resource) {
        log.debug(" >> updateStructureGroupAttribute");

        try {
            StructureGroupAttributeModel updatedResource = structureGroupAttributeService.updateStructureGroupAttribute(structureGroupAttributeId, resource);
            return ok(updatedResource);
        } catch (NotFoundException e) {
            log.warn(" ### Structure Group Attribute not found for update: {}", structureGroupAttributeId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while updating Structure Group Attribute", e);
            return internalServerError();
        }
    }

    @DeleteMapping("/{structureGroupAttributeId}")
    @Operation(
            operationId = "Delete Structure Group Attribute resource.",
            summary = "Delete Structure Group Attribute resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<Void> deleteStructureGroupAttribute(
            @PathVariable Long structureGroupAttributeId) {
        log.debug(" >> deleteStructureGroupAttribute");

        try {
            structureGroupAttributeService.deleteStructureGroupAttribute(structureGroupAttributeId);
            return noContent();
        } catch (NotFoundException e) {
            log.warn(" ### Structure Group Attribute for deletion not found: {}", structureGroupAttributeId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while deleting Structure Group Attribute", e);
            return internalServerError();
        }
    }
}
