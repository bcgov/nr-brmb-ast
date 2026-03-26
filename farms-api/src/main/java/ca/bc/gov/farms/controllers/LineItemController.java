package ca.bc.gov.farms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.farms.common.controllers.CommonController;
import ca.bc.gov.farms.data.models.LineItemListModel;
import ca.bc.gov.farms.data.models.LineItemModel;
import ca.bc.gov.farms.services.LineItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/lineItems")
public class LineItemController extends CommonController {

    protected LineItemController() {
        super(LineItemController.class.getName());
    }

    @Autowired
    private LineItemService lineItemService;

    @GetMapping
    @Operation(
            operationId = "Get Line Item resources by Program Year.",
            summary = "Get Line Item resources by Program Year."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = LineItemListModel.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<LineItemListModel> getLineItemsByProgramYear(
            @RequestParam Integer programYear) {
        log.debug(" >> getLineItemsByProgramYear: {}", programYear);

        try {
            LineItemListModel resource = lineItemService.getLineItemsByProgramYear(programYear);
            return ok(resource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Line Items", e);
            return internalServerError();
        }
    }

    @GetMapping("/{lineItemId:\\\\d+}")
    @Operation(
            operationId = "Get Line Item resource by Line Item Id.",
            summary = "Get Line Item resource by Line Item Id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = LineItemModel.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<LineItemModel> getLineItem(
            @PathVariable Long lineItemId) {
        log.debug(" >> getLineItem: {}", lineItemId);

        try {
            LineItemModel resource = lineItemService.getLineItem(lineItemId);
            return ok(resource);
        } catch (NotFoundException e) {
            log.warn(" ### Line Item not found: {}", lineItemId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Line Item", e);
            return internalServerError();
        }
    }
}
