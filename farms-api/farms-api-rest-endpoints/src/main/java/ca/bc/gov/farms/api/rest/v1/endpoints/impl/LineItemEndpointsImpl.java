package ca.bc.gov.farms.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.farms.api.rest.v1.endpoints.LineItemEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.LineItemListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.LineItemRsrc;
import ca.bc.gov.farms.service.api.v1.LineItemService;

public class LineItemEndpointsImpl extends BaseEndpointsImpl implements LineItemEndpoints {

    private static final Logger logger = LoggerFactory.getLogger(LineItemEndpointsImpl.class);

    @Autowired
    private LineItemService service;

    @Override
    public Response getLineItemsByProgramYear(Integer programYear) {
        logger.debug("<getLineItemsByProgramYear");

        Response response = null;

        logRequest();

        try {
            LineItemListRsrc result = (LineItemListRsrc) service.getLineItemsByProgramYear(programYear,
                    getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getLineItemsByProgramYear " + response);
        return response;
    }

    @Override
    public Response getLineItem(Long lineItemId) {
        logger.debug("<getLineItem");
        Response response = null;

        logRequest();

        try {
            LineItemRsrc result = (LineItemRsrc) service.getLineItem(
                    lineItemId,
                    getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getLineItem " + response);
        return response;
    }

    @Override
    public Response createLineItem(LineItemRsrc lineItemRsrc) {
        logger.debug("<createLineItem");

        Response response = null;

        logRequest();

        try {
            LineItemRsrc result = (LineItemRsrc) service.createLineItem(lineItemRsrc, getFactoryContext());
            response = Response.status(Status.CREATED).entity(result).tag(result.getUnquotedETag()).build();
        } catch (ValidationFailureException e) {
            response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">createLineItem " + response.getStatus());
        return response;
    }

    @Override
    public Response deleteLineItem(Long lineItemId) {
        logger.debug("<deleteLineItem");

        Response response = null;

        logRequest();

        try {
            service.deleteLineItem(lineItemId);
            response = Response.status(Status.NO_CONTENT).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">deleteLineItem " + response.getStatus());
        return response;
    }

    @Override
    public Response updateLineItem(Long lineItemId, LineItemRsrc lineItemRsrc) {
        logger.debug("<updateLineItem");

        Response response = null;

        logRequest();

        try {
            LineItemRsrc result = (LineItemRsrc) service.updateLineItem(lineItemId, lineItemRsrc, getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (ValidationFailureException e) {
            response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">updateLineItem " + response.getStatus());
        return response;
    }

    @Override
    public Response copyLineItems(Integer currentYear) {
        logger.debug("<copyLineItems");

        Response response = null;

        logRequest();

        try {
            LineItemListRsrc result = (LineItemListRsrc) service.copyLineItems(currentYear, getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">copyLineItems " + response);
        return response;
    }
}
