package ca.bc.gov.farms.api.rest.v1.endpoints.impl;

import java.io.InputStream;

import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.farms.api.rest.v1.endpoints.ImportEndpoints;

public class ImportEndpointsImpl extends BaseEndpointsImpl implements ImportEndpoints {

    private static final Logger logger = LoggerFactory.getLogger(ImportEndpointsImpl.class);

    @Override
    public Response importBPU(InputStream fileInputStream, FormDataContentDisposition fileDetail) throws Exception {
        logger.debug("<importBPU");

        Response response = null;

        logRequest();

        try {
            
        } catch (Exception e) {
            response = getInternalServerErrorResponse(e);
        }

        logResponse(response);

        logger.debug(">importBPU " + response);
        return response;
    }
    
}
