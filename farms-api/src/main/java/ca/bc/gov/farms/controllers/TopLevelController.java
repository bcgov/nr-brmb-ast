package ca.bc.gov.farms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.farms.common.controllers.CommonController;
import ca.bc.gov.farms.data.assemblers.TopLevelResourceAssembler;
import ca.bc.gov.farms.data.models.TopLevelModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/")
public class TopLevelController extends CommonController {

    protected TopLevelController() {
        super(TopLevelController.class.getName());
    }

    @Autowired
    private TopLevelResourceAssembler topLevelResourceAssembler;

    @GetMapping
    @Operation(
            operationId = "Get Top Level resource.",
            summary = "Get Top Level resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = TopLevelModel.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<TopLevelModel> getTopLevel() {
        log.debug(" >> getTopLevel");

        try {
            TopLevelModel resource = topLevelResourceAssembler.getTopLevel();
            return ok(resource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Top Level", e);
            return internalServerError();
        }
    }
}
