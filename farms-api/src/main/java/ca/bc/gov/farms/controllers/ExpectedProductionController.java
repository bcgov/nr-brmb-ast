package ca.bc.gov.farms.controllers;

import org.apache.commons.lang3.StringUtils;
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
import ca.bc.gov.farms.data.models.ExpectedProductionListModel;
import ca.bc.gov.farms.data.models.ExpectedProductionModel;
import ca.bc.gov.farms.services.ExpectedProductionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/expectedProductions")
public class ExpectedProductionController extends CommonController {

    protected ExpectedProductionController() {
        super(ExpectedProductionController.class.getName());
    }

    @Autowired
    private ExpectedProductionService expectedProductionService;

    @GetMapping
    @Operation(
            operationId = "Get all Expected Production resources.",
            summary = "Get all Expected Production resources."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ExpectedProductionListModel.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<ExpectedProductionListModel> getAllExpectedProductions(
            @RequestParam(required = false) String inventoryItemCode) {
        log.debug(" >> getAllExpectedProductions");

        ExpectedProductionListModel resources = null;
        try {
            if (StringUtils.isBlank(inventoryItemCode)) {
                resources = expectedProductionService.getAllExpectedProductions();
            } else {
                resources = expectedProductionService.getExpectedProductionByInventoryItemCode(inventoryItemCode);
            }
            return ok(resources);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Expected Productions", e);
            return internalServerError();
        }
    }

    @GetMapping("/{expectedProductionId}")
    @Operation(
            operationId = "Get Expected Production resource by Expected Production Id.",
            summary = "Get Expected Production resource by Expected Production Id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ExpectedProductionModel.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<ExpectedProductionModel> getExpectedProduction(
            @PathVariable Long expectedProductionId) {
        log.debug(" >> getExpectedProduction: {}", expectedProductionId);

        try {
            ExpectedProductionModel resource = expectedProductionService.getExpectedProduction(expectedProductionId);
            return ok(resource);
        } catch (NotFoundException e) {
            log.warn(" ### Expected Production not found: {}", expectedProductionId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Expected Production", e);
            return internalServerError();
        }
    }

    @PostMapping
    @Operation(
            operationId = "Create Expected Production resource.",
            summary = "Create Expected Production resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = ExpectedProductionModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<ExpectedProductionModel> createExpectedProduction(
            @Valid @RequestBody ExpectedProductionModel resource) {
        log.debug(" >> createExpectedProduction");

        try {
            ExpectedProductionModel newResource = expectedProductionService.createExpectedProduction(resource);
            return ResponseEntity.status(201).body(newResource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while creating Expected Production", e);
            return internalServerError();
        }
    }

    @PutMapping("/{expectedProductionId}")
    @Operation(
            operationId = "Update Expected Production resource.",
            summary = "Update Expected Production resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ExpectedProductionModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<ExpectedProductionModel> updateExpectedProduction(
            @PathVariable Long expectedProductionId,
            @Valid @RequestBody ExpectedProductionModel resource) {
        log.debug(" >> updateExpectedProduction");

        try {
            ExpectedProductionModel updatedResource = expectedProductionService.updateExpectedProduction(expectedProductionId, resource);
            return ok(updatedResource);
        } catch (NotFoundException e) {
            log.warn(" ### Expected Production not found for update: {}", expectedProductionId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while updating Expected Production", e);
            return internalServerError();
        }
    }

    @DeleteMapping("/{expectedProductionId}")
    @Operation(
            operationId = "Delete Expected Production resource.",
            summary = "Delete Expected Production resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))) })
    public ResponseEntity<Void> deleteExpectedProduction(
            @PathVariable Long expectedProductionId) {
        log.debug(" >> deleteExpectedProduction");

        try {
            expectedProductionService.deleteExpectedProduction(expectedProductionId);
            return noContent();
        } catch (NotFoundException e) {
            log.warn(" ### Expected Production for deletion not found: {}", expectedProductionId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while deleting Expected Production", e);
            return internalServerError();
        }
    }
}
