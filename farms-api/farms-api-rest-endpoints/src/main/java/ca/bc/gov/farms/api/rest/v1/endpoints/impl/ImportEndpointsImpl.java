package ca.bc.gov.farms.api.rest.v1.endpoints.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ca.bc.gov.farms.api.rest.v1.endpoints.ImportEndpoints;
import ca.bc.gov.farms.domain.codes.ImportClassCodes;
import ca.bc.gov.farms.domain.codes.ImportStateCodes;
import ca.bc.gov.farms.persistence.v1.dto.ImportVersionDto;
import ca.bc.gov.farms.service.api.v1.ImportBPUService;
import ca.bc.gov.farms.service.api.v1.ImportService;

public class ImportEndpointsImpl extends BaseEndpointsImpl implements ImportEndpoints {

    private static final Logger logger = LoggerFactory.getLogger(ImportEndpointsImpl.class);

    @Autowired
    private ImportService importService;

    @Autowired
    private ImportBPUService importBPUService;

    @Override
    public Response importBPU(InputStream fileInputStream, FormDataContentDisposition fileDetail) throws Exception {
        logger.debug("<importBPU");

        Response response = null;

        logRequest();

        try {
            byte[] fileContent = readBytes(fileInputStream);
            String fileName = fileDetail.getFileName();

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

    private byte[] readBytes(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            int nRead;
            byte[] data = new byte[4096];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            return buffer.toByteArray();
        }
    }

}
