package ca.bc.gov.farms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.farms.common.controllers.CommonController;
import ca.bc.gov.farms.data.models.ProductiveUnitCodeListRsrc;
import ca.bc.gov.farms.services.ProductiveUnitCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/productiveUnitCodes")
public class ProductiveUnitCodeController extends CommonController {

    protected ProductiveUnitCodeController() {
        super(ProductiveUnitCodeController.class.getName());
    }

    @Autowired
    private ProductiveUnitCodeService productiveUnitCodeService;

    @GetMapping
    @Operation(
            operationId = "Get all Productive Unit Code resources.",
            summary = "Get all Productive Unit Code resources."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ProductiveUnitCodeListRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<ProductiveUnitCodeListRsrc> getAllProductiveUnitCodes() {
        log.debug(" >> getAllProductiveUnitCodes");

        try {
            ProductiveUnitCodeListRsrc resources = productiveUnitCodeService.getAllProductiveUnitCodes();
            return ok(resources);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Productive Unit Codes", e);
            return internalServerError();
        }
    }
}
