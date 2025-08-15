package ca.bc.gov.farms.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.farms.api.rest.v1.endpoints.InventoryItemDetailEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.InventoryItemDetailListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.InventoryItemDetailRsrc;
import ca.bc.gov.farms.service.api.v1.InventoryItemDetailService;

public class InventoryItemDetailEndpointsImpl extends BaseEndpointsImpl implements InventoryItemDetailEndpoints {

    private static final Logger logger = LoggerFactory.getLogger(InventoryItemDetailEndpointsImpl.class);

    @Autowired
    private InventoryItemDetailService service;

    @Override
    public Response getInventoryItemDetailsByInventoryItemCode(String inventoryItemCode) {
        logger.debug("<getInventoryItemDetailsByInventoryItemCode");

        Response response = null;

        logRequest();

        try {
            InventoryItemDetailListRsrc result = (InventoryItemDetailListRsrc) service
                    .getInventoryItemDetailsByInventoryItemCode(inventoryItemCode, getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getInventoryItemDetailsByInventoryItemCode " + response);
        return response;
    }

    @Override
    public Response getInventoryItemDetail(Long inventoryItemDetailId) {
        logger.debug("<getInventoryItemDetail");
        Response response = null;

        logRequest();

        try {
            InventoryItemDetailRsrc result = (InventoryItemDetailRsrc) service
                    .getInventoryItemDetail(inventoryItemDetailId, getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getInventoryItemDetail " + response);
        return response;
    }

    @Override
    public Response createInventoryItemDetail(InventoryItemDetailRsrc inventoryItemDetailRsrc) {
        logger.debug("<createInventoryItemDetail");

        Response response = null;

        logRequest();

        try {
            InventoryItemDetailRsrc result = (InventoryItemDetailRsrc) service
                    .createInventoryItemDetail(inventoryItemDetailRsrc, getFactoryContext());
            response = Response.status(Status.CREATED).entity(result).tag(result.getUnquotedETag()).build();
        } catch (ValidationFailureException e) {
            response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">createInventoryItemDetail " + response.getStatus());
        return response;
    }

    @Override
    public Response deleteInventoryItemDetail(Long inventoryItemDetailId) {
        logger.debug("<deleteInventoryItemDetail");

        Response response = null;

        logRequest();

        try {
            service.deleteInventoryItemDetail(inventoryItemDetailId);
            response = Response.status(Status.NO_CONTENT).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">deleteInventoryItemDetail " + response.getStatus());
        return response;
    }

    @Override
    public Response updateInventoryItemDetail(Long inventoryItemDetailId,
            InventoryItemDetailRsrc inventoryItemDetailRsrc) {
        logger.debug("<updateInventoryItemDetail");

        Response response = null;

        logRequest();

        try {
            InventoryItemDetailRsrc result = (InventoryItemDetailRsrc) service
                    .updateInventoryItemDetail(inventoryItemDetailId, inventoryItemDetailRsrc, getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (ValidationFailureException e) {
            response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">updateInventoryItemDetail " + response.getStatus());
        return response;
    }

}
