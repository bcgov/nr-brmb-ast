package ca.bc.gov.farms.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.farms.api.rest.v1.endpoints.ExpectedProductionEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.ExpectedProductionRsrc;
import ca.bc.gov.farms.service.api.v1.ExpectedProductionService;

public class ExpectedProductionEndpointsImpl extends BaseEndpointsImpl implements ExpectedProductionEndpoints {

    private static final Logger logger = LoggerFactory.getLogger(ExpectedProductionEndpointsImpl.class);

    @Autowired
    private ExpectedProductionService service;

    @Override
    public Response getExpectedProductionByInventoryItemCode(String inventoryItemCode) {
        logger.debug("<getExpectedProductionByInventoryItemCode");

        Response response = null;

        logRequest();

        try {
            ExpectedProductionRsrc result = (ExpectedProductionRsrc) service
                    .getExpectedProductionByInventoryItemCode(inventoryItemCode, getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getExpectedProductionByInventoryItemCode " + response);
        return response;
    }

    @Override
    public Response getExpectedProduction(Long expectedProductionId) {
        logger.debug("<getExpectedProduction");
        Response response = null;

        logRequest();

        try {
            ExpectedProductionRsrc result = (ExpectedProductionRsrc) service.getExpectedProduction(
                    expectedProductionId,
                    getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getExpectedProduction " + response);
        return response;
    }

    @Override
    public Response createExpectedProduction(ExpectedProductionRsrc expectedProductionRsrc) {
        logger.debug("<createExpectedProduction");

        Response response = null;

        logRequest();

        try {
            ExpectedProductionRsrc result = (ExpectedProductionRsrc) service
                    .createExpectedProduction(expectedProductionRsrc, getFactoryContext());
            response = Response.status(Status.CREATED).entity(result).tag(result.getUnquotedETag()).build();
        } catch (ValidationFailureException e) {
            response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">createExpectedProduction " + response.getStatus());
        return response;
    }

    @Override
    public Response deleteExpectedProduction(Long expectedProductionId) {
        logger.debug("<deleteExpectedProduction");

        Response response = null;

        logRequest();

        try {
            service.deleteExpectedProduction(expectedProductionId);
            response = Response.status(Status.NO_CONTENT).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">deleteExpectedProduction " + response.getStatus());
        return response;
    }

    @Override
    public Response updateExpectedProduction(Long expectedProductionId,
            ExpectedProductionRsrc expectedProductionRsrc) {
        logger.debug("<updateExpectedProduction");

        Response response = null;

        logRequest();

        try {
            ExpectedProductionRsrc result = (ExpectedProductionRsrc) service
                    .updateExpectedProduction(expectedProductionId, expectedProductionRsrc,
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

        logger.debug(">updateExpectedProduction " + response.getStatus());
        return response;
    }

}
