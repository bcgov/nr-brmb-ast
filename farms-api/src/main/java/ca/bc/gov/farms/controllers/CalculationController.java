package ca.bc.gov.farms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.farms.common.controllers.CommonController;
import ca.bc.gov.farms.data.models.CalculationRsrc;
import ca.bc.gov.farms.data.models.EnrolmentCalculationRsrc;
import ca.bc.gov.farms.services.EnrolmentCalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/calculations")
public class CalculationController extends CommonController {

    protected CalculationController() {
        super(CalculationController.class.getName());
    }

    @Autowired
    private EnrolmentCalculationService enrolmentCalculationService;

    @GetMapping
    @Operation(
            operationId = "Get calculation test response.",
            summary = "Get calculation test response."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = CalculationRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<CalculationRsrc> getCalculation() {
        log.debug(" >> getCalculation");

        CalculationRsrc resource = CalculationRsrc.builder()
                .message("Hello world")
                .build();

        return ok(resource);
    }

    @GetMapping("/enrolment-notice-workflow")
    @Operation(
            operationId = "Get ENW enrolment calculation by participant PIN and program year.",
            summary = "Get ENW enrolment calculation by participant PIN and program year."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = EnrolmentCalculationRsrc.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<EnrolmentCalculationRsrc> getEnrolmentNoticeWorkflowCalculation(
            @RequestParam("participantPin") Integer participantPin,
            @RequestParam("programYear") Integer programYear) {
        log.debug(" >> getEnrolmentNoticeWorkflowCalculation: participantPin={}, programYear={}",
                participantPin, programYear);

        try {
            EnrolmentCalculationRsrc resource = enrolmentCalculationService
                    .getEnrolmentCalculation(participantPin, programYear);
            return ok(resource);
        } catch (IllegalArgumentException e) {
            log.warn(" ### Invalid ENW enrolment calculation request", e);
            return badRequest();
        } catch (NotFoundException e) {
            log.warn(" ### ENW enrolment calculation not found for participantPin={}, programYear={}",
                    participantPin, programYear, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching ENW enrolment calculation", e);
            return internalServerError();
        }
    }
}
