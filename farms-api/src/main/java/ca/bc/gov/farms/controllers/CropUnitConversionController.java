package ca.bc.gov.farms.controllers;

import org.apache.commons.lang3.StringUtils;
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
import ca.bc.gov.farms.data.models.CropUnitConversionListRsrc;
import ca.bc.gov.farms.data.models.CropUnitConversionRsrc;
import ca.bc.gov.farms.services.CropUnitConversionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/cropUnitConversions")
public class CropUnitConversionController extends CommonController {

    protected CropUnitConversionController() {
        super(CropUnitConversionController.class.getName());
    }

    @Autowired
    private CropUnitConversionService cropUnitConversionService;

    @GetMapping
    @Operation(
            operationId = "Get all Crop Unit Conversion resources.",
            summary = "Get all Crop Unit Conversion resources."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = CropUnitConversionListRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<CropUnitConversionListRsrc> getAllCropUnitConversions(
            @RequestParam(required = false) String inventoryItemCode) {
        log.debug(" >> getAllCropUnitConversions");

        CropUnitConversionListRsrc resources = null;
        try {
            if (StringUtils.isBlank(inventoryItemCode)) {
                resources = cropUnitConversionService.getAllCropUnitConversions();
            } else {
                resources = cropUnitConversionService.getCropUnitConversionsByInventoryItemCode(inventoryItemCode);
            }
            return ok(resources);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Crop Unit Conversions", e);
            return internalServerError();
        }
    }

    @GetMapping("/{cropUnitDefaultId}")
    @Operation(
            operationId = "Get Crop Unit Conversion resource by Crop Unit Conversion Factor Id.",
            summary = "Get Crop Unit Conversion resource by Crop Unit Conversion Factor Id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = CropUnitConversionRsrc.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<CropUnitConversionRsrc> getCropUnitConversion(
            @PathVariable Long cropUnitDefaultId) {
        log.debug(" >> getCropUnitConversion: {}", cropUnitDefaultId);

        try {
            CropUnitConversionRsrc resource = cropUnitConversionService.getCropUnitConversion(cropUnitDefaultId);
            return ok(resource);
        } catch (NotFoundException e) {
            log.warn(" ### Crop Unit Conversion not found: {}", cropUnitDefaultId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Crop Unit Conversion", e);
            return internalServerError();
        }
    }

    @PostMapping
    @Operation(
            operationId = "Create Crop Unit Conversion resource.",
            summary = "Create Crop Unit Conversion resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = CropUnitConversionRsrc.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<CropUnitConversionRsrc> createCropUnitConversion(
            @Valid @RequestBody CropUnitConversionRsrc resource) {
        log.debug(" >> createCropUnitConversion");

        try {
            CropUnitConversionRsrc newResource = cropUnitConversionService.createCropUnitConversion(resource);
            return ResponseEntity.status(201).body(newResource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while creating Crop Unit Conversion", e);
            return internalServerError();
        }
    }

    @PutMapping("/{cropUnitDefaultId}")
    @Operation(
            operationId = "Update Crop Unit Conversion resource.",
            summary = "Update Crop Unit Conversion resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = CropUnitConversionRsrc.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<CropUnitConversionRsrc> updateCropUnitConversion(
            @PathVariable Long cropUnitDefaultId,
            @Valid @RequestBody CropUnitConversionRsrc resource) {
        log.debug(" >> updateCropUnitConversion");

        try {
            CropUnitConversionRsrc updatedResource = cropUnitConversionService.updateCropUnitConversion(cropUnitDefaultId, resource);
            return ok(updatedResource);
        } catch (NotFoundException e) {
            log.warn(" ### Crop Unit Conversion not found for update: {}", cropUnitDefaultId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while updating Crop Unit Conversion", e);
            return internalServerError();
        }
    }

    @DeleteMapping("/{cropUnitDefaultId}")
    @Operation(
            operationId = "Delete Crop Unit Conversion resource.",
            summary = "Delete Crop Unit Conversion resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<Void> deleteCropUnitConversion(
            @PathVariable Long cropUnitDefaultId) {
        log.debug(" >> deleteCropUnitConversion");

        try {
            cropUnitConversionService.deleteCropUnitConversion(cropUnitDefaultId);
            return noContent();
        } catch (NotFoundException e) {
            log.warn(" ### Crop Unit Conversion for deletion not found: {}", cropUnitDefaultId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while deleting Crop Unit Conversion", e);
            return internalServerError();
        }
    }
}
