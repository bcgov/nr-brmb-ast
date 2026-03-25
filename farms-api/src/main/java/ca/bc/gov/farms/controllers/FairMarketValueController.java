package ca.bc.gov.farms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.brmb.common.service.api.ConflictException;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.farms.common.controllers.CommonController;
import ca.bc.gov.farms.data.models.FairMarketValueListModel;
import ca.bc.gov.farms.data.models.FairMarketValueModel;
import ca.bc.gov.farms.services.FairMarketValueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/fairMarketValues")
public class FairMarketValueController extends CommonController {

    protected FairMarketValueController() {
        super(FairMarketValueController.class.getName());
    }

    @Autowired
    private FairMarketValueService fairMarketValueService;

    @GetMapping
    @Operation(
            operationId = "Get Fair Market Value resources by Program Year.",
            summary = "Get Fair Market Value resources by Program Year."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = FairMarketValueListModel.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<FairMarketValueListModel> getFairMarketValuesByProgramYear(
            @RequestParam Integer programYear) {
        log.debug(" >> getFairMarketValuesByProgramYear: {}", programYear);

        try {
            FairMarketValueListModel resource = fairMarketValueService.getFairMarketValuesByProgramYear(programYear);
            return ok(resource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Fair Market Values", e);
            return internalServerError();
        }
    }

    @GetMapping("/{fairMarketValueId}")
    @Operation(
            operationId = "Get Fair Market Value resource by Fair Market Value Id.",
            summary = "Get Fair Market Value resource by Fair Market Value Id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = FairMarketValueModel.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<FairMarketValueModel> getFairMarketValue(
            @PathVariable String fairMarketValueId) {
        log.debug(" >> getFairMarketValue: {}", fairMarketValueId);

        try {
            FairMarketValueModel resource = fairMarketValueService.getFairMarketValue(fairMarketValueId);
            return ok(resource);
        } catch (NotFoundException e) {
            log.warn(" ### Fair Market Value not found: {}", fairMarketValueId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Fair Market Value", e);
            return internalServerError();
        }
    }

    @PostMapping
    @Operation(
            operationId = "Create Fair Market Value resource.",
            summary = "Create Fair Market Value resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = FairMarketValueModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<FairMarketValueModel> createFairMarketValue(
            @Valid @RequestBody FairMarketValueModel resource) {
        log.debug(" >> createFairMarketValue");

        try {
            FairMarketValueModel newResource = fairMarketValueService.createFairMarketValue(resource);
            return ResponseEntity.status(201).body(newResource);
        } catch (ConflictException e) {
            log.warn(" ### Error while creating Fair Market Value", e);
            return conflict();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while creating Fair Market Value", e);
            return internalServerError();
        }
    }

    @PutMapping("/{fairMarketValueId}")
    @Operation(
            operationId = "Update Fair Market Value resource.",
            summary = "Update Fair Market Value resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = FairMarketValueModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<FairMarketValueModel> updateFairMarketValue(
            @PathVariable String fairMarketValueId,
            @Valid @RequestBody FairMarketValueModel resource) {
        log.debug(" >> updateFairMarketValue");

        try {
            FairMarketValueModel updatedResource = fairMarketValueService.updateFairMarketValue(fairMarketValueId, resource);
            return ok(updatedResource);
        } catch (NotFoundException e) {
            log.warn(" ### Fair Market Value not found for update: {}", fairMarketValueId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while updating Fair Market Value", e);
            return internalServerError();
        }
    }

    @DeleteMapping("/{fairMarketValueId}")
    @Operation(
            operationId = "Delete Fair Market Value resource.",
            summary = "Delete Fair Market Value resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<Void> deleteFairMarketValue(
            @PathVariable String fairMarketValueId) {
        log.debug(" >> deleteFairMarketValue");

        try {
            fairMarketValueService.deleteFairMarketValue(fairMarketValueId);
            return noContent();
        } catch (NotFoundException e) {
            log.warn(" ### Fair Market Value for deletion not found: {}", fairMarketValueId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while deleting Fair Market Value", e);
            return internalServerError();
        }
    }
}
