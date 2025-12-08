package ca.bc.gov.farms.api.rest.v1.endpoints.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.farms.api.rest.v1.endpoints.ProductiveUnitCodeEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.ProductiveUnitCodeListRsrc;
import ca.bc.gov.farms.service.api.v1.ProductiveUnitCodeService;
import jakarta.ws.rs.core.Response;

public class ProductiveUnitCodeEndpointsImpl extends BaseEndpointsImpl implements ProductiveUnitCodeEndpoints {

    private static final Logger logger = LoggerFactory.getLogger(ProductiveUnitCodeEndpointsImpl.class);

    @Autowired
    private ProductiveUnitCodeService service;

    @Override
    public Response getAllProductiveUnitCodes() {
        logger.debug("<getAllProductiveUnitCodes");

        Response response = null;

        logRequest();

        ProductiveUnitCodeListRsrc result = null;

        try {
            result = (ProductiveUnitCodeListRsrc) service.getAllProductiveUnitCodes(getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getAllProductiveUnitCodes " + response);
        return response;
    }

}
