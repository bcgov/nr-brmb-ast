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
import ca.bc.gov.farms.data.models.InventoryItemAttributeModel;
import ca.bc.gov.farms.services.InventoryItemAttributeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/inventoryItemAttributes")
public class InventoryItemAttributeController extends CommonController {

    protected InventoryItemAttributeController() {
        super(InventoryItemAttributeController.class.getName());
    }

    @Autowired
    private InventoryItemAttributeService inventoryItemAttributeService;

    @GetMapping
    @Operation(
            operationId = "Get Inventory Item Attribute resource by Inventory Item Code.",
            summary = "Get Inventory Item Attribute resource by Inventory Item Code."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = InventoryItemAttributeModel.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<InventoryItemAttributeModel> getInventoryItemAttributeByInventoryItemCode(
            @RequestParam String inventoryItemCode) {
        log.debug(" >> getInventoryItemAttributeByInventoryItemCode: {}", inventoryItemCode);

        try {
            InventoryItemAttributeModel resource = inventoryItemAttributeService.getInventoryItemAttributeByInventoryItemCode(inventoryItemCode);
            return ok(resource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Inventory Item Attributes", e);
            return internalServerError();
        }
    }

    @GetMapping("/{inventoryItemAttributeId}")
    @Operation(
            operationId = "Get Inventory Item Attribute resource by Inventory Item Attribute Id.",
            summary = "Get Inventory Item Attribute resource by Inventory Item Attribute Id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = InventoryItemAttributeModel.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<InventoryItemAttributeModel> getInventoryItemAttribute(
            @PathVariable Long inventoryItemAttributeId) {
        log.debug(" >> getInventoryItemAttribute: {}", inventoryItemAttributeId);

        try {
            InventoryItemAttributeModel resource = inventoryItemAttributeService.getInventoryItemAttribute(inventoryItemAttributeId);
            return ok(resource);
        } catch (NotFoundException e) {
            log.warn(" ### Inventory Item Attribute not found: {}", inventoryItemAttributeId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Inventory Item Attribute", e);
            return internalServerError();
        }
    }

    @PostMapping
    @Operation(
            operationId = "Create Inventory Item Attribute resource.",
            summary = "Create Inventory Item Attribute resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = InventoryItemAttributeModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<InventoryItemAttributeModel> createInventoryItemAttribute(
            @Valid @RequestBody InventoryItemAttributeModel resource) {
        log.debug(" >> createInventoryItemAttribute");

        try {
            InventoryItemAttributeModel newResource = inventoryItemAttributeService.createInventoryItemAttribute(resource);
            return ResponseEntity.status(201).body(newResource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while creating Inventory Item Attribute", e);
            return internalServerError();
        }
    }

    @PutMapping("/{inventoryItemAttributeId}")
    @Operation(
            operationId = "Update Inventory Item Attribute resource.",
            summary = "Update Inventory Item Attribute resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = InventoryItemAttributeModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<InventoryItemAttributeModel> updateInventoryItemAttribute(
            @PathVariable Long inventoryItemAttributeId,
            @Valid @RequestBody InventoryItemAttributeModel resource) {
        log.debug(" >> updateInventoryItemAttribute");

        try {
            InventoryItemAttributeModel updatedResource = inventoryItemAttributeService.updateInventoryItemAttribute(inventoryItemAttributeId, resource);
            return ok(updatedResource);
        } catch (NotFoundException e) {
            log.warn(" ### Inventory Item Attribute not found for update: {}", inventoryItemAttributeId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while updating Inventory Item Attribute", e);
            return internalServerError();
        }
    }

    @DeleteMapping("/{inventoryItemAttributeId}")
    @Operation(
            operationId = "Delete Inventory Item Attribute resource.",
            summary = "Delete Inventory Item Attribute resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<Void> deleteInventoryItemAttribute(
            @PathVariable Long inventoryItemAttributeId) {
        log.debug(" >> deleteInventoryItemAttribute");

        try {
            inventoryItemAttributeService.deleteInventoryItemAttribute(inventoryItemAttributeId);
            return noContent();
        } catch (NotFoundException e) {
            log.warn(" ### Inventory Item Attribute for deletion not found: {}", inventoryItemAttributeId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while deleting Inventory Item Attribute", e);
            return internalServerError();
        }
    }
}
