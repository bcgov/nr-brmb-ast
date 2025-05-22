package ca.bc.gov.farms.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.farms.api.rest.v1.endpoints.FairMarketValueEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.FairMarketValueListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.FairMarketValueRsrc;
import ca.bc.gov.farms.service.api.v1.FairMarketValueService;

public class FairMarketValueEndpointsImpl extends BaseEndpointsImpl implements FairMarketValueEndpoints {

    private static final Logger logger = LoggerFactory.getLogger(FairMarketValueEndpointsImpl.class);

    @Autowired
    private FairMarketValueService service;

    @Override
    public Response getFairMarketValuesByProgramYear(Integer programYear) {
        logger.debug("<getFairMarketValuesByProgramYear");

        Response response = null;

        logRequest();

        try {
            FairMarketValueListRsrc result = (FairMarketValueListRsrc) service
                    .getFairMarketValuesByProgramYear(programYear, getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getFairMarketValuesByProgramYear " + response);
        return response;
    }

    @Override
    public Response getFairMarketValue(String fairMarketValueId) {
        logger.debug("<getFairMarketValue");
        Response response = null;

        logRequest();

        try {
            FairMarketValueRsrc result = (FairMarketValueRsrc) service.getFairMarketValue(fairMarketValueId,
                    getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (NotFoundException e) {
            response = Response.status(Response.Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getFairMarketValue " + response);
        return response;
    }

    @Override
    public Response createFairMarketValue(FairMarketValueRsrc fairMarketValueRsrc) {
        logger.debug("<createFairMarketValue");

        Response response = null;

        logRequest();

        try {
            FairMarketValueRsrc result = (FairMarketValueRsrc) service.createFairMarketValue(fairMarketValueRsrc,
                    getFactoryContext());
            response = Response.status(Response.Status.CREATED).entity(result).tag(result.getUnquotedETag()).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">createFairMarketValue " + response.getStatus());
        return response;
    }

}
