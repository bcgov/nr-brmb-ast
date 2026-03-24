package ca.bc.gov.farms.common.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.brmb.common.checkhealth.CheckHealthValidator;
import ca.bc.gov.brmb.common.checkhealth.DependencyLoopException;
import ca.bc.gov.brmb.common.checkhealth.MissingCallstackException;
import ca.bc.gov.brmb.common.model.ValidationStatus;
import ca.bc.gov.brmb.common.rest.resource.HealthCheckResponseRsrc;
import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/checkHealth")
public class CheckHealthController extends CommonController {

    protected CheckHealthController() {
        super(CheckHealthController.class.getName());
    }

    @Autowired
    private CheckHealthValidator checkHealthValidator;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            operationId = "Query the health of the service.",
            summary = "Query the health of the service."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = HealthCheckResponseRsrc.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<HealthCheckResponseRsrc> checkHealth(
            @RequestParam String callstack) {
        log.debug(" >> checkHealth");

        try {
            HealthCheckResponseRsrc result = doCheckHealth(callstack);
            return ok(result);
        } catch (MissingCallstackException e) {
            return badRequest();
        } catch (DependencyLoopException e) {
            return noContent();
        } catch (Throwable t) {
            return internalServerError();
        }
    }

    private HealthCheckResponseRsrc doCheckHealth(String callstack)
            throws DependencyLoopException, MissingCallstackException {
        log.debug("<checkHealth");

        if(callstack==null||callstack.trim().length()==0) {
            throw new MissingCallstackException();
        }

        String componentIdentifier = checkHealthValidator.getComponentIdentifier().trim();
        String componentName = checkHealthValidator.getComponentName();

        String[] identifiers = callstack.split(",");
        for(String identifier:identifiers) {
            if(identifier.trim().equals(componentIdentifier)) {
                throw new DependencyLoopException();
            }
        }

        HealthCheckResponseRsrc result = null;

        try {

            result = checkHealthValidator.validate(callstack+","+componentIdentifier);

        } catch(Throwable t) {
            result = new HealthCheckResponseRsrc();
            result.setComponentIdentifier(componentIdentifier);
            result.setComponentName(componentName);
            result.setValidationStatus(ValidationStatus.RED);
            result.setStatusDetails("Failed to perform healthcheck due to unexpected error: "+t.getMessage());
        }

        log.debug(">checkHealth "+result);
        return result;
    }
}
