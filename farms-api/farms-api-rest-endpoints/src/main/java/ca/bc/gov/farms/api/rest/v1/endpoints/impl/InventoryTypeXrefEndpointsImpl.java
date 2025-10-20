package ca.bc.gov.farms.api.rest.v1.endpoints.impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.farms.api.rest.v1.endpoints.InventoryTypeXrefEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.InventoryTypeXrefListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.InventoryTypeXrefRsrc;
import ca.bc.gov.farms.service.api.v1.InventoryTypeXrefService;

public class InventoryTypeXrefEndpointsImpl extends BaseEndpointsImpl implements InventoryTypeXrefEndpoints {

    private static final Logger logger = LoggerFactory.getLogger(InventoryTypeXrefEndpointsImpl.class);

    @Autowired
    private InventoryTypeXrefService service;

    @Override
    public Response getInventoryTypeXrefsByInventoryClassCode(String inventoryClassCode) {
        logger.debug("<getInventoryTypeXrefsByInventoryClassCode");

        Response response = null;

        logRequest();

        try {
            InventoryTypeXrefListRsrc result = (InventoryTypeXrefListRsrc) service
                    .getInventoryTypeXrefsByInventoryClassCode(inventoryClassCode, getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getInventoryTypeXrefsByInventoryClassCode " + response);
        return response;
    }

    @Override
    public Response getInventoryTypeXref(Long agristabilityCommodityXrefId) {
        logger.debug("<getInventoryTypeXref");
        Response response = null;

        logRequest();

        try {
            InventoryTypeXrefRsrc result = (InventoryTypeXrefRsrc) service.getInventoryTypeXref(
                    agristabilityCommodityXrefId,
                    getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getInventoryTypeXref " + response);
        return response;
    }

    @Override
    public Response createInventoryTypeXref(InventoryTypeXrefRsrc inventoryTypeXrefRsrc) {
        logger.debug("<createInventoryTypeXref");

        Response response = null;

        logRequest();

        try {
            InventoryTypeXrefRsrc result = (InventoryTypeXrefRsrc) service
                    .createInventoryTypeXref(inventoryTypeXrefRsrc, getFactoryContext());
            response = Response.status(Status.CREATED).entity(result).tag(result.getUnquotedETag()).build();
        } catch (ValidationFailureException e) {
            response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">createInventoryTypeXref " + response.getStatus());
        return response;
    }

    @Override
    public Response deleteInventoryTypeXref(Long agristabilityCommodityXrefId) {
        logger.debug("<deleteInventoryTypeXref");

        Response response = null;

        logRequest();

        try {
            service.deleteInventoryTypeXref(agristabilityCommodityXrefId);
            response = Response.status(Status.NO_CONTENT).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">deleteInventoryTypeXref " + response.getStatus());
        return response;
    }

    @Override
    public Response updateInventoryTypeXref(Long agristabilityCommodityXrefId,
            InventoryTypeXrefRsrc inventoryTypeXrefRsrc) {
        logger.debug("<updateInventoryTypeXref");

        Response response = null;

        logRequest();

        try {
            InventoryTypeXrefRsrc result = (InventoryTypeXrefRsrc) service
                    .updateInventoryTypeXref(agristabilityCommodityXrefId, inventoryTypeXrefRsrc, getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (ValidationFailureException e) {
            response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">updateInventoryTypeXref " + response.getStatus());
        return response;
    }

}
