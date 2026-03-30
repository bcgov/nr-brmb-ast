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
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.farms.common.controllers.CommonController;
import ca.bc.gov.farms.data.models.YearConfigurationParameterListModel;
import ca.bc.gov.farms.data.models.YearConfigurationParameterModel;
import ca.bc.gov.farms.services.YearConfigurationParameterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/yearConfigurationParameters")
public class YearConfigurationParameterController extends CommonController {

    protected YearConfigurationParameterController() {
        super(YearConfigurationParameterController.class.getName());
    }

    @Autowired
    private YearConfigurationParameterService yearConfigurationParameterService;

    @GetMapping
    @Operation(operationId = "Get all Year Configuration Parameter resources.", summary = "Get all Year Configuration Parameter resources.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = YearConfigurationParameterListModel.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<YearConfigurationParameterListModel> getAllYearConfigurationParameters() {
        log.debug(" >> getAllYearConfigurationParameters");

        try {
            YearConfigurationParameterListModel resources = yearConfigurationParameterService
                    .getAllYearConfigurationParameters();
            return ok(resources);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Year Configuration Parameters", e);
            return internalServerError();
        }
    }

    @GetMapping("/{yearConfigurationParameterId}")
    @Operation(
            operationId = "Get Year Configuration Parameter resource by Year Configuration Parameter Id.",
            summary = "Get Year Configuration Parameter resource by Year Configuration Parameter Id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = YearConfigurationParameterModel.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<YearConfigurationParameterModel> getYearConfigurationParameter(
            @PathVariable Long yearConfigurationParameterId) {
        log.debug(" >> getYearConfigurationParameter: {}", yearConfigurationParameterId);

        try {
            YearConfigurationParameterModel resource = yearConfigurationParameterService.getYearConfigurationParameter(yearConfigurationParameterId);
            return ok(resource);
        } catch (NotFoundException e) {
            log.warn(" ### Year Configuration Parameter not found: {}", yearConfigurationParameterId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Year Configuration Parameter", e);
            return internalServerError();
        }
    }

    @PostMapping
    @Operation(
            operationId = "Create Year Configuration Parameter resource.",
            summary = "Create Year Configuration Parameter resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = YearConfigurationParameterModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<YearConfigurationParameterModel> createYearConfigurationParameter(
            @Valid @RequestBody YearConfigurationParameterModel resource) {
        log.debug(" >> createYearConfigurationParameter");

        try {
            YearConfigurationParameterModel newResource = yearConfigurationParameterService.createYearConfigurationParameter(resource);
            return ResponseEntity.status(201).body(newResource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while creating Year Configuration Parameter", e);
            return internalServerError();
        }
    }

    @PutMapping("/{yearConfigurationParameterId}")
    @Operation(
            operationId = "Update Year Configuration Parameter resource.",
            summary = "Update Year Configuration Parameter resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = YearConfigurationParameterModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<YearConfigurationParameterModel> updateYearConfigurationParameter(
            @PathVariable Long yearConfigurationParameterId,
            @Valid @RequestBody YearConfigurationParameterModel resource) {
        log.debug(" >> updateYearConfigurationParameter");

        try {
            YearConfigurationParameterModel updatedResource = yearConfigurationParameterService.updateYearConfigurationParameter(yearConfigurationParameterId, resource);
            return ok(updatedResource);
        } catch (NotFoundException e) {
            log.warn(" ### Year Configuration Parameter not found for update: {}", yearConfigurationParameterId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while updating Year Configuration Parameter", e);
            return internalServerError();
        }
    }

    @DeleteMapping("/{yearConfigurationParameterId}")
    @Operation(
            operationId = "Delete Year Configuration Parameter resource.",
            summary = "Delete Year Configuration Parameter resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<YearConfigurationParameterModel> deleteYearConfigurationParameter(
            @PathVariable Long yearConfigurationParameterId) {
        log.debug(" >> deleteYearConfigurationParameter");

        try {
            yearConfigurationParameterService.deleteYearConfigurationParameter(yearConfigurationParameterId);
            return noContent();
        } catch (NotFoundException e) {
            log.warn(" ### Year Configuration Parameter for deletion not found: {}", yearConfigurationParameterId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while deleting Year Configuration Parameter", e);
            return internalServerError();
        }
    }
}
