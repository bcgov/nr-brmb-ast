package ca.bc.gov.farms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.farms.common.controllers.CommonController;
import ca.bc.gov.farms.data.models.YearConfigurationParameterListModel;
import ca.bc.gov.farms.services.YearConfigurationParameterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
}
