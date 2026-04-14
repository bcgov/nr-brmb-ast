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
import ca.bc.gov.farms.data.models.BenchmarkPerUnitListRsrc;
import ca.bc.gov.farms.data.models.BenchmarkPerUnitRsrc;
import ca.bc.gov.farms.services.BenchmarkPerUnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/benchmarkPerUnits")
public class BenchmarkPerUnitController extends CommonController {

    protected BenchmarkPerUnitController() {
        super(BenchmarkPerUnitController.class.getName());
    }

    @Autowired
    private BenchmarkPerUnitService benchmarkPerUnitService;

    @GetMapping
    @Operation(
            operationId = "Get Benchmark Per Unit resources by Program Year.",
            summary = "Get Benchmark Per Unit resources by Program Year."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = BenchmarkPerUnitListRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<BenchmarkPerUnitListRsrc> getBenchmarkPerUnitsByProgramYear(
            @RequestParam Integer programYear) {
        log.debug(" >> getBenchmarkPerUnitsByProgramYear: {}", programYear);

        try {
            BenchmarkPerUnitListRsrc resource = benchmarkPerUnitService.getBenchmarkPerUnitsByProgramYear(programYear);
            return ok(resource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Benchmark Per Units", e);
            return internalServerError();
        }
    }

    @GetMapping("/{benchmarkPerUnitId}")
    @Operation(
            operationId = "Get Benchmark Per Unit resource by Benchmark Per Unit Id.",
            summary = "Get Benchmark Per Unit resource by Benchmark Per Unit Id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = BenchmarkPerUnitRsrc.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<BenchmarkPerUnitRsrc> getBenchmarkPerUnit(
            @PathVariable Long benchmarkPerUnitId) {
        log.debug(" >> getBenchmarkPerUnit: {}", benchmarkPerUnitId);

        try {
            BenchmarkPerUnitRsrc resource = benchmarkPerUnitService.getBenchmarkPerUnit(benchmarkPerUnitId);
            return ok(resource);
        } catch (NotFoundException e) {
            log.warn(" ### Benchmark Per Unit not found: {}", benchmarkPerUnitId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Benchmark Per Unit", e);
            return internalServerError();
        }
    }

    @PostMapping
    @Operation(
            operationId = "Create Benchmark Per Unit resource.",
            summary = "Create Benchmark Per Unit resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = BenchmarkPerUnitRsrc.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<BenchmarkPerUnitRsrc> createBenchmarkPerUnit(
            @Valid @RequestBody BenchmarkPerUnitRsrc resource) {
        log.debug(" >> createBenchmarkPerUnit");

        try {
            BenchmarkPerUnitRsrc newResource = benchmarkPerUnitService.createBenchmarkPerUnit(resource);
            return ResponseEntity.status(201).body(newResource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while creating Benchmark Per Unit", e);
            return internalServerError();
        }
    }

    @PutMapping("/{benchmarkPerUnitId}")
    @Operation(
            operationId = "Update Benchmark Per Unit resource.",
            summary = "Update Benchmark Per Unit resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = BenchmarkPerUnitRsrc.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<BenchmarkPerUnitRsrc> updateBenchmarkPerUnit(
            @PathVariable Long benchmarkPerUnitId,
            @Valid @RequestBody BenchmarkPerUnitRsrc resource) {
        log.debug(" >> updateBenchmarkPerUnit");

        try {
            BenchmarkPerUnitRsrc updatedResource = benchmarkPerUnitService.updateBenchmarkPerUnit(benchmarkPerUnitId, resource);
            return ok(updatedResource);
        } catch (NotFoundException e) {
            log.warn(" ### Benchmark Per Unit not found for update: {}", benchmarkPerUnitId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while updating Benchmark Per Unit", e);
            return internalServerError();
        }
    }

    @DeleteMapping("/{benchmarkPerUnitId}")
    @Operation(
            operationId = "Delete Benchmark Per Unit resource.",
            summary = "Delete Benchmark Per Unit resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<Void> deleteBenchmarkPerUnit(
            @PathVariable Long benchmarkPerUnitId) {
        log.debug(" >> deleteBenchmarkPerUnit");

        try {
            benchmarkPerUnitService.deleteBenchmarkPerUnit(benchmarkPerUnitId);
            return noContent();
        } catch (NotFoundException e) {
            log.warn(" ### Benchmark Per Unit for deletion not found: {}", benchmarkPerUnitId, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while deleting Benchmark Per Unit", e);
            return internalServerError();
        }
    }
}
