package ca.bc.gov.farms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.farms.common.controllers.CommonController;
import ca.bc.gov.farms.data.models.CodeRsrc;
import ca.bc.gov.farms.data.models.CodeTableModel;
import ca.bc.gov.farms.services.CodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/codeTables/{codeTableName}")
public class CodeTableController extends CommonController {

    protected CodeTableController() {
        super(CodeTableController.class.getName());
    }

    @Autowired
    private CodeService codeService;

    @GetMapping
    @Operation(
            operationId = "Get Code Table resource by code table name.",
            summary = "Get Code Table resource by code table name."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = CodeRsrc.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = MessageListRsrc.class)))
    })
    public ResponseEntity<CodeTableModel> getCodeTable(
            @PathVariable String codeTableName) {
        log.debug(" >> getCodeTable: {}", codeTableName);

        try {
            CodeTableModel resource = codeService.getCodeTable(codeTableName);
            return ok(resource);
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while fetching Code Table", e);
            return internalServerError();
        }
    }
}
