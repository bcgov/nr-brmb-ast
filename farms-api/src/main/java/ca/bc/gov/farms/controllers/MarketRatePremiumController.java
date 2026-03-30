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
import ca.bc.gov.farms.data.models.MarketRatePremiumListModel;
import ca.bc.gov.farms.data.models.MarketRatePremiumModel;
import ca.bc.gov.farms.services.MarketRatePremiumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/marketRatePremiums")
public class MarketRatePremiumController extends CommonController {

    protected MarketRatePremiumController() {
        super(MarketRatePremiumController.class.getName());
    }

    @Autowired
    private MarketRatePremiumService marketRatePremiumService;

    @GetMapping
    @Operation(
            operationId = "Get all Market Rate Premium resources.",
            summary = "Get all Market Rate Premium resources."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = MarketRatePremiumListModel.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<MarketRatePremiumListModel> getAllMarketRatePremiums() {
        log.debug(" >> getAllMarketRatePremiums");

        try {
            MarketRatePremiumListModel resources = marketRatePremiumService.getAllMarketRatePremiums();
            return ok(resources);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Market Rate Premiums", e);
            return internalServerError();
        }
    }

    @GetMapping("/{marketRatePremiumId}")
    @Operation(
            operationId = "Get Market Rate Premium resource by Market Rate Premium Id.",
            summary = "Get Market Rate Premium resource by Market Rate Premium Id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = MarketRatePremiumModel.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<MarketRatePremiumModel> getMarketRatePremium(
            @PathVariable Long marketRatePremiumId) {
        log.debug(" >> getMarketRatePremium: {}", marketRatePremiumId);

        try {
            MarketRatePremiumModel resource = marketRatePremiumService.getMarketRatePremium(marketRatePremiumId);
            return ok(resource);
        } catch (NotFoundException e) {
            log.warn(" ### Market Rate Premium not found: {}", marketRatePremiumId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Market Rate Premium", e);
            return internalServerError();
        }
    }

    @PostMapping
    @Operation(
            operationId = "Create Market Rate Premium resource.",
            summary = "Create Market Rate Premium resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = MarketRatePremiumModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<MarketRatePremiumModel> createMarketRatePremium(
            @Valid @RequestBody MarketRatePremiumModel resource) {
        log.debug(" >> createMarketRatePremium");

        try {
            MarketRatePremiumModel newResource = marketRatePremiumService.createMarketRatePremium(resource);
            return ResponseEntity.status(201).body(newResource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while creating Market Rate Premium", e);
            return internalServerError();
        }
    }

    @PutMapping("/{marketRatePremiumId}")
    @Operation(
            operationId = "Update Market Rate Premium resource.",
            summary = "Update Market Rate Premium resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = MarketRatePremiumModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<MarketRatePremiumModel> updateMarketRatePremium(
            @PathVariable Long marketRatePremiumId,
            @Valid @RequestBody MarketRatePremiumModel resource) {
        log.debug(" >> updateMarketRatePremium");

        try {
            MarketRatePremiumModel updatedResource = marketRatePremiumService.updateMarketRatePremium(marketRatePremiumId, resource);
            return ok(updatedResource);
        } catch (NotFoundException e) {
            log.warn(" ### Market Rate Premium not found for update: {}", marketRatePremiumId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while updating Market Rate Premium", e);
            return internalServerError();
        }
    }

    @DeleteMapping("/{marketRatePremiumId}")
    @Operation(
            operationId = "Delete Market Rate Premium resource.",
            summary = "Delete Market Rate Premium resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<MarketRatePremiumModel> deleteMarketRatePremium(
            @PathVariable Long marketRatePremiumId) {
        log.debug(" >> deleteMarketRatePremium");

        try {
            marketRatePremiumService.deleteMarketRatePremium(marketRatePremiumId);
            return noContent();
        } catch (NotFoundException e) {
            log.warn(" ### Market Rate Premium for deletion not found: {}", marketRatePremiumId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while deleting Market Rate Premium", e);
            return internalServerError();
        }
    }
}
