package ca.bc.gov.farms.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.farms.common.controllers.CommonController;
import ca.bc.gov.farms.data.models.ScenarioBenefitListRsrc;
import ca.bc.gov.farms.services.ScenarioBenefitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/calculations")
@Slf4j
public class ScenarioBenefitController extends CommonController {

    private final ScenarioBenefitService service;

    public ScenarioBenefitController(ScenarioBenefitService service) {
        super(ScenarioBenefitController.class.getName());
        this.service = service;
    }

    @GetMapping("/scenario-benefits")
    @Operation(operationId = "getScenarioBenefits", summary = "Get scenario benefits by participant PIN, program year and scenario number.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = ScenarioBenefitListRsrc.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    public ResponseEntity<ScenarioBenefitListRsrc> getScenarioBenefits(
            @RequestParam("participantPin") Integer participantPin,
            @RequestParam("programYear") Integer programYear,
            @RequestParam("scenarioNumber") Integer scenarioNumber) {
        try {
            return ok(service.getScenarioBenefits(participantPin, programYear, scenarioNumber));
        } catch (RuntimeException e) {
            log.error("Error fetching scenario benefits", e);
            return internalServerError();
        }
    }
}
