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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.farms.common.controllers.CommonController;
import ca.bc.gov.farms.data.models.LineItemListRsrc;
import ca.bc.gov.farms.data.models.LineItemModel;
import ca.bc.gov.farms.services.LineItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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
                    content = @Content(schema = @Schema(implementation = LineItemListRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<LineItemListRsrc> getLineItemsByProgramYear(
            @RequestParam Integer programYear) {
        log.debug(" >> getLineItemsByProgramYear: {}", programYear);

        try {
            LineItemListRsrc resource = lineItemService.getLineItemsByProgramYear(programYear);
            return ok(resource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Line Items", e);
            return internalServerError();
        }
    }

    @GetMapping("/{lineItemId:\\d+}")
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

    @PostMapping
    @Operation(
            operationId = "Create Line Item resource.",
            summary = "Create Line Item resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = LineItemModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<LineItemModel> createLineItem(
            @Valid @RequestBody LineItemModel resource) {
        log.debug(" >> createLineItem");

        try {
            LineItemModel newResource = lineItemService.createLineItem(resource);
            return ResponseEntity.status(201).body(newResource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while creating Line Item", e);
            return internalServerError();
        }
    }

    @PutMapping("/{lineItemId:\\d+}")
    @Operation(
            operationId = "Update Line Item resource.",
            summary = "Update Line Item resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = LineItemModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<LineItemModel> updateLineItem(
            @PathVariable Long lineItemId,
            @Valid @RequestBody LineItemModel resource) {
        log.debug(" >> updateLineItem");

        try {
            LineItemModel updatedResource = lineItemService.updateLineItem(lineItemId, resource);
            return ok(updatedResource);
        } catch (NotFoundException e) {
            log.warn(" ### Line Item not found for update: {}", lineItemId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while updating Line Item", e);
            return internalServerError();
        }
    }

    @DeleteMapping("/{lineItemId:\\d+}")
    @Operation(
            operationId = "Delete Line Item resource.",
            summary = "Delete Line Item resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<Void> deleteLineItem(
            @PathVariable Long lineItemId) {
        log.debug(" >> deleteLineItem");

        try {
            lineItemService.deleteLineItem(lineItemId);
            return noContent();
        } catch (NotFoundException e) {
            log.warn(" ### Line Item for deletion not found: {}", lineItemId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while deleting Line Item", e);
            return internalServerError();
        }
    }

    @PostMapping("/copy/{currentYear}")
    @Operation(
            operationId = "Copy Line Item resources.",
            summary = "Copy Line Item resources."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = LineItemListRsrc.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<LineItemListRsrc> copyLineItems(
            @PathVariable Integer currentYear) {
        log.debug(" >> copyLineItems");

        try {
            LineItemListRsrc newResources = lineItemService.copyLineItems(currentYear);
            return ok(newResources);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while copying Line Items", e);
            return internalServerError();
        }
    }
}
