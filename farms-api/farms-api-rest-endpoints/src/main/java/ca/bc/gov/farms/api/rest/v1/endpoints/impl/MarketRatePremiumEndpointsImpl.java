package ca.bc.gov.farms.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.farms.api.rest.v1.endpoints.MarketRatePremiumEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.MarketRatePremiumListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.MarketRatePremiumRsrc;
import ca.bc.gov.farms.service.api.v1.MarketRatePremiumService;

public class MarketRatePremiumEndpointsImpl extends BaseEndpointsImpl
        implements MarketRatePremiumEndpoints {

    private static final Logger logger = LoggerFactory.getLogger(MarketRatePremiumEndpointsImpl.class);

    @Autowired
    private MarketRatePremiumService service;

    @Override
    public Response getAllMarketRatePremiums() {
        logger.debug("<getAllMarketRatePremiums");

        Response response = null;

        logRequest();

        try {
            MarketRatePremiumListRsrc result = (MarketRatePremiumListRsrc) service
                    .getAllMarketRatePremiums(getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getAllMarketRatePremiums " + response);
        return response;
    }

    @Override
    public Response getMarketRatePremium(Long marketRatePremiumId) {
        logger.debug("<getMarketRatePremium");
        Response response = null;

        logRequest();

        try {
            MarketRatePremiumRsrc result = (MarketRatePremiumRsrc) service.getMarketRatePremium(
                    marketRatePremiumId,
                    getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getMarketRatePremium " + response);
        return response;
    }

    @Override
    public Response createMarketRatePremium(MarketRatePremiumRsrc marketRatePremiumRsrc) {
        logger.debug("<createMarketRatePremium");

        Response response = null;

        logRequest();

        try {
            MarketRatePremiumRsrc result = (MarketRatePremiumRsrc) service
                    .createMarketRatePremium(marketRatePremiumRsrc, getFactoryContext());
            response = Response.status(Status.CREATED).entity(result).tag(result.getUnquotedETag()).build();
        } catch (ValidationFailureException e) {
            response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">createMarketRatePremium " + response.getStatus());
        return response;
    }

    @Override
    public Response deleteMarketRatePremium(Long marketRatePremiumId) {
        logger.debug("<deleteMarketRatePremium");

        Response response = null;

        logRequest();

        try {
            service.deleteMarketRatePremium(marketRatePremiumId);
            response = Response.status(Status.NO_CONTENT).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">deleteMarketRatePremium " + response.getStatus());
        return response;
    }

    @Override
    public Response updateMarketRatePremium(Long marketRatePremiumId,
            MarketRatePremiumRsrc marketRatePremiumRsrc) {
        logger.debug("<updateMarketRatePremium");

        Response response = null;

        logRequest();

        try {
            MarketRatePremiumRsrc result = (MarketRatePremiumRsrc) service
                    .updateMarketRatePremium(marketRatePremiumId, marketRatePremiumRsrc,
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

        logger.debug(">updateMarketRatePremium " + response.getStatus());
        return response;
    }

}
