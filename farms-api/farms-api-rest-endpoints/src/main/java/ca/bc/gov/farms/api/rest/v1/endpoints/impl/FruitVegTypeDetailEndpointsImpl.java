package ca.bc.gov.farms.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.farms.api.rest.v1.endpoints.FruitVegTypeDetailEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.FruitVegTypeDetailListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.FruitVegTypeDetailRsrc;
import ca.bc.gov.farms.service.api.v1.FruitVegTypeDetailService;

public class FruitVegTypeDetailEndpointsImpl extends BaseEndpointsImpl
        implements FruitVegTypeDetailEndpoints {

    private static final Logger logger = LoggerFactory.getLogger(FruitVegTypeDetailEndpointsImpl.class);

    @Autowired
    private FruitVegTypeDetailService service;

    @Override
    public Response getAllFruitVegTypeDetails() {
        logger.debug("<getAllFruitVegTypeDetails");

        Response response = null;

        logRequest();

        try {
            FruitVegTypeDetailListRsrc result = (FruitVegTypeDetailListRsrc) service
                    .getAllFruitVegTypeDetails(getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getAllFruitVegTypeDetails " + response);
        return response;
    }

    @Override
    public Response getFruitVegTypeDetail(String fruitVegTypeCode) {
        logger.debug("<getFruitVegTypeDetail");
        Response response = null;

        logRequest();

        try {
            FruitVegTypeDetailRsrc result = (FruitVegTypeDetailRsrc) service.getFruitVegTypeDetail(
                    fruitVegTypeCode,
                    getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getFruitVegTypeDetail " + response);
        return response;
    }

    @Override
    public Response createFruitVegTypeDetail(FruitVegTypeDetailRsrc fruitVegTypeDetailRsrc) {
        logger.debug("<createFruitVegTypeDetail");

        Response response = null;

        logRequest();

        try {
            FruitVegTypeDetailRsrc result = (FruitVegTypeDetailRsrc) service
                    .createFruitVegTypeDetail(fruitVegTypeDetailRsrc, getFactoryContext());
            response = Response.status(Status.CREATED).entity(result).tag(result.getUnquotedETag()).build();
        } catch (ValidationFailureException e) {
            response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">createFruitVegTypeDetail " + response.getStatus());
        return response;
    }

    @Override
    public Response deleteFruitVegTypeDetail(String fruitVegTypeCode) {
        logger.debug("<deleteFruitVegTypeDetail");

        Response response = null;

        logRequest();

        try {
            service.deleteFruitVegTypeDetail(fruitVegTypeCode);
            response = Response.status(Status.NO_CONTENT).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">deleteFruitVegTypeDetail " + response.getStatus());
        return response;
    }

    @Override
    public Response updateFruitVegTypeDetail(String fruitVegTypeCode,
            FruitVegTypeDetailRsrc fruitVegTypeDetailRsrc) {
        logger.debug("<updateFruitVegTypeDetail");

        Response response = null;

        logRequest();

        try {
            FruitVegTypeDetailRsrc result = (FruitVegTypeDetailRsrc) service
                    .updateFruitVegTypeDetail(fruitVegTypeCode, fruitVegTypeDetailRsrc,
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

        logger.debug(">updateFruitVegTypeDetail " + response.getStatus());
        return response;
    }

}
