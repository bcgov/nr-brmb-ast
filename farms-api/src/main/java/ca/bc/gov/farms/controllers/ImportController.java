package ca.bc.gov.farms.controllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.farms.common.controllers.CommonController;
import ca.bc.gov.farms.data.entities.ImportVersionEntity;
import ca.bc.gov.farms.services.ImportBPUService;
import ca.bc.gov.farms.services.ImportCRAService;
import ca.bc.gov.farms.services.ImportClassCodes;
import ca.bc.gov.farms.services.ImportFMVService;
import ca.bc.gov.farms.services.ImportIVPRService;
import ca.bc.gov.farms.services.ImportService;
import ca.bc.gov.farms.services.ImportStateCodes;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/import")
public class ImportController extends CommonController {

    protected ImportController() {
        super(ImportController.class.getName());
    }

    @Autowired
    private ImportService importService;

    @Autowired
    private ImportBPUService importBPUService;

    @Autowired
    private ImportCRAService importCRAService;

    @Autowired
    private ImportFMVService importFMVService;

    @Autowired
    private ImportIVPRService importIVPRService;

    @PostMapping("/bpu/{fileName}")
    public ResponseEntity<Void> importBPU(
            @PathVariable String fileName,
            @RequestBody byte[] fileContent) {
        log.debug(" >> importBPU");

        try {
            // Call the service layer to handle the import
            ImportVersionEntity importVersionDto = importService.createImportVersion(
                    ImportClassCodes.BPU, ImportStateCodes.SCHEDULED_FOR_STAGING, ImportClassCodes.BPU_DESCRIPTION,
                    fileName, fileContent, "UserId");

            Long importVersionId = importVersionDto.getImportVersionId();
            InputStream inputStream = new ByteArrayInputStream(fileContent);
            String userId = "UserId";
            importBPUService.importCSV(importVersionId, inputStream, userId);

            importBPUService.processImport(importVersionId, userId);

            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while importing BPU", e);
            return internalServerError();
        }
    }

    @PostMapping("/fmv/{fileName}")
    public ResponseEntity<Void> importFMV(
            @PathVariable String fileName,
            @RequestBody byte[] fileContent) {
        log.debug(" >> importFMV");

        try {
            // Call the service layer to handle the import
            ImportVersionEntity importVersionDto = importService.createImportVersion(
                    ImportClassCodes.FMV, ImportStateCodes.SCHEDULED_FOR_STAGING, ImportClassCodes.FMV_DESCRIPTION,
                    fileName, fileContent, "UserId");

            Long importVersionId = importVersionDto.getImportVersionId();
            InputStream inputStream = new ByteArrayInputStream(fileContent);
            String userId = "UserId";
            importFMVService.importCSV(importVersionId, inputStream, userId);

            importFMVService.processImport(importVersionId, userId);

            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while importing FMV", e);
            return internalServerError();
        }
    }

    @PostMapping("/ivpr/{fileName}")
    public ResponseEntity<Void> importIVPR(
            @PathVariable String fileName,
            @RequestBody byte[] fileContent) {
        log.debug(" >> importIVPR");

        try {
            // Call the service layer to handle the import
            ImportVersionEntity importVersionDto = importService.createImportVersion(
                    ImportClassCodes.IVPR, ImportStateCodes.SCHEDULED_FOR_STAGING, ImportClassCodes.IVPR_DESCRIPTION,
                    fileName, fileContent, "UserId");

            Long importVersionId = importVersionDto.getImportVersionId();
            InputStream inputStream = new ByteArrayInputStream(fileContent);
            String userId = "UserId";
            importIVPRService.importCSV(importVersionId, inputStream, userId);

            importIVPRService.processImport(importVersionId, userId);

            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while importing IVPR", e);
            return internalServerError();
        }
    }

    @PostMapping("/cra/{fileName}")
    public ResponseEntity<Void> importCRA(
            @PathVariable String fileName,
            @RequestBody byte[] fileContent) {
        log.debug(" >> importCRA");

        try {
            // Call the service layer to handle the import
            ImportVersionEntity importVersionDto = importService.createImportVersion(
                    ImportClassCodes.CRA, ImportStateCodes.SCHEDULED_FOR_STAGING, ImportClassCodes.CRA_DESCRIPTION,
                    fileName, fileContent, "UserId");

            Long importVersionId = importVersionDto.getImportVersionId();
            InputStream inputStream = new ByteArrayInputStream(fileContent);
            String userId = "UserId";
            importCRAService.importCSV(importVersionId, inputStream, userId);

            importCRAService.processImport(importVersionId, userId);

            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error(" ### RuntimeException while importing CRA", e);
            return internalServerError();
        }
    }
}
