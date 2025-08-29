package ca.bc.gov.farms.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.farms.api.rest.v1.endpoints.InventoryItemAttributeEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.InventoryItemAttributeRsrc;
import ca.bc.gov.farms.service.api.v1.InventoryItemAttributeService;

public class InventoryItemAttributeEndpointsImpl extends BaseEndpointsImpl implements InventoryItemAttributeEndpoints {

    private static final Logger logger = LoggerFactory.getLogger(InventoryItemAttributeEndpointsImpl.class);

    @Autowired
    private InventoryItemAttributeService service;

    @Override
    public Response getInventoryItemAttributesByInventoryItemCode(String rollupInventoryItemCode) {
        logger.debug("<getInventoryItemAttributesByInventoryItemCode");

        Response response = null;

        logRequest();

        try {
            InventoryItemAttributeRsrc result = (InventoryItemAttributeRsrc) service
                    .getInventoryItemAttributesByInventoryItemCode(rollupInventoryItemCode, getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getInventoryItemAttributesByInventoryItemCode " + response);
        return response;
    }

    @Override
    public Response getInventoryItemAttribute(Long inventoryItemAttributeId) {
        logger.debug("<getInventoryItemAttribute");
        Response response = null;

        logRequest();

        try {
            InventoryItemAttributeRsrc result = (InventoryItemAttributeRsrc) service.getInventoryItemAttribute(
                    inventoryItemAttributeId,
                    getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getInventoryItemAttribute " + response);
        return response;
    }

    @Override
    public Response createInventoryItemAttribute(InventoryItemAttributeRsrc inventoryItemAttributeRsrc) {
        logger.debug("<createInventoryItemAttribute");

        Response response = null;

        logRequest();

        try {
            InventoryItemAttributeRsrc result = (InventoryItemAttributeRsrc) service
                    .createInventoryItemAttribute(inventoryItemAttributeRsrc, getFactoryContext());
            response = Response.status(Status.CREATED).entity(result).tag(result.getUnquotedETag()).build();
        } catch (ValidationFailureException e) {
            response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">createInventoryItemAttribute " + response.getStatus());
        return response;
    }

    @Override
    public Response deleteInventoryItemAttribute(Long inventoryItemAttributeId) {
        logger.debug("<deleteInventoryItemAttribute");

        Response response = null;

        logRequest();

        try {
            service.deleteInventoryItemAttribute(inventoryItemAttributeId);
            response = Response.status(Status.NO_CONTENT).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">deleteInventoryItemAttribute " + response.getStatus());
        return response;
    }

    @Override
    public Response updateInventoryItemAttribute(Long inventoryItemAttributeId,
            InventoryItemAttributeRsrc inventoryItemAttributeRsrc) {
        logger.debug("<updateInventoryItemAttribute");

        Response response = null;

        logRequest();

        try {
            InventoryItemAttributeRsrc result = (InventoryItemAttributeRsrc) service
                    .updateInventoryItemAttribute(inventoryItemAttributeId, inventoryItemAttributeRsrc,
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

        logger.debug(">updateInventoryItemAttribute " + response.getStatus());
        return response;
    }

}
