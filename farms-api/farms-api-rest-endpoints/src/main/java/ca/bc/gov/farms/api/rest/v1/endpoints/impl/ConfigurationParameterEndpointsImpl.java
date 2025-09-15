package ca.bc.gov.farms.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.farms.api.rest.v1.endpoints.ConfigurationParameterEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.ConfigurationParameterRsrc;
import ca.bc.gov.farms.service.api.v1.ConfigurationParameterService;

public class ConfigurationParameterEndpointsImpl extends BaseEndpointsImpl
        implements ConfigurationParameterEndpoints {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationParameterEndpointsImpl.class);

    @Autowired
    private ConfigurationParameterService service;

    @Override
    public Response getAllConfigurationParameters() {
        logger.debug("<getAllConfigurationParameters");

        Response response = null;

        logRequest();

        try {
            ConfigurationParameterRsrc result = (ConfigurationParameterRsrc) service
                    .getAllConfigurationParameters(getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getAllConfigurationParameters " + response);
        return response;
    }

    @Override
    public Response getConfigurationParameter(Long configurationParameterId) {
        logger.debug("<getConfigurationParameter");
        Response response = null;

        logRequest();

        try {
            ConfigurationParameterRsrc result = (ConfigurationParameterRsrc) service.getConfigurationParameter(
                    configurationParameterId,
                    getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getConfigurationParameter " + response);
        return response;
    }

    @Override
    public Response createConfigurationParameter(ConfigurationParameterRsrc configurationParameterRsrc) {
        logger.debug("<createConfigurationParameter");

        Response response = null;

        logRequest();

        try {
            ConfigurationParameterRsrc result = (ConfigurationParameterRsrc) service
                    .createConfigurationParameter(configurationParameterRsrc, getFactoryContext());
            response = Response.status(Status.CREATED).entity(result).tag(result.getUnquotedETag()).build();
        } catch (ValidationFailureException e) {
            response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">createConfigurationParameter " + response.getStatus());
        return response;
    }

    @Override
    public Response deleteConfigurationParameter(Long configurationParameterId) {
        logger.debug("<deleteConfigurationParameter");

        Response response = null;

        logRequest();

        try {
            service.deleteConfigurationParameter(configurationParameterId);
            response = Response.status(Status.NO_CONTENT).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">deleteConfigurationParameter " + response.getStatus());
        return response;
    }

    @Override
    public Response updateConfigurationParameter(Long configurationParameterId,
            ConfigurationParameterRsrc configurationParameterRsrc) {
        logger.debug("<updateConfigurationParameter");

        Response response = null;

        logRequest();

        try {
            ConfigurationParameterRsrc result = (ConfigurationParameterRsrc) service
                    .updateConfigurationParameter(configurationParameterId, configurationParameterRsrc,
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

        logger.debug(">updateConfigurationParameter " + response.getStatus());
        return response;
    }

}
