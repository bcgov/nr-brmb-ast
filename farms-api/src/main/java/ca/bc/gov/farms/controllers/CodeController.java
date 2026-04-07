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
import ca.bc.gov.farms.data.models.CodeModel;
import ca.bc.gov.farms.services.CodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/codeTables/{codeTableName}/codes")
public class CodeController extends CommonController {

    protected CodeController() {
        super(CodeController.class.getName());
    }

    @Autowired
    private CodeService codeService;

    @GetMapping("/{codeValue}")
    @Operation(
            operationId = "Get Code resource by code value.",
            summary = "Get Code resource by code value."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = CodeModel.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<CodeModel> getCode(
            @PathVariable String codeTableName,
            @PathVariable String codeValue) {
        log.debug(" >> getCode: {}", codeValue);

        try {
            CodeModel resource = codeService.getCode(codeTableName, codeValue);
            return ok(resource);
        } catch (NotFoundException e) {
            log.warn(" ### Code not found: {}", codeValue, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Code", e);
            return internalServerError();
        }
    }

    @PostMapping
    @Operation(
            operationId = "Create Code resource.",
            summary = "Create Code resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = CodeModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<CodeModel> createCode(
            @PathVariable String codeTableName,
            @Valid @RequestBody CodeModel resource) {
        log.debug(" >> createCode");

        try {
            CodeModel newResource = codeService.createCode(codeTableName, resource);
            return ResponseEntity.status(201).body(newResource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while creating Code", e);
            return internalServerError();
        }
    }

    @PutMapping("/{codeValue}")
    @Operation(
            operationId = "Update Code resource.",
            summary = "Update Code resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = CodeModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<CodeModel> updateCode(
            @PathVariable String codeTableName,
            @PathVariable String codeValue,
            @Valid @RequestBody CodeModel resource) {
        log.debug(" >> updateCode");

        try {
            CodeModel updatedResource = codeService.updateCode(codeTableName, codeValue, resource);
            return ok(updatedResource);
        } catch (NotFoundException e) {
            log.warn(" ### Code not found for update: {}", codeValue, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while updating Code", e);
            return internalServerError();
        }
    }

    @DeleteMapping("/{codeValue}")
    @Operation(
            operationId = "Delete Code resource.",
            summary = "Delete Code resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<Void> deleteCode(
            @PathVariable String codeTableName,
            @PathVariable String codeValue) {
        log.debug(" >> deleteCode");

        try {
            codeService.deleteCode(codeTableName, codeValue);
            return noContent();
        } catch (NotFoundException e) {
            log.warn(" ### Code for deletion not found: {}", codeValue, e);
            return notFound();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while deleting Code", e);
            return internalServerError();
        }
    }
}
