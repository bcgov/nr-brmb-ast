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
import ca.bc.gov.farms.data.models.InventoryItemDetailListRsrc;
import ca.bc.gov.farms.data.models.InventoryItemDetailModel;
import ca.bc.gov.farms.services.InventoryItemDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/inventoryItemDetails")
public class InventoryItemDetailController extends CommonController {

    protected InventoryItemDetailController() {
        super(InventoryItemDetailController.class.getName());
    }

    @Autowired
    private InventoryItemDetailService inventoryItemDetailService;

    @GetMapping
    @Operation(
            operationId = "Get Inventory Item Detail resources by Inventory Item Code.",
            summary = "Get Inventory Item Detail resources by Inventory Item Code."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = InventoryItemDetailListRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<InventoryItemDetailListRsrc> getInventoryItemDetailsByInventoryItemCode(
            @RequestParam String inventoryItemCode) {
        log.debug(" >> getInventoryItemDetailsByInventoryItemCode: {}", inventoryItemCode);

        try {
            InventoryItemDetailListRsrc resource = inventoryItemDetailService.getInventoryItemDetailsByInventoryItemCode(inventoryItemCode);
            return ok(resource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Inventory Item Details", e);
            return internalServerError();
        }
    }

    @GetMapping("/{inventoryItemDetailId}")
    @Operation(
            operationId = "Get Inventory Item Detail resource by Inventory Item Detail Id.",
            summary = "Get Inventory Item Detail resource by Inventory Item Detail Id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = InventoryItemDetailModel.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<InventoryItemDetailModel> getInventoryItemDetail(
            @PathVariable Long inventoryItemDetailId) {
        log.debug(" >> getInventoryItemDetail: {}", inventoryItemDetailId);

        try {
            InventoryItemDetailModel resource = inventoryItemDetailService.getInventoryItemDetail(inventoryItemDetailId);
            return ok(resource);
        } catch (NotFoundException e) {
            log.warn(" ### Inventory Item Detail not found: {}", inventoryItemDetailId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Inventory Item Detail", e);
            return internalServerError();
        }
    }

    @PostMapping
    @Operation(
            operationId = "Create Inventory Item Detail resource.",
            summary = "Create Inventory Item Detail resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = InventoryItemDetailModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<InventoryItemDetailModel> createInventoryItemDetail(
            @Valid @RequestBody InventoryItemDetailModel resource) {
        log.debug(" >> createInventoryItemDetail");

        try {
            InventoryItemDetailModel newResource = inventoryItemDetailService.createInventoryItemDetail(resource);
            return ResponseEntity.status(201).body(newResource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while creating Inventory Item Detail", e);
            return internalServerError();
        }
    }

    @PutMapping("/{inventoryItemDetailId}")
    @Operation(
            operationId = "Update Inventory Item Detail resource.",
            summary = "Update Inventory Item Detail resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = InventoryItemDetailModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<InventoryItemDetailModel> updateInventoryItemDetail(
            @PathVariable Long inventoryItemDetailId,
            @Valid @RequestBody InventoryItemDetailModel resource) {
        log.debug(" >> updateInventoryItemDetail");

        try {
            InventoryItemDetailModel updatedResource = inventoryItemDetailService.updateInventoryItemDetail(inventoryItemDetailId, resource);
            return ok(updatedResource);
        } catch (NotFoundException e) {
            log.warn(" ### Inventory Item Detail not found for update: {}", inventoryItemDetailId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while updating Inventory Item Detail", e);
            return internalServerError();
        }
    }

    @DeleteMapping("/{inventoryItemDetailId}")
    @Operation(
            operationId = "Delete Inventory Item Detail resource.",
            summary = "Delete Inventory Item Detail resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<Void> deleteInventoryItemDetail(
            @PathVariable Long inventoryItemDetailId) {
        log.debug(" >> deleteInventoryItemDetail");

        try {
            inventoryItemDetailService.deleteInventoryItemDetail(inventoryItemDetailId);
            return noContent();
        } catch (NotFoundException e) {
            log.warn(" ### Inventory Item Detail for deletion not found: {}", inventoryItemDetailId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while deleting Inventory Item Detail", e);
            return internalServerError();
        }
    }
}
