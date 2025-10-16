package ca.bc.gov.farms.api.rest.v1.endpoints.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.farms.api.rest.v1.endpoints.StructureGroupAttributeEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.StructureGroupAttributeRsrc;
import ca.bc.gov.farms.service.api.v1.StructureGroupAttributeService;

public class StructureGroupAttributeEndpointsImpl extends BaseEndpointsImpl
        implements StructureGroupAttributeEndpoints {

    private static final Logger logger = LoggerFactory.getLogger(StructureGroupAttributeEndpointsImpl.class);

    @Autowired
    private StructureGroupAttributeService service;

    @Override
    public Response getStructureGroupAttributesByStructureGroupCode(String structureGroupCode) {
        logger.debug("<getStructureGroupAttributesByStructureGroupCode");

        Response response = null;

        logRequest();

        try {
            StructureGroupAttributeRsrc result = (StructureGroupAttributeRsrc) service
                    .getStructureGroupAttributesByStructureGroupCode(structureGroupCode, getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getStructureGroupAttributesByStructureGroupCode " + response);
        return response;
    }

    @Override
    public Response getStructureGroupAttribute(Long structureGroupAttributeId) {
        logger.debug("<getStructureGroupAttribute");
        Response response = null;

        logRequest();

        try {
            StructureGroupAttributeRsrc result = (StructureGroupAttributeRsrc) service.getStructureGroupAttribute(
                    structureGroupAttributeId,
                    getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getStructureGroupAttribute " + response);
        return response;
    }

    @Override
    public Response createStructureGroupAttribute(StructureGroupAttributeRsrc structureGroupAttributeRsrc) {
        logger.debug("<createStructureGroupAttribute");

        Response response = null;

        logRequest();

        try {
            StructureGroupAttributeRsrc result = (StructureGroupAttributeRsrc) service
                    .createStructureGroupAttribute(structureGroupAttributeRsrc, getFactoryContext());
            response = Response.status(Status.CREATED).entity(result).tag(result.getUnquotedETag()).build();
        } catch (ValidationFailureException e) {
            response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">createStructureGroupAttribute " + response.getStatus());
        return response;
    }

    @Override
    public Response deleteStructureGroupAttribute(Long structureGroupAttributeId) {
        logger.debug("<deleteStructureGroupAttribute");

        Response response = null;

        logRequest();

        try {
            service.deleteStructureGroupAttribute(structureGroupAttributeId);
            response = Response.status(Status.NO_CONTENT).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">deleteStructureGroupAttribute " + response.getStatus());
        return response;
    }

    @Override
    public Response updateStructureGroupAttribute(Long structureGroupAttributeId,
            StructureGroupAttributeRsrc structureGroupAttributeRsrc) {
        logger.debug("<updateStructureGroupAttribute");

        Response response = null;

        logRequest();

        try {
            StructureGroupAttributeRsrc result = (StructureGroupAttributeRsrc) service
                    .updateStructureGroupAttribute(structureGroupAttributeId, structureGroupAttributeRsrc,
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

        logger.debug(">updateStructureGroupAttribute " + response.getStatus());
        return response;
    }

}
