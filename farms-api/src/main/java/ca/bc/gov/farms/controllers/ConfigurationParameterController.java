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
import ca.bc.gov.farms.data.models.ConfigurationParameterListModel;
import ca.bc.gov.farms.data.models.ConfigurationParameterModel;
import ca.bc.gov.farms.services.ConfigurationParameterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/configurationParameters")
public class ConfigurationParameterController extends CommonController {

    protected ConfigurationParameterController() {
        super(ConfigurationParameterController.class.getName());
    }

    @Autowired
    private ConfigurationParameterService configurationParameterService;

    @GetMapping
    @Operation(
            operationId = "Get all Configuration Parameter resources.",
            summary = "Get all Configuration Parameter resources."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ConfigurationParameterListModel.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<ConfigurationParameterListModel> getAllConfigurationParameters(
            @RequestParam String parameterNamePrefix) {
        log.debug(" >> getAllConfigurationParameters: {}", parameterNamePrefix);

        ConfigurationParameterListModel resource = null;
        try {
            if (StringUtils.isBlank(parameterNamePrefix)) {
                resource = configurationParameterService.getAllConfigurationParameters();
            } else {
                resource = configurationParameterService.getConfigurationParametersByParameterNamePrefix(parameterNamePrefix);
            }
            return ok(resource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Configuration Parameters", e);
            return internalServerError();
        }
    }

    @GetMapping("/{configurationParameterId}")
    @Operation(
            operationId = "Get Configuration Parameter resource by Configuration Parameter Id.",
            summary = "Get Configuration Parameter resource by Configuration Parameter Id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ConfigurationParameterModel.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<ConfigurationParameterModel> getConfigurationParameter(
            @PathVariable Long configurationParameterId) {
        log.debug(" >> getConfigurationParameter: {}", configurationParameterId);

        try {
            ConfigurationParameterModel resource = configurationParameterService.getConfigurationParameter(configurationParameterId);
            return ok(resource);
        } catch (NotFoundException e) {
            log.warn(" ### Configuration Parameter not found: {}", configurationParameterId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Configuration Parameter", e);
            return internalServerError();
        }
    }

    @PostMapping
    @Operation(
            operationId = "Create Configuration Parameter resource.",
            summary = "Create Configuration Parameter resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = ConfigurationParameterModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<ConfigurationParameterModel> createConfigurationParameter(
            @Valid @RequestBody ConfigurationParameterModel resource) {
        log.debug(" >> createConfigurationParameter");

        try {
            ConfigurationParameterModel newResource = configurationParameterService.createConfigurationParameter(resource);
            return ResponseEntity.status(201).body(newResource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while creating Configuration Parameter", e);
            return internalServerError();
        }
    }

    @PutMapping("/{configurationParameterId}")
    @Operation(
            operationId = "Update Configuration Parameter resource.",
            summary = "Update Configuration Parameter resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ConfigurationParameterModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<ConfigurationParameterModel> updateConfigurationParameter(
            @PathVariable Long configurationParameterId,
            @Valid @RequestBody ConfigurationParameterModel resource) {
        log.debug(" >> updateConfigurationParameter");

        try {
            ConfigurationParameterModel updatedResource = configurationParameterService.updateConfigurationParameter(configurationParameterId, resource);
            return ok(updatedResource);
        } catch (NotFoundException e) {
            log.warn(" ### Configuration Parameter not found for update: {}", configurationParameterId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while updating Configuration Parameter", e);
            return internalServerError();
        }
    }

    @DeleteMapping("/{configurationParameterId}")
    @Operation(
            operationId = "Delete Configuration Parameter resource.",
            summary = "Delete Configuration Parameter resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<Void> deleteConfigurationParameter(
            @PathVariable Long configurationParameterId) {
        log.debug(" >> deleteConfigurationParameter");

        try {
            configurationParameterService.deleteConfigurationParameter(configurationParameterId);
            return noContent();
        } catch (NotFoundException e) {
            log.warn(" ### Configuration Parameter for deletion not found: {}", configurationParameterId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while deleting Configuration Parameter", e);
            return internalServerError();
        }
    }
}
