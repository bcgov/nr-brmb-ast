package ca.bc.gov.farms.api.rest.v1.endpoints.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.farms.api.rest.v1.endpoints.YearConfigurationParameterEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.YearConfigurationParameterListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.YearConfigurationParameterRsrc;
import ca.bc.gov.farms.service.api.v1.YearConfigurationParameterService;

public class YearConfigurationParameterEndpointsImpl extends BaseEndpointsImpl
        implements YearConfigurationParameterEndpoints {

    private static final Logger logger = LoggerFactory.getLogger(YearConfigurationParameterEndpointsImpl.class);

    @Autowired
    private YearConfigurationParameterService service;

    @Override
    public Response getAllYearConfigurationParameters() {
        logger.debug("<getAllYearConfigurationParameters");

        Response response = null;

        logRequest();

        try {
            YearConfigurationParameterListRsrc result = (YearConfigurationParameterListRsrc) service
                    .getAllYearConfigurationParameters(getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getAllYearConfigurationParameters " + response);
        return response;
    }

    @Override
    public Response getYearConfigurationParameter(Long yearConfigurationParameterId) {
        logger.debug("<getYearConfigurationParameter");
        Response response = null;

        logRequest();

        try {
            YearConfigurationParameterRsrc result = (YearConfigurationParameterRsrc) service
                    .getYearConfigurationParameter(
                            yearConfigurationParameterId,
                            getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getYearConfigurationParameter " + response);
        return response;
    }

    @Override
    public Response createYearConfigurationParameter(YearConfigurationParameterRsrc yearConfigurationParameterRsrc) {
        logger.debug("<createYearConfigurationParameter");

        Response response = null;

        logRequest();

        try {
            YearConfigurationParameterRsrc result = (YearConfigurationParameterRsrc) service
                    .createYearConfigurationParameter(yearConfigurationParameterRsrc, getFactoryContext());
            response = Response.status(Status.CREATED).entity(result).tag(result.getUnquotedETag()).build();
        } catch (ValidationFailureException e) {
            response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">createYearConfigurationParameter " + response.getStatus());
        return response;
    }

    @Override
    public Response deleteYearConfigurationParameter(Long yearConfigurationParameterId) {
        logger.debug("<deleteYearConfigurationParameter");

        Response response = null;

        logRequest();

        try {
            service.deleteYearConfigurationParameter(yearConfigurationParameterId);
            response = Response.status(Status.NO_CONTENT).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">deleteYearConfigurationParameter " + response.getStatus());
        return response;
    }

    @Override
    public Response updateYearConfigurationParameter(Long yearConfigurationParameterId,
            YearConfigurationParameterRsrc yearConfigurationParameterRsrc) {
        logger.debug("<updateYearConfigurationParameter");

        Response response = null;

        logRequest();

        try {
            YearConfigurationParameterRsrc result = (YearConfigurationParameterRsrc) service
                    .updateYearConfigurationParameter(yearConfigurationParameterId, yearConfigurationParameterRsrc,
                            getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (ValidationFailureException e) {
            response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">updateYearConfigurationParameter " + response.getStatus());
        return response;
    }

}
