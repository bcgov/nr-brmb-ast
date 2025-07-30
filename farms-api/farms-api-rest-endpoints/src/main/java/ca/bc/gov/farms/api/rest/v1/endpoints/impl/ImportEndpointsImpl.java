package ca.bc.gov.farms.api.rest.v1.endpoints.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ca.bc.gov.farms.api.rest.v1.endpoints.ImportEndpoints;
import ca.bc.gov.farms.domain.codes.ImportClassCodes;
import ca.bc.gov.farms.domain.codes.ImportStateCodes;
import ca.bc.gov.farms.persistence.v1.dto.ImportVersionDto;
import ca.bc.gov.farms.service.api.v1.ImportBPUService;
import ca.bc.gov.farms.service.api.v1.ImportCRAService;
import ca.bc.gov.farms.service.api.v1.ImportFMVService;
import ca.bc.gov.farms.service.api.v1.ImportService;

public class ImportEndpointsImpl extends BaseEndpointsImpl implements ImportEndpoints {

    private static final Logger logger = LoggerFactory.getLogger(ImportEndpointsImpl.class);

    @Autowired
    private ImportService importService;

    @Autowired
    private ImportBPUService importBPUService;

    @Autowired
    private ImportCRAService importCRAService;

    @Autowired
    private ImportFMVService importFMVService;

    @Override
    public Response importBPU(String fileName, byte[] fileContent) throws Exception {
        logger.debug("<importBPU");

        Response response = null;

        logRequest();

        try {
            // Call the service layer to handle the import
            ImportVersionDto importVersionDto = importService.createImportVersion(
                    ImportClassCodes.BPU, ImportStateCodes.SCHEDULED_FOR_STAGING, ImportClassCodes.BPU_DESCRIPTION,
                    fileName, fileContent, "UserId");

            Long importVersionId = importVersionDto.getImportVersionId();
            InputStream inputStream = new ByteArrayInputStream(fileContent);
            String userId = "UserId";
            importBPUService.importCSV(importVersionId, inputStream, userId);

            importBPUService.processImport(importVersionId, userId);

            response = Response.ok().build();
        } catch (Exception e) {
            response = getInternalServerErrorResponse(e);
        }

        logResponse(response);

        logger.debug(">importBPU " + response);
        return response;
    }

    @Override
    public Response importCRA(String fileName, byte[] fileContent) throws Exception {
        logger.debug("<importCRA");

        Response response = null;

        logRequest();

        try {
            // Call the service layer to handle the import
            ImportVersionDto importVersionDto = importService.createImportVersion(
                    ImportClassCodes.CRA, ImportStateCodes.SCHEDULED_FOR_STAGING, ImportClassCodes.CRA_DESCRIPTION,
                    fileName, fileContent, "UserId");

            Long importVersionId = importVersionDto.getImportVersionId();
            InputStream inputStream = new ByteArrayInputStream(fileContent);
            String userId = "UserId";
            importCRAService.importCSV(importVersionId, inputStream, userId);

            importCRAService.processImport(importVersionId, userId);

            response = Response.ok().build();
        } catch (Exception e) {
            response = getInternalServerErrorResponse(e);
        }

        logResponse(response);

        logger.debug(">importCRA " + response);
        return response;

    }

    @Override
    public Response importFMV(String fileName, byte[] fileContent) throws Exception {
        logger.debug("<importFMV");

        Response response = null;

        logRequest();

        try {
            // Call the service layer to handle the import
            ImportVersionDto importVersionDto = importService.createImportVersion(
                    ImportClassCodes.FMV, ImportStateCodes.SCHEDULED_FOR_STAGING, ImportClassCodes.FMV_DESCRIPTION,
                    fileName, fileContent, "UserId");

            Long importVersionId = importVersionDto.getImportVersionId();
            InputStream inputStream = new ByteArrayInputStream(fileContent);
            String userId = "UserId";
            importFMVService.importCSV(importVersionId, inputStream, userId);

            importFMVService.processImport(importVersionId, userId);

            response = Response.ok().build();
        } catch (Exception e) {
            response = getInternalServerErrorResponse(e);
        }

        logResponse(response);

        logger.debug(">importFMV " + response);
        return response;
    }

}
