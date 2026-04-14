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
import ca.bc.gov.farms.data.models.InventoryTypeXrefListRsrc;
import ca.bc.gov.farms.data.models.InventoryTypeXrefRsrc;
import ca.bc.gov.farms.services.InventoryTypeXrefService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/inventoryTypeXrefs")
public class InventoryTypeXrefController extends CommonController {

    protected InventoryTypeXrefController() {
        super(InventoryTypeXrefController.class.getName());
    }

    @Autowired
    private InventoryTypeXrefService inventoryTypeXrefService;

    @GetMapping
    @Operation(
            operationId = "Get Inventory Type Xref resources by Inventory Class Code.",
            summary = "Get Inventory Type Xref resources by Inventory Class Code."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = InventoryTypeXrefListRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<InventoryTypeXrefListRsrc> getInventoryTypeXrefsByInventoryClassCode(
            @RequestParam String inventoryClassCode) {
        log.debug(" >> getInventoryTypeXrefsByInventoryClassCode: {}", inventoryClassCode);

        try {
            InventoryTypeXrefListRsrc resource = inventoryTypeXrefService.getInventoryTypeXrefsByInventoryClassCode(inventoryClassCode);
            return ok(resource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Inventory Type Xrefs", e);
            return internalServerError();
        }
    }

    @GetMapping("/{agristabilityCommodityXrefId}")
    @Operation(
            operationId = "Get Inventory Type Xref resource by Inventory Type Xref Id.",
            summary = "Get Inventory Type Xref resource by Inventory Type Xref Id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = InventoryTypeXrefRsrc.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<InventoryTypeXrefRsrc> getInventoryTypeXref(
            @PathVariable Long agristabilityCommodityXrefId) {
        log.debug(" >> getInventoryTypeXref: {}", agristabilityCommodityXrefId);

        try {
            InventoryTypeXrefRsrc resource = inventoryTypeXrefService.getInventoryTypeXref(agristabilityCommodityXrefId);
            return ok(resource);
        } catch (NotFoundException e) {
            log.warn(" ### Inventory Type Xref not found: {}", agristabilityCommodityXrefId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Inventory Type Xref", e);
            return internalServerError();
        }
    }

    @PostMapping
    @Operation(
            operationId = "Create Inventory Type Xref resource.",
            summary = "Create Inventory Type Xref resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                content = @Content(schema = @Schema(implementation = InventoryTypeXrefRsrc.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<InventoryTypeXrefRsrc> createInventoryTypeXref(
            @Valid @RequestBody InventoryTypeXrefRsrc resource) {
        log.debug(" >> createInventoryTypeXref");

        try {
            InventoryTypeXrefRsrc newResource = inventoryTypeXrefService.createInventoryTypeXref(resource);
            return ResponseEntity.status(201).body(newResource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while creating Inventory Type Xref", e);
            return internalServerError();
        }
    }

    @PutMapping("/{agristabilityCommodityXrefId}")
    @Operation(
            operationId = "Update Inventory Type Xref resource.",
            summary = "Update Inventory Type Xref resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = InventoryTypeXrefRsrc.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<InventoryTypeXrefRsrc> updateInventoryTypeXref(
            @PathVariable Long agristabilityCommodityXrefId,
            @Valid @RequestBody InventoryTypeXrefRsrc resource) {
        log.debug(" >> updateInventoryTypeXref");

        try {
            InventoryTypeXrefRsrc updatedResource = inventoryTypeXrefService.updateInventoryTypeXref(agristabilityCommodityXrefId, resource);
            return ok(updatedResource);
        } catch (NotFoundException e) {
            log.warn(" ### Inventory Type Xref not found for update: {}", agristabilityCommodityXrefId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while updating Inventory Type Xref", e);
            return internalServerError();
        }
    }

    @DeleteMapping("/{agristabilityCommodityXrefId}")
    @Operation(
            operationId = "Delete Inventory Type Xref resource.",
            summary = "Delete Inventory Type Xref resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<Void> deleteInventoryTypeXref(
            @PathVariable Long agristabilityCommodityXrefId) {
        log.debug(" >> deleteInventoryTypeXref");

        try {
            inventoryTypeXrefService.deleteInventoryTypeXref(agristabilityCommodityXrefId);
            return noContent();
        } catch (NotFoundException e) {
            log.warn(" ### Inventory Type Xref for deletion not found: {}", agristabilityCommodityXrefId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while deleting Inventory Type Xref", e);
            return internalServerError();
        }
    }
}
