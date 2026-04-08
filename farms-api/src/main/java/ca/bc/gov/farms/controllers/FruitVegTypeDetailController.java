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
import ca.bc.gov.farms.data.models.FruitVegTypeDetailListModel;
import ca.bc.gov.farms.data.models.FruitVegTypeDetailModel;
import ca.bc.gov.farms.services.FruitVegTypeDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/fruitVegTypeDetails")
public class FruitVegTypeDetailController extends CommonController {

    protected FruitVegTypeDetailController() {
        super(FruitVegTypeDetailController.class.getName());
    }

    @Autowired
    private FruitVegTypeDetailService fruitVegTypeDetailService;

    @GetMapping
    @Operation(
            operationId = "Get all Fruit and Vegetable Type Detail resources.",
            summary = "Get all Fruit and Vegetable Type Detail resources."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = FruitVegTypeDetailListModel.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<FruitVegTypeDetailListModel> getAllFruitVegTypeDetails() {
        log.debug(" >> getAllFruitVegTypeDetails");

        try {
            FruitVegTypeDetailListModel resources = fruitVegTypeDetailService.getAllFruitVegTypeDetails();
            return ok(resources);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Fruit Veg Type Details", e);
            return internalServerError();
        }
    }

    @GetMapping("/{fruitVegTypeCode}")
    @Operation(
            operationId = "Get Fruit and Vegetable Type Detail resource by Id.",
            summary = "Get Fruit and Vegetable Type Detail resource by Id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = FruitVegTypeDetailModel.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<FruitVegTypeDetailModel> getFruitVegTypeDetail(
            @PathVariable String fruitVegTypeCode) {
        log.debug(" >> getFruitVegTypeDetail: {}", fruitVegTypeCode);

        try {
            FruitVegTypeDetailModel resource = fruitVegTypeDetailService.getFruitVegTypeDetail(fruitVegTypeCode);
            return ok(resource);
        } catch (NotFoundException e) {
            log.warn(" ### Fruit Veg Type Detail not found: {}", fruitVegTypeCode, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Fruit Veg Type Detail", e);
            return internalServerError();
        }
    }

    @PostMapping
    @Operation(
            operationId = "Create Fruit and Vegetable Type Detail resource.",
            summary = "Create Fruit and Vegetable Type Detail resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = FruitVegTypeDetailModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<FruitVegTypeDetailModel> createFruitVegTypeDetail(
            @Valid @RequestBody FruitVegTypeDetailModel resource) {
        log.debug(" >> createFruitVegTypeDetail");

        try {
            FruitVegTypeDetailModel newResource = fruitVegTypeDetailService.createFruitVegTypeDetail(resource);
            return ResponseEntity.status(201).body(newResource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while creating Fruit Veg Type Detail", e);
            return internalServerError();
        }
    }

    @PutMapping("/{fruitVegTypeCode}")
    @Operation(
            operationId = "Update Fruit and Vegetable Type Detail resource.",
            summary = "Update Fruit and Vegetable Type Detail resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = FruitVegTypeDetailModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<FruitVegTypeDetailModel> updateFruitVegTypeDetail(
            @PathVariable String fruitVegTypeCode,
            @Valid @RequestBody FruitVegTypeDetailModel resource) {
        log.debug(" >> updateFruitVegTypeDetail");

        try {
            FruitVegTypeDetailModel updatedResource = fruitVegTypeDetailService.updateFruitVegTypeDetail(fruitVegTypeCode, resource);
            return ok(updatedResource);
        } catch (NotFoundException e) {
            log.warn(" ### Fruit Veg Type Detail not found for update: {}", fruitVegTypeCode, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while updating Fruit Veg Type Detail", e);
            return internalServerError();
        }
    }

    @DeleteMapping("/{fruitVegTypeCode}")
    @Operation(
            operationId = "Delete Fruit and Vegetable Type Detail resource.",
            summary = "Delete Fruit and Vegetable Type Detail resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<Void> deleteFruitVegTypeDetail(
            @PathVariable String fruitVegTypeCode) {
        log.debug(" >> deleteFruitVegTypeDetail");

        try {
            fruitVegTypeDetailService.deleteFruitVegTypeDetail(fruitVegTypeCode);
            return noContent();
        } catch (NotFoundException e) {
            log.warn(" ### Fruit Veg Type Detail for deletion not found: {}", fruitVegTypeCode, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while deleting Fruit Veg Type Detail", e);
            return internalServerError();
        }
    }
}
